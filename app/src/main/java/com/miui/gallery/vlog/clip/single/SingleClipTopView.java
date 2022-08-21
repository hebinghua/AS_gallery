package com.miui.gallery.vlog.clip.single;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.clip.speed.Speed;
import com.miui.gallery.vlog.clip.speed.SpeedPickerView;
import com.miui.gallery.vlog.clip.speed.SpeedView;
import com.miui.gallery.vlog.clip.widget.VlogTimeView;

/* loaded from: classes2.dex */
public class SingleClipTopView extends FrameLayout implements View.OnClickListener, SpeedPickerView.SpeedCallback {
    public boolean mIsPlayIconShowing;
    public Listener mListener;
    public ImageView mPlayIv;
    public FrameLayout mPlayLayout;
    public SpeedPickerView mSpeedPicker;
    public SpeedView mSpeedView;
    public VlogTimeView mTimeView;

    /* loaded from: classes2.dex */
    public interface Listener {
        void onPauseClick();

        void onPlayClick();

        void onSpeedClick();

        void onSpeedSelect(Speed speed);
    }

    public SingleClipTopView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        View inflate = FrameLayout.inflate(context, R$layout.vlog_single_clip_top_menu_layout, this);
        this.mPlayLayout = (FrameLayout) inflate.findViewById(R$id.play_layout);
        this.mSpeedView = (SpeedView) inflate.findViewById(R$id.speed_view);
        this.mPlayIv = (ImageView) inflate.findViewById(R$id.play_view);
        this.mTimeView = (VlogTimeView) inflate.findViewById(R$id.time_view);
        this.mSpeedPicker = (SpeedPickerView) inflate.findViewById(R$id.speed_picker);
        this.mPlayLayout.setOnClickListener(this);
        this.mSpeedView.setOnClickListener(this);
        this.mSpeedPicker.setSpeedCallback(this);
        this.mIsPlayIconShowing = true;
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build();
        FolmeUtil.setCustomTouchAnim(this.mPlayLayout, build, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mSpeedView, build, null, null, true);
    }

    public void expandSpeedView() {
        this.mPlayLayout.setVisibility(8);
        this.mSpeedView.setVisibility(8);
        this.mTimeView.setVisibility(8);
        this.mSpeedPicker.setVisibility(0);
    }

    public void collapseSpeedView() {
        this.mPlayLayout.setVisibility(0);
        this.mSpeedView.setVisibility(0);
        this.mTimeView.setVisibility(0);
        this.mSpeedPicker.setVisibility(8);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void setCurrentTime(long j) {
        this.mTimeView.updateCurrentTime(j);
    }

    public void setTimeViewState(VlogTimeView.State state) {
        this.mTimeView.updateState(state);
    }

    public void setTotalTime(long j) {
        this.mTimeView.updateAllTime(j);
    }

    public void showPlay() {
        this.mPlayIv.setSelected(false);
        this.mIsPlayIconShowing = true;
    }

    public void showPause() {
        this.mPlayIv.setSelected(true);
        this.mIsPlayIconShowing = false;
    }

    public final void onPlayClick() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onPlayClick();
            this.mPlayLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_pause));
        }
    }

    public final void onPauseClick() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onPauseClick();
            this.mPlayLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_play));
        }
    }

    public final void onSpeedClick() {
        expandSpeedView();
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSpeedClick();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mPlayLayout) {
            if (this.mIsPlayIconShowing) {
                onPlayClick();
            } else {
                onPauseClick();
            }
        } else if (view != this.mSpeedView) {
        } else {
            onSpeedClick();
        }
    }

    public VlogTimeView.State getTimeViewState() {
        return this.mTimeView.getState();
    }

    @Override // com.miui.gallery.vlog.clip.speed.SpeedPickerView.SpeedCallback
    public void onSpeedSelect(Speed speed) {
        collapseSpeedView();
        this.mSpeedView.setSpeed(speed);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSpeedSelect(speed);
        }
    }

    @Override // com.miui.gallery.vlog.clip.speed.SpeedPickerView.SpeedCallback
    public void onSpeedReselect(Speed speed) {
        collapseSpeedView();
    }

    public void setSpeedPickerItemEnables(boolean[] zArr) {
        this.mSpeedPicker.setItemEnables(zArr);
    }

    public double[] getSupportSpeeds() {
        return this.mSpeedPicker.getSupportSpeeds();
    }

    public boolean isSpeedViewExpand() {
        return this.mSpeedPicker.getVisibility() == 0;
    }
}
