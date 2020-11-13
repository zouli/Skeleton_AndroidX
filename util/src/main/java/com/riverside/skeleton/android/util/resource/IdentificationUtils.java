package com.riverside.skeleton.android.util.resource;

import java.util.UUID;

/**
 * 标识工具类    1.0
 * b_e  2018/08/16
 */
public class IdentificationUtils {
    /**
     * 取得UUID
     *
     * @return UUID
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}