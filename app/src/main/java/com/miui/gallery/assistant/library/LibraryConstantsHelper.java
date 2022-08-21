package com.miui.gallery.assistant.library;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyCheckHelper;
import com.miui.gallery.util.IDPhotoEntranceUtils;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class LibraryConstantsHelper {
    public static final String CURRENT_ABI;
    public static final Long[] sImageFeatureSelectionLibraries;
    public static final Long[] sImageProcessLibraries;
    public static final HashMap<Long, String> sLibraryDirMap;
    public static final Long[] sStoryLibraries;
    public static final Long[] sAnalyticFaceAndSceneSelectionLibraries = {3414L};
    public static final Long[] sPhotoMovieLibraries = {1018L};
    public static final Long[] sPhotoVlogLibraries = {101953L, 3414L, 34567L};
    public static final Long[] sInternationalPhotoVlogLibraries = {101953L, 34567L};
    public static final Long[] sSkyTransferLibraries = {Long.valueOf(SkyCheckHelper.getLibraryId())};
    public static final Long[] sAdjustLibraries = {20000012L};
    public static final Long[] sCropLibraries = {10000003L};
    public static final Long[] sPortraitColorLibraries = {1031L};
    public static final Long[] sVideoCompressLibraries = {1043L};
    public static final Long[] sRemoverProLibraries = {7000106L};
    public static final Long[] sAIModeScreenSceneLibraries = {10286L};
    public static final Long[] sFFmpegLibraries = {1038L};
    public static final Long[] sMapLibraries = {104702L};
    public static final Long[] sMagicMattingLibraries = {20010006L};
    public static final Long[] sIDPhotoLibraries = {Long.valueOf(IDPhotoEntranceUtils.getLibraryId())};
    public static final Long[] sArtStillLibraries = {20030006L};
    public static final Long[] sVideoPostLibraries = {20040004L};
    public static final Long[] sAllLibraries = LibraryUtils.getAllLibraries();

    static {
        Long[] lArr = {1002002L, 1004003L};
        sImageFeatureSelectionLibraries = lArr;
        Long[] lArr2 = {1002002L, 1004003L, 3414L};
        sStoryLibraries = lArr2;
        if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
            lArr = lArr2;
        } else if (!MediaFeatureManager.isImageFeatureCalculationEnable()) {
            lArr = new Long[0];
        }
        sImageProcessLibraries = lArr;
        CURRENT_ABI = LibraryUtils.getCurrentAbi();
        sLibraryDirMap = new HashMap<>();
    }

    public static File getSpecificDirForLibrary(long j) {
        String str = sLibraryDirMap.get(Long.valueOf(j));
        if (str == null) {
            str = "libs";
        }
        return GalleryApp.sGetAndroidContext().getDir(str, 0);
    }

    public static Set<File> getAllDirs() {
        HashSet hashSet = new HashSet();
        Long[] lArr = sAllLibraries;
        if (lArr != null && lArr.length > 0) {
            for (Long l : lArr) {
                hashSet.add(getSpecificDirForLibrary(l.longValue()));
            }
        }
        return hashSet;
    }

    public static Set<File> getAllDirsOfParentDir() {
        File specificDirForLibrary = getSpecificDirForLibrary(0L);
        HashSet hashSet = new HashSet();
        if (specificDirForLibrary.exists() && specificDirForLibrary.isDirectory()) {
            File[] listFiles = specificDirForLibrary.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.exists() && file.isDirectory()) {
                        hashSet.add(file);
                    }
                }
            }
            hashSet.add(specificDirForLibrary);
        }
        return hashSet;
    }
}
