package com.miui.gallery.vlog.clip.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class VlogTimeView extends FrameLayout {
    public TextView mAllTimeView;
    public ConstraintLayout mConstraintLayout;
    public TextView mCurrentTimeView;
    public View mDivider;
    public Guideline mGuideline;
    public State mState;
    public long mTotalTimeMicros;

    /* loaded from: classes2.dex */
    public enum State {
        SHOW_BOTH,
        SHOW_TOTAL_ONLY,
        SHOW_TOTAL_PRECISE
    }

    public VlogTimeView(Context context) {
        super(context);
        this.mState = State.SHOW_BOTH;
        init(context);
    }

    public VlogTimeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mState = State.SHOW_BOTH;
        init(context);
    }

    public final void init(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        FrameLayout.inflate(context, R$layout.vlog_clip_time_view_layout, this);
        this.mAllTimeView = (TextView) findViewById(R$id.tv_all_time);
        this.mCurrentTimeView = (TextView) findViewById(R$id.tv_time);
        this.mGuideline = (Guideline) findViewById(R$id.guideline);
        this.mDivider = findViewById(R$id.divider);
        this.mConstraintLayout = (ConstraintLayout) findViewById(R$id.root);
    }

    public void updateAllTime(long j) {
        String formatTime;
        this.mTotalTimeMicros = j;
        if (this.mState == State.SHOW_TOTAL_PRECISE) {
            formatTime = VlogUtils.getFormatTimePrecise(j / 1000);
        } else {
            formatTime = VlogUtils.getFormatTime(j / 1000);
        }
        this.mAllTimeView.setText(formatTime);
    }

    public void updateCurrentTime(long j) {
        this.mCurrentTimeView.setText(VlogUtils.getFormatTime(j / 1000));
    }

    public void updateState(State state) {
        if (this.mState == state) {
            return;
        }
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mConstraintLayout);
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$vlog$clip$widget$VlogTimeView$State[state.ordinal()];
        if (i == 1) {
            constraintSet.connect(this.mAllTimeView.getId(), 6, this.mGuideline.getId(), 7);
            constraintSet.connect(this.mAllTimeView.getId(), 3, 0, 3);
            constraintSet.connect(this.mAllTimeView.getId(), 4, 0, 4);
            constraintSet.applyTo(this.mConstraintLayout);
            this.mCurrentTimeView.setVisibility(0);
            this.mDivider.setVisibility(0);
        } else if (i == 2 || i == 3) {
            constraintSet.connect(this.mAllTimeView.getId(), 6, 0, 6);
            constraintSet.connect(this.mAllTimeView.getId(), 7, 0, 7);
            constraintSet.connect(this.mAllTimeView.getId(), 3, 0, 3);
            constraintSet.connect(this.mAllTimeView.getId(), 4, 0, 4);
            constraintSet.applyTo(this.mConstraintLayout);
            this.mCurrentTimeView.setVisibility(8);
            this.mDivider.setVisibility(8);
        }
        this.mState = state;
        updateAllTime(this.mTotalTimeMicros);
    }

    /* renamed from: com.miui.gallery.vlog.clip.widget.VlogTimeView$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$vlog$clip$widget$VlogTimeView$State;

        static {
            int[] iArr = new int[State.values().length];
            $SwitchMap$com$miui$gallery$vlog$clip$widget$VlogTimeView$State = iArr;
            try {
                iArr[State.SHOW_BOTH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$widget$VlogTimeView$State[State.SHOW_TOTAL_ONLY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$widget$VlogTimeView$State[State.SHOW_TOTAL_PRECISE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public State getState() {
        return this.mState;
    }
}
