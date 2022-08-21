package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class PeoplePageGridHeaderItem extends RelativeLayout {
    public TextView mGroupName;

    public PeoplePageGridHeaderItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mGroupName = (TextView) findViewById(R.id.groupName);
    }

    public void bindData(String str) {
        this.mGroupName.setText(str);
    }
}
