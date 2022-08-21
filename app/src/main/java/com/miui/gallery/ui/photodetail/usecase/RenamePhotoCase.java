package com.miui.gallery.ui.photodetail.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class RenamePhotoCase extends BaseUseCase<String, RequestBean> {
    public AbstractCloudRepository mRepository;

    public RenamePhotoCase(AbstractCloudRepository abstractCloudRepository) {
        this.mRepository = abstractCloudRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<String> buildUseCaseFlowable(RequestBean requestBean) {
        if (requestBean == null) {
            return Flowable.empty();
        }
        return this.mRepository.doRenamePhoto(requestBean.id, requestBean.path, requestBean.newName);
    }

    /* loaded from: classes2.dex */
    public static class RequestBean {
        public long id;
        public String newName;
        public String path;

        public RequestBean(String str, String str2, long j) {
            this.path = str;
            this.newName = str2;
            this.id = j;
        }
    }
}
