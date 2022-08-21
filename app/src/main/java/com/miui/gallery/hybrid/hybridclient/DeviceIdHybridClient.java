package com.miui.gallery.hybrid.hybridclient;

import android.content.Context;
import android.webkit.JavascriptInterface;
import com.miui.gallery.hybrid.hybridclient.HybridClient;
import com.miui.gallery.request.HostManager;
import com.miui.security.id.IdentifierManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DeviceIdHybridClient extends GalleryHybridClient {
    public DeviceIdHybridClient(Context context, String str) {
        super(context, str);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public List<HybridClient.JsInterfacePair> getJavascriptInterfaces() {
        List<HybridClient.JsInterfacePair> javascriptInterfaces = super.getJavascriptInterfaces();
        if (javascriptInterfaces == null) {
            javascriptInterfaces = new ArrayList<>(1);
        }
        javascriptInterfaces.add(new HybridClient.JsInterfacePair("MiuiGalleryJSBridge", new MiuiGalleryJSBridge()));
        return javascriptInterfaces;
    }

    /* loaded from: classes2.dex */
    public class MiuiGalleryJSBridge {
        public MiuiGalleryJSBridge() {
        }

        @JavascriptInterface
        public String getDeviceId() {
            if (HostManager.isInternalUrl(DeviceIdHybridClient.this.mCurrentUrl)) {
                return IdentifierManager.getOAID(DeviceIdHybridClient.this.mContext);
            }
            return null;
        }
    }
}
