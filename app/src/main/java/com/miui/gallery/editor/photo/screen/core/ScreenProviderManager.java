package com.miui.gallery.editor.photo.screen.core;

import android.content.Context;
import com.miui.gallery.editor.photo.screen.mosaic.ScreenMosaicProvider;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class ScreenProviderManager extends ScreenProvider {
    public static final ScreenProviderManager INSTANCE = new ScreenProviderManager();
    public Map<Class, ScreenProvider> mProviderMap;

    public ScreenProviderManager() {
        HashMap hashMap = new HashMap();
        this.mProviderMap = hashMap;
        hashMap.put(ScreenMosaicProvider.class, new ScreenMosaicProvider());
    }

    @Override // com.miui.gallery.editor.photo.screen.core.ScreenProvider
    public void onActivityCreate(Context context) {
        for (ScreenProvider screenProvider : this.mProviderMap.values()) {
            if (screenProvider != null) {
                screenProvider.onActivityCreate(context);
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.core.ScreenProvider
    public void onActivityDestroy() {
        for (ScreenProvider screenProvider : this.mProviderMap.values()) {
            if (screenProvider != null) {
                screenProvider.onActivityDestroy();
            }
        }
    }

    public <T extends ScreenProvider> T getProvider(Class cls) {
        return (T) this.mProviderMap.get(cls);
    }
}
