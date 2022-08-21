package com.xiaomi.mediatranscode;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
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
import com.xiaomi.mediatranscode.MoviePlayer;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes3.dex */
public class MiVideoTranscode implements SurfaceTexture.OnFrameAvailableListener, MoviePlayer.PlayerFeedback, MoviePlayer.FrameCallback {
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
    private static final String TAG = "MiVideoTranscode";
    private TransferCallBack mCallBack;
    private int mCameraTextureId;
    private EglBase mEgl;
    private int mFrameBuffer;
    private EGLSurface mGLSurface;
    private GLHandler mGlHandler;
    private int mLutTextureId;
    private OriginalRenderDrawer mOriginalDrawer;
    public MoviePlayer.PlayTask mPlayTask;
    private RecordRenderDrawer mRecordDrawer;
    private SurfaceTexture mSurfaceTexture;
    private HandlerThread mThread;
    private MoviePlayer player = null;
    private String mSourceFile = "";
    private int mRecoderWidth = 0;
    private int mRecoderHeight = 0;
    private int mBitRate = 0;
    private int mFrameRate = 0;
    private String mPath = "";
    private String mCodecName = "avc";
    private boolean mPlayerExit = false;
    private boolean mRecoderExit = false;
    private long mSeekStartMS = -1;
    private long mSeekEndMS = -1;
    private int mlastCount = 0;
    private boolean mExit = false;
    private int mSourceWidth = 0;
    private int mSourceHeight = 0;
    private boolean mError = false;
    private boolean mIsQcomm = false;
    private int mFrameNums = 0;
    public boolean mIsStarted = false;
    private int mCaptureOne = 0;
    private int mNums = 0;
    private long mStartTime = 0;

    /* loaded from: classes3.dex */
    public interface TransferCallBack {
        void OnGetPercent(int i);

        void OnTranscodeFailed(int i);

        void OnTranscodeSuccessed();
    }

    @Override // com.xiaomi.mediatranscode.MoviePlayer.FrameCallback
    public void loopReset() {
    }

    @Override // com.xiaomi.mediatranscode.MoviePlayer.FrameCallback
    public void postRender() {
    }

    @Override // com.xiaomi.mediatranscode.MoviePlayer.FrameCallback
    public void preRender(long j) {
    }

    public static /* synthetic */ int access$1408(MiVideoTranscode miVideoTranscode) {
        int i = miVideoTranscode.mCaptureOne;
        miVideoTranscode.mCaptureOne = i + 1;
        return i;
    }

