package com.miui.gallery.trackers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import androidx.core.net.ConnectivityManagerCompat;
import com.miui.gallery.arch.internal.TaskExecutor;
import com.miui.gallery.util.logger.DefaultLogger;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NetworkStateTracker.kt */
@SuppressLint({"MissingPermission"})
/* loaded from: classes2.dex */
public final class NetworkStateTracker extends ConstraintTracker<NetworkState> {
    public static final Companion Companion = new Companion(null);
    public final ConnectivityManager connectivityManager;
    public NetworkStateCallback networkCallback;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NetworkStateTracker(Context context, TaskExecutor taskExecutor) {
        super(context, taskExecutor);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(taskExecutor, "taskExecutor");
        Object systemService = getAppContext().getSystemService(ConnectivityManager.class);
        Intrinsics.checkNotNullExpressionValue(systemService, "appContext.getSystemServ…ivityManager::class.java)");
        this.connectivityManager = (ConnectivityManager) systemService;
        this.networkCallback = new NetworkStateCallback(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.trackers.ConstraintTracker
    /* renamed from: getInitialState */
    public NetworkState mo1417getInitialState() {
        return getActiveNetworkState();
    }

    @Override // com.miui.gallery.trackers.ConstraintTracker
    public void startTracking() {
        try {
            DefaultLogger.d("NetworkStateTracker", "Registering network callback");
            ConnectivityManager connectivityManager = this.connectivityManager;
            NetworkStateCallback networkStateCallback = this.networkCallback;
            Intrinsics.checkNotNull(networkStateCallback);
            connectivityManager.registerDefaultNetworkCallback(networkStateCallback);
        } catch (IllegalArgumentException e) {
            DefaultLogger.e("NetworkStateTracker", "Received exception while registering network callback", e);
        } catch (SecurityException e2) {
            DefaultLogger.e("NetworkStateTracker", "Received exception while registering network callback", e2);
        }
    }

    @Override // com.miui.gallery.trackers.ConstraintTracker
    public void stopTracking() {
        try {
            DefaultLogger.d("NetworkStateTracker", "Unregistering network callback");
            ConnectivityManager connectivityManager = this.connectivityManager;
            NetworkStateCallback networkStateCallback = this.networkCallback;
            Intrinsics.checkNotNull(networkStateCallback);
            connectivityManager.unregisterNetworkCallback(networkStateCallback);
        } catch (IllegalArgumentException e) {
            DefaultLogger.e("NetworkStateTracker", "Received exception while unregistering network callback", e);
        } catch (SecurityException e2) {
            DefaultLogger.e("NetworkStateTracker", "Received exception while unregistering network callback", e2);
        }
    }

    public final NetworkState getActiveNetworkState() {
        NetworkInfo activeNetworkInfo = this.connectivityManager.getActiveNetworkInfo();
        boolean z = true;
        boolean z2 = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        boolean isActiveNetworkValidated = isActiveNetworkValidated();
        boolean isActiveNetworkMetered = ConnectivityManagerCompat.isActiveNetworkMetered(this.connectivityManager);
        if (activeNetworkInfo == null || activeNetworkInfo.isRoaming()) {
            z = false;
        }
        return new NetworkState(z2, isActiveNetworkValidated, isActiveNetworkMetered, z);
    }

    public final boolean isActiveNetworkValidated() {
        try {
            NetworkCapabilities networkCapabilities = this.connectivityManager.getNetworkCapabilities(this.connectivityManager.getActiveNetwork());
            if (networkCapabilities == null) {
                return false;
            }
            return networkCapabilities.hasCapability(16);
        } catch (SecurityException e) {
            DefaultLogger.e("NetworkStateTracker", "Unable to validate active network", e);
            return false;
        }
    }

    /* compiled from: NetworkStateTracker.kt */
    /* loaded from: classes2.dex */
    public final class NetworkStateCallback extends ConnectivityManager.NetworkCallback {
        public final /* synthetic */ NetworkStateTracker this$0;

        public NetworkStateCallback(NetworkStateTracker this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities capabilities) {
            Intrinsics.checkNotNullParameter(network, "network");
            Intrinsics.checkNotNullParameter(capabilities, "capabilities");
            DefaultLogger.d("NetworkStateTracker", Intrinsics.stringPlus("Network capabilities changed: ", capabilities));
            NetworkStateTracker networkStateTracker = this.this$0;
            networkStateTracker.setState(networkStateTracker.getActiveNetworkState());
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            Intrinsics.checkNotNullParameter(network, "network");
            DefaultLogger.d("NetworkStateTracker", "Network connection lost");
            NetworkStateTracker networkStateTracker = this.this$0;
            networkStateTracker.setState(networkStateTracker.getActiveNetworkState());
        }
    }

    /* compiled from: NetworkStateTracker.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
