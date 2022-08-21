package com.xiaomi.mediacodec;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.opengl.EGLSurface;
import android.opengl.GLES30;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mediacodec.EglBase;
import com.xiaomi.mediacodec.MoviePlayer;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes3.dex */
public class MiHWDecoder implements SurfaceTexture.OnFrameAvailableListener, MoviePlayer.PlayerFeedback, MoviePlayer.FrameCallback {
    private static final String AVC_MIME_TYPE_AVC = "video/avc";
    private static final String AVC_MIME_TYPE_HEVC = "video/hevc";
    private static final String AVC_MIME_TYPE_MPEG4 = "video/mp4v-es";
    private static final int CMD_AUDIO_BYTE = 7;
    private static final int CMD_AUDIO_FORMATE = 6;
    private static final int CMD_FBO_DRAW = 2;
    private static final int CMD_INIT = 1;
    private static final int CMD_READFILE_END = 5;
    private static final int CMD_RECODER_END = 8;
    private static final int CMD_RELEASE = 4;
    private static final int CMD_SCREEN_DRAW = 3;
    private static final int CMD_VIDEO_FRAME = 9;
    private static final String TAG = "MiHWDecoder";
    private int mCameraTextureId;
    private EglBase mEgl;
    private int mFrameBuffer;
    private EGLSurface mGLSurface;
    private GLHandler mGlHandler;
    private OriginalRenderDrawer mOriginalDrawer;
    public MoviePlayer.PlayTask mPlayTask;
    private SurfaceTexture mSurfaceTexture;
    private HandlerThread mThread;
    private boolean mVideoOnly;
    private volatile EglBase.Context shader_egl_context;
    private MoviePlayer player = null;
    private String mSourceFile = "";
    private long mContext = 0;
    private int mRecoderWidth = 0;
    private int mRecoderHeight = 0;
    private volatile boolean mPlayerExit = false;
    private long mSeekStartMS = -1;
    private long mSeekEndMS = -1;
    private int mlastCount = 0;
    private int mSourceWidth = 0;
    private int mSourceHeight = 0;
    private boolean mError = false;
    private boolean mIsQcomm = false;
    private boolean mLoop = false;
    private int mFrameNums = 0;
    public boolean mIsStarted = false;
    private int mCaptureOne = 0;
    private int mNums = 0;
    private long mStartTime = 0;

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void loopReset() {
    }

    public native void onAudioFormatJni(long j, int i, int i2);

    public native void onAudioFrameJni(long j, byte[] bArr, int i, long j2);

    public native void onDecoderFinishedJni(long j);

    public native void onStreamDurationJni(long j, long j2);

    public native void onVideoFrameJni(long j, int i, int i2, int i3, long j2);

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void postRender() {
    }

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void preRender(long j) {
    }

    public static /* synthetic */ int access$1008(MiHWDecoder miHWDecoder) {
        int i = miHWDecoder.mNums;
        miHWDecoder.mNums = i + 1;
        return i;
    }

    public static /* synthetic */ int access$908(MiHWDecoder miHWDecoder) {
        int i = miHWDecoder.mCaptureOne;
        miHWDecoder.mCaptureOne = i + 1;
        return i;
    }

    public MiHWDecoder(boolean z) {
        this.mVideoOnly = false;
        Logg.LogI("eglGetCurrentContext:" + EglBase.getCurrentContext());
        this.shader_egl_context = EglBase.getCurrentContext();
        this.mVideoOnly = z;
    }

    public MoviePlayer getPlayer() {
        return this.player;
    }

    public void setTransferDurationTime(long j, long j2) {
        Logg.LogI(" startTransfer mSeekStartMS " + j + " mSeekEndMS " + j2);
        this.mSeekStartMS = j;
        this.mSeekEndMS = j2;
        Logg.LogI(" startTransfer mSeekStartMS " + this.mSeekStartMS + " mSeekEndMS " + this.mSeekEndMS);
    }

