package com.miui.gallery.video.timeburst;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Size;
import android.view.Surface;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.assistant.ImageUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.timeburst.gles.EglCore;
import com.miui.gallery.video.timeburst.gles.GLTextureShader;
import com.miui.gallery.video.timeburst.gles.OpenGlUtils;
import com.miui.gallery.video.timeburst.gles.WindowSurface;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes2.dex */
public class BurstPhotoComposer implements IBurstPhotoComposer {
    public BitmapDecoder mBitmapDecoder;
    public boolean mCancel;
    public ComposeCallback mComposeCallback;
    public Handler mComposeHandler;
    public ComposeThread mComposeThread;
    public long mConsumeMem;
    public int mDegree;
    public Encoder mEncoder;
    public int mHeight;
    public Handler mMainHandler;
    public String mOutputFile;
    public List<String> mPathList;
    public BlockingQueue<FrameHolder> mQueue;
    public String mTempOutputFile;
    public final int mTotalFrame;
    public int mWidth;
    public int mLifeStatus = 0;
    public Encoder.Callback mEncoderCallback = new Encoder.Callback() { // from class: com.miui.gallery.video.timeburst.BurstPhotoComposer.1
        public int lastProgress = -1;

        @Override // com.miui.gallery.video.timeburst.BurstPhotoComposer.Encoder.Callback
        public void onEncode(int i) {
            int i2 = (int) ((i * 100) / BurstPhotoComposer.this.mTotalFrame);
            if (i2 != this.lastProgress) {
                BurstPhotoComposer.this.callOnProgress(i2);
            }
        }
    };

