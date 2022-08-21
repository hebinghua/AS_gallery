package com.miui.gallery.util.uil;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.provider.GalleryContract;
import java.util.Arrays;
import java.util.Collections;

/* loaded from: classes2.dex */
public class ShortCutHelper {
    public static int getMaxShortcutCount(Context context) {
        return ShortcutManagerCompat.getMaxShortcutCountPerActivity(context);
    }

    public static void remove(Context context, String... strArr) {
        ShortcutManagerCompat.removeDynamicShortcuts(context, Arrays.asList(strArr));
    }

    public static void add(Context context, ShortcutInfoCompat shortcutInfoCompat) {
        if (ShortcutManagerCompat.getShortcuts(context, 2).size() + ShortcutManagerCompat.getShortcuts(context, 1).size() >= getMaxShortcutCount(context)) {
            return;
        }
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcutInfoCompat);
    }

    public static void addRecommendShortcut(Context context) {
        if (!MediaFeatureManager.isDeviceSupportStoryFunction() || isDynamicShortcutExist(context, "shortcut_recommend")) {
            return;
        }
        Intent intent = new Intent("android.intent.action.VIEW", GalleryContract.Common.URI_RECOMMEND_TAB);
        intent.setPackage(context.getPackageName());
        add(context, new ShortcutInfoCompat.Builder(context, "shortcut_recommend").setShortLabel(context.getString(R.string.recommend_page_label)).setIcon(IconCompat.createFromIcon(Icon.createWithResource(context, (int) R.drawable.action_shortcut_recommend))).setIntent(intent).build());
    }

    public static ShortcutInfoCompat getDynamicShortcut(Context context, String str) {
        for (ShortcutInfoCompat shortcutInfoCompat : ShortcutManagerCompat.getDynamicShortcuts(context)) {
            if (shortcutInfoCompat.getId().equals(str)) {
                return shortcutInfoCompat;
            }
        }
        return null;
    }

    public static boolean isDynamicShortcutExist(Context context, String str) {
        return getDynamicShortcut(context, str) != null;
    }

    public static void updateRecommendShortcut() {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (isDynamicShortcutExist(sGetAndroidContext, "shortcut_recommend")) {
            Intent intent = new Intent("android.intent.action.VIEW", GalleryContract.Common.URI_RECOMMEND_TAB);
            intent.setPackage(sGetAndroidContext.getPackageName());
            ShortcutManagerCompat.updateShortcuts(sGetAndroidContext, Collections.singletonList(new ShortcutInfoCompat.Builder(sGetAndroidContext, "shortcut_recommend").setShortLabel(sGetAndroidContext.getResources().getString(R.string.recommend_page_label)).setIcon(IconCompat.createFromIcon(Icon.createWithResource(sGetAndroidContext, (int) R.drawable.action_shortcut_recommend))).setIntent(intent).build()));
        }
    }

    public static void updateSlimShortcut() {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (isDynamicShortcutExist(sGetAndroidContext, "shortcut_slim")) {
            Intent intent = new Intent("android.intent.action.VIEW", GalleryContract.Common.URI_CLEANER_PAGE);
            intent.setPackage(sGetAndroidContext.getPackageName());
            intent.putExtra("extra_from_type", 1018);
            ShortcutInfoCompat build = new ShortcutInfoCompat.Builder(sGetAndroidContext, "shortcut_slim").setShortLabel(sGetAndroidContext.getResources().getString(R.string.cleaner_title)).setIcon(IconCompat.createFromIcon(Icon.createWithResource(sGetAndroidContext, (int) R.drawable.action_shortcut_slim))).setIntent(intent).build();
            add(sGetAndroidContext, build);
            ShortcutManagerCompat.updateShortcuts(sGetAndroidContext, Collections.singletonList(build));
        }
    }
}
