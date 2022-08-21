package com.miui.pickdrag.base.gesture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: KeyDispatchHelper.kt */
/* loaded from: classes3.dex */
public final class KeyDispatchHelper extends BroadcastReceiver {
    public static final Companion Companion = new Companion(null);
    public final KeyDispatchCallback keyDispatchListener;
    public Map<String, Long> keyDispatchTimeMap = new LinkedHashMap();

    public KeyDispatchHelper(KeyDispatchCallback keyDispatchCallback) {
        this.keyDispatchListener = keyDispatchCallback;
    }

    /* compiled from: KeyDispatchHelper.kt */
    /* loaded from: classes3.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void register(Context applicationContext) {
        Intrinsics.checkNotNullParameter(applicationContext, "applicationContext");
        applicationContext.registerReceiver(this, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public final void unregister(Context applicationContext) {
        Intrinsics.checkNotNullParameter(applicationContext, "applicationContext");
        applicationContext.unregisterReceiver(this);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        KeyDispatchCallback keyDispatchCallback;
        String str = null;
        if (!TextUtils.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS", intent == null ? null : intent.getAction())) {
            return;
        }
        if (intent != null) {
            str = intent.getStringExtra("reason");
        }
        Long l = this.keyDispatchTimeMap.get(str);
        if (System.currentTimeMillis() - (l == null ? 0L : l.longValue()) < 150) {
            return;
        }
        if (str != null) {
            this.keyDispatchTimeMap.put(str, Long.valueOf(System.currentTimeMillis()));
        }
        if (Intrinsics.areEqual(str, "homekey")) {
            KeyDispatchCallback keyDispatchCallback2 = this.keyDispatchListener;
            if (keyDispatchCallback2 == null) {
                return;
            }
            keyDispatchCallback2.onKeyDispatched(3);
        } else if (!Intrinsics.areEqual(str, "recentapps") || (keyDispatchCallback = this.keyDispatchListener) == null) {
        } else {
            keyDispatchCallback.onKeyDispatched(82);
        }
    }
}
