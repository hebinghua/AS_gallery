package com.baidu.mapsdkplatform.comapi;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: classes.dex */
class d implements FilenameFilter {
    public final /* synthetic */ String a;
    public final /* synthetic */ NativeLoader b;

    public d(NativeLoader nativeLoader, String str) {
        this.b = nativeLoader;
        this.a = str;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str != null && str.contains("libBaiduMapSDK_") && !str.contains(this.a);
    }
}
