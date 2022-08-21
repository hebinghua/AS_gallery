package com.miui.gallery.stat;

import com.miui.gallery.imodule.base.IModule;
import kotlin.text.StringsKt__StringsJVMKt;

/* compiled from: StatsDependsImpl.kt */
/* loaded from: classes2.dex */
public final class StatsDependsImpl implements StatsDependsModule, IModule {
    @Override // com.miui.gallery.stat.StatsDependsModule
    public boolean isGlobalBuild() {
        return StringsKt__StringsJVMKt.equals("global", "cn", true);
    }
}
