package com.miui.gallery.ui.album.rubbishalbum.viewbean;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.util.NoMediaUtil;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.RichTextUtil;

/* loaded from: classes2.dex */
public class RubbishItemItemViewBean extends CommonAlbumItemViewBean<Album, RubbishItemItemViewBean> {
    public boolean isManualHide;

    @Override // com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public CharSequence getSubTitle() {
        if (!this.isManualHide) {
            CharSequence charSequence = this.albumSubTitleText;
            if (charSequence == null) {
                return null;
            }
            return charSequence.toString();
        }
        return RichTextUtil.splitTextWithDrawable(GalleryApp.sGetAndroidContext(), this.albumSubTitleText, GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.home_page_grid_header_front_text), ResourceUtils.getString(R.string.manual_hide_state), GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.timeline_time_text_size), (int) R.drawable.info_divider);
    }

    public boolean isManualHide() {
        return this.isManualHide;
    }

    public void setManualHide(boolean z) {
        this.isManualHide = z;
    }

    @Override // com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(Album album) {
        super.mapping((RubbishItemItemViewBean) album);
        setAlbumSubTitleText(ResourceUtils.getQuantityString(R.plurals.album_photo_count_text, album.getPhotoCount(), Integer.valueOf(album.getPhotoCount())));
        setManualHide(NoMediaUtil.isManualHideAlbum(((Album) getSource()).getLocalPath()));
    }
}
