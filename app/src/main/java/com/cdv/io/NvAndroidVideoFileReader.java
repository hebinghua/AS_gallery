package com.cdv.io;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import com.cdv.utils.NvAndroidInterruptionChecker;
import com.cdv.utils.NvAndroidUtils;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

/* loaded from: classes.dex */
public class NvAndroidVideoFileReader implements SurfaceTexture.OnFrameAvailableListener {
    private static final int ERROR_EOF = 1;
    private static final int ERROR_FAIL = 2;
    private static final int ERROR_INTERRUPTED_DECODING = 3;
    private static final int ERROR_MEDIA_EXTRACTOR_PRELOAD_FAILED = 4;
    private static final int ERROR_OK = 0;
    private static final int SKIP_MODE_ALL_NONREFERENCE = 1;
    private static final int SKIP_MODE_BELOW_TIMESTAMP = 2;
    private static final int SKIP_MODE_NONE = 0;
    private static final String TAG = "NvAndroidVideoFileReader";
    private static Method m_setOnFrameAvailableListener2 = null;
    private static final boolean m_verbose = false;
    private MediaCodec.BufferInfo m_bufferInfo;
    private Handler m_cleanupHandler;
    private Context m_context;
    private Handler m_handler;
    private NvAndroidInterruptionChecker m_interruptionChecker;
    private Semaphore m_surfaceTextureCreationSemaphore;
    private int m_texId;
    private String m_videoFilePath;
    private MediaExtractor m_extractor = null;
    private int m_videoTrackIndex = -1;
    private MediaFormat m_format = null;
    private long m_duration = 0;
    private boolean m_extractorInOriginalState = true;
    private SurfaceTexture m_surfaceTexture = null;
    private Surface m_surface = null;
    private MediaCodec m_decoder = null;
    private boolean m_decoderSetupFailed = false;
    private boolean m_decoderStarted = false;
    public ByteBuffer[] m_decoderInputBuffers = null;
    private Object m_frameSyncObject = new Object();
    private boolean m_frameAvailable = false;
    private long m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
    private long m_timestampOfCurTexFrame = Long.MIN_VALUE;
    private boolean m_curTexImageUpdated = false;
    private boolean m_firstPlaybackTexFrameUnconsumed = false;
    private boolean m_sawOutputEOS = false;
    private long m_preloadedTimestamp = Long.MIN_VALUE;
    private long m_lastSeekTimestamp = Long.MIN_VALUE;
    private long m_lastSeekActualTimestamp = Long.MIN_VALUE;
    private int m_usedTemporalLayer = -1;
    private long m_temporalLayerEndTime = -1;
    private long m_contiuousDecodingThreshold = 1500000;
    private boolean m_inputBufferQueued = false;
    private long m_timestampOfLastInputFrame = Long.MIN_VALUE;
    private boolean m_sawInputEOS = false;
    private int m_pendingInputFrameCount = 0;
    private boolean m_skipNonReferenceFrameWhenPlayback = false;

