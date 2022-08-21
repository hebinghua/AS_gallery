package com.miui.gallery.editor.photo.penengine.entity;

import com.miui.gallery.R;
import com.miui.gallery.editor.photo.penengine.entity.Tool;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicData;
import java.util.List;

/* loaded from: classes2.dex */
public class Mosaic extends ActivableTool {
    public int currentSelectIndex;
    public List<MosaicData> mosaicDataList;
    public int size;

    public Mosaic(int i) {
        super(Tool.ToolType.MOSAIC, R.drawable.screen_ic_brush_mosaic_body, R.drawable.screen_ic_brush_mosaic_shadow_select, R.drawable.screen_ic_brush_mosaic_shadow_unselect);
        this.size = i;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int i) {
        this.size = i;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.Tool
    public String toString() {
        return "Mosaic{currentSelectIndex=" + this.currentSelectIndex + ", size=" + this.size + ", mosaicDataList=" + this.mosaicDataList + '}';
    }

    public int getCurrentSelectIndex() {
        return this.currentSelectIndex;
    }

    public void setCurrentSelectIndex(int i) {
        this.currentSelectIndex = i;
    }

    public void setMosaicDataList(List<MosaicData> list) {
        this.mosaicDataList = list;
    }

    public MosaicData getMosaicData() {
        return this.mosaicDataList.get(this.currentSelectIndex);
    }
}
