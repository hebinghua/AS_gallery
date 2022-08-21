package miuix.internal.hybrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import miuix.hybrid.R;

/* loaded from: classes3.dex */
public class WebContainerView extends FrameLayout {
    public boolean mIsPulling;
    public float mLastX;
    public float mLastY;
    public boolean mPullable;
    public int mTouchSlop;
    public View mWebView;

    public WebContainerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsPulling = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.HybridViewStyle, 0, 0);
        this.mPullable = obtainStyledAttributes.getBoolean(R.styleable.HybridViewStyle_hybridPullable, true);
        obtainStyledAttributes.recycle();
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setWebView(View view) {
        View view2;
        if (view == null || (view2 = this.mWebView) == view) {
            return;
        }
        if (view2 != null) {
            removeView(view2);
        }
        this.mWebView = view;
        addView(view, 0, new FrameLayout.LayoutParams(-1, -1));
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mWebView != null && this.mPullable) {
            int actionMasked = motionEvent.getActionMasked();
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            if (actionMasked != 3 && actionMasked != 1) {
                if (actionMasked == 0) {
                    this.mLastX = rawX;
                    this.mLastY = rawY;
                } else if (actionMasked == 2) {
                    if (this.mIsPulling) {
                        return true;
                    }
                    float f = this.mLastY - rawY;
                    float abs = Math.abs(this.mLastX - rawX);
                    float abs2 = Math.abs(f);
                    this.mLastX = rawX;
                    this.mLastY = rawY;
                    if (this.mWebView.getScrollY() == 0 && f < 0.0f && abs2 > abs && abs2 > this.mTouchSlop) {
                        this.mIsPulling = true;
                        return true;
                    }
                }
                return false;
            }
            this.mIsPulling = false;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x001b, code lost:
        if (r5 != 3) goto L16;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            boolean r0 = r4.mPullable
            r1 = 0
            if (r0 == 0) goto L38
            boolean r0 = r4.mIsPulling
            if (r0 != 0) goto La
            goto L38
        La:
            float r0 = r5.getRawY()
            int r5 = r5.getAction()
            if (r5 == 0) goto L36
            r2 = 1
            if (r5 == r2) goto L30
            r2 = 2
            if (r5 == r2) goto L1e
            r0 = 3
            if (r5 == r0) goto L30
            goto L38
        L1e:
            float r5 = r4.mLastY
            float r5 = r0 - r5
            float r2 = r4.getTranslationY()
            r3 = 1056964608(0x3f000000, float:0.5)
            float r5 = r5 * r3
            float r2 = r2 + r5
            r4.setTranslationY(r2)
            r4.mLastY = r0
            goto L38
        L30:
            r4.mIsPulling = r1
            r4.springBack()
            goto L38
        L36:
            r4.mLastY = r0
        L38:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: miuix.internal.hybrid.WebContainerView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void springBack() {
        if (getTranslationY() == 0.0f) {
            return;
        }
        ViewPropertyAnimator animate = animate();
        animate.translationY(0.0f);
        animate.setDuration(getResources().getInteger(17694721));
        animate.start();
    }
}
