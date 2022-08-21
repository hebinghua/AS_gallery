package com.miui.gallery.ui.album.rubbishalbum.usecase;

import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class QueryRubbishTipViewCoverList extends HotUseCase<PageResults<CoverList>, Integer> {
    public AbstractAlbumRepository mAlbumRepository;

    public QueryRubbishTipViewCoverList(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<PageResults<CoverList>> buildFlowable(Integer num) {
        return this.mAlbumRepository.queryRubbishAlbumsAllPhoto(num);
    }
}
