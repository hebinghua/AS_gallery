package com.miui.gallery.vlog;

import android.content.Context;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.activity.ExternalPhotoPageActivity;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.imodule.base.IModule;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.gallery.video.editor.util.FileHelper;
import com.miui.gallery.vlog.rule.MatchedTemplate;
import com.miui.gallery.vlog.rule.TemplateMatcher;
import com.miui.gallery.vlog.rule.TemplateMatcherAlg;
import com.miui.gallery.vlog.rule.TemplateMatcherNoAlg;
import java.io.File;
import java.util.List;

/* loaded from: classes2.dex */
public class VlogDependsModuleImpl implements VlogDependsModule, IModule {
    public volatile TemplateMatcher mTemplateMatcher;

    public VlogDependsModuleImpl(Context context) {
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public Context GetAndroidContext() {
        return GalleryApp.sGetAndroidContext();
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public String getLibraryPath() {
        return LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext());
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public boolean loadAiCaptionLibrary() {
        return AiCaptionLibraryHelper.checkAndLoad();
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public boolean isAiCaptionLibraryExist() {
        return AiCaptionLibraryHelper.isExist();
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public void scanSingleFile(Context context, String str) {
        ScannerEngine.getInstance().scanFile(context, str, 13);
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public boolean is960FpsVideo(String str) {
        return (SpecialTypeMediaUtils.parseFlagsForVideo(str) & 16) != 0;
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public String generateOutputFilePath(File file) {
        return FileHelper.generateOutputFilePath(file);
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public Uri translateToContent(String str) {
        return GalleryOpenProvider.translateToContent(str);
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public Class getPhotoPagerClass() {
        return ExternalPhotoPageActivity.class;
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public <T> List<T> matchToTemplate(String str, List<String> list) {
        TemplateMatcher templateMatcher = this.mTemplateMatcher;
        if (templateMatcher == null) {
            templateMatcher = BaseBuildUtil.isInternational() ? new TemplateMatcherNoAlg(GetAndroidContext().getAssets()) : new TemplateMatcherAlg(GetAndroidContext().getAssets());
        }
        MatchedTemplate matchToTemplateFromDB = templateMatcher.matchToTemplateFromDB(str, list);
        this.mTemplateMatcher = templateMatcher;
        if (matchToTemplateFromDB == null) {
            return null;
        }
        return (List<T>) matchToTemplateFromDB.mMatchClips;
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public long[] addToFavorite(Context context, String str) {
        return CloudUtils.addToFavoritesByPath(context, str);
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public void removeInstallListener() {
        MediaEditorInstaller.getInstance().removeInstallListener();
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public boolean installIfNotExist(FragmentActivity fragmentActivity, final VlogDependsModule.Callback callback, boolean z) {
        return MediaEditorInstaller.getInstance().installIfNotExist(fragmentActivity, callback != null ? new MediaEditorInstaller.Callback() { // from class: com.miui.gallery.vlog.VlogDependsModuleImpl.1
            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onDialogConfirm() {
                VlogDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onDialogConfirm();
                }
            }

            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onDialogCancel() {
                VlogDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onDialogCancel();
                }
            }

            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onInstallSuccess() {
                VlogDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onInstallSuccess();
                }
            }
        } : null, z);
    }

    @Override // com.miui.gallery.imodule.modules.VlogDependsModule
    public void release() {
        if (this.mTemplateMatcher != null) {
            this.mTemplateMatcher.release();
        }
        this.mTemplateMatcher = null;
    }
}
