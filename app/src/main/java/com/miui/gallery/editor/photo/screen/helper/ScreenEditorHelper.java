package com.miui.gallery.editor.photo.screen.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.editor.photo.sdk.CleanScheduler;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StorageUtils;
import java.io.File;

/* loaded from: classes2.dex */
public class ScreenEditorHelper {
    public static final String DEPARECATED_TEMP_SHARE_SCREEN_EDITOR_PATH = StorageUtils.getPathInPrimaryStorage(BaseFileUtils.concat("/Android/data/com.miui.gallery/cache/screenEditorTemp", ".delete_screen_cache"));
    public static final String TEMP_SHARE_SCREEN_EDITOR_PATH = BaseFileUtils.concat(StorageUtils.getShareTempDirectory(), ".delete_screen_cache");

    public static String copyScreenFileToCache(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        File file = new File(str);
        if (!file.exists()) {
            return "";
        }
        String name = file.getName();
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("ScreenEditorHelper", "copyScreenFileToCache");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String str2 = TEMP_SHARE_SCREEN_EDITOR_PATH;
        if (storageStrategyManager.getDocumentFile(str2, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag) == null) {
            return str;
        }
        File file2 = new File(BaseFileUtils.concat(str2, name));
        if (!StorageSolutionProvider.get().copyFile(str, file2.getAbsolutePath(), appendInvokerTag)) {
            return "";
        }
        scheduleClean();
        return file2.getAbsolutePath();
    }

    public static void scheduleClean() {
        CleanScheduler.schedule(StaticContext.sGetAndroidContext(), "ScreenEditorHelper#clean", BaseFileUtils.concat(GalleryApp.sGetAndroidContext().getExternalCacheDir().getAbsolutePath(), ".delete_screen_cache"), BaseFileUtils.concat(GalleryApp.sGetAndroidContext().getCacheDir().getAbsolutePath(), ".delete_screen_cache"), StorageUtils.getPathInPrimaryStorage(DEPARECATED_TEMP_SHARE_SCREEN_EDITOR_PATH), StorageUtils.getShareTempDirectory());
    }

    public static void updateLocalDBNotShowInRecycleBin(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String str2 = (String) GalleryUtils.safeQuery("cloud", new String[]{"serverId"}, "localFile= ?", new String[]{str}, (String) null, ScreenEditorHelper$$ExternalSyntheticLambda0.INSTANCE);
        ContentValues contentValues = new ContentValues();
        if (TextUtils.isEmpty(str2)) {
            contentValues.put("serverStatus", (Integer) 15);
        } else if ("query_string_not_found_tag".equals(str2)) {
            return;
        } else {
            contentValues.put("serverStatus", "toBePurged");
        }
        GalleryUtils.safeUpdate(GalleryCloudUtils.CLOUD_URI, contentValues, "localFile= ?", new String[]{str});
    }

    public static /* synthetic */ String lambda$updateLocalDBNotShowInRecycleBin$0(Cursor cursor) {
        return (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst()) ? "query_string_not_found_tag" : cursor.getString(0);
    }
}
