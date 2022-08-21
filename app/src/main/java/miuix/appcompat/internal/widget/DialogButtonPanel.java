package miuix.appcompat.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import miuix.appcompat.R$dimen;

/* loaded from: classes3.dex */
public class DialogButtonPanel extends LinearLayout {
    public final int HORIZONTAL_MARGIN;
    public final int VERTICAL_MARGIN;

    public DialogButtonPanel(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DialogButtonPanel(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.HORIZONTAL_MARGIN = getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_dialog_btn_margin_horizontal);
        this.VERTICAL_MARGIN = getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_dialog_btn_margin_vertical);
    }

    public final void handleLayoutParams(LinearLayout.LayoutParams layoutParams) {
        int childCount = getChildCount();
        if (getOrientation() == 1) {
            layoutParams.width = -1;
            if (childCount <= 0) {
                return;
            }
            layoutParams.topMargin = this.VERTICAL_MARGIN;
            return;
        }
        layoutParams.width = 0;
        if (childCount <= 0) {
            return;
        }
        layoutParams.setMarginStart(this.HORIZONTAL_MARGIN);
    }

    public void clearVisibleChildMargins() {
        View view;
        int childCount = getChildCount();
        int i = 0;
        while (true) {
            view = null;
            if (i >= childCount) {
                break;
            }
            view = getChildAt(i);
            if (view.getVisibility() == 0) {
                break;
            }
            i++;
        }
        if (view != null) {
            clearParams((LinearLayout.LayoutParams) view.getLayoutParams());
        }
    }

    public final void clearParams(LinearLayout.LayoutParams layoutParams) {
        layoutParams.setMarginStart(0);
        layoutParams.topMargin = 0;
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        clearParams((LinearLayout.LayoutParams) view.getLayoutParams());
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        handleLayoutParams((LinearLayout.LayoutParams) layoutParams);
        super.addView(view, i, layoutParams);
    }
}
