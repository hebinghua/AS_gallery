package com.miui.gallery.editor.photo.core.common.provider;

import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class AbstractTextProvider extends SdkProvider<TextData, AbstractEffectFragment> {
    public abstract List<TextData> listCity();

    public abstract List<TextData> listFestival();

    public abstract List<TextData> listScene();

    public abstract List<TextData> listSpot();

    public abstract List<TextData> listWatermark();

    public AbstractTextProvider(Effect<? extends SdkProvider<TextData, AbstractEffectFragment>> effect) {
        super(effect);
    }
}
