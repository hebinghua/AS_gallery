package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryViewHolder;
import com.miui.gallery.ui.album.common.CustomViewItemViewBean;
import com.miui.gallery.util.ViewUtils;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class CustomViewItemModel extends BaseGalleryItemModel<CustomViewItemViewBean, VH> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.custom_item_view_layout;
    }

    public CustomViewItemModel(CustomViewItemViewBean customViewItemViewBean) {
        super(customViewItemViewBean.getId(), customViewItemViewBean);
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(VH vh) {
        super.bindData((CustomViewItemModel) vh);
        Folme.useAt(vh.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(vh.itemView, new AnimConfig[0]);
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<VH>() { // from class: com.miui.gallery.adapter.itemmodel.CustomViewItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public VH mo1603create(View view, View view2) {
                if (CustomViewItemModel.this.getItemData().getLayoutId() == 0 && (view2 instanceof ViewGroup)) {
                    return new VH(view, CustomViewItemModel.this.getItemData().getView());
                }
                return new VH(view, CustomViewItemModel.this.getItemData().getLayoutId());
            }
        };
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getViewType() {
        if (getItemData().getView() != null || getItemData().getLayoutId() != 0) {
            return (int) getItemData().getId();
        }
        return super.getViewType();
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getSpanSize(int i, int i2, int i3) {
        return getItemData().isFillSpan() ? i : getItemData().getSpanCount();
    }

    /* loaded from: classes.dex */
    public static class VH extends BaseGalleryViewHolder {
        public View mCustomView;
        public ViewStub mainView;

        public VH(View view, int i) {
            super(view);
            ViewStub viewStub = (ViewStub) view.findViewById(R.id.vsMainView);
            this.mainView = viewStub;
            viewStub.setLayoutResource(i);
            this.mCustomView = this.mainView.inflate();
        }

        public VH(View view, View view2) {
            super(view);
            this.mCustomView = view2;
            if (view2.getParent() != null) {
                ViewUtils.removeParent(this.mCustomView);
                ((ViewGroup) view).removeView(this.mCustomView);
            }
            int id = view.getId();
            Object tag = this.mCustomView.getTag(R.id.custom_view_item_layout_param_tag);
            ConstraintLayout.LayoutParams layoutParams = null;
            if (tag instanceof CustomViewItemViewBean.CustomViewLayoutParamConfig) {
                CustomViewItemViewBean.CustomViewLayoutParamConfig customViewLayoutParamConfig = (CustomViewItemViewBean.CustomViewLayoutParamConfig) tag;
                layoutParams = new ConstraintLayout.LayoutParams(customViewLayoutParamConfig.getWidth() == 0 ? -1 : customViewLayoutParamConfig.getWidth(), customViewLayoutParamConfig.getHeight() == 0 ? -1 : customViewLayoutParamConfig.getHeight());
                int[] margins = customViewLayoutParamConfig.getMargins();
                if (margins != null) {
                    layoutParams.setMargins(margins[0], margins[1], margins[2], margins[3]);
                    if (margins[3] != 0) {
                        layoutParams.bottomToBottom = id;
                    }
                }
                if (customViewLayoutParamConfig.getWidth() == -1) {
                    layoutParams.startToStart = id;
                    layoutParams.endToEnd = id;
                    layoutParams.constrainedWidth = true;
                }
            }
            if (layoutParams == null) {
                ((ConstraintLayout) view).addView(this.mCustomView);
            } else {
                ((ConstraintLayout) view).addView(this.mCustomView, layoutParams);
            }
        }
    }
}
