package com.miui.gallery.ui.photodetail.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class EditPhotoDateTime extends BaseUseCase<String, RequestBean> {
    public AbstractCloudRepository mRepository;

    public EditPhotoDateTime(AbstractCloudRepository abstractCloudRepository) {
        this.mRepository = abstractCloudRepository;
    }

    public Flowable<String> buildUseCaseFlowable(RequestBean requestBean) {
        if (requestBean == null) {
            return Flowable.empty();
        }
        return requestBean.id != 0 ? this.mRepository.doEditPhotoDateTime(requestBean.id, requestBean.newTime, requestBean.isFavorite) : this.mRepository.doEditPhotoDateTime(requestBean.path, requestBean.newTime, requestBean.isFavorite);
    }

    /* loaded from: classes2.dex */
    public static class RequestBean {
        public long id;
        public boolean isFavorite;
        public long newTime;
        public String path;

        public RequestBean(long j, long j2) {
            this.newTime = j;
            this.id = j2;
        }

        public long getNewTime() {
            return this.newTime;
        }

        public String getPath() {
            return this.path;
        }

        public void setFavorite(boolean z) {
            this.isFavorite = z;
        }
    }
}
