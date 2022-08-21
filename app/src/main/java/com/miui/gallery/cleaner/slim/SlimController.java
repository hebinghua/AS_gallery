package com.miui.gallery.cleaner.slim;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.ThumbnailMetaWriter;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArraySet;
import miui.cloud.util.SyncAutoSettingUtil;

/* loaded from: classes.dex */
public class SlimController {
    public static final int[] TIME_COST_STAGE = {5, 10, 15, 25, 40, 60, nexClip.kClip_Rotate_180, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME};
    public static SlimController instance;
    public long[] mIds;
    public volatile boolean mIsRemainItemsToInit;
    public long mReleaseSize;
    public int mRemainCount;
    public LinkedList<MediaItem> mRemainItems;
    public int mSlimPhotoCount;
    public int mSlimVideoCount;
    public SlimWorkerPool mSlimWorkerPool;
    public long mStartTime;
    public SpaceSlimObserverHolder mObserverHolder = new SpaceSlimObserverHolder();
    public final Object mRemainItemsLock = new Object();

    /* loaded from: classes.dex */
    public interface OnSlimDoneListener {
        void onSlimDone(long j);
    }

    /* loaded from: classes.dex */
    public interface SpaceSlimObserver {
        void onSlimPaused();

        void onSlimProgress(long j, long j2, int i);

        void onSlimResumed();
    }

    public static /* synthetic */ long access$1014(SlimController slimController, long j) {
        long j2 = slimController.mReleaseSize + j;
        slimController.mReleaseSize = j2;
        return j2;
    }

    public static /* synthetic */ int access$1110(SlimController slimController) {
        int i = slimController.mRemainCount;
        slimController.mRemainCount = i - 1;
        return i;
    }

    public static /* synthetic */ int access$1208(SlimController slimController) {
        int i = slimController.mSlimPhotoCount;
        slimController.mSlimPhotoCount = i + 1;
        return i;
    }

    public static /* synthetic */ int access$1308(SlimController slimController) {
        int i = slimController.mSlimVideoCount;
        slimController.mSlimVideoCount = i + 1;
        return i;
    }

    public static synchronized SlimController getInstance() {
        SlimController slimController;
        synchronized (SlimController.class) {
            if (instance == null) {
                instance = new SlimController();
            }
            slimController = instance;
        }
        return slimController;
    }

    public void registerObserver(SpaceSlimObserver spaceSlimObserver) {
        this.mObserverHolder.observers.add(spaceSlimObserver);
    }

    public void unregisterObserver(SpaceSlimObserver spaceSlimObserver) {
        this.mObserverHolder.observers.remove(spaceSlimObserver);
    }

