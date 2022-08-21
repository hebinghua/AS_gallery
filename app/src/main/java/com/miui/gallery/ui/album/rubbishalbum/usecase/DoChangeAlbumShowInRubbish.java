package com.miui.gallery.ui.album.rubbishalbum.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.ui.album.common.base.requestbean.BaseOperationAlbumRequestBean;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class DoChangeAlbumShowInRubbish extends BaseUseCase<Boolean, BaseOperationAlbumRequestBean> {
    public AbstractAlbumRepository mAlbumRepository;

    public DoChangeAlbumShowInRubbish(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<Boolean> buildUseCaseFlowable(BaseOperationAlbumRequestBean baseOperationAlbumRequestBean) {
        return this.mAlbumRepository.doChangeAlbumShowInRubbishPage(baseOperationAlbumRequestBean.isEnable(), baseOperationAlbumRequestBean.getAlbumIds());
    }
}
