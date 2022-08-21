package com.miui.gallery.editor.photo.core;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderFragment;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class SdkProvider<D extends Metadata, F extends RenderFragment> {
    public static RenderScript$Translator<?> DUMMY = new RenderScript$Translator<RenderData>(new ArrayList()) { // from class: com.miui.gallery.editor.photo.core.SdkProvider.1
    };
    public Application mApplication;
    public volatile boolean mInitialized;
    public Handler mNotifyHandler = new Handler(Looper.getMainLooper());
    public Effect<? extends SdkProvider<D, F>> mSupported;
    public RenderScript$Translator mTranslator;

    public abstract RenderEngine createEngine(Context context);

    public abstract List<? extends D> list();

    public abstract F onCreateFragment();

    public SdkProvider(Effect<? extends SdkProvider<D, F>> effect) {
        this.mSupported = effect;
    }

    public void attach(Application application) {
        this.mApplication = application;
    }

    public void onActivityCreate() {
        this.mTranslator = onCreateTranslator();
    }

    public boolean initialized() {
        return this.mInitialized;
    }

    public final F createFragment() {
        F onCreateFragment = onCreateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("RenderFragment:effect", this.mSupported.ordinal());
        onCreateFragment.setArguments(bundle);
        return onCreateFragment;
    }

    public RenderScript$Translator onCreateTranslator() {
        return DUMMY;
    }

    public final Application getApplicationContext() {
        return this.mApplication;
    }

    public final void notifyInitializeFinish() {
        DefaultLogger.d("SdkProvider", "%s initialize finish, post notify task", this);
        this.mInitialized = true;
    }
}
