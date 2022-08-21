package com.miui.gallery.util;

import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.android.internal.storage.StorageInfo;
import com.android.internal.storage.StorageManager;
import com.miui.gallery.base.R$bool;
import com.miui.gallery.base.R$string;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.deprecated.Storage;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.xspace.XSpaceHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class StorageUtils {
    public static final LazyValue<Void, String> sPrimaryStoragePathCache = new LazyValue<Void, String>() { // from class: com.miui.gallery.util.StorageUtils.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public String mo1272onInit(Void r1) {
            return Storage.getPrimaryStorageRoot();
        }
    };
    public static final LazyValue<Void, String> sSecondaryStoragePathCache = new LazyValue<Void, String>() { // from class: com.miui.gallery.util.StorageUtils.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public String mo1272onInit(Void r1) {
            return Storage.getExternalSDCardRoot();
        }
    };
    public static final LazyValue<Void, File> sNetworkCacheDirectoryCache = new LazyValue<Void, File>() { // from class: com.miui.gallery.util.StorageUtils.3
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public File mo1272onInit(Void r2) {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                return new File(StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/request"));
            }
            return new File(StorageConstants.ABSOLUTE_DIRECTORY_NETWORK_CACHE_INTERNAL);
        }
    };
    public static final LazyValue<Context, List<StorageInfo>> sVolumesCache = new LazyValue<Context, List<StorageInfo>>() { // from class: com.miui.gallery.util.StorageUtils.4
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public List<StorageInfo> mo1272onInit(Context context) {
            List<StorageInfo> storageInfos = StorageManager.getInstance().getStorageInfos(context);
            StorageInfo xSpaceStorageInfo = StorageUtils.getXSpaceStorageInfo(context);
            if (xSpaceStorageInfo != null) {
                storageInfos.add(xSpaceStorageInfo);
            }
            context.getExternalFilesDirs("mounted");
            context.getExternalCacheDirs();
            return storageInfos;
        }
    };

    public static boolean isUsingSecondaryStorage(Context context) {
        return hasExternalSDCard(context) && isExternalSDCardPriorStorage();
    }

    public static String getPriorStoragePath() {
        if (isUsingSecondaryStorage(StaticContext.sGetAndroidContext())) {
            return getSecondaryStoragePath();
        }
        return getPrimaryStoragePath();
    }

    public static String getPathInPriorStorage(String str) {
        return getFilePathUnder(getPriorStoragePath(), ensureCommonRelativePath(str));
    }

    public static boolean isExternalSDCardPriorStorage() {
        Context sGetAndroidContext = StaticContext.sGetAndroidContext();
        int componentEnabledSetting = sGetAndroidContext.getPackageManager().getComponentEnabledSetting(new ComponentName(sGetAndroidContext, PriorityStorageBroadcastReceiver.class));
        if (componentEnabledSetting == 0) {
            return sGetAndroidContext.getResources().getBoolean(R$bool.priority_storage);
        }
        return componentEnabledSetting == 1;
    }

    public static String getPrimaryStoragePath() {
        return sPrimaryStoragePathCache.get(null);
    }

    public static boolean isPrimaryStorageReadable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(getPrimaryStoragePath(), IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("StorageUtils", "isPrimaryStorageReadable"));
            return documentFile != null && documentFile.exists() && documentFile.canRead();
        }
        return false;
    }

    public static boolean isInPrimaryStorage(String str) {
        return !TextUtils.isEmpty(str) && BaseFileUtils.contains(getPrimaryStoragePath(), str);
    }

    public static String getPathInPrimaryStorage(String str) {
        return getFilePathUnder(getPrimaryStoragePath(), str);
    }

    public static String getSecondaryStoragePath() {
        return sSecondaryStoragePathCache.get(null);
    }

    public static boolean isInSecondaryStorage(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String secondaryStoragePath = getSecondaryStoragePath();
        if (!TextUtils.isEmpty(secondaryStoragePath)) {
            return BaseFileUtils.contains(secondaryStoragePath, str);
        }
        return false;
    }

    public static String getPathInSecondaryStorage(String str) {
        return getFilePathUnder(getSecondaryStoragePath(), str);
    }

    public static boolean hasExternalSDCard(Context context) {
        for (StorageInfo storageInfo : StorageManager.getInstance().getStorageInfos(context)) {
            if (storageInfo.isMounted() && storageInfo.isSd()) {
                return true;
            }
        }
        return false;
    }

    public static List<StorageInfo> getVolumes(Context context) {
        return sVolumesCache.get(context);
    }

    public static List<String> getMountedVolumePaths(Context context) {
        List<StorageInfo> volumes = getVolumes(context);
        ArrayList arrayList = new ArrayList(volumes.size());
        for (StorageInfo storageInfo : volumes) {
            if (storageInfo.isMounted() && !TextUtils.isEmpty(storageInfo.getPath())) {
                arrayList.add(storageInfo.getPath());
            }
        }
        return arrayList;
    }

    public static String[] getPathsInExternalStorage(Context context, String str) {
        if (context == null) {
            return null;
        }
        List<String> mountedVolumePaths = getMountedVolumePaths(context);
        int size = mountedVolumePaths.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = getFilePathUnder(mountedVolumePaths.get(i), str);
        }
        return strArr;
    }

    public static String getVolumePath(Context context, String str) {
        if (context != null && isAbsolutePath(str)) {
            for (String str2 : getMountedVolumePaths(context)) {
                if (BaseFileUtils.contains(str2, str)) {
                    return str2;
                }
            }
        }
        return null;
    }

    public static String getMediaStoreVolumeName(Context context, String str) {
        String volumeName;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (Build.VERSION.SDK_INT >= 29 && isInSecondaryStorage(str) && (volumeName = getVolumeName(context, str, true)) != null) ? volumeName : "external";
    }

    public static String getVolumeName(Context context, String str, boolean z) {
        for (StorageInfo storageInfo : getVolumes(context)) {
            if (storageInfo.isMounted() && !TextUtils.isEmpty(storageInfo.getPath()) && BaseFileUtils.contains(storageInfo.getPath(), str)) {
                String uUid = storageInfo.getUUid();
                if (z) {
                    uUid = normalizeUuid(uUid);
                }
                return checkArgumentVolumeName(uUid);
            }
        }
        return null;
    }

    public static StorageInfo getStorageInfo(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str) || !isAbsolutePath(str)) {
            return null;
        }
        for (StorageInfo storageInfo : sVolumesCache.get(context)) {
            if (BaseFileUtils.contains(storageInfo.getPath(), str)) {
                return storageInfo;
            }
        }
        return null;
    }

    public static String normalizeUuid(String str) {
        if (str != null) {
            return str.toLowerCase(Locale.US);
        }
        return null;
    }

    public static String checkArgumentVolumeName(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if ("internal".equals(str) || "external".equals(str) || "external_primary".equals(str)) {
            return str;
        }
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (('a' > charAt || charAt > 'f') && (('A' > charAt || charAt > 'F') && (('0' > charAt || charAt > '9') && charAt != '-'))) {
                throw new IllegalArgumentException("Invalid volume name: " + str);
            }
        }
        return str;
    }

    public static boolean isInExternalStorage(Context context, String str) {
        if (!isAbsolutePath(str)) {
            return false;
        }
        for (String str2 : getMountedVolumePaths(context)) {
            if (BaseFileUtils.contains(str2, str)) {
                return true;
            }
        }
        return false;
    }

    public static String getPathForDisplay(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            Iterator<StorageInfo> it = getVolumes(context).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                StorageInfo next = it.next();
                if (BaseFileUtils.contains(next.getPath(), str)) {
                    String description = next.getDescription();
                    if (next.isPrimary()) {
                        description = context.getString(R$string.storage_description_primary);
                    } else if (next.isSd()) {
                        description = context.getString(R$string.storage_description_sdcard);
                    } else if (next.isXspace()) {
                        description = context.getString(R$string.storage_description_xspace);
                    } else if (next.isUsb()) {
                        description = context.getString(R$string.storage_description_usb);
                    }
                    if (!TextUtils.isEmpty(description)) {
                        return String.format(Locale.US, "%s%s", description, str.substring(next.getPath().length()));
                    }
                }
            }
        }
        return str == null ? "" : str;
    }

    public static String[] getMicroThumbnailDirectories(Context context) {
        return getPathsInExternalStorage(context, "/Android/data/com.miui.gallery/cache/microthumbnailFile");
    }

    public static String getSafePriorMicroThumbnailPath() {
        return getPathInPriorStorage("/Android/data/com.miui.gallery/cache/microthumbnailFile");
    }

    public static String getSafePriorMicroThumbnailPath(String str) {
        if (str != null && !str.endsWith(".jpg")) {
            str = str + ".jpg";
        }
        return getPathInPriorStorage("/Android/data/com.miui.gallery/cache/microthumbnailFile" + File.separator + str);
    }

    public static String getSafePriorOriginTempDirectory() {
        return getPathInPriorStorage("/Android/data/com.miui.gallery/cache/downloadFile");
    }

    public static String getShareTempDirectory() {
        return getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/securityShareTemp");
    }

    public static File getNetworkCacheDirectory() {
        return sNetworkCacheDirectoryCache.get(null);
    }

    public static String getRelativePath(Context context, String str) {
        return getRelativePath(context, str, true);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [boolean] */
    public static String getRelativePath(Context context, String str, boolean z) {
        if (context != null && !TextUtils.isEmpty(str)) {
            if (!isAbsolutePath(str)) {
                return str;
            }
            for (String str2 : getMountedVolumePaths(context)) {
                if (BaseFileUtils.contains(str2, str)) {
                    if (str2.length() < str.length()) {
                        String substring = str.substring(str2.length());
                        String str3 = File.separator;
                        ?? startsWith = substring.startsWith(str3);
                        int length = substring.endsWith(str3) ? substring.length() - 1 : substring.length();
                        if (startsWith < length) {
                            return substring.substring(startsWith == true ? 1 : 0, length);
                        }
                    }
                    return z ? GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH : "";
                }
            }
        }
        return null;
    }

    public static String ensureCommonRelativePath(String str) {
        return (!GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH.equals(str) && str != null) ? str : "";
    }

    public static String[] getAbsolutePath(Context context, String str) {
        if (isAbsolutePath(str)) {
            return new String[]{str};
        }
        if (context != null) {
            return getPathsInExternalStorage(context, str);
        }
        return null;
    }

    public static String getFilePathUnder(String str, String str2) {
        if (str == null) {
            return null;
        }
        if (str2 == null) {
            str2 = "";
        }
        return BaseFileUtils.concat(str, str2);
    }

    public static long getTotalBytes(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return new StatFs(str).getTotalBytes();
            } catch (Exception e) {
                DefaultLogger.e("StorageUtils", String.format(Locale.US, "Failed to get total bytes [%s]", e));
                return 0L;
            }
        }
        return 0L;
    }

    public static long getAvailableBytes(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return new StatFs(str).getAvailableBytes();
            } catch (Exception e) {
                DefaultLogger.e("StorageUtils", String.format(Locale.US, "Failed to get available bytes [%s]", e));
                return 0L;
            }
        }
        return 0L;
    }

    public static StorageInfo getXSpaceStorageInfo(Context context) {
        File xSpacePath;
        if (!XSpaceHelper.isXSpaceEnable(context) || (xSpacePath = XSpaceHelper.getXSpacePath()) == null || !xSpacePath.exists() || !xSpacePath.canRead()) {
            return null;
        }
        StorageInfo.Builder builder = new StorageInfo.Builder(xSpacePath.getAbsolutePath());
        builder.setXspace(true);
        builder.setMounted(true);
        return builder.build();
    }

    public static boolean isAbsolutePath(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(File.separator);
    }

    public static long getLastModifiedByRelativePath(Context context, String str) {
        if (str == null) {
            return -1L;
        }
        String pathInPrimaryStorage = getPathInPrimaryStorage(ensureCommonRelativePath(str));
        if (TextUtils.isEmpty(pathInPrimaryStorage)) {
            return 0L;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("StorageUtils", "getLastModifiedByRelativePath"));
        if (documentFile != null) {
            return documentFile.lastModified();
        }
        return 0L;
    }

    public static void clearCache() {
        sPrimaryStoragePathCache.reset();
        sSecondaryStoragePathCache.reset();
        sNetworkCacheDirectoryCache.reset();
        sVolumesCache.reset();
        com.miui.gallery.storage.utils.Utils.APP_SPECIFIC_EXTERNAL_DIRECTORY.reset();
        com.miui.gallery.storage.utils.Utils.OTHER_APP_SPECIFIC_EXTERNAL_DIRECTORY.reset();
    }

    public static List<String> getForbiddenRenameDirectory() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM);
        arrayList.add("/Android/data/com.miui.gallery/files/trashBin");
        arrayList.add("MIUI/Gallery/cloud/secretAlbum");
        return arrayList;
    }

    public static boolean isSupportRename(String str) {
        for (String str2 : getForbiddenRenameDirectory()) {
            if (str.toLowerCase().contains(str2.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
