package com.miui.gallery.ui.album.aialbum.viewbean;

import android.net.Uri;
import com.miui.gallery.provider.GalleryContract;

/* loaded from: classes2.dex */
public class TagsAlbumItemViewBean extends LocationAndTagsAlbumItemViewBean {
    @Override // com.miui.gallery.ui.album.aialbum.viewbean.LocationAndTagsAlbumItemViewBean
    public Uri getMoreActionUri() {
        return GalleryContract.Search.URI_TAG_LIST_PAGE;
    }
}
