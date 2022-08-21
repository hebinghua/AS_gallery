package com.miui.gallery.ui.album.main.usecase;

import android.annotation.SuppressLint;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor;
import com.miui.gallery.base_optimization.clean.thread.SubScribeThreadExecutor;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.provider.album.AlbumSnapshotHelper;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.common.usecase.QueryAlbumsCase;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class QueryAlbumsByAlbumTabScene extends HotUseCase<AlbumDataListResult, RequestBean> {
    public final Object LOCK;
    public boolean isNeedCacheSourceDatas;
    public final QueryAlbumsCase mInternalQuery;
    public List<Album> mLastSource;

    public QueryAlbumsByAlbumTabScene(AbstractAlbumRepository abstractAlbumRepository) {
        this(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor(), abstractAlbumRepository);
    }

    public QueryAlbumsByAlbumTabScene(SubScribeThreadExecutor subScribeThreadExecutor, ObserveThreadExecutor observeThreadExecutor, AbstractAlbumRepository abstractAlbumRepository) {
        super(subScribeThreadExecutor, observeThreadExecutor);
        this.isNeedCacheSourceDatas = true;
        this.LOCK = new Object();
        this.mInternalQuery = new QueryAlbumsCase(subScribeThreadExecutor, observeThreadExecutor, abstractAlbumRepository);
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<AlbumDataListResult> buildFlowable(final RequestBean requestBean) {
        return ((Flowable) this.mInternalQuery.buildFlowable((QueryAlbumsCase.ParamBean) requestBean).flatMap(new Function<PageResults<List<Album>>, Publisher<List<Album>>>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAlbumsByAlbumTabScene.2
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<List<Album>> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
                if (QueryAlbumsByAlbumTabScene.this.isNeedCacheSourceDatas || pageResults.getFromType() != 4) {
                    synchronized (QueryAlbumsByAlbumTabScene.this.LOCK) {
                        QueryAlbumsByAlbumTabScene.this.mLastSource = pageResults.getResult();
                    }
                    return Flowable.just(QueryAlbumsByAlbumTabScene.this.mLastSource);
                }
                return Flowable.empty();
            }
        }).to(new AlbumGroupByAlbumTypeFunction(requestBean.getOtherConfig()))).doAfterNext(new Consumer<AlbumDataListResult>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAlbumsByAlbumTabScene.1
            @Override // io.reactivex.functions.Consumer
            public void accept(AlbumDataListResult albumDataListResult) throws Exception {
                AlbumSnapshotHelper.persist(QueryAlbumsByAlbumTabScene.this.mLastSource);
                if (requestBean.getOnDataProcessingCallback() != null) {
                    requestBean.getOnDataProcessingCallback().onProcessEnd(albumDataListResult);
                }
            }
        });
    }

    @SuppressLint({"CheckResult"})
    public void reDispatchAlbumData() {
        try {
            if (this.mLastSource == null) {
                return;
            }
            RxGalleryExecutors.getInstance().getUserThreadExecutor().execute(new Runnable() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAlbumsByAlbumTabScene.3
                @Override // java.lang.Runnable
                public void run() {
                    ArrayList arrayList;
                    synchronized (QueryAlbumsByAlbumTabScene.this.LOCK) {
                        arrayList = new ArrayList(QueryAlbumsByAlbumTabScene.this.mLastSource);
                    }
                    QueryAlbumsByAlbumTabScene.this.getCompositeDisposable().add(((Flowable) Flowable.just(arrayList).to(new AlbumGroupByAlbumTypeFunction(((RequestBean) QueryAlbumsByAlbumTabScene.this.getParam()).getOtherConfig()))).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<AlbumDataListResult>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAlbumsByAlbumTabScene.3.1
                        @Override // io.reactivex.functions.Consumer
                        public void accept(AlbumDataListResult albumDataListResult) throws Exception {
                            if (QueryAlbumsByAlbumTabScene.this.mLastSubscribe != null) {
                                QueryAlbumsByAlbumTabScene.this.mLastSubscribe.onNext(albumDataListResult);
                            } else {
                                DefaultLogger.e("QueryAlbumsByAlbumTabScene", "reDispatchAlbumData datas,but lastSubscribe is null");
                            }
                        }
                    }, new Consumer<Throwable>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryAlbumsByAlbumTabScene.3.2
                        @Override // io.reactivex.functions.Consumer
                        public void accept(Throwable th) throws Exception {
                            DefaultLogger.e("QueryAlbumsByAlbumTabScene", "reDispatchAlbumData error:", th);
                        }
                    }));
                }
            });
        } catch (Exception e) {
            DefaultLogger.e("QueryAlbumsByAlbumTabScene", "reDispatchAlbumData error:", e);
        }
    }

    /* loaded from: classes2.dex */
    public static class RequestBean extends QueryAlbumsCase.ParamBean {
        public final AlbumGroupByAlbumTypeFunction.Config mOtherConfig;

        public RequestBean(AlbumGroupByAlbumTypeFunction.Config config) {
            super(AlbumConstants.QueryScene.SCENE_ALBUM_TAB_PAGE, null, null, false);
            this.mOtherConfig = config;
        }

        public RequestBean(QueryAlbumsCase.ParamBean paramBean, AlbumGroupByAlbumTypeFunction.Config config) {
            super(paramBean.getQueryFlags(), paramBean.getExtraSelection(), paramBean.getExtraSelectionArgs(), false);
            this.mOtherConfig = config;
        }

        public AlbumGroupByAlbumTypeFunction.Config getOtherConfig() {
            return this.mOtherConfig;
        }

        public AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback getOnDataProcessingCallback() {
            AlbumGroupByAlbumTypeFunction.Config config = this.mOtherConfig;
            if (config != null) {
                return config.getCallback();
            }
            return null;
        }
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase, com.miui.gallery.base_optimization.clean.LifecycleUseCase
    public void onStart() {
        this.isNeedCacheSourceDatas = false;
        super.onStart();
    }
}
