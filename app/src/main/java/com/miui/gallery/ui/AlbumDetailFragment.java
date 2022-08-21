package com.miui.gallery.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.FileSize;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.adapter.IAlbumAdapter;
import com.miui.gallery.adapter.PreloadItem;
import com.miui.gallery.adapter.ProportionStringTagAdapter;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.glide.GlideApp;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.GlideRequests;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlbumDetailSortImmersionMenuHelper;
import com.miui.gallery.ui.BaseAlbumOperationDialogFragment;
import com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.viewmodel.AlbumDetailViewModel;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.ViewDragListener;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsule;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerTimeCapsuleView;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider;
import com.miui.gallery.widget.recyclerview.ProportionTagBaseAdapter;
import com.miui.gallery.widget.recyclerview.ProportionTagModel;
import com.miui.privacy.LockSettingsHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.c.b;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import miui.gallery.support.actionbar.ActionBarCompat;
import miuix.appcompat.app.AppCompatActivity;
import miuix.popupwidget.widget.GuidePopupWindow;

/* loaded from: classes2.dex */
public abstract class AlbumDetailFragment<ADAPTER extends IAlbumAdapter> extends AlbumDetailFragmentBase<ADAPTER> implements ListPreloader.PreloadModelProvider<PreloadItem> {
    public LockSettingsHelper mChooseLockSettingsHelper;
    public EmptyPage mEmptyPage;
    public MenuStatusManager mMenuStatusManager;
    public boolean mNeedConfirmPassWord;
    public GuidePopupWindow mPopupWindow;
    public ReplaceAlbumCoverUtils.CallBack mReplaceAlbumCoverCallBack;
    public Album mReplaceOperationAlbum;
    public GlideRequests mRequestManager;
    public AlbumDetailSortImmersionMenuHelper mSortImmersionMenuHelper;
    public ProportionTagBaseAdapter<Integer> mTimeTagAdapter;
    public boolean mTagProportionChanged = true;
    public boolean mHasCheckedLogin = false;
    public BaseAlbumOperationDialogFragment.OnAlbumOperationListener mOnAlbumRenamedListener = new BaseAlbumOperationDialogFragment.OnAlbumOperationListener() { // from class: com.miui.gallery.ui.AlbumDetailFragment.4
        {
            AlbumDetailFragment.this = this;
        }

        @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment.OnAlbumOperationListener
        public void onOperationDone(long j, String str, Bundle bundle) {
            if (j <= 0 || !AlbumDetailFragment.this.isAdded()) {
                return;
            }
            AlbumDetailFragment albumDetailFragment = AlbumDetailFragment.this;
            albumDetailFragment.mAlbumName = str;
            albumDetailFragment.updateActionBarTitle();
        }
    };

    /* renamed from: $r8$lambda$6Ez-3IrD9HVVy-fqgcr8uvNPWyQ */
    public static /* synthetic */ void m1423$r8$lambda$6Ez3IrD9HVVyfqgcr8uvNPWyQ(AlbumDetailFragment albumDetailFragment, Boolean bool) {
        albumDetailFragment.lambda$registerLiveDataObserver$0(bool);
    }

    public static /* synthetic */ void $r8$lambda$Hamvr8ui0ml7G41OyoYcPcaxOTg(AlbumDetailFragment albumDetailFragment, Integer num) {
        albumDetailFragment.lambda$registerLiveDataObserver$1(num);
    }

