package com.miui.gallery.editor.photo.core.imports.filter.adjust;

import android.opengl.GLES20;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate;

/* loaded from: classes2.dex */
public class GPUImageSaturationFilter extends BaseOriginalFilter implements IFilterEmptyValidate {
    public float mSaturation;
    public int mSaturationLocation;

    @Override // com.miui.filtersdk.filter.base.BaseOriginalFilter
    public boolean isDegreeAdjustSupported() {
        return true;
    }

    public GPUImageSaturationFilter(int i) {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", " varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float saturation;\n \n // Values from \"Graphics Shaders: Theory and Practice\" by Bailey and Cunningham\n const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n \n void main()\n {\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp float luminance = dot(textureColor.rgb, luminanceWeighting);\n    lowp vec3 greyScaleColor = vec3(luminance);\n    \n    gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, saturation), textureColor.w);\n     \n }");
        setDegree(i);
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.mSaturationLocation = GLES20.glGetUniformLocation(getProgram(), "saturation");
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setSaturation(((this.mDegree * 2.0f) / 100.0f) + 0.0f);
    }

    public void setSaturation(float f) {
        this.mSaturation = f;
        setFloat(this.mSaturationLocation, f);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate
    public boolean isEmpty() {
        return this.mDegree == 50;
    }
}
