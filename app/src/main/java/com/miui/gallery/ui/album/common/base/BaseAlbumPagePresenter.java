package com.miui.gallery.ui.album.common.base;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Pair;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.common.BaseItemModel;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.base_optimization.support.UtilsMethodSupportDelegate;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.share.Path;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils;
import com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V;
import com.miui.gallery.ui.album.common.base.requestbean.BaseOperationAlbumRequestBean;
import com.miui.gallery.ui.album.common.usecase.DoChangeAlbumBackupCase;
import com.miui.gallery.ui.album.common.usecase.DoChangeAlbumHideStatusCase;
import com.miui.gallery.ui.album.common.usecase.DoChangeAlbumShowInPhotoTabCase;
import com.miui.gallery.ui.album.common.viewbean.ShareAlbumViewBean;
import com.miui.gallery.ui.album.common.viewbean.SystemAlbumViewBean;
import com.miui.gallery.ui.album.main.usecase.DoChangeAlbumSortTypeCase;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import com.miui.gallery.ui.album.rubbishalbum.usecase.DoAddNoMediaForAlbum;
import com.miui.gallery.ui.album.rubbishalbum.usecase.DoChangeAlbumShowInOtherAlbum;
import com.miui.gallery.ui.album.rubbishalbum.usecase.DoChangeAlbumShowInRubbish;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import com.xiaomi.stat.MiStat;
import io.reactivex.subscribers.DisposableSubscriber;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class BaseAlbumPagePresenter<V extends BaseAlbumPageContract$V> extends BaseAlbumPageContract$P<V> {
    private static final String TAG = "BaseAlbumPagePresenter";
    public AbstractAlbumRepository mAlbumRepository = (AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY);
    private Album mAlbumSource;

    public static /* synthetic */ void $r8$lambda$iFF6NWkjEujuLPubVbGMsygQJOc(long[] jArr) {
        lambda$removeAlbumDetailSortInSp$0(jArr);
    }

    public static /* synthetic */ BaseViewBean $r8$lambda$mM9bT_eMyi2ccmYfDF_v7vp5wFg(BaseAlbumPagePresenter baseAlbumPagePresenter, long j) {
        return baseAlbumPagePresenter.lambda$removeData$1(j);
    }

    public DoChangeAlbumSortTypeCase.IllegaDataHandler getChangeAlbumSortTypeIllegaDataHandler() {
        return null;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void setCurrentAlbum(Album album) {
        if (album == null) {
            return;
        }
        this.mAlbumSource = album;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public Album getAlbumEntity(BaseViewBean baseViewBean) {
        if (baseViewBean != null && (baseViewBean instanceof CommonAlbumItemViewBean)) {
            DTO source = ((CommonAlbumItemViewBean) baseViewBean).getSource();
            if (source instanceof Album) {
                return (Album) source;
            }
        }
        return null;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public Album getAlbumEntity(EpoxyModel epoxyModel) {
        if (!(epoxyModel instanceof BaseItemModel)) {
            return null;
        }
        Object itemData = ((BaseItemModel) epoxyModel).getItemData();
        if (itemData instanceof BaseViewBean) {
            BaseViewBean baseViewBean = (BaseViewBean) itemData;
            if (baseViewBean.getSource() instanceof Album) {
                return (Album) baseViewBean.getSource();
            }
        }
        return null;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public Album getAlbumEntity() {
        return this.mAlbumSource;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public long getAlbumId() {
        if (getAlbumEntity() == null) {
            return -1L;
        }
        return getAlbumEntity().getAlbumId();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public String getAlbumName(long j) {
        if (getAlbumEntity() == null) {
            return null;
        }
        return getAlbumEntity().getAlbumName();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isAutoBackupAlbum() {
        return getAlbumEntity() != null && getAlbumEntity().isAutoUploadedAlbum();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isOtherShareAlbum() {
        return getAlbumEntity() != null && getAlbumEntity().isOtherShareAlbum();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public Path getSharePathInfo() {
        if (getAlbumEntity() == null) {
            return null;
        }
        return new Path(getAlbumEntity().getAlbumId(), getAlbumEntity().isOtherShareAlbum(), getAlbumEntity().isBabyAlbum());
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isForceTypeTime() {
        return getAlbumEntity() != null && getAlbumEntity().isForceTypeTime();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isSystemAlbum() {
        return getAlbumEntity() != null && getAlbumEntity().isSystemAlbum();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isCloudSyncEnable() {
        return SyncUtil.isGalleryCloudSyncable(GalleryApp.sGetAndroidContext());
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isScreenshotsAlbum() {
        return getAlbumEntity() != null && getAlbumEntity().isScreenshotsAlbum();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isHaveAlbumBean() {
        return getAlbumEntity() != null;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isOtherAlbum() {
        return getAlbumEntity() != null && getAlbumEntity().isOtherAlbum();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isShowedPhotosTabAlbum() {
        return getAlbumEntity() != null && getAlbumEntity().isShowedPhotosTabAlbum();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isHiddenAlbum() {
        return getAlbumEntity() != null && getAlbumEntity().isHiddenAlbum();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isBabyAlbum() {
        return getAlbumEntity() != null && getAlbumEntity().isBabyAlbum();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean isManualRenameRestricted() {
        return getAlbumEntity() != null && getAlbumEntity().isManualRenameRestricted();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumHiddenStatus(boolean z, Album... albumArr) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Object[]) albumArr)) {
            ((BaseAlbumPageContract$V) getView()).onChangeAlbumHideStatusFailed(-1, z, null);
        } else {
            doChangeAlbumHiddenStatus(z, CollectionUtils.arrayToCollection(albumArr));
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumHiddenStatus(final boolean z, final Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            ((BaseAlbumPageContract$V) getView()).onChangeAlbumHideStatusFailed(-1, z, collection);
            TimeMonitor.cancelTimeMonitor("403.7.0.1.13789");
            return;
        }
        final long[] operationAlbumIds = getOperationAlbumIds(collection);
        final long logEventTime = DebugUtil.logEventTime("operationTrace", z ? "hide_album" : "unhide_album", false);
        new DoChangeAlbumHideStatusCase(this.mAlbumRepository).execute(new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.1
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            {
                BaseAlbumPagePresenter.this = this;
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(Boolean bool) {
                DebugUtil.logEventTime("operationTrace", z ? "hide_album" : "unhide_album", logEventTime);
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onChangeAlbumHideStatusSuccess(operationAlbumIds, z, collection);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onChangeAlbumHideStatusFailed(0, z, collection);
            }
        }, new BaseOperationAlbumRequestBean(operationAlbumIds, z));
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsBackupStatus(boolean z, Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            ((BaseAlbumPageContract$V) getView()).onChangeAlbumBackupStatusFailed(-1, z, collection);
        } else {
            internalChangeAlbumsBackupStatu(z, collection, getOperationAlbumIds(collection));
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsBackupStatus(boolean z, Album... albumArr) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Object[]) albumArr)) {
            ((BaseAlbumPageContract$V) getView()).onChangeAlbumBackupStatusFailed(-1, z, null);
        } else {
            doChangeAlbumsBackupStatus(z, CollectionUtils.arrayToCollection(albumArr));
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsBackupStatus(boolean z, long... jArr) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty(jArr)) {
            ((BaseAlbumPageContract$V) getView()).onChangeAlbumBackupStatusFailed(-1, z, null);
        } else {
            internalChangeAlbumsBackupStatu(z, null, jArr);
        }
    }

    private void internalChangeAlbumsBackupStatu(final boolean z, final Collection<Album> collection, final long... jArr) {
        final long logEventTime = DebugUtil.logEventTime("operationTrace", z ? "auto_upload_enable" : "auto_upload_disable", false);
        new DoChangeAlbumBackupCase(this.mAlbumRepository).execute(new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.2
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            {
                BaseAlbumPagePresenter.this = this;
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(Boolean bool) {
                DebugUtil.logEventTime("operationTrace", z ? "auto_upload_enable" : "auto_upload_disable", logEventTime);
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onChangeAlbumBackupStatusSuccess(jArr, z, collection);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onChangeAlbumBackupStatusFailed(0, z, collection);
            }
        }, new BaseOperationAlbumRequestBean(jArr, z));
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsShowInPhotosTab(boolean z, Album... albumArr) {
        if (!UtilsMethodSupportDelegate.getInstance().isEmpty((Object[]) albumArr)) {
            doChangeAlbumsShowInPhotosTab(z, CollectionUtils.arrayToCollection(albumArr));
        } else if (z) {
            ((BaseAlbumPageContract$V) getView()).onAlbumShowInPhotoTabFailed(-1, null);
        } else {
            ((BaseAlbumPageContract$V) getView()).onCancelAlbumShowInPhotoTabFailed(-1, null);
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsShowInPhotosTab(final boolean z, final Collection<Album> collection) {
        if (!UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            final long[] operationAlbumIds = getOperationAlbumIds(collection);
            final long logEventTime = DebugUtil.logEventTime("operationTrace", z ? "show_in_home_enable" : "show_in_home_disable", false);
            new DoChangeAlbumShowInPhotoTabCase(this.mAlbumRepository).execute(new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.3
                @Override // org.reactivestreams.Subscriber
                public void onComplete() {
                }

                {
                    BaseAlbumPagePresenter.this = this;
                }

                @Override // org.reactivestreams.Subscriber
                public void onNext(Boolean bool) {
                    DebugUtil.logEventTime("operationTrace", z ? "show_in_home_enable" : "show_in_home_disable", logEventTime);
                    if (z) {
                        ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onAlbumShowInPhotoTabSuccess(operationAlbumIds, collection);
                    } else {
                        ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onCancelAlbumShowInPhotoTabSuccess(operationAlbumIds, collection);
                    }
                }

                @Override // org.reactivestreams.Subscriber
                public void onError(Throwable th) {
                    if (z) {
                        ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onAlbumShowInPhotoTabFailed(0, collection);
                    } else {
                        ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onCancelAlbumShowInPhotoTabFailed(0, collection);
                    }
                }
            }, new BaseOperationAlbumRequestBean(operationAlbumIds, z));
        } else if (z) {
            ((BaseAlbumPageContract$V) getView()).onAlbumShowInPhotoTabFailed(-1, collection);
        } else {
            ((BaseAlbumPageContract$V) getView()).onCancelAlbumShowInPhotoTabFailed(-1, collection);
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsShowInOtherAlbum(boolean z, Album... albumArr) {
        if (!UtilsMethodSupportDelegate.getInstance().isEmpty((Object[]) albumArr)) {
            doChangeAlbumsShowInOtherAlbum(z, CollectionUtils.arrayToCollection(albumArr));
        } else if (z) {
            ((BaseAlbumPageContract$V) getView()).onRemoveAlbumsFromOtherAlbumsFailed(-1, null);
        } else {
            ((BaseAlbumPageContract$V) getView()).onMoveAlbumToOtherAlbumsFailed(-1, null);
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsShowInOtherAlbum(final boolean z, final Collection<Album> collection) {
        if (!UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            final long logEventTime = DebugUtil.logEventTime("operationTrace", z ? "remove_from_other_albums" : "move_to_other_albums", false);
            final long[] operationAlbumIds = getOperationAlbumIds(collection);
            new DoChangeAlbumShowInOtherAlbum(this.mAlbumRepository).execute(new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.4
                @Override // org.reactivestreams.Subscriber
                public void onComplete() {
                }

                {
                    BaseAlbumPagePresenter.this = this;
                }

                @Override // org.reactivestreams.Subscriber
                public void onNext(Boolean bool) {
                    DebugUtil.logEventTime("operationTrace", z ? "remove_from_other_albums" : "move_to_other_albums", logEventTime);
                    if (z) {
                        ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onMoveAlbumToOtherAlbumsSuccess(operationAlbumIds, collection);
                    } else {
                        ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onRemoveAlbumsFromOtherAlbumsSuccess(operationAlbumIds, collection);
                    }
                }

                @Override // org.reactivestreams.Subscriber
                public void onError(Throwable th) {
                    if (z) {
                        ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onRemoveAlbumsFromOtherAlbumsFailed(0, collection);
                    } else {
                        ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onMoveAlbumToOtherAlbumsFailed(0, collection);
                    }
                }
            }, new BaseOperationAlbumRequestBean(operationAlbumIds, z));
        } else if (z) {
            TimeMonitor.cancelTimeMonitor("403.7.0.1.13791");
            ((BaseAlbumPageContract$V) getView()).onRemoveAlbumsFromOtherAlbumsFailed(-1, collection);
        } else {
            TimeMonitor.cancelTimeMonitor("403.40.0.1.13794");
            ((BaseAlbumPageContract$V) getView()).onMoveAlbumToOtherAlbumsFailed(-1, collection);
        }
    }

    public void trackAlbumOperation(String str, String str2, int i, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        hashMap.put("success", Boolean.valueOf(z));
        TrackController.trackClick(hashMap);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doDeleteAlbums(final int i, final Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            ((BaseAlbumPageContract$V) getView()).onDeleteAlbumsFailed(-1, i, collection);
            return;
        }
        final long[] operationAlbumIds = getOperationAlbumIds(collection);
        DeletionTask.Param param = new DeletionTask.Param(operationAlbumIds, i, 22);
        DeletionTask deletionTask = new DeletionTask();
        deletionTask.setFragmentActivityForStoragePermissionMiss(((BaseAlbumPageContract$V) getView()).getSafeActivity());
        deletionTask.setOnDeletionCompleteListener(new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.5
            {
                BaseAlbumPagePresenter.this = this;
            }

            @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
            public void onDeleted(int i2, long[] jArr) {
                if (i2 >= 0) {
                    ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onDeleteAlbumsSuccess(operationAlbumIds, i, collection);
                    BaseAlbumPagePresenter.this.removeAlbumDetailSortInSp(operationAlbumIds);
                    return;
                }
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onDeleteAlbumsFailed(0, i, collection);
            }
        });
        deletionTask.showProgress(((BaseAlbumPageContract$V) getView()).getSafeActivity());
        deletionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
    }

    public void removeAlbumDetailSortInSp(final long[] jArr) {
        if (jArr == null || jArr.length == 0) {
            return;
        }
        ThreadManager.execute(47, new Runnable() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BaseAlbumPagePresenter.$r8$lambda$iFF6NWkjEujuLPubVbGMsygQJOc(jArr);
            }
        });
    }

    public static /* synthetic */ void lambda$removeAlbumDetailSortInSp$0(long[] jArr) {
        List list = (List) Arrays.stream(jArr).boxed().collect(Collectors.toList());
        if (list != null) {
            GalleryPreferences.Album.removeUserCreateAlbumSort(list);
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doDeleteAlbums(int i, Album... albumArr) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Object[]) albumArr)) {
            ((BaseAlbumPageContract$V) getView()).onDeleteAlbumsFailed(-1, i, null);
        } else {
            doDeleteAlbums(i, CollectionUtils.arrayToCollection(albumArr));
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsMoveToRubbishAlbums(final boolean z, final Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            ((BaseAlbumPageContract$V) getView()).onAlbumMoveToRubbishAlbumsFailed(-1, z, collection);
            return;
        }
        final long logEventTime = DebugUtil.logEventTime("operationTrace", z ? "move_to_rubbish_albums" : "remove_from_rubbish_albums", false);
        final long[] array = collection.stream().filter(new Predicate<Album>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.7
            {
                BaseAlbumPagePresenter.this = this;
            }

            @Override // java.util.function.Predicate
            public boolean test(Album album) {
                return !album.isShareAlbum() && !album.isShareToDevice();
            }
        }).mapToLong(new ToLongFunction<Album>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.6
            {
                BaseAlbumPagePresenter.this = this;
            }

            @Override // java.util.function.ToLongFunction
            public long applyAsLong(Album album) {
                return album.getAlbumId();
            }
        }).toArray();
        if (array != null && array.length != collection.size()) {
            ((BaseAlbumPageContract$V) getView()).onAlbumMoveToRubbishAlbumsFailed(1000, z, collection);
        } else {
            new DoChangeAlbumShowInRubbish(this.mAlbumRepository).execute(new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.8
                @Override // org.reactivestreams.Subscriber
                public void onComplete() {
                }

                {
                    BaseAlbumPagePresenter.this = this;
                }

                @Override // org.reactivestreams.Subscriber
                public void onNext(Boolean bool) {
                    DebugUtil.logEventTime("operationTrace", z ? "move_to_rubbish_albums" : "remove_from_rubbish_albums", logEventTime);
                    ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onAlbumMoveToRubbishAlbumsSuccess(array, collection);
                }

                @Override // org.reactivestreams.Subscriber
                public void onError(Throwable th) {
                    ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onAlbumMoveToRubbishAlbumsFailed(0, z, collection);
                }
            }, new BaseOperationAlbumRequestBean(array, z));
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumsMoveToRubbishAlbums(boolean z, Album... albumArr) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Object[]) albumArr)) {
            return;
        }
        doChangeAlbumsMoveToRubbishAlbums(z, CollectionUtils.arrayToCollection(albumArr));
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public boolean doAddNoMediaForAlbums(final Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            ((BaseAlbumPageContract$V) getView()).onAddNoMediaForAlbumFailed(-1, collection);
            return false;
        }
        final long logEventTime = DebugUtil.logEventTime("operationTrace", "add_no_media", false);
        LinkedList<String> linkedList = new LinkedList();
        for (Album album : collection) {
            linkedList.add(album.getLocalPath());
        }
        if (!BaseMiscUtil.isValid(linkedList)) {
            ((BaseAlbumPageContract$V) getView()).onAddNoMediaForAlbumFailed(-1, collection);
            return false;
        }
        LinkedList linkedList2 = new LinkedList();
        for (String str : linkedList) {
            String[] absolutePath = StorageUtils.getAbsolutePath(((BaseAlbumPageContract$V) getView()).getContext(), StorageUtils.ensureCommonRelativePath(str));
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
                StorageSolutionProvider.get().requestPermission(((BaseAlbumPageContract$V) getView()).getSafeActivity(), permissionResult.path, permissionResult.type);
            }
        }
        if (!z) {
            return false;
        }
        new DoAddNoMediaForAlbum(this.mAlbumRepository).executeWith(new DisposableSubscriber<RubbishAlbumManualHideResult>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.9
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            {
                BaseAlbumPagePresenter.this = this;
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(RubbishAlbumManualHideResult rubbishAlbumManualHideResult) {
                DebugUtil.logEventTime("operationTrace", "add_no_media", logEventTime);
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onAddNoMediaForAlbumSuccess(collection, rubbishAlbumManualHideResult);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e("operationTrace", "[Add NoMedia] operation error,result : [%s]", th.toString());
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onAddNoMediaForAlbumFailed(0, collection);
            }
        }, linkedList, ((BaseAlbumPageContract$V) getView()).getLifecycle());
        return true;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doReplaceAlbumCover(long j, Long l) {
        BaseViewBean findDataById = ((BaseAlbumPageContract$V) getView()).findDataById(j);
        if (findDataById instanceof CommonAlbumItemViewBean) {
            DTO source = ((CommonAlbumItemViewBean) findDataById).getSource();
            if (!(source instanceof Album)) {
                return;
            }
            doReplaceAlbumCover((Album) source, l);
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doReplaceAlbumCover(Album album, Long l) {
        doReplaceAlbumCover(Collections.singleton(album), l);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doReplaceAlbumCover(Collection<Album> collection, Long l) {
        if (collection == null) {
            return;
        }
        ReplaceAlbumCoverUtils.doReplaceAlbumCover(collection, l.longValue(), collection.size() > 1, (GalleryFragment) getView(), getReplaceAlbumCoverCallBack(collection));
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public ReplaceAlbumCoverUtils.CallBack getReplaceAlbumCoverCallBack(final Collection<Album> collection) {
        return new ReplaceAlbumCoverUtils.CallBack() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.10
            {
                BaseAlbumPagePresenter.this = this;
            }

            @Override // com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.CallBack
            public void onSuccess(List<Pair<Album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> list) {
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onReplaceAlbumCoverIsSuccess(collection, list);
            }

            @Override // com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.CallBack
            public void onFailed(Collection<Album> collection2, long j) {
                ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onReplaceAlbumCoverIsFailed(collection2, Long.valueOf(j));
            }
        };
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void doChangeAlbumSortType(List<BaseViewBean> list, int i) {
        if (list == null || list.isEmpty() || -1 == i) {
            DefaultLogger.e(TAG, "doChangeAlbumSortType invalid args");
        } else {
            new DoChangeAlbumSortTypeCase().executeWith(new DisposableSubscriber<DoChangeAlbumSortTypeCase.SortResult>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter.11
                {
                    BaseAlbumPagePresenter.this = this;
                }

                @Override // org.reactivestreams.Subscriber
                public void onNext(DoChangeAlbumSortTypeCase.SortResult sortResult) {
                    ((BaseAlbumPageContract$V) BaseAlbumPagePresenter.this.getView()).onChangeAlbumSortTypeSuccess(sortResult);
                }

                @Override // org.reactivestreams.Subscriber
                public void onError(Throwable th) {
                    DefaultLogger.e(BaseAlbumPagePresenter.TAG, "changeAlbumSortType error %s.", th.toString());
                }

                @Override // org.reactivestreams.Subscriber
                public void onComplete() {
                    DefaultLogger.v(BaseAlbumPagePresenter.TAG, "changeAlbumSortType complete.");
                }
            }, new DoChangeAlbumSortTypeCase.RequestParam(getChangeAlbumSortTypeIllegaDataHandler(), list), ((BaseAlbumPageContract$V) getView()).getLifecycle());
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public int getOperationAlbumsPhotoCount(Collection<Album> collection) {
        int i = 0;
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            return 0;
        }
        for (Album album : collection) {
            if (album != null) {
                i += album.getPhotoCount();
            }
        }
        return i;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public long[] getOperationAlbumIds(Collection<Album> collection) {
        int i = 0;
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            return new long[0];
        }
        long[] jArr = new long[collection.size()];
        for (Album album : collection) {
            jArr[i] = album.getAlbumId();
            i++;
        }
        return jArr;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void addAlbum(Album album) {
        if (getView() == 0 || album == null) {
            return;
        }
        CommonAlbumItemViewBean commonAlbumItemViewBean = new CommonAlbumItemViewBean();
        commonAlbumItemViewBean.mapping((CommonAlbumItemViewBean) album);
        ((BaseAlbumPageContract$V) getView()).addData(commonAlbumItemViewBean);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void removeData(long[] jArr) {
        if (jArr == null || jArr.length == 0 || getView() == 0) {
            return;
        }
        ((BaseAlbumPageContract$V) getView()).removeDatas((List) Arrays.stream(jArr).parallel().mapToObj(new LongFunction() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter$$ExternalSyntheticLambda1
            @Override // java.util.function.LongFunction
            public final Object apply(long j) {
                return BaseAlbumPagePresenter.$r8$lambda$mM9bT_eMyi2ccmYfDF_v7vp5wFg(BaseAlbumPagePresenter.this, j);
            }
        }).collect(Collectors.toList()));
    }

    public /* synthetic */ BaseViewBean lambda$removeData$1(long j) {
        return ((BaseAlbumPageContract$V) getView()).findDataById(j);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void updateAlbumName(long j, String str) {
        if (!TextUtils.isEmpty(str) && getView() != 0) {
            BaseViewBean findDataById = ((BaseAlbumPageContract$V) getView()).findDataById(j);
            if (!(findDataById instanceof CommonAlbumItemViewBean)) {
                return;
            }
            CommonAlbumItemViewBean commonAlbumItemViewBean = new CommonAlbumItemViewBean((CommonAlbumItemViewBean) findDataById);
            commonAlbumItemViewBean.set(47, str);
            commonAlbumItemViewBean.set(79, null);
            ((BaseAlbumPageContract$V) getView()).updateData(((BaseAlbumPageContract$V) getView()).findDataIndexById(j), commonAlbumItemViewBean);
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void updateAlbumCover(Album album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult doReplaceAlbumCoverResult) {
        CommonAlbumItemViewBean commonAlbumItemViewBean;
        if (getView() != 0 && !Objects.isNull(album) && !Objects.isNull(doReplaceAlbumCoverResult)) {
            if (TextUtils.isEmpty(doReplaceAlbumCoverResult.getNowCoverPath()) && album.isVirtualAlbum()) {
                return;
            }
            album.setCoverPath(doReplaceAlbumCoverResult.getNowCoverPath());
            album.setCoverId(doReplaceAlbumCoverResult.getNowCoverId());
            album.setManualSetCover(doReplaceAlbumCoverResult.isManualSetCover().booleanValue());
            album.setCoverSyncState(doReplaceAlbumCoverResult.getCoverSyncState());
            if (album.isSystemAlbum()) {
                commonAlbumItemViewBean = new SystemAlbumViewBean();
            } else if (album.isShareAlbum()) {
                commonAlbumItemViewBean = new ShareAlbumViewBean();
            } else {
                commonAlbumItemViewBean = new CommonAlbumItemViewBean();
            }
            commonAlbumItemViewBean.mapping((CommonAlbumItemViewBean) album);
            ((BaseAlbumPageContract$V) getView()).updateDataById(album.getAlbumId(), commonAlbumItemViewBean);
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$P
    public void updateAlbumShowInPhotoTab(boolean z, long... jArr) {
        if (getView() == 0 || jArr == null) {
            return;
        }
        for (long j : jArr) {
            BaseViewBean findDataById = ((BaseAlbumPageContract$V) getView()).findDataById(j);
            if (CommonAlbumItemViewBean.class.isInstance(findDataById)) {
                CommonAlbumItemViewBean commonAlbumItemViewBean = (CommonAlbumItemViewBean) findDataById;
                if (!(commonAlbumItemViewBean.getSource() instanceof Album)) {
                    return;
                }
                Album album = (Album) commonAlbumItemViewBean.getSource();
                long attributes = album.getAttributes();
                if (z) {
                    album.setAttributes(attributes | 4);
                } else {
                    album.setAttributes(attributes & (-5));
                }
                ((BaseAlbumPageContract$V) getView()).updateData(((BaseAlbumPageContract$V) getView()).findDataIndexById(j), commonAlbumItemViewBean);
            }
        }
    }
}
