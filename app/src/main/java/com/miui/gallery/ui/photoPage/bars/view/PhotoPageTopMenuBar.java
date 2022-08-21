package com.miui.gallery.ui.photoPage.bars.view;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.R;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseScreenUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MiscUtil;
import com.nexstreaming.nexeditorsdk.nexClip;

/* loaded from: classes2.dex */
public class PhotoPageTopMenuBar extends AbstractPhotoPageTopMenuBar {
    public LinearLayout mExpandMenuLayout;
    public ConstraintLayout mLocationContainer;
    public LinearLayout mMenuLayout;
    public int mOriginLeftPadding;
    public int mOriginRightPadding;
    public int mOriginTopPadding;

    public PhotoPageTopMenuBar(Activity activity, AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, IViewProvider iViewProvider, IPhotoPageActionBarManager iPhotoPageActionBarManager, ActionBarCustomViewBuilder.CustomViewType customViewType) {
        super(activity, listenerInfo, iViewProvider, iPhotoPageActionBarManager, customViewType);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar
    public void init(final AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo) {
        View.OnClickListener onClickListener;
        super.init(listenerInfo);
        if (listenerInfo != null && (onClickListener = listenerInfo.mOnBackClickListener) != null) {
            this.mActionBackView.setOnClickListener(onClickListener);
        }
        if (!this.mActionBarManager.isVideoPlayerSupportActionBarAdjust() || BaseBuildUtil.isLargeScreenDevice()) {
            this.mExpandMenuLayout = (LinearLayout) this.mRootView.findViewById(R.id.expand_menu_layout);
        } else {
            this.mLocationContainer = (ConstraintLayout) this.mRootView.findViewById(R.id.photo_page_top_menu_bar_location_container);
        }
        this.mMenuLayout = (LinearLayout) this.mRootView.findViewById(R.id.menu_layout);
        this.mTitle = (TextView) this.mRootView.findViewById(R.id.top_bar_title);
        this.mLine = this.mRootView.findViewById(R.id.line);
        this.mLocation = (TextView) this.mRootView.findViewById(R.id.top_bar_location);
        this.mMapEntrance = (ImageView) this.mRootView.findViewById(R.id.top_bar_map_entrance);
        this.mOperationView = (ImageView) this.mRootView.findViewById(R.id.operation_btn);
        View topMenuView = this.mViewProvider.getTopMenuView(this.mMenuLayout);
        if (topMenuView != null) {
            this.mMenuLayout.addView(topMenuView);
        }
        this.mActionBarManager.setAccessibilityDelegateFor(topMenuView);
        this.mActionBarManager.setAccessibilityDelegateFor(this.mTitle);
        this.mActionBarManager.setAccessibilityDelegateFor(this.mLocation);
        this.mActionBarManager.setAccessibilityDelegateFor(this.mOperationView);
        filterResource();
        if (isUsedCutoutModeShortEdges()) {
            this.mOriginLeftPadding = this.mRootView.getPaddingLeft();
            this.mOriginRightPadding = this.mRootView.getPaddingRight();
            this.mOriginTopPadding = this.mRootView.getPaddingTop();
            setToDisplayOrientation(this.mRootView);
        } else {
            int notchHeight = MiscUtil.getNotchHeight(this.mActivity);
            if (notchHeight > 0) {
                int paddingRight = this.mRootView.getPaddingRight();
                View view = this.mRootView;
                view.setPadding(view.getPaddingLeft(), this.mRootView.getPaddingTop() + notchHeight, paddingRight, this.mRootView.getPaddingBottom());
            }
        }
        if (listenerInfo != null && listenerInfo.mOnRotateClickListener != null) {
            View findViewById = this.mRootView.findViewById(R.id.rotate_btn);
            this.mRotateBtn = findViewById;
            this.mActionBarManager.setAccessibilityDelegateFor(findViewById);
            if (this.mRotateBtn == null) {
                return;
            }
            if (!LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
                this.mRotateBtn.setHapticFeedbackEnabled(true);
            }
            this.mRotateBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopMenuBar$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    PhotoPageTopMenuBar.this.lambda$init$0(listenerInfo, view2);
                }
            });
        }
        if (listenerInfo == null || listenerInfo.mOnLocationInfoClickListener == null) {
            return;
        }
        this.mLocation.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopMenuBar$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PhotoPageTopMenuBar.lambda$init$1(AbstractPhotoPageTopMenuBar.ListenerInfo.this, view2);
            }
        });
        this.mMapEntrance.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopMenuBar$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PhotoPageTopMenuBar.lambda$init$2(AbstractPhotoPageTopMenuBar.ListenerInfo.this, view2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, View view) {
        if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
            LinearMotorHelper.performHapticFeedback(this.mRotateBtn, LinearMotorHelper.HAPTIC_TAP_LIGHT);
        } else {
            this.mRotateBtn.playSoundEffect(0);
        }
        listenerInfo.mOnRotateClickListener.onClick(view);
    }

    public static /* synthetic */ void lambda$init$1(AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, View view) {
        listenerInfo.mOnLocationInfoClickListener.onClick(view);
    }

    public static /* synthetic */ void lambda$init$2(AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, View view) {
        listenerInfo.mOnLocationInfoClickListener.onClick(view);
    }

    public final boolean isUsedCutoutModeShortEdges() {
        return EnterTypeUtils.isFromCamera(this.mActivity.getIntent().getExtras()) || MiscUtil.isUsedCutoutModeShortEdges(this.mActivity);
    }

    public final void setToDisplayOrientation(View view) {
        int rotation = ((WindowManager) this.mActivity.getSystemService("window")).getDefaultDisplay().getRotation();
        resetPadding(view, rotation == 1 ? 90 : rotation == 3 ? nexClip.kClip_Rotate_270 : 0);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void onOrientationChanged(int i, int i2) {
        super.onOrientationChanged(i, i2);
        if (isUsedCutoutModeShortEdges()) {
            View view = this.mRootView;
            if (i2 == 90) {
                i2 = 270;
            } else if (i2 == 270) {
                i2 = 90;
            }
            resetPadding(view, i2);
        }
    }

    public final void resetPadding(View view, int i) {
        boolean z;
        int i2;
        if (i == 90 || i == 270) {
            int paddingTop = view.getPaddingTop();
            int notchHeight = MiscUtil.getNotchHeight(this.mActivity);
            int i3 = this.mOriginTopPadding;
            boolean z2 = true;
            int i4 = 0;
            if (paddingTop != notchHeight + i3) {
                paddingTop = notchHeight + i3;
                z = true;
            } else {
                z = false;
            }
            int paddingLeft = view.getPaddingLeft();
            int paddingRight = view.getPaddingRight();
            int dimension = (int) this.mActivity.getResources().getDimension(R.dimen.photo_page_top_menu_left_padding);
            if (i == 90) {
                i2 = dimension;
                dimension = 0;
            } else {
                i2 = 0;
            }
            if (BaseScreenUtil.isFullScreenGestureNav(this.mActivity)) {
                i2 = 0;
            } else {
                i4 = dimension;
            }
            int i5 = this.mOriginLeftPadding;
            if (paddingLeft != i4 + i5) {
                paddingLeft = i4 + i5;
                z = true;
            }
            int i6 = this.mOriginRightPadding;
            if (paddingRight != i2 + i6) {
                paddingRight = i2 + i6;
            } else {
                z2 = z;
            }
            if (!z2) {
                return;
            }
            view.setPadding(paddingLeft, paddingTop, paddingRight, view.getPaddingBottom());
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void onActivityConfigurationChanged(Configuration configuration) {
        if (isUsedCutoutModeShortEdges()) {
            setToDisplayOrientation(this.mRootView);
        }
        ImageView imageView = this.mActionBackView;
        if (imageView != null) {
            imageView.requestFocus();
        }
        if (configuration.orientation != 2 || !this.mActionBarManager.isVideoPlayerSupportActionBarAdjust() || BaseBuildUtil.isLargeScreenDevice()) {
            return;
        }
        if (this.mActivity.isInMultiWindowMode()) {
            this.mLine.setAlpha(0.0f);
            this.mLocationContainer.setVisibility(8);
            this.mLocation.setVisibility(8);
            return;
        }
        this.mLine.setAlpha(1.0f);
        this.mLocationContainer.setVisibility(0);
        this.mLocation.setVisibility(0);
        this.mLocationContainer.setMaxWidth(this.mActivity.getResources().getDimensionPixelSize(R.dimen.photo_page_top_menu_bar_location_container_max_width));
        this.mLocation.setMaxWidth(this.mActivity.getResources().getDimensionPixelSize(R.dimen.photo_page_top_menu_bar_location_max_width));
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setLockButtonLock(boolean z) {
        ImageView imageView = this.mLockBtn;
        if (imageView == null) {
            return;
        }
        imageView.setImageResource(!z ? R.drawable.lock_orientation_unlock_to_locked_land : R.drawable.lock_orientation_locked_to_unlock_land);
        super.setLockButtonLock(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void playLockButtonAnimation(boolean z) {
        ImageView imageView = this.mLockBtn;
        if (imageView == null) {
            return;
        }
        imageView.setImageResource(z ? R.drawable.lock_orientation_unlock_to_locked_land : R.drawable.lock_orientation_locked_to_unlock_land);
        super.playLockButtonAnimation(z);
    }
}
