package com.miui.gallery.cleaner;

import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.cleaner.BaseScanner;
import com.miui.gallery.cleaner.slim.SlimScanner;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes.dex */
public class ScannerManager {
    public static final int[] TIME_COST_STAGE = {5, 10, 15, 25, 40, 60, nexClip.kClip_Rotate_180, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME};
    public static ScannerManager instance;
    public Future<Void> mScanFuture;
    public long mScanStartTime;
    public volatile long mSize;
    public ArrayList<BaseScanner> mScanners = new ArrayList<>();
    public final ArrayList<ScanResult> mScanResults = new ArrayList<>();
    public ScanObserverHolder mObserverHolder = new ScanObserverHolder();
    public boolean mReset = true;
    public FutureHandler mScanFutureHandler = new FutureHandler<Void>() { // from class: com.miui.gallery.cleaner.ScannerManager.5
        @Override // com.miui.gallery.concurrent.FutureHandler
        public void onPostExecute(Future<Void> future) {
            if (!future.isCancelled()) {
                ScannerManager.this.mObserverHolder.onScanFinish(ScannerManager.this.mSize);
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", SamplingStatHelper.formatValueStage((int) ((System.currentTimeMillis() - ScannerManager.this.mScanStartTime) / 1000), ScannerManager.TIME_COST_STAGE));
                SamplingStatHelper.recordCountEvent("cleaner", "cleaner_scan_finish", hashMap);
            }
        }
    };
    public ThreadPool.Job<Void> mScanJob = new ThreadPool.Job<Void>() { // from class: com.miui.gallery.cleaner.ScannerManager.6
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run  reason: collision with other method in class */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            Iterator it = ScannerManager.this.mScanners.iterator();
            while (it.hasNext()) {
                BaseScanner baseScanner = (BaseScanner) it.next();
                if (jobContext.isCancelled()) {
                    return null;
                }
                ScanResult scan = baseScanner.scan();
                if (scan != null && (baseScanner.mType == 0 || scan.getSize() > 0)) {
                    if (baseScanner.mType != 0 || scan.getSize() != 0) {
                        synchronized (ScannerManager.this) {
                            if (jobContext.isCancelled()) {
                                return null;
                            }
                            synchronized (ScannerManager.this.mScanResults) {
                                ScannerManager.access$214(ScannerManager.this, scan.getSize());
                                ScannerManager.this.mScanResults.add(scan);
                            }
                            baseScanner.addListener(ScannerManager.this.mOnScanResultUpdateListener);
                            ScannerManager.this.onScanProgress(jobContext);
                        }
                    }
                }
            }
            synchronized (ScannerManager.this) {
                if (!jobContext.isCancelled()) {
                    ScannerManager.this.mScanFuture = null;
                }
            }
            return null;
        }
    };
    public BaseScanner.OnScanResultUpdateListener mOnScanResultUpdateListener = new BaseScanner.OnScanResultUpdateListener() { // from class: com.miui.gallery.cleaner.ScannerManager.7
        @Override // com.miui.gallery.cleaner.BaseScanner.OnScanResultUpdateListener
        public void onUpdate(int i, long j, ScanResult scanResult) {
            if (scanResult != null && scanResult.getSize() > 0) {
                ScannerManager.this.updateScanResult(i, scanResult);
            } else if (i != 0 || scanResult == null || scanResult.getSize() >= 0) {
                ScannerManager.this.removeScanResult(i);
            } else {
                ScannerManager.this.updateScanResult(i, scanResult);
            }
            if (j > 0) {
                Iterator it = ScannerManager.this.mScanners.iterator();
                while (it.hasNext()) {
                    BaseScanner baseScanner = (BaseScanner) it.next();
                    if (baseScanner.mType != i) {
                        baseScanner.onMediaItemDeleted(j);
                    }
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public interface ScanObserver {
        void onScanCanceled();

        void onScanFinish(long j);

        void onScanProgress(long j);

        void onScanResultUpdate(long j);

        void onScanStart();
    }

    public static /* synthetic */ long access$214(ScannerManager scannerManager, long j) {
        long j2 = scannerManager.mSize + j;
        scannerManager.mSize = j2;
        return j2;
    }

    public ScannerManager() {
        this.mScanners.add(new SlimScanner());
        this.mScanners.add(new ScreenshotScanner());
        this.mScanners.add(new RawPhotoScanner());
        this.mScanners.add(new SimilarScanner());
    }

    public static synchronized ScannerManager getInstance() {
        ScannerManager scannerManager;
        synchronized (ScannerManager.class) {
            if (instance == null) {
                instance = new ScannerManager();
            }
            scannerManager = instance;
        }
        return scannerManager;
    }

    public void registerObserver(ScanObserver scanObserver) {
        this.mObserverHolder.observers.add(scanObserver);
    }

    public void unregisterObserver(ScanObserver scanObserver) {
        this.mObserverHolder.observers.remove(scanObserver);
    }

    public BaseScanner getScanner(int i) {
        Iterator<BaseScanner> it = this.mScanners.iterator();
        while (it.hasNext()) {
            BaseScanner next = it.next();
            if (next.mType == i) {
                return next;
            }
        }
        return null;
    }

    public synchronized void startScan() {
        resetScan();
        this.mScanStartTime = System.currentTimeMillis();
        this.mScanFuture = ThreadManager.getMiscPool().submit(this.mScanJob, this.mScanFutureHandler);
        this.mReset = false;
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.cleaner.ScannerManager.1
            @Override // java.lang.Runnable
            public void run() {
                ScannerManager.this.mObserverHolder.onScanStart();
            }
        });
    }

    public synchronized void resetScan() {
        Future<Void> future = this.mScanFuture;
        if (future != null) {
            future.cancel();
            this.mScanFuture = null;
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.cleaner.ScannerManager.2
                @Override // java.lang.Runnable
                public void run() {
                    ScannerManager.this.mObserverHolder.onScanCanceled();
                }
            });
        }
        Iterator<BaseScanner> it = this.mScanners.iterator();
        while (it.hasNext()) {
            it.next().reset();
        }
        synchronized (this.mScanResults) {
            this.mSize = 0L;
            this.mScanStartTime = 0L;
            this.mScanResults.clear();
        }
        this.mReset = true;
    }

    public synchronized boolean isReset() {
        return this.mReset;
    }

    public synchronized boolean isScanning() {
        return this.mScanFuture != null;
    }

    public synchronized boolean isScanFinish() {
        boolean z;
        if (!isReset()) {
            if (!isScanning()) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    public List<ScanResult> getScanResults() {
        return new ArrayList(this.mScanResults);
    }

    public boolean isScanResultEmpty() {
        boolean z;
        synchronized (this.mScanResults) {
            if (!this.mScanResults.isEmpty() && this.mSize != 0) {
                z = false;
            }
            z = true;
        }
        return z;
    }

    public long getScanSize() {
        long j;
        synchronized (this.mScanResults) {
            j = this.mSize;
        }
        return j;
    }

    public long getStartTime() {
        return this.mScanStartTime;
    }

    public final void updateScanResult() {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.cleaner.ScannerManager.3
            @Override // java.lang.Runnable
            public void run() {
                ScannerManager.this.mObserverHolder.onScanResultUpdate(ScannerManager.this.mSize);
            }
        });
    }

    public final void removeScanResult(int i) {
        synchronized (this.mScanResults) {
            Iterator<ScanResult> it = this.mScanResults.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ScanResult next = it.next();
                if (next.getType() == i) {
                    it.remove();
                    this.mSize -= next.getSize();
                    if (this.mSize < 0) {
                        this.mSize = 0L;
                    }
                }
            }
        }
        updateScanResult();
    }

    public final void updateScanResult(int i, ScanResult scanResult) {
        synchronized (this.mScanResults) {
            int i2 = 0;
            int size = this.mScanResults.size();
            while (true) {
                if (i2 >= size) {
                    break;
                }
                ScanResult scanResult2 = this.mScanResults.get(i2);
                if (scanResult2.getType() == i) {
                    this.mScanResults.set(i2, scanResult);
                    this.mSize += scanResult.getSize() - scanResult2.getSize();
                    if (this.mSize < 0) {
                        this.mSize = 0L;
                    }
                } else {
                    i2++;
                }
            }
        }
        updateScanResult();
    }

    public final void onScanProgress(final ThreadPool.JobContext jobContext) {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.cleaner.ScannerManager.4
            @Override // java.lang.Runnable
            public void run() {
                if (!jobContext.isCancelled()) {
                    ScannerManager.this.mObserverHolder.onScanProgress(ScannerManager.this.mSize);
                }
            }
        });
    }

    /* loaded from: classes.dex */
    public static class ScanObserverHolder implements ScanObserver {
        public CopyOnWriteArraySet<ScanObserver> observers;

        public ScanObserverHolder() {
            this.observers = new CopyOnWriteArraySet<>();
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanStart() {
            Iterator<ScanObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onScanStart();
            }
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanCanceled() {
            Iterator<ScanObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onScanCanceled();
            }
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanProgress(long j) {
            Iterator<ScanObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onScanProgress(j);
            }
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanFinish(long j) {
            Iterator<ScanObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onScanFinish(j);
            }
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanResultUpdate(long j) {
            Iterator<ScanObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onScanResultUpdate(j);
            }
        }
    }
}
