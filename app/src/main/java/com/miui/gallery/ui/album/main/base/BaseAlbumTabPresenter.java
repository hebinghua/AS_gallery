package com.miui.gallery.ui.album.main.base;

import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.base_optimization.clean.LifecycleUseCase;
import com.miui.gallery.base_optimization.mvp.view.IView;
import com.miui.gallery.base_optimization.util.ExceptionUtils;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.ui.album.aialbum.usecase.QueryMediaTypeGroupCase;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import com.miui.gallery.ui.album.common.MediaGroupTypeViewBean;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.common.usecase.QueryShareAlbumCase;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$V;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase;
import com.miui.gallery.ui.album.main.usecase.QueryAlbumsByAlbumTabScene;
import com.miui.gallery.ui.album.main.usecase.QueryOtherAlbumCovers;
import com.miui.gallery.ui.album.main.usecase.QuerySnapAlbumList;
import com.miui.gallery.ui.album.main.usecase.QueryTrashBinCase;
import com.miui.gallery.ui.album.main.usecase.ScanCleanerCase;
import com.miui.gallery.ui.album.main.usecase.SearchStatusListenerUsecase;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.ui.album.main.utils.splitgroup.IAlbumPageComponentVersion;
import com.miui.gallery.ui.album.main.viewbean.AIAlbumGridCoverViewBean;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import com.miui.gallery.ui.album.main.viewbean.OtherAlbumGridCoverViewBean;
import com.miui.gallery.ui.album.rubbishalbum.usecase.QueryRubbishAlbum;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.SimpleDisposableSubscriber;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import miuix.core.util.FileUtils;

