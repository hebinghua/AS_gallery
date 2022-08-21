package com.miui.gallery.editor.photo.app.doodle;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.miui.gallery.R;
import java.util.List;
import miuix.view.animation.SineEaseOutInterpolator;

/* loaded from: classes2.dex */
public class PaintSelectView extends View {
    public Rect mBasePaintRect;
    public Context mContext;
    public int mCurrentPaintIndex;
    public List<DoodlePaintItem> mDoodlePaintItemList;
    public GesListener mGesListener;
    public GestureDetector mGestureDetector;
    public Handler mHandler;
    public DecelerateInterpolator mInterpolator;
    public boolean mIsSelectMode;
    public int mItemOffset;
    public ValueAnimator mPaintAnimator;
    public PaintAnimatorListener mPaintAnimatorListener;
    public int mPaintBottomOffset;
    public int mPaintDrawableWidth;
    public int mPaintRightOffset;
    public PaintSizeChangeListener mPaintSizeChangeListener;

    /* loaded from: classes2.dex */
    public interface PaintSizeChangeListener {
        void onPaintSizeChange(float f);
    }

    public PaintSelectView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPaintAnimatorListener = new PaintAnimatorListener();
        this.mInterpolator = new DecelerateInterpolator();
        this.mCurrentPaintIndex = 1;
        this.mBasePaintRect = new Rect();
        this.mGesListener = new GesListener();
        this.mIsSelectMode = false;
        this.mHandler = new Handler();
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        initPaint();
        GestureDetector gestureDetector = new GestureDetector(this.mContext, this.mGesListener);
        this.mGestureDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
    }

    public final void initPaint() {
        List<DoodlePaintItem> list = DoodlePaintItem.getList(getResources());
        this.mDoodlePaintItemList = list;
        this.mPaintDrawableWidth = list.get(0).getIntrinsicWidth();
        this.mPaintRightOffset = this.mContext.getResources().getDimensionPixelSize(R.dimen.doodle_paint_select_right_offset);
        this.mPaintBottomOffset = this.mContext.getResources().getDimensionPixelSize(R.dimen.doodle_paint_select_bottom_offset);
        this.mDoodlePaintItemList.get(this.mCurrentPaintIndex).setSelect(true);
        this.mItemOffset = this.mContext.getResources().getDimensionPixelSize(R.dimen.doodle_paint_item_offset);
    }

    public final void setPaintLocation(int i, int i2) {
        Rect rect = this.mBasePaintRect;
        int i3 = this.mPaintDrawableWidth;
        rect.set(i - i3, i2 - i3, i, i2);
        this.mBasePaintRect.offset(-this.mPaintRightOffset, -this.mPaintBottomOffset);
        for (int i4 = 0; i4 < this.mDoodlePaintItemList.size(); i4++) {
            this.mDoodlePaintItemList.get(i4).setBounds(this.mBasePaintRect);
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setPaintLocation(i, i2);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        drawPaint(canvas);
    }

    public final void drawPaint(Canvas canvas) {
        for (int i = 0; i < this.mDoodlePaintItemList.size(); i++) {
            if (i != this.mCurrentPaintIndex) {
                this.mDoodlePaintItemList.get(i).draw(canvas);
            }
        }
        this.mDoodlePaintItemList.get(this.mCurrentPaintIndex).draw(canvas);
    }

    /* loaded from: classes2.dex */
    public class GesListener implements GestureDetector.OnGestureListener {
        public int mIndex;

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        public GesListener() {
            this.mIndex = -1;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            int findTouchIndex = PaintSelectView.this.findTouchIndex(motionEvent.getX(), motionEvent.getY());
            this.mIndex = findTouchIndex;
            if (findTouchIndex == -1) {
                PaintSelectView.this.expansionPaint(false);
                return false;
            }
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (this.mIndex == -1) {
                return false;
            }
            if (PaintSelectView.this.mIsSelectMode) {
                PaintSelectView.this.setSelectIndex(this.mIndex);
            } else {
                PaintSelectView.this.expansionPaint(true);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSelectIndex(int i) {
        this.mDoodlePaintItemList.get(this.mCurrentPaintIndex).setSelect(false);
        this.mCurrentPaintIndex = i;
        DoodlePaintItem doodlePaintItem = this.mDoodlePaintItemList.get(i);
        doodlePaintItem.setSelect(true);
        PaintSizeChangeListener paintSizeChangeListener = this.mPaintSizeChangeListener;
        if (paintSizeChangeListener != null) {
            paintSizeChangeListener.onPaintSizeChange(doodlePaintItem.paintType.paintSize);
            this.mHandler.removeCallbacksAndMessages(null);
        }
        invalidate();
        doSelectAnimator(doodlePaintItem);
    }

    public final void doSelectAnimator(final DoodlePaintItem doodlePaintItem) {
        ValueAnimator doodlePaintItemSizeAnim = getDoodlePaintItemSizeAnim(doodlePaintItem, 1.0f, 1.1f);
        doodlePaintItemSizeAnim.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.editor.photo.app.doodle.PaintSelectView.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PaintSelectView.this.getDoodlePaintItemSizeAnim(doodlePaintItem, 1.1f, 1.0f).start();
            }
        });
        doodlePaintItemSizeAnim.start();
    }

    public final ValueAnimator getDoodlePaintItemSizeAnim(final DoodlePaintItem doodlePaintItem, float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(new SineEaseOutInterpolator());
        ofFloat.setDuration(150L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.app.doodle.PaintSelectView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                doodlePaintItem.setScale(((Float) valueAnimator.getAnimatedValue()).floatValue());
                PaintSelectView.this.invalidate();
            }
        });
        return ofFloat;
    }

    public final int findTouchIndex(float f, float f2) {
        for (int i = 0; i < this.mDoodlePaintItemList.size(); i++) {
            if (this.mDoodlePaintItemList.get(i).isContain(f, f2)) {
                return i;
            }
        }
        return -1;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    public void expansionPaint(boolean z) {
        int centerX;
        this.mIsSelectMode = z;
        ValueAnimator valueAnimator = this.mPaintAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        } else {
            this.mPaintAnimator = new ValueAnimator();
        }
        int size = this.mDoodlePaintItemList.size() + 1;
        PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[size];
        for (int i = 0; i < this.mDoodlePaintItemList.size(); i++) {
            int centerX2 = this.mDoodlePaintItemList.get(i).centerX();
            if (z) {
                centerX = this.mBasePaintRect.centerX() - ((this.mPaintDrawableWidth + this.mItemOffset) * i);
            } else {
                centerX = this.mBasePaintRect.centerX();
            }
            propertyValuesHolderArr[i] = PropertyValuesHolder.ofFloat(String.valueOf(i), centerX2, centerX);
        }
        if (z) {
            propertyValuesHolderArr[size - 1] = PropertyValuesHolder.ofFloat("progress", 0.0f, 1.0f);
        } else {
            propertyValuesHolderArr[size - 1] = PropertyValuesHolder.ofFloat("progress", 1.0f, 0.0f);
        }
        this.mPaintAnimator.setValues(propertyValuesHolderArr);
        this.mPaintAnimator.addUpdateListener(this.mPaintAnimatorListener);
        this.mPaintAnimator.addListener(this.mPaintAnimatorListener);
        this.mPaintAnimator.setInterpolator(this.mInterpolator);
        this.mPaintAnimator.setDuration(300L);
        this.mPaintAnimator.start();
    }

    /* loaded from: classes2.dex */
    public class PaintAnimatorListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        public PaintAnimatorListener() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            for (int i = 0; i < PaintSelectView.this.mDoodlePaintItemList.size(); i++) {
                DoodlePaintItem doodlePaintItem = (DoodlePaintItem) PaintSelectView.this.mDoodlePaintItemList.get(i);
                float floatValue = ((Float) valueAnimator.getAnimatedValue(String.valueOf(i))).floatValue();
                float floatValue2 = ((Float) valueAnimator.getAnimatedValue("progress")).floatValue();
                doodlePaintItem.offset((int) (floatValue - doodlePaintItem.centerX()), 0);
                doodlePaintItem.setAlpha(floatValue2);
            }
            PaintSelectView.this.invalidate();
        }
    }

    public void setPaintSizeChangeListener(PaintSizeChangeListener paintSizeChangeListener) {
        this.mPaintSizeChangeListener = paintSizeChangeListener;
        if (paintSizeChangeListener != null) {
            paintSizeChangeListener.onPaintSizeChange(this.mDoodlePaintItemList.get(this.mCurrentPaintIndex).paintType.paintSize);
        }
    }

    public void setPaintColor(int i) {
        for (DoodlePaintItem doodlePaintItem : this.mDoodlePaintItemList) {
            doodlePaintItem.setCurrentColor(i);
        }
        invalidate();
    }
}
