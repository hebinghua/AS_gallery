package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemCategory;
import com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB;
import com.nexstreaming.app.common.util.n;
import com.nexstreaming.kminternal.kinemaster.fonts.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public final class nexFont {
    private static final String LOG_TAG = "nexFont";
    private static List<nexFont> list = new ArrayList();
    private static boolean s_update;
    private boolean builtin;
    private Font font;
    private String sample;
    private boolean system;

    private nexFont(boolean z, Font font, String str, boolean z2) {
        this.builtin = z;
        this.font = font;
        this.sample = str;
        this.system = z2;
    }

    public String getId() {
        return this.font.a();
    }

    public String getSampleText() {
        return this.sample;
    }

    public boolean isBuiltinFont() {
        return this.builtin;
    }

    public boolean isSystemFont() {
        return this.system;
    }

    public Bitmap getSampleImage(Context context) {
        return this.font.c(context);
    }

    public Typeface getTypeFace() {
        return com.nexstreaming.kminternal.kinemaster.fonts.c.a().b(getId());
    }

    public static List<nexFont> getPresetList() {
        if (list.size() == 0) {
            for (com.nexstreaming.kminternal.kinemaster.fonts.b bVar : com.nexstreaming.kminternal.kinemaster.fonts.c.a().b()) {
                for (Font font : bVar.a()) {
                    list.add(new nexFont(true, font, font.a((Context) null), font.a().startsWith("system")));
                }
            }
            for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(ItemCategory.font)) {
                if (!fVar.isHidden()) {
                    String valueOf = String.valueOf((int) fVar.getAssetPackage().getAssetSubCategory().getSubCategoryId());
                    if (valueOf == null) {
                        valueOf = "asset";
                    }
                    String sampleText = fVar.getSampleText();
                    if (sampleText == null || sampleText.trim().length() < 1) {
                        sampleText = n.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), fVar.getLabel());
                    }
                    if (sampleText == null) {
                        sampleText = fVar.getId();
                    }
                    Log.d(LOG_TAG, "asset font id=" + fVar.getId() + ", loacalPath=" + getLocalPath(fVar));
                    list.add(new nexFont(fVar.getPackageURI().contains(AssetLocalInstallDB.getInstalledAssetPath()) ^ true, new Font(fVar.getId(), valueOf, new File(getLocalPath(fVar)), sampleText), sampleText, false));
                }
            }
        }
        return list;
    }

    public static nexFont getFont(String str) {
        checkUpdate();
        for (nexFont nexfont : list) {
            if (nexfont.getId().compareTo(str) == 0) {
                return nexfont;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0050 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String getLocalPath(com.nexstreaming.app.common.nexasset.assetpackage.f r5) {
        /*
            java.lang.String r0 = "nexFont"
            r1 = 0
            com.nexstreaming.kminternal.kinemaster.config.a r2 = com.nexstreaming.kminternal.kinemaster.config.a.a()     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            android.content.Context r2 = r2.b()     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            java.lang.String r3 = r5.getPackageURI()     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            java.lang.String r4 = r5.getId()     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader r2 = com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.a(r2, r3, r4)     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            java.lang.String r5 = r5.getFilePath()     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L4c
            java.io.File r5 = r2.c(r5)     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L4c
            java.lang.String r5 = r5.getAbsolutePath()     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L4c
            r2.close()     // Catch: java.io.IOException -> L27
            goto L2f
        L27:
            r1 = move-exception
            java.lang.String r2 = r1.getMessage()
            android.util.Log.e(r0, r2, r1)
        L2f:
            return r5
        L30:
            r5 = move-exception
            goto L36
        L32:
            r5 = move-exception
            goto L4e
        L34:
            r5 = move-exception
            r2 = r1
        L36:
            java.lang.String r3 = r5.getMessage()     // Catch: java.lang.Throwable -> L4c
            android.util.Log.e(r0, r3, r5)     // Catch: java.lang.Throwable -> L4c
            if (r2 == 0) goto L4b
            r2.close()     // Catch: java.io.IOException -> L43
            goto L4b
        L43:
            r5 = move-exception
            java.lang.String r2 = r5.getMessage()
            android.util.Log.e(r0, r2, r5)
        L4b:
            return r1
        L4c:
            r5 = move-exception
            r1 = r2
        L4e:
            if (r1 == 0) goto L5c
            r1.close()     // Catch: java.io.IOException -> L54
            goto L5c
        L54:
            r1 = move-exception
            java.lang.String r2 = r1.getMessage()
            android.util.Log.e(r0, r2, r1)
        L5c:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexFont.getLocalPath(com.nexstreaming.app.common.nexasset.assetpackage.f):java.lang.String");
    }

    public static String[] getFontIds() {
        checkUpdate();
        int size = list.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = list.get(i).getId();
        }
        return strArr;
    }

    public static Typeface getTypeface(Context context, String str) {
        return com.nexstreaming.kminternal.kinemaster.fonts.c.a().b(str);
    }

    public static void reload() {
        list.clear();
        getPresetList();
    }

    public static boolean isLoadedFont(String str) {
        if (com.nexstreaming.kminternal.kinemaster.fonts.c.a().a(str)) {
            return true;
        }
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        return c != null && c.getCategory() == ItemCategory.font;
    }

    public static void clearBuiltinFontsCache() {
        com.nexstreaming.kminternal.kinemaster.fonts.c.a().c();
    }

    public static void needUpdate() {
        s_update = true;
    }

    public static void checkUpdate() {
        if (s_update) {
            s_update = false;
            reload();
        }
    }
}
