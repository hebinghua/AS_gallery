package micloud.compat.independent.utils;

import android.content.Context;
import android.util.Log;
import com.xiaomi.micloudsdk.provider.MiCloudSettings;

/* loaded from: classes3.dex */
public class RelocationCacheCompat_V18 extends RelocationCacheCompat_Base {
    @Override // micloud.compat.independent.utils.RelocationCacheCompat_Base, micloud.compat.independent.utils.IRelocationCacheCompat
    public void cacheXiaomiAccountName(Context context, String str) {
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", "set accountName: " + str + " to settings");
        }
        MiCloudSettings.putString(context.getContentResolver(), "micloud_accountname_v2", str);
    }

    @Override // micloud.compat.independent.utils.RelocationCacheCompat_Base, micloud.compat.independent.utils.IRelocationCacheCompat
    public String getCachedXiaomiAccountName(Context context) {
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", "get accountName from settings");
        }
        return MiCloudSettings.getString(context.getContentResolver(), "micloud_accountname_v2");
    }

    @Override // micloud.compat.independent.utils.RelocationCacheCompat_Base, micloud.compat.independent.utils.IRelocationCacheCompat
    public void cacheHostList(Context context, String str) {
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", "set hostList to settings");
        }
        MiCloudSettings.putString(context.getContentResolver(), "micloud_hosts_v2", str);
    }

    @Override // micloud.compat.independent.utils.RelocationCacheCompat_Base, micloud.compat.independent.utils.IRelocationCacheCompat
    public String getCachedHostList(Context context) {
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", "get hostList from settings");
        }
        return MiCloudSettings.getString(context.getContentResolver(), "micloud_hosts_v2");
    }
}
