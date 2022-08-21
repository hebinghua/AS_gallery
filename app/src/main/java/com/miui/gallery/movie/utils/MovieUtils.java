package com.miui.gallery.movie.utils;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MovieDependsModule;
import com.miui.gallery.movie.entity.ImageEntity;
import com.miui.gallery.movie.entity.MovieResource;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.ConvertFilepathUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MovieUtils {
    public static void goDetail(Context context, Uri uri) {
        MovieDependsModule movieDependsModule = (MovieDependsModule) ModuleRegistry.getModule(MovieDependsModule.class);
        if (movieDependsModule != null) {
            Intent intent = new Intent(context, movieDependsModule.getPhotoPagerClass());
            intent.setData(uri);
            intent.putExtra("com.miui.gallery.extra.deleting_include_cloud", true);
            context.startActivity(intent);
        }
    }

    public static List<ImageEntity> getImageFromClipData(Context context, Intent intent) {
        ArrayList arrayList = new ArrayList();
        ClipData clipData = intent.getClipData();
        if (clipData == null) {
            arrayList.add(new ImageEntity(ConvertFilepathUtil.getPath(context, intent.getData()), null));
            return arrayList;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("MovieUtils_", "getImageFromClipData");
        for (int i = 0; i < clipData.getItemCount(); i++) {
            ClipData.Item itemAt = clipData.getItemAt(i);
            if (itemAt != null) {
                String path = ConvertFilepathUtil.getPath(context, itemAt.getUri());
                String valueOf = String.valueOf(itemAt.getText());
                DefaultLogger.d("MovieUtils_", "getImageFromClipData path is %s ,%s", path, itemAt.getUri());
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                if (documentFile == null || !documentFile.exists()) {
                    DefaultLogger.e("MovieUtils_", "getImageFromClipData path is null %s", itemAt.getUri());
                } else {
                    arrayList.add(new ImageEntity(path, valueOf));
                }
            }
        }
        return arrayList;
    }

    public static void checkResourceExist(List<? extends MovieResource> list) {
        if (list == null) {
            return;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("MovieUtils_", "checkResourceExist");
        for (MovieResource movieResource : list) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(movieResource.getDownloadSrcPath(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile != null && documentFile.exists()) {
                movieResource.downloadState = 17;
            }
        }
    }
}
