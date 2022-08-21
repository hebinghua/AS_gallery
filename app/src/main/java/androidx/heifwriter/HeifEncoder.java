package androidx.heifwriter;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class HeifEncoder implements AutoCloseable, SurfaceTexture.OnFrameAvailableListener {
    public final Callback mCallback;
    public ByteBuffer mCurrentBuffer;
    public final Rect mDstRect;
    public SurfaceEOSTracker mEOSTracker;
    public MediaCodec mEncoder;
    public EglWindowSurface mEncoderEglSurface;
    public Surface mEncoderSurface;
    public final int mGridCols;
    public final int mGridHeight;
    public final int mGridRows;
    public final int mGridWidth;
    public final Handler mHandler;
    public final HandlerThread mHandlerThread;
    public final int mHeight;
    public boolean mInputEOS;
    public int mInputIndex;
    public final int mInputMode;
    public Surface mInputSurface;
    public SurfaceTexture mInputTexture;
    public final int mNumTiles;
    public EglRectBlt mRectBlt;
    public final Rect mSrcRect;
    public int mTextureId;
    public final boolean mUseGrid;
    public final int mWidth;
    public final ArrayList<ByteBuffer> mEmptyBuffers = new ArrayList<>();
    public final ArrayList<ByteBuffer> mFilledBuffers = new ArrayList<>();
    public final ArrayList<Integer> mCodecInputBuffers = new ArrayList<>();
    public final float[] mTmpMatrix = new float[16];

    /* loaded from: classes.dex */
    public static abstract class Callback {
        public abstract void onComplete(HeifEncoder heifEncoder);

        public abstract void onDrainOutputBuffer(HeifEncoder heifEncoder, ByteBuffer byteBuffer);

        public abstract void onError(HeifEncoder heifEncoder, MediaCodec.CodecException codecException);

        public abstract void onOutputFormatChanged(HeifEncoder heifEncoder, MediaFormat mediaFormat);
    }

    /* JADX WARN: Removed duplicated region for block: B:70:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0239  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public HeifEncoder(int r20, int r21, boolean r22, int r23, int r24, android.os.Handler r25, androidx.heifwriter.HeifEncoder.Callback r26) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 626
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.heifwriter.HeifEncoder.<init>(int, int, boolean, int, int, android.os.Handler, androidx.heifwriter.HeifEncoder$Callback):void");
    }

    public final void copyTilesGL() {
        GLES20.glViewport(0, 0, this.mGridWidth, this.mGridHeight);
        for (int i = 0; i < this.mGridRows; i++) {
            for (int i2 = 0; i2 < this.mGridCols; i2++) {
                int i3 = this.mGridWidth;
                int i4 = i2 * i3;
                int i5 = this.mGridHeight;
                int i6 = i * i5;
                this.mSrcRect.set(i4, i6, i3 + i4, i5 + i6);
                this.mRectBlt.copyRect(this.mTextureId, Texture2dProgram.V_FLIP_MATRIX, this.mSrcRect);
                EglWindowSurface eglWindowSurface = this.mEncoderEglSurface;
                int i7 = this.mInputIndex;
                this.mInputIndex = i7 + 1;
                eglWindowSurface.setPresentationTime(computePresentationTime(i7) * 1000);
                this.mEncoderEglSurface.swapBuffers();
            }
        }
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this) {
            EglWindowSurface eglWindowSurface = this.mEncoderEglSurface;
            if (eglWindowSurface == null) {
                return;
            }
            eglWindowSurface.makeCurrent();
            surfaceTexture.updateTexImage();
            surfaceTexture.getTransformMatrix(this.mTmpMatrix);
            if (this.mEOSTracker.updateLastInputAndEncoderTime(surfaceTexture.getTimestamp(), computePresentationTime((this.mInputIndex + this.mNumTiles) - 1))) {
                copyTilesGL();
            }
            surfaceTexture.releaseTexImage();
            this.mEncoderEglSurface.makeUnCurrent();
        }
    }

    public void start() {
        this.mEncoder.start();
    }

    public void addBitmap(Bitmap bitmap) {
        if (this.mInputMode != 2) {
            throw new IllegalStateException("addBitmap is only allowed in bitmap input mode");
        }
        if (!this.mEOSTracker.updateLastInputAndEncoderTime(computePresentationTime(this.mInputIndex) * 1000, computePresentationTime((this.mInputIndex + this.mNumTiles) - 1))) {
            return;
        }
        synchronized (this) {
            EglWindowSurface eglWindowSurface = this.mEncoderEglSurface;
            if (eglWindowSurface == null) {
                return;
            }
            eglWindowSurface.makeCurrent();
            this.mRectBlt.loadTexture(this.mTextureId, bitmap);
            copyTilesGL();
            this.mEncoderEglSurface.makeUnCurrent();
        }
    }

    public void stopAsync() {
        int i = this.mInputMode;
        if (i == 2) {
            this.mEOSTracker.updateInputEOSTime(0L);
        } else if (i != 0) {
        } else {
            addYuvBufferInternal(null);
        }
    }

    public final long computePresentationTime(int i) {
        return ((i * 1000000) / this.mNumTiles) + 132;
    }

    public final void addYuvBufferInternal(byte[] bArr) {
        ByteBuffer acquireEmptyBuffer = acquireEmptyBuffer();
        if (acquireEmptyBuffer == null) {
            return;
        }
        acquireEmptyBuffer.clear();
        if (bArr != null) {
            acquireEmptyBuffer.put(bArr);
        }
        acquireEmptyBuffer.flip();
        synchronized (this.mFilledBuffers) {
            this.mFilledBuffers.add(acquireEmptyBuffer);
        }
        this.mHandler.post(new Runnable() { // from class: androidx.heifwriter.HeifEncoder.1
            @Override // java.lang.Runnable
            public void run() {
                HeifEncoder.this.maybeCopyOneTileYUV();
            }
        });
    }

    public void maybeCopyOneTileYUV() {
        while (true) {
            ByteBuffer currentBuffer = getCurrentBuffer();
            if (currentBuffer == null || this.mCodecInputBuffers.isEmpty()) {
                return;
            }
            int i = 0;
            int intValue = this.mCodecInputBuffers.remove(0).intValue();
            boolean z = this.mInputIndex % this.mNumTiles == 0 && currentBuffer.remaining() == 0;
            if (!z) {
                Image inputImage = this.mEncoder.getInputImage(intValue);
                int i2 = this.mGridWidth;
                int i3 = this.mInputIndex;
                int i4 = this.mGridCols;
                int i5 = (i3 % i4) * i2;
                int i6 = this.mGridHeight;
                int i7 = ((i3 / i4) % this.mGridRows) * i6;
                this.mSrcRect.set(i5, i7, i2 + i5, i6 + i7);
                copyOneTileYUV(currentBuffer, inputImage, this.mWidth, this.mHeight, this.mSrcRect, this.mDstRect);
            }
            MediaCodec mediaCodec = this.mEncoder;
            int capacity = z ? 0 : mediaCodec.getInputBuffer(intValue).capacity();
            int i8 = this.mInputIndex;
            this.mInputIndex = i8 + 1;
            long computePresentationTime = computePresentationTime(i8);
            if (z) {
                i = 4;
            }
            mediaCodec.queueInputBuffer(intValue, 0, capacity, computePresentationTime, i);
            if (z || this.mInputIndex % this.mNumTiles == 0) {
                returnEmptyBufferAndNotify(z);
            }
        }
    }

    public static void copyOneTileYUV(ByteBuffer byteBuffer, Image image, int i, int i2, Rect rect, Rect rect2) {
        int i3;
        int i4;
        if (rect.width() != rect2.width() || rect.height() != rect2.height()) {
            throw new IllegalArgumentException("src and dst rect size are different!");
        }
        if (i % 2 == 0 && i2 % 2 == 0) {
            int i5 = 2;
            if (rect.left % 2 == 0 && rect.top % 2 == 0 && rect.right % 2 == 0 && rect.bottom % 2 == 0 && rect2.left % 2 == 0 && rect2.top % 2 == 0 && rect2.right % 2 == 0 && rect2.bottom % 2 == 0) {
                Image.Plane[] planes = image.getPlanes();
                int i6 = 0;
                while (i6 < planes.length) {
                    ByteBuffer buffer = planes[i6].getBuffer();
                    int pixelStride = planes[i6].getPixelStride();
                    int min = Math.min(rect.width(), i - rect.left);
                    int min2 = Math.min(rect.height(), i2 - rect.top);
                    if (i6 > 0) {
                        i4 = ((i * i2) * (i6 + 3)) / 4;
                        i3 = i5;
                    } else {
                        i3 = 1;
                        i4 = 0;
                    }
                    for (int i7 = 0; i7 < min2 / i3; i7++) {
                        byteBuffer.position(((((rect.top / i3) + i7) * i) / i3) + i4 + (rect.left / i3));
                        buffer.position((((rect2.top / i3) + i7) * planes[i6].getRowStride()) + ((rect2.left * pixelStride) / i3));
                        int i8 = 0;
                        while (true) {
                            int i9 = min / i3;
                            if (i8 < i9) {
                                buffer.put(byteBuffer.get());
                                if (pixelStride > 1 && i8 != i9 - 1) {
                                    buffer.position((buffer.position() + pixelStride) - 1);
                                }
                                i8++;
                            }
                        }
                    }
                    i6++;
                    i5 = 2;
                }
                return;
            }
        }
        throw new IllegalArgumentException("src or dst are not aligned!");
    }

    public final ByteBuffer acquireEmptyBuffer() {
        ByteBuffer remove;
        synchronized (this.mEmptyBuffers) {
            while (!this.mInputEOS && this.mEmptyBuffers.isEmpty()) {
                try {
                    this.mEmptyBuffers.wait();
                } catch (InterruptedException unused) {
                }
            }
            remove = this.mInputEOS ? null : this.mEmptyBuffers.remove(0);
        }
        return remove;
    }

    public final ByteBuffer getCurrentBuffer() {
        if (!this.mInputEOS && this.mCurrentBuffer == null) {
            synchronized (this.mFilledBuffers) {
                this.mCurrentBuffer = this.mFilledBuffers.isEmpty() ? null : this.mFilledBuffers.remove(0);
            }
        }
        if (this.mInputEOS) {
            return null;
        }
        return this.mCurrentBuffer;
    }

    public final void returnEmptyBufferAndNotify(boolean z) {
        synchronized (this.mEmptyBuffers) {
            this.mInputEOS = z | this.mInputEOS;
            this.mEmptyBuffers.add(this.mCurrentBuffer);
            this.mEmptyBuffers.notifyAll();
        }
        this.mCurrentBuffer = null;
    }

    public void stopInternal() {
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mEncoder.release();
            this.mEncoder = null;
        }
        synchronized (this.mEmptyBuffers) {
            this.mInputEOS = true;
            this.mEmptyBuffers.notifyAll();
        }
        synchronized (this) {
            EglRectBlt eglRectBlt = this.mRectBlt;
            if (eglRectBlt != null) {
                eglRectBlt.release(false);
                this.mRectBlt = null;
            }
            EglWindowSurface eglWindowSurface = this.mEncoderEglSurface;
            if (eglWindowSurface != null) {
                eglWindowSurface.release();
                this.mEncoderEglSurface = null;
            }
            SurfaceTexture surfaceTexture = this.mInputTexture;
            if (surfaceTexture != null) {
                surfaceTexture.release();
                this.mInputTexture = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public class SurfaceEOSTracker {
        public final boolean mCopyTiles;
        public boolean mSignaled;
        public long mInputEOSTimeNs = -1;
        public long mLastInputTimeNs = -1;
        public long mEncoderEOSTimeUs = -1;
        public long mLastEncoderTimeUs = -1;
        public long mLastOutputTimeUs = -1;

        public SurfaceEOSTracker(boolean z) {
            this.mCopyTiles = z;
        }

        public synchronized void updateInputEOSTime(long j) {
            if (this.mCopyTiles) {
                if (this.mInputEOSTimeNs < 0) {
                    this.mInputEOSTimeNs = j;
                }
            } else if (this.mEncoderEOSTimeUs < 0) {
                this.mEncoderEOSTimeUs = j / 1000;
            }
            updateEOSLocked();
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0013 A[Catch: all -> 0x001c, TryCatch #0 {, blocks: (B:3:0x0001, B:11:0x0013, B:12:0x0015), top: B:18:0x0001 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public synchronized boolean updateLastInputAndEncoderTime(long r5, long r7) {
            /*
                r4 = this;
                monitor-enter(r4)
                long r0 = r4.mInputEOSTimeNs     // Catch: java.lang.Throwable -> L1c
                r2 = 0
                int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r2 < 0) goto L10
                int r0 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
                if (r0 > 0) goto Le
                goto L10
            Le:
                r0 = 0
                goto L11
            L10:
                r0 = 1
            L11:
                if (r0 == 0) goto L15
                r4.mLastEncoderTimeUs = r7     // Catch: java.lang.Throwable -> L1c
            L15:
                r4.mLastInputTimeNs = r5     // Catch: java.lang.Throwable -> L1c
                r4.updateEOSLocked()     // Catch: java.lang.Throwable -> L1c
                monitor-exit(r4)
                return r0
            L1c:
                r5 = move-exception
                monitor-exit(r4)
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.heifwriter.HeifEncoder.SurfaceEOSTracker.updateLastInputAndEncoderTime(long, long):boolean");
        }

        public synchronized void updateLastOutputTime(long j) {
            this.mLastOutputTimeUs = j;
            updateEOSLocked();
        }

        public final void updateEOSLocked() {
            if (this.mSignaled) {
                return;
            }
            if (this.mEncoderEOSTimeUs < 0) {
                long j = this.mInputEOSTimeNs;
                if (j >= 0 && this.mLastInputTimeNs >= j) {
                    long j2 = this.mLastEncoderTimeUs;
                    if (j2 < 0) {
                        doSignalEOSLocked();
                        return;
                    }
                    this.mEncoderEOSTimeUs = j2;
                }
            }
            long j3 = this.mEncoderEOSTimeUs;
            if (j3 < 0 || j3 > this.mLastOutputTimeUs) {
                return;
            }
            doSignalEOSLocked();
        }

        public final void doSignalEOSLocked() {
            HeifEncoder.this.mHandler.post(new Runnable() { // from class: androidx.heifwriter.HeifEncoder.SurfaceEOSTracker.1
                @Override // java.lang.Runnable
                public void run() {
                    MediaCodec mediaCodec = HeifEncoder.this.mEncoder;
                    if (mediaCodec != null) {
                        mediaCodec.signalEndOfInputStream();
                    }
                }
            });
            this.mSignaled = true;
        }
    }

    /* loaded from: classes.dex */
    public class EncoderCallback extends MediaCodec.Callback {
        public boolean mOutputEOS;

        public EncoderCallback() {
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
            if (mediaCodec != HeifEncoder.this.mEncoder) {
                return;
            }
            if (!"image/vnd.android.heic".equals(mediaFormat.getString("mime"))) {
                mediaFormat.setString("mime", "image/vnd.android.heic");
                mediaFormat.setInteger(nexExportFormat.TAG_FORMAT_WIDTH, HeifEncoder.this.mWidth);
                mediaFormat.setInteger(nexExportFormat.TAG_FORMAT_HEIGHT, HeifEncoder.this.mHeight);
                HeifEncoder heifEncoder = HeifEncoder.this;
                if (heifEncoder.mUseGrid) {
                    mediaFormat.setInteger("tile-width", heifEncoder.mGridWidth);
                    mediaFormat.setInteger("tile-height", HeifEncoder.this.mGridHeight);
                    mediaFormat.setInteger("grid-rows", HeifEncoder.this.mGridRows);
                    mediaFormat.setInteger("grid-cols", HeifEncoder.this.mGridCols);
                }
            }
            HeifEncoder heifEncoder2 = HeifEncoder.this;
            heifEncoder2.mCallback.onOutputFormatChanged(heifEncoder2, mediaFormat);
        }

        @Override // android.media.MediaCodec.Callback
        public void onInputBufferAvailable(MediaCodec mediaCodec, int i) {
            HeifEncoder heifEncoder = HeifEncoder.this;
            if (mediaCodec != heifEncoder.mEncoder || heifEncoder.mInputEOS) {
                return;
            }
            heifEncoder.mCodecInputBuffers.add(Integer.valueOf(i));
            HeifEncoder.this.maybeCopyOneTileYUV();
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputBufferAvailable(MediaCodec mediaCodec, int i, MediaCodec.BufferInfo bufferInfo) {
            if (mediaCodec != HeifEncoder.this.mEncoder || this.mOutputEOS) {
                return;
            }
            if (bufferInfo.size > 0 && (bufferInfo.flags & 2) == 0) {
                ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(i);
                outputBuffer.position(bufferInfo.offset);
                outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                SurfaceEOSTracker surfaceEOSTracker = HeifEncoder.this.mEOSTracker;
                if (surfaceEOSTracker != null) {
                    surfaceEOSTracker.updateLastOutputTime(bufferInfo.presentationTimeUs);
                }
                HeifEncoder heifEncoder = HeifEncoder.this;
                heifEncoder.mCallback.onDrainOutputBuffer(heifEncoder, outputBuffer);
            }
            this.mOutputEOS = ((bufferInfo.flags & 4) != 0) | this.mOutputEOS;
            mediaCodec.releaseOutputBuffer(i, false);
            if (!this.mOutputEOS) {
                return;
            }
            stopAndNotify(null);
        }

        @Override // android.media.MediaCodec.Callback
        public void onError(MediaCodec mediaCodec, MediaCodec.CodecException codecException) {
            if (mediaCodec != HeifEncoder.this.mEncoder) {
                return;
            }
            Log.e("HeifEncoder", "onError: " + codecException);
            stopAndNotify(codecException);
        }

        public final void stopAndNotify(MediaCodec.CodecException codecException) {
            HeifEncoder.this.stopInternal();
            if (codecException == null) {
                HeifEncoder heifEncoder = HeifEncoder.this;
                heifEncoder.mCallback.onComplete(heifEncoder);
                return;
            }
            HeifEncoder heifEncoder2 = HeifEncoder.this;
            heifEncoder2.mCallback.onError(heifEncoder2, codecException);
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this.mEmptyBuffers) {
            this.mInputEOS = true;
            this.mEmptyBuffers.notifyAll();
        }
        this.mHandler.postAtFrontOfQueue(new Runnable() { // from class: androidx.heifwriter.HeifEncoder.2
            @Override // java.lang.Runnable
            public void run() {
                HeifEncoder.this.stopInternal();
            }
        });
    }
}
