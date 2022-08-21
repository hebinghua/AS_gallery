package com.miui.gallery.imodule.loader;

import com.miui.gallery.imodule.base.IModule;
import com.miui.gallery.imodule.base.IModuleManager;

/* loaded from: classes2.dex */
public class ModuleRegistry {
    public static final IModuleManager moduleManager = ModuleManagerImpl.getModuleManager();

    public static <T extends IModule> T getModule(Class<? extends IModule> cls) {
        return (T) moduleManager.getModule(cls);
    }
}
