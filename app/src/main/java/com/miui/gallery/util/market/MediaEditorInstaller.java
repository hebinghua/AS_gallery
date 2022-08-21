package com.miui.gallery.util.market;

import android.content.res.Resources;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.utils.NetworkUtils;

/* loaded from: classes2.dex */
public final class MediaEditorInstaller extends MarketInstaller {
    public Callback mInstallListener;

    /* loaded from: classes2.dex */
    public interface Callback {
        default void onDialogCancel() {
        }

        default void onDialogConfirm() {
        }

        default void onInstallSuccess() {
        }
    }

    /* renamed from: $r8$lambda$v_ncpuWigs7I9Bh2pY_-6tKd--s */
    public static /* synthetic */ void m1740$r8$lambda$v_ncpuWigs7I9Bh2pY_6tKds(MediaEditorInstaller mediaEditorInstaller, boolean z, boolean z2) {
        mediaEditorInstaller.lambda$judgeNetState$0(z, z2);
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public long getPackageMinVersion() {
        return 0L;
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public String getPackageName() {
        return "com.miui.mediaeditor";
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallLimit() {
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallResume() {
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallStart() {
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstalling() {
    }

    /* loaded from: classes2.dex */
    public static final class Singleton {
        public static final MediaEditorInstaller INSTANCE = new MediaEditorInstaller();
    }

    public MediaEditorInstaller() {
    }

    public static final MediaEditorInstaller getInstance() {
        return Singleton.INSTANCE;
    }

    public static boolean isShowDownloadMediaEditorAppDialog() {
        return GalleryPreferences.DownloadMediaEditorAppPres.getShowDownloadMediaEditorAppCount() < 1;
    }

    public final void checkCTAPermission(final FragmentActivity fragmentActivity) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            AgreementsUtils.showUserAgreements(fragmentActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.util.market.MediaEditorInstaller.1
                {
                    MediaEditorInstaller.this = this;
                }

                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public void onAgreementInvoked(boolean z) {
                    if (z) {
                        MediaEditorInstaller.this.judgeNetState(fragmentActivity);
                    }
                }
            });
        } else {
            judgeNetState(fragmentActivity);
        }
    }

    public final void judgeNetState(FragmentActivity fragmentActivity) {
        if (NetworkUtils.isNetworkAvailable(fragmentActivity)) {
            if (BaseNetworkUtils.isActiveNetworkMetered()) {
                NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.util.market.MediaEditorInstaller$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                    public final void onConfirmed(boolean z, boolean z2) {
                        MediaEditorInstaller.m1740$r8$lambda$v_ncpuWigs7I9Bh2pY_6tKds(MediaEditorInstaller.this, z, z2);
                    }
                });
                return;
            } else {
                installPackage();
                return;
            }
        }
        ToastUtils.makeText(fragmentActivity, (int) R.string.no_network_connect);
    }

    public /* synthetic */ void lambda$judgeNetState$0(boolean z, boolean z2) {
        if (z) {
            installPackage();
        }
    }

    public boolean installIfNotExist(final FragmentActivity fragmentActivity, Callback callback, boolean z) {
        if (isPackageAvailable()) {
            return true;
        }
        this.mInstallListener = callback;
        if (z) {
            checkCTAPermission(fragmentActivity);
            return true;
        } else if (!isShowDownloadMediaEditorAppDialog()) {
            return true;
        } else {
            Resources resources = fragmentActivity.getResources();
            ConfirmDialog.showConfirmDialog(fragmentActivity.getSupportFragmentManager(), resources.getString(R.string.install_media_editor_title), resources.getString(R.string.install_media_editor_message), resources.getString(R.string.cancel), resources.getString(R.string.download_tip), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.util.market.MediaEditorInstaller.2
                {
                    MediaEditorInstaller.this = this;
                }

                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onConfirm(DialogFragment dialogFragment) {
                    MediaEditorInstaller.this.checkCTAPermission(fragmentActivity);
                    if (MediaEditorInstaller.this.mInstallListener != null) {
                        MediaEditorInstaller.this.mInstallListener.onDialogConfirm();
                    }
                }

                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onCancel(DialogFragment dialogFragment) {
                    if (MediaEditorInstaller.this.mInstallListener != null) {
                        MediaEditorInstaller.this.mInstallListener.onDialogCancel();
                    }
                }
            });
            DefaultLogger.d("MediaEditorInstaller", "show download mediaeditor app dialog");
            GalleryPreferences.DownloadMediaEditorAppPres.increaseShowDownloadMediaEditorAppCount();
            return false;
        }
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallFail(int i, int i2) {
        DefaultLogger.d("MediaEditorInstaller", "onInstallFail: " + i + "," + i2);
        if (i == -6) {
            return;
        }
        if (i == -4 && i2 == -1) {
            return;
        }
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), i2 != 10 ? (i2 == 11 || i2 == 16) ? R.string.macarons_download_storage_fail : i2 != 28 ? R.string.macarons_download_other_fail : R.string.macarons_download_network_fail : R.string.macarons_download_sdscard_fail);
    }

    @Override // com.miui.gallery.util.market.MarketInstaller
    public void onInstallSuccess() {
        Callback callback = this.mInstallListener;
        if (callback != null) {
            callback.onInstallSuccess();
        }
    }

    public void removeInstallListener() {
        if (this.mInstallListener != null) {
            this.mInstallListener = null;
        }
    }
}
