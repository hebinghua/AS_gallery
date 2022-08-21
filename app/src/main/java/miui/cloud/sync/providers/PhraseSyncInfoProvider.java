package miui.cloud.sync.providers;

import android.content.Context;
import android.net.Uri;
import miui.cloud.sync.SyncInfoProviderBase;

/* loaded from: classes3.dex */
public class PhraseSyncInfoProvider extends SyncInfoProviderBase {
    public Uri SYNCED_CONTENT_URI = Uri.parse("content://miui.phrase/phrase/synced");
    public Uri UNSYNCED_CONTENT_URI = Uri.parse("content://miui.phrase/phrase/unsynced");

    public int getWifiOnlyUnsyncedCount(Context context) {
        return 0;
    }

    public int getSyncedCount(Context context) {
        return queryCount(context, this.SYNCED_CONTENT_URI, (String) null, (String[]) null);
    }

    public int getUnsyncedCount(Context context) {
        return queryCount(context, this.UNSYNCED_CONTENT_URI, (String) null, (String[]) null);
    }
}
