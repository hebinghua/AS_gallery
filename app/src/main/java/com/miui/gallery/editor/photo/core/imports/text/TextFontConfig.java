package com.miui.gallery.editor.photo.core.imports.text;

import android.content.Context;
import java.io.File;

/* loaded from: classes2.dex */
public class TextFontConfig {
    public static String FONT_PATH;

    public static void init(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir == null) {
            externalFilesDir = context.getFilesDir();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(externalFilesDir);
        String str = File.separator;
        sb.append(str);
        sb.append("text_font");
        String sb2 = sb.toString();
        FONT_PATH = sb2 + str + "font";
    }
}
