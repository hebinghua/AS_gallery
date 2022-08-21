package com.miui.gallery.ui;

import android.accounts.Account;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.qos.logback.core.joran.action.Action;
import com.google.common.collect.Lists;
import com.miui.account.AccountHelper;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AlbumDetailAdapter;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.adapter.IAlbumAdapter;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.picker.PickGalleryActivity;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.share.Path;
import com.miui.gallery.share.UIHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlbumDetailFragmentBase;
import com.miui.gallery.ui.CopyOrMoveDialog;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.ui.pictures.PinchCallback;
import com.miui.gallery.ui.pictures.PinchManager;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.viewmodel.AlbumDetailViewModel;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.miui.gallery.widget.TimerDialog;
import com.miui.gallery.widget.ViewDragListener;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.ClickEventInterceptCallback;
import com.miui.gallery.widget.recyclerview.EditableListSpanSizeProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.InterceptableRecyclerView;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.RecyclerViewUtils;
import com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import miui.cloud.util.SyncAutoSettingUtil;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public abstract class AlbumDetailFragmentBase<ADAPTER extends IAlbumAdapter> extends PhotoListFragmentBase<ADAPTER> implements DefaultLifecycleObserver, ViewDragListener.DropReceiver {
    public static final String SELECTION_ONLY_LOCAL;
    public static final HashMap<PictureViewMode, Integer> sViewModePreferenceMap;
    public boolean isDataFirstLoaded;
    public int[] mAfterDeletedFirstVisiblePosAndOffset;
    public ADAPTER mAlbumDetailAdapter;
    public String mAlbumLocalPath;
    public int mDailyAlbumDate;
    public Dialog mDialog;
    public EditableListViewWrapper.DragDataProvider mDragDataProvider;
    public EditableListViewWrapper mEditableWrapper;
    public boolean mEnableAutoUploadPending;
    public String mExtraSelection;
    public int mFromType;
    public int mInitialPhotoCount;
    public boolean mIsDailyAlbum;
    public boolean mIsFavoritesAlbum;
    public boolean mIsLocalAlbum;
    public boolean mIsOtherShareAlbum;
    public boolean mIsScreenRecorderAlbum;
    public boolean mIsScreenshotAlbum;
    public boolean mIsScreenshotRecorderAlbum;
    public boolean mIsShareAlbum;
    public boolean mIsShareToDevice;
    public boolean mIsVideoAlbum;
    public String mMediaIds;
    public int mPhotoCount;
    public PinchManager mPinchManager;
    public String mScreenshotAppName;
    public boolean mSharePending;
    public AlbumDetailViewModel mViewModel;
    public boolean mIsBabyAlbum = false;
    public boolean mIsInMultiWindowMode = false;
    public PictureViewMode mViewMode = PictureViewMode.MICRO_THUMB;
    public AlbumDetailFragmentBase<ADAPTER>.AlbumDetailMultiChoiceModeListener mChoiceModeListener = new AlbumDetailMultiChoiceModeListener();
    public AlbumDetailFragmentBase<ADAPTER>.AlbumDetailKeyboardShortcutCallback mShortcutCallBack = new AlbumDetailKeyboardShortcutCallback();

    public static /* synthetic */ void $r8$lambda$2MgLYAKqeuP1mbHEUrUNKiu09Qs(AlbumDetailFragmentBase albumDetailFragmentBase, long[] jArr, FragmentActivity fragmentActivity, int i) {
        albumDetailFragmentBase.lambda$doAddPhotos$4(jArr, fragmentActivity, i);
    }

    public static /* synthetic */ void $r8$lambda$89AyPPLDhkYaWUooD5uQZAIskWE(AlbumDetailFragmentBase albumDetailFragmentBase, int i) {
        albumDetailFragmentBase.lambda$onInflateView$0(i);
    }

    public static /* synthetic */ int $r8$lambda$WMB4VY_i2lhJXkeas4XV5cPOdXg(AlbumDetailFragmentBase albumDetailFragmentBase, int i) {
        return albumDetailFragmentBase.lambda$onInflateView$1(i);
    }

    public static /* synthetic */ int $r8$lambda$cqW5K9E9YwaQ8oOCn1sRsi2pO5o(AlbumDetailFragmentBase albumDetailFragmentBase, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        return albumDetailFragmentBase.lambda$onInflateView$2(recyclerView, view, i, j, f, f2);
    }

    /* renamed from: $r8$lambda$d6XvkLxIpDCW77rmY5d-ksV89zQ */
    public static /* synthetic */ void m1424$r8$lambda$d6XvkLxIpDCW77rmY5dksV89zQ(AlbumDetailFragmentBase albumDetailFragmentBase, int i) {
        albumDetailFragmentBase.lambda$onDataLoaded$3(i);
    }

    @Override // com.miui.gallery.widget.ViewDragListener.DropReceiver
    public boolean canReceive() {
        return true;
    }

    public abstract String getCreatorIdByPosition(int i);

    public RecyclerView.ItemAnimator getItemAnimator() {
        return null;
    }

    public boolean isPasteSupported() {
        return false;
    }

    public abstract boolean needEnableAutoUpload();

    public abstract void onEnterActionMode();

    public void onExitActionMode() {
    }

    static {
        HashMap<PictureViewMode, Integer> hashMap = new HashMap<>();
        sViewModePreferenceMap = hashMap;
        hashMap.put(PictureViewMode.LARGE_THUMB, 0);
        hashMap.put(PictureViewMode.MICRO_THUMB, 1);
        SELECTION_ONLY_LOCAL = " AND " + InternalContract$Cloud.ALIAS_LOCAL_MEDIA;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        Intent intent = this.mActivity.getIntent();
        this.mAlbumId = intent.getLongExtra("album_id", -1L);
        this.mAlbumLocalPath = intent.getStringExtra("album_local_path");
        if (bundle != null && !TextUtils.isEmpty(bundle.getString("album_name"))) {
            this.mAlbumName = bundle.getString("album_name");
        } else {
            this.mAlbumName = intent.getStringExtra("album_name");
        }
        this.mIsOtherShareAlbum = intent.getBooleanExtra("other_share_album", false);
        this.mIsLocalAlbum = intent.getBooleanExtra("is_local_album", false);
        this.mIsScreenshotAlbum = intent.getBooleanExtra("screenshot_album", false);
        this.mIsScreenRecorderAlbum = intent.getBooleanExtra("screenrecorder_album", false);
        this.mIsScreenshotRecorderAlbum = intent.getBooleanExtra("screenshot_recorder_album", false);
        this.mIsVideoAlbum = intent.getBooleanExtra("video_album", false);
        this.mScreenshotAppName = intent.getStringExtra("screenshot_app_name");
        this.mIsShareAlbum = this.mIsOtherShareAlbum || intent.getBooleanExtra("owner_share_album", false);
        this.mIsDailyAlbum = intent.getBooleanExtra("daily_album", false);
        this.mFromType = intent.getIntExtra("extra_from_type", -1);
        this.mMediaIds = intent.getStringExtra("media_ids");
        this.mIsShareToDevice = (intent.getLongExtra("attributes", 0L) & 1280) != 0;
        this.mExtraSelection = intent.getStringExtra("photo_selection");
        if (getActivity() != null) {
            this.mViewModel = (AlbumDetailViewModel) new ViewModelProvider(getActivity()).get(AlbumDetailViewModel.class);
        }
        if (this.mIsDailyAlbum) {
            int intExtra = intent.getIntExtra("daily_album_date", -1);
            this.mDailyAlbumDate = intExtra;
            if (intExtra <= 0) {
                finish();
            }
        }
        this.mIsFavoritesAlbum = intent.getBooleanExtra("favorites_album", false);
        initAlbumDetailSort();
        EditableListViewWrapper editableListViewWrapper = new EditableListViewWrapper(this.mRecyclerView);
        this.mEditableWrapper = editableListViewWrapper;
        if (this.mIsScreenshotAlbum || this.mIsScreenshotRecorderAlbum || this.mIsVideoAlbum) {
            editableListViewWrapper.disableScaleImageViewAniWhenInActionMode();
        }
        this.mEditableWrapper.enableChoiceMode(true);
        this.mEditableWrapper.enterChoiceModeWithLongClick(true);
        this.mEditableWrapper.setMultiChoiceModeListener(this.mChoiceModeListener);
        this.mEditableWrapper.setOnItemClickListener(getGridViewOnItemClickListener());
        GalleryGridLayoutManager galleryGridLayoutManager = new GalleryGridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        galleryGridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new EditableListSpanSizeProvider(this.mEditableWrapper, galleryGridLayoutManager)));
        this.mEditableWrapper.setLayoutManager(galleryGridLayoutManager);
        this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setOnFastScrollerStateChangedListener(new FastScrollerBar.OnStateChangedListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.widget.recyclerview.FastScrollerBar.OnStateChangedListener
            public final void onStateChanged(int i) {
                AlbumDetailFragmentBase.$r8$lambda$89AyPPLDhkYaWUooD5uQZAIskWE(AlbumDetailFragmentBase.this, i);
            }
        });
        this.mRecyclerView.setAdapterPos2ViewPosConverter(new GalleryRecyclerView.AdapterPos2ViewPosConverter() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.widget.recyclerview.GalleryRecyclerView.AdapterPos2ViewPosConverter
            public final int convert(int i) {
                return AlbumDetailFragmentBase.$r8$lambda$WMB4VY_i2lhJXkeas4XV5cPOdXg(AlbumDetailFragmentBase.this, i);
            }
        });
        this.mRecyclerView.setOnDragListener(new ViewDragListener(this));
        this.mEditableWrapper.setDragDataProvider(getDragDataProvider());
        this.mIsInMultiWindowMode = ActivityCompat.isInMultiWindowMode(this.mActivity);
        if (!this.mIsDailyAlbum) {
            this.mViewMode = PictureViewMode.getViewModeByOrdinal(GalleryPreferences.Album.getAlbumViewMode());
        }
        setPictureViewMode(this.mViewMode);
        if (!this.mViewMode.isAggregated()) {
            this.mRecyclerView.setItemAnimator(getItemAnimator());
        }
        this.mPinchManager = PinchManager.install(this.mRecyclerView, (ITransitionalAdapter) mo1564getAdapter(), new PinchCallback() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.1
            @Override // com.miui.gallery.ui.pictures.PinchCallback
            public boolean isTargetModeReachable(PictureViewMode pictureViewMode) {
                return true;
            }

            {
                AlbumDetailFragmentBase.this = this;
            }

            @Override // com.miui.gallery.ui.pictures.PinchCallback
            public boolean isPinchAvailable() {
                return !AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode();
            }

            @Override // com.miui.gallery.ui.pictures.PinchCallback
            public int[] unpackGroupIndexAndOffset(int i) {
                return AlbumDetailFragmentBase.this.mEditableWrapper.unpackGroupedPosition(i);
            }

            @Override // com.miui.gallery.ui.pictures.PinchCallback
            public void onPictureViewModeChanged(PictureViewMode pictureViewMode) {
                AlbumDetailFragmentBase.this.setPictureViewMode(pictureViewMode);
            }

            @Override // com.miui.gallery.ui.pictures.PinchCallback
            public void onTransitionFinish(boolean z) {
                RecyclerViewUtils.setChildVisibility(AlbumDetailFragmentBase.this.mRecyclerView, 0);
            }

            @Override // com.miui.gallery.ui.pictures.PinchCallback
            public void onPreTransition() {
                AlbumDetailFragmentBase.this.mRecyclerView.setItemAnimator(null);
            }

            @Override // com.miui.gallery.ui.pictures.PinchCallback
            public void onPostTransition() {
                if (!AlbumDetailFragmentBase.this.mViewMode.isAggregated()) {
                    AlbumDetailFragmentBase albumDetailFragmentBase = AlbumDetailFragmentBase.this;
                    albumDetailFragmentBase.mRecyclerView.setItemAnimator(albumDetailFragmentBase.getItemAnimator());
                }
            }
        }, this.mViewMode);
        this.mEditableWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.2
            {
                AlbumDetailFragmentBase.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                PinchManager pinchManager;
                if (AlbumDetailFragmentBase.this.mViewMode.isAggregated() && (pinchManager = AlbumDetailFragmentBase.this.mPinchManager) != null) {
                    pinchManager.zoomInBy(f + view.getLeft() + view.getTranslationX(), f2 + view.getTop() + view.getTranslationY());
                    return true;
                }
                return AlbumDetailFragmentBase.this.getGridViewOnItemClickListener().onItemClick(recyclerView, view, i, j, f, f2);
            }
        });
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        if (galleryRecyclerView instanceof InterceptableRecyclerView) {
            ((InterceptableRecyclerView) galleryRecyclerView).setClickEventInterceptCallback(new ClickEventInterceptCallback() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.widget.recyclerview.ClickEventInterceptCallback
                public final int onInterceptLongClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                    return AlbumDetailFragmentBase.$r8$lambda$cqW5K9E9YwaQ8oOCn1sRsi2pO5o(AlbumDetailFragmentBase.this, recyclerView, view, i, j, f, f2);
                }
            });
        }
        return onInflateView;
    }

    public /* synthetic */ void lambda$onInflateView$0(int i) {
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.reductionTouchView();
        }
    }

    public /* synthetic */ int lambda$onInflateView$1(int i) {
        return this.mEditableWrapper.getRawPosition(i);
    }

    public /* synthetic */ int lambda$onInflateView$2(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        if (!this.mViewMode.isAggregated()) {
            PinchManager pinchManager = this.mPinchManager;
            return (pinchManager == null || !pinchManager.isTransiting()) ? 2 : 1;
        }
        return 1;
    }

    public void setPictureViewMode(PictureViewMode pictureViewMode) {
        this.mViewMode = pictureViewMode;
        if (pictureViewMode.isAggregated()) {
            this.mRecyclerView.setItemAnimator(null);
        }
        ((IAlbumAdapter) mo1564getAdapter()).setViewMode(pictureViewMode, this);
        updateConfiguration(getResources().getConfiguration());
        adjustSpacing(pictureViewMode);
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.setItemAnimEnable(!pictureViewMode.isAggregated());
        }
        PinchManager pinchManager = this.mPinchManager;
        if (pinchManager != null) {
            pinchManager.changeMode(pictureViewMode);
        }
        HashMap<PictureViewMode, Integer> hashMap = sViewModePreferenceMap;
        if (hashMap.containsKey(pictureViewMode)) {
            GalleryPreferences.Album.setAlbumViewMode(hashMap.get(pictureViewMode).intValue());
        }
    }

    public final void adjustSpacing(PictureViewMode pictureViewMode) {
        int spacing = pictureViewMode.isAggregated() ? 0 : pictureViewMode.getSpacing();
        int spacing2 = pictureViewMode.getSpacing();
        GridItemSpacingDecoration gridItemSpacingDecoration = this.mSpacingDecoration;
        if (gridItemSpacingDecoration != null) {
            gridItemSpacingDecoration.setSpacing(spacing, spacing2);
        }
    }

    public void initAlbumDetailSort() {
        SortBy sortBy;
        String str = " ASC ";
        switch (GalleryPreferences.Album.getAlbumDetailSort(this.mAlbumId, -1)) {
            case 0:
                sortBy = SortBy.UPDATE_DATE;
                str = " DESC ";
                break;
            case 1:
                sortBy = SortBy.UPDATE_DATE;
                break;
            case 2:
                sortBy = SortBy.CREATE_DATE;
                str = " DESC ";
                break;
            case 3:
                sortBy = SortBy.CREATE_DATE;
                break;
            case 4:
                sortBy = SortBy.NAME;
                str = " DESC ";
                break;
            case 5:
                sortBy = SortBy.NAME;
                break;
            case 6:
                sortBy = SortBy.SIZE;
                str = " DESC ";
                break;
            case 7:
                sortBy = SortBy.SIZE;
                break;
            default:
                sortBy = getAlbumDetailDefaultSort();
                str = " DESC ";
                break;
        }
        this.mViewModel.initSortInfo(sortBy, str);
    }

    public SortBy getAlbumDetailDefaultSort() {
        if (isCameraAlbum() || this.mIsScreenshotAlbum || this.mIsScreenshotRecorderAlbum || Album.isUserCreateAlbum(this.mAlbumLocalPath)) {
            return SortBy.CREATE_DATE;
        }
        return SortBy.UPDATE_DATE;
    }

    public final void scanAlbumFolderIfNeeded() {
        String str;
        if (this.mIsOtherShareAlbum || isVirtualAlbum() || (str = this.mAlbumLocalPath) == null) {
            return;
        }
        ThreadManager.getMiscPool().submit(new AlbumScanTask(str, this.mAlbumId));
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public void onDataLoaded(final int i) {
        int[] iArr;
        super.onDataLoaded(i);
        if (!this.isDataFirstLoaded) {
            this.isDataFirstLoaded = true;
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AlbumDetailFragmentBase.m1424$r8$lambda$d6XvkLxIpDCW77rmY5dksV89zQ(AlbumDetailFragmentBase.this, i);
                }
            });
        }
        if (this.mPhotoCount > i && (iArr = this.mAfterDeletedFirstVisiblePosAndOffset) != null) {
            this.mRecyclerView.scrollToPositionWithOffset(iArr[0], iArr[1]);
            this.mAfterDeletedFirstVisiblePosAndOffset = null;
        }
        this.mPhotoCount = i;
        if (this.mInitialPhotoCount == 0 || i != 0 || isFavoritesAlbum() || this.mViewModel.isAlbumGroup()) {
            return;
        }
        DefaultLogger.w("AlbumDetailFragmentBase", "All medias are deleted, finish");
        finish();
    }

    public /* synthetic */ void lambda$onDataLoaded$3(int i) {
        if (!isDetached()) {
            onDataFirstBound(i);
        }
    }

    public void onDataFirstBound(int i) {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        scanAlbumFolderIfNeeded();
        this.mInitialPhotoCount = i;
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        consumePendingEvent();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        if (this.mInitialPhotoCount != 0 && this.mPhotoCount == 0) {
            doDeleteEmptyAlbum(this.mActivity, this.mAlbumId, this.mAlbumLocalPath, isUserAlbum());
        }
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        super.onDestroy();
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.dismiss();
        }
        PinchManager pinchManager = this.mPinchManager;
        if (pinchManager != null) {
            pinchManager.destroy();
        }
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, androidx.fragment.app.Fragment
    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        this.mIsInMultiWindowMode = z;
    }

    public EditableListViewWrapper.DragDataProvider getDragDataProvider() {
        if (mo1564getAdapter() == 0) {
            return null;
        }
        if (this.mDragDataProvider == null) {
            this.mDragDataProvider = new EditableListViewWrapper.DragDataProvider() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.3
                {
                    AlbumDetailFragmentBase.this = this;
                }

                @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.DragDataProvider
                public String getBestPathFromPosition(int i) {
                    return ((IAlbumAdapter) AlbumDetailFragmentBase.this.mo1564getAdapter()).getBestQualityPath(i);
                }

                @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.DragDataProvider
                public String getMimeTypeFromPosition(int i) {
                    return ((IAlbumAdapter) AlbumDetailFragmentBase.this.mo1564getAdapter()).getMimeType(i);
                }

                @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.DragDataProvider
                public boolean shouldStartDragInPhone() {
                    if (Build.VERSION.SDK_INT >= 29 && !AlbumDetailFragmentBase.this.isSecretAlbum()) {
                        return AlbumDetailFragmentBase.this.mIsInMultiWindowMode || ActivityCompat.isInFreeFormWindow(AlbumDetailFragmentBase.this.getActivity());
                    }
                    return false;
                }
            };
        }
        return this.mDragDataProvider;
    }

    public void consumePendingEvent() {
        if (this.mEnableAutoUploadPending) {
            if (SyncUtil.existXiaomiAccount(this.mActivity)) {
                if (SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
                    if (doChangeAutoUpload(true) && this.mSharePending) {
                        doShare();
                    }
                } else {
                    ToastUtils.makeText(this.mActivity, (int) R.string.sync_diabled_toast);
                }
            } else {
                ToastUtils.makeText(this.mActivity, (int) R.string.xiaomi_account_not_exists_toast);
            }
        }
        this.mEnableAutoUploadPending = false;
        this.mSharePending = false;
    }

    public boolean isSecretAlbum() {
        return this.mAlbumId == -1000;
    }

    public boolean isFromMap() {
        return getActivity() != null && getActivity().getIntent().getBooleanExtra("is_from_map", false);
    }

    public boolean isIgnoreLocalMode() {
        return getActivity() != null && getActivity().getIntent().getIntExtra("extra_from_type", -1) == 1001;
    }

    public boolean isDailyAlbum() {
        return this.mIsDailyAlbum;
    }

    public boolean isOthersShareAlbum() {
        return this.mIsOtherShareAlbum;
    }

    public boolean isShareAlbum() {
        return this.mIsShareAlbum;
    }

    public boolean isVideoAlbum() {
        return this.mAlbumId == 2147483647L;
    }

    public boolean isAllPhotosAlbum() {
        return this.mAlbumId == 2147483644;
    }

    public boolean isCustomQueryAlbum() {
        long j = this.mAlbumId;
        return j == 2147383646 || j == 2147383645;
    }

    public boolean isFavoritesAlbum() {
        return this.mAlbumId == 2147483642;
    }

    public boolean isVirtualAlbum() {
        return isVideoAlbum() || isSecretAlbum() || isDailyAlbum() || isFavoritesAlbum() || isAllPhotosAlbum() || isCustomQueryAlbum();
    }

    public boolean isCameraAlbum() {
        Intent intent = this.mActivity.getIntent();
        if (intent != null) {
            return TextUtils.equals(intent.getStringExtra("album_server_id"), String.valueOf(1L));
        }
        return false;
    }

    public boolean isSystemAlbum() {
        Intent intent = this.mActivity.getIntent();
        if (intent != null) {
            return Album.isSystemAlbum(intent.getStringExtra("album_server_id"));
        }
        return false;
    }

    public boolean isUserAlbum() {
        return !isSystemAlbum() && !isVirtualAlbum() && !isShareAlbum();
    }

    public final boolean isOnlyShowLocalMode() {
        return GalleryPreferences.LocalMode.isOnlyShowLocalPhoto();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String getSelection() {
        if (isOthersShareAlbum()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (isVideoAlbum()) {
            sb.append("serverType = ? AND ");
            sb.append("alias_hidden = ? AND ");
            sb.append("alias_rubbish = ? AND ");
            sb.append("localGroupId != ?");
        } else if (isAllPhotosAlbum()) {
            return GalleryPreferences.LocalMode.isOnlyShowLocalPhoto() ? AlbumDetailAdapter.ALL_PHOTOS_SELECTION_LOCAL_MEDIA : "sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000";
        } else if (isCustomQueryAlbum()) {
            if (!TextUtils.isEmpty(this.mExtraSelection)) {
                return this.mExtraSelection;
            }
            return isFromMap() ? "_id IN (?)" : "sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000 AND title LIKE ?";
        } else if (isDailyAlbum()) {
            sb.append("alias_create_date = ? AND ");
            sb.append("alias_hidden = ? AND ");
            sb.append("localGroupId != ?");
        } else if (isFavoritesAlbum()) {
            sb.append("alias_is_favorite = ? AND ");
            sb.append("alias_hidden = ? AND ");
            sb.append("alias_rubbish = ? AND ");
            sb.append("localGroupId != ?");
        } else if (this.mIsScreenshotAlbum) {
            if (!TextUtils.isEmpty(this.mScreenshotAppName)) {
                sb.append("location");
                sb.append(" = ? AND ");
            }
            sb.append("localGroupId = ?");
        } else if (this.mIsScreenshotRecorderAlbum) {
            sb.append("localGroupId = ?");
        } else {
            sb.append("localGroupId = ?");
        }
        if (isOnlyShowLocalMode() && !isSecretAlbum() && !isIgnoreLocalMode()) {
            sb.append(SELECTION_ONLY_LOCAL);
        }
        return sb.toString();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public Uri getUri() {
        return getUri(SortBy.NONE);
    }

    public Uri getUri(SortBy sortBy) {
        Uri uri;
        if (isOthersShareAlbum()) {
            uri = ContentUris.withAppendedId(GalleryContract.Media.URI_OTHER_ALBUM_MEDIA, this.mAlbumId);
        } else if (isAllPhotosAlbum() || isCustomQueryAlbum()) {
            uri = AlbumDetailAdapter.ALL_PHOTOS_URI;
        } else {
            uri = GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA;
        }
        return uri.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).appendQueryParameter("media_group_by", String.valueOf(7)).build();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String[] getSelectionArgs() {
        if (isVideoAlbum()) {
            return new String[]{String.valueOf(2), String.valueOf(0), String.valueOf(0), String.valueOf(-1000L)};
        }
        if (isOthersShareAlbum() || isAllPhotosAlbum()) {
            return null;
        }
        return isCustomQueryAlbum() ? isFromMap() ? new String[]{String.valueOf(this.mMediaIds)} : new String[]{String.valueOf(this.mAlbumName)} : isDailyAlbum() ? new String[]{String.valueOf(this.mDailyAlbumDate), String.valueOf(0), String.valueOf(-1000L)} : isFavoritesAlbum() ? new String[]{String.valueOf(1), String.valueOf(0), String.valueOf(0), String.valueOf(-1000L)} : (!this.mIsScreenshotAlbum || TextUtils.isEmpty(this.mScreenshotAppName)) ? (!this.mViewModel.isAlbumGroup() || getArguments() == null) ? new String[]{String.valueOf(this.mAlbumId)} : new String[]{String.valueOf(getArguments().getLong("childAlbumId"))} : new String[]{this.mScreenshotAppName, String.valueOf(this.mAlbumId)};
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.menu_add_photos) {
            pickPhotos();
            if (this.mIsBabyAlbum) {
                TrackController.trackClick("403.42.2.1.11293", AutoTracking.getRef());
            } else {
                TrackController.trackClick("403.15.2.1.11181", AutoTracking.getRef());
            }
            return true;
        } else if (itemId == R.id.menu_share) {
            showAlbumShareInfo();
            if (this.mIsBabyAlbum) {
                TrackController.trackClick("403.42.2.1.11294", AutoTracking.getRef());
            }
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }

    public final void pickPhotos() {
        Intent intent = new Intent(this.mActivity, PickGalleryActivity.class);
        intent.putExtra("pick-upper-bound", -1);
        intent.putExtra("pick-need-id", true);
        startActivityForResult(intent, 7);
    }

    public static boolean hasOtherShareMedia(long[] jArr) {
        for (long j : jArr) {
            if (ShareMediaManager.isOtherShareMediaId(j)) {
                return true;
            }
        }
        return false;
    }

    public void doAddPhotos(final long[] jArr) {
        if (isOthersShareAlbum()) {
            DefaultLogger.d("AlbumDetailFragmentBase", "Is other shared album, do copy operation for default");
            CopyMoveDialogFragment.show((FragmentActivity) this.mActivity, this.mAlbumId, jArr, false, (MediaAndAlbumOperations.OnAddAlbumListener) null);
            recordCopyMoveAction(false);
        } else if (hasOtherShareMedia(jArr)) {
            DefaultLogger.d("AlbumDetailFragmentBase", "Contains other's share media, do copy operation for default");
            CopyMoveDialogFragment.show((FragmentActivity) this.mActivity, this.mAlbumId, jArr, false, (MediaAndAlbumOperations.OnAddAlbumListener) null);
            recordCopyMoveAction(false);
        } else {
            CopyOrMoveDialog copyOrMoveDialog = new CopyOrMoveDialog();
            copyOrMoveDialog.setOnOperationSelectedListener(new CopyOrMoveDialog.OnOperationSelectedListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.CopyOrMoveDialog.OnOperationSelectedListener
                public final void onOperationSelected(FragmentActivity fragmentActivity, int i) {
                    AlbumDetailFragmentBase.$r8$lambda$2MgLYAKqeuP1mbHEUrUNKiu09Qs(AlbumDetailFragmentBase.this, jArr, fragmentActivity, i);
                }
            });
            copyOrMoveDialog.showAllowingStateLoss(getFragmentManager(), "CopyOrMoveDialog");
        }
    }

    public /* synthetic */ void lambda$doAddPhotos$4(long[] jArr, FragmentActivity fragmentActivity, int i) {
        if (i == 1) {
            CopyMoveDialogFragment.show((FragmentActivity) this.mActivity, this.mAlbumId, jArr, false, (MediaAndAlbumOperations.OnAddAlbumListener) null);
            recordCopyMoveAction(false);
        } else if (i == 2) {
        } else {
            CopyMoveDialogFragment.show((FragmentActivity) this.mActivity, this.mAlbumId, jArr, true, (MediaAndAlbumOperations.OnAddAlbumListener) null);
            recordCopyMoveAction(true);
        }
    }

    public void recordCopyMoveAction(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "move" + String.valueOf(z));
        hashMap.put("from", getClass().getSimpleName());
        SamplingStatHelper.recordCountEvent("organize_photos", "move_or_copy", hashMap);
    }

    public final void doShare() {
        UIHelper.showAlbumShareInfo(this.mActivity, new Path(this.mIsOtherShareAlbum ? ShareAlbumHelper.getOriginalAlbumId(this.mAlbumId) : this.mAlbumId, this.mIsOtherShareAlbum, ((IAlbumAdapter) mo1564getAdapter()).isBabyAlbum()), this.mIsOtherShareAlbum ? 6 : 54);
    }

    public final void showAlbumShareInfo() {
        if (needEnableAutoUpload()) {
            showDialog(R.string.auto_upload_before_share_title, R.string.auto_upload_before_share_message, 17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.4
                {
                    AlbumDetailFragmentBase.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    AlbumDetailFragmentBase.this.onEnableAutoUpload(true);
                }
            });
        } else {
            doShare();
        }
    }

    public void showDialog(int i, int i2, int i3, DialogInterface.OnClickListener onClickListener) {
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.dismiss();
        }
        AlertDialog create = new AlertDialog.Builder(this.mActivity).setTitle(i).setMessage(i2).setPositiveButton(i3, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        this.mDialog = create;
        create.show();
    }

    public void removeFromOtherAlbums() {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.operation_remove_from_other_albums).setMessage(R.string.remove_from_other_albums_tip).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.5
            {
                AlbumDetailFragmentBase.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AlbumDetailFragmentBase.this.doChangeShowInOtherAlbums(false);
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).show();
    }

    public void enableAutoUpload() {
        DialogUtil.showInfoDialog(getActivity(), (int) R.string.enable_auto_upload_tip, (int) R.string.enable_auto_upload_title, 17039370, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.6
            {
                AlbumDetailFragmentBase.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AlbumDetailFragmentBase.this.onEnableAutoUpload(false);
            }
        }, (DialogInterface.OnClickListener) null);
    }

    public void onEnableAutoUpload(final boolean z) {
        if (!SyncUtil.existXiaomiAccount(this.mActivity)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("cloud_guide_source", GalleryIntent$CloudGuideSource.AUTOBACKUP);
            bundle.putLong("autobackup_album_id", this.mAlbumId);
            IntentUtil.guideToLoginXiaomiAccount(this.mActivity, bundle);
            this.mEnableAutoUploadPending = true;
            this.mSharePending = z;
        } else if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
            showDialog(R.string.to_enable_sync_dialog_title, R.string.to_enable_sync_dialog_message, R.string.to_enable_sync_dialog_positive_button_text, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.7
                {
                    AlbumDetailFragmentBase.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    AlbumDetailFragmentBase.this.mActivity.startActivity(new Intent("com.xiaomi.action.MICLOUD_MAIN"));
                    AlbumDetailFragmentBase.this.mEnableAutoUploadPending = true;
                    AlbumDetailFragmentBase.this.mSharePending = z;
                }
            });
        } else if (!doChangeAutoUpload(true) || !z) {
        } else {
            doShare();
        }
    }

    public void doChangeShowInRubbishAlbums(boolean z) {
        MediaAndAlbumOperations.doChangeShowInRubbishAlbums(this.mActivity, new long[]{this.mAlbumId}, z, true);
        ToastUtils.makeTextLong(getActivity(), z ? R.string.show_in_rubbish_albums_enable_toast_long_press_menu : R.string.show_in_other_albums_disable_toast_long_press_menu);
    }

    public void doChangeShowInOtherAlbums(boolean z) {
        MediaAndAlbumOperations.doChangeShowInOtherAlbums(this.mActivity, this.mAlbumId, z);
        ToastUtils.makeTextLong(getActivity(), z ? R.string.show_in_other_albums_enable_toast_long_press_menu : R.string.show_in_other_albums_disable_toast_long_press_menu);
    }

    public void moveToOtherAlbums() {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.operation_move_to_other_albums).setMessage(R.string.move_to_other_albums_tip).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.8
            {
                AlbumDetailFragmentBase.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AlbumDetailFragmentBase.this.doChangeShowInOtherAlbums(true);
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).show();
    }

    public void disableAutoUpload() {
        DialogUtil.showInfoDialog(getActivity(), (int) R.string.disable_auto_upload_tip, (int) R.string.disable_auto_upload_title, 17039370, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.9
            {
                AlbumDetailFragmentBase.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AlbumDetailFragmentBase.this.doChangeAutoUpload(false);
            }
        }, (DialogInterface.OnClickListener) null);
    }

    public boolean doChangeAutoUpload(boolean z) {
        if (!z && (this.mIsShareAlbum || this.mIsShareToDevice)) {
            ToastUtils.makeText(this.mActivity, (int) R.string.share_album_needs_auto_upload_tip);
            return false;
        }
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(this.mActivity);
        if (xiaomiAccount == null) {
            return false;
        }
        if (z && !ContentResolver.getSyncAutomatically(xiaomiAccount, "com.miui.gallery.cloud.provider")) {
            if (!SyncUtil.setSyncAutomatically(this.mActivity, true)) {
                return false;
            }
            ((IAlbumAdapter) mo1564getAdapter()).updateGalleryCloudSyncableState();
        }
        MediaAndAlbumOperations.doChangeAutoUpload(this.mActivity, this.mAlbumId, z);
        ToastUtils.makeTextLong(this.mActivity, z ? R.string.auto_upload_enable_toast_long_press_menu : R.string.auto_upload_disable_toast_long_press_menu);
        return true;
    }

    public void doAddNoMediaForRubbishAlbum() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(this.mAlbumLocalPath);
        MediaAndAlbumOperations.doAddNoMediaForRubbishAlbum(linkedList);
    }

    public void moveToRubbishAlbums() {
        new TimerDialog.Builder(getActivity()).setTitle(R.string.operation_move_to_rubbish_albums).setMessage(R.string.move_to_rubbish_albums_tip).setCheckBox(R.string.add_nomedia_with_move_to_rubbish, false, 5000L).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.10
            {
                AlbumDetailFragmentBase.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (((TimerDialog) dialogInterface).isChecked()) {
                    AlbumDetailFragmentBase.this.doAddNoMediaForRubbishAlbum();
                }
                AlbumDetailFragmentBase.this.doChangeShowInRubbishAlbums(true);
            }
        }).setNegativeButton(17039360, null).build().show();
    }

    public void removeFromRubbishAlbums() {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.operation_remove_from_rubbish_albums).setMessage(R.string.remove_single_album_from_rubbish_page_tip).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.11
            {
                AlbumDetailFragmentBase.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AlbumDetailFragmentBase.this.doChangeShowInRubbishAlbums(false);
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).show();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        ArrayList arrayList;
        if (i == 6) {
            if (i2 != -1) {
                return;
            }
            finish();
            return;
        }
        if (i != 7) {
            if (i != 54) {
                super.onActivityResult(i, i2, intent);
            } else if (i2 == -1) {
                this.mIsShareToDevice = intent.getBooleanExtra("extra_is_share_to_device", false);
            }
        } else if (i2 == -1 && intent != null && (arrayList = (ArrayList) intent.getSerializableExtra("pick-result-data")) != null && !arrayList.isEmpty()) {
            long[] jArr = new long[arrayList.size()];
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                jArr[i3] = ((Long) arrayList.get(i3)).longValue();
            }
            doAddPhotos(jArr);
        }
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setPictureViewMode(this.mViewMode);
        this.mRecyclerView.scrollToPosition(this.mRecyclerView.findFirstVisibleItemPosition());
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public void updateConfiguration(Configuration configuration) {
        ((IAlbumAdapter) mo1564getAdapter()).setConfiguration(configuration);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) this.mRecyclerView.getLayoutManager();
        if (gridLayoutManager != null) {
            if (this.mViewMode.isAggregated()) {
                gridLayoutManager.setSpanCount(1);
            } else {
                gridLayoutManager.setSpanCount(this.mViewMode.getSpan());
            }
        }
        this.mRecyclerView.invalidateItemDecorations();
        ((IAlbumAdapter) mo1564getAdapter()).notifyDataChanged();
    }

    public final boolean canAddSecret() {
        return !isOthersShareAlbum();
    }

    /* loaded from: classes2.dex */
    public class AlbumDetailMultiChoiceModeListener implements MultiChoiceModeListener {
        public MenuItem mAddToAlbum;
        public MenuItem mCreativity;
        public MenuItem mDelete;
        public ActionMode mMode;
        public MenuItem mRemoveSecret;
        public MenuItem mSend;

        public static /* synthetic */ void $r8$lambda$4xUSgnhyR5HxtyQZl9mg24Iqrn0(AlbumDetailMultiChoiceModeListener albumDetailMultiChoiceModeListener) {
            albumDetailMultiChoiceModeListener.lambda$onActionItemClicked$1();
        }

        public static /* synthetic */ void $r8$lambda$VhTIqBwIv859lQ7vB_yHHLZA02c(AlbumDetailMultiChoiceModeListener albumDetailMultiChoiceModeListener, long[] jArr, boolean z) {
            albumDetailMultiChoiceModeListener.lambda$onActionItemClicked$0(jArr, z);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        public AlbumDetailMultiChoiceModeListener() {
            AlbumDetailFragmentBase.this = r1;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
            updateMenuState();
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void statGroupItemsCheckedStateChanged(boolean z) {
            String str;
            if (z) {
                if (AlbumDetailFragmentBase.this.isAllPhotosAlbum()) {
                    str = "403.44.3.1.11222";
                } else {
                    str = AlbumDetailFragmentBase.this.mIsBabyAlbum ? "403.42.3.1.11300" : "403.15.3.1.11193";
                }
                TrackController.trackClick(str, AlbumDetailFragmentBase.this.getEnterActionModeTip());
            }
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
            updateMenuState();
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            this.mMode = actionMode;
            if (AlbumDetailFragmentBase.this.isSecretAlbum()) {
                actionMode.getMenuInflater().inflate(R.menu.secret_page_menu, menu);
                this.mRemoveSecret = menu.findItem(R.id.remove_secret);
            } else {
                actionMode.getMenuInflater().inflate(R.menu.home_page_menu, menu);
                this.mAddToAlbum = menu.findItem(R.id.add_to_album);
                this.mCreativity = menu.findItem(R.id.action_produce);
                if (!GalleryPreferences.Assistant.isCreativityFunctionOn() || (AlbumDetailFragmentBase.this.isVideoAlbum() && !VlogEntranceUtils.isAvailable())) {
                    this.mCreativity.setVisible(false);
                }
            }
            MenuItem findItem = menu.findItem(R.id.delete);
            this.mDelete = findItem;
            if (findItem != null) {
                findItem.setVisible(true);
            }
            this.mSend = menu.findItem(R.id.action_send);
            SamplingStatHelper.recordCountEvent("album_detail", "action_mode_create");
            AlbumDetailFragmentBase.this.onEnterActionMode();
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {
            String str;
            String str2;
            if (menuItem.getItemId() != 16908313 && menuItem.getItemId() != 16908314) {
                LinearMotorHelper.performHapticFeedback(AlbumDetailFragmentBase.this.mActivity, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            }
            if (menuItem.getItemId() == 16908314) {
                if (AlbumDetailFragmentBase.this.isAllPhotosAlbum()) {
                    str2 = "403.44.3.1.11221";
                } else {
                    str2 = AlbumDetailFragmentBase.this.mIsBabyAlbum ? "403.42.3.1.11299" : "403.15.3.1.11192";
                }
                TrackController.trackClick(str2, AlbumDetailFragmentBase.this.getEnterActionModeTip());
            }
            switch (menuItem.getItemId()) {
                case R.id.action_produce /* 2131361895 */:
                    AppCompatActivity appCompatActivity = AlbumDetailFragmentBase.this.mActivity;
                    MediaAndAlbumOperations.OnCompleteListener onCompleteListener = new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.AlbumDetailMultiChoiceModeListener.1
                        {
                            AlbumDetailMultiChoiceModeListener.this = this;
                        }

                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public void onComplete(long[] jArr) {
                            List<CheckableAdapter.CheckedItem> checkedItems = AlbumDetailFragmentBase.this.mEditableWrapper.getCheckedItems();
                            int i = 0;
                            for (int i2 = 0; i2 < checkedItems.size(); i2++) {
                                if (MediaFeatureCacheManager.getInstance().isBestImage(checkedItems.get(i2).getId(), false, false, null)) {
                                    i++;
                                }
                            }
                            HashMap hashMap = new HashMap();
                            hashMap.put(MiStat.Param.COUNT, String.valueOf(checkedItems.size()));
                            hashMap.put("best_image_count", String.valueOf(i));
                            SamplingStatHelper.recordCountEvent("album_detail", "produce", hashMap);
                            actionMode.finish();
                        }
                    };
                    List<CheckableAdapter.CheckedItem> checkedItems = AlbumDetailFragmentBase.this.mEditableWrapper.getCheckedItems();
                    AlbumDetailFragmentBase albumDetailFragmentBase = AlbumDetailFragmentBase.this;
                    MediaAndAlbumOperations.doProduceCreation(appCompatActivity, onCompleteListener, checkedItems, albumDetailFragmentBase.mIsBabyAlbum, albumDetailFragmentBase.isAllPhotosAlbum());
                    return true;
                case R.id.action_send /* 2131361903 */:
                    AlbumDetailFragmentBase.this.onSend(null, null);
                    return true;
                case R.id.add_to_album /* 2131361918 */:
                    TimeMonitor.createNewTimeMonitor("403.26.0.1.13762");
                    if (AlbumDetailFragmentBase.this.isAllPhotosAlbum()) {
                        str = "403.44.3.1.11228";
                    } else {
                        str = AlbumDetailFragmentBase.this.mIsBabyAlbum ? "403.42.3.1.11304" : "403.15.3.1.11199";
                    }
                    TrackController.trackClick(str, AlbumDetailFragmentBase.this.getEnterActionModeTip(), AlbumDetailFragmentBase.this.mEditableWrapper.getCheckedItemCount());
                    TrackController.trackExpose("403.26.0.1.11240", AlbumDetailFragmentBase.this.getEnterActionModeTip());
                    MediaAndAlbumOperations.addToAlbum(AlbumDetailFragmentBase.this.mActivity, new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase$AlbumDetailMultiChoiceModeListener$$ExternalSyntheticLambda0
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                        public final void onComplete(long[] jArr, boolean z) {
                            AlbumDetailFragmentBase.AlbumDetailMultiChoiceModeListener.$r8$lambda$VhTIqBwIv859lQ7vB_yHHLZA02c(AlbumDetailFragmentBase.AlbumDetailMultiChoiceModeListener.this, jArr, z);
                        }
                    }, new MediaAndAlbumOperations.OnPicToPdfClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase$AlbumDetailMultiChoiceModeListener$$ExternalSyntheticLambda1
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnPicToPdfClickListener
                        public final void onPicToPdfClick() {
                            AlbumDetailFragmentBase.AlbumDetailMultiChoiceModeListener.$r8$lambda$4xUSgnhyR5HxtyQZl9mg24Iqrn0(AlbumDetailFragmentBase.AlbumDetailMultiChoiceModeListener.this);
                        }
                    }, ShareAlbumHelper.isOtherShareAlbumId(AlbumDetailFragmentBase.this.mAlbumId), false, AlbumDetailFragmentBase.this.canAddSecret(), AlbumDetailFragmentBase.this.mEditableWrapper.isCheckedItemContainVideo(), MiscUtil.ListToArray(AlbumDetailFragmentBase.this.getBurstCheckedItemIds()));
                    return true;
                case R.id.delete /* 2131362250 */:
                    AlbumDetailFragmentBase.this.doDelete(actionMode);
                    return true;
                case R.id.remove_secret /* 2131363196 */:
                    MediaAndAlbumOperations.removeFromSecretAlbum(AlbumDetailFragmentBase.this.mActivity, new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.AlbumDetailMultiChoiceModeListener.2
                        {
                            AlbumDetailMultiChoiceModeListener.this = this;
                        }

                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public void onComplete(long[] jArr) {
                            actionMode.finish();
                            SamplingStatHelper.recordCountEvent("album_detail", "remove_from_secret");
                        }
                    }, MiscUtil.ListToArray(AlbumDetailFragmentBase.this.getBurstCheckedItemIds()));
                    return true;
                default:
                    return false;
            }
        }

        public /* synthetic */ void lambda$onActionItemClicked$0(long[] jArr, boolean z) {
            this.mMode.finish();
            SamplingStatHelper.recordCountEvent("album_detail", "add_to_album");
        }

        public /* synthetic */ void lambda$onActionItemClicked$1() {
            AlbumDetailFragmentBase albumDetailFragmentBase = AlbumDetailFragmentBase.this;
            if (PicToPdfHelper.prepareGotoPicToPdfPreviewPage(albumDetailFragmentBase.mActivity, albumDetailFragmentBase.mEditableWrapper.getCheckedItems())) {
                TrackController.trackClick("403.26.0.1.11243", "403.26.0.1.11240", AlbumDetailFragmentBase.this.mEditableWrapper.getCheckedItems().size());
                AlbumDetailFragmentBase.this.mEditableWrapper.stopActionMode();
            }
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            AlbumDetailFragmentBase.this.onExitActionMode();
        }

        public final void updateMenuState() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.getCheckedItemCount() < 1) {
                if (isMenuItemVisible(this.mDelete)) {
                    this.mDelete.setEnabled(false);
                }
                if (isMenuItemVisible(this.mAddToAlbum)) {
                    this.mAddToAlbum.setEnabled(false);
                }
                if (isMenuItemVisible(this.mCreativity)) {
                    this.mCreativity.setEnabled(false);
                }
                if (isMenuItemVisible(this.mRemoveSecret)) {
                    this.mRemoveSecret.setEnabled(false);
                }
                if (!isMenuItemVisible(this.mSend)) {
                    return;
                }
                this.mSend.setEnabled(false);
                return;
            }
            if (isMenuItemVisible(this.mDelete)) {
                this.mDelete.setEnabled(true);
            }
            if (isMenuItemVisible(this.mAddToAlbum)) {
                this.mAddToAlbum.setEnabled(true);
            }
            if (isMenuItemVisible(this.mCreativity)) {
                this.mCreativity.setEnabled(true);
            }
            if (isMenuItemVisible(this.mRemoveSecret)) {
                this.mRemoveSecret.setEnabled(true);
            }
            if (!isMenuItemVisible(this.mSend)) {
                return;
            }
            this.mSend.setEnabled(true);
        }

        public final boolean isMenuItemVisible(MenuItem menuItem) {
            return menuItem != null && menuItem.isVisible();
        }

        public ActionMode getActionMode() {
            return this.mMode;
        }
    }

    public List<Long> getBurstCheckedItemIds() {
        List<Integer> checkedPositions = this.mEditableWrapper.getCheckedPositions();
        LinkedList linkedList = new LinkedList();
        for (Integer num : checkedPositions) {
            linkedList.addAll(((IAlbumAdapter) mo1564getAdapter()).getBurstItemKeys(num.intValue()));
        }
        return linkedList;
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x00ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void doDelete(final android.view.ActionMode r13) {
        /*
            r12 = this;
            boolean r0 = r12.isOthersShareAlbum()
            r1 = 0
            if (r0 == 0) goto L8e
            com.miui.gallery.widget.editwrapper.EditableListViewWrapper r0 = r12.mEditableWrapper
            java.util.List r0 = r0.getCheckedPositions()
            int r2 = r0.size()
            r3 = 0
            if (r2 <= 0) goto L52
            r2 = r1
            r4 = r3
        L16:
            int r5 = r0.size()
            if (r2 >= r5) goto L53
            java.lang.Object r5 = r0.get(r2)
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
            if (r4 != 0) goto L2d
            java.util.HashMap r4 = new java.util.HashMap
            r4.<init>()
        L2d:
            com.miui.gallery.adapter.IMediaAdapter r6 = r12.mo1564getAdapter()
            com.miui.gallery.adapter.IAlbumAdapter r6 = (com.miui.gallery.adapter.IAlbumAdapter) r6
            java.util.ArrayList r6 = r6.getBurstItemKeys(r5)
            java.util.Iterator r6 = r6.iterator()
        L3b:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto L4f
            java.lang.Object r7 = r6.next()
            java.lang.Long r7 = (java.lang.Long) r7
            java.lang.String r8 = r12.getCreatorIdByPosition(r5)
            r4.put(r7, r8)
            goto L3b
        L4f:
            int r2 = r2 + 1
            goto L16
        L52:
            r4 = r3
        L53:
            if (r4 == 0) goto L7d
            java.util.ArrayList r2 = com.miui.gallery.provider.cache.ShareMediaManager.getOwnerSharedImageIds(r4)
            if (r2 == 0) goto L7d
            int r4 = r2.size()
            if (r4 <= 0) goto L7d
            int r3 = r2.size()
            long[] r3 = new long[r3]
            r4 = r1
        L68:
            int r5 = r2.size()
            if (r4 >= r5) goto L7d
            java.lang.Object r5 = r2.get(r4)
            java.lang.Long r5 = (java.lang.Long) r5
            long r5 = r5.longValue()
            r3[r4] = r5
            int r4 = r4 + 1
            goto L68
        L7d:
            int r2 = r0.size()
            if (r2 <= 0) goto L96
            if (r3 == 0) goto L8c
            int r2 = r3.length
            int r0 = r0.size()
            if (r2 >= r0) goto L96
        L8c:
            r0 = 1
            goto L97
        L8e:
            java.util.List r0 = r12.getBurstCheckedItemIds()
            long[] r3 = com.miui.gallery.util.MiscUtil.ListToArray(r0)
        L96:
            r0 = r1
        L97:
            r11 = r3
            com.miui.gallery.widget.editwrapper.EditableListViewWrapper r2 = r12.mEditableWrapper
            int[] r2 = r2.getAfterDeletedFirstVisiblePosAndOffset()
            r12.mAfterDeletedFirstVisiblePosAndOffset = r2
            if (r11 == 0) goto Lc8
            int r2 = r11.length
            if (r2 <= 0) goto Lc8
            miuix.appcompat.app.AppCompatActivity r2 = r12.mActivity
            com.miui.gallery.ui.AlbumDetailFragmentBase$12 r4 = new com.miui.gallery.ui.AlbumDetailFragmentBase$12
            r4.<init>()
            r5 = 0
            long r6 = r12.mAlbumId
            java.lang.String r8 = r12.mAlbumName
            r9 = 23
            boolean r13 = r12.isAllPhotosAlbum()
            if (r13 == 0) goto Lbc
            r1 = 2
        Lba:
            r10 = r1
            goto Lc2
        Lbc:
            boolean r13 = r12.mIsBabyAlbum
            if (r13 == 0) goto Lba
            r1 = 3
            goto Lba
        Lc2:
            java.lang.String r3 = "AlbumDetailFragmentBaseDeleteMediaDialogFragment"
            com.miui.gallery.util.MediaAndAlbumOperations.delete(r2, r3, r4, r5, r6, r8, r9, r10, r11)
            goto Ldb
        Lc8:
            if (r0 == 0) goto Ld8
            androidx.fragment.app.FragmentActivity r0 = r12.getActivity()
            r1 = 2131887181(0x7f12044d, float:1.9408962E38)
            java.lang.String r1 = r12.getString(r1)
            com.miui.gallery.util.ToastUtils.makeText(r0, r1)
        Ld8:
            r13.finish()
        Ldb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.AlbumDetailFragmentBase.doDelete(android.view.ActionMode):void");
    }

    /* loaded from: classes2.dex */
    public static class AlbumScanTask implements ThreadPool.Job<Void> {
        public final long mAlbumId;
        public final String mAlbumLocalPath;

        public AlbumScanTask(String str, long j) {
            this.mAlbumLocalPath = str;
            this.mAlbumId = j;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            String[] absolutePath = StorageUtils.getAbsolutePath(GalleryApp.sGetAndroidContext(), StorageUtils.ensureCommonRelativePath(this.mAlbumLocalPath));
            if (absolutePath == null || absolutePath.length <= 0) {
                return null;
            }
            ScannerEngine.getInstance().scanPathsAsync(Lists.newArrayList(absolutePath), 9);
            return null;
        }
    }

    public void onSend(String str, String str2) {
        String str3;
        List<Integer> checkedPositions = this.mEditableWrapper.getCheckedPositions();
        ArrayList arrayList = new ArrayList(checkedPositions.size());
        int i = 0;
        int i2 = Integer.MAX_VALUE;
        for (int i3 = 0; i3 < checkedPositions.size(); i3++) {
            int intValue = checkedPositions.get(i3).intValue();
            arrayList.add(Long.valueOf(((IAlbumAdapter) mo1564getAdapter()).getItemKey(intValue)));
            if (intValue < i2) {
                i2 = intValue;
            }
        }
        if (checkedPositions.size() == 0) {
            return;
        }
        int[] iArr = new int[checkedPositions.size()];
        long[] jArr = new long[arrayList.size()];
        for (int i4 = 0; i4 < checkedPositions.size(); i4++) {
            iArr[i4] = checkedPositions.get(i4).intValue();
            jArr[i4] = ((Long) arrayList.get(i4)).longValue();
        }
        if (i2 != Integer.MAX_VALUE) {
            i = i2;
        }
        IntentUtil.gotoPreviewSelectPage(this, getUri(), i, ((IAlbumAdapter) mo1564getAdapter()).getItemCount(), getSelection(), getSelectionArgs(), getCurrentSortOrder(), new ImageLoadParams.Builder().setKey(((IAlbumAdapter) mo1564getAdapter()).getItemKey(i)).setFilePath(((IAlbumAdapter) mo1564getAdapter()).getBindImagePath(i)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setRegionRect(((IAlbumAdapter) mo1564getAdapter()).getItemDecodeRectF(i)).setInitPosition(i).setMimeType(((IAlbumAdapter) mo1564getAdapter()).getMimeType(i)).setSecretKey(((IAlbumAdapter) mo1564getAdapter()).getItemSecretKey(i)).setFileLength(((IAlbumAdapter) mo1564getAdapter()).getFileLength(i)).setCreateTime(((IAlbumAdapter) mo1564getAdapter()).getCreateTime(i)).setLocation(((IAlbumAdapter) mo1564getAdapter()).getLocation(i)).build(), jArr, iArr, str, str2);
        this.mEditableWrapper.stopActionMode();
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Param.COUNT, String.valueOf(checkedPositions.size()));
        SamplingStatHelper.recordCountEvent("album_detail", "send", hashMap);
        if (isAllPhotosAlbum()) {
            str3 = "403.44.3.1.11224";
        } else {
            str3 = this.mIsBabyAlbum ? "403.42.3.1.11302" : "403.15.3.1.11195";
        }
        TrackController.trackClick(str3, getEnterActionModeTip(), checkedPositions.size());
    }

    @Override // com.miui.gallery.widget.ViewDragListener.DropReceiver
    public String receivePath() {
        return (needSaveToMiShareAfterDrop() || TextUtils.isEmpty(StorageUtils.ensureCommonRelativePath(this.mAlbumLocalPath))) ? "MiShare" : this.mAlbumLocalPath;
    }

    @Override // com.miui.gallery.widget.ViewDragListener.DropReceiver
    public boolean doAfterReceived(ArrayList<String> arrayList) {
        if (needSaveToMiShareAfterDrop() && !isFavoritesAlbum()) {
            Uri.Builder buildUpon = GalleryContract.Common.URI_ALBUM_PAGE.buildUpon();
            buildUpon.appendQueryParameter("local_path", receivePath());
            JumpDialogFragment.showAlbumPage(getActivity(), buildUpon.build());
        }
        if (isFavoritesAlbum()) {
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                MediaAndAlbumOperations.addToFavoritesByPath(getActivity(), null, it.next());
            }
            return true;
        }
        return false;
    }

    public final boolean needSaveToMiShareAfterDrop() {
        return isVirtualAlbum() || this.mIsOtherShareAlbum || ((IAlbumAdapter) mo1564getAdapter()).isBabyAlbum();
    }

    public boolean needShowTimeLine() {
        return this.mViewModel.isTimeGroup() && (this.mViewModel.getSortBy() == SortBy.UPDATE_DATE || this.mViewModel.getSortBy() == SortBy.CREATE_DATE) && !isDailyAlbum();
    }

    public String getEnterActionModeTip() {
        return isAllPhotosAlbum() ? "403.44.3.1.11229" : this.mIsBabyAlbum ? "403.42.3.1.11305" : "403.15.3.1.11200";
    }

    public static void doDeleteEmptyAlbum(FragmentActivity fragmentActivity, long j, final String str, boolean z) {
        if (GalleryPreferences.LocalMode.isOnlyShowLocalPhoto() || !z) {
            return;
        }
        DeletionTask.Param param = new DeletionTask.Param(new long[]{j}, 0, 50);
        DeletionTask deletionTask = new DeletionTask();
        final WeakReference weakReference = new WeakReference(fragmentActivity);
        deletionTask.setFragmentActivityForStoragePermissionMiss(fragmentActivity);
        deletionTask.setOnDeletionCompleteListener(new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.AlbumDetailFragmentBase.13
            @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
            public void onDeleted(int i, long[] jArr) {
                if (weakReference.get() == null) {
                    return;
                }
                if (i < 0) {
                    ToastUtils.makeText((Context) weakReference.get(), ((FragmentActivity) weakReference.get()).getString(R.string.delete_album_failed));
                }
                HashMap hashMap = new HashMap(1, 1.0f);
                hashMap.put(Action.FILE_ATTRIBUTE, str);
                SamplingStatHelper.recordCountEvent("album", "delete_album", hashMap);
            }
        });
        deletionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallBack);
    }

    /* loaded from: classes2.dex */
    public class AlbumDetailKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public AlbumDetailKeyboardShortcutCallback() {
            AlbumDetailFragmentBase.this = r1;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onCopyPressed() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode()) {
                KeyboardShortcutsCopyHelper.getInstance().setCopyList(AlbumDetailFragmentBase.this.getBurstCheckedItemIds(), false);
                ActionMode actionMode = AlbumDetailFragmentBase.this.mChoiceModeListener.getActionMode();
                if (actionMode == null) {
                    return true;
                }
                actionMode.finish();
                return true;
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onCutPressed() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode() && !AlbumDetailFragmentBase.this.isOthersShareAlbum()) {
                KeyboardShortcutsCopyHelper.getInstance().setCopyList(AlbumDetailFragmentBase.this.getBurstCheckedItemIds(), true);
                ActionMode actionMode = AlbumDetailFragmentBase.this.mChoiceModeListener.getActionMode();
                if (actionMode != null) {
                    actionMode.finish();
                }
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode()) {
                AlbumDetailFragmentBase.this.mEditableWrapper.setAllItemsCheckState(true);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode()) {
                AlbumDetailFragmentBase albumDetailFragmentBase = AlbumDetailFragmentBase.this;
                albumDetailFragmentBase.doDelete(albumDetailFragmentBase.mChoiceModeListener.getActionMode());
                return true;
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onPastePressed() {
            if (!AlbumDetailFragmentBase.this.isPasteSupported()) {
                return false;
            }
            ArrayList<Long> copyList = KeyboardShortcutsCopyHelper.getInstance().getCopyList();
            boolean deleteOrigin = KeyboardShortcutsCopyHelper.getInstance().deleteOrigin();
            if (copyList != null && !copyList.isEmpty()) {
                long[] jArr = new long[copyList.size()];
                for (int i = 0; i < copyList.size(); i++) {
                    jArr[i] = copyList.get(i).longValue();
                }
                AlbumDetailFragmentBase albumDetailFragmentBase = AlbumDetailFragmentBase.this;
                CopyMoveDialogFragment.show(albumDetailFragmentBase.mActivity, albumDetailFragmentBase.mAlbumId, jArr, deleteOrigin, (MediaAndAlbumOperations.OnAddAlbumListener) null);
                AlbumDetailFragmentBase.this.recordCopyMoveAction(false);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onYearModePressed() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode()) {
                return false;
            }
            AlbumDetailFragmentBase.this.setPictureViewMode(PictureViewMode.TINY_THUMB);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onMonthCompactModePressed() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode()) {
                return false;
            }
            AlbumDetailFragmentBase.this.setPictureViewMode(PictureViewMode.MINI_THUMB);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onMonthLooseModePressed() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode()) {
                return false;
            }
            AlbumDetailFragmentBase.this.setPictureViewMode(PictureViewMode.MICRO_THUMB);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDayModePressed() {
            if (AlbumDetailFragmentBase.this.mEditableWrapper.isInActionMode()) {
                return false;
            }
            AlbumDetailFragmentBase.this.setPictureViewMode(PictureViewMode.LARGE_THUMB);
            return true;
        }
    }
}
