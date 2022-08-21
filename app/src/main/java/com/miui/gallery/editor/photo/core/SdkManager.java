package com.miui.gallery.editor.photo.core;

import android.app.Application;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class SdkManager {
    public static final SdkManager INSTANCE = new SdkManager();
    public SdkProvider[] mProviders = new SdkProvider[Effect.values().length];
    public boolean mHasCreateCalled = false;

    static {
        load("com.miui.gallery.editor.photo.core.imports.filter.FilterProvider", "com.miui.gallery.editor.photo.core.imports.crop.CropProvider", "com.miui.gallery.editor.photo.core.imports.sticker.StickerProvider", "com.miui.gallery.editor.photo.core.imports.longcrop.LongCropProvider", "com.miui.gallery.editor.photo.core.imports.text.TextProvider", "com.miui.gallery.editor.photo.core.imports.doodle.DoodleProvider", "com.miui.gallery.editor.photo.core.imports.mosaic.MosaicProvider", "com.miui.gallery.editor.photo.core.imports.remover.RemoverProvider", "com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyProvider", "com.miui.gallery.editor.photo.core.imports.frame.FrameProvider", "com.miui.gallery.editor.photo.core.imports.sky.SkyProvider", "com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2Provider", "com.miui.gallery.editor.photo.core.imports.adjust2.Beautify2Provider", "com.miui.gallery.editor.photo.core.imports.remover2.Remover2Provider");
    }

    public void onAttach(Application application) {
        SdkProvider[] sdkProviderArr;
        DefaultLogger.d("SdkManager", "attach to application");
        for (SdkProvider sdkProvider : this.mProviders) {
            if (sdkProvider != null) {
                sdkProvider.attach(application);
            }
        }
    }

    public void onActivityCreate() {
        SdkProvider[] sdkProviderArr;
        if (!this.mHasCreateCalled) {
            DefaultLogger.d("SdkManager", "creating application");
            for (SdkProvider sdkProvider : this.mProviders) {
                if (sdkProvider != null) {
                    DefaultLogger.d("SdkManager", "notify %s's provider application is creating", sdkProvider.mSupported);
                    sdkProvider.onActivityCreate();
                }
            }
            this.mHasCreateCalled = true;
        }
    }

    public <T extends SdkProvider> T getProvider(Effect<T> effect) {
        return (T) this.mProviders[effect.ordinal()];
    }

    public <T extends SdkProvider> void register(T t) {
        DefaultLogger.d("SdkManager", "registering %s", t);
        Effect<? extends SdkProvider<D, F>> effect = t.mSupported;
        SdkProvider sdkProvider = this.mProviders[effect.ordinal()];
        if (sdkProvider == null) {
            this.mProviders[effect.ordinal()] = t;
            return;
        }
        throw new IllegalStateException("already registered provider found: " + sdkProvider);
    }

    public static void load(String... strArr) {
        DefaultLogger.d("SdkManager", "loading %s", Arrays.toString(strArr));
        for (String str : strArr) {
            try {
                Class.forName(str);
            } catch (ClassNotFoundException e) {
                DefaultLogger.w("SdkManager", e);
            }
        }
    }
}
