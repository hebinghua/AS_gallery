package com.miui.gallery.ui.actionBar;

import android.view.LayoutInflater;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.util.MiscUtil;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public abstract class BaseCommonActionBarHelper {
    public int ACTION_BAR_LAYOUT_RES;
    public int HOME_ICON_DARk_RES;
    public int HOME_ICON_ID;
    public int HOME_ICON_LIGHT_RES;
    public int HOME_MENU_DARK_RES;
    public int HOME_MENU_ID;
    public int HOME_MENU_LIGHT_RES;
    public int HOME_TEXT_COLOR_DARK_RES;
    public int HOME_TEXT_COLOR_LIGHT_RES;
    public int HOME_TEXT_ID;
    public int TRANSLUCENT_ACTION_BAR_BG_RES;
    public boolean isAnimActionBarBg;
    public ActionBar mActionBar;
    public AppCompatActivity mContext;
    public LayoutInflater mInflater;
    public boolean mIsNightMode;
    public View.OnClickListener mOnBackClickListener;
    public View.OnClickListener mOnMenuClickListener;

    public static /* synthetic */ void $r8$lambda$rQei5u0f28TbnScBmmTxHYCRqo0(BaseCommonActionBarHelper baseCommonActionBarHelper, View view) {
        baseCommonActionBarHelper.lambda$new$0(view);
    }

    public BaseCommonActionBarHelper(AppCompatActivity appCompatActivity, ThemeConfig themeConfig) {
        if (appCompatActivity == null) {
            throw new IllegalStateException("Activity is null !");
        }
        themeConfig = themeConfig == null ? new DefaultThemeConfig() : themeConfig;
        if (themeConfig.getOnBackClickListener() == null) {
            themeConfig.setOnBackClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.actionBar.BaseCommonActionBarHelper$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    BaseCommonActionBarHelper.$r8$lambda$rQei5u0f28TbnScBmmTxHYCRqo0(BaseCommonActionBarHelper.this, view);
                }
            });
        }
        this.mContext = appCompatActivity;
        this.mActionBar = appCompatActivity.getAppCompatActionBar();
        this.mInflater = appCompatActivity.getLayoutInflater();
        this.mIsNightMode = MiscUtil.isNightMode(appCompatActivity);
        parseConfig(themeConfig);
    }

    public /* synthetic */ void lambda$new$0(View view) {
        this.mContext.finish();
    }

    public final void parseConfig(ThemeConfig themeConfig) {
        this.ACTION_BAR_LAYOUT_RES = themeConfig.getActionBarLayoutRes();
        this.TRANSLUCENT_ACTION_BAR_BG_RES = themeConfig.getTranslucentActionBarBgRes();
        this.HOME_ICON_ID = themeConfig.getHomeIconId();
        this.HOME_TEXT_ID = themeConfig.getHomeTextId();
        this.HOME_MENU_ID = themeConfig.getHomeMenuId();
        this.HOME_TEXT_COLOR_DARK_RES = themeConfig.getHomeTextColorDarkRes();
        this.HOME_TEXT_COLOR_LIGHT_RES = themeConfig.getHomeTextColorLightRes();
        this.HOME_ICON_DARk_RES = themeConfig.getHomeIconDarkRes();
        this.HOME_ICON_LIGHT_RES = themeConfig.getHomeIconLightRes();
        this.HOME_MENU_DARK_RES = themeConfig.getHomeMenuDarkRes();
        this.HOME_MENU_LIGHT_RES = themeConfig.getHomeMenuLightRes();
        this.mOnBackClickListener = themeConfig.getOnBackClickListener();
        this.mOnMenuClickListener = themeConfig.getOnMenuClickListener();
        this.isAnimActionBarBg = themeConfig.isAnimActionBarBg();
    }

    public int getActionBarHeight() {
        return this.mActionBar.getCustomView().getHeight();
    }

    /* loaded from: classes2.dex */
    public static class ThemeConfig {
        public boolean isAnimActionBarBg;
        public int mActionBarLayoutRes;
        public int mHomeIconDarkRes;
        public int mHomeIconId;
        public int mHomeIconLightRes;
        public int mHomeMenuDarkRes;
        public int mHomeMenuId;
        public int mHomeMenuLightRes;
        public int mHomeTextColorDarkRes;
        public int mHomeTextColorLightRes;
        public int mHomeTextId;
        public View.OnClickListener mOnBackClickListener;
        public View.OnClickListener mOnMenuClickListener;
        public int mTranslucentActionBarBgRes;

        public int getActionBarLayoutRes() {
            return this.mActionBarLayoutRes;
        }

        public void setActionBarLayoutRes(int i) {
            this.mActionBarLayoutRes = i;
        }

        public int getTranslucentActionBarBgRes() {
            return this.mTranslucentActionBarBgRes;
        }

        public int getHomeIconId() {
            return this.mHomeIconId;
        }

        public int getHomeTextId() {
            return this.mHomeTextId;
        }

        public int getHomeMenuId() {
            return this.mHomeMenuId;
        }

        public int getHomeTextColorDarkRes() {
            return this.mHomeTextColorDarkRes;
        }

        public int getHomeTextColorLightRes() {
            return this.mHomeTextColorLightRes;
        }

        public int getHomeIconDarkRes() {
            return this.mHomeIconDarkRes;
        }

        public int getHomeIconLightRes() {
            return this.mHomeIconLightRes;
        }

        public int getHomeMenuDarkRes() {
            return this.mHomeMenuDarkRes;
        }

        public int getHomeMenuLightRes() {
            return this.mHomeMenuLightRes;
        }

        public View.OnClickListener getOnBackClickListener() {
            return this.mOnBackClickListener;
        }

        public ThemeConfig setOnBackClickListener(View.OnClickListener onClickListener) {
            this.mOnBackClickListener = onClickListener;
            return this;
        }

        public View.OnClickListener getOnMenuClickListener() {
            return this.mOnMenuClickListener;
        }

        public ThemeConfig setOnMenuClickListener(View.OnClickListener onClickListener) {
            this.mOnMenuClickListener = onClickListener;
            return this;
        }

        public void setAnimActionBarBg(boolean z) {
            this.isAnimActionBarBg = z;
        }

        public boolean isAnimActionBarBg() {
            return this.isAnimActionBarBg;
        }
    }

    /* loaded from: classes2.dex */
    public static class DefaultThemeConfig extends ThemeConfig {
        public DefaultThemeConfig() {
            this.mActionBarLayoutRes = R.layout.story_album_actionbar_content;
            this.mTranslucentActionBarBgRes = R.color.action_bar_background;
            this.mHomeIconId = R.id.home_arrow;
            this.mHomeTextId = R.id.home_text;
            this.mHomeMenuId = R.id.home_menu;
            this.mHomeTextColorDarkRes = R.color.white_90_transparent;
            this.mHomeTextColorLightRes = R.color.black_90_transparent;
            this.mHomeIconDarkRes = R.drawable.miuix_appcompat_action_bar_back_dark;
            this.mHomeIconLightRes = R.drawable.miuix_appcompat_action_bar_back_light;
            this.mHomeMenuDarkRes = R.drawable.miuix_appcompat_action_mode_immersion_more_dark;
            this.mHomeMenuLightRes = R.drawable.miuix_appcompat_action_mode_immersion_more_light;
            this.isAnimActionBarBg = true;
        }
    }
}
