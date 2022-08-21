package com.miui.gallery.editor.photo.core.imports.doodle;

import android.content.Context;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import java.util.List;

/* loaded from: classes2.dex */
class DoodleProvider extends SdkProvider<DoodleData, AbstractDoodleFragment> {
    public List<DoodleData> mDoodleDataList;

    private DoodleProvider() {
        super(Effect.DOODLE);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        initialize();
    }

    public final void initialize() {
        this.mDoodleDataList = DoodleManager.getDoodleData();
        notifyInitializeFinish();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends DoodleData> list() {
        return this.mDoodleDataList;
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractDoodleFragment onCreateFragment() {
        return new DoodleFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new DoodleEngine();
    }

    static {
        SdkManager.INSTANCE.register(new DoodleProvider());
    }
}
