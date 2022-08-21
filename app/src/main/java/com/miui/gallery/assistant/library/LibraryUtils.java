package com.miui.gallery.assistant.library;

import android.content.Context;
import android.os.Build;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorCheckHelper;
import com.miui.gallery.editor.photo.app.remover2.sdk.Remover2CheckHelper;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyCheckHelper;
import com.miui.gallery.glide.load.resource.bitmap.FFmpegVideoDecoder;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.net.download.Verifier;
import com.miui.gallery.util.ArtStillEntranceUtils;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.IDPhotoEntranceUtils;
import com.miui.gallery.util.MagicMattingEntranceUtils;
import com.miui.gallery.util.PhotoMovieEntranceUtils;
import com.miui.gallery.util.ProcessUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.VideoPostEntranceUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.compress.VideoCompressCheckHelper;
import com.miui.gallery.vlog.VlogEntranceUtils;
import java.io.File;

/* loaded from: classes.dex */
public class LibraryUtils {
    public static Long[] getAllLibraries() {
        String currentProcessName = ProcessUtils.currentProcessName();
        DefaultLogger.d("LibraryUtils", "process: %s", currentProcessName);
        if ("com.miui.gallery:widgetProvider".equals(currentProcessName)) {
            return getAllLibrariesForWidget();
        }
        Long[] lArr = new Long[0];
        if (MapInitializerImpl.checkMapAvailable()) {
            lArr = concat(lArr, LibraryConstantsHelper.sMapLibraries);
        }
        if (!PhotoMovieEntranceUtils.isPhotoMovieUseMiSDK()) {
            lArr = concat(lArr, LibraryConstantsHelper.sPhotoMovieLibraries);
        }
        if (VlogEntranceUtils.isAvailable()) {
            if (BaseBuildUtil.isInternational()) {
                lArr = concat(lArr, LibraryConstantsHelper.sInternationalPhotoVlogLibraries);
            } else {
                lArr = concat(lArr, LibraryConstantsHelper.sPhotoVlogLibraries);
            }
        }
        if (SkyCheckHelper.isSkyEnable()) {
            lArr = concat(lArr, LibraryConstantsHelper.sSkyTransferLibraries);
        }
        Long[] concat = concat(concat(lArr, LibraryConstantsHelper.sAdjustLibraries), LibraryConstantsHelper.sCropLibraries);
        if (PortraitColorCheckHelper.isPortraitEnable()) {
            concat = concat(concat, LibraryConstantsHelper.sPortraitColorLibraries);
        }
        if (MediaFeatureManager.isStoryGenerateEnable()) {
            concat = concat(concat, LibraryConstantsHelper.sStoryLibraries);
        } else if (MediaFeatureManager.isImageFeatureCalculationEnable()) {
            concat = concat(concat, LibraryConstantsHelper.sImageFeatureSelectionLibraries);
        }
        if (VideoCompressCheckHelper.isDeviceSupport()) {
            concat = concat(concat, LibraryConstantsHelper.sVideoCompressLibraries);
        }
        if (ScreenUtils.isDeviceSupportAIMode()) {
            concat = concat(concat, LibraryConstantsHelper.sAIModeScreenSceneLibraries);
        }
        if (Remover2CheckHelper.isRemover2Support()) {
            concat = concat(concat, LibraryConstantsHelper.sRemoverProLibraries);
        }
        if (MagicMattingEntranceUtils.isAvailable()) {
            concat = concat(concat, LibraryConstantsHelper.sMagicMattingLibraries);
        }
        if (IDPhotoEntranceUtils.isAvailable()) {
            concat = concat(concat, LibraryConstantsHelper.sIDPhotoLibraries);
        }
        if (ArtStillEntranceUtils.isAvailable()) {
            concat = concat(concat, LibraryConstantsHelper.sArtStillLibraries);
        }
        if (VideoPostEntranceUtils.isAvailable()) {
            concat = concat(concat, LibraryConstantsHelper.sVideoPostLibraries);
        }
        return FFmpegVideoDecoder.sFFmpegSupport ? concat(concat, LibraryConstantsHelper.sFFmpegLibraries) : concat;
    }

    public static Long[] getAllLibrariesForWidget() {
        if (MediaFeatureManager.isStoryGenerateEnable()) {
            return LibraryConstantsHelper.sStoryLibraries;
        }
        if (!MediaFeatureManager.isImageFeatureCalculationEnable()) {
            return null;
        }
        return LibraryConstantsHelper.sImageFeatureSelectionLibraries;
    }

    public static String getCurrentAbi() {
        String[] strArr = Build.SUPPORTED_ABIS;
        String[] strArr2 = Build.SUPPORTED_64_BIT_ABIS;
        if (strArr2 != null && strArr2.length > 0) {
            return strArr2[0];
        }
        return strArr[0];
    }

    public static Long[] concat(Long[] lArr, Long[] lArr2) {
        if (lArr == null || lArr.length == 0) {
            return lArr2;
        }
        if (lArr2 == null || lArr2.length == 0) {
            return lArr;
        }
        Long[] lArr3 = new Long[lArr.length + lArr2.length];
        System.arraycopy(lArr, 0, lArr3, 0, lArr.length);
        System.arraycopy(lArr2, 0, lArr3, lArr.length, lArr2.length);
        return lArr3;
    }

    public static String getLibraryDirPath(Context context) {
        return LibraryConstantsHelper.getSpecificDirForLibrary(0L).getAbsolutePath();
    }

    public static String getLibraryDirPath(Context context, long j) {
        return LibraryConstantsHelper.getSpecificDirForLibrary(j).getAbsolutePath();
    }

    public static boolean isLibraryItemExist(Context context, long j, LibraryItem libraryItem) {
        if (context == null || libraryItem == null) {
            return false;
        }
        File file = new File(libraryItem.getTargetPath(context, j));
        return file.exists() && new Verifier.Sha1(libraryItem.getSha1()).verify(FileUtils.getFileSha1(file.getAbsolutePath()));
    }
}
