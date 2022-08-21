package com.xiaomi.bokeh;

import android.graphics.Bitmap;
import android.util.Log;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.EditorThreadPoolUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes3.dex */
public class MiPortraitSegmenter {
    public static MiPortraitSegmenter sInstance = new MiPortraitSegmenter();
    public IPortraitEnhanceListener mEnhanceListener;
    public boolean mIsLoaded;
    public volatile boolean mIsPortraited;
    public volatile boolean mIsSegmented;
    public IPortraitSegmentListener mSegmentListener;
    public Disposable mUpdateEnhanceDisposable;
    public Disposable mWaitSegmentDisposable;
    public long mNativeAddress = 0;
    public final Object mSync = new Object();

    /* loaded from: classes3.dex */
    public interface IPortraitEnhanceListener {
        void processResult(Bitmap bitmap);
    }

    /* loaded from: classes3.dex */
    public interface IPortraitSegmentListener {
        void segment(boolean z);
    }

    /* renamed from: $r8$lambda$-dooV8kq1yCX8wbl3y2zpswxycI */
    public static /* synthetic */ void m1855$r8$lambda$dooV8kq1yCX8wbl3y2zpswxycI(MiPortraitSegmenter miPortraitSegmenter, Bitmap bitmap) {
        miPortraitSegmenter.lambda$updateEnhanceBitmap$3(bitmap);
    }

    /* renamed from: $r8$lambda$T2J0hDCcY-KvnWDgMnfOLOkxSSo */
    public static /* synthetic */ void m1856$r8$lambda$T2J0hDCcYKvnWDgMnfOLOkxSSo(MiPortraitSegmenter miPortraitSegmenter, Bitmap bitmap, float f, ObservableEmitter observableEmitter) {
        miPortraitSegmenter.lambda$updateEnhanceBitmap$2(bitmap, f, observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$jQ0d5gIw5G9Jyt6IVNjSawfSnTc(MiPortraitSegmenter miPortraitSegmenter, Boolean bool) {
        miPortraitSegmenter.lambda$waitSegment$1(bool);
    }

    public static /* synthetic */ void $r8$lambda$mY83gUmXN_sD2aiyvOJGj3l3TSE(MiPortraitSegmenter miPortraitSegmenter, Bitmap bitmap, ObservableEmitter observableEmitter) {
        miPortraitSegmenter.lambda$waitSegment$0(bitmap, observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$pLyXThFEmC2_juWDRueUhkDlB4c(MiPortraitSegmenter miPortraitSegmenter, ObservableEmitter observableEmitter) {
        miPortraitSegmenter.lambda$destroy$4(observableEmitter);
    }

    private native long createMiPortraitSegmenter();

    private native void destroyMiPortraitSegmenter(long j);

    private native int initMiPortraitSegmenter(long j);

    private native int segAndEnhanceBitmap(long j, Object obj, int i, int i2, int i3);

    private native void updateEnhanceBitmap(long j, Object obj, int i, int i2, float f);

    public static MiPortraitSegmenter getInstance() {
        return sInstance;
    }

    public final void initLibrary() {
        synchronized (this.mSync) {
            try {
                System.load(LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext()) + "/libFaceDetLmd.so");
                System.load(LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext()) + "/libmiportrait_seg.so");
                this.mIsLoaded = true;
            } catch (Error e) {
                Log.e("MiPortraitSegmenter", " portrait init error : ", e);
            }
        }
    }

    public final boolean initSegmenter() {
        boolean z;
        synchronized (this.mSync) {
            if (this.mNativeAddress != 0) {
                destroy();
            }
            long createMiPortraitSegmenter = createMiPortraitSegmenter();
            this.mNativeAddress = createMiPortraitSegmenter;
            z = initMiPortraitSegmenter(createMiPortraitSegmenter) == 0;
        }
        return z;
    }

    public void waitSegment(final Bitmap bitmap, IPortraitSegmentListener iPortraitSegmentListener) {
        if (this.mSegmentListener == null) {
            this.mSegmentListener = iPortraitSegmentListener;
        }
        destroyPortraitColorDisposable(this.mWaitSegmentDisposable);
        this.mWaitSegmentDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.xiaomi.bokeh.MiPortraitSegmenter$$ExternalSyntheticLambda1
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                MiPortraitSegmenter.$r8$lambda$mY83gUmXN_sD2aiyvOJGj3l3TSE(MiPortraitSegmenter.this, bitmap, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.xiaomi.bokeh.MiPortraitSegmenter$$ExternalSyntheticLambda4
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                MiPortraitSegmenter.$r8$lambda$jQ0d5gIw5G9Jyt6IVNjSawfSnTc(MiPortraitSegmenter.this, (Boolean) obj);
            }
        });
    }

    public /* synthetic */ void lambda$waitSegment$0(Bitmap bitmap, ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(Boolean.valueOf(waitSegment(bitmap)));
    }

