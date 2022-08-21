package com.miui.gallery.util.deviceprovider;

import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloud.CloudUtils;

/* loaded from: classes2.dex */
public class BigApplicationHelper extends ApplicationHelper {
    @Override // com.miui.gallery.util.deviceprovider.ApplicationHelper
    public boolean isSecretAlbumFeatureOpenInternal() {
        return true;
    }

    @Override // com.miui.gallery.util.deviceprovider.ApplicationHelper
    public final IntentProviderInterface getIntentProviderInternal() {
        return new BigIntentProvider();
    }

    @Override // com.miui.gallery.util.deviceprovider.ApplicationHelper
    public MiCloudProviderInterface getMiCloudProviderInternal() {
        return new BigMiCloudProvider();
    }

    @Override // com.miui.gallery.util.deviceprovider.ApplicationHelper
    public BitmapProviderInterface getBitmapProviderInternal() {
        return new BigBitmapProvider();
    }

    @Override // com.miui.gallery.util.deviceprovider.ApplicationHelper
    public boolean supportShareInternal() {
        return CloudUtils.supportShare();
    }

    @Override // com.miui.gallery.util.deviceprovider.ApplicationHelper
    public boolean supportStoryAlbumInternal() {
        return MediaFeatureManager.isDeviceSupportStoryFunction();
    }
}
