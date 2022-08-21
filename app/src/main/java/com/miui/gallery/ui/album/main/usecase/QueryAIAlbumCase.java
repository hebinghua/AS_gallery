package com.miui.gallery.ui.album.main.usecase;

import android.util.SparseBooleanArray;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.base_optimization.util.ExceptionUtils;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.datalayer.repository.album.ai.utils.AIAlbumDataUtils;
import com.miui.gallery.model.dto.AIAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.AIAlbumDisplayHelper;
import com.miui.gallery.ui.album.common.GroupDatasResult;
import com.miui.gallery.ui.album.main.viewbean.AIAlbumGridCoverViewBean;
import com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean;
import com.miui.gallery.util.CheckEmptyDataSubscriber;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class QueryAIAlbumCase extends HotUseCase<FourPalaceGridCoverViewBean, RequestParam> {
    public volatile boolean isFirst;
    public final AIAlbumDisplayHelper.WeakReferencedAIAlbumDisplayStatusObserver mAIAlbumStatusChangeListener;
    public final AbstractAlbumRepository mAlbumRepository;
    public final AIAlbumDisplayHelper.DisplayStatusCallback mDisplayStatusCallback;
    public GroupDatasResult<AIAlbumCover> mFillGroupResult;
    public GroupDatasResult<AIAlbumCover> mLastGroupResult;
    public RequestParam mLastRequestParam;

    /* loaded from: classes2.dex */
    public static abstract class InternalSubscribe<T> extends CheckEmptyDataSubscriber<T> {
        public abstract void onAIAlbumDisplayStatusChange(boolean z, boolean z2);
    }

    public QueryAIAlbumCase(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mLastGroupResult = new GroupDatasResult<>(Arrays.asList("ai_cover_face", "ai_cover_location", "ai_cover_tag"));
        this.mFillGroupResult = new GroupDatasResult<>(Arrays.asList("ai_cover_face", "ai_cover_location", "ai_cover_tag"));
        this.isFirst = true;
        AIAlbumDisplayHelper.DisplayStatusCallback displayStatusCallback = new AIAlbumDisplayHelper.DisplayStatusCallback() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase.5
            @Override // com.miui.gallery.ui.AIAlbumDisplayHelper.DisplayStatusCallback
            public void onStatusChanged(SparseBooleanArray sparseBooleanArray) {
                if (sparseBooleanArray != null && sparseBooleanArray.indexOfKey(2) >= 0) {
                    QueryAIAlbumCase.this.internalDispatchAIStatusChange(sparseBooleanArray.get(2));
                }
            }
        };
        this.mDisplayStatusCallback = displayStatusCallback;
        this.mAlbumRepository = abstractAlbumRepository;
        this.mAIAlbumStatusChangeListener = new AIAlbumDisplayHelper.WeakReferencedAIAlbumDisplayStatusObserver(displayStatusCallback);
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<FourPalaceGridCoverViewBean> buildFlowable(RequestParam requestParam) {
        if (requestParam == null) {
            return Flowable.just(generatorDefaultAIAlbumBean());
        }
        return Flowable.fromCallable(new Callable<Boolean>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo1596call() throws Exception {
                boolean z = false;
                boolean z2 = AIAlbumDisplayHelper.getInstance().registerAIAlbumDisplayStatusObserver(QueryAIAlbumCase.this.mAIAlbumStatusChangeListener).get(2, false);
                QueryAIAlbumCase queryAIAlbumCase = QueryAIAlbumCase.this;
                if (SyncUtil.isGalleryCloudSyncable(GalleryApp.sGetAndroidContext()) && z2) {
                    z = true;
                }
                queryAIAlbumCase.internalDispatchAIStatusChange(z);
                DefaultLogger.d("QueryAndGeneratorAIAlbumsCover", "query AI Album Cover execute,AI Status %b", Boolean.valueOf(z2));
                return Boolean.valueOf(z2);
            }
        }).flatMap(new AnonymousClass1(requestParam));
    }

    /* renamed from: com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Function<Boolean, Publisher<FourPalaceGridCoverViewBean>> {
        public final /* synthetic */ RequestParam val$param;

        public AnonymousClass1(RequestParam requestParam) {
            this.val$param = requestParam;
        }

        @Override // io.reactivex.functions.Function
        /* renamed from: apply  reason: avoid collision after fix types in other method */
        public Publisher<FourPalaceGridCoverViewBean> mo2564apply(Boolean bool) throws Exception {
            if (bool.booleanValue() || this.val$param.isForceQuery()) {
                QueryAIAlbumCase.this.mLastRequestParam = this.val$param;
                int[] iArr = {QueryAIAlbumCase.this.mLastRequestParam.faceNum, QueryAIAlbumCase.this.mLastRequestParam.locationNum, QueryAIAlbumCase.this.mLastRequestParam.tagNum};
                if (QueryAIAlbumCase.this.mLastRequestParam.isEnableFilling) {
                    int loadSum = QueryAIAlbumCase.this.mLastRequestParam.getLoadSum();
                    iArr[1] = loadSum;
                    iArr[2] = loadSum;
                }
                return (Publisher) QueryAIAlbumCase.this.mAlbumRepository.queryAIAlbumCover(Integer.valueOf(iArr[0]), Integer.valueOf(iArr[1]), Integer.valueOf(iArr[2])).map(new Function<PageResults<CoverList>, FourPalaceGridCoverViewBean>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase.1.2
                    @Override // io.reactivex.functions.Function
                    /* renamed from: apply  reason: avoid collision after fix types in other method */
                    public FourPalaceGridCoverViewBean mo2564apply(PageResults<CoverList> pageResults) throws Exception {
                        DefaultLogger.d("QueryAndGeneratorAIAlbumsCover", "start convert to FourPalaceGridCoverViewBean");
                        AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean = new AIAlbumGridCoverViewBean();
                        if (AnonymousClass1.this.val$param.isIgnoreCache() && pageResults.isFromFile()) {
                            aIAlbumGridCoverViewBean.mapping((CoverList) null);
                            return aIAlbumGridCoverViewBean;
                        }
                        aIAlbumGridCoverViewBean.mapping(pageResults.getResult());
                        return aIAlbumGridCoverViewBean;
                    }
                }).to(new Function<Flowable<FourPalaceGridCoverViewBean>, Flowable<FourPalaceGridCoverViewBean>>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase.1.1
                    @Override // io.reactivex.functions.Function
                    /* renamed from: apply  reason: avoid collision after fix types in other method */
                    public Flowable<FourPalaceGridCoverViewBean> mo2564apply(Flowable<FourPalaceGridCoverViewBean> flowable) throws Exception {
                        return QueryAIAlbumCase.this.wrapperDownStream(flowable.map(new Function<FourPalaceGridCoverViewBean, FourPalaceGridCoverViewBean>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase.1.1.1
                            @Override // io.reactivex.functions.Function
                            /* renamed from: apply  reason: avoid collision after fix types in other method */
                            public FourPalaceGridCoverViewBean mo2564apply(FourPalaceGridCoverViewBean fourPalaceGridCoverViewBean) throws Exception {
                                DefaultLogger.d("QueryAndGeneratorAIAlbumsCover", "Batch update " + fourPalaceGridCoverViewBean);
                                QueryAIAlbumCase queryAIAlbumCase = QueryAIAlbumCase.this;
                                return queryAIAlbumCase.batchUpdateDatas((AIAlbumGridCoverViewBean) fourPalaceGridCoverViewBean, queryAIAlbumCase.mLastRequestParam);
                            }
                        }));
                    }
                });
            }
            DefaultLogger.d("QueryAndGeneratorAIAlbumsCover", "because the AI Status is Closed ,so return empty obj");
            return Flowable.just(QueryAIAlbumCase.this.generatorDefaultAIAlbumBean());
        }
    }

    public final Flowable<FourPalaceGridCoverViewBean> wrapperDownStream(Flowable<FourPalaceGridCoverViewBean> flowable) {
        return flowable.doOnCancel(new Action() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase.4
            @Override // io.reactivex.functions.Action
            public void run() throws Exception {
                if (QueryAIAlbumCase.this.mAIAlbumStatusChangeListener != null) {
                    DefaultLogger.d("QueryAndGeneratorAIAlbumsCover", "downStream cancel,now unRegister AIAlbumDisplayStatusObserver");
                    AIAlbumDisplayHelper.getInstance().unregisterAIAlbumDisplayStatusObserver(QueryAIAlbumCase.this.mAIAlbumStatusChangeListener);
                }
            }
        }).onErrorResumeNext(new Function<Throwable, Publisher<? extends FourPalaceGridCoverViewBean>>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase.3
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<? extends FourPalaceGridCoverViewBean> mo2564apply(Throwable th) throws Exception {
                DefaultLogger.e("QueryAndGeneratorAIAlbumsCover", "load Ai album error:" + ExceptionUtils.getStackTraceString(th));
                return Flowable.just(QueryAIAlbumCase.this.generatorDefaultAIAlbumBean());
            }
        });
    }

    public final AIAlbumGridCoverViewBean generatorDefaultAIAlbumBean() {
        AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean = new AIAlbumGridCoverViewBean();
        aIAlbumGridCoverViewBean.mapping((CoverList) null);
        return aIAlbumGridCoverViewBean;
    }

    public static RequestParam generatorQueryParamBean(int i, int i2, int i3) {
        RequestParam requestParam = new RequestParam();
        requestParam.setFaceNum(i);
        requestParam.setLocationNum(i2);
        requestParam.setTagNum(i3);
        return requestParam;
    }

    public final AIAlbumGridCoverViewBean batchUpdateDatas(AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean, RequestParam requestParam) {
        int i;
        int i2;
        int i3 = requestParam.faceNum;
        int i4 = requestParam.locationNum;
        int i5 = requestParam.tagNum;
        List<AIAlbumCover> datas = this.mLastGroupResult.getDatas();
        List<AIAlbumCover> covers = aIAlbumGridCoverViewBean.getCovers();
        int size = datas == null ? 0 : datas.size();
        if (covers == null) {
            covers = new ArrayList(0);
        }
        int size2 = covers.size();
        if (size != 0 && size2 == 0) {
            List<AIAlbumCover> covers2 = aIAlbumGridCoverViewBean.getCovers();
            if (AIAlbumDataUtils.isFaceEmptyList(covers2)) {
                this.mLastGroupResult.removeGroup("ai_cover_face");
                this.mFillGroupResult.removeGroup("ai_cover_face");
            } else if (AIAlbumDataUtils.isLocationEmptyList(covers2)) {
                this.mLastGroupResult.removeGroup("ai_cover_location");
                this.mFillGroupResult.removeGroup("ai_cover_location");
            } else if (AIAlbumDataUtils.isTagEmptyList(covers2)) {
                this.mLastGroupResult.removeGroup("ai_cover_tag");
                this.mFillGroupResult.removeGroup("ai_cover_tag");
            }
        } else {
            ArrayList arrayList = new ArrayList(i3);
            ArrayList arrayList2 = new ArrayList(i4);
            ArrayList arrayList3 = new ArrayList(i5);
            for (AIAlbumCover aIAlbumCover : covers) {
                if (aIAlbumCover.type.equals("ai_cover_face")) {
                    arrayList.add(aIAlbumCover);
                } else if (aIAlbumCover.type.equals("ai_cover_location")) {
                    arrayList2.add(aIAlbumCover);
                } else if (aIAlbumCover.type.equals("ai_cover_tag")) {
                    arrayList3.add(aIAlbumCover);
                }
            }
            if (!arrayList.isEmpty()) {
                this.mLastGroupResult.addOrUpdateGroupDatas("ai_cover_face", arrayList.size() >= i3 ? arrayList.subList(0, i3) : arrayList);
            }
            if (!arrayList2.isEmpty()) {
                this.mLastGroupResult.addOrUpdateGroupDatas("ai_cover_location", arrayList2.size() >= i4 ? arrayList2.subList(0, i4) : arrayList2);
            }
            if (!arrayList3.isEmpty()) {
                this.mLastGroupResult.addOrUpdateGroupDatas("ai_cover_tag", arrayList3.size() >= i5 ? arrayList3.subList(0, i5) : arrayList3);
            }
            if (requestParam.isEnableFilling) {
                if (!arrayList.isEmpty()) {
                    this.mFillGroupResult.addOrUpdateGroupDatas("ai_cover_face", arrayList);
                }
                if (!arrayList2.isEmpty()) {
                    this.mFillGroupResult.addOrUpdateGroupDatas("ai_cover_location", arrayList2);
                }
                if (!arrayList3.isEmpty()) {
                    this.mFillGroupResult.addOrUpdateGroupDatas("ai_cover_tag", arrayList3);
                }
            }
        }
        if (requestParam.isEnableFilling && !this.mFillGroupResult.isEmpty()) {
            int i6 = i4 + i5 + i3;
            if (!this.mLastGroupResult.containsKey("ai_cover_face")) {
                List<AIAlbumCover> groupDatas = this.mLastGroupResult.getGroupDatas("ai_cover_tag");
                if (groupDatas == null || groupDatas.isEmpty()) {
                    aIAlbumGridCoverViewBean.setCovers(new ArrayList(this.mFillGroupResult.getGroupDatas("ai_cover_location")));
                } else if (groupDatas.size() == requestParam.tagNum) {
                    ArrayList arrayList4 = new ArrayList(i6);
                    List<AIAlbumCover> groupDatas2 = this.mFillGroupResult.getGroupDatas("ai_cover_location");
                    int i7 = (i6 - i5) - i4;
                    if (groupDatas2 != null) {
                        i = groupDatas2.size() >= i6 ? i6 : groupDatas2.size();
                        arrayList4.addAll(groupDatas2.subList(0, i));
                    } else {
                        i = 0;
                    }
                    List<AIAlbumCover> groupDatas3 = this.mFillGroupResult.getGroupDatas("ai_cover_tag");
                    if (groupDatas3 != null) {
                        if (groupDatas3.size() <= i7) {
                            arrayList4.addAll(groupDatas3);
                        } else if (i > i7) {
                            int size3 = arrayList4.size();
                            for (int i8 = 0; i8 < groupDatas3.subList(0, i7).size() && (i2 = i8 + i7) < size3; i8++) {
                                arrayList4.set(i2, groupDatas3.get(i8));
                            }
                        } else {
                            for (AIAlbumCover aIAlbumCover2 : groupDatas3) {
                                if (arrayList4.size() >= i6) {
                                    break;
                                }
                                arrayList4.add(aIAlbumCover2);
                            }
                        }
                    }
                    aIAlbumGridCoverViewBean.setCovers(arrayList4);
                }
            } else {
                aIAlbumGridCoverViewBean.setCovers(new ArrayList(this.mLastGroupResult.getDatas()));
            }
        }
        return aIAlbumGridCoverViewBean;
    }

    /* loaded from: classes2.dex */
    public static class RequestParam {
        public int faceNum;
        public boolean isEnableFilling = true;
        public boolean isForceQuery;
        public boolean isIgnoreCache;
        public int locationNum;
        public int tagNum;

        public int getLoadSum() {
            return this.faceNum + this.locationNum + this.tagNum;
        }

        public void setFaceNum(int i) {
            this.faceNum = i;
        }

        public void setLocationNum(int i) {
            this.locationNum = i;
        }

        public void setTagNum(int i) {
            this.tagNum = i;
        }

        public boolean isForceQuery() {
            return this.isForceQuery;
        }

        public void setForceQuery(boolean z) {
            this.isForceQuery = z;
        }

        public boolean isIgnoreCache() {
            return this.isIgnoreCache;
        }

        public void setIgnoreCache(boolean z) {
            this.isIgnoreCache = z;
        }
    }

    public final void internalDispatchAIStatusChange(boolean z) {
        FlowableSubscriber flowableSubscriber = this.mLastSubscribe;
        if (flowableSubscriber == null || !(flowableSubscriber instanceof InternalSubscribe)) {
            return;
        }
        DefaultLogger.d("QueryAndGeneratorAIAlbumsCover", "AIAlbum status change new statue: %b", Boolean.valueOf(z));
        ((InternalSubscribe) this.mLastSubscribe).onAIAlbumDisplayStatusChange(z, this.isFirst);
        this.isFirst = false;
    }
}
