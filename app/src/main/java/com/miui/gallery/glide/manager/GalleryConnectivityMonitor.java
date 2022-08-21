package com.miui.gallery.glide.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.util.Preconditions;
import com.miui.gallery.trackers.ConstraintListener;
import com.miui.gallery.trackers.NetworkState;
import com.miui.gallery.trackers.Trackers;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GalleryConnectivityMonitor.kt */
/* loaded from: classes2.dex */
public final class GalleryConnectivityMonitor implements ConnectivityMonitor {
    public static final Companion Companion = new Companion(null);
    public final GalleryConnectivityMonitor$connectivityListener$1 connectivityListener;
    public final Context context;
    public boolean isConnected;
    public boolean isRegistered;
    public final ConnectivityMonitor.ConnectivityListener listener;

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.miui.gallery.glide.manager.GalleryConnectivityMonitor$connectivityListener$1] */
    public GalleryConnectivityMonitor(Context context, ConnectivityMonitor.ConnectivityListener listener) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listener = listener;
        Context applicationContext = context.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "context.applicationContext");
        this.context = applicationContext;
        this.connectivityListener = new ConstraintListener<NetworkState>() { // from class: com.miui.gallery.glide.manager.GalleryConnectivityMonitor$connectivityListener$1
            @Override // com.miui.gallery.trackers.ConstraintListener
            public void onConstraintChanged(NetworkState networkState) {
                boolean access$isConnected$p = GalleryConnectivityMonitor.access$isConnected$p(GalleryConnectivityMonitor.this);
                GalleryConnectivityMonitor.access$setConnected$p(GalleryConnectivityMonitor.this, networkState == null ? false : networkState.isConnected());
                if (access$isConnected$p != GalleryConnectivityMonitor.access$isConnected$p(GalleryConnectivityMonitor.this)) {
                    DefaultLogger.d("GalleryConnectivityMonitor", Intrinsics.stringPlus("connectivity changed, isConnected: ", Boolean.valueOf(GalleryConnectivityMonitor.access$isConnected$p(GalleryConnectivityMonitor.this))));
                    GalleryConnectivityMonitor.access$getListener$p(GalleryConnectivityMonitor.this).onConnectivityChanged(GalleryConnectivityMonitor.access$isConnected$p(GalleryConnectivityMonitor.this));
                }
            }
        };
    }

    public static final /* synthetic */ ConnectivityMonitor.ConnectivityListener access$getListener$p(GalleryConnectivityMonitor galleryConnectivityMonitor) {
        return galleryConnectivityMonitor.listener;
    }

    public static final /* synthetic */ boolean access$isConnected$p(GalleryConnectivityMonitor galleryConnectivityMonitor) {
        return galleryConnectivityMonitor.isConnected;
    }

    public static final /* synthetic */ void access$setConnected$p(GalleryConnectivityMonitor galleryConnectivityMonitor, boolean z) {
        galleryConnectivityMonitor.isConnected = z;
    }

    public final void register() {
        if (this.isRegistered) {
            return;
        }
        this.isConnected = isConnected(this.context);
        try {
            Trackers.getNetworkStateTracker().registerListener(this.connectivityListener);
            this.isRegistered = true;
        } catch (Exception unused) {
            DefaultLogger.e("GalleryConnectivityMonitor", "Failed to register network state listener");
        }
    }

    public final void unregister() {
        if (!this.isRegistered) {
            return;
        }
        Trackers.getNetworkStateTracker().unregisterListener(this.connectivityListener);
        this.isRegistered = false;
    }

    @SuppressLint({"MissingPermission"})
    public final boolean isConnected(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Object systemService = context.getSystemService("connectivity");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.net.ConnectivityManager");
        Object checkNotNull = Preconditions.checkNotNull((ConnectivityManager) systemService);
        Intrinsics.checkNotNullExpressionValue(checkNotNull, "checkNotNull(\n          …ectivityManager\n        )");
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) checkNotNull).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (RuntimeException e) {
            if (Log.isLoggable("GalleryConnectivityMonitor", 5)) {
                Log.w("GalleryConnectivityMonitor", "Failed to determine connectivity status when connectivity changed", e);
            }
            return true;
        }
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStart() {
        register();
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
        unregister();
    }

    /* compiled from: GalleryConnectivityMonitor.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
