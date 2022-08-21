package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes2.dex */
public class GalleryVideoView extends TextureView {
    public AudioAttributes mAudioAttributes;
    public int mAudioFocusType;
    public AudioManager mAudioManager;
    public int mAudioSession;
    public MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    public MediaPlayer.OnCompletionListener mCompletionListener;
    public Context mContext;
    public int mCurrentBufferPercentage;
    public int mCurrentState;
    public int mDefaultHeight;
    public int mDefaultWidth;
    public MediaPlayer.OnErrorListener mErrorListener;
    public long mFileLength;
    public long mFileOffset;
    public String mFilePath;
    public Map<String, String> mHeaders;
    public MediaPlayer.OnInfoListener mInfoListener;
    public MediaPlayer mMediaPlayer;
    public MediaPlayer.OnCompletionListener mOnCompletionListener;
    public MediaPlayer.OnErrorListener mOnErrorListener;
    public MediaPlayer.OnInfoListener mOnInfoListener;
    public MediaPlayer.OnPreparedListener mOnPreparedListener;
    public MediaPlayer.OnPreparedListener mPreparedListener;
    public int mSeekWhenPrepared;
    public MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    public SurfaceTexture mSurfaceTexture;
    public TextureView.SurfaceTextureListener mSurfaceTextureListener;
    public int mTargetState;
    public boolean mTransformApply;
    public Uri mUri;
    public int mVideoHeight;
    public int mVideoWidth;
    public float mVolume;

    public GalleryVideoView(Context context) {
        this(context, null);
    }

