package com.miui.gallery.ui.album.hiddenalbum;

import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.ui.album.common.usecase.DoUnHideAlbumCase;
import com.miui.gallery.ui.album.hiddenalbum.usecase.QueryHiddenList;
import com.miui.gallery.ui.album.hiddenalbum.viewbean.HiddenAlbumItemViewBean;
import com.miui.gallery.util.DebugUtil;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.List;

/* loaded from: classes2.dex */
public class HiddenAlbumPresenter extends HiddenAlbumContract$P {
    private QueryHiddenList mSelectHiddenList;
    private DoUnHideAlbumCase mUnHideAlbum;

    @Override // com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumContract$P
    public void initData() {
        if (this.mSelectHiddenList == null) {
            this.mSelectHiddenList = new QueryHiddenList((AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY));
        }
        this.mSelectHiddenList.execute(new DisposableSubscriber<List<HiddenAlbumItemViewBean>>() { // from class: com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumPresenter.1
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(List<HiddenAlbumItemViewBean> list) {
                HiddenAlbumPresenter.this.getView().showHiddenListData(list);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                HiddenAlbumPresenter.this.getView().showHiddenListData(null);
            }
        }, null);
    }

    @Override // com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumContract$P
    public void unAlbumHide(final HiddenAlbumItemViewBean hiddenAlbumItemViewBean) {
        if (hiddenAlbumItemViewBean == null || hiddenAlbumItemViewBean.getId() <= 0) {
            return;
        }
        if (this.mUnHideAlbum == null) {
            this.mUnHideAlbum = new DoUnHideAlbumCase((AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY));
        }
        final long logEventTime = DebugUtil.logEventTime("operationTrace", "unhide_album", false);
        this.mUnHideAlbum.execute(new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumPresenter.2
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(Boolean bool) {
                if (bool.booleanValue()) {
                    HiddenAlbumPresenter.this.getView().showUnHiddenAlbumIsSuccess(hiddenAlbumItemViewBean);
                }
                DebugUtil.logEventTime("operationTrace", "unhide_album", logEventTime);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                HiddenAlbumPresenter.this.getView().showUnHiddenAlbumIsFailed();
            }
        }, Long.valueOf(hiddenAlbumItemViewBean.getId()));
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        super.onDestroy();
        DoUnHideAlbumCase doUnHideAlbumCase = this.mUnHideAlbum;
        if (doUnHideAlbumCase != null) {
            doUnHideAlbumCase.dispose();
        }
        QueryHiddenList queryHiddenList = this.mSelectHiddenList;
        if (queryHiddenList != null) {
            queryHiddenList.dispose();
        }
    }
}
