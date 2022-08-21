package com.miui.gallery.ui.album.common.viewbean;

import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.ExtraSourceProvider;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.ResourceUtils;
import java.util.Objects;

/* loaded from: classes2.dex */
public class TrashAlbumViewBean extends BaseViewBean<Integer, TrashAlbumViewBean> implements ExtraSourceProvider<Album> {
    public CharSequence albumSubTitleText;
    public Album mAlbumSource;

    public TrashAlbumViewBean() {
        super(2147483638L);
    }

    public CharSequence getAlbumSubTitleText() {
        return this.albumSubTitleText;
    }

    public void setAlbumSubTitleText(CharSequence charSequence) {
        this.albumSubTitleText = charSequence;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(Integer num) {
        super.mapping((TrashAlbumViewBean) num);
        setAlbumSubTitleText(String.valueOf(num));
        Album album = new Album(getId());
        this.mAlbumSource = album;
        album.setAlbumName(ResourceUtils.getString(R.string.trash_bin));
        this.mAlbumSource.setSortInfo(GalleryPreferences.Album.getFixedAlbumSortInfo(getId()));
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean, java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass() && super.equals(obj)) {
            return TextUtils.equals(this.albumSubTitleText, ((TrashAlbumViewBean) obj).albumSubTitleText);
        }
        return false;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        return Objects.hash(this.albumSubTitleText);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.model.dto.ExtraSourceProvider
    /* renamed from: provider */
    public Album mo1601provider() {
        return this.mAlbumSource;
    }
}
