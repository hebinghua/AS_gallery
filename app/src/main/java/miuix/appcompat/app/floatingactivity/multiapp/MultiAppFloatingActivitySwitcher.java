package miuix.appcompat.app.floatingactivity.multiapp;

import android.content.Intent;
import android.os.Bundle;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes3.dex */
public final class MultiAppFloatingActivitySwitcher {
    public static MultiAppFloatingActivitySwitcher sInstance;

    public static MultiAppFloatingActivitySwitcher getInstance() {
        return sInstance;
    }

    public static void onSaveInstanceState(AppCompatActivity appCompatActivity, Bundle bundle) {
        getInstance();
    }

    public static void configureFloatingService(Intent intent, Intent intent2) {
        intent.putExtra("floating_service_pkg", intent2.getStringExtra("floating_service_pkg"));
        intent.putExtra("floating_service_path", intent2.getStringExtra("floating_service_path"));
    }
}
