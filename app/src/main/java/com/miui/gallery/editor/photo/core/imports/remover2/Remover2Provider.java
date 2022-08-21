package com.miui.gallery.editor.photo.core.imports.remover2;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemover2Fragment;
import com.miui.gallery.editor.photo.core.common.model.Remover2Data;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class Remover2Provider extends SdkProvider<Remover2Data, AbstractRemover2Fragment> {
    public Remover2Provider() {
        super(Effect.REMOVER2);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        notifyInitializeFinish();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends Remover2Data> list() {
        return Arrays.asList(new Remover2Data((short) 0, getApplicationContext().getString(R.string.photo_editor_remover2_edit_type_free), R.drawable.icon_removepro_dust, 0), new Remover2Data((short) 1, getApplicationContext().getString(R.string.photo_editor_remover2_edit_type_line), R.drawable.icon_removepro_line, 1), new Remover2Data((short) 2, getApplicationContext().getString(R.string.photo_editor_remover2_edit_type_people), R.drawable.icon_removepro_people, 2));
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractRemover2Fragment onCreateFragment() {
        return new Remover2RenderFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new Remover2Engine();
    }

    static {
        SdkManager.INSTANCE.register(new Remover2Provider());
    }
}
