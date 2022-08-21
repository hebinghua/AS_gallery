package miui.cloud.sync.providers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import miui.cloud.sync.SyncInfoProviderBase;

/* loaded from: classes3.dex */
public class GlobalBrowserSyncInfoProvider extends SyncInfoProviderBase {
    public static final Uri AUTHORITY_URI;
    public static final Uri BOOKMARK_CONTENT_URI;
    public static final Uri HISTORY_CONTENT_URI;

    public int getWifiOnlyUnsyncedCount(Context context) {
        return 0;
    }

    static {
        Uri parse = Uri.parse("content://com.miui.browser.global");
        AUTHORITY_URI = parse;
        BOOKMARK_CONTENT_URI = Uri.withAppendedPath(parse, "bookmarks");
        HISTORY_CONTENT_URI = Uri.withAppendedPath(parse, "historysync");
    }

    public int getUnsyncedCount(Context context) {
        int bookmarksDirtyCount = getBookmarksDirtyCount(context);
        int historyDirtyCount = getHistoryDirtyCount(context);
        if (bookmarksDirtyCount == -1 || historyDirtyCount == -1) {
            return -1;
        }
        return bookmarksDirtyCount + historyDirtyCount;
    }

    public final int getBookmarksDirtyCount(Context context) {
        int queryCount = queryCount(context, BOOKMARK_CONTENT_URI, "_id != 1 AND dirty=1", (String[]) null);
        if (isDebug()) {
            Log.d("GlobalBrowserSyncInfoProvider", "getGlobalBrowserDirtyBookmarksCount count = " + queryCount);
        }
        return queryCount;
    }

    public final int getHistoryDirtyCount(Context context) {
        int queryCount = queryCount(context, HISTORY_CONTENT_URI, "(sourceid IS NULL OR deleted=1)", (String[]) null);
        if (isDebug()) {
            Log.d("GlobalBrowserSyncInfoProvider", "getGlobalBrowserDirtyHistoryCount count = " + queryCount);
        }
        return queryCount;
    }

    public int getSyncedCount(Context context) {
        int bookmarksSyncedCount = getBookmarksSyncedCount(context);
        int historySyncedCount = getHistorySyncedCount(context);
        if (bookmarksSyncedCount == -1 || historySyncedCount == -1) {
            return -1;
        }
        return bookmarksSyncedCount + historySyncedCount;
    }

    public final int getBookmarksSyncedCount(Context context) {
        int queryCount = queryCount(context, BOOKMARK_CONTENT_URI, "_id != 1 AND dirty =0  AND sourceid is not null", (String[]) null);
        if (isDebug()) {
            Log.d("GlobalBrowserSyncInfoProvider", "getGlobalBrowserSyncedBookmarksCount count = " + queryCount);
        }
        return queryCount;
    }

    public final int getHistorySyncedCount(Context context) {
        int queryCount = queryCount(context, HISTORY_CONTENT_URI, "(sourceid IS not NULL AND deleted=0)", (String[]) null);
        if (isDebug()) {
            Log.d("GlobalBrowserSyncInfoProvider", "getGlobalBrowserSyncedHistoryCount count = " + queryCount);
        }
        return queryCount;
    }
}
