package com.miui.pickdrag.base;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.lifecycle.MutableLiveData;
import com.miui.pickdrag.PickerSlideLayer;
import com.miui.pickdrag.ViewExtensionKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PickerSlideBackHelper.kt */
/* loaded from: classes3.dex */
public final class PickerSlideBackHelper {
    public static final Companion Companion = new Companion(null);
    public static final MutableLiveData<Integer> mSlideDistance = new MutableLiveData<>();
    public PickerSlideViewDragHolder pickerSlideViewDragHolder;

    public final PickerSlideViewDragHolder createViewDrag(PickerSlideLayer slideBackViewGroup) {
        Intrinsics.checkNotNullParameter(slideBackViewGroup, "slideBackViewGroup");
        return createViewDrag$default(this, slideBackViewGroup, 0.0f, 2, null);
    }

    /* compiled from: PickerSlideBackHelper.kt */
    /* loaded from: classes3.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final MutableLiveData<Integer> getMSlideDistance() {
            return PickerSlideBackHelper.mSlideDistance;
        }
    }

    public static /* synthetic */ PickerSlideViewDragHolder createViewDrag$default(PickerSlideBackHelper pickerSlideBackHelper, PickerSlideLayer pickerSlideLayer, float f, int i, Object obj) {
        if ((i & 2) != 0) {
            f = 1.0f;
        }
        return pickerSlideBackHelper.createViewDrag(pickerSlideLayer, f);
    }

    public final PickerSlideViewDragHolder createViewDrag(PickerSlideLayer slideBackViewGroup, float f) {
        Intrinsics.checkNotNullParameter(slideBackViewGroup, "slideBackViewGroup");
        PickerSlideViewDragHolder pickerSlideViewDragHolder = new PickerSlideViewDragHolder(slideBackViewGroup, f);
        this.pickerSlideViewDragHolder = pickerSlideViewDragHolder;
        Intrinsics.checkNotNull(pickerSlideViewDragHolder);
        return pickerSlideViewDragHolder;
    }

    /* compiled from: PickerSlideBackHelper.kt */
    /* loaded from: classes3.dex */
    public static final class PickerSlideViewDragHolder {
        public static final Companion Companion = new Companion(null);
        public PickerSlideLayer.PickerSlideExit mPickerSlideExit;
        public final PickerSlideLayer slideBackViewGroup;
        public final PickerViewDragCallBack viewDragCallBack;
        public final ViewDragHelper viewDragHelper;

        public PickerSlideViewDragHolder(PickerSlideLayer slideBackViewGroup, float f) {
            Intrinsics.checkNotNullParameter(slideBackViewGroup, "slideBackViewGroup");
            this.slideBackViewGroup = slideBackViewGroup;
            PickerViewDragCallBack pickerViewDragCallBack = new PickerViewDragCallBack(this, slideBackViewGroup);
            this.viewDragCallBack = pickerViewDragCallBack;
            ViewDragHelper create = ViewDragHelper.create(slideBackViewGroup, f, pickerViewDragCallBack);
            Intrinsics.checkNotNullExpressionValue(create, "create(slideBackViewGrou…tivity, viewDragCallBack)");
            this.viewDragHelper = create;
        }

        /* compiled from: PickerSlideBackHelper.kt */
        /* loaded from: classes3.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }

        public final ViewDragHelper getViewDragHelper() {
            return this.viewDragHelper;
        }

        public final PickerSlideLayer.PickerSlideExit getMPickerSlideExit() {
            return this.mPickerSlideExit;
        }

        public final void setMPickerSlideExit(PickerSlideLayer.PickerSlideExit pickerSlideExit) {
            this.mPickerSlideExit = pickerSlideExit;
        }

        public final void followVerticalSlide(View pickerDragLayerContentView, int i) {
            Intrinsics.checkNotNullParameter(pickerDragLayerContentView, "pickerDragLayerContentView");
            ViewCompat.offsetTopAndBottom(pickerDragLayerContentView, i);
        }

        public final void invokeSlideExit() {
            this.viewDragCallBack.invokeSlideExit();
        }

        public final boolean canInterceptVerticalScroll(MotionEvent event) {
            Intrinsics.checkNotNullParameter(event, "event");
            View[] viewArr = this.slideBackViewGroup.mCanDragSlideView;
            if (viewArr == null) {
                return false;
            }
            Intrinsics.checkNotNullExpressionValue(viewArr, "slideBackViewGroup.mCanDragSlideView");
            int length = viewArr.length;
            int i = 0;
            while (i < length) {
                View view = viewArr[i];
                i++;
                if (event.getAction() == 0 && view != null && ViewExtensionKt.isMotionEventIn(view, event)) {
                    return true;
                }
            }
            return false;
        }

        /* compiled from: PickerSlideBackHelper.kt */
        /* loaded from: classes3.dex */
        public final class PickerViewDragCallBack extends ViewDragHelper.Callback {
            public final int mFlingMinVelocity;
            public boolean mShouldInvokeSlideExit;
            public int mSlideChildHeight;
            public int mSlideChildTopWhenCapture;
            public final ViewGroup mSlideWrapperViewGroup;
            public final /* synthetic */ PickerSlideViewDragHolder this$0;

