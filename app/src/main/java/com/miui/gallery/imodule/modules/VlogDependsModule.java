package com.miui.gallery.imodule.modules;

import android.content.Context;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.imodule.base.IModule;
import java.io.File;
import java.util.List;

/* loaded from: classes2.dex */
public interface VlogDependsModule extends IModule {

    /* loaded from: classes2.dex */
    public interface Callback {
        default void onDialogCancel() {
        }

        default void onDialogConfirm() {
        }

        default void onInstallSuccess() {
        }
    }

    Context GetAndroidContext();

    long[] addToFavorite(Context context, String str);

    String generateOutputFilePath(File file);

    String getLibraryPath();

    Class getPhotoPagerClass();

    boolean installIfNotExist(FragmentActivity fragmentActivity, Callback callback, boolean z);

    boolean is960FpsVideo(String str);

    boolean isAiCaptionLibraryExist();

    boolean loadAiCaptionLibrary();

    <T> List<T> matchToTemplate(String str, List<String> list);

    void release();

    void removeInstallListener();

    void scanSingleFile(Context context, String str);

    Uri translateToContent(String str);
}