    public GalleryVideoView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GalleryVideoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mMediaPlayer = null;
        this.mAudioFocusType = 1;
        this.mVolume = -1.0f;
        this.mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.miui.gallery.widget.GalleryVideoView.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i2, int i3) {
                GalleryVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                GalleryVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                if (GalleryVideoView.this.mVideoWidth == 0 || GalleryVideoView.this.mVideoHeight == 0) {
                    return;
                }
                GalleryVideoView.this.requestLayout();
            }
        };
        this.mPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: com.miui.gallery.widget.GalleryVideoView.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                GalleryVideoView.this.mCurrentState = 2;
                if (GalleryVideoView.this.mOnPreparedListener != null) {
                    GalleryVideoView.this.mOnPreparedListener.onPrepared(GalleryVideoView.this.mMediaPlayer);
                }
                GalleryVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                GalleryVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                int i2 = GalleryVideoView.this.mSeekWhenPrepared;
                if (i2 != 0) {
                    GalleryVideoView.this.seekTo(i2);
                }
                if (GalleryVideoView.this.mTargetState == 3) {
                    GalleryVideoView.this.start();
                }
            }
        };
        this.mCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: com.miui.gallery.widget.GalleryVideoView.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                GalleryVideoView.this.mCurrentState = 5;
                GalleryVideoView.this.mTargetState = 5;
                if (GalleryVideoView.this.mOnCompletionListener != null) {
                    GalleryVideoView.this.mOnCompletionListener.onCompletion(GalleryVideoView.this.mMediaPlayer);
                }
                if (GalleryVideoView.this.mAudioFocusType != 0) {
                    GalleryVideoView.this.mAudioManager.abandonAudioFocus(null);
                }
            }
        };
        this.mInfoListener = new MediaPlayer.OnInfoListener() { // from class: com.miui.gallery.widget.GalleryVideoView.4
            @Override // android.media.MediaPlayer.OnInfoListener
            public boolean onInfo(MediaPlayer mediaPlayer, int i2, int i3) {
                if (GalleryVideoView.this.mOnInfoListener != null) {
                    GalleryVideoView.this.mOnInfoListener.onInfo(mediaPlayer, i2, i3);
                    return true;
                }
                return true;
            }
        };
        this.mErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.miui.gallery.widget.GalleryVideoView.5
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i2, int i3) {
                DefaultLogger.d("GalleryVideoView", "Error: " + i2 + "," + i3);
                GalleryVideoView.this.mCurrentState = -1;
                GalleryVideoView.this.mTargetState = -1;
                if (GalleryVideoView.this.mOnErrorListener != null) {
                    GalleryVideoView.this.mOnErrorListener.onError(GalleryVideoView.this.mMediaPlayer, i2, i3);
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.miui.gallery.widget.GalleryVideoView.6
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i2) {
                GalleryVideoView.this.mCurrentBufferPercentage = i2;
            }
        };
        this.mSurfaceTextureListener = new TextureView.SurfaceTextureListener() { // from class: com.miui.gallery.widget.GalleryVideoView.7
            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i2, int i3) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i2, int i3) {
                GalleryVideoView.this.mSurfaceTexture = surfaceTexture;
                GalleryVideoView.this.openVideo();
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                GalleryVideoView.this.mSurfaceTexture = null;
                GalleryVideoView.this.release(true);
                DefaultLogger.d("GalleryVideoView", "surfaceDestroyed");
                return true;
            }
        };
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        if (Build.VERSION.SDK_INT >= 21) {
            this.mAudioAttributes = new AudioAttributes.Builder().setUsage(1).setContentType(3).build();
        }
        setSurfaceTextureListener(this.mSurfaceTextureListener);
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5 = this.mDefaultWidth;
        int i6 = this.mDefaultHeight;
        int i7 = this.mVideoWidth;
        if (i7 > 0 && (i4 = this.mVideoHeight) > 0) {
            i5 = i7;
            i6 = i4;
        }
        int defaultSize = TextureView.getDefaultSize(i5, i);
        int defaultSize2 = TextureView.getDefaultSize(i6, i2);
        if (i5 > 0 && i6 > 0 && !this.mTransformApply) {
            int mode = View.MeasureSpec.getMode(i);
            int size = View.MeasureSpec.getSize(i);
            int mode2 = View.MeasureSpec.getMode(i2);
            int size2 = View.MeasureSpec.getSize(i2);
            if (mode == 1073741824 && mode2 == 1073741824) {
                int i8 = i5 * size2;
                int i9 = size * i6;
                if (i8 < i9) {
                    defaultSize = i8 / i6;
                    defaultSize2 = size2;
                } else {
                    if (i8 > i9) {
                        defaultSize2 = i9 / i5;
                        defaultSize = size;
                    }
                    defaultSize = size;
                    defaultSize2 = size2;
                }
            } else {
                if (mode == 1073741824) {
                    int i10 = (i6 * size) / i5;
                    if (mode2 != Integer.MIN_VALUE || i10 <= size2) {
                        defaultSize = size;
                        defaultSize2 = i10;
                    }
                    defaultSize = size;
                } else if (mode2 == 1073741824) {
                    int i11 = (i5 * size2) / i6;
                    if (mode != Integer.MIN_VALUE || i11 <= size) {
                        defaultSize2 = size2;
                        defaultSize = i11;
                    }
                    defaultSize = size;
                } else {
                    if (mode2 != Integer.MIN_VALUE || i6 <= size2) {
                        i3 = i5;
                        size2 = i6;
                    } else {
                        i3 = (size2 * i5) / i6;
                    }
                    if (mode != Integer.MIN_VALUE || i3 <= size) {
                        defaultSize = i3;
                    } else {
                        defaultSize2 = (i6 * size) / i5;
                        defaultSize = size;
                    }
                }
                defaultSize2 = size2;
            }
        }
        setMeasuredDimension(defaultSize, defaultSize2);
    }

    @Override // android.view.View
    public CharSequence getAccessibilityClassName() {
        return GalleryVideoView.class.getName();
    }

    @Override // android.view.TextureView
    public void setTransform(Matrix matrix) {
        if (matrix != null && !matrix.isIdentity()) {
            super.setTransform(matrix);
            this.mTransformApply = true;
            return;
        }
        this.mTransformApply = false;
    }

    public void setVolume(float f) {
        MediaPlayer mediaPlayer;
        this.mVolume = f;
        if (f < 0.0f || (mediaPlayer = this.mMediaPlayer) == null) {
            return;
        }
        mediaPlayer.setVolume(f, f);
    }

    public void setVideoPath(String str) {
        setVideoURI(Uri.parse(str));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    public void setVideoURI(Uri uri, Map<String, String> map) {
        this.mUri = uri;
        this.mHeaders = map;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void setVideoFilePath(String str) {
        setVideoFilePath(str, 0L, 576460752303423487L);
    }

    public void setVideoFilePath(String str, long j) {
        setVideoFilePath(str, j, 576460752303423487L);
    }

    public void setVideoFilePath(String str, long j, long j2) {
        this.mFilePath = str;
        this.mFileOffset = j;
        this.mFileLength = j2;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void requestAudioFocus(int i) {
        setAudioFocusRequest(i);
        if (i != 0) {
            this.mAudioManager.requestAudioFocus(null, 3, i);
        }
    }

    public void setAudioFocusRequest(int i) {
        if (i != 0 && i != 1 && i != 2 && i != 3 && i != 4) {
            throw new IllegalArgumentException("Illegal audio focus type " + i);
        }
        this.mAudioFocusType = i;
    }

    public void setAudioAttributes(AudioAttributes audioAttributes) {
        if (audioAttributes == null) {
            throw new IllegalArgumentException("Illegal null AudioAttributes");
        }
        this.mAudioAttributes = audioAttributes;
    }

    public void stopPlayback() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
            this.mAudioManager.abandonAudioFocus(null);
        }
    }

    public final void openVideo() {
        Throwable th;
        if ((this.mUri != null || !TextUtils.isEmpty(this.mFilePath)) && this.mSurfaceTexture != null) {
            release(false);
            int i = this.mAudioFocusType;
            FileInputStream fileInputStream = null;
            if (i != 0) {
                this.mAudioManager.requestAudioFocus(null, 3, i);
            }
            try {
                try {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    this.mMediaPlayer = mediaPlayer;
                    int i2 = this.mAudioSession;
                    if (i2 != 0) {
                        mediaPlayer.setAudioSessionId(i2);
                    } else {
                        this.mAudioSession = mediaPlayer.getAudioSessionId();
                    }
                    this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                    this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                    this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                    this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                    this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                    this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                    this.mCurrentBufferPercentage = 0;
                    Uri uri = this.mUri;
                    if (uri != null) {
                        this.mMediaPlayer.setDataSource(this.mContext, uri, this.mHeaders);
                    } else if (!TextUtils.isEmpty(this.mFilePath)) {
                        FileInputStream fileInputStream2 = new FileInputStream(this.mFilePath);
                        try {
                            this.mMediaPlayer.setDataSource(fileInputStream2.getFD(), this.mFileOffset, this.mFileLength);
                            fileInputStream = fileInputStream2;
                        } catch (IOException e) {
                            e = e;
                            fileInputStream = fileInputStream2;
                            DefaultLogger.w("GalleryVideoView", "Unable to open content: " + this.mUri, e);
                            this.mCurrentState = -1;
                            this.mTargetState = -1;
                            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
                            if (fileInputStream == null) {
                                return;
                            }
                            try {
                                fileInputStream.close();
                                return;
                            } catch (IOException e2) {
                                DefaultLogger.w("GalleryVideoView", "close stream error", e2);
                                return;
                            }
                        } catch (IllegalArgumentException e3) {
                            e = e3;
                            fileInputStream = fileInputStream2;
                            DefaultLogger.w("GalleryVideoView", "Unable to open content: " + this.mUri, e);
                            this.mCurrentState = -1;
                            this.mTargetState = -1;
                            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
                            if (fileInputStream == null) {
                                return;
                            }
                            try {
                                fileInputStream.close();
                                return;
                            } catch (IOException e4) {
                                DefaultLogger.w("GalleryVideoView", "close stream error", e4);
                                return;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            fileInputStream = fileInputStream2;
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e5) {
                                    DefaultLogger.w("GalleryVideoView", "close stream error", e5);
                                }
                            }
                            throw th;
                        }
                    }
                    this.mMediaPlayer.setSurface(new Surface(this.mSurfaceTexture));
                    if (Build.VERSION.SDK_INT >= 21) {
                        this.mMediaPlayer.setAudioAttributes(this.mAudioAttributes);
                    }
                    this.mMediaPlayer.setScreenOnWhilePlaying(true);
                    float f = this.mVolume;
                    if (f >= 0.0f) {
                        this.mMediaPlayer.setVolume(f, f);
                    }
                    this.mMediaPlayer.prepareAsync();
                    this.mCurrentState = 1;
                    if (fileInputStream == null) {
                        return;
                    }
                    try {
                        fileInputStream.close();
                    } catch (IOException e6) {
                        DefaultLogger.w("GalleryVideoView", "close stream error", e6);
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (IOException e7) {
                e = e7;
            } catch (IllegalArgumentException e8) {
                e = e8;
            }
        }
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.mOnCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnInfoListener(MediaPlayer.OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    public final void release(boolean z) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (z) {
                this.mTargetState = 0;
            }
            if (this.mAudioFocusType == 0) {
                return;
            }
            this.mAudioManager.abandonAudioFocus(null);
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int i) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo(i);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = i;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public final boolean isInPlaybackState() {
        int i;
        return (this.mMediaPlayer == null || (i = this.mCurrentState) == -1 || i == 0 || i == 1) ? false : true;
    }

    public int getAudioSessionId() {
        if (this.mAudioSession == 0) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mAudioSession = mediaPlayer.getAudioSessionId();
            mediaPlayer.release();
        }
        return this.mAudioSession;
    }
}