    public synchronized void start(long[] jArr) {
        stop();
        Account account = AccountCache.getAccount();
        if (!(account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider"))) {
            return;
        }
        if (Preference.getSyncShouldClearDataBase()) {
            return;
        }
        if (jArr != null && jArr.length != 0) {
            DefaultLogger.d("SlimController", "start slim");
            this.mIds = jArr;
            this.mRemainCount = jArr.length;
            this.mSlimPhotoCount = 0;
            this.mSlimVideoCount = 0;
            synchronized (this.mRemainItemsLock) {
                this.mIsRemainItemsToInit = true;
            }
            SlimWorkerPool slimWorkerPool = new SlimWorkerPool();
            this.mSlimWorkerPool = slimWorkerPool;
            slimWorkerPool.start();
            this.mStartTime = System.currentTimeMillis();
        }
    }

    public synchronized void resume() {
        SlimWorkerPool slimWorkerPool = this.mSlimWorkerPool;
        if (slimWorkerPool != null) {
            slimWorkerPool.resume();
            DefaultLogger.d("SlimController", "resume slim");
        }
    }

    public synchronized void pause() {
        SlimWorkerPool slimWorkerPool = this.mSlimWorkerPool;
        if (slimWorkerPool != null) {
            slimWorkerPool.pause();
            DefaultLogger.d("SlimController", "pause slim");
        }
    }

    public synchronized long getStartTime() {
        return this.mStartTime;
    }

    public synchronized void stop() {
        if (this.mSlimWorkerPool != null) {
            DefaultLogger.d("SlimController", "stop slim");
            this.mSlimWorkerPool.stop();
        }
        this.mIds = null;
        this.mRemainCount = 0;
        this.mSlimPhotoCount = 0;
        this.mSlimVideoCount = 0;
        this.mReleaseSize = 0L;
        this.mStartTime = 0L;
        synchronized (this.mRemainItemsLock) {
            LinkedList<MediaItem> linkedList = this.mRemainItems;
            if (linkedList != null) {
                linkedList.clear();
            }
            this.mRemainItems = null;
            this.mIsRemainItemsToInit = false;
        }
    }

    public synchronized int getTotalCount() {
        long[] jArr;
        jArr = this.mIds;
        return jArr == null ? 0 : jArr.length;
    }

    public synchronized int getRemainCount() {
        return this.mRemainCount;
    }

    public synchronized int getSlimPhotoCount() {
        return this.mSlimPhotoCount;
    }

    public synchronized int getSlimVideoCountCount() {
        return this.mSlimVideoCount;
    }

    public synchronized int getSlimCount() {
        return getTotalCount() - getRemainCount();
    }

    public synchronized long getReleaseSize() {
        return this.mReleaseSize;
    }

    public synchronized boolean isSlimming() {
        return this.mSlimWorkerPool != null;
    }

    public synchronized boolean isSlimStarted() {
        boolean z;
        if (!isSlimming()) {
            if (getReleaseSize() <= 0) {
                z = false;
            }
        }
        z = true;
        return z;
    }

    public synchronized boolean isSlimPaused() {
        boolean z;
        SlimWorkerPool slimWorkerPool = this.mSlimWorkerPool;
        if (slimWorkerPool != null) {
            if (slimWorkerPool.isPaused()) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    public final void onSlimProgress(final int i) {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.cleaner.slim.SlimController.1
            @Override // java.lang.Runnable
            public void run() {
                SlimController.this.mObserverHolder.onSlimProgress(i, SlimController.this.getReleaseSize(), SlimController.this.getRemainCount());
            }
        });
    }

    public final void onSlimPaused() {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.cleaner.slim.SlimController.2
            @Override // java.lang.Runnable
            public void run() {
                SlimController.this.mObserverHolder.onSlimPaused();
            }
        });
    }

    public final void onSlimResumed() {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.cleaner.slim.SlimController.3
            @Override // java.lang.Runnable
            public void run() {
                SlimController.this.mObserverHolder.onSlimResumed();
            }
        });
    }

    /* loaded from: classes.dex */
    public class SlimWorker extends Thread {
        public volatile boolean mIsCanceled;
        public volatile boolean mIsPaused;
        public OnSlimDoneListener mOnSlimDoneListener;
        public final Object mSlimLock;

        public SlimWorker() {
            this.mIsCanceled = false;
            this.mIsPaused = false;
            this.mSlimLock = new Object();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Process.setThreadPriority(10);
            if (this.mIsCanceled) {
                return;
            }
            if (this.mIsPaused) {
                doPause();
            }
            if (SlimController.this.mIsRemainItemsToInit) {
                synchronized (SlimController.this.mRemainItemsLock) {
                    if (SlimController.this.mIsRemainItemsToInit) {
                        SlimController slimController = SlimController.this;
                        slimController.mRemainItems = slimController.queryMediaItem(slimController.mIds);
                        SlimController.this.mIsRemainItemsToInit = false;
                    }
                }
            }
            long j = 0;
            if (!isRemainItemsValid()) {
                SlimController.this.onSlimProgress(0);
                onDone(0L);
                return;
            }
            ArrayList arrayList = new ArrayList();
            while (true) {
                MediaItem nextRemainItem = getNextRemainItem();
                if (nextRemainItem != null) {
                    if (this.mIsCanceled || Preference.getSyncShouldClearDataBase()) {
                        return;
                    }
                    if (this.mIsPaused) {
                        doPause();
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    long handleSlim = SlimController.this.handleSlim(nextRemainItem, arrayList);
                    j += System.currentTimeMillis() - currentTimeMillis;
                    synchronized (SlimController.this) {
                        if (!this.mIsCanceled) {
                            SlimController.access$1014(SlimController.this, handleSlim);
                            SlimController.access$1110(SlimController.this);
                            if (nextRemainItem.serverType == 1) {
                                SlimController.access$1208(SlimController.this);
                            } else {
                                SlimController.access$1308(SlimController.this);
                            }
                        }
                        SlimController.this.onSlimProgress(nextRemainItem.id);
                    }
                } else {
                    onDone(j);
                    if (!BaseMiscUtil.isValid(arrayList)) {
                        return;
                    }
                    DeleteRecorder.getInstance().record((DeleteRecord[]) arrayList.toArray(new DeleteRecord[0]));
                    return;
                }
            }
        }

        public final MediaItem getNextRemainItem() {
            synchronized (SlimController.this.mRemainItemsLock) {
                if (BaseMiscUtil.isValid(SlimController.this.mRemainItems)) {
                    MediaItem mediaItem = (MediaItem) SlimController.this.mRemainItems.remove(0);
                    SlimController.this.mRemainItems.remove(mediaItem);
                    return mediaItem;
                }
                return null;
            }
        }

        public final boolean isRemainItemsValid() {
            boolean isValid;
            synchronized (SlimController.this.mRemainItemsLock) {
                isValid = BaseMiscUtil.isValid(SlimController.this.mRemainItems);
            }
            return isValid;
        }

        public void doPause() {
            synchronized (this.mSlimLock) {
                while (this.mIsPaused) {
                    try {
                        this.mSlimLock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public final void onDone(long j) {
            OnSlimDoneListener onSlimDoneListener;
            if (this.mIsCanceled || (onSlimDoneListener = this.mOnSlimDoneListener) == null) {
                return;
            }
            onSlimDoneListener.onSlimDone(j);
        }

        public boolean isCanceled() {
            return this.mIsCanceled;
        }

        public boolean isPaused() {
            return this.mIsPaused;
        }

        public void cancel() {
            this.mIsCanceled = true;
        }

        public void pauseSlim() {
            this.mIsPaused = true;
            SlimController.this.onSlimPaused();
        }

        public void resumeSlim() {
            this.mIsPaused = false;
            synchronized (this.mSlimLock) {
                this.mSlimLock.notifyAll();
            }
            SlimController.this.onSlimResumed();
        }

        public void setOnSlimDoneListener(OnSlimDoneListener onSlimDoneListener) {
            this.mOnSlimDoneListener = onSlimDoneListener;
        }
    }

    public final LinkedList<MediaItem> queryMediaItem(long[] jArr) {
        return (LinkedList) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, MediaItem.PROJECTION, String.format("%s IN (%s) AND %s IS NOT NULL AND %s != '' AND %s = %s", j.c, TextUtils.join(",", MiscUtil.arrayToList(jArr)), "serverId", "serverId", "localFlag", 0), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<LinkedList<MediaItem>>() { // from class: com.miui.gallery.cleaner.slim.SlimController.4
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public LinkedList<MediaItem> mo1808handle(Cursor cursor) {
                LinkedList<MediaItem> linkedList = new LinkedList<>();
                if (cursor == null || !cursor.moveToFirst()) {
                    return linkedList;
                }
                do {
                    linkedList.add(MediaItem.cursorToEntity(cursor));
                } while (cursor.moveToNext());
                return linkedList;
            }
        });
    }

    public final long handleSlim(MediaItem mediaItem, ArrayList<DeleteRecord> arrayList) {
        DocumentFile documentFile;
        DocumentFile documentFile2;
        if (TextUtils.isEmpty(mediaItem.localFilePath)) {
            return 0L;
        }
        File file = new File(mediaItem.localFilePath);
        if (!file.exists()) {
            return 0L;
        }
        long length = file.length();
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("SlimController", "handleSlim");
        if (TextUtils.equals(ExifUtil.getUserCommentSha1(mediaItem.thumbnailFilePath), mediaItem.sha1)) {
            File file2 = new File(mediaItem.thumbnailFilePath);
            long length2 = file2.length();
            DefaultLogger.d("SlimController", "thumbnail file already exits %s", mediaItem.thumbnailFilePath);
            if (!file2.getName().equalsIgnoreCase(file.getName()) && (documentFile2 = StorageSolutionProvider.get().getDocumentFile(mediaItem.localFilePath, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                documentFile2.delete();
                DefaultLogger.i("SlimController", "delete localFile: %s", mediaItem.localFilePath);
                arrayList.add(new DeleteRecord(45, mediaItem.localFilePath, "SlimController"));
            }
            saveToDB(mediaItem.id, mediaItem.thumbnailFilePath, null);
            return length - length2;
        } else if (!TextUtils.equals(FileUtils.getSha1(mediaItem.localFilePath), mediaItem.sha1)) {
            saveToDB(mediaItem.id, mediaItem.thumbnailFilePath, null);
            return 0L;
        } else {
            String str = mediaItem.fileName;
            String str2 = mediaItem.sha1;
            File buildThumbnail = buildThumbnail(str, str2, mediaItem.localFilePath, mediaItem.serverType, new ThumbnailMetaWriter(str2, str, mediaItem.mixDateTime, mediaItem.dateTime, mediaItem.gpsDateStamp, mediaItem.gpsTimeStamp));
            if (buildThumbnail == null) {
                return 0L;
            }
            arrayList.add(new DeleteRecord(45, mediaItem.localFilePath, "SlimController"));
            if (!buildThumbnail.getName().equalsIgnoreCase(file.getName()) && (documentFile = StorageSolutionProvider.get().getDocumentFile(mediaItem.localFilePath, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                documentFile.delete();
                DefaultLogger.i("SlimController", "delete localFile: %s", mediaItem.localFilePath);
            }
            String absolutePath = buildThumbnail.getAbsolutePath();
            mediaItem.thumbnailFilePath = absolutePath;
            DefaultLogger.i("SlimController", "successfully slim %s to %s", mediaItem.localFilePath, absolutePath);
            saveToDB(mediaItem.id, mediaItem.thumbnailFilePath, "");
            long length3 = length - buildThumbnail.length();
            if (length3 > 0) {
                return length3;
            }
            DefaultLogger.w("SlimController", "slim size < 0: %s", mediaItem.localFilePath);
            return 0L;
        }
    }

    public static void saveToDB(int i, String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("thumbnailFile", str);
        contentValues.put("localFile", str2);
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Uri uri = GalleryContract.Cloud.CLOUD_URI;
        SafeDBUtil.safeUpdate(sGetAndroidContext, uri, contentValues, "_id = " + i, (String[]) null);
    }

    public static File buildThumbnail(String str, String str2, String str3, int i, ThumbnailMetaWriter thumbnailMetaWriter) {
        String buildBigThumbnailsForVideo;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            DefaultLogger.w("SlimController", "illegal args: fileName=%s, sha1=%s, localFilePath=%s", str, str2, str3);
            return null;
        }
        File file = new File(str3);
        if (!file.isFile()) {
            DefaultLogger.w("SlimController", "file not exists: %s", str3);
            return null;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("SlimController", "buildThumbnail");
        File file2 = new File(file.getParent(), BaseFileUtils.getFileTitle(str) + ".jpg");
        if (file2.isFile()) {
            try {
                ExifUtil.UserCommentData userCommentData = ExifUtil.getUserCommentData(file2.getAbsolutePath());
                if (TextUtils.equals(str2, userCommentData != null ? userCommentData.getSha1() : null)) {
                    return file2;
                }
                if (!file2.getName().equalsIgnoreCase(file.getName())) {
                    file2 = new File(file.getParent(), BaseFileUtils.getFileTitle(str) + "_" + System.currentTimeMillis() + ".jpg");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        String str4 = "." + str2;
        if (i == 1) {
            buildBigThumbnailsForVideo = CloudUtils.buildBigThumbnailForImage(str3, str2, getSlimTempPath(), str4, thumbnailMetaWriter);
        } else {
            buildBigThumbnailsForVideo = CloudUtils.buildBigThumbnailsForVideo(str3, str2, getSlimTempPath(), str4, thumbnailMetaWriter);
        }
        if (TextUtils.isEmpty(buildBigThumbnailsForVideo)) {
            DefaultLogger.e("SlimController", "fail to build thumbnail. args: fileName=%s, sha1=%s, localFilePath=%s", str, str2, str3);
            return null;
        }
        File file3 = new File(buildBigThumbnailsForVideo);
        if (StorageSolutionProvider.get().moveFile(buildBigThumbnailsForVideo, file2.getAbsolutePath(), appendInvokerTag)) {
            return file2;
        }
        DefaultLogger.e("SlimController", "fail to rename %s to %s!", file3.getAbsoluteFile(), file2.getAbsoluteFile());
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(buildBigThumbnailsForVideo, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
        if (documentFile != null) {
            documentFile.delete();
        }
        return null;
    }

    public static String getSlimTempPath() {
        String filePathUnder = StorageUtils.getFilePathUnder(StorageUtils.getPrimaryStoragePath(), "/Android/data/com.miui.gallery/cache/slim");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("SlimController", "getSlimTempPath");
        if (!new File(filePathUnder).exists()) {
            StorageSolutionProvider.get().getDocumentFile(filePathUnder, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag);
        }
        return filePathUnder;
    }

    /* loaded from: classes.dex */
    public class SlimWorkerPool {
        public OnSlimDoneListener mOnSlimDoneListener;
        public SlimWorker[] mSlimWorkers;

        public SlimWorkerPool() {
            this.mOnSlimDoneListener = new OnSlimDoneListener() { // from class: com.miui.gallery.cleaner.slim.SlimController.SlimWorkerPool.1
                public int mDoneCount = 0;
                public long mTimeCost = 0;

                @Override // com.miui.gallery.cleaner.slim.SlimController.OnSlimDoneListener
                public void onSlimDone(long j) {
                    synchronized (SlimWorkerPool.this) {
                        if (SlimWorkerPool.this.mSlimWorkers == null) {
                            return;
                        }
                        if (this.mTimeCost < j) {
                            this.mTimeCost = j;
                        }
                        int i = this.mDoneCount + 1;
                        this.mDoneCount = i;
                        if (i == 4) {
                            SlimWorkerPool.this.stop();
                            HashMap hashMap = new HashMap();
                            hashMap.put("cost_time", SamplingStatHelper.formatValueStage(((float) this.mTimeCost) / 1000.0f, SlimController.TIME_COST_STAGE));
                            SamplingStatHelper.recordCountEvent("cleaner", "slim_finish", hashMap);
                        }
                    }
                }
            };
        }

        public synchronized void start() {
            this.mSlimWorkers = new SlimWorker[4];
            for (int i = 0; i < 4; i++) {
                this.mSlimWorkers[i] = new SlimWorker();
                this.mSlimWorkers[i].setOnSlimDoneListener(this.mOnSlimDoneListener);
                this.mSlimWorkers[i].start();
            }
        }

        public synchronized void stop() {
            if (this.mSlimWorkers == null) {
                return;
            }
            for (int i = 0; i < 4; i++) {
                this.mSlimWorkers[i].cancel();
                this.mSlimWorkers[i] = null;
            }
            this.mSlimWorkers = null;
            SlimController.this.mSlimWorkerPool = null;
        }

        public synchronized void resume() {
            if (this.mSlimWorkers != null) {
                for (int i = 0; i < 4; i++) {
                    this.mSlimWorkers[i].resumeSlim();
                }
            }
        }

        public synchronized void pause() {
            if (this.mSlimWorkers != null) {
                for (int i = 0; i < 4; i++) {
                    this.mSlimWorkers[i].pauseSlim();
                }
            }
        }

        public synchronized boolean isPaused() {
            boolean z;
            SlimWorker[] slimWorkerArr = this.mSlimWorkers;
            z = false;
            if (slimWorkerArr != null && !slimWorkerArr[0].isCanceled()) {
                if (this.mSlimWorkers[0].isPaused()) {
                    z = true;
                }
            }
            return z;
        }
    }

    /* loaded from: classes.dex */
    public static class MediaItem {
        public static final String[] PROJECTION = {j.c, "fileName", "sha1", "exifOrientation", "exifDateTime", "exifGPSDateStamp", "exifGPSTimeStamp", "mixedDateTime", "localFile", "thumbnailFile", "serverType"};
        public String dateTime;
        public String fileName;
        public String gpsDateStamp;
        public String gpsTimeStamp;
        public int id;
        public String localFilePath;
        public long mixDateTime;
        public int orientation;
        public int serverType;
        public String sha1;
        public String thumbnailFilePath;

        public static MediaItem cursorToEntity(Cursor cursor) {
            MediaItem mediaItem = new MediaItem();
            int i = 0;
            mediaItem.id = cursor.getInt(0);
            mediaItem.fileName = cursor.getString(1);
            mediaItem.sha1 = cursor.getString(2);
            if (!cursor.isNull(3)) {
                i = cursor.getInt(3);
            }
            mediaItem.orientation = i;
            mediaItem.dateTime = cursor.getString(4);
            mediaItem.gpsDateStamp = cursor.getString(5);
            mediaItem.gpsTimeStamp = cursor.getString(6);
            mediaItem.mixDateTime = cursor.getLong(7);
            mediaItem.localFilePath = cursor.getString(8);
            mediaItem.thumbnailFilePath = cursor.getString(9);
            mediaItem.serverType = cursor.getInt(10);
            return mediaItem;
        }
    }

    /* loaded from: classes.dex */
    public static class SpaceSlimObserverHolder implements SpaceSlimObserver {
        public CopyOnWriteArraySet<SpaceSlimObserver> observers;

        public SpaceSlimObserverHolder() {
            this.observers = new CopyOnWriteArraySet<>();
        }

        @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
        public void onSlimProgress(long j, long j2, int i) {
            Iterator<SpaceSlimObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onSlimProgress(j, j2, i);
            }
        }

        @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
        public void onSlimResumed() {
            Iterator<SpaceSlimObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onSlimResumed();
            }
        }

        @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
        public void onSlimPaused() {
            Iterator<SpaceSlimObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onSlimPaused();
            }
        }
    }
}
