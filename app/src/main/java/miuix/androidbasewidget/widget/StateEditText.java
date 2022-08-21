package miuix.androidbasewidget.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import miuix.internal.util.ViewUtils;

/* loaded from: classes3.dex */
public class StateEditText extends EditText {
    public static final Class<?>[] WIDGET_MANAGER_CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    public Drawable[] mExtraDrawables;
    public String mLabel;
    public StaticLayout mLabelLayout;
    public int mLabelLength;
    public int mLabelMaxWidth;
    public boolean mPressed;
    public WidgetManager mWidgetManager;
    public int mWidgetPadding;

    public void setWidgetManager(WidgetManager widgetManager) {
        WidgetManager widgetManager2 = this.mWidgetManager;
        if (widgetManager2 != null) {
            widgetManager2.onDetached();
        }
        this.mWidgetManager = widgetManager;
        if (widgetManager != null) {
            widgetManager.onAttached(this);
        }
    }

    public void setLabel(String str) {
        this.mLabel = str;
        int i = 0;
        if (this.mLabelMaxWidth > 0) {
            if (!TextUtils.isEmpty(str)) {
                i = Math.min((int) getPaint().measureText(this.mLabel), this.mLabelMaxWidth);
            }
            this.mLabelLength = i;
        } else {
            if (!TextUtils.isEmpty(str)) {
                i = (int) getPaint().measureText(this.mLabel);
            }
            this.mLabelLength = i;
        }
        if (!TextUtils.isEmpty(this.mLabel)) {
            createLabelLayout();
        }
        invalidate();
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingRight() {
        int widgetLength;
        int compoundPaddingRight = super.getCompoundPaddingRight();
        if (ViewUtils.isLayoutRtl(this)) {
            widgetLength = getLabelLength();
        } else {
            widgetLength = getWidgetLength();
        }
        return compoundPaddingRight + widgetLength;
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingLeft() {
        int labelLength;
        int compoundPaddingLeft = super.getCompoundPaddingLeft();
        if (ViewUtils.isLayoutRtl(this)) {
            labelLength = getWidgetLength();
        } else {
            labelLength = getLabelLength();
        }
        return compoundPaddingLeft + labelLength;
    }

    private int getLabelLength() {
        int i = this.mLabelLength;
        return i + (i == 0 ? 0 : this.mWidgetPadding);
    }

    private int getWidgetLength() {
        Drawable[] drawableArr = this.mExtraDrawables;
        if (drawableArr != null) {
            int i = 0;
            for (Drawable drawable : drawableArr) {
                i = i + drawable.getIntrinsicWidth() + this.mWidgetPadding;
            }
            return i;
        }
        return 0;
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return dispatchEndDrawableTouchEvent(motionEvent) || super.dispatchTouchEvent(motionEvent);
    }

    public final boolean dispatchEndDrawableTouchEvent(MotionEvent motionEvent) {
        if (this.mWidgetManager != null) {
            return isWidgetResumedEvent(motionEvent);
        }
        return false;
    }

    public final boolean isWidgetResumedEvent(MotionEvent motionEvent) {
        if (this.mExtraDrawables != null) {
            int scrollX = getScrollX();
            int i = 0;
            while (true) {
                Drawable[] drawableArr = this.mExtraDrawables;
                if (i >= drawableArr.length) {
                    break;
                }
                Rect bounds = drawableArr[i].getBounds();
                if (motionEvent.getX() < bounds.right - scrollX && motionEvent.getX() > bounds.left - scrollX) {
                    return onWidgetTouchEvent(motionEvent, i);
                }
                i++;
            }
        }
        this.mPressed = false;
        return false;
    }

    public final boolean onWidgetTouchEvent(MotionEvent motionEvent, int i) {
        WidgetManager widgetManager;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mPressed = true;
        } else if (action == 1) {
            if (this.mPressed && (widgetManager = this.mWidgetManager) != null) {
                widgetManager.onWidgetClick(i);
                this.mPressed = false;
                return true;
            }
        } else if (action == 3 && this.mPressed) {
            this.mPressed = false;
        }
        return this.mPressed;
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (TextUtils.isEmpty(this.mLabel) || this.mLabelLayout == null) {
            return;
        }
        if (this.mLabelMaxWidth == 0 && this.mLabelLength > getMeasuredWidth() / 2) {
            this.mLabelLength = getMeasuredWidth() / 2;
            createLabelLayout();
        }
        int height = this.mLabelLayout.getHeight() + getPaddingTop() + getPaddingBottom();
        if (height <= getMeasuredHeight()) {
            return;
        }
        setMeasuredDimension(getMeasuredWidth(), height);
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawExtraWidget(canvas);
        drawLabel(canvas);
    }

    public final void drawLabel(Canvas canvas) {
        if (TextUtils.isEmpty(this.mLabel) || this.mLabelLayout == null) {
            return;
        }
        int color = getPaint().getColor();
        getPaint().setColor(getCurrentTextColor());
        int paddingStart = getPaddingStart();
        Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
        int i = 0;
        if (compoundDrawablesRelative[0] != null) {
            i = this.mWidgetPadding + compoundDrawablesRelative[0].getIntrinsicWidth();
        }
        float max = Math.max(0.0f, (getMeasuredHeight() - this.mLabelLayout.getHeight()) / 2.0f);
        canvas.save();
        if (ViewUtils.isLayoutRtl(this)) {
            canvas.translate((((getScrollX() + getWidth()) - i) - this.mLabelLength) - paddingStart, max);
        } else {
            canvas.translate(paddingStart + getScrollX() + i, max);
        }
        this.mLabelLayout.draw(canvas);
        canvas.restore();
        getPaint().setColor(color);
    }

    public final void drawExtraWidget(Canvas canvas) {
        if (this.mExtraDrawables != null) {
            int width = getWidth();
            int height = getHeight();
            int scrollX = getScrollX();
            int paddingEnd = getPaddingEnd();
            Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
            int i = 0;
            int intrinsicWidth = compoundDrawablesRelative[2] == null ? 0 : compoundDrawablesRelative[2].getIntrinsicWidth() + this.mWidgetPadding;
            int i2 = height / 2;
            int i3 = 0;
            while (true) {
                Drawable[] drawableArr = this.mExtraDrawables;
                if (i >= drawableArr.length) {
                    return;
                }
                int intrinsicWidth2 = drawableArr[i].getIntrinsicWidth();
                int intrinsicHeight = this.mExtraDrawables[i].getIntrinsicHeight();
                if (ViewUtils.isLayoutRtl(this)) {
                    int i4 = scrollX + paddingEnd + intrinsicWidth;
                    int i5 = intrinsicHeight / 2;
                    this.mExtraDrawables[i].setBounds(i4 + i3, i2 - i5, i4 + intrinsicWidth2 + i3, i5 + i2);
                } else {
                    int i6 = ((scrollX + width) - paddingEnd) - intrinsicWidth;
                    int i7 = intrinsicHeight / 2;
                    this.mExtraDrawables[i].setBounds((i6 - intrinsicWidth2) - i3, i2 - i7, i6 - i3, i7 + i2);
                }
                i3 = this.mWidgetPadding + intrinsicWidth2;
                this.mExtraDrawables[i].draw(canvas);
                i++;
            }
        }
    }

    public final void createLabelLayout() {
        if (Build.VERSION.SDK_INT >= 23) {
            String str = this.mLabel;
            this.mLabelLayout = StaticLayout.Builder.obtain(str, 0, str.length(), getPaint(), this.mLabelLength).build();
            return;
        }
        this.mLabelLayout = new StaticLayout(this.mLabel, getPaint(), this.mLabelLength, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    /* loaded from: classes3.dex */
    public static abstract class WidgetManager {
        public abstract Drawable[] getWidgetDrawables();

        public void onAttached(StateEditText stateEditText) {
        }

        public void onDetached() {
        }

        public abstract void onWidgetClick(int i);

        public WidgetManager(Context context, AttributeSet attributeSet) {
        }
    }
}
