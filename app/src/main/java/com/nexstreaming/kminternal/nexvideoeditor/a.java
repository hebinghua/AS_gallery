package com.nexstreaming.kminternal.nexvideoeditor;

import android.graphics.Typeface;
import com.nexstreaming.kminternal.kinemaster.fonts.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* compiled from: EffectResourceLoader.java */
/* loaded from: classes3.dex */
public interface a {
    Typeface a(String str) throws Font.TypefaceLoadException;

    File a(String str, String str2) throws IOException;

    InputStream b(String str, String str2) throws IOException;

    Typeface c(String str, String str2) throws Font.TypefaceLoadException, IOException;
}
