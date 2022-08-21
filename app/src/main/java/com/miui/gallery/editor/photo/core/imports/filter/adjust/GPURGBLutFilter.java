package com.miui.gallery.editor.photo.core.imports.filter.adjust;

import android.opengl.GLES20;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.filtersdk.utils.OpenGlUtils;
import com.miui.gallery.GalleryApp;

/* loaded from: classes2.dex */
public abstract class GPURGBLutFilter extends BaseOriginalFilter {
    public float mPercent;
    public int mPercentLocation;
    public int mRGBLutLocation;
    public int[] mToneCurveTexture;

    public abstract String getLUTPath();

    @Override // com.miui.filtersdk.filter.base.BaseOriginalFilter
    public boolean isDegreeAdjustSupported() {
        return true;
    }

    public GPURGBLutFilter(int i) {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "precision mediump float;\nprecision mediump float;\n\nuniform sampler2D inputImageTexture;\nuniform sampler2D sRGBLut;\nvarying vec2 textureCoordinate;\nuniform float mPercent;\n\n\nvoid main() {\n    vec3 src = texture2D(inputImageTexture, textureCoordinate).rgb;\n\n    vec3 color;\n    color.r = texture2D(sRGBLut, vec2(src.r, 0.0)).r;\n    color.g = texture2D(sRGBLut, vec2(src.g, 0.0)).g;\n    color.b = texture2D(sRGBLut, vec2(src.b, 0.0)).b;\n\n    gl_FragColor = vec4(mix(src, color, mPercent),1.0);\n}");
        this.mToneCurveTexture = new int[]{-1};
        setDegree(i);
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, this.mToneCurveTexture, 0);
        this.mToneCurveTexture[0] = -1;
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onDrawArraysAfter() {
        if (this.mToneCurveTexture[0] != -1) {
            GLES20.glActiveTexture(33987);
            GLES20.glBindTexture(3553, 0);
            GLES20.glActiveTexture(33984);
        }
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onDrawArraysPre() {
        if (this.mToneCurveTexture[0] != -1) {
            GLES20.glActiveTexture(33987);
            GLES20.glBindTexture(3553, this.mToneCurveTexture[0]);
            GLES20.glUniform1i(this.mRGBLutLocation, 3);
            GLES20.glUniform1f(this.mPercentLocation, this.mPercent);
        }
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.mRGBLutLocation = GLES20.glGetUniformLocation(getProgram(), "sRGBLut");
        this.mPercentLocation = GLES20.glGetUniformLocation(getProgram(), "mPercent");
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        runOnDraw(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.filter.adjust.GPURGBLutFilter.1
            @Override // java.lang.Runnable
            public void run() {
                GPURGBLutFilter.this.mToneCurveTexture[0] = OpenGlUtils.loadTexture(GalleryApp.sGetAndroidContext(), GPURGBLutFilter.this.getLUTPath());
            }
        });
    }
}
