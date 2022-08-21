package com.miui.gallery.miplay;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.ui.MiplayVolumeControlLayout;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.VideoSeekBar;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;

/* loaded from: classes2.dex */
public class GalleryMiplayControlFragment extends GalleryFragment implements View.OnClickListener {
    public TextView mCurPos;
    public long mCurProgress;
    public volatile boolean mIsVideoEnd;
    public final MediaMetaData mMediaMetaData;
    public GalleryMiPlayManager mMiPlayManager;
    public ImageButton mPlayPauseBtn;
    public View mRootView;
    public VideoSeekBar mVideoSeekbar;
    public MiplayVolumeControlLayout mVolumeControl;
    public boolean mIsPlayVideo = true;
    public volatile boolean mNeedNotifyPositionChange = true;

    public static /* synthetic */ void $r8$lambda$OEGLhAPeBRC4JlIEGPe33wdUGTU(GalleryMiplayControlFragment galleryMiplayControlFragment, int i) {
        galleryMiplayControlFragment.lambda$onInflateView$0(i);
    }

    public GalleryMiplayControlFragment(MediaMetaData mediaMetaData) {
        this.mMediaMetaData = mediaMetaData;
        this.mCurProgress = mediaMetaData.getPosition();
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GalleryMiPlayManager galleryMiPlayManager = GalleryMiPlayManager.getInstance();
        this.mMiPlayManager = galleryMiPlayManager;
        galleryMiPlayManager.getVolume();
        this.mMiPlayManager.registerMiplayStatusListener(new MiplayStatusListener() { // from class: com.miui.gallery.miplay.GalleryMiplayControlFragment.1
            {
                GalleryMiplayControlFragment.this = this;
            }

            @Override // com.miui.gallery.miplay.MiplayStatusListener
            public void onStopPlay() {
                DefaultLogger.d("GalleryMiplayControlFragment", "stop play by user");
                GalleryMiplayControlFragment.this.stopPlay(false);
            }

            @Override // com.miui.gallery.miplay.MiplayStatusListener
            public void onVideoEnd() {
                DefaultLogger.d("GalleryMiplayControlFragment", "video end");
                GalleryMiplayControlFragment.this.mIsVideoEnd = true;
                GalleryMiplayControlFragment.this.mVideoSeekbar.setProgress((int) GalleryMiplayControlFragment.this.mMediaMetaData.getDuration());
                GalleryMiplayControlFragment.this.stopPlay(false);
            }

            @Override // com.miui.gallery.miplay.MiplayStatusListener
            public void notifyPositionChange(long j) {
                DefaultLogger.d("GalleryMiplayControlFragment", "notifyPositionChange: pos->%s, mNeedNotifyPositionChange->%s", Long.valueOf(j), Boolean.valueOf(GalleryMiplayControlFragment.this.mNeedNotifyPositionChange));
                if (!GalleryMiplayControlFragment.this.mNeedNotifyPositionChange || GalleryMiplayControlFragment.this.mVideoSeekbar == null) {
                    return;
                }
                GalleryMiplayControlFragment.this.mVideoSeekbar.setProgress((int) j);
            }

            @Override // com.miui.gallery.miplay.MiplayStatusListener
            public void notifySeekDone() {
                GalleryMiplayControlFragment.this.mNeedNotifyPositionChange = true;
            }

            @Override // com.miui.gallery.miplay.MiplayStatusListener
            public void onResume() {
                GalleryMiplayControlFragment.this.refreshPlayStatus(true);
            }

            @Override // com.miui.gallery.miplay.MiplayStatusListener
            public void onPause() {
                GalleryMiplayControlFragment.this.refreshPlayStatus(false);
            }

            @Override // com.miui.gallery.miplay.MiplayStatusListener
            public void onVolumeChange(long j) {
                GalleryMiplayControlFragment.this.setVolume((int) j, false);
            }
        });
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.gallery_miplay_layout, viewGroup, false);
        this.mRootView = inflate;
        this.mPlayPauseBtn = (ImageButton) this.mRootView.findViewById(R.id.miplay_play_pause_btn);
        this.mVolumeControl = (MiplayVolumeControlLayout) this.mRootView.findViewById(R.id.miplay_volume_control);
        this.mVideoSeekbar = (VideoSeekBar) this.mRootView.findViewById(R.id.miplay_video_seekbar);
        this.mCurPos = (TextView) this.mRootView.findViewById(R.id.miplay_seekbar_cur_progress);
        TextView textView = (TextView) this.mRootView.findViewById(R.id.miplay_seekbar_total_progress);
        ((Button) inflate.findViewById(R.id.miplay_stop_video)).setOnClickListener(this);
        this.mPlayPauseBtn.setOnClickListener(this);
        this.mRootView.setOnClickListener(this);
        this.mVolumeControl.setVolumeChangeListener(new MiplayVolumeControlLayout.MiplayVolumeChangeListener() { // from class: com.miui.gallery.miplay.GalleryMiplayControlFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.MiplayVolumeControlLayout.MiplayVolumeChangeListener
            public final void onVolumeChange(int i) {
                GalleryMiplayControlFragment.$r8$lambda$OEGLhAPeBRC4JlIEGPe33wdUGTU(GalleryMiplayControlFragment.this, i);
            }
        });
        this.mVideoSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.miui.gallery.miplay.GalleryMiplayControlFragment.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            {
                GalleryMiplayControlFragment.this = this;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                DefaultLogger.d("GalleryMiplayControlFragment", "onProgressChanged:isTriggerByUser->%s, mNeedNotifyPositionChange->%s,", Boolean.valueOf(z), Boolean.valueOf(GalleryMiplayControlFragment.this.mNeedNotifyPositionChange));
                if (z) {
                    GalleryMiplayControlFragment.this.mNeedNotifyPositionChange = false;
                    if (GalleryMiplayControlFragment.this.mMiPlayManager != null) {
                        DefaultLogger.d("GalleryMiplayControlFragment", "notify tv seek to->%s", Integer.valueOf(i));
                        GalleryMiplayControlFragment.this.mMiPlayManager.seek(i);
                    }
                }
                GalleryMiplayControlFragment.this.mCurProgress = i;
                GalleryMiplayControlFragment.this.mCurPos.setText(FormatUtil.formatVideoDuration(i / 1000));
                DefaultLogger.d("GalleryMiplayControlFragment", "progress->%s, mCurProgress->%s", Integer.valueOf(i), Long.valueOf(GalleryMiplayControlFragment.this.mCurProgress));
            }
        });
        MediaMetaData mediaMetaData = this.mMediaMetaData;
        if (mediaMetaData != null) {
            textView.setText(FormatUtil.formatVideoDuration(mediaMetaData.getDuration() / 1000));
            DefaultLogger.d("GalleryMiplayControlFragment", "initial position->%s, total->%s", Long.valueOf(this.mCurProgress), Long.valueOf(this.mMediaMetaData.getDuration()));
            if (this.mMediaMetaData.getDuration() != 0) {
                this.mVideoSeekbar.setMax((int) this.mMediaMetaData.getDuration());
                this.mVideoSeekbar.setProgress((int) this.mCurProgress);
            }
            if (this.mCurProgress == 0) {
                this.mCurPos.setText(FormatUtil.formatVideoDuration(0L));
            }
        }
        refreshView(getResources().getConfiguration());
        return this.mRootView;
    }

    public /* synthetic */ void lambda$onInflateView$0(int i) {
        this.mMiPlayManager.setVolume(i);
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        refreshView(configuration);
    }

    public final void refreshView(Configuration configuration) {
        View view = this.mRootView;
        if (view != null) {
            boolean z = configuration.orientation == 2;
            view.setBackground(getResources().getDrawable(R.drawable.miplay_bg));
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) this.mRootView);
            if (z) {
                constraintSet.connect(R.id.miplay_icon_container, 4, this.mPlayPauseBtn.getId(), 3);
                constraintSet.setVerticalBias(R.id.miplay_icon_container, 0.5f);
            } else {
                constraintSet.connect(R.id.miplay_icon_container, 4, 0, 4);
                constraintSet.setVerticalBias(R.id.miplay_icon_container, 0.295f);
            }
            int id = this.mPlayPauseBtn.getId();
            Resources resources = getResources();
            int i = R.dimen.miplay_btn_margin_start_end_land;
            constraintSet.setMargin(id, 6, resources.getDimensionPixelSize(z ? R.dimen.miplay_btn_margin_start_end_land : R.dimen.miplay_btn_margin_start_end));
            int id2 = this.mVolumeControl.getId();
            Resources resources2 = getResources();
            if (!z) {
                i = R.dimen.miplay_btn_margin_start_end;
            }
            constraintSet.setMargin(id2, 7, resources2.getDimensionPixelSize(i));
            constraintSet.setMargin(this.mCurPos.getId(), 6, 50);
            constraintSet.applyTo((ConstraintLayout) this.mRootView);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mRootView == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.miplay_play_pause_btn) {
            DefaultLogger.d("GalleryMiplayControlFragment", "start or pause video:%s", Boolean.valueOf(this.mIsPlayVideo));
            refreshPlayStatus(!this.mIsPlayVideo);
            this.mMiPlayManager.changePlayStatus(!this.mIsPlayVideo ? 1 : 0);
        } else if (id == R.id.miplay_stop_video) {
            stopPlay(true);
        } else {
            hideVolumeSeekbar();
        }
    }

    public final void refreshPlayStatus(boolean z) {
        this.mIsPlayVideo = z;
        if (this.mPlayPauseBtn == null || getContext() == null) {
            return;
        }
        this.mPlayPauseBtn.setImageResource(this.mIsPlayVideo ? R.drawable.ic_miplay_pause : R.drawable.ic_miplay_play);
        this.mPlayPauseBtn.setContentDescription(getString(this.mIsPlayVideo ? R.string.miplay_pause_video : R.string.miplay_play_video));
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mMiPlayManager.registerMiplayStatusListener(null);
        super.onDestroy();
    }

    public final void hideVolumeSeekbar() {
        MiplayVolumeControlLayout miplayVolumeControlLayout = this.mVolumeControl;
        if (miplayVolumeControlLayout != null) {
            miplayVolumeControlLayout.collapse();
        }
    }

    public final void setVolume(int i, boolean z) {
        MiplayVolumeControlLayout miplayVolumeControlLayout = this.mVolumeControl;
        if (miplayVolumeControlLayout != null) {
            miplayVolumeControlLayout.setVolume(i, false, z);
        }
    }

    public void onVolumeBtnKeyDown(boolean z) {
        MiplayVolumeControlLayout miplayVolumeControlLayout = this.mVolumeControl;
        if (miplayVolumeControlLayout != null) {
            miplayVolumeControlLayout.onVolumeBtnKeyDown(z);
        }
    }

    public void onVolumeBtnKeyUp() {
        MiplayVolumeControlLayout miplayVolumeControlLayout = this.mVolumeControl;
        if (miplayVolumeControlLayout != null) {
            miplayVolumeControlLayout.onVolumeBtnKeyUp();
        }
    }

    public void stopPlay(boolean z) {
        DefaultLogger.d("GalleryMiplayControlFragment", "stop play video: isLocal:->%s", Boolean.valueOf(z));
        if (z) {
            DefaultLogger.d("GalleryMiplayControlFragment", "notify sdk to stop");
            this.mMiPlayManager.stop();
        }
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        DefaultLogger.d("GalleryMiplayControlFragment", "set result: mCurProgress->%s, validateProgress->%s, isVideoEnd->%s", Long.valueOf(this.mCurProgress), Long.valueOf(validProgress()), Boolean.valueOf(this.mIsVideoEnd));
        Intent intent = new Intent();
        intent.putExtra("video_miplay_progress", validProgress());
        intent.putExtra("video_miplay_end", this.mIsVideoEnd);
        getActivity().setResult(-1, intent);
        getActivity().finish();
    }

    public final long validProgress() {
        long j = this.mCurProgress;
        if (j < 0) {
            this.mCurProgress = 0L;
        } else if (j > this.mMediaMetaData.getDuration()) {
            this.mCurProgress = this.mMediaMetaData.getDuration();
        }
        return this.mCurProgress;
    }

    public void onBackPressed() {
        DefaultLogger.d("GalleryMiplayControlFragment", "onBackPressed");
        stopPlay(true);
    }
}
