package com.miui.gallery.ui.album.otheralbum;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment;
import com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter;
import java.util.Collection;

/* loaded from: classes2.dex */
public abstract class OtherAlbumContract$V<P extends BaseOtherAlbumPresenter> extends BaseOtherAlbumFragment<P> {
    public abstract int getCheckedCount();

    public abstract long[] getCheckedItemIds();

    public abstract int[] getCheckedItemOrderedPositions();

    public abstract void showMoveOperations(Collection<Album> collection, boolean z);

    public abstract void startChoiceMode();

    public abstract void stopChoiceMode();
}
