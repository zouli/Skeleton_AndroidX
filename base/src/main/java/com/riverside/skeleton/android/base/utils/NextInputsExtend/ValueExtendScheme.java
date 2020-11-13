package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import com.github.yoojia.inputs.Scheme;

/**
 * 值扩展 1.0
 * b_e  2018/03/29
 */
public class ValueExtendScheme {
    // 8位，英文+数字
    public static String PASSWORD_8DL_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$";

    public ValueExtendScheme() {
    }

    /**
     * 验证正则表达式
     *
     * @param regex
     * @return
     */
    public static Scheme Regex(String regex) {
        return (new Scheme(new RegexVerifier(regex))).msg("此为非法字符串");
    }
}
