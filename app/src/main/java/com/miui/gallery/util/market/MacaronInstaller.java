package com.miui.gallery.util.market;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.MacaronStrategy;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.settings.Settings;
import com.xiaomi.micloudsdk.utils.NetworkUtils;

/* loaded from: classes2.dex */
public class MacaronInstaller extends MarketInstaller {
    public static final String[] sDeviceWhiteList;
    public static MacaronInstaller sInstance;
    public static boolean sIsDeviceRegionSupport;
    public static final String[] sRegionWhiteList = {"IN"};
    public static final String[] sUnSupportImageMimeType = {"image/x-adobe-dng", "image/gif", "image/heif", "image/heic"};
    public String mPackageName;

    public static boolean isFunctionOn() {
        return false;
    }

    public static synchronized MacaronInstaller getInstance() {
        MacaronInstaller macaronInstaller;
        synchronized (MacaronInstaller.class) {
            if (sInstance == null) {
                sInstance = new MacaronInstaller();
            }
            macaronInstaller = sInstance;
        }
        return macaronInstaller;
    }

    static {
        String[] strArr = {"begonia", "begoniain", "ginkgo"};
        sDeviceWhiteList = strArr;
        sIsDeviceRegionSupport = false;
        String region = Settings.getRegion();
        for (String str : strArr) {
            if (str.equalsIgnoreCase(Build.DEVICE)) {
                for (String str2 : sRegionWhiteList) {
                    if (str2.equalsIgnoreCase(region)) {
                        sIsDeviceRegionSupport = true;
                    }
                }
            }
        }
    }

    public static boolean isMimeTypeSupport(String str) {
        for (String str2 : sUnSupportImageMimeType) {
            if (str2.equalsIgnoreCase(str)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkInstall(FragmentActivity fragmentActivity) {
        if (isPackageAvailable()) {
            return true;
        }
        if (this.mInstallState == 0) {
            showInstallDialog(fragmentActivity);
            return false;
        }
        installPackage();
        return false;
    }

    public void showInstallDialog(final FragmentActivity fragmentActivity) {
        DialogUtil.showInfoDialog(fragmentActivity, (int) R.string.macarons_download_dialog_message, (int) R.string.macarons_download_dialog_title, (int) R.string.macarons_download_dialog_confirm, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.util.market.MacaronInstaller.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                MacaronInstaller.this.checkCTAPermission(fragmentActivity);
            }
        }, (DialogInterface.OnClickListener) null);
    }

    public final void checkCTAPermission(final FragmentActivity fragmentActivity) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            AgreementsUtils.showUserAgreements(fragmentActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.util.market.MacaronInstaller.2
                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public void onAgreementInvoked(boolean z) {
                    if (z) {
                        MacaronInstaller.this.judgeNetState(fragmentActivity);
                    }
                }
            });
        } else {
            judgeNetState(fragmentActivity);
        }
    }

    public void judgeNetState(FragmentActivity fragmentActivity) {
        if (NetworkUtils.isNetworkAvailable(fragmentActivity)) {
            installPackage();
        } else {
            ToastUtils.makeText(fragmentActivity, (int) R.string.no_network_connect);
        }
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public long getPackageMinVersion() {
        MacaronStrategy macaronStrategy = CloudControlStrategyHelper.getMacaronStrategy();
        if (macaronStrategy != null) {
            return macaronStrategy.getMinVersion();
        }
        return 0L;
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public String getPackageName() {
        String str = this.mPackageName;
        if (str != null) {
            return str;
        }
        MacaronStrategy macaronStrategy = CloudControlStrategyHelper.getMacaronStrategy();
        if (macaronStrategy != null) {
            this.mPackageName = macaronStrategy.getPackageName();
        }
        return this.mPackageName;
    }

    public Intent getJumpIntent() {
        Intent intent = new Intent();
        MacaronStrategy macaronStrategy = CloudControlStrategyHelper.getMacaronStrategy();
        if (macaronStrategy == null) {
            return intent;
        }
        intent.setAction(macaronStrategy.getIntentAction());
        intent.setFlags(1);
        return intent;
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstalling() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.macarons_downloading);
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallLimit() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.macarons_download_later);
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallStart() {
        if (BaseBuildUtil.isInternational()) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.macarons_downloading);
        }
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallResume() {
        if (BaseBuildUtil.isInternational()) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.macarons_downloading);
        }
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallFail(int i, int i2) {
        if (i == -6) {
            return;
        }
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), i2 != 10 ? (i2 == 11 || i2 == 16) ? R.string.macarons_download_storage_fail : i2 != 28 ? R.string.macarons_download_other_fail : R.string.macarons_download_network_fail : R.string.macarons_download_sdscard_fail);
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallSuccess() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.macarons_download_success);
    }
}
