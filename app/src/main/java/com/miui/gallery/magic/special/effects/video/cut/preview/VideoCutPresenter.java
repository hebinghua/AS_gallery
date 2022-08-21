package com.miui.gallery.magic.special.effects.video.cut.preview;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.RecordSettings;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class VideoCutPresenter extends BasePresenter<VideoCutFragment, VideoCutModel, ICut$VP> implements MediaPlayer.OnCompletionListener {
    public boolean isPause;
    public Uri mFileUri;
    public int[] mFromToPosition;
    public MediaPlayer mMediaPlayer;
    public Surface mSurface;
    public Timer mTimer;
    public TimerTask mTimerTask;
    public String mStrForegroundVideo = "";
    public Handler mHandel = new Handler() { // from class: com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutPresenter.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 1) {
                if (i != 8) {
                    return;
                }
                VideoCutPresenter videoCutPresenter = VideoCutPresenter.this;
                if (videoCutPresenter.isStop || videoCutPresenter.isPause) {
                    return;
                }
                int i2 = VideoCutPresenter.this.mFromToPosition[1];
                VideoCutPresenter.this.getContract().setVideoTime(RecordSettings.getGapTime(VideoCutPresenter.this.mMediaPlayer.getCurrentPosition()), RecordSettings.getGapTime(i2));
                VideoCutPresenter.this.getActivity().event(1, Integer.valueOf(VideoCutPresenter.this.mMediaPlayer.getDuration()));
                if (VideoCutPresenter.this.isPause) {
                    return;
                }
                MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "start play  case: SET_COMPLETE ");
                VideoCutPresenter.this.play(true);
                return;
            }
            VideoCutPresenter videoCutPresenter2 = VideoCutPresenter.this;
            if (videoCutPresenter2.isStop || videoCutPresenter2.isPause) {
                return;
            }
            int i3 = VideoCutPresenter.this.mFromToPosition[1];
            int currentPosition = VideoCutPresenter.this.mMediaPlayer.getCurrentPosition();
            VideoCutPresenter.this.getContract().setVideoTime(RecordSettings.getGapTime(currentPosition), RecordSettings.getGapTime(i3));
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("MagicLogger VideoCutPresenter", "SET_PROGRESS +cu: " + currentPosition);
            VideoCutPresenter.this.getActivity().event(1, Integer.valueOf(currentPosition));
            if (currentPosition < VideoCutPresenter.this.mFromToPosition[1] || VideoCutPresenter.this.isPause) {
                return;
            }
            MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "start play  case: SET_PROGRESS ");
            VideoCutPresenter.this.play(true);
        }
    };
    public boolean isStop = true;

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public VideoCutModel getModelInstance() {
        return new VideoCutModel(this);
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public ICut$VP mo1070initContract() {
        return new ICut$VP() { // from class: com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutPresenter.1
            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void setVideoTime(String str, String str2) {
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onPlayVideo() {
                MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "start play onPlayVideo ");
                VideoCutPresenter.this.play(false);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void initVideoData(Intent intent) {
                VideoCutPresenter videoCutPresenter = VideoCutPresenter.this;
                videoCutPresenter.mStrForegroundVideo = ((VideoCutModel) videoCutPresenter.mModel).getContract().decode(intent.getData());
                VideoCutPresenter.this.mFileUri = intent.getData();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void setSurfaceTextureListener() {
                ((VideoCutFragment) VideoCutPresenter.this.mView.get()).mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() { // from class: com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutPresenter.1.1
                    @Override // android.view.TextureView.SurfaceTextureListener
                    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
                    }

                    @Override // android.view.TextureView.SurfaceTextureListener
                    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                    }

                    @Override // android.view.TextureView.SurfaceTextureListener
                    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                        VideoCutPresenter.this.playVideo();
                    }

                    @Override // android.view.TextureView.SurfaceTextureListener
                    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                        if (VideoCutPresenter.this.mSurface != null) {
                            VideoCutPresenter.this.mSurface.release();
                            return false;
                        }
                        return false;
                    }
                });
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public String getVideoPath() {
                return VideoCutPresenter.this.mStrForegroundVideo;
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public int getTotalTime() {
                return VideoCutPresenter.this.mMediaPlayer.getDuration();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void seekTo(long j, boolean z) {
                if (!VideoCutPresenter.this.mMediaPlayer.isPlaying() || z) {
                    VideoCutPresenter.this.mySeekTo((int) j);
                }
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onStopTrackingTouch() {
                try {
                    if (VideoCutPresenter.this.mMediaPlayer == null || VideoCutPresenter.this.mMediaPlayer.isPlaying()) {
                        return;
                    }
                    MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "start onStopTrackingTouch  ");
                    VideoCutPresenter.this.mMediaPlayer.start();
                    VideoCutPresenter.this.isPause = false;
                } catch (IllegalStateException unused) {
                }
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void stop() {
                if (VideoCutPresenter.this.mMediaPlayer == null) {
                    return;
                }
                VideoCutPresenter.this.mMediaPlayer.stop();
                VideoCutPresenter.this.mMediaPlayer.reset();
                VideoCutPresenter.this.mMediaPlayer.release();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onPause() {
                MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "pause  @Override onPause ");
                if (VideoCutPresenter.this.mMediaPlayer != null && VideoCutPresenter.this.mMediaPlayer.isPlaying()) {
                    VideoCutPresenter.this.mMediaPlayer.pause();
                }
                VideoCutPresenter.this.isPause = true;
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onResume() {
                MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "start  @Override onResume ");
                if (VideoCutPresenter.this.mMediaPlayer != null && !VideoCutPresenter.this.mMediaPlayer.isPlaying()) {
                    VideoCutPresenter.this.mMediaPlayer.start();
                }
                VideoCutPresenter.this.isPause = false;
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onRePlayVideo() {
                MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "start play onRePlayVideo ");
                VideoCutPresenter.this.play(true);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void pauseVideo() {
                if (VideoCutPresenter.this.mMediaPlayer != null && VideoCutPresenter.this.mMediaPlayer.isPlaying()) {
                    onPlayVideo();
                }
            }
        };
    }

    public void changeVideoSize() {
        float f;
        float f2;
        int videoWidth = this.mMediaPlayer.getVideoWidth();
        float max = Math.max(videoWidth / ((VideoCutFragment) this.mView.get()).mTextureView.getWidth(), this.mMediaPlayer.getVideoHeight() / ((VideoCutFragment) this.mView.get()).mTextureView.getHeight());
        ((VideoCutFragment) this.mView.get()).mTextureView.setLayoutParams(new RelativeLayout.LayoutParams((int) Math.ceil(f / max), (int) Math.ceil(f2 / max)));
    }

    public final void play(boolean z) {
        if (this.mMediaPlayer == null) {
            return;
        }
        this.mFromToPosition = (int[]) getActivity().event(7, Integer.valueOf(this.mMediaPlayer.getDuration()));
        if (this.mMediaPlayer.isPlaying() && !z) {
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("MagicLogger VideoCutPresenter", "pause play method " + Arrays.toString(this.mFromToPosition));
            this.mMediaPlayer.pause();
        } else {
            MagicLog magicLog2 = MagicLog.INSTANCE;
            magicLog2.showLog("MagicLogger VideoCutPresenter", "start play method " + Arrays.toString(this.mFromToPosition));
            this.mMediaPlayer.start();
            this.isPause = false;
            mySeekTo(this.mFromToPosition[0]);
        }
        if (!this.isStop) {
            return;
        }
        startTimer();
    }

    public final void mySeekTo(int i) {
        if (this.mMediaPlayer != null) {
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("MagicLogger VideoCutPresenter", "mySeekTo:   " + i);
            this.mMediaPlayer.seekTo((long) i, 3);
        }
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void unBindView() {
        Handler handler = this.mHandel;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        stopTimer();
        getContract().stop();
        super.unBindView();
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.mHandel.sendEmptyMessage(8);
    }

    public final void startTimer() {
        TimerTask timerTask;
        if (this.mTimer == null) {
            this.mTimer = new Timer();
        }
        if (this.mTimerTask == null) {
            this.mTimerTask = new TimerTask() { // from class: com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutPresenter.3
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (VideoCutPresenter.this.mMediaPlayer != null && !VideoCutPresenter.this.isPause) {
                        VideoCutPresenter.this.mHandel.sendEmptyMessage(1);
                    }
                }
            };
        }
        Timer timer = this.mTimer;
        if (timer == null || (timerTask = this.mTimerTask) == null) {
            return;
        }
        timer.schedule(timerTask, 100L, 100L);
        this.isStop = false;
    }

    public final void stopTimer() {
        this.isStop = true;
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
        TimerTask timerTask = this.mTimerTask;
        if (timerTask != null) {
            timerTask.cancel();
            this.mTimerTask = null;
        }
    }

    public final void playVideo() {
        this.mSurface = new Surface(((VideoCutFragment) this.mView.get()).mTextureView.getSurfaceTexture());
        initMediaPlayer();
    }

    public final void initMediaPlayer() {
        if (this.mMediaPlayer == null) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mMediaPlayer = mediaPlayer;
            try {
                mediaPlayer.setDataSource(getActivity(), this.mFileUri);
                this.mMediaPlayer.prepareAsync();
                this.mMediaPlayer.setOnCompletionListener(this);
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                Surface surface = this.mSurface;
                if (surface != null) {
                    surface.release();
                }
            }
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutPresenter.4
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(final MediaPlayer mediaPlayer2) {
                    MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "mediaPlay onPrepared ");
                    VideoCutPresenter.this.mHandel.postDelayed(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutPresenter.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoCutPresenter.this.changeVideoSize();
                            VideoCutPresenter.this.getActivity().event(5, new Size(mediaPlayer2.getVideoWidth(), mediaPlayer2.getVideoHeight()));
                            MagicLog.INSTANCE.showLog("MagicLogger VideoCutPresenter", "start play  mMediaPlayer--> onPrepared ");
                            VideoCutPresenter.this.play(true);
                        }
                    }, 200L);
                }
            });
        }
        this.mMediaPlayer.setSurface(this.mSurface);
    }
}
