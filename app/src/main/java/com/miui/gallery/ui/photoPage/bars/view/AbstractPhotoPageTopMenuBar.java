package com.miui.gallery.ui.photoPage.bars.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.popupwidget.widget.GuidePopupWindow;

/* loaded from: classes2.dex */
public abstract class AbstractPhotoPageTopMenuBar implements IPhotoPageTopBar {
    public boolean isOnPcMode;
    public ImageView mActionBackView;
    public IPhotoPageActionBarManager mActionBarManager;
    public Activity mActivity;
    public View mLine;
    public TextView mLocation;
    public ImageView mLockBtn;
    public ImageView mMapEntrance;
    public ImageView mOperationView;
    public View mRootView;
    public View mRotateBtn;
    public TextView mSubTitle;
    public TextView mTitle;
    public final IViewProvider mViewProvider;
    public ImageView mWatchAll;

    /* loaded from: classes2.dex */
    public static class ListenerInfo {
        public View.OnClickListener mOnBackClickListener;
        public View.OnClickListener mOnLocationInfoClickListener;
        public View.OnClickListener mOnLockClickListener;
        public View.OnClickListener mOnRotateClickListener;
        public View.OnClickListener mOnWatchAllClickListener;
    }

    /* renamed from: $r8$lambda$RiW81FPgRAPG_FfubQ_9O3r-s2c */
    public static /* synthetic */ void m1638$r8$lambda$RiW81FPgRAPG_FfubQ_9O3rs2c(ListenerInfo listenerInfo, View view) {
        lambda$init$0(listenerInfo, view);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void dismissPopMenu() {
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void onOrientationChanged(int i, int i2) {
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setSpecialTypeEnterClickListener(View.OnClickListener onClickListener) {
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setSpecialTypeEnterVisible(boolean z) {
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void updateSpecialTypeEnter(int i, int i2) {
    }

    /* loaded from: classes2.dex */
    public static class BarFactory {
        public static IPhotoPageTopBar create(ActionBarCustomViewBuilder.CustomViewType customViewType, GalleryActivity galleryActivity, ListenerInfo listenerInfo, IViewProvider iViewProvider, IPhotoPageActionBarManager iPhotoPageActionBarManager) {
            if (customViewType == ActionBarCustomViewBuilder.CustomViewType.PadTopMenu) {
                return new PhotoPagePadTopMenuBar(galleryActivity, listenerInfo, iViewProvider, iPhotoPageActionBarManager, customViewType);
            }
            if (customViewType == ActionBarCustomViewBuilder.CustomViewType.TopMenu) {
                return new PhotoPageTopMenuBar(galleryActivity, listenerInfo, iViewProvider, iPhotoPageActionBarManager, customViewType);
            }
            return new PhotoPageTopBar(galleryActivity, listenerInfo, iViewProvider, iPhotoPageActionBarManager, customViewType);
        }
    }

    public AbstractPhotoPageTopMenuBar(Activity activity, ListenerInfo listenerInfo, IViewProvider iViewProvider, IPhotoPageActionBarManager iPhotoPageActionBarManager, ActionBarCustomViewBuilder.CustomViewType customViewType) {
        this.mActivity = activity;
        this.mViewProvider = iViewProvider;
        this.mActionBarManager = iPhotoPageActionBarManager;
        View topBarView = iViewProvider.getTopBarView(null, customViewType);
        this.mRootView = topBarView;
        if (topBarView == null) {
            DefaultLogger.e("PhotoPageFragment", "getTopBarView error ! mRootView is null , isLandMode = [%b]", Boolean.valueOf(MiscUtil.isLandMode(activity)));
            return;
        }
        init(listenerInfo);
        this.isOnPcMode = (activity.getResources().getConfiguration().uiMode & 8192) != 0;
    }

    public void init(final ListenerInfo listenerInfo) {
        ImageView imageView = (ImageView) this.mRootView.findViewById(R.id.watch_all);
        this.mWatchAll = imageView;
        if (imageView != null) {
            if (listenerInfo == null || listenerInfo.mOnWatchAllClickListener == null) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                this.mActionBarManager.setAccessibilityDelegateFor(this.mWatchAll);
                this.mWatchAll.setOnClickListener(listenerInfo.mOnWatchAllClickListener);
            }
        }
        if (listenerInfo != null && listenerInfo.mOnLockClickListener != null) {
            ImageView imageView2 = (ImageView) this.mRootView.findViewById(R.id.lock_btn);
            this.mLockBtn = imageView2;
            this.mActionBarManager.setAccessibilityDelegateFor(imageView2);
            this.mLockBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AbstractPhotoPageTopMenuBar.m1638$r8$lambda$RiW81FPgRAPG_FfubQ_9O3rs2c(AbstractPhotoPageTopMenuBar.ListenerInfo.this, view);
                }
            });
        }
        ImageView imageView3 = (ImageView) this.mRootView.findViewById(R.id.action_back);
        this.mActionBackView = imageView3;
        this.mActionBarManager.setAccessibilityDelegateFor(imageView3);
    }

    public static /* synthetic */ void lambda$init$0(ListenerInfo listenerInfo, View view) {
        listenerInfo.mOnLockClickListener.onClick(view);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public View getView() {
        return this.mRootView;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setTitle(String str) {
        this.mTitle.setText(str);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setLocation(Context context, BaseDataItem baseDataItem, boolean z) {
        String location = baseDataItem.getLocation();
        if (LocationUtil.isLocationValidate(location)) {
            this.mLocation.setText(location);
            this.mLocation.setVisibility(0);
            this.mLine.setVisibility(0);
            if (!z && !baseDataItem.isSecret() && MapInitializerImpl.checkMapAvailable() && LocationUtil.isValidateCoordinate(baseDataItem.getCoordidate()[0], baseDataItem.getCoordidate()[1])) {
                this.mLocation.setClickable(true);
                this.mMapEntrance.setVisibility(0);
                return;
            }
            this.mLocation.setClickable(false);
            this.mMapEntrance.setVisibility(8);
            return;
        }
        this.mLocation.setVisibility(8);
        this.mLine.setVisibility(8);
        this.mMapEntrance.setVisibility(8);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setRotateButtonVisible(boolean z) {
        View view = this.mRotateBtn;
        if (view != null) {
            view.setVisibility((!z || !VideoPlayerCompat.isVideoPlayerSupportRotateScreen() || this.isOnPcMode) ? 8 : 0);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setLockButtonVisible(boolean z) {
        ImageView imageView = this.mLockBtn;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setLockButtonLock(boolean z) {
        ImageView imageView = this.mLockBtn;
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (!(drawable instanceof AnimatedVectorDrawable)) {
                return;
            }
            ((AnimatedVectorDrawable) drawable).reset();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void playLockButtonAnimation(boolean z) {
        ImageView imageView = this.mLockBtn;
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (!(drawable instanceof AnimatedVectorDrawable)) {
                return;
            }
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            if (animatedVectorDrawable.isRunning()) {
                return;
            }
            animatedVectorDrawable.start();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void showLockButtonGuide() {
        ImageView imageView = this.mLockBtn;
        if (imageView == null || imageView.getVisibility() != 0 || !isValid()) {
            return;
        }
        GuidePopupWindow guidePopupWindow = new GuidePopupWindow(this.mLockBtn.getContext());
        guidePopupWindow.setArrowMode(8);
        guidePopupWindow.setGuideText(R.string.notice_click_lock_orientation);
        guidePopupWindow.show(this.mLockBtn, 0, 0, false);
    }

    public final boolean isValid() {
        Activity activity = this.mActivity;
        return activity != null && !activity.isFinishing() && !this.mActivity.isDestroyed();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void show() {
        this.mRootView.setVisibility(0);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void hide() {
        this.mRootView.setVisibility(8);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setOperationViewClickListener(final View.OnClickListener onClickListener) {
        if (this.mOperationView == null) {
            return;
        }
        if (!LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
            this.mOperationView.setHapticFeedbackEnabled(true);
        } else {
            this.mOperationView.setHapticFeedbackEnabled(false);
        }
        this.mOperationView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar.1
            {
                AbstractPhotoPageTopMenuBar.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
                    LinearMotorHelper.performHapticFeedback(AbstractPhotoPageTopMenuBar.this.mOperationView, LinearMotorHelper.HAPTIC_TAP_LIGHT);
                } else {
                    AbstractPhotoPageTopMenuBar.this.mOperationView.playSoundEffect(0);
                }
                View.OnClickListener onClickListener2 = onClickListener;
                if (onClickListener2 != null) {
                    onClickListener2.onClick(view);
                }
            }
        });
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setOperationViewVisibility(int i) {
        ImageView imageView = this.mOperationView;
        if (imageView != null) {
            imageView.setVisibility(i);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setCacheHolder(PhotoPageActionBarManager.BarSelector.CacheHolder cacheHolder) {
        View.OnClickListener onClickListener;
        if (cacheHolder == null || this.mOperationView == null || (onClickListener = cacheHolder.mOnClickListener) == null) {
            return;
        }
        setOperationViewClickListener(onClickListener);
    }

    public void filterResource() {
        ImageView imageView;
        if (this.mActivity == null || (imageView = this.mOperationView) == null || this.mWatchAll == null) {
            return;
        }
        boolean z = this instanceof PhotoPageTopBar;
        imageView.setImageResource(z ? R.drawable.motion_photo_play_icon : R.drawable.button_photo_top_bar_play_light);
        this.mWatchAll.setImageResource(z ? R.drawable.button_photo_top_bar_watch_all_port_light : R.drawable.button_photo_top_bar_watch_all_land_light);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setSubTitle(String str) {
        TextView textView = this.mSubTitle;
        if (textView == null || textView.getVisibility() != 0) {
            return;
        }
        this.mSubTitle.setText(str);
    }
}
