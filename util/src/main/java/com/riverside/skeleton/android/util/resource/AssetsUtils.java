package com.riverside.skeleton.android.util.resource;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Assets工具类    1.0
 * b_e  2018/08/10
 */
public class AssetsUtils {
    /**
     * 取得文件的输入流
     *
     * @param context
     * @param filename
     * @return
     */
    public static InputStream getInputStream(Context context, String filename) {
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(filename);
            return is;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 取得文件内容（String）
     *
     * @param context
     * @param filename
     * @return
     */
    public static String getString(Context context, String filename) {
        InputStream is = getInputStream(context, filename);
        if (is != null) {
            String result = "";
            String line;
            try {
                InputStreamReader ir = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(ir);
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                is.close();
                return result;
            } catch (IOException e) {
            }
        }
        return "";
    }

    /**
     * 取得文件内容（图片）
     *
     * @param context
     * @param filename
     * @return
     */
    public static Bitmap getBitmap(Context context, String filename) {
        InputStream is = getInputStream(context, filename);
        if (is != null) {
            Bitmap image;
            image = BitmapFactory.decodeStream(is);
            return image;
        }
        return null;
    }

    /**
     * 取得目录下文件名
     *
     * @param context
     * @param path
     * @return
     */
    public static String[] getFileList(Context context, String path) {
        AssetManager am = context.getResources().getAssets();
        try {
            return am.list(path);
        } catch (IOException e) {
            return null;
        }
    }
}

