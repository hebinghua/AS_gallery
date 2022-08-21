package com.miui.gallery.ui.album.cloudalbum;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.share.ShareAlbumCacheManager;
import com.miui.gallery.ui.album.cloudalbum.usecase.QueryCloudAlbumList;
import com.miui.gallery.ui.album.cloudalbum.viewbean.CloudAlbumItemViewBean;
import com.miui.gallery.ui.album.common.base.requestbean.BaseOperationAlbumRequestBean;
import com.miui.gallery.ui.album.common.usecase.DoChangeAlbumBackupCase;
import com.miui.gallery.ui.album.common.usecase.QueryShareAlbumCase;
import com.miui.gallery.util.DebugUtil;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.List;

/* loaded from: classes2.dex */
public class CloudAlbumListPresenter extends CloudAlbumListContract$P {
    private BaseUseCase mDoChangeAlbumBackUp;
    private BaseUseCase mQueryCloudAlbumList;
    private BaseUseCase mQueryShareAlbumDetailInfo;

    public CloudAlbumListPresenter() {
        AbstractAlbumRepository abstractAlbumRepository = (AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY);
        this.mQueryCloudAlbumList = new QueryCloudAlbumList(abstractAlbumRepository);
        this.mQueryShareAlbumDetailInfo = new QueryShareAlbumCase(abstractAlbumRepository);
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$P
    public void initData() {
        initShareAlbumDetailInfoIfNeed();
        this.mQueryCloudAlbumList.execute(new DisposableSubscriber<List<CloudAlbumItemViewBean>>() { // from class: com.miui.gallery.ui.album.cloudalbum.CloudAlbumListPresenter.1
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(List<CloudAlbumItemViewBean> list) {
                CloudAlbumListPresenter.this.getView().showCloudList(list);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                CloudAlbumListPresenter.this.getView().showCloudList(null);
            }
        }, null);
    }

    private void initShareAlbumDetailInfoIfNeed() {
        if (ShareAlbumCacheManager.getInstance().getShareAlbumList() == null) {
            this.mQueryShareAlbumDetailInfo.execute(new DisposableSubscriber<List>() { // from class: com.miui.gallery.ui.album.cloudalbum.CloudAlbumListPresenter.2
                @Override // org.reactivestreams.Subscriber
                public void onComplete() {
                }

                @Override // org.reactivestreams.Subscriber
                public void onError(Throwable th) {
                }

                @Override // org.reactivestreams.Subscriber
                public void onNext(List list) {
                }
            }, null);
        }
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$P
    public CloudAlbumItemViewBean converterTagBeanToCloudAlbumItemItemViewBean(Object obj) {
        if (obj != null && (obj instanceof CloudAlbumItemViewBean)) {
            return (CloudAlbumItemViewBean) obj;
        }
        return null;
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$P
    public void doChangeAlbumUploadStatus(Object obj, final boolean z) {
        final long logEventTime = DebugUtil.logEventTime("operationTrace", z ? "auto_upload_enable" : "auto_upload_disable", false);
        final CloudAlbumItemViewBean converterTagBeanToCloudAlbumItemItemViewBean = converterTagBeanToCloudAlbumItemItemViewBean(obj);
        if (converterTagBeanToCloudAlbumItemItemViewBean == null) {
            getView().showChangeAlbumUploadStatusFailed(converterTagBeanToCloudAlbumItemItemViewBean);
        } else if (Album.isShareAlbum(converterTagBeanToCloudAlbumItemItemViewBean.getId()) || converterTagBeanToCloudAlbumItemItemViewBean.isShareToDevice()) {
            getView().showChangeAlbumCantBeShareAlbum(converterTagBeanToCloudAlbumItemItemViewBean);
        } else if (Album.isRubbishAlbum(((Album) converterTagBeanToCloudAlbumItemItemViewBean.getSource()).getAttributes())) {
            getView().showChangeAlbumCantBeRubbishAlbum(converterTagBeanToCloudAlbumItemItemViewBean);
        } else {
            if (this.mDoChangeAlbumBackUp == null) {
                this.mDoChangeAlbumBackUp = new DoChangeAlbumBackupCase((AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY));
            }
            this.mDoChangeAlbumBackUp.execute(new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.cloudalbum.CloudAlbumListPresenter.3
                @Override // org.reactivestreams.Subscriber
                public void onComplete() {
                }

                @Override // org.reactivestreams.Subscriber
                public void onNext(Boolean bool) {
                    if (bool.booleanValue()) {
                        CloudAlbumListPresenter.this.getView().showChangeAlbumUploadStatusSuccess(converterTagBeanToCloudAlbumItemItemViewBean, z);
                    } else {
                        CloudAlbumListPresenter.this.getView().showChangeAlbumUploadStatusFailed(converterTagBeanToCloudAlbumItemItemViewBean);
                    }
                    DebugUtil.logEventTime("operationTrace", z ? "auto_upload_enable" : "auto_upload_disable", logEventTime);
                }

                @Override // org.reactivestreams.Subscriber
                public void onError(Throwable th) {
                    CloudAlbumListPresenter.this.getView().showChangeAlbumUploadStatusFailed(converterTagBeanToCloudAlbumItemItemViewBean);
                }
            }, new BaseOperationAlbumRequestBean(converterTagBeanToCloudAlbumItemItemViewBean.getId(), z));
        }
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$P
    public boolean isShareAlbum(Object obj) {
        if (converterTagBeanToCloudAlbumItemItemViewBean(obj) == null) {
            return false;
        }
        return isShareAlbum((CloudAlbumItemViewBean) obj);
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$P
    public boolean isShareAlbum(CloudAlbumItemViewBean cloudAlbumItemViewBean) {
        if (cloudAlbumItemViewBean == null) {
            return false;
        }
        return Album.isShareAlbum(cloudAlbumItemViewBean.getId());
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        super.onDestroy();
        BaseUseCase baseUseCase = this.mQueryCloudAlbumList;
        if (baseUseCase != null) {
            baseUseCase.dispose();
        }
        BaseUseCase baseUseCase2 = this.mDoChangeAlbumBackUp;
        if (baseUseCase2 != null) {
            baseUseCase2.dispose();
        }
    }
}
