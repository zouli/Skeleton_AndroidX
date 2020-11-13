package com.riverside.skeleton.android.util.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.riverside.skeleton.android.util.R;

/**
 * 打开文件工具类  1.1
 * b_e  2018/08/10
 * 1.1  支持Android8.0    2019/04/08
 */
public class OpenFileUtils {
    /**
     * 生成Intent
     *
     * @param uri
     * @param type
     * @return
     */
    private static Intent createIntent(Uri uri, String type, int style) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (style) {
            case 1:
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("oneshot", 0);
                intent.putExtra("configchange", 0);
                break;
            case 2:
                intent.addCategory(Intent.CATEGORY_DEFAULT);
            case 3:
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, type);
        return intent;
    }

    /**
     * 打开Html文件
     *
     * @param url
     * @return
     */
    public static Intent getHtmlFileIntent(String url) {
        Uri uri = Uri.parse(url).buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(url).build();
        return createIntent(uri, "text/html", 0);
    }

    /**
     * 打开音频文件
     *
     * @param url
     * @return
     */
    public static Intent getAudioFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "audio/*", 1);
    }

    /**
     * 打开视频文件
     *
     * @param url
     * @return
     */
    public static Intent getVideoFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "video/*", 1);
    }

    /**
     * 打开文本文件
     *
     * @param url
     * @return
     */
    public static Intent getTextFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "text/plain", 2);
    }

    /**
     * 打开图片文件
     *
     * @param url
     * @return
     */
    public static Intent getImageFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "image/*", 2);
    }

    /**
     * 打开PDF文件
     *
     * @param url
     * @return
     */
    public static Intent getPdfFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "application/pdf", 2);
    }

    /**
     * 打开CHM文件
     *
     * @param url
     * @return
     */
    public static Intent getChmFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "application/x-chm", 2);
    }

    /**
     * 打开Word文件
     *
     * @param url
     * @return
     */
    public static Intent getWordFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "application/msword", 2);
    }

    /**
     * 打开Excel文件
     *
     * @param url
     * @return
     */
    public static Intent getExcelFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "application/vnd.ms-excel", 2);
    }

    /**
     * 打开PPT文件
     *
     * @param url
     * @return
     */
    public static Intent getPPTFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "application/vnd.ms-powerpoint", 2);
    }

    /**
     * 打开APK文件
     *
     * @param url
     * @return
     */
    public static Intent getApkFileIntent(Context context, String url) {
        Uri uri = FileHelper.getFileUri(context, url);
        return createIntent(uri, "application/vnd.android.package-archive", 3);
    }

    /**
     * 启动对应App
     *
     * @param context
     * @param url
     * @return
     */
    public static boolean startApp(Context context, String url) {
        boolean ret = true;
        if (FileUtils.isExists(url)) {
            if (isExtensionArray(context, url, R.array.file_extension_image)) {
                context.startActivity(getImageFileIntent(context, url));
            } else if (isExtensionArray(context, url, R.array.file_extension_web)) {
                context.startActivity(getHtmlFileIntent(url));
            } else if (isExtensionArray(context, url, R.array.file_extension_package)) {
                context.startActivity(getApkFileIntent(context, url));
            } else if (isExtensionArray(context, url, R.array.file_extension_audio)) {
                context.startActivity(getAudioFileIntent(context, url));
            } else if (isExtensionArray(context, url, R.array.file_extension_video)) {
                context.startActivity(getVideoFileIntent(context, url));
            } else if (isExtensionArray(context, url, R.array.file_extension_text)) {
                context.startActivity(getTextFileIntent(context, url));
            } else if (isExtensionArray(context, url, R.array.file_extension_pdf)) {
                context.startActivity(getPdfFileIntent(context, url));
            } else if (isExtensionArray(context, url, R.array.file_extension_word)) {
                context.startActivity(getWordFileIntent(context, url));
            } else if (isExtensionArray(context, url, R.array.file_extension_excel)) {
                context.startActivity(getExcelFileIntent(context, url));
            } else if (isExtensionArray(context, url, R.array.file_extension_ppt)) {
                context.startActivity(getPPTFileIntent(context, url));
            } else {
                ret = false;
            }
        } else {
            ret = false;
        }
        return ret;
    }

    /**
     * 判断是否包含在数组中
     *
     * @param context
     * @param url
     * @param array_id
     * @return
     */
    private static boolean isExtensionArray(Context context, String url, int array_id) {
        for (String ext : context.getResources().getStringArray(array_id)) {
            if (url.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}

