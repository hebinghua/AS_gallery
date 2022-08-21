package com.miui.gallery.data;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.service.IntentServiceBase;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.StorageUtils;
import java.io.File;
import java.io.FilenameFilter;

/* loaded from: classes.dex */
public class BackgroundJobService extends IntentServiceBase {
    @Override // com.miui.gallery.service.IntentServiceBase
    public int getNotificationId() {
        return 11;
    }

    @Override // com.miui.gallery.service.IntentServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.IntentServiceBase, android.app.IntentService
    public void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        int intExtra = intent.getIntExtra("job", 0);
        if (intExtra == 2) {
            deleteSecretThumbnail();
            return;
        }
        throw new IllegalArgumentException("unsupported job: " + intExtra);
    }

    public final void deleteSecretThumbnail() {
        deleteSecretThumbnail(StorageUtils.getPathInPrimaryStorage("MIUI/Gallery/cloud/secretAlbum"));
        deleteSecretThumbnail(StorageUtils.getPathInSecondaryStorage("MIUI/Gallery/cloud/secretAlbum"));
    }

    public final void deleteSecretThumbnail(String str) {
        File[] listFiles;
        File file = new File(str);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("BackgroundJobService", "deleteSecretThumbnail");
        if (!file.exists() || (listFiles = file.listFiles(new FilenameFilter() { // from class: com.miui.gallery.data.BackgroundJobService.1
            @Override // java.io.FilenameFilter
            public boolean accept(File file2, String str2) {
                return str2.length() > 0 && str2.endsWith(".img");
            }
        })) == null) {
            return;
        }
        for (File file2 : listFiles) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(file2.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile != null) {
                documentFile.delete();
            }
        }
    }

    public static void startJobDeleteSecretThumbnail(Context context) {
        Intent intent = new Intent(context, BackgroundJobService.class);
        intent.putExtra("job", 2);
        BackgroundServiceHelper.startForegroundServiceIfNeed(context, intent);
    }
}
