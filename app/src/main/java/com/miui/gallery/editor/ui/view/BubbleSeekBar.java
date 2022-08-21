package com.miui.gallery.editor.ui.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.editor.R$id;
import com.miui.gallery.editor.R$layout;
import com.miui.gallery.editor.R$styleable;
import com.miui.gallery.widget.OrientationProvider;
import java.lang.ref.WeakReference;
import java.util.Collection;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class BubbleSeekBar extends View {
    public final String TAG;
    public int mBsbSliderToFillGap;
    public int mBsbVisibilityHeight;
    public int mBsbVisibilityWidth;
    public PopupWindow mBubble;
    public int mBubbleSeekbarDistance;
    public int mBubbleWidthNormal;
    public int mBubbleY;
    public int mColorEmpty;
    public int mColorFill;
    public int mColorSlider;
    public float mCurrentEmptyProgressHeight;
    public float mCurrentEmptyProgressWidth;
    public float mCurrentFillProgressHeight;
    public float mCurrentFillProgressWidth;
    public float mCurrentProgress;
    public float mCurrentSliderWidth;
    public float mDeviationProgress;
    public int mEmptyProgressHeight;
    public int mEmptyProgressWidth;
    public EnlargeAnimListener mEnlargeAnimListener;
    public int mFillProgressHeight;
    public Handler mHandler;
    public int mHeight;
    public boolean mHideBubble;
    public AnimConfig mHideBubbleConfig;
    public int mIdentityHashCode;
    public Runnable mInteractiveRunnable;
    public boolean mIsBarHorizontal;
    public boolean mIsPress;
    public int mMaxProgress;
    public int mMinProgress;
    public float mMoveOffset;
    public int mOffsetX;
    public int mOffsetY;
    public OrientationProvider mOrientationProvider;
    public Paint mPaintEmpty;
    public Paint mPaintFill;
    public Paint mPaintSlider;
    public int mPopWidowPadding;
    public int mPopWidowSize;
    public View mPopWindowContentView;
    public float mPopWindowContentViewAlpha;
    public int mPopWindowContentViewSize;
    public int mPopX;
    public int mPopY;
    public float mPressX;
    public float mPressY;
    public int mPreviousProgress;
    public ProgressListener mProgressListener;
    public float mProgressPosition;
    public TextView mProgressTv;
    public AnimConfig mShowBubbleConfig;
    public boolean mSliderEenlargeHide;
    public int mSliderWidth;
    public int mSliderWidthHighlight;
    public int mStartPointProgress;
    public float mStartPointProportion;
    public boolean mTrackingAndNotMove;
    public BubbleTransitionListener mTransitionListener;
    public int mWidth;
    public static final IntValueProperty ANIM_Y = new IntValueProperty("ANIM_Y");
    public static final IntValueProperty ANIM_WIDTH = new IntValueProperty("ANIM_WIDTH");
    public static final ValueProperty ANIM_ALPHA = new ValueProperty("ANIM_ALPHA");
    public static final ValueProperty ANIM_EMPTY_HEIGHT = new ValueProperty("ANIM_EMPTY_HEIGHT");
    public static final ValueProperty ANIM_FILL_HEIGHT = new ValueProperty("ANIM_FILL_HEIGHT");
    public static final ValueProperty ANIM_FILL_WIDTH = new ValueProperty("ANIM_FILL_WIDTH");
    public static final ValueProperty ANIM_EMPTY_WIDTH = new ValueProperty("ANIM_EMPTY_WIDTH");
    public static final ValueProperty ANIM_SLIDER_WIDTH = new ValueProperty("ANIM_SLIDER_WIDTH");

    /* loaded from: classes2.dex */
    public interface EnlargeAnimListener {
        void onAnimProgressChanged(int i);
    }

    /* loaded from: classes2.dex */
    public interface ProgressListener {
        void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i);

        default void onProgressStartChange(BubbleSeekBar bubbleSeekBar, int i) {
        }

        void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar);

        void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar);
    }

    public BubbleSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = "BubbleSeekBar";
        this.mHandler = new Handler();
        this.mOrientationProvider = OrientationProvider.PORTRAIT;
        this.mInteractiveRunnable = new Runnable() { // from class: com.miui.gallery.editor.ui.view.BubbleSeekBar.1
            @Override // java.lang.Runnable
            public void run() {
                BubbleSeekBar.this.setEnabled(true);
            }
        };
        init(context, attributeSet);
    }

    public final void init(Context context, AttributeSet attributeSet) {
        initDimensionValues(context, attributeSet);
        initPaint();
        initPopupWindow();
        int i = this.mEmptyProgressHeight;
        this.mCurrentEmptyProgressHeight = i;
        int i2 = this.mEmptyProgressWidth;
        this.mCurrentEmptyProgressWidth = i2;
        int i3 = this.mFillProgressHeight;
        this.mCurrentFillProgressHeight = i3;
        this.mCurrentFillProgressWidth = i2 - (i - i3);
        this.mCurrentSliderWidth = this.mSliderWidth;
        this.mBubbleWidthNormal = getResources().getDimensionPixelSize(R$dimen.px_100);
        this.mSliderWidthHighlight = getResources().getDimensionPixelOffset(R$dimen.px_50);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.seek_bar_bubble_shape_bg_solid_radius);
        this.mPopWidowSize = dimensionPixelSize;
        this.mPopWidowPadding = (dimensionPixelSize - this.mBubbleWidthNormal) / 2;
        this.mBubbleWidthNormal = getResources().getDimensionPixelSize(R$dimen.seek_bar_bubble_window_size);
        this.mIdentityHashCode = System.identityHashCode(this);
        setEnabled(false);
        this.mIsBarHorizontal = this.mOrientationProvider.isPortrait(getContext());
    }

    public final void initDimensionValues(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BubbleSeekBar);
        this.mColorEmpty = obtainStyledAttributes.getColor(R$styleable.BubbleSeekBar_bsb_color_empty, 268435456);
        this.mColorFill = obtainStyledAttributes.getColor(R$styleable.BubbleSeekBar_bsb_color_fill, 1073741824);
        this.mColorSlider = obtainStyledAttributes.getColor(R$styleable.BubbleSeekBar_bsb_color_slider, 0);
        this.mMaxProgress = obtainStyledAttributes.getInteger(R$styleable.BubbleSeekBar_bsb_max_progress, 100);
        this.mMinProgress = obtainStyledAttributes.getInteger(R$styleable.BubbleSeekBar_bsb_min_progress, 0);
        this.mSliderWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.BubbleSeekBar_bsb_slider_width, 0);
        this.mCurrentProgress = obtainStyledAttributes.getInteger(R$styleable.BubbleSeekBar_bsb_current_progress, 0);
        this.mEmptyProgressHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.BubbleSeekBar_bsb_empty_progress_height, 50);
        this.mEmptyProgressWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.BubbleSeekBar_bsb_empty_progress_width, 50);
        this.mFillProgressHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.BubbleSeekBar_bsb_fill_progress_height, this.mEmptyProgressHeight);
        this.mHideBubble = obtainStyledAttributes.getBoolean(R$styleable.BubbleSeekBar_bsb_hide_bubble, true);
        this.mSliderEenlargeHide = obtainStyledAttributes.getBoolean(R$styleable.BubbleSeekBar_bsb_slider_enlarge_hide, false);
        this.mBsbVisibilityWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.BubbleSeekBar_bsb_visibility_width, 100);
        this.mBubbleSeekbarDistance = obtainStyledAttributes.getDimensionPixelSize(R$styleable.BubbleSeekBar_bsb_bubble_seekbar_distance, 0);
        this.mBsbVisibilityHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.BubbleSeekBar_bsb_visibility_height, 100);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, com.miui.gallery.baseui.R$styleable.OrientationProvider);
        String string = obtainStyledAttributes2.getString(com.miui.gallery.baseui.R$styleable.OrientationProvider_orientation_provider);
        obtainStyledAttributes2.recycle();
        OrientationProvider createOrientationProvider = OrientationProvider.createOrientationProvider(string);
        if (createOrientationProvider != null) {
            this.mOrientationProvider = createOrientationProvider;
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIsBarHorizontal = this.mOrientationProvider.isPortrait(getContext());
    }

    public final void initPaint() {
        Paint paint = new Paint();
        this.mPaintEmpty = paint;
        paint.setAntiAlias(true);
        this.mPaintEmpty.setColor(this.mColorEmpty);
        this.mPaintEmpty.setStyle(Paint.Style.FILL);
        Paint paint2 = new Paint();
        this.mPaintFill = paint2;
        paint2.setAntiAlias(true);
        this.mPaintFill.setColor(this.mColorFill);
        this.mPaintFill.setStyle(Paint.Style.FILL);
        Paint paint3 = new Paint();
        this.mPaintSlider = paint3;
        paint3.setAntiAlias(true);
        this.mPaintSlider.setColor(this.mColorSlider);
        this.mPaintSlider.setStyle(Paint.Style.FILL);
    }

    public final void initPopupWindow() {
        AnimConfig minDuration = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.3f)).setMinDuration(200L);
        IntValueProperty intValueProperty = ANIM_Y;
        AnimConfig special = minDuration.setSpecial(intValueProperty, EaseManager.getStyle(-2, 0.9f, 0.2f), new float[0]);
        IntValueProperty intValueProperty2 = ANIM_WIDTH;
        AnimConfig special2 = special.setSpecial(intValueProperty2, EaseManager.getStyle(-2, 0.9f, 0.2f), new float[0]);
        ValueProperty valueProperty = ANIM_ALPHA;
        this.mShowBubbleConfig = special2.setSpecial(valueProperty, EaseManager.getStyle(6, 100.0f), new float[0]);
        this.mHideBubbleConfig = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.3f)).setMinDuration(200L).setSpecial(intValueProperty, EaseManager.getStyle(5, 250.0f), new float[0]).setSpecial(intValueProperty2, EaseManager.getStyle(5, 250.0f), new float[0]).setSpecial(valueProperty, EaseManager.getStyle(6, 100.0f), 150L, new float[0]);
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.seek_bar_bubble_window, (ViewGroup) null);
        View findViewById = inflate.findViewById(R$id.rl_content_view);
        this.mPopWindowContentView = findViewById;
        this.mProgressTv = (TextView) findViewById.findViewById(R$id.tv_progress);
        int i = this.mPopWidowSize;
        this.mBubble = new Bubble(inflate, i, i, false);
        this.mPopWindowContentViewSize = this.mPopWindowContentView.getLayoutParams().width;
        this.mBubble.setTouchable(false);
        this.mPopWindowContentView.setAlpha(this.mPopWindowContentViewAlpha);
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        BubbleTransitionListener bubbleTransitionListener = this.mTransitionListener;
        if (bubbleTransitionListener != null) {
            bubbleTransitionListener.rebuildBubbleRef(this);
        }
        if (!this.mBubble.isShowing() && !this.mHideBubble) {
            this.mBubble.showAsDropDown(this);
        }
        this.mHandler.postDelayed(this.mInteractiveRunnable, 300L);
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mWidth = View.MeasureSpec.getSize(i);
        int size = View.MeasureSpec.getSize(i2);
        this.mHeight = size;
        if (this.mIsBarHorizontal) {
            this.mOffsetY = (size - this.mBsbVisibilityHeight) / 2;
            this.mOffsetX = (this.mWidth - this.mBsbVisibilityWidth) / 2;
            return;
        }
        this.mOffsetY = (size - this.mBsbVisibilityWidth) / 2;
        this.mOffsetX = (this.mWidth - this.mBsbVisibilityHeight) / 2;
    }

    public final void drawHorizontal(Canvas canvas) {
        int i;
        float f;
        float f2;
        int i2;
        float f3;
        float f4;
        int i3;
        int i4;
        int i5 = this.mHeight;
        int i6 = this.mBsbVisibilityHeight;
        this.mOffsetY = (i5 - i6) / 2;
        int i7 = this.mWidth;
        int i8 = this.mBsbVisibilityWidth;
        this.mOffsetX = (i7 - i8) / 2;
        float f5 = (i6 - this.mCurrentEmptyProgressHeight) / 2.0f;
        float f6 = (i6 - this.mCurrentFillProgressHeight) / 2.0f;
        float f7 = (i8 - this.mCurrentEmptyProgressWidth) / 2.0f;
        if (this.mBubble.isShowing() && !this.mHideBubble) {
            ViewGroup.LayoutParams layoutParams = this.mPopWindowContentView.getLayoutParams();
            int i9 = this.mBsbVisibilityWidth;
            int i10 = this.mBsbVisibilityHeight;
            Float valueOf = Float.valueOf((((i9 - i10) * ((this.mCurrentProgress + this.mDeviationProgress) - this.mMinProgress)) / (this.mMaxProgress - i4)) + (i10 / 2));
            int i11 = this.mPopWidowSize;
            int floatValue = (int) ((this.mOffsetX + valueOf.floatValue()) - (i11 / 2));
            this.mPopX = floatValue;
            int i12 = -((((this.mBsbVisibilityHeight + this.mOffsetY) + i11) + this.mBubbleY) - this.mPopWidowPadding);
            this.mPopY = i12;
            this.mBubble.update(this, floatValue, i12, i11, i11);
            int i13 = this.mPopWindowContentViewSize;
            layoutParams.width = i13;
            layoutParams.height = i13;
            this.mPopWindowContentView.setLayoutParams(layoutParams);
            this.mPopWindowContentView.setAlpha(this.mPopWindowContentViewAlpha);
        }
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(this.mIsPress ? R$dimen.bubble_seek_bar_slider_to_fill_gap_highlight : R$dimen.bubble_seek_bar_slider_to_fill_gap_normal);
        this.mBsbSliderToFillGap = dimensionPixelSize;
        float f8 = ((this.mCurrentProgress + this.mDeviationProgress) - this.mMinProgress) / (this.mMaxProgress - i);
        this.mProgressPosition = ((this.mBsbVisibilityWidth - this.mBsbVisibilityHeight) * f8) + (this.mEmptyProgressHeight / 2) + dimensionPixelSize;
        int i14 = this.mOffsetX;
        int i15 = this.mOffsetY;
        RectF rectF = new RectF(i14 + f7, i15 + f5, (this.mBsbVisibilityWidth - f7) + i14, (this.mBsbVisibilityHeight - f5) + i15);
        float f9 = this.mCurrentEmptyProgressHeight;
        canvas.drawRoundRect(rectF, f9, f9, this.mPaintEmpty);
        float f10 = this.mCurrentFillProgressHeight;
        float f11 = (f10 - (this.mBsbSliderToFillGap * 2)) / 2.0f;
        float min = Math.min(this.mProgressPosition, (this.mBsbVisibilityWidth - f6) - (f10 / 2.0f));
        this.mProgressPosition = min;
        this.mProgressPosition = Math.max(min, (this.mCurrentFillProgressHeight / 2.0f) + f6);
        int i16 = this.mStartPointProgress;
        int i17 = this.mMinProgress;
        float f12 = (i16 - i17) / (this.mMaxProgress - i17);
        this.mStartPointProportion = f12;
        if (f12 == 0.0f) {
            int i18 = this.mOffsetX;
            RectF rectF2 = new RectF(i18 + f6, this.mOffsetY + f6, i18 + this.mProgressPosition + (this.mCurrentFillProgressHeight / 2.0f), (i3 + this.mBsbVisibilityHeight) - f6);
            float f13 = this.mCurrentFillProgressHeight;
            canvas.drawRoundRect(rectF2, f13, f13, this.mPaintFill);
        } else {
            float f14 = this.mStartPointProportion;
            if (f8 > f14) {
                f = this.mWidth / 2;
                f2 = this.mCurrentFillProgressHeight;
            } else {
                f = this.mOffsetX + this.mProgressPosition;
                f2 = this.mCurrentFillProgressHeight;
            }
            float f15 = f - (f2 / 2.0f);
            float f16 = this.mOffsetY + f6;
            if (f8 < f14) {
                f3 = this.mWidth / 2;
                f4 = this.mCurrentFillProgressHeight;
            } else {
                f3 = this.mOffsetX + this.mProgressPosition;
                f4 = this.mCurrentFillProgressHeight;
            }
            RectF rectF3 = new RectF(f15, f16, f3 + (f4 / 2.0f), (i2 + this.mBsbVisibilityHeight) - f6);
            float f17 = this.mCurrentFillProgressHeight;
            canvas.drawRoundRect(rectF3, f17, f17, this.mPaintFill);
        }
        if (!this.mSliderEenlargeHide) {
            float f18 = this.mCurrentSliderWidth;
            if (f18 != 0.0f) {
                f11 = f18 / 2.0f;
            }
            canvas.drawCircle(this.mOffsetX + this.mProgressPosition, this.mOffsetY + f6 + (this.mCurrentFillProgressHeight / 2.0f), f11, this.mPaintSlider);
        }
    }

    public final void drawVertical(Canvas canvas) {
        int i;
        int i2;
        float f;
        float f2;
        float f3;
        float f4;
        int i3;
        int i4;
        int i5;
        int i6 = this.mHeight;
        int i7 = this.mBsbVisibilityWidth;
        this.mOffsetY = (i6 - i7) / 2;
        int i8 = this.mWidth;
        int i9 = this.mBsbVisibilityHeight;
        this.mOffsetX = (i8 - i9) / 2;
        float f5 = (i9 - this.mCurrentFillProgressHeight) / 2.0f;
        float f6 = (i7 - this.mCurrentEmptyProgressWidth) / 2.0f;
        float f7 = (i9 - this.mCurrentEmptyProgressHeight) / 2.0f;
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(this.mIsPress ? R$dimen.bubble_seek_bar_slider_to_fill_gap_highlight : R$dimen.bubble_seek_bar_slider_to_fill_gap_normal);
        this.mBsbSliderToFillGap = dimensionPixelSize;
        float f8 = ((this.mCurrentProgress + this.mDeviationProgress) - this.mMinProgress) / (this.mMaxProgress - i);
        this.mProgressPosition = ((this.mBsbVisibilityWidth - this.mBsbVisibilityHeight) * (1.0f - f8)) + (this.mEmptyProgressHeight / 2) + dimensionPixelSize;
        int i10 = this.mOffsetX;
        int i11 = this.mOffsetY;
        RectF rectF = new RectF(i10 + f7, i11 + f6, (this.mBsbVisibilityHeight - f7) + i10, (this.mBsbVisibilityWidth - f6) + i11);
        float f9 = this.mCurrentEmptyProgressHeight;
        canvas.drawRoundRect(rectF, f9, f9, this.mPaintEmpty);
        float f10 = this.mCurrentFillProgressHeight;
        float f11 = (f10 - (this.mBsbSliderToFillGap * 2)) / 2.0f;
        float min = Math.min(this.mProgressPosition, (this.mBsbVisibilityWidth - f5) - (f10 / 2.0f));
        this.mProgressPosition = min;
        this.mProgressPosition = Math.max(min, (this.mCurrentFillProgressHeight / 2.0f) + f5);
        int i12 = this.mStartPointProgress;
        int i13 = this.mMinProgress;
        float f12 = (i12 - i13) / (this.mMaxProgress - i13);
        this.mStartPointProportion = f12;
        if (f12 == 0.0f) {
            RectF rectF2 = new RectF(this.mOffsetX + f5, (this.mOffsetY + this.mProgressPosition) - (this.mCurrentFillProgressHeight / 2.0f), (i4 + this.mBsbVisibilityHeight) - f5, (i5 + this.mBsbVisibilityWidth) - f5);
            float f13 = this.mCurrentFillProgressHeight;
            canvas.drawRoundRect(rectF2, f13, f13, this.mPaintFill);
        } else {
            float f14 = this.mOffsetX + f5;
            float f15 = this.mStartPointProportion;
            if (f8 < f15) {
                f = this.mHeight / 2;
                f2 = this.mCurrentFillProgressHeight;
            } else {
                f = this.mOffsetY + this.mProgressPosition;
                f2 = this.mCurrentFillProgressHeight;
            }
            float f16 = f - (f2 / 2.0f);
            float f17 = (i2 + this.mBsbVisibilityHeight) - f5;
            if (f8 > f15) {
                f3 = this.mHeight / 2;
                f4 = this.mCurrentFillProgressHeight;
            } else {
                f3 = this.mOffsetY + this.mProgressPosition;
                f4 = this.mCurrentFillProgressHeight;
            }
            RectF rectF3 = new RectF(f14, f16, f17, f3 + (f4 / 2.0f));
            float f18 = this.mCurrentFillProgressHeight;
            canvas.drawRoundRect(rectF3, f18, f18, this.mPaintFill);
        }
        if (!this.mSliderEenlargeHide) {
            float f19 = this.mCurrentSliderWidth;
            if (f19 != 0.0f) {
                f11 = f19 / 2.0f;
            }
            canvas.drawCircle(this.mOffsetX + f5 + (this.mCurrentFillProgressHeight / 2.0f), this.mOffsetY + this.mProgressPosition, f11, this.mPaintSlider);
        }
        if (!this.mBubble.isShowing() || this.mHideBubble) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = this.mPopWindowContentView.getLayoutParams();
        this.mPopX = ((this.mOffsetX - this.mPopWidowSize) - this.mBubbleY) + this.mPopWidowPadding;
        int i14 = this.mBsbVisibilityWidth;
        int i15 = this.mBsbVisibilityHeight;
        Float valueOf = Float.valueOf((((i14 - i15) * ((this.mCurrentProgress + this.mDeviationProgress) - this.mMinProgress)) / (this.mMaxProgress - i3)) + (i15 / 2));
        int i16 = this.mPopWidowSize;
        int i17 = -(((int) (this.mOffsetY + valueOf.floatValue())) + (i16 / 2));
        this.mPopY = i17;
        this.mBubble.update(this, this.mPopX, i17, i16, i16);
        int i18 = this.mPopWindowContentViewSize;
        layoutParams.width = i18;
        layoutParams.height = i18;
        this.mPopWindowContentView.setLayoutParams(layoutParams);
        this.mPopWindowContentView.setAlpha(this.mPopWindowContentViewAlpha);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this.mIsBarHorizontal) {
            drawHorizontal(canvas);
        } else {
            drawVertical(canvas);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0015, code lost:
        if (r0 != 3) goto L12;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r14) {
        /*
            Method dump skipped, instructions count: 540
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.ui.view.BubbleSeekBar.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void onMove(MotionEvent motionEvent) {
        if (this.mIsBarHorizontal) {
            this.mMoveOffset = motionEvent.getX() - this.mPressX;
        } else {
            this.mMoveOffset = this.mPressY - motionEvent.getY();
        }
        float f = ((this.mMaxProgress - this.mMinProgress) * this.mMoveOffset) / (this.mBsbVisibilityWidth - this.mBsbVisibilityHeight);
        this.mDeviationProgress = f;
        float min = Math.min(f, getMaxProgress() - this.mCurrentProgress);
        this.mDeviationProgress = min;
        this.mDeviationProgress = Math.max(min, getMinProgress() - this.mCurrentProgress);
    }

    private TransitionListener getTransitionListener() {
        if (this.mTransitionListener == null) {
            this.mTransitionListener = new BubbleTransitionListener(this);
        }
        return this.mTransitionListener;
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Folme.useValue(Integer.valueOf(this.mIdentityHashCode)).cancel();
        this.mBubble.dismiss();
    }

    /* loaded from: classes2.dex */
    public static class BubbleTransitionListener extends TransitionListener {
        public WeakReference<BubbleSeekBar> mBubbleRef;

        public BubbleTransitionListener(BubbleSeekBar bubbleSeekBar) {
            this.mBubbleRef = new WeakReference<>(bubbleSeekBar);
        }

        public void rebuildBubbleRef(BubbleSeekBar bubbleSeekBar) {
            if (this.mBubbleRef == null) {
                this.mBubbleRef = new WeakReference<>(bubbleSeekBar);
            }
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
            super.onUpdate(obj, collection);
            WeakReference<BubbleSeekBar> weakReference = this.mBubbleRef;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            BubbleSeekBar bubbleSeekBar = this.mBubbleRef.get();
            UpdateInfo findBy = UpdateInfo.findBy(collection, BubbleSeekBar.ANIM_Y);
            if (findBy != null) {
                bubbleSeekBar.mBubbleY = findBy.getIntValue();
            }
            UpdateInfo findBy2 = UpdateInfo.findBy(collection, BubbleSeekBar.ANIM_WIDTH);
            if (findBy2 != null) {
                bubbleSeekBar.mPopWindowContentViewSize = findBy2.getIntValue();
            }
            UpdateInfo findBy3 = UpdateInfo.findBy(collection, BubbleSeekBar.ANIM_ALPHA);
            if (findBy3 != null) {
                float floatValue = findBy3.getFloatValue();
                if (floatValue >= 0.0f && floatValue <= 1.0f) {
                    bubbleSeekBar.mPopWindowContentViewAlpha = findBy3.getFloatValue();
                }
            }
            UpdateInfo findBy4 = UpdateInfo.findBy(collection, BubbleSeekBar.ANIM_EMPTY_HEIGHT);
            if (findBy4 != null) {
                bubbleSeekBar.mCurrentEmptyProgressHeight = findBy4.getFloatValue();
            }
            UpdateInfo findBy5 = UpdateInfo.findBy(collection, BubbleSeekBar.ANIM_FILL_HEIGHT);
            if (findBy5 != null) {
                bubbleSeekBar.mCurrentFillProgressHeight = findBy5.getFloatValue();
            }
            UpdateInfo findBy6 = UpdateInfo.findBy(collection, BubbleSeekBar.ANIM_FILL_WIDTH);
            if (findBy6 != null) {
                bubbleSeekBar.mCurrentFillProgressWidth = findBy6.getFloatValue();
            }
            UpdateInfo findBy7 = UpdateInfo.findBy(collection, BubbleSeekBar.ANIM_EMPTY_WIDTH);
            if (findBy7 != null) {
                bubbleSeekBar.mCurrentEmptyProgressWidth = findBy7.getFloatValue();
            }
            UpdateInfo findBy8 = UpdateInfo.findBy(collection, BubbleSeekBar.ANIM_SLIDER_WIDTH);
            if (findBy8 != null) {
                bubbleSeekBar.mCurrentSliderWidth = findBy8.getFloatValue();
            }
            bubbleSeekBar.invalidate();
        }
    }

    public void updateWidth(int i, int i2) {
        this.mBsbVisibilityWidth = i;
        this.mCurrentEmptyProgressWidth = i2;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }

    public void setEnlargeListener(EnlargeAnimListener enlargeAnimListener) {
        this.mEnlargeAnimListener = enlargeAnimListener;
    }

    public int getMinProgress() {
        return this.mMinProgress;
    }

    public void setMinProgress(int i) {
        this.mMinProgress = i;
        postInvalidate();
    }

    public void setMaxProgress(int i) {
        this.mMaxProgress = i;
        postInvalidate();
    }

    public int getMaxProgress() {
        return this.mMaxProgress;
    }

    public void setCurrentProgress(float f) {
        int i = this.mMaxProgress;
        if (f > i) {
            f = i;
        }
        this.mCurrentProgress = f;
        postInvalidate();
    }

    public float getCurrentProgress() {
        return this.mCurrentProgress + this.mDeviationProgress;
    }

    public void setHideBubble(boolean z) {
        this.mHideBubble = z;
    }

    public float getCurrentHeight() {
        return this.mCurrentEmptyProgressHeight;
    }

    public void setCurrentHeight(float f) {
        this.mCurrentEmptyProgressHeight = f;
        EnlargeAnimListener enlargeAnimListener = this.mEnlargeAnimListener;
        if (enlargeAnimListener != null) {
            int i = this.mEmptyProgressHeight;
            enlargeAnimListener.onAnimProgressChanged((int) ((this.mMaxProgress * (f - i)) / (this.mBsbVisibilityHeight - i)));
        }
        invalidate();
    }

    public float getCurrentFillHeight() {
        return this.mCurrentFillProgressHeight;
    }

    public void setCurrentFillHeight(float f) {
        this.mCurrentFillProgressHeight = f;
    }

    public float getCurrentWidth() {
        return this.mCurrentEmptyProgressWidth;
    }

    public void setCurrentWidth(float f) {
        this.mCurrentEmptyProgressWidth = f;
    }

    public float getCurrentSliderWidth() {
        return this.mCurrentSliderWidth;
    }

    public void setCurrentSliderWidth(float f) {
        this.mCurrentSliderWidth = f;
    }

    /* loaded from: classes2.dex */
    public class Bubble extends PopupWindow {
        public Bubble(View view, int i, int i2, boolean z) {
            super(view, i, i2, z);
        }
    }
}
