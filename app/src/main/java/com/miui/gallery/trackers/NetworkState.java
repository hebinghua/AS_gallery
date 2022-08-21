package com.miui.gallery.trackers;

/* compiled from: NetworkState.kt */
/* loaded from: classes2.dex */
public final class NetworkState {
    public final boolean isConnected;
    public final boolean isMetered;
    public final boolean isNotRoaming;
    public final boolean isValidated;

    public NetworkState(boolean z, boolean z2, boolean z3, boolean z4) {
        this.isConnected = z;
        this.isValidated = z2;
        this.isMetered = z3;
        this.isNotRoaming = z4;
    }

    public final boolean isConnected() {
        return this.isConnected;
    }

    public final boolean isMetered() {
        return this.isMetered;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NetworkState)) {
            return false;
        }
        NetworkState networkState = (NetworkState) obj;
        return this.isConnected == networkState.isConnected && this.isValidated == networkState.isValidated && this.isMetered == networkState.isMetered && this.isNotRoaming == networkState.isNotRoaming;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [boolean, int] */
    public int hashCode() {
        ?? r0 = this.isConnected;
        int i = r0;
        if (this.isValidated) {
            i = r0 + 16;
        }
        int i2 = i;
        if (this.isMetered) {
            i2 = i + 256;
        }
        return this.isNotRoaming ? i2 + 4096 : i2;
    }

    public String toString() {
        return "[ Connected=" + this.isConnected + " Validated=" + this.isValidated + " Metered=" + this.isMetered + " NotRoaming=" + this.isNotRoaming + " ]";
    }
}
