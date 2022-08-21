package com.miui.gallery.search.statistics;

import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.util.FileSize;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class SearchStatStorageHelper {
    public static int saveLogItem(SearchStatLogItem searchStatLogItem) {
        ArrayList<SearchStatLogItem> savedLogs;
        String json;
        OutputStreamWriter outputStreamWriter;
        File file = new File(getLogFilePath());
        try {
            if (file.exists() && file.length() > FileSize.MB_COEFFICIENT) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(getLogFilePath(), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("SearchStatStorageHelper", "saveLogItem"));
                if (documentFile != null) {
                    documentFile.delete();
                }
                SearchLog.i("SearchStatStorageHelper", "Clear former log file due to too many logs");
            }
        } catch (Exception e) {
            SearchLog.e("SearchStatStorageHelper", e);
        }
        OutputStreamWriter outputStreamWriter2 = null;
        try {
            try {
                savedLogs = getSavedLogs();
                if (savedLogs == null) {
                    savedLogs = new ArrayList<>(1);
                }
                savedLogs.add(searchStatLogItem);
                json = new Gson().toJson(savedLogs);
                outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            } catch (Exception unused) {
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            outputStreamWriter.write(json);
            SearchLog.i("SearchStatStorageHelper", "Insert item succeed, now total %d items", Integer.valueOf(savedLogs.size()));
            int size = savedLogs.size();
            try {
                outputStreamWriter.close();
            } catch (Exception unused2) {
            }
            return size;
        } catch (Exception unused3) {
            outputStreamWriter2 = outputStreamWriter;
            SearchLog.i("SearchStatStorageHelper", "Insert item [%s] failed");
            try {
                outputStreamWriter2.close();
                return 0;
            } catch (Exception unused4) {
                return 0;
            }
        } catch (Throwable th2) {
            th = th2;
            outputStreamWriter2 = outputStreamWriter;
            try {
                outputStreamWriter2.close();
            } catch (Exception unused5) {
            }
            throw th;
        }
    }

    public static ArrayList<SearchStatLogItem> getSavedLogs() {
        InputStreamReader inputStreamReader;
        Throwable th;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(getLogFilePath()));
            try {
                try {
                    ArrayList<SearchStatLogItem> arrayList = (ArrayList) new Gson().fromJson(inputStreamReader, new TypeToken<ArrayList<SearchStatLogItem>>() { // from class: com.miui.gallery.search.statistics.SearchStatStorageHelper.1
                    }.getType());
                    try {
                        inputStreamReader.close();
                    } catch (Exception unused) {
                    }
                    return arrayList;
                } catch (Exception unused2) {
                    SearchLog.e("SearchStatStorageHelper", "Read saved logs from cache failed!");
                    try {
                        inputStreamReader.close();
                    } catch (Exception unused3) {
                    }
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    inputStreamReader.close();
                } catch (Exception unused4) {
                }
                throw th;
            }
        } catch (Exception unused5) {
            inputStreamReader = null;
        } catch (Throwable th3) {
            inputStreamReader = null;
            th = th3;
            inputStreamReader.close();
            throw th;
        }
    }

    public static void clearSavedLogs() {
        File file = new File(getLogFilePath());
        if (!file.exists() || !file.delete()) {
            return;
        }
        SearchLog.i("SearchStatStorageHelper", "Delete statistic log file");
    }

    public static String getLogFilePath() {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/searchCache");
        StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("SearchStatStorageHelper", "getLogFilePath"));
        return BaseFileUtils.concat(pathInPrimaryStorage, "search_log.json");
    }
}
