package com.miui.gallery.editor.photo.core.common.model;

import android.os.Parcel;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.imports.filter.Renderable;

/* loaded from: classes2.dex */
public abstract class RenderMetaData extends Metadata implements Renderable {
    public RenderMetaData(short s, String str) {
        super(s, str);
    }

    public RenderMetaData(Parcel parcel) {
        super(parcel);
    }
}
