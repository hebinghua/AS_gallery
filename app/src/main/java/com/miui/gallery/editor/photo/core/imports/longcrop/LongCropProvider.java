package com.miui.gallery.editor.photo.core.imports.longcrop;

import android.content.Context;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractLongCropFragment;
import java.util.List;

/* loaded from: classes2.dex */
public class LongCropProvider extends SdkProvider<Object, AbstractLongCropFragment> {
    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends Object> list() {
        return null;
    }

    public LongCropProvider() {
        super(Effect.LONG_CROP);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        notifyInitializeFinish();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractLongCropFragment onCreateFragment() {
        return new LongScreenshotCropFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new CropRenderEngine();
    }

    static {
        SdkManager.INSTANCE.register(new LongCropProvider());
    }
}
