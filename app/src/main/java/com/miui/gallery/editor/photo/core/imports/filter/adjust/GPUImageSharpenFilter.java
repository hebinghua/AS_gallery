package com.miui.gallery.editor.photo.core.imports.filter.adjust;

import android.opengl.GLES20;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.gallery.editor.blocksdk.Block;
import com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate;
import com.miui.gallery.editor.photo.core.imports.filter.render.ISpecialProcessFilter;

/* loaded from: classes2.dex */
public class GPUImageSharpenFilter extends BaseOriginalFilter implements IFilterEmptyValidate, ISpecialProcessFilter {
    public int mImageHeightFactorLocation;
    public int mImageWidthFactorLocation;
    public int mSharpHeight;
    public int mSharpWidth;
    public float mSharpness;
    public int mSharpnessLocation;

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.ISpecialProcessFilter
    public int getSpecialBoard() {
        return 0;
    }

    @Override // com.miui.filtersdk.filter.base.BaseOriginalFilter
    public boolean isDegreeAdjustSupported() {
        return true;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.ISpecialProcessFilter
    public void setBlock(Block block) {
    }

    public GPUImageSharpenFilter(int i) {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform float imageWidthFactor; \nuniform float imageHeightFactor; \nuniform float sharpness;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate; \nvarying vec2 topTextureCoordinate;\nvarying vec2 bottomTextureCoordinate;\n\nvarying float centerMultiplier;\nvarying float edgeMultiplier;\n\nvoid main()\n{\n    gl_Position = position;\n    \n    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n    \n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n    topTextureCoordinate = inputTextureCoordinate.xy + heightStep;     \n    bottomTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    \n    centerMultiplier = 1.0 - 4.0 * sharpness;\n    edgeMultiplier = sharpness;\n}", "precision highp float;\n\n            varying highp vec2 textureCoordinate;\n            varying highp vec2 leftTextureCoordinate;\n            varying highp vec2 rightTextureCoordinate;\n            varying highp vec2 topTextureCoordinate;\n            varying highp vec2 bottomTextureCoordinate;\n\n            varying highp float centerMultiplier;\n            varying highp float edgeMultiplier;\n\n            uniform sampler2D inputImageTexture;\n\n            void main()\n            {\n                mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n                mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n                mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n                mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n                mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n\n                mediump vec3 sharpenColor = textureColor * centerMultiplier + (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier);\n\n                float weight = 0.3;\n\n                mediump vec3 re = (textureColor - weight * sharpenColor) / (1.0 - weight);\n\n                gl_FragColor = vec4(re , 1.0);\n            }");
        setDegree(i);
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setSharpness(((this.mDegree * 2.0f) / 100.0f) + 0.0f);
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.mSharpnessLocation = GLES20.glGetUniformLocation(getProgram(), "sharpness");
        this.mImageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        this.mImageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onDisplaySizeChanged(int i, int i2) {
        super.onDisplaySizeChanged(i, i2);
        this.mSharpWidth = i;
        this.mSharpHeight = i2;
        setFloat(this.mImageWidthFactorLocation, 1.0f / i);
        setFloat(this.mImageHeightFactorLocation, 1.0f / this.mSharpHeight);
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void initFrameBuffers(int i, int i2) {
        super.initFrameBuffers(i, i2);
        this.mSharpWidth = i;
        this.mSharpHeight = i2;
        setFloat(this.mImageWidthFactorLocation, 1.0f / i);
        setFloat(this.mImageHeightFactorLocation, 1.0f / this.mSharpHeight);
    }

    public void setSharpness(float f) {
        this.mSharpness = f;
        setFloat(this.mSharpnessLocation, f);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate
    public boolean isEmpty() {
        return this.mDegree == 0;
    }
}
