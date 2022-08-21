package com.miui.gallery.imodule.modules;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.imodule.base.IModule;

/* loaded from: classes2.dex */
public interface MagicDependsModule extends IModule {

    /* loaded from: classes2.dex */
    public interface Callback {
        default void onDialogCancel() {
        }

        default void onDialogConfirm() {
        }

        default void onInstallSuccess() {
        }
    }

    Context getAndroidContext();

    String getFileProviderAuthority();

    boolean installIfNotExist(FragmentActivity fragmentActivity, Callback callback, boolean z);

    boolean is8450();

    boolean is8KVideo(String str);

    boolean isInFreeFormWindow(Context context);

    void removeInstallListener();

    void scanSingleFile(Context context, String str);
}