    public BurstPhotoComposer(Context context, List<String> list, int i, int i2, int i3) {
        long phoneTotalMem = Utils.getPhoneTotalMem(context);
        this.mConsumeMem = ((float) phoneTotalMem) * 0.1f;
        this.mPathList = list;
        this.mTotalFrame = list.size();
        this.mWidth = i;
        this.mHeight = i2;
        this.mDegree = i3;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mOutputFile = Utils.createOutputFile(new File(list.get(0)).getParent());
        String tempFile = getTempFile();
        this.mTempOutputFile = tempFile;
        Encoder encoder = new Encoder(this.mWidth, this.mHeight, tempFile, this.mDegree);
        this.mEncoder = encoder;
        encoder.setCallback(this.mEncoderCallback);
        this.mBitmapDecoder = new BitmapDecoder(this, this.mPathList, this.mWidth, this.mHeight);
        DefaultLogger.d("BurstPhoto_Composer", "[Time Burst] total mem %d, consume mem %d", Long.valueOf(phoneTotalMem), Long.valueOf(this.mConsumeMem));
        DefaultLogger.d("BurstPhoto_Composer", "[Time Burst] output video size %d x %d", Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight));
        ComposeThread composeThread = new ComposeThread();
        this.mComposeThread = composeThread;
        composeThread.start();
        this.mComposeHandler = new Handler(this.mComposeThread.getLooper(), this.mComposeThread);
    }

    public String getOutputPath() {
        return this.mOutputFile;
    }

    @Override // com.miui.gallery.video.timeburst.IBurstPhotoComposer
    public void compose() {
        if (this.mLifeStatus != 0) {
            return;
        }
        this.mLifeStatus = 1;
        this.mComposeHandler.sendEmptyMessage(100);
    }

    /* loaded from: classes2.dex */
    public class ComposeThread extends HandlerThread implements Handler.Callback {
        public ComposeThread() {
            super("compose-thread");
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i != 100) {
                if (i != 101) {
                    return false;
                }
                BurstPhotoComposer.this.releaseInner();
                return false;
            }
            try {
                handleResult(BurstPhotoComposer.this.composeInner());
                return false;
            } catch (Exception unused) {
                handleResult(2);
                return false;
            }
        }

        public final void handleResult(int i) {
            if (i == 1) {
                BurstPhotoComposer.this.callOnFinish();
            } else if (i == 2) {
                BurstPhotoComposer.this.callOnError();
            } else if (i != 3) {
            } else {
                BurstPhotoComposer.this.callOnCancel();
            }
        }
    }

    public final void callOnError() {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.timeburst.BurstPhotoComposer.2
            @Override // java.lang.Runnable
            public void run() {
                if (BurstPhotoComposer.this.mComposeCallback != null) {
                    BurstPhotoComposer.this.mComposeCallback.onError();
                }
            }
        });
    }

    public final void callOnFinish() {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.timeburst.BurstPhotoComposer.3
            @Override // java.lang.Runnable
            public void run() {
                if (BurstPhotoComposer.this.mComposeCallback != null) {
                    BurstPhotoComposer.this.mComposeCallback.onFinish(BurstPhotoComposer.this.getOutputPath());
                }
            }
        });
    }

    public final void callOnCancel() {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.timeburst.BurstPhotoComposer.4
            @Override // java.lang.Runnable
            public void run() {
                if (BurstPhotoComposer.this.mComposeCallback != null) {
                    BurstPhotoComposer.this.mComposeCallback.onCancel();
                }
            }
        });
    }

    public final int composeInner() {
        if (!BaseMiscUtil.isValid(this.mPathList)) {
            DefaultLogger.d("BurstPhoto_Composer", "source list is null");
            return 2;
        } else if (!this.mEncoder.isSupport(this.mWidth, this.mHeight)) {
            DefaultLogger.d("BurstPhoto_Composer", "width height not support");
            return 2;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            Size decodeSize = this.mBitmapDecoder.getDecodeSize();
            int width = decodeSize.getWidth() * decodeSize.getHeight() * 4;
            int min = Math.min(10, (int) (this.mConsumeMem / width));
            this.mQueue = new LinkedBlockingQueue(min);
            DefaultLogger.d("BurstPhoto_Composer", "[Time Burst] decode size %dx%d,buffer count %d,consume mem %d", Integer.valueOf(decodeSize.getWidth()), Integer.valueOf(decodeSize.getHeight()), Integer.valueOf(min), Integer.valueOf(min * width));
            this.mBitmapDecoder.start();
            boolean encodeFrames = this.mEncoder.encodeFrames();
            releaseInner();
            DefaultLogger.d("BurstPhoto_Composer", "[Time Burst] compose consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            if (this.mCancel) {
                deleteTempFile();
                return 3;
            } else if (encodeFrames) {
                return StorageSolutionProvider.get().moveFile(this.mTempOutputFile, this.mOutputFile, FileHandleRecordHelper.appendInvokerTag("BurstPhoto_Composer", "composeInner")) ? 1 : 2;
            } else {
                deleteTempFile();
                return 2;
            }
        }
    }

    @Override // com.miui.gallery.video.timeburst.IBurstPhotoComposer
    public void cancel() {
        this.mCancel = true;
        BitmapDecoder bitmapDecoder = this.mBitmapDecoder;
        if (bitmapDecoder != null) {
            bitmapDecoder.stop();
        }
        Encoder encoder = this.mEncoder;
        if (encoder != null) {
            encoder.stop();
        }
    }

    @Override // com.miui.gallery.video.timeburst.IBurstPhotoComposer
    public void release() {
        this.mComposeHandler.sendEmptyMessage(101);
        this.mComposeThread.quitSafely();
        this.mComposeCallback = null;
        this.mLifeStatus = 2;
    }

    public final void releaseInner() {
        Encoder encoder = this.mEncoder;
        if (encoder != null) {
            encoder.release();
            this.mEncoder = null;
        }
        BitmapDecoder bitmapDecoder = this.mBitmapDecoder;
        if (bitmapDecoder != null) {
            bitmapDecoder.release();
            this.mBitmapDecoder = null;
        }
        BlockingQueue<FrameHolder> blockingQueue = this.mQueue;
        if (blockingQueue != null) {
            blockingQueue.clear();
        }
    }

    @Override // com.miui.gallery.video.timeburst.IBurstPhotoComposer
    public void setComposeCallback(ComposeCallback composeCallback) {
        this.mComposeCallback = composeCallback;
    }

    public final void callOnProgress(final int i) {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.video.timeburst.BurstPhotoComposer.5
            @Override // java.lang.Runnable
            public void run() {
                if (BurstPhotoComposer.this.mComposeCallback != null) {
                    BurstPhotoComposer.this.mComposeCallback.onProgress(i);
                }
            }
        });
    }

    public final void deleteTempFile() {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(this.mTempOutputFile, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("BurstPhoto_Composer", "deleteTempFile"));
        if (documentFile != null) {
            documentFile.delete();
        }
    }

    public final String getTempFile() {
        String str = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/timeBurst") + File.separator + ".burstvideo";
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("BurstPhoto_Composer", "getTempFile");
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
        if (documentFile != null && documentFile.exists()) {
            documentFile.delete();
        }
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
        if (documentFile2 == null || !documentFile2.exists()) {
            return null;
        }
        return str;
    }

    /* loaded from: classes2.dex */
    public static class FrameHolder {
        public Bitmap mBitmap;
        public boolean mLast;

        public FrameHolder(Bitmap bitmap, boolean z) {
            this.mBitmap = bitmap;
            this.mLast = z;
        }
    }

    /* loaded from: classes2.dex */
    public static class BitmapDecoder {
        public int mBufferIndex;
        public BurstPhotoComposer mBurstPhotoComposer;
        public int mDecodeHeight;
        public int mDecodeIndex;
        public int mDecodeWidth;
        public ExecutorService mExecutor;
        public int mFrameCount;
        public int mInSampleSize;
        public List<String> mPathList;
        public boolean mStop;
        public int mThreadCount;
        public final Object mDecodeLock = new Object();
        public final Object mBufferLock = new Object();

        public static /* synthetic */ int access$1408(BitmapDecoder bitmapDecoder) {
            int i = bitmapDecoder.mDecodeIndex;
            bitmapDecoder.mDecodeIndex = i + 1;
            return i;
        }

        public BitmapDecoder(BurstPhotoComposer burstPhotoComposer, List<String> list, int i, int i2) {
            this.mBurstPhotoComposer = burstPhotoComposer;
            this.mPathList = list;
            this.mFrameCount = list.size();
            this.mDecodeHeight = i2;
            this.mDecodeWidth = i;
            int max = Math.max(4, Runtime.getRuntime().availableProcessors());
            this.mThreadCount = max;
            this.mExecutor = Executors.newFixedThreadPool(max);
        }

        public void stop() {
            this.mStop = true;
        }

        public final void start() {
            for (int i = 0; i < this.mThreadCount; i++) {
                this.mExecutor.submit(new DecodeRunnable());
            }
        }

        /* loaded from: classes2.dex */
        public class DecodeRunnable implements Runnable {
            public DecodeRunnable() {
            }

            @Override // java.lang.Runnable
            public void run() {
                int i;
                while (true) {
                    if (BitmapDecoder.this.mStop) {
                        BitmapDecoder.this.endQueue();
                        break;
                    }
                    synchronized (BitmapDecoder.this.mDecodeLock) {
                        if (BitmapDecoder.this.mDecodeIndex >= BitmapDecoder.this.mFrameCount) {
                            break;
                        }
                        i = BitmapDecoder.this.mDecodeIndex;
                        BitmapDecoder.access$1408(BitmapDecoder.this);
                    }
                    BitmapDecoder.this.putToQueue(i, BitmapDecoder.this.decodeImage(i));
                }
                DefaultLogger.d("BurstPhoto_Composer", "return from thread %s", Thread.currentThread().getName());
            }
        }

        public final void endQueue() {
            try {
                this.mBurstPhotoComposer.mQueue.put(new FrameHolder(null, true));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public final void putToQueue(int i, Bitmap bitmap) {
            if (this.mBufferIndex >= this.mFrameCount) {
                return;
            }
            while (!this.mStop) {
                synchronized (this.mBufferLock) {
                    DefaultLogger.d("BurstPhoto_Composer", "wait index %d,receive index %d", Integer.valueOf(this.mBufferIndex), Integer.valueOf(i));
                    if (this.mBufferIndex == i) {
                        try {
                            this.mBurstPhotoComposer.mQueue.put(new FrameHolder(bitmap, i == this.mFrameCount - 1));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.mBufferIndex++;
                        return;
                    }
                }
            }
        }

        public Size getDecodeSize() {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(this.mPathList.get(0), options);
            int calculateInSampleSize = ImageUtil.calculateInSampleSize(options, this.mDecodeWidth, this.mDecodeHeight);
            this.mInSampleSize = calculateInSampleSize;
            return new Size(options.outWidth / calculateInSampleSize, options.outHeight / calculateInSampleSize);
        }

        public final Bitmap decodeImage(int i) {
            String str = this.mPathList.get(i);
            if (this.mInSampleSize == 0) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(str, options);
                this.mInSampleSize = ImageUtil.calculateInSampleSize(options, this.mDecodeWidth, this.mDecodeHeight);
            }
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = this.mInSampleSize;
            return BitmapFactory.decodeFile(str, options2);
        }

        public void release() {
            ExecutorService executorService = this.mExecutor;
            if (executorService != null) {
                executorService.shutdown();
                this.mExecutor = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Encoder {
        public MediaCodec.BufferInfo mBufferInfo;
        public BurstPhotoComposer mBurstPhotoComposer;
        public Callback mCallback;
        public int mDecodeHeight;
        public int mDecodeWidth;
        public EglCore mEglCore;
        public MediaCodec mEnCoder;
        public GLTextureShader mGLTextureShader;
        public MediaMuxer mMediaMuxer;
        public boolean mStop;
        public int mTrackIndex;
        public WindowSurface mWindowSurface;

        /* loaded from: classes2.dex */
        public interface Callback {
            void onEncode(int i);
        }

        public final long getTimeNS(int i) {
            return i * 33333333;
        }

        public Encoder(BurstPhotoComposer burstPhotoComposer, int i, int i2, String str, int i3) {
            this.mBufferInfo = new MediaCodec.BufferInfo();
            this.mBurstPhotoComposer = burstPhotoComposer;
            this.mDecodeWidth = i;
            this.mDecodeHeight = i2;
            try {
                MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", i, i2);
                createVideoFormat.setInteger("color-format", 2130708361);
                createVideoFormat.setInteger("frame-rate", 30);
                createVideoFormat.setInteger("bitrate", calBitrate(this.mDecodeWidth, this.mDecodeHeight));
                createVideoFormat.setInteger("i-frame-interval", 1);
                MediaCodec createEncoderByType = MediaCodec.createEncoderByType("video/avc");
                this.mEnCoder = createEncoderByType;
                createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                MediaMuxer mediaMuxer = new MediaMuxer(str, 0);
                this.mMediaMuxer = mediaMuxer;
                mediaMuxer.setOrientationHint(i3);
            } catch (IOException e) {
                DefaultLogger.e("BurstPhoto_Composer", "init encoder error %s", e);
            }
        }

        public final int calBitrate(int i, int i2) {
            float f;
            float f2;
            int min = Math.min(i, i2);
            if (min <= 720) {
                f = (i * i2) / 921600.0f;
                f2 = 1.2E7f;
            } else if (min <= 1080) {
                f = (i * i2) / 2073600.0f;
                f2 = 1.7E7f;
            } else {
                f = (i * i2) / 8294400.0f;
                f2 = 5.0E7f;
            }
            return Math.min(50000000, (int) (f * f2));
        }

        public final void stop() {
            this.mStop = true;
        }

        public final boolean isSupport(int i, int i2) {
            MediaCodec mediaCodec = this.mEnCoder;
            if (mediaCodec == null) {
                DefaultLogger.d("BurstPhoto_Composer", "encoder is null");
                return false;
            }
            MediaCodecInfo.VideoCapabilities videoCapabilities = mediaCodec.getCodecInfo().getCapabilitiesForType("video/avc").getVideoCapabilities();
            if (videoCapabilities == null) {
                return false;
            }
            return videoCapabilities.isSizeSupported(i, i2);
        }

        public void setCallback(Callback callback) {
            this.mCallback = callback;
        }

        public boolean encodeFrames() {
            boolean z;
            Surface createInputSurface = this.mEnCoder.createInputSurface();
            this.mEglCore = new EglCore(null, 3);
            WindowSurface windowSurface = new WindowSurface(this.mEglCore, createInputSurface, true);
            this.mWindowSurface = windowSurface;
            windowSurface.makeCurrent();
            this.mGLTextureShader = new GLTextureShader();
            System.currentTimeMillis();
            this.mEnCoder.start();
            GLES20.glViewport(0, 0, this.mDecodeWidth, this.mDecodeHeight);
            GLES20.glDisable(2929);
            GLES20.glClear(16640);
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            int i = -1;
            int i2 = 0;
            while (!this.mStop) {
                FrameHolder bufferFromQueue = getBufferFromQueue();
                if (bufferFromQueue != null && bufferFromQueue.mBitmap != null) {
                    GLES20.glClear(16640);
                    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                    i = OpenGlUtils.loadTexture(bufferFromQueue.mBitmap, i);
                    this.mGLTextureShader.draw(i);
                    this.mWindowSurface.setPresentationTime(getTimeNS(i2));
                    this.mWindowSurface.swapBuffers();
                    drainEncoder(bufferFromQueue.mLast);
                    i2++;
                    this.mCallback.onEncode(i2);
                    if (bufferFromQueue.mLast) {
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            z = true;
            GLES20.glDeleteTextures(1, new int[]{i}, 0);
            return z;
        }

        public final void drainEncoder(boolean z) {
            MediaCodec mediaCodec = this.mEnCoder;
            if (mediaCodec == null) {
                return;
            }
            if (z) {
                try {
                    mediaCodec.signalEndOfInputStream();
                    DefaultLogger.d("BurstPhoto_Composer", "signalEndOfInputStream");
                } catch (Exception e) {
                    DefaultLogger.e("BurstPhoto_Composer", "signalEndOfInputStream error %s", e.getMessage());
                }
            }
            while (true) {
                int dequeueOutputBuffer = this.mEnCoder.dequeueOutputBuffer(this.mBufferInfo, AbstractComponentTracker.LINGERING_TIMEOUT);
                if (dequeueOutputBuffer > 0) {
                    ByteBuffer outputBuffer = this.mEnCoder.getOutputBuffer(dequeueOutputBuffer);
                    if (outputBuffer == null) {
                        DefaultLogger.d("BurstPhoto_Composer", "getOutputBuffer return null");
                        return;
                    }
                    MediaCodec.BufferInfo bufferInfo = this.mBufferInfo;
                    if ((bufferInfo.flags & 2) != 0) {
                        bufferInfo.size = 0;
                        DefaultLogger.d("BurstPhoto_Composer", "ignoring BUFFER_FLAG_CODEC_CONFIG");
                    }
                    MediaCodec.BufferInfo bufferInfo2 = this.mBufferInfo;
                    if (bufferInfo2.size != 0) {
                        outputBuffer.position(bufferInfo2.offset);
                        MediaCodec.BufferInfo bufferInfo3 = this.mBufferInfo;
                        outputBuffer.limit(bufferInfo3.offset + bufferInfo3.size);
                        try {
                            this.mMediaMuxer.writeSampleData(this.mTrackIndex, outputBuffer, this.mBufferInfo);
                        } catch (Exception e2) {
                            DefaultLogger.d("BurstPhoto_Composer", e2.getMessage());
                        }
                    }
                    this.mEnCoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        if (!z) {
                            DefaultLogger.d("BurstPhoto_Composer", "reached end of stream unexpectedly");
                            return;
                        } else {
                            DefaultLogger.d("BurstPhoto_Composer", "end of stream reached");
                            return;
                        }
                    }
                } else if (dequeueOutputBuffer == -1) {
                    if (!z) {
                        return;
                    }
                    DefaultLogger.d("BurstPhoto_Composer", "no output available, spinning to await EOS");
                } else if (dequeueOutputBuffer == -2) {
                    MediaFormat outputFormat = this.mEnCoder.getOutputFormat();
                    DefaultLogger.d("BurstPhoto_Composer", "encoder output format changed: " + outputFormat);
                    this.mTrackIndex = this.mMediaMuxer.addTrack(outputFormat);
                    this.mMediaMuxer.start();
                } else {
                    DefaultLogger.d("BurstPhoto_Composer", "unexpected result from encoder.dequeueOutputBuffer: ", Integer.valueOf(dequeueOutputBuffer));
                }
            }
        }

        public final FrameHolder getBufferFromQueue() {
            try {
                return (FrameHolder) this.mBurstPhotoComposer.mQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void release() {
            safeStop(this.mEnCoder);
            safeRelease(this.mEnCoder);
            this.mEnCoder = null;
            DefaultLogger.d("BurstPhoto_Composer", "RELEASE CODEC");
            safeStop(this.mMediaMuxer);
            safeRelease(this.mMediaMuxer);
            this.mMediaMuxer = null;
            DefaultLogger.d("BurstPhoto_Composer", "RELEASE MUXER");
            WindowSurface windowSurface = this.mWindowSurface;
            if (windowSurface != null) {
                windowSurface.release();
                this.mWindowSurface = null;
            }
            EglCore eglCore = this.mEglCore;
            if (eglCore != null) {
                eglCore.release();
                this.mEglCore = null;
            }
            GLTextureShader gLTextureShader = this.mGLTextureShader;
            if (gLTextureShader != null) {
                gLTextureShader.destroy();
                this.mGLTextureShader = null;
            }
        }

        public static void safeStop(MediaCodec mediaCodec) {
            if (mediaCodec != null) {
                try {
                    mediaCodec.stop();
                } catch (IllegalStateException unused) {
                    DefaultLogger.d("BurstPhoto_Composer", "");
                }
            }
        }

        public static void safeRelease(MediaCodec mediaCodec) {
            if (mediaCodec != null) {
                try {
                    mediaCodec.release();
                } catch (IllegalStateException unused) {
                    DefaultLogger.d("BurstPhoto_Composer", "");
                }
            }
        }

        public static void safeStop(MediaMuxer mediaMuxer) {
            if (mediaMuxer != null) {
                try {
                    mediaMuxer.stop();
                    DefaultLogger.d("BurstPhoto_Composer", "RELEASE MUXER");
                } catch (IllegalStateException unused) {
                    DefaultLogger.d("BurstPhoto_Composer", "");
                }
            }
        }

        public static void safeRelease(MediaMuxer mediaMuxer) {
            if (mediaMuxer != null) {
                try {
                    mediaMuxer.release();
                } catch (IllegalStateException unused) {
                    DefaultLogger.d("BurstPhoto_Composer", "");
                }
            }
        }
    }
}
