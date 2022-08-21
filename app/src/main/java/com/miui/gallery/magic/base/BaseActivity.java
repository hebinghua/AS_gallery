package com.miui.gallery.magic.base;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.app.activity.AndroidActivity;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.SystemUiUtil;

/* loaded from: classes2.dex */
public abstract class BaseActivity extends AndroidActivity {
    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = 1;
            getWindow().setAttributes(attributes);
        }
        requestDisableStrategy(StrategyContext.DisableStrategyType.DIRECTION);
    }

    public void setOrientation() {
        if (OrientationCheckHelper.isSupportOrientationChange()) {
            SystemUiUtil.setRequestedOrientation(2, this);
        } else {
            SystemUiUtil.setRequestedOrientation(1, this);
        }
    }
}