    static {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                m_setOnFrameAvailableListener2 = SurfaceTexture.class.getDeclaredMethod("setOnFrameAvailableListener", SurfaceTexture.OnFrameAvailableListener.class, Handler.class);
                Log.d(TAG, "New SurfaceTexture.setOnFrameAvailableListener() method is available!");
            } catch (Exception unused) {
                m_setOnFrameAvailableListener2 = null;
            }
        }
    }

    public NvAndroidVideoFileReader(Handler handler, Handler handler2) {
        this.m_handler = null;
        this.m_cleanupHandler = null;
        this.m_bufferInfo = null;
        this.m_handler = handler;
        this.m_cleanupHandler = handler2;
        this.m_bufferInfo = new MediaCodec.BufferInfo();
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x003f, code lost:
        r9.m_videoTrackIndex = r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean OpenFile(java.lang.String r10, int r11, android.content.Context r12, int r13, long r14) {
        /*
            Method dump skipped, instructions count: 376
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReader.OpenFile(java.lang.String, int, android.content.Context, int, long):boolean");
    }

    public void preload(long j) {
        int preloadInternal = preloadInternal(j);
        int i = 0;
        while (preloadInternal == 4) {
            this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
            Log.w(TAG, "Try to recreate MediaExtractor!");
            if (!recreateMediaExtractor()) {
                Log.e(TAG, "Failed to recreate MediaExtractor!");
            }
            preloadInternal = preloadInternal(j);
            i++;
            Log.w(TAG, "Try to preload! times=" + i);
            if (i >= 2) {
                break;
            }
        }
        if (preloadInternal == 4) {
            Log.w(TAG, "Try to recreate MediaExtractor after preload!");
            this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
            if (recreateMediaExtractor()) {
                return;
            }
            Log.e(TAG, "Failed to recreate MediaExtractor!");
        }
    }

    public boolean hasDecoderSetupFailed() {
        return this.m_decoderSetupFailed;
    }

    public void setActualDuration(long j) {
        if (j > this.m_duration) {
            this.m_duration = j;
        }
    }

    public void setInterruptionChecker(NvAndroidInterruptionChecker nvAndroidInterruptionChecker) {
        this.m_interruptionChecker = nvAndroidInterruptionChecker;
    }

    public void skipNonReferenceFrame(boolean z) {
        this.m_skipNonReferenceFrameWhenPlayback = z;
    }

    public void CloseFile() {
        InvalidLastSeekTimestamp();
        CleanupDecoder(true);
        MediaExtractor mediaExtractor = this.m_extractor;
        if (mediaExtractor != null) {
            mediaExtractor.release();
            this.m_extractor = null;
            this.m_videoTrackIndex = -1;
            this.m_format = null;
            this.m_duration = 0L;
            this.m_extractorInOriginalState = true;
        }
        this.m_usedTemporalLayer = -1;
        this.m_temporalLayerEndTime = -1L;
        this.m_videoFilePath = null;
        this.m_context = null;
    }

    public void SetDecodeTemporalLayer(int i, long j) {
        if (i == this.m_usedTemporalLayer) {
            return;
        }
        this.m_temporalLayerEndTime = j;
        this.m_usedTemporalLayer = i;
    }

    public int SeekVideoFrame(long j, long j2) {
        if (!IsValid()) {
            return 1;
        }
        long max = Math.max(j, 0L);
        long j3 = this.m_duration;
        if (max >= j3) {
            if (max >= 40000 + j3) {
                return 1;
            }
            max = j3 - 1;
        }
        long j4 = this.m_timestampOfCurTexFrame;
        if (j4 != Long.MIN_VALUE && Math.abs(max - j4) <= j2) {
            return 0;
        }
        int SeekInternal = SeekInternal(max, j2, false, 2);
        if (SeekInternal == 0) {
            this.m_lastSeekTimestamp = max;
            this.m_lastSeekActualTimestamp = this.m_timestampOfCurTexFrame;
        } else {
            InvalidLastSeekTimestamp();
        }
        return SeekInternal;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x005b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x005c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int StartPlayback(long r12, long r14) {
        /*
            r11 = this;
            boolean r0 = r11.IsValid()
            r1 = 1
            if (r0 != 0) goto L8
            return r1
        L8:
            r2 = 0
            long r12 = java.lang.Math.max(r12, r2)
            long r2 = r11.m_duration
            int r0 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r0 < 0) goto L15
            return r1
        L15:
            long r2 = r11.m_preloadedTimestamp
            r4 = -9223372036854775808
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 == 0) goto L2b
            int r0 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r0 < 0) goto L28
            long r2 = r11.m_timestampOfCurTexFrame
            int r0 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r0 > 0) goto L28
            r12 = r2
        L28:
            r11.m_preloadedTimestamp = r4
            goto L3d
        L2b:
            long r2 = r11.m_lastSeekTimestamp
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 == 0) goto L3d
            int r0 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r0 != 0) goto L3d
            long r2 = r11.m_lastSeekActualTimestamp
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 == 0) goto L3d
            r5 = r2
            goto L3e
        L3d:
            r5 = r12
        L3e:
            long r12 = r11.m_timestampOfCurTexFrame
            int r0 = (r5 > r12 ? 1 : (r5 == r12 ? 0 : -1))
            r2 = 0
            if (r0 != 0) goto L4e
            long r3 = r11.m_timestampOfLastDecodedFrame
            int r12 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1))
            if (r12 != 0) goto L4e
            r11.m_firstPlaybackTexFrameUnconsumed = r1
            return r2
        L4e:
            r9 = 0
            r10 = 2
            r4 = r11
            r7 = r14
            int r12 = r4.SeekInternal(r5, r7, r9, r10)
            r11.InvalidLastSeekTimestamp()
            if (r12 == 0) goto L5c
            return r12
        L5c:
            r11.m_firstPlaybackTexFrameUnconsumed = r1
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReader.StartPlayback(long, long):int");
    }

    public int GetNextVideoFrameForPlayback() {
        if (!IsValid()) {
            return 1;
        }
        if (!this.m_firstPlaybackTexFrameUnconsumed) {
            int DecodeToFrame = DecodeToFrame(Long.MIN_VALUE, 0L, false, this.m_skipNonReferenceFrameWhenPlayback ? 1 : 0);
            InvalidLastSeekTimestamp();
            if (DecodeToFrame != 0) {
                return DecodeToFrame;
            }
        } else {
            this.m_firstPlaybackTexFrameUnconsumed = false;
        }
        return 0;
    }

    public long GetTimestampOfCurrentTextureFrame() {
        return this.m_timestampOfCurTexFrame;
    }

    public void GetTransformMatrixOfSurfaceTexture(float[] fArr) {
        SurfaceTexture surfaceTexture = this.m_surfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.getTransformMatrix(fArr);
        }
    }

    public void updateCurTexImage() {
        try {
            if (this.m_timestampOfCurTexFrame == Long.MIN_VALUE || this.m_curTexImageUpdated) {
                return;
            }
            SurfaceTexture surfaceTexture = this.m_surfaceTexture;
            if (surfaceTexture != null) {
                surfaceTexture.updateTexImage();
            }
            this.m_curTexImageUpdated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this.m_frameSyncObject) {
            if (this.m_frameAvailable) {
                Log.e(TAG, "m_frameAvailable already set, frame could be dropped!");
            }
            this.m_frameAvailable = true;
            this.m_frameSyncObject.notifyAll();
        }
    }

    private boolean IsValid() {
        return this.m_decoder != null;
    }

    private int preloadInternal(long j) {
        if (!IsValid()) {
            return 2;
        }
        long max = Math.max(Math.min(j, this.m_duration - 1), 0L);
        int SeekInternal = SeekInternal(max, 0L, true, 2);
        if (SeekInternal == 0) {
            if (this.m_timestampOfCurTexFrame == Long.MIN_VALUE) {
                return SeekInternal;
            }
            this.m_preloadedTimestamp = max;
            return SeekInternal;
        } else if (SeekInternal != 1 || this.m_timestampOfCurTexFrame != Long.MIN_VALUE) {
            return SeekInternal;
        } else {
            long j2 = this.m_timestampOfLastDecodedFrame;
            if (j2 != Long.MIN_VALUE && j2 < this.m_duration - 100000) {
                return 4;
            }
            return SeekInternal;
        }
    }

    private boolean recreateMediaExtractor() {
        try {
            this.m_extractor.release();
            MediaExtractor createMediaExtractorFromMediaFilePath = NvAndroidUtils.createMediaExtractorFromMediaFilePath(this.m_context, this.m_videoFilePath);
            this.m_extractor = createMediaExtractorFromMediaFilePath;
            if (createMediaExtractorFromMediaFilePath == null) {
                throw new Exception("Failed to re-create media extractor!");
            }
            createMediaExtractorFromMediaFilePath.selectTrack(this.m_videoTrackIndex);
            this.m_extractorInOriginalState = true;
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            this.m_extractor = null;
            this.m_videoTrackIndex = -1;
            this.m_format = null;
            this.m_duration = 0L;
            this.m_extractorInOriginalState = true;
            CloseFile();
            return false;
        }
    }

    private boolean SetupDecoder(String str) {
        try {
            MediaCodec createDecoderByType = MediaCodec.createDecoderByType(str);
            this.m_decoder = createDecoderByType;
            createDecoderByType.configure(this.m_format, this.m_surface, (MediaCrypto) null, 0);
            this.m_decoder.start();
            this.m_decoderStarted = true;
            this.m_decoderInputBuffers = this.m_decoder.getInputBuffers();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CleanupDecoder(false);
            return false;
        }
    }

    private void CleanupDecoder(boolean z) {
        updateCurTexImage();
        if (this.m_decoder != null && this.m_decoderStarted) {
            try {
                if (this.m_sawInputEOS && !this.m_sawOutputEOS) {
                    DrainOutputBuffers(false);
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
        if (!z) {
            CleanupDecoderCore(false);
        } else {
            this.m_cleanupHandler.post(new Runnable() { // from class: com.cdv.io.NvAndroidVideoFileReader.2
                @Override // java.lang.Runnable
                public void run() {
                    NvAndroidVideoFileReader.this.CleanupDecoderCore(true);
                }
            });
        }
        this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
        this.m_timestampOfCurTexFrame = Long.MIN_VALUE;
        this.m_firstPlaybackTexFrameUnconsumed = false;
        this.m_pendingInputFrameCount = 0;
        this.m_timestampOfLastInputFrame = Long.MIN_VALUE;
        this.m_sawInputEOS = false;
        this.m_sawOutputEOS = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void CleanupDecoderCore(boolean z) {
        MediaCodec mediaCodec = this.m_decoder;
        if (mediaCodec != null) {
            if (this.m_decoderStarted) {
                try {
                    if (this.m_inputBufferQueued) {
                        try {
                            mediaCodec.flush();
                        } catch (Exception unused) {
                        }
                        this.m_inputBufferQueued = false;
                    }
                    this.m_decoder.stop();
                } catch (Exception e) {
                    Log.e(TAG, "" + e.getMessage());
                    e.printStackTrace();
                }
                this.m_decoderStarted = false;
                this.m_decoderInputBuffers = null;
            }
            this.m_decoder.release();
            this.m_decoder = null;
        }
        if (z) {
            Surface surface = this.m_surface;
            if (surface != null) {
                surface.release();
                this.m_surface = null;
            }
            SurfaceTexture surfaceTexture = this.m_surfaceTexture;
            if (surfaceTexture == null) {
                return;
            }
            surfaceTexture.release();
            this.m_surfaceTexture = null;
        }
    }

    private int SeekInternal(long j, long j2, boolean z, int i) {
        long j3 = this.m_timestampOfLastDecodedFrame;
        boolean z2 = true;
        if ((j3 == Long.MIN_VALUE || j <= j3 || j >= j3 + this.m_contiuousDecodingThreshold) && (!this.m_extractorInOriginalState || j >= this.m_contiuousDecodingThreshold)) {
            z2 = false;
        }
        if (!z2) {
            try {
                this.m_extractor.seekTo(j, 0);
                if (this.m_extractor.getSampleTime() < 0 && j < this.m_duration - 100000) {
                    Log.w(TAG, "Try to recreate MediaExtractor!");
                    if (!recreateMediaExtractor()) {
                        Log.e(TAG, "Failed to recreate MediaExtractor!");
                        CloseFile();
                        return 2;
                    }
                    this.m_extractor.seekTo(j, 0);
                }
                if (!this.m_sawInputEOS && !this.m_sawOutputEOS) {
                    if (this.m_inputBufferQueued) {
                        try {
                            this.m_decoder.flush();
                        } catch (Exception unused) {
                        }
                        this.m_inputBufferQueued = false;
                        this.m_pendingInputFrameCount = 0;
                    }
                }
                CleanupDecoder(false);
                if (!SetupDecoder(this.m_format.getString("mime"))) {
                    return 2;
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
                return 2;
            }
        }
        return DecodeToFrame(j, j2, z, i);
    }

    private int DecodeToFrame(long j, long j2, boolean z, int i) {
        try {
            return DoDecodeToFrame(j, j2, z, i);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CleanupDecoder(false);
            return 2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c2, code lost:
        r21.m_timestampOfLastInputFrame = r12;
        r21.m_decoder.queueInputBuffer(r15, 0, r17, r12, 0);
        r21.m_inputBufferQueued = true;
        r21.m_pendingInputFrameCount++;
        r21.m_extractor.advance();
        r21.m_extractorInOriginalState = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int DoDecodeToFrame(long r22, long r24, boolean r26, int r27) {
        /*
            Method dump skipped, instructions count: 453
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReader.DoDecodeToFrame(long, long, boolean, int):int");
    }

    private boolean isNonReferenceFrame(ByteBuffer byteBuffer) {
        if (byteBuffer != null && byteBuffer.limit() >= 5) {
            byte[] bArr = new byte[5];
            int position = byteBuffer.position();
            byteBuffer.get(bArr);
            byteBuffer.position(position);
            return bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 1 && (bArr[4] & 31) == 1 && ((bArr[4] >> 5) & 3) == 0;
        }
        return false;
    }

    private boolean canSkipFrame(ByteBuffer byteBuffer, long j) {
        if (byteBuffer != null && this.m_usedTemporalLayer > 0 && j < this.m_temporalLayerEndTime) {
            byte[] bArr = new byte[16];
            int position = byteBuffer.position();
            byteBuffer.get(bArr);
            byteBuffer.position(position);
            int i = bArr[4] & 31;
            if (bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 1 && (i == 14 || i == 20)) {
                if (!(((bArr[5] & 255) >> 7) > 0) || (((bArr[7] & 255) >> 5) & 7) <= this.m_usedTemporalLayer) {
                    return false;
                }
            } else if (this.m_usedTemporalLayer <= 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean AwaitNewImage(boolean z) {
        synchronized (this.m_frameSyncObject) {
            do {
                if (!this.m_frameAvailable) {
                    try {
                        this.m_frameSyncObject.wait(3000L);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "" + e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    this.m_frameAvailable = false;
                    if (!z) {
                        return true;
                    }
                    try {
                        this.m_surfaceTexture.updateTexImage();
                        return true;
                    } catch (Exception e2) {
                        Log.e(TAG, "" + e2.getMessage());
                        e2.printStackTrace();
                        return false;
                    }
                }
            } while (this.m_frameAvailable);
            Log.e(TAG, "Frame wait timed out!");
            return false;
        }
    }

    private void DrainOutputBuffers(boolean z) {
        if (z || (this.m_sawInputEOS && !this.m_sawOutputEOS)) {
            int i = 0;
            while (!this.m_sawOutputEOS) {
                int dequeueOutputBuffer = this.m_decoder.dequeueOutputBuffer(this.m_bufferInfo, 5000L);
                i++;
                if (dequeueOutputBuffer != -1 && dequeueOutputBuffer != -3 && dequeueOutputBuffer != -2) {
                    if (dequeueOutputBuffer < 0) {
                        Log.e(TAG, "DrainDecoderBuffers(): Unexpected result from decoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
                        return;
                    }
                    if ((this.m_bufferInfo.flags & 4) != 0) {
                        this.m_sawOutputEOS = true;
                    }
                    this.m_decoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    i = 0;
                }
                if (i > 100) {
                    Log.e(TAG, "DrainDecoderBuffers(): We have tried too many times and can't decode a frame!");
                    return;
                }
            }
        }
    }

    private void InvalidLastSeekTimestamp() {
        this.m_lastSeekTimestamp = Long.MIN_VALUE;
        this.m_lastSeekActualTimestamp = Long.MIN_VALUE;
    }

    private boolean isInterruptedDecoding() {
        NvAndroidInterruptionChecker nvAndroidInterruptionChecker = this.m_interruptionChecker;
        if (nvAndroidInterruptionChecker == null) {
            return false;
        }
        return nvAndroidInterruptionChecker.isInterrupted();
    }
}
