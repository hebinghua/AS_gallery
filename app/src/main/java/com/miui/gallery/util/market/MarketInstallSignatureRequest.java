package com.miui.gallery.util.market;

import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.net.BaseGalleryRequest;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class MarketInstallSignatureRequest extends BaseGalleryRequest {
    public MarketInstallSignatureRequest(String str) {
        super(0, HostManager.Market.getSignatureUrl(str));
        setUseCache(false);
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        SignatureResult signatureResult = new SignatureResult();
        signatureResult.nonce = jSONObject.getString("nonce");
        signatureResult.signature = jSONObject.getString("sign");
        deliverResponse(signatureResult);
    }
}
