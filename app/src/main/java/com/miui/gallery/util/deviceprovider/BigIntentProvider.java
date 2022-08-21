package com.miui.gallery.util.deviceprovider;

/* loaded from: classes2.dex */
public class BigIntentProvider implements IntentProviderInterface {
    @Override // com.miui.gallery.util.deviceprovider.IntentProviderInterface
    public String getExtraAccount() {
        return "account";
    }

    @Override // com.miui.gallery.util.deviceprovider.IntentProviderInterface
    public String getXiaomiAccountType() {
        return "com.xiaomi";
    }
}
