package com.miui.gallery.editor.photo.app.filter.portrait;

import android.os.Build;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class PortraitColorCheckHelper {
    public static PortraitColorCheckHelper sInstance = new PortraitColorCheckHelper();
    public static final String[] sWhiteList = {"begonia", "begoniain", "merlin", "merlinin", "merlinnfc", "monet", "monetin", "vangogh", "joyeuse", "excalibur", "durandal", "curtanacn", "curtana", "dipper", "ursa", "jason", "wayne", "platina", "chiron", "sagit", "polaris", "perseus", "equuleus", "sirius", "comet", "lavender", "capricorn", "lithium", "natrium", "scorpio", "gemini", "cepheus", "davinci", "davinciin", "grus", "raphael", "raphaelin", "pyxis", "vela", "crux", "beryllium", "ginkgo", "violet", "laurus", "phoenix", "phoenixin", "andromeda", "cmi", "umi", "lmi", "lmipro", "lmiin", "lmiinpro", "draco", "picasso", "picassoin", "tucana", "toco", "tocoin", "lancelot", "atom", "bomb", "gram", "galahad", "apollo", "cas", "apricot", "banana", "shiva", "cezanne", "gauguin", "surya", "gauguinpro", "karna", "cannon", "cannong", "lime", "citrus", "lemon", "pomelo", "venus", "gauguininpro", "star", "cetus", "courbet", "courbetin", "sweet", "sweetin", "haydn", "alioth", "mojito", "rainbow", "sunny", "rosemary", "maltose", "secret", "mars", "renoir", "ares", "aresin", "thyme", "haydnin", "aliothin", "chopin", "camellia", "camellian", "vayu", "bhima", "biloba", "odin", "vili", "enuma", "elish", "nabu", "argo", "agate", "agatein", "selene", "eos", "amber", "lisa", "mona", "evergo", "evergreen", "zeus", "cupid", "psyche", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "ingres", "poussin", "fleur", "miel", "viva", "vida", "thor", "loki", "zizhan", "zijin", "taoyao", "opal"};

    public static PortraitColorCheckHelper getInstance() {
        return sInstance;
    }

    public static boolean isPortraitEnable() {
        if (BuildUtil.isEditorProcess()) {
            return false;
        }
        if (BuildUtil.isBlackShark()) {
            return true;
        }
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("_");
            if (split != null && split.length > 0) {
                str = split[0];
            }
            for (String str2 : sWhiteList) {
                if (str2.equalsIgnoreCase(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPortraitColorAvailable() {
        if (!isPortraitEnable()) {
            return false;
        }
        return PortraitColorLibraryLoaderHelper.getInstance().isDownloaded();
    }

    public void startDownloadWithWifi() {
        if (!BaseNetworkUtils.isNetworkConnected() || BaseNetworkUtils.isActiveNetworkMetered()) {
            return;
        }
        download(false);
    }

    public void startDownloadWithCheck(final FragmentActivity fragmentActivity) {
        if (isDownloading()) {
            DefaultLogger.d("PortraitColorCheckHelper", "the sdk is downloading.");
        } else if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("PortraitColorCheckHelper", "download sdk failed ,cta not allowed");
            AgreementsUtils.showUserAgreements(fragmentActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorCheckHelper$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public final void onAgreementInvoked(boolean z) {
                    PortraitColorCheckHelper.this.lambda$startDownloadWithCheck$0(fragmentActivity, z);
                }
            });
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.photo_portrait_color_download_fail);
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorCheckHelper$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    PortraitColorCheckHelper.this.lambda$startDownloadWithCheck$1(z, z2);
                }
            });
        } else {
            download(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadWithCheck$0(FragmentActivity fragmentActivity, boolean z) {
        if (z) {
            startDownloadWithCheck(fragmentActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadWithCheck$1(boolean z, boolean z2) {
        if (z) {
            download(true);
        } else {
            DefaultLogger.d("PortraitColorCheckHelper", "the sdk cancel download.");
        }
    }

    public final void download(boolean z) {
        PortraitColorLibraryLoaderHelper.getInstance().startDownload(z);
    }

    public void setDownloadStateListener(PortraitColorDownloadStateListener portraitColorDownloadStateListener) {
        PortraitColorLibraryLoaderHelper.getInstance().setDownloadStateListener(portraitColorDownloadStateListener);
    }

    public void release() {
        PortraitColorLibraryLoaderHelper.getInstance().setDownloadStateListener(null);
    }

    public boolean isDownloading() {
        return PortraitColorLibraryLoaderHelper.getInstance().isDownloading();
    }

    public boolean isNeedDownload() {
        return PortraitColorLibraryLoaderHelper.getInstance().isNeedDownload();
    }
}
