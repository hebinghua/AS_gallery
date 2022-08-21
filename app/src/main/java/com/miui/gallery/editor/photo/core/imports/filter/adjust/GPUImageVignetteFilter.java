package com.miui.gallery.editor.photo.core.imports.filter.adjust;

import android.graphics.PointF;
import android.opengl.GLES20;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.gallery.R;
import com.miui.gallery.editor.blocksdk.Block;
import com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate;
import com.miui.gallery.editor.photo.core.imports.filter.render.ISpecialProcessFilter;
import com.miui.gallery.editor.photo.core.imports.filter.render.OpenGlUtils;
import com.miui.gallery.util.StaticContext;

/* loaded from: classes2.dex */
public class GPUImageVignetteFilter extends BaseOriginalFilter implements IFilterEmptyValidate, ISpecialProcessFilter {
    public float[] mBlockOffset;
    public int mBlockOffsetLocation;
    public PointF mVignetteCenter;
    public int mVignetteCenterLocation;
    public float[] mVignetteColor;
    public int mVignetteColorLocation;
    public float mVignetteEnd;
    public int mVignetteEndLocation;
    public float mVignetteStart;
    public int mVignetteStartLocation;

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.ISpecialProcessFilter
    public int getSpecialBoard() {
        return 0;
    }

    @Override // com.miui.filtersdk.filter.base.BaseOriginalFilter
    public boolean isDegreeAdjustSupported() {
        return true;
    }

    public GPUImageVignetteFilter(PointF pointF, float[] fArr) {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", OpenGlUtils.readShaderFromRawResource(StaticContext.sGetAndroidContext(), R.raw.filter_vigneete));
        this.mVignetteCenter = pointF;
        this.mVignetteColor = fArr;
    }

    public GPUImageVignetteFilter(int i) {
        this(new PointF(0.5f, 0.5f), new float[]{0.0f, 0.0f, 0.0f});
        setDegree(i);
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setVignetteStart(((this.mDegree * (-0.51f)) / 100.0f) + 0.71f);
        setVignetteEnd(((this.mDegree * (-0.19999993f)) / 100.0f) + 1.3f);
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteColorLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteColor");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        this.mBlockOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "blockOffset");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteColor(this.mVignetteColor);
        this.mBlockOffset = new float[]{0.0f, 0.0f};
        setBlockOffset(0.0f, 0.0f);
    }

    public void setBlockOffset(float f, float f2) {
        float[] fArr = this.mBlockOffset;
        fArr[0] = f;
        fArr[1] = f2;
        setFloatVec2(this.mBlockOffsetLocation, fArr);
    }

    public void setVignetteCenter(PointF pointF) {
        this.mVignetteCenter = pointF;
        setPoint(this.mVignetteCenterLocation, pointF);
    }

    public void setVignetteColor(float[] fArr) {
        this.mVignetteColor = fArr;
        setFloatVec3(this.mVignetteColorLocation, fArr);
    }

    public void setVignetteStart(float f) {
        this.mVignetteStart = f;
        setFloat(this.mVignetteStartLocation, f);
    }

    public void setVignetteEnd(float f) {
        this.mVignetteEnd = f;
        setFloat(this.mVignetteEndLocation, f);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate
    public boolean isEmpty() {
        return this.mDegree == 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.ISpecialProcessFilter
    public void setBlock(Block block) {
        Block.TotalBlockInfo totalBlockInfo = block.mTotalBlockInfo;
        setVignetteCenter(new PointF(totalBlockInfo.mTotalColumn / 2.0f, totalBlockInfo.mTotalRow / 2.0f));
        setBlockOffset(block.mColumn, block.mRow);
    }
}
