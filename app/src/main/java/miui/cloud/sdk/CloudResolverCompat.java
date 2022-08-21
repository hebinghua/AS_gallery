package miui.cloud.sdk;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;
import com.xiaomi.micloudsdk.sync.MiCloudResolver;
import miui.cloud.content.MiSyncPolicyResolver;

/* loaded from: classes3.dex */
public class CloudResolverCompat {
    public static final LazyValue<Context, Integer> SDK_VERSION = new LazyValue<Context, Integer>() { // from class: miui.cloud.sdk.CloudResolverCompat.1
        @Override // miui.cloud.sdk.LazyValue
        public Integer onInit(Context context) {
            return Integer.valueOf(ManifestParser.createFromPackage(context.getPackageManager(), "com.xiaomi.micloud.sdk").parse(null).getModule().getLevel());
        }
    };

    public static int getSDKVersion(Context context) {
        return SDK_VERSION.get(context).intValue();
    }

    public static boolean isSyncPaused(Context context, Account account, String str) {
        return getResumeTime(context, account, str) > System.currentTimeMillis();
    }

    public static long getResumeTime(Context context, Account account, String str) {
        try {
            if (getSDKVersion(context) < 29) {
                return MiCloudResolver.getResumeTime(context, str);
            }
            return MiSyncPolicyResolver.getMiSyncPauseToTime(account);
        } catch (Throwable th) {
            Log.e("CloudResolverCompat", "getResumeTime", th);
            return -1L;
        }
    }

    public static boolean pauseSync(Context context, Account account, String str, long j) {
        try {
            if (getSDKVersion(context) < 29) {
                MiCloudResolver.pauseSync(context, account, str, j);
                return true;
            }
            MiSyncPolicyResolver.setMiSyncPauseToTime(account, System.currentTimeMillis() + j);
            return true;
        } catch (Throwable th) {
            Log.e("CloudResolverCompat", "pauseSync", th);
            return false;
        }
    }

    public static boolean resumeSync(Context context, Account account, String str) {
        try {
            if (getSDKVersion(context) < 29) {
                MiCloudResolver.resumeSync(context, account, str);
                return true;
            }
            MiSyncPolicyResolver.setMiSyncResume(account);
            return true;
        } catch (Throwable th) {
            Log.e("CloudResolverCompat", "resumeSync", th);
            return false;
        }
    }
}
