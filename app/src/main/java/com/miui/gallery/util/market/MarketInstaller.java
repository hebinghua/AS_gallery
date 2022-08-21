package com.miui.gallery.util.market;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.market.sdk.FloatCardManager;
import com.market.sdk.MarketManager;
import com.market.sdk.utils.AppGlobal;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;

/* loaded from: classes2.dex */
public abstract class MarketInstaller {
    public BroadcastReceiver mInstallReceiver;
    public int mInstallState = 0;
    public long mLastStartTime = 0;
    public int mStartCount = 0;

    public abstract long getPackageMinVersion();

    public abstract String getPackageName();

    public abstract void onInstallFail(int i, int i2);

    public abstract void onInstallLimit();

    public abstract void onInstallResume();

    public abstract void onInstallStart();

    public abstract void onInstallSuccess();

    public abstract void onInstalling();

    public boolean isPackageAvailable() {
        String packageName = getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            DefaultLogger.e("MarketInstaller", "get package name fail");
            return false;
        } else if (!BaseMiscUtil.isPackageInstalled(packageName)) {
            DefaultLogger.e("MarketInstaller", "package not installed");
            return false;
        } else {
            if (MiscUtil.getAppVersionCode(packageName) >= getPackageMinVersion()) {
                return true;
            }
            DefaultLogger.e("MarketInstaller", "%s version is lower then needed, upgrading!", packageName);
            return false;
        }
    }

    public void installPackage() {
        DefaultLogger.d("MarketInstaller", "install mediaeditor Package ,state is %d", Integer.valueOf(this.mInstallState));
        int i = this.mInstallState;
        if (i != 0) {
            if (i == 1) {
                onInstalling();
                return;
            } else if (i == 2) {
                onInstallResume();
                this.mInstallState = 1;
                startInstall(true);
                return;
            } else {
                DefaultLogger.e("MarketInstaller", "wrong install state");
                return;
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastStartTime < 60000) {
            this.mStartCount++;
        } else {
            this.mLastStartTime = currentTimeMillis;
            this.mStartCount = 1;
        }
        if (this.mStartCount > 10) {
            DefaultLogger.d("MarketInstaller", "limit install, start times:" + this.mStartCount);
            onInstallLimit();
            return;
        }
        this.mInstallState = 1;
        onInstallStart();
        startInstall(false);
    }

    public final void startInstall(final boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("name", getPackageName());
        hashMap.put("state", "resume_" + z);
        SamplingStatHelper.recordCountEvent("market_install", "install_start", hashMap);
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<String>() { // from class: com.miui.gallery.util.market.MarketInstaller.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public String mo1807run(ThreadPool.JobContext jobContext) {
                return MarketInstaller.this.generateAppData();
            }
        }, new FutureHandler<String>() { // from class: com.miui.gallery.util.market.MarketInstaller.2
            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<String> future) {
                if (future == null || TextUtils.isEmpty(future.get())) {
                    DefaultLogger.d("MarketInstaller", "empty signature");
                    MarketInstaller.this.mInstallState = 0;
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("name", MarketInstaller.this.getPackageName());
                    hashMap2.put("reason", "empty_deeplink");
                    SamplingStatHelper.recordCountEvent("market_install", "install_fail", hashMap2);
                    MarketInstaller.this.onInstallFail(-2, 28);
                    return;
                }
                String str = future.get();
                try {
                    AppGlobal.setContext(StaticContext.sGetAndroidContext());
                    FloatCardManager floatCardManager = MarketManager.getManager().getFloatCardManager();
                    MarketInstaller.this.registerInstallReceiver();
                    if (z) {
                        floatCardManager.resumeByFloat(str);
                    } else {
                        floatCardManager.downloadByFloat(str);
                    }
                } catch (Exception e) {
                    DefaultLogger.d("MarketInstaller", "error install");
                    HashMap hashMap3 = new HashMap();
                    hashMap3.put("name", MarketInstaller.this.getPackageName());
                    hashMap3.put("reason", "install_exception");
                    SamplingStatHelper.recordCountEvent("market_install", "install_fail", hashMap3);
                    MarketInstaller marketInstaller = MarketInstaller.this;
                    marketInstaller.mInstallState = 0;
                    marketInstaller.unregisterInstallReceiver();
                    MarketInstaller.this.onInstallFail(0, 0);
                    e.printStackTrace();
                }
            }
        });
    }

    public final String generateAppData() {
        SignatureResult signatureResult = null;
        try {
            Object[] executeSync = new MarketInstallSignatureRequest(getPackageName()).executeSync();
            if (executeSync != null && executeSync.length > 0) {
                signatureResult = (SignatureResult) executeSync[0];
            }
        } catch (RequestError e) {
            DefaultLogger.d("MarketInstaller", "get signature error");
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        if (signatureResult != null && !TextUtils.isEmpty(signatureResult.signature)) {
            sb.append("market://details/detailfloat?");
            sb.append("packageName=");
            sb.append(getPackageName());
            sb.append("&nonce=");
            sb.append(signatureResult.nonce);
            sb.append("&ref=");
            sb.append("MiuiGallery");
            sb.append("&startDownload=");
            sb.append("true");
            sb.append("&show_cta=");
            sb.append("true");
            sb.append("&appClientId=");
            sb.append("2882303761517280635");
            sb.append("&senderPackageName=");
            sb.append("com.miui.gallery");
            sb.append("&appSignature=");
            sb.append(signatureResult.signature);
            sb.append("&overlayPosition=");
            sb.append("1");
        }
        return sb.toString();
    }

    public final void registerInstallReceiver() {
        if (this.mInstallReceiver == null) {
            DefaultLogger.d("MarketInstaller", "register install receiver");
            this.mInstallReceiver = new DownloadReceiver();
            GalleryApp.sGetAndroidContext().registerReceiver(this.mInstallReceiver, new IntentFilter("com.xiaomi.market.DOWNLOAD_INSTALL_RESULT"));
        }
    }

    public final void unregisterInstallReceiver() {
        if (this.mInstallReceiver != null) {
            DefaultLogger.d("MarketInstaller", "unregister install receiver");
            GalleryApp.sGetAndroidContext().unregisterReceiver(this.mInstallReceiver);
            this.mInstallReceiver = null;
        }
    }

    /* loaded from: classes2.dex */
    public class DownloadReceiver extends MarketDownloadReceiver {
        public DownloadReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("packageName");
            DefaultLogger.d("MarketInstaller", "package name %s", stringExtra);
            if (!TextUtils.equals(stringExtra, MarketInstaller.this.getPackageName())) {
                return;
            }
            int intExtra = intent.getIntExtra("errorCode", 0);
            int intExtra2 = intent.getIntExtra("status", 0);
            DefaultLogger.d("MarketInstaller", "install error code: %d, download status: %d", Integer.valueOf(intExtra), Integer.valueOf(intExtra2));
            if (isInstallExists(intExtra)) {
                MarketInstaller.this.startInstall(true);
                MarketInstaller.this.mInstallState = 1;
            } else if (!isInstallFinish(intExtra)) {
                if (intExtra2 == -3) {
                    MarketInstaller.this.mInstallState = 2;
                } else {
                    MarketInstaller.this.mInstallState = 1;
                }
            } else {
                int intExtra3 = intent.getIntExtra("reason", 0);
                if (isInstallSuccess(intExtra)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("name", MarketInstaller.this.getPackageName());
                    SamplingStatHelper.recordCountEvent("market_install", "install_success", hashMap);
                    MarketInstaller.this.onInstallSuccess();
                    DefaultLogger.d("MarketInstaller", "Install success");
                } else {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("name", MarketInstaller.this.getPackageName());
                    hashMap2.put("reason", intExtra + "_" + intExtra3);
                    SamplingStatHelper.recordCountEvent("market_install", "install_fail", hashMap2);
                    MarketInstaller.this.onInstallFail(intExtra, intExtra3);
                    DefaultLogger.w("MarketInstaller", "install fail, error code : %d, reason : %d", Integer.valueOf(intExtra), Integer.valueOf(intExtra3));
                }
                MarketInstaller marketInstaller = MarketInstaller.this;
                marketInstaller.mInstallState = 0;
                marketInstaller.unregisterInstallReceiver();
            }
        }
    }
}
