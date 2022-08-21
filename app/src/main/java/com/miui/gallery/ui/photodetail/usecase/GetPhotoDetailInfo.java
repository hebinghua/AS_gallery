package com.miui.gallery.ui.photodetail.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.PhotoDetailInfo;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.ui.photodetail.PhotoDetailPresenter;
import com.miui.gallery.ui.photodetail.viewbean.PhotoDetailViewBean;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/* loaded from: classes2.dex */
public class GetPhotoDetailInfo extends BaseUseCase<PhotoDetailViewBean, BaseDataItem> {
    public AbstractCloudRepository mRepository;

    public GetPhotoDetailInfo(AbstractCloudRepository abstractCloudRepository) {
        this.mRepository = abstractCloudRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<PhotoDetailViewBean> buildUseCaseFlowable(BaseDataItem baseDataItem) {
        if (baseDataItem == null) {
            return Flowable.empty();
        }
        DefaultLogger.v(PhotoDetailPresenter.TAG, "getPhotoDetailInfo baseDataItem info: [%s]", baseDataItem.toString());
        return this.mRepository.getPhotoDetailInfo(baseDataItem).map(new Function<PhotoDetailInfo, PhotoDetailViewBean>() { // from class: com.miui.gallery.ui.photodetail.usecase.GetPhotoDetailInfo.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public PhotoDetailViewBean mo2564apply(PhotoDetailInfo photoDetailInfo) throws Exception {
                DefaultLogger.v(PhotoDetailPresenter.TAG, "getPhotoDetailInfo detailInfo : [%s]", photoDetailInfo.toString());
                PhotoDetailViewBean photoDetailViewBean = new PhotoDetailViewBean();
                photoDetailViewBean.mapping(photoDetailInfo);
                DefaultLogger.v(PhotoDetailPresenter.TAG, "getPhotoDetailInfo PhotoDetailViewBean : [%s]", photoDetailViewBean.toString());
                return photoDetailViewBean;
            }
        });
    }
}
