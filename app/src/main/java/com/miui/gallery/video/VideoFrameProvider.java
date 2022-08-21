package com.miui.gallery.video;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.Surface;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.video.gallery.galleryvideo.gallery.GalleryVideoInfo;
import com.miui.video.localvideoplayer.FrameParams;
import com.miui.video.localvideoplayer.VideoFrameInterface;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes2.dex */
public class VideoFrameProvider {
    public static final String[] UNSUPPORTED_DEVICES;
    public static float mVideoVolume;
    public static boolean sIsDeviceSupported;
    public RequestTask mLastSingleFrameTask;
    public RequestTask mLastThumbListTask;
    public Handler mWorkHandler;
    public List<Listener> mListeners = new CopyOnWriteArrayList();
    public final LruCache<String, ThumbListInfo> mThumbListCache = new LruCache<>(2);
    public final LruCache<String, GalleryVideoInfo> mVideoInfoCache = new LruCache<>(2);
    public HandlerThread mHandlerThread = new HandlerThread("VideoFrameProvider-Thread");
    public Handler mMainHandler = new Handler(Looper.getMainLooper());
    public MiuiVideoConnection mConnection = new MiuiVideoConnection(GalleryApp.sGetAndroidContext());

    /* loaded from: classes2.dex */
    public interface Listener {
        void onSingleFrameResponse(String str, long j);

        void onThumbListResponse(String str, ThumbListInfo thumbListInfo);

        void onVideoInfoResponse(String str, GalleryVideoInfo galleryVideoInfo);
    }

    public static /* synthetic */ void $r8$lambda$huaFXAmvOsIrqstinb5Gptnt5S0(VideoFrameProvider videoFrameProvider, String str, GalleryVideoInfo galleryVideoInfo) {
        videoFrameProvider.lambda$notifyVideoInfoResponse$0(str, galleryVideoInfo);
    }

    public static /* synthetic */ void $r8$lambda$nQXJYZn0tIhj802DUhzpY53J2nY(VideoFrameProvider videoFrameProvider, String str, ThumbListInfo thumbListInfo) {
        videoFrameProvider.lambda$notifyThumbListResponse$1(str, thumbListInfo);
    }

    /* renamed from: $r8$lambda$ubvn37g17yd-ttQvE5bASUQ-Nzk */
    public static /* synthetic */ void m1746$r8$lambda$ubvn37g17ydttQvE5bASUQNzk(VideoFrameProvider videoFrameProvider, String str, long j) {
        videoFrameProvider.lambda$notifySingleFrame$2(str, j);
    }

