package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemCategory;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public final class nexEffectLibrary {
    private static nexEffectLibrary sSingleton;
    private final Context mAppContext;

    @Deprecated
    /* loaded from: classes3.dex */
    public static abstract class OnInstallPluginEffectPackageAsyncListener {
        public abstract void onComplete(int i, String[] strArr);

        public abstract void onPackageLoaded(int i);
    }

    @Deprecated
    public nexTheme findThemeById(String str) {
        return null;
    }

    @Deprecated
    public nexTheme[] getThemes() {
        return new nexTheme[0];
    }

    @Deprecated
    public boolean installPluginEffectPackageAsync(String[] strArr, OnInstallPluginEffectPackageAsyncListener onInstallPluginEffectPackageAsyncListener) {
        return true;
    }

    private nexEffectLibrary(Context context) {
        this.mAppContext = context;
    }

    public static nexEffectLibrary getEffectLibrary(Context context) {
        Context applicationContext = context.getApplicationContext();
        nexEffectLibrary nexeffectlibrary = sSingleton;
        if (nexeffectlibrary != null && !nexeffectlibrary.mAppContext.getPackageName().equals(applicationContext.getPackageName())) {
            sSingleton = null;
        }
        if (sSingleton == null) {
            sSingleton = new nexEffectLibrary(applicationContext);
        }
        return sSingleton;
    }

    @Deprecated
    public ArrayList<nexTheme> getThemesEx() {
        return new ArrayList<>();
    }

    public ArrayList<nexTransitionEffect> getTransitionEffectsEx() {
        ArrayList<nexTransitionEffect> arrayList = new ArrayList<>();
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).a(ItemCategory.transition)) {
            if (!fVar.isHidden()) {
                arrayList.add(new nexTransitionEffect(fVar.getId()));
            }
        }
        return arrayList;
    }

    public nexTransitionEffect[] getTransitionEffects() {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.f> a = com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).a(ItemCategory.transition);
        int i = 0;
        int i2 = 0;
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : a) {
            if (!fVar.isHidden()) {
                i2++;
            }
        }
        nexTransitionEffect[] nextransitioneffectArr = new nexTransitionEffect[i2];
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar2 : a) {
            if (!fVar2.isHidden()) {
                nextransitioneffectArr[i] = new nexTransitionEffect(fVar2.getId());
                nextransitioneffectArr[i].itemMethodType = nexAssetPackageManager.getMethodType(fVar2.getType());
                i++;
            }
        }
        return nextransitioneffectArr;
    }

    public nexClipEffect[] getClipEffects() {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.f> a = com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).a(ItemCategory.effect);
        int i = 0;
        int i2 = 0;
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : a) {
            if (!fVar.isHidden()) {
                i2++;
            }
        }
        nexClipEffect[] nexclipeffectArr = new nexClipEffect[i2];
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar2 : a) {
            if (!fVar2.isHidden()) {
                nexclipeffectArr[i] = new nexClipEffect(fVar2.getId());
                nexclipeffectArr[i].itemMethodType = nexAssetPackageManager.getMethodType(fVar2.getType());
                i++;
            }
        }
        return nexclipeffectArr;
    }

    public nexOverlayFilter[] getOverlayFilters() {
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).a(ItemCategory.effect)) {
            if (!fVar.isHidden() && fVar.getType() == ItemType.renderitem) {
                arrayList.add(fVar.getId());
            }
        }
        int size = arrayList.size();
        nexOverlayFilter[] nexoverlayfilterArr = new nexOverlayFilter[size];
        for (int i = 0; i < size; i++) {
            nexoverlayfilterArr[i] = new nexOverlayFilter((String) arrayList.get(i));
        }
        return nexoverlayfilterArr;
    }

    public nexClipEffect findClipEffectById(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).c(str);
        if (c != null && c.getCategory() == ItemCategory.effect) {
            nexClipEffect nexclipeffect = new nexClipEffect(str);
            nexclipeffect.itemMethodType = nexAssetPackageManager.getMethodType(c.getType());
            return nexclipeffect;
        }
        return null;
    }

    public nexTransitionEffect findTransitionEffectById(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).c(str);
        if (c != null && c.getCategory() == ItemCategory.transition) {
            nexTransitionEffect nextransitioneffect = new nexTransitionEffect(str);
            nextransitioneffect.itemMethodType = nexAssetPackageManager.getMethodType(c.getType());
            return nextransitioneffect;
        }
        return null;
    }

    public nexOverlayFilter findOverlayFilterById(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).c(str);
        if (c != null && c.getType() == ItemType.renderitem) {
            return new nexOverlayFilter(str);
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x005d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.nexstreaming.nexeditorsdk.nexEffectOptions getEffectOptions(android.content.Context r17, java.lang.String r18) {
        /*
            Method dump skipped, instructions count: 417
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexEffectLibrary.getEffectOptions(android.content.Context, java.lang.String):com.nexstreaming.nexeditorsdk.nexEffectOptions");
    }

    public boolean checkEffectID(String str) {
        boolean a = com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).a(str, ItemCategory.transition);
        return !a ? com.nexstreaming.app.common.nexasset.assetpackage.c.a(this.mAppContext).a(str, ItemCategory.effect) : a;
    }

    @Deprecated
    public static String getPluginDirPath() {
        return EditorGlobal.g().getAbsolutePath();
    }
}
