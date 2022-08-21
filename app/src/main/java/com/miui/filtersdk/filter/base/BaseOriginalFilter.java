package com.miui.filtersdk.filter.base;

/* loaded from: classes.dex */
public class BaseOriginalFilter extends GPUImageFilter {
    public int mDegree;

    public boolean isDegreeAdjustSupported() {
        return false;
    }

    public BaseOriginalFilter() {
    }

    public BaseOriginalFilter(String str, String str2) {
        super(str, str2);
    }

    public void setDegree(int i) {
        if (!isDegreeAdjustSupported()) {
            throw new AssertionError("Degree adjustment of the filter is not supported!");
        }
        if (i < 0) {
            i = 0;
        }
        if (i > 100) {
            i = 100;
        }
        this.mDegree = i;
    }
}
