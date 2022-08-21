package com.miui.gallery.editor.photo.core.imports.filter.adjust;

import com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate;

/* loaded from: classes2.dex */
public class GPUContrastFilter extends GPURGBLutFilter implements IFilterEmptyValidate {
    public GPUContrastFilter(int i) {
        super(i);
        int i2 = this.mDegree;
        if (i2 > 50) {
            this.mPercent = (((i2 - 50) * 1.0f) / 50.0f) + 0.0f;
        } else {
            this.mPercent = (((50 - i2) * 1.0f) / 50.0f) + 0.0f;
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.adjust.GPURGBLutFilter
    public String getLUTPath() {
        return this.mDegree > 50 ? "filter/adjust/adjust_high_contrast.png" : "filter/adjust/adjust_low_contrast.png";
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate
    public boolean isEmpty() {
        return this.mDegree == 50;
    }
}
