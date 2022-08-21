package com.miui.gallery.magic.fetch;

import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.net.fetch.FetchManager;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ArtResourceFetcher {
    public static final ArtResourceFetcher INSTANCE = new ArtResourceFetcher();
    public List<Request> mRequestList = new ArrayList();

    public boolean isExistResource() {
        return ResourceFileConfig.exist("artphoto_windowfog", 14693595597635680L);
    }

    public void fetch(Request request) {
        this.mRequestList.add(request);
        FetchManager.INSTANCE.enqueue(request);
    }

    public String getResourceBasePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(ResourceFileConfig.sResPath);
        String str = File.separator;
        sb.append(str);
        sb.append("artphoto_windowfog");
        sb.append(str);
        sb.append(14693595597635680L);
        sb.append(str);
        return sb.toString();
    }

    public void cancelAll() {
        FetchManager.INSTANCE.cancel(this.mRequestList);
    }

    /* loaded from: classes2.dex */
    public static class ResourceFileConfig {
        public static String sResPath = MagicUtils.getGalleryApp().getFilesDir().getPath() + File.separator + "magic_image_effects";

        public static boolean exist(String str, long j) {
            File resItemDir = resItemDir(str, j);
            if (resItemDir == null) {
                return false;
            }
            return resItemDir.exists();
        }

        public static void deleteHistoricVersion(String str) {
            File resItemBaseDir = resItemBaseDir(str);
            if (resItemBaseDir != null && resItemBaseDir.exists()) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(resItemBaseDir.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("ArtResourceFetcher", "deleteHistoricVersion"));
                if (documentFile == null) {
                    return;
                }
                documentFile.delete();
            }
        }

        public static File resItemBaseDir(String str) {
            if (str == null) {
                return null;
            }
            return new File(sResPath, str);
        }

        public static File resItemDir(String str, long j) {
            if (resItemBaseDir(str) == null) {
                return null;
            }
            return new File(resItemBaseDir(str), String.valueOf(j));
        }

        public static File resItemZipFile(String str) {
            String str2 = sResPath;
            return new File(str2, str + ".zip");
        }
    }
}
