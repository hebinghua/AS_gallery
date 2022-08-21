package com.xiaomi.micloudsdk.sync;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes3.dex */
public class SyncStatUtil {
    public static void statOnSyncStart(Context context, boolean z) {
        performSyncPhoneStateStat(context, true, z);
    }

    public static void statOnSyncEnd(Context context, boolean z, boolean z2, String str, String str2, long j, long j2, long j3, long j4) {
        performSyncTimeStat(context, j3, j4, z2);
        performSyncResultStat(context, z, z2, str, str2, j, j2);
        performSyncPhoneStateStat(context, false, z);
    }

    public static void performSyncPhoneStateStat(Context context, boolean z, boolean z2) {
        Log.d("SyncStatUtil", "performSyncPhoneState: ");
        ContentValues contentValues = new ContentValues();
        contentValues.put("micloud_force", Boolean.valueOf(z2));
        contentValues.put("stat_key_sync_start", Boolean.valueOf(z));
        insertStatValueInternal(context, MiCloudStatConstantsV2.OPEN_SYNC_PHONE_STATE, contentValues);
    }

    public static void performSyncResultStat(Context context, boolean z, boolean z2, String str, String str2, long j, long j2) {
        Log.d("SyncStatUtil", "performSyncResultStat: ");
        ContentValues contentValues = new ContentValues();
        Boolean bool = Boolean.FALSE;
        contentValues.put("micloud_ignore_temperature", bool);
        contentValues.put("micloud_ignore_wifi_settings", bool);
        contentValues.put("micloud_ignore_battery_low", bool);
        contentValues.put("stat_key_sync_retry", Boolean.TRUE);
        contentValues.put("micloud_force", Boolean.valueOf(z));
        contentValues.put("stat_key_sync_reason", str);
        contentValues.put("stat_key_sync_successful", Boolean.valueOf(z2));
        contentValues.put("stat_key_sync_authority", str2);
        if (j != -1 && j2 != -1) {
            contentValues.put("stat_key_unsynced_count_before_sync", Long.valueOf(j));
            contentValues.put("stat_key_unsynced_count_after_sync", Long.valueOf(j2));
        }
        insertStatValueInternal(context, MiCloudStatConstantsV2.OPEN_SYNC_RESULT_URI, contentValues);
    }

    public static void performSyncTimeStat(Context context, long j, long j2, boolean z) {
        Log.d("SyncStatUtil", "performSyncTimeStat: ");
        ContentValues contentValues = new ContentValues();
        contentValues.put("stat_key_sync_time_consume", Long.valueOf(j2 - j));
        contentValues.put("stat_key_sync_time_start_in_millis", Long.valueOf(j));
        contentValues.put("stat_key_sync_time_end_in_millis", Long.valueOf(j2));
        contentValues.put("stat_key_sync_successful", Boolean.valueOf(z));
        insertStatValueInternal(context, MiCloudStatConstantsV2.OPEN_SYNC_TIME_CONSUME, contentValues);
    }

    public static void insertStatValueInternal(Context context, Uri uri, ContentValues contentValues) {
        ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(uri);
        try {
            if (acquireContentProviderClient == null) {
                Log.i("SyncStatUtil", "CloudService version is too low.");
                return;
            }
            try {
                acquireContentProviderClient.insert(uri, contentValues);
            } catch (RemoteException unused) {
                Log.e("SyncStatUtil", "CloudService stat provider is unavailable.");
            }
        } finally {
            acquireContentProviderClient.release();
        }
    }
}
