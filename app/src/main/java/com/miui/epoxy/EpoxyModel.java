package com.miui.epoxy;

import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.utils.EpoxyAdapterUtils;
import java.util.List;

/* loaded from: classes.dex */
public abstract class EpoxyModel<VH extends EpoxyViewHolder> {
    public static long idCounter = -2;
    public boolean afterModelBuild;
    public long id;
    @Deprecated
    public VH viewHolder;

    public void bindData(VH vh) {
    }

    public void bindPartialData(VH vh, List<Object> list) {
    }

    public void detachedFromWindow(VH vh) {
    }

    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        return null;
    }

    public abstract int getLayoutRes();

    public int getSpanSize(int i, int i2, int i3) {
        return 1;
    }

    /* renamed from: getViewHolderCreator */
    public abstract EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator();

    public boolean isContentTheSame(EpoxyModel<?> epoxyModel) {
        return true;
    }

    public void unbind(VH vh) {
    }

    public VH getViewHolder() {
        return this.viewHolder;
    }

    public EpoxyModel(long j) {
        this.afterModelBuild = false;
        if (j == -1) {
            long j2 = idCounter;
            idCounter = j2 - 1;
            this.id = j2;
            return;
        }
        this.id = j;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public EpoxyModel() {
        /*
            r4 = this;
            long r0 = com.miui.epoxy.EpoxyModel.idCounter
            r2 = 1
            long r2 = r0 - r2
            com.miui.epoxy.EpoxyModel.idCounter = r2
            r4.<init>(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.epoxy.EpoxyModel.<init>():void");
    }

    public final long id() {
        return this.id;
    }

    public int getViewType() {
        return EpoxyAdapterUtils.hashInt(getLayoutRes());
    }

    public static boolean isValidPayload(List<Object> list) {
        return !list.isEmpty() && !(list.get(0) instanceof EpoxyAdapter.DefaultPayload);
    }

    public void attachedToWindow(VH vh) {
        this.viewHolder = vh;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EpoxyModel)) {
            return false;
        }
        EpoxyModel epoxyModel = (EpoxyModel) obj;
        return this.id == epoxyModel.id && getViewType() == epoxyModel.getViewType();
    }

    public int hashCode() {
        long j = this.id;
        return (((int) (j ^ (j >>> 32))) * 31) + getViewType();
    }
}
