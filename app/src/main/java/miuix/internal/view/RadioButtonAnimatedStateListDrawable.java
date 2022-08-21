package miuix.internal.view;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$style;
import miuix.internal.view.CheckWidgetAnimatedStateListDrawable;

/* loaded from: classes3.dex */
public class RadioButtonAnimatedStateListDrawable extends CheckBoxAnimatedStateListDrawable {
    public int mDrawPadding;

    @Override // miuix.internal.view.CheckBoxAnimatedStateListDrawable
    public boolean isSingleSelectionWidget() {
        return true;
    }

    public RadioButtonAnimatedStateListDrawable() {
        this.mDrawPadding = 19;
    }

    public RadioButtonAnimatedStateListDrawable(Resources resources, Resources.Theme theme, CheckWidgetAnimatedStateListDrawable.CheckWidgetConstantState checkWidgetConstantState) {
        super(resources, theme, checkWidgetConstantState);
        this.mDrawPadding = 19;
        if (resources != null) {
            this.mDrawPadding = resources.getDimensionPixelSize(R$dimen.miuix_appcompat_radio_button_drawable_padding);
        }
    }

    @Override // miuix.internal.view.CheckBoxAnimatedStateListDrawable
    public int getCheckWidgetDrawableStyle() {
        return R$style.CheckWidgetDrawable_RadioButton;
    }

    @Override // miuix.internal.view.CheckBoxAnimatedStateListDrawable, miuix.internal.view.CheckWidgetAnimatedStateListDrawable
    public CheckWidgetAnimatedStateListDrawable.CheckWidgetConstantState newCheckWidgetConstantState() {
        return new RadioButtonConstantState();
    }

    /* loaded from: classes3.dex */
    public static class RadioButtonConstantState extends CheckWidgetAnimatedStateListDrawable.CheckWidgetConstantState {
        @Override // miuix.internal.view.CheckWidgetAnimatedStateListDrawable.CheckWidgetConstantState
        public Drawable newAnimatedStateListDrawable(Resources resources, Resources.Theme theme, CheckWidgetAnimatedStateListDrawable.CheckWidgetConstantState checkWidgetConstantState) {
            return new RadioButtonAnimatedStateListDrawable(resources, theme, checkWidgetConstantState);
        }
    }

    @Override // miuix.internal.view.CheckBoxAnimatedStateListDrawable
    public void setCheckWidgetDrawableBounds(int i, int i2, int i3, int i4) {
        int i5 = this.mDrawPadding;
        super.setCheckWidgetDrawableBounds(i + i5, i2 + i5, i3 - i5, i4 - i5);
    }

    @Override // miuix.internal.view.CheckBoxAnimatedStateListDrawable
    public void setCheckWidgetDrawableBounds(Rect rect) {
        int i = this.mDrawPadding;
        rect.inset(i, i);
        super.setCheckWidgetDrawableBounds(rect);
    }
}
