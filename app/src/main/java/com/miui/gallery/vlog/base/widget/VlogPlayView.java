package com.miui.gallery.vlog.base.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.view.VlogVideoProgressBar;

/* loaded from: classes2.dex */
public class VlogPlayView extends RelativeLayout {
    public static int LEFT = 0;
    public static int RIGHT = 1;
    public int LAYOUT;
    public float downX;
    public float dx;
    public Context mContext;
    public DisplayView mDisplayView;
    public boolean mEnablePlayProgress;
    public IProgress mIProgress;
    public float mPercent;
    public int mScaledTouchSlop;
    public View.OnTouchListener mTouchListener;
    public VlogVideoProgressBar mVlogVideoProgressBar;
    public float moveX;
    public float upX;

    /* loaded from: classes2.dex */
    public interface IProgress {
        void onPlayProgressChanging(int i, float f);

        void onPlayProgressEndChanged();

        void onPlayProgressStartChanged();
    }

    public static /* synthetic */ boolean $r8$lambda$wG2sW9QsOH44TXvKk8mqvqWzAKM(VlogPlayView vlogPlayView, View view, MotionEvent motionEvent) {
        return vlogPlayView.lambda$new$0(view, motionEvent);
    }

    public VlogPlayView(Context context) {
        super(context);
        this.downX = 0.0f;
        this.moveX = 0.0f;
        this.dx = 0.0f;
        this.LAYOUT = -1;
        this.upX = 0.0f;
        this.mEnablePlayProgress = true;
        this.mTouchListener = new View.OnTouchListener() { // from class: com.miui.gallery.vlog.base.widget.VlogPlayView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return VlogPlayView.$r8$lambda$wG2sW9QsOH44TXvKk8mqvqWzAKM(VlogPlayView.this, view, motionEvent);
            }
        };
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        RelativeLayout.inflate(context, R$layout.vlog_play_view_layoutt, this);
        this.mVlogVideoProgressBar = (VlogVideoProgressBar) findViewById(R$id.progress_bar);
        this.mDisplayView = (DisplayView) findViewById(R$id.display_view);
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mDisplayView.setOnTouchListener(this.mTouchListener);
        int systemWindowInsetTop = ViewCompat.getSystemWindowInsetTop(this);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.vlog_video_view_gap_top);
        if (systemWindowInsetTop != 0) {
            dimensionPixelSize -= systemWindowInsetTop;
        }
        ((ViewGroup.MarginLayoutParams) this.mDisplayView.getLayoutParams()).topMargin = dimensionPixelSize;
        int dimensionPixelSize2 = SystemUiUtil.isWaterFallScreen() ? getResources().getDimensionPixelSize(com.miui.gallery.editor.R$dimen.editor_waterfall_screen_horizontal_protect_size) : 0;
        setPadding(dimensionPixelSize2, 0, dimensionPixelSize2, 0);
    }

    public void setIProgress(IProgress iProgress) {
        this.mIProgress = iProgress;
    }

    public DisplayView getDisplayView() {
        DisplayView displayView = this.mDisplayView;
        if (displayView == null) {
            return null;
        }
        return displayView;
    }

    public void updatePlayProgress(float f) {
        this.mVlogVideoProgressBar.setProgress(f);
    }

    public void setPlayProgressEnable(boolean z) {
        this.mEnablePlayProgress = z;
    }

    public void showProgressView() {
        this.mVlogVideoProgressBar.setVisibility(0);
    }

    public void hideProgressView() {
        this.mVlogVideoProgressBar.setVisibility(8);
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x0013, code lost:
        if (r4 != 3) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public /* synthetic */ boolean lambda$new$0(android.view.View r4, android.view.MotionEvent r5) {
        /*
            r3 = this;
            boolean r4 = r3.mEnablePlayProgress
            r0 = 0
            if (r4 != 0) goto L6
            return r0
        L6:
            int r4 = r5.getAction()
            r1 = 1
            if (r4 == 0) goto L72
            if (r4 == r1) goto L5a
            r2 = 2
            if (r4 == r2) goto L16
            r2 = 3
            if (r4 == r2) goto L5a
            goto L84
        L16:
            float r4 = r5.getX()
            r3.moveX = r4
            float r5 = r3.downX
            float r4 = r4 - r5
            r3.dx = r4
            float r4 = java.lang.Math.abs(r4)
            int r5 = r3.mScaledTouchSlop
            float r5 = (float) r5
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto L84
            com.miui.gallery.vlog.base.widget.DisplayView r4 = r3.mDisplayView
            int r4 = r4.getWidth()
            if (r4 <= 0) goto L84
            float r4 = r3.dx
            float r4 = java.lang.Math.abs(r4)
            com.miui.gallery.vlog.base.widget.DisplayView r5 = r3.mDisplayView
            int r5 = r5.getWidth()
            float r5 = (float) r5
            float r4 = r4 / r5
            r3.mPercent = r4
            float r5 = r3.dx
            r0 = 0
            int r5 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
            if (r5 >= 0) goto L4e
            int r5 = com.miui.gallery.vlog.base.widget.VlogPlayView.LEFT
            goto L50
        L4e:
            int r5 = com.miui.gallery.vlog.base.widget.VlogPlayView.RIGHT
        L50:
            r3.LAYOUT = r5
            com.miui.gallery.vlog.base.widget.VlogPlayView$IProgress r0 = r3.mIProgress
            if (r0 == 0) goto L84
            r0.onPlayProgressChanging(r5, r4)
            goto L84
        L5a:
            float r4 = r5.getX()
            r3.upX = r4
            float r5 = r3.downX
            float r4 = r4 - r5
            r3.dx = r4
            com.miui.gallery.vlog.base.widget.VlogPlayView$IProgress r4 = r3.mIProgress
            if (r4 == 0) goto L6c
            r4.onPlayProgressEndChanged()
        L6c:
            com.miui.gallery.vlog.view.VlogVideoProgressBar r4 = r3.mVlogVideoProgressBar
            r4.setIsTouching(r0)
            goto L84
        L72:
            float r4 = r5.getX()
            r3.downX = r4
            com.miui.gallery.vlog.base.widget.VlogPlayView$IProgress r4 = r3.mIProgress
            if (r4 == 0) goto L7f
            r4.onPlayProgressStartChanged()
        L7f:
            com.miui.gallery.vlog.view.VlogVideoProgressBar r4 = r3.mVlogVideoProgressBar
            r4.setIsTouching(r1)
        L84:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.base.widget.VlogPlayView.lambda$new$0(android.view.View, android.view.MotionEvent):boolean");
    }
}
