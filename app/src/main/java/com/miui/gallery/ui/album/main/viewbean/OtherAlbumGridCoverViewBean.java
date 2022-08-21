package com.miui.gallery.ui.album.main.viewbean;

import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.ExtraSourceProvider;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.DimensionUtils;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class OtherAlbumGridCoverViewBean extends FourPalaceGridCoverViewBean<BaseAlbumCover> implements ExtraSourceProvider<Album> {
    public Album mAlbumSource;

    @Override // com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean
    public void setCovers(List<BaseAlbumCover> list) {
        super.setCovers(list);
        if (list == null || list.isEmpty()) {
            return;
        }
        final ArrayList arrayList = new ArrayList(list.size());
        getCovers().forEach(new Consumer<BaseAlbumCover>() { // from class: com.miui.gallery.ui.album.main.viewbean.OtherAlbumGridCoverViewBean.1
            @Override // java.util.function.Consumer
            public void accept(BaseAlbumCover baseAlbumCover) {
                String str = baseAlbumCover.albumName;
                if (!arrayList.contains(str)) {
                    arrayList.add(str);
                }
            }
        });
        setAlbumDescription(TextUtils.join(",", arrayList));
    }

    @Override // com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean, com.miui.gallery.util.CheckEmptyDataSubscriber.onCheckEmpty
    public boolean isEmpty() {
        return getAlbumName() == null && getAlbumDescription() == null && super.isEmpty();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(CoverList coverList) {
        super.mapping(coverList);
        setId(2147483641L);
        setAlbumName(ResourceUtils.getString(R.string.other_album));
        if (!TextUtils.isEmpty(getAlbumDescription())) {
            setAlbumDescription(ViewUtils.getTextContentExceedMaxWidth(GalleryApp.sGetAndroidContext(), DimensionUtils.getDimension(R.dimen.album_grid_cover_size), getAlbumDescription(), DimensionUtils.getDimension(R.dimen.album_grid_sub_title_text_size)));
        }
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
