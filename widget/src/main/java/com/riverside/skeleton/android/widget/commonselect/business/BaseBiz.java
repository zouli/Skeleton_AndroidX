package com.riverside.skeleton.android.widget.commonselect.business;

/**
 * 实现类模板    1.0
 * b_e  2018/02/16
 */
public abstract class BaseBiz {
    /**
     * 取得设置设置参数
     *
     * @param args
     */
    public abstract void setCriteria(String... args);

    /**
     * 取得列表数据
     * 通过BizCallback取得数据
     *
     * @param callback
     */
    public abstract void getList(BizCallback callback);
}
