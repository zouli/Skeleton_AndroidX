package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import com.github.yoojia.inputs.Scheme;
import com.github.yoojia.inputs.TextLazyLoader;

/**
 * 逻辑扩展 1.0
 * b_e  2017/11/23
 */
public class LogicScheme {
    /**
     * 本项不为空时前一项不能为空
     *
     * @param lazyLoader
     * @return
     */
    public static Scheme RequiredAndPreviousRequired(final TextLazyLoader lazyLoader) {
        return new Scheme(new PreviousRequiredBridge(lazyLoader)).msg("前一项不能为空");
    }
}
