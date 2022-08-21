package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.cloudalbum.viewbean.CloudAlbumItemViewBean;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;
import miuix.slidingwidget.widget.SlidingButton;

/* loaded from: classes.dex */
public class CloudAlbumItemModel extends CommonSimpleLinearAlbumItemModel<CloudAlbumItemViewBean, CloudAlbumViewHolder> {
    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel, com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((CloudAlbumViewHolder) epoxyViewHolder, (List<Object>) list);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public /* bridge */ /* synthetic */ void bindPartialData(CommonAlbumItemModel.ViewHolder viewHolder, List list) {
        bindPartialData((CloudAlbumViewHolder) viewHolder, (List<Object>) list);
    }

    public CloudAlbumItemModel(CloudAlbumItemViewBean cloudAlbumItemViewBean) {
        super(cloudAlbumItemViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel, com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<CloudAlbumViewHolder> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<CloudAlbumViewHolder>() { // from class: com.miui.gallery.adapter.itemmodel.CloudAlbumItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public CloudAlbumViewHolder mo1603create(View view, View view2) {
                return new CloudAlbumViewHolder(view);
            }
        };
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public void bindData(CloudAlbumViewHolder cloudAlbumViewHolder) {
        super.bindData((CloudAlbumItemModel) cloudAlbumViewHolder);
        cloudAlbumViewHolder.itemView.setTag(getItemData());
        bindSwitchButton(cloudAlbumViewHolder, ((CloudAlbumItemViewBean) getItemData()).isBackup());
        Folme.useAt(cloudAlbumViewHolder.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(cloudAlbumViewHolder.itemView, new AnimConfig[0]);
    }

    public final void bindSwitchButton(CloudAlbumViewHolder cloudAlbumViewHolder, boolean z) {
        cloudAlbumViewHolder.mCbBackup.setChecked(z);
    }

    public void bindPartialData(CloudAlbumViewHolder cloudAlbumViewHolder, List<Object> list) {
        super.bindPartialData((CloudAlbumItemModel) cloudAlbumViewHolder, list);
        CloudAlbumItemViewBean cloudAlbumItemViewBean = (CloudAlbumItemViewBean) list.get(0);
        if (!isEmpty(cloudAlbumItemViewBean.getSource())) {
            bindSwitchButton(cloudAlbumViewHolder, ((Album) cloudAlbumItemViewBean.getSource()).isAutoUploadedAlbum());
        }
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    /* renamed from: instanceDiffResultBean  reason: collision with other method in class */
    public CloudAlbumItemViewBean mo1574instanceDiffResultBean() {
        return new CloudAlbumItemViewBean();
    }

    /* loaded from: classes.dex */
    public static class CloudAlbumViewHolder extends CommonSimpleLinearAlbumItemModel.ViewHolder {
        public SlidingButton mCbBackup;

        @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel.ViewHolder
        public boolean needRightArrowButton() {
            return false;
        }

        public CloudAlbumViewHolder(View view) {
            super(view);
            view.setId(R.id.item_cloud_album);
            this.mCbBackup = (SlidingButton) inflateOtherView(R.layout.album_common_linear_sliding_button_layout, R.id.cbBackup);
        }
    }
}
