package com.nexstreaming.kminternal.kinemaster.fonts;

import android.graphics.Typeface;
import android.util.Log;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.kminternal.kinemaster.fonts.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: FontManager.java */
/* loaded from: classes3.dex */
public class c {
    private static c a;
    private List<b> b = null;
    private Map<String, Font> c;

    public static c a() {
        if (a == null) {
            a = new c();
        }
        return a;
    }

    private static Map<String, Integer> d() {
        HashMap hashMap = new HashMap();
        hashMap.put("latin", 0);
        hashMap.put("hangul", 0);
        hashMap.put("chs", 0);
        hashMap.put("cht", 0);
        hashMap.put("japanese", 0);
        hashMap.put("android", 0);
        return hashMap;
    }

    /* compiled from: FontManager.java */
    /* loaded from: classes3.dex */
    public static class a implements b {
        private final String a;
        private final int b;
        private final List<Font> c = new ArrayList();

        public a(String str, int i) {
            this.a = str;
            this.b = i;
        }

        @Override // com.nexstreaming.kminternal.kinemaster.fonts.b
        public List<Font> a() {
            return Collections.unmodifiableList(this.c);
        }

        public List<Font> b() {
            return this.c;
        }
    }

    public List<b> b() {
        if (this.b == null) {
            e();
        }
        return this.b;
    }

    private void e() {
        Map<String, Integer> d = d();
        HashMap hashMap = new HashMap();
        for (Font font : f().values()) {
            String b = font.b();
            a aVar = (a) hashMap.get(b);
            if (aVar == null) {
                Integer num = d.get(b);
                if (num == null) {
                    num = 0;
                }
                a aVar2 = new a(b, num.intValue());
                hashMap.put(b, aVar2);
                aVar = aVar2;
            }
            aVar.b().add(font);
        }
        this.b = Collections.unmodifiableList(new ArrayList(hashMap.values()));
    }

    private Map<String, Font> f() {
        if (this.c == null) {
            List<Font> a2 = com.nexstreaming.kminternal.kinemaster.fonts.a.a();
            this.c = new HashMap();
            for (Font font : a2) {
                this.c.put(font.a(), font);
            }
        }
        return this.c;
    }

    public void c() {
        this.c = null;
    }

    public boolean a(String str) {
        if (f().get(str) != null) {
            return true;
        }
        f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        return c != null && c.getType() == ItemType.font;
    }

    public Typeface b(String str) {
        if (str != null && str.trim().length() >= 1) {
            String substring = str.substring(str.indexOf(47) + 1);
            Font font = f().get(substring);
            if (font != null) {
                try {
                    return font.b(com.nexstreaming.kminternal.kinemaster.config.a.a().b());
                } catch (Font.TypefaceLoadException unused) {
                    return null;
                }
            }
            Log.d("FontManager", "Get typeface: " + substring);
            f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(substring);
            if (c == null || c.getType() != ItemType.font) {
                Log.w("FontManager", "Typeface not found: " + substring);
            } else if (com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(c.getAssetPackage())) {
                Log.w("FontManager", "Typeface expire: " + substring);
                return null;
            } else {
                try {
                    AssetPackageReader a2 = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c.getPackageURI(), c.getAssetPackage().getAssetId());
                    try {
                        return a2.d(c.getFilePath());
                    } catch (AssetPackageReader.LocalPathNotAvailableException e) {
                        Log.e("FontManager", "Error loading typeface: " + substring, e);
                        return null;
                    } finally {
                        com.nexstreaming.app.common.util.b.a(a2);
                    }
                } catch (IOException e2) {
                    Log.e("FontManager", "Error loading typeface: " + substring, e2);
                    return null;
                }
            }
        }
        return null;
    }
}
