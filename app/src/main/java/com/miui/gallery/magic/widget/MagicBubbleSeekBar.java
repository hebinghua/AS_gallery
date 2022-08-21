package com.miui.gallery.magic.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$styleable;
import java.util.Collection;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class MagicBubbleSeekBar extends View {
    public int mBsbNormalWidth;
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
    public float mCurrentProgress;
    public float mCurrentSliderWidth;
    public float mDeviationProgress;
    public int mEmptyProgressHeight;
    public EnlargeAnimListener mEnlargeAnimListener;
    public int mFillProgressHeight;
    public Handler mHandler;
    public int mHeight;
    public boolean mHideBubble;
    public AnimConfig mHideBubbleConfig;
    public int mIdentityHashCode;
    public Runnable mInteractiveRunnable;
    public boolean mIsPress;
    public int mMaxProgress;
    public int mMinProgress;
    public float mMoveOffset;
    public int mOffsetX;
    public int mOffsetY;
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
    public TransitionListener mTransitionListener;
    public int mWidth;
    public static final IntValueProperty ANIM_Y = new IntValueProperty("ANIM_Y");
    public static final IntValueProperty ANIM_WIDTH = new IntValueProperty("ANIM_WIDTH");
    public static final ValueProperty ANIM_ALPHA = new ValueProperty("ANIM_ALPHA");
    public static final ValueProperty ANIM_EMPTY_HEIGHT = new ValueProperty("ANIM_EMPTY_HEIGHT");
    public static final ValueProperty ANIM_FILL_HEIGHT = new ValueProperty("ANIM_FILL_HEIGHT");
    public static final ValueProperty ANIM_EMPTY_WIDTH = new ValueProperty("ANIM_EMPTY_WIDTH");
    public static final ValueProperty ANIM_SLIDER_WIDTH = new ValueProperty("ANIM_SLIDER_WIDTH");

    /* loaded from: classes2.dex */
    public interface EnlargeAnimListener {
        void onAnimProgressChanged(int i);
    }

    /* loaded from: classes2.dex */
    public interface ProgressListener {
        void onProgressChanged(MagicBubbleSeekBar magicBubbleSeekBar, int i);

        default void onProgressStartChange(MagicBubbleSeekBar magicBubbleSeekBar, int i) {
        }

        void onStartTrackingTouch(MagicBubbleSeekBar magicBubbleSeekBar);

        void onStopTrackingTouch(MagicBubbleSeekBar magicBubbleSeekBar);
    }

    public MagicBubbleSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHandler = new Handler();
        this.mTransitionListener = new TransitionListener() { // from class: com.miui.gallery.magic.widget.MagicBubbleSeekBar.1
            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
                super.onUpdate(obj, collection);
                UpdateInfo findBy = UpdateInfo.findBy(collection, MagicBubbleSeekBar.ANIM_Y);
                if (findBy != null) {
                    MagicBubbleSeekBar.this.mBubbleY = findBy.getIntValue();
                }
                UpdateInfo findBy2 = UpdateInfo.findBy(collection, MagicBubbleSeekBar.ANIM_WIDTH);
                if (findBy2 != null) {
                    MagicBubbleSeekBar.this.mPopWindowContentViewSize = findBy2.getIntValue();
                }
                UpdateInfo findBy3 = UpdateInfo.findBy(collection, MagicBubbleSeekBar.ANIM_ALPHA);
                if (findBy3 != null) {
                    float floatValue = findBy3.getFloatValue();
                    if (floatValue >= 0.0f && floatValue <= 1.0f) {
                        MagicBubbleSeekBar.this.mPopWindowContentViewAlpha = findBy3.getFloatValue();
                    }
                }
                UpdateInfo findBy4 = UpdateInfo.findBy(collection, MagicBubbleSeekBar.ANIM_EMPTY_HEIGHT);
                if (findBy4 != null) {
                    MagicBubbleSeekBar.this.mCurrentEmptyProgressHeight = findBy4.getIntValue();
                }
                UpdateInfo findBy5 = UpdateInfo.findBy(collection, MagicBubbleSeekBar.ANIM_FILL_HEIGHT);
                if (findBy5 != null) {
                    MagicBubbleSeekBar.this.mCurrentFillProgressHeight = findBy5.getIntValue();
                }
                UpdateInfo findBy6 = UpdateInfo.findBy(collection, MagicBubbleSeekBar.ANIM_EMPTY_WIDTH);
                if (findBy6 != null) {
                    MagicBubbleSeekBar.this.mCurrentEmptyProgressWidth = findBy6.getIntValue();
                }
                UpdateInfo findBy7 = UpdateInfo.findBy(collection, MagicBubbleSeekBar.ANIM_SLIDER_WIDTH);
                if (findBy7 != null) {
                    MagicBubbleSeekBar.this.mCurrentSliderWidth = findBy7.getIntValue();
                }
                MagicBubbleSeekBar.this.invalidate();
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj, UpdateInfo updateInfo) {
                super.onComplete(obj, updateInfo);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onCancel(Object obj, UpdateInfo updateInfo) {
                super.onCancel(obj, updateInfo);
            }
        };
        this.mInteractiveRunnable = new Runnable() { // from class: com.miui.gallery.magic.widget.MagicBubbleSeekBar.2
            @Override // java.lang.Runnable
            public void run() {
                MagicBubbleSeekBar.this.setEnabled(true);
            }
        };
        init(context, attributeSet);
    }

    public final void init(Context context, AttributeSet attributeSet) {
        initDimensionValues(context, attributeSet);
        initPaint();
        initPopupWindow();
        int i = this.mBsbNormalWidth;
        if (i == 0) {
            i = this.mBsbVisibilityHeight;
        }
        this.mBsbNormalWidth = i;
        this.mCurrentEmptyProgressHeight = this.mEmptyProgressHeight;
        this.mCurrentFillProgressHeight = this.mFillProgressHeight;
        this.mCurrentEmptyProgressWidth = i;
        this.mCurrentSliderWidth = this.mSliderWidth;
        this.mBubbleWidthNormal = getResources().getDimensionPixelSize(R$dimen.magic_px_100);
        this.mSliderWidthHighlight = getResources().getDimensionPixelOffset(R$dimen.magic_px_50);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.magic_seek_bar_bubble_shape_bg_solid_radius);
        this.mPopWidowSize = dimensionPixelSize;
        this.mPopWidowPadding = (dimensionPixelSize - this.mBubbleWidthNormal) / 2;
        this.mIdentityHashCode = System.identityHashCode(this);
        setEnabled(false);
    }

    public final void initDimensionValues(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.MagicBubbleSeekBar);
        this.mColorEmpty = obtainStyledAttributes.getColor(R$styleable.MagicBubbleSeekBar_bsb_color_empty, 268435456);
        this.mColorFill = obtainStyledAttributes.getColor(R$styleable.MagicBubbleSeekBar_bsb_color_fill, 1073741824);
        this.mColorSlider = obtainStyledAttributes.getColor(R$styleable.MagicBubbleSeekBar_bsb_color_slider, 0);
        this.mMaxProgress = obtainStyledAttributes.getInteger(R$styleable.MagicBubbleSeekBar_bsb_max_progress, 100);
        this.mMinProgress = obtainStyledAttributes.getInteger(R$styleable.MagicBubbleSeekBar_bsb_min_progress, 0);
        this.mSliderWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagicBubbleSeekBar_bsb_slider_width, 0);
        this.mCurrentProgress = obtainStyledAttributes.getFloat(R$styleable.MagicBubbleSeekBar_bsb_current_progress, 0.0f);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagicBubbleSeekBar_bsb_empty_progress_height, 50);
        this.mEmptyProgressHeight = dimensionPixelSize;
        this.mFillProgressHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagicBubbleSeekBar_bsb_fill_progress_height, dimensionPixelSize);
        this.mHideBubble = obtainStyledAttributes.getBoolean(R$styleable.MagicBubbleSeekBar_bsb_hide_bubble, true);
        this.mSliderEenlargeHide = obtainStyledAttributes.getBoolean(R$styleable.MagicBubbleSeekBar_bsb_slider_enlarge_hide, false);
        this.mBsbNormalWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagicBubbleSeekBar_bsb_normal_width, 100);
        this.mBsbVisibilityWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagicBubbleSeekBar_bsb_visibility_width, 100);
        this.mBubbleSeekbarDistance = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagicBubbleSeekBar_bsb_bubble_seekbar_distance, 0);
        this.mBsbVisibilityHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagicBubbleSeekBar_bsb_visibility_height, 100);
        obtainStyledAttributes.recycle();
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
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.ts_seek_bar_bubble_window, (ViewGroup) null);
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
        this.mOffsetY = (size - this.mBsbVisibilityHeight) / 2;
        this.mOffsetX = (this.mWidth - this.mBsbVisibilityWidth) / 2;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        float f;
        float f2;
        int i3;
        float f3;
        float f4;
        int i4;
        int i5;
        super.onDraw(canvas);
        int i6 = this.mBsbVisibilityHeight;
        float f5 = (i6 - this.mCurrentEmptyProgressHeight) / 2.0f;
        float f6 = (i6 - this.mCurrentFillProgressHeight) / 2.0f;
        float f7 = (this.mBsbVisibilityWidth - this.mCurrentEmptyProgressWidth) / 2.0f;
        if (this.mBubble.isShowing() && !this.mHideBubble) {
            ViewGroup.LayoutParams layoutParams = this.mPopWindowContentView.getLayoutParams();
            int i7 = this.mBsbVisibilityWidth;
            int i8 = this.mBsbVisibilityHeight;
            Float valueOf = Float.valueOf((((i7 - i8) * ((this.mCurrentProgress + this.mDeviationProgress) - this.mMinProgress)) / (this.mMaxProgress - i5)) + (i8 / 2));
            int i9 = this.mPopWidowSize;
            int floatValue = (int) ((this.mOffsetX + valueOf.floatValue()) - (i9 / 2));
            this.mPopX = floatValue;
            int i10 = -((((this.mBsbVisibilityHeight + this.mOffsetY) + i9) + this.mBubbleY) - this.mPopWidowPadding);
            this.mPopY = i10;
            this.mBubble.update(this, floatValue, i10, i9, i9);
            int i11 = this.mPopWindowContentViewSize;
            layoutParams.width = i11;
            layoutParams.height = i11;
            this.mPopWindowContentView.setLayoutParams(layoutParams);
            this.mPopWindowContentView.setAlpha(this.mPopWindowContentViewAlpha);
        }
        Resources resources = getContext().getResources();
        if (this.mIsPress) {
            i = R$dimen.magic_bubble_seek_bar_slider_to_fill_gap_highlight;
        } else {
            i = R$dimen.magic_bubble_seek_bar_slider_to_fill_gap_normal;
        }
        int dimensionPixelSize = resources.getDimensionPixelSize(i);
        this.mBsbSliderToFillGap = dimensionPixelSize;
        float f8 = ((this.mCurrentProgress + this.mDeviationProgress) - this.mMinProgress) / (this.mMaxProgress - i2);
        this.mProgressPosition = ((this.mBsbVisibilityWidth - this.mBsbVisibilityHeight) * f8) + (this.mEmptyProgressHeight / 2) + dimensionPixelSize;
        int i12 = this.mOffsetX;
        int i13 = this.mOffsetY;
        RectF rectF = new RectF(i12 + f7, i13 + f5, (this.mBsbVisibilityWidth - f7) + i12, (this.mBsbVisibilityHeight - f5) + i13);
        float f9 = this.mCurrentEmptyProgressHeight;
        canvas.drawRoundRect(rectF, f9, f9, this.mPaintEmpty);
        float f10 = this.mCurrentFillProgressHeight;
        float f11 = (f10 - (this.mBsbSliderToFillGap * 2)) / 2.0f;
        float min = Math.min(this.mProgressPosition, (this.mBsbVisibilityWidth - f6) - (f10 / 2.0f));
        this.mProgressPosition = min;
        this.mProgressPosition = Math.max(min, (this.mCurrentFillProgressHeight / 2.0f) + f6);
        int i14 = this.mStartPointProgress;
        int i15 = this.mMinProgress;
        float f12 = (i14 - i15) / (this.mMaxProgress - i15);
        this.mStartPointProportion = f12;
        if (f12 == 0.0f) {
            int i16 = this.mOffsetX;
            RectF rectF2 = new RectF(i16 + f6, this.mOffsetY + f6, i16 + this.mProgressPosition + (this.mCurrentFillProgressHeight / 2.0f) + 4.0f, (i4 + this.mBsbVisibilityHeight) - f6);
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
            RectF rectF3 = new RectF(f15, f16, f3 + (f4 / 2.0f), (i3 + this.mBsbVisibilityHeight) - f6);
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
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.magic.widget.MagicBubbleSeekBar.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mBubble.dismiss();
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
