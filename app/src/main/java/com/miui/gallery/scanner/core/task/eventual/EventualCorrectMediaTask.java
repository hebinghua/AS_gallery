package com.miui.gallery.scanner.core.task.eventual;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.VideoAttrsReader;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class EventualCorrectMediaTask extends EventualScanTask {
    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        return 589989125;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public EventualCorrectMediaTask(Context context, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig, null);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) {
        int sceneCode = getConfig().getSceneCode();
        if (sceneCode == 21) {
            correctMedia(this.mContext);
        } else if (sceneCode == 22) {
            correctMediaIdle(this.mContext);
        } else {
            DefaultLogger.w("EventualCorrectMediaTask", "invalid scene code found for EventualCorrectMediaTask [%d].", Integer.valueOf(getConfig().getSceneCode()));
        }
        return ScanResult.success(ScanContracts$ScanResultReason.DEFAULT).build();
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return obj instanceof EventualCorrectMediaTask;
    }

    public final void correctMedia(Context context) {
        new CleanEmptyPathAlbum(context, "2021-02-24T00:00:00Z").correct();
        new CorrectInvalidDurationItem(context, "2021-09-03T00:00:00Z", 315360000L).correct();
        new CleanInvalidItem(context, "2021-09-03T00:00:00Z", 315360000L).correct();
    }

    public final void correctMediaIdle(Context context) {
        new RefillLocationForScreenshots(context, "2021-09-30T00:00:00Z").correct();
        new CorrectForSystemAlbumLocalPath(context, "2021-12-17T00:00:00Z").correct();
        new CorrectSecretEmptyFile(context, "2021-12-17T00:00:00Z").correct();
        new IdleCorrectLogic(context, "2022-01-18T00:00:00Z", 315360000L).correct();
    }

    /* loaded from: classes2.dex */
    public static abstract class CorrectLogic {
        public String TAG;
        public final Context context;
        public final boolean valid;

        public abstract void doCorrect(Context context);

        public CorrectLogic(Context context, String str) {
            this(context, str, 31536000L);
        }

        public CorrectLogic(Context context, String str, long j) {
            this.TAG = getClass().getSimpleName();
            this.context = context;
            this.valid = Instant.now().isBefore(Instant.ofEpochSecond(Instant.parse(str).getEpochSecond() + j));
        }

        public final void correct() {
            if (!this.valid) {
                DefaultLogger.w(this.TAG, "invalid correct logic [%s] found, clean the dirty code soon.", getClass().getSimpleName());
                return;
            }
            DefaultLogger.d(this.TAG, "correct [%s].", getClass().getSimpleName());
            try {
                doCorrect(this.context);
            } catch (Throwable th) {
                DefaultLogger.e(this.TAG, "correct throws [%s]", th);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class CleanEmptyPathAlbum extends CorrectLogic {
        public CleanEmptyPathAlbum(Context context, String str) {
            super(context, str);
        }

        @Override // com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectLogic
        public void doCorrect(Context context) {
            if (GalleryPreferences.MediaScanner.getEverCleanEmptyPathAlbum()) {
                return;
            }
            Uri uri = GalleryContract.Album.URI;
            List list = (List) SafeDBUtil.safeQuery(context, uri, new String[]{j.c}, "localPath IS NULL", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Long>>() { // from class: com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CleanEmptyPathAlbum.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public List<Long> mo1808handle(Cursor cursor) {
                    if (cursor == null || cursor.getCount() <= 0) {
                        return Collections.emptyList();
                    }
                    LinkedList linkedList = new LinkedList();
                    while (cursor.moveToNext()) {
                        linkedList.add(Long.valueOf(cursor.getLong(0)));
                    }
                    return linkedList;
                }
            });
            if (!BaseMiscUtil.isValid(list)) {
                GalleryPreferences.MediaScanner.setEverCleanEmptyPathAlbum();
                return;
            }
            SafeDBUtil.safeDelete(context, uri, "_id IN (" + TextUtils.join(", ", list) + ")", null);
            Uri uri2 = GalleryContract.Cloud.CLOUD_URI;
            SafeDBUtil.safeDelete(context, uri2, "localGroupId IN (" + TextUtils.join(", ", list) + ")", null);
            GalleryPreferences.MediaScanner.setEverCleanEmptyPathAlbum();
        }
    }

    /* loaded from: classes2.dex */
    public static class RefillLocationForScreenshots extends CorrectLogic {
        public RefillLocationForScreenshots(Context context, String str) {
            super(context, str);
        }

        @Override // com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectLogic
        public void doCorrect(Context context) {
            if (GalleryPreferences.MediaScanner.getEverRefillLocationForScreenshots()) {
                return;
            }
            List<Pair> list = (List) SafeDBUtil.safeQuery(context, GalleryContract.Cloud.CLOUD_URI, new String[]{j.c, "source_pkg"}, "source_pkg NOTNULL AND location IS NULL AND localGroupId=(SELECT _id FROM album WHERE serverId=2)", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Pair<Long, String>>>() { // from class: com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.RefillLocationForScreenshots.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public List<Pair<Long, String>> mo1808handle(Cursor cursor) {
                    if (cursor == null || cursor.getCount() <= 0) {
                        return Collections.emptyList();
                    }
                    LinkedList linkedList = new LinkedList();
                    while (cursor.moveToNext()) {
                        long j = cursor.getLong(0);
                        linkedList.add(new Pair(Long.valueOf(j), PackageUtils.getAppNameByPackage(cursor.getString(1))));
                    }
                    return linkedList;
                }
            });
            if (!BaseMiscUtil.isValid(list)) {
                GalleryPreferences.MediaScanner.setEverRefillLocationForScreenshots();
                return;
            }
            ArrayList<ContentProviderOperation> arrayList = new ArrayList<>(100);
            for (Pair pair : list) {
                ContentProviderOperation.Builder newUpdate = ContentProviderOperation.newUpdate(GalleryContract.Cloud.CLOUD_URI);
                arrayList.add(newUpdate.withSelection("_id=" + pair.first, null).withValue("location", pair.second).build());
                if (arrayList.size() == 100) {
                    try {
                        try {
                            context.getContentResolver().applyBatch("com.miui.gallery.cloud.provider", arrayList);
                        } finally {
                            arrayList.clear();
                        }
                    } catch (Exception e) {
                        DefaultLogger.e(this.TAG, e);
                    }
                }
            }
            try {
                context.getContentResolver().applyBatch("com.miui.gallery.cloud.provider", arrayList);
                GalleryPreferences.MediaScanner.setEverRefillLocationForScreenshots();
            } catch (Exception e2) {
                DefaultLogger.e(this.TAG, e2);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class IdleCorrectLogic extends CorrectLogic {
        public IdleCorrectLogic(Context context, String str, long j) {
            super(context, str, j);
        }

        @Override // com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectLogic
        public void doCorrect(Context context) {
            cleanHomelessItems(context);
            dealWithNewNoMediaFileInserted(context);
        }

        public final void cleanHomelessItems(Context context) {
            List list = (List) SafeDBUtil.safeQuery(context, GalleryContract.Album.URI, new String[]{j.c}, "", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Long>>() { // from class: com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.IdleCorrectLogic.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public List<Long> mo1808handle(Cursor cursor) {
                    if (cursor == null || cursor.getCount() <= 0) {
                        return Collections.emptyList();
                    }
                    ArrayList arrayList = new ArrayList(cursor.getCount());
                    while (cursor.moveToNext()) {
                        arrayList.add(Long.valueOf(cursor.getLong(0)));
                    }
                    return arrayList;
                }
            });
            if (!BaseMiscUtil.isValid(list)) {
                return;
            }
            list.add(-1000L);
            Uri uri = GalleryContract.Cloud.CLOUD_URI;
            DefaultLogger.w(this.TAG, "cleanHomelessItems [%d].", Integer.valueOf(SafeDBUtil.safeDelete(context, uri, "localGroupId NOT IN (" + TextUtils.join(", ", list) + ")", null)));
        }

        public final void dealWithNewNoMediaFileInserted(Context context) {
            List<Pair> list = (List) SafeDBUtil.safeQuery(context, GalleryContract.Album.URI, new String[]{j.c, "localPath"}, "(( attributes & 2048 = 0) AND ( attributes & 4096 = 0)) AND serverId IS NULL", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Pair<Long, String>>>() { // from class: com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.IdleCorrectLogic.2
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public List<Pair<Long, String>> mo1808handle(Cursor cursor) {
                    if (cursor == null || cursor.getCount() <= 0) {
                        return Collections.emptyList();
                    }
                    ArrayList arrayList = new ArrayList(cursor.getCount());
                    while (cursor.moveToNext()) {
                        arrayList.add(new Pair(Long.valueOf(cursor.getLong(0)), cursor.getString(1)));
                    }
                    return arrayList;
                }
            });
            if (!BaseMiscUtil.isValid(list)) {
                return;
            }
            LinkedList linkedList = new LinkedList();
            for (Pair pair : list) {
                boolean z = true;
                for (String str : StorageUtils.getAbsolutePath(context, StorageUtils.ensureCommonRelativePath((String) pair.second))) {
                    z &= MediaScannerHelper.isScannableDirectory(new File(str));
                }
                if (!z) {
                    linkedList.add((Long) pair.first);
                    DefaultLogger.w(this.TAG, "dealWithNewNoMediaFileInserted [%s].", pair.second);
                }
            }
            if (!BaseMiscUtil.isValid(linkedList)) {
                return;
            }
            CloudUtils.updateAlbumAttributes(context, GalleryContract.Cloud.CLOUD_URI, MiscUtil.ListToArray(linkedList), 2048L, true, false, false);
        }
    }

    /* loaded from: classes2.dex */
    public static class CleanInvalidItem extends CorrectLogic {
        public CleanInvalidItem(Context context, String str, long j) {
            super(context, str, j);
        }

        @Override // com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectLogic
        public void doCorrect(Context context) {
            List list = (List) SafeDBUtil.safeQuery(context, GalleryContract.Album.URI, new String[]{j.c}, "serverStatus IN ('deleted', 'purged')", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Long>>() { // from class: com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CleanInvalidItem.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public List<Long> mo1808handle(Cursor cursor) {
                    if (cursor == null || cursor.getCount() <= 0) {
                        return Collections.emptyList();
                    }
                    ArrayList arrayList = new ArrayList(cursor.getCount());
                    while (cursor.moveToNext()) {
                        arrayList.add(Long.valueOf(cursor.getLong(0)));
                    }
                    return arrayList;
                }
            });
            if (!BaseMiscUtil.isValid(list)) {
                return;
            }
            Uri uri = GalleryContract.Cloud.CLOUD_URI;
            int safeDelete = SafeDBUtil.safeDelete(context, uri, "localGroupId IN (" + TextUtils.join(", ", list) + ") AND localFlag=7", null);
            if (safeDelete <= 0) {
                return;
            }
            DefaultLogger.w(this.TAG, "clean [%s] invalid items since it belongs to a deleted/purged album", Integer.valueOf(safeDelete));
        }
    }

    /* loaded from: classes2.dex */
    public static class CorrectForSystemAlbumLocalPath extends CorrectLogic {
        public CorrectForSystemAlbumLocalPath(Context context, String str) {
            super(context, str);
        }

        @Override // com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectLogic
        public void doCorrect(final Context context) {
            SafeDBUtil.safeQuery(context, GalleryContract.Album.URI, new String[]{j.c, "localPath", "attributes"}, String.format(Locale.US, "serverId = %s", 2L), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectForSystemAlbumLocalPath.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public Void mo1808handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    long j = cursor.getLong(0);
                    String string = cursor.getString(1);
                    long j2 = cursor.getLong(2);
                    boolean isOtherAlbum = Album.isOtherAlbum(j2);
                    String asString = AlbumDataHelper.getScreenshotsRecordValues().getAsString("localPath");
                    if (TextUtils.equals(string, asString) && !isOtherAlbum) {
                        return null;
                    }
                    ContentValues contentValues = new ContentValues(2);
                    long j3 = (-81) & j2;
                    contentValues.put("attributes", Long.valueOf(j3));
                    contentValues.put("localPath", asString);
                    DefaultLogger.fd(CorrectForSystemAlbumLocalPath.this.TAG, "find error screenshots data,now fix it status=[%s],error data info: localPath=[%s],attributes=[%s]--->new data:localPath=[%s],attributes=[%s]", Integer.valueOf(SafeDBUtil.safeUpdate(context, GalleryContract.Album.URI, contentValues, "_id=?", new String[]{String.valueOf(j)})), string, Long.valueOf(j2), asString, Long.valueOf(j3));
                    return null;
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public static class CorrectInvalidDurationItem extends CorrectLogic {
        public CorrectInvalidDurationItem(Context context, String str, long j) {
            super(context, str, j);
        }

        @Override // com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectLogic
        public void doCorrect(Context context) {
            Uri build = GalleryContract.Cloud.CLOUD_URI.buildUpon().appendQueryParameter("limit", String.valueOf(20)).build();
            String[] strArr = {j.c, "localFile"};
            Cursor queryToCursor = SafeDBUtil.queryToCursor(context, build, strArr, "serverType= 2 AND duration<=0 AND " + InternalContract$Cloud.ALIAS_ORIGIN_FILE_VALID + " AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", null, "_id DESC");
            if (queryToCursor != null) {
                try {
                    if (queryToCursor.getCount() > 0) {
                        while (queryToCursor.moveToNext()) {
                            long j = queryToCursor.getLong(0);
                            String string = queryToCursor.getString(1);
                            DefaultLogger.d(this.TAG, "try correct duration of [%s]", string);
                            try {
                                VideoAttrsReader read = VideoAttrsReader.read(string);
                                if (read.getDuration() > 0) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("duration", Long.valueOf(read.getDuration() / 1000));
                                    GalleryUtils.safeUpdate(GalleryContract.Cloud.CLOUD_URI, contentValues, "_id=?", new String[]{String.valueOf(j)});
                                }
                            } catch (Exception e) {
                                DefaultLogger.w(this.TAG, "try correct duration of [%s] error, [%s]", string, e);
                            }
                        }
                        queryToCursor.close();
                        return;
                    }
                } catch (Throwable th) {
                    try {
                        queryToCursor.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            if (queryToCursor != null) {
                queryToCursor.close();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class CorrectSecretEmptyFile extends CorrectLogic {
        public CorrectSecretEmptyFile(Context context, String str) {
            super(context, str);
        }

        @Override // com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectLogic
        public void doCorrect(Context context) {
            final HashMap hashMap = new HashMap();
            final HashMap hashMap2 = new HashMap();
            Uri uri = GalleryContract.Cloud.CLOUD_URI;
            String[] strArr = {j.c, "thumbnailFile", "localFile"};
            SafeDBUtil.safeQuery(context, uri, strArr, "localGroupId = -1000 AND " + InternalContract$Cloud.ALIAS_FILE_VALID, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask.CorrectSecretEmptyFile.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public Void mo1808handle(Cursor cursor) {
                    if (cursor != null && cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            long j = cursor.getLong(0);
                            if (!TextUtils.isEmpty(cursor.getString(1))) {
                                hashMap.put(Long.valueOf(j), cursor.getString(1));
                            }
                            if (!TextUtils.isEmpty(cursor.getString(2))) {
                                hashMap2.put(Long.valueOf(j), cursor.getString(2));
                            }
                        }
                    }
                    return null;
                }
            });
            hashMap.entrySet().removeIf(EventualCorrectMediaTask$CorrectSecretEmptyFile$$ExternalSyntheticLambda1.INSTANCE);
            hashMap2.entrySet().removeIf(EventualCorrectMediaTask$CorrectSecretEmptyFile$$ExternalSyntheticLambda0.INSTANCE);
            if (!hashMap.isEmpty()) {
                ContentValues contentValues = new ContentValues();
                contentValues.putNull("thumbnailFile");
                SafeDBUtil.safeUpdate(context, uri, contentValues, "localGroupId = -1000 AND (localFlag = 0 AND serverStatus = 'custom') AND _id IN (" + TextUtils.join(", ", hashMap.keySet()) + ")", (String[]) null);
            }
            if (!hashMap2.isEmpty()) {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.putNull("localFile");
                SafeDBUtil.safeUpdate(context, uri, contentValues2, "localGroupId = -1000 AND (localFlag = 0 AND serverStatus = 'custom') AND _id IN (" + TextUtils.join(", ", hashMap2.keySet()) + ")", (String[]) null);
            }
        }

        public static /* synthetic */ boolean lambda$doCorrect$0(Map.Entry entry) {
            return new File((String) entry.getValue()).length() > 0;
        }

        public static /* synthetic */ boolean lambda$doCorrect$1(Map.Entry entry) {
            return new File((String) entry.getValue()).length() > 0;
        }
    }
}
