package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.util.FormatUtil;

/* loaded from: classes2.dex */
public class AlbumDetailGridItem extends MicroThumbGridItem {
    public TextView mFileSize;
    public TextView mFileSizeTop;

    public AlbumDetailGridItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mFileSize = (TextView) findViewById(R.id.file_size);
        this.mFileSizeTop = (TextView) findViewById(R.id.file_size_top);
    }

    public void bindFileSize(long j) {
        TextView textView = this.mTypeIndicator;
        if (textView == null || textView.getVisibility() != 0) {
            TextView textView2 = this.mFileSize;
            if (textView2 == null) {
                return;
            }
            if (j > 0) {
                textView2.setVisibility(0);
                this.mFileSize.setText(FormatUtil.formatFileSize(getContext(), j));
                return;
            }
            textView2.setVisibility(8);
            return;
        }
        TextView textView3 = this.mFileSizeTop;
        if (textView3 == null) {
            return;
        }
        if (j > 0) {
            textView3.setVisibility(0);
            this.mFileSizeTop.setText(FormatUtil.formatFileSize(getContext(), j));
            return;
        }
        textView3.setVisibility(8);
    }
}
