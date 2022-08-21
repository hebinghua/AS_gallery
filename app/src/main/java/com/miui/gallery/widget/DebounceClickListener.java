package com.miui.gallery.widget;

import android.view.View;
import com.baidu.platform.comapi.UIMsg;

/* loaded from: classes2.dex */
public abstract class DebounceClickListener implements View.OnClickListener {
    public long mLastClickTimeNanoseconds;
    public int mMinClickInterval;

    public abstract void onClickConfirmed(View view);

    public DebounceClickListener() {
        this(UIMsg.MSG_MAP_PANO_DATA);
    }

    public DebounceClickListener(int i) {
        this.mMinClickInterval = i * 1000000;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (System.nanoTime() - this.mLastClickTimeNanoseconds < this.mMinClickInterval) {
            return;
        }
        this.mLastClickTimeNanoseconds = System.nanoTime();
        onClickConfirmed(view);
    }
}