    public static /* synthetic */ void $r8$lambda$jzDANxydLI5uEfO79L22eLnwCA4(AlbumDetailFragment albumDetailFragment) {
        albumDetailFragment.lambda$configShareGuide$2();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public int getLayoutSource() {
        return R.layout.album_detail;
    }

    public abstract void onSortByChanged();

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mRequestManager = GlideApp.with(this);
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        if (this.mChooseLockSettingsHelper == null) {
            this.mChooseLockSettingsHelper = new LockSettingsHelper(getActivity());
        }
        Intent intent = this.mActivity.getIntent();
        if (intent.getBooleanExtra("other_enter_secret_album", false)) {
            this.mNeedConfirmPassWord = true;
        }
        this.mMenuStatusManager = new MenuStatusManager(this.mActivity, intent.getStringExtra("album_server_id"), intent.getLongExtra("attributes", 0L), intent.getBooleanExtra("album_unwriteable", false), isOthersShareAlbum(), intent.getStringExtra("album_local_path"), intent.getIntExtra("extra_from_type", -1), isShareAlbum(), isAllPhotosAlbum(), isCustomQueryAlbum());
        if (!TextUtils.isEmpty(this.mAlbumName)) {
            updateActionBarTitle();
        }
        if (isPreviewMode()) {
            this.mEditableWrapper.enableChoiceMode(false);
            this.mEditableWrapper.enterChoiceModeWithLongClick(false);
        }
        this.mViewModel.setIsTimeGroup(GalleryPreferences.Album.getAlbumDetailTimeGroup(this.mAlbumId, true));
        ActionBarCompat.setCustomEndViewOnly(this.mActivity, R.layout.album_detail_action_bar_more);
        AlbumDetailSortImmersionMenuHelper albumDetailSortImmersionMenuHelper = new AlbumDetailSortImmersionMenuHelper(this.mActivity, this.mViewModel.getSortBy(), this.mViewModel.getSortOrder().equals(" ASC "));
        this.mSortImmersionMenuHelper = albumDetailSortImmersionMenuHelper;
        albumDetailSortImmersionMenuHelper.setOnClickItemListener(new AlbumDetailSortImmersionMenuHelper.OnItemClickListener() { // from class: com.miui.gallery.ui.AlbumDetailFragment.1
            {
                AlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.ui.AlbumDetailSortImmersionMenuHelper.OnItemClickListener
            public void onItemClick(SortBy sortBy) {
                AlbumDetailFragment.this.stopAndHideScroller();
                AlbumDetailFragment.this.sortOnChange(sortBy);
            }
        });
        this.mRecyclerView.setFastScrollerCapsuleViewProvider(new FastScrollerCapsuleViewProvider() { // from class: com.miui.gallery.ui.AlbumDetailFragment.2
            {
                AlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
            public boolean isShowCapsule() {
                return !AlbumDetailFragment.this.isDailyAlbum();
            }

            @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
            public FastScrollerCapsule createFastScrollerCapsule() {
                FastScrollerTimeCapsuleView fastScrollerTimeCapsuleView = new FastScrollerTimeCapsuleView(AlbumDetailFragment.this.getContext());
                fastScrollerTimeCapsuleView.setStyle(R.style.FastScrollTimeCapsule);
                return fastScrollerTimeCapsuleView;
            }
        });
        this.mRecyclerView.setProportionTagAdapterProvider(new ProportionTagAdapterProvider<Integer>() { // from class: com.miui.gallery.ui.AlbumDetailFragment.3
            {
                AlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public boolean isShowProportionTag() {
                return (AlbumDetailFragment.this.getResources().getConfiguration().orientation == 2 || AlbumDetailFragment.this.mViewModel.getSortBy() == SortBy.NAME || AlbumDetailFragment.this.mViewModel.getSortBy() == SortBy.SIZE) ? false : true;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public ProportionTagBaseAdapter<Integer> createTagAdapter() {
                if (AlbumDetailFragment.this.mTimeTagAdapter == null) {
                    AlbumDetailFragment albumDetailFragment = AlbumDetailFragment.this;
                    albumDetailFragment.mTimeTagAdapter = new ProportionStringTagAdapter(albumDetailFragment.getContext());
                }
                return AlbumDetailFragment.this.mTimeTagAdapter;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public boolean isProportionTagChanged() {
                return AlbumDetailFragment.this.mTagProportionChanged;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public List<ProportionTagModel<Integer>> getProportionTagModel() {
                AlbumDetailFragment albumDetailFragment = AlbumDetailFragment.this;
                albumDetailFragment.mTagProportionChanged = false;
                return ((IAlbumAdapter) albumDetailFragment.mo1564getAdapter()).calculateTagProportionList();
            }
        });
        registerLiveDataObserver();
        return onInflateView;
    }

    public final void registerLiveDataObserver() {
        this.mViewModel.isNeedReorder().observe(this, new Observer() { // from class: com.miui.gallery.ui.AlbumDetailFragment$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                AlbumDetailFragment.m1423$r8$lambda$6Ez3IrD9HVVyfqgcr8uvNPWyQ(AlbumDetailFragment.this, (Boolean) obj);
            }
        });
        this.mViewModel.getSwitchOperationId().observe(this, new Observer() { // from class: com.miui.gallery.ui.AlbumDetailFragment$$ExternalSyntheticLambda1
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                AlbumDetailFragment.$r8$lambda$Hamvr8ui0ml7G41OyoYcPcaxOTg(AlbumDetailFragment.this, (Integer) obj);
            }
        });
    }

    public /* synthetic */ void lambda$registerLiveDataObserver$0(Boolean bool) {
        this.mSortImmersionMenuHelper.setSortOrder(this.mViewModel.getSortBy(), this.mViewModel.getSortOrder().equals(" ASC "));
        this.mSortImmersionMenuHelper.updateMenuItem();
        onSortByChanged();
    }

    public /* synthetic */ void lambda$registerLiveDataObserver$1(Integer num) {
        switch (num.intValue()) {
            case R.id.menu_disable_auto_upload /* 2131362890 */:
                this.mMenuStatusManager.onAutoUploadStatusChanged(false);
                return;
            case R.id.menu_enable_auto_upload /* 2131362892 */:
                this.mMenuStatusManager.onAutoUploadStatusChanged(true);
                return;
            case R.id.menu_move_to_other_albums /* 2131362901 */:
                this.mMenuStatusManager.onShowInOtherAlbumsStatusChanged(true);
                return;
            case R.id.menu_move_to_rubbish_albums /* 2131362902 */:
                this.mMenuStatusManager.onShowInRubbishAlbumPageStatusChanged(true);
                return;
            case R.id.menu_not_show_in_photos_tab /* 2131362903 */:
                this.mMenuStatusManager.onShowInPhotosTabStatusChanged(false);
                return;
            case R.id.menu_remove_from_other_albums /* 2131362909 */:
                this.mMenuStatusManager.onShowInOtherAlbumsStatusChanged(false);
                return;
            case R.id.menu_remove_from_rubbish_albums /* 2131362910 */:
                this.mMenuStatusManager.onShowInRubbishAlbumPageStatusChanged(false);
                return;
            case R.id.menu_show_in_photos_tab /* 2131362920 */:
                this.mMenuStatusManager.onShowInPhotosTabStatusChanged(true);
                return;
            default:
                return;
        }
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public void setPictureViewMode(PictureViewMode pictureViewMode) {
        super.setPictureViewMode(pictureViewMode);
        this.mTagProportionChanged = true;
    }

    public void hideScrollerBar() {
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        if (galleryRecyclerView != null) {
            galleryRecyclerView.hideScrollerBar();
        }
    }

    public final void sortOnChange(SortBy sortBy) {
        if (sortBy == SortBy.UPDATE_DATE) {
            SamplingStatHelper.recordCountEvent("album_detail", "sort_by_update_date");
            TrackController.trackClick(isAllPhotosAlbum() ? "403.44.2.1.11217" : "403.15.2.1.11183", AutoTracking.getRef(), this.mViewModel.getSortOrder());
        }
        if (sortBy == SortBy.CREATE_DATE) {
            SamplingStatHelper.recordCountEvent("album_detail", "sort_by_create_date");
            TrackController.trackClick(isAllPhotosAlbum() ? "403.44.2.1.11218" : "403.15.2.1.11184", AutoTracking.getRef(), this.mViewModel.getSortOrder());
        }
        if (sortBy == SortBy.NAME) {
            SamplingStatHelper.recordCountEvent("album_detail", "sort_by_name");
            TrackController.trackClick(isAllPhotosAlbum() ? "403.44.2.1.11219" : "403.15.2.1.11185", AutoTracking.getRef(), this.mViewModel.getSortOrder());
        }
        if (sortBy == SortBy.SIZE) {
            SamplingStatHelper.recordCountEvent("album_detail", "sort_by_size");
            TrackController.trackClick(isAllPhotosAlbum() ? "403.44.2.1.11220" : "403.15.2.1.11186", AutoTracking.getRef(), this.mViewModel.getSortOrder());
        }
        AlbumDetailViewModel albumDetailViewModel = this.mViewModel;
        String str = " DESC ";
        if (albumDetailViewModel.getSortBy() == sortBy && this.mViewModel.getSortOrder().equals(str)) {
            str = " ASC ";
        }
        albumDetailViewModel.setSortOrder(str);
        this.mViewModel.setSortBy(sortBy);
        this.mViewModel.notifyReorder();
    }

    public final void configShareGuide() {
        MenuStatusManager menuStatusManager;
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isDestroyed() || isPreviewMode() || (menuStatusManager = this.mMenuStatusManager) == null || !menuStatusManager.checkOperationSupport(512L) || this.mIsOtherShareAlbum || !GalleryPreferences.Album.isFirstVisitOwnerAlbumDetail()) {
            return;
        }
        activity.getWindow().findViewById(16908290).post(new Runnable() { // from class: com.miui.gallery.ui.AlbumDetailFragment$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AlbumDetailFragment.$r8$lambda$jzDANxydLI5uEfO79L22eLnwCA4(AlbumDetailFragment.this);
            }
        });
        GalleryPreferences.Album.setFirstVisitOwnerAlbumDetail(false);
    }

    public /* synthetic */ void lambda$configShareGuide$2() {
        View findViewById = getActionEndView().findViewById(R.id.more);
        GuidePopupWindow guidePopupWindow = new GuidePopupWindow(getContext());
        this.mPopupWindow = guidePopupWindow;
        guidePopupWindow.setArrowMode(10);
        this.mPopupWindow.setGuideText(R.string.can_authorize_owner_albums_share_to_tv);
        this.mPopupWindow.show(findViewById, false);
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public void onDataFirstBound(int i) {
        super.onDataFirstBound(i);
        configShareGuide();
    }

    public final void updateActionBarTitle() {
        this.mActivity.getAppCompatActionBar().setTitle(this.mAlbumName);
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mActivity.setImmersionMenuEnabled(showOptionsMenu());
        if (isSecretAlbum()) {
            this.mActivity.getWindow().addFlags(8192);
        }
        if (bundle != null) {
            this.mViewModel.setSortBy((SortBy) bundle.getSerializable("album_detail_sort_by"));
            this.mViewModel.setSortOrder(bundle.getString("album_detail_sort_order"));
            this.mNeedConfirmPassWord = bundle.getBoolean("need_password");
        }
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 27) {
            if (i2 != -1) {
                finish();
            } else {
                this.mNeedConfirmPassWord = false;
            }
        } else if (ReplaceAlbumCoverUtils.handleActivityResultByReplaceAlbumCover(this, i, i2, intent, Collections.singletonList(this.mReplaceOperationAlbum), this.mReplaceAlbumCoverCallBack)) {
        } else {
            if (i == 28) {
                if (i2 == -1) {
                    return;
                }
                finish();
            } else if (i != 29) {
                super.onActivityResult(i, i2, intent);
            } else if (i2 != -1) {
                finish();
            } else if (!isNeedConfirmPassWord()) {
            } else {
                if (this.mChooseLockSettingsHelper.isPrivacyPasswordEnabled()) {
                    this.mNeedConfirmPassWord = false;
                    LockSettingsHelper.startAuthenticatePasswordActivity(this, 27);
                    return;
                }
                LockSettingsHelper.startSetPrivacyPasswordActivity(this, 28);
            }
        }
    }

    public final boolean isNeedConfirmPassWord() {
        return isSecretAlbum() && this.mNeedConfirmPassWord && !isInPhotoPage();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public View getEmptyView() {
        this.mEmptyPage = (EmptyPage) super.getEmptyView();
        this.mEmptyPage.setOnDragListener(new ViewDragListener(this));
        return this.mEmptyPage;
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (isNeedConfirmPassWord() && !this.mHasCheckedLogin) {
            this.mHasCheckedLogin = true;
            IntentUtil.checkLoginAndSyncState(this);
        }
        EmptyPage emptyPage = this.mEmptyPage;
        if (emptyPage != null) {
            emptyPage.resumeMaml();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("tip", getPageTip());
        hashMap.put("ref_tip", AutoTracking.getRef());
        if (isSecretAlbum()) {
            hashMap.put(MiStat.Param.COUNT, String.valueOf(this.mEditableWrapper.getCount()));
        } else if (isAllPhotosAlbum()) {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "all");
            AutoTracking.trackView("403.44.1.1.11212", getSortByString(this.mViewModel.getSortBy()));
            AutoTracking.trackView("403.44.1.1.11214", String.valueOf(this.mViewModel.isTimeGroup()));
        } else {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, getSortByString(this.mViewModel.getSortBy()));
            hashMap.put("status", String.valueOf(this.mViewModel.isTimeGroup()));
        }
        AutoTracking.trackView(hashMap);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public void onPhotoPageDestroy(Intent intent) {
        super.onPhotoPageDestroy(intent);
        this.mNeedConfirmPassWord = false;
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        EmptyPage emptyPage = this.mEmptyPage;
        if (emptyPage != null) {
            emptyPage.pauseMaml();
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mNeedConfirmPassWord = true;
        if (!this.mViewModel.isAlbumGroup()) {
            saveAlbumSortToPreference();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        GuidePopupWindow guidePopupWindow = this.mPopupWindow;
        if (guidePopupWindow != null) {
            guidePopupWindow.dismiss(false);
        }
        EmptyPage emptyPage = this.mEmptyPage;
        if (emptyPage != null) {
            emptyPage.destroyMaml();
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public void finish() {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null || appCompatActivity.isFinishing()) {
            return;
        }
        this.mActivity.finish();
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public void onEnterActionMode() {
        if (this.mMenuStatusManager.canShowInPhotosTab()) {
            ImageSelectionTipFragment.showImageSelectionTipDialogIfNecessary(getActivity());
        }
        SamplingStatHelper.recordStringPropertyEvent("best_image_count", String.valueOf(MediaFeatureCacheManager.getInstance().getBestImageCount(false)));
        if (!TextUtils.isEmpty(getEnterActionModeTip())) {
            TrackController.trackExpose(getEnterActionModeTip(), getPageTip());
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("album_detail_sort_by", this.mViewModel.getSortBy());
        bundle.putString("album_detail_sort_order", this.mViewModel.getSortOrder());
        bundle.putBoolean("need_password", this.mNeedConfirmPassWord);
        bundle.putString("album_name", this.mAlbumName);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return isSecretAlbum() ? "album_secret" : isFavoritesAlbum() ? "album_favorites" : isCameraAlbum() ? "album_camera" : isVideoAlbum() ? "album_video" : this.mIsScreenshotAlbum ? "album_screenshot" : isAllPhotosAlbum() ? "all_photos" : isCustomQueryAlbum() ? "custom_query" : "album_detail";
    }

    @Override // com.bumptech.glide.ListPreloader.PreloadModelProvider
    public RequestBuilder<?> getPreloadRequestBuilder(PreloadItem preloadItem) {
        return this.mRequestManager.mo985asBitmap().mo962load((Object) GalleryModel.of(preloadItem.path)).mo946apply((BaseRequestOptions<?>) GlideOptions.microThumbOf()).mo974priority(Priority.LOW).fileLength(preloadItem.fileLength).decodeRegion(RegionConfig.of(preloadItem.region)).secretKey(preloadItem.secretKey);
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public void onEmptyViewVisibilityChanged(int i) {
        super.onEmptyViewVisibilityChanged(i);
        if (i == 0) {
            this.mRecyclerView.setVisibility(8);
            return;
        }
        this.mEmptyPage.setVisibility(8);
        this.mRecyclerView.setVisibility(0);
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public boolean needEnableAutoUpload() {
        return this.mMenuStatusManager.checkOperationSupport(128L);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.album_detail_page_options, menu);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu.size() <= 0) {
            return;
        }
        prepareMenuItem(menu, R.id.menu_add_photos, this.mMenuStatusManager.checkOperationSupport(1L));
        MenuItem prepareMenuItem = prepareMenuItem(menu, R.id.menu_time_group, this.mMenuStatusManager.checkOperationSupport(2048L));
        prepareMenuItem(menu, R.id.menu_sort, this.mMenuStatusManager.checkOperationSupport(4096L));
        prepareMenuItem(menu, R.id.menu_remove_from_other_albums, this.mMenuStatusManager.checkOperationSupport(2L));
        prepareMenuItem(menu, R.id.menu_move_to_other_albums, this.mMenuStatusManager.checkOperationSupport(8L));
        prepareMenuItem(menu, R.id.menu_enable_auto_upload, this.mMenuStatusManager.checkOperationSupport(128L));
        prepareMenuItem(menu, R.id.menu_disable_auto_upload, this.mMenuStatusManager.checkOperationSupport(256L));
        prepareMenuItem(menu, R.id.menu_show_in_photos_tab, this.mMenuStatusManager.checkOperationSupport(32L));
        prepareMenuItem(menu, R.id.menu_not_show_in_photos_tab, this.mMenuStatusManager.checkOperationSupport(64L));
        MenuItem prepareMenuItem2 = prepareMenuItem(menu, R.id.menu_share, this.mIsShareAlbum || this.mMenuStatusManager.checkOperationSupport(512L));
        prepareMenuItem(menu, R.id.menu_rename, this.mMenuStatusManager.checkOperationSupport(FileSize.KB_COEFFICIENT));
        prepareMenuItem(menu, R.id.menu_move_to_rubbish_albums, this.mMenuStatusManager.checkOperationSupport(4L));
        prepareMenuItem(menu, R.id.menu_remove_from_rubbish_albums, this.mMenuStatusManager.checkOperationSupport(16L));
        prepareMenuItem(menu, R.id.menu_replace_album_cover, this.mMenuStatusManager.checkOperationSupport(FileAppender.DEFAULT_BUFFER_SIZE));
        if (prepareMenuItem != null) {
            if (this.mViewModel.isTimeGroup()) {
                prepareMenuItem.setTitle(R.string.operation_hide_time);
                if (this.mViewModel.getSortBy() == SortBy.SIZE || this.mViewModel.getSortBy() == SortBy.NAME) {
                    prepareMenuItem.setTitle(R.string.operation_show_time);
                }
            } else {
                prepareMenuItem.setTitle(R.string.operation_show_time);
            }
        }
        if (prepareMenuItem2 == null) {
            return;
        }
        if (SyncUtil.isGalleryCloudSyncable(getActivity()) && (this.mIsShareAlbum || this.mMenuStatusManager.isShareToDevice())) {
            if (!this.mIsOtherShareAlbum) {
                prepareMenuItem2.setTitle(R.string.operation_share_manage);
                return;
            } else {
                prepareMenuItem2.setTitle(R.string.operation_share);
                return;
            }
        }
        prepareMenuItem2.setTitle(R.string.operation_share);
    }

    public final MenuItem prepareMenuItem(Menu menu, int i, boolean z) {
        MenuItem findItem = menu.findItem(i);
        if (findItem == null) {
            return null;
        }
        if (findItem.isVisible() != z) {
            findItem.setVisible(z);
        }
        return findItem;
    }

    public final boolean hasOptionMenuItemVisible() {
        return this.mMenuStatusManager.getSupportOperationFlag() != 0;
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                if (isAdded() && isResumed()) {
                    finish();
                }
                return true;
            case R.id.menu_disable_auto_upload /* 2131362890 */:
                disableAutoUpload();
                SamplingStatHelper.recordCountEvent("album_detail", "auto_upload_disable");
                TrackController.trackClick("403.15.2.1.11190", getPageTip());
                return true;
            case R.id.menu_enable_auto_upload /* 2131362892 */:
                enableAutoUpload();
                SamplingStatHelper.recordCountEvent("album_detail", "auto_upload_enable");
                TrackController.trackClick("403.15.2.1.11189", getPageTip());
                return true;
            case R.id.menu_move_to_other_albums /* 2131362901 */:
                moveToOtherAlbums();
                SamplingStatHelper.recordCountEvent("album_detail", "show_in_others_enable");
                TrackController.trackClick("403.15.2.1.11187", getPageTip());
                return true;
            case R.id.menu_move_to_rubbish_albums /* 2131362902 */:
                moveToRubbishAlbums();
                SamplingStatHelper.recordCountEvent("album_detail", "move_to_rubbish_albums");
                return true;
            case R.id.menu_not_show_in_photos_tab /* 2131362903 */:
                SamplingStatHelper.recordCountEvent("album_detail", "show_in_home_disable");
                TrackController.trackClick("403.15.2.1.11188", getPageTip());
                doChangeShowInPhotosTab(false);
                return true;
            case R.id.menu_remove_from_other_albums /* 2131362909 */:
                removeFromOtherAlbums();
                SamplingStatHelper.recordCountEvent("album_detail", "show_in_others_disable");
                TrackController.trackClick("403.15.2.1.11187", getPageTip());
                return true;
            case R.id.menu_remove_from_rubbish_albums /* 2131362910 */:
                removeFromRubbishAlbums();
                SamplingStatHelper.recordCountEvent("album_detail", "remove_from_rubbish_albums");
                return true;
            case R.id.menu_rename /* 2131362911 */:
                doRename();
                SamplingStatHelper.recordCountEvent("album_detail", "rename_album");
                return true;
            case R.id.menu_replace_album_cover /* 2131362913 */:
                showReplaceAlbumCoverImmersionMenuDialog();
                SamplingStatHelper.recordCountEvent("album_detail", "replace_album_cover");
                TrackController.trackClick(isAllPhotosAlbum() ? "403.44.2.1.11215" : "403.15.2.1.11180", getPageTip());
                return true;
            case R.id.menu_share /* 2131362919 */:
                if (this.mIsShareAlbum || this.mMenuStatusManager.isShareToDevice()) {
                    if (!this.mIsOtherShareAlbum) {
                        SamplingStatHelper.recordCountEvent("album", "share_album_manage");
                    } else {
                        SamplingStatHelper.recordCountEvent("album", "share_album");
                    }
                } else {
                    SamplingStatHelper.recordCountEvent("album", "share_album");
                }
                return super.onOptionsItemSelected(menuItem);
            case R.id.menu_show_in_photos_tab /* 2131362920 */:
                doChangeShowInPhotosTab(true);
                SamplingStatHelper.recordCountEvent("album_detail", "show_in_home_enable");
                TrackController.trackClick("403.15.2.1.11188", getPageTip());
                return true;
            case R.id.menu_sort /* 2131362922 */:
                this.mSortImmersionMenuHelper.showImmersionMenu(getActionEndView());
                return true;
            case R.id.menu_time_group /* 2131362923 */:
                stopAndHideScroller();
                if (!this.mViewModel.isTimeGroup() || (this.mViewModel.getSortBy() != SortBy.NAME && this.mViewModel.getSortBy() != SortBy.SIZE)) {
                    AlbumDetailViewModel albumDetailViewModel = this.mViewModel;
                    albumDetailViewModel.setIsTimeGroup(!albumDetailViewModel.isTimeGroup());
                    GalleryPreferences.Album.setAlbumDetailTimeGroup(this.mAlbumId, this.mViewModel.isTimeGroup());
                } else {
                    ToastUtils.makeText(getContext(), getResources().getString(R.string.album_detail_default_sort_tip));
                }
                if (this.mViewModel.getSortBy() == SortBy.NAME || this.mViewModel.getSortBy() == SortBy.SIZE) {
                    this.mViewModel.setSortBy(getAlbumDetailDefaultSort());
                    this.mViewModel.setSortOrder(" DESC ");
                    this.mSortImmersionMenuHelper.setRefreshSortToDefault();
                }
                this.mViewModel.notifyReorder();
                if (isSecretAlbum()) {
                    TrackController.trackClick("403.51.0.1.11420", getPageTip(), String.valueOf(this.mViewModel.isTimeGroup()));
                } else if (isAllPhotosAlbum()) {
                    TrackController.trackClick("403.44.2.1.11216", getPageTip(), String.valueOf(this.mViewModel.isTimeGroup()));
                } else {
                    TrackController.trackClick("403.15.2.1.11182", getPageTip(), String.valueOf(this.mViewModel.isTimeGroup()));
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public String configOrderBy(SortBy sortBy) {
        String sortByString = getSortByString(sortBy);
        return sortByString + this.mViewModel.getSortOrder();
    }

    /* renamed from: com.miui.gallery.ui.AlbumDetailFragment$6 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass6 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$adapter$SortBy;

        static {
            int[] iArr = new int[SortBy.values().length];
            $SwitchMap$com$miui$gallery$adapter$SortBy = iArr;
            try {
                iArr[SortBy.UPDATE_DATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$adapter$SortBy[SortBy.CREATE_DATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$adapter$SortBy[SortBy.NAME.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$adapter$SortBy[SortBy.SIZE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public final String getSortByString(SortBy sortBy) {
        int i = AnonymousClass6.$SwitchMap$com$miui$gallery$adapter$SortBy[sortBy.ordinal()];
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "alias_sort_time" : MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE : "title" : "alias_create_time" : "dateModified";
    }

    public final void doRename() {
        AlbumRenameDialogFragment.newInstance(this.mAlbumId, this.mAlbumName, "AlbumDetailFragment", this.mOnAlbumRenamedListener).showAllowingStateLoss(getFragmentManager(), "AlbumRenameDialogFragment");
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String getCurrentSortOrder() {
        return getSortByString(this.mViewModel.getSortBy()) + this.mViewModel.getSortOrder();
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public boolean doChangeAutoUpload(boolean z) {
        if (super.doChangeAutoUpload(z)) {
            this.mViewModel.doSwitchOperation(z ? R.id.menu_enable_auto_upload : R.id.menu_disable_auto_upload);
            return true;
        }
        return false;
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public void doChangeShowInOtherAlbums(boolean z) {
        super.doChangeShowInOtherAlbums(z);
        this.mViewModel.doSwitchOperation(z ? R.id.menu_move_to_other_albums : R.id.menu_remove_from_other_albums);
    }

    public final void doChangeShowInPhotosTab(boolean z) {
        MediaAndAlbumOperations.doChangeShowInPhotosTab(this.mActivity, this.mAlbumId, z);
        this.mViewModel.doSwitchOperation(z ? R.id.menu_show_in_photos_tab : R.id.menu_not_show_in_photos_tab);
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public void doChangeShowInRubbishAlbums(boolean z) {
        super.doChangeShowInRubbishAlbums(z);
        this.mViewModel.doSwitchOperation(z ? R.id.menu_move_to_rubbish_albums : R.id.menu_remove_from_rubbish_albums);
        finish();
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public String getCreatorIdByPosition(int i) {
        return ((IAlbumAdapter) mo1564getAdapter()).getCreatorId(i);
    }

    public final boolean showOptionsMenu() {
        return !isDailyAlbum() && !this.mMenuStatusManager.isUnWriteable() && hasOptionMenuItemVisible();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public boolean isPreviewMode() {
        if (getActivity() == null || getActivity().getIntent() == null) {
            return false;
        }
        return this.mMenuStatusManager.isHiddenOrRubbishAlbum();
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        AlbumDetailSortImmersionMenuHelper albumDetailSortImmersionMenuHelper = this.mSortImmersionMenuHelper;
        if (albumDetailSortImmersionMenuHelper != null) {
            albumDetailSortImmersionMenuHelper.dismiss();
        }
        super.onDestroy();
    }

    public void saveAlbumSortToPreference() {
        boolean equals = this.mViewModel.getSortOrder().equals(" DESC ");
        int i = this.mViewModel.getSortBy() == SortBy.UPDATE_DATE ? !equals : 0;
        if (this.mViewModel.getSortBy() == SortBy.CREATE_DATE) {
            i = equals ? 2 : 3;
        }
        if (this.mViewModel.getSortBy() == SortBy.NAME) {
            i = equals ? 4 : 5;
        }
        if (this.mViewModel.getSortBy() == SortBy.SIZE) {
            i = equals ? 6 : 7;
        }
        GalleryPreferences.Album.setAlbumDetailSort(this.mAlbumId, i);
    }

    public final void showReplaceAlbumCoverImmersionMenuDialog() {
        if (getActivity() == null) {
            return;
        }
        if (isAllPhotosAlbum() || isVideoAlbum() || isFavoritesAlbum() || this.mIsScreenshotRecorderAlbum) {
            Album generateVirtualAlbumBean = ReplaceAlbumCoverUtils.generateVirtualAlbumBean(this.mAlbumId);
            this.mReplaceOperationAlbum = generateVirtualAlbumBean;
            generateVirtualAlbumBean.setAlbumName(this.mAlbumName);
            this.mReplaceOperationAlbum.setServerId(getActivity().getIntent().getStringExtra("album_server_id"));
            this.mReplaceOperationAlbum.setAttributes(getActivity().getIntent().getLongExtra("attributes", 0L));
        } else {
            Cursor query = AlbumCacheManager.getInstance().query(AlbumManager.QUERY_ALBUM_PROJECTION, "_id=?", new String[]{String.valueOf(this.mAlbumId)}, (String) null, (String) null, (String) null, (Bundle) null);
            if (query != null && query.moveToFirst()) {
                this.mReplaceOperationAlbum = Album.fromCursor(query);
            }
        }
        if (this.mReplaceOperationAlbum == null) {
            return;
        }
        if (this.mReplaceAlbumCoverCallBack == null) {
            this.mReplaceAlbumCoverCallBack = new ReplaceAlbumCoverUtils.CallBack() { // from class: com.miui.gallery.ui.AlbumDetailFragment.5
                @Override // com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.CallBack
                public void onFailed(Collection<Album> collection, long j) {
                }

                {
                    AlbumDetailFragment.this = this;
                }

                @Override // com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.CallBack
                public void onSuccess(List<Pair<Album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> list) {
                    if (AlbumDetailFragment.this.getActivity() == null) {
                        return;
                    }
                    if (list.isEmpty()) {
                        ToastUtils.makeText(AlbumDetailFragment.this.getThemedContext(), (int) R.string.operation_replace_album_cover_status_default_mode_success);
                        return;
                    }
                    DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult doReplaceAlbumCoverResult = (DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult) list.get(0).second;
                    AlbumDetailFragment.this.getActivity().getIntent().putExtra("extra_is_manual_set_cover", doReplaceAlbumCoverResult.isManualSetCover());
                    if (doReplaceAlbumCoverResult.isManualSetCover().booleanValue()) {
                        ToastUtils.makeText(AlbumDetailFragment.this.getThemedContext(), (int) R.string.operation_replace_album_cover_status_success);
                    } else {
                        ToastUtils.makeText(AlbumDetailFragment.this.getThemedContext(), (int) R.string.operation_replace_album_cover_status_default_mode_success);
                    }
                }
            };
        }
        ReplaceAlbumCoverUtils.startReplaceAlbumCoverProcess(Collections.singletonList(this.mReplaceOperationAlbum), this, this.mReplaceAlbumCoverCallBack, b.l);
    }

    public final String getPageTip() {
        return isSecretAlbum() ? "403.51.0.1.11419" : isAllPhotosAlbum() ? "403.44.0.1.11210" : "403.15.1.1.11176";
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public boolean isPasteSupported() {
        return this.mMenuStatusManager.checkOperationSupport(1L) && !KeyboardShortcutsCopyHelper.getInstance().isCopyListEmpty() && (!KeyboardShortcutsCopyHelper.getInstance().deleteOrigin() || !isShareAlbum());
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        if (isPasteSupported()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getPasteShortcutInfo());
        }
        if (this.mEditableWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getCopyShortcutInfo());
            if (!isOthersShareAlbum()) {
                arrayList.add(KeyboardShortcutGroupManager.getInstance().getCutShortcutInfo());
            }
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        } else {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getYearShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getMonthCompactShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getMonthLooseShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getDayShortcutInfo());
        }
        list.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public final View getActionEndView() {
        return this.mActivity.getAppCompatActionBar().getEndView();
    }
}
