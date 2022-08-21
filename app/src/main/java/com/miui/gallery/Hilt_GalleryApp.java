package com.miui.gallery;

import android.app.Application;
import dagger.hilt.android.internal.managers.ApplicationComponentManager;
import dagger.hilt.android.internal.managers.ComponentSupplier;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.internal.GeneratedComponentManager;
import dagger.hilt.internal.UnsafeCasts;

/* loaded from: classes.dex */
public abstract class Hilt_GalleryApp extends Application implements GeneratedComponentManager {
    public final ApplicationComponentManager componentManager = new ApplicationComponentManager(new ComponentSupplier() { // from class: com.miui.gallery.Hilt_GalleryApp.1
        @Override // dagger.hilt.android.internal.managers.ComponentSupplier
        public Object get() {
            return DaggerGalleryApp_HiltComponents_SingletonC.builder().applicationContextModule(new ApplicationContextModule(Hilt_GalleryApp.this)).build();
        }
    });

    public final ApplicationComponentManager componentManager() {
        return this.componentManager;
    }

    @Override // dagger.hilt.internal.GeneratedComponentManager
    /* renamed from: generatedComponent */
    public final Object mo2560generatedComponent() {
        return componentManager().mo2560generatedComponent();
    }

    @Override // android.app.Application
    public void onCreate() {
        ((GalleryApp_GeneratedInjector) mo2560generatedComponent()).injectGalleryApp((GalleryApp) UnsafeCasts.unsafeCast(this));
        super.onCreate();
    }
}
