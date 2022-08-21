package com.miui.epoxy;

import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.EpoxyWrapperViewHolder;
import java.util.List;

/* loaded from: classes.dex */
public abstract class EpoxyWrapperModel<VH extends EpoxyWrapperViewHolder<MVH>, M extends EpoxyModel<MVH>, MVH extends EpoxyViewHolder> extends EpoxyModel<VH> {
    public final M childModel;

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator  reason: collision with other method in class */
    public abstract EpoxyAdapter.WrapperViewHolderCreator<VH, MVH> mo541getViewHolderCreator();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void attachedToWindow(EpoxyViewHolder epoxyViewHolder) {
        attachedToWindow((EpoxyWrapperModel<VH, M, MVH>) ((EpoxyWrapperViewHolder) epoxyViewHolder));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindData(EpoxyViewHolder epoxyViewHolder) {
        bindData((EpoxyWrapperModel<VH, M, MVH>) ((EpoxyWrapperViewHolder) epoxyViewHolder));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((EpoxyWrapperModel<VH, M, MVH>) ((EpoxyWrapperViewHolder) epoxyViewHolder), (List<Object>) list);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void detachedFromWindow(EpoxyViewHolder epoxyViewHolder) {
        detachedFromWindow((EpoxyWrapperModel<VH, M, MVH>) ((EpoxyWrapperViewHolder) epoxyViewHolder));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void unbind(EpoxyViewHolder epoxyViewHolder) {
        unbind((EpoxyWrapperModel<VH, M, MVH>) ((EpoxyWrapperViewHolder) epoxyViewHolder));
    }

    public M getChildModel() {
        return this.childModel;
    }

    public EpoxyWrapperModel(M m) {
        super(m.id());
        this.childModel = m;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getViewType() {
        return (super.getViewType() * 31) + this.childModel.getViewType();
    }

    public void bindData(VH vh) {
        this.childModel.bindData(vh.getChildViewHolder());
    }

    public void bindPartialData(VH vh, List<Object> list) {
        this.childModel.bindPartialData(vh.getChildViewHolder(), list);
    }

    public void unbind(VH vh) {
        this.childModel.unbind(vh.getChildViewHolder());
    }

    public void attachedToWindow(VH vh) {
        this.childModel.attachedToWindow(vh.getChildViewHolder());
    }

    public void detachedFromWindow(VH vh) {
        this.childModel.detachedFromWindow(vh.getChildViewHolder());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public boolean isContentTheSame(EpoxyModel<?> epoxyModel) {
        if (super.isContentTheSame(epoxyModel)) {
            M m = this.childModel;
            boolean z = epoxyModel instanceof EpoxyWrapperModel;
            EpoxyModel<?> epoxyModel2 = epoxyModel;
            if (z) {
                epoxyModel2 = ((EpoxyWrapperModel) epoxyModel).childModel;
            }
            if (m.isContentTheSame(epoxyModel2)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof EpoxyWrapperModel) {
            return this.childModel.getDiffChangeResult(((EpoxyWrapperModel) epoxyModel).childModel);
        }
        return this.childModel.getDiffChangeResult(epoxyModel);
    }
}
