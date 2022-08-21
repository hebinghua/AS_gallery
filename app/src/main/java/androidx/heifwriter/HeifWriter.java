package androidx.heifwriter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import androidx.heifwriter.HeifEncoder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public final class HeifWriter implements AutoCloseable {
    public final Handler mHandler;
    public final HandlerThread mHandlerThread;
    public HeifEncoder mHeifEncoder;
    public final int mInputMode;
    public final int mMaxImages;
    public MediaMuxer mMuxer;
    public int mNumTiles;
    public int mOutputIndex;
    public final int mPrimaryIndex;
    public final int mRotation;
    public boolean mStarted;
    public int[] mTrackIndexArray;
    public final ResultWaiter mResultWaiter = new ResultWaiter();
    public final AtomicBoolean mMuxerStarted = new AtomicBoolean(false);
    public final List<Pair<Integer, ByteBuffer>> mExifList = new ArrayList();

    /* loaded from: classes.dex */
    public static final class Builder {
        public final FileDescriptor mFd;
        public boolean mGridEnabled;
        public Handler mHandler;
        public final int mHeight;
        public final int mInputMode;
        public int mMaxImages;
        public final String mPath;
        public int mPrimaryIndex;
        public int mQuality;
        public int mRotation;
        public final int mWidth;

        public Builder(String str, int i, int i2, int i3) {
            this(str, null, i, i2, i3);
        }

        public Builder(String str, FileDescriptor fileDescriptor, int i, int i2, int i3) {
            this.mGridEnabled = true;
            this.mQuality = 100;
            this.mMaxImages = 1;
            this.mPrimaryIndex = 0;
            this.mRotation = 0;
            if (i <= 0 || i2 <= 0) {
                throw new IllegalArgumentException("Invalid image size: " + i + "x" + i2);
            }
            this.mPath = str;
            this.mFd = fileDescriptor;
            this.mWidth = i;
            this.mHeight = i2;
            this.mInputMode = i3;
        }

        public Builder setRotation(int i) {
            if (i != 0 && i != 90 && i != 180 && i != 270) {
                throw new IllegalArgumentException("Invalid rotation angle: " + i);
            }
            this.mRotation = i;
            return this;
        }

        public Builder setQuality(int i) {
            if (i < 0 || i > 100) {
                throw new IllegalArgumentException("Invalid quality: " + i);
            }
            this.mQuality = i;
            return this;
        }

        public HeifWriter build() throws IOException {
            return new HeifWriter(this.mPath, this.mFd, this.mWidth, this.mHeight, this.mRotation, this.mGridEnabled, this.mQuality, this.mMaxImages, this.mPrimaryIndex, this.mInputMode, this.mHandler);
        }
    }

    @SuppressLint({"WrongConstant"})
    public HeifWriter(String str, FileDescriptor fileDescriptor, int i, int i2, int i3, boolean z, int i4, int i5, int i6, int i7, Handler handler) throws IOException {
        if (i6 >= i5) {
            throw new IllegalArgumentException("Invalid maxImages (" + i5 + ") or primaryIndex (" + i6 + ")");
        }
        MediaFormat.createVideoFormat("image/vnd.android.heic", i, i2);
        this.mNumTiles = 1;
        this.mRotation = i3;
        this.mInputMode = i7;
        this.mMaxImages = i5;
        this.mPrimaryIndex = i6;
        Looper looper = handler != null ? handler.getLooper() : null;
        if (looper == null) {
            HandlerThread handlerThread = new HandlerThread("HeifEncoderThread", -2);
            this.mHandlerThread = handlerThread;
            handlerThread.start();
            looper = handlerThread.getLooper();
        } else {
            this.mHandlerThread = null;
        }
        Handler handler2 = new Handler(looper);
        this.mHandler = handler2;
        this.mMuxer = str != null ? new MediaMuxer(str, 3) : new MediaMuxer(fileDescriptor, 3);
        this.mHeifEncoder = new HeifEncoder(i, i2, z, i4, i7, handler2, new HeifCallback());
    }

    public void start() {
        checkStarted(false);
        this.mStarted = true;
        this.mHeifEncoder.start();
    }

    public void addBitmap(Bitmap bitmap) {
        checkStartedAndMode(2);
        synchronized (this) {
            HeifEncoder heifEncoder = this.mHeifEncoder;
            if (heifEncoder != null) {
                heifEncoder.addBitmap(bitmap);
            }
        }
    }

    @SuppressLint({"WrongConstant"})
    public void processExifData() {
        Pair<Integer, ByteBuffer> remove;
        if (!this.mMuxerStarted.get()) {
            return;
        }
        while (true) {
            synchronized (this.mExifList) {
                if (this.mExifList.isEmpty()) {
                    return;
                }
                remove = this.mExifList.remove(0);
            }
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            bufferInfo.set(((ByteBuffer) remove.second).position(), ((ByteBuffer) remove.second).remaining(), 0L, 16);
            this.mMuxer.writeSampleData(this.mTrackIndexArray[((Integer) remove.first).intValue()], (ByteBuffer) remove.second, bufferInfo);
        }
    }

    public void stop(long j) throws Exception {
        checkStarted(true);
        synchronized (this) {
            HeifEncoder heifEncoder = this.mHeifEncoder;
            if (heifEncoder != null) {
                heifEncoder.stopAsync();
            }
        }
        this.mResultWaiter.waitForResult(j);
        processExifData();
        closeInternal();
    }

    public final void checkStarted(boolean z) {
        if (this.mStarted == z) {
            return;
        }
        throw new IllegalStateException("Already started");
    }

    public final void checkMode(int i) {
        if (this.mInputMode == i) {
            return;
        }
        throw new IllegalStateException("Not valid in input mode " + this.mInputMode);
    }

    public final void checkStartedAndMode(int i) {
        checkStarted(true);
        checkMode(i);
    }

    public void closeInternal() {
        MediaMuxer mediaMuxer = this.mMuxer;
        if (mediaMuxer != null) {
            mediaMuxer.stop();
            this.mMuxer.release();
            this.mMuxer = null;
        }
        HeifEncoder heifEncoder = this.mHeifEncoder;
        if (heifEncoder != null) {
            heifEncoder.close();
            synchronized (this) {
                this.mHeifEncoder = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public class HeifCallback extends HeifEncoder.Callback {
        public boolean mEncoderStopped;

        public HeifCallback() {
        }

        @Override // androidx.heifwriter.HeifEncoder.Callback
        public void onOutputFormatChanged(HeifEncoder heifEncoder, MediaFormat mediaFormat) {
            if (this.mEncoderStopped) {
                return;
            }
            if (HeifWriter.this.mTrackIndexArray != null) {
                stopAndNotify(new IllegalStateException("Output format changed after muxer started"));
                return;
            }
            try {
                HeifWriter.this.mNumTiles = mediaFormat.getInteger("grid-rows") * mediaFormat.getInteger("grid-cols");
            } catch (ClassCastException | NullPointerException unused) {
                HeifWriter.this.mNumTiles = 1;
            }
            HeifWriter heifWriter = HeifWriter.this;
            heifWriter.mTrackIndexArray = new int[heifWriter.mMaxImages];
            if (heifWriter.mRotation > 0) {
                Log.d("HeifWriter", "setting rotation: " + HeifWriter.this.mRotation);
                HeifWriter heifWriter2 = HeifWriter.this;
                heifWriter2.mMuxer.setOrientationHint(heifWriter2.mRotation);
            }
            int i = 0;
            while (true) {
                HeifWriter heifWriter3 = HeifWriter.this;
                if (i < heifWriter3.mTrackIndexArray.length) {
                    mediaFormat.setInteger("is-default", i == heifWriter3.mPrimaryIndex ? 1 : 0);
                    HeifWriter heifWriter4 = HeifWriter.this;
                    heifWriter4.mTrackIndexArray[i] = heifWriter4.mMuxer.addTrack(mediaFormat);
                    i++;
                } else {
                    heifWriter3.mMuxer.start();
                    HeifWriter.this.mMuxerStarted.set(true);
                    HeifWriter.this.processExifData();
                    return;
                }
            }
        }

        @Override // androidx.heifwriter.HeifEncoder.Callback
        public void onDrainOutputBuffer(HeifEncoder heifEncoder, ByteBuffer byteBuffer) {
            if (this.mEncoderStopped) {
                return;
            }
            HeifWriter heifWriter = HeifWriter.this;
            if (heifWriter.mTrackIndexArray == null) {
                stopAndNotify(new IllegalStateException("Output buffer received before format info"));
                return;
            }
            if (heifWriter.mOutputIndex < heifWriter.mMaxImages * heifWriter.mNumTiles) {
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                bufferInfo.set(byteBuffer.position(), byteBuffer.remaining(), 0L, 0);
                HeifWriter heifWriter2 = HeifWriter.this;
                heifWriter2.mMuxer.writeSampleData(heifWriter2.mTrackIndexArray[heifWriter2.mOutputIndex / heifWriter2.mNumTiles], byteBuffer, bufferInfo);
            }
            HeifWriter heifWriter3 = HeifWriter.this;
            int i = heifWriter3.mOutputIndex + 1;
            heifWriter3.mOutputIndex = i;
            if (i != heifWriter3.mMaxImages * heifWriter3.mNumTiles) {
                return;
            }
            stopAndNotify(null);
        }

        @Override // androidx.heifwriter.HeifEncoder.Callback
        public void onComplete(HeifEncoder heifEncoder) {
            stopAndNotify(null);
        }

        @Override // androidx.heifwriter.HeifEncoder.Callback
        public void onError(HeifEncoder heifEncoder, MediaCodec.CodecException codecException) {
            stopAndNotify(codecException);
        }

        public final void stopAndNotify(Exception exc) {
            if (this.mEncoderStopped) {
                return;
            }
            this.mEncoderStopped = true;
            HeifWriter.this.mResultWaiter.signalResult(exc);
        }
    }

    /* loaded from: classes.dex */
    public static class ResultWaiter {
        public boolean mDone;
        public Exception mException;

        public synchronized void waitForResult(long j) throws Exception {
            int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
            if (i < 0) {
                throw new IllegalArgumentException("timeoutMs is negative");
            }
            if (i == 0) {
                while (!this.mDone) {
                    try {
                        wait();
                    } catch (InterruptedException unused) {
                    }
                }
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                while (!this.mDone && j > 0) {
                    try {
                        wait(j);
                    } catch (InterruptedException unused2) {
                    }
                    j -= System.currentTimeMillis() - currentTimeMillis;
                }
            }
            if (!this.mDone) {
                this.mDone = true;
                this.mException = new TimeoutException("timed out waiting for result");
            }
            Exception exc = this.mException;
            if (exc != null) {
                throw exc;
            }
        }

        public synchronized void signalResult(Exception exc) {
            if (!this.mDone) {
                this.mDone = true;
                this.mException = exc;
                notifyAll();
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mHandler.postAtFrontOfQueue(new Runnable() { // from class: androidx.heifwriter.HeifWriter.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HeifWriter.this.closeInternal();
                } catch (Exception unused) {
                }
            }
        });
    }
}
