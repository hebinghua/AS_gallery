package com.xiaomi.push;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

/* loaded from: classes3.dex */
public class dl implements Application.ActivityLifecycleCallbacks {
    public Context a;

    /* renamed from: a  reason: collision with other field name */
    public String f215a;
    public String b;

    public dl(Context context, String str) {
        this.f215a = "";
        this.a = context;
        this.f215a = str;
    }

    public final void a(String str) {
        hr hrVar = new hr();
        hrVar.a(str);
        hrVar.a(System.currentTimeMillis());
        hrVar.a(hl.ActivityActiveTimeStamp);
        dt.a(this.a, hrVar);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        String localClassName = activity.getLocalClassName();
        if (TextUtils.isEmpty(this.f215a) || TextUtils.isEmpty(localClassName)) {
            return;
        }
        this.b = "";
        if (!TextUtils.isEmpty("") && !TextUtils.equals(this.b, localClassName)) {
            this.f215a = "";
            return;
        }
        a(this.a.getPackageName() + "|" + localClassName + ":" + this.f215a + "," + String.valueOf(System.currentTimeMillis() / 1000));
        this.f215a = "";
        this.b = "";
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        if (TextUtils.isEmpty(this.b)) {
            this.b = activity.getLocalClassName();
        }
        this.f215a = String.valueOf(System.currentTimeMillis() / 1000);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
    }
}
