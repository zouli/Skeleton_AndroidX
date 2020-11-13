package com.riverside.skeleton.android.widget.commonselect.business;

import java.util.List;

/**
 * 返回数据接口   1.0
 * b_e  2018/02/16
 */
public interface BizCallback {
    /**
     * 返回数据接口
     *
     * @param datas
     */
    void callback(List<BaseBizItem> datas);
}