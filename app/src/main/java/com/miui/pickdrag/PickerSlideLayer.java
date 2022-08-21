package com.miui.pickdrag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import com.miui.pickdrag.base.BasePickerDragActivity;
import com.miui.pickdrag.base.PickerSlideBackHelper;

/* loaded from: classes3.dex */
public class PickerSlideLayer extends FrameLayout {
    public boolean isCheckedViewDragHelperIntercept;
    public View[] mCanDragSlideView;
    public int[] mCanDragSlideViewIds;
    public View mDecorContentContainer;
    public boolean mIsInterceptTouch;
    public float mLastX;
    public float mLastY;
    public final PickerSlideBackHelper.PickerSlideViewDragHolder mSlideViewDragHolder;
    public final int mTouchSlop;

    /* loaded from: classes3.dex */
    public interface PickerSlideExit {
        void onFlingExitStart();

        void onSlideExit();

        void onSlideStart();
    }

    public PickerSlideLayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCanDragSlideViewIds = null;
        this.mCanDragSlideView = null;
        this.mSlideViewDragHolder = new PickerSlideBackHelper().createViewDrag(this);
        if (context instanceof BasePickerDragActivity) {
            BasePickerDragActivity basePickerDragActivity = (BasePickerDragActivity) context;
            initViewDrag(basePickerDragActivity);
            int[] canSlideViewIds = basePickerDragActivity.canSlideViewIds();
            this.mCanDragSlideViewIds = canSlideViewIds;
            if (canSlideViewIds == null) {
                this.mCanDragSlideViewIds = new int[]{R$id.drawer_handler_container};
            } else {
                int[] iArr = new int[canSlideViewIds.length + 1];
                this.mCanDragSlideViewIds = iArr;
                iArr[0] = R$id.drawer_handler_container;
                System.arraycopy(canSlideViewIds, 0, iArr, 1, iArr.length - 1);
            }
        }
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mDecorContentContainer = findViewById(R$id.picker_slide_content_body);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean z = motionEvent.getAction() == 0;
        if (z) {
            findCanDragSlideView();
        }
        if (motionEvent.getAction() == 0) {
            this.mLastX = motionEvent.getRawX();
            this.mLastY = motionEvent.getRawY();
            this.mIsInterceptTouch = false;
            this.isCheckedViewDragHelperIntercept = false;
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (!z || dispatchTouchEvent) {
            return dispatchTouchEvent;
        }
        return true;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        float rawX = motionEvent.getRawX() - this.mLastX;
        float rawY = motionEvent.getRawY() - this.mLastY;
        boolean z2 = true;
        boolean z3 = Math.abs(rawY) > ((float) this.mTouchSlop) && Math.abs(rawX) < Math.abs(rawY) && rawY > 0.0f;
        boolean z4 = !this.mSlideViewDragHolder.getViewDragHelper().continueSettling(false);
        boolean canInterceptVerticalScroll = z4 ? this.mSlideViewDragHolder.canInterceptVerticalScroll(motionEvent) : false;
        if (canInterceptVerticalScroll) {
            this.isCheckedViewDragHelperIntercept = true;
            z = this.mSlideViewDragHolder.getViewDragHelper().shouldInterceptTouchEvent(motionEvent);
        } else {
            z = false;
        }
        if (!z4 || !canInterceptVerticalScroll || !z || !z3) {
            z2 = false;
        }
        this.mIsInterceptTouch = z2;
        return z2;
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.isCheckedViewDragHelperIntercept) {
            this.mSlideViewDragHolder.getViewDragHelper().processTouchEvent(motionEvent);
        }
        return this.mIsInterceptTouch;
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mSlideViewDragHolder.getViewDragHelper().continueSettling(true)) {
            invalidate();
        }
    }

    public final void initViewDrag(BasePickerDragActivity basePickerDragActivity) {
        this.mSlideViewDragHolder.setMPickerSlideExit(basePickerDragActivity);
    }

    public final void findCanDragSlideView() {
        int[] iArr;
        if (this.mCanDragSlideView != null || (iArr = this.mCanDragSlideViewIds) == null || iArr.length <= 0) {
            return;
        }
        this.mCanDragSlideView = new View[iArr.length];
        int i = 0;
        while (true) {
            int[] iArr2 = this.mCanDragSlideViewIds;
            if (i >= iArr2.length) {
                return;
            }
            this.mCanDragSlideView[i] = findViewById(iArr2[i]);
            i++;
        }
    }

    public void followOtherActivitySlide(int i) {
        View view = this.mDecorContentContainer;
        if (view == null || i <= 0) {
            return;
        }
        this.mSlideViewDragHolder.followVerticalSlide(this.mDecorContentContainer, i - view.getTop());
        if (i < this.mDecorContentContainer.getHeight()) {
            return;
        }
        this.mSlideViewDragHolder.invokeSlideExit();
    }

    public boolean isActivityExitAnimating() {
        return getContext() != null && (getContext() instanceof BasePickerDragActivity) && ((BasePickerDragActivity) getContext()).isScheduleExitAnim();
    }
}
