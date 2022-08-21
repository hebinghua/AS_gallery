package com.miui.gallery.video.compress;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.EditorThreadPoolUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexChecker;
import com.xiaomi.mediatranscode.MediaTranscode;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class VideoCompressHelper {
    public static boolean sIsLoaded;
    public CompressCallback mCompressCallback;
    public int mCompressHeight;
    public int mCompressWidth;
    public volatile boolean mIsInit;
    public boolean mIsTranscoding;
    public String mOutputFile;
    public String mPath;
    public long mSize;
    public String mTempFile;
    public Handler mMainHandler = new Handler(Looper.getMainLooper());
    public List<Resolution> mDatas = new ArrayList();
    public MediaTranscode.Callback mCallback = new AnonymousClass1();

    /* loaded from: classes2.dex */
    public interface CompressCallback {
        void onCompressCancel();

        void onCompressFailed(int i);

        void onCompressFinish(String str);

        void onCompressProgress(int i);

        void onProbeResult(int i, List<Resolution> list);
    }

    static {
        try {
            System.load(LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext()) + "/libtranscode.so");
            sIsLoaded = true;
        } catch (Error e) {
            DefaultLogger.w("VideoCompressHelper_", e);
        }
    }

    public VideoCompressHelper(Context context) {
        this.mIsInit = false;
        if (this.mIsInit || !sIsLoaded) {
            return;
        }
        MediaTranscode.Init(context.getApplicationContext());
        this.mIsInit = true;
    }

    /* renamed from: com.miui.gallery.video.compress.VideoCompressHelper$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements MediaTranscode.Callback {
        public int progress;

        public AnonymousClass1() {
        }

        @Override // com.xiaomi.mediatranscode.MediaTranscode.Callback
        public void OnTranscodeProgress(int i) {
            this.progress = i;
            VideoCompressHelper.this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.1.1
                @Override // java.lang.Runnable
                public void run() {
                    if (VideoCompressHelper.this.mCompressCallback != null) {
                        VideoCompressHelper.this.mCompressCallback.onCompressProgress(AnonymousClass1.this.progress);
                    }
                }
            });
        }

        @Override // com.xiaomi.mediatranscode.MediaTranscode.Callback
        public void OnTranscodeFailed(final int i) {
            VideoCompressHelper.this.mIsTranscoding = false;
            if (!TextUtils.isEmpty(VideoCompressHelper.this.mOutputFile)) {
                String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("VideoCompressHelper_", "OnTranscodeFailed");
                StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                String tempFile = VideoCompressHelper.this.getTempFile();
                IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
                DocumentFile documentFile = storageStrategyManager.getDocumentFile(tempFile, permission, appendInvokerTag);
                if (documentFile != null) {
                    documentFile.delete();
                }
                DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(VideoCompressHelper.this.getOutputFile(), permission, appendInvokerTag);
                if (documentFile2 != null) {
                    documentFile2.delete();
                }
            }
            VideoCompressHelper.this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.1.2
                @Override // java.lang.Runnable
                public void run() {
                    if (VideoCompressHelper.this.mCompressCallback != null) {
                        VideoCompressHelper.this.mCompressCallback.onCompressFailed(i);
                    }
                }
            });
        }

        @Override // com.xiaomi.mediatranscode.MediaTranscode.Callback
        public void OnTranscodeSuccessed() {
            VideoCompressHelper.this.mIsTranscoding = false;
            final String outputFile = VideoCompressHelper.this.getOutputFile();
            DefaultLogger.d("VideoCompressHelper_", "OnTranscodeSuccessed:");
            StorageSolutionProvider.get().moveFile(VideoCompressHelper.this.getTempFile(), outputFile, FileHandleRecordHelper.appendInvokerTag("VideoCompressHelper_", "OnTranscodeSuccessed"));
            SpecialTypeMediaUtils.addVideoCompressPath(outputFile);
            ScannerEngine.getInstance().scanFile(GalleryApp.sGetAndroidContext(), outputFile, 13);
            SpecialTypeMediaUtils.removeVideoCompressPath(outputFile);
            VideoCompressHelper.this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.1.3
                @Override // java.lang.Runnable
                public void run() {
                    if (VideoCompressHelper.this.mCompressCallback != null) {
                        VideoCompressHelper.this.mCompressCallback.onCompressFinish(outputFile);
                    }
                }
            });
        }
    }

    public void prepareVideoCompress(final String str, long j) {
        if (!sIsLoaded) {
            return;
        }
        this.mPath = str;
        this.mSize = j;
        Observable.create(new ObservableOnSubscribe<Integer>() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.3
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                int i = 1;
                int ProbeTranscodeInfo = VideoCompressHelper.this.mIsInit ? MediaTranscode.ProbeTranscodeInfo(str) : 1;
                int i2 = 3;
                boolean z = false;
                boolean z2 = ProbeTranscodeInfo != 3;
                if (ProbeTranscodeInfo == 4 || ProbeTranscodeInfo == 5 || ProbeTranscodeInfo == 6) {
                    ProbeTranscodeInfo = 1;
                }
                if (str.contains("_COMPRESSED") && VideoCompressHelper.this.getVideoShortSize() <= 720) {
                    ProbeTranscodeInfo = 3;
                    z2 = false;
                }
                if (VideoCompressHelper.this.getVideoShortSize() > 720 || VideoCompressHelper.this.get720PCompressRatio() < 0.9d) {
                    i2 = ProbeTranscodeInfo;
                } else {
                    z2 = false;
                }
                if (VideoCompressHelper.this.get8KCompressRatio() > SearchStatUtils.POW && VideoCompressHelper.this.get8KCompressRatio() < 0.9d && VideoCompressHelper.this.get8KCompressRatio() * VideoCompressHelper.this.mSize < 3.5E9d) {
                    z2 = false;
                }
                if (VideoCompressHelper.this.get4KCompressRatio() > SearchStatUtils.POW && VideoCompressHelper.this.get4KCompressRatio() < 0.9d && VideoCompressHelper.this.get4KCompressRatio() * VideoCompressHelper.this.mSize < 3.5E9d) {
                    z2 = false;
                }
                if (VideoCompressHelper.this.get1080PCompressRatio() > SearchStatUtils.POW && VideoCompressHelper.this.get1080PCompressRatio() < 0.9d && VideoCompressHelper.this.get1080PCompressRatio() * VideoCompressHelper.this.mSize < 3.5E9d) {
                    z2 = false;
                }
                if (VideoCompressHelper.this.get720PCompressRatio() <= SearchStatUtils.POW || VideoCompressHelper.this.get720PCompressRatio() >= 0.9d || VideoCompressHelper.this.get720PCompressRatio() * VideoCompressHelper.this.mSize >= 3.5E9d) {
                    z = z2;
                }
                if (!z) {
                    i = i2;
                }
                if (i == 0) {
                    VideoCompressHelper.this.initData();
                }
                observableEmitter.onNext(Integer.valueOf(i));
            }
        }).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.2
            @Override // io.reactivex.functions.Consumer
            public void accept(Integer num) throws Exception {
                if (VideoCompressHelper.this.mCompressCallback != null) {
                    VideoCompressHelper.this.mCompressCallback.onProbeResult(num.intValue(), VideoCompressHelper.this.mDatas);
                }
            }
        });
    }

    public void compressVideo() {
        if (!sIsLoaded || this.mCallback == null || this.mCompressWidth == 0) {
            return;
        }
        this.mOutputFile = "";
        Observable.create(new ObservableOnSubscribe<String>() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.5
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                VideoCompressHelper.this.formatOutputFile();
                VideoCompressHelper.this.formatTempFile();
                if (VideoCompressHelper.this.mIsInit) {
                    String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("VideoCompressHelper_", "compressVideo");
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(VideoCompressHelper.this.getTempFile(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile != null && documentFile.exists()) {
                        documentFile.delete();
                    }
                    StorageSolutionProvider.get().getDocumentFile(VideoCompressHelper.this.getTempFile(), IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
                    VideoCompressHelper.this.mIsTranscoding = true;
                    MediaTranscode.Transcode(VideoCompressHelper.this.mPath, VideoCompressHelper.this.mTempFile, VideoCompressHelper.this.mCompressWidth, VideoCompressHelper.this.mCompressHeight, VideoCompressHelper.this.mCallback);
                    return;
                }
                observableEmitter.onNext("");
            }
        }).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.4
            @Override // io.reactivex.functions.Consumer
            public void accept(String str) throws Exception {
                if (VideoCompressHelper.this.mCompressCallback != null) {
                    VideoCompressHelper.this.mCompressCallback.onCompressCancel();
                }
            }
        });
    }

    public final void initData() {
        this.mDatas.clear();
        if (showOriginalOrNot()) {
            if (is8K()) {
                if (getVideoCompressRatio("video_4k") > 0.0f && getVideoCompressRatio("video_4k") < 0.9d && getVideoCompressRatio("video_4k") * ((float) this.mSize) < 3.5E9f) {
                    this.mDatas.add(new Resolution(get4KCompressWidth(), get4KCompressHeight(), getVideoCompressRatio("video_4k"), R.string.video_quality_4k, R.string.video_compress_size));
                }
                if (getVideoCompressRatio("video_1080P") <= 0.0f || getVideoCompressRatio("video_1080P") >= 0.9d || getVideoCompressRatio("video_1080P") * ((float) this.mSize) >= 3.5E9f) {
                    return;
                }
                this.mDatas.add(new Resolution(get1080PCompressWidth(), get1080PCompressHeight(), getVideoCompressRatio("video_1080P"), R.string.video_quality_1080p, R.string.video_compress_size));
            } else if (is4K()) {
                if (getVideoCompressRatio("video_4k") > 0.0f && getVideoCompressRatio("video_4k") < 0.9d && getVideoCompressRatio("video_4k") * ((float) this.mSize) < 3.5E9f) {
                    this.mDatas.add(new Resolution(getVideoWidth(), getVideoHeight(), getVideoCompressRatio("video_4k"), R.string.video_quality_4k_hd, R.string.video_compress_size));
                }
                if (getVideoCompressRatio("video_1080P") > 0.0f && getVideoCompressRatio("video_1080P") < 0.9d && getVideoCompressRatio("video_1080P") * ((float) this.mSize) < 3.5E9f) {
                    this.mDatas.add(new Resolution(get1080PCompressWidth(), get1080PCompressHeight(), getVideoCompressRatio("video_1080P"), R.string.video_quality_1080p, R.string.video_compress_size));
                }
                if (getVideoCompressRatio("video_720p") <= 0.0f || getVideoCompressRatio("video_720p") >= 0.9d || getVideoCompressRatio("video_720p") * ((float) this.mSize) >= 3.5E9f) {
                    return;
                }
                this.mDatas.add(new Resolution(get720PCompressWidth(), get720PCompressHeight(), getVideoCompressRatio("video_720p"), R.string.video_quality_720p, R.string.video_compress_size));
            } else if (is1080P()) {
                if (getVideoCompressRatio("video_1080P") > 0.0f && getVideoCompressRatio("video_1080P") < 0.9d && getVideoCompressRatio("video_1080P") * ((float) this.mSize) < 3.5E9f) {
                    this.mDatas.add(new Resolution(get1080PCompressWidth(), get1080PCompressHeight(), getVideoCompressRatio("video_1080P"), R.string.video_quality_1080p_hd, R.string.video_compress_size));
                }
                if (getVideoCompressRatio("video_720p") <= 0.0f || getVideoCompressRatio("video_720p") >= 0.9d || getVideoCompressRatio("video_720p") * ((float) this.mSize) >= 3.5E9f) {
                    return;
                }
                this.mDatas.add(new Resolution(get720PCompressWidth(), get720PCompressHeight(), getVideoCompressRatio("video_720p"), R.string.video_quality_720p, R.string.video_compress_size));
            } else if (!is720P() || getVideoCompressRatio("video_720p") <= 0.0f || getVideoCompressRatio("video_720p") >= 0.9d || getVideoCompressRatio("video_720p") * ((float) this.mSize) >= 3.5E9f) {
            } else {
                this.mDatas.add(new Resolution(get720PCompressWidth(), get720PCompressHeight(), getVideoCompressRatio("video_720p"), R.string.video_quality_720p_hd, R.string.video_compress_size));
            }
        } else if (is4K()) {
            if (getVideoCompressRatio("video_1080P") > 0.0f && getVideoCompressRatio("video_1080P") < 0.9d && getVideoCompressRatio("video_1080P") * ((float) this.mSize) < 3.5E9f) {
                this.mDatas.add(new Resolution(get1080PCompressWidth(), get1080PCompressHeight(), getVideoCompressRatio("video_1080P"), R.string.video_quality_1080p, R.string.video_compress_size));
            }
            if (getVideoCompressRatio("video_720p") <= 0.0f || getVideoCompressRatio("video_720p") >= 0.9d || getVideoCompressRatio("video_720p") * ((float) this.mSize) >= 3.5E9f) {
                return;
            }
            this.mDatas.add(new Resolution(get720PCompressWidth(), get720PCompressHeight(), getVideoCompressRatio("video_720p"), R.string.video_quality_720p, R.string.video_compress_size));
        } else if (!is1080P() || getVideoCompressRatio("video_720p") <= 0.0f || getVideoCompressRatio("video_720p") >= 0.9d || getVideoCompressRatio("video_720p") * ((float) this.mSize) >= 3.5E9f) {
        } else {
            this.mDatas.add(new Resolution(get720PCompressWidth(), get720PCompressHeight(), getVideoCompressRatio("video_720p"), R.string.video_quality_720p, R.string.video_compress_size));
        }
    }

    public final boolean showOriginalOrNot() {
        return !this.mPath.contains("_COMPRESSED");
    }

    public String initSubTitle(Context context, long j) {
        boolean contains = this.mPath.contains("_COMPRESSED");
        int i = R.string.video_quality_1080p;
        if (contains) {
            return is4K() ? context.getString(R.string.video_quality_title_sub, context.getString(R.string.video_quality_4k), FormatUtil.formatFileSize(context, j)) : is1080P() ? context.getString(R.string.video_quality_title_sub, context.getString(R.string.video_quality_1080p), FormatUtil.formatFileSize(context, j)) : "";
        } else if (is8K()) {
            return context.getString(R.string.video_quality_title_sub, context.getString(R.string.video_quality_8k), FormatUtil.formatFileSize(context, j));
        } else {
            if (is4K()) {
                Object[] objArr = new Object[2];
                objArr[0] = getVideoCompressRatio("video_4k") >= 1.0f ? context.getString(R.string.video_quality_4k) : context.getString(R.string.video_quality_4k_original);
                objArr[1] = FormatUtil.formatFileSize(context, j);
                return context.getString(R.string.video_quality_title_sub, objArr);
            } else if (!is1080P()) {
                return context.getString(R.string.video_quality_title_sub, context.getString(R.string.video_quality_720p_original), FormatUtil.formatFileSize(context, j));
            } else {
                Object[] objArr2 = new Object[2];
                if (getVideoCompressRatio("video_1080P") < 1.0f) {
                    i = R.string.video_quality_1080p_original;
                }
                objArr2[0] = context.getString(i);
                objArr2[1] = FormatUtil.formatFileSize(context, j);
                return context.getString(R.string.video_quality_title_sub, objArr2);
            }
        }
    }

    public final int get4KCompressWidth() {
        return getVideoWidth() > getVideoHeight() ? nexChecker.UHD_WIDTH : nexChecker.UHD_HEIGHT;
    }

    public final int get4KCompressHeight() {
        return getVideoHeight() > getVideoWidth() ? nexChecker.UHD_WIDTH : nexChecker.UHD_HEIGHT;
    }

    public final int get1080PCompressWidth() {
        if (getVideoWidth() > getVideoHeight()) {
            return (int) ((1080.0f / getVideoHeight()) * getVideoWidth());
        }
        return 1080;
    }

    public final int get1080PCompressHeight() {
        if (getVideoWidth() > getVideoHeight()) {
            return 1080;
        }
        return (int) ((1080.0f / getVideoWidth()) * getVideoHeight());
    }

    public final int get720PCompressWidth() {
        if (getVideoWidth() > getVideoHeight()) {
            return (int) ((720.0f / getVideoHeight()) * getVideoWidth());
        }
        return 720;
    }

    public final int get720PCompressHeight() {
        if (getVideoWidth() > getVideoHeight()) {
            return 720;
        }
        return (int) ((720.0f / getVideoWidth()) * getVideoHeight());
    }

    public final void formatTempFile() {
        this.mTempFile = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/timeBurst") + File.separator + ".gallery_compress_temp";
    }

    public String getTempFile() {
        if (TextUtils.isEmpty(this.mTempFile)) {
            formatTempFile();
        }
        return this.mTempFile + ".mp4";
    }

    public final void formatOutputFile() {
        String str = BaseFileUtils.getParentFolderPath(this.mPath) + File.separator;
        String str2 = str + BaseFileUtils.getFileNameWithoutExtension(this.mPath);
        Locale locale = Locale.US;
        this.mOutputFile = String.format(locale, "%s_%d%s", str2, 0, "_COMPRESSED");
        File file = new File(String.format(locale, "%s.mp4", this.mOutputFile));
        int i = 0;
        while (file.exists()) {
            i++;
            Locale locale2 = Locale.US;
            this.mOutputFile = String.format(locale2, "%s_%d%s", str2, Integer.valueOf(i), "_COMPRESSED");
            file = new File(String.format(locale2, "%s.mp4", this.mOutputFile));
        }
    }

    public void setCompressSize(int i, int i2) {
        this.mCompressWidth = i;
        this.mCompressHeight = i2;
    }

    public String getOutputFile() {
        if (TextUtils.isEmpty(this.mOutputFile)) {
            formatOutputFile();
        }
        return this.mOutputFile + ".mp4";
    }

    public int getVideoWidth() {
        return MediaTranscode.getVideoWidth();
    }

    public int getVideoHeight() {
        return MediaTranscode.getVideoHeight();
    }

    public final double get4KCompressRatio() {
        return MediaTranscode.get4KCompressRatio();
    }

    public final double get8KCompressRatio() {
        return MediaTranscode.get8KCompressRatio();
    }

    public final double get1080PCompressRatio() {
        return MediaTranscode.get1080PCompressRatio();
    }

    public final double get720PCompressRatio() {
        return MediaTranscode.get720PCompressRatio();
    }

    public final int getVideoLongSize() {
        return getVideoHeight() > getVideoWidth() ? getVideoHeight() : getVideoWidth();
    }

    public final int getVideoShortSize() {
        return getVideoHeight() < getVideoWidth() ? getVideoHeight() : getVideoWidth();
    }

    public boolean is8K() {
        return getVideoLongSize() >= 7680 && getVideoShortSize() >= 4320;
    }

    public boolean is4K() {
        return getVideoLongSize() >= 3840 && getVideoShortSize() >= 2160;
    }

    public boolean is1080P() {
        return getVideoShortSize() >= 1080;
    }

    public boolean is720P() {
        return getVideoShortSize() >= 720;
    }

    public float getVideoCompressRatio(String str) {
        double d;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1619723361:
                if (str.equals("video_720p")) {
                    c = 0;
                    break;
                }
                break;
            case 1151385851:
                if (str.equals("video_4k")) {
                    c = 1;
                    break;
                }
                break;
            case 1151385975:
                if (str.equals("video_8k")) {
                    c = 2;
                    break;
                }
                break;
            case 1322588437:
                if (str.equals("video_1080P")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                d = get720PCompressRatio();
                break;
            case 1:
                d = get4KCompressRatio();
                break;
            case 2:
                d = get8KCompressRatio();
                break;
            case 3:
                d = get1080PCompressRatio();
                break;
            default:
                d = SearchStatUtils.POW;
                break;
        }
        return (float) d;
    }

    public void cancelCompress() {
        if (!sIsLoaded) {
            return;
        }
        Observable.create(new ObservableOnSubscribe<String>() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.7
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                DefaultLogger.d("VideoCompressHelper_", "mIsTranscoding=%b, if true, cancel transcode.", Boolean.valueOf(VideoCompressHelper.this.mIsTranscoding));
                if (VideoCompressHelper.this.mIsTranscoding) {
                    VideoCompressHelper.this.mIsTranscoding = false;
                    MediaTranscode.CancelTranscode(VideoCompressHelper.this.mPath);
                    if (!TextUtils.isEmpty(VideoCompressHelper.this.mOutputFile)) {
                        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("VideoCompressHelper_", "cancelCompress");
                        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                        String tempFile = VideoCompressHelper.this.getTempFile();
                        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
                        DocumentFile documentFile = storageStrategyManager.getDocumentFile(tempFile, permission, appendInvokerTag);
                        if (documentFile != null) {
                            documentFile.delete();
                        }
                        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(VideoCompressHelper.this.getOutputFile(), permission, appendInvokerTag);
                        if (documentFile2 != null) {
                            documentFile2.delete();
                        }
                    }
                }
                observableEmitter.onNext("");
            }
        }).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() { // from class: com.miui.gallery.video.compress.VideoCompressHelper.6
            @Override // io.reactivex.functions.Consumer
            public void accept(String str) throws Exception {
                if (VideoCompressHelper.this.mCompressCallback != null) {
                    VideoCompressHelper.this.mCompressCallback.onCompressCancel();
                }
            }
        });
    }

    public void release() {
        this.mMainHandler.removeCallbacksAndMessages(null);
        this.mCallback = null;
        this.mCompressCallback = null;
        if (!this.mIsInit || !sIsLoaded) {
            return;
        }
        MediaTranscode.UnInit();
        this.mIsInit = false;
    }

    public void setCompressCallback(CompressCallback compressCallback) {
        this.mCompressCallback = compressCallback;
    }
}
