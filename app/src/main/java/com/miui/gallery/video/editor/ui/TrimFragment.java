package com.miui.gallery.video.editor.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback;
import com.miui.gallery.video.editor.ui.menu.TrimView;
import com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar;
import com.miui.gallery.video.editor.widget.rangeseekbar.drawable.VideoThumbnailBackgroundDrawable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TrimFragment extends MenuFragment implements VideoEditorRangeSeekBar.OnSeekBarChangeListener {
    public View mCancelView;
    public boolean mHasCliped;
    public View mOkView;
    public int mProgress;
    public VideoEditorRangeSeekBar mRangeSeekBar;
    public TextView mTitleView;
    public TextView mTvVideoTime;
    public int mSavedStartRange = 0;
    public int mSavedEndRange = 0;
    public boolean needResetTrimInfo = false;
    public boolean mIsModified = false;
    public MyStateChangeListener mStateChangeListener = new MyStateChangeListener();

    public static /* synthetic */ void $r8$lambda$paaxohM8fyiLOpQF9XOFldzo1bM(TrimFragment trimFragment, View view) {
        trimFragment.lambda$initListener$1(view);
    }

    public static /* synthetic */ void $r8$lambda$t1MMio4z2m9aXFZ2eTVmoKwdSNU(TrimFragment trimFragment, View view) {
        trimFragment.lambda$initListener$0(view);
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public int getEffectId() {
        return R.id.video_editor_trim;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new TrimView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mCancelView = view.findViewById(R.id.cancel);
        this.mOkView = view.findViewById(R.id.ok);
        TextView textView = (TextView) view.findViewById(R.id.title);
        this.mTitleView = textView;
        textView.setText(this.mContext.getResources().getString(R.string.video_editor_trim));
        VideoEditorRangeSeekBar videoEditorRangeSeekBar = (VideoEditorRangeSeekBar) view.findViewById(R.id.video_editor_trim_range_seek_bar);
        this.mRangeSeekBar = videoEditorRangeSeekBar;
        videoEditorRangeSeekBar.setOnSeekBarChangeListener(this);
        this.mTvVideoTime = (TextView) view.findViewById(R.id.trim_tv_time);
        this.mRangeSeekBar.setTotal(this.mVideoEditor.getVideoTotalTime());
        this.mRangeSeekBar.setMax(this.mVideoEditor.getProjectTotalTime());
        this.mSavedStartRange = this.mRangeSeekBar.getStartRange();
        this.mSavedEndRange = this.mRangeSeekBar.getEndRange();
        this.needResetTrimInfo = true;
        this.mTvVideoTime.setText(getCurrentVideoTime(this.mVideoEditor.getProjectTotalTime()));
        this.mRangeSeekBar.setBitmapProvider(new VideoThumbnailBackgroundDrawable.BitmapProvider() { // from class: com.miui.gallery.video.editor.ui.TrimFragment.1
            {
                TrimFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.widget.rangeseekbar.drawable.VideoThumbnailBackgroundDrawable.BitmapProvider
            public Bitmap getBitmap(int i, int i2) {
                return TrimFragment.this.mVideoEditor.pickThumbnail(i);
            }
        });
        this.mRangeSeekBar.setThumbnailAspectRatio(this.mVideoEditor.getCurrentDisplayRatio());
        this.mVideoEditor.addStateChangeListener(this.mStateChangeListener);
        this.mVideoEditor.setOnVideoThumbnailChangedListener(new VideoEditor.OnVideoThumbnailChangedListener() { // from class: com.miui.gallery.video.editor.ui.TrimFragment.2
            {
                TrimFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnVideoThumbnailChangedListener
            public void onVideoThumbnailChanged() {
                TrimFragment trimFragment = TrimFragment.this;
                if (trimFragment.mVideoEditor == null || trimFragment.mRangeSeekBar == null) {
                    return;
                }
                TrimFragment.this.mRangeSeekBar.setThumbnailAspectRatio(TrimFragment.this.mVideoEditor.getCurrentDisplayRatio());
            }
        });
        updateAutoTrimView();
        initListener();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        this.mRangeSeekBar.setTotal(this.mVideoEditor.getVideoTotalTime());
        this.mRangeSeekBar.setMax(this.mVideoEditor.getProjectTotalTime());
        this.mSavedStartRange = this.mRangeSeekBar.getStartRange();
        this.mSavedEndRange = this.mRangeSeekBar.getEndRange();
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public void onVideoLoadCompleted() {
        VideoEditorRangeSeekBar videoEditorRangeSeekBar;
        super.onVideoLoadCompleted();
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor == null || (videoEditorRangeSeekBar = this.mRangeSeekBar) == null) {
            return;
        }
        videoEditorRangeSeekBar.setTotal(videoEditor.getVideoTotalTime());
        this.mRangeSeekBar.setMax(this.mVideoEditor.getProjectTotalTime());
        this.mSavedEndRange = this.mVideoEditor.getVideoTotalTime();
        this.mSavedStartRange = 0;
        this.mRangeSeekBar.setStartRange(0);
        this.mRangeSeekBar.setEndRange(this.mVideoEditor.getVideoTotalTime());
        this.mRangeSeekBar.lockRangeTo(0, this.mVideoEditor.getVideoTotalTime(), null);
        this.mTvVideoTime.setText(getCurrentVideoTime(this.mVideoEditor.getProjectTotalTime()));
        this.mRangeSeekBar.setStopSlide("00:01".equals(getCurrentVideoTime(this.mVideoEditor.getVideoTotalTime())));
        updateAutoTrimView();
    }

    public /* synthetic */ void lambda$initListener$0(View view) {
        doApply();
    }

    public final void initListener() {
        this.mOkView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.TrimFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TrimFragment.$r8$lambda$t1MMio4z2m9aXFZ2eTVmoKwdSNU(TrimFragment.this, view);
            }
        });
        this.mCancelView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.TrimFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TrimFragment.$r8$lambda$paaxohM8fyiLOpQF9XOFldzo1bM(TrimFragment.this, view);
            }
        });
    }

    public /* synthetic */ void lambda$initListener$1(View view) {
        doCancel();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mVideoEditor.removeStateChangeListener(this.mStateChangeListener);
        this.mRangeSeekBar = null;
        updateAutoTrimView();
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public void onPlayButtonClicked() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor != null) {
            int state = videoEditor.getState();
            if ((state != 0 && state != 2) || !this.mIsModified) {
                return;
            }
            final int startRange = this.mRangeSeekBar.getStartRange();
            int endRange = this.mRangeSeekBar.getEndRange();
            if (startRange < endRange) {
                this.mVideoEditor.setTrimInfo(startRange, endRange);
                this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.TrimFragment.3
                    {
                        TrimFragment.this = this;
                    }

                    @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                    public void onCompleted() {
                        TrimFragment.this.needResetTrimInfo = true;
                        if (TrimFragment.this.mProgress != 0) {
                            TrimFragment trimFragment = TrimFragment.this;
                            trimFragment.mVideoEditor.seek(trimFragment.mProgress - startRange, new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.TrimFragment.3.1
                                {
                                    AnonymousClass3.this = this;
                                }

                                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                                public void onCompleted() {
                                    TrimFragment.this.mVideoEditor.resume();
                                }
                            });
                            return;
                        }
                        TrimFragment.this.mVideoEditor.play();
                    }
                });
                this.mIsModified = false;
                return;
            }
            ToastUtils.makeText(this.mContext, getString(R.string.video_editor_invalid_trim_arguments, 1));
        }
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public List<String> getCurrentEffect() {
        return new ArrayList();
    }

    public boolean doApply() {
        VideoEditorRangeSeekBar videoEditorRangeSeekBar = this.mRangeSeekBar;
        if (videoEditorRangeSeekBar == null || videoEditorRangeSeekBar.isTouching()) {
            return false;
        }
        if (this.mVideoEditor == null) {
            DefaultLogger.d("TrimFragment", "doApply: videoEditor is null.");
            return false;
        }
        final int startRange = this.mRangeSeekBar.getStartRange();
        final int endRange = this.mRangeSeekBar.getEndRange();
        if (startRange < endRange && endRange - startRange > 1000) {
            this.mVideoEditor.setTrimInfo(startRange, endRange);
            this.mRangeSeekBar.lockRangeTo(startRange, endRange, new VideoEditorRangeSeekBar.ISeekbarZooming() { // from class: com.miui.gallery.video.editor.ui.TrimFragment.4
                {
                    TrimFragment.this = this;
                }

                @Override // com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.ISeekbarZooming
                public void onAnimationEnd() {
                    TrimFragment.this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.TrimFragment.4.1
                        {
                            AnonymousClass4.this = this;
                        }

                        @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                        public void onCompleted() {
                            AnonymousClass4 anonymousClass4 = AnonymousClass4.this;
                            TrimFragment trimFragment = TrimFragment.this;
                            if (trimFragment.mVideoEditor == null) {
                                return;
                            }
                            trimFragment.mSavedStartRange = startRange;
                            AnonymousClass4 anonymousClass42 = AnonymousClass4.this;
                            TrimFragment.this.mSavedEndRange = endRange;
                            TrimFragment.this.needResetTrimInfo = true;
                            TrimFragment.this.mIsModified = false;
                            TrimFragment.this.mVideoEditor.play();
                            TrimFragment.this.recordEventWithApply();
                            TrimFragment.this.onExitMode();
                        }
                    });
                }
            });
        } else {
            ToastUtils.makeText(this.mRangeSeekBar.getContext(), String.format(getResources().getString(R.string.video_editor_invalid_trim_arguments), 1));
            onExitMode();
        }
        return true;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public boolean doCancel() {
        if (this.mVideoEditor == null) {
            DefaultLogger.d("TrimFragment", "doCancel: videoEditor is null.");
            return false;
        }
        VideoEditorRangeSeekBar videoEditorRangeSeekBar = this.mRangeSeekBar;
        if (videoEditorRangeSeekBar == null || videoEditorRangeSeekBar.isZooming() || this.mRangeSeekBar.isTouching()) {
            return false;
        }
        if (this.mHasCliped) {
            this.mVideoEditor.setTrimInfo(this.mSavedStartRange, this.mSavedEndRange);
            return this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.TrimFragment.5
                {
                    TrimFragment.this = this;
                }

                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                public void onCompleted() {
                    TrimFragment trimFragment = TrimFragment.this;
                    if (trimFragment.mVideoEditor == null) {
                        return;
                    }
                    trimFragment.needResetTrimInfo = true;
                    TrimFragment.this.mVideoEditor.play();
                    TrimFragment.this.mRangeSeekBar.setStartRange(TrimFragment.this.mSavedStartRange);
                    TrimFragment.this.mRangeSeekBar.setEndRange(TrimFragment.this.mSavedEndRange);
                    TrimFragment.this.mIsModified = false;
                    TrimFragment.this.recordEventWithCancel();
                    TrimFragment.this.onExitMode();
                }
            });
        }
        onExitMode();
        return true;
    }

    @Override // com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.OnSeekBarChangeListener
    public void onProgressChanged(VideoEditorRangeSeekBar videoEditorRangeSeekBar, int i, int i2, boolean z) {
        int endRange = this.mRangeSeekBar.getEndRange() - this.mRangeSeekBar.getStartRange();
        this.mRangeSeekBar.setStopSlide("00:01".equals(getCurrentVideoTime(endRange)));
        if ("00:01".equals(getCurrentVideoTime(endRange))) {
            LinearMotorHelper.performHapticFeedback(this.mRangeSeekBar, LinearMotorHelper.HAPTIC_MESH_NORMAL);
        }
        VideoEditor videoEditor = this.mVideoEditor;
        videoEditor.seek(videoEditor.getVideoStartTime() + i2, null);
        this.mIsModified = true;
        this.mRangeSeekBar.hideProgressBar();
        this.mTvVideoTime.setText(getCurrentVideoTime(endRange));
        this.mHasCliped = true;
        this.mProgress = 0;
    }

    @Override // com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.OnSeekBarChangeListener
    public void onProgressPreview(VideoEditorRangeSeekBar videoEditorRangeSeekBar, int i, int i2, boolean z) {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor != null) {
            int projectTotalTime = (int) ((i2 * this.mVideoEditor.getProjectTotalTime()) / videoEditor.getVideoTotalTime());
            this.mProgress = projectTotalTime;
            this.mVideoEditor.seek(projectTotalTime, null);
            this.mIsModified = true;
        }
    }

    @Override // com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(VideoEditorRangeSeekBar videoEditorRangeSeekBar, final int i, int i2) {
        if (this.needResetTrimInfo) {
            VideoEditor videoEditor = this.mVideoEditor;
            videoEditor.setTrimInfo(0, videoEditor.getVideoTotalTime());
            this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.TrimFragment.6
                {
                    TrimFragment.this = this;
                }

                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                public void onCompleted() {
                    TrimFragment trimFragment = TrimFragment.this;
                    if (trimFragment.mVideoEditor != null) {
                        trimFragment.needResetTrimInfo = false;
                        TrimFragment.this.mIsModified = true;
                        VideoEditor videoEditor2 = TrimFragment.this.mVideoEditor;
                        videoEditor2.seek(videoEditor2.getVideoStartTime() + i == 0 ? TrimFragment.this.mRangeSeekBar.getStartRange() : TrimFragment.this.mRangeSeekBar.getEndRange(), null);
                    }
                }
            });
        }
        recordEventWithEffectChanged();
    }

    @Override // com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.OnSeekBarChangeListener
    public void onTouchSeekBar(boolean z) {
        this.mVideoEditor.setTouchSeekBar(z);
        updatePlayBtnView();
    }

    /* loaded from: classes2.dex */
    public class MyStateChangeListener implements VideoEditor.StateChangeListener {
        public MyStateChangeListener() {
            TrimFragment.this = r1;
        }

        @Override // com.miui.gallery.video.editor.VideoEditor.StateChangeListener
        public void onStateChanged(int i) {
            if (i == 1) {
                TrimFragment.this.mRangeSeekBar.setMax(TrimFragment.this.mVideoEditor.getProjectTotalTime());
                TrimFragment.this.updatePlayBtnView();
            }
        }

        @Override // com.miui.gallery.video.editor.VideoEditor.StateChangeListener
        public void onTimeChanged(int i) {
            TrimFragment.this.mRangeSeekBar.setProgress(i);
            TrimFragment.this.mRangeSeekBar.showProgressBar();
        }
    }

    public final String getCurrentVideoTime(int i) {
        String str;
        String str2;
        int i2 = i / 60000;
        int i3 = (i - ((i2 * 60) * 1000)) / 1000;
        if (i2 >= 10) {
            str = i2 + "";
        } else if (i2 > 0) {
            str = "0" + i2;
        } else {
            str = "00";
        }
        if (i3 >= 10) {
            str2 = str + ":" + i3;
        } else if (i3 >= 1) {
            str2 = str + ":0" + i3;
        } else {
            str2 = str + ":01";
        }
        if (i == 1000) {
            LinearMotorHelper.performHapticFeedback(this.mRangeSeekBar, LinearMotorHelper.HAPTIC_MESH_NORMAL);
        }
        return str2;
    }

    public final void updateAutoTrimView() {
        IVideoEditorListener$IVideoEditorFragmentCallback iVideoEditorListener$IVideoEditorFragmentCallback = this.mCallback;
        if (iVideoEditorListener$IVideoEditorFragmentCallback != null) {
            iVideoEditorListener$IVideoEditorFragmentCallback.updateAutoTrimView();
        }
    }
}
