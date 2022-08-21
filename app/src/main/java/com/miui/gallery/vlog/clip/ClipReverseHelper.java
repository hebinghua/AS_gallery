package com.miui.gallery.vlog.clip;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.clip.ClipReverseHelper;
import com.miui.gallery.vlog.clip.ReverseDialogFragment;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.home.VlogStorage;
import com.miui.gallery.vlog.tools.PathNameUtil;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.xiaomi.milab.videosdk.FrameRetriever;
import com.xiaomi.milab.videosdk.MediaTranscode;
import com.xiaomi.milab.videosdk.message.TranscodeCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;

/* loaded from: classes2.dex */
public class ClipReverseHelper {
    public Callback mCallback;
    public Context mContext;
    public String mDstPath;
    public FragmentManager mFragmentManager;
    public int mIndex;
    public Boolean mIsCanceled;
    public ReverseDialogFragment mReverseDialogFragment;
    public Disposable mReverseDisposable;
    public MediaTranscode mMediaTranscode = new MediaTranscode();
    public Handler mMainHandler = new Handler(Looper.getMainLooper());

    /* loaded from: classes2.dex */
    public interface Callback {
        void onCancel();

        void onFail(String str, String str2, int i);

        void onSuccess(String str, String str2, int i);
    }

    public static /* synthetic */ void $r8$lambda$8BtHE9urriZ7vbnug2ic14O2jfg(ClipReverseHelper clipReverseHelper) {
        clipReverseHelper.lambda$convertMediaFile$3();
    }

    /* renamed from: $r8$lambda$D4o5H1dHHQUiSxg-pn3baMlcC-w */
    public static /* synthetic */ void m1783$r8$lambda$D4o5H1dHHQUiSxgpn3baMlcCw(ClipReverseHelper clipReverseHelper, Throwable th) {
        clipReverseHelper.lambda$convertMediaFile$2(th);
    }

    public static /* synthetic */ void $r8$lambda$cDHiyEKzXwEceuRZo9cs1IGcPKA(ClipReverseHelper clipReverseHelper, Object obj) {
        clipReverseHelper.lambda$convertMediaFile$1(obj);
    }

    /* renamed from: $r8$lambda$wg8as6L0i-6m0tiWW87gu1HbM6I */
    public static /* synthetic */ void m1784$r8$lambda$wg8as6L0i6m0tiWW87gu1HbM6I(ClipReverseHelper clipReverseHelper, ObservableEmitter observableEmitter) {
        clipReverseHelper.lambda$convertMediaFile$0(observableEmitter);
    }

    public ClipReverseHelper(Context context) {
        this.mContext = context;
    }

    public boolean reverseFile(int i, String str, int i2, long j, long j2) {
        this.mIndex = i;
        if (TextUtils.isEmpty(str) || !new File(str).exists()) {
            DefaultLogger.e("ClipReverseHelper", "convert file is fail, the src file is not exists. ");
            return false;
        }
        String str2 = VlogStorage.getTempReverseFilePath() + PathNameUtil.getOutPathNameNoSuffix(str) + "_" + i2 + "_covert.mp4";
        this.mDstPath = str2;
        return convertMediaFile(str, str2, true, j, j2, null) == 0;
    }

