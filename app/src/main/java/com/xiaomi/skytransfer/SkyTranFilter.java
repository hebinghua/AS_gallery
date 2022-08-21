package com.xiaomi.skytransfer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.editor.photo.app.sky.SeqExecutor;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyTransferTempData;
import com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes3.dex */
public class SkyTranFilter {
    public boolean mIsLoaded;
    public Bitmap mLastBitmap;
    public long mNativeObj;
    public SkyTransferTempData mTransferTempData;
    public static AtomicInteger sSegmentCounter = new AtomicInteger();
    public static SkyTranFilter sInstance = new SkyTranFilter();
    public int mSkyScene = 1;
    public final Object mSync = new Object();
    public SeqExecutor mSeqExecutor = new SeqExecutor();

    /* renamed from: $r8$lambda$E6Ssp7p07L-lbiqtdg3G9DmJo5M */
    public static /* synthetic */ void m2556$r8$lambda$E6Ssp7p07Llbiqtdg3G9DmJo5M(SkyTranFilter skyTranFilter) {
        skyTranFilter.release();
    }

    private native void bitmapAdjustMoment(long j, Object obj, Object obj2, byte[] bArr, byte[] bArr2, byte[] bArr3, int i, boolean z, float f);

    private native void bitmapTransferSky(long j, Object obj, Object obj2, byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5, byte[] bArr3, byte[] bArr4, byte[] bArr5, int i6, String str, boolean z, float f, boolean z2);

    private native void freeSkyTransfer(long j);

    private native int getSkyScene(long j, byte[] bArr, int i, int i2);

    private native int getSkyTransMode(long j);

    private native long newSkyTransfer(String str);

    private native void segment(long j, byte[] bArr, int i, int i2, byte[] bArr2, boolean z, boolean z2);

