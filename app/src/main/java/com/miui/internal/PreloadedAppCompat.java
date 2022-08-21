package com.miui.internal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver2;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import java.util.ArrayList;
import miui.content.pm.PreloadedAppPolicy;
import miui.util.ReflectionUtils;

/* loaded from: classes3.dex */
public class PreloadedAppCompat {
    public static final int INSTALL_FLAG_NEED_CONFIRM = 1;
    public static final int INSTALL_FLAG_SHOW_TOAST = 2;
    private static final String TAG = "PreloadedAppCompat";

    /* loaded from: classes3.dex */
    public interface PackageInstallObserver {
        void onPackageInstalled(String str, int i, String str2, Bundle bundle);
    }

    public static boolean installPreloadedDataApp(Context context, String str, PackageInstallObserver packageInstallObserver, int i) {
        try {
            return PreloadedAppPolicy.installPreloadedDataApp(context, str, new InstallCallBack(packageInstallObserver), i);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    private static boolean ensureWhiteList(String str) {
        try {
            return ((ArrayList) ReflectionUtils.getStaticObjectField(PreloadedAppPolicy.class, "sProtectedDataApps", ArrayList.class)).add(str);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    /* loaded from: classes3.dex */
    public static final class InstallCallBack extends IPackageInstallObserver2.Stub {
        private final PackageInstallObserver mObserver;

        public void onUserActionRequired(Intent intent) throws RemoteException {
        }

        public InstallCallBack(PackageInstallObserver packageInstallObserver) {
            this.mObserver = packageInstallObserver;
        }

        public void onPackageInstalled(final String str, final int i, final String str2, final Bundle bundle) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.internal.PreloadedAppCompat.InstallCallBack.1
                @Override // java.lang.Runnable
                public void run() {
                    if (InstallCallBack.this.mObserver != null) {
                        InstallCallBack.this.mObserver.onPackageInstalled(str, i, str2, bundle);
                    }
                }
            });
        }
    }
}
