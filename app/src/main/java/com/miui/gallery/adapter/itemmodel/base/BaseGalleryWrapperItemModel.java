package com.miui.gallery.adapter.itemmodel.base;

import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.common.BaseWrapperItemModel;
import com.miui.epoxy.common.BaseWrapperItemModel.VH;

/* loaded from: classes.dex */
public abstract class BaseGalleryWrapperItemModel<DATA, CVH extends EpoxyViewHolder, VH extends BaseWrapperItemModel.VH<CVH>, MODEL extends EpoxyModel<CVH>> extends BaseWrapperItemModel<DATA, CVH, VH, MODEL> {
    public BaseGalleryWrapperItemModel(MODEL model) {
        super(model);
    }
}
