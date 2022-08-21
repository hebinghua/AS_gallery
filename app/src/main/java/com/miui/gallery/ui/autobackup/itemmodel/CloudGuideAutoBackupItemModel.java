package com.miui.gallery.ui.autobackup.itemmodel;

import android.view.View;
import android.widget.CompoundButton;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel;
import com.miui.gallery.ui.autobackup.viewbean.CloudGuideAutoBackupItemViewBean;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;
import miuix.slidingwidget.widget.SlidingButton;

/* loaded from: classes2.dex */
public class CloudGuideAutoBackupItemModel extends CommonSimpleLinearAlbumItemModel<CloudGuideAutoBackupItemViewBean, VH> {
    public CloudGuideAutoBackupItemModel(CloudGuideAutoBackupItemViewBean cloudGuideAutoBackupItemViewBean) {
        super(cloudGuideAutoBackupItemViewBean.getId(), cloudGuideAutoBackupItemViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public void bindData(VH vh) {
        super.bindData((CloudGuideAutoBackupItemModel) vh);
        CloudGuideAutoBackupItemViewBean cloudGuideAutoBackupItemViewBean = (CloudGuideAutoBackupItemViewBean) getItemData();
        vh.mCheckBox.setEnabled(cloudGuideAutoBackupItemViewBean.isEnable());
        vh.mCheckBox.setChecked(cloudGuideAutoBackupItemViewBean.isBackup());
        Folme.useAt(vh.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(vh.itemView, new AnimConfig[0]);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel, com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<VH>() { // from class: com.miui.gallery.ui.autobackup.itemmodel.CloudGuideAutoBackupItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public VH mo1603create(View view, View view2) {
                return new VH(view);
            }
        };
    }

    /* loaded from: classes2.dex */
    public static class VH extends CommonSimpleLinearAlbumItemModel.ViewHolder {
        public final SlidingButton mCheckBox;

        @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel.ViewHolder
        public boolean needRightArrowButton() {
            return false;
        }

        public VH(View view) {
            super(view);
            view.setId(R.id.item_cloud_album);
            this.mCheckBox = (SlidingButton) inflateOtherView(R.layout.album_common_linear_sliding_button_layout, R.id.cbBackup);
        }

        public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
            SlidingButton slidingButton = this.mCheckBox;
            if (slidingButton != null) {
                slidingButton.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        }

        public SlidingButton getCheckBox() {
            return this.mCheckBox;
        }
    }
}
