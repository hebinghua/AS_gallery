package com.miui.gallery.ui.album.rubbishalbum.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.ui.album.common.base.requestbean.BaseOperationAlbumRequestBean;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class DoChangeAlbumShowInOtherAlbum extends BaseUseCase<Boolean, BaseOperationAlbumRequestBean> {
    public AbstractAlbumRepository mAlbumRepository;

    public DoChangeAlbumShowInOtherAlbum(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<Boolean> buildUseCaseFlowable(BaseOperationAlbumRequestBean baseOperationAlbumRequestBean) {
        if (baseOperationAlbumRequestBean == null || baseOperationAlbumRequestBean.getAlbumIds().length == 0) {
            return Flowable.error(new IllegalArgumentException("albumIds can't null"));
        }
        return this.mAlbumRepository.doChangeAlbumShowInOtherAlbumPage(baseOperationAlbumRequestBean.isEnable(), baseOperationAlbumRequestBean.getAlbumIds());
    }
}
