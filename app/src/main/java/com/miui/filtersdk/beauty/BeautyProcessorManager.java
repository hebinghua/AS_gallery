package com.miui.filtersdk.beauty;

/* loaded from: classes.dex */
public class BeautyProcessorManager {
    public static final BeautyProcessorManager INSTANCE = new BeautyProcessorManager();

    public IntelligentBeautyProcessor getBeautyProcessor() {
        return new ArcsoftBeautyProcessor();
    }
}
