package com.miui.gallery.ui.album.rubbishalbum;

import android.util.SparseBooleanArray;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.AsyncDiffEpoxyAdapter;
import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.common.base.requestbean.BaseOperationAlbumRequestBean;
import com.miui.gallery.ui.album.rubbishalbum.usecase.DoAddNoMediaForRubbishAlbum;
import com.miui.gallery.ui.album.rubbishalbum.usecase.DoChangeAlbumShowInRubbish;
import com.miui.gallery.ui.album.rubbishalbum.usecase.DoRemoveNoMediaForRubbishAlbum;
import com.miui.gallery.ui.album.rubbishalbum.usecase.QueryRubbishAlbumList;
import com.miui.gallery.ui.album.rubbishalbum.viewbean.RubbishItemItemViewBean;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.NoMediaUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.InitState;
import io.reactivex.subscribers.DisposableSubscriber;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/* loaded from: classes2.dex */
public class RubbishAlbumPresenter extends RubbishAlbumContract$P {
    private static final String TAG = "RubbishAlbumPresenter";
    private BaseUseCase mAddNoMediaForRubbishAlbum;
    private BaseUseCase mDoChangeAlbumShowRubbishAlbum;
    private boolean mIsWorking;
    private HotUseCase mQueryRubbishAlbumList;
    private BaseUseCase mRemoveNoMediaForRubbishAlbum;
    private SparseBooleanArray mRubbishAlbumStatus;
    private AbstractAlbumRepository mAlbumRepository = (AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY);
    private InitState mInitState = new RubbishAlbumActionModeInitState();

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public void initData() {
        if (this.mQueryRubbishAlbumList == null) {
            this.mQueryRubbishAlbumList = new QueryRubbishAlbumList(this.mAlbumRepository);
        }
        this.mQueryRubbishAlbumList.executeWith(new DisposableSubscriber<List<RubbishItemItemViewBean>>() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumPresenter.1
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(List<RubbishItemItemViewBean> list) {
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).showRubbishListResult(list);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).showRubbishListResult(null);
            }
        }, null, ((RubbishAlbumContract$V) getView()).getLifecycle());
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public void deleteAllAlbumsFromRubbishPage(List<RubbishItemItemViewBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        doDeleteAlbums(0, (Album[]) list.stream().map(new Function<RubbishItemItemViewBean, Album>() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumPresenter.2
            @Override // java.util.function.Function
            public Album apply(RubbishItemItemViewBean rubbishItemItemViewBean) {
                return (Album) rubbishItemItemViewBean.getSource();
            }
        }).toArray(RubbishAlbumPresenter$$ExternalSyntheticLambda0.INSTANCE));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Album[] lambda$deleteAllAlbumsFromRubbishPage$0(int i) {
        return new Album[i];
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public void removeFromRubbishAlbums(final RubbishItemItemViewBean rubbishItemItemViewBean) {
        if (rubbishItemItemViewBean == null || rubbishItemItemViewBean.getId() <= 0) {
            return;
        }
        final long id = rubbishItemItemViewBean.getId();
        if (this.mDoChangeAlbumShowRubbishAlbum == null) {
            this.mDoChangeAlbumShowRubbishAlbum = new DoChangeAlbumShowInRubbish(this.mAlbumRepository);
        }
        DebugUtil.logEventTime("operationTrace", "remove_from_rubbish_albums", false);
        this.mDoChangeAlbumShowRubbishAlbum.executeWith(new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumPresenter.3
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(Boolean bool) {
                if (bool.booleanValue()) {
                    ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).removeAlbumFromRubbishIsSuccess(id, rubbishItemItemViewBean);
                } else {
                    DefaultLogger.e(RubbishAlbumPresenter.TAG, "failed to remove album %s from rubbish.", rubbishItemItemViewBean.getTitle());
                }
                DebugUtil.logEventTime("operationTrace", "remove_from_rubbish_albums", true);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e(RubbishAlbumPresenter.TAG, "failed to remove album %s from rubbish. %s", rubbishItemItemViewBean.getTitle(), th.toString());
            }
        }, new BaseOperationAlbumRequestBean(id, false), ((RubbishAlbumContract$V) getView()).getLifecycle());
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public void doAddNoMediaForRubbishAlbum(final List<BaseViewBean> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        DebugUtil.logEventTime("operationTrace", "add_no_media", false);
        LinkedList<String> linkedList = new LinkedList();
        Iterator<BaseViewBean> it = list.iterator();
        while (it.hasNext()) {
            linkedList.add(((Album) ((RubbishItemItemViewBean) it.next()).getSource()).getLocalPath());
        }
        LinkedList linkedList2 = new LinkedList();
        for (String str : linkedList) {
            String[] absolutePath = StorageUtils.getAbsolutePath(((RubbishAlbumContract$V) getView()).getContext(), StorageUtils.ensureCommonRelativePath(str));
            if (absolutePath != null) {
                for (int i = 0; i < absolutePath.length; i++) {
                    absolutePath[i] = absolutePath[i] + File.separator + ".nomedia";
                }
                linkedList2.addAll(Arrays.asList(absolutePath));
            }
        }
        boolean z = true;
        for (IStoragePermissionStrategy.PermissionResult permissionResult : StorageSolutionProvider.get().checkPermission(linkedList2, IStoragePermissionStrategy.Permission.INSERT)) {
            boolean z2 = permissionResult.granted;
            z &= z2;
            if (!z2) {
                StorageSolutionProvider.get().requestPermission(((RubbishAlbumContract$V) getView()).getSafeActivity(), permissionResult.path, permissionResult.type);
            }
        }
        if (!z) {
            return;
        }
        if (this.mAddNoMediaForRubbishAlbum == null) {
            this.mAddNoMediaForRubbishAlbum = new DoAddNoMediaForRubbishAlbum(this.mAlbumRepository);
        }
        this.mIsWorking = true;
        this.mAddNoMediaForRubbishAlbum.executeWith(new DisposableSubscriber<RubbishAlbumManualHideResult>() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumPresenter.4
            @Override // org.reactivestreams.Subscriber
            public void onNext(RubbishAlbumManualHideResult rubbishAlbumManualHideResult) {
                DefaultLogger.d(RubbishAlbumPresenter.TAG, "doAddNoMediaForRubbishAlbum, result: %s", rubbishAlbumManualHideResult.toString());
                DebugUtil.logEventTime("operationTrace", "add_no_media", true);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e(RubbishAlbumPresenter.TAG, "doAddNoMediaForRubbishAlbum onError. %s", th.toString());
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).exitActionMode();
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).update(RubbishAlbumPresenter.this.resetState(list));
            }

            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).exitActionMode();
                RubbishAlbumPresenter.this.mIsWorking = false;
                RubbishAlbumPresenter.this.setQueryUsecaseState(true);
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).update(RubbishAlbumPresenter.this.resetState(list));
            }
        }, linkedList, ((RubbishAlbumContract$V) getView()).getLifecycle());
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public void doRemoveNoMediaForRubbishAlbum(final List<BaseViewBean> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        DebugUtil.logEventTime("operationTrace", "remove_no_media", false);
        if (this.mRemoveNoMediaForRubbishAlbum == null) {
            this.mRemoveNoMediaForRubbishAlbum = new DoRemoveNoMediaForRubbishAlbum(this.mAlbumRepository);
        }
        this.mIsWorking = true;
        this.mRemoveNoMediaForRubbishAlbum.executeWith(new DisposableSubscriber<RubbishAlbumManualHideResult>() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumPresenter.5
            @Override // org.reactivestreams.Subscriber
            public void onNext(RubbishAlbumManualHideResult rubbishAlbumManualHideResult) {
                DefaultLogger.d(RubbishAlbumPresenter.TAG, "doRemoveNoMediaForRubbishAlbum, result: %s", rubbishAlbumManualHideResult.toString());
                DebugUtil.logEventTime("operationTrace", "remove_no_media", true);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e(RubbishAlbumPresenter.TAG, "doRemoveNoMediaForRubbishAlbum onError. %s", th.toString());
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).exitActionMode();
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).update(RubbishAlbumPresenter.this.resetState(list));
            }

            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).exitActionMode();
                RubbishAlbumPresenter.this.mIsWorking = false;
                RubbishAlbumPresenter.this.setQueryUsecaseState(true);
                ((RubbishAlbumContract$V) RubbishAlbumPresenter.this.getView()).update(RubbishAlbumPresenter.this.resetState(list));
            }
        }, list, ((RubbishAlbumContract$V) getView()).getLifecycle());
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        super.onDestroy();
        this.mQueryRubbishAlbumList = null;
        this.mDoChangeAlbumShowRubbishAlbum = null;
        this.mAddNoMediaForRubbishAlbum = null;
        this.mRemoveNoMediaForRubbishAlbum = null;
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public void classifyCheckState(List<Integer> list, List<BaseViewBean> list2, List<BaseViewBean> list3, List<BaseViewBean> list4) {
        for (int i = 0; i < this.mRubbishAlbumStatus.size(); i++) {
            if (list.contains(Integer.valueOf(i)) && !this.mRubbishAlbumStatus.get(i)) {
                list3.add(list2.get(i));
            } else if (!list.contains(Integer.valueOf(i)) && this.mRubbishAlbumStatus.get(i)) {
                list4.add(list2.get(i));
            }
        }
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public InitState getInitState() {
        return this.mInitState;
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public void onStartChoiceMode() {
        setQueryUsecaseState(false);
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P
    public void onStopChoiceMode() {
        if (!this.mIsWorking) {
            setQueryUsecaseState(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<BaseViewBean> resetState(List<BaseViewBean> list) {
        Iterator<BaseViewBean> it = list.iterator();
        while (it.hasNext()) {
            RubbishItemItemViewBean rubbishItemItemViewBean = (RubbishItemItemViewBean) it.next();
            rubbishItemItemViewBean.setManualHide(NoMediaUtil.isManualHideAlbum(((Album) rubbishItemItemViewBean.getSource()).getLocalPath()));
        }
        return list;
    }

    /* loaded from: classes2.dex */
    public class RubbishAlbumActionModeInitState implements InitState {
        public RubbishAlbumActionModeInitState() {
        }

        @Override // com.miui.gallery.widget.InitState
        public SparseBooleanArray getInitState(RecyclerView.Adapter adapter) {
            List datas = ((AsyncDiffEpoxyAdapter) adapter).getDatas();
            RubbishAlbumPresenter.this.mRubbishAlbumStatus = new SparseBooleanArray(datas.size());
            for (int i = 0; i < datas.size(); i++) {
                RubbishAlbumPresenter.this.mRubbishAlbumStatus.put(i, ((RubbishItemItemViewBean) datas.get(i)).isManualHide());
            }
            return RubbishAlbumPresenter.this.mRubbishAlbumStatus;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setQueryUsecaseState(boolean z) {
        HotUseCase hotUseCase = this.mQueryRubbishAlbumList;
        if (hotUseCase == null) {
            return;
        }
        if (z) {
            hotUseCase.onStart();
        } else {
            hotUseCase.onStop();
        }
    }
}
