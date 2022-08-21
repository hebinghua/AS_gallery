package com.miui.gallery.adapter;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;

/* loaded from: classes.dex */
public class ExcludeOnTouchListener implements View.OnTouchListener {
    public View mExcludedView;
    public View mHostView;
    public int mLastX;
    public int mLastY;
    public int mMoveX;
    public int mMoveY;
    public OnTouchValidListener mOnTouchValidListener;
    public int mSlop = ViewConfiguration.get(GalleryApp.sGetAndroidContext()).getScaledTouchSlop();

    /* loaded from: classes.dex */
    public interface OnTouchValidListener {
        void onTouchValid(View view);
    }

    public ExcludeOnTouchListener(View view, View view2, OnTouchValidListener onTouchValidListener) {
        this.mHostView = view;
        this.mExcludedView = view2;
        this.mOnTouchValidListener = onTouchValidListener;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        if (this.mExcludedView == null) {
            return false;
        }
        Rect rect = new Rect();
        this.mExcludedView.getGlobalVisibleRect(rect);
        rect.inset(-rect.width(), -rect.height());
        if (rect.contains(rawX, rawY)) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mLastX = rawX;
            this.mLastY = rawY;
            this.mMoveX = 0;
            this.mMoveY = 0;
            if (view.getParent() instanceof GalleryRecyclerView) {
                ((GalleryRecyclerView) view.getParent()).setTag(R.id.ignore_target_view_action_event_disposable, Boolean.TRUE);
            }
            return true;
        }
        if (action == 1) {
            int i = this.mMoveX;
            int i2 = this.mSlop;
            if (i <= i2 && this.mMoveY <= i2) {
                OnTouchValidListener onTouchValidListener = this.mOnTouchValidListener;
                if (onTouchValidListener != null) {
                    onTouchValidListener.onTouchValid(view);
                }
                return true;
            }
        } else if (action == 2) {
            this.mMoveX += Math.abs(this.mLastX - rawX);
            this.mMoveY += Math.abs(this.mLastY - rawY);
            this.mLastX = rawX;
            this.mLastY = rawY;
        }
        return false;
    }
}
