package com.miui.gallery.ui.album.common.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class DoUnHideAlbumCase extends BaseUseCase<Boolean, Long> {
    public AbstractAlbumRepository mAlbumRepository;

    public DoUnHideAlbumCase(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<Boolean> buildUseCaseFlowable(Long l) {
        if (l == null || l.longValue() <= 0) {
            return Flowable.empty();
        }
        return this.mAlbumRepository.cancelAlbumHiddenStatus(l.longValue());
    }
}
