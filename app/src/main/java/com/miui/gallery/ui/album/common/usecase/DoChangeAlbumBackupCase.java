package com.miui.gallery.ui.album.common.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.ui.album.common.base.requestbean.BaseOperationAlbumRequestBean;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class DoChangeAlbumBackupCase extends BaseUseCase<Boolean, BaseOperationAlbumRequestBean> {
    public AbstractAlbumRepository mRepository;

    public DoChangeAlbumBackupCase(AbstractAlbumRepository abstractAlbumRepository) {
        this.mRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<Boolean> buildUseCaseFlowable(BaseOperationAlbumRequestBean baseOperationAlbumRequestBean) {
        return this.mRepository.doChangeAlbumBackupStatus(baseOperationAlbumRequestBean.isEnable(), baseOperationAlbumRequestBean.getFirstAlbumId());
    }
}
