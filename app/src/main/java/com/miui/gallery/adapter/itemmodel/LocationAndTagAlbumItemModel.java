package com.miui.gallery.adapter.itemmodel;

import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.config.CommonGridItemViewDisplaySetting;
import com.miui.gallery.adapter.itemmodel.common.grid.CommonSimpleGridAlbumItemModel;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.search.core.display.icon.IconImageLoader;
import com.miui.gallery.ui.album.aialbum.viewbean.LocationAndTagsAlbumItemViewBean;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class LocationAndTagAlbumItemModel extends CommonSimpleGridAlbumItemModel<LocationAndTagsAlbumItemViewBean> {
    public static final CommonGridItemViewDisplaySetting sConfig = new CommonGridItemViewDisplaySetting.DisplayConfig().setTitleViewCenterHorizontal(true).setShowSubTitleView(false).setForegroundResource(R.drawable.album_common_grid_item_rounded_selector).titleSize(R.dimen.album_ai_item_title_text_size).create();

    public LocationAndTagAlbumItemModel(LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean) {
        super(locationAndTagsAlbumItemViewBean);
        setDisplaySetting(sConfig);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    /* renamed from: instanceDiffResultBean  reason: collision with other method in class */
    public LocationAndTagsAlbumItemViewBean mo1574instanceDiffResultBean() {
        return new LocationAndTagsAlbumItemViewBean();
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public void bindCover(ImageView imageView, LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean, RequestOptions requestOptions) {
        imageView.setForeground(null);
        Folme.useAt(imageView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(imageView, new AnimConfig[0]);
        if (!((LocationAndTagsAlbumItemViewBean) getItemData()).isEmptyBackupIcons()) {
            if (locationAndTagsAlbumItemViewBean.isMoreStyle()) {
                imageView.setForeground(imageView.getResources().getDrawable(R.drawable.ai_album_more_foreground));
            }
            IconImageLoader.getInstance().displayImageEager(imageView.getContext(), locationAndTagsAlbumItemViewBean.getCoverUri(), DownloadType.MICRO, imageView, requestOptions, ((LocationAndTagsAlbumItemViewBean) getItemData()).getBackupIcons());
            return;
        }
        super.bindCover(imageView, (ImageView) locationAndTagsAlbumItemViewBean, requestOptions);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel, com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof LocationAndTagAlbumItemModel) {
            LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean = (LocationAndTagsAlbumItemViewBean) ((LocationAndTagAlbumItemModel) epoxyModel).getItemData();
            LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean2 = (LocationAndTagsAlbumItemViewBean) getItemData();
            LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean3 = new LocationAndTagsAlbumItemViewBean();
            if (!isEquals(locationAndTagsAlbumItemViewBean.getTitle(), locationAndTagsAlbumItemViewBean2.getTitle())) {
                locationAndTagsAlbumItemViewBean3.set(47, locationAndTagsAlbumItemViewBean.getTitle());
            }
            if (locationAndTagsAlbumItemViewBean2.getCoverUri() != null && locationAndTagsAlbumItemViewBean.getCoverUri() != null && !isEquals(locationAndTagsAlbumItemViewBean.getCoverUri(), locationAndTagsAlbumItemViewBean2.getCoverUri())) {
                locationAndTagsAlbumItemViewBean3.set(111, locationAndTagsAlbumItemViewBean.getCoverUri());
            }
            locationAndTagsAlbumItemViewBean3.setSource(locationAndTagsAlbumItemViewBean.getSource());
            if (locationAndTagsAlbumItemViewBean2.isMoreStyle() != locationAndTagsAlbumItemViewBean.isMoreStyle()) {
                locationAndTagsAlbumItemViewBean3.setNeedShowMoreStyle(locationAndTagsAlbumItemViewBean.isMoreStyle());
                locationAndTagsAlbumItemViewBean3.set(47, locationAndTagsAlbumItemViewBean.getTitle());
                if (locationAndTagsAlbumItemViewBean.getCoverUri() != null) {
                    locationAndTagsAlbumItemViewBean3.set(111, locationAndTagsAlbumItemViewBean.getCoverUri());
                }
            }
            return locationAndTagsAlbumItemViewBean3;
        }
        return super.getDiffChangeResult(epoxyModel);
    }
}
