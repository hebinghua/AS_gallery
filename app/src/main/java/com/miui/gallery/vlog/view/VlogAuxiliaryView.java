package com.miui.gallery.vlog.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.util.BaseScreenUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class VlogAuxiliaryView extends ConstraintLayout {
    public FrameLayout mAudioLayout;
    public Guideline mAudioLayoutLine;
    public Context mContext;
    public Guideline mDisplayOperationGuideLine;
    public FrameLayout mDisplayOperationLayout;
    public FrameLayout mRatioLayout;
    public Guideline mRatioLayoutLine;
    public FrameLayout mVideoSizeLayout;
    public Guideline mVideoSizeLayoutLine;
    public FrameLayout mVideoTimeLayout;
    public Guideline mVideoTimeLayoutLine;
    public HashMap<String, View> mViewHashMap;

    public VlogAuxiliaryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mViewHashMap = new HashMap<>();
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        ViewGroup.inflate(context, R$layout.vlog_auxiliary_view_layout, this);
        this.mVideoSizeLayout = (FrameLayout) findViewById(R$id.video_size_layout);
        this.mVideoTimeLayout = (FrameLayout) findViewById(R$id.video_time_layout);
        this.mRatioLayout = (FrameLayout) findViewById(R$id.video_float_ratio_layout);
        this.mAudioLayout = (FrameLayout) findViewById(R$id.video_float_audio_layout);
        this.mDisplayOperationLayout = (FrameLayout) findViewById(R$id.display_operation_layout);
        this.mVideoSizeLayoutLine = (Guideline) findViewById(R$id.video_size_layout_line);
        this.mVideoTimeLayoutLine = (Guideline) findViewById(R$id.video_time_layout_line);
        this.mRatioLayoutLine = (Guideline) findViewById(R$id.video_float_ratio_line);
        this.mAudioLayoutLine = (Guideline) findViewById(R$id.video_float_audio_line);
        this.mDisplayOperationGuideLine = (Guideline) findViewById(R$id.display_operation_guide_line);
    }

    public void updateTimeView(View view, boolean z) {
        if (view == null) {
            DefaultLogger.d("VlogAuxiliaryView", "updateSpeedXView: view is null.");
            return;
        }
        String valueOf = String.valueOf(view.hashCode());
        if (z && !this.mViewHashMap.containsKey(valueOf)) {
            this.mVideoTimeLayoutLine.setGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.vlog_video_view_gap_bottom));
            this.mViewHashMap.put(valueOf, view);
            this.mVideoTimeLayout.addView(view);
        } else if (z || !this.mViewHashMap.containsKey(valueOf)) {
        } else {
            this.mViewHashMap.remove(valueOf);
            this.mVideoTimeLayout.removeView(view);
            this.mVideoTimeLayoutLine.setGuidelineEnd(0);
        }
    }

    public void updateTextEditorView(View view, boolean z) {
        if (view == null) {
            DefaultLogger.d("VlogAuxiliaryView", "updateTextEditorView: view is null.");
            return;
        }
        String valueOf = String.valueOf(view.hashCode());
        if (z && !this.mViewHashMap.containsKey(valueOf)) {
            this.mVideoSizeLayoutLine.setGuidelineBegin(0);
            this.mViewHashMap.put(valueOf, view);
            this.mVideoSizeLayout.addView(view, 0);
        } else if (z || !this.mViewHashMap.containsKey(valueOf)) {
        } else {
            this.mViewHashMap.remove(valueOf);
            this.mVideoSizeLayout.removeView(view);
        }
    }

    public void updateCaptionClearView(View view, boolean z) {
        if (view == null) {
            DefaultLogger.d("VlogAuxiliaryView", "updateCaptionClearView: view is null.");
            return;
        }
        String valueOf = String.valueOf(view.hashCode());
        if (z && !this.mViewHashMap.containsKey(valueOf)) {
            this.mRatioLayoutLine.setGuidelineBegin(VlogUtils.getDimensionPixelSize(BaseScreenUtil.isFullScreenGestureNav(this.mContext) ? R$dimen.vlog_ratio_view_line_begin : R$dimen.vlog_ratio_view_has_nav_line_begin));
            this.mViewHashMap.put(valueOf, view);
            this.mRatioLayout.addView(view);
        } else if (z || !this.mViewHashMap.containsKey(valueOf)) {
        } else {
            this.mViewHashMap.remove(valueOf);
            this.mRatioLayout.removeView(view);
        }
    }

    public void updateDisplayOperationView(View view, boolean z) {
        if (view == null) {
            DefaultLogger.d("VlogAuxiliaryView", "updateDisplayOperationView: view is null.");
            return;
        }
        String valueOf = String.valueOf(view.hashCode());
        if (z && !this.mViewHashMap.containsKey(valueOf)) {
            this.mDisplayOperationGuideLine.setGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.vlog_menu_common_operation_height) + getResources().getDimensionPixelSize(R$dimen.vlog_display_operation_margin_bottom));
            this.mViewHashMap.put(valueOf, view);
            this.mDisplayOperationLayout.addView(view);
        } else if (z || !this.mViewHashMap.containsKey(valueOf)) {
        } else {
            this.mViewHashMap.remove(valueOf);
            this.mDisplayOperationLayout.removeView(view);
            this.mDisplayOperationGuideLine.setGuidelineEnd(0);
        }
    }
}
