package com.miui.gallery.util;

import android.os.Handler;
import android.view.View;

/* loaded from: classes2.dex */
public class ProgressBarHandler {
    public View mProgressBar;
    public Runnable mHideRunnable = new Runnable() { // from class: com.miui.gallery.util.ProgressBarHandler.1
        @Override // java.lang.Runnable
        public void run() {
            ProgressBarHandler.this.mProgressBar.setVisibility(4);
        }
    };
    public Runnable mShowRunnable = new Runnable() { // from class: com.miui.gallery.util.ProgressBarHandler.2
        @Override // java.lang.Runnable
        public void run() {
            ProgressBarHandler.this.mProgressBar.setVisibility(0);
        }
    };
    public Handler mHandler = new Handler();

    public ProgressBarHandler(View view) {
        this.mProgressBar = view;
    }

    public void showDelay(int i) {
        this.mHandler.removeCallbacks(this.mHideRunnable);
        this.mHandler.postDelayed(this.mShowRunnable, i);
    }

    public void hide() {
        this.mHandler.removeCallbacks(this.mShowRunnable);
        this.mHandler.post(this.mHideRunnable);
    }
}
