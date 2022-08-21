package com.miui.gallery.editor.photo.core.imports.adjust2;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class Adjust2Provider extends SdkProvider<Adjust2Data, AbstractEffectFragment> {
    public static Adjust2Provider ADJUST2 = new Adjust2Provider(Effect.ADJUST2);

    static {
        SdkManager.INSTANCE.register(ADJUST2);
    }

    public Adjust2Provider(Effect effect) {
        super(effect);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        notifyInitializeFinish();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractEffectFragment onCreateFragment() {
        return new Adjust2RenderFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new Adjust2Engine(context);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends Adjust2Data> list() {
        return getAdjust2Data();
    }

    public static List<Adjust2DataImpl> getAdjust2Data() {
        return Arrays.asList(new Adjust2DataImpl(7, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_exposure), R.drawable.photo_editor_adjust_exposure_selector, true, R.raw.exposure), new Adjust2DataImpl(1, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_brightness), R.drawable.photo_editor_adjust_brightness_selector, true, R.raw.brightness), new Adjust2DataImpl(2, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_contrast), R.drawable.photo_editor_adjust_contrast_selector, true, R.raw.contrast), new Adjust2DataImpl(3, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_saturation), R.drawable.photo_editor_adjust_saturation_selector, true, R.raw.saturation), new Adjust2DataImpl(13, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_natural_saturation), R.drawable.photo_editor_adjust_saturation_natural_selector, true, R.raw.satuation_natural), new Adjust2DataImpl(8, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_color_temperature), R.drawable.photo_editor_adjust_temperature_selector, true, R.raw.temperature), new Adjust2DataImpl(9, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_tiunt), R.drawable.photo_editor_adjust_tiunt_selector, true, R.raw.tiunt), new Adjust2DataImpl(10, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_highlight_shadow), R.drawable.photo_editor_adjust_hightlight_shadow_selector, true, R.raw.highlight_shadow), new Adjust2DataImpl(12, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_faderray), R.drawable.photo_editor_adjust_faderray_selector, false, R.raw.fadegray), new Adjust2DataImpl(6, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_grain), R.drawable.photo_editor_adjust_grain_selector, false, R.raw.grain), new Adjust2DataImpl(4, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_sharpen), R.drawable.photo_editor_adjust_sharpen_selector, false, R.raw.sharpen), new Adjust2DataImpl(5, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_vignette), R.drawable.photo_editor_adjust_vignette_selector, true, R.raw.vignette), new Adjust2DataImpl(11, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_shadow), R.drawable.photo_editor_adjust_vignette_selector, true, 0));
    }
}
