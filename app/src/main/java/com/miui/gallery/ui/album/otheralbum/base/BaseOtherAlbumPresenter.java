package com.miui.gallery.ui.album.otheralbum.base;

import android.net.Uri;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.base_optimization.mvp.view.IView;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$V;
import com.miui.gallery.ui.album.otheralbum.usecase.QueryOtherAlbumList;
import com.miui.gallery.ui.album.rubbishalbum.usecase.QueryRubbishTipViewCoverList;
import com.miui.gallery.util.CheckEmptyDataSubscriber;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseOtherAlbumPresenter<V extends BaseOtherAlbumContract$V> extends BaseOtherAlbumContract$P<V> {
    private static final String TAG = "BaseOtherAlbumPresenter";
    private boolean isHaveRubbish;
    public HotUseCase mQueryOtherAlbumList;
    public HotUseCase mQueryRubbishAlbumCoverList;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter, com.miui.gallery.base_optimization.mvp.presenter.IPresenter
    public /* bridge */ /* synthetic */ void onAttachView(IView iView) {
        onAttachView((BaseOtherAlbumPresenter<V>) ((BaseOtherAlbumContract$V) iView));
    }

    public void initUseCase(AbstractAlbumRepository abstractAlbumRepository) {
        this.mQueryOtherAlbumList = new QueryOtherAlbumList(abstractAlbumRepository);
        this.mQueryRubbishAlbumCoverList = new QueryRubbishTipViewCoverList(abstractAlbumRepository);
    }

    public void onAttachView(V v) {
        super.onAttachView((BaseOtherAlbumPresenter<V>) v);
        initUseCase(this.mAlbumRepository);
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$P
    public void initData() {
        queryOtherAlbum();
        queryRubbishAlbumCover();
    }

    public void queryOtherAlbum() {
        this.mQueryOtherAlbumList.executeWith(new CheckEmptyDataSubscriber<PageResults<List<CommonAlbumItemViewBean>>>() { // from class: com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter.1
            @Override // com.miui.gallery.util.CheckEmptyDataSubscriber
            public void onEvent(PageResults<List<CommonAlbumItemViewBean>> pageResults) {
                BaseOtherAlbumPresenter.this.dispatchAlbumDatas(pageResults);
            }

            @Override // com.miui.gallery.util.CheckEmptyDataSubscriber
            public void onEventEmpty(PageResults<List<CommonAlbumItemViewBean>> pageResults) {
                BaseOtherAlbumPresenter.this.dispatchAlbumDatas(pageResults);
            }
        }, null, ((BaseOtherAlbumContract$V) getView()).getLifecycle());
    }

    public void queryRubbishAlbumCover() {
        this.mQueryRubbishAlbumCoverList.executeWith(new DisposableSubscriber<PageResults<CoverList>>() { // from class: com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter.2
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(PageResults<CoverList> pageResults) {
                BaseOtherAlbumPresenter.this.isHaveRubbish = true;
                ((BaseOtherAlbumContract$V) BaseOtherAlbumPresenter.this.getView()).showRubbishAlbumResult(pageResults.getResult().getCovers());
            }
        }, 3, ((BaseOtherAlbumContract$V) getView()).getLifecycle());
    }

    public void dispatchAlbumDatas(PageResults<List<CommonAlbumItemViewBean>> pageResults) {
        ((BaseOtherAlbumContract$V) getView()).showOthersAlbumResult(pageResults.getResult());
        if (isHaveRubbish()) {
            ((BaseOtherAlbumContract$V) getView()).refreshRubbishTipView();
        }
    }

    public boolean isHaveRubbish() {
        return this.isHaveRubbish;
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$P
    public Uri getDownLoadUri(BaseAlbumCover baseAlbumCover) {
        return Album.getCoverUri(baseAlbumCover.coverSyncState, baseAlbumCover.coverId);
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        super.onDestroy();
        this.mQueryOtherAlbumList = null;
        this.mQueryRubbishAlbumCoverList = null;
    }
}
