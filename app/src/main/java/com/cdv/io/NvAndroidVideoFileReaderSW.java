package com.cdv.io;

import android.content.Context;
import android.media.Image;
import android.media.ImageReader;
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
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class NvAndroidVideoFileReaderSW {
    private static final int ERROR_EOF = 1;
    private static final int ERROR_FAIL = 2;
    private static final int ERROR_INTERRUPTED_DECODING = 3;
    private static final int ERROR_MEDIA_EXTRACTOR_PRELOAD_FAILED = 4;
    private static final int ERROR_OK = 0;
    private static final int SKIP_MODE_ALL_NONREFERENCE = 1;
    private static final int SKIP_MODE_BELOW_TIMESTAMP = 2;
    private static final int SKIP_MODE_NONE = 0;
    private static final String TAG = "NvAndroidVideoFileReaderSW";
    private static final boolean m_verbose = false;
    private MediaCodec.BufferInfo m_bufferInfo;
    private Handler m_cleanupHandler;
    private Handler m_handler;
    private NvAndroidInterruptionChecker m_interruptionChecker;
    private long m_owner;
    private MediaExtractor m_extractor = null;
    private int m_videoTrackIndex = -1;
    private MediaFormat m_format = null;
    private long m_duration = 0;
    private boolean m_extractorInOriginalState = true;
    private MediaCodec m_decoder = null;
    private ImageReader m_imageReader = null;
    private boolean m_decoderUseSurface = false;
    private Object m_frameSyncObject = new Object();
    private boolean m_imageReady = false;
    private boolean m_decoderSetupFailed = false;
    private boolean m_decoderStarted = false;
    public ByteBuffer[] m_decoderInputBuffers = null;
    public ByteBuffer[] m_decoderOutputBuffers = null;
    private long m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
    private long m_timestampOfLastCopiedFrame = Long.MIN_VALUE;
    private boolean m_inputBufferQueued = false;
    private int m_pendingInputFrameCount = 0;
    private boolean m_sawInputEOS = false;
    private boolean m_sawOutputEOS = false;
    private long m_preloadedTimestamp = Long.MIN_VALUE;
    private long m_lastSeekTimestamp = Long.MIN_VALUE;
    private long m_lastSeekActualTimestamp = Long.MIN_VALUE;
    private boolean m_onlyDecodeKeyFrame = false;
    private long m_contiuousDecodingThreshold = 1000000;
    private boolean m_skipNonReferenceFrameWhenPlayback = false;

    private native void nativeCopyVideoFrame(long j, ByteBuffer byteBuffer, int i, int i2, long j2);

    private native void nativeCopyVideoFrameFromYUV420ImagePlanes(long j, int i, int i2, int i3, int i4, ByteBuffer byteBuffer, int i5, int i6, ByteBuffer byteBuffer2, int i7, int i8, ByteBuffer byteBuffer3, int i9, int i10, long j2);

    private native void nativeSetFormatInfo(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    public NvAndroidVideoFileReaderSW(long j, Handler handler, Handler handler2) {
        this.m_owner = 0L;
        this.m_handler = null;
        this.m_cleanupHandler = null;
        this.m_bufferInfo = null;
        this.m_owner = j;
        this.m_handler = handler;
        this.m_cleanupHandler = handler2;
        this.m_bufferInfo = new MediaCodec.BufferInfo();
    }

    public boolean OpenFile(String str, Context context, int i, long j) {
        if (IsValid()) {
            Log.e(TAG, "You can't call OpenFile() twice!");
            return false;
        }
        MediaExtractor createMediaExtractorFromMediaFilePath = NvAndroidUtils.createMediaExtractorFromMediaFilePath(context, str);
        this.m_extractor = createMediaExtractorFromMediaFilePath;
        if (createMediaExtractorFromMediaFilePath == null) {
            return false;
        }
        this.m_extractorInOriginalState = true;
        int trackCount = createMediaExtractorFromMediaFilePath.getTrackCount();
        int i2 = 0;
        while (true) {
            if (i2 >= trackCount) {
                break;
            } else if (this.m_extractor.getTrackFormat(i2).getString("mime").startsWith("video/")) {
                this.m_videoTrackIndex = i2;
                break;
            } else {
                i2++;
            }
        }
        int i3 = this.m_videoTrackIndex;
        if (i3 < 0) {
            Log.e(TAG, "Failed to find a video track from " + str);
            CloseFile();
            return false;
        }
        this.m_extractor.selectTrack(i3);
        MediaFormat trackFormat = this.m_extractor.getTrackFormat(this.m_videoTrackIndex);
        this.m_format = trackFormat;
        int i4 = Build.VERSION.SDK_INT;
        if (i4 == 16) {
            trackFormat.setInteger("max-input-size", 0);
        }
        boolean equals = Build.HARDWARE.equals("qcom");
        if (i4 >= 23 && i >= 0 && !equals) {
            MediaFormat mediaFormat = this.m_format;
            if (i <= 0) {
                i = 120;
            }
            mediaFormat.setInteger("operating-rate", i);
        }
        try {
            this.m_duration = this.m_format.getLong("durationUs");
            String string = this.m_format.getString("mime");
            if (equals && this.m_format.containsKey("frame-rate")) {
                this.m_format.setInteger("frame-rate", 0);
            }
            this.m_decoderSetupFailed = false;
            if (!SetupDecoder(string)) {
                this.m_decoderSetupFailed = true;
                CloseFile();
                return false;
            }
            this.m_contiuousDecodingThreshold = j;
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CloseFile();
            return false;
        }
    }

    public void preload(long j) {
        int preloadInternal = preloadInternal(j);
        int i = 0;
        while (preloadInternal == 4) {
            this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
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
        }
    }

    public boolean hasDecoderSetupFailed() {
        return this.m_decoderSetupFailed;
    }

    public void enableOnlyDecodeKeyFrame(boolean z) {
        this.m_onlyDecodeKeyFrame = z;
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
            this.m_onlyDecodeKeyFrame = false;
        }
    }

    public int SeekVideoFrame(long j, long j2) {
        if (!IsValid()) {
            return 1;
        }
        long max = Math.max(j, 0L);
        long j3 = this.m_duration;
        if (max >= j3) {
            if (max >= 25000 + j3) {
                return 1;
            }
            max = j3 - 1;
        }
        long j4 = this.m_timestampOfLastCopiedFrame;
        if (j4 != Long.MIN_VALUE && Math.abs(max - j4) <= j2) {
            return 0;
        }
        int SeekInternal = SeekInternal(max, j2, this.m_onlyDecodeKeyFrame ? 0 : 2);
        if (SeekInternal == 0) {
            this.m_lastSeekTimestamp = max;
            this.m_lastSeekActualTimestamp = this.m_timestampOfLastCopiedFrame;
        } else {
            InvalidLastSeekTimestamp();
        }
        return SeekInternal;
    }

    public int StartPlayback(long j, long j2) {
        long j3;
        long j4;
        if (!IsValid()) {
            return 1;
        }
        long max = Math.max(j, 0L);
        if (max >= this.m_duration) {
            return 1;
        }
        long j5 = this.m_preloadedTimestamp;
        if (j5 != Long.MIN_VALUE) {
            if (max >= j5) {
                long j6 = this.m_timestampOfLastCopiedFrame;
                if (max <= j6) {
                    max = j6;
                }
            }
            this.m_preloadedTimestamp = Long.MIN_VALUE;
        } else {
            long j7 = this.m_lastSeekTimestamp;
            if (j7 != Long.MIN_VALUE && max == j7) {
                long j8 = this.m_lastSeekActualTimestamp;
                if (j8 != Long.MIN_VALUE) {
                    j3 = j8;
                    j4 = this.m_timestampOfLastCopiedFrame;
                    if (j3 != j4 && j4 == this.m_timestampOfLastDecodedFrame) {
                        return 0;
                    }
                    int SeekInternal = SeekInternal(j3, j2, 2);
                    InvalidLastSeekTimestamp();
                    return SeekInternal;
                }
            }
        }
        j3 = max;
        j4 = this.m_timestampOfLastCopiedFrame;
        if (j3 != j4) {
        }
        int SeekInternal2 = SeekInternal(j3, j2, 2);
        InvalidLastSeekTimestamp();
        return SeekInternal2;
    }

    public int GetNextVideoFrameForPlayback() {
        if (!IsValid()) {
            return 1;
        }
        int DecodeToFrame = DecodeToFrame(Long.MIN_VALUE, 0L, this.m_skipNonReferenceFrameWhenPlayback ? 1 : 0);
        InvalidLastSeekTimestamp();
        return DecodeToFrame;
    }

    private boolean IsValid() {
        return this.m_decoder != null;
    }

    private boolean preferDecodeToImageReader(String str) {
        if (!Build.MANUFACTURER.equals("OPPO") || !Build.MODEL.equals("R15")) {
            return false;
        }
        return str.equals("video/mpeg2");
    }

    private int preloadInternal(long j) {
        if (!IsValid()) {
            return 2;
        }
        long max = Math.max(Math.min(j, this.m_duration - 1), 0L);
        int SeekInternal = SeekInternal(max, 0L, 2);
        if (SeekInternal == 0) {
            if (this.m_timestampOfLastCopiedFrame == Long.MIN_VALUE) {
                return SeekInternal;
            }
            this.m_preloadedTimestamp = max;
            return SeekInternal;
        } else if (SeekInternal != 1 || this.m_timestampOfLastCopiedFrame != Long.MIN_VALUE) {
            return SeekInternal;
        } else {
            long j2 = this.m_timestampOfLastDecodedFrame;
            if (j2 != Long.MIN_VALUE && j2 < this.m_duration - 100000) {
                return 4;
            }
            return SeekInternal;
        }
    }

    private boolean SetupDecoder(String str) {
        if (!preferDecodeToImageReader(str) || !setupDecoderWithImageReader(str)) {
            return setupDecoderWithBuffers(str);
        }
        return true;
    }

    private boolean setupDecoderWithBuffers(String str) {
        try {
            MediaCodec createDecoderByType = MediaCodec.createDecoderByType(str);
            this.m_decoder = createDecoderByType;
            createDecoderByType.configure(this.m_format, (Surface) null, (MediaCrypto) null, 0);
            this.m_decoder.start();
            this.m_decoderStarted = true;
            this.m_decoderInputBuffers = this.m_decoder.getInputBuffers();
            this.m_decoderOutputBuffers = this.m_decoder.getOutputBuffers();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CleanupDecoder(false);
            return false;
        }
    }

    private boolean setupDecoderWithImageReader(String str) {
        boolean z;
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        try {
            MediaCodec createDecoderByType = MediaCodec.createDecoderByType(str);
            this.m_decoder = createDecoderByType;
            int[] iArr = createDecoderByType.getCodecInfo().getCapabilitiesForType(str).colorFormats;
            int length = iArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = false;
                    break;
                } else if (iArr[i] == 2135033992) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if (!z) {
                Log.w(TAG, "We can't decode to ImageReader if COLOR_FormatYUV420Flexible is not supported!");
                this.m_decoder.release();
                this.m_decoder = null;
                return false;
            }
            this.m_format.setInteger("color-format", 2135033992);
            ImageReader newInstance = ImageReader.newInstance(this.m_format.getInteger(nexExportFormat.TAG_FORMAT_WIDTH), this.m_format.getInteger(nexExportFormat.TAG_FORMAT_HEIGHT), 35, 1);
            this.m_imageReader = newInstance;
            newInstance.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { // from class: com.cdv.io.NvAndroidVideoFileReaderSW.1
                @Override // android.media.ImageReader.OnImageAvailableListener
                public void onImageAvailable(ImageReader imageReader) {
                    synchronized (NvAndroidVideoFileReaderSW.this.m_frameSyncObject) {
                        NvAndroidVideoFileReaderSW.this.m_imageReady = true;
                        NvAndroidVideoFileReaderSW.this.m_frameSyncObject.notifyAll();
                    }
                }
            }, this.m_handler);
            this.m_decoder.configure(this.m_format, this.m_imageReader.getSurface(), (MediaCrypto) null, 0);
            this.m_decoder.start();
            this.m_decoderStarted = true;
            this.m_decoderUseSurface = true;
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
        if (this.m_decoder != null && this.m_decoderStarted) {
            try {
                if (this.m_sawInputEOS && !this.m_sawOutputEOS) {
                    DrainOutputBuffers();
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
        if (!z) {
            CleanupDecoderCore(false);
        } else {
            this.m_cleanupHandler.post(new Runnable() { // from class: com.cdv.io.NvAndroidVideoFileReaderSW.2
                @Override // java.lang.Runnable
                public void run() {
                    NvAndroidVideoFileReaderSW.this.CleanupDecoderCore(true);
                }
            });
        }
        this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
        this.m_timestampOfLastCopiedFrame = Long.MIN_VALUE;
        this.m_pendingInputFrameCount = 0;
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
        ImageReader imageReader = this.m_imageReader;
        if (imageReader != null) {
            imageReader.close();
            this.m_imageReader = null;
        }
        this.m_decoderUseSurface = false;
    }

    private int SeekInternal(long j, long j2, int i) {
        long j3 = this.m_timestampOfLastDecodedFrame;
        boolean z = true;
        if ((j3 == Long.MIN_VALUE || j <= j3 || j >= j3 + this.m_contiuousDecodingThreshold) && (!this.m_extractorInOriginalState || j >= this.m_contiuousDecodingThreshold)) {
            z = false;
        }
        if (this.m_onlyDecodeKeyFrame) {
            z = false;
        }
        if (!z) {
            try {
                this.m_extractor.seekTo(j, 0);
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
        return DecodeToFrame(j, j2, i);
    }

    private int DecodeToFrame(long j, long j2, int i) {
        try {
            return DoDecodeToFrame(j, j2, i);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CleanupDecoder(false);
            return 2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a2, code lost:
        r15.m_decoder.queueInputBuffer(r17, 0, r19, r20, 0);
        r15.m_inputBufferQueued = true;
        r15.m_pendingInputFrameCount++;
        r15.m_extractor.advance();
        r15.m_extractorInOriginalState = r11;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:115:0x026e A[LOOP:0: B:3:0x0010->B:115:0x026e, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0266 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x018e  */
    /* JADX WARN: Type inference failed for: r22v2 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int DoDecodeToFrame(long r30, long r32, int r34) {
        /*
            Method dump skipped, instructions count: 660
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReaderSW.DoDecodeToFrame(long, long, int):int");
    }

    private Image AwaitNewImage() {
        synchronized (this.m_frameSyncObject) {
            do {
                if (!this.m_imageReady) {
                    try {
                        this.m_frameSyncObject.wait(3000L);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "" + e.getMessage());
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    this.m_imageReady = false;
                    try {
                        return this.m_imageReader.acquireLatestImage();
                    } catch (Exception e2) {
                        Log.e(TAG, "" + e2.getMessage());
                        e2.printStackTrace();
                        return null;
                    }
                }
            } while (this.m_imageReady);
            Log.e(TAG, "ImageReader wait timed out!");
            return null;
        }
    }

    private void DrainOutputBuffers() {
        if (!this.m_sawInputEOS || this.m_sawOutputEOS) {
            return;
        }
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

    private void InvalidLastSeekTimestamp() {
        this.m_lastSeekTimestamp = Long.MIN_VALUE;
        this.m_lastSeekActualTimestamp = Long.MIN_VALUE;
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

    /* JADX WARN: Removed duplicated region for block: B:34:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void ParseMediaFormat(android.media.MediaFormat r14) {
        /*
            r13 = this;
            java.lang.String r1 = "width"
            boolean r2 = r14.containsKey(r1)
            if (r2 == 0) goto Ld6
            java.lang.String r2 = "height"
            boolean r3 = r14.containsKey(r2)
            if (r3 == 0) goto Ld6
            java.lang.String r3 = "color-format"
            boolean r4 = r14.containsKey(r3)
            if (r4 != 0) goto L1b
            goto Ld6
        L1b:
            int r1 = r14.getInteger(r1)
            int r2 = r14.getInteger(r2)
            int r3 = r14.getInteger(r3)
            int r4 = android.os.Build.VERSION.SDK_INT
            r5 = 18
            if (r4 < r5) goto L42
            android.media.MediaCodec r6 = r13.m_decoder
            java.lang.String r6 = r6.getName()
            r7 = 25
            if (r3 != r7) goto L42
            java.lang.String r7 = "OMX.k3.video.decoder.avc"
            boolean r6 = r6.equals(r7)
            if (r6 == 0) goto L42
            r3 = 2130706688(0x7f000100, float:1.7014638E38)
        L42:
            r6 = r3
            java.lang.String r3 = "slice-height"
            boolean r7 = r14.containsKey(r3)
            if (r7 == 0) goto L50
            int r3 = r14.getInteger(r3)
            goto L51
        L50:
            r3 = r2
        L51:
            java.lang.String r7 = "stride"
            boolean r8 = r14.containsKey(r7)
            if (r8 == 0) goto L5e
            int r7 = r14.getInteger(r7)
            goto L5f
        L5e:
            r7 = r1
        L5f:
            if (r4 < r5) goto L7f
            android.media.MediaCodec r4 = r13.m_decoder
            java.lang.String r4 = r4.getName()
            java.lang.String r5 = "OMX.Nvidia."
            boolean r5 = r4.startsWith(r5)
            if (r5 == 0) goto L74
            int r3 = r3 + 15
            r3 = r3 & (-16)
            goto L7f
        L74:
            java.lang.String r5 = "OMX.SEC.avc.dec"
            boolean r4 = r4.startsWith(r5)
            if (r4 == 0) goto L7f
            r8 = r1
            r7 = r2
            goto L81
        L7f:
            r8 = r7
            r7 = r3
        L81:
            java.lang.String r3 = "crop-left"
            boolean r4 = r14.containsKey(r3)
            r5 = 0
            if (r4 == 0) goto L90
            int r3 = r14.getInteger(r3)
            r9 = r3
            goto L91
        L90:
            r9 = r5
        L91:
            java.lang.String r3 = "crop-right"
            boolean r4 = r14.containsKey(r3)
            if (r4 == 0) goto L9e
            int r1 = r14.getInteger(r3)
            goto La0
        L9e:
            int r1 = r1 + (-1)
        La0:
            r10 = r1
            java.lang.String r1 = "crop-top"
            boolean r3 = r14.containsKey(r1)
            if (r3 == 0) goto Laf
            int r1 = r14.getInteger(r1)
            r11 = r1
            goto Lb0
        Laf:
            r11 = r5
        Lb0:
            java.lang.String r1 = "crop-bottom"
            boolean r3 = r14.containsKey(r1)
            if (r3 == 0) goto Lbe
            int r0 = r14.getInteger(r1)
            r12 = r0
            goto Lc1
        Lbe:
            int r2 = r2 + (-1)
            r12 = r2
        Lc1:
            int r0 = r10 + 1
            int r3 = r0 - r9
            int r0 = r12 + 1
            int r4 = r0 - r11
            long r1 = r13.m_owner
            r0 = r13
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r9
            r9 = r10
            r10 = r11
            r11 = r12
            r0.nativeSetFormatInfo(r1, r3, r4, r5, r6, r7, r8, r9, r10, r11)
        Ld6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReaderSW.ParseMediaFormat(android.media.MediaFormat):void");
    }

    private boolean isInterruptedDecoding() {
        NvAndroidInterruptionChecker nvAndroidInterruptionChecker = this.m_interruptionChecker;
        if (nvAndroidInterruptionChecker == null) {
            return false;
        }
        return nvAndroidInterruptionChecker.isInterrupted();
    }
}
