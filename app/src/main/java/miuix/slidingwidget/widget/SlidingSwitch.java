package miuix.slidingwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import miuix.slidingwidget.R$attr;
import miuix.slidingwidget.R$style;
import miuix.slidingwidget.R$styleable;

/* loaded from: classes3.dex */
public class SlidingSwitch extends Switch {
    public SlidingButtonHelper mHelper;

    public static /* synthetic */ boolean $r8$lambda$MIZv4AQHcUGl0sIMBdDVkC2mvus(SlidingSwitch slidingSwitch, View view, MotionEvent motionEvent) {
        return slidingSwitch.lambda$new$0(view, motionEvent);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.view.View
    public boolean onSetAlpha(int i) {
        return true;
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(Drawable drawable) {
    }

    public SlidingSwitch(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.slidingButtonStyle);
    }

    public SlidingSwitch(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        SlidingButtonHelper slidingButtonHelper = new SlidingButtonHelper(this);
        this.mHelper = slidingButtonHelper;
        slidingButtonHelper.initDrawable();
        this.mHelper.initAnim();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SlidingButton, i, R$style.Widget_SlidingButton_DayNight);
        this.mHelper.initResource(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        setOnHoverListener(new View.OnHoverListener() { // from class: miuix.slidingwidget.widget.SlidingSwitch$$ExternalSyntheticLambda0
            @Override // android.view.View.OnHoverListener
            public final boolean onHover(View view, MotionEvent motionEvent) {
                return SlidingSwitch.$r8$lambda$MIZv4AQHcUGl0sIMBdDVkC2mvus(SlidingSwitch.this, view, motionEvent);
            }
        });
    }

    public /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.onHoverEvent(motionEvent);
            return true;
        }
        return false;
    }

    public void setOnPerformCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.setOnPerformCheckedChangeListener(onCheckedChangeListener);
        }
    }

    @Override // android.view.View
    public void setLayerType(int i, Paint paint) {
        super.setLayerType(i, paint);
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.setLayerType(i);
        }
    }

    @Override // android.widget.Switch, android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        if (isChecked() != z) {
            super.setChecked(z);
            boolean isChecked = isChecked();
            SlidingButtonHelper slidingButtonHelper = this.mHelper;
            if (slidingButtonHelper == null) {
                return;
            }
            slidingButtonHelper.setChecked(isChecked);
        }
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        super.setAlpha(f);
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.setAlpha(f);
        }
        invalidate();
    }

    @Override // android.view.View
    public float getAlpha() {
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            return slidingButtonHelper.getAlpha();
        }
        return super.getAlpha();
    }

    @Override // android.widget.Switch, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.setSliderDrawState();
        }
    }

    @Override // android.widget.Switch, android.widget.TextView, android.view.View
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(this.mHelper.getMeasuredWidth(), this.mHelper.getMeasuredHeight());
        this.mHelper.setParentClipChildren();
    }

    @Override // android.widget.CompoundButton, android.view.View
    public boolean performClick() {
        super.performClick();
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.notifyCheckedChangeListener();
            return true;
        }
        return true;
    }

    @Override // android.widget.Switch, android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper == null) {
            return true;
        }
        slidingButtonHelper.onTouchEvent(motionEvent);
        return true;
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        super.setPressed(z);
        invalidate();
    }

    @Override // android.widget.Switch, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper == null) {
            super.onDraw(canvas);
        } else {
            slidingButtonHelper.onDraw(canvas);
        }
    }

    @Override // android.widget.Switch, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        SlidingButtonHelper slidingButtonHelper;
        return super.verifyDrawable(drawable) || ((slidingButtonHelper = this.mHelper) != null && slidingButtonHelper.verifyDrawable(drawable));
    }

    @Override // android.widget.Switch, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.jumpDrawablesToCurrentState();
        }
    }
}
