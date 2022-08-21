package com.miui.gallery.editor.photo.penengine.entity;

import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.imports.doodle.DoodleManager;
import com.miui.gallery.editor.photo.penengine.entity.Tool;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class Shape extends Tool implements IDefaultColorable {
    public int[] colorArray;
    public int currentSelectIndex;
    public List<DoodleData> doodleDataList;
    public int selectColorIndex;

    public Shape(int[] iArr, int i) {
        super(Tool.ToolType.SHAPE);
        this.currentSelectIndex = 1;
        this.colorArray = iArr;
        this.selectColorIndex = i;
        this.doodleDataList = DoodleManager.getScreenDoodleData();
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.Tool
    public String toString() {
        return "Shape{colorArray=" + Arrays.toString(this.colorArray) + ", selectColorIndex=" + this.selectColorIndex + ", currentSelectIndex=" + this.currentSelectIndex + ", doodleDataList=" + this.doodleDataList + '}';
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.IDefaultColorable
    public int[] getColorArray() {
        return this.colorArray;
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

    @Override // com.miui.gallery.editor.photo.penengine.entity.IColorable
    public int getColor() {
        return this.colorArray[this.selectColorIndex];
    }

    public int getCurrentSelectIndex() {
        return this.currentSelectIndex;
    }

    public void setCurrentSelectIndex(int i) {
        this.currentSelectIndex = i;
    }

    public List<DoodleData> getDoodleDataList() {
        return this.doodleDataList;
    }

    public DoodleData getDoodleData() {
        return this.doodleDataList.get(this.currentSelectIndex);
    }
}
