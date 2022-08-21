package com.miui.gallery.storage.strategies.android30.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.miui.gallery.storage.exceptions.StorageException;
import com.miui.gallery.storage.utils.IMediaStoreIdResolver;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import com.xiaomi.stat.a.j;
import java.util.Locale;

/* loaded from: classes2.dex */
public class MediaStoreIdResolver implements IMediaStoreIdResolver {
    public final Context mApplicationContext;
    public final IMediaStoreIdResolver mInnerResolver;

    @Override // com.miui.gallery.storage.utils.IStorageFunction
    public boolean handles(Context context, int i, int i2) {
        return true;
    }

    public MediaStoreIdResolver(Context context, IMediaStoreIdResolver iMediaStoreIdResolver) {
        this.mApplicationContext = context;
        this.mInnerResolver = iMediaStoreIdResolver;
    }

    @Override // com.miui.gallery.storage.utils.IMediaStoreIdResolver
    public long getMediaStoreId(String str) {
        IMediaStoreIdResolver iMediaStoreIdResolver = this.mInnerResolver;
        if (iMediaStoreIdResolver != null) {
            return iMediaStoreIdResolver.getMediaStoreId(str);
        }
        long mediaStoreIdInner1 = getMediaStoreIdInner1(str);
        return mediaStoreIdInner1 != -1 ? mediaStoreIdInner1 : getMediaStoreIdInner2(str);
    }

    public final long getMediaStoreIdInner1(String str) {
        Uri uri = getUri(this.mApplicationContext, str);
        if (uri == null) {
            return -1L;
        }
        Cursor query = this.mApplicationContext.getContentResolver().query(uri, new String[]{j.c}, getQuerySelection1(str), null, null, null);
        try {
            if (query == null) {
                throw new StorageException("[getMediaStoreIdInner] invalid cursor.", new Object[0]);
            }
            if (!query.moveToFirst()) {
                query.close();
                return -1L;
            }
            long j = query.getLong(0);
            query.close();
            return j;
        } catch (Throwable th) {
            if (query != null) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public final long getMediaStoreIdInner2(String str) {
        Uri uri = getUri(this.mApplicationContext, str);
        if (uri == null) {
            return -1L;
        }
        Cursor query = this.mApplicationContext.getContentResolver().query(uri, new String[]{j.c}, getQuerySelection2(str), null, null, null);
        try {
            if (query == null) {
                throw new StorageException("[getMediaStoreIdInner] invalid cursor.", new Object[0]);
            }
            if (!query.moveToFirst()) {
                query.close();
                return -1L;
            }
            long j = query.getLong(0);
            query.close();
            return j;
        } catch (Throwable th) {
            if (query != null) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static String getQuerySelection1(String str) {
        return String.format(Locale.US, "bucket_id=%d AND _display_name='%s' COLLATE NOCASE", Integer.valueOf(BaseFileUtils.getBucketID(BaseFileUtils.getParentFolderPath(str))), BaseFileUtils.getFileName(str));
    }

    public static String getQuerySelection2(String str) {
        return String.format(Locale.US, "bucket_id=%d AND _data='%s' COLLATE NOCASE", Integer.valueOf(BaseFileUtils.getBucketID(BaseFileUtils.getParentFolderPath(str))), str);
    }

    public static Uri getUri(Context context, String str) {
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        String mediaStoreVolumeName = StorageUtils.getMediaStoreVolumeName(context, str);
        if (BaseFileMimeUtil.isImageFromMimeType(mimeType)) {
            return MediaStore.Images.Media.getContentUri(mediaStoreVolumeName);
        }
        if (BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
            return MediaStore.Video.Media.getContentUri(mediaStoreVolumeName);
        }
        return MediaStore.Files.getContentUri(mediaStoreVolumeName);
    }
}
