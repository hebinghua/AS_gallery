package com.miui.gallery.ui.album.main.viewbean;

import com.miui.gallery.R;
import com.miui.gallery.model.dto.AIAlbumCover;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.ExtraSourceProvider;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.ResourceUtils;

/* loaded from: classes2.dex */
public class AIAlbumGridCoverViewBean extends FourPalaceGridCoverViewBean<AIAlbumCover> implements ExtraSourceProvider<Album> {
    public Album mAlbumSource;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(CoverList coverList) {
        super.mapping(coverList);
        setAlbumName(ResourceUtils.getString(R.string.album_ai_page_title));
        setAlbumDescription(ResourceUtils.getString(R.string.album_ai_description));
        setId(2147483639L);
        Album album = new Album(getId());
        this.mAlbumSource = album;
        album.setAlbumName(getAlbumName());
        this.mAlbumSource.setSortInfo(GalleryPreferences.Album.getFixedAlbumSortInfo(getId()));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.model.dto.ExtraSourceProvider
    /* renamed from: provider */
    public Album mo1601provider() {
        return this.mAlbumSource;
    }
}
