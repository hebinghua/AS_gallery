package com.miui.gallery.ui.album.common.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.ShareAlbum;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryShareAlbumCase extends BaseUseCase<List<ShareAlbum>, Void> {
    public AbstractAlbumRepository mRepository;

    public QueryShareAlbumCase(AbstractAlbumRepository abstractAlbumRepository) {
        this.mRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<List<ShareAlbum>> buildUseCaseFlowable(Void r1) {
        return this.mRepository.queryAlbumListShareInfo();
    }
}
