package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.common.BaseItemModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryViewHolder;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class AlbumTabToolItemModel extends BaseGalleryItemModel<AlbumTabToolItemBean, VH> {
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((VH) epoxyViewHolder, (List<Object>) list);
    }

    public AlbumTabToolItemModel(AlbumTabToolItemBean albumTabToolItemBean) {
        super(albumTabToolItemBean.getId(), albumTabToolItemBean);
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return AlbumPageConfig.getAlbumTabConfig().isGridModeByAlbumTabToolGroup() ? R.layout.layout_album_tab_tool_item_grid : R.layout.layout_album_tab_tool_item_linear;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(VH vh) {
        super.bindData((AlbumTabToolItemModel) vh);
        AlbumTabToolItemBean itemData = getItemData();
        AlbumTabToolItemBean.Icon iconBean = itemData.getIconBean();
        if (iconBean != null) {
            if (!iconBean.isNeedUseBase64DecoderIcon()) {
                bindImage(vh.mIcon, iconBean.getData(), AlbumImageLoadOptions.getInstance().getDefaultNoCacheModeOption().clone().mo976signature(AndroidResourceSignature.obtain(getThemeContext(vh))));
            } else {
                BindImageHelper.bindBase64Image(iconBean.getData(), vh.mIcon, AlbumImageLoadOptions.getInstance().getDefaultNoCacheModeOption());
            }
        } else {
            bindImage(vh.mIcon, AlbumImageLoadOptions.getInstance().getDefaultNoCacheModeOption());
        }
        vh.mTitle.setText(itemData.getTitle());
        vh.mTitle.setTextColor(getThemeContext(vh).getColor(AlbumPageConfig.getInstance().isGridPageMode() ? R.color.album_tab_tool_item_grid_mode_text_color_grid : R.color.album_tab_tool_item_text_color_linear));
        vh.itemView.setContentDescription(itemData.getTitle());
        vh.mSubTitle.setText(itemData.getSubTitle());
    }

    public void bindPartialData(VH vh, List<Object> list) {
        super.bindPartialData((AlbumTabToolItemModel) vh, list);
        AlbumTabToolItemBean albumTabToolItemBean = (AlbumTabToolItemBean) list.get(0);
        if (albumTabToolItemBean.getSubTitle() != null) {
            vh.mSubTitle.setText(albumTabToolItemBean.getSubTitle());
        }
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getSpanSize(int i, int i2, int i3) {
        return AlbumPageConfig.getAlbumTabConfig().isGridModeByAlbumTabToolGroup() ? super.getSpanSize(i, i2, i3) : i;
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<VH>() { // from class: com.miui.gallery.adapter.itemmodel.AlbumTabToolItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public VH mo1603create(View view, View view2) {
                return new VH(view);
            }
        };
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof AlbumTabToolItemModel) {
            String subTitle = getItemData().getSubTitle();
            String subTitle2 = ((AlbumTabToolItemModel) epoxyModel).getItemData().getSubTitle();
            AlbumTabToolItemBean albumTabToolItemBean = new AlbumTabToolItemBean();
            if (!subTitle.equals(subTitle2)) {
                albumTabToolItemBean.setSubTitle(subTitle2);
                return albumTabToolItemBean;
            }
        }
        return super.getDiffChangeResult(epoxyModel);
    }

    /* loaded from: classes.dex */
    public static class VH extends BaseGalleryViewHolder {
        public ImageView mIcon;
        public TextView mSubTitle;
        public TextView mTitle;

        public VH(View view) {
            super(view);
            this.mIcon = (ImageView) view.findViewById(R.id.iv_icon);
            this.mTitle = (TextView) view.findViewById(R.id.tv_title);
            this.mSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
            this.mTitle.getPaint().setFakeBoldText(true);
        }

        public ImageView getIcon() {
            return this.mIcon;
        }

        public TextView getTitle() {
            return this.mTitle;
        }
    }

    @Override // com.miui.epoxy.common.BaseItemModel, com.miui.epoxy.EpoxyModel
    public boolean isContentTheSame(EpoxyModel<?> epoxyModel) {
        if (epoxyModel instanceof BaseItemModel) {
            return Objects.equals((AlbumTabToolItemBean) ((BaseItemModel) epoxyModel).getItemData(), getItemData());
        }
        return super.isContentTheSame(epoxyModel);
    }
}
