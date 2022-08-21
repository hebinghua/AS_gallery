package com.miui.gallery.agreement;

import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;

/* loaded from: classes.dex */
public class OnNetworkInvokedWrapper implements OnAgreementInvokedListener {
    public final OnAgreementInvokedListener mWrapped;

    public OnNetworkInvokedWrapper(OnAgreementInvokedListener onAgreementInvokedListener) {
        this.mWrapped = onAgreementInvokedListener;
    }

    @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
    public void onAgreementInvoked(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.58.2.1.14898");
        if (z) {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "sure");
            TrackController.trackClick(hashMap);
            BaseGalleryPreferences.CTA.setCanConnectNetwork(true);
        } else {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "cancel");
            TrackController.trackClick(hashMap);
            BaseGalleryPreferences.CTA.setToAllowUseOnOfflineGlobal(true);
        }
        OnAgreementInvokedListener onAgreementInvokedListener = this.mWrapped;
        if (onAgreementInvokedListener != null) {
            onAgreementInvokedListener.onAgreementInvoked(z);
        }
    }
}
