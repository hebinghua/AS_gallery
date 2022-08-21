package com.miui.gallery.model.datalayer.repository.album.rubbish;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface IRubbishAlbumModel {
    Flowable<RubbishAlbumManualHideResult> doAddNoMediaForRubbishAlbum(List<String> list);

    Flowable<Boolean> doChangeAlbumShowInRubbishPage(boolean z, long[] jArr);

    Flowable<RubbishAlbumManualHideResult> doRemoveNoMediaForRubbishAlbum(List<String> list);

    Flowable<PageResults<List<Album>>> queryRubbishAlbum(Integer num);

    Flowable<PageResults<CoverList>> queryRubbishAlbumsAllPhoto(Integer num);
}
