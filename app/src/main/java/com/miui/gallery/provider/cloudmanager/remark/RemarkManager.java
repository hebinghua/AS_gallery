package com.miui.gallery.provider.cloudmanager.remark;

import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.dao.base.EntityTransaction;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.provider.cloudmanager.remark.info.IRemarkInfo;
import com.miui.gallery.provider.cloudmanager.remark.info.MediaRemarkEntity;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes2.dex */
public class RemarkManager {
    public static final LinkedHashMap<Long, IRemarkInfo> sCache;
    public static volatile boolean sLoaded;
    public static final ReentrantReadWriteLock.ReadLock sReadLock;
    public static final ReentrantReadWriteLock sReadWriteLock;
    public static long[] sUnHandleMediaIds;
    public static final ReentrantReadWriteLock.WriteLock sWriteLock;

    static {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
        sReadWriteLock = reentrantReadWriteLock;
        sReadLock = reentrantReadWriteLock.readLock();
        sWriteLock = reentrantReadWriteLock.writeLock();
        sCache = new LinkedHashMap<>();
    }

    public static void remarkMediaId(IRemarkInfo iRemarkInfo) {
        if (iRemarkInfo == null) {
            return;
        }
        LinkedHashMap<Long, IRemarkInfo> linkedHashMap = sCache;
        if (linkedHashMap.containsValue(iRemarkInfo)) {
            DefaultLogger.e("galleryAction_Remark", "remarkMediaId => fail ! Cache contains [%d]", Long.valueOf(iRemarkInfo.getKey()));
            return;
        }
        ReentrantReadWriteLock.WriteLock writeLock = sWriteLock;
        writeLock.lock();
        try {
            if (!linkedHashMap.containsValue(iRemarkInfo)) {
                if (-1 != GalleryEntityManager.getInstance().insert(iRemarkInfo.getEntity())) {
                    linkedHashMap.put(Long.valueOf(iRemarkInfo.getKey()), iRemarkInfo);
                    DefaultLogger.d("galleryAction_Remark", "remarkMediaId => success ! id = [%d]", Long.valueOf(iRemarkInfo.getKey()));
                    writeLock.unlock();
                    return;
                }
                DefaultLogger.e("galleryAction_Remark", "remarkMediaId => fail ! id = [%d]", Long.valueOf(iRemarkInfo.getKey()));
                writeLock.unlock();
                return;
            }
            writeLock.unlock();
        } catch (Throwable th) {
            sWriteLock.unlock();
            throw th;
        }
    }

    public static void doneRemarkMediaIds(long... jArr) {
        if (!BaseMiscUtil.isValid(jArr)) {
            return;
        }
        List<Long> arrayToList = MiscUtil.arrayToList(jArr);
        LinkedHashMap<Long, IRemarkInfo> linkedHashMap = sCache;
        if (!linkedHashMap.keySet().containsAll(arrayToList)) {
            DefaultLogger.w("galleryAction_Remark", "doneRemarkMediaIds => fail ! Cache not containsAll [%s]", StringUtils.join(",", jArr));
            return;
        }
        EntityTransaction transaction = GalleryEntityManager.getInstance().getTransaction();
        transaction.begin();
        ReentrantReadWriteLock.WriteLock writeLock = sWriteLock;
        writeLock.lock();
        try {
            if (!linkedHashMap.keySet().containsAll(arrayToList)) {
                DefaultLogger.w("galleryAction_Remark", "doneRemarkMediaIds => fail ! Cache not containsAll [%s]", StringUtils.join(",", jArr));
                writeLock.unlock();
                transaction.end();
                return;
            }
            if (!GalleryEntityManager.getInstance().delete(MediaRemarkEntity.class, String.format("%s = %s AND %s IN (%s)", "operationType", 1, "mediaId", StringUtils.join(",", jArr)), null)) {
                DefaultLogger.e("galleryAction_Remark", "doneRemarkMediaIds => fail ! ids = [%s]", StringUtils.join(",", jArr));
                writeLock.unlock();
                transaction.end();
                return;
            }
            for (long j : jArr) {
                sCache.remove(Long.valueOf(j));
            }
            List<Long> widgetShouldUpdateIds = CustomWidgetDBManager.getInstance().widgetShouldUpdateIds(arrayToList);
            if (BaseMiscUtil.isValid(widgetShouldUpdateIds)) {
                updateCustomWidgetStatus(widgetShouldUpdateIds.stream().mapToLong(RemarkManager$$ExternalSyntheticLambda1.INSTANCE).toArray());
            }
            DefaultLogger.d("galleryAction_Remark", "doneRemarkMediaIds => success ! count = [%d] ids = [%s]", Integer.valueOf(jArr.length), StringUtils.join(",", jArr));
            transaction.commit();
        } finally {
            sWriteLock.unlock();
            transaction.end();
        }
    }

