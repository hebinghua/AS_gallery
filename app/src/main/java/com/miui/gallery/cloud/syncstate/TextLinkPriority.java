package com.miui.gallery.cloud.syncstate;

import java.util.HashMap;

/* loaded from: classes.dex */
public class TextLinkPriority {
    public static HashMap<String, Integer> priorities;

    static {
        HashMap<String, Integer> hashMap = new HashMap<>();
        priorities = hashMap;
        hashMap.put(SyncStatus.SYNC_PENDING.name(), 0);
        priorities.put(SyncStatus.SYNCING.name(), 3);
        priorities.put(SyncStatus.SYNCING_METADATA.name(), 3);
        priorities.put(SyncStatus.SYNC_META_PENDING.name(), 3);
        priorities.put(SyncStatus.SYNCED.name(), 3);
        priorities.put(SyncStatus.SYNC_PAUSE.name(), 0);
        priorities.put(SyncStatus.SYNCED_WITH_OVERSIZED_FILE.name(), 0);
        priorities.put(SyncStatus.NO_ACCOUNT.name(), 0);
        priorities.put(SyncStatus.MASTER_SYNC_OFF.name(), 0);
        priorities.put(SyncStatus.SYNC_OFF.name(), 0);
        priorities.put(SyncStatus.CTA_NOT_ALLOW.name(), 0);
        priorities.put(SyncStatus.MI_MOVER_RUNNING.name(), 3);
        priorities.put(SyncStatus.DISCONNECTED.name(), 0);
        priorities.put(SyncStatus.NO_WIFI.name(), 0);
        priorities.put(SyncStatus.BATTERY_LOW.name(), 0);
        priorities.put(SyncStatus.SYSTEM_SPACE_LOW.name(), 0);
        priorities.put(SyncStatus.CLOUD_SPACE_FULL.name(), 0);
        priorities.put(SyncStatus.UNKNOWN_ERROR.name(), 0);
        priorities.put(SyncStatus.EXCEPTED_ERROR.name(), 0);
        priorities.put(SyncStatus.UNAVAILABLE.name(), 0);
    }

    public static int getPriority(String str, int i) {
        Integer num = priorities.get(str);
        return num == null ? i : num.intValue();
    }
}
