package com.miui.mishare.app.view.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import miuix.internal.util.DeviceHelper;
import miuix.popupwidget.R$attr;
import miuix.popupwidget.R$dimen;
import miuix.popupwidget.R$id;
import miuix.popupwidget.R$style;
import miuix.popupwidget.R$styleable;

/* loaded from: classes3.dex */
public class ArrowPopupView extends FrameLayout implements View.OnTouchListener {
    public View mAnchor;
    public AnimationSet mAnimationSet;
    public AnimatorSet mAnimator;
    public AppCompatImageView mArrow;
    public Drawable mArrowBottom;
    public Drawable mArrowBottomLeft;
    public Drawable mArrowBottomRight;
    public Drawable mArrowLeft;
    public int mArrowMode;
    public ArrowPopupWindow mArrowPopupWindow;
    public Drawable mArrowRight;
    public int mArrowSpaceLeft;
    public int mArrowSpaceTop;
    public Drawable mArrowTop;
    public Drawable mArrowTopLeft;
    public Drawable mArrowTopRight;
    public Drawable mArrowTopWithTitle;
    public boolean mAutoDismiss;
    public Drawable mBackground;
    public Drawable mBackgroundLeft;
    public Drawable mBackgroundRight;
    public FrameLayout mContentFrame;
    public LinearLayout mContentFrameWrapper;
    public Animation.AnimationListener mHideAnimatorListener;
    public boolean mIsDismissing;
    public int mMinBorder;
    public AppCompatButton mNegativeButton;
    public WrapperOnClickListener mNegativeClickListener;
    public int mOffsetX;
    public int mOffsetY;
    public AppCompatButton mPositiveButton;
    public WrapperOnClickListener mPositiveClickListener;
    public Animation.AnimationListener mShowAnimationListener;
    public boolean mShowingAnimation;
    public int mSpaceLeft;
    public int mSpaceTop;
    public Drawable mTitleBackground;
    public LinearLayout mTitleLayout;
    public AppCompatTextView mTitleText;
    public Rect mTmpRect;
    public RectF mTmpRectF;
    public View.OnTouchListener mTouchInterceptor;
    public int mTranslationValue;

    @Deprecated
    public float getRollingPercent() {
        return 1.0f;
    }

    @Deprecated
    public void setRollingPercent(float f) {
    }

