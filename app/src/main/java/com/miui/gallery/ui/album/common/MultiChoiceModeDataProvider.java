package com.miui.gallery.ui.album.common;

import com.miui.gallery.model.dto.Album;
import java.util.Collection;

/* loaded from: classes2.dex */
public interface MultiChoiceModeDataProvider {
    int providerCheckedCount();

    Collection<Album> providerCurrentOperationAlbums();
}
