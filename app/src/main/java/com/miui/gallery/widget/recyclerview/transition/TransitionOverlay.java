package com.miui.gallery.widget.recyclerview.transition;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import miuix.view.animation.SineEaseInOutInterpolator;

/* loaded from: classes3.dex */
public class TransitionOverlay extends View implements ITransitionOverlay {
    public int mCurrState;
    public boolean mIsAutoTransit;
    public float mProgress;
    public List<ITransitionRender> mRenderItems;
    public OverScroller mScroller;

    public TransitionOverlay(Context context) {
        super(context);
        this.mCurrState = 0;
        this.mScroller = new OverScroller(context, new SineEaseInOutInterpolator());
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionOverlay
    public void updateProgress(RecyclerView recyclerView, boolean z, float f) {
        List<ITransitionRender> list = this.mRenderItems;
        if (list == null || list.isEmpty()) {
            DefaultLogger.w("TransitionOverlay", "mRenderItems is empty");
            return;
        }
        this.mIsAutoTransit = z;
        if (Float.compare(this.mProgress, f) == 0) {
            return;
        }
        for (ITransitionRender iTransitionRender : this.mRenderItems) {
            iTransitionRender.onTransition(this.mProgress);
        }
        int determineNextState = determineNextState(f);
        this.mProgress = f;
        if (z) {
            if (!this.mScroller.isFinished()) {
                this.mScroller.forceFinished(true);
            }
        } else {
            int i = this.mCurrState;
            if (i == 0 && determineNextState == 1) {
                this.mScroller.startScroll(0, 0, 1000000, 0, 380);
            } else if (i == 1 && determineNextState == 2) {
                OverScroller overScroller = this.mScroller;
                overScroller.startScroll(overScroller.getCurrX(), 0, -this.mScroller.getCurrX(), 0, 380);
            } else if (i == 2 && determineNextState == 1) {
                OverScroller overScroller2 = this.mScroller;
                overScroller2.startScroll(overScroller2.getCurrX(), 0, 1000000 - this.mScroller.getCurrX(), 0, 380);
            }
        }
        this.mCurrState = determineNextState;
        invalidate();
    }

    public final int determineNextState(float f) {
        int i = this.mCurrState;
        if (i == 0) {
            if (f < 0.23f) {
                return i;
            }
        } else if (i == 1) {
            if (f >= 0.23f) {
                return i;
            }
            return 2;
        } else if (f <= 0.23f) {
            return i;
        }
        return 1;
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mIsAutoTransit || !this.mScroller.computeScrollOffset()) {
            return;
        }
        postInvalidateOnAnimation();
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionOverlay
    public void prepare(RecyclerView recyclerView, List<ITransitionRender> list) {
        this.mRenderItems = list;
        for (ITransitionRender iTransitionRender : list) {
            iTransitionRender.onPreTransition();
        }
        this.mScroller.startScroll(0, 0, 0, 0);
        this.mScroller.forceFinished(true);
        invalidate();
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionOverlay
    public void release() {
        List<ITransitionRender> list = this.mRenderItems;
        if (list != null) {
            for (ITransitionRender iTransitionRender : list) {
                iTransitionRender.onPostTransition();
            }
            this.mRenderItems.clear();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionOverlay
    public void attach(RecyclerView recyclerView) {
        setBackgroundColor(recyclerView.getResources().getColor(R.color.window_background));
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        if (recyclerView.getClipToPadding()) {
            int paddingLeft = recyclerView.getPaddingLeft();
            int paddingRight = recyclerView.getPaddingRight();
            int paddingTop = recyclerView.getPaddingTop();
            int paddingBottom = recyclerView.getPaddingBottom();
            measure(View.MeasureSpec.makeMeasureSpec((recyclerView.getWidth() - paddingLeft) - paddingRight, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec((recyclerView.getHeight() - paddingTop) - paddingBottom, Integer.MIN_VALUE));
            layout(paddingLeft, paddingTop, recyclerView.getRight() - paddingRight, recyclerView.getBottom() - paddingBottom);
        } else {
            measure(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), Integer.MIN_VALUE));
            layout(0, 0, recyclerView.getRight(), recyclerView.getBottom());
        }
        recyclerView.getOverlay().add(this);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionOverlay
    public void detach(RecyclerView recyclerView) {
        this.mCurrState = 0;
        this.mIsAutoTransit = false;
        recyclerView.getOverlay().remove(this);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<ITransitionRender> list = this.mRenderItems;
        if (list != null) {
            if (this.mIsAutoTransit) {
                for (ITransitionRender iTransitionRender : list) {
                    iTransitionRender.draw(canvas, this.mProgress, -1.0f);
                }
                return;
            }
            float currX = this.mScroller.getCurrX() / 1000000.0f;
            for (ITransitionRender iTransitionRender2 : this.mRenderItems) {
                iTransitionRender2.draw(canvas, this.mProgress, currX);
            }
        }
    }
}
