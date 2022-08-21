package com.miui.gallery.editor.photo.app.remover2;

import android.graphics.Bitmap;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.editor.photo.core.imports.remover2.BoundingBox;
import com.miui.gallery.editor.photo.core.imports.remover2.Remover2NNFData;
import com.miui.gallery.util.EditorThreadPoolUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes2.dex */
public class Inpaint2 {
    public static Inpaint2 sInstance = new Inpaint2();
    public boolean sHasPeople;
    public boolean sIsInited;
    public boolean sIsLoaded;
    public final Object mSync = new Object();
    public boolean mNeedSegment = true;
    public long inpaintInstancePtr = -1;

    public static /* synthetic */ void $r8$lambda$n2KsMmJIOKpSRcok4YJ8aNbffZg(Inpaint2 inpaint2, ObservableEmitter observableEmitter) {
        inpaint2.lambda$release$0(observableEmitter);
    }

    private native int genPeopleBitmapSDK(Bitmap bitmap, int i, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7);

    private native long initSDK();

    private native int inpaintSDK(Bitmap bitmap, Bitmap bitmap2, int i, int i2, Remover2NNFData remover2NNFData, long j, int i3, int[] iArr);

    private native void releaseSDK(long j);

    private native BoundingBox[] segmentPredictSDK(Bitmap bitmap, long j, int i, int i2, byte[] bArr);

    private native int tuneSDK(float[] fArr, float[] fArr2, Bitmap bitmap, int i, int i2, Bitmap bitmap2, long j, int i3);

    private native int upsampleSDK(Bitmap bitmap, int i, int i2, Remover2NNFData remover2NNFData);

    public static Inpaint2 getInstance() {
        return sInstance;
    }

    public void init() {
        synchronized (this.mSync) {
            try {
                if (!this.sIsLoaded) {
                    System.load(LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext()) + "/libmace.so");
                    System.load(LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext()) + "/libremove.so");
                    System.load(LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext()) + "/libvis.so");
                    System.load(LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext()) + "/libinpaint2_jni.so");
                    this.sIsLoaded = true;
                }
            } catch (Error unused) {
            }
            if (this.sIsLoaded && !this.sIsInited) {
                long initSDK = initSDK();
                this.inpaintInstancePtr = initSDK;
                if (initSDK != -1) {
                    this.sIsInited = true;
                }
            }
        }
    }

    public boolean isInited() {
        return this.sIsInited;
    }

    public boolean isNeedSegment() {
        return this.mNeedSegment;
    }

    public int tune(float[] fArr, float[] fArr2, Bitmap bitmap, int i, int i2, Bitmap bitmap2) {
        synchronized (this.mSync) {
            if (this.sIsInited) {
                long j = this.inpaintInstancePtr;
                if (j != -1) {
                    return tuneSDK(fArr, fArr2, bitmap, i, i2, bitmap2, j, 1);
                }
            }
            return -1;
        }
    }

    public int inpaint(Bitmap bitmap, Bitmap bitmap2, int i, int i2, Remover2NNFData remover2NNFData, int i3, int[] iArr) {
        synchronized (this.mSync) {
            if (this.sIsInited) {
                long j = this.inpaintInstancePtr;
                if (j != -1) {
                    return inpaintSDK(bitmap, bitmap2, i, i2, remover2NNFData, j, i3, iArr);
                }
            }
            return -1;
        }
    }

    public BoundingBox[] segmentPredict(Bitmap bitmap, byte[] bArr) {
        synchronized (this.mSync) {
            if (this.sIsLoaded) {
                long j = this.inpaintInstancePtr;
                if (j != -1) {
                    BoundingBox[] segmentPredictSDK = segmentPredictSDK(bitmap, j, bitmap.getWidth(), bitmap.getHeight(), bArr);
                    if (segmentPredictSDK != null && segmentPredictSDK.length > 0) {
                        this.sHasPeople = true;
                    } else {
                        this.sHasPeople = false;
                    }
                    this.mNeedSegment = false;
                    return segmentPredictSDK;
                }
            }
            return null;
        }
    }

    public void genPeopleBitmap(Bitmap bitmap, int i, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        genPeopleBitmapSDK(bitmap, i, bArr, i2, i3, i4, i5, i6, i7);
    }

    public boolean isHavePeople() {
        return this.sHasPeople;
    }

    public int upsample(Bitmap bitmap, Remover2NNFData remover2NNFData) {
        int upsampleSDK;
        synchronized (this.mSync) {
            upsampleSDK = upsampleSDK(bitmap, bitmap.getWidth(), bitmap.getHeight(), remover2NNFData);
        }
        return upsampleSDK;
    }

    public void release() {
        Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.app.remover2.Inpaint2$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                Inpaint2.$r8$lambda$n2KsMmJIOKpSRcok4YJ8aNbffZg(Inpaint2.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public /* synthetic */ void lambda$release$0(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(Boolean.valueOf(destroyInpaintPro()));
    }

    public final boolean destroyInpaintPro() {
        synchronized (this.mSync) {
            if (this.sIsInited) {
                long j = this.inpaintInstancePtr;
                if (j >= 0) {
                    this.sIsInited = false;
                    this.mNeedSegment = true;
                    releaseSDK(j);
                    return true;
                }
            }
            return false;
        }
    }
}
