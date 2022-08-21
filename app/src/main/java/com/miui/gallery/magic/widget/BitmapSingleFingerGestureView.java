package com.miui.gallery.magic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;

/* loaded from: classes2.dex */
public class BitmapSingleFingerGestureView extends BitmapGestureView {
    public BitmapSingleFingerGestureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BitmapSingleFingerGestureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public final void init() {
        setFeatureGestureListener(new SimpleFeatureListener() { // from class: com.miui.gallery.magic.widget.BitmapSingleFingerGestureView.1
            @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
            public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (BitmapSingleFingerGestureView.this.mState != BitmapGestureView.State.BY_CHILD || BitmapSingleFingerGestureView.this.mBitmapGestureParamsHolder == null) {
                    return;
                }
                BitmapSingleFingerGestureView.this.mBitmapGestureParamsHolder.performTransition(-f, -f2);
            }
        });
    }

    /* loaded from: classes2.dex */
    public class SimpleFeatureListener implements BitmapGestureView.FeatureGesListener {
        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
        }

        public SimpleFeatureListener() {
        }
    }
}
