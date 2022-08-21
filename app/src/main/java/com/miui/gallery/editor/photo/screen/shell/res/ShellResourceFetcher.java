package com.miui.gallery.editor.photo.screen.shell.res;

import android.os.Build;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.net.fetch.FetchManager;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ToastUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ShellResourceFetcher {
    public static final ShellResourceFetcher INSTANCE = new ShellResourceFetcher();
    public static HashMap<String, Long> sResIdMap;
    public List<Request> mRequestList = new ArrayList();

    static {
        HashMap<String, Long> hashMap = new HashMap<>();
        sResIdMap = hashMap;
        hashMap.put("davinci", 14473805317865600L);
        sResIdMap.put("davinciin", 14473805317865600L);
        sResIdMap.put("raphael", 14473805317865600L);
        sResIdMap.put("raphaelin", 14473805317865600L);
        sResIdMap.put("phoenix", 14473803215011968L);
        sResIdMap.put("phoenixin", 14473803215011968L);
        sResIdMap.put("picasso", 14473732091674720L);
        sResIdMap.put("picassoin", 14473732091674720L);
        sResIdMap.put("lmi", 14473854723621024L);
        sResIdMap.put("lmipro", 14473854723621024L);
        sResIdMap.put("lmiin", 14473854723621024L);
        sResIdMap.put("lmiinpro", 14473854723621024L);
        sResIdMap.put("cezanne", 14473856724959360L);
        sResIdMap.put("crux", 14473807342010528L);
        sResIdMap.put("cepheus", 13816016549183488L);
        sResIdMap.put("umi", 14138522745241632L);
        sResIdMap.put("cmi", 14138526378819616L);
        sResIdMap.put("cas", 14473858928017568L);
        sResIdMap.put("thyme", 15380255807897664L);
        sResIdMap.put("venus", 15380138500358208L);
        sResIdMap.put("cannon", 15997054898602176L);
        sResIdMap.put("lime", 15996996783177888L);
        sResIdMap.put("gauguinpro", 15996083264356448L);
    }

    public void checkFetch(FragmentActivity fragmentActivity, final Request.Listener listener) {
        if (getResId() == Long.MIN_VALUE) {
            return;
        }
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.screen_editor_shell_net_error);
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.screen_editor_shell_net_error);
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.editor.photo.screen.shell.res.ShellResourceFetcher$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    ShellResourceFetcher.this.lambda$checkFetch$0(listener, z, z2);
                }
            });
        } else {
            fetch(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFetch$0(Request.Listener listener, boolean z, boolean z2) {
        if (z) {
            fetch(listener);
        }
    }

    public void fetch(Request.Listener listener) {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.screen_editor_shell_res_downloading);
        ShellRequest shellRequest = new ShellRequest(getResId());
        shellRequest.setListener(listener);
        this.mRequestList.add(shellRequest);
        FetchManager.INSTANCE.enqueue(shellRequest);
    }

    public void cancelAll() {
        FetchManager.INSTANCE.cancel(this.mRequestList);
    }

    public static String getMaterialPath() {
        return ShellResourceFileConfig.resItemDir(getResId()).getPath();
    }

    public static boolean isResExist() {
        return ShellResourceFileConfig.exist(getResId());
    }

    public static long getResId() {
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            for (Map.Entry<String, Long> entry : sResIdMap.entrySet()) {
                if (str.equals(entry.getKey())) {
                    return entry.getValue().longValue();
                }
            }
            return Long.MIN_VALUE;
        }
        return Long.MIN_VALUE;
    }

    public static boolean hasShellRes() {
        return getResId() != Long.MIN_VALUE;
    }

    /* loaded from: classes2.dex */
    public static class ShellResourceFileConfig {
        public static String sResPath = GalleryApp.sGetAndroidContext().getFilesDir().getPath() + File.separator + "shell_resource";

        public static boolean exist(long j) {
            return resItemDir(j).exists();
        }

        public static void deleteHistoricVersion() {
            File resItemBaseDir = resItemBaseDir();
            if (!resItemBaseDir.exists()) {
                return;
            }
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(resItemBaseDir.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("ShellResourceFetcher", "deleteHistoricVersion"));
            if (documentFile == null) {
                return;
            }
            documentFile.delete();
        }

        public static File resItemBaseDir() {
            return new File(sResPath);
        }

        public static File resItemDir(long j) {
            return new File(resItemBaseDir(), String.valueOf(j));
        }

        public static File resItemZipFile() {
            return new File(sResPath, "shell.zip");
        }
    }
}
