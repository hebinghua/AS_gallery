package com.miui.gallery.adapter.itemmodel.common.grid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.bumptech.glide.request.RequestOptions;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.config.CommonGridItemViewDisplaySetting;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;

/* loaded from: classes.dex */
public class CommonSimpleGridAlbumItemModel<DATA extends AlbumDetailInfoProvider> extends CommonAlbumItemModel<DATA, CommonAlbumItemModel.ViewHolder> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.album_common_grid_item;
    }

    public CommonSimpleGridAlbumItemModel(DATA data) {
        this(data.getId(), data);
    }

    public CommonSimpleGridAlbumItemModel(long j, DATA data) {
        super(j, data);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public RequestOptions getImageLoaderOptions(Context context, DATA data) {
        if (data.getSource() == null) {
            return AlbumImageLoadOptions.getInstance().getDefaultAlbumImageOptions();
        }
        return AlbumImageLoadOptions.getInstance().getDefaultAlbumImageOptions(data.getCoverSize());
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public void configDisplaySetting(CommonGridItemViewDisplaySetting commonGridItemViewDisplaySetting, CommonAlbumItemModel.ViewHolder viewHolder, DATA data) {
        if (commonGridItemViewDisplaySetting == null) {
            resetHolderWhenHolderReuse(viewHolder);
            return;
        }
        configDisplayTitleView(viewHolder);
        configDisplaySubTitleView(viewHolder);
        configDisplayImageView(viewHolder, this.mDisplaySetting);
    }

    public final void resetHolderWhenHolderReuse(CommonAlbumItemModel.ViewHolder viewHolder) {
        viewHolder.mAlbumCover.setForeground(null);
    }

    public void configDisplayImageView(CommonAlbumItemModel.ViewHolder viewHolder, CommonGridItemViewDisplaySetting commonGridItemViewDisplaySetting) {
        if (commonGridItemViewDisplaySetting.isChangeImageSize()) {
            ViewGroup.LayoutParams layoutParams = viewHolder.mAlbumCover.getLayoutParams();
            if (commonGridItemViewDisplaySetting.getImageWidth() != 0) {
                layoutParams.width = commonGridItemViewDisplaySetting.getImageWidth();
            }
            if (commonGridItemViewDisplaySetting.getImageHeight() != 0) {
                layoutParams.height = commonGridItemViewDisplaySetting.getImageHeight();
            }
            viewHolder.mAlbumCover.setLayoutParams(layoutParams);
            viewHolder.itemView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        }
    }

    public void configDisplayTitleView(CommonAlbumItemModel.ViewHolder viewHolder) {
        if (this.mDisplaySetting.isShowTitleView()) {
            boolean isTitleCenterHorizontal = this.mDisplaySetting.isTitleCenterHorizontal();
            boolean isTitleCenterVertical = this.mDisplaySetting.isTitleCenterVertical();
            if (isTitleCenterHorizontal || isTitleCenterVertical) {
                setCenterGravity(viewHolder.mAlbumTitle.getId(), (ConstraintLayout) viewHolder.itemView, isTitleCenterHorizontal, isTitleCenterVertical);
            }
            setTextColorAndSize(viewHolder.mAlbumTitle, this.mDisplaySetting.getTitleColor(), this.mDisplaySetting.getTitleSize());
            return;
        }
        gone(viewHolder.mAlbumTitle);
    }

    public void configDisplaySubTitleView(CommonAlbumItemModel.ViewHolder viewHolder) {
        if (this.mDisplaySetting.isShowSubTitleView()) {
            boolean isSubTitleCenterHorizontal = this.mDisplaySetting.isSubTitleCenterHorizontal();
            boolean isSubTitleCenterVertical = this.mDisplaySetting.isSubTitleCenterVertical();
            if (isSubTitleCenterHorizontal || isSubTitleCenterVertical) {
                setCenterGravity(viewHolder.mAlbumSubTitle.getId(), (ConstraintLayout) viewHolder.itemView, isSubTitleCenterHorizontal, isSubTitleCenterVertical);
            }
            setTextColorAndSize(viewHolder.mAlbumSubTitle, this.mDisplaySetting.getSubTitleColor(), this.mDisplaySetting.getSubTitleSize());
            return;
        }
        gone(viewHolder.mAlbumSubTitle);
    }

    public final void setTextColorAndSize(TextView textView, int i, int i2) {
        if (i != 0) {
            textView.setTextColor(i);
        }
        if (i2 != 0) {
            textView.setTextSize(0, i2);
        }
    }

    public final void setCenterGravity(int i, ConstraintLayout constraintLayout, boolean z, boolean z2) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        if (z) {
            constraintSet.clear(i, 6);
            constraintSet.centerHorizontally(i, 0);
        }
        if (z2) {
            constraintSet.clear(i, 3);
            constraintSet.centerVertically(i, 0);
        }
        constraintSet.applyTo(constraintLayout);
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<CommonAlbumItemModel.ViewHolder> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<CommonAlbumItemModel.ViewHolder>() { // from class: com.miui.gallery.adapter.itemmodel.common.grid.CommonSimpleGridAlbumItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public CommonAlbumItemModel.ViewHolder mo1603create(View view, View view2) {
                return new CommonAlbumItemModel.ViewHolder(view);
            }
        };
    }
}
