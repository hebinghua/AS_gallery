package com.miui.gallery.cloud;

import android.os.SystemClock;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.util.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public abstract class RequestItemBase {
    public static final long DELAY_UPLOAD_TIME;
    public static final Map<Integer, Boolean> PRI_THREAD;
    public static boolean isUpload;
    public int commitRetryTimes;
    public int createRetryTimes;
    public final long delayInMillis;
    public int kssRetryTimes;
    public long mDownloadedSize;
    public SyncType mSpecificType;
    public long mTotalSize;
    public boolean needReRequest;
    public int otherRetryTimes;
    public final int priority;
    public String requestId;
    public String requestIds;
    public GallerySyncCode result;
    public long retryAfter;
    public final long startInMillis;
    public AtomicInteger status;

    public static int getDownloadTypeByPriority(int i) {
        switch (i) {
            case 6:
            case 7:
            case 12:
                return 2;
            case 8:
            case 13:
                return 1;
            case 9:
                return 4;
            case 10:
            case 11:
                return 3;
            default:
                return 0;
        }
    }

    public static boolean isCancellablePriority(int i) {
        return (i == 0 || i == 1) ? false : true;
    }

    public abstract ArrayList<RequestItemBase> getItems();

    @Deprecated
    public abstract int getRequestLimitAGroup();

    public abstract boolean isInSameAlbum(RequestItemBase requestItemBase);

    @Deprecated
    public abstract boolean supportMultiRequest();

    static {
        HashMap hashMap = new HashMap(20);
        Boolean bool = Boolean.TRUE;
        hashMap.put(0, bool);
        hashMap.put(1, bool);
        hashMap.put(2, bool);
        hashMap.put(3, bool);
        hashMap.put(4, bool);
        hashMap.put(5, bool);
        hashMap.put(6, bool);
        hashMap.put(7, bool);
        hashMap.put(8, bool);
        Boolean bool2 = Boolean.FALSE;
        hashMap.put(9, bool2);
        hashMap.put(10, bool2);
        hashMap.put(11, bool2);
        hashMap.put(12, bool2);
        hashMap.put(13, bool2);
        PRI_THREAD = Collections.unmodifiableMap(hashMap);
        DELAY_UPLOAD_TIME = SyncConditionManager.sGetSyncConfig().getDelayUploadTime();
    }

    public static boolean isBackgroundPriority(int i) {
        return PRI_THREAD.get(Integer.valueOf(i)).booleanValue();
    }

    public RequestItemBase(int i) {
        this(i, 0L);
    }

    public RequestItemBase(int i, long j) {
        this.needReRequest = false;
        this.status = new AtomicInteger(0);
        this.mSpecificType = SyncType.UNKNOW;
        this.priority = i;
        this.startInMillis = SystemClock.uptimeMillis();
        this.delayInMillis = j;
        init();
    }

    public void init() {
        this.createRetryTimes = 0;
        this.commitRetryTimes = 0;
        this.kssRetryTimes = 0;
        this.otherRetryTimes = 0;
        this.retryAfter = 0L;
        this.needReRequest = false;
        this.requestId = null;
        this.requestIds = null;
        this.mDownloadedSize = 0L;
        this.mTotalSize = 0L;
        this.result = null;
    }

    public SyncType getSpecificType() {
        return this.mSpecificType;
    }

    public void setSpecificType(SyncType syncType) {
        this.mSpecificType = syncType;
    }

    public void setStatus(int i) {
        this.status.set(i);
    }

    public boolean compareAndSetStatus(int i, int i2) {
        return this.status.compareAndSet(i, i2);
    }

    public int getStatus() {
        return this.status.get();
    }

    public long getDelayToExecuteInMillis(long j) {
        return (this.startInMillis + this.delayInMillis) - j;
    }

    public int getDownloadType() {
        return getDownloadTypeByPriority(this.priority);
    }

    public static long getDelay(long j, String str) {
        if (j != 7 || isUpload) {
            return 0L;
        }
        long lastModified = new File(str).lastModified();
        long j2 = DELAY_UPLOAD_TIME;
        return Utils.clamp(j2 - (System.currentTimeMillis() - lastModified), 0L, j2);
    }
}
