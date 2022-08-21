package com.miui.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.photosapi.PhotosOemApi;
import com.miui.gallery.provider.ProcessingMediaManager;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes2.dex */
public class CameraParallelProcessingSetLoader extends MediaStoreImageSetLoader {
    @Override // com.miui.gallery.loader.MediaSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String getOrder() {
        return "date_taken DESC, media_store_id DESC";
    }

    @Override // com.miui.gallery.loader.MediaSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String[] getSelectionArgs() {
        return null;
    }

    @Override // com.miui.gallery.loader.MediaStoreImageSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String getTAG() {
        return "CameraParallelProcessingSetLoader";
    }

    public CameraParallelProcessingSetLoader(Context context, Uri uri, Bundle bundle, boolean z) {
        super(context, uri, bundle, z);
    }

    @Override // com.miui.gallery.loader.MediaStoreImageSetLoader, com.miui.gallery.loader.MediaSetLoader
    public Uri getContentUri(boolean z) {
        if (z) {
            return null;
        }
        return PhotosOemApi.getQueryProcessingUri(getContext());
    }

    @Override // com.miui.gallery.loader.MediaStoreImageSetLoader, com.miui.gallery.loader.MediaSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String getSelection() {
        return "start_time>" + (System.currentTimeMillis() - 40000) + " AND media_store_id IS NOT NULL";
    }

    @Override // com.miui.gallery.loader.MediaStoreImageSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String[] getProjection() {
        return new String[]{"media_store_id", "media_path", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "mime_type", "date_taken", nexExportFormat.TAG_FORMAT_WIDTH, nexExportFormat.TAG_FORMAT_HEIGHT, "jpeg_rotation"};
    }

    @Override // com.miui.gallery.loader.MediaSetLoader, com.miui.gallery.loader.CursorSetLoader
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, String str3) {
        if (ProcessingMediaManager.CAMERA_PROVIDER_VERSION.get(null).intValue() < 3) {
            return null;
        }
        return super.query(uri, strArr, str, strArr2, str2, str3);
    }

    @Override // com.miui.gallery.loader.MediaStoreImageSetLoader, com.miui.gallery.loader.CursorSetLoader
    public CursorDataSet wrapDataSet(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return super.wrapDataSet(cursor);
    }
}