    public static SkyTranFilter getInstance() {
        return sInstance;
    }

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    public final void init() {
        if (!this.mIsLoaded) {
            try {
                String libraryDirPath = LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext());
                System.loadLibrary("c++_shared");
                System.load(libraryDirPath + "/libsegment.so");
                System.load(libraryDirPath + "/libscene_sky_classify.so");
                System.load(libraryDirPath + "/libsky_transfer_jni.so");
                this.mIsLoaded = true;
                DefaultLogger.d("SkyTranFilter", "library load success");
            } catch (Error e) {
                DefaultLogger.e("SkyTranFilter", e);
            }
        }
        if (!this.mIsLoaded || this.mNativeObj != 0) {
            return;
        }
        String str = GalleryApp.sGetAndroidContext().getFilesDir().getPath() + File.separator + "sky_alg_cache";
        StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("SkyTranFilter", "init"));
        this.mNativeObj = newSkyTransfer(str);
    }

    public int segmentSeq(final Bitmap bitmap) {
        final long currentTimeMillis = System.currentTimeMillis();
        final int incrementAndGet = sSegmentCounter.incrementAndGet();
        DefaultLogger.d("SkyTranFilter", "submit %d segment", Integer.valueOf(incrementAndGet));
        Integer num = (Integer) this.mSeqExecutor.runExclusive(1, new Callable<Integer>() { // from class: com.xiaomi.skytransfer.SkyTranFilter.1
            {
                SkyTranFilter.this = this;
            }

            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Integer mo2557call() throws Exception {
                DefaultLogger.d("SkyTranFilter", "execute %d segment", Integer.valueOf(incrementAndGet));
                int segment = SkyTranFilter.this.segment(bitmap);
                DefaultLogger.d("SkyTranFilter", "finish execute segment %d consume %d", Integer.valueOf(incrementAndGet), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                return Integer.valueOf(segment);
            }
        }, -1);
        if (num == null) {
            return 2;
        }
        return num.intValue();
    }

    public boolean transferSeq(final Bitmap bitmap, final Bitmap bitmap2, final SkyRenderData skyRenderData, final SkyTransferTempData skyTransferTempData, final boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        Boolean bool = (Boolean) this.mSeqExecutor.runExclusive(2, new Callable<Boolean>() { // from class: com.xiaomi.skytransfer.SkyTranFilter.2
            {
                SkyTranFilter.this = this;
            }

            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo2558call() throws Exception {
                return Boolean.valueOf(SkyTranFilter.this.transferSky(bitmap, bitmap2, skyRenderData, skyTransferTempData, z));
            }
        }, 2, 3);
        DefaultLogger.d("SkyTranFilter", "transfer seq consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public boolean transferAdjustMomentSeq(final Bitmap bitmap, final Bitmap bitmap2, final SkyRenderData skyRenderData) {
        long currentTimeMillis = System.currentTimeMillis();
        Boolean bool = (Boolean) this.mSeqExecutor.runExclusive(2, new Callable<Boolean>() { // from class: com.xiaomi.skytransfer.SkyTranFilter.3
            {
                SkyTranFilter.this = this;
            }

            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo2559call() throws Exception {
                return Boolean.valueOf(SkyTranFilter.this.transferSkyAdjustMoment(bitmap, bitmap2, skyRenderData));
            }
        }, 2, 3);
        DefaultLogger.d("SkyTranFilter", "adjust moment seq consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public void releaseSeqAsync() {
        DefaultLogger.d("SkyTranFilter", "submit release task");
        this.mSeqExecutor.submitExclusive(3, new Runnable() { // from class: com.xiaomi.skytransfer.SkyTranFilter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SkyTranFilter.m2556$r8$lambda$E6Ssp7p07Llbiqtdg3G9DmJo5M(SkyTranFilter.this);
            }
        }, 3);
    }

    public final void release() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.mNativeObj;
        if (j != 0) {
            freeSkyTransfer(j);
        }
        this.mTransferTempData = null;
        this.mNativeObj = 0L;
        this.mLastBitmap = null;
        this.mSkyScene = 1;
        DefaultLogger.d("SkyTranFilter", "release consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public final int segment(Bitmap bitmap) {
        synchronized (this.mSync) {
            SkyTransferTempData skyTransferTempData = this.mTransferTempData;
            if (skyTransferTempData != null && bitmap == this.mLastBitmap) {
                return skyTransferTempData.mSkyMode;
            }
            this.mTransferTempData = null;
            if (this.mNativeObj == 0) {
                init();
            }
            DefaultLogger.d("SkyTranFilter", "segment start");
            if (this.mNativeObj == 0) {
                DefaultLogger.w("SkyTranFilter", "segment mNativeObj is null");
                return 2;
            } else if (bitmap == null) {
                DefaultLogger.w("SkyTranFilter", "segment img is null or category < 0");
                return 2;
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                this.mTransferTempData = new SkyTransferTempData(bitmap);
                byte[] bitmap2RGB = BitmapUtils.bitmap2RGB(bitmap, true);
                long j = this.mNativeObj;
                SkyTransferTempData skyTransferTempData2 = this.mTransferTempData;
                segment(j, bitmap2RGB, skyTransferTempData2.mSegWidth, skyTransferTempData2.mSegHeight, skyTransferTempData2.mMaskData, true, true);
                this.mTransferTempData.mSkyMode = getSkyTransMode(this.mNativeObj);
                this.mTransferTempData.mCountDownLatch.countDown();
                DefaultLogger.d("SkyTranFilter", "segment end: %d consume %d", Integer.valueOf(this.mTransferTempData.mSkyMode), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                this.mLastBitmap = bitmap;
                return this.mTransferTempData.mSkyMode;
            }
        }
    }

    public final boolean transferSky(Bitmap bitmap, Bitmap bitmap2, SkyRenderData skyRenderData, SkyTransferTempData skyTransferTempData, boolean z) {
        if (bitmap == null || skyTransferTempData == null || skyRenderData == null) {
            return false;
        }
        if (skyTransferTempData.mSkyMode == 2) {
            DefaultLogger.w("SkyTranFilter", "transfer sky mode is forbidden");
            return false;
        }
        synchronized (this.mSync) {
            try {
                try {
                    DefaultLogger.d("SkyTranFilter", "transferSky start %d-%d ,%s", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), skyRenderData.toString());
                    if (this.mNativeObj == 0) {
                        DefaultLogger.w("SkyTranFilter", "transfer sky native obj is null");
                        return false;
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    int i = (!skyRenderData.isNocturne() || this.mSkyScene != 2) ? 3 : 4;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    StringBuilder sb = new StringBuilder();
                    sb.append(skyRenderData.getPath());
                    String str = File.separator;
                    sb.append(str);
                    sb.append(i == 3 ? skyRenderData.getBitmapPath() : "background_night");
                    Bitmap safeDecodeBitmap = BitmapUtils.safeDecodeBitmap(sb.toString(), options, null);
                    int width = safeDecodeBitmap.getWidth();
                    int height = safeDecodeBitmap.getHeight();
                    byte[] bitmap2RGB = i == 3 ? BitmapUtils.bitmap2RGB(safeDecodeBitmap, true) : BitmapUtils.bitmap2RGBA(safeDecodeBitmap);
                    BaseBitmapUtils.recycleSilently(safeDecodeBitmap);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(skyRenderData.getPath());
                    sb2.append(str);
                    sb2.append(i == 3 ? "fore" : "fore_night");
                    byte[] byteFromPath = getByteFromPath(sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(skyRenderData.getPath());
                    sb3.append(str);
                    sb3.append(i == 3 ? "whole" : "whole_night");
                    byte[] byteFromPath2 = getByteFromPath(sb3.toString());
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(skyRenderData.getPath());
                    sb4.append(str);
                    sb4.append(i == 3 ? "complex" : "complex_night");
                    byte[] byteFromPath3 = getByteFromPath(sb4.toString());
                    if (bitmap2RGB != null && byteFromPath != null && byteFromPath2 != null && byteFromPath3 != null) {
                        bitmapTransferSky(this.mNativeObj, bitmap, bitmap2, skyTransferTempData.mMaskData, skyTransferTempData.mSegWidth, skyTransferTempData.mSegHeight, bitmap2RGB, width, height, i, byteFromPath, byteFromPath2, byteFromPath3, skyRenderData.getMaterialId(), "", skyTransferTempData.mSkyMode == 1, skyRenderData.getProgress() / 100.0f, z);
                        skyRenderData.setCanAdjustMoment(true);
                        DefaultLogger.d("SkyTranFilter", "transferSky end consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                        return true;
                    }
                    DefaultLogger.w("SkyTranFilter", "res decode failed");
                    return false;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public void transferSkyForSave(Bitmap bitmap, SkyRenderData skyRenderData, SkyTransferTempData skyTransferTempData, boolean z) {
        transferSeq(bitmap, null, skyRenderData, skyTransferTempData, z);
    }

    public boolean transferSkyForShow(Bitmap bitmap, Bitmap bitmap2, SkyRenderData skyRenderData) {
        return transferSeq(bitmap, bitmap2, skyRenderData, this.mTransferTempData, false);
    }

    public final boolean transferSkyAdjustMoment(Bitmap bitmap, Bitmap bitmap2, SkyRenderData skyRenderData) {
        if (bitmap == null || skyRenderData == null) {
            return false;
        }
        synchronized (this.mSync) {
            if (this.mNativeObj == 0) {
                DefaultLogger.v("SkyTranFilter", "transfer sky native obj is null");
                return false;
            }
            long currentTimeMillis = System.currentTimeMillis();
            DefaultLogger.d("SkyTranFilter", "transferSkyAdjustMoment start %s", skyRenderData.toString());
            char c = (!skyRenderData.isNocturne() || this.mSkyScene != 2) ? (char) 3 : (char) 4;
            StringBuilder sb = new StringBuilder();
            sb.append(skyRenderData.getPath());
            String str = File.separator;
            sb.append(str);
            sb.append(c == 3 ? "fore" : "fore_night");
            byte[] byteFromPath = getByteFromPath(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(skyRenderData.getPath());
            sb2.append(str);
            sb2.append(c == 3 ? "whole" : "whole_night");
            byte[] byteFromPath2 = getByteFromPath(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append(skyRenderData.getPath());
            sb3.append(str);
            sb3.append(c == 3 ? "complex" : "complex_night");
            byte[] byteFromPath3 = getByteFromPath(sb3.toString());
            if (byteFromPath != null && byteFromPath2 != null && byteFromPath3 != null) {
                bitmapAdjustMoment(this.mNativeObj, bitmap, bitmap2, byteFromPath, byteFromPath2, byteFromPath3, skyRenderData.getMaterialId(), this.mTransferTempData.mSkyMode == 1, skyRenderData.getProgress() / 100.0f);
                DefaultLogger.d("SkyTranFilter", "transferSkyAdjustMoment end consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                return true;
            }
            DefaultLogger.w("SkyTranFilter", "res decode failed");
            return false;
        }
    }

    public SkyTransferTempData getTransferTempData() {
        return this.mTransferTempData;
    }

    public final byte[] getByteFromPath(String str) {
        Bitmap safeDecodeBitmap = BitmapUtils.safeDecodeBitmap(str, new BitmapFactory.Options(), null);
        byte[] bitmap2RGB = BitmapUtils.bitmap2RGB(safeDecodeBitmap, true);
        BaseBitmapUtils.recycleSilently(safeDecodeBitmap);
        return bitmap2RGB;
    }

    public void skyScene(Bitmap bitmap) {
        synchronized (this.mSync) {
            init();
            getSkyScene(this.mNativeObj, BitmapUtils.bitmap2RGB(bitmap, true), bitmap.getWidth(), bitmap.getHeight());
            this.mSkyScene = 1;
        }
    }
}
