package com.miui.gallery.listener;

import android.view.View;

/* loaded from: classes2.dex */
public abstract class SingleClickListener implements View.OnClickListener {
    public long mLastClickTime = 0;
    public int mId = -1;

    public abstract void onSingleClick(View view);

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        long currentTimeMillis = System.currentTimeMillis();
        int id = view.getId();
        if (this.mId != id) {
            this.mId = id;
            this.mLastClickTime = currentTimeMillis;
            onSingleClick(view);
        } else if (currentTimeMillis - this.mLastClickTime <= 1000) {
        } else {
            this.mLastClickTime = currentTimeMillis;
            onSingleClick(view);
        }
    }
}
