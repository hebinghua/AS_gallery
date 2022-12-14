package com.miui.gallery.editor.photo.core.common.fragment;

import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderFragment;

/* loaded from: classes2.dex */
public abstract class AbstractEffectFragment extends RenderFragment {
    public abstract void add(Metadata metadata, Object obj);

    public void addSignature(Metadata metadata, Object obj) {
    }

    public abstract void remove(Metadata metadata);

    public abstract void render();
}
