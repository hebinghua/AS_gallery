package com.miui.epoxy.common;

import android.content.Context;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import java.util.Objects;

/* loaded from: classes.dex */
public abstract class BaseItemModel<DATA, VH extends EpoxyViewHolder> extends EpoxyModel<VH> {
    public DATA mItemData;

    public BaseItemModel() {
        this(-1L);
    }

    public BaseItemModel(long j) {
        this(j, null);
    }

    public BaseItemModel(long j, DATA data) {
        super(j);
        this.mItemData = data;
    }

    public Context getThemeContext(VH vh) {
        if (vh == null) {
            return null;
        }
        return vh.itemView.getContext();
    }

    public DATA getItemData() {
        return this.mItemData;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public boolean isContentTheSame(EpoxyModel<?> epoxyModel) {
        if (epoxyModel instanceof BaseItemModel) {
            return Objects.equals(((BaseItemModel) epoxyModel).getItemData(), getItemData());
        }
        return super.isContentTheSame(epoxyModel);
    }
}
