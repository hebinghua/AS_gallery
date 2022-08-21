package com.miui.gallery.glide.manager;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.miui.gallery.util.logger.DefaultLogger;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GalleryConnectivityMonitorFactory.kt */
/* loaded from: classes2.dex */
public final class GalleryConnectivityMonitorFactory implements ConnectivityMonitorFactory {
    public static final Companion Companion = new Companion(null);

    @Override // com.bumptech.glide.manager.ConnectivityMonitorFactory
    public ConnectivityMonitor build(Context context, ConnectivityMonitor.ConnectivityListener listener) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(listener, "listener");
        boolean z = ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE") == 0;
        DefaultLogger.d("GalleryConnectivityMonitor", z ? "ACCESS_NETWORK_STATE permission granted, registering connectivity monitor" : "ACCESS_NETWORK_STATE permission missing, cannot register connectivity monitor");
        if (z) {
            return new GalleryConnectivityMonitor(context, listener);
        }
        return new NullConnectivityMonitor();
    }

    /* compiled from: GalleryConnectivityMonitorFactory.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
