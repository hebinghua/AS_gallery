package com.miui.gallery.projection;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.projection.IConnectController;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ProjectionVideoController extends RelativeLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, IConnectController.OnMediaPlayListener {
    public IConnectController mConnectControl;
    public boolean mDragging;
    public OnFinishListener mFinishListener;
    public Handler mHandler;
    public ImageView mIvPause;
    public LinearLayout mLayoutQuit;
    public SeekBar mSbSeek;
    public TextView mTvCurPos;
    public TextView mTvDuration;

    /* loaded from: classes2.dex */
    public interface OnFinishListener {
        void onFinish();
    }

    public int getPauseImageResId() {
        return R.drawable.projection_video_pause;
    }

    public int getPlayImageResId() {
        return R.drawable.projection_video_play;
    }

    @Override // com.miui.gallery.projection.IConnectController.OnMediaPlayListener
    public void onLoading() {
    }

    public ProjectionVideoController(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHandler = new Handler() { // from class: com.miui.gallery.projection.ProjectionVideoController.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 100) {
                    int showProgress = ProjectionVideoController.this.showProgress();
                    DefaultLogger.d("RemoteVideoControl", "show progress %s pos %d", Boolean.valueOf(ProjectionVideoController.this.mDragging), Integer.valueOf(showProgress));
                    if (!ProjectionVideoController.this.mDragging && ProjectionVideoController.this.mConnectControl.isPlaying()) {
                        message = obtainMessage(100);
                        removeMessages(100);
                        sendMessageDelayed(message, 1000 - (showProgress % 1000));
                    }
                }
                super.handleMessage(message);
            }
        };
    }

    public void initView() {
        this.mConnectControl = ConnectControllerSingleton.getInstance();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_quit);
        this.mLayoutQuit = linearLayout;
        linearLayout.setOnClickListener(this);
        ImageView imageView = (ImageView) findViewById(R.id.iv_pause);
        this.mIvPause = imageView;
        imageView.setOnClickListener(this);
        this.mTvCurPos = (TextView) findViewById(R.id.tv_cur_time);
        this.mTvDuration = (TextView) findViewById(R.id.tv_duration);
        SeekBar seekBar = (SeekBar) findViewById(R.id.sb_seek);
        this.mSbSeek = seekBar;
        seekBar.setOnSeekBarChangeListener(this);
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.mFinishListener = onFinishListener;
    }

    public final int showProgress() {
        IConnectController iConnectController = this.mConnectControl;
        if (iConnectController == null || this.mDragging) {
            return 0;
        }
        int currentPosition = iConnectController.getCurrentPosition();
        int duration = this.mConnectControl.getDuration();
        updateStatus();
        DefaultLogger.v("RemoteVideoControl", "position %d, duration %d", Integer.valueOf(currentPosition), Integer.valueOf(duration));
        if (duration == -1) {
            return 0;
        }
        SeekBar seekBar = this.mSbSeek;
        if (seekBar != null && duration > 0) {
            long j = (currentPosition * 100) / duration;
            seekBar.setProgress((int) j);
            DefaultLogger.v("RemoteVideoControl", "seek set %d", Long.valueOf(j));
        }
        if (duration == 0) {
            return currentPosition;
        }
        TextView textView = this.mTvDuration;
        if (textView != null) {
            textView.setText(FormatUtil.formatVideoDuration(duration / 1000));
        }
        TextView textView2 = this.mTvCurPos;
        if (textView2 != null) {
            textView2.setText(FormatUtil.formatVideoDuration(currentPosition / 1000));
        }
        return currentPosition;
    }

    public void clearStatus() {
        TextView textView = this.mTvDuration;
        if (textView != null) {
            textView.setText("");
        }
        TextView textView2 = this.mTvCurPos;
        if (textView2 != null) {
            textView2.setText("");
        }
        SeekBar seekBar = this.mSbSeek;
        if (seekBar != null) {
            seekBar.setProgress(0);
        }
        updateStatus();
    }

    public void startPlay(String str, String str2) {
        clearStatus();
        this.mConnectControl.playVideo(str, str2);
        this.mConnectControl.resume();
        this.mConnectControl.registerMediaPlayListener(this);
        this.mHandler.removeMessages(100);
        this.mHandler.sendEmptyMessage(100);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        stopPlay();
        super.onDetachedFromWindow();
    }

    public void stopPlay() {
        this.mConnectControl.stop();
        this.mConnectControl.unregisterMediaPlayListener(this);
        clearStatus();
        this.mHandler.removeMessages(100);
        OnFinishListener onFinishListener = this.mFinishListener;
        if (onFinishListener != null) {
            onFinishListener.onFinish();
        }
    }

    public void updateStatus() {
        if (this.mConnectControl.isPlaying()) {
            this.mIvPause.setImageResource(getPauseImageResId());
            this.mHandler.removeMessages(100);
            this.mHandler.sendEmptyMessage(100);
            return;
        }
        this.mIvPause.setImageResource(getPlayImageResId());
        this.mHandler.removeMessages(100);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mLayoutQuit) {
            stopPlay();
        } else if (view != this.mIvPause) {
        } else {
            if (this.mConnectControl.isPlaying()) {
                this.mConnectControl.pause();
            } else {
                this.mConnectControl.resume();
            }
            updateStatus();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (seekBar != this.mSbSeek || !z) {
            return;
        }
        this.mConnectControl.getCurrentPosition();
        long duration = (this.mConnectControl.getDuration() * i) / 100;
        this.mConnectControl.seekTo((int) duration);
        TextView textView = this.mTvCurPos;
        if (textView == null) {
            return;
        }
        textView.setText(FormatUtil.formatVideoDuration(duration / 1000));
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar == this.mSbSeek) {
            this.mDragging = true;
            this.mConnectControl.pause();
            updateStatus();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == this.mSbSeek) {
            this.mDragging = false;
            this.mConnectControl.resume();
            updateStatus();
        }
    }

    @Override // com.miui.gallery.projection.IConnectController.OnMediaPlayListener
    public void onPlaying() {
        updateStatus();
    }

    @Override // com.miui.gallery.projection.IConnectController.OnMediaPlayListener
    public void onStopped() {
        updateStatus();
        stopPlay();
    }

    @Override // com.miui.gallery.projection.IConnectController.OnMediaPlayListener
    public void onPaused() {
        updateStatus();
    }
}
