package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import android.view.ViewGroup;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.widget.EmptyPageWithoutSBL;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class DefaultEmptyPageItemModel extends BaseGalleryItemModel<DefaultEmptyPageBean, EpoxyViewHolder> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getSpanSize(int i, int i2, int i3) {
        return i;
    }

    public DefaultEmptyPageItemModel(DefaultEmptyPageBean defaultEmptyPageBean) {
        super(defaultEmptyPageBean.getId(), defaultEmptyPageBean);
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return getItemData().getLayoutId() == 0 ? R.layout.empty_page_default : getItemData().getLayoutId();
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(EpoxyViewHolder epoxyViewHolder) {
        super.bindData(epoxyViewHolder);
        if (getItemData().getConfig() != null) {
            View view = epoxyViewHolder.itemView;
            if (view instanceof EmptyPageWithoutSBL) {
                ((EmptyPageWithoutSBL) view).apply(getItemData().getConfig());
            }
        }
        Folme.useAt(epoxyViewHolder.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(epoxyViewHolder.itemView, new AnimConfig[0]);
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<EpoxyViewHolder> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<EpoxyViewHolder>() { // from class: com.miui.gallery.adapter.itemmodel.DefaultEmptyPageItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create */
            public EpoxyViewHolder mo1603create(View view, View view2) {
                View emptyView = DefaultEmptyPageItemModel.this.getItemData().getEmptyView();
                if (emptyView != null && (view instanceof ViewGroup)) {
                    if (emptyView.getParent() == view) {
                        ((ViewGroup) view).removeView(emptyView);
                    }
                    return new EpoxyViewHolder(emptyView);
                }
                return new EpoxyViewHolder(view);
            }
        };
    }

    /* loaded from: classes.dex */
    public static class DefaultEmptyPageBean extends BaseViewBean {
        public EmptyPageWithoutSBL.EmptyConfig mConfig;
        public View mEmptyView;
        public int mLayoutId;

        public DefaultEmptyPageBean(int i) {
            this.id = i;
            this.mLayoutId = i;
        }

        public DefaultEmptyPageBean(int i, EmptyPageWithoutSBL.EmptyConfig emptyConfig) {
            this.id = i;
            this.mConfig = emptyConfig;
            this.mLayoutId = 0;
            this.mEmptyView = null;
        }

        public View getEmptyView() {
            return this.mEmptyView;
        }

        public int getLayoutId() {
            return this.mLayoutId;
        }

        public EmptyPageWithoutSBL.EmptyConfig getConfig() {
            return this.mConfig;
        }

        @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
        public long getId() {
            return this.id;
        }
    }
}