    public static void updateCustomWidgetStatus(long[] jArr) {
        try {
            if (jArr.length <= 20) {
                GalleryWidgetUtils.updateCustomWidgetStatus(jArr);
                return;
            }
            GalleryWidgetUtils.updateCustomWidgetStatus(Arrays.copyOfRange(jArr, 0, 20));
            updateCustomWidgetStatus(Arrays.copyOfRange(jArr, 20, jArr.length));
        } catch (Exception e) {
            DefaultLogger.e("galleryAction_Remark", e);
        }
    }

    public static boolean onAccountDelete() {
        ReentrantReadWriteLock.WriteLock writeLock = sWriteLock;
        writeLock.lock();
        try {
            try {
                if (GalleryEntityManager.getInstance().deleteAll(MediaRemarkEntity.class)) {
                    sCache.clear();
                    CacheMarkManager.clear();
                    sLoaded = false;
                    DefaultLogger.d("galleryAction_Remark", "onAccountDelete => deleteAll success ! ");
                    writeLock.unlock();
                    return true;
                }
            } catch (Exception e) {
                DefaultLogger.e("galleryAction_Remark", "onAccountDelete occur error. %s", e);
                writeLock = sWriteLock;
            }
            writeLock.unlock();
            DefaultLogger.e("galleryAction_Remark", "onAccountDelete => deleteAll fail ! ");
            return false;
        } catch (Throwable th) {
            sWriteLock.unlock();
            throw th;
        }
    }

    public static synchronized long[] loadUnHandleMediaIds() {
        synchronized (RemarkManager.class) {
            if (sLoaded) {
                return sUnHandleMediaIds;
            }
            TimingTracing.beginTracing("galleryAction_Remark", "loadUnHandleMediaIds");
            Set<Long> loadCloudMediaIds = loadCloudMediaIds();
            if (loadCloudMediaIds == null) {
                loadCloudMediaIds = new HashSet();
            }
            Set<Long> loadRemarkMediaIds = loadRemarkMediaIds();
            if (BaseMiscUtil.isValid(loadRemarkMediaIds)) {
                loadCloudMediaIds.addAll(loadRemarkMediaIds);
            }
            if (BaseMiscUtil.isValid(loadCloudMediaIds)) {
                sUnHandleMediaIds = new long[loadCloudMediaIds.size()];
                int i = 0;
                for (Long l : loadCloudMediaIds) {
                    sUnHandleMediaIds[i] = l == null ? 0L : l.longValue();
                    i++;
                }
                long[] jArr = sUnHandleMediaIds;
                sLoaded = true;
                TimingTracing.stopTracing(null);
                return jArr;
            }
            sLoaded = true;
            TimingTracing.stopTracing(null);
            return null;
        }
    }

    public static Set<Long> loadRemarkMediaIds() {
        int i = 0;
        List query = GalleryEntityManager.getInstance().query(MediaRemarkEntity.class, "operationType = ?", new String[]{String.valueOf(1)}, "_id ASC", null);
        ReentrantReadWriteLock.WriteLock writeLock = sWriteLock;
        writeLock.lock();
        if (query != null) {
            try {
                i = query.size();
            } catch (Throwable th) {
                sWriteLock.unlock();
                throw th;
            }
        }
        DefaultLogger.d("galleryAction_Remark", "loadRemarkMediaIds => count = [%d]", Integer.valueOf(i));
        LinkedHashMap<Long, IRemarkInfo> linkedHashMap = sCache;
        linkedHashMap.clear();
        if (BaseMiscUtil.isValid(query)) {
            query.forEach(RemarkManager$$ExternalSyntheticLambda0.INSTANCE);
            writeLock.unlock();
            return linkedHashMap.keySet();
        }
        Set<Long> keySet = linkedHashMap.keySet();
        writeLock.unlock();
        return keySet;
    }

