package com.miui.gallery.editor.photo.screen.mosaic;

import com.miui.gallery.editor.photo.screen.base.IScreenOperationEditor;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicData;

/* loaded from: classes2.dex */
public interface IScreenMosaicOperation extends IScreenOperationEditor {
    void setMosaicData(MosaicData mosaicData, int i);

    void setMosaicPaintSize(int i);
}
