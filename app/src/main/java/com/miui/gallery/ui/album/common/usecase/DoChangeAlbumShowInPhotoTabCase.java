package com.miui.gallery.ui.album.common.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.ui.album.common.base.requestbean.BaseOperationAlbumRequestBean;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class DoChangeAlbumShowInPhotoTabCase extends BaseUseCase<Boolean, BaseOperationAlbumRequestBean> {
    public AbstractAlbumRepository mAlbumRepository;

    public DoChangeAlbumShowInPhotoTabCase(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<Boolean> buildUseCaseFlowable(BaseOperationAlbumRequestBean baseOperationAlbumRequestBean) {
        if (baseOperationAlbumRequestBean == null) {
            return getArgumentNotNullError();
        }
        return this.mAlbumRepository.doChangeAlbumShowInPhotoTabPage(baseOperationAlbumRequestBean.isEnable(), baseOperationAlbumRequestBean.getFirstAlbumId());
    }
}