    public static /* synthetic */ void lambda$loadRemarkMediaIds$1(MediaRemarkEntity mediaRemarkEntity) {
        if (mediaRemarkEntity != null) {
            IRemarkInfo converterInfo = RemarkInfoFactory.converterInfo(mediaRemarkEntity);
            sCache.put(Long.valueOf(converterInfo.getKey()), converterInfo);
            DefaultLogger.d("galleryAction_Remark", "loadRemarkMediaIds => success info = [%s]", converterInfo.toString());
        }
    }

    public static Set<Long> loadCloudMediaIds() {
        return (Set) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{j.c, "localFile", "thumbnailFile"}, String.format(Locale.US, "%s = '%s' AND %s = %d", "serverStatus", "cleanLocal", "localFlag", -1), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Set<Long>>() { // from class: com.miui.gallery.provider.cloudmanager.remark.RemarkManager.1
            /* JADX WARN: Removed duplicated region for block: B:20:0x0053  */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.util.Set<java.lang.Long> mo1808handle(android.database.Cursor r15) {
                /*
                    r14 = this;
                    java.util.HashSet r0 = new java.util.HashSet
                    r0.<init>()
                    java.util.HashSet r1 = new java.util.HashSet
                    r1.<init>()
                    r2 = 2
                    r3 = 1
                    r4 = 0
                    if (r15 == 0) goto L4d
                    int r5 = r15.getCount()
                    if (r5 <= 0) goto L4d
                    boolean r5 = r15.moveToNext()
                    if (r5 == 0) goto L4d
                    java.util.Set r5 = com.miui.gallery.provider.cloudmanager.remark.RemarkManager.access$000()
                L1f:
                    long r6 = r15.getLong(r4)
                    java.lang.String r8 = r15.getString(r3)
                    java.lang.String r9 = r15.getString(r2)
                    boolean r8 = r5.contains(r8)
                    if (r8 != 0) goto L40
                    boolean r8 = r5.contains(r9)
                    if (r8 == 0) goto L38
                    goto L40
                L38:
                    java.lang.Long r6 = java.lang.Long.valueOf(r6)
                    r0.add(r6)
                    goto L47
                L40:
                    java.lang.Long r6 = java.lang.Long.valueOf(r6)
                    r1.add(r6)
                L47:
                    boolean r6 = r15.moveToNext()
                    if (r6 != 0) goto L1f
                L4d:
                    int r15 = r1.size()
                    if (r15 <= 0) goto Lc6
                    int r15 = r1.size()
                    r5 = 1000(0x3e8, float:1.401E-42)
                    int r15 = java.lang.Math.min(r5, r15)
                    java.util.ArrayList r6 = new java.util.ArrayList
                    r6.<init>(r15)
                    java.util.Iterator r15 = r1.iterator()
                    r7 = r4
                L67:
                    boolean r8 = r15.hasNext()
                    if (r8 == 0) goto Lc6
                    java.lang.Object r8 = r15.next()
                    java.lang.Long r8 = (java.lang.Long) r8
                    long r8 = r8.longValue()
                    java.lang.Long r8 = java.lang.Long.valueOf(r8)
                    r6.add(r8)
                    int r7 = r7 + r3
                    int r8 = r6.size()
                    if (r8 >= r5) goto L8b
                    int r8 = r1.size()
                    if (r7 != r8) goto L67
                L8b:
                    android.content.Context r8 = com.miui.gallery.GalleryApp.sGetAndroidContext()
                    android.net.Uri r9 = com.miui.gallery.provider.GalleryContract.Cloud.CLOUD_URI
                    java.util.Locale r10 = java.util.Locale.US
                    r11 = 6
                    java.lang.Object[] r11 = new java.lang.Object[r11]
                    java.lang.String r12 = "serverStatus"
                    r11[r4] = r12
                    java.lang.String r12 = "cleanLocal"
                    r11[r3] = r12
                    java.lang.String r12 = "localFlag"
                    r11[r2] = r12
                    r12 = 3
                    r13 = -1
                    java.lang.Integer r13 = java.lang.Integer.valueOf(r13)
                    r11[r12] = r13
                    r12 = 4
                    java.lang.String r13 = "_id"
                    r11[r12] = r13
                    r12 = 5
                    java.lang.String r13 = ","
                    java.lang.String r13 = android.text.TextUtils.join(r13, r6)
                    r11[r12] = r13
                    java.lang.String r12 = "%s = '%s' AND %s = %d AND %s IN (%s)"
                    java.lang.String r10 = java.lang.String.format(r10, r12, r11)
                    r11 = 0
                    com.miui.gallery.util.SafeDBUtil.safeDelete(r8, r9, r10, r11)
                    r6.clear()
                    goto L67
                Lc6:
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.remark.RemarkManager.AnonymousClass1.mo1808handle(android.database.Cursor):java.util.Set");
            }
        });
    }

    public static Set<String> loadValidCloudMediaPaths() {
        return (Set) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"localFile", "thumbnailFile"}, String.format(Locale.US, "%s AND (%s OR %s)", "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", InternalContract$Cloud.ALIAS_ORIGIN_FILE_VALID, InternalContract$Cloud.ALIAS_THUMBNAIL_VALID), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Set<String>>() { // from class: com.miui.gallery.provider.cloudmanager.remark.RemarkManager.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public Set<String> mo1808handle(Cursor cursor) {
                HashSet hashSet = new HashSet();
                if (cursor == null || !cursor.moveToNext()) {
                    return hashSet;
                }
                do {
                    String string = cursor.getString(0);
                    String string2 = cursor.getString(1);
                    if (!TextUtils.isEmpty(string)) {
                        hashSet.add(string);
                    }
                    if (!TextUtils.isEmpty(string2)) {
                        hashSet.add(string2);
                    }
                } while (cursor.moveToNext());
                return hashSet;
            }
        });
    }

    public static boolean isUnHandleMedia(String str, int i) {
        TimingTracing.beginTracing("galleryAction_Remark", "isUnHandleMedia");
        if (!sLoaded) {
            loadUnHandleMediaIds();
        }
        ReentrantReadWriteLock.ReadLock readLock = sReadLock;
        readLock.lock();
        try {
            LinkedHashMap<Long, IRemarkInfo> linkedHashMap = sCache;
            boolean z = false;
            if (!BaseMiscUtil.isValid(linkedHashMap)) {
                DefaultLogger.d("galleryAction_Remark", "isUnHandleMedia => filePath = [%s] type = [%d] result = [%b] no UnHandleMedia cache", str, Integer.valueOf(i), Boolean.FALSE);
                readLock.unlock();
            } else {
                Iterator<IRemarkInfo> it = linkedHashMap.values().iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().isUnHandleMedia(str)) {
                            z = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                DefaultLogger.d("galleryAction_Remark", "isUnHandleMedia => filePath = [%s] type = [%d] result = [%b]", str, Integer.valueOf(i), Boolean.valueOf(z));
                sReadLock.unlock();
            }
            TimingTracing.stopTracing(null);
            return z;
        } catch (Throwable th) {
            sReadLock.unlock();
            TimingTracing.stopTracing(null);
            throw th;
        }
    }

    /* loaded from: classes2.dex */
    public static class CacheMarkManager {
        public static final HashMap<String, Long> sMarkInfo = new HashMap<>();

        public static synchronized void markData(String str) {
            synchronized (CacheMarkManager.class) {
                sMarkInfo.put(str, -1L);
            }
        }

        public static synchronized void markData(HashMap<String, Long> hashMap) {
            synchronized (CacheMarkManager.class) {
                sMarkInfo.putAll(hashMap);
            }
        }

        public static synchronized boolean isMarkPath(String str) {
            boolean containsKey;
            synchronized (CacheMarkManager.class) {
                containsKey = sMarkInfo.containsKey(str);
            }
            return containsKey;
        }

        public static synchronized List<Long> appendMarkIds(List<Long> list) {
            ArrayList arrayList;
            synchronized (CacheMarkManager.class) {
                arrayList = new ArrayList(sMarkInfo.values());
                if (list != null && !list.isEmpty()) {
                    arrayList.addAll(list);
                }
            }
            return arrayList;
        }

        public static synchronized void clear() {
            synchronized (CacheMarkManager.class) {
                sMarkInfo.clear();
            }
        }
    }
}
