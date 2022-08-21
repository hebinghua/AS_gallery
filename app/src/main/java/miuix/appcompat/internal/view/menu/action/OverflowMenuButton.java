package miuix.appcompat.internal.view.menu.action;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$styleable;

/* loaded from: classes3.dex */
public class OverflowMenuButton extends LinearLayout {
    public ActionMenuItemViewChildren mChildren;
    public OnOverflowMenuButtonClickListener mOnOverflowMenuButtonClickListener;

    /* loaded from: classes3.dex */
    public interface OnOverflowMenuButtonClickListener {
        void onOverflowMenuButtonClick();
    }

    public OverflowMenuButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.OverflowMenuButton, i, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.OverflowMenuButton_android_drawableTop);
        CharSequence text = obtainStyledAttributes.getText(R$styleable.OverflowMenuButton_android_text);
        obtainStyledAttributes.recycle();
        ActionMenuItemViewChildren actionMenuItemViewChildren = new ActionMenuItemViewChildren(this);
        this.mChildren = actionMenuItemViewChildren;
        actionMenuItemViewChildren.setIcon(drawable);
        this.mChildren.setText(text);
        setClickable(true);
        setFocusable(true);
        setVisibility(0);
        setEnabled(true);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mChildren.onConfigurationChanged(getContext().getResources().getConfiguration());
        setPaddingRelative(getPaddingStart(), getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_action_button_bg_top_padding), getPaddingEnd(), getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_action_button_bg_bottom_padding));
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mChildren.setEnabled(z);
    }

    public final boolean isVisible() {
        View view = this;
        while (view != null && view.getVisibility() == 0) {
            ViewParent parent = view.getParent();
            view = parent instanceof ViewGroup ? (ViewGroup) parent : null;
        }
        return view == null;
    }

    @Override // android.view.View
    public boolean performClick() {
        if (!super.performClick() && isVisible()) {
            playSoundEffect(0);
            OnOverflowMenuButtonClickListener onOverflowMenuButtonClickListener = this.mOnOverflowMenuButtonClickListener;
            if (onOverflowMenuButtonClickListener != null) {
                onOverflowMenuButtonClickListener.onOverflowMenuButtonClick();
            }
            return true;
        }
        return true;
    }

    public void setOnOverflowMenuButtonClickListener(OnOverflowMenuButtonClickListener onOverflowMenuButtonClickListener) {
        this.mOnOverflowMenuButtonClickListener = onOverflowMenuButtonClickListener;
    }
}
