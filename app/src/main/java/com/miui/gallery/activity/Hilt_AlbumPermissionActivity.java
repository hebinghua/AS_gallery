package com.miui.gallery.activity;

import android.content.Context;
import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.internal.GeneratedComponentManager;
import dagger.hilt.internal.UnsafeCasts;

/* loaded from: classes.dex */
public abstract class Hilt_AlbumPermissionActivity extends BaseActivity implements GeneratedComponentManager {
    public volatile ActivityComponentManager componentManager;
    public final Object componentManagerLock = new Object();
    public boolean injected = false;

    public Hilt_AlbumPermissionActivity() {
        _initHiltInternal();
    }

    public final void _initHiltInternal() {
        addOnContextAvailableListener(new OnContextAvailableListener() { // from class: com.miui.gallery.activity.Hilt_AlbumPermissionActivity.1
            @Override // androidx.activity.contextaware.OnContextAvailableListener
            public void onContextAvailable(Context context) {
                Hilt_AlbumPermissionActivity.this.inject();
            }
        });
    }

    @Override // dagger.hilt.internal.GeneratedComponentManager
    /* renamed from: generatedComponent */
    public final Object mo2560generatedComponent() {
        return componentManager().mo2560generatedComponent();
    }

    public ActivityComponentManager createComponentManager() {
        return new ActivityComponentManager(this);
    }

    public final ActivityComponentManager componentManager() {
        if (this.componentManager == null) {
            synchronized (this.componentManagerLock) {
                if (this.componentManager == null) {
                    this.componentManager = createComponentManager();
                }
            }
        }
        return this.componentManager;
    }

    public void inject() {
        if (!this.injected) {
            this.injected = true;
            ((AlbumPermissionActivity_GeneratedInjector) mo2560generatedComponent()).injectAlbumPermissionActivity((AlbumPermissionActivity) UnsafeCasts.unsafeCast(this));
        }
    }

    @Override // androidx.activity.ComponentActivity, androidx.lifecycle.HasDefaultViewModelProviderFactory
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return DefaultViewModelFactories.getActivityFactory(this, super.getDefaultViewModelProviderFactory());
    }
}
