package com.miui.gallery.editor.photo.core.imports.miuibeauty;

import android.content.Context;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.RenderScript$Translator;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MiuiBeautyProvider<T extends Metadata> extends SdkProvider<T, AbstractEffectFragment> {
    public static MiuiBeautyProvider<MiuiBeautifyData> sMiuiBeautify = new MiuiBeautyProvider<MiuiBeautifyData>(Effect.MIUIBEAUTIFY) { // from class: com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyProvider.1
        public String toString() {
            return "sMiuiBeautify";
        }

        @Override // com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyProvider, com.miui.gallery.editor.photo.core.SdkProvider
        public /* bridge */ /* synthetic */ AbstractEffectFragment onCreateFragment() {
            return super.onCreateFragment();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyProvider, com.miui.gallery.editor.photo.core.SdkProvider
        public List<? extends MiuiBeautifyData> list() {
            MiuiBeautyEffect[] beautyEffects;
            ArrayList arrayList = new ArrayList();
            for (MiuiBeautyEffect miuiBeautyEffect : MiuiBeautyManager.getBeautyEffects()) {
                arrayList.add(new MiuiBeautifyData((short) miuiBeautyEffect.mOrdinal, miuiBeautyEffect.mName, miuiBeautyEffect.mBeautyType));
            }
            return arrayList;
        }

        @Override // com.miui.gallery.editor.photo.core.SdkProvider
        public RenderScript$Translator onCreateTranslator() {
            return super.onCreateTranslator();
        }
    };

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends T> list() {
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        notifyInitializeFinish();
    }

    public MiuiBeautyProvider(Effect<? extends SdkProvider<T, AbstractEffectFragment>> effect) {
        super(effect);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractEffectFragment onCreateFragment() {
        return new MiuiBeautyRenderFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new MiuiBeautyEngine();
    }

    static {
        SdkManager.INSTANCE.register(sMiuiBeautify);
    }
}
