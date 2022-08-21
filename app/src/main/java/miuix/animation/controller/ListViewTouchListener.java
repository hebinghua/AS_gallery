package miuix.animation.controller;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: classes3.dex */
public class ListViewTouchListener implements View.OnTouchListener {
    public int mTouchSlop;
    public WeakHashMap<View, View.OnTouchListener> mListeners = new WeakHashMap<>();
    public Rect mRect = new Rect();
    public float mDownX = Float.MAX_VALUE;
    public float mDownY = Float.MAX_VALUE;

    public ListViewTouchListener(ViewGroup viewGroup) {
        this.mTouchSlop = ViewConfiguration.get(viewGroup.getContext()).getScaledTouchSlop();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mDownX = motionEvent.getRawX();
            this.mDownY = motionEvent.getRawY();
        } else if (actionMasked == 2) {
            if (motionEvent.getRawY() - this.mDownY > this.mTouchSlop || motionEvent.getRawX() - this.mDownX > this.mTouchSlop) {
                z = true;
                notifyItemListeners((ViewGroup) view, motionEvent, z);
                return false;
            }
        } else {
            this.mDownY = Float.MAX_VALUE;
            this.mDownX = Float.MAX_VALUE;
        }
        z = false;
        notifyItemListeners((ViewGroup) view, motionEvent, z);
        return false;
    }

    public void putListener(View view, View.OnTouchListener onTouchListener) {
        this.mListeners.put(view, onTouchListener);
    }

    public final void notifyItemListeners(ViewGroup viewGroup, MotionEvent motionEvent, boolean z) {
        View touchedItemView = getTouchedItemView(viewGroup, motionEvent);
        for (Map.Entry<View, View.OnTouchListener> entry : this.mListeners.entrySet()) {
            View key = entry.getKey();
            entry.getValue().onTouch(key, !z && key == touchedItemView ? motionEvent : null);
        }
    }

    public final View getTouchedItemView(ViewGroup viewGroup, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            childAt.getLocalVisibleRect(this.mRect);
            this.mRect.offset(childAt.getLeft(), childAt.getTop());
            if (this.mRect.contains(x, y)) {
                return childAt;
            }
        }
        return null;
    }
}
