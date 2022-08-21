package com.miui.preload;

import android.content.Context;
import android.os.Bundle;
import com.miui.internal.PreloadedAppCompat;

/* loaded from: classes3.dex */
public class PreloadedAppHelper {

    /* loaded from: classes3.dex */
    public interface PackageInstallObserver {
        void onPackageInstalled(String str, int i, String str2, Bundle bundle);
    }

    public static boolean installPreloadedDataApp(Context context, String str, PackageInstallObserver packageInstallObserver, int i) {
        return PreloadedAppCompat.installPreloadedDataApp(context, str, new InstallCallBack(packageInstallObserver), i);
    }

    /* loaded from: classes3.dex */
    public static class InstallCallBack implements PreloadedAppCompat.PackageInstallObserver {
        public final PackageInstallObserver mObserver;

        public InstallCallBack(PackageInstallObserver packageInstallObserver) {
            this.mObserver = packageInstallObserver;
        }

        @Override // com.miui.internal.PreloadedAppCompat.PackageInstallObserver
        public void onPackageInstalled(String str, int i, String str2, Bundle bundle) {
            PackageInstallObserver packageInstallObserver = this.mObserver;
            if (packageInstallObserver != null) {
                packageInstallObserver.onPackageInstalled(str, i, str2, bundle);
            }
        }
    }
}
