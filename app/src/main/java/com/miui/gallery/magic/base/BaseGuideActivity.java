package com.miui.gallery.magic.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import java.util.Objects;

/* loaded from: classes2.dex */
public abstract class BaseGuideActivity extends BaseActivity {
    public BaseGuideBroadcastReceiver receiver;

    @Override // com.miui.gallery.magic.base.BaseActivity, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initBroadcastReceiver();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.receiver);
    }

    public final void initBroadcastReceiver() {
        this.receiver = new BaseGuideBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("receiver_action_save_finish");
        registerReceiver(this.receiver, intentFilter);
    }

    /* loaded from: classes2.dex */
    public class BaseGuideBroadcastReceiver extends BroadcastReceiver {
        public BaseGuideBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Objects.requireNonNull(action);
            if ("receiver_action_save_finish".equals(action)) {
                BaseGuideActivity.this.finish();
            }
        }
    }
}
