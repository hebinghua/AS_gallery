package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class TrashGridItem extends MicroThumbGridItem {
    public TextView mRemainDurationText;

    public TrashGridItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mRemainDurationText = (TextView) findViewById(R.id.remain_duration);
    }

    public void setRemainDuration(long j) {
        String quantityString;
        int i = (int) (j / 86400000);
        int i2 = (int) ((j % 86400000) / 3600000);
        if (i > 0) {
            quantityString = getContext().getResources().getQuantityString(R.plurals.photo_remain_days, i, Integer.valueOf(i));
        } else {
            quantityString = getContext().getResources().getQuantityString(R.plurals.photo_remain_hours, i2, Integer.valueOf(i2));
        }
        this.mRemainDurationText.setText(quantityString);
        if (i <= 3) {
            this.mRemainDurationText.setTextColor(getResources().getColor(R.color.trash_grid_remain_text_color_warn));
        } else {
            this.mRemainDurationText.setTextColor(getResources().getColor(R.color.trash_grid_remain_text_color));
        }
    }
}
