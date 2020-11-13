package com.riverside.skeleton.android.net.rest.utils;

import android.os.Handler;
import android.os.Looper;

import com.riverside.skeleton.android.util.resource.IdentificationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.ByteString;

public class ProgressRequestBody extends RequestBody {
    public static final MediaType MIXED = MediaType.parse("multipart/mixed");
    public static final MediaType IMAGE = MediaType.parse("image/*");

    private final ByteString boundary;
    private final MediaType originalType;
    private final MediaType contentType;
    private final List<Part> parts;
    private UploadCallbacks callbacks;

    private static final byte[] COLON_SPACE = {':', ' '};
    private static final byte[] CRLF = {'\r', '\n'};
    private static final byte[] DASH_DASH = {'-', '-'};
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public interface UploadCallbacks {
        void onProgressUpdate(String filename, int percentage);
    }

    ProgressRequestBody(ByteString boundary, MediaType type, List<ProgressRequestBody.Part> parts) {
        this.boundary = boundary;
        this.originalType = type;
        this.contentType = MediaType.parse(type + "; boundary=" + boundary.utf8());
        this.parts = Util.immutableList(parts);
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        for (int p = 0, partCount = parts.size(); p < partCount; p++) {
            ProgressRequestBody.Part part = parts.get(p);
            Headers headers = part.headers;
            callbacks = part.callbacks;
            String url = part.url;

            File file = new File(url);
            long fileLength = file.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(file);
            long uploaded = 0;

            sink.write(DASH_DASH);
            sink.write(boundary);
            sink.write(CRLF);

            if (headers != null) {
                for (int h = 0, headerCount = headers.size(); h < headerCount; h++) {
                    sink.writeUtf8(headers.name(h))
                            .write(COLON_SPACE)
                            .writeUtf8(headers.value(h))
                            .write(CRLF);
                }
            }

            MediaType contentType = contentType();
            if (contentType != null) {
                sink.writeUtf8("Content-Type: ")
                        .writeUtf8(contentType.toString())
                        .write(CRLF);
            }

            long contentLength = fileLength;
            if (contentLength != -1) {
                sink.writeUtf8("Content-Length: ")
                        .writeDecimalLong(contentLength)
                        .write(CRLF);
            }

            sink.write(CRLF);
            Handler handler = new Handler(Looper.getMainLooper());
            try {
                int read;
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(url, uploaded, fileLength));

                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
                handler.post(new ProgressUpdater(url, fileLength, fileLength));
            }
            sink.write(CRLF);
        }

        sink.write(DASH_DASH);
        sink.write(boundary);
        sink.write(DASH_DASH);
        sink.write(CRLF);
    }

    static StringBuilder appendQuotedString(StringBuilder target, String key) {
        target.append('"');
        for (int i = 0, len = key.length(); i < len; i++) {
            char ch = key.charAt(i);
            switch (ch) {
                case '\n':
                    target.append("%0A");
                    break;
                case '\r':
                    target.append("%0D");
                    break;
                case '"':
                    target.append("%22");
                    break;
                default:
                    target.append(ch);
                    break;
            }
        }
        target.append('"');
        return target;
    }

    public static final class Part {
        public static ProgressRequestBody.Part create(Headers headers, String url, UploadCallbacks callbacks) {
            if (url == null) {
                throw new NullPointerException("url == null");
            }
            if (headers != null && headers.get("Content-Type") != null) {
                throw new IllegalArgumentException("Unexpected header: Content-Type");
            }
            if (headers != null && headers.get("Content-Length") != null) {
                throw new IllegalArgumentException("Unexpected header: Content-Length");
            }
            return new ProgressRequestBody.Part(headers, url, callbacks);
        }

        public static ProgressRequestBody.Part createFormData(String name, String filename, String url, UploadCallbacks callbacks) {
            if (name == null) {
                throw new NullPointerException("name == null");
            }
            StringBuilder disposition = new StringBuilder("form-data; name=");
            appendQuotedString(disposition, name);

            if (filename != null) {
                disposition.append("; filename=");
                appendQuotedString(disposition, filename);
            }

            return create(Headers.of("Content-Disposition", disposition.toString()), url, callbacks);
        }

        private final Headers headers;
        private final String url;
        private final UploadCallbacks callbacks;

        private Part(Headers headers, String url, UploadCallbacks callbacks) {
            this.headers = headers;
            this.url = url;
            this.callbacks = callbacks;
        }
    }

    public static final class Builder {
        private final ByteString boundary;
        private MediaType type = MIXED;
        private final List<ProgressRequestBody.Part> parts = new ArrayList<>();

        public Builder() {
            boundary = ByteString.encodeUtf8(IdentificationUtils.getUUID());
        }

        /**
         * Add a form data part to the body.
         */
        public ProgressRequestBody.Builder addFormDataPart(String name, String filename, String url, UploadCallbacks callbacks) {
            return addPart(ProgressRequestBody.Part.createFormData(name, filename, url, callbacks));
        }

        /**
         * Add a part to the body.
         */
        public ProgressRequestBody.Builder addPart(ProgressRequestBody.Part part) {
            if (part == null) throw new NullPointerException("part == null");
            parts.add(part);
            return this;
        }

        /**
         * Assemble the specified parts into a request body.
         */
        public ProgressRequestBody build() {
            if (parts.isEmpty()) {
                throw new IllegalStateException("Multipart body must have at least one part.");
            }
            return new ProgressRequestBody(boundary, type, parts);
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        private String mFilename;

        public ProgressUpdater(String filename, long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
            mFilename = filename;
        }

        @Override
        public void run() {
            if (callbacks != null) {
                callbacks.onProgressUpdate(mFilename, (int) (100 * mUploaded / mTotal));
            }
        }
    }
}

