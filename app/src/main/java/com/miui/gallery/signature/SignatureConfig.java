package com.miui.gallery.signature;

import android.content.Context;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import java.io.File;

/* loaded from: classes2.dex */
public class SignatureConfig {
    public static File getSignatureFolder(Context context) {
        File file = new File(context.getFilesDir() + File.separator + "signature_folder");
        if (!file.exists()) {
            StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("SignatureConfig", "getSignatureFolder"));
        }
        return file;
    }
}
