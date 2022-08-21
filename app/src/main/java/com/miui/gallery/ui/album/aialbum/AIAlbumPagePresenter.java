package com.miui.gallery.ui.album.aialbum;

import android.net.Uri;
import android.util.ArrayMap;
import android.util.Pair;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.trans.ItemModelTransManager;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.base_optimization.util.ExceptionUtils;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.IMedia;
import com.miui.gallery.ui.album.aialbum.usecase.QueryIgnorePeopleCoverList;
import com.miui.gallery.ui.album.aialbum.usecase.QueryLocationCoverList;
import com.miui.gallery.ui.album.aialbum.usecase.QueryPersonList;
import com.miui.gallery.ui.album.aialbum.usecase.QueryTagsCoverList;
import com.miui.gallery.ui.album.aialbum.viewbean.LocationAndTagsAlbumItemViewBean;
import com.miui.gallery.ui.album.aialbum.viewbean.TagsAlbumItemViewBean;
import com.miui.gallery.ui.album.common.GroupDatasResult;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.viewbean.MapAlbumViewBean;
import com.miui.gallery.ui.album.main.viewbean.ai.PeopleFaceAlbumViewBean;
import com.miui.gallery.ui.album.picker.CrossUserAlbumRepositoryModelInstance;
import com.miui.gallery.ui.album.picker.PickerPageUtils;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import com.miui.gallery.util.thread.ThreadManager;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class AIAlbumPagePresenter extends AIAlbumPageContract$P {
    private static final int[] EMPTY_IDS = {2147483646, 2147483645, 2147483644, 2147483643};
    private static final String TAG = "AIAlbumPagePresenter";
    private MapAlbumViewBean mEmptyMapAlbumViewBean;
    private GroupDatasResult mGroupResults;
    private BaseViewBean mLocationEmptyViewBean;
    private int mLocationTypeItemStartPosition;
    private MapAlbumViewBean mMapAlbumViewBean;
    private LibraryLoaderHelper.OnLibraryLoadListener mMapLibraryLoadListener;
    private BaseViewBean mPeopleEmptyViewBean;
    private BaseUseCase mQueryIgnorePeopleCoverList;
    private BaseUseCase mQueryLocationCoverList;
    private HotUseCase mQueryPeopleCoverList;
    private BaseUseCase mQueryTagsCoverList;
    private Subject<Pair<List<BaseViewBean>, List<EpoxyModel<?>>>> mRefreshRubbishSubject;
    private BaseViewBean mTagEmptyViewBean;
    private int mTagTypeItemStartPosition;
    private String[] mTypeSort;
    private boolean isFirst = true;
    private ArrayMap<String, List<BaseViewBean>> mResults = new ArrayMap<>(2);
    private final Object mWriteLock = new Object();

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter, com.miui.gallery.base_optimization.mvp.presenter.IPresenter
    public void onAttachView(AIAlbumPageContract$V aIAlbumPageContract$V) {
        super.onAttachView((AIAlbumPagePresenter) aIAlbumPageContract$V);
        checkSortOrder();
        initUseCases((AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY));
    }

    public void initUseCases(AbstractAlbumRepository abstractAlbumRepository) {
        if (PickerPageUtils.isCrossUserPick(getView())) {
            abstractAlbumRepository = (AbstractAlbumRepository) ModelManager.getInstance().getModel(AbstractAlbumRepository.class, new CrossUserAlbumRepositoryModelInstance(getView().getSafeActivity()));
            this.mGroupResults = new GroupDatasResult(Arrays.asList("ai_cover_face"));
        } else {
            this.mQueryLocationCoverList = new QueryLocationCoverList(abstractAlbumRepository);
            this.mQueryTagsCoverList = new QueryTagsCoverList(abstractAlbumRepository);
            this.mGroupResults = new GroupDatasResult(Arrays.asList("ai_cover_face", "ai_cover_map", "ai_cover_location", "ai_cover_tag"));
        }
        this.mQueryPeopleCoverList = new QueryPersonList(abstractAlbumRepository);
        this.mQueryIgnorePeopleCoverList = new QueryIgnorePeopleCoverList(abstractAlbumRepository);
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$P
    public void initData() {
        PublishSubject create = PublishSubject.create();
        this.mRefreshRubbishSubject = create;
        addDisposeDelay((Disposable) create.observeOn(RxGalleryExecutors.getInstance().getUiThreadScheduler()).subscribeWith(new DisposableObserver<Pair<List<BaseViewBean>, List<EpoxyModel<?>>>>() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPagePresenter.1
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onNext(Pair<List<BaseViewBean>, List<EpoxyModel<?>>> pair) {
                AIAlbumPagePresenter.this.getView().showPageDatas((List) pair.first, (List) pair.second);
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                TimeMonitor.cancelTimeMonitor("403.16.0.1.13782");
                DefaultLogger.e(AIAlbumPagePresenter.TAG, ExceptionUtils.getStackTraceString(th));
            }
        }));
        internalInitEmptyAlbumCoverBean();
        internalQueryFaceAlbum();
        internalQueryTagsAlbum();
        internalQueryLocationsAlbum();
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$P
    public void generateMapAlbums(List<IMedia> list) {
        MapAlbumViewBean mapAlbumViewBean = new MapAlbumViewBean();
        this.mMapAlbumViewBean = mapAlbumViewBean;
        mapAlbumViewBean.mapping(list);
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mMapAlbumViewBean);
        dispatchDatas("ai_cover_map", arrayList);
    }

    private void internalInitEmptyAlbumCoverBean() {
        PeopleFaceAlbumViewBean peopleFaceAlbumViewBean = new PeopleFaceAlbumViewBean();
        this.mPeopleEmptyViewBean = peopleFaceAlbumViewBean;
        int[] iArr = EMPTY_IDS;
        peopleFaceAlbumViewBean.setId(iArr[0]);
        LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean = new LocationAndTagsAlbumItemViewBean();
        this.mLocationEmptyViewBean = locationAndTagsAlbumItemViewBean;
        locationAndTagsAlbumItemViewBean.setIntentActionURI(GalleryContract.Search.URI_LOCATION_LIST_PAGE.toString());
        this.mLocationEmptyViewBean.setId(iArr[1]);
        LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean2 = new LocationAndTagsAlbumItemViewBean();
        this.mTagEmptyViewBean = locationAndTagsAlbumItemViewBean2;
        locationAndTagsAlbumItemViewBean2.setIntentActionURI(GalleryContract.Search.URI_TAG_LIST_PAGE.toString());
        this.mTagEmptyViewBean.setId(iArr[2]);
        MapAlbumViewBean mapAlbumViewBean = new MapAlbumViewBean();
        this.mEmptyMapAlbumViewBean = mapAlbumViewBean;
        mapAlbumViewBean.setId(iArr[3]);
        this.mEmptyMapAlbumViewBean.setAlbumName(ResourceUtils.getString(R.string.map_album));
        this.mEmptyMapAlbumViewBean.setCovers(Collections.EMPTY_LIST);
        this.mEmptyMapAlbumViewBean.mapping((List<IMedia>) null);
        this.mEmptyMapAlbumViewBean.setIntentActionURI(GalleryContract.Common.URI_MAP_ALNBUM.toString());
    }

    private void internalQueryLocationsAlbum() {
        BaseUseCase baseUseCase = this.mQueryLocationCoverList;
        if (baseUseCase == null) {
            return;
        }
        if (!baseUseCase.isDispose()) {
            this.mQueryLocationCoverList.disposeAndAgain();
        }
        this.mQueryLocationCoverList.executeWith(new DisposableSubscriber<List<LocationAndTagsAlbumItemViewBean>>() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPagePresenter.2
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(List<LocationAndTagsAlbumItemViewBean> list) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (list.size() >= AIAlbumPagePresenter.this.getView().getLocationAlbumLoadNumber()) {
                    list.get(list.size() - 1).setNeedShowMoreStyle(true);
                }
                AIAlbumPagePresenter.this.dispatchDatas("ai_cover_location", list);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e(AIAlbumPagePresenter.TAG, ExceptionUtils.getStackTraceString(th));
            }
        }, Integer.valueOf(getView().getLocationAlbumLoadNumber()), getView().getLifecycle());
    }

    private void internalQueryTagsAlbum() {
        BaseUseCase baseUseCase = this.mQueryTagsCoverList;
        if (baseUseCase == null) {
            return;
        }
        if (!baseUseCase.isDispose()) {
            this.mQueryTagsCoverList.disposeAndAgain();
        }
        this.mQueryTagsCoverList.executeWith(new DisposableSubscriber<List<TagsAlbumItemViewBean>>() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPagePresenter.3
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(List<TagsAlbumItemViewBean> list) {
                if (list != null && list.size() >= AIAlbumPagePresenter.this.getView().getTagAlbumLoadNumber()) {
                    list.get(AIAlbumPagePresenter.this.getView().getTagAlbumLoadNumber() - 1).setNeedShowMoreStyle(true);
                }
                AIAlbumPagePresenter.this.dispatchDatas("ai_cover_tag", list);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e(AIAlbumPagePresenter.TAG, ExceptionUtils.getStackTraceString(th));
            }
        }, Integer.valueOf(getView().getTagAlbumLoadNumber()), getView().getLifecycle());
    }

    private void internalQueryFaceAlbum() {
        if (!this.mQueryPeopleCoverList.isDispose()) {
            this.mQueryPeopleCoverList.disposeAndAgain();
        }
        this.mQueryPeopleCoverList.executeWith(new DisposableSubscriber<List<PeopleFaceAlbumViewBean>>() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPagePresenter.4
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(List<PeopleFaceAlbumViewBean> list) {
                if (list != null && list.size() >= AIAlbumPagePresenter.this.getView().getFaceAlbumLoadNumber()) {
                    list.get(AIAlbumPagePresenter.this.getView().getFaceAlbumLoadNumber() - 1).setNeedShowMoreStyle(true);
                }
                AIAlbumPagePresenter.this.dispatchDatas("ai_cover_face", list);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e(AIAlbumPagePresenter.TAG, ExceptionUtils.getStackTraceString(th));
            }
        }, Integer.valueOf(getView().getFaceAlbumLoadNumber()), getView().getLifecycle());
        this.mQueryIgnorePeopleCoverList.executeWith(new DisposableSubscriber<List<PeopleFaceAlbumViewBean>>() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPagePresenter.5
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(List<PeopleFaceAlbumViewBean> list) {
                int indexOfKey;
                if (list.isEmpty() || (indexOfKey = AIAlbumPagePresenter.this.mResults.indexOfKey("ai_cover_face")) < 0 || ((List) AIAlbumPagePresenter.this.mResults.valueAt(indexOfKey)).size() != 0) {
                    return;
                }
                list.clear();
                list.add(new PeopleFaceAlbumViewBean());
                AIAlbumPagePresenter.this.dispatchDatas("ai_cover_face", list);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e(AIAlbumPagePresenter.TAG, ExceptionUtils.getStackTraceString(th));
            }
        }, 1, getView().getLifecycle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchDatas(final String str, final List<? extends BaseViewBean> list) {
        if (this.isFirst) {
            getView().loadPageDatasIsSuccess();
            this.isFirst = false;
        }
        ThreadManager.execute(79, new Runnable() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPagePresenter.6
            @Override // java.lang.Runnable
            public void run() {
                String[] strArr;
                Pair pair;
                synchronized (AIAlbumPagePresenter.this.mWriteLock) {
                    AIAlbumPagePresenter.this.mGroupResults.addOrUpdateGroupDatas(str, list);
                    for (String str2 : AIAlbumPagePresenter.this.mTypeSort) {
                        List groupDatas = AIAlbumPagePresenter.this.mGroupResults.getGroupDatas(str2);
                        if (str2.equals("ai_cover_face")) {
                            pair = new Pair(AIAlbumPagePresenter.this.mPeopleEmptyViewBean, AIAlbumPagePresenter.this.getView().generatePeopleGroupTitle());
                        } else {
                            pair = str2.equals("ai_cover_map") ? new Pair(AIAlbumPagePresenter.this.mEmptyMapAlbumViewBean, AIAlbumPagePresenter.this.getView().generateLocationGroupTitle()) : str2.equals("ai_cover_location") ? new Pair(AIAlbumPagePresenter.this.mLocationEmptyViewBean, AIAlbumPagePresenter.this.getView().generateLocationGroupTitle()) : new Pair(AIAlbumPagePresenter.this.mTagEmptyViewBean, AIAlbumPagePresenter.this.getView().generateTagsGroupTitle());
                        }
                        if (groupDatas == null || groupDatas.isEmpty()) {
                            AIAlbumPagePresenter.this.mGroupResults.addOrUpdateGroupItem(str2, pair.first, false);
                        }
                        if (!AIAlbumPagePresenter.this.getView().isMapAlbumAvailable()) {
                            AIAlbumPagePresenter.this.mGroupResults.addGroupGapDecorator(str2, 1, pair.second);
                        } else if (!str2.equals("ai_cover_location")) {
                            AIAlbumPagePresenter.this.mGroupResults.addGroupGapDecorator(str2, 1, pair.second);
                        }
                    }
                    List datas = AIAlbumPagePresenter.this.mGroupResults.getDatas(true);
                    if (AIAlbumPagePresenter.this.mGroupResults.containsKey("ai_cover_face")) {
                        AIAlbumPagePresenter aIAlbumPagePresenter = AIAlbumPagePresenter.this;
                        aIAlbumPagePresenter.mLocationTypeItemStartPosition = aIAlbumPagePresenter.mGroupResults.getGroupDatas("ai_cover_face").size();
                        if (AIAlbumPagePresenter.this.mGroupResults.containsKey("ai_cover_location")) {
                            AIAlbumPagePresenter aIAlbumPagePresenter2 = AIAlbumPagePresenter.this;
                            aIAlbumPagePresenter2.mTagTypeItemStartPosition = aIAlbumPagePresenter2.mLocationTypeItemStartPosition + AIAlbumPagePresenter.this.mGroupResults.getGroupDatas("ai_cover_location").size();
                        }
                    }
                    List list2 = (List) datas.parallelStream().map(new Function<BaseViewBean, EpoxyModel<?>>() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPagePresenter.6.1
                        @Override // java.util.function.Function
                        public EpoxyModel<?> apply(BaseViewBean baseViewBean) {
                            return ItemModelTransManager.getInstance().transDataToModel(baseViewBean);
                        }
                    }).collect(Collectors.toList());
                    if (list2.size() != 0) {
                        AIAlbumPagePresenter.this.mRefreshRubbishSubject.onNext(new Pair(datas, list2));
                    }
                }
            }
        });
    }

    private void checkSortOrder() {
        if (getView().isMapAlbumAvailable()) {
            this.mTypeSort = new String[]{"ai_cover_face", "ai_cover_map", "ai_cover_location", "ai_cover_tag"};
        } else {
            this.mTypeSort = new String[]{"ai_cover_face", "ai_cover_location", "ai_cover_tag"};
        }
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$P
    public Uri getLocationOrTagsIntentUri(Object obj) {
        if (obj instanceof LocationAndTagsAlbumItemViewBean) {
            LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean = (LocationAndTagsAlbumItemViewBean) obj;
            if (locationAndTagsAlbumItemViewBean.isMoreStyle()) {
                TrackController.trackClick(obj instanceof TagsAlbumItemViewBean ? "403.16.3.1.11251" : "403.16.2.1.11249", AutoTracking.getRef());
                return locationAndTagsAlbumItemViewBean.getMoreActionUri();
            }
            if (obj instanceof TagsAlbumItemViewBean) {
                TimeMonitor.createNewTimeMonitor("403.19.0.1.13788");
                TrackController.trackClick("403.16.3.1.11250", AutoTracking.getRef());
            } else {
                TimeMonitor.createNewTimeMonitor("403.18.0.1.13787");
                TrackController.trackClick("403.16.2.1.11248", AutoTracking.getRef());
            }
            return Uri.parse(locationAndTagsAlbumItemViewBean.getIntentActionURI());
        }
        return null;
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$P
    public int getLocationTypeItemStartPosition() {
        return this.mLocationTypeItemStartPosition;
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$P
    public int getTagTypeItemStartPosition() {
        return this.mTagTypeItemStartPosition;
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        super.onDestroy();
        this.mQueryLocationCoverList = null;
        this.mQueryPeopleCoverList = null;
        this.mQueryTagsCoverList = null;
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$P
    public void onConfigurationChanged() {
        internalQueryFaceAlbum();
        internalQueryLocationsAlbum();
        internalQueryTagsAlbum();
    }
}