            public PickerViewDragCallBack(PickerSlideViewDragHolder this$0, ViewGroup mSlideWrapperViewGroup) {
                Intrinsics.checkNotNullParameter(this$0, "this$0");
                Intrinsics.checkNotNullParameter(mSlideWrapperViewGroup, "mSlideWrapperViewGroup");
                this.this$0 = this$0;
                this.mSlideWrapperViewGroup = mSlideWrapperViewGroup;
                this.mFlingMinVelocity = ViewConfiguration.get(mSlideWrapperViewGroup.getContext()).getScaledMinimumFlingVelocity() * 2;
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public boolean tryCaptureView(View child, int i) {
                Intrinsics.checkNotNullParameter(child, "child");
                return !this.this$0.slideBackViewGroup.isActivityExitAnimating();
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public int getViewVerticalDragRange(View child) {
                Intrinsics.checkNotNullParameter(child, "child");
                return this.mSlideWrapperViewGroup.getHeight();
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public int clampViewPositionVertical(View child, int i, int i2) {
                Intrinsics.checkNotNullParameter(child, "child");
                return Integer.max(this.mSlideChildTopWhenCapture - 50, i);
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public void onViewCaptured(View capturedChild, int i) {
                Intrinsics.checkNotNullParameter(capturedChild, "capturedChild");
                if (this.mSlideChildHeight != capturedChild.getHeight()) {
                    this.mSlideChildHeight = capturedChild.getHeight();
                    this.mSlideChildTopWhenCapture = capturedChild.getTop();
                }
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public void onViewPositionChanged(View changedView, int i, int i2, int i3, int i4) {
                Intrinsics.checkNotNullParameter(changedView, "changedView");
                PickerSlideBackHelper.Companion.getMSlideDistance().setValue(Integer.valueOf(i2));
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public void onViewDragStateChanged(int i) {
                if (i == 1) {
                    invokeSlideStart();
                } else if (i != 0 || !this.mShouldInvokeSlideExit) {
                } else {
                    this.mShouldInvokeSlideExit = false;
                    invokeSlideExit();
                }
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public void onViewReleased(View releasedChild, float f, float f2) {
                int i;
                Intrinsics.checkNotNullParameter(releasedChild, "releasedChild");
                if (Math.abs(f2) < this.mFlingMinVelocity) {
                    int top = releasedChild.getTop();
                    int i2 = this.mSlideChildTopWhenCapture;
                    int i3 = this.mSlideChildHeight;
                    if ((top - i2) / i3 >= 0.5f) {
                        this.mShouldInvokeSlideExit = true;
                        i = i2 + i3;
                    } else {
                        i = i2;
                    }
                } else if (f2 > 0.0f) {
                    this.mShouldInvokeSlideExit = true;
                    i = this.mSlideChildTopWhenCapture + this.mSlideChildHeight;
                } else {
                    i = this.mSlideChildTopWhenCapture;
                }
                if (this.mShouldInvokeSlideExit) {
                    invokeFlingExitStart();
                }
                this.this$0.getViewDragHelper().settleCapturedViewAt(0, i);
                this.mSlideWrapperViewGroup.invalidate();
            }

            public final void invokeSlideStart() {
                PickerSlideLayer.PickerSlideExit mPickerSlideExit = this.this$0.getMPickerSlideExit();
                if (mPickerSlideExit == null) {
                    return;
                }
                mPickerSlideExit.onSlideStart();
            }

            public final void invokeFlingExitStart() {
                PickerSlideLayer.PickerSlideExit mPickerSlideExit = this.this$0.getMPickerSlideExit();
                if (mPickerSlideExit == null) {
                    return;
                }
                mPickerSlideExit.onFlingExitStart();
            }

            public final void invokeSlideExit() {
                PickerSlideLayer.PickerSlideExit mPickerSlideExit = this.this$0.getMPickerSlideExit();
                if (mPickerSlideExit == null) {
                    return;
                }
                mPickerSlideExit.onSlideExit();
            }
        }
    }
}
