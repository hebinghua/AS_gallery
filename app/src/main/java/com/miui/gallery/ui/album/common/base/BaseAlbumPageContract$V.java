package com.miui.gallery.ui.album.common.base;

import android.util.Pair;
import com.miui.gallery.app.base.BaseListPageFragment;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P;
import com.miui.gallery.ui.album.main.usecase.DoChangeAlbumSortTypeCase;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseAlbumPageContract$V<PRESENTER extends BaseAlbumPageContract$P> extends BaseListPageFragment<BaseViewBean, PRESENTER> {
    public abstract void onAddNoMediaForAlbumFailed(int i, Collection<Album> collection);

    public abstract void onAddNoMediaForAlbumSuccess(Collection<Album> collection, RubbishAlbumManualHideResult rubbishAlbumManualHideResult);

    public abstract void onAlbumMoveToRubbishAlbumsFailed(int i, boolean z, Collection<Album> collection);

    public abstract void onAlbumMoveToRubbishAlbumsSuccess(long[] jArr, Collection<Album> collection);

    public abstract void onAlbumShowInPhotoTabFailed(int i, Collection<Album> collection);

    public abstract void onAlbumShowInPhotoTabSuccess(long[] jArr, Collection<Album> collection);

    public abstract void onCancelAlbumShowInPhotoTabFailed(int i, Collection<Album> collection);

    public abstract void onCancelAlbumShowInPhotoTabSuccess(long[] jArr, Collection<Album> collection);

    public abstract void onChangeAlbumBackupStatusFailed(int i, boolean z, Collection<Album> collection);

    public abstract void onChangeAlbumBackupStatusSuccess(long[] jArr, boolean z, Collection<Album> collection);

    public abstract void onChangeAlbumHideStatusFailed(int i, boolean z, Collection<Album> collection);

    public abstract void onChangeAlbumHideStatusSuccess(long[] jArr, boolean z, Collection<Album> collection);

    public abstract void onChangeAlbumSortTypeSuccess(DoChangeAlbumSortTypeCase.SortResult sortResult);

    public abstract void onDeleteAlbumsFailed(int i, int i2, Collection<Album> collection);

    public abstract void onDeleteAlbumsSuccess(long[] jArr, int i, Collection<Album> collection);

    public abstract void onMoveAlbumToOtherAlbumsFailed(int i, Collection<Album> collection);

    public abstract void onMoveAlbumToOtherAlbumsSuccess(long[] jArr, Collection<Album> collection);

    public abstract void onRemoveAlbumsFromOtherAlbumsFailed(int i, Collection<Album> collection);

    public abstract void onRemoveAlbumsFromOtherAlbumsSuccess(long[] jArr, Collection<Album> collection);

    public abstract void onReplaceAlbumCoverIsFailed(Collection<Album> collection, Long l);

    public abstract void onReplaceAlbumCoverIsSuccess(Collection<Album> collection, List<Pair<Album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> list);
}