    public final long convertMediaFile(String str, String str2, boolean z, long j, long j2, Hashtable<String, Object> hashtable) {
        if (this.mFragmentManager == null) {
            this.mFragmentManager = ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).getFragmentManager();
        }
        if (this.mReverseDialogFragment == null) {
            ReverseDialogFragment reverseDialogFragment = new ReverseDialogFragment();
            this.mReverseDialogFragment = reverseDialogFragment;
            reverseDialogFragment.setReverseCallback(new ReverseDialogFragment.ReverseCallback() { // from class: com.miui.gallery.vlog.clip.ClipReverseHelper$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.vlog.clip.ReverseDialogFragment.ReverseCallback
                public final void onCancel() {
                    ClipReverseHelper.$r8$lambda$8BtHE9urriZ7vbnug2ic14O2jfg(ClipReverseHelper.this);
                }
            });
        }
        this.mIsCanceled = Boolean.FALSE;
        this.mReverseDialogFragment.showAllowingStateLoss(this.mFragmentManager, "ReverseDialogFragment");
        FrameRetriever frameRetriever = new FrameRetriever();
        frameRetriever.setDataSource(str);
        MediaTranscode.EncodeParams encodeParams = new MediaTranscode.EncodeParams();
        encodeParams.audioBitrate = 48000;
        encodeParams.frequency = 44100;
        encodeParams.reverse = true;
        encodeParams.channels = 2;
        encodeParams.fps = 30.0d;
        encodeParams.from = ((int) j) / 1000;
        encodeParams.to = ((int) j2) / 1000;
        encodeParams.interval = 1;
        encodeParams.videoBitrate = (int) frameRetriever.getBitrate();
        encodeParams.height = frameRetriever.getHeight();
        encodeParams.width = frameRetriever.getWidth();
        frameRetriever.release();
        return this.mMediaTranscode.convert(str, str2, encodeParams, new TranscodeCallbackImpl(this, str, str2));
    }

    public /* synthetic */ void lambda$convertMediaFile$3() {
        this.mReverseDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.clip.ClipReverseHelper$$ExternalSyntheticLambda1
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                ClipReverseHelper.m1784$r8$lambda$wg8as6L0i6m0tiWW87gu1HbM6I(ClipReverseHelper.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.vlog.clip.ClipReverseHelper$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                ClipReverseHelper.$r8$lambda$cDHiyEKzXwEceuRZo9cs1IGcPKA(ClipReverseHelper.this, obj);
            }
        }, new Consumer() { // from class: com.miui.gallery.vlog.clip.ClipReverseHelper$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                ClipReverseHelper.m1783$r8$lambda$D4o5H1dHHQUiSxgpn3baMlcCw(ClipReverseHelper.this, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$convertMediaFile$0(ObservableEmitter observableEmitter) throws Exception {
        cancelReverse();
        observableEmitter.onNext(new Object());
        observableEmitter.onComplete();
    }

    public /* synthetic */ void lambda$convertMediaFile$1(Object obj) throws Exception {
        dismissDialog();
    }

    public /* synthetic */ void lambda$convertMediaFile$2(Throwable th) throws Exception {
        dismissDialog();
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void dismissDialog() {
        ReverseDialogFragment reverseDialogFragment = this.mReverseDialogFragment;
        if (reverseDialogFragment != null) {
            reverseDialogFragment.dismiss();
        }
    }

    public boolean cancelReverse() {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCancel();
        }
        this.mIsCanceled = Boolean.TRUE;
        this.mMediaTranscode.stop();
        this.mIndex = 0;
        if (!TextUtils.isEmpty(this.mDstPath)) {
            File file = new File(this.mDstPath);
            if (!file.exists()) {
                return true;
            }
            file.delete();
            this.mDstPath = "";
            return true;
        }
        return true;
    }

    /* loaded from: classes2.dex */
    public static class TranscodeCallbackImpl implements TranscodeCallback {
        public WeakReference<ClipReverseHelper> clipReverseHelperRef;
        public String destFile;
        public String srcFile;

        public static /* synthetic */ void $r8$lambda$P4yaPEEoBLk1fRBZwk6uc3Oo1rQ(TranscodeCallbackImpl transcodeCallbackImpl, ClipReverseHelper clipReverseHelper) {
            transcodeCallbackImpl.lambda$onTranscodeFail$2(clipReverseHelper);
        }

        /* renamed from: $r8$lambda$wGg_YZbdFl-m5_fhF-OLHDBNa-s */
        public static /* synthetic */ void m1785$r8$lambda$wGg_YZbdFlm5_fhFOLHDBNas(ClipReverseHelper clipReverseHelper, int i) {
            lambda$onTranscodeProgress$0(clipReverseHelper, i);
        }

        public static /* synthetic */ void $r8$lambda$wyxFckzBM1SqBdEGhn928A2PKYs(TranscodeCallbackImpl transcodeCallbackImpl, ClipReverseHelper clipReverseHelper) {
            transcodeCallbackImpl.lambda$onTranscodeSuccess$1(clipReverseHelper);
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeCancel() {
        }

        public TranscodeCallbackImpl(ClipReverseHelper clipReverseHelper, String str, String str2) {
            this.clipReverseHelperRef = new WeakReference<>(clipReverseHelper);
            this.srcFile = str;
            this.destFile = str2;
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeProgress(final int i) {
            final ClipReverseHelper clipReverseHelper = this.clipReverseHelperRef.get();
            if (clipReverseHelper == null) {
                return;
            }
            clipReverseHelper.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.clip.ClipReverseHelper$TranscodeCallbackImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ClipReverseHelper.TranscodeCallbackImpl.m1785$r8$lambda$wGg_YZbdFlm5_fhFOLHDBNas(ClipReverseHelper.this, i);
                }
            });
        }

        public static /* synthetic */ void lambda$onTranscodeProgress$0(ClipReverseHelper clipReverseHelper, int i) {
            clipReverseHelper.onProgress(i);
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeSuccess() {
            final ClipReverseHelper clipReverseHelper = this.clipReverseHelperRef.get();
            if (clipReverseHelper == null) {
                return;
            }
            clipReverseHelper.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.clip.ClipReverseHelper$TranscodeCallbackImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ClipReverseHelper.TranscodeCallbackImpl.$r8$lambda$wyxFckzBM1SqBdEGhn928A2PKYs(ClipReverseHelper.TranscodeCallbackImpl.this, clipReverseHelper);
                }
            });
        }

        public /* synthetic */ void lambda$onTranscodeSuccess$1(ClipReverseHelper clipReverseHelper) {
            clipReverseHelper.onFinish(this.srcFile, this.destFile, 0);
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeFail() {
            final ClipReverseHelper clipReverseHelper = this.clipReverseHelperRef.get();
            if (clipReverseHelper == null) {
                return;
            }
            clipReverseHelper.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.clip.ClipReverseHelper$TranscodeCallbackImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ClipReverseHelper.TranscodeCallbackImpl.$r8$lambda$P4yaPEEoBLk1fRBZwk6uc3Oo1rQ(ClipReverseHelper.TranscodeCallbackImpl.this, clipReverseHelper);
                }
            });
        }

        public /* synthetic */ void lambda$onTranscodeFail$2(ClipReverseHelper clipReverseHelper) {
            clipReverseHelper.onFail(this.srcFile, this.destFile, 0);
        }
    }

    public void onProgress(float f) {
        DefaultLogger.d("ClipReverseHelper", "onProgress: %s .", Float.valueOf(f));
        ReverseDialogFragment reverseDialogFragment = this.mReverseDialogFragment;
        if (reverseDialogFragment != null) {
            reverseDialogFragment.setProgress((int) f);
        }
    }

    public void onFinish(String str, String str2, int i) {
        DefaultLogger.d("ClipReverseHelper", "finish %s", str);
        ReverseDialogFragment reverseDialogFragment = this.mReverseDialogFragment;
        if (reverseDialogFragment != null) {
            reverseDialogFragment.dismiss();
        }
        if (this.mCallback == null || this.mIsCanceled.booleanValue()) {
            return;
        }
        this.mCallback.onSuccess(str, str2, i);
        this.mDstPath = "";
    }

    public void onFail(String str, String str2, int i) {
        DefaultLogger.d("ClipReverseHelper", "fail %s", str);
        ReverseDialogFragment reverseDialogFragment = this.mReverseDialogFragment;
        if (reverseDialogFragment != null) {
            reverseDialogFragment.dismiss();
        }
        if (this.mCallback == null || this.mIsCanceled.booleanValue()) {
            return;
        }
        this.mCallback.onFail(str, str2, i);
        this.mDstPath = "";
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void release() {
        Disposable disposable = this.mReverseDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mReverseDisposable.dispose();
        }
        ReverseDialogFragment reverseDialogFragment = this.mReverseDialogFragment;
        if (reverseDialogFragment != null) {
            reverseDialogFragment.setReverseCallback(null);
            this.mReverseDialogFragment.dismiss();
        }
        MediaTranscode mediaTranscode = this.mMediaTranscode;
        if (mediaTranscode != null) {
            mediaTranscode.release();
        }
        this.mMediaTranscode = null;
        this.mCallback = null;
        this.mDstPath = "";
    }
}
