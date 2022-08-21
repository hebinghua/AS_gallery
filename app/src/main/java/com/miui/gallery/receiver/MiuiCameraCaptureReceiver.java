package com.miui.gallery.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.activity.BaseExternalPhotoPageActivity;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.DecodeInfoHelper;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;
import java.io.File;

/* loaded from: classes2.dex */
public class MiuiCameraCaptureReceiver extends BroadcastReceiver {
    public static long sLastPreloadTime;
    public static final PreloadBigPhotoRunnable sPreloadBigPhotoRunnable = new PreloadBigPhotoRunnable();

    public final Uri parseMediaStoreUri(Intent intent) {
        Uri uri = (Uri) intent.getParcelableExtra("extra_media_store_uri");
        if (uri == null) {
            String stringExtra = intent.getStringExtra("extra_file_path");
            long longExtra = intent.getLongExtra("extra_media_store_id", -1L);
            if (longExtra <= 0) {
                return null;
            }
            return MediaStoreUtils.getFileMediaUri(StorageUtils.getMediaStoreVolumeName(GalleryApp.sGetAndroidContext(), stringExtra), longExtra, BaseFileMimeUtil.getMimeType(stringExtra));
        }
        return uri;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onReceive(final android.content.Context r19, final android.content.Intent r20) {
        /*
            Method dump skipped, instructions count: 291
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.receiver.MiuiCameraCaptureReceiver.onReceive(android.content.Context, android.content.Intent):void");
    }

    /* loaded from: classes2.dex */
    public static class PreloadBigPhotoRunnable implements Runnable {
        public long mFileLength;
        public String mFilePath;
        public boolean mIsBlurred;
        public boolean mIsTemp;
        public Future mLoadingTask;
        public Uri mMediaStoreUri;

        public PreloadBigPhotoRunnable() {
            this.mMediaStoreUri = null;
        }

        public void setData(String str, Uri uri, boolean z, boolean z2, long j) {
            cancel();
            this.mFilePath = str;
            this.mMediaStoreUri = uri;
            this.mIsTemp = z;
            this.mIsBlurred = z2;
            this.mFileLength = j;
        }

        public final void cancel() {
            Future future = this.mLoadingTask;
            if (future != null) {
                future.cancel();
                ((PreloadJob) this.mLoadingTask.getJob()).cancel();
                this.mLoadingTask = null;
                DefaultLogger.d("MiuiCameraCaptureReceiver", "cancel requesting for %s", this.mFilePath);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!TextUtils.isEmpty(this.mFilePath) || this.mMediaStoreUri != null) {
                this.mLoadingTask = ThreadManager.getMiscPool().submit(new PreloadJob(this.mFilePath, this.mMediaStoreUri, this.mIsTemp, this.mIsBlurred, this.mFileLength));
            }
        }

        /* loaded from: classes2.dex */
        public static class PreloadJob implements ThreadPool.Job<Void> {
            public final long mFileLength;
            public final String mFilePath;
            public final boolean mIsBlurred;
            public final boolean mIsTemp;
            public Uri mMediaStoreUri;
            public volatile Target<Bitmap> mPreloadTarget;

            public PreloadJob(String str, Uri uri, boolean z, boolean z2, long j) {
                this.mFilePath = str;
                this.mMediaStoreUri = uri;
                this.mIsTemp = z;
                this.mIsBlurred = z2;
                this.mFileLength = j;
            }

            public void cancel() {
                if (this.mPreloadTarget != null) {
                    Glide.with(GalleryApp.sGetAndroidContext()).clear(this.mPreloadTarget);
                }
            }

            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                if (jobContext.isCancelled()) {
                    return null;
                }
                if (!PermissionUtils.checkPermission(GalleryApp.sGetAndroidContext(), "android.permission.READ_EXTERNAL_STORAGE")) {
                    DefaultLogger.w("MiuiCameraCaptureReceiver", "Can't access external storage, relate permission is ungranted");
                    return null;
                }
                if (this.mMediaStoreUri == null) {
                    this.mMediaStoreUri = Build.VERSION.SDK_INT >= 29 ? getFileMediaUri(this.mFilePath) : MediaStoreUtils.getFileMediaUri(this.mFilePath);
                }
                if (!jobContext.isCancelled() && this.mMediaStoreUri != null && !MiscUtil.isAppProcessInForeground()) {
                    this.mPreloadTarget = BaseExternalPhotoPageActivity.preloadThumbnail(this.mMediaStoreUri.toString(), this.mIsTemp, this.mIsBlurred, BaseFileMimeUtil.getMimeType(this.mFilePath), this.mFileLength);
                }
                return null;
            }

            public static Uri getFileMediaUri(final String str) {
                Uri contentUri;
                if (TextUtils.isEmpty(str)) {
                    return null;
                }
                final String mediaStoreVolumeName = StorageUtils.getMediaStoreVolumeName(StaticContext.sGetAndroidContext(), str);
                String format = String.format("%s='%s'", "_data", str);
                Context sGetAndroidContext = StaticContext.sGetAndroidContext();
                if (BaseFileMimeUtil.isImageFromMimeType(BaseFileMimeUtil.getMimeType(str))) {
                    contentUri = MediaStore.Images.Media.getContentUri("external");
                } else {
                    contentUri = MediaStore.Video.Media.getContentUri("external");
                }
                return (Uri) SafeDBUtil.safeQuery(sGetAndroidContext, contentUri, new String[]{j.c, "_data", "mime_type", "date_modified", nexExportFormat.TAG_FORMAT_WIDTH, nexExportFormat.TAG_FORMAT_HEIGHT}, format, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Uri>() { // from class: com.miui.gallery.receiver.MiuiCameraCaptureReceiver.PreloadBigPhotoRunnable.PreloadJob.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                    /* renamed from: handle */
                    public Uri mo1808handle(Cursor cursor) {
                        if (cursor == null || !cursor.moveToFirst()) {
                            return null;
                        }
                        long j = cursor.getLong(0);
                        String string = cursor.getString(2);
                        if (BaseFileMimeUtil.isImageFromMimeType(string)) {
                            DecodeInfoHelper.DecodeInfo decodeInfo = new DecodeInfoHelper.DecodeInfo();
                            decodeInfo.lastModified = cursor.getLong(3);
                            decodeInfo.fileUri = Uri.fromFile(new File(str));
                            decodeInfo.mediaUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), cursor.getLong(0));
                            decodeInfo.width = cursor.getInt(4);
                            decodeInfo.height = cursor.getInt(5);
                            DecodeInfoHelper.getInstance().put(decodeInfo);
                        }
                        return MediaStoreUtils.getFileMediaUri(mediaStoreVolumeName, j, string);
                    }
                });
            }
        }
    }
}
