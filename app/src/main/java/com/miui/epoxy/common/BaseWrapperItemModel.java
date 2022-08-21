package com.miui.epoxy.common;

import android.view.View;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.EpoxyWrapperModel;
import com.miui.epoxy.EpoxyWrapperViewHolder;
import com.miui.epoxy.common.BaseWrapperItemModel.VH;

/* loaded from: classes.dex */
public abstract class BaseWrapperItemModel<DATA, CVH extends EpoxyViewHolder, VH extends VH<CVH>, MODEL extends EpoxyModel<CVH>> extends EpoxyWrapperModel<VH, MODEL, CVH> {
    public BaseWrapperItemModel(MODEL model) {
        super(model);
    }

    /* loaded from: classes.dex */
    public static class VH<CVH extends EpoxyViewHolder> extends EpoxyWrapperViewHolder<CVH> {
        public VH(View view, CVH cvh) {
            super(view, cvh);
        }
    }

    public DATA getItemData() {
        M m = this.childModel;
        if (m instanceof BaseItemModel) {
            return (DATA) ((BaseItemModel) m).getItemData();
        }
        if (!(m instanceof BaseWrapperItemModel)) {
            return null;
        }
        return (DATA) ((BaseItemModel) ((BaseWrapperItemModel) m).getChildModel()).getItemData();
    }
}