    public static /* synthetic */ int access$1508(MiVideoTranscode miVideoTranscode) {
        int i = miVideoTranscode.mNums;
        miVideoTranscode.mNums = i + 1;
        return i;
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

    private static int selectTrack(MediaExtractor mediaExtractor, String str) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            String string = trackFormat.getString("mime");
            if (string.startsWith(str + h.g)) {
                Logg.LogI("Extractor selected track " + i + " (" + string + "): " + trackFormat);
                return i;
            }
        }
        return -1;
    }

    public boolean getVideoInfo(String str) {
        MediaExtractor mediaExtractor;
        boolean z;
        File file = new File(str);
        MediaExtractor mediaExtractor2 = null;
        try {
            mediaExtractor = new MediaExtractor();
        } catch (Exception unused) {
        }
        try {
            mediaExtractor.setDataSource(file.toString());
            int selectTrack = selectTrack(mediaExtractor, "audio");
            if (selectTrack != -1) {
                String string = mediaExtractor.getTrackFormat(selectTrack).getString("mime");
                Logg.LogI("audio format is: " + string);
                if (!string.equals("audio/mpeg")) {
                    int selectTrack2 = selectTrack(mediaExtractor);
                    if (selectTrack2 < 0) {
                        return false;
                    }
                    mediaExtractor.selectTrack(selectTrack2);
                    MediaFormat trackFormat = mediaExtractor.getTrackFormat(selectTrack2);
                    GlUtil.mPictureRotation = 0;
                    if (trackFormat.containsKey("rotation-degrees")) {
                        GlUtil.mPictureRotation = trackFormat.getInteger("rotation-degrees");
                    }
                    GlUtil.mWidht = trackFormat.getInteger(nexExportFormat.TAG_FORMAT_WIDTH);
                    GlUtil.mHeight = trackFormat.getInteger(nexExportFormat.TAG_FORMAT_HEIGHT);
                    Logg.LogI(" MoviePlayer play url " + file.getAbsolutePath() + " width " + GlUtil.mWidht + " height " + GlUtil.mHeight + " rotation " + GlUtil.mPictureRotation);
                    z = true;
                    mediaExtractor.release();
                    return z;
                }
            }
            z = false;
            mediaExtractor.release();
            return z;
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
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mediatranscode.MiVideoTranscode.findHwCodec(java.lang.String, int, int, float, boolean):boolean");
    }

    public boolean isAvcEncoderSupported(int i, int i2, float f) {
        Logg.LogI("width: " + i + ", height: " + i2 + ", frameRate: " + f);
        this.mIsQcomm = false;
        return findHwCodec(AVC_MIME_TYPE_AVC, i, i2, f, true);
    }

    public boolean isHevcEncoderSupported(int i, int i2, float f) {
        Logg.LogI("width: " + i + ", height: " + i2 + ", frameRate: " + f);
        this.mIsQcomm = false;
        return findHwCodec(AVC_MIME_TYPE_HEVC, i, i2, f, true);
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

    public void startTransfer(String str, String str2, int i, int i2, int i3, int i4, String str3, TransferCallBack transferCallBack) {
        Logg.LogI(" startTransfer sourcePath " + str + " codecName " + str2 + " targetWidth " + i + " targetHeight " + i2 + " bitRate " + i3 + " frameRate " + i4 + " targetPath " + str3 + " mSeekStartMS " + this.mSeekStartMS + " mSeekEndMS " + this.mSeekEndMS + " mIsQcomm " + this.mIsQcomm);
        this.mRecordDrawer = new RecordRenderDrawer(this);
        this.mOriginalDrawer = new OriginalRenderDrawer();
        this.mCallBack = transferCallBack;
        this.mSourceFile = str;
        this.mRecoderWidth = i;
        this.mRecoderHeight = i2;
        if (this.mIsQcomm) {
            this.mFrameRate = 30;
            this.mBitRate = i3;
        } else {
            this.mFrameRate = i4;
            this.mBitRate = i3;
        }
        this.mPath = str3;
        this.mCodecName = str2;
        this.mlastCount = 0;
        this.mExit = false;
        this.mIsStarted = true;
        this.mError = false;
        HandlerThread handlerThread = new HandlerThread("GL thread");
        this.mThread = handlerThread;
        handlerThread.start();
        GLHandler gLHandler = new GLHandler(this.mThread.getLooper());
        this.mGlHandler = gLHandler;
        Message obtainMessage = gLHandler.obtainMessage(1);
        this.mGlHandler.removeMessages(1);
        this.mGlHandler.sendMessageDelayed(obtainMessage, 1000L);
        this.mRecordDrawer.setParams(str2, this.mRecoderWidth, this.mRecoderHeight, this.mFrameRate, this.mBitRate, this.mPath);
        Logg.LogI(" startTransfer");
        this.mPlayerExit = false;
        this.mRecoderExit = false;
    }

    public void stopTransfer(TransferCallBack transferCallBack) {
        Logg.LogI("stopTransfer start");
        if (!this.mIsStarted) {
            return;
        }
        MoviePlayer moviePlayer = this.player;
        if (moviePlayer != null) {
            moviePlayer.requestStop();
        }
        this.mIsStarted = false;
        while (true) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.mExit && this.mPlayerExit && this.mRecoderExit) {
                break;
            }
        }
        HandlerThread handlerThread = this.mThread;
        if (handlerThread != null) {
            handlerThread.quit();
            this.mThread = null;
        }
        Logg.LogI("stopTransfer");
    }

    public void onRecoderEOF() {
        this.mGlHandler.sendEmptyMessage(4);
        this.mExit = true;
    }

    public void onRecoderFailed(int i) {
        Logg.LogE(" Recoder errror " + i);
        if (i > 0) {
            this.mError = true;
        }
        MoviePlayer moviePlayer = this.player;
        if (moviePlayer != null) {
            moviePlayer.requestStop();
        }
        this.mPlayerExit = true;
        this.mGlHandler.sendEmptyMessage(5);
        if (i > 0) {
            this.mCallBack.OnTranscodeFailed(2);
        }
    }

    public void onFileComplete() {
        this.mRecoderExit = true;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if (!this.mIsStarted) {
            return;
        }
        this.mGlHandler.removeMessages(2);
        this.mGlHandler.sendEmptyMessage(2);
    }

    @Override // com.xiaomi.mediatranscode.MoviePlayer.PlayerFeedback
    public void playbackStopped(int i) {
        Logg.LogI(" playbackStopped" + i);
        if (i > 0) {
            this.mError = true;
        }
        this.mPlayerExit = true;
        this.mGlHandler.sendEmptyMessage(5);
        if (i > 0) {
            this.mCallBack.OnTranscodeFailed(1);
        }
    }

    /* loaded from: classes3.dex */
    public class GLHandler extends Handler {
        public GLHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            boolean z = false;
            if (i == 1) {
                Logg.LogI(" createPbufferSurface width " + GlUtil.mWidht + " height " + GlUtil.mHeight);
                MiVideoTranscode.this.mEgl = EglBase.create();
                MiVideoTranscode.this.mEgl.createPbufferSurface(GlUtil.mWidht, GlUtil.mHeight);
                MiVideoTranscode.this.mEgl.makeCurrent();
                MiVideoTranscode.this.mFrameBuffer = GlesUtil.createFrameBuffer();
                if (MiVideoTranscode.this.mRecoderWidth == GlUtil.mWidht && MiVideoTranscode.this.mRecoderHeight == GlUtil.mHeight) {
                    z = true;
                }
                MiVideoTranscode.this.mOriginalDrawer.setReserverResolution(z);
                MiVideoTranscode.this.mRecordDrawer.setReserverResolution(z);
                MiVideoTranscode.this.mCameraTextureId = GlesUtil.createCameraTexture(z);
                MiVideoTranscode.this.mLutTextureId = GlesUtil.createLutTexture();
                MiVideoTranscode.this.mSurfaceTexture = new SurfaceTexture(MiVideoTranscode.this.mCameraTextureId);
                MiVideoTranscode.this.mSurfaceTexture.setOnFrameAvailableListener(MiVideoTranscode.this);
                MiVideoTranscode.this.Play();
                MiVideoTranscode.this.mOriginalDrawer.create();
                MiVideoTranscode.this.mOriginalDrawer.setInputTextureId(MiVideoTranscode.this.mCameraTextureId);
                MiVideoTranscode.this.mOriginalDrawer.setLutTextureId(MiVideoTranscode.this.mLutTextureId);
                MiVideoTranscode.this.mOriginalDrawer.surfaceChangedSize(GlUtil.mWidht, GlUtil.mHeight);
                int outputTextureId = MiVideoTranscode.this.mOriginalDrawer.getOutputTextureId();
                MiVideoTranscode.this.mRecordDrawer.setParams(MiVideoTranscode.this.mCodecName, MiVideoTranscode.this.mRecoderWidth, MiVideoTranscode.this.mRecoderHeight, MiVideoTranscode.this.mFrameRate, MiVideoTranscode.this.mBitRate, MiVideoTranscode.this.mPath);
                MiVideoTranscode.this.mRecordDrawer.create();
                MiVideoTranscode.this.mRecordDrawer.surfaceChangedSize(MiVideoTranscode.this.mRecoderWidth, MiVideoTranscode.this.mRecoderHeight);
                MiVideoTranscode.this.mRecordDrawer.setLutTextureId(MiVideoTranscode.this.mLutTextureId);
                MiVideoTranscode.this.mRecordDrawer.setInputTextureId(outputTextureId);
                MiVideoTranscode.this.mRecordDrawer.startRecord();
                MiVideoTranscode.this.mPlayTask.execute();
            } else if (i != 2) {
                if (i != 4) {
                    if (i == 5) {
                        if (MiVideoTranscode.this.mRecordDrawer == null) {
                            return;
                        }
                        MiVideoTranscode.this.mRecordDrawer.stopRecord();
                        return;
                    } else if (i == 6) {
                        MiVideoTranscode.this.mRecordDrawer.addAudioFormat(message.obj);
                        return;
                    } else if (i != 7) {
                        if (i != 9 || MiVideoTranscode.this.mCallBack == null) {
                            return;
                        }
                        MiVideoTranscode.this.mCallBack.OnGetPercent(((Integer) message.obj).intValue());
                        return;
                    } else {
                        MiVideoTranscode.this.mRecordDrawer.addAudioFrame(message.obj);
                        return;
                    }
                }
                if (MiVideoTranscode.this.mOriginalDrawer != null) {
                    GlesUtil.deleteFrameBuffer(MiVideoTranscode.this.mFrameBuffer, MiVideoTranscode.this.mOriginalDrawer.getOutputTextureId());
                    Logg.LogI(" detete frame ");
                }
                if (MiVideoTranscode.this.mRecordDrawer != null) {
                    MiVideoTranscode.this.mRecordDrawer.quit();
                    MiVideoTranscode.this.mRecordDrawer = null;
                }
                MiVideoTranscode.this.mOriginalDrawer = null;
                if (MiVideoTranscode.this.mEgl != null) {
                    MiVideoTranscode.this.mEgl.release();
                }
                MiVideoTranscode.this.mExit = true;
                MiVideoTranscode.this.mEgl = null;
                Logg.LogI(" recoder end ");
                if (MiVideoTranscode.this.mCallBack == null || MiVideoTranscode.this.mError) {
                    return;
                }
                MiVideoTranscode.this.mCallBack.OnTranscodeSuccessed();
            } else {
                MiVideoTranscode.this.mSurfaceTexture.updateTexImage();
                float[] fArr = new float[16];
                MiVideoTranscode.this.mSurfaceTexture.getTransformMatrix(fArr);
                long timestamp = MiVideoTranscode.this.mSurfaceTexture.getTimestamp();
                GlesUtil.bindFrameBuffer(MiVideoTranscode.this.mFrameBuffer, MiVideoTranscode.this.mOriginalDrawer.getOutputTextureId());
                MiVideoTranscode.this.mOriginalDrawer.draw(timestamp, fArr);
                GLES30.glFlush();
                GlesUtil.unBindFrameBuffer();
                if (MiVideoTranscode.this.mCaptureOne < 0) {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(GlUtil.mWidht * GlUtil.mHeight * 4);
                    GLES30.glBindFramebuffer(36160, MiVideoTranscode.this.mFrameBuffer);
                    allocateDirect.order(ByteOrder.LITTLE_ENDIAN);
                    GLES30.glReadPixels(0, 0, GlUtil.mWidht, GlUtil.mHeight, 6408, 5121, allocateDirect);
                    allocateDirect.rewind();
                    Bitmap createBitmap = Bitmap.createBitmap(GlUtil.mWidht, GlUtil.mHeight, Bitmap.Config.ARGB_8888);
                    createBitmap.copyPixelsFromBuffer(allocateDirect);
                    GLES30.glBindFramebuffer(36160, 0);
                    MiVideoTranscode.access$1408(MiVideoTranscode.this);
                    GlUtil.saveFile(createBitmap, "/sdcard/kk", "kkk" + MiVideoTranscode.this.mCaptureOne + ".jpeg");
                }
                MiVideoTranscode.access$1508(MiVideoTranscode.this);
                if ((MiVideoTranscode.this.mSeekStartMS == -1 || timestamp >= MiVideoTranscode.this.mSeekStartMS * 1000 * 1000) && (MiVideoTranscode.this.mSeekEndMS == -1 || timestamp <= MiVideoTranscode.this.mSeekEndMS * 1000 * 1000)) {
                    MiVideoTranscode.this.mRecordDrawer.draw(timestamp, fArr);
                } else {
                    MiVideoTranscode.this.getPlayer().getOneFrame();
                }
                if (MiVideoTranscode.this.mSeekEndMS == -1 || timestamp <= MiVideoTranscode.this.mSeekEndMS * 1000 * 1000) {
                    return;
                }
                MiVideoTranscode.this.player.requestStop();
            }
        }
    }

    public void Play() {
        Surface surface = new Surface(this.mSurfaceTexture);
        try {
            MoviePlayer moviePlayer = new MoviePlayer(new File(this.mSourceFile), surface, this, this.mSeekStartMS);
            this.player = moviePlayer;
            if (this.mRecoderWidth == 0 || this.mRecoderHeight == 0) {
                this.mRecoderWidth = moviePlayer.getVideoWidth();
                this.mRecoderHeight = this.player.getVideoHeight();
            }
            this.mPlayTask = new MoviePlayer.PlayTask(this.player, this);
            Logg.LogI("start play");
        } catch (Exception e) {
            Logg.LogE("Unable to play movie" + e);
            surface.release();
        }
    }

    @Override // com.xiaomi.mediatranscode.MoviePlayer.FrameCallback
    public void onAudioFormat(MediaFormat mediaFormat) {
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(6, mediaFormat));
    }

    @Override // com.xiaomi.mediatranscode.MoviePlayer.FrameCallback
    public void onAudioFrame(MoviePlayer.AudioFrame audioFrame) {
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(7, audioFrame));
    }

    @Override // com.xiaomi.mediatranscode.MoviePlayer.FrameCallback
    public void onVideoFrame(int i) {
        int i2 = this.mlastCount;
        if (i < i2) {
            i = i2 + 1;
        } else {
            this.mlastCount = i;
        }
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(9, Integer.valueOf(i)));
    }

    @Override // com.xiaomi.mediatranscode.MoviePlayer.FrameCallback
    public void onVideoCrop(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.mOriginalDrawer.cropSize(i, i2, i3, i4, i5, i6, i7);
        this.mRecordDrawer.cropSize(i, i2, i3, i4, i5, i6, i7);
    }
}
