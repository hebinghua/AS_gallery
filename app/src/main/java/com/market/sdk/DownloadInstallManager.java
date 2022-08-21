package com.market.sdk;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import com.market.sdk.XiaomiUpdateAgent;
import com.market.sdk.utils.Client;
import com.market.sdk.utils.Coder;
import com.market.sdk.utils.Constants;
import com.market.sdk.utils.Log;
import com.market.sdk.utils.PkgUtils;
import com.market.sdk.utils.ReflectUtils;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.io.File;

/* loaded from: classes.dex */
public class DownloadInstallManager {
    public static DownloadManager mDownloadManager;
    public static DownloadInstallManager sDownloadInstallManager;
    public LocalAppInfo mAppInfo;
    public Context mContext;
    public long mDownloadId = -1;
    public XiaomiUpdateAgent.UpdateInfo mUpdateInfo;
    public WorkerHandler mWorkerHandler;
    public HandlerThread mWorkerThread;

    public DownloadInstallManager(Context context) {
        Client.init(context);
        this.mContext = context.getApplicationContext();
        initDownloadManager();
        HandlerThread handlerThread = new HandlerThread("Worker Thread");
        this.mWorkerThread = handlerThread;
        handlerThread.start();
        this.mWorkerHandler = new WorkerHandler(this.mWorkerThread.getLooper());
    }

    public final void initDownloadManager() {
        mDownloadManager = (DownloadManager) this.mContext.getSystemService("download");
        if (Client.isLaterThanN()) {
            ReflectUtils.invoke(DownloadManager.class, mDownloadManager, "setAccessFilename", ReflectUtils.getMethodSignature(Void.TYPE, Boolean.TYPE), Boolean.TRUE);
        }
    }

    public void handleDownloadComplete(long j) {
        DownloadManagerInfo find;
        if (j >= 0) {
            long j2 = this.mDownloadId;
            if (j2 != j || (find = DownloadManagerInfo.find(j2)) == null || find.status == 16 || TextUtils.isEmpty(find.downloadFilePath)) {
                return;
            }
            this.mWorkerHandler.install(find.downloadFilePath, !TextUtils.isEmpty(this.mUpdateInfo.diffUrl));
        }
    }

