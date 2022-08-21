package com.miui.gallery.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.util.SplitUtils;
import miui.os.Build;

/* loaded from: classes.dex */
public class FloatingWindowActivity extends GalleryActivity {
    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
        requestDisableStrategy(StrategyContext.DisableStrategyType.LANDSCAPE_DIRECTION);
        updateConfiguration(getResources().getConfiguration());
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
    }

    public boolean useDialog() {
        return (getResources().getInteger(R.integer.preference_dialog) == 1) && getIntent().getBooleanExtra("use_dialog", false) && !needForceSplit();
    }

    public final void updateConfiguration(Configuration configuration) {
        if (useDialog()) {
            setFloatingWindowMode(true);
        } else {
            setFloatingWindowMode(false);
        }
        if (needForceSplit()) {
            if (configuration.orientation == 2) {
                setExtraHorizontalPaddingLevel(2);
            } else {
                setExtraHorizontalPaddingLevel(1);
            }
        }
    }

    public boolean needHideBackAndFixedSmallTitle() {
        return (!isSplitModeEnabled() || getIntent() == null || (SplitUtils.getMiuiFlags(getIntent()) & 4) == 0) ? false : true;
    }

    public boolean needForceSplit() {
        return (!isSplitModeEnabled() || getIntent() == null || (SplitUtils.getMiuiFlags(getIntent()) & 16) == 0) ? false : true;
    }

    public final boolean isSplitModeEnabled() {
        return Build.IS_TABLET || ((getResources().getConfiguration().screenLayout & 15) == 3);
    }
}
