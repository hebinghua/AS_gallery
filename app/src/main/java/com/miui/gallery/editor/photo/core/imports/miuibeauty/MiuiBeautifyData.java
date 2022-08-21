package com.miui.gallery.editor.photo.core.imports.miuibeauty;

import com.miui.filtersdk.beauty.BeautyParameterType;
import com.miui.gallery.editor.photo.core.Metadata;

/* loaded from: classes2.dex */
public class MiuiBeautifyData extends Metadata {
    public BeautyParameterType mParameterType;

    public MiuiBeautifyData(short s, String str, BeautyParameterType beautyParameterType) {
        super(s, str);
        this.mParameterType = beautyParameterType;
    }
}
