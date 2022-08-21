package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.push.GalleryPushManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.recorder.RecorderManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class AddAccount {
    public static void onAddAccount(Context context, Account account) {
        GalleryPushManager.getInstance().onAddAccount(context, account);
        requestCloudControl();
        scanSecretFiles(context, account);
        RecorderManager.getInstance().onAddAccount();
    }

    public static void requestCloudControl() {
        boolean existXiaomiAccount = SyncUtil.existXiaomiAccount(GalleryApp.sGetAndroidContext());
        DefaultLogger.d("AddAccount", "Exist xiaomi Account %s", String.valueOf(existXiaomiAccount));
        if (existXiaomiAccount) {
            DefaultLogger.d("AddAccount", "Done request cloud control data, result %s", String.valueOf(new CloudControlRequestHelper(GalleryApp.sGetAndroidContext()).execRequestSync(true)));
        }
    }

    public static void scanSecretFiles(Context context, Account account) {
        String[] pathsInExternalStorage;
        File[] listFiles;
        String probeSecretFileName;
        if (context == null || account == null || TextUtils.isEmpty(account.name) || TextUtils.isEmpty(account.type) || (pathsInExternalStorage = StorageUtils.getPathsInExternalStorage(context, "MIUI/Gallery/cloud/secretAlbum")) == null || pathsInExternalStorage.length <= 0) {
            return;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("AddAccount", "scanSecretFiles");
        for (String str : pathsInExternalStorage) {
            if (!TextUtils.isEmpty(str)) {
                Set<String> queryExistSecretFiles = queryExistSecretFiles(context);
                File file = new File(str);
                if (file.exists() && file.isDirectory() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                    ArrayList arrayList = new ArrayList();
                    for (File file2 : listFiles) {
                        if (file2.isFile() && ((queryExistSecretFiles == null || !queryExistSecretFiles.contains(file2.getAbsolutePath())) && (probeSecretFileName = CloudUtils.SecretAlbumUtils.probeSecretFileName(file2.getName(), account.name)) != null)) {
                            DefaultLogger.d("AddAccount", "Recover secret file [%s] to album", file2.getAbsolutePath());
                            File file3 = new File(file2.getParent(), probeSecretFileName);
                            if (StorageSolutionProvider.get().moveFile(file2.getAbsolutePath(), file3.getAbsolutePath(), appendInvokerTag)) {
                                arrayList.add(new Uri.Builder().scheme(Action.FILE_ATTRIBUTE).path(file3.getAbsolutePath()).build());
                            }
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        try {
                            com.miui.gallery.provider.CloudUtils.addToSecret(context, arrayList);
                        } catch (StoragePermissionMissingException unused) {
                        }
                    }
                }
            }
        }
    }

    public static Set<String> queryExistSecretFiles(Context context) {
        return (Set) SafeDBUtil.safeQuery(context, GalleryContract.Cloud.CLOUD_URI, new String[]{"localFile", "thumbnailFile"}, DatabaseUtils.concatenateWhere(DatabaseUtils.concatenateWhere("serverType IN (1,2)", "(localGroupId=-1000)"), InternalContract$Cloud.ALIAS_FILE_VALID), (String[]) null, (String) null, AddAccount$$ExternalSyntheticLambda0.INSTANCE);
    }

    public static /* synthetic */ Set lambda$queryExistSecretFiles$0(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        HashSet hashSet = new HashSet();
        while (cursor.moveToNext()) {
            String string = cursor.getString(0);
            String string2 = cursor.getString(1);
            if (!TextUtils.isEmpty(string)) {
                hashSet.add(string);
            }
            if (!TextUtils.isEmpty(string2)) {
                hashSet.add(string2);
            }
        }
        return hashSet;
    }
}
