package miui.cloud.sync;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import miui.cloud.sync.providers.AntispamSyncInfoProvider;
import miui.cloud.sync.providers.BluetoothSyncInfoProvider;
import miui.cloud.sync.providers.BrowserSyncInfoProvider;
import miui.cloud.sync.providers.CalendarSyncInfoProvider;
import miui.cloud.sync.providers.CalllogSyncInfoProvider;
import miui.cloud.sync.providers.ContactsSyncInfoProvider;
import miui.cloud.sync.providers.GallerySyncInfoProvider;
import miui.cloud.sync.providers.GlobalBrowserSyncInfoProvider;
import miui.cloud.sync.providers.MusicSyncInfoProvider;
import miui.cloud.sync.providers.NotesSyncInfoProvider;
import miui.cloud.sync.providers.PersonalAssistantSyncInfoProvider;
import miui.cloud.sync.providers.PhraseSyncInfoProvider;
import miui.cloud.sync.providers.QuickSearchBoxProvider;
import miui.cloud.sync.providers.SmsSyncInfoProvider;
import miui.cloud.sync.providers.SoundRecorderSyncInfoProvider;
import miui.cloud.sync.providers.WifiSyncInfoProvider;

/* loaded from: classes3.dex */
public final class SyncInfoHelper {
    public static final Map<String, SyncInfoProvider> authorityMap;

    static {
        HashMap hashMap = new HashMap();
        authorityMap = hashMap;
        hashMap.put("com.android.contacts", new ContactsSyncInfoProvider());
        hashMap.put("sms", new SmsSyncInfoProvider());
        hashMap.put("com.miui.gallery.cloud.provider", new GallerySyncInfoProvider());
        hashMap.put("call_log", new CalllogSyncInfoProvider());
        hashMap.put("notes", new NotesSyncInfoProvider());
        hashMap.put("wifi", new WifiSyncInfoProvider());
        hashMap.put("records", new SoundRecorderSyncInfoProvider());
        hashMap.put("com.miui.browser", new BrowserSyncInfoProvider());
        hashMap.put("com.miui.browser.global", new GlobalBrowserSyncInfoProvider());
        hashMap.put("antispam", new AntispamSyncInfoProvider());
        hashMap.put("com.android.calendar", new CalendarSyncInfoProvider());
        hashMap.put("personal_assistant", new PersonalAssistantSyncInfoProvider());
        hashMap.put("com.android.quicksearchbox.cloud", new QuickSearchBoxProvider());
        hashMap.put("com.miui.player.cloud", new MusicSyncInfoProvider());
        hashMap.put("com.miui.player", new MusicSyncInfoProvider());
        hashMap.put("miui.phrase", new PhraseSyncInfoProvider());
        hashMap.put("com.android.bluetooth.ble.app.headsetdata.provider", new BluetoothSyncInfoProvider());
    }

    public static int getUnsyncedDataCount(Context context, String str) throws SyncInfoUnavailableException {
        SyncInfoProvider syncInfoProvider = authorityMap.get(str);
        if (syncInfoProvider == null) {
            throw new SyncInfoUnavailableException("getUnsyncedDataCount not implemented on authority: " + str);
        }
        return syncInfoProvider.getUnsyncedCount(context);
    }

    public static int getWifiOnlyUnsyncedDataCount(Context context, String str) throws SyncInfoUnavailableException {
        SyncInfoProvider syncInfoProvider = authorityMap.get(str);
        if (syncInfoProvider == null) {
            throw new SyncInfoUnavailableException("getWifiOnlyUnsyncedDataCount not implemented on authority: " + str);
        }
        return syncInfoProvider.getWifiOnlyUnsyncedCount(context);
    }

    public static int getUnSyncedSecretDataCount(Context context, String str) throws SyncInfoUnavailableException {
        SyncInfoProvider syncInfoProvider = authorityMap.get(str);
        if (syncInfoProvider == null) {
            throw new SyncInfoUnavailableException("getUnsyncedSecretDataCount not implemented on authority: " + str);
        }
        return syncInfoProvider.getUnSyncedSecretCount(context);
    }

    public static int getSyncedDataCount(Context context, String str) throws SyncInfoUnavailableException {
        SyncInfoProvider syncInfoProvider = authorityMap.get(str);
        if (syncInfoProvider == null) {
            throw new SyncInfoUnavailableException("getSyncedDataCount not implemented on authority: " + str);
        }
        return syncInfoProvider.getSyncedCount(context);
    }
}