    static {
        String[] strArr = {"land", "santoni", "ido", "nikel", "song", "rolex", "riva", "cactus", "cereus", "pine", "tiare", "olive", "olivewood", "dandelion", "prada", "mido", "rosy", "sakura", "daisy", "sakura_india", "tiffany", "tissot", "jasmine", "oxygen", "ugglite", "clover", "nitrogen", "ysl", "vince", "whyred", "tulip", "hercules", "pavo", "aquila", "onc", "lotus", "laurel_sprout", "kenzo", "urd", "avenger", "libra"};
        UNSUPPORTED_DEVICES = strArr;
        sIsDeviceSupported = true;
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("_");
            if (split.length > 0) {
                str = split[0];
            }
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (strArr[i].equalsIgnoreCase(str)) {
                    sIsDeviceSupported = false;
                    break;
                } else {
                    i++;
                }
            }
        }
        mVideoVolume = 1.0f;
    }

    public static boolean isDeviceSupport() {
        return sIsDeviceSupported;
    }

    public void onStart() {
        DefaultLogger.d("VideoFrameProvider", "onStart");
        this.mConnection.reset();
    }

    public void onStop() {
        DefaultLogger.d("VideoFrameProvider", "onStop");
        this.mConnection.cancel();
    }

    public void onMiuiVideoInstalled() {
        this.mConnection.onMiuiVideoInstalled();
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    public static void setVolume(float f) {
        mVideoVolume = f;
    }

    public static float getVolume() {
        return mVideoVolume;
    }

    public void prepareForVideo(String str) {
        if (this.mHandlerThread == null || TextUtils.isEmpty(str)) {
            return;
        }
        cancelTask(this.mLastSingleFrameTask);
        cancelTask(this.mLastThumbListTask);
        this.mLastSingleFrameTask = null;
        this.mLastThumbListTask = null;
        submitTask(new PrepareRequestTask(str));
    }

    public void setSurfaceForVideo(String str, Surface surface) {
        if (this.mHandlerThread == null || TextUtils.isEmpty(str) || surface == null) {
            return;
        }
        submitTask(new AssignSurfaceTask(str, surface));
    }

    public void releaseForVideo(String str) {
        if (this.mHandlerThread == null || TextUtils.isEmpty(str)) {
            return;
        }
        submitTask(new ReleaseRequestTask(str));
    }

    public void requestSingleFrame(String str, int i, int i2, long j, Surface surface) {
        if (this.mHandlerThread == null || TextUtils.isEmpty(str)) {
            return;
        }
        SingleFrameRequestTask singleFrameRequestTask = new SingleFrameRequestTask(str, j, i, i2, surface);
        cancelTask(this.mLastSingleFrameTask);
        submitTask(singleFrameRequestTask);
        this.mLastSingleFrameTask = singleFrameRequestTask;
    }

    public void requestThumbList(String str, int i, int i2, boolean z) {
        if (this.mHandlerThread == null || TextUtils.isEmpty(str)) {
            return;
        }
        ThumbListInfo thumbListInfo = getThumbListInfo(str);
        if (thumbListInfo == null || (!BaseMiscUtil.isValid(thumbListInfo.getThumbList()) && z)) {
            ThumbListRequestTask thumbListRequestTask = new ThumbListRequestTask(str, i, i2);
            cancelTask(this.mLastThumbListTask);
            submitTask(thumbListRequestTask);
            this.mLastThumbListTask = thumbListRequestTask;
            return;
        }
        notifyThumbListResponse(str, thumbListInfo);
    }

    public void requestThumbList(String str, int i, int i2, int i3, int i4, long j) {
        if (this.mHandlerThread == null || TextUtils.isEmpty(str)) {
            return;
        }
        ThumbListInfo thumbListInfo = getThumbListInfo(str);
        if (thumbListInfo != null) {
            notifyThumbListResponse(str, thumbListInfo);
            return;
        }
        ThumbListRequestTask thumbListRequestTask = new ThumbListRequestTask(str, i, i2, i3, i4, j);
        cancelTask(this.mLastThumbListTask);
        submitTask(thumbListRequestTask);
        this.mLastThumbListTask = thumbListRequestTask;
    }

    public void requestVideoInfo(String str) {
        if (this.mHandlerThread == null || TextUtils.isEmpty(str)) {
            return;
        }
        GalleryVideoInfo videoInfo = getVideoInfo(str);
        if (videoInfo != null) {
            notifyVideoInfoResponse(str, videoInfo);
        } else {
            submitTask(new VideoInfoRequestTask(str));
        }
    }

    public void cancelFrameTask() {
        if (this.mHandlerThread == null) {
            return;
        }
        cancelTask(this.mLastSingleFrameTask);
        cancelTask(this.mLastThumbListTask);
    }

    public ThumbListInfo getThumbListInfo(String str) {
        ThumbListInfo thumbListInfo;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        synchronized (this.mThumbListCache) {
            thumbListInfo = this.mThumbListCache.get(str);
        }
        return thumbListInfo;
    }

    public final GalleryVideoInfo getVideoInfo(String str) {
        GalleryVideoInfo galleryVideoInfo;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        synchronized (this.mVideoInfoCache) {
            galleryVideoInfo = this.mVideoInfoCache.get(str);
        }
        return galleryVideoInfo;
    }

    public void release() {
        if (this.mHandlerThread == null) {
            return;
        }
        this.mListeners.clear();
        synchronized (this.mThumbListCache) {
            this.mThumbListCache.evictAll();
        }
        synchronized (this.mVideoInfoCache) {
            this.mVideoInfoCache.evictAll();
        }
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        submitTask(new ReleaseTask());
    }

    public static int getFrameCount(long j) {
        if (j <= 0) {
            DefaultLogger.w("VideoFrameProvider", "the duration must more than 0");
            return 0;
        }
        int i = (int) (j / 1000);
        if (i > 7) {
            i = (int) ((Math.pow(i - 7, 0.4000000059604645d) + 7.0d) - 1.0d);
        }
        return Math.max(5, Math.min(i, 30));
    }

    public final void submitTask(Runnable runnable) {
        HandlerThread handlerThread;
        Handler handler = this.mWorkHandler;
        if (handler == null && (handlerThread = this.mHandlerThread) != null) {
            handlerThread.start();
            handler = new Handler(this.mHandlerThread.getLooper());
            this.mWorkHandler = handler;
        }
        if (handler != null) {
            DefaultLogger.d("VideoFrameProvider", "submit task [%s].", runnable.toString());
            handler.post(runnable);
        }
    }

    public final void cancelTask(Runnable runnable) {
        Handler handler = this.mWorkHandler;
        if (handler == null || runnable == null) {
            return;
        }
        handler.removeCallbacks(runnable);
    }

    public final void notifyVideoInfoResponse(final String str, final GalleryVideoInfo galleryVideoInfo) {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.VideoFrameProvider$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                VideoFrameProvider.$r8$lambda$huaFXAmvOsIrqstinb5Gptnt5S0(VideoFrameProvider.this, str, galleryVideoInfo);
            }
        });
    }

    public /* synthetic */ void lambda$notifyVideoInfoResponse$0(String str, GalleryVideoInfo galleryVideoInfo) {
        for (Listener listener : this.mListeners) {
            listener.onVideoInfoResponse(str, galleryVideoInfo);
        }
    }

    public final void notifyThumbListResponse(final String str, final ThumbListInfo thumbListInfo) {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.VideoFrameProvider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                VideoFrameProvider.$r8$lambda$nQXJYZn0tIhj802DUhzpY53J2nY(VideoFrameProvider.this, str, thumbListInfo);
            }
        });
    }

    public /* synthetic */ void lambda$notifyThumbListResponse$1(String str, ThumbListInfo thumbListInfo) {
        for (Listener listener : this.mListeners) {
            listener.onThumbListResponse(str, thumbListInfo);
        }
    }

    public final void notifySingleFrame(final String str, final long j) {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.VideoFrameProvider$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VideoFrameProvider.m1746$r8$lambda$ubvn37g17ydttQvE5bASUQNzk(VideoFrameProvider.this, str, j);
            }
        });
    }

    public /* synthetic */ void lambda$notifySingleFrame$2(String str, long j) {
        for (Listener listener : this.mListeners) {
            listener.onSingleFrameResponse(str, j);
        }
    }

    /* loaded from: classes2.dex */
    public class PrepareRequestTask extends RequestTask {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PrepareRequestTask(String str) {
            super(str, 0, 0);
            VideoFrameProvider.this = r2;
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.RequestTask
        public void handle(VideoFrameInterface videoFrameInterface) throws RemoteException {
            DefaultLogger.d("VideoFrameProvider", "prepareForVideo");
            videoFrameInterface.prepare(this.mPath);
        }
    }

    /* loaded from: classes2.dex */
    public class AssignSurfaceTask extends RequestTask {
        public Surface mSurface;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AssignSurfaceTask(String str, Surface surface) {
            super(str, 0, 0);
            VideoFrameProvider.this = r2;
            this.mSurface = surface;
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.RequestTask
        public void handle(VideoFrameInterface videoFrameInterface) throws RemoteException {
            DefaultLogger.d("VideoFrameProvider", "setSurfaceForVideo");
            Surface surface = this.mSurface;
            videoFrameInterface.setSurface(surface, surface.hashCode(), this.mPath);
        }
    }

    /* loaded from: classes2.dex */
    public class ReleaseRequestTask extends RequestTask {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ReleaseRequestTask(String str) {
            super(str, 0, 0);
            VideoFrameProvider.this = r2;
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.RequestTask
        public void handle(VideoFrameInterface videoFrameInterface) throws RemoteException {
            DefaultLogger.d("VideoFrameProvider", "releaseForVideo [%s].", this.mPath);
            videoFrameInterface.release(this.mPath);
        }
    }

    /* loaded from: classes2.dex */
    public class SingleFrameRequestTask extends RequestTask {
        public Surface mSurface;
        public long mTime;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SingleFrameRequestTask(String str, long j, int i, int i2, Surface surface) {
            super(str, i, i2);
            VideoFrameProvider.this = r1;
            this.mTime = j;
            this.mSurface = surface;
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.RequestTask
        public void handle(VideoFrameInterface videoFrameInterface) throws RemoteException {
            DefaultLogger.d("VideoFrameProvider", "request frame start %d", Long.valueOf(this.mTime));
            Surface surface = this.mSurface;
            videoFrameInterface.showPreviewFrameAtTime(surface, surface.hashCode(), this.mPath, this.mTime);
            VideoFrameProvider.this.notifySingleFrame(this.mPath, this.mTime);
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.RequestTask
        public void onError() {
            VideoFrameProvider.this.notifySingleFrame(this.mPath, this.mTime);
        }
    }

    /* loaded from: classes2.dex */
    public class ThumbListRequestTask extends RequestTask {
        public long mVideoDuration;
        public int mVideoHeight;
        public int mVideoWidth;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ThumbListRequestTask(String str, int i, int i2) {
            super(str, i, i2);
            VideoFrameProvider.this = r1;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ThumbListRequestTask(String str, int i, int i2, int i3, int i4, long j) {
            super(str, i, i2);
            VideoFrameProvider.this = r1;
            this.mVideoWidth = i3;
            this.mVideoHeight = i4;
            this.mVideoDuration = j;
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.RequestTask
        public void handle(VideoFrameInterface videoFrameInterface) throws RemoteException {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.mVideoWidth <= 0 || this.mVideoHeight <= 0 || this.mVideoDuration <= 0) {
                extractVideoMetadata(this.mPath);
                if (this.mVideoWidth <= 0 || this.mVideoHeight <= 0 || this.mVideoDuration <= 0) {
                    DefaultLogger.w("VideoFrameProvider", "request thumbList width height or duration invalid ");
                    return;
                }
            }
            int i = this.mVideoWidth;
            int i2 = this.mVideoHeight;
            float f = (i * 1.0f) / i2;
            int i3 = this.mRequestWidth;
            int i4 = this.mRequestHeight;
            if (f > (i3 * 1.0f) / i4) {
                i3 = (int) (((i4 * 1.0f) * i) / i2);
            } else {
                i4 = (int) (((i3 * 1.0f) * i2) / i);
            }
            List<Bitmap> arrayList = new ArrayList<>();
            RemoteException e = null;
            try {
                arrayList = getFrameList(videoFrameInterface, i3, i4);
            } catch (RemoteException e2) {
                e = e2;
            } catch (Exception e3) {
                DefaultLogger.e("VideoFrameProvider", "request thumb list error : %s", e3);
            }
            ThumbListInfo thumbListInfo = new ThumbListInfo(arrayList, this.mPath, this.mVideoWidth, this.mVideoHeight, this.mVideoDuration);
            VideoFrameProvider.this.notifyThumbListResponse(this.mPath, thumbListInfo);
            synchronized (VideoFrameProvider.this.mThumbListCache) {
                VideoFrameProvider.this.mThumbListCache.put(this.mPath, thumbListInfo);
            }
            DefaultLogger.d("VideoFrameProvider", "request thumb list cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            if (e == null) {
                return;
            }
            throw e;
        }

        public final void extractVideoMetadata(String str) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                try {
                    mediaMetadataRetriever.setDataSource(str);
                    this.mVideoDuration = Integer.parseInt(mediaMetadataRetriever.extractMetadata(9));
                    this.mVideoWidth = Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
                    this.mVideoHeight = Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
                } catch (Exception e) {
                    DefaultLogger.w("VideoFrameProvider", "extractVideoMetadata error\n", e);
                }
            } finally {
                mediaMetadataRetriever.release();
            }
        }

        public final List<Bitmap> getFrameList(VideoFrameInterface videoFrameInterface, int i, int i2) throws RemoteException {
            FrameParams frameList = videoFrameInterface.getFrameList(this.mPath, i, i2);
            ArrayList arrayList = new ArrayList();
            if (frameList != null) {
                FileDescriptor fileDescriptor = frameList.getFileDescriptor();
                if (fileDescriptor != null) {
                    byte[] readRemoteData = VideoFrameProvider.readRemoteData(fileDescriptor);
                    if (readRemoteData != null) {
                        int width = frameList.getWidth();
                        int height = frameList.getHeight();
                        int i3 = width * height * 4;
                        int count = frameList.getCount();
                        for (int i4 = 0; i4 < count; i4++) {
                            Bitmap createBitmap = VideoFrameProvider.createBitmap(readRemoteData, i3 * i4, width, height, frameList.getConfig());
                            if (createBitmap != null) {
                                arrayList.add(createBitmap);
                            }
                        }
                    }
                } else {
                    DefaultLogger.w("VideoFrameProvider", "request thumbList fd null");
                }
            } else {
                DefaultLogger.w("VideoFrameProvider", "request thumbList params null");
            }
            return arrayList;
        }
    }

    /* loaded from: classes2.dex */
    public class VideoInfoRequestTask extends RequestTask {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public VideoInfoRequestTask(String str) {
            super(str, -1, -1);
            VideoFrameProvider.this = r2;
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.RequestTask
        public void handle(VideoFrameInterface videoFrameInterface) throws RemoteException {
            GalleryVideoInfo videoInfo = videoFrameInterface.getVideoInfo(this.mPath);
            synchronized (VideoFrameProvider.this.mVideoInfoCache) {
                VideoFrameProvider.this.mVideoInfoCache.put(this.mPath, videoInfo);
            }
            VideoFrameProvider.this.notifyVideoInfoResponse(this.mPath, videoInfo);
        }
    }

    /* loaded from: classes2.dex */
    public class ReleaseTask implements Runnable {
        public ReleaseTask() {
            VideoFrameProvider.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.video.VideoFrameProvider.ReleaseTask.1
                {
                    ReleaseTask.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (VideoFrameProvider.this.mWorkHandler != null) {
                        VideoFrameProvider.this.mWorkHandler.removeCallbacksAndMessages(null);
                        VideoFrameProvider.this.mWorkHandler = null;
                    }
                    if (VideoFrameProvider.this.mHandlerThread != null) {
                        VideoFrameProvider.this.mHandlerThread.quit();
                        VideoFrameProvider.this.mHandlerThread = null;
                    }
                    VideoFrameProvider.this.mConnection.release();
                    VideoFrameProvider.this.mMainHandler.removeCallbacks(null);
                    VideoFrameProvider.this.onStop();
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public abstract class RequestTask implements Runnable {
        public String mPath;
        public int mRequestHeight;
        public int mRequestWidth;

        public abstract void handle(VideoFrameInterface videoFrameInterface) throws RemoteException;

        public void onError() {
        }

        public RequestTask(String str, int i, int i2) {
            VideoFrameProvider.this = r1;
            this.mPath = str;
            this.mRequestWidth = i;
            this.mRequestHeight = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            DefaultLogger.d("VideoFrameProvider", "start RequestTask [%s].", toString());
            VideoFrameInterface serviceInterface = VideoFrameProvider.this.mConnection.getServiceInterface();
            if (serviceInterface == null) {
                DefaultLogger.d("VideoFrameProvider", "interface null");
                return;
            }
            try {
                try {
                    handle(serviceInterface);
                } catch (Exception e) {
                    onError();
                    DefaultLogger.w("VideoFrameProvider", "interface remote error\n", e);
                }
            } finally {
                DefaultLogger.d("VideoFrameProvider", "end RequestTask [%s].", toString());
            }
        }

        public String toString() {
            return getClass().getSimpleName() + this.mPath;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v3, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r10v9 */
    public static byte[] readRemoteData(FileDescriptor fileDescriptor) {
        ?? r10;
        ByteArrayOutputStream byteArrayOutputStream;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        if (fileDescriptor == null || !fileDescriptor.valid()) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            try {
                fileInputStream = new FileInputStream(fileDescriptor);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            e = e;
            byteArrayOutputStream = null;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            r10 = 0;
            BaseMiscUtil.closeSilently(fileInputStream2);
            BaseMiscUtil.closeSilently(r10);
            throw th;
        }
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read > 0) {
                        byteArrayOutputStream.write(bArr, 0, read);
                    } else {
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        DefaultLogger.d("VideoFrameProvider", "read remote data length: %d, cost: %dms", Integer.valueOf(byteArray.length), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                        BaseMiscUtil.closeSilently(fileInputStream);
                        BaseMiscUtil.closeSilently(byteArrayOutputStream);
                        return byteArray;
                    }
                }
            } catch (Exception e2) {
                e = e2;
                DefaultLogger.w("VideoFrameProvider", "read remote data error\n", e);
                BaseMiscUtil.closeSilently(fileInputStream);
                BaseMiscUtil.closeSilently(byteArrayOutputStream);
                return null;
            }
        } catch (Exception e3) {
            e = e3;
            byteArrayOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            fileDescriptor = null;
            fileInputStream2 = fileInputStream;
            r10 = fileDescriptor;
            BaseMiscUtil.closeSilently(fileInputStream2);
            BaseMiscUtil.closeSilently(r10);
            throw th;
        }
    }

    public static Bitmap createBitmap(byte[] bArr, int i, int i2, int i3, Bitmap.Config config) {
        if (bArr != null && bArr.length != 0) {
            int i4 = i2 * i3;
            int i5 = (i4 * 4) + i;
            if (i5 > bArr.length) {
                return null;
            }
            int[] iArr = new int[i4];
            int i6 = 0;
            while (true) {
                int i7 = i + 3;
                if (i7 < i5) {
                    iArr[i6] = (bArr[i7] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
                    i += 4;
                    i6++;
                } else {
                    try {
                        return BaseBitmapUtils.transformSafeDrawBitmap(Bitmap.createBitmap(iArr, i2, i3, config));
                    } catch (Exception e) {
                        DefaultLogger.w("VideoFrameProvider", "createBitmap error\n", e);
                    }
                }
            }
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public class MiuiVideoConnection {
        public Context mContext;
        public ServiceConnectionImpl mServiceConnection;
        public VideoFrameInterface mServiceInterface;
        public boolean mEnable = true;
        public boolean mCanceled = false;

        public MiuiVideoConnection(Context context) {
            VideoFrameProvider.this = r1;
            this.mContext = context;
        }

        public synchronized void onMiuiVideoInstalled() {
            this.mEnable = true;
        }

        public synchronized void reset() {
            this.mCanceled = false;
        }

        public synchronized void cancel() {
            this.mCanceled = true;
            disconnect(this.mEnable);
        }

        public synchronized void release() {
            disconnect(false);
            this.mContext = null;
        }

        public synchronized VideoFrameInterface getServiceInterface() {
            return connect();
        }

        public final synchronized VideoFrameInterface connect() {
            VideoFrameInterface videoFrameInterface = this.mServiceInterface;
            if (videoFrameInterface != null) {
                return videoFrameInterface;
            }
            if (!this.mEnable || this.mCanceled || this.mContext == null) {
                return null;
            }
            try {
                if (this.mServiceConnection == null) {
                    Intent intent = new Intent();
                    intent.setClassName(VideoPlayerCompat.getMiuiVideoPackageName(), "com.miui.video.localvideoplayer.VideoFrameService");
                    intent.putExtra("version", 1);
                    if (BaseMiscUtil.isServiceSupported(intent)) {
                        ServiceConnectionImpl serviceConnectionImpl = new ServiceConnectionImpl();
                        this.mServiceConnection = serviceConnectionImpl;
                        if (this.mContext.bindService(intent, serviceConnectionImpl, 1)) {
                            DefaultLogger.d("MiuiVideoConnection", "bind service success");
                            wait();
                        } else {
                            DefaultLogger.w("MiuiVideoConnection", "bind service failed");
                            disconnect(false);
                        }
                    } else {
                        DefaultLogger.w("MiuiVideoConnection", "bind service not support.");
                        disconnect(false);
                    }
                } else {
                    wait();
                }
            } catch (InterruptedException unused) {
            } catch (Exception e) {
                DefaultLogger.w("MiuiVideoConnection", "bind service error.\n", e);
                disconnect(false);
            }
            return this.mServiceInterface;
        }

        public final synchronized void disconnect(boolean z) {
            DefaultLogger.d("MiuiVideoConnection", "disconnect");
            this.mEnable = z;
            this.mServiceInterface = null;
            ServiceConnectionImpl serviceConnectionImpl = this.mServiceConnection;
            if (serviceConnectionImpl != null) {
                try {
                    Context context = this.mContext;
                    if (context != null) {
                        context.unbindService(serviceConnectionImpl);
                        DefaultLogger.d("MiuiVideoConnection", "unbind service");
                    }
                } catch (Exception e) {
                    DefaultLogger.w("MiuiVideoConnection", "unbind service error.\n", e);
                }
                this.mServiceConnection.release();
                this.mServiceConnection = null;
            }
            notifyAll();
        }

        public final synchronized void onConnectSuccess(ServiceConnectionImpl serviceConnectionImpl, IBinder iBinder) {
            DefaultLogger.d("MiuiVideoConnection", "connect success");
            if (serviceConnectionImpl.isReleased()) {
                return;
            }
            VideoFrameInterface asInterface = VideoFrameInterface.Stub.asInterface(iBinder);
            this.mServiceInterface = asInterface;
            if (asInterface != null) {
                notifyAll();
                DefaultLogger.d("MiuiVideoConnection", "connected");
            } else {
                disconnect(false);
            }
        }

        public final synchronized void onConnectFailed(ServiceConnectionImpl serviceConnectionImpl, boolean z) {
            DefaultLogger.d("MiuiVideoConnection", "connect failed");
            if (serviceConnectionImpl.isReleased()) {
                return;
            }
            disconnect(z);
        }

        /* loaded from: classes2.dex */
        public class ServiceConnectionImpl implements ServiceConnection {
            public volatile boolean mReleased;

            public ServiceConnectionImpl() {
                MiuiVideoConnection.this = r1;
            }

            public void release() {
                this.mReleased = true;
            }

            public boolean isReleased() {
                return this.mReleased;
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MiuiVideoConnection.this.onConnectSuccess(this, iBinder);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                DefaultLogger.w("MiuiVideoConnection", "onServiceDisconnected");
                MiuiVideoConnection.this.onConnectFailed(this, true);
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(ComponentName componentName) {
                DefaultLogger.w("MiuiVideoConnection", "onBindingDied");
                MiuiVideoConnection.this.onConnectFailed(this, true);
            }

            @Override // android.content.ServiceConnection
            public void onNullBinding(ComponentName componentName) {
                DefaultLogger.w("MiuiVideoConnection", "onNullBinding");
                MiuiVideoConnection.this.onConnectFailed(this, false);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ThumbListInfo {
        public long mDuration;
        public int mHeight;
        public String mPath;
        public List<Bitmap> mThumbList;
        public int mWidth;

        public ThumbListInfo(List<Bitmap> list, String str, int i, int i2, long j) {
            this.mThumbList = list;
            this.mPath = str;
            this.mWidth = i;
            this.mHeight = i2;
            this.mDuration = j;
        }

        public List<Bitmap> getThumbList() {
            return this.mThumbList;
        }

        public long getDuration() {
            return this.mDuration;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public int hashCode() {
            String str = this.mPath;
            if (str == null) {
                return 0;
            }
            return str.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof ThumbListInfo) {
                return TextUtils.equals(this.mPath, ((ThumbListInfo) obj).mPath);
            }
            return false;
        }
    }
}
