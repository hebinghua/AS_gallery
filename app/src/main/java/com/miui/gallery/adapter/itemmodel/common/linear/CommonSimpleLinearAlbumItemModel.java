package com.miui.gallery.adapter.itemmodel.common.linear;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.bumptech.glide.request.RequestOptions;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel.ViewHolder;
import com.miui.gallery.ui.CommonWrapperCheckableLinearItemLayout;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;

/* loaded from: classes.dex */
public class CommonSimpleLinearAlbumItemModel<DATA extends CommonAlbumItemViewBean, VH extends ViewHolder> extends CommonAlbumItemModel<DATA, VH> implements CommonWrapperCheckableLinearItemLayout.OnChangeCheckableStatusCallback {
    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.album_common_vertical_item;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public /* bridge */ /* synthetic */ RequestOptions getImageLoaderOptions(Context context, AlbumDetailInfoProvider albumDetailInfoProvider) {
        return getImageLoaderOptions(context, (Context) ((CommonAlbumItemViewBean) albumDetailInfoProvider));
    }

    public CommonSimpleLinearAlbumItemModel(DATA data) {
        this(data.getId(), data);
    }

    public CommonSimpleLinearAlbumItemModel(long j, DATA data) {
        super(j, data);
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return (EpoxyAdapter.IViewHolderCreator<VH>) new EpoxyAdapter.IViewHolderCreator<VH>() { // from class: com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public VH mo1603create(View view, View view2) {
                return (VH) new ViewHolder(view);
            }
        };
    }

    public RequestOptions getImageLoaderOptions(Context context, DATA data) {
        if (data.getSource() == null) {
            return AlbumImageLoadOptions.getInstance().getDefaultAlbumImageOptions();
        }
        return AlbumImageLoadOptions.getInstance().getDefaultAlbumImageOptions(data.getCoverSize());
    }

    public void onChangeCheckableStatus(ConstraintLayout constraintLayout, boolean z) {
        constraintLayout.findViewById(R.id.tvRightArrow).setVisibility(z ? 8 : 0);
    }

    /* loaded from: classes.dex */
    public static class ViewHolder extends CommonAlbumItemModel.ViewHolder {
        public ViewStub.OnInflateListener mInflateListener;
        public ViewStub mOtherViewStub;
        public View mRightArrowView;

        public int defaultRightArrowLayout() {
            return R.layout.album_common_linear_right_arrow_button_layout;
        }

        public boolean needRightArrowButton() {
            return true;
        }

        public ViewHolder(View view) {
            super(view);
            this.mOtherViewStub = (ViewStub) view.findViewById(R.id.vsOtherView);
            if (needRightArrowButton()) {
                ViewStub viewStub = (ViewStub) view.findViewById(R.id.vsRightArrowButton);
                viewStub.setLayoutResource(defaultRightArrowLayout());
                viewStub.setOnInflateListener(new ViewStub.OnInflateListener() { // from class: com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel.ViewHolder.1
                    @Override // android.view.ViewStub.OnInflateListener
                    public void onInflate(ViewStub viewStub2, View view2) {
                        ConstraintLayout constraintLayout = (ConstraintLayout) view2.getParent();
                        ConstraintSet constraintSet = ViewHolder.this.getConstraintSet(constraintLayout);
                        constraintSet.constrainWidth(R.id.album_common_title, 0);
                        constraintSet.constrainWidth(R.id.album_common_sub_title, 0);
                        constraintSet.setVerticalBias(R.id.album_common_title, 0.3f);
                        constraintSet.connect(R.id.album_common_title, 7, view2.getId(), 6, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getTitleAndRightArrowMargin());
                        constraintSet.connect(R.id.album_common_sub_title, 7, view2.getId(), 6, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getTitleAndRightArrowMargin());
                        constraintSet.applyTo(constraintLayout);
                    }
                });
                this.mRightArrowView = viewStub.inflate();
                this.mRightArrowView = view.findViewById(R.id.tvRightArrow);
            }
        }

        public final ConstraintSet getConstraintSet(ConstraintLayout constraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            return constraintSet;
        }

        public final View inflateOtherView(int i, int i2) {
            return inflateOtherView(i, i2, null);
        }

        public View inflateOtherView(int i, int i2, ViewStub.OnInflateListener onInflateListener) {
            this.mOtherViewStub.setLayoutResource(i);
            this.mOtherViewStub.setInflatedId(i2);
            ViewStub viewStub = this.mOtherViewStub;
            if (onInflateListener == null) {
                onInflateListener = new ViewStub.OnInflateListener() { // from class: com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel.ViewHolder.2
                    @Override // android.view.ViewStub.OnInflateListener
                    public void onInflate(ViewStub viewStub2, View view) {
                        ConstraintLayout constraintLayout = (ConstraintLayout) view.getParent();
                        ConstraintSet constraintSet = ViewHolder.this.getConstraintSet(constraintLayout);
                        constraintSet.constrainWidth(R.id.album_common_title, 0);
                        constraintSet.constrainWidth(R.id.album_common_sub_title, 0);
                        constraintSet.setVerticalBias(R.id.album_common_title, 0.3f);
                        constraintSet.connect(R.id.album_common_title, 7, view.getId(), 6, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getTitleAndRightArrowMargin());
                        constraintSet.connect(R.id.album_common_sub_title, 7, view.getId(), 6, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getTitleAndRightArrowMargin());
                        constraintSet.applyTo(constraintLayout);
                        if (ViewHolder.this.mInflateListener != null) {
                            ViewHolder.this.mInflateListener.onInflate(viewStub2, view);
                        }
                    }
                };
            }
            viewStub.setOnInflateListener(onInflateListener);
            Object tag = this.itemView.getTag(R.id.album_common_other_view_inflate_listener_tag);
            if (tag instanceof ViewStub.OnInflateListener) {
                this.mInflateListener = (ViewStub.OnInflateListener) tag;
            }
            return this.mOtherViewStub.inflate();
        }
    }
}