    public static synchronized DownloadInstallManager getManager(Context context) {
        DownloadInstallManager downloadInstallManager;
        synchronized (DownloadInstallManager.class) {
            if (sDownloadInstallManager == null) {
                sDownloadInstallManager = new DownloadInstallManager(context);
            }
            downloadInstallManager = sDownloadInstallManager;
        }
        return downloadInstallManager;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isDownloading(com.market.sdk.LocalAppInfo r11) {
        /*
            r10 = this;
            android.content.Context r0 = r10.mContext
            com.market.sdk.SDKDatabaseHelper r1 = com.market.sdk.SDKDatabaseHelper.getHelper(r0)
            java.lang.String[] r3 = com.market.sdk.utils.Constants.Update.UPDATE_PROJECTION
            r0 = 1
            java.lang.String[] r5 = new java.lang.String[r0]
            java.lang.String r11 = r11.packageName
            r9 = 0
            r5[r9] = r11
            java.lang.String r2 = "update_download"
            java.lang.String r4 = "package_name=?"
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r11 = r1.query(r2, r3, r4, r5, r6, r7, r8)
            r1 = -1
            if (r11 == 0) goto L35
            boolean r3 = r11.moveToFirst()     // Catch: java.lang.Throwable -> L30
            if (r3 == 0) goto L35
            java.lang.String r3 = "download_id"
            int r3 = r11.getColumnIndex(r3)     // Catch: java.lang.Throwable -> L30
            long r3 = r11.getLong(r3)     // Catch: java.lang.Throwable -> L30
            goto L36
        L30:
            r0 = move-exception
            r11.close()
            throw r0
        L35:
            r3 = r1
        L36:
            int r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r1 != 0) goto L40
            if (r11 == 0) goto L3f
            r11.close()
        L3f:
            return r9
        L40:
            if (r11 == 0) goto L45
            r11.close()
        L45:
            android.app.DownloadManager$Query r11 = new android.app.DownloadManager$Query
            r11.<init>()
            long[] r1 = new long[r0]
            r1[r9] = r3
            r11.setFilterById(r1)
            android.app.DownloadManager r1 = com.market.sdk.DownloadInstallManager.mDownloadManager
            android.database.Cursor r11 = r1.query(r11)
            r1 = -1
            if (r11 == 0) goto L70
            boolean r2 = r11.moveToFirst()     // Catch: java.lang.Throwable -> L6b
            if (r2 == 0) goto L70
            java.lang.String r1 = "status"
            int r1 = r11.getColumnIndexOrThrow(r1)     // Catch: java.lang.Throwable -> L6b
            int r1 = r11.getInt(r1)     // Catch: java.lang.Throwable -> L6b
            goto L70
        L6b:
            r0 = move-exception
            r11.close()
            throw r0
        L70:
            r2 = 4
            if (r1 == r2) goto L7e
            if (r1 == r0) goto L7e
            r2 = 2
            if (r1 == r2) goto L7e
            if (r11 == 0) goto L7d
            r11.close()
        L7d:
            return r9
        L7e:
            if (r11 == 0) goto L83
            r11.close()
        L83:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.market.sdk.DownloadInstallManager.isDownloading(com.market.sdk.LocalAppInfo):boolean");
    }

    public final synchronized void reloadUpdateInfoFromDB() {
        if (this.mUpdateInfo != null) {
            return;
        }
        if (this.mAppInfo == null) {
            Context context = this.mContext;
            LocalAppInfo appInfo = XiaomiUpdateAgent.getAppInfo(context, context.getPackageName());
            this.mAppInfo = appInfo;
            if (appInfo == null) {
                return;
            }
        }
        Cursor query = SDKDatabaseHelper.getHelper(this.mContext).query("update_download", Constants.Update.UPDATE_PROJECTION, "package_name=?", new String[]{this.mAppInfo.packageName}, null, null, null);
        if (query == null || !query.moveToFirst()) {
            if (query != null) {
                query.close();
            }
            return;
        }
        this.mDownloadId = query.getLong(query.getColumnIndex("download_id"));
        XiaomiUpdateAgent.UpdateInfo updateInfo = new XiaomiUpdateAgent.UpdateInfo();
        updateInfo.versionCode = query.getInt(query.getColumnIndex("version_code"));
        updateInfo.apkUrl = query.getString(query.getColumnIndex("apk_url"));
        updateInfo.apkHash = query.getString(query.getColumnIndex("apk_hash"));
        updateInfo.diffUrl = query.getString(query.getColumnIndex("diff_url"));
        updateInfo.diffHash = query.getString(query.getColumnIndex("diff_hash"));
        this.mUpdateInfo = updateInfo;
        query.close();
    }

    /* loaded from: classes.dex */
    public class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        public void reloadDownloadTasks() {
            if (DownloadInstallManager.this.mAppInfo == null || DownloadInstallManager.this.mUpdateInfo == null) {
                DownloadInstallManager downloadInstallManager = DownloadInstallManager.this;
                downloadInstallManager.mAppInfo = XiaomiUpdateAgent.getAppInfo(downloadInstallManager.mContext, DownloadInstallManager.this.mContext.getPackageName());
                if (DownloadInstallManager.this.mAppInfo == null) {
                    return;
                }
                DownloadInstallManager.this.reloadUpdateInfoFromDB();
            }
        }

        public void install(final String str, final boolean z) {
            post(new Runnable() { // from class: com.market.sdk.DownloadInstallManager.WorkerHandler.2
                @Override // java.lang.Runnable
                public void run() {
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    WorkerHandler.this.reloadDownloadTasks();
                    String str2 = str;
                    if (z) {
                        WorkerHandler workerHandler = WorkerHandler.this;
                        str2 = workerHandler.getPatchedApk(str2, DownloadInstallManager.this.mUpdateInfo.diffHash);
                    }
                    if (WorkerHandler.this.verify(str2)) {
                        WorkerHandler.this.launchPackageInstaller(str2);
                    } else {
                        Log.e("MarketUpdateDownload", "verify downloaded apk failed");
                    }
                }
            });
        }

        public final void launchPackageInstaller(String str) {
            Uri generateInstallUri = DownloadInstallManager.this.generateInstallUri(str);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(generateInstallUri, "application/vnd.android.package-archive");
            String queryDefaultPackageForIntent = PkgUtils.queryDefaultPackageForIntent(intent);
            if (TextUtils.isEmpty(queryDefaultPackageForIntent)) {
                Log.e("MarketUpdateDownload", "no activity found to install apk");
                return;
            }
            if (TextUtils.equals(generateInstallUri.getScheme(), MiStat.Param.CONTENT)) {
                DownloadInstallManager.this.mContext.grantUriPermission(queryDefaultPackageForIntent, generateInstallUri, 1);
            }
            intent.setPackage(queryDefaultPackageForIntent);
            intent.setFlags(268435456);
            DownloadInstallManager.this.mContext.startActivity(intent);
        }

        public final boolean verify(String str) {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            return TextUtils.equals(Coder.encodeMD5(new File(str)), DownloadInstallManager.this.mUpdateInfo.apkHash);
        }

        public final String getPatchedApk(String str, String str2) {
            if (TextUtils.isEmpty(str2) || TextUtils.equals(Coder.encodeMD5(new File(str)), str2)) {
                String str3 = str + ".apk";
                if (DownloadInstallManager.this.mAppInfo == null || TextUtils.isEmpty(DownloadInstallManager.this.mAppInfo.sourceDir)) {
                    return null;
                }
                Patcher.patch(DownloadInstallManager.this.mAppInfo.sourceDir, str3, str);
                try {
                    new File(str).delete();
                } catch (Exception unused) {
                }
                return str3;
            }
            return null;
        }
    }

    public final Uri generateInstallUri(String str) {
        if (Client.isLaterThanN()) {
            File file = new File(str);
            return LazyFileProvider.getUriForFile(this.mContext, this.mContext.getPackageName() + ".selfupdate.fileprovider", file);
        }
        return Uri.parse("file://" + str);
    }

    /* loaded from: classes.dex */
    public static class DownloadManagerInfo {
        public int currBytes;
        public String downloadFilePath;
        public long id;
        public int reason;
        public int status;
        public int totalBytes;

        public static DownloadManagerInfo find(long j) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(j);
            try {
                Cursor query2 = DownloadInstallManager.mDownloadManager.query(query);
                if (query2 != null) {
                    try {
                        if (query2.moveToFirst()) {
                            return query(query2);
                        }
                    } finally {
                        query2.close();
                    }
                }
                if (query2 != null) {
                }
                return null;
            } catch (Exception e) {
                Log.e("MarketUpdateDownload", "Query download from DownloadManager failed - " + e.toString());
                return null;
            }
        }

        @SuppressLint({"InlinedApi"})
        public static DownloadManagerInfo query(Cursor cursor) {
            try {
                int columnIndexOrThrow = cursor.getColumnIndexOrThrow(j.c);
                int columnIndexOrThrow2 = cursor.getColumnIndexOrThrow("status");
                int columnIndexOrThrow3 = cursor.getColumnIndexOrThrow("reason");
                int columnIndexOrThrow4 = cursor.getColumnIndexOrThrow("bytes_so_far");
                int columnIndexOrThrow5 = cursor.getColumnIndexOrThrow("total_size");
                int columnIndexOrThrow6 = cursor.getColumnIndexOrThrow(Client.isLaterThanHoneycomb() ? "local_filename" : "file_path");
                DownloadManagerInfo downloadManagerInfo = new DownloadManagerInfo();
                downloadManagerInfo.id = cursor.getLong(columnIndexOrThrow);
                downloadManagerInfo.status = cursor.getInt(columnIndexOrThrow2);
                downloadManagerInfo.reason = cursor.getInt(columnIndexOrThrow3);
                downloadManagerInfo.currBytes = cursor.getInt(columnIndexOrThrow4);
                downloadManagerInfo.totalBytes = cursor.getInt(columnIndexOrThrow5);
                downloadManagerInfo.downloadFilePath = cursor.getString(columnIndexOrThrow6);
                return downloadManagerInfo;
            } catch (Exception unused) {
                return null;
            }
        }
    }
}
