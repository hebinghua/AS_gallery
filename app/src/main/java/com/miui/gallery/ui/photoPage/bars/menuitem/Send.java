package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TextView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.tracing.Trace;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.ImageSelectionTipFragment;
import com.miui.gallery.ui.PhotoChoiceTitle;
import com.miui.gallery.ui.ShareStateRouter;
import com.miui.gallery.ui.photoPage.PhotoPageOrientationManager;
import com.miui.gallery.ui.photoPage.PhotoPageThemeManager;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase;
import com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner;
import com.miui.gallery.ui.photoPage.bars.menuitem.Send;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.SecurityShareHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.widget.slip.VerticalSlipLayout;

/* loaded from: classes2.dex */
public class Send extends BaseMenuItemDelegate implements IManagerOwner {
    public boolean isOriginWindowModeIsLandscape;
    public ChoiceManager mChoiceManager;

    /* loaded from: classes2.dex */
    public static class ChooserObserverState extends ViewModel {
        public MutableLiveData<Boolean> mState = new MutableLiveData<>();
    }

    public static Send instance(IMenuItem iMenuItem) {
        return new Send(iMenuItem);
    }

    public Send(IMenuItem iMenuItem) {
        super(iMenuItem);
        iMenuItem.setNeedFolmeAnim(false);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public void doInitFunction() {
        ChoiceManager choiceManager = new ChoiceManager(this.mOwner.getOrientationController(), this.mOwner.getThemeController());
        this.mChoiceManager = choiceManager;
        choiceManager.initFunction();
        BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack = this.mConfigMenuCallBack;
        if (iConfigMenuCallBack != null) {
            iConfigMenuCallBack.setChoiceManager(this.mChoiceManager);
        }
        super.doInitFunction();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        this.mChoiceManager.onSendClick(baseDataItem);
        TrackController.trackClick("403.11.5.1.11159", AutoTracking.getRef());
    }

    /* loaded from: classes2.dex */
    public class ChoiceManager extends ChoiceManagerBase implements VerticalSlipLayout.OnSlipListener, Observer<Boolean> {
        public final boolean isEnterInChoiceMode;
        public boolean mBarsVisibleBeforeSlip;
        public Configuration mConfig;
        public BaseDataItem mCurrentItem;
        public final boolean mIsLargeScreenDevice;
        public boolean mIsLargeWindow;
        public final PhotoPageOrientationManager.IPhotoPageOrientationManagerController mOrientationController;
        public PhotoChoiceTitle mPhotoChoiceTitle;
        public Intent mShareTargetIntent;
        public VerticalSlipLayout mSlipLayout;
        public boolean mSlipPending;
        public float mSlipProgress;
        public Runnable mSlipRunnable;
        public boolean mSlipped;
        public final PhotoPageThemeManager.IPhotoPageThemeManagerController mThemeController;
        public boolean mTriggerSlipByConfigurationChange;

        public static /* synthetic */ void $r8$lambda$6TqzP1vOBZqTTBxQIdT6dNQZMx8(ChoiceManager choiceManager) {
            choiceManager.lambda$onSendClick$3();
        }

        public static /* synthetic */ void $r8$lambda$7dcaQiVgMWCLRyr_uyvAXQO9LrA(ChoiceManager choiceManager, View view) {
            choiceManager.lambda$initFunction$0(view);
        }

        public static /* synthetic */ void $r8$lambda$I9tBEjOnUOA5HBxTFjJgK3iTKGs(ChoiceManager choiceManager, Configuration configuration) {
            choiceManager.onConfigurationChange(configuration);
        }

        public static /* synthetic */ void $r8$lambda$W6jmld4uO5XMeEH_gy38iMyS6O0(ChoiceManager choiceManager) {
            choiceManager.lambda$onSlipEnd$2();
        }

        /* renamed from: $r8$lambda$c1aRtlhl-8OP9QlMBYL26tczqXA */
        public static /* synthetic */ void m1633$r8$lambda$c1aRtlhl8OP9QlMBYL26tczqXA(ChoiceManager choiceManager) {
            choiceManager.lambda$onUiOrientationChanged$1();
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public int getContainerId() {
            return R.id.child_container;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ChoiceManager(PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController, PhotoPageThemeManager.IPhotoPageThemeManagerController iPhotoPageThemeManagerController) {
            super(r3);
            Send.this = r3;
            this.isEnterInChoiceMode = this.mDataProvider.getFieldData().mArguments.getBoolean("com.miui.gallery.extra.photo_enter_choice_mode", false);
            this.mIsLargeScreenDevice = BaseBuildUtil.isLargeScreenDevice();
            this.mIsLargeWindow = BaseBuildUtil.isLargeScreenIndependentOrientation();
            this.mOrientationController = iPhotoPageOrientationManagerController;
            this.mThemeController = iPhotoPageThemeManagerController;
            this.mDataProvider.getViewModelData().addConfigurationObserver(this.mContext, this.mFragment, new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Send$ChoiceManager$$ExternalSyntheticLambda1
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    Send.ChoiceManager.$r8$lambda$I9tBEjOnUOA5HBxTFjJgK3iTKGs(Send.ChoiceManager.this, (Configuration) obj);
                }
            });
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public void initFunction() {
            PhotoPageAdapter photoPageAdapter;
            super.initFunction();
            View view = this.mFragment.getView();
            if (view == null) {
                DefaultLogger.e("PhotoPageFragment_MenuManager_MenuItem_Send", "fragment root view is null!");
                return;
            }
            VerticalSlipLayout verticalSlipLayout = (VerticalSlipLayout) view.findViewById(R.id.slip_layout);
            this.mSlipLayout = verticalSlipLayout;
            verticalSlipLayout.setOnSlipListener(this);
            this.mPhotoChoiceTitle = (PhotoChoiceTitle) view.findViewById(R.id.photo_choice_title);
            onUiOrientationChanged(this.mDataProvider.getFieldData().mCurrent.getConfiguration());
            if (this.isEnterInChoiceMode) {
                this.mSlipLayout.setDraggable(true);
                this.mSlipLayout.setSlippedWhenEnter(true);
                this.mDataProvider.getFieldData().mArguments.remove("com.miui.gallery.extra.photo_enter_choice_mode");
                setUpChooserFragment(0, true, 1, "image/*");
            }
            if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(this.mContext).booleanValue() && (photoPageAdapter = this.mAdapter) != null) {
                photoPageAdapter.setUseSlipModeV2(true);
            }
            this.mPhotoChoiceTitle.getExitButton().setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Send$ChoiceManager$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    Send.ChoiceManager.$r8$lambda$7dcaQiVgMWCLRyr_uyvAXQO9LrA(Send.ChoiceManager.this, view2);
                }
            });
        }

        public /* synthetic */ void lambda$initFunction$0(View view) {
            onBackPressed();
        }

        public final void onConfigurationChange(Configuration configuration) {
            Configuration configuration2;
            PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController;
            PhotoPageAdapter.ChoiceMode choiceMode;
            if (configuration != null && (configuration2 = this.mConfig) != null && configuration.orientation != configuration2.orientation && (iPhotoPageOrientationManagerController = this.mOrientationController) != null && !iPhotoPageOrientationManagerController.checkSensorAvailable() && (choiceMode = this.mChoiceMode) != null && choiceMode.isInAction()) {
                this.mConfig = configuration;
                return;
            }
            VerticalSlipLayout verticalSlipLayout = this.mSlipLayout;
            if (verticalSlipLayout != null && verticalSlipLayout.isSlipping()) {
                this.mConfig = configuration;
            } else {
                onUiOrientationChanged(configuration);
            }
        }

        public final void onUiOrientationChanged(Configuration configuration) {
            boolean z;
            if (this.mSlipLayout == null || configuration == null) {
                return;
            }
            this.mConfig = configuration;
            PhotoPageAdapter.ChoiceMode choiceMode = this.mChoiceMode;
            this.mTriggerSlipByConfigurationChange = choiceMode != null && choiceMode.isInAction();
            this.mIsLargeWindow = BaseBuildUtil.isLargeScreenIndependentOrientation();
            boolean slipLayoutDraggable = setSlipLayoutDraggable(configuration);
            if (this.mDataProvider.getFieldData().isHideInAdvanceByLandAction || ((z = this.mSlipped) && slipLayoutDraggable)) {
                if (this.mSlipped && (this.mIsLargeScreenDevice || this.mIsLargeWindow)) {
                    setUnSlipped(false);
                    Send.this.mOwner.configForLargeScreenDevice(configuration);
                } else {
                    this.mSlipPending = false;
                }
                if (this.mSlipRunnable == null) {
                    this.mSlipRunnable = new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Send$ChoiceManager$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            Send.ChoiceManager.m1633$r8$lambda$c1aRtlhl8OP9QlMBYL26tczqXA(Send.ChoiceManager.this);
                        }
                    };
                }
                ThreadManager.getMainHandler().removeCallbacks(this.mSlipRunnable);
                ThreadManager.getMainHandler().postDelayed(this.mSlipRunnable, 200L);
            } else if (!z && !this.mSlipLayout.isSlipped()) {
            } else {
                setUnSlipped(false);
                this.mSlipLayout.setDraggable(false);
                if (this.mDataProvider.getFieldData().isInPreviewMode || this.mDataProvider.getFieldData().isUsingCameraAnim) {
                    return;
                }
                setPhotoChoiceTitleVisible(false);
                PhotoPageAdapter.ChoiceMode choiceMode2 = this.mChoiceMode;
                if (choiceMode2 != null) {
                    choiceMode2.unChooseAll();
                    this.mChoiceMode.finish();
                }
                this.mBarsVisibleBeforeSlip = false;
                this.mSlipLayout.refreshSlipState(false);
                Send.this.mOwner.setActionBarVisible(true, true);
            }
        }

        public /* synthetic */ void lambda$onUiOrientationChanged$1() {
            if (this.mFragment.isAdded()) {
                setSlipped(true);
            }
        }

        public void settleItem(BaseDataItem baseDataItem) {
            if (baseDataItem == null) {
                return;
            }
            this.mCurrentItem = baseDataItem;
            setSlipLayoutDraggable(Send.this.mOwner.getCurrentItemScale(), this.mConfig);
            if (!ShareStateRouter.IS_MISHARE_AVAILABLE.get(this.mContext).booleanValue()) {
                return;
            }
            Send.this.mMenuItemManager.refreshProjectState();
        }

        public final boolean isSupportSend() {
            return !this.mDataProvider.getFieldData().isStartWhenLockedAndSecret && !this.mDataProvider.getFieldData().isFromRecommendFacePage && !this.mDataProvider.getFieldData().isPreviewMode && Send.this.isSupport();
        }

        public void onPhotoScale(float f) {
            setSlipLayoutDraggable(f, this.mConfig);
        }

        public final void viewRequestFocus(View view) {
            if (view == null || !view.isShown() || view.getVisibility() != 0) {
                return;
            }
            view.requestFocus(BaiduSceneResult.VISA);
        }

        public void setSlipped(boolean z) {
            PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController;
            if (this.mSlipLayout == null) {
                return;
            }
            if (!this.mIsLargeScreenDevice && !this.mIsLargeWindow && !this.mDataProvider.getFieldData().mCurrent.isInMultiWindowMode() && (iPhotoPageOrientationManagerController = this.mOrientationController) != null && !iPhotoPageOrientationManagerController.isPortraitConfiguration()) {
                this.mOrientationController.setRequestedOrientation(1, "setSlipped");
                this.mOrientationController.setSensorEnable(false);
                this.mSlipPending = true;
                return;
            }
            this.mSlipLayout.setSlipped(z);
        }

        public final void setSlipLayoutDraggable(float f, Configuration configuration) {
            if (this.mSlipProgress > 0.0f || this.mSlipped || f <= 1.0f) {
                setSlipLayoutDraggable(configuration);
            } else {
                this.mSlipLayout.setDraggable(false);
            }
        }

        public final boolean setSlipLayoutDraggable(Configuration configuration) {
            if (this.mSlipLayout == null) {
                return false;
            }
            boolean isSupportSend = isSupportSend();
            if (configuration != null && configuration.smallestScreenWidthDp < BaseBuildUtil.BIG_HORIZONTAL_WINDOW_STANDARD && configuration.orientation == 2) {
                isSupportSend &= this.mDataProvider.getFieldData().mCurrent.isInMultiWindowMode();
            }
            if (this.mSlipped) {
                this.mSlipLayout.setDraggable(true);
            } else {
                this.mSlipLayout.setDraggable(isSupportSend);
            }
            return isSupportSend;
        }

        public final void setPhotoChoiceTitleVisible(boolean z) {
            PhotoChoiceTitle photoChoiceTitle = this.mPhotoChoiceTitle;
            if (photoChoiceTitle == null) {
                return;
            }
            int i = z ? 0 : 4;
            if (i == photoChoiceTitle.getVisibility()) {
                return;
            }
            this.mPhotoChoiceTitle.setVisibility(i);
        }

        public boolean isSlipped() {
            VerticalSlipLayout verticalSlipLayout = this.mSlipLayout;
            return verticalSlipLayout != null && verticalSlipLayout.isSlipped();
        }

        public boolean isSlipping() {
            VerticalSlipLayout verticalSlipLayout = this.mSlipLayout;
            return verticalSlipLayout != null && verticalSlipLayout.isSlipping();
        }

        public final void setUnSlipped(boolean z) {
            VerticalSlipLayout verticalSlipLayout = this.mSlipLayout;
            if (verticalSlipLayout == null) {
                return;
            }
            verticalSlipLayout.setUnSlipped(z);
        }

        @Override // com.miui.gallery.widget.slip.VerticalSlipLayout.OnSlipListener
        public void onSlipStart(boolean z) {
            boolean z2 = this.mBarsVisibleBeforeSlip | z;
            this.mBarsVisibleBeforeSlip = z2;
            this.mTriggerSlipByConfigurationChange = z & this.mTriggerSlipByConfigurationChange;
            if (!this.mSlipped) {
                this.mBarsVisibleBeforeSlip = (this.mDataProvider.getFieldData().isHideInAdvanceByLandAction || Send.this.mOwner.isActionBarShowing()) | z2;
                Send.this.mOwner.setActionBarVisible(false, true);
                PhotoPageAdapter photoPageAdapter = this.mAdapter;
                if (photoPageAdapter != null) {
                    this.mChoiceMode = photoPageAdapter.startActionMode(this);
                }
                setPhotoChoiceTitleVisible(true);
                Send.this.mOwner.postRecordStringPropertyEvent("best_image_count", String.valueOf(MediaFeatureCacheManager.getInstance().getBestImageCount(false)));
            }
        }

        @Override // com.miui.gallery.widget.slip.VerticalSlipLayout.OnSlipListener
        public void onSlipping(float f) {
            this.mSlipProgress = f;
            PhotoPageAdapter photoPageAdapter = this.mAdapter;
            if (photoPageAdapter != null) {
                photoPageAdapter.setSlipProgress(f);
            }
        }

        public final void setSlippedValue(boolean z) {
            this.mSlipped = z;
            this.mDataProvider.getViewModelData().setSlippedValue(z);
        }

        @Override // com.miui.gallery.widget.slip.VerticalSlipLayout.OnSlipListener
        public void onSlipEnd(boolean z) {
            PhotoPageAdapter.ChoiceMode choiceMode;
            Trace.beginSection("onSlipEnd");
            refreshChooserState(z);
            setSlippedValue(z);
            setSlipLayoutDraggable(this.mConfig);
            if (!z) {
                setPhotoChoiceTitleVisible(false);
                if (!this.mTriggerSlipByConfigurationChange) {
                    PhotoPageAdapter.ChoiceMode choiceMode2 = this.mChoiceMode;
                    if (choiceMode2 != null) {
                        choiceMode2.unChooseAll();
                        this.mChoiceMode.finish();
                    }
                    AutoTracking.trackNavAndView("403.11.0.1.11536", "403.11.0.1.11151");
                    PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController = this.mOrientationController;
                    if (iPhotoPageOrientationManagerController != null) {
                        iPhotoPageOrientationManagerController.setSensorEnable(iPhotoPageOrientationManagerController.checkSensorAvailable());
                        this.mOrientationController.tryRestoreOrientation();
                    }
                    if (this.mDataProvider.getFieldData().mArguments.getBoolean("com.miui.gallery.extra.show_menu_after_choice_mode", false)) {
                        Send.this.mOwner.setActionBarVisible(true, true);
                    } else {
                        Send.this.mOwner.setActionBarVisible(this.mBarsVisibleBeforeSlip, true);
                    }
                    this.mBarsVisibleBeforeSlip = false;
                    Send send = Send.this;
                    send.mMenuItemManager.requestFocus(send);
                    Send.this.mOwner.postRecordCountEvent("photo", "fast_share_mode_exit");
                }
                this.mDataProvider.getFieldData().isHideInAdvanceByLandAction = false;
            } else {
                if (!this.mTriggerSlipByConfigurationChange) {
                    AutoTracking.trackNavAndView("403.37.0.1.11533", "403.37.0.1.11230");
                }
                PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController2 = this.mOrientationController;
                if (iPhotoPageOrientationManagerController2 != null) {
                    if (this.mIsLargeScreenDevice || this.mIsLargeWindow) {
                        if (iPhotoPageOrientationManagerController2.checkSensorAvailable()) {
                            this.mOrientationController.setSensorEnable(true);
                            this.mOrientationController.setRequestedOrientation(4, "onSlipEnd");
                        } else {
                            this.mOrientationController.setSensorEnable(false);
                        }
                    } else {
                        iPhotoPageOrientationManagerController2.setSensorEnable(false);
                        this.mOrientationController.setRequestedOrientation(1, "onSlipEnd");
                    }
                }
                BaseDataItem dataItem = this.mDataProvider.getFieldData().mCurrent.getDataItem();
                if (dataItem != null && (choiceMode = this.mChoiceMode) != null && !this.mTriggerSlipByConfigurationChange) {
                    choiceMode.setChecked(this.mDataProvider.getFieldData().mCurrent.getPosition(), dataItem.getKey(), true);
                }
                this.mTriggerSlipByConfigurationChange = false;
                viewRequestFocus(this.mPhotoChoiceTitle);
                Send.this.mOwner.postRecordCountEvent("photo", "fast_share_mode_enter");
                if (this.mShareTargetIntent != null && dataItem != null) {
                    Send.this.mChoiceManager.onIntentSelected(this.mShareTargetIntent);
                    this.mShareTargetIntent = null;
                }
                if (dataItem != null && MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(dataItem.getKey(), false, false, dataItem.getBurstKeys())) {
                    ImageSelectionTipFragment.showImageSelectionTipDialogIfNecessary(this.mContext);
                }
                this.mPhotoChoiceTitle.postDelayed(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Send$ChoiceManager$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        Send.ChoiceManager.$r8$lambda$W6jmld4uO5XMeEH_gy38iMyS6O0(Send.ChoiceManager.this);
                    }
                }, 500L);
            }
            Trace.endSection();
        }

        public /* synthetic */ void lambda$onSlipEnd$2() {
            PhotoPageAdapter photoPageAdapter = this.mAdapter;
            if (photoPageAdapter != null) {
                photoPageAdapter.notifyDataSetChanged();
            }
        }

        public final void refreshChooserState(boolean z) {
            if (z) {
                Send.this.mOwner.setScreenSceneEffect(true);
                if (this.mFragment.getChildFragmentManager().findFragmentByTag("ChooserFragment") == null) {
                    Send.this.mMenuItemManager.initFunction(PhotoPageMenuManager.MenuItemType.CAST);
                    ((ChooserObserverState) new ViewModelProvider(this.mFragment).get(ChooserObserverState.class)).mState.observe(this.mFragment, this);
                    BaseDataItem baseDataItem = this.mCurrentItem;
                    setUpChooserFragment(0, true, 1, (baseDataItem == null || baseDataItem.isImage()) ? "image/*" : "video/*");
                    return;
                }
                showChooserFragment();
                return;
            }
            Send.this.mOwner.setScreenSceneEffect(false);
            hideChooserFragment();
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(Boolean bool) {
            if (bool.booleanValue()) {
                Send.this.mMenuItemManager.refreshProjectState();
            }
        }

        @Override // com.miui.gallery.widget.slip.VerticalSlipLayout.OnSlipListener
        public void onSlipStateChanged(int i) {
            PhotoPageThemeManager.IPhotoPageThemeManagerController iPhotoPageThemeManagerController;
            this.mDataProvider.getFieldData().mCurrent.slipState = i;
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Send", "slipState changed state = [%d]", Integer.valueOf(i));
            if (this.mDataProvider.getFieldData().isHideInAdvanceByLandAction && (iPhotoPageThemeManagerController = this.mThemeController) != null) {
                iPhotoPageThemeManagerController.setLightTheme(true, true);
            } else if (i != 1) {
                Send.this.mOwner.refreshTheme(true);
            } else if (this.mOrientationController == null) {
            } else {
                if (this.mIsLargeWindow && this.mIsLargeScreenDevice) {
                    return;
                }
                TrackController.trackFling("403.11.4.1.11157");
                this.mOrientationController.noteRestoreOrientation();
            }
        }

        public boolean isPendingSlipped() {
            return (this.mDataProvider.getFieldData().mCurrent.slipState == 0 && isSlipped()) || this.mSlipPending || this.mBarsVisibleBeforeSlip || this.mSlipLayout.isFlingToSlipped();
        }

        public boolean onBackPressed() {
            VerticalSlipLayout verticalSlipLayout = this.mSlipLayout;
            boolean z = false;
            if (verticalSlipLayout != null && verticalSlipLayout.isSlipped()) {
                VerticalSlipLayout verticalSlipLayout2 = this.mSlipLayout;
                if (BaseBuildUtil.isLargeScreenIndependentOrientation() || !Send.this.isOriginWindowModeIsLandscape) {
                    z = true;
                }
                verticalSlipLayout2.setUnSlipped(z);
                return true;
            }
            return false;
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public TextView getChoiceTitle() {
            PhotoChoiceTitle photoChoiceTitle = this.mPhotoChoiceTitle;
            if (photoChoiceTitle != null) {
                return photoChoiceTitle.getTitle();
            }
            return null;
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public TextView getSubTitle() {
            PhotoChoiceTitle photoChoiceTitle = this.mPhotoChoiceTitle;
            if (photoChoiceTitle != null) {
                return photoChoiceTitle.getSubTitle();
            }
            return null;
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public void onShared(boolean z) {
            super.onShared(z);
            if (this.mDataProvider.getFieldData().isHideInAdvanceByLandAction) {
                return;
            }
            setUnSlipped(true);
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public void release() {
            super.release();
            if (this.mSlipRunnable != null) {
                ThreadManager.getMainHandler().removeCallbacks(this.mSlipRunnable);
            }
            VerticalSlipLayout verticalSlipLayout = this.mSlipLayout;
            if (verticalSlipLayout != null) {
                verticalSlipLayout.doRelease();
            }
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase, com.miui.gallery.ui.ChooserFragment.OnProjectClickedListener
        public void onProjectedClicked() {
            super.onProjectedClicked();
            Send.this.mMenuItemManager.onProjectedClicked();
        }

        public void onSendClick(BaseDataItem baseDataItem) {
            Send send = Send.this;
            send.mOwner.postRecordCountEvent(send.getItemClickEventCategory(baseDataItem), "send");
            Send send2 = Send.this;
            send2.isOriginWindowModeIsLandscape = send2.mOwner.isLandscapeWindowMode();
            PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController = this.mOrientationController;
            if (iPhotoPageOrientationManagerController != null) {
                iPhotoPageOrientationManagerController.noteRestoreOrientation();
            }
            setSlipLayoutDraggable(this.mConfig);
            if ((this.mIsLargeScreenDevice && this.mIsLargeWindow) || !Send.this.mOwner.isLandscapeWindowMode()) {
                Send.this.mChoiceManager.setSlipped(true);
            } else {
                Send.this.mOwner.hideBars(false);
                Send.this.mOwner.onSendClick();
                this.mDataProvider.getFieldData().isHideInAdvanceByLandAction = true;
                ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Send$ChoiceManager$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Send.ChoiceManager.$r8$lambda$6TqzP1vOBZqTTBxQIdT6dNQZMx8(Send.ChoiceManager.this);
                    }
                }, 10L);
            }
            SecurityShareHelper.startPrivacyProtectTipSettingsActivity(this.mContext);
        }

        public /* synthetic */ void lambda$onSendClick$3() {
            Send.this.mChoiceManager.setSlipped(true);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner
    public BaseActivity getBaseActivity() {
        return (BaseActivity) this.mContext;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner
    public GalleryFragment getFragment() {
        return this.mFragment;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner
    public PhotoPageAdapter getAdapter() {
        return this.mOwner.getAdapter();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner
    public IDataProvider getDataProvider() {
        return this.mDataProvider;
    }
}
