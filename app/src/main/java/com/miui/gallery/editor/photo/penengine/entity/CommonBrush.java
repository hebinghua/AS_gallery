package com.miui.gallery.editor.photo.penengine.entity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.core.content.ContextCompat;
import com.miui.gallery.editor.photo.penengine.entity.Tool;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes2.dex */
public class CommonBrush extends Brush implements IDefaultColorable {
    public float alpha;
    public int[] colorArray;
    public int fgHeadResId;
    public int selectColorIndex;
    public int selectSizeIndex;
    public int[] sizeArray;

    public CommonBrush(Tool.ToolType toolType, int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2, int i6, float f) {
        super(toolType, i2, i3, i4);
        this.fgHeadResId = i;
        this.sizeArray = iArr;
        this.selectSizeIndex = i5;
        this.colorArray = iArr2;
        this.selectColorIndex = i6;
        this.alpha = f;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.ActivableTool, com.miui.gallery.editor.photo.penengine.entity.IActivable
    public Drawable createFg(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, getFgHeadResId());
        Objects.requireNonNull(drawable);
        r0[0].setTint(getColor());
        Drawable drawable2 = ContextCompat.getDrawable(context, getFgBodyResId());
        Objects.requireNonNull(drawable2);
        Drawable[] drawableArr = {drawable.mutate(), drawable2.mutate()};
        return new LayerDrawable(drawableArr);
    }

    public int getFgHeadResId() {
        return this.fgHeadResId;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.IDefaultColorable
    public int[] getColorArray() {
        return this.colorArray;
    }

    public int getSelectSizeIndex() {
        return this.selectSizeIndex;
    }

    public void setSelectSizeIndex(int i) {
        this.selectSizeIndex = i;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.IDefaultColorable
    public int getSelectColorIndex() {
        return this.selectColorIndex;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.IDefaultColorable
    public void setSelectColorIndex(int i) {
        this.selectColorIndex = i;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.IDefaultColorable
    public void updateSelectColor(int i) {
        this.colorArray[this.selectColorIndex] = i;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float f) {
        this.alpha = f;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.Brush, com.miui.gallery.editor.photo.penengine.entity.IColorable
    public int getColor() {
        return this.colorArray[this.selectColorIndex];
    }

    public int getSize() {
        return this.sizeArray[this.selectSizeIndex];
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.Brush, com.miui.gallery.editor.photo.penengine.entity.Tool
    public String toString() {
        return "CommonBrush{fgHeadResId=" + this.fgHeadResId + ", sizeArray=" + Arrays.toString(this.sizeArray) + ", colorArray=" + Arrays.toString(this.colorArray) + ", selectSizeIndex=" + this.selectSizeIndex + ", selectColorIndex=" + this.selectColorIndex + ", alpha=" + this.alpha + '}';
    }
}
