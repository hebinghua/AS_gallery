package cn.kuaipan.android.http;

import android.content.Context;
import android.text.TextUtils;
import cn.kuaipan.android.exception.KscRuntimeException;
import cn.kuaipan.android.utils.FileUtils;
import cn.kuaipan.android.utils.TwoKeyHashMap;
import java.io.File;
import java.util.Objects;

/* loaded from: classes.dex */
public class NetCacheManager {
    public static final TwoKeyHashMap<Boolean, String, NetCacheManager> sCaches = new TwoKeyHashMap<>();
    public final Context mContext;
    public final String mDirName;
    public final boolean mExternal;
    public String mFolderPath;
    public int mLatestId = 0;

    public static synchronized NetCacheManager getInstance(Context context, boolean z) {
        NetCacheManager netCacheManager;
        synchronized (NetCacheManager.class) {
            netCacheManager = getInstance(context, z, null);
        }
        return netCacheManager;
    }

    public static synchronized NetCacheManager getInstance(Context context, boolean z, String str) {
        NetCacheManager netCacheManager;
        synchronized (NetCacheManager.class) {
            if (TextUtils.isEmpty(str)) {
                str = "net_cache";
            }
            TwoKeyHashMap<Boolean, String, NetCacheManager> twoKeyHashMap = sCaches;
            netCacheManager = twoKeyHashMap.get(Boolean.valueOf(z), str);
            if (netCacheManager == null) {
                netCacheManager = new NetCacheManager(context, z, str);
                twoKeyHashMap.put(Boolean.valueOf(z), str, netCacheManager);
            }
        }
        return netCacheManager;
    }

    public NetCacheManager(Context context, boolean z, String str) {
        Objects.requireNonNull(context, "Context can't be null.");
        this.mContext = context;
        this.mExternal = z;
        this.mDirName = str;
        final File cacheDir = FileUtils.getCacheDir(context, str, z);
        if (cacheDir != null) {
            this.mFolderPath = cacheDir.getAbsolutePath();
            new Thread() { // from class: cn.kuaipan.android.http.NetCacheManager.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    FileUtils.deleteChildren(cacheDir);
                }
            }.start();
        }
    }

    public File assignCache() {
        File nextCache;
        do {
            nextCache = getNextCache();
        } while (nextCache.exists());
        nextCache.deleteOnExit();
        return nextCache;
    }

    public final File getNextCache() {
        int i;
        synchronized (this) {
            i = this.mLatestId + 1;
            this.mLatestId = i;
        }
        String format = String.format("%08d.tmp", Integer.valueOf(i));
        File cacheDir = FileUtils.getCacheDir(this.mContext, this.mDirName, this.mExternal);
        if (cacheDir == null) {
            throw new KscRuntimeException(500004);
        }
        this.mFolderPath = cacheDir.getAbsolutePath();
        return new File(cacheDir, format);
    }

    public void releaseCache(File file) {
        if (file == null || !TextUtils.equals(this.mFolderPath, file.getParent())) {
            return;
        }
        file.delete();
    }
}
