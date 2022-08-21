package com.miui.gallery.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miui.core.SdkHelper;
import com.miui.gallery.R;
import com.miui.gallery.activity.facebaby.BabyLockWallpaperSettingActivity;
import com.miui.gallery.adapter.AlbumType;
import com.miui.gallery.adapter.BabyAlbumDetailAdapter;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.baby.BabyAlbumUtils;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.permission.core.AppOpUtils;
import com.miui.gallery.picker.PickGalleryActivity;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.deprecated.ThumbnailInfo;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.actionBar.BaseCommonActionBarHelper;
import com.miui.gallery.ui.actionBar.SimpleThemeActionBarHelper;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.BurstFilterCursor;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.baby.BabyAlbumRecommendationFinder;
import com.miui.gallery.util.baby.BabyFaceFinder;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.GalleryPullZoomLayout;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsule;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class BabyAlbumDetailFragment extends AlbumDetailFragmentBase {
    public SimpleThemeActionBarHelper mActionBarHelper;
    public String mBabyAlbumPeopleServerId;
    public BabyInfo mBabyInfo;
    public ImageView mCornerView;
    public View mEmptyView;
    public View mFaceGroup;
    public View mGotoPickHeaderBackgroundMaskView;
    public float mProgress;
    public TextView mRecommendFacePhoto2Text;
    public View mRecommendFacePhoto2ThisAlbumView;
    public GalleryPullZoomLayout mScrollingLayout;
    public boolean mShowInPhotosTab;
    public ThumbnailInfo mThumbnailInfo;
    public View recommendFacePhoto2TextBtn;
    public Handler mHandler = new Handler();
    public MyRecommendationPhotoHelper mMyRecommendationPhotoHelper = new MyRecommendationPhotoHelper();
    public boolean mIsInActionMode = false;

    public static /* synthetic */ void $r8$lambda$KUD4C17TXMi2YhLwdxpLF_L6hR8(BabyAlbumDetailFragment babyAlbumDetailFragment, int i) {
        babyAlbumDetailFragment.lambda$onInflateView$1(i);
    }

    public static /* synthetic */ void $r8$lambda$V82qnAFTwZV4JWvtQtMOdhhacR4(BabyAlbumDetailFragment babyAlbumDetailFragment, View view) {
        babyAlbumDetailFragment.lambda$onInflateView$0(view);
    }

    public static /* synthetic */ void $r8$lambda$YrGH8WnTijbaDKptp_EWqNZ_tVY(BabyAlbumDetailFragment babyAlbumDetailFragment, DialogInterface dialogInterface, int i) {
        babyAlbumDetailFragment.lambda$onOptionsItemSelected$4(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$aGvHIlFcH3SAYBwkBclbjF8Sr4g(BabyAlbumDetailFragment babyAlbumDetailFragment, int i, GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        babyAlbumDetailFragment.lambda$onInflateView$2(i, scrollBy, f);
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String getCurrentSortOrder() {
        return "alias_create_time DESC ";
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public int getLayoutSource() {
        return R.layout.baby_album_detail;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "baby";
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public boolean isPasteSupported() {
        return true;
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        this.mFastScrollerMarginTop = getResources().getDimensionPixelOffset(R.dimen.baby_album_page_header_first_height) + getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
        this.mScrollingLayout = (GalleryPullZoomLayout) onInflateView.findViewById(R.id.scrolling_layout);
        this.mRecyclerView.setNestedScrollingEnabled(true);
        Intent intent = this.mActivity.getIntent();
        this.mShowInPhotosTab = (intent.getLongExtra("attributes", 0L) & 4) != 0;
        this.mFaceGroup = onInflateView.findViewById(R.id.face_ll);
        this.mRecommendFacePhoto2ThisAlbumView = onInflateView.findViewById(R.id.recommend_face_photo_to_this_album);
        View findViewById = onInflateView.findViewById(R.id.recommend_face_photo_to_this_album_button);
        this.recommendFacePhoto2TextBtn = findViewById;
        FolmeUtil.setDefaultTouchAnim(findViewById, null, false, false, true);
        this.mRecommendFacePhoto2Text = (TextView) onInflateView.findViewById(R.id.recommend_face_photo_to_this_album_text);
        this.mCornerView = (ImageView) onInflateView.findViewById(R.id.image_corner_view);
        this.mBabyAlbumPeopleServerId = intent.getStringExtra("people_id");
        this.mEditableWrapper.setAdapter(mo1564getAdapter());
        mo1564getAdapter().setSpanCount(this.mColumns);
        mo1564getAdapter().setSpacing(this.mSpacing);
        this.mRecyclerView.setScrollingCalculator(mo1564getAdapter());
        this.mRecyclerView.setCapsuleCalculator(mo1564getAdapter());
        this.mRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
        final int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line) + MiscUtil.getDefaultSplitActionBarHeight(this.mActivity);
        this.mRecyclerView.setFastScrollerBottomMargin(dimensionPixelSize);
        this.mEditableWrapper.setDragDataProvider(getDragDataProvider());
        this.mEditableWrapper.setHandleTouchAnimItemType(AlbumDetailGridItem.class.getSimpleName());
        this.mEmptyView = onInflateView.findViewById(16908292);
        this.mGotoPickHeaderBackgroundMaskView = onInflateView.findViewById(R.id.goto_pick_header_background_mask);
        this.mThumbnailInfo = new ThumbnailInfo(getOriginalAlbumId(), this.mIsOtherShareAlbum, intent.getStringExtra("thumbnail_info_of_baby"));
        this.mBabyInfo = BabyInfo.fromJSON(intent.getStringExtra("baby_info"));
        initialFaceHeader((BabyAlbumDetailFaceHeaderItem) onInflateView.findViewById(R.id.face_header_item), this.mBabyInfo, this.mThumbnailInfo, this.mAlbumName, intent.getStringExtra("baby_sharer_info"), this.mBabyAlbumPeopleServerId);
        BaseCommonActionBarHelper.DefaultThemeConfig defaultThemeConfig = new BaseCommonActionBarHelper.DefaultThemeConfig();
        defaultThemeConfig.setActionBarLayoutRes(R.layout.baby_album_page_title);
        defaultThemeConfig.setOnBackClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BabyAlbumDetailFragment.$r8$lambda$V82qnAFTwZV4JWvtQtMOdhhacR4(BabyAlbumDetailFragment.this, view);
            }
        });
        SimpleThemeActionBarHelper simpleThemeActionBarHelper = new SimpleThemeActionBarHelper(this.mActivity, defaultThemeConfig);
        this.mActionBarHelper = simpleThemeActionBarHelper;
        simpleThemeActionBarHelper.inflateActionBar();
        this.mActionBarHelper.setNullStyleActionBar();
        this.mActionBarHelper.setActionBarTitle(this.mAlbumName);
        this.mScrollingLayout.post(new Runnable() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.1
            {
                BabyAlbumDetailFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                BabyAlbumDetailFragment.this.mScrollingLayout.setActionBarHeight(BabyAlbumDetailFragment.this.mActivity.getAppCompatActionBar().getHeight());
            }
        });
        this.mScrollingLayout.setOnScrollListener(new GalleryPullZoomLayout.OnScrollListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.widget.GalleryPullZoomLayout.OnScrollListener
            public final void onScrolled(GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
                BabyAlbumDetailFragment.$r8$lambda$aGvHIlFcH3SAYBwkBclbjF8Sr4g(BabyAlbumDetailFragment.this, dimensionPixelSize, scrollBy, f);
            }
        });
        this.mRecyclerView.setFastScrollerCapsuleViewProvider(new FastScrollerCapsuleViewProvider() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.2
            @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
            public boolean isShowCapsule() {
                return true;
            }

            {
                BabyAlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
            public FastScrollerCapsule createFastScrollerCapsule() {
                FastScrollerStringCapsuleView fastScrollerStringCapsuleView = new FastScrollerStringCapsuleView(BabyAlbumDetailFragment.this.getContext());
                fastScrollerStringCapsuleView.setStyle(R.style.FastScrollStringCapsule);
                return fastScrollerStringCapsuleView;
            }
        });
        this.mIsBabyAlbum = true;
        return onInflateView;
    }

    public /* synthetic */ void lambda$onInflateView$0(View view) {
        this.mActivity.finish();
    }

    public /* synthetic */ void lambda$onInflateView$2(int i, GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        this.mProgress = f;
        if (!this.mIsInActionMode) {
            this.mActionBarHelper.refreshTopBar(f);
        }
        if (!this.mActionBarHelper.isShowTranslucentStatusBar()) {
            this.mEditableWrapper.setScrollPickEnable(true);
            this.mRecyclerView.setFastScrollEnabled(true);
            this.mRecyclerView.setOnFastScrollerStateChangedListener(new FastScrollerBar.OnStateChangedListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment$$ExternalSyntheticLambda4
                @Override // com.miui.gallery.widget.recyclerview.FastScrollerBar.OnStateChangedListener
                public final void onStateChanged(int i2) {
                    BabyAlbumDetailFragment.$r8$lambda$KUD4C17TXMi2YhLwdxpLF_L6hR8(BabyAlbumDetailFragment.this, i2);
                }
            });
            this.mRecyclerView.setFastScrollerBottomMargin(i);
            return;
        }
        this.mEditableWrapper.setScrollPickEnable(false);
        this.mRecyclerView.setFastScrollEnabled(false);
    }

    public /* synthetic */ void lambda$onInflateView$1(int i) {
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.reductionTouchView();
            if (i != 2) {
                return;
            }
            TrackController.trackClick("403.42.1.1.11292", AutoTracking.getRef());
        }
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.PhotoListFragmentBase, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration();
    }

    public final void updateConfiguration() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.recommendFacePhoto2TextBtn.getLayoutParams();
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.baby_album_recommend_btn_margin_se);
        layoutParams.setMarginStart(dimensionPixelOffset);
        layoutParams.setMarginEnd(dimensionPixelOffset);
        ((RelativeLayout.LayoutParams) this.mFaceGroup.getLayoutParams()).setMarginStart(getResources().getDimensionPixelOffset(R.dimen.baby_album_detail_face_margin_start));
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        View view = this.mEmptyView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        ((EmptyPage) view).resumeMaml();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        View view = this.mEmptyView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        ((EmptyPage) view).pauseMaml();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        View view = this.mEmptyView;
        if (view != null && (view instanceof EmptyPage)) {
            ((EmptyPage) view).destroyMaml();
        }
        GalleryPullZoomLayout galleryPullZoomLayout = this.mScrollingLayout;
        if (galleryPullZoomLayout != null) {
            galleryPullZoomLayout.setOnScrollListener(null);
        }
    }

    public final void initialFaceHeader(BabyAlbumDetailFaceHeaderItem babyAlbumDetailFaceHeaderItem, BabyInfo babyInfo, ThumbnailInfo thumbnailInfo, String str, String str2, String str3) {
        mo1564getAdapter().setAlbumId(getOriginalAlbumId());
        mo1564getAdapter().setPeopleServerId(str3);
        mo1564getAdapter().setFaceHeader(babyInfo, thumbnailInfo, str, str2, babyAlbumDetailFaceHeaderItem, this.mGotoPickHeaderBackgroundMaskView, new View.OnClickListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.3
            {
                BabyAlbumDetailFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BabyAlbumDetailFragment babyAlbumDetailFragment = BabyAlbumDetailFragment.this;
                if (babyAlbumDetailFragment.mIsOtherShareAlbum || !babyAlbumDetailFragment.mActionBarHelper.isShowTranslucentStatusBar()) {
                    return;
                }
                BabyAlbumDetailFragment.this.showMenuDialog();
            }
        }, this.mIsOtherShareAlbum);
    }

    public final void gotoPickHeaderBackground() {
        Intent intent = new Intent(this.mActivity, PickGalleryActivity.class);
        intent.putExtra("pick-upper-bound", 1);
        intent.putExtra("pick-need-id", true);
        startActivityForResult(intent, 18);
    }

    public final void showMenuDialog() {
        new AlertDialog.Builder(this.mActivity).setSingleChoiceItems(new String[]{this.mActivity.getString(R.string.baby_album_change_background)}, -1, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.4
            {
                BabyAlbumDetailFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (i == 0) {
                    BabyAlbumDetailFragment.this.gotoPickHeaderBackground();
                    SamplingStatHelper.recordCountEvent("baby", "baby_change_head_background");
                    return;
                }
                throw new IllegalStateException("unknown item clicked: " + i);
            }
        }).create().show();
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.PhotoListFragmentBase
    public void onDataLoaded(int i) {
        Cursor cursor = mo1564getAdapter().getCursor();
        if (cursor == null || cursor.getCount() == 0) {
            return;
        }
        if (cursor.getCount() == 1) {
            cursor.moveToPosition(0);
        } else {
            cursor.moveToPosition(1);
        }
        mo1564getAdapter().firstBindHeaderPic(cursor);
        MyRecommendationPhotoHelper myRecommendationPhotoHelper = this.mMyRecommendationPhotoHelper;
        if (cursor instanceof BurstFilterCursor) {
            cursor = ((BurstFilterCursor) cursor).getWrappedCursor();
        }
        myRecommendationPhotoHelper.seeIfHasRecommendationPhotoFromFaceAlbum(cursor);
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getAdapter */
    public BabyAlbumDetailAdapter mo1564getAdapter() {
        if (this.mAlbumDetailAdapter == 0) {
            this.mAlbumDetailAdapter = new BabyAlbumDetailAdapter(this.mActivity, getLifecycle());
            if (isOthersShareAlbum()) {
                this.mAlbumDetailAdapter.setAlbumType(AlbumType.OTHER_SHARE_BABY);
            } else {
                this.mAlbumDetailAdapter.setAlbumType(AlbumType.BABY);
            }
        }
        return (BabyAlbumDetailAdapter) this.mAlbumDetailAdapter;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.baby_album, menu);
        MenuItem findItem = menu.findItem(R.id.menu_share);
        MenuItem findItem2 = menu.findItem(R.id.menu_set_as_baby_lock_wallpaper);
        findItem.setVisible(ApplicationHelper.supportShare());
        findItem2.setVisible(SdkHelper.IS_MIUI && BuildUtil.isDefaultLockStyle());
        this.mRecyclerView.post(new Runnable() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.5
            {
                BabyAlbumDetailFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                BabyAlbumDetailFragment babyAlbumDetailFragment = BabyAlbumDetailFragment.this;
                if (babyAlbumDetailFragment.mActivity == null) {
                    return;
                }
                GalleryRecyclerView galleryRecyclerView = babyAlbumDetailFragment.mRecyclerView;
                galleryRecyclerView.setPadding(galleryRecyclerView.getPaddingLeft(), BabyAlbumDetailFragment.this.mRecyclerView.getTop(), BabyAlbumDetailFragment.this.mRecyclerView.getPaddingRight(), MiscUtil.getDefaultSplitActionBarHeight(BabyAlbumDetailFragment.this.mActivity));
            }
        });
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public boolean needEnableAutoUpload() {
        return !SyncUtil.isGalleryCloudSyncable(this.mActivity);
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        LinearMotorHelper.performHapticFeedback(this.mActivity, LinearMotorHelper.HAPTIC_TAP_LIGHT);
        switch (menuItem.getItemId()) {
            case R.id.menu_baby_info /* 2131362878 */:
                mo1564getAdapter().gotoBabyInfoSettingPage(this.mIsOtherShareAlbum);
                SamplingStatHelper.recordCountEvent("baby", "baby_edit_baby_info");
                TrackController.trackClick("403.42.2.1.11298", AutoTracking.getRef());
                return true;
            case R.id.menu_send_shortcut /* 2131362916 */:
                if (AppOpUtils.isShortCutEnable(getContext())) {
                    Bitmap faceImageOfFaceHeaderItem = mo1564getAdapter().getFaceImageOfFaceHeaderItem();
                    Intent intent = this.mActivity.getIntent();
                    IntentUtil.createShortCutForBabyAlbum(this.mActivity, this.mIsOtherShareAlbum, this.mAlbumId, this.mAlbumName, faceImageOfFaceHeaderItem, intent.getStringExtra("people_id"), intent.getStringExtra("thumbnail_info_of_baby"), intent.getStringExtra("baby_info"), intent.getStringExtra("baby_sharer_info"));
                    if (AppOpUtils.isShortCutEnable(getContext())) {
                        AppCompatActivity appCompatActivity = this.mActivity;
                        ToastUtils.makeText(appCompatActivity, appCompatActivity.getString(R.string.success_create_quick_icon_on_desk));
                    }
                    SamplingStatHelper.recordCountEvent("baby", "baby_send_shortcut");
                    TrackController.trackClick("403.42.2.1.11295", AutoTracking.getRef());
                } else {
                    new AlertDialog.Builder(getContext()).setTitle(getString(R.string.grant_permission_title)).setMessage(getString(R.string.grant_permission_shortcut)).setNegativeButton(17039360, BabyAlbumDetailFragment$$ExternalSyntheticLambda1.INSTANCE).setPositiveButton(R.string.grant_permission_go_and_set_2, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            BabyAlbumDetailFragment.$r8$lambda$YrGH8WnTijbaDKptp_EWqNZ_tVY(BabyAlbumDetailFragment.this, dialogInterface, i);
                        }
                    }).show();
                }
                return true;
            case R.id.menu_set_as_baby_lock_wallpaper /* 2131362917 */:
                setAsBabyLockWallpaper();
                SamplingStatHelper.recordCountEvent("baby", "baby_set_lock_wallpaper");
                TrackController.trackClick("403.42.2.1.11296", AutoTracking.getRef());
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public static /* synthetic */ void lambda$onOptionsItemSelected$3(DialogInterface dialogInterface, int i) {
        TrackController.trackClick("403.42.2.1.14905", AutoTracking.getRef(), "cancel");
    }

    public /* synthetic */ void lambda$onOptionsItemSelected$4(DialogInterface dialogInterface, int i) {
        TrackController.trackClick("403.42.2.1.14905", AutoTracking.getRef(), "sure");
        IntentUtil.enterGalleryPermissionSetting(getContext());
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public void onEnterActionMode() {
        this.mIsInActionMode = true;
        SimpleThemeActionBarHelper simpleThemeActionBarHelper = this.mActionBarHelper;
        if (simpleThemeActionBarHelper != null) {
            simpleThemeActionBarHelper.refreshTopBar(1.0f);
        }
        if (this.mShowInPhotosTab) {
            ImageSelectionTipFragment.showImageSelectionTipDialogIfNecessary(getActivity());
        }
        TrackController.trackExpose("403.42.3.1.11305", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public void onExitActionMode() {
        super.onExitActionMode();
        this.mIsInActionMode = false;
        SimpleThemeActionBarHelper simpleThemeActionBarHelper = this.mActionBarHelper;
        if (simpleThemeActionBarHelper != null) {
            simpleThemeActionBarHelper.refreshTopBar(this.mProgress);
        }
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        ArrayList arrayList;
        if (i == 12) {
            if (intent == null) {
                return;
            }
            this.mThumbnailInfo = new ThumbnailInfo(getOriginalAlbumId(), this.mIsOtherShareAlbum, intent.getStringExtra("thumbnail_info_of_baby"));
            mo1564getAdapter().resetBabyInfoAndThumbnailInfo((BabyInfo) intent.getExtras().getParcelable("baby-info"), this.mThumbnailInfo);
        } else if (i == 14) {
            if (i2 != -1 || intent == null) {
                return;
            }
            this.mMyRecommendationPhotoHelper.onSaveChoosedPeopleAndGo2ChooseFace(intent);
        } else if (i != 18) {
            if (i != 31) {
                super.onActivityResult(i, i2, intent);
            } else if (i2 != -1 || intent == null) {
            } else {
                this.mMyRecommendationPhotoHelper.onBabyPicturesPicked(intent);
            }
        } else if (i2 != -1 || intent == null || (arrayList = (ArrayList) intent.getSerializableExtra("pick-result-data")) == null || arrayList.isEmpty()) {
        } else {
            setBabyAlbumBg(((Long) arrayList.get(0)).longValue());
            this.mGotoPickHeaderBackgroundMaskView.setVisibility(8);
        }
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public String getCreatorIdByPosition(int i) {
        return mo1564getAdapter().getCreatorId(i);
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.PhotoListFragmentBase
    public Uri getUri() {
        return getUri(SortBy.CREATE_DATE);
    }

    public final void setAsBabyLockWallpaper() {
        Intent intent = new Intent(this.mActivity, BabyLockWallpaperSettingActivity.class);
        intent.putExtra("album_checked_by_default", String.valueOf(getOriginalAlbumId()));
        intent.putExtra("is_other_shared", this.mIsOtherShareAlbum);
        startActivity(intent);
    }

    public final long getOriginalAlbumId() {
        return this.mIsOtherShareAlbum ? ShareAlbumHelper.getOriginalAlbumId(this.mAlbumId) : this.mAlbumId;
    }

    public final void setBabyAlbumBg(final long j) {
        final ThumbnailInfo thumbnailInfo;
        if (j >= 0 && (thumbnailInfo = this.mThumbnailInfo) != null) {
            new AsyncTask<Void, Void, String>() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.6
                {
                    BabyAlbumDetailFragment.this = this;
                }

                @Override // android.os.AsyncTask
                public String doInBackground(Void... voidArr) {
                    return thumbnailInfo.setBgImage(j);
                }

                @Override // android.os.AsyncTask
                public void onPostExecute(String str) {
                    if (!TextUtils.isEmpty(str)) {
                        BabyAlbumDetailFragment.this.mo1564getAdapter().rebindHeaderPic(str);
                    }
                }
            }.execute(new Void[0]);
        }
    }

    /* loaded from: classes2.dex */
    public class MyRecommendationPhotoHelper {
        public SparseArray<Boolean> mAllBabyAlbumPhoto;
        public BabyAlbumRecommendationFinder mBabyAlbumRecommendationFinder;
        public BabyFaceFinder mBabyFaceFinder;
        public BabyAlbumRecommendationFinder.RecommendationDatas mRecommendPhotoDatas;

        public MyRecommendationPhotoHelper() {
            BabyAlbumDetailFragment.this = r1;
        }

        public final void seeIfHasRecommendationPhotoFromFaceAlbum(Cursor cursor) {
            if (BabyAlbumDetailFragment.this.mBabyInfo == null) {
                return;
            }
            BabyAlbumDetailFragment babyAlbumDetailFragment = BabyAlbumDetailFragment.this;
            if (!babyAlbumDetailFragment.mIsOtherShareAlbum || !TextUtils.isEmpty(babyAlbumDetailFragment.mBabyInfo.mPeopleId)) {
                if (!TextUtils.isEmpty(BabyAlbumDetailFragment.this.mBabyAlbumPeopleServerId) && !BabyAlbumDetailFragment.this.mBabyInfo.mAutoupdate) {
                    if (this.mBabyAlbumRecommendationFinder == null) {
                        BabyAlbumRecommendationFinder babyAlbumRecommendationFinder = new BabyAlbumRecommendationFinder(BabyAlbumDetailFragment.this.mBabyAlbumPeopleServerId);
                        this.mBabyAlbumRecommendationFinder = babyAlbumRecommendationFinder;
                        babyAlbumRecommendationFinder.setRecommendationFoundListener(new BabyAlbumRecommendationFinder.RecommendationFoundListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.MyRecommendationPhotoHelper.2
                            {
                                MyRecommendationPhotoHelper.this = this;
                            }

                            @Override // com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.RecommendationFoundListener
                            public void onRecommendationFound(final BabyAlbumRecommendationFinder.RecommendationDatas recommendationDatas) {
                                BabyAlbumDetailFragment.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.MyRecommendationPhotoHelper.2.1
                                    {
                                        AnonymousClass2.this = this;
                                    }

                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AppCompatActivity appCompatActivity = BabyAlbumDetailFragment.this.mActivity;
                                        if (appCompatActivity == null || appCompatActivity.isFinishing()) {
                                            return;
                                        }
                                        MyRecommendationPhotoHelper.this.refreshRecommandBar(recommendationDatas);
                                    }
                                });
                            }
                        });
                    }
                    this.mAllBabyAlbumPhoto = new SparseArray<>();
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String string = cursor.getString(cursor.getColumnIndex("title"));
                        if (!TextUtils.isEmpty(string)) {
                            this.mAllBabyAlbumPhoto.append(string.hashCode(), Boolean.TRUE);
                        }
                        cursor.moveToNext();
                    }
                    this.mBabyAlbumRecommendationFinder.findRecommendation(this.mAllBabyAlbumPhoto, String.valueOf(BabyAlbumDetailFragment.this.mAlbumId));
                    return;
                }
                disappearRecommendationView();
                return;
            }
            if (this.mBabyFaceFinder == null) {
                BabyFaceFinder babyFaceFinder = new BabyFaceFinder();
                this.mBabyFaceFinder = babyFaceFinder;
                babyFaceFinder.setBabyAlbumsFoundListener(new BabyFaceFinder.BabyAlbumsFoundListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.MyRecommendationPhotoHelper.1
                    {
                        MyRecommendationPhotoHelper.this = this;
                    }

                    @Override // com.miui.gallery.util.baby.BabyFaceFinder.BabyAlbumsFoundListener
                    public void onBabyAlbumsFound(final Boolean bool) {
                        BabyAlbumDetailFragment.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.MyRecommendationPhotoHelper.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                AppCompatActivity appCompatActivity = BabyAlbumDetailFragment.this.mActivity;
                                if (appCompatActivity == null || appCompatActivity.isFinishing()) {
                                    return;
                                }
                                MyRecommendationPhotoHelper.this.refreshRecommandBar(bool);
                            }
                        });
                    }
                });
            }
            this.mBabyFaceFinder.startFindFace(String.valueOf(BabyAlbumDetailFragment.this.mAlbumId));
        }

        public final void disappearRecommendationView() {
            BabyAlbumDetailFragment.this.mRecommendFacePhoto2ThisAlbumView.setVisibility(8);
            BabyAlbumDetailFragment.this.mCornerView.setVisibility(0);
        }

        public final void displayRecommendationView() {
            BabyAlbumDetailFragment.this.mRecommendFacePhoto2ThisAlbumView.setVisibility(0);
            BabyAlbumDetailFragment.this.mCornerView.setVisibility(8);
        }

        public final void refreshRecommandBar(Boolean bool) {
            if (!bool.booleanValue()) {
                disappearRecommendationView();
                return;
            }
            BabyAlbumDetailFragment babyAlbumDetailFragment = BabyAlbumDetailFragment.this;
            String string = babyAlbumDetailFragment.mActivity.getString(R.string.baby_album_sharer_recommand_face, new Object[]{babyAlbumDetailFragment.mBabyInfo.mNickName});
            BabyAlbumDetailFragment.this.recommendFacePhoto2TextBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.MyRecommendationPhotoHelper.3
                {
                    MyRecommendationPhotoHelper.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    BabyAlbumDetailFragment babyAlbumDetailFragment2 = BabyAlbumDetailFragment.this;
                    AppCompatActivity appCompatActivity = babyAlbumDetailFragment2.mActivity;
                    IntentUtil.pickPeople(appCompatActivity, appCompatActivity.getString(R.string.choose_some_people, new Object[]{babyAlbumDetailFragment2.mBabyInfo.mNickName}));
                    GalleryPreferences.Baby.setLastClickPeopleRecommandationTime(String.valueOf(BabyAlbumDetailFragment.this.mAlbumId));
                }
            });
            BabyAlbumDetailFragment.this.mRecommendFacePhoto2Text.setText(string);
            displayRecommendationView();
        }

        public final void refreshRecommandBar(final BabyAlbumRecommendationFinder.RecommendationDatas recommendationDatas) {
            int i;
            int recommendationSize;
            if (!recommendationDatas.hasNewRecommendation()) {
                disappearRecommendationView();
                return;
            }
            this.mRecommendPhotoDatas = recommendationDatas;
            if (!(GalleryPreferences.Baby.getLastClickBabyPhotosRecommandationTime(String.valueOf(BabyAlbumDetailFragment.this.mAlbumId)) == 0)) {
                i = R.plurals.baby_album_sharer_recommand_new_pictures;
                recommendationSize = recommendationDatas.getRecommendationSize();
            } else {
                i = R.plurals.baby_album_sharer_recommand_old_pictures;
                recommendationSize = recommendationDatas.getRecommendationSize();
            }
            String quantityString = BabyAlbumDetailFragment.this.mActivity.getResources().getQuantityString(i, recommendationSize, Integer.valueOf(recommendationSize), BabyAlbumDetailFragment.this.mBabyInfo.mNickName);
            BabyAlbumDetailFragment.this.recommendFacePhoto2TextBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.BabyAlbumDetailFragment.MyRecommendationPhotoHelper.4
                {
                    MyRecommendationPhotoHelper.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    BabyAlbumDetailFragment babyAlbumDetailFragment = BabyAlbumDetailFragment.this;
                    AppCompatActivity appCompatActivity = babyAlbumDetailFragment.mActivity;
                    String str = babyAlbumDetailFragment.mBabyInfo.mNickName;
                    BabyAlbumRecommendationFinder.RecommendationDatas recommendationDatas2 = recommendationDatas;
                    IntentUtil.pickFace(appCompatActivity, str, recommendationDatas2.peopleServerId, recommendationDatas2.peopleLocalId, recommendationDatas2.ids, -1, false);
                    GalleryPreferences.Baby.setLastClickBabyPhotosRecommandationTime(String.valueOf(BabyAlbumDetailFragment.this.mAlbumId));
                }
            });
            BabyAlbumDetailFragment.this.mRecommendFacePhoto2Text.setText(quantityString);
            displayRecommendationView();
        }

        public final void onSaveChoosedPeopleAndGo2ChooseFace(Intent intent) {
            String stringExtra = intent.getStringExtra("local_id_of_album");
            if (!TextUtils.isEmpty(stringExtra)) {
                BabyAlbumDetailFragment.this.mBabyInfo.mPeopleId = intent.getStringExtra("server_id_of_album");
                BabyAlbumDetailFragment babyAlbumDetailFragment = BabyAlbumDetailFragment.this;
                String valueOf = String.valueOf(babyAlbumDetailFragment.mIsOtherShareAlbum ? ShareAlbumHelper.getOriginalAlbumId(babyAlbumDetailFragment.mAlbumId) : babyAlbumDetailFragment.mAlbumId);
                BabyAlbumUtils.saveBabyInfo(valueOf, BabyAlbumDetailFragment.this.mBabyInfo, BabyAlbumDetailFragment.this.mIsOtherShareAlbum);
                ContentValues contentValues = new ContentValues(1);
                if (BabyAlbumDetailFragment.this.isOthersShareAlbum()) {
                    contentValues.put("peopleId", BabyAlbumDetailFragment.this.mBabyInfo.mPeopleId);
                } else {
                    ContentValues contentValues2 = new ContentValues(1);
                    contentValues2.put("peopleId", BabyAlbumDetailFragment.this.mBabyInfo.mPeopleId);
                    contentValues.put(CallMethod.ARG_EXTRA_STRING, AlbumDataHelper.genUpdateAlbumExtraInfoSql(contentValues2, false));
                }
                BabyAlbumDetailFragment.this.mActivity.getContentResolver().update(BabyAlbumDetailFragment.this.mIsOtherShareAlbum ? GalleryContract.ShareAlbum.OTHER_SHARE_URI : GalleryContract.Album.URI, contentValues, String.format("%s=%s", j.c, valueOf), null);
                BabyAlbumDetailFragment babyAlbumDetailFragment2 = BabyAlbumDetailFragment.this;
                IntentUtil.pickFace(babyAlbumDetailFragment2.mActivity, babyAlbumDetailFragment2.mBabyInfo.mNickName, BabyAlbumDetailFragment.this.mBabyInfo.mPeopleId, stringExtra, null, -1, false);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:50:0x007a  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x0081  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void onBabyPicturesPicked(android.content.Intent r15) {
            /*
                r14 = this;
                java.lang.String r0 = "pick-result-data"
                java.io.Serializable r15 = r15.getSerializableExtra(r0)
                java.util.ArrayList r15 = (java.util.ArrayList) r15
                if (r15 == 0) goto La5
                boolean r0 = r15.isEmpty()
                if (r0 == 0) goto L12
                goto La5
            L12:
                com.miui.gallery.ui.BabyAlbumDetailFragment r0 = com.miui.gallery.ui.BabyAlbumDetailFragment.this
                miuix.appcompat.app.AppCompatActivity r0 = r0.mActivity
                android.content.ContentResolver r1 = r0.getContentResolver()
                android.net.Uri r2 = com.miui.gallery.provider.GalleryContract.Cloud.CLOUD_URI
                java.lang.String r0 = "*"
                java.lang.String[] r3 = new java.lang.String[]{r0}
                r0 = 1
                java.lang.Object[] r4 = new java.lang.Object[r0]
                java.lang.String r5 = ","
                java.lang.String r5 = android.text.TextUtils.join(r5, r15)
                r7 = 0
                r4[r7] = r5
                java.lang.String r5 = "_id IN (%s)"
                java.lang.String r4 = java.lang.String.format(r5, r4)
                r5 = 0
                r6 = 0
                android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6)
                com.miui.gallery.ui.BabyAlbumDetailFragment r2 = com.miui.gallery.ui.BabyAlbumDetailFragment.this
                com.miui.gallery.cloud.baby.BabyInfo r2 = com.miui.gallery.ui.BabyAlbumDetailFragment.access$700(r2)
                if (r2 == 0) goto L6b
                com.miui.gallery.ui.BabyAlbumDetailFragment r2 = com.miui.gallery.ui.BabyAlbumDetailFragment.this
                com.miui.gallery.cloud.baby.BabyInfo r2 = com.miui.gallery.ui.BabyAlbumDetailFragment.access$700(r2)
                boolean r2 = r2.mAutoupdate
                if (r2 != 0) goto L6b
                com.miui.gallery.util.baby.BabyAlbumRecommendationFinder$RecommendationDatas r2 = r14.mRecommendPhotoDatas
                if (r2 == 0) goto L6c
                int r2 = r2.totalFaceCountInFaceAlbum
                int r15 = r15.size()
                if (r2 != r15) goto L59
                goto L6c
            L59:
                com.miui.gallery.ui.BabyAlbumDetailFragment r15 = com.miui.gallery.ui.BabyAlbumDetailFragment.this
                java.lang.String r15 = r15.mAlbumName
                boolean r15 = com.miui.gallery.preference.GalleryPreferences.Baby.getHasShowAutoUpdateTipWithoutSelectingAll(r15)
                if (r15 != 0) goto L6b
                com.miui.gallery.ui.BabyAlbumDetailFragment r15 = com.miui.gallery.ui.BabyAlbumDetailFragment.this
                java.lang.String r15 = r15.mAlbumName
                com.miui.gallery.preference.GalleryPreferences.Baby.setHasShowAutoUpdateTipWithoutSelectingAll(r15)
                goto L6c
            L6b:
                r0 = r7
            L6c:
                com.miui.gallery.ui.BabyAlbumDetailFragment$MyRecommendationPhotoHelper$5 r15 = new com.miui.gallery.ui.BabyAlbumDetailFragment$MyRecommendationPhotoHelper$5
                r15.<init>()
                com.miui.gallery.model.SendToCloudFolderItem r0 = new com.miui.gallery.model.SendToCloudFolderItem
                r9 = 0
                com.miui.gallery.ui.BabyAlbumDetailFragment r2 = com.miui.gallery.ui.BabyAlbumDetailFragment.this
                boolean r3 = r2.mIsOtherShareAlbum
                if (r3 == 0) goto L81
                long r2 = r2.mAlbumId
                long r2 = com.miui.gallery.provider.ShareAlbumHelper.getOriginalAlbumId(r2)
                goto L83
            L81:
                long r2 = r2.mAlbumId
            L83:
                java.lang.String r10 = java.lang.String.valueOf(r2)
                com.miui.gallery.ui.BabyAlbumDetailFragment r2 = com.miui.gallery.ui.BabyAlbumDetailFragment.this
                boolean r11 = r2.mIsOtherShareAlbum
                java.lang.String r12 = r2.mAlbumName
                r13 = 0
                r8 = r0
                r8.<init>(r9, r10, r11, r12, r13)
                com.miui.gallery.ui.BabyAlbumDetailFragment r2 = com.miui.gallery.ui.BabyAlbumDetailFragment.this
                miuix.appcompat.app.AppCompatActivity r2 = r2.mActivity
                r3 = 2131886157(0x7f12004d, float:1.9406885E38)
                com.miui.gallery.util.baby.CopyFaceAlbumItemsToBabyAlbumTask r0 = com.miui.gallery.util.baby.CopyFaceAlbumItemsToBabyAlbumTask.instance(r2, r1, r0, r7, r3)
                r0.setProgressFinishListener(r15)
                java.lang.Void[] r15 = new java.lang.Void[r7]
                r0.execute(r15)
            La5:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.BabyAlbumDetailFragment.MyRecommendationPhotoHelper.onBabyPicturesPicked(android.content.Intent):void");
        }
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(KeyboardShortcutGroupManager.getInstance().getPasteShortcutInfo());
        if (this.mEditableWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getCopyShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getCutShortcutInfo());
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
}
