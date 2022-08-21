package com.miui.gallery.photosapi;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public final class PhotosOemApi {
    public static int getVersion(Context context) {
        try {
            ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(new Uri.Builder().scheme(MiStat.Param.CONTENT).authority(getAuthority(context)).build());
            if (acquireContentProviderClient == null) {
                if (acquireContentProviderClient != null) {
                    acquireContentProviderClient.close();
                }
                return 1;
            }
            Bundle call = acquireContentProviderClient.call("version", null, null);
            if (call != null) {
                int i = call.getInt("version");
                acquireContentProviderClient.close();
                return i;
            }
            acquireContentProviderClient.close();
            return 1;
        } catch (Exception unused) {
            return 1;
        }
    }

    public static String getAuthority(Context context) {
        return context.getString(R$string.provider_authority);
    }

    public static Uri getQueryProcessingUri(Context context, long j) {
        return getBaseBuilder(context).appendPath("processing").appendPath(String.valueOf(j)).build();
    }

    public static Uri getQueryProcessingUri(Context context) {
        return getBaseBuilder(context).appendPath("processing").build();
    }

    public static Uri.Builder getBaseBuilder(Context context) {
        return new Uri.Builder().scheme(MiStat.Param.CONTENT).authority(getAuthority(context));
    }
}
