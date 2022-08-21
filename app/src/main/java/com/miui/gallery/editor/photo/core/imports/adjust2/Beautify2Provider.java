package com.miui.gallery.editor.photo.core.imports.adjust2;

import android.content.Context;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;

/* loaded from: classes2.dex */
public class Beautify2Provider extends Adjust2Provider {
    public static Beautify2Provider BEAUTIFY2 = new Beautify2Provider(Effect.BEAUTIFY2);

    @Override // com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2Provider, com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractEffectFragment onCreateFragment() {
        return null;
    }

    static {
        SdkManager.INSTANCE.register(BEAUTIFY2);
    }

    public Beautify2Provider(Effect effect) {
        super(effect);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2Provider, com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        notifyInitializeFinish();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2Provider, com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new Adjust2Engine(context);
    }
}
