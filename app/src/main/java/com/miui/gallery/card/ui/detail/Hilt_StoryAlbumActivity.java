package com.miui.gallery.card.ui.detail;

import android.content.Context;
import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.lifecycle.ViewModelProvider;
import com.miui.gallery.activity.BaseActivity;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.internal.GeneratedComponentManager;
import dagger.hilt.internal.UnsafeCasts;

/* loaded from: classes.dex */
public abstract class Hilt_StoryAlbumActivity extends BaseActivity implements GeneratedComponentManager {
    public volatile ActivityComponentManager componentManager;
    public final Object componentManagerLock = new Object();
    public boolean injected = false;

    public Hilt_StoryAlbumActivity() {
        _initHiltInternal();
    }

    public final void _initHiltInternal() {
        addOnContextAvailableListener(new OnContextAvailableListener() { // from class: com.miui.gallery.card.ui.detail.Hilt_StoryAlbumActivity.1
            @Override // androidx.activity.contextaware.OnContextAvailableListener
            public void onContextAvailable(Context context) {
                Hilt_StoryAlbumActivity.this.inject();
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
            ((StoryAlbumActivity_GeneratedInjector) mo2560generatedComponent()).injectStoryAlbumActivity((StoryAlbumActivity) UnsafeCasts.unsafeCast(this));
        }
    }

    @Override // androidx.activity.ComponentActivity, androidx.lifecycle.HasDefaultViewModelProviderFactory
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return DefaultViewModelFactories.getActivityFactory(this, super.getDefaultViewModelProviderFactory());
    }
}
