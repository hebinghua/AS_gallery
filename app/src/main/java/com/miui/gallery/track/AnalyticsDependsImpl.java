package com.miui.gallery.track;

import com.miui.gallery.analytics.AnalyticsDependsModule;
import com.miui.gallery.imodule.base.IModule;
import kotlin.text.StringsKt__StringsJVMKt;

/* compiled from: AnalyticsDependsImpl.kt */
/* loaded from: classes2.dex */
public final class AnalyticsDependsImpl implements AnalyticsDependsModule, IModule {
    @Override // com.miui.gallery.analytics.AnalyticsDependsModule
    public boolean isGlobalBuild() {
        return StringsKt__StringsJVMKt.equals("global", "cn", true);
    }
}
