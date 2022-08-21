package com.miui.gallery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class DialogCheckableView extends RelativeLayout {
    public CheckBox mCheckBox;
    public TextView mDesc;

    public DialogCheckableView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DialogCheckableView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DialogCheckableView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        CheckBox checkBox = (CheckBox) findViewById(R.id.check_box);
        this.mCheckBox = checkBox;
        checkBox.setClickable(false);
        this.mDesc = (TextView) findViewById(R.id.check_box_desc);
    }

    public void setDesc(int i) {
        this.mDesc.setText(i);
    }

    public void setChecked(boolean z) {
        this.mCheckBox.setChecked(z);
    }

    public boolean isChecked() {
        return this.mCheckBox.isChecked();
    }
}