    private static int selectTrack(MediaExtractor mediaExtractor) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            String string = trackFormat.getString("mime");
            if (string.startsWith("video/")) {
                Logg.LogI("Extractor selected track " + i + " (" + string + "): " + trackFormat);
                return i;
            }
        }
        return -1;
    }

    public boolean getVideoInfo(String str) {
        MediaExtractor mediaExtractor;
        File file = new File(str);
        MediaExtractor mediaExtractor2 = null;
        try {
            mediaExtractor = new MediaExtractor();
        } catch (Exception unused) {
        }
        try {
            mediaExtractor.setDataSource(file.toString());
            int selectTrack = selectTrack(mediaExtractor);
            if (selectTrack < 0) {
                return false;
            }
            mediaExtractor.selectTrack(selectTrack);
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(selectTrack);
            GlUtil.mPictureRotation = 0;
            if (trackFormat.containsKey("rotation-degrees")) {
                GlUtil.mPictureRotation = trackFormat.getInteger("rotation-degrees");
            }
            GlUtil.mWidht = trackFormat.getInteger(nexExportFormat.TAG_FORMAT_WIDTH);
            GlUtil.mHeight = trackFormat.getInteger(nexExportFormat.TAG_FORMAT_HEIGHT);
            Logg.LogI(" MoviePlayer play url " + file.getAbsolutePath() + " width " + GlUtil.mWidht + " height " + GlUtil.mHeight + " rotation " + GlUtil.mPictureRotation);
            mediaExtractor.release();
            return true;
        } catch (Exception unused2) {
            mediaExtractor2 = mediaExtractor;
            if (mediaExtractor2 != null) {
                mediaExtractor2.release();
            }
            return false;
        }
    }

    public int getVideoWidth() {
        Logg.LogI("getVideoWidth: " + GlUtil.mWidht);
        return GlUtil.mWidht;
    }

    public int getVideoHeight() {
        Logg.LogI("getVideoHeight: " + GlUtil.mHeight);
        return GlUtil.mHeight;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0041, code lost:
        if (r5.isEncoder() != false) goto L16;
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a9 A[Catch: Exception -> 0x01c5, TryCatch #1 {Exception -> 0x01c5, blocks: (B:26:0x0081, B:23:0x007a, B:25:0x007e, B:28:0x0086, B:30:0x008c, B:32:0x0092, B:45:0x00cd, B:38:0x00a0, B:40:0x00a9, B:42:0x00c3, B:43:0x00c8, B:46:0x00d0, B:48:0x00ee, B:49:0x010b, B:51:0x0143, B:53:0x014f, B:55:0x0183, B:59:0x0196, B:61:0x019d, B:63:0x01a8, B:65:0x01ae, B:68:0x01ba, B:62:0x01a3, B:35:0x0099), top: B:79:0x0081 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x019d A[Catch: Exception -> 0x01c5, TryCatch #1 {Exception -> 0x01c5, blocks: (B:26:0x0081, B:23:0x007a, B:25:0x007e, B:28:0x0086, B:30:0x008c, B:32:0x0092, B:45:0x00cd, B:38:0x00a0, B:40:0x00a9, B:42:0x00c3, B:43:0x00c8, B:46:0x00d0, B:48:0x00ee, B:49:0x010b, B:51:0x0143, B:53:0x014f, B:55:0x0183, B:59:0x0196, B:61:0x019d, B:63:0x01a8, B:65:0x01ae, B:68:0x01ba, B:62:0x01a3, B:35:0x0099), top: B:79:0x0081 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01a3 A[Catch: Exception -> 0x01c5, TryCatch #1 {Exception -> 0x01c5, blocks: (B:26:0x0081, B:23:0x007a, B:25:0x007e, B:28:0x0086, B:30:0x008c, B:32:0x0092, B:45:0x00cd, B:38:0x00a0, B:40:0x00a9, B:42:0x00c3, B:43:0x00c8, B:46:0x00d0, B:48:0x00ee, B:49:0x010b, B:51:0x0143, B:53:0x014f, B:55:0x0183, B:59:0x0196, B:61:0x019d, B:63:0x01a8, B:65:0x01ae, B:68:0x01ba, B:62:0x01a3, B:35:0x0099), top: B:79:0x0081 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01ae A[Catch: Exception -> 0x01c5, TryCatch #1 {Exception -> 0x01c5, blocks: (B:26:0x0081, B:23:0x007a, B:25:0x007e, B:28:0x0086, B:30:0x008c, B:32:0x0092, B:45:0x00cd, B:38:0x00a0, B:40:0x00a9, B:42:0x00c3, B:43:0x00c8, B:46:0x00d0, B:48:0x00ee, B:49:0x010b, B:51:0x0143, B:53:0x014f, B:55:0x0183, B:59:0x0196, B:61:0x019d, B:63:0x01a8, B:65:0x01ae, B:68:0x01ba, B:62:0x01a3, B:35:0x0099), top: B:79:0x0081 }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01b8  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x00d0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x00cd A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x00cb A[EDGE_INSN: B:90:0x00cb->B:44:0x00cb ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean findHwCodec(java.lang.String r16, int r17, int r18, float r19, boolean r20) {
        /*
            Method dump skipped, instructions count: 478
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mediacodec.MiHWDecoder.findHwCodec(java.lang.String, int, int, float, boolean):boolean");
    }

    public boolean isAvcDecoderSupported(int i, int i2, float f) {
        Logg.LogI("width: " + i + ", height: " + i2 + ", frameRate: " + f);
        return findHwCodec(AVC_MIME_TYPE_AVC, i, i2, f, false);
    }

    public boolean isHevcDecoderSupported(int i, int i2, float f) {
        Logg.LogI("width: " + i + ", height: " + i2 + ", frameRate: " + f);
        return findHwCodec(AVC_MIME_TYPE_HEVC, i, i2, f, false);
    }

    public boolean isMpeg4DecoderSupported(int i, int i2, float f) {
        Logg.LogI("width: " + i + ", height: " + i2 + ", frameRate: " + f);
        return findHwCodec(AVC_MIME_TYPE_AVC, i, i2, f, false);
    }

    public void setLoopMode(boolean z) {
        Logg.LogI("setLoopMode: " + z);
        this.mLoop = z;
        MoviePlayer.PlayTask playTask = this.mPlayTask;
        if (playTask != null) {
            playTask.setLoopMode(z);
        }
    }

    public boolean seekTo(long j, int i) {
        Logg.LogI("MiHWDecoder seekTo: msec: " + j + " seekMode: " + i);
        if (!this.mIsStarted) {
            this.mSeekStartMS = j;
            startDecoder(this.mSourceFile, this.mContext);
        }
        MoviePlayer.PlayTask playTask = this.mPlayTask;
        if (playTask != null) {
            return playTask.seekTo(j, i);
        }
        return false;
    }

    public void frameReceived() {
        Logg.LogI("MiHWDecoder frameReceived! ");
        MoviePlayer.PlayTask playTask = this.mPlayTask;
        if (playTask != null) {
            playTask.frameReceived();
        }
    }

    public void startDecoder(String str, long j) {
        if (str.isEmpty()) {
            Logg.LogI(" startDecoder sourcePath is empty !");
            return;
        }
        Logg.LogI(" startDecoder sourcePath: " + str + " context: " + j);
        this.mOriginalDrawer = new OriginalRenderDrawer();
        this.mSourceFile = str;
        this.mContext = j;
        this.mlastCount = 0;
        this.mIsStarted = true;
        this.mError = false;
        HandlerThread handlerThread = new HandlerThread("GL thread");
        this.mThread = handlerThread;
        handlerThread.start();
        GLHandler gLHandler = new GLHandler(this.mThread.getLooper());
        this.mGlHandler = gLHandler;
        Message obtainMessage = gLHandler.obtainMessage(1);
        this.mGlHandler.removeMessages(1);
        this.mGlHandler.sendMessage(obtainMessage);
        Logg.LogI(" startDecoder done");
        this.mPlayerExit = false;
    }

    public void pauseDecoder() {
        MoviePlayer moviePlayer;
        Logg.LogI("pauseDecoder");
        if (this.mIsStarted && (moviePlayer = this.player) != null) {
            moviePlayer.requestPause();
        }
    }

    public void resumeDecoder() {
        MoviePlayer moviePlayer;
        Logg.LogI("resumeDecoder");
        if (this.mIsStarted && (moviePlayer = this.player) != null) {
            moviePlayer.requestResume();
        }
    }

    public void stopDecoder() {
        Logg.LogI("stopDecoder");
        if (!this.mIsStarted) {
            return;
        }
        MoviePlayer moviePlayer = this.player;
        if (moviePlayer != null) {
            moviePlayer.requestStop();
        }
        this.mIsStarted = false;
        long currentTimeMillis = System.currentTimeMillis();
        while (!this.mPlayerExit && System.currentTimeMillis() - currentTimeMillis <= 2000) {
        }
        HandlerThread handlerThread = this.mThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            this.mGlHandler.removeCallbacksAndMessages(null);
            this.mThread = null;
            this.mGlHandler = null;
        }
        if (this.player != null) {
            this.player = null;
        }
        if (this.mPlayTask != null) {
            this.mPlayTask = null;
        }
        Logg.LogI("stopDecoder done");
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Logg.LogI("MiHWDecoder onFrameAvailable ");
        if (!this.mIsStarted) {
            return;
        }
        this.mGlHandler.removeMessages(2);
        this.mGlHandler.sendEmptyMessage(2);
    }

    @Override // com.xiaomi.mediacodec.MoviePlayer.PlayerFeedback
    public void playbackStopped(int i) {
        Logg.LogI(" playbackStopped " + i);
        if (i > 0) {
            this.mError = true;
        }
        GLHandler gLHandler = this.mGlHandler;
        if (gLHandler != null) {
            gLHandler.sendEmptyMessage(5);
            this.mGlHandler.sendEmptyMessage(4);
        }
    }

    /* loaded from: classes3.dex */
    public class GLHandler extends Handler {
        private GLHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            boolean z = false;
            if (i == 1) {
                Logg.LogI(" createPbufferSurface width " + GlUtil.mWidht + " height " + GlUtil.mHeight + " shader_egl_context:" + MiHWDecoder.this.shader_egl_context);
                try {
                    MiHWDecoder miHWDecoder = MiHWDecoder.this;
                    miHWDecoder.mEgl = EglBase.create(miHWDecoder.shader_egl_context);
                } catch (Exception e) {
                    e.printStackTrace();
                    MiHWDecoder.this.mEgl = EglBase.create();
                }
                MiHWDecoder.this.mEgl.createPbufferSurface(GlUtil.mWidht, GlUtil.mHeight);
                MiHWDecoder.this.mEgl.makeCurrent();
                MiHWDecoder.this.mFrameBuffer = GlesUtil.createFrameBuffer();
                if (MiHWDecoder.this.mRecoderWidth == GlUtil.mWidht && MiHWDecoder.this.mRecoderHeight == GlUtil.mHeight) {
                    z = true;
                }
                MiHWDecoder.this.mOriginalDrawer.setReserverResolution(z);
                MiHWDecoder.this.mCameraTextureId = GlesUtil.createCameraTexture(z);
                MiHWDecoder.this.mSurfaceTexture = new SurfaceTexture(MiHWDecoder.this.mCameraTextureId);
                MiHWDecoder.this.mSurfaceTexture.setOnFrameAvailableListener(MiHWDecoder.this);
                MiHWDecoder.this.Play();
                MiHWDecoder.this.mOriginalDrawer.create();
                MiHWDecoder.this.mOriginalDrawer.setInputTextureId(MiHWDecoder.this.mCameraTextureId);
                MiHWDecoder.this.mOriginalDrawer.surfaceChangedSize(GlUtil.mWidht, GlUtil.mHeight);
                MiHWDecoder.this.mOriginalDrawer.getOutputTextureId();
                MiHWDecoder.this.mPlayTask.execute();
            } else if (i != 2) {
                if (i != 4) {
                    if (i == 6) {
                        MediaFormat mediaFormat = (MediaFormat) message.obj;
                        int integer = mediaFormat.getInteger("channel-count");
                        int integer2 = mediaFormat.getInteger("sample-rate");
                        MiHWDecoder miHWDecoder2 = MiHWDecoder.this;
                        miHWDecoder2.onAudioFormatJni(miHWDecoder2.mContext, integer, integer2);
                        return;
                    } else if (i != 7) {
                        return;
                    } else {
                        MoviePlayer.MediaFrame mediaFrame = (MoviePlayer.MediaFrame) message.obj;
                        int remaining = mediaFrame.buffer.remaining();
                        byte[] bArr = new byte[remaining];
                        mediaFrame.buffer.get(bArr, 0, remaining);
                        MiHWDecoder miHWDecoder3 = MiHWDecoder.this;
                        long j = miHWDecoder3.mContext;
                        MediaCodec.BufferInfo bufferInfo = mediaFrame.info;
                        miHWDecoder3.onAudioFrameJni(j, bArr, bufferInfo.size, bufferInfo.presentationTimeUs / 1000);
                        return;
                    }
                }
                if (MiHWDecoder.this.mOriginalDrawer != null) {
                    GlesUtil.deleteFrameBuffer(MiHWDecoder.this.mFrameBuffer, MiHWDecoder.this.mOriginalDrawer.getOutputTextureId());
                    GLES30.glDeleteTextures(1, new int[]{MiHWDecoder.this.mCameraTextureId}, 0);
                    MiHWDecoder.this.mOriginalDrawer.destroy();
                    Logg.LogI(" detete frame ");
                }
                if (MiHWDecoder.this.mSurfaceTexture != null) {
                    MiHWDecoder.this.mSurfaceTexture.release();
                    MiHWDecoder.this.mSurfaceTexture = null;
                }
                MiHWDecoder.this.mOriginalDrawer = null;
                if (MiHWDecoder.this.mEgl != null) {
                    MiHWDecoder.this.mEgl.release();
                }
                MiHWDecoder.this.mPlayerExit = true;
                MiHWDecoder.this.mEgl = null;
                Logg.LogI(" recoder end " + MiHWDecoder.this.mPlayerExit);
            } else {
                MiHWDecoder.this.mSurfaceTexture.updateTexImage();
                GlesUtil.checkError();
                float[] fArr = new float[16];
                MiHWDecoder.this.mSurfaceTexture.getTransformMatrix(fArr);
                long timestamp = MiHWDecoder.this.mSurfaceTexture.getTimestamp();
                GlesUtil.bindFrameBuffer(MiHWDecoder.this.mFrameBuffer, MiHWDecoder.this.mOriginalDrawer.getOutputTextureId());
                GlesUtil.checkError();
                MiHWDecoder.this.mOriginalDrawer.draw(timestamp, fArr);
                GlesUtil.checkError();
                GLES30.glFlush();
                GlesUtil.checkError();
                GlesUtil.unBindFrameBuffer();
                GlesUtil.checkError();
                int videoWidth = MiHWDecoder.this.getPlayer().getVideoWidth();
                int videoHeight = MiHWDecoder.this.getPlayer().getVideoHeight();
                if (MiHWDecoder.this.mCaptureOne < 0) {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(videoWidth * videoHeight * 4);
                    GLES30.glBindFramebuffer(36160, MiHWDecoder.this.mFrameBuffer);
                    allocateDirect.order(ByteOrder.LITTLE_ENDIAN);
                    GLES30.glReadPixels(0, 0, videoWidth, videoHeight, 6408, 5121, allocateDirect);
                    allocateDirect.rewind();
                    Bitmap createBitmap = Bitmap.createBitmap(videoWidth, videoHeight, Bitmap.Config.ARGB_8888);
                    createBitmap.copyPixelsFromBuffer(allocateDirect);
                    GLES30.glBindFramebuffer(36160, 0);
                    MiHWDecoder.access$908(MiHWDecoder.this);
                    GlUtil.saveFile(createBitmap, "/sdcard/kk", "kkk" + MiHWDecoder.this.mCaptureOne + ".jpeg");
                }
                MiHWDecoder.access$1008(MiHWDecoder.this);
                if ((MiHWDecoder.this.mSeekStartMS == -1 || timestamp >= MiHWDecoder.this.mSeekStartMS * 1000 * 1000) && (MiHWDecoder.this.mSeekEndMS == -1 || timestamp <= MiHWDecoder.this.mSeekEndMS * 1000 * 1000)) {
                    MiHWDecoder miHWDecoder4 = MiHWDecoder.this;
                    miHWDecoder4.onVideoFrameJni(miHWDecoder4.mContext, MiHWDecoder.this.mOriginalDrawer.getOutputTextureId(), videoWidth, videoHeight, timestamp / 1000000);
                    return;
                }
                MiHWDecoder.this.getPlayer().getOneFrame();
            }
        }
    }

    public void Play() {
        Surface surface = new Surface(this.mSurfaceTexture);
        try {
            MoviePlayer moviePlayer = new MoviePlayer(new File(this.mSourceFile), surface, this, this.mSeekStartMS, this.mVideoOnly);
            this.player = moviePlayer;
            if (this.mRecoderWidth == 0 || this.mRecoderHeight == 0) {
                this.mRecoderWidth = moviePlayer.getVideoWidth();
                this.mRecoderHeight = this.player.getVideoHeight();
            }
            MoviePlayer.PlayTask playTask = new MoviePlayer.PlayTask(this.player, this);
            this.mPlayTask = playTask;
            playTask.setLoopMode(this.mLoop);
            Logg.LogI("start play");
        } catch (IOException e) {
            Logg.LogE("Unable to play movie" + e);
            surface.release();
        }
    }

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void onAudioFormat(MediaFormat mediaFormat) {
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(6, mediaFormat));
    }

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void onAudioFrame(MoviePlayer.MediaFrame mediaFrame) {
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(7, mediaFrame));
    }

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void onVideoFrame(int i) {
        int i2 = this.mlastCount;
        if (i < i2) {
            i = i2 + 1;
        } else {
            this.mlastCount = i;
        }
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(9, Integer.valueOf(i)));
    }

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void onVideoCrop(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mOriginalDrawer.cropSize(i, i2, i3, i4, i5, i6);
    }

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void onStreamDuration(long j) {
        onStreamDurationJni(this.mContext, j);
    }

    @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
    public void onDecoderFinished() {
        Logg.LogE("onDecoderFinished! ");
        onDecoderFinishedJni(this.mContext);
    }
}
