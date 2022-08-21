package com.miui.gallery.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.text.TextUtils;
import com.android.internal.SystemPropertiesCompat;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class ShortcutUtil {
    public static void createShortcut(Context context, String str, String str2, Bitmap bitmap, int i, Intent intent) {
        Icon createWithResource;
        if (Build.VERSION.SDK_INT < 26) {
            Intent intent2 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            intent2.setPackage(SystemPropertiesCompat.get("ro.miui.product.home", "com.miui.home"));
            intent2.putExtra("duplicate", false);
            intent2.putExtra("android.intent.extra.shortcut.NAME", str2);
            if (bitmap != null) {
                intent2.putExtra("android.intent.extra.shortcut.ICON", bitmap);
            } else {
                intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(context, i));
            }
            intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
            context.sendBroadcast(intent2);
            return;
        }
        try {
            ShortcutManager shortcutManager = (ShortcutManager) context.getApplicationContext().getSystemService(ShortcutManager.class);
            if (!shortcutManager.isRequestPinShortcutSupported()) {
                return;
            }
            List<ShortcutInfo> pinnedShortcuts = shortcutManager.getPinnedShortcuts();
            if (BaseMiscUtil.isValid(pinnedShortcuts)) {
                for (ShortcutInfo shortcutInfo : pinnedShortcuts) {
                    if (TextUtils.equals(shortcutInfo.getId(), str)) {
                        return;
                    }
                }
            }
            if (bitmap != null) {
                createWithResource = Icon.createWithBitmap(bitmap);
            } else {
                createWithResource = Icon.createWithResource(context, i);
            }
            shortcutManager.requestPinShortcut(new ShortcutInfo.Builder(context, str).setIcon(createWithResource).setShortLabel(str2).setIntent(intent).build(), null);
        } catch (Exception e) {
            DefaultLogger.e("ShortcutUtil", "createShortcut error.\n", e);
        }
    }
}
