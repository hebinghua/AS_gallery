package com.miui.gallery.util.market;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.picker.uri.OriginUrlRequestor;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes2.dex */
public class PrintInstaller extends MarketInstaller {
    public static PrintInstaller sInstance;
    public CopyOnWriteArraySet<InstallStateListener> mInstallStateListeners = new CopyOnWriteArraySet<>();
    public String mPackageName;

    /* loaded from: classes2.dex */
    public interface InstallStateListener {
        void onFinish(boolean z, int i, int i2);

        void onInstallLimited();

        void onInstalling();
    }

    public static int getFailReasonMsg(int i, int i2) {
        if (i == -6) {
            return 0;
        }
        return i2 != 10 ? (i2 == 11 || i2 == 16) ? R.string.error_install_storage_not_enough : i2 != 28 ? R.string.install_fail : R.string.error_install_connect_fail : R.string.error_install_sdcard_not_available;
    }

    public boolean isPhotoPrintEnable() {
        return false;
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallResume() {
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallStart() {
    }

    public static synchronized PrintInstaller getInstance() {
        PrintInstaller printInstaller;
        synchronized (PrintInstaller.class) {
            if (sInstance == null) {
                sInstance = new PrintInstaller();
            }
            printInstaller = sInstance;
        }
        return printInstaller;
    }

    public boolean ensurePrintFucntionAvailable() {
        if (!isPackageAvailable()) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(CloudControlStrategyHelper.getCreationStrategy().getPrintIntentUri()));
        intent.putExtra("prodType", String.valueOf(2));
        intent.setFlags(1);
        if (BaseMiscUtil.isIntentSupported(intent)) {
            return true;
        }
        installPackage();
        return false;
    }

    public void printPhotos(Activity activity, Uri[] uriArr, List<OriginUrlRequestor.OriginInfo> list) {
        if (!ensurePrintFucntionAvailable()) {
            return;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(CloudControlStrategyHelper.getCreationStrategy().getPrintIntentUri()));
        intent.putExtra("prodType", String.valueOf(2));
        if (BaseMiscUtil.isValid(list)) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(list.get(i).toJson());
            }
            intent.putExtra("pick-result-origin-download-info", arrayList);
            intent.putExtra("pick-result-data", new ArrayList(Arrays.asList(uriArr)));
            intent.setFlags(1);
            if (BaseMiscUtil.isIntentSupported(intent)) {
                try {
                    activity.startActivity(intent);
                    return;
                } catch (ActivityNotFoundException unused) {
                    DefaultLogger.e("PrintInstaller", "Select images to print failed");
                    return;
                }
            }
            installPackage();
            return;
        }
        DefaultLogger.e("PrintInstaller", "No OriginInfo for print");
    }

    public void start(Context context) {
        boolean z;
        if (context == null) {
            return;
        }
        String packageName = getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            DefaultLogger.e("PrintInstaller", "get package from cloud control failed");
            return;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setPackage(packageName);
        if (BaseMiscUtil.isIntentSupported(intent)) {
            try {
                context.startActivity(intent);
                SamplingStatHelper.recordCountEvent("photo_print", "photo_print_app_start_success");
            } catch (ActivityNotFoundException unused) {
                DefaultLogger.e("PrintInstaller", "find print activity failed");
            }
            z = true;
        } else {
            installPackage();
            z = false;
        }
        if (!GalleryPreferences.PhotoPrint.isPrintFirstClicked()) {
            return;
        }
        GalleryPreferences.PhotoPrint.setIsPrintFirstClicked(false);
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.format(Locale.US, "isInstalled_%s", Boolean.valueOf(z)));
        hashMap.put("trigger_time", Integer.valueOf(GalleryPreferences.PhotoPrint.getSilentInstallTimes()));
        SamplingStatHelper.recordCountEvent("photo_print", "photo_print_first_clicked", hashMap);
    }

    public void addInstallStateListener(InstallStateListener installStateListener) {
        if (installStateListener != null) {
            this.mInstallStateListeners.add(installStateListener);
        }
    }

    public void removeInstallStateListener(InstallStateListener installStateListener) {
        if (installStateListener != null) {
            this.mInstallStateListeners.remove(installStateListener);
        }
    }

    public boolean isPrintInstalling() {
        return this.mInstallState == 1;
    }

    public void notifyInstallLimited() {
        if (!this.mInstallStateListeners.isEmpty()) {
            Iterator<InstallStateListener> it = this.mInstallStateListeners.iterator();
            while (it.hasNext()) {
                it.next().onInstallLimited();
            }
        }
    }

    public void notifyInstalling() {
        if (!this.mInstallStateListeners.isEmpty()) {
            Iterator<InstallStateListener> it = this.mInstallStateListeners.iterator();
            while (it.hasNext()) {
                it.next().onInstalling();
            }
        }
    }

    public void notifyInstallFinish(boolean z, int i, int i2) {
        if (!this.mInstallStateListeners.isEmpty()) {
            Iterator<InstallStateListener> it = this.mInstallStateListeners.iterator();
            while (it.hasNext()) {
                it.next().onFinish(z, i, i2);
            }
        }
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public long getPackageMinVersion() {
        return CloudControlStrategyHelper.getCreationStrategy().getMinPrintVersionCode();
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public String getPackageName() {
        String str = this.mPackageName;
        if (str != null) {
            return str;
        }
        String printPackageName = CloudControlStrategyHelper.getPrintPackageName();
        this.mPackageName = printPackageName;
        return printPackageName;
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstalling() {
        notifyInstalling();
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallLimit() {
        notifyInstallLimited();
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallFail(int i, int i2) {
        notifyInstallFinish(false, i, i2);
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallSuccess() {
        notifyInstallFinish(true, 0, 0);
    }
}
