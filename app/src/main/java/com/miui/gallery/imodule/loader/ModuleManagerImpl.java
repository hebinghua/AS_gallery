package com.miui.gallery.imodule.loader;

import com.miui.gallery.imodule.base.IModule;
import com.miui.gallery.imodule.base.IModuleManager;
import com.miui.gallery.inject.ModuleEntryPoint;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import dagger.hilt.android.EntryPointAccessors;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Provider;

/* loaded from: classes2.dex */
public class ModuleManagerImpl implements IModuleManager {
    public static final IModuleManager mModuleManager = new ModuleManagerImpl();
    public final Map<Class<? extends IModule>, Provider<Object>> mStaticModuleRegistry = ((ModuleEntryPoint) EntryPointAccessors.fromApplication(StaticContext.sGetAndroidContext(), ModuleEntryPoint.class)).getModuleMapping();
    public final Map<Class<? extends IModule>, Object> mDynamicModuleRegistry = new HashMap();

    public static IModuleManager getModuleManager() {
        return mModuleManager;
    }

    @Override // com.miui.gallery.imodule.base.IModuleManager
    public IModule getModule(Class<? extends IModule> cls) {
        IModule iModule = (IModule) this.mDynamicModuleRegistry.get(cls);
        if (iModule != null) {
            return iModule;
        }
        Provider<Object> provider = this.mStaticModuleRegistry.get(cls);
        if (provider != null) {
            return (IModule) provider.get();
        }
        DefaultLogger.w("ModuleManagerImpl", "No module impl registered with %s", cls.getCanonicalName());
        return null;
    }
}
