package com.miui.gallery.vlog.base.interfaces;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    public View mChildView;
    public GestureDetector mGestureDetector;
    public OnItemClickListener mListener;
    public RecyclerView mRecyclerView;

    /* loaded from: classes2.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int i);

        void onLongClick(View view, int i);

        void onScroll(View view, int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public RecyclerItemClickListener(Context context, OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
        this.mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class: com.miui.gallery.vlog.base.interfaces.RecyclerItemClickListener.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                if (RecyclerItemClickListener.this.mChildView == null || RecyclerItemClickListener.this.mListener == null) {
                    return true;
                }
                RecyclerItemClickListener.this.mListener.onItemClick(RecyclerItemClickListener.this.mChildView, RecyclerItemClickListener.this.mRecyclerView.getChildAdapterPosition(RecyclerItemClickListener.this.mChildView));
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
                if (RecyclerItemClickListener.this.mChildView == null || RecyclerItemClickListener.this.mListener == null) {
                    return;
                }
                RecyclerItemClickListener.this.mListener.onLongClick(RecyclerItemClickListener.this.mChildView, RecyclerItemClickListener.this.mRecyclerView.getChildAdapterPosition(RecyclerItemClickListener.this.mChildView));
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (RecyclerItemClickListener.this.mChildView == null || RecyclerItemClickListener.this.mListener == null) {
                    return true;
                }
                RecyclerItemClickListener.this.mListener.onScroll(RecyclerItemClickListener.this.mChildView, RecyclerItemClickListener.this.mRecyclerView.getChildAdapterPosition(RecyclerItemClickListener.this.mChildView));
                return true;
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        this.mRecyclerView = recyclerView;
        this.mChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        this.mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }
}
