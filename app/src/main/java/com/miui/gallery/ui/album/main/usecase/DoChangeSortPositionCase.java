package com.miui.gallery.ui.album.main.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class DoChangeSortPositionCase extends BaseUseCase<Boolean, Param> {
    public AbstractAlbumRepository mRepository;

    public DoChangeSortPositionCase(AbstractAlbumRepository abstractAlbumRepository) {
        this.mRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<Boolean> buildUseCaseFlowable(Param param) {
        return this.mRepository.doChangeAlbumSortPosition(param.ids, param.values);
    }

    /* loaded from: classes2.dex */
    public static class Param {
        public long[] ids;
        public String[] values;

        public Param(long[] jArr, String[] strArr) {
            this.ids = jArr;
            this.values = strArr;
        }
    }
}
