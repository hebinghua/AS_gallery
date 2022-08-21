package com.miui.gallery.inject;

import com.miui.gallery.imodule.base.IModule;
import java.util.Map;
import javax.inject.Provider;

/* compiled from: ModuleEntryPoint.kt */
/* loaded from: classes2.dex */
public interface ModuleEntryPoint {
    Map<Class<? extends IModule>, Provider<Object>> getModuleMapping();
}