    public ArrowPopupView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.arrowPopupViewStyle);
    }

    public ArrowPopupView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTmpRect = new Rect();
        this.mTmpRectF = new RectF();
        this.mAutoDismiss = true;
        this.mShowingAnimation = false;
        this.mShowAnimationListener = new Animation.AnimationListener() { // from class: com.miui.mishare.app.view.popup.ArrowPopupView.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                ArrowPopupView.this.mAnimationSet = null;
                if (ArrowPopupView.this.mShowingAnimation) {
                    ArrowPopupView.this.animateShowing();
                }
            }
        };
        this.mHideAnimatorListener = new Animation.AnimationListener() { // from class: com.miui.mishare.app.view.popup.ArrowPopupView.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                ArrowPopupView.this.mIsDismissing = false;
                ArrowPopupView.this.mAnimationSet = null;
                ArrowPopupView.this.mArrowPopupWindow.dismiss();
                ArrowPopupView.this.setArrowMode(0);
            }
        };
        this.mArrowMode = 0;
        this.mAutoDismiss = true;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ArrowPopupView, i, R$style.Widget_ArrowPopupView_DayNight);
        this.mBackground = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_contentBackground);
        this.mBackgroundLeft = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_backgroundLeft);
        this.mBackgroundRight = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_backgroundRight);
        this.mTitleBackground = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_titleBackground);
        this.mArrowTop = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_topArrow);
        this.mArrowTopWithTitle = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_topArrowWithTitle);
        this.mArrowBottom = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_bottomArrow);
        this.mArrowRight = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_rightArrow);
        this.mArrowLeft = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_leftArrow);
        this.mArrowTopLeft = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_topLeftArrow);
        this.mArrowTopRight = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_topRightArrow);
        this.mArrowBottomRight = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_bottomRightArrow);
        this.mArrowBottomLeft = obtainStyledAttributes.getDrawable(R$styleable.ArrowPopupView_bottomLeftArrow);
        obtainStyledAttributes.recycle();
        this.mMinBorder = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_window_min_border);
    }

    public void setAutoDismiss(boolean z) {
        this.mAutoDismiss = z;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mArrow = (AppCompatImageView) findViewById(R$id.popup_arrow);
        this.mContentFrame = (FrameLayout) findViewById(16908290);
        LinearLayout linearLayout = (LinearLayout) findViewById(R$id.content_wrapper);
        this.mContentFrameWrapper = linearLayout;
        linearLayout.setBackground(this.mBackground);
        this.mContentFrameWrapper.setMinimumHeight(getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_view_min_height));
        if (this.mBackgroundLeft != null && this.mBackgroundRight != null) {
            Rect rect = new Rect();
            this.mBackgroundLeft.getPadding(rect);
            LinearLayout linearLayout2 = this.mContentFrameWrapper;
            int i = rect.top;
            linearLayout2.setPadding(i, i, i, i);
        }
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R$id.title_layout);
        this.mTitleLayout = linearLayout3;
        linearLayout3.setBackground(this.mTitleBackground);
        this.mTitleText = (AppCompatTextView) findViewById(16908310);
        this.mPositiveButton = (AppCompatButton) findViewById(16908314);
        this.mNegativeButton = (AppCompatButton) findViewById(16908313);
        this.mPositiveClickListener = new WrapperOnClickListener();
        this.mNegativeClickListener = new WrapperOnClickListener();
        this.mPositiveButton.setOnClickListener(this.mPositiveClickListener);
        this.mNegativeButton.setOnClickListener(this.mNegativeClickListener);
    }

    public void addShadow() {
        addShadow(this.mArrow, new ViewOutlineProvider() { // from class: com.miui.mishare.app.view.popup.ArrowPopupView.3
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                int i;
                if (view.getWidth() == 0 || view.getHeight() == 0) {
                    return;
                }
                int width = view.getWidth();
                int height = view.getHeight();
                Rect rect = new Rect(0, 0, width, height);
                if (width > height) {
                    int i2 = (width - height) / 2;
                    rect.left += i2;
                    rect.right -= i2;
                } else {
                    int i3 = (height - width) / 2;
                    rect.top += i3;
                    rect.bottom -= i3;
                }
                Path path = new Path();
                int i4 = ArrowPopupView.this.mArrowMode;
                if (i4 == 32) {
                    path.moveTo(rect.right, rect.top);
                    path.quadTo(-rect.width(), (rect.bottom + rect.top) / 2.0f, rect.right, rect.bottom);
                } else if (i4 != 64) {
                    switch (i4) {
                        case 8:
                            int i5 = rect.right;
                            path.moveTo(rect.left, rect.bottom);
                            path.quadTo((i5 + i) / 2.0f, -rect.height(), rect.right, rect.bottom);
                            path.close();
                            break;
                        case 9:
                            path.moveTo(0.0f, ArrowPopupView.this.mArrowTop.getIntrinsicHeight());
                            path.quadTo(0.0f, (-ArrowPopupView.this.mArrowTop.getIntrinsicHeight()) * 0.7f, rect.right, ArrowPopupView.this.mArrowTop.getIntrinsicHeight());
                            path.close();
                            break;
                        case 10:
                            path.moveTo(0.0f, ArrowPopupView.this.mArrowTop.getIntrinsicHeight());
                            path.quadTo(rect.right, (-ArrowPopupView.this.mArrowTop.getIntrinsicHeight()) * 0.7f, rect.right, ArrowPopupView.this.mArrowTop.getIntrinsicHeight());
                            path.close();
                            break;
                    }
                } else {
                    path.moveTo(rect.left, rect.top);
                    path.quadTo(rect.right + rect.width(), (rect.bottom + rect.top) / 2.0f, rect.left, rect.bottom);
                    path.close();
                }
                if (path.isConvex()) {
                    outline.setConvexPath(path);
                    return;
                }
                Log.d("ArrowPopupView", "outline path is not convex");
                outline.setOval(rect);
            }
        });
        addShadow(this.mContentFrameWrapper, new ViewOutlineProvider() { // from class: com.miui.mishare.app.view.popup.ArrowPopupView.4
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                if (view.getWidth() == 0 || view.getHeight() == 0) {
                    return;
                }
                Rect rect = new Rect(0, 0, view.getWidth(), view.getHeight());
                rect.bottom -= view.getPaddingBottom();
                rect.top += view.getPaddingTop();
                rect.right -= view.getPaddingRight();
                rect.left += view.getPaddingLeft();
                outline.setRoundRect(rect, ArrowPopupView.this.getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_view_round_corners));
            }
        });
    }

    public final void addShadow(View view, ViewOutlineProvider viewOutlineProvider) {
        view.setOutlineProvider(viewOutlineProvider);
        view.setElevation(getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_arrow_popup_window_elevation) * 2);
        if (Build.VERSION.SDK_INT >= 28) {
            view.setOutlineSpotShadowColor(Color.argb(128, 0, 0, 0));
            view.setOutlineAmbientShadowColor(Color.argb(128, 0, 0, 0));
        }
    }

    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(-2, -2));
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mContentFrame.removeAllViews();
        if (view != null) {
            this.mContentFrame.addView(view, layoutParams);
        }
    }

    public View getContentView() {
        if (this.mContentFrame.getChildCount() > 0) {
            return this.mContentFrame.getChildAt(0);
        }
        return null;
    }

    public void setContentView(int i) {
        setContentView(LayoutInflater.from(getContext()).inflate(i, (ViewGroup) null));
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitleLayout.setVisibility(TextUtils.isEmpty(charSequence) ? 8 : 0);
        this.mTitleText.setText(charSequence);
    }

    public AppCompatButton getPositiveButton() {
        return this.mPositiveButton;
    }

    public AppCompatButton getNegativeButton() {
        return this.mNegativeButton;
    }

    public final boolean isVerticalMode() {
        return isTopMode() || isBottomMode();
    }

    public final boolean isCertainMode(int i) {
        return (this.mArrowMode & i) == i;
    }

    public final boolean isLeftMode() {
        return isCertainMode(32);
    }

    public final boolean isRightMode() {
        return isCertainMode(64);
    }

    public final boolean isTopMode() {
        return isCertainMode(8);
    }

    public final boolean isBottomMode() {
        return isCertainMode(16);
    }

    public final void arrowLayout() {
        if (isVerticalMode()) {
            arrowVerticalLayout();
        } else {
            arrowHorizontalLayout();
        }
        View contentView = getContentView();
        if (contentView != null) {
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            if (contentView.getMeasuredHeight() > this.mContentFrameWrapper.getMeasuredHeight() - this.mTitleLayout.getMeasuredHeight()) {
                layoutParams.height = this.mContentFrameWrapper.getMeasuredHeight() - this.mTitleLayout.getMeasuredHeight();
                contentView.setLayoutParams(layoutParams);
            } else if (contentView.getMeasuredWidth() > this.mContentFrameWrapper.getMeasuredWidth()) {
                layoutParams.width = this.mContentFrameWrapper.getMeasuredWidth();
                contentView.setLayoutParams(layoutParams);
            }
            if (layoutParams.height > 0 && layoutParams.width > 0) {
                return;
            }
            Log.w("ArrowPopupView", "Invalid LayoutPrams of content view, please check the anchor view");
        }
    }

    public final void arrowVerticalLayout() {
        int i;
        int width = this.mAnchor.getWidth();
        int height = this.mAnchor.getHeight();
        int width2 = getWidth();
        int height2 = getHeight();
        int max = Math.max(this.mContentFrameWrapper.getMeasuredWidth(), this.mContentFrameWrapper.getMinimumWidth());
        int max2 = Math.max(this.mContentFrameWrapper.getMeasuredHeight(), this.mContentFrameWrapper.getMinimumHeight());
        int arrowWidth = getArrowWidth();
        int arrowHeight = getArrowHeight();
        int[] iArr = new int[2];
        this.mAnchor.getLocationOnScreen(iArr);
        int i2 = iArr[0];
        int i3 = iArr[1];
        getLocationOnScreen(iArr);
        int i4 = ((width / 2) + i2) - iArr[0];
        this.mSpaceLeft = i4;
        int i5 = width2 - i4;
        this.mArrowSpaceLeft = (i2 + ((width - arrowWidth) / 2)) - iArr[0];
        this.mSpaceTop = getTop() + this.mOffsetY;
        if (isBottomMode()) {
            this.mSpaceTop += ((i3 - iArr[1]) - max2) + (this.mContentFrameWrapper.getPaddingBottom() - arrowHeight);
            i = (((i3 - iArr[1]) - arrowHeight) + this.mOffsetY) - 1;
        } else if (isTopMode()) {
            int paddingTop = this.mSpaceTop + (((i3 + height) - iArr[1]) - this.mContentFrameWrapper.getPaddingTop()) + arrowHeight;
            this.mSpaceTop = paddingTop;
            i = paddingTop + (this.mContentFrameWrapper.getPaddingTop() - arrowHeight) + 1;
        } else {
            i = 0;
        }
        int i6 = max / 2;
        int i7 = max - i6;
        int i8 = this.mSpaceLeft;
        if (i8 >= i6 && i5 >= i7) {
            this.mSpaceLeft = i8 - i6;
        } else if (i5 < i7) {
            this.mSpaceLeft = width2 - max;
        } else {
            this.mSpaceLeft = 0;
        }
        int i9 = this.mSpaceLeft;
        int i10 = this.mOffsetX;
        int i11 = i9 + i10;
        this.mSpaceLeft = i11;
        int i12 = this.mArrowSpaceLeft + i10;
        this.mArrowSpaceLeft = i12;
        if (i12 < 0) {
            this.mArrowSpaceLeft = 0;
        } else if (i12 + arrowWidth > width2) {
            this.mArrowSpaceLeft = i12 - ((i12 + arrowWidth) - width2);
        }
        this.mContentFrameWrapper.layout(Math.max(i11, 0), Math.max(this.mSpaceTop, 0), Math.min(this.mSpaceLeft + max, width2), Math.min(this.mSpaceTop + max2, height2));
        executeLayoutArrow(arrowWidth, arrowHeight, i);
    }

    public final void executeLayoutArrow(int i, int i2, int i3) {
        int left;
        int i4;
        int right;
        int bottom;
        int i5 = this.mArrowMode;
        if (i5 == 9) {
            left = (this.mContentFrameWrapper.getLeft() + this.mContentFrameWrapper.getPaddingStart()) - 1;
            i3 = (i3 + this.mContentFrameWrapper.getPaddingTop()) - i2;
            AppCompatImageView appCompatImageView = this.mArrow;
            appCompatImageView.layout(left, i3, left + i, appCompatImageView.getMeasuredHeight() + i3);
        } else if (i5 == 10) {
            left = ((this.mContentFrameWrapper.getRight() - this.mContentFrameWrapper.getPaddingEnd()) - i) + 1;
            i3 = (i3 + this.mContentFrameWrapper.getPaddingTop()) - i2;
            AppCompatImageView appCompatImageView2 = this.mArrow;
            appCompatImageView2.layout(left, i3, left + i, appCompatImageView2.getMeasuredHeight() + i3);
        } else {
            if (i5 == 17) {
                right = (this.mContentFrameWrapper.getRight() - this.mContentFrameWrapper.getPaddingEnd()) - i;
                bottom = (this.mContentFrameWrapper.getBottom() - this.mContentFrameWrapper.getPaddingBottom()) - (this.mArrow.getMeasuredHeight() - i2);
            } else if (i5 == 18) {
                right = this.mContentFrameWrapper.getLeft() + this.mContentFrameWrapper.getPaddingStart();
                bottom = (this.mContentFrameWrapper.getBottom() - this.mContentFrameWrapper.getPaddingBottom()) - (this.mArrow.getMeasuredHeight() - i2);
                AppCompatImageView appCompatImageView3 = this.mArrow;
                appCompatImageView3.layout(right, bottom, right + i, appCompatImageView3.getMeasuredHeight() + bottom);
            } else {
                i4 = this.mArrowSpaceLeft;
                AppCompatImageView appCompatImageView4 = this.mArrow;
                appCompatImageView4.layout(i4, i3, i + i4, appCompatImageView4.getDrawable().getIntrinsicHeight() + i3);
            }
            int i6 = right;
            i3 = bottom - 5;
            i4 = i6;
            AppCompatImageView appCompatImageView42 = this.mArrow;
            appCompatImageView42.layout(i4, i3, i + i4, appCompatImageView42.getDrawable().getIntrinsicHeight() + i3);
        }
        i4 = left;
        AppCompatImageView appCompatImageView422 = this.mArrow;
        appCompatImageView422.layout(i4, i3, i + i4, appCompatImageView422.getDrawable().getIntrinsicHeight() + i3);
    }

    public final void arrowHorizontalLayout() {
        int i;
        int[] iArr = new int[2];
        this.mAnchor.getLocationOnScreen(iArr);
        int i2 = iArr[0];
        int i3 = iArr[1];
        getLocationOnScreen(iArr);
        int width = this.mAnchor.getWidth();
        int height = this.mAnchor.getHeight();
        int width2 = getWidth();
        int height2 = getHeight();
        int max = Math.max(this.mContentFrameWrapper.getMeasuredWidth(), this.mContentFrameWrapper.getMinimumWidth());
        int max2 = Math.max(this.mContentFrameWrapper.getMeasuredHeight(), this.mContentFrameWrapper.getMinimumHeight());
        int arrowWidth = getArrowWidth();
        int arrowHeight = getArrowHeight();
        int i4 = ((height / 2) + i3) - iArr[1];
        this.mSpaceTop = i4;
        int i5 = height2 - i4;
        this.mArrowSpaceTop = ((i3 + ((height - arrowHeight) / 2)) - iArr[1]) + ((this.mContentFrameWrapper.getPaddingTop() - this.mContentFrameWrapper.getPaddingBottom()) / 2);
        int i6 = max2 / 2;
        int i7 = max2 - i6;
        this.mSpaceLeft = getLeft() + this.mOffsetX;
        if (isRightMode()) {
            this.mSpaceLeft += (((i2 - max) + this.mContentFrameWrapper.getPaddingRight()) - arrowWidth) - iArr[0];
            i = (((i2 - arrowWidth) - iArr[0]) + this.mOffsetX) - 1;
        } else if (isLeftMode()) {
            int paddingLeft = this.mSpaceLeft + ((((i2 + width) - this.mContentFrameWrapper.getPaddingLeft()) + arrowWidth) - iArr[0]);
            this.mSpaceLeft = paddingLeft;
            i = paddingLeft + (this.mContentFrameWrapper.getPaddingLeft() - arrowWidth) + 1;
        } else {
            i = 0;
        }
        int i8 = this.mSpaceTop;
        if (i8 >= i6 && i5 >= i7) {
            this.mSpaceTop = (i8 - i6) + this.mOffsetY;
        } else if (i5 < i7) {
            this.mSpaceTop = (height2 - max2) + this.mOffsetY;
        } else {
            this.mSpaceTop = this.mOffsetY;
        }
        int i9 = this.mArrowSpaceTop + this.mOffsetY;
        this.mArrowSpaceTop = i9;
        if (i9 < 0) {
            this.mArrowSpaceTop = 0;
        } else if (i9 + arrowHeight > height2) {
            this.mArrowSpaceTop = i9 - ((i9 + arrowHeight) - height2);
        }
        this.mContentFrameWrapper.layout(Math.max(this.mSpaceLeft, 0), Math.max(this.mSpaceTop, 0), Math.min(this.mSpaceLeft + max, width2), Math.min(this.mSpaceTop + max2, height2));
        AppCompatImageView appCompatImageView = this.mArrow;
        int i10 = this.mArrowSpaceTop;
        appCompatImageView.layout(i, i10, arrowWidth + i, arrowHeight + i10);
    }

    private int getArrowWidth() {
        int measuredWidth = this.mArrow.getMeasuredWidth();
        return measuredWidth == 0 ? this.mArrow.getDrawable().getIntrinsicWidth() : measuredWidth;
    }

    private int getArrowHeight() {
        int i = this.mArrowMode;
        if (i == 9 || i == 10) {
            return this.mArrowTop.getIntrinsicHeight();
        }
        if (i == 17 || i == 18) {
            return this.mArrowBottom.getIntrinsicHeight();
        }
        int measuredHeight = this.mArrow.getMeasuredHeight();
        return measuredHeight == 0 ? this.mArrow.getDrawable().getIntrinsicHeight() : measuredHeight;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!this.mAnchor.isAttachedToWindow()) {
            if (!this.mArrowPopupWindow.isShowing()) {
                return;
            }
            this.mArrowPopupWindow.dismiss();
            return;
        }
        if (this.mArrowMode == 0) {
            adjustArrowMode();
        }
        arrowLayout();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int i;
        int right;
        float f;
        int measuredWidth;
        int i2;
        if (this.mBackground != null) {
            return;
        }
        int width = this.mSpaceLeft + (this.mContentFrameWrapper.getWidth() / 2);
        int height = this.mSpaceTop + (this.mContentFrameWrapper.getHeight() / 2);
        int i3 = this.mArrowMode;
        if (i3 != 8) {
            if (i3 == 16) {
                f = 180.0f;
                measuredWidth = this.mArrowSpaceLeft + (this.mArrow.getMeasuredWidth() / 2);
                i = this.mContentFrameWrapper.getRight() - measuredWidth;
                i2 = this.mSpaceLeft;
            } else if (i3 == 32) {
                f = -90.0f;
                measuredWidth = this.mArrowSpaceTop + (this.mArrow.getMeasuredHeight() / 2);
                i = this.mContentFrameWrapper.getBottom() - measuredWidth;
                i2 = this.mSpaceTop;
            } else if (i3 != 64) {
                f = 0.0f;
                i = 0;
                right = 0;
            } else {
                f = 90.0f;
                int measuredHeight = this.mArrowSpaceTop + (this.mArrow.getMeasuredHeight() / 2);
                i = measuredHeight - this.mSpaceTop;
                right = this.mContentFrameWrapper.getBottom() - measuredHeight;
            }
            right = measuredWidth - i2;
        } else {
            int measuredWidth2 = this.mArrowSpaceLeft + (this.mArrow.getMeasuredWidth() / 2);
            i = measuredWidth2 - this.mSpaceLeft;
            right = this.mContentFrameWrapper.getRight() - measuredWidth2;
            f = 0.0f;
        }
        int save = canvas.save();
        float f2 = width;
        float f3 = height;
        canvas.rotate(f, f2, f3);
        int i4 = this.mArrowMode;
        if (i4 == 8 || i4 == 16) {
            canvas.translate(this.mSpaceLeft, this.mSpaceTop);
            this.mBackgroundLeft.setBounds(0, 0, i, this.mContentFrameWrapper.getHeight());
            canvas.translate(0.0f, isTopMode() ? this.mTranslationValue : -this.mTranslationValue);
            this.mBackgroundLeft.draw(canvas);
            canvas.translate(i, 0.0f);
            this.mBackgroundRight.setBounds(0, 0, right, this.mContentFrameWrapper.getHeight());
            this.mBackgroundRight.draw(canvas);
        } else if (i4 == 32 || i4 == 64) {
            canvas.translate(f2 - (this.mContentFrameWrapper.getHeight() / 2.0f), f3 - (this.mContentFrameWrapper.getWidth() / 2.0f));
            this.mBackgroundLeft.setBounds(0, 0, i, this.mContentFrameWrapper.getWidth());
            canvas.translate(0.0f, isLeftMode() ? this.mTranslationValue : -this.mTranslationValue);
            this.mBackgroundLeft.draw(canvas);
            canvas.translate(i, 0.0f);
            this.mBackgroundRight.setBounds(0, 0, right, this.mContentFrameWrapper.getWidth());
            this.mBackgroundRight.draw(canvas);
        }
        canvas.restoreToCount(save);
    }

    public final void getAnimationPivot(float[] fArr) {
        float f;
        float f2;
        int top = this.mArrow.getTop();
        int bottom = this.mArrow.getBottom();
        int left = this.mArrow.getLeft();
        int right = this.mArrow.getRight();
        int i = this.mArrowMode;
        if (i == 32) {
            f = left;
        } else if (i != 64) {
            switch (i) {
                case 8:
                    f = (right + left) / 2.0f;
                    f2 = top;
                    break;
                case 9:
                    f = left;
                    f2 = top;
                    break;
                case 10:
                    f = right;
                    f2 = top;
                    break;
                default:
                    switch (i) {
                        case 16:
                            f = (right + left) / 2.0f;
                            f2 = bottom;
                            break;
                        case 17:
                            f = right;
                            f2 = bottom;
                            break;
                        case 18:
                            f = left;
                            f2 = bottom;
                            break;
                        default:
                            f = (right + left) / 2.0f;
                            break;
                    }
            }
            fArr[0] = f;
            fArr[1] = f2;
        } else {
            f = right;
        }
        f2 = (bottom + top) / 2.0f;
        fArr[0] = f;
        fArr[1] = f2;
    }

    public void animateToShow() {
        invalidate();
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.miui.mishare.app.view.popup.ArrowPopupView.5
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                ArrowPopupView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                if (ArrowPopupView.this.mAnimator != null) {
                    ArrowPopupView.this.mAnimator.cancel();
                }
                if (ArrowPopupView.this.mAnimationSet != null) {
                    ArrowPopupView.this.mAnimationSet.cancel();
                }
                ArrowPopupView.this.mAnimationSet = new AnimationSet(true);
                float[] fArr = new float[2];
                ArrowPopupView.this.getAnimationPivot(fArr);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f, 0, fArr[0], 0, fArr[1]);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                if (!DeviceHelper.isFeatureWholeAnim()) {
                    ArrowPopupView.this.mAnimationSet.setDuration(0L);
                } else {
                    scaleAnimation.setDuration(280L);
                    alphaAnimation.setDuration(280L);
                }
                ArrowPopupView.this.mAnimationSet.addAnimation(scaleAnimation);
                ArrowPopupView.this.mAnimationSet.addAnimation(alphaAnimation);
                ArrowPopupView.this.mAnimationSet.setAnimationListener(ArrowPopupView.this.mShowAnimationListener);
                ArrowPopupView.this.mAnimationSet.setInterpolator(new DecelerateInterpolator(2.0f));
                ArrowPopupView arrowPopupView = ArrowPopupView.this;
                arrowPopupView.startAnimation(arrowPopupView.mAnimationSet);
                return true;
            }
        });
    }

    public void animateToDismiss() {
        if (this.mIsDismissing) {
            return;
        }
        AnimatorSet animatorSet = this.mAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimationSet animationSet = this.mAnimationSet;
        if (animationSet != null) {
            animationSet.cancel();
        }
        this.mAnimationSet = new AnimationSet(true);
        float[] fArr = new float[2];
        getAnimationPivot(fArr);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f, 0, fArr[0], 0, fArr[1]);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        if (DeviceHelper.isFeatureWholeAnim()) {
            scaleAnimation.setDuration(150L);
            alphaAnimation.setDuration(150L);
        } else {
            this.mAnimationSet.setDuration(0L);
        }
        this.mAnimationSet.addAnimation(scaleAnimation);
        this.mAnimationSet.addAnimation(alphaAnimation);
        this.mAnimationSet.setAnimationListener(this.mHideAnimatorListener);
        this.mAnimationSet.setInterpolator(new AccelerateInterpolator(2.0f));
        startAnimation(this.mAnimationSet);
    }

    public final void animateShowing() {
        if (DeviceHelper.isFeatureWholeAnim()) {
            AnimationSet animationSet = this.mAnimationSet;
            if (animationSet != null) {
                animationSet.cancel();
            }
            AnimatorSet animatorSet = this.mAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.mAnimator = animatorSet2;
            animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: com.miui.mishare.app.view.popup.ArrowPopupView.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ArrowPopupView.this.mArrowPopupWindow.dismiss();
                }
            });
            float f = getContext().getResources().getDisplayMetrics().density * 4.0f;
            Property property = View.TRANSLATION_Y;
            int i = this.mArrowMode;
            if (i == 16) {
                f = -f;
            } else if (i == 32) {
                property = View.TRANSLATION_X;
            } else if (i == 64) {
                f = -f;
                property = View.TRANSLATION_X;
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mContentFrameWrapper, property, 0.0f, f, 0.0f);
            ofFloat.setInterpolator(new AccelerateDecelerateInterpolator());
            ofFloat.setDuration(1200L);
            if (this.mAutoDismiss) {
                ofFloat.setRepeatCount(8);
            } else {
                ofFloat.setRepeatCount(-1);
            }
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.mishare.app.view.popup.ArrowPopupView.7
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ArrowPopupView.this.mTranslationValue = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    int abs = Math.abs(ArrowPopupView.this.mTranslationValue);
                    ArrowPopupView arrowPopupView = ArrowPopupView.this;
                    arrowPopupView.invalidate(arrowPopupView.mContentFrameWrapper.getLeft() - abs, ArrowPopupView.this.mContentFrameWrapper.getTop() - abs, ArrowPopupView.this.mContentFrameWrapper.getRight() + abs, ArrowPopupView.this.mContentFrameWrapper.getBottom() + abs);
                }
            });
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mArrow, property, 0.0f, f, 0.0f);
            ofFloat2.setInterpolator(new AccelerateDecelerateInterpolator());
            ofFloat2.setDuration(1200L);
            if (this.mAutoDismiss) {
                ofFloat2.setRepeatCount(8);
            } else {
                ofFloat2.setRepeatCount(-1);
            }
            this.mAnimator.playTogether(ofFloat, ofFloat2);
            this.mAnimator.start();
        }
    }

    public final void adjustArrowMode() {
        int[] iArr = new int[2];
        this.mAnchor.getLocationInWindow(iArr);
        int width = getWidth();
        int height = getHeight();
        int measuredWidth = this.mContentFrameWrapper.getMeasuredWidth();
        int measuredHeight = this.mContentFrameWrapper.getMeasuredHeight();
        int height2 = this.mAnchor.getHeight();
        int width2 = this.mAnchor.getWidth();
        SparseIntArray sparseIntArray = new SparseIntArray(4);
        int i = 16;
        sparseIntArray.put(16, iArr[1] - measuredHeight);
        sparseIntArray.put(8, ((height - iArr[1]) - height2) - measuredHeight);
        int i2 = 0;
        sparseIntArray.put(64, iArr[0] - measuredWidth);
        sparseIntArray.put(32, ((width - iArr[0]) - width2) - measuredWidth);
        int i3 = Integer.MIN_VALUE;
        while (true) {
            if (i2 >= sparseIntArray.size()) {
                break;
            }
            int keyAt = sparseIntArray.keyAt(i2);
            if (sparseIntArray.get(keyAt) >= this.mMinBorder) {
                i = keyAt;
                break;
            }
            if (sparseIntArray.get(keyAt) > i3) {
                i3 = sparseIntArray.get(keyAt);
                i = keyAt;
            }
            i2++;
        }
        setArrowMode(i);
    }

    public int getArrowMode() {
        return this.mArrowMode;
    }

    public void setArrowMode(int i) {
        this.mArrowMode = i;
        if (i == 32) {
            this.mArrow.setImageDrawable(this.mArrowLeft);
        } else if (i != 64) {
            switch (i) {
                case 8:
                    this.mArrow.setImageDrawable(this.mTitleLayout.getVisibility() == 0 ? this.mArrowTopWithTitle : this.mArrowTop);
                    return;
                case 9:
                    this.mArrow.setImageDrawable(this.mArrowTopLeft);
                    return;
                case 10:
                    this.mArrow.setImageDrawable(this.mArrowTopRight);
                    return;
                default:
                    switch (i) {
                        case 16:
                            this.mArrow.setImageDrawable(this.mArrowBottom);
                            return;
                        case 17:
                            this.mArrow.setImageDrawable(this.mArrowBottomRight);
                            return;
                        case 18:
                            this.mArrow.setImageDrawable(this.mArrowBottomLeft);
                            return;
                        default:
                            return;
                    }
            }
        } else {
            this.mArrow.setImageDrawable(this.mArrowRight);
        }
    }

    public void setAnchor(View view) {
        this.mAnchor = view;
    }

    public void setOffset(int i, int i2) {
        this.mOffsetX = i;
        this.mOffsetY = i2;
    }

    public void setArrowPopupWindow(ArrowPopupWindow arrowPopupWindow) {
        this.mArrowPopupWindow = arrowPopupWindow;
    }

    public void setTouchInterceptor(View.OnTouchListener onTouchListener) {
        this.mTouchInterceptor = onTouchListener;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        Rect rect = this.mTmpRect;
        this.mContentFrameWrapper.getHitRect(rect);
        if (motionEvent.getAction() == 0 && !rect.contains(x, y)) {
            this.mArrowPopupWindow.dismiss(true);
            return true;
        }
        View.OnTouchListener onTouchListener = this.mTouchInterceptor;
        return onTouchListener != null && onTouchListener.onTouch(view, motionEvent);
    }

    public void enableShowingAnimation(boolean z) {
        this.mShowingAnimation = z;
    }

    /* loaded from: classes3.dex */
    public class WrapperOnClickListener implements View.OnClickListener {
        public View.OnClickListener mOnClickListener;

        public WrapperOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            View.OnClickListener onClickListener = this.mOnClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
            ArrowPopupView.this.mArrowPopupWindow.dismiss(true);
        }
    }
}
