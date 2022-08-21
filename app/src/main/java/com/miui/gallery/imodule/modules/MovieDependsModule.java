package com.miui.gallery.imodule.modules;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.imodule.base.IModule;

/* loaded from: classes2.dex */
public interface MovieDependsModule extends IModule {

    /* loaded from: classes2.dex */
    public interface Callback {
        default void onDialogCancel() {
        }

        default void onDialogConfirm() {
        }

        default void onInstallSuccess() {
        }
    }

    String getMovieLibraryPath();

    Class getPhotoPagerClass();

    Class getStoryPickClass();

    boolean installIfNotExist(FragmentActivity fragmentActivity, Callback callback, boolean z);

    boolean isDeviceSupportPhotoMovie();

    boolean isPhotoMovieAvailable();

    boolean isPhotoMovieUseMiSDK();

    void removeInstallListener();

    void scanSingleFile(Context context, String str);
}
