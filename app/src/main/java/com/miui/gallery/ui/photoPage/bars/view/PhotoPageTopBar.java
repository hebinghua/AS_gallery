package com.miui.gallery.ui.photoPage.bars.view;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;
import com.miui.gallery.util.LinearMotorHelper;

/* loaded from: classes2.dex */
public class PhotoPageTopBar extends AbstractPhotoPageTopMenuBar {
    public View mContainerVertical;
    public View mSpecialTypeEnter;
    public TextView mSubTitle;
    public View mTitleLayout;

    public PhotoPageTopBar(Activity activity, AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, IViewProvider iViewProvider, IPhotoPageActionBarManager iPhotoPageActionBarManager, ActionBarCustomViewBuilder.CustomViewType customViewType) {
        super(activity, listenerInfo, iViewProvider, iPhotoPageActionBarManager, customViewType);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar
    public void init(final AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo) {
        View.OnClickListener onClickListener;
        super.init(listenerInfo);
        if (listenerInfo != null && (onClickListener = listenerInfo.mOnBackClickListener) != null) {
            this.mActionBackView.setOnClickListener(onClickListener);
        }
        this.mTitleLayout = this.mRootView.findViewById(R.id.photo_page_top_bar_title_layout);
        this.mTitle = (TextView) this.mRootView.findViewById(R.id.photo_page_top_bar_title);
        this.mSubTitle = (TextView) this.mRootView.findViewById(R.id.photo_page_top_bar_subtitle);
        this.mLine = this.mRootView.findViewById(R.id.photo_page_top_bar_divider_line);
        this.mLocation = (TextView) this.mRootView.findViewById(R.id.photo_page_top_bar_location);
        this.mMapEntrance = (ImageView) this.mRootView.findViewById(R.id.photo_page_top_bar_map_entrance);
        this.mOperationView = (ImageView) this.mRootView.findViewById(R.id.operation_btn);
        filterResource();
        this.mSpecialTypeEnter = this.mRootView.findViewById(R.id.special_type_enter);
        this.mActionBarManager.setAccessibilityDelegateFor(this.mTitleLayout);
        this.mActionBarManager.setAccessibilityDelegateFor(this.mOperationView);
        this.mActionBarManager.setAccessibilityDelegateFor(this.mSpecialTypeEnter);
        if (listenerInfo != null && listenerInfo.mOnLocationInfoClickListener != null) {
            this.mLocation.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopBar$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageTopBar.lambda$init$0(AbstractPhotoPageTopMenuBar.ListenerInfo.this, view);
                }
            });
            this.mMapEntrance.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopBar$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageTopBar.lambda$init$1(AbstractPhotoPageTopMenuBar.ListenerInfo.this, view);
                }
            });
        }
        if (listenerInfo != null && listenerInfo.mOnRotateClickListener != null) {
            View findViewById = this.mRootView.findViewById(R.id.rotate_btn);
            this.mRotateBtn = findViewById;
            this.mActionBarManager.setAccessibilityDelegateFor(findViewById);
            if (!LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
                this.mRotateBtn.setHapticFeedbackEnabled(true);
            }
            this.mRotateBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopBar$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageTopBar.this.lambda$init$2(listenerInfo, view);
                }
            });
        }
        if (!this.mActionBarManager.isVideoPlayerSupportActionBarAdjust()) {
            this.mContainerVertical = this.mRootView.findViewById(R.id.top_bar_container);
        }
    }

    public static /* synthetic */ void lambda$init$0(AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, View view) {
        listenerInfo.mOnLocationInfoClickListener.onClick(view);
    }

    public static /* synthetic */ void lambda$init$1(AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, View view) {
        listenerInfo.mOnLocationInfoClickListener.onClick(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$2(AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, View view) {
        if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
            LinearMotorHelper.performHapticFeedback(this.mRotateBtn, LinearMotorHelper.HAPTIC_TAP_LIGHT);
        } else {
            this.mRotateBtn.playSoundEffect(0);
        }
        listenerInfo.mOnRotateClickListener.onClick(view);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setLockButtonLock(boolean z) {
        ImageView imageView = this.mLockBtn;
        if (imageView == null) {
            return;
        }
        imageView.setImageResource(!z ? R.drawable.lock_orientation_unlock_to_locked_port : R.drawable.lock_orientation_locked_to_unlock_port);
        super.setLockButtonLock(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void playLockButtonAnimation(boolean z) {
        ImageView imageView = this.mLockBtn;
        if (imageView == null) {
            return;
        }
        imageView.setImageResource(z ? R.drawable.lock_orientation_unlock_to_locked_port : R.drawable.lock_orientation_locked_to_unlock_port);
        super.playLockButtonAnimation(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setSubTitle(String str) {
        TextView textView = this.mSubTitle;
        if (textView != null) {
            textView.setText(str);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void updateSpecialTypeEnter(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            return;
        }
        View view = this.mSpecialTypeEnter;
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(i);
        } else if (!(view instanceof TextView)) {
        } else {
            TextView textView = (TextView) view;
            textView.setText(i2);
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(view.getResources().getDrawable(i), (Drawable) null, (Drawable) null, (Drawable) null);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setSpecialTypeEnterClickListener(View.OnClickListener onClickListener) {
        View view = this.mSpecialTypeEnter;
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void setSpecialTypeEnterVisible(boolean z) {
        View view = this.mSpecialTypeEnter;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void onActivityConfigurationChanged(Configuration configuration) {
        ImageView imageView = this.mActionBackView;
        if (imageView != null) {
            imageView.requestFocus();
        }
        if (this.mContainerVertical != null) {
            this.mContainerVertical.setMinimumHeight(this.mActivity.getResources().getDimensionPixelSize(R.dimen.photo_page_top_bar_min_height));
        }
    }
}
