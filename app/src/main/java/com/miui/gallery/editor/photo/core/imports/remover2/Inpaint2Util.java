package com.miui.gallery.editor.photo.core.imports.remover2;

/* loaded from: classes2.dex */
public class Inpaint2Util {
    public static int[] MASKCOLORS = {-8111873, -15429889, -13713153, -13376315, -34020, -16612, -3924737};

    public static int getMaskColor(int i) {
        return MASKCOLORS[i % 7];
    }
}
