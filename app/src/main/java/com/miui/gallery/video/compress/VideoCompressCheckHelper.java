package com.miui.gallery.video.compress;

import android.os.Build;
import android.text.TextUtils;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class VideoCompressCheckHelper {
    public static VideoCompressCheckHelper sInstance;
    public static boolean sIsDeviceSupported;
    public static final String[] sWhiteList;

    static {
        String[] strArr = {"monet", "monetin", "vangogh", "joyeuse", "excalibur", "durandal", "curtanacn", "curtana", "picassoin", "picasso", "lmi", "lmipro", "lmiin", "lmiinpro", "umi", "cmi", "dipper", "ursa", "chiron", "sagit", "polaris", "perseus", "equuleus", "sirius", "comet", "capricorn", "lithium", "natrium", "scorpio", "gemini", "cepheus", "davinci", "davinciin", "grus", "raphael", "raphaelin", "pyxis", "vela", "crux", "beryllium", "tucana", "violet", "phoenix", "phoenixin", "andromeda", "draco", "toco", "tocoin", "gram", "apollo", "cas", "gauguin", "surya", "gauguinpro", "karna", "lime", "citrus", "lemon", "pomelo", "venus", "gauguininpro", "star", "cetus", "courbet", "courbetin", "sweet", "sweetin", "haydn", "alioth", "mojito", "rainbow", "sunny", "mars", "renoir", "thyme", "haydnin", "aliothin", "vayu", "bhima", "odin", "vili", "enuma", "elish", "nabu", "argo", "lisa", "mona", "zeus", "cupid", "psyche", "ingres", "poussin", "spes", "spesn", "veux", "peux", "munch", "fog", "thor", "loki", "taoyao", "zijin", "zizhan", "unicorn", "mayfly", "wind", "rain", "mayfly", "diting"};
        sWhiteList = strArr;
        int i = 0;
        sIsDeviceSupported = false;
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("_");
            if (split != null && split.length > 0) {
                str = split[0];
            }
            int length = strArr.length;
            while (true) {
                if (i >= length) {
                    break;
                } else if (strArr[i].equalsIgnoreCase(str)) {
                    sIsDeviceSupported = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        sInstance = new VideoCompressCheckHelper();
    }

    public static boolean isDeviceSupport() {
        return sIsDeviceSupported;
    }

    public static VideoCompressCheckHelper getInstance() {
        return sInstance;
    }

    public static boolean isVideoCompressAvailable() {
        return VideoCompressLibraryLoaderHelper.getInstance().isDownloaded();
    }

    public void startDownloadWithCheck(FragmentActivity fragmentActivity) {
        if (isDownloading()) {
            DefaultLogger.d("VideoCompressCheckHelper", "the sdk is downloading.");
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.video_compress_download_failed_for_notwork);
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            ConfirmDialog.showConfirmDialog(fragmentActivity.getSupportFragmentManager(), fragmentActivity.getResources().getString(R.string.video_compress_lib_download), fragmentActivity.getResources().getString(R.string.video_compress_lib_download_without_wifi, Float.valueOf(1.3f)), fragmentActivity.getResources().getString(R.string.base_btn_cancel), fragmentActivity.getResources().getString(R.string.base_btn_download), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.video.compress.VideoCompressCheckHelper.1
                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onConfirm(DialogFragment dialogFragment) {
                    VideoCompressCheckHelper.this.download(true);
                }

                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onCancel(DialogFragment dialogFragment) {
                    DefaultLogger.d("VideoCompressCheckHelper", "the sdk cancel download.");
                }
            });
        } else {
            download(false);
        }
    }

    public final void download(boolean z) {
        VideoCompressLibraryLoaderHelper.getInstance().startDownload(z);
    }

    public void setDownloadStateListener(VideoCompressDownloadStateListener videoCompressDownloadStateListener) {
        VideoCompressLibraryLoaderHelper.getInstance().setDownloadStateListener(videoCompressDownloadStateListener);
    }

    public void release() {
        VideoCompressLibraryLoaderHelper.getInstance().setDownloadStateListener(null);
    }

    public boolean isDownloading() {
        return VideoCompressLibraryLoaderHelper.getInstance().isDownloading();
    }
}
