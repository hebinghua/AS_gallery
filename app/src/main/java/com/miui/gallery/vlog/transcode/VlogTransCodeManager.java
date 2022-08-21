package com.miui.gallery.vlog.transcode;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.tools.DebugLogUtils;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes2.dex */
public class VlogTransCodeManager {
    public Callback mCallback;
    public Disposable mCheckTransCodeDisposable;
    public boolean mIsSingleEdit;
    public LinkedList<String> mPaths;
    public Disposable mTransCodeDisposable;
    public TransCodeBean mTranscodeBean;

    /* loaded from: classes2.dex */
    public interface Callback {
        void handleResult(List<String> list);

        void onTransCode(boolean z);
    }

    /* renamed from: $r8$lambda$0-RB0b-AoOr2y2lm0664pK83sSg */
    public static /* synthetic */ void m1803$r8$lambda$0RB0bAoOr2y2lm0664pK83sSg(VlogTransCodeManager vlogTransCodeManager, ObservableEmitter observableEmitter) {
        vlogTransCodeManager.lambda$processTransCoding$0(observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$WiGNMrkjT5zZpXr5EUIuTVT4Mkw(VlogTransCodeManager vlogTransCodeManager, ObservableEmitter observableEmitter) {
        vlogTransCodeManager.lambda$transcode$2(observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$YgLCIidJEulKzBORo2ylcEcnieQ(VlogTransCodeManager vlogTransCodeManager, List list) {
        vlogTransCodeManager.lambda$transcode$3(list);
    }

    /* renamed from: $r8$lambda$dCh0dmyU9J7mINfTdG-NoSbgK7k */
    public static /* synthetic */ void m1804$r8$lambda$dCh0dmyU9J7mINfTdGNoSbgK7k(VlogTransCodeManager vlogTransCodeManager, Boolean bool) {
        vlogTransCodeManager.lambda$processTransCoding$1(bool);
    }

    public VlogTransCodeManager(Context context, List<String> list, boolean z) {
        this.mIsSingleEdit = false;
        LinkedList<String> linkedList = new LinkedList<>();
        this.mPaths = linkedList;
        if (list != null) {
            linkedList.addAll(list);
        }
        this.mIsSingleEdit = z;
        MiVideoSdkManager.loadSo(context);
        MiVideoSdkManager.initXmsContext(context);
    }

    public void processTransCoding(Callback callback) {
        this.mCallback = callback;
        this.mCheckTransCodeDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.transcode.VlogTransCodeManager$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                VlogTransCodeManager.m1803$r8$lambda$0RB0bAoOr2y2lm0664pK83sSg(VlogTransCodeManager.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.vlog.transcode.VlogTransCodeManager$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                VlogTransCodeManager.m1804$r8$lambda$dCh0dmyU9J7mINfTdGNoSbgK7k(VlogTransCodeManager.this, (Boolean) obj);
            }
        });
    }

    public /* synthetic */ void lambda$processTransCoding$0(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(Boolean.valueOf(needTranscode()));
    }

    public /* synthetic */ void lambda$processTransCoding$1(Boolean bool) throws Exception {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onTransCode(bool.booleanValue());
        }
        if (!bool.booleanValue()) {
            handleResult(this.mPaths);
        } else {
            transcode();
        }
    }

    public final void transcode() {
        DebugLogUtils.startDebugLog("VlogTransCodeManager_", "vlog transcode");
        this.mTransCodeDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.transcode.VlogTransCodeManager$$ExternalSyntheticLambda1
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                VlogTransCodeManager.$r8$lambda$WiGNMrkjT5zZpXr5EUIuTVT4Mkw(VlogTransCodeManager.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.vlog.transcode.VlogTransCodeManager$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                VlogTransCodeManager.$r8$lambda$YgLCIidJEulKzBORo2ylcEcnieQ(VlogTransCodeManager.this, (List) obj);
            }
        });
    }

    public /* synthetic */ void lambda$transcode$2(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(process());
    }

    public /* synthetic */ void lambda$transcode$3(List list) throws Exception {
        DebugLogUtils.endDebugLog("VlogTransCodeManager_", "vlog transcode");
        handleResult(list);
    }

    public final boolean needTranscode() {
        Iterator<String> it = this.mPaths.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (isNeedTransCode(it.next()) != 0) {
                z = true;
            }
        }
        DefaultLogger.d("VlogTransCodeManager_", "needTranscode: %b", Boolean.valueOf(z));
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:132:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0115  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<java.lang.String> process() {
        /*
            Method dump skipped, instructions count: 346
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.transcode.VlogTransCodeManager.process():java.util.List");
    }

    public final MediaTranscode.EncodeParams buildEncodeParams(FrameRetriever frameRetriever, int i, int i2) {
        MediaTranscode.EncodeParams encodeParams = new MediaTranscode.EncodeParams();
        encodeParams.audioBitrate = 48000;
        encodeParams.frequency = 44100;
        encodeParams.reverse = false;
        encodeParams.channels = 2;
        encodeParams.fps = 30.0d;
        encodeParams.from = 0L;
        encodeParams.to = frameRetriever.getDuration();
        encodeParams.interval = 1;
        encodeParams.videoBitrate = (int) frameRetriever.getBitrate();
        encodeParams.height = i2;
        encodeParams.width = i;
        return encodeParams;
    }

    public final int isNeedTransCode(String str) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.e("VlogTransCodeManager_", "path is null.");
            return 0;
        }
        if (this.mTranscodeBean == null) {
            this.mTranscodeBean = (TransCodeBean) VlogUtils.fromJson(VlogUtils.getJsonFromAssetDir(VlogUtils.getGalleryApp(), this.mIsSingleEdit ? "transcode/singletranscodeinfos.json" : "transcode/transcodeinfos.json"), TransCodeBean.class);
        }
        TransCodeBean transCodeBean = this.mTranscodeBean;
        if (transCodeBean == null) {
            DefaultLogger.e("VlogTransCodeManager_", "bean is null.");
            return 0;
        }
        return transCodeBean.isTransCodeAvailable(str);
    }

    public final String getDstPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String outPathNameNoSuffix = PathNameUtil.getOutPathNameNoSuffix(str);
        StringBuilder sb = new StringBuilder();
        sb.append(VlogConfig.getTransCodePath());
        sb.append(File.separator);
        sb.append(outPathNameNoSuffix);
        sb.append(this.mIsSingleEdit ? "_tcs_" : "_tcm_");
        sb.append(str.hashCode());
        sb.append(".mp4");
        return sb.toString();
    }

    public final void handleResult(List<String> list) {
        DefaultLogger.d("VlogTransCodeManager_", "handleResult path " + list.size());
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.handleResult(list);
        }
        release();
    }

    /* loaded from: classes2.dex */
    public static class TranscodeCallbackImpl implements TranscodeCallback {
        public CountDownLatch mCountDownLatch;
        public File mSrcFile;
        public int mStatus = 2;

        public TranscodeCallbackImpl(File file, CountDownLatch countDownLatch) {
            this.mSrcFile = file;
            this.mCountDownLatch = countDownLatch;
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeProgress(int i) {
            DefaultLogger.d("VlogTransCodeManager_", "trans progress.%s %s", Integer.valueOf(i), this.mSrcFile.getName());
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeSuccess() {
            DefaultLogger.d("VlogTransCodeManager_", "trans succeed.");
            this.mStatus = 1;
            this.mCountDownLatch.countDown();
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeCancel() {
            DefaultLogger.d("VlogTransCodeManager_", "trans cancel.");
            this.mStatus = 3;
            this.mCountDownLatch.countDown();
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeFail() {
            DefaultLogger.d("VlogTransCodeManager_", "trans failed.");
            this.mStatus = 2;
            this.mCountDownLatch.countDown();
        }

        public int getStatus() {
            return this.mStatus;
        }
    }

    public void release() {
        this.mCallback = null;
        this.mPaths.clear();
        Disposable disposable = this.mCheckTransCodeDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mCheckTransCodeDisposable.dispose();
            this.mCheckTransCodeDisposable = null;
        }
        Disposable disposable2 = this.mTransCodeDisposable;
        if (disposable2 != null && !disposable2.isDisposed()) {
            this.mTransCodeDisposable.dispose();
            this.mTransCodeDisposable = null;
        }
        ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).release();
    }
}
