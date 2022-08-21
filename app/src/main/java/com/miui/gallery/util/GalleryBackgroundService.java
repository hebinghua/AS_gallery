package com.miui.gallery.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.activity.BaseExternalPhotoPageActivity;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryBackgroundService extends Service {
    public MessengerHandler mHandler = new MessengerHandler();
    public Messenger mMessenger = new Messenger(this.mHandler);
    public Future<Void> mPreloadFuture;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        DefaultLogger.d("photoPageStartup", "GalleryBackgroundService onCreate ");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        DefaultLogger.d("photoPageStartup", "GalleryBackgroundService onBind");
        TimeMonitor.cancelTimeMonitor("403.1.0.1.13757");
        cancel();
        this.mPreloadFuture = ThreadManager.getMiscPool().submit(new PreloadJob());
        return this.mMessenger.getBinder();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        DefaultLogger.d("GalleryBackgroundService", "GalleryBackgroundService onDestroy");
        cancel();
    }

    public final void cancel() {
        Future<Void> future = this.mPreloadFuture;
        if (future != null) {
            future.cancel();
            ((PreloadJob) this.mPreloadFuture.getJob()).cancel();
            this.mPreloadFuture = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class MessengerHandler extends Handler {
        public MessengerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            CameraPreviewParams create;
            super.handleMessage(message);
            if (message.what == 0 && (create = CameraPreviewParams.create(message.getData())) != null) {
                DefaultLogger.d("photoPageStartup", "onHandleCameraPreviewParams, path: [%s], size: [%d x %d].", create.getUri(), Integer.valueOf(create.getWidth()), Integer.valueOf(create.getHeight()));
                CameraPreviewManager.getInstance().onHandleCameraPreviewParams(create);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class PreloadJob implements ThreadPool.Job<Void> {
        public volatile Target<Bitmap> mLoadingImage;

        public PreloadJob() {
        }

        public void cancel() {
            if (this.mLoadingImage != null) {
                Glide.with(GalleryApp.sGetAndroidContext()).clear(this.mLoadingImage);
            }
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run  reason: collision with other method in class */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            if (jobContext.isCancelled()) {
                return null;
            }
            if (!PermissionUtils.checkPermission(GalleryApp.sGetAndroidContext(), "android.permission.WRITE_EXTERNAL_STORAGE")) {
                DefaultLogger.w("GalleryBackgroundService", "Can't access external storage, relate permission is ungranted");
                return null;
            }
            MediaInfo lastMedia = getLastMedia();
            if (!jobContext.isCancelled() && lastMedia != null) {
                DefaultLogger.d("GalleryBackgroundService", "PreloadJob uri:%s", lastMedia.mUri);
                this.mLoadingImage = BaseExternalPhotoPageActivity.preloadThumbnail(lastMedia.mUri, lastMedia.mMimeType, lastMedia.mSize);
            }
            return null;
        }

        public static MediaInfo getLastMedia() {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            String[] genAllBucketIds = BucketIdUtils.genAllBucketIds(sGetAndroidContext, MIUIStorageConstants.DIRECTORY_CAMERA_PATH, null);
            MediaInfo lastMedia = getLastMedia(sGetAndroidContext, MediaStore.Images.Media.getContentUri("external"), genAllBucketIds);
            MediaInfo lastMedia2 = getLastMedia(sGetAndroidContext, MediaStore.Video.Media.getContentUri("external"), genAllBucketIds);
            return lastMedia == null ? lastMedia2 : (lastMedia2 != null && lastMedia.mDateModified <= lastMedia2.mDateModified) ? lastMedia2 : lastMedia;
        }

        /* JADX WARN: Code restructure failed: missing block: B:22:0x00b9, code lost:
            r6.close();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static com.miui.gallery.util.GalleryBackgroundService.PreloadJob.MediaInfo getLastMedia(android.content.Context r6, android.net.Uri r7, java.lang.String[] r8) {
            /*
                android.os.Bundle r0 = new android.os.Bundle
                r0.<init>()
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "bucket_id IN ("
                r1.append(r2)
                java.lang.String r2 = ", "
                java.lang.String r8 = android.text.TextUtils.join(r2, r8)
                r1.append(r8)
                java.lang.String r8 = ") AND "
                r1.append(r8)
                java.lang.String r8 = "_size"
                r1.append(r8)
                java.lang.String r2 = " > 0"
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                java.lang.String r2 = "android:query-arg-sql-selection"
                r0.putString(r2, r1)
                java.lang.String r1 = "android:query-arg-sql-sort-order"
                java.lang.String r2 = "datetaken DESC, _id DESC"
                r0.putString(r1, r2)
                android.content.Context r1 = com.miui.gallery.GalleryApp.sGetAndroidContext()
                android.content.pm.ApplicationInfo r1 = r1.getApplicationInfo()
                int r1 = r1.targetSdkVersion
                r2 = 1
                r3 = 30
                if (r1 < r3) goto L4f
                java.lang.String r1 = java.lang.String.valueOf(r2)
                java.lang.String r3 = "android:query-arg-sql-limit"
                r0.putString(r3, r1)
            L4f:
                android.content.ContentResolver r6 = r6.getContentResolver()
                android.net.Uri$Builder r1 = r7.buildUpon()
                java.lang.String r3 = "limit"
                java.lang.String r4 = "1"
                android.net.Uri$Builder r1 = r1.appendQueryParameter(r3, r4)
                android.net.Uri r1 = r1.build()
                java.lang.String r3 = "_id"
                java.lang.String r4 = "mime_type"
                java.lang.String r5 = "date_modified"
                java.lang.String[] r8 = new java.lang.String[]{r3, r4, r8, r5}
                r3 = 0
                android.database.Cursor r6 = r6.query(r1, r8, r0, r3)
                if (r6 == 0) goto Lb7
                int r8 = r6.getCount()     // Catch: java.lang.Throwable -> Lad
                if (r8 <= 0) goto Lb7
                boolean r8 = r6.moveToFirst()     // Catch: java.lang.Throwable -> Lad
                if (r8 != 0) goto L81
                goto Lb7
            L81:
                com.miui.gallery.util.GalleryBackgroundService$PreloadJob$MediaInfo r8 = new com.miui.gallery.util.GalleryBackgroundService$PreloadJob$MediaInfo     // Catch: java.lang.Throwable -> Lad
                r8.<init>()     // Catch: java.lang.Throwable -> Lad
                r0 = 0
                long r0 = r6.getLong(r0)     // Catch: java.lang.Throwable -> Lad
                android.net.Uri r7 = android.content.ContentUris.withAppendedId(r7, r0)     // Catch: java.lang.Throwable -> Lad
                java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> Lad
                r8.mUri = r7     // Catch: java.lang.Throwable -> Lad
                java.lang.String r7 = r6.getString(r2)     // Catch: java.lang.Throwable -> Lad
                r8.mMimeType = r7     // Catch: java.lang.Throwable -> Lad
                r7 = 2
                long r0 = r6.getLong(r7)     // Catch: java.lang.Throwable -> Lad
                r8.mSize = r0     // Catch: java.lang.Throwable -> Lad
                r7 = 3
                long r0 = r6.getLong(r7)     // Catch: java.lang.Throwable -> Lad
                r8.mDateModified = r0     // Catch: java.lang.Throwable -> Lad
                r6.close()
                return r8
            Lad:
                r7 = move-exception
                r6.close()     // Catch: java.lang.Throwable -> Lb2
                goto Lb6
            Lb2:
                r6 = move-exception
                r7.addSuppressed(r6)
            Lb6:
                throw r7
            Lb7:
                if (r6 == 0) goto Lbc
                r6.close()
            Lbc:
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.GalleryBackgroundService.PreloadJob.getLastMedia(android.content.Context, android.net.Uri, java.lang.String[]):com.miui.gallery.util.GalleryBackgroundService$PreloadJob$MediaInfo");
        }

        /* loaded from: classes2.dex */
        public static class MediaInfo {
            public long mDateModified;
            public String mMimeType;
            public long mSize;
            public String mUri;

            public MediaInfo() {
            }
        }
    }
}
