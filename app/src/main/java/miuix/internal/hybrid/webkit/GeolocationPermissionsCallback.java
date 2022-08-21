package miuix.internal.hybrid.webkit;

import android.webkit.GeolocationPermissions;
import miuix.hybrid.GeolocationPermissions;

/* loaded from: classes3.dex */
public class GeolocationPermissionsCallback implements GeolocationPermissions.Callback {
    public GeolocationPermissions.Callback mCallback;

    public GeolocationPermissionsCallback(GeolocationPermissions.Callback callback) {
        this.mCallback = callback;
    }

    @Override // miuix.hybrid.GeolocationPermissions.Callback
    public void invoke(String str, boolean z, boolean z2) {
        this.mCallback.invoke(str, z, z2);
    }
}
