package com.miui.gallery.movie.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import miuix.view.animation.QuadraticEaseInOutInterpolator;

/* loaded from: classes2.dex */
public abstract class BaseMovieView extends ViewGroup {
    public View mDisplayView;
    public View mExtraContent;
    public Handler mHandler;
    public View mLoadingView;
    public float mPercent;
    public PlayProgressView mPlayProgress;
    public FrameLayout mPreviewBtn;
    public IProgressChangeListener mProgressChangeListener;
    public int mScaledTouchSlop;
    public boolean mTouchAvailable;
    public float mViewWidth;

    /* loaded from: classes2.dex */
    public interface IProgressChangeListener {
        void onVideoProgressChanged();

        void onVideoProgressChanging(int i, float f);
    }

    public abstract View createDisplayView();

    public BaseMovieView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mViewWidth = 0.0f;
        this.mHandler = new Handler() { // from class: com.miui.gallery.movie.ui.view.BaseMovieView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    BaseMovieView.showView(BaseMovieView.this.mLoadingView, true);
                }
                super.handleMessage(message);
            }
        };
    }

    public void init() {
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R$layout.movie_view_display, this);
        this.mDisplayView = createDisplayView();
        this.mExtraContent = findViewById(R$id.extra_content);
        addView(this.mDisplayView, 0);
        this.mDisplayView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mPlayProgress = (PlayProgressView) findViewById(R$id.play_progress);
        this.mPreviewBtn = (FrameLayout) findViewById(R$id.preview_btn);
        this.mLoadingView = findViewById(com.miui.gallery.editor.R$id.progress);
        this.mDisplayView.setOnTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.movie.ui.view.BaseMovieView.2
            public boolean changed;
            public float downX = 0.0f;
            public float moveX = 0.0f;
            public float dx = 0.0f;
            public int layout = -1;

            /* JADX WARN: Code restructure failed: missing block: B:11:0x0017, code lost:
                if (r5 != 3) goto L12;
             */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public boolean onTouch(android.view.View r5, android.view.MotionEvent r6) {
                /*
                    r4 = this;
                    com.miui.gallery.movie.ui.view.BaseMovieView r5 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    boolean r5 = com.miui.gallery.movie.ui.view.BaseMovieView.access$100(r5)
                    r0 = 0
                    if (r5 != 0) goto La
                    return r0
                La:
                    int r5 = r6.getAction()
                    r1 = 1
                    if (r5 == 0) goto L92
                    if (r5 == r1) goto L79
                    r2 = 2
                    if (r5 == r2) goto L1b
                    r6 = 3
                    if (r5 == r6) goto L79
                    goto L91
                L1b:
                    float r5 = r6.getX()
                    r4.moveX = r5
                    float r6 = r4.downX
                    float r5 = r5 - r6
                    r4.dx = r5
                    float r5 = java.lang.Math.abs(r5)
                    com.miui.gallery.movie.ui.view.BaseMovieView r6 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    int r6 = com.miui.gallery.movie.ui.view.BaseMovieView.access$200(r6)
                    float r6 = (float) r6
                    int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
                    if (r5 <= 0) goto L91
                    com.miui.gallery.movie.ui.view.BaseMovieView r5 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    float r5 = com.miui.gallery.movie.ui.view.BaseMovieView.access$300(r5)
                    r6 = 0
                    int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
                    if (r5 <= 0) goto L91
                    com.miui.gallery.movie.ui.view.BaseMovieView r5 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    float r2 = r4.dx
                    float r2 = java.lang.Math.abs(r2)
                    com.miui.gallery.movie.ui.view.BaseMovieView r3 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    float r3 = com.miui.gallery.movie.ui.view.BaseMovieView.access$300(r3)
                    float r2 = r2 / r3
                    com.miui.gallery.movie.ui.view.BaseMovieView.access$402(r5, r2)
                    float r5 = r4.dx
                    int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
                    if (r5 >= 0) goto L5a
                    r5 = r0
                    goto L5b
                L5a:
                    r5 = r1
                L5b:
                    r4.layout = r5
                    r4.changed = r1
                    com.miui.gallery.movie.ui.view.BaseMovieView r5 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    com.miui.gallery.movie.ui.view.BaseMovieView$IProgressChangeListener r5 = com.miui.gallery.movie.ui.view.BaseMovieView.access$500(r5)
                    if (r5 == 0) goto L91
                    com.miui.gallery.movie.ui.view.BaseMovieView r5 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    com.miui.gallery.movie.ui.view.BaseMovieView$IProgressChangeListener r5 = com.miui.gallery.movie.ui.view.BaseMovieView.access$500(r5)
                    int r6 = r4.layout
                    com.miui.gallery.movie.ui.view.BaseMovieView r1 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    float r1 = com.miui.gallery.movie.ui.view.BaseMovieView.access$400(r1)
                    r5.onVideoProgressChanging(r6, r1)
                    goto L91
                L79:
                    boolean r5 = r4.changed
                    if (r5 == 0) goto L91
                    com.miui.gallery.movie.ui.view.BaseMovieView r5 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    com.miui.gallery.movie.ui.view.BaseMovieView$IProgressChangeListener r5 = com.miui.gallery.movie.ui.view.BaseMovieView.access$500(r5)
                    if (r5 == 0) goto L8e
                    com.miui.gallery.movie.ui.view.BaseMovieView r5 = com.miui.gallery.movie.ui.view.BaseMovieView.this
                    com.miui.gallery.movie.ui.view.BaseMovieView$IProgressChangeListener r5 = com.miui.gallery.movie.ui.view.BaseMovieView.access$500(r5)
                    r5.onVideoProgressChanged()
                L8e:
                    boolean r5 = r4.changed
                    return r5
                L91:
                    return r0
                L92:
                    float r5 = r6.getX()
                    r4.downX = r5
                    r4.changed = r0
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.movie.ui.view.BaseMovieView.AnonymousClass2.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        });
        FolmeUtil.setCustomTouchAnim(this.mPreviewBtn, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.9f).build(), null, null, null, true);
    }

    public void setTouchAvailable(boolean z) {
        this.mTouchAvailable = z;
    }

    public void updatePlayProgress(float f) {
        this.mPlayProgress.setProgress(f);
    }

    public void showPlayProgress(boolean z) {
        showView(this.mPlayProgress, z);
    }

    public void showPreviewBtn(boolean z) {
        showView(this.mPreviewBtn, z);
    }

    public void showLoadingView(boolean z) {
        if (z) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 500L);
            return;
        }
        this.mHandler.removeMessages(1);
        showView(this.mLoadingView, false);
    }

    public void showExtraContent(boolean z) {
        View view = this.mExtraContent;
        Property property = View.ALPHA;
        float[] fArr = new float[2];
        float f = 0.0f;
        fArr[0] = z ? 0.0f : 1.0f;
        if (z) {
            f = 1.0f;
        }
        fArr[1] = f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, property, fArr);
        ofFloat.setInterpolator(new QuadraticEaseInOutInterpolator());
        ofFloat.setDuration(220L);
        ofFloat.start();
    }

    public void setPreviewBtnClickListener(View.OnClickListener onClickListener) {
        this.mPreviewBtn.setOnClickListener(onClickListener);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = i4 - i2;
        View view = this.mDisplayView;
        if (view != null) {
            int measuredWidth = (i5 - view.getMeasuredWidth()) / 2;
            int measuredWidth2 = this.mDisplayView.getMeasuredWidth() + measuredWidth;
            int measuredHeight = (int) ((i6 - this.mDisplayView.getMeasuredHeight()) / 1.6d);
            int measuredHeight2 = this.mDisplayView.getMeasuredHeight() + measuredHeight;
            this.mDisplayView.layout(measuredWidth, measuredHeight, measuredWidth2, measuredHeight2);
            this.mExtraContent.layout(measuredWidth, measuredHeight, measuredWidth2, measuredHeight2);
        }
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        float heightToWidth = MovieConfig.getHeightToWidth();
        float f = size2;
        float f2 = size;
        float f3 = f / f2;
        if (Float.compare(f3, heightToWidth) == 0) {
            i3 = size;
        } else if (f3 >= heightToWidth) {
            i4 = (int) (f2 * heightToWidth);
            i3 = size;
            this.mDisplayView.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
            this.mExtraContent.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
            this.mViewWidth = i3;
            setMeasuredDimension(size, size2);
        } else {
            i3 = (int) (f / heightToWidth);
        }
        i4 = size2;
        this.mDisplayView.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
        this.mExtraContent.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
        this.mViewWidth = i3;
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mDisplayView.setOnClickListener(onClickListener);
    }

    public void setProgressChangeListener(IProgressChangeListener iProgressChangeListener) {
        this.mProgressChangeListener = iProgressChangeListener;
    }

    public static void showView(View view, boolean z) {
        if (view != null) {
            view.setVisibility(z ? 0 : 4);
        }
    }
}
