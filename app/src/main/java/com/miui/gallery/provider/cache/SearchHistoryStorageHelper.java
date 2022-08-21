package com.miui.gallery.provider.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchHistoryStorageHelper {
    public static int addHistoryItem(SearchHistoryItem searchHistoryItem, List<SearchHistoryItem> list) {
        List savedHistories = getSavedHistories();
        if (savedHistories == null || savedHistories.isEmpty()) {
            savedHistories = new ArrayList(1);
        } else {
            removeDupHistoryItem(savedHistories, searchHistoryItem);
        }
        savedHistories.add(0, searchHistoryItem);
        if (savedHistories.size() > 6) {
            savedHistories.remove(5);
        }
        list.addAll(savedHistories);
        return saveHistories(savedHistories);
    }

    public static List<SearchHistoryItem> getSavedHistories() {
        InputStreamReader inputStreamReader;
        Throwable th;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(getHistoryFilePath()));
            try {
                try {
                    ArrayList arrayList = (ArrayList) new Gson().fromJson(inputStreamReader, new TypeToken<ArrayList<SearchHistoryItem>>() { // from class: com.miui.gallery.provider.cache.SearchHistoryStorageHelper.1
                    }.getType());
                    try {
                        inputStreamReader.close();
                    } catch (Exception unused) {
                    }
                    return arrayList;
                } catch (Exception unused2) {
                    SearchLog.e("SearchHistoryStorageHelper", "Read saved logs from cache failed!");
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

    public static int removeHistoryItem(String str, List<SearchHistoryItem> list) {
        List<SearchHistoryItem> savedHistories = getSavedHistories();
        if (savedHistories != null && !savedHistories.isEmpty()) {
            int size = savedHistories.size();
            SearchHistoryItem searchHistoryItem = null;
            Iterator<SearchHistoryItem> it = savedHistories.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SearchHistoryItem next = it.next();
                if (StringUtils.nullToEmpty(next.intentActionURI).equals(StringUtils.nullToEmpty(str))) {
                    searchHistoryItem = next;
                    break;
                }
            }
            if (searchHistoryItem != null) {
                savedHistories.remove(searchHistoryItem);
            }
            if (savedHistories.size() == 0) {
                list.clear();
                return clearSavedHistories();
            } else if (savedHistories.size() != size && saveHistories(savedHistories) > 0) {
                list.addAll(savedHistories);
                return 1;
            }
        }
        return 0;
    }

    public static int clearSavedHistories() {
        File file = new File(getHistoryFilePath());
        if (!file.exists() || !file.delete()) {
            return 0;
        }
        SearchLog.i("SearchHistoryStorageHelper", "Delete history log file");
        return 1;
    }

    public static int removeDupHistoryItem(List<SearchHistoryItem> list, SearchHistoryItem searchHistoryItem) {
        SearchHistoryItem searchHistoryItem2;
        Iterator<SearchHistoryItem> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                searchHistoryItem2 = null;
                break;
            }
            searchHistoryItem2 = it.next();
            if (StringUtils.nullToEmpty(searchHistoryItem2.intentActionURI).equals(StringUtils.nullToEmpty(searchHistoryItem.intentActionURI))) {
                break;
            }
        }
        if (searchHistoryItem2 != null) {
            return list.remove(searchHistoryItem2) ? 1 : 0;
        }
        return 0;
    }

    public static int saveHistories(List<SearchHistoryItem> list) {
        String json;
        OutputStreamWriter outputStreamWriter;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        OutputStreamWriter outputStreamWriter2 = null;
        try {
            try {
                json = new Gson().toJson(list);
                outputStreamWriter = new OutputStreamWriter(new FileOutputStream(getHistoryFilePath()));
            } catch (Exception unused) {
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            outputStreamWriter.write(json);
            SearchLog.i("SearchHistoryStorageHelper", "Save histories [%s] succeed", list);
            int size = list.size();
            try {
                outputStreamWriter.close();
            } catch (Exception unused2) {
            }
            return size;
        } catch (Exception unused3) {
            outputStreamWriter2 = outputStreamWriter;
            SearchLog.i("SearchHistoryStorageHelper", "Save histories [%s] failed", list);
            try {
                outputStreamWriter2.close();
            } catch (Exception unused4) {
            }
            return 0;
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

    public static String getHistoryFilePath() {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/searchCache");
        StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("SearchHistoryStorageHelper", "getHistoryFilePath"));
        return BaseFileUtils.concat(pathInPrimaryStorage, "search_histories.json");
    }
}
