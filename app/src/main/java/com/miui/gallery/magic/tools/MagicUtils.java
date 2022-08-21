package com.miui.gallery.magic.tools;

import android.content.Context;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MagicDependsModule;

/* loaded from: classes2.dex */
public class MagicUtils {
    public static Context getGalleryApp() {
        return ((MagicDependsModule) ModuleRegistry.getModule(MagicDependsModule.class)).getAndroidContext();
    }

    public static boolean is8KVideo(String str) {
        return ((MagicDependsModule) ModuleRegistry.getModule(MagicDependsModule.class)).is8KVideo(str);
    }

    public static void scanSingleFile(Context context, String str) {
        ((MagicDependsModule) ModuleRegistry.getModule(MagicDependsModule.class)).scanSingleFile(context, str);
    }

    public static String getFileProviderAuthority() {
        return ((MagicDependsModule) ModuleRegistry.getModule(MagicDependsModule.class)).getFileProviderAuthority();
    }
}
