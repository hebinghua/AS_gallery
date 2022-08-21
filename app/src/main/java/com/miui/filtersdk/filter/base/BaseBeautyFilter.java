package com.miui.filtersdk.filter.base;

import com.miui.filtersdk.beauty.BeautyProcessor;

/* loaded from: classes.dex */
public abstract class BaseBeautyFilter extends GPUImageFilter {
    public BeautyProcessor mBeautyProcessor;

    public void setBeautyProcessor(BeautyProcessor beautyProcessor) {
        this.mBeautyProcessor = beautyProcessor;
    }
}
