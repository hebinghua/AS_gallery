package com.miui.gallery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class LoadMoreLayout extends LinearLayout {
    public ProgressBar mProgressBar;
    public TextView mTitle;

    public LoadMoreLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mProgressBar = (ProgressBar) findViewById(R.id.progress);
        this.mTitle = (TextView) findViewById(R.id.title);
        this.mProgressBar.setVisibility(8);
    }

    public void startLoad() {
        this.mProgressBar.setVisibility(0);
        this.mTitle.setText(R.string.loading_dots);
    }

    public void setLoadComplete() {
        this.mProgressBar.setVisibility(8);
        this.mTitle.setText(R.string.load_complete);
    }
}
