package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import com.github.yoojia.inputs.Verifier;

import java.util.regex.Pattern;

/**
 * 正则表达式验证器 1.0
 * b_e  2018/03/29
 */
public class RegexVerifier implements Verifier {
    String regex;

    public RegexVerifier(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean perform(String rawInput) throws Exception {
        return Pattern.matches(regex, rawInput);
    }
}
