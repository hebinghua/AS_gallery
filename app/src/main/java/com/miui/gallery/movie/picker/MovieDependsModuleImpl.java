package com.miui.gallery.movie.picker;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.activity.ExternalPhotoPageActivity;
import com.miui.gallery.imodule.base.IModule;
import com.miui.gallery.imodule.modules.MovieDependsModule;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.util.MovieLibraryLoaderHelper;
import com.miui.gallery.util.PhotoMovieEntranceUtils;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.mediaeditor.api.MediaEditorApiHelper;

/* loaded from: classes2.dex */
public class MovieDependsModuleImpl implements MovieDependsModule, IModule {
    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public Class getStoryPickClass() {
        return MovieStoryPickActivity.class;
    }

    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public Class getPhotoPagerClass() {
        return ExternalPhotoPageActivity.class;
    }

    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public String getMovieLibraryPath() {
        return MovieLibraryLoaderHelper.getInstance().getLibraryDirPath();
    }

    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public void scanSingleFile(Context context, String str) {
        ScannerEngine.getInstance().scanFile(context, str, 13);
    }

    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public boolean isPhotoMovieAvailable() {
        return MediaEditorApiHelper.isPhotoMovieAvailable();
    }

    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public boolean isPhotoMovieUseMiSDK() {
        return PhotoMovieEntranceUtils.isPhotoMovieUseMiSDK();
    }

    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public boolean installIfNotExist(FragmentActivity fragmentActivity, final MovieDependsModule.Callback callback, boolean z) {
        return MediaEditorInstaller.getInstance().installIfNotExist(fragmentActivity, callback != null ? new MediaEditorInstaller.Callback() { // from class: com.miui.gallery.movie.picker.MovieDependsModuleImpl.1
            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onDialogConfirm() {
                MovieDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onDialogConfirm();
                }
            }

            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onDialogCancel() {
                MovieDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onDialogCancel();
                }
            }

            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onInstallSuccess() {
                MovieDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onInstallSuccess();
                }
            }
        } : null, z);
    }

    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public void removeInstallListener() {
        MediaEditorInstaller.getInstance().removeInstallListener();
    }

    @Override // com.miui.gallery.imodule.modules.MovieDependsModule
    public boolean isDeviceSupportPhotoMovie() {
        return MediaEditorApiHelper.isDeviceSupportPhotoMovie();
    }
}