    public /* synthetic */ void lambda$waitSegment$1(Boolean bool) throws Exception {
        IPortraitSegmentListener iPortraitSegmentListener = this.mSegmentListener;
        if (iPortraitSegmentListener != null) {
            iPortraitSegmentListener.segment(bool.booleanValue());
        }
    }

    public void updateEnhanceBitmap(final Bitmap bitmap, final float f, IPortraitEnhanceListener iPortraitEnhanceListener) {
        if (this.mEnhanceListener == null) {
            this.mEnhanceListener = iPortraitEnhanceListener;
        }
        this.mUpdateEnhanceDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.xiaomi.bokeh.MiPortraitSegmenter$$ExternalSyntheticLambda2
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                MiPortraitSegmenter.m1856$r8$lambda$T2J0hDCcYKvnWDgMnfOLOkxSSo(MiPortraitSegmenter.this, bitmap, f, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.xiaomi.bokeh.MiPortraitSegmenter$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                MiPortraitSegmenter.m1855$r8$lambda$dooV8kq1yCX8wbl3y2zpswxycI(MiPortraitSegmenter.this, (Bitmap) obj);
            }
        });
    }

    public /* synthetic */ void lambda$updateEnhanceBitmap$2(Bitmap bitmap, float f, ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(updateEnhanceBitmap(bitmap, f));
    }

    public /* synthetic */ void lambda$updateEnhanceBitmap$3(Bitmap bitmap) throws Exception {
        IPortraitEnhanceListener iPortraitEnhanceListener = this.mEnhanceListener;
        if (iPortraitEnhanceListener != null) {
            iPortraitEnhanceListener.processResult(bitmap);
        }
    }

    public void setSegmentListener(IPortraitSegmentListener iPortraitSegmentListener) {
        this.mSegmentListener = iPortraitSegmentListener;
    }

    public void setEnhanceListener(IPortraitEnhanceListener iPortraitEnhanceListener) {
        this.mEnhanceListener = iPortraitEnhanceListener;
    }

    public boolean waitSegment(Bitmap bitmap) {
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mSync) {
            if (this.mIsSegmented) {
                return this.mIsPortraited;
            }
            boolean z = false;
            if (bitmap == null) {
                Log.e("MiPortraitSegmenter", "bmp is null");
                return false;
            }
            Bitmap cloneBitmap = BitmapUtils.cloneBitmap(bitmap);
            if (cloneBitmap == bitmap) {
                Log.d("MiPortraitSegmenter", "waitSegment: createBitmap return same object as src");
                return false;
            }
            if (!this.mIsLoaded) {
                initLibrary();
                if (!this.mIsLoaded) {
                    return false;
                }
            }
            if (!initSegmenter()) {
                return false;
            }
            if (segAndEnhanceBitmap(this.mNativeAddress, cloneBitmap, bitmap.getWidth(), bitmap.getHeight(), 0) == 0) {
                z = true;
            }
            this.mIsPortraited = z;
            this.mIsSegmented = true;
            DefaultLogger.d("MiPortraitSegmenter", "[PORTRAIT COLOR] segment effect consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return this.mIsPortraited;
        }
    }

    public Bitmap updateEnhanceBitmap(Bitmap bitmap, float f) {
        synchronized (this.mSync) {
            if (!this.mIsPortraited) {
                return bitmap;
            }
            if (this.mNativeAddress == 0) {
                Log.v("MiPortraitSegmenter", "segAndEnhance native obj is null");
                return null;
            } else if (bitmap == null) {
                Log.e("MiPortraitSegmenter", "bmp is null");
                return null;
            } else {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                long currentTimeMillis = System.currentTimeMillis();
                updateEnhanceBitmap(this.mNativeAddress, bitmap, width, height, f);
                DefaultLogger.d("MiPortraitSegmenter", "[PORTRAIT COLOR] apply effect consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                return bitmap;
            }
        }
    }

    public final void destroyPortraitColorDisposable(Disposable disposable) {
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        disposable.dispose();
    }

    public void destroy() {
        this.mEnhanceListener = null;
        this.mSegmentListener = null;
        destroyPortraitColorDisposable(this.mWaitSegmentDisposable);
        destroyPortraitColorDisposable(this.mUpdateEnhanceDisposable);
        Observable.create(new ObservableOnSubscribe() { // from class: com.xiaomi.bokeh.MiPortraitSegmenter$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                MiPortraitSegmenter.$r8$lambda$pLyXThFEmC2_juWDRueUhkDlB4c(MiPortraitSegmenter.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public /* synthetic */ void lambda$destroy$4(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(Boolean.valueOf(destroyMiPortraitSegmenter()));
    }

    public final boolean destroyMiPortraitSegmenter() {
        synchronized (this.mSync) {
            long j = this.mNativeAddress;
            if (j != 0) {
                destroyMiPortraitSegmenter(j);
                this.mNativeAddress = 0L;
            }
            this.mIsPortraited = false;
            this.mIsSegmented = false;
        }
        return true;
    }
}