/* loaded from: classes2.dex */
public class BaseAlbumTabPresenter<V extends BaseAlbumTabContract$V> extends BaseAlbumTabContract$P<V> {
    private static final String TAG = "BaseAlbumTabPresenter";
    public AIAlbumGridCoverViewBean mAIAlbumBean;
    public AlbumTabToolItemBean mCleanerBean;
    public IAlbumPageComponentVersion mComponentInfo;
    public HotUseCase[] mHotUseCases;
    public AlbumDataListResult mLastAlbumDataResult;
    public List<MediaGroupTypeViewBean> mMediaTypeGroupBeans;
    private List<AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback> mOnDataProcessingListeners;
    public OtherAlbumGridCoverViewBean mOtherAlbumBean;
    private HotUseCase mQueryAllAlbumList;
    private HotUseCase mQueryAndGenerateAIAlbumsCover;
    private HotUseCase mQueryAndGeneratorOtherAlbumsCover;
    private HotUseCase mQueryMediaType;
    private HotUseCase mQueryRubbishAlbum;
    private LifecycleUseCase mQueryShareAlbumDetailInfo;
    private LifecycleUseCase mQuerySnapAlbumList;
    private HotUseCase mQueryTrashBin;
    public AlbumTabToolItemBean mRubbishAlbumBean;
    private HotUseCase mScanCleanerCase;
    private LifecycleUseCase mSearchStatusListenerUsecase;
    public AlbumTabToolItemBean mTrashBinBean;
    public boolean isEnableAIAlbum = AccountCache.isHaveAccount();
    public boolean isEnableTrashAlbum = isCanShowTrashAlbum();
    private boolean isNeedSrollToHead = true;
    public int mLastSearchStatus = -1;

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public boolean isNeedLoadAdvanceAIAlbum() {
        return true;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public boolean isNeedLoadAdvanceOtherAlbum() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter, com.miui.gallery.base_optimization.mvp.presenter.IPresenter
    public /* bridge */ /* synthetic */ void onAttachView(IView iView) {
        onAttachView((BaseAlbumTabPresenter<V>) ((BaseAlbumTabContract$V) iView));
    }

    public BaseAlbumTabPresenter() {
        IAlbumPageComponentVersion component = AlbumPageConfig.getInstance().getComponent(this);
        this.mComponentInfo = component;
        addOnDataProcessingCallback(component.getQueryAllAlbumsLoadComplateListener());
    }

    public void onAttachView(V v) {
        super.onAttachView((BaseAlbumTabPresenter<V>) v);
        initUseCases(this.mAlbumRepository);
    }

    public void initUseCases(AbstractAlbumRepository abstractAlbumRepository) {
        this.mQuerySnapAlbumList = new QuerySnapAlbumList(abstractAlbumRepository);
        this.mQueryAndGenerateAIAlbumsCover = new QueryAIAlbumCase(abstractAlbumRepository);
        this.mQueryAndGeneratorOtherAlbumsCover = new QueryOtherAlbumCovers(abstractAlbumRepository);
        this.mQueryAllAlbumList = new QueryAlbumsByAlbumTabScene(abstractAlbumRepository);
        this.mQueryShareAlbumDetailInfo = new QueryShareAlbumCase(abstractAlbumRepository);
        this.mQueryRubbishAlbum = new QueryRubbishAlbum(abstractAlbumRepository);
        this.mSearchStatusListenerUsecase = new SearchStatusListenerUsecase(GalleryApp.sGetAndroidContext(), null);
        this.mQueryMediaType = new QueryMediaTypeGroupCase((AbstractCloudRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.CLOUD_REPOSITORY));
        this.mQueryTrashBin = new QueryTrashBinCase(abstractAlbumRepository);
        ScanCleanerCase scanCleanerCase = new ScanCleanerCase();
        this.mScanCleanerCase = scanCleanerCase;
        this.mHotUseCases = new HotUseCase[]{this.mQueryAndGenerateAIAlbumsCover, this.mQueryAndGeneratorOtherAlbumsCover, this.mQueryAllAlbumList, this.mQueryRubbishAlbum, this.mQueryMediaType, this.mQueryTrashBin, scanCleanerCase};
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public boolean isEnableAlbumById(int i) {
        switch (i) {
            case 2147483638:
                return this.isEnableTrashAlbum;
            case 2147483639:
                return this.isEnableAIAlbum;
            case 2147483640:
            default:
                return false;
            case 2147483641:
                return true;
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public void setEnableAlbumById(int i, boolean z) {
        switch (i) {
            case 2147483638:
                if (!this.isEnableTrashAlbum && z) {
                    queryTrashBin();
                }
                this.isEnableTrashAlbum = z;
                return;
            case 2147483639:
                if (!this.isEnableAIAlbum && z) {
                    queryAIAlbumCover();
                }
                this.isEnableAIAlbum = z;
                return;
            default:
                return;
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public void initPart() {
        if (!BaseBuildUtil.isLowRamDevice()) {
            querySnapAlbums();
            if (isNeedLoadAdvanceAIAlbum() && isEnableAIAlbum()) {
                queryAIAlbumCover();
            }
            if (!isNeedLoadAdvanceOtherAlbum()) {
                return;
            }
            queryOtherAlbumCover();
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public void initAll() {
        if ((!isNeedLoadAdvanceAIAlbum() || BaseBuildUtil.isLowRamDevice()) && isEnableAIAlbum()) {
            queryAIAlbumCover();
        }
        initShareAlbumsDetailInfo();
        queryAlbums();
        queryTrashBin();
        queryRubbishAlbum();
        queryCleaner();
        if (!isNeedLoadAdvanceOtherAlbum() || BaseBuildUtil.isLowRamDevice()) {
            queryOtherAlbumCover();
        }
        queryMediaGroup();
    }

    public boolean isEnableAIAlbum() {
        return isEnableAlbumById(2147483639);
    }

    public boolean isEnableTrashAlbum() {
        return isEnableAlbumById(2147483638);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public List<MediaGroupTypeViewBean> getMediaTypeGroups() {
        return this.mMediaTypeGroupBeans;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public BaseViewBean getTrashAlbumBean() {
        return this.mTrashBinBean;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public BaseViewBean getAIAlbumBean() {
        return this.mAIAlbumBean;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public BaseViewBean getOtherAlbumBean() {
        return this.mOtherAlbumBean;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public BaseViewBean getRubbishAlbumBean() {
        return this.mRubbishAlbumBean;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public BaseViewBean getCleanerBean() {
        return this.mCleanerBean;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public AlbumDataListResult getAlbumDataResult() {
        return this.mLastAlbumDataResult;
    }

    public void initShareAlbumsDetailInfo() {
        this.mQueryShareAlbumDetailInfo.executeWith(new SimpleDisposableSubscriber() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.1
            @Override // org.reactivestreams.Subscriber
            public void onNext(Object obj) {
            }
        }, null, ((BaseAlbumTabContract$V) getView()).getLifecycle());
    }

    public void querySnapAlbums() {
        this.mQuerySnapAlbumList.executeWith(new SimpleDisposableSubscriber<AlbumDataListResult>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.2
            @Override // org.reactivestreams.Subscriber
            public void onNext(AlbumDataListResult albumDataListResult) {
                if (albumDataListResult.isEmpty()) {
                    DefaultLogger.fd(BaseAlbumTabPresenter.TAG, "return snap albums,is empty");
                } else {
                    BaseAlbumTabPresenter.this.dispatchAlbumDatas(albumDataListResult);
                }
            }
        }, new AlbumGroupByAlbumTypeFunction.Config(getQueryAllAlbumsLoadComplateListener()), ((BaseAlbumTabContract$V) getView()).getViewLifecycleOwner().getLifecycle());
    }

    public void queryAlbums() {
        ((BaseAlbumTabContract$V) getView()).getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.3
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                BaseAlbumTabPresenter.this.isNeedSrollToHead = false;
                ((BaseAlbumTabContract$V) BaseAlbumTabPresenter.this.getView()).getRecyclerView().removeOnScrollListener(this);
            }
        });
        if (!this.mQueryAllAlbumList.isDispose()) {
            this.mQueryAllAlbumList.disposeAndAgain();
        }
        this.mQueryAllAlbumList.executeWith(new SimpleDisposableSubscriber<AlbumDataListResult>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.4
            @Override // org.reactivestreams.Subscriber
            public void onNext(AlbumDataListResult albumDataListResult) {
                BaseAlbumTabPresenter baseAlbumTabPresenter = BaseAlbumTabPresenter.this;
                baseAlbumTabPresenter.dispatchAlbumDatas(albumDataListResult, baseAlbumTabPresenter.isNeedSrollToHead);
            }
        }, getQueryAllAlbumListParam(), ((BaseAlbumTabContract$V) getView()).getLifecycle());
    }

    public QueryAlbumsByAlbumTabScene.RequestBean getQueryAllAlbumListParam() {
        return new QueryAlbumsByAlbumTabScene.RequestBean(new AlbumGroupByAlbumTypeFunction.Config(getQueryAllAlbumsLoadComplateListener()));
    }

    public AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback getQueryAllAlbumsLoadComplateListener() {
        return new AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.5
            @Override // com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback
            public void onProcessEnd(AlbumDataListResult albumDataListResult) {
                if (BaseAlbumTabPresenter.this.mOnDataProcessingListeners != null) {
                    for (AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback onDataProcessingCallback : BaseAlbumTabPresenter.this.mOnDataProcessingListeners) {
                        onDataProcessingCallback.onProcessEnd(albumDataListResult);
                    }
                }
            }

            @Override // com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback
            public void onProcessStart(List<Album> list) {
                if (BaseAlbumTabPresenter.this.mOnDataProcessingListeners != null) {
                    for (AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback onDataProcessingCallback : BaseAlbumTabPresenter.this.mOnDataProcessingListeners) {
                        onDataProcessingCallback.onProcessStart(list);
                    }
                }
            }

            @Override // com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback
            public void onSplitGroupFinish(AlbumDataListResult albumDataListResult) {
                if (BaseAlbumTabPresenter.this.mOnDataProcessingListeners != null) {
                    for (AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback onDataProcessingCallback : BaseAlbumTabPresenter.this.mOnDataProcessingListeners) {
                        onDataProcessingCallback.onSplitGroupFinish(albumDataListResult);
                    }
                }
            }
        };
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public void addOnDataProcessingCallback(AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback onDataProcessingCallback) {
        if (this.mOnDataProcessingListeners == null) {
            this.mOnDataProcessingListeners = new LinkedList();
        }
        this.mOnDataProcessingListeners.add(onDataProcessingCallback);
    }

    public void dispatchAlbumDatas(AlbumDataListResult albumDataListResult, boolean z) {
        this.mLastAlbumDataResult = albumDataListResult;
        if (!isEnableAIAlbum() && this.mLastAlbumDataResult.isEmpty() && this.mOtherAlbumBean == null && this.mAIAlbumBean == null) {
            ((BaseAlbumTabContract$V) getView()).showAlbumDatas(null, null, z);
        } else {
            ((BaseAlbumTabContract$V) getView()).showAlbumDatas(albumDataListResult.getDatas(), albumDataListResult.getModels(), z);
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public <T extends BaseViewBean> List<T> getGroupDatas(String str) {
        AlbumDataListResult albumDataListResult = this.mLastAlbumDataResult;
        if (albumDataListResult != null) {
            return (List<T>) albumDataListResult.getGroupDatas(str);
        }
        return null;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public String[] getSupportGroups() {
        return AlbumSplitGroupHelper.getSplitGroupMode().getSupportGroups();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public String getGroupType(Album album) {
        return AlbumSplitGroupHelper.getSplitGroupMode().getGroupType(album);
    }

    public void dispatchAlbumDatas(AlbumDataListResult albumDataListResult) {
        dispatchAlbumDatas(albumDataListResult, false);
    }

    public void queryOtherAlbumCover(QueryOtherAlbumCovers.RequestParam requestParam) {
        this.mQueryAndGeneratorOtherAlbumsCover.executeWith(new SimpleDisposableSubscriber<OtherAlbumGridCoverViewBean>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.6
            @Override // org.reactivestreams.Subscriber
            public void onNext(OtherAlbumGridCoverViewBean otherAlbumGridCoverViewBean) {
                BaseAlbumTabPresenter baseAlbumTabPresenter = BaseAlbumTabPresenter.this;
                if (otherAlbumGridCoverViewBean.isEmpty()) {
                    otherAlbumGridCoverViewBean = null;
                }
                baseAlbumTabPresenter.dispatchOtherAlbum(otherAlbumGridCoverViewBean);
            }
        }, requestParam, ((BaseAlbumTabContract$V) getView()).getLifecycle());
    }

    public void queryOtherAlbumCover() {
        queryOtherAlbumCover(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOtherAlbum(OtherAlbumGridCoverViewBean otherAlbumGridCoverViewBean) {
        if (getView() == 0) {
            return;
        }
        try {
            if (Objects.equals(this.mOtherAlbumBean, otherAlbumGridCoverViewBean)) {
                return;
            }
            this.mOtherAlbumBean = otherAlbumGridCoverViewBean;
            DefaultLogger.fd(TAG, "dispatchOtherAlbumDatas datas:");
            DefaultLogger.fd(TAG, otherAlbumGridCoverViewBean);
            if (-1 == (this.mOtherAlbumBean == null ? -1 : ((BaseAlbumTabContract$V) getView()).findDataIndexById(this.mOtherAlbumBean.getId()))) {
                ((QueryAlbumsByAlbumTabScene) this.mQueryAllAlbumList).reDispatchAlbumData();
            } else {
                ((BaseAlbumTabContract$V) getView()).updateDataById(this.mOtherAlbumBean.getId(), this.mOtherAlbumBean);
            }
        } catch (Exception e) {
            DefaultLogger.e(TAG, "Exception dispatchOtherAlbumCoverDatas() ,message %s", ExceptionUtils.getStackTraceString(e));
        }
    }

    public void queryAIAlbumCover() {
        this.mSearchStatusListenerUsecase.executeWith(new SimpleDisposableSubscriber<Integer>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.7
            @Override // org.reactivestreams.Subscriber
            public void onNext(Integer num) {
                if (SearchConstants.isNoAccountStatus(num.intValue())) {
                    BaseAlbumTabPresenter.this.isEnableAIAlbum = false;
                    return;
                }
                int i = BaseAlbumTabPresenter.this.mLastSearchStatus;
                if (-1 == i || (SearchConstants.isErrorStatus(i) && !SearchConstants.isErrorStatus(num.intValue()))) {
                    BaseAlbumTabPresenter.this.startQueryAIAlbumCover();
                }
                BaseAlbumTabPresenter.this.mLastSearchStatus = num.intValue();
            }
        }, null, ((BaseAlbumTabContract$V) getView()).getViewLifecycleOwner().getLifecycle());
    }

    public void startQueryAIAlbumCover() {
        startQueryAIAlbumCover(QueryAIAlbumCase.generatorQueryParamBean(2, 1, 1));
    }

    public void startQueryAIAlbumCover(QueryAIAlbumCase.RequestParam requestParam) {
        this.mQueryAndGenerateAIAlbumsCover.executeWith(new QueryAIAlbumCase.InternalSubscribe<AIAlbumGridCoverViewBean>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.8
            @Override // com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase.InternalSubscribe
            public void onAIAlbumDisplayStatusChange(boolean z, boolean z2) {
                if (!z2) {
                    BaseAlbumTabPresenter baseAlbumTabPresenter = BaseAlbumTabPresenter.this;
                    boolean z3 = baseAlbumTabPresenter.isEnableAIAlbum;
                    if (!z3 && z) {
                        baseAlbumTabPresenter.isEnableAIAlbum = true;
                        baseAlbumTabPresenter.startQueryAIAlbumCover();
                        return;
                    } else if (z3 && !z) {
                        baseAlbumTabPresenter.isEnableAIAlbum = false;
                        baseAlbumTabPresenter.dispatchEmptyAIAlbumEvent(null);
                        return;
                    }
                }
                BaseAlbumTabPresenter.this.isEnableAIAlbum = z;
            }

            @Override // com.miui.gallery.util.CheckEmptyDataSubscriber
            public void onEvent(AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean) {
                BaseAlbumTabPresenter.this.dispatchAIAlbum(aIAlbumGridCoverViewBean);
            }

            @Override // com.miui.gallery.util.CheckEmptyDataSubscriber
            public void onEventEmpty(AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean) {
                BaseAlbumTabPresenter.this.dispatchEmptyAIAlbumEvent(aIAlbumGridCoverViewBean);
            }
        }, requestParam, ((BaseAlbumTabContract$V) getView()).getLifecycle());
    }

    public void dispatchAIAlbum(AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean) {
        if (getView() == 0 || !isEnableAIAlbum() || aIAlbumGridCoverViewBean == null) {
            return;
        }
        try {
            if (this.mAIAlbumBean != null && Objects.equals(aIAlbumGridCoverViewBean.getCovers(), this.mAIAlbumBean.getCovers())) {
                return;
            }
            this.mAIAlbumBean = aIAlbumGridCoverViewBean;
            if (this.mLastAlbumDataResult == null) {
                this.mLastAlbumDataResult = new AlbumDataListResult();
            }
            if (-1 == ((BaseAlbumTabContract$V) getView()).findDataIndexById(this.mAIAlbumBean.getId())) {
                DefaultLogger.fd(TAG, "dispatchAIAlbumDatas datas:");
                DefaultLogger.fd(TAG, aIAlbumGridCoverViewBean);
                ((QueryAlbumsByAlbumTabScene) this.mQueryAllAlbumList).reDispatchAlbumData();
                return;
            }
            ((BaseAlbumTabContract$V) getView()).updateDataById(this.mAIAlbumBean.getId(), this.mAIAlbumBean);
        } catch (Exception e) {
            DefaultLogger.e(TAG, "Exception dispatchAIAlbumDatas() ,message %s", e.getMessage());
        }
    }

    public void dispatchEmptyAIAlbumEvent(AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean) {
        AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean2;
        if (isEnableAIAlbum()) {
            dispatchAIAlbum(aIAlbumGridCoverViewBean);
            return;
        }
        AlbumDataListResult albumDataListResult = this.mLastAlbumDataResult;
        if (albumDataListResult == null || (aIAlbumGridCoverViewBean2 = this.mAIAlbumBean) == null || albumDataListResult.removeDataById(getGroupType(aIAlbumGridCoverViewBean2.mo1601provider()), this.mAIAlbumBean.getId(), true) == null) {
            return;
        }
        this.mAIAlbumBean = null;
        dispatchAlbumDatas(this.mLastAlbumDataResult);
    }

    public void queryTrashBin() {
        this.mQueryTrashBin.executeWith(new SimpleDisposableSubscriber<Optional<AlbumTabToolItemBean>>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.9
            @Override // org.reactivestreams.Subscriber
            public void onNext(Optional<AlbumTabToolItemBean> optional) {
                if (optional.isPresent()) {
                    BaseAlbumTabPresenter.this.mTrashBinBean = optional.get();
                    BaseAlbumTabPresenter baseAlbumTabPresenter = BaseAlbumTabPresenter.this;
                    baseAlbumTabPresenter.dispatchAlbumTabToolItemBean(baseAlbumTabPresenter.mTrashBinBean);
                }
            }
        }, null, ((BaseAlbumTabContract$V) getView()).getLifecycle());
    }

    public void dispatchAlbumTabToolItemBean(AlbumTabToolItemBean albumTabToolItemBean) {
        if (albumTabToolItemBean == null || getView() == 0) {
            return;
        }
        if (-1 == ((BaseAlbumTabContract$V) getView()).findDataIndexById(albumTabToolItemBean.getId())) {
            ((QueryAlbumsByAlbumTabScene) this.mQueryAllAlbumList).reDispatchAlbumData();
        } else {
            ((BaseAlbumTabContract$V) getView()).updateDataByIdIfNeed(albumTabToolItemBean.getId(), albumTabToolItemBean);
        }
    }

    public void queryMediaGroup() {
        this.mQueryMediaType.executeWith(new SimpleDisposableSubscriber<List<MediaGroupTypeViewBean>>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.10
            @Override // org.reactivestreams.Subscriber
            public void onNext(List<MediaGroupTypeViewBean> list) {
                BaseAlbumTabPresenter baseAlbumTabPresenter = BaseAlbumTabPresenter.this;
                baseAlbumTabPresenter.mMediaTypeGroupBeans = list;
                ((QueryAlbumsByAlbumTabScene) baseAlbumTabPresenter.mQueryAllAlbumList).reDispatchAlbumData();
            }
        }, getQueryMediaTypeParamBean(), ((BaseAlbumTabContract$V) getView()).getLifecycle());
    }

    public QueryMediaTypeGroupCase.RequestBean getQueryMediaTypeParamBean() {
        return new QueryMediaTypeGroupCase.RequestBean(AlbumConstants.MedidTypeScene.SCENE_ALBUM_TAB_PAGE, false);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public boolean isCanShowTrashAlbum() {
        boolean z = true;
        if (AccountCache.isHaveAccount()) {
            return true;
        }
        boolean z2 = AlbumConfigSharedPreferences.getInstance().getBoolean(GalleryPreferences.PrefKeys.IS_LOCAL_HAVE_TRASH_FILE, false);
        if (z2) {
            return z2;
        }
        if (FileUtils.getFolderSize(new File(TrashManager.getTrashBinPath())) <= 0) {
            z = false;
        }
        AlbumConfigSharedPreferences.getInstance().putBoolean(GalleryPreferences.PrefKeys.IS_LOCAL_HAVE_TRASH_FILE, z);
        return z;
    }

    public void queryRubbishAlbum() {
        this.mQueryRubbishAlbum.executeWith(new SimpleDisposableSubscriber<Optional<AlbumTabToolItemBean>>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.11
            @Override // org.reactivestreams.Subscriber
            public void onNext(Optional<AlbumTabToolItemBean> optional) {
                if (optional.isPresent()) {
                    BaseAlbumTabPresenter.this.mRubbishAlbumBean = optional.get();
                    BaseAlbumTabPresenter baseAlbumTabPresenter = BaseAlbumTabPresenter.this;
                    baseAlbumTabPresenter.dispatchAlbumTabToolItemBean(baseAlbumTabPresenter.mRubbishAlbumBean);
                }
            }
        }, null, ((BaseAlbumTabContract$V) getView()).getLifecycle());
    }

    public void queryCleaner() {
        this.mScanCleanerCase.executeWith(new SimpleDisposableSubscriber<AlbumTabToolItemBean>() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter.12
            @Override // org.reactivestreams.Subscriber
            public void onNext(AlbumTabToolItemBean albumTabToolItemBean) {
                BaseAlbumTabPresenter baseAlbumTabPresenter = BaseAlbumTabPresenter.this;
                baseAlbumTabPresenter.mCleanerBean = albumTabToolItemBean;
                baseAlbumTabPresenter.dispatchAlbumTabToolItemBean(albumTabToolItemBean);
            }
        }, null, ((BaseAlbumTabContract$V) getView()).getLifecycle());
    }

    public void dispatchCleaner(String str) {
        if (this.mCleanerBean == null || getView() == 0) {
            return;
        }
        this.mCleanerBean.setSubTitle(str);
        if (-1 == ((BaseAlbumTabContract$V) getView()).findDataIndexById(this.mCleanerBean.getId())) {
            ((QueryAlbumsByAlbumTabScene) this.mQueryAllAlbumList).reDispatchAlbumData();
        } else {
            ((BaseAlbumTabContract$V) getView()).updateDataById(this.mCleanerBean.getId(), this.mCleanerBean);
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public boolean isEnableDragMode() {
        return AlbumSortHelper.isCustomSortOrder();
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        super.onDestroy();
        this.mQueryAllAlbumList = null;
        this.mQuerySnapAlbumList = null;
        this.mQueryAndGenerateAIAlbumsCover = null;
        this.mQueryAndGeneratorOtherAlbumsCover = null;
        List<AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback> list = this.mOnDataProcessingListeners;
        if (list != null) {
            list.clear();
        }
        this.mOnDataProcessingListeners = null;
    }
}
