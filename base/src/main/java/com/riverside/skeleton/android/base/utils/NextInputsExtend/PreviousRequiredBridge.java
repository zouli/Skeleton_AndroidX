package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import com.github.yoojia.inputs.PairedVerifier;
import com.github.yoojia.inputs.TextLazyLoader;
import com.github.yoojia.inputs.Texts;

/**
 * 验证扩展 1.0
 * 本项不为空时前一项不能为空
 * <p>
 * b_e  2017/11/23
 */
public class PreviousRequiredBridge extends PairedVerifier<String> {
    private final TextLazyLoader mStringLazyLoader;

    public PreviousRequiredBridge(TextLazyLoader lazyLoader) {
        super(lazyLoader.getValue(), lazyLoader.getValue());
        this.mStringLazyLoader = lazyLoader;
    }

    @Override
    public String benchmark1stValueForMessage() {
        return mStringLazyLoader.getValue();
    }

    @Override
    public String benchmark2ndValueForMessage() {
        return mStringLazyLoader.getValue();
    }

    @Override
    protected String typedCast(String input) {
        return input;
    }

    @Override
    protected boolean performTyped(String typedInput) {
        return !Texts.isEmpty(typedInput) && !Texts.isEmpty(mStringLazyLoader.getValue());
    }
}
