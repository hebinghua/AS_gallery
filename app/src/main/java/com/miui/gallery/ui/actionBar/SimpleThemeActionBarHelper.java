package com.miui.gallery.ui.actionBar;

import android.app.ActionBar;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.ui.actionBar.BaseCommonActionBarHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import miui.gallery.support.actionbar.ActionBarCompat;
import miuix.animation.Folme;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class SimpleThemeActionBarHelper extends BaseCommonActionBarHelper {
    public boolean isTopBarShowing;
    public ImageView mHomeIcon;
    public Button mHomeMenu;
    public TextView mHomeText;
    public StateColor mIconAndMenuState;
    public boolean mIsShowTranslucentStatusBar;
    public ColorDrawable mTranslucentActionBarBg;

    /* loaded from: classes2.dex */
    public enum StateColor {
        Light,
        Dark
    }

    public static /* synthetic */ void $r8$lambda$YPtLCdkF7nvEN2lvzQh0AfmQ9S4(View view, int i, View view2) {
        lambda$setTouchDelegate$0(view, i, view2);
    }

    public SimpleThemeActionBarHelper(AppCompatActivity appCompatActivity, BaseCommonActionBarHelper.ThemeConfig themeConfig) {
        super(appCompatActivity, themeConfig);
        this.mIsShowTranslucentStatusBar = true;
        this.mIconAndMenuState = StateColor.Light;
    }

    public void inflateActionBar() {
        View.OnClickListener onClickListener;
        this.mTranslucentActionBarBg = new ColorDrawable(this.mContext.getColor(this.TRANSLUCENT_ACTION_BAR_BG_RES));
        View inflate = this.mInflater.inflate(this.ACTION_BAR_LAYOUT_RES, (ViewGroup) null);
        inflate.setLayoutParams(new ActionBar.LayoutParams(-1, -1));
        this.mHomeIcon = (ImageView) inflate.findViewById(this.HOME_ICON_ID);
        this.mHomeText = (TextView) inflate.findViewById(this.HOME_TEXT_ID);
        this.mHomeMenu = (Button) inflate.findViewById(this.HOME_MENU_ID);
        this.mHomeIcon.setOnClickListener(this.mOnBackClickListener);
        setTouchDelegate(this.mHomeIcon, 50);
        Button button = this.mHomeMenu;
        if (button != null && (onClickListener = this.mOnMenuClickListener) != null) {
            button.setOnClickListener(onClickListener);
        }
        this.mActionBar.setDisplayOptions(16, 16);
        this.mActionBar.setCustomView(inflate);
        ActionBarCompat.setExpandState(this.mContext, 0);
        ActionBarCompat.setResizable(this.mContext, false);
    }

    public void setTouchDelegate(final View view, final int i) {
        final View view2 = (View) view.getParent();
        if (view2 == null) {
            return;
        }
        view2.post(new Runnable() { // from class: com.miui.gallery.ui.actionBar.SimpleThemeActionBarHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SimpleThemeActionBarHelper.$r8$lambda$YPtLCdkF7nvEN2lvzQh0AfmQ9S4(view, i, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$setTouchDelegate$0(View view, int i, View view2) {
        Rect rect = new Rect();
        view.getHitRect(rect);
        rect.top -= i;
        rect.bottom += i;
        rect.left -= i;
        rect.right += i;
        view2.setTouchDelegate(new TouchDelegate(rect, view));
    }

    public void setNullStyleActionBar() {
        this.mIsShowTranslucentStatusBar = true;
        this.isTopBarShowing = false;
        refreshHomeIconAndColor();
        this.mTranslucentActionBarBg.setAlpha(0);
        refreshActionBarBg();
    }

    public void refreshIconAndMenu(float f) {
        if (this.mIsNightMode) {
            return;
        }
        boolean floatEquals = BaseMiscUtil.floatEquals(f, 1.0f);
        if ((!floatEquals || this.mIconAndMenuState != StateColor.Light) && (floatEquals || this.mIconAndMenuState != StateColor.Dark)) {
            return;
        }
        setHomeColor(!this.mIsNightMode && floatEquals);
        startRefreshAnim();
    }

    public void refreshTextAndBg(float f) {
        boolean z = !BaseMiscUtil.floatEquals(f, 1.0f);
        this.mIsShowTranslucentStatusBar = z;
        setHomeText(z, this.mIsNightMode);
        this.mTranslucentActionBarBg.setAlpha(this.mIsShowTranslucentStatusBar ? 0 : 255);
        refreshActionBarBg();
    }

    public final void startRefreshAnim() {
        ImageView imageView = this.mHomeIcon;
        if (imageView != null) {
            Folme.useAt(imageView).visible().setHide();
            FolmeUtil.setCustomVisibleAnim(this.mHomeIcon, true, null, null);
        }
        Button button = this.mHomeMenu;
        if (button != null) {
            Folme.useAt(button).visible().setHide();
            FolmeUtil.setCustomVisibleAnim(this.mHomeMenu, true, null, null);
        }
        if (this.isAnimActionBarBg) {
            View customView = this.mActionBar.getCustomView();
            Folme.useAt(customView).visible().setHide();
            FolmeUtil.setCustomVisibleAnim(customView, true, null, null);
        }
    }

    public void setMultiWindowModeStatusBarTextColor() {
        if (this.mContext.isInMultiWindowMode()) {
            this.mContext.getWindow().getDecorView().setSystemUiVisibility(8192);
        }
    }

    public void refreshTopBar(float f) {
        this.mIsShowTranslucentStatusBar = !BaseMiscUtil.floatEquals(f, 1.0f);
        setMultiWindowModeStatusBarTextColor();
        if (this.mIsShowTranslucentStatusBar) {
            if (!this.isTopBarShowing) {
                return;
            }
            refreshHomeIconAndColor();
            this.mTranslucentActionBarBg.setAlpha(0);
            refreshActionBarBg();
            this.isTopBarShowing = false;
        } else if (this.isTopBarShowing) {
        } else {
            refreshHomeIconAndColor();
            this.mTranslucentActionBarBg.setAlpha(255);
            refreshActionBarBg();
            startRefreshAnim();
            this.isTopBarShowing = true;
        }
    }

    public final void refreshActionBarBg() {
        miuix.appcompat.app.ActionBar actionBar = this.mActionBar;
        if (actionBar == null) {
            return;
        }
        actionBar.setBackgroundDrawable((!this.mIsShowTranslucentStatusBar || !this.mIsNightMode) ? this.mTranslucentActionBarBg : null);
    }

    public final void refreshHomeIconAndColor() {
        setHomeText(this.mIsShowTranslucentStatusBar, this.mIsNightMode);
        setHomeColor(!this.mIsNightMode && !this.mIsShowTranslucentStatusBar);
    }

    public final void setHomeText(boolean z, boolean z2) {
        if (!z) {
            this.mHomeText.setVisibility(0);
            this.mHomeText.setTextColor(this.mContext.getColor(z2 ? this.HOME_TEXT_COLOR_DARK_RES : this.HOME_TEXT_COLOR_LIGHT_RES));
            return;
        }
        this.mHomeText.setVisibility(4);
    }

    public final void setHomeColor(boolean z) {
        this.mHomeIcon.setImageResource(z ? this.HOME_ICON_LIGHT_RES : this.HOME_ICON_DARk_RES);
        this.mContext.setTranslucentStatus(z ? 1 : 2);
        Button button = this.mHomeMenu;
        if (button == null) {
            return;
        }
        button.setBackgroundResource(z ? this.HOME_MENU_LIGHT_RES : this.HOME_MENU_DARK_RES);
        this.mIconAndMenuState = z ? StateColor.Dark : StateColor.Light;
    }

    public boolean isShowTranslucentStatusBar() {
        return this.mIsShowTranslucentStatusBar;
    }

    public void setActionBarTitle(String str) {
        this.mHomeText.setText(str);
    }

    public void requestFocus() {
        ImageView imageView = this.mHomeIcon;
        if (imageView == null || !imageView.isShown()) {
            return;
        }
        this.mHomeIcon.setFocusable(true);
        this.mHomeIcon.setFocusableInTouchMode(true);
        this.mHomeIcon.requestFocus(BaiduSceneResult.VISA);
    }

    public int getTitleHeight() {
        return this.mHomeText.getHeight();
    }
}
