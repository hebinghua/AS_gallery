package com.miui.gallery.sdk.download;

import android.net.Uri;
import com.miui.gallery.sdk.download.DownloadOptions;
import com.miui.gallery.sdk.download.adapter.IUriAdapter;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.downloader.IDownloader;
import com.miui.gallery.sdk.download.downloader.MicroBatchDownloader;
import com.miui.gallery.sdk.download.downloader.MicroThumbnailDownloader;
import com.miui.gallery.sdk.download.downloader.OriginDownloader;
import com.miui.gallery.sdk.download.downloader.ThumbnailDownloader;
import com.miui.gallery.sdk.download.executor.AbsDownloadExecutor;
import com.miui.gallery.sdk.download.executor.SingleThreadExecutor;
import com.miui.gallery.sdk.download.executor.ThreadPoolExecutor;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public class DownloadEngine {
    public static final Map<DownloadType, Integer> sDownloaderMap;
    public static final Map<DownloadType, Integer> sExecutorsMap;
    public final DownloadOptions mDefaultDownloadOptions = new DownloadOptions.Builder().setUriAdapter(IUriAdapter.DEFAULT).build();
    public final Map<String, ReentrantLock> mUriLocks = new HashMap();
    public static final AbsDownloadExecutor[] sExecutors = new AbsDownloadExecutor[5];
    public static final IDownloader[] sDownloaders = new IDownloader[4];

    static {
        HashMap hashMap = new HashMap();
        sExecutorsMap = hashMap;
        DownloadType downloadType = DownloadType.MICRO;
        hashMap.put(downloadType, 0);
        DownloadType downloadType2 = DownloadType.THUMBNAIL;
        hashMap.put(downloadType2, 1);
        DownloadType downloadType3 = DownloadType.ORIGIN;
        hashMap.put(downloadType3, 2);
        DownloadType downloadType4 = DownloadType.ORIGIN_FORCE;
        hashMap.put(downloadType4, 2);
        DownloadType downloadType5 = DownloadType.THUMBNAIL_BATCH;
        hashMap.put(downloadType5, 3);
        DownloadType downloadType6 = DownloadType.ORIGIN_BATCH;
        hashMap.put(downloadType6, 3);
        DownloadType downloadType7 = DownloadType.MICRO_BATCH;
        hashMap.put(downloadType7, 4);
        HashMap hashMap2 = new HashMap();
        sDownloaderMap = hashMap2;
        hashMap2.put(downloadType, 0);
        hashMap2.put(downloadType2, 1);
        hashMap2.put(downloadType5, 1);
        hashMap2.put(downloadType3, 2);
        hashMap2.put(downloadType4, 2);
        hashMap2.put(downloadType6, 2);
        hashMap2.put(downloadType7, 3);
    }

    public void download(DownloadItem downloadItem, boolean z, boolean z2) {
        if (downloadItem == null || downloadItem.getKey() == null) {
            throw new IllegalArgumentException("Invalid download item");
        }
        getExecutor(downloadItem.getType()).download(downloadItem, z, z2);
    }

    /* renamed from: com.miui.gallery.sdk.download.DownloadEngine$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$sdk$download$DownloadType;

        static {
            int[] iArr = new int[DownloadType.values().length];
            $SwitchMap$com$miui$gallery$sdk$download$DownloadType = iArr;
            try {
                iArr[DownloadType.THUMBNAIL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.THUMBNAIL_BATCH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.ORIGIN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.ORIGIN_BATCH.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.ORIGIN_FORCE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.MICRO.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.MICRO_BATCH.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public final String generateLockKey(Uri uri, DownloadType downloadType) {
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$sdk$download$DownloadType[downloadType.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return uri.toString();
            case 6:
            case 7:
                return uri.buildUpon().appendQueryParameter(nexExportFormat.TAG_FORMAT_TYPE, "micro").build().toString();
            default:
                return DownloadUtil.generateKey(uri, downloadType);
        }
    }

    public ReentrantLock getLockForUri(Uri uri, DownloadType downloadType) {
        ReentrantLock reentrantLock;
        synchronized (this.mUriLocks) {
            String generateLockKey = generateLockKey(uri, downloadType);
            reentrantLock = this.mUriLocks.get(generateLockKey);
            if (reentrantLock == null) {
                reentrantLock = new ReentrantLock();
                this.mUriLocks.put(generateLockKey, reentrantLock);
            }
        }
        return reentrantLock;
    }

    public boolean cancel(DownloadItem downloadItem) {
        return getExecutor(downloadItem.getType()).cancel(downloadItem);
    }

    public void cancelAll(DownloadType downloadType) {
        getExecutor(downloadType).cancelAll();
    }

    public void cancelAll() {
        AbsDownloadExecutor[] absDownloadExecutorArr;
        for (AbsDownloadExecutor absDownloadExecutor : sExecutors) {
            if (absDownloadExecutor != null) {
                absDownloadExecutor.cancelAll();
            }
        }
    }

    public boolean contains(DownloadItem downloadItem) {
        return getExecutor(downloadItem.getType()).contains(downloadItem);
    }

    public DownloadItem peek(DownloadItem downloadItem) {
        return getExecutor(downloadItem.getType()).peek(downloadItem);
    }

    public IDownloader getDownloader(DownloadType downloadType) {
        IDownloader iDownloader;
        int intValue = sDownloaderMap.get(downloadType).intValue();
        IDownloader[] iDownloaderArr = sDownloaders;
        synchronized (iDownloaderArr) {
            if (iDownloaderArr[intValue] == null) {
                iDownloaderArr[intValue] = createDownloader(intValue);
            }
            iDownloader = iDownloaderArr[intValue];
        }
        return iDownloader;
    }

    public static IDownloader createDownloader(int i) {
        if (i != 0) {
            if (i == 1) {
                return new ThumbnailDownloader();
            }
            if (i == 2) {
                return new OriginDownloader();
            }
            if (i == 3) {
                return new MicroBatchDownloader();
            }
            throw new UnsupportedOperationException("Unsupported type: " + i);
        }
        return new MicroThumbnailDownloader();
    }

    public static AbsDownloadExecutor getExecutor(DownloadType downloadType) {
        AbsDownloadExecutor absDownloadExecutor;
        int intValue = sExecutorsMap.get(downloadType).intValue();
        AbsDownloadExecutor[] absDownloadExecutorArr = sExecutors;
        synchronized (absDownloadExecutorArr) {
            if (absDownloadExecutorArr[intValue] == null) {
                absDownloadExecutorArr[intValue] = createExecutor(intValue);
            }
            absDownloadExecutor = absDownloadExecutorArr[intValue];
        }
        return absDownloadExecutor;
    }

    public static AbsDownloadExecutor createExecutor(int i) {
        if (i != 0) {
            if (i == 1) {
                return new ThreadPoolExecutor(1, 100, "ThumbnailDownload");
            }
            if (i == 2) {
                return new ThreadPoolExecutor(1, -1, "OriginDownload");
            }
            if (i == 3) {
                return new ThreadPoolExecutor(10, -1, "BatchDownload");
            }
            if (i == 4) {
                return new SingleThreadExecutor(25, -1, "MicroBatchDownload");
            }
            throw new IllegalArgumentException("invalidate executor type " + i);
        }
        return new ThreadPoolExecutor(8, 100, "MicroDownload");
    }
}
