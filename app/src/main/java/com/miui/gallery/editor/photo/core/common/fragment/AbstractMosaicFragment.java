package com.miui.gallery.editor.photo.core.common.fragment;

import com.miui.gallery.editor.photo.core.RenderFragment;
import com.miui.gallery.editor.photo.core.common.model.MosaicData;

/* loaded from: classes2.dex */
public abstract class AbstractMosaicFragment extends RenderFragment {
    public abstract boolean isDrawingMosaic();

    public abstract void setMosaicData(MosaicData mosaicData);

    public abstract void setMosaicPaintSize(int i);
}
