package com.miui.gallery.editor.photo.app.sky.res;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyCheckHelper;
import com.miui.gallery.editor.photo.core.common.model.SkyData;
import com.miui.gallery.net.fetch.FetchManager;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class ResourceFetcher {
    public static HashMap<String, Long> sResIdMap;
    public List<Request> mRequestList = new ArrayList();

    static {
        HashMap<String, Long> hashMap = new HashMap<>();
        sResIdMap = hashMap;
        hashMap.put("sky_yuyun", 13320094108549248L);
        sResIdMap.put("sky_yuhui", 13320092374663264L);
        sResIdMap.put("sky_xizhao", 13320090658537632L);
        sResIdMap.put("sky_yingxia", 13320088731189408L);
        sResIdMap.put("sky_qixia", 13320086938517664L);
        sResIdMap.put("sky_bikong", 13320084707279008L);
        sResIdMap.put("sky_qingtian", 13320070372327520L);
        sResIdMap.put("sky_duoyun", 13320066930180192L);
        sResIdMap.put("sky_boyun", 13320064817234048L);
        sResIdMap.put("sky_zhaoxia", 13320062602707072L);
        sResIdMap.put("sky_hongni", 13320060517351552L);
        sResIdMap.put("sky_muguang", 13320058676510816L);
        sResIdMap.put("sky_wanxia", 13320055192158368L);
        sResIdMap.put("sky_yunxu", 13320053582266496L);
        sResIdMap.put("sky_caihong", 13320051419775136L);
        sResIdMap.put("sky_xuetian", 13320049219731616L);
        sResIdMap.put("sky_cengyun", 13320047191392416L);
        sResIdMap.put("sky_qingkong", 13320034187411584L);
        sResIdMap.put("sky_xiyang", 13320029484679328L);
        sResIdMap.put("sky_luoxia", 13320026521272480L);
        if (SkyCheckHelper.isLargeType()) {
            sResIdMap.put("sky_xuanyue", 14825185267351616L);
            sResIdMap.put("sky_haoyue", 14825181698457632L);
            sResIdMap.put("sky_yingyue", 14825188294066368L);
        } else {
            sResIdMap.put("sky_xuanyue", 14274508029624384L);
            sResIdMap.put("sky_haoyue", 14274505562390560L);
            sResIdMap.put("sky_yingyue", 13936586926063616L);
        }
        sResIdMap.put("sky_chenguang", 13936589242564608L);
        sResIdMap.put("sky_xuanguang", 13936590988312640L);
        sResIdMap.put("sky_shandian", 13936592561438720L);
        sResIdMap.put("sky_xingchen", 13936594872893440L);
        sResIdMap.put("sky_xingji", 13936596985774112L);
        sResIdMap.put("sky_xingui", 13936598720249856L);
        sResIdMap.put("sky_yinhe", 13936600750227456L);
        sResIdMap.put("sky_xinghe", 13936602767818752L);
        sResIdMap.put("sky_shenkong", 13936604503998528L);
        sResIdMap.put("dynamic_sky_chuangyu", 13320005813862528L);
        sResIdMap.put("dynamic_sky_duoyun", 13320003944251552L);
        sResIdMap.put("dynamic_sky_jiyun", 13320000902987904L);
        sResIdMap.put("dynamic_sky_muguang", 13319999037767808L);
        sResIdMap.put("dynamic_sky_qingtian", 13319995866873952L);
        sResIdMap.put("dynamic_sky_xuxue", 13319992331337856L);
        sResIdMap.put("dynamic_sky_shuyu", 13975910539395104L);
        sResIdMap.put("dynamic_sky_yanhua", 14145345447264288L);
        sResIdMap.put("dynamic_sky_yinhe", 13936620245811232L);
        sResIdMap.put("dynamic_sky_text_yanhua", 14670914371059808L);
    }

    public void checkFetch(FragmentActivity fragmentActivity, final SkyData skyData, final Request.Listener listener) {
        if (!BaseNetworkUtils.isNetworkConnected() || !BaseGalleryPreferences.CTA.canConnectNetwork()) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.filter_sky_download_failed_msg);
            DefaultLogger.d("ResourceFetcher", "download sky data no network");
            listener.onFail();
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.editor.photo.app.sky.res.ResourceFetcher$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    ResourceFetcher.this.lambda$checkFetch$0(skyData, listener, z, z2);
                }
            });
        } else {
            fetch(skyData, listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFetch$0(SkyData skyData, Request.Listener listener, boolean z, boolean z2) {
        if (z) {
            fetch(skyData, listener);
        } else {
            listener.onFail();
        }
    }

    public void fetch(SkyData skyData, Request.Listener listener) {
        if (skyData == null || skyData.getMaterialName() == null) {
            if (listener == null) {
                return;
            }
            listener.onFail();
            return;
        }
        SkyRequest skyRequest = new SkyRequest(skyData.getMaterialName(), getResId(skyData));
        skyRequest.setListener(listener);
        this.mRequestList.add(skyRequest);
        FetchManager.INSTANCE.enqueue(skyRequest);
    }

    public void prepare() {
        if (new File(ResourceFileConfig.sOldResPath).exists()) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(ResourceFileConfig.sOldResPath, IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("ResourceFetcher", "prepare"));
            if (documentFile == null) {
                return;
            }
            documentFile.delete();
            DefaultLogger.d("ResourceFetcher", "remove old res dir");
        }
    }

    public void addDownloadStatus(List<SkyData> list) {
        for (SkyData skyData : list) {
            if (ResourceFileConfig.exist(skyData.getMaterialName(), getResId(skyData))) {
                skyData.setDownloadState(17);
            } else {
                skyData.setDownloadState(19);
            }
        }
    }

    public void release() {
        FetchManager.INSTANCE.cancel(this.mRequestList);
    }

    public static String getMaterialPath(SkyData skyData) {
        return ResourceFileConfig.resItemDir(skyData.getMaterialName(), getResId(skyData)).getPath();
    }

    public static long getResId(SkyData skyData) {
        Long l;
        if (skyData == null || (l = sResIdMap.get(skyData.getMaterialName())) == null) {
            return Long.MIN_VALUE;
        }
        return l.longValue();
    }

    /* loaded from: classes2.dex */
    public static class ResourceFileConfig {
        public static String sOldResPath;
        public static String sResPath;

        static {
            StringBuilder sb = new StringBuilder();
            sb.append(GalleryApp.sGetAndroidContext().getFilesDir().getPath());
            String str = File.separator;
            sb.append(str);
            sb.append("sky_res");
            sOldResPath = sb.toString();
            sResPath = GalleryApp.sGetAndroidContext().getFilesDir().getPath() + str + "sky_resource";
        }

        public static boolean exist(String str, long j) {
            return resItemDir(str, j).exists();
        }

        public static void deleteHistoricVersion(String str) {
            File resItemBaseDir = resItemBaseDir(str);
            if (resItemBaseDir == null || !resItemBaseDir.exists()) {
                return;
            }
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(resItemBaseDir.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("ResourceFetcher", "downloadFile"));
            if (documentFile == null) {
                return;
            }
            documentFile.delete();
        }

        public static File resItemBaseDir(String str) {
            if (str == null) {
                return null;
            }
            return new File(sResPath, str);
        }

        public static File resItemDir(String str, long j) {
            return new File(resItemBaseDir(str), String.valueOf(j));
        }

        public static File resItemZipFile(String str) {
            String str2 = sResPath;
            return new File(str2, str + ".zip");
        }
    }
}
