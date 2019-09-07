package com.zyf.fwms.commonlibrary.base.baseadapter;

import android.view.View;

/**
 * Created by jingbin on 2016/3/2.
 */
public interface OnItemClickListener<T> {
    void onClick(View view, T t, int position);
}
