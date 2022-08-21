package com.miui.gallery.ui.album.main;

import android.text.TextUtils;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.base_optimization.clean.LifecycleUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.album.common.DefaultViewBeanFactory;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.usecase.DoChangeSortPositionCase;
import com.miui.gallery.ui.album.main.usecase.DoChangeSortTypeCaseByAlbumTabSceneCase;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import com.miui.gallery.util.SimpleDisposableSubscriber;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.itemdrag.RecyclerViewDragItemManager;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumTabPresenter extends AlbumTabContract$P {
    private static final String TAG = "AlbumTabPresenter";
    private boolean isPending;
    private LifecycleUseCase mChangeAlbumSort;
    private int mFirstVisibleItemPosition;

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter
    public void initUseCases(AbstractAlbumRepository abstractAlbumRepository) {
        super.initUseCases(abstractAlbumRepository);
        this.mChangeAlbumSort = new DoChangeSortPositionCase(abstractAlbumRepository);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter, com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public void initPart() {
        super.initPart();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter, com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public void initAll() {
        super.initAll();
        ((AlbumTabContract$V) getView()).openDragMode(isEnableDragMode());
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter, com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public boolean isNeedLoadAdvanceOtherAlbum() {
        return GalleryPreferences.Album.isFixedAlbumAlreadySetter(((AlbumTabContract$V) getView()).getOtherAlbumId());
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$P
    public void onStartChoiceMode() {
        doChangeDataPendingStatus(true);
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$P
    public void onStopChoiceMode() {
        doChangeDataPendingStatus(false);
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$P
    public void doChangeDataPendingStatus(boolean z) {
        int i = 0;
        if (z) {
            this.isPending = true;
            HotUseCase[] hotUseCaseArr = this.mHotUseCases;
            int length = hotUseCaseArr.length;
            while (i < length) {
                hotUseCaseArr[i].onStop();
                i++;
            }
        } else if (!this.isPending || ((AlbumTabContract$V) getView()).isInMoveMode() || ((AlbumTabContract$V) getView()).isInChoiceMode()) {
        } else {
            this.isPending = false;
            HotUseCase[] hotUseCaseArr2 = this.mHotUseCases;
            int length2 = hotUseCaseArr2.length;
            while (i < length2) {
                hotUseCaseArr2[i].onStart();
                i++;
            }
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter, com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumSortType(List<BaseViewBean> list, int i) {
        if (isEnableDragMode()) {
            DefaultLogger.d(TAG, "启用拖拽模式");
            ((AlbumTabContract$V) getView()).openItemSwapWhenDragMode();
        } else {
            DefaultLogger.d(TAG, "关闭拖拽模式");
            ((AlbumTabContract$V) getView()).closeItemSwapWhenDragMode();
        }
        this.mFirstVisibleItemPosition = ((AlbumTabContract$V) getView()).getCurrentListVisiblePosition();
        new DoChangeSortTypeCaseByAlbumTabSceneCase().executeWith(new SimpleDisposableSubscriber<AlbumDataListResult>() { // from class: com.miui.gallery.ui.album.main.AlbumTabPresenter.1
            @Override // org.reactivestreams.Subscriber
            public void onNext(AlbumDataListResult albumDataListResult) {
                ((AlbumTabContract$V) AlbumTabPresenter.this.getView()).onChangeAlbumSortTypeSuccess(null);
                AlbumTabPresenter albumTabPresenter = AlbumTabPresenter.this;
                albumTabPresenter.dispatchAlbumDatas(albumDataListResult, albumTabPresenter.mFirstVisibleItemPosition == 0);
                AlbumTabPresenter.this.mFirstVisibleItemPosition = -1;
            }
        }, new DoChangeSortTypeCaseByAlbumTabSceneCase.RequestParam(getQueryAllAlbumsLoadComplateListener(), list), ((AlbumTabContract$V) getView()).getLifecycle());
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$P
    public void doChangeSortPosition(DoChangeSortPositionCase.Param param, DisposableSubscriber<Boolean> disposableSubscriber) {
        LifecycleUseCase lifecycleUseCase = this.mChangeAlbumSort;
        if (lifecycleUseCase != null) {
            lifecycleUseCase.executeWith(disposableSubscriber, param, ((AlbumTabContract$V) getView()).getLifecycle());
        }
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$P
    public RecyclerViewDragItemManager.OnDragCallback getDragItemTouchCallback() {
        return this.mComponentInfo.getDragItemTouchCallback();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter, com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter, com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void addAlbum(Album album) {
        if (album == null) {
            DefaultLogger.e(TAG, "addAlbum to memory error,args is null");
            return;
        }
        String groupType = AlbumSplitGroupHelper.getSplitGroupMode().getGroupType(album);
        if (TextUtils.isEmpty(groupType)) {
            DefaultLogger.e(TAG, "addAlbum to memory error,can;t get groupType");
            return;
        }
        this.mLastAlbumDataResult.addGroupItem(groupType, -1, DefaultViewBeanFactory.getInstance().factory(album), false);
    }
}
