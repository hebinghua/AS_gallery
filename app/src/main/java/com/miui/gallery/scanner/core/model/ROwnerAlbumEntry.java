package com.miui.gallery.scanner.core.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: classes2.dex */
public class ROwnerAlbumEntry extends OwnerAlbumEntry {
    public int mTargetPublicMediaCount;
    public long mTargetPublicMediaGenerationModifiedSum;

    @Override // com.miui.gallery.scanner.core.model.OwnerAlbumEntry, com.miui.gallery.scanner.core.model.OwnerEntry
    public boolean isLatest(BasicFileAttributes basicFileAttributes) {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        String[] absolutePath = StorageUtils.getAbsolutePath(GalleryApp.sGetAndroidContext(), StorageUtils.ensureCommonRelativePath(this.mLocalPath));
        if (absolutePath == null || absolutePath.length == 0 || absolutePath.length > 2) {
            return false;
        }
        return super.isLatest(basicFileAttributes) && checkPublicMediaIsLatest(sGetAndroidContext, absolutePath);
    }

    public final boolean checkPublicMediaIsLatest(Context context, String[] strArr) {
        for (String str : strArr) {
            int bucketID = BaseFileUtils.getBucketID(str);
            Cursor query = context.getContentResolver().query(MediaStore.Images.Media.getContentUri("external"), new String[]{"COUNT(generation_modified)", "SUM(generation_modified)"}, "bucket_id='" + bucketID + "'", null, null);
            if (query != null) {
                try {
                    if (query.getCount() > 0 && query.moveToFirst()) {
                        this.mTargetPublicMediaCount = (int) (this.mTargetPublicMediaCount + query.getLong(0));
                        this.mTargetPublicMediaGenerationModifiedSum += query.getLong(1);
                        query.close();
                        query = context.getContentResolver().query(MediaStore.Video.Media.getContentUri("external"), new String[]{"COUNT(generation_modified)", "SUM(generation_modified)"}, "bucket_id='" + bucketID + "'", null, null);
                        if (query != null) {
                            try {
                                if (query.getCount() > 0 && query.moveToFirst()) {
                                    this.mTargetPublicMediaCount = (int) (this.mTargetPublicMediaCount + query.getLong(0));
                                    this.mTargetPublicMediaGenerationModifiedSum += query.getLong(1);
                                    query.close();
                                }
                            } finally {
                            }
                        }
                        if (query != null) {
                            query.close();
                        }
                        return true;
                    }
                } finally {
                }
            }
            if (query != null) {
                query.close();
            }
            return true;
        }
        return this.mPublicMediaCount == this.mTargetPublicMediaCount && this.mPublicMediaGenerationModifiedSum == this.mTargetPublicMediaGenerationModifiedSum;
    }

    @Override // com.miui.gallery.scanner.core.model.OwnerAlbumEntry
    public void updatePublicMediaStatus(Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("scan_public_media_count", Integer.valueOf(this.mTargetPublicMediaCount));
        contentValues.put("scan_public_media_generation_modified", Long.valueOf(this.mTargetPublicMediaGenerationModifiedSum));
        SafeDBUtil.safeUpdate(context, GalleryContract.Album.URI, contentValues, "_id=?", new String[]{String.valueOf(this.mId)});
        DefaultLogger.d("ROwnerAlbumEntry", "update album [%s] public media count from [%d] to [%d].", this.mLocalPath, Integer.valueOf(this.mPublicMediaCount), Integer.valueOf(this.mTargetPublicMediaCount));
        DefaultLogger.d("ROwnerAlbumEntry", "update album [%s] public media generation modified sum from [%d] to [%d].", this.mLocalPath, Long.valueOf(this.mPublicMediaGenerationModifiedSum), Long.valueOf(this.mTargetPublicMediaGenerationModifiedSum));
    }
}
