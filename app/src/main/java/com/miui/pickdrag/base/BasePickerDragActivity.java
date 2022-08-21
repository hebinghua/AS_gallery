package com.miui.pickdrag.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.lifecycle.Observer;
import com.miui.pickdrag.PickerSlideLayer;
import com.miui.pickdrag.R$anim;
import com.miui.pickdrag.R$id;
import com.miui.pickdrag.R$layout;
import com.miui.pickdrag.anim.ActivityAnimationController;
import com.miui.pickdrag.anim.BlurAnimationController;
import com.miui.pickdrag.anim.PageAnimationListener;
import com.miui.pickdrag.anim.PickerStackAnimListener;
import com.miui.pickdrag.base.PickerSlideBackHelper;
import com.miui.pickdrag.base.gesture.GestureSlideUpCallback;
import com.miui.pickdrag.base.gesture.GestureSlideUpHelper;
import com.miui.pickdrag.base.gesture.KeyDispatchCallback;
import com.miui.pickdrag.base.gesture.KeyDispatchHelper;
import com.miui.pickdrag.base.stack.PickerActivityStackImpl;
import com.miui.pickdrag.base.stack.PickerActivityStacker;
import com.miui.pickdrag.blur.BlurController;
import com.miui.pickdrag.blur.BlurControllerFactory;
import com.miui.pickdrag.utils.Device;
import com.miui.pickdrag.utils.SystemBarManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import miuix.appcompat.app.AppCompatActivity;
import miuix.smooth.SmoothFrameLayout;

/* compiled from: BasePickerDragActivity.kt */
/* loaded from: classes3.dex */
public abstract class BasePickerDragActivity extends AppCompatActivity implements PickerSlideLayer.PickerSlideExit, PageAnimationListener, GestureSlideUpCallback, PickerStackAnimListener {
    public static int PICKER_ACTIVITY_COUNT;
    public View dragContentView;
    public boolean isBlurShowing;
    public boolean isContentViewInited;
    public boolean isResume;
    public int levelInPickerStack;
    public ActivityAnimationController mAnimationController;
    public FrameLayout mBlackBackground;
    public BlurAnimationController mBlurAnimController;
    public BlurController mBlurController;
    public View mBottomSpaceHolder;
    public FrameLayout mContentContainer;
    public LinearLayout mDecorContentContainer;
    public GestureSlideUpHelper mGestureSlideUpHelper;
    public KeyDispatchHelper mKeyDispatchHelper;
    public final Observer<Integer> mSlideAnimObserver = new Observer() { // from class: com.miui.pickdrag.base.BasePickerDragActivity$$ExternalSyntheticLambda0
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            BasePickerDragActivity.$r8$lambda$OO2dvBJAErFXQGmpuZNJFdFARh8(BasePickerDragActivity.this, (Integer) obj);
        }
    };
    public PickerSlideLayer mSlideLayer;
    public SmoothFrameLayout mSmoothContentContainer;
    public static final Companion Companion = new Companion(null);
    public static final String TAG = BasePickerDragActivity.class.getSimpleName();
    public static final PickerActivityStacker mPickerActivityStacker = new PickerActivityStackImpl();

    public static /* synthetic */ void $r8$lambda$OO2dvBJAErFXQGmpuZNJFdFARh8(BasePickerDragActivity basePickerDragActivity, Integer num) {
        m1844mSlideAnimObserver$lambda0(basePickerDragActivity, num);
    }

    public int[] canSlideViewIds() {
        return null;
    }

    @Override // com.miui.pickdrag.PickerSlideLayer.PickerSlideExit
    public void onSlideStart() {
    }

    public static final /* synthetic */ BlurAnimationController access$getMBlurAnimController$p(BasePickerDragActivity basePickerDragActivity) {
        return basePickerDragActivity.mBlurAnimController;
    }

    public final int getLevelInPickerStack() {
        return this.levelInPickerStack;
    }

    /* renamed from: mSlideAnimObserver$lambda-0 */
    public static final void m1844mSlideAnimObserver$lambda0(BasePickerDragActivity this$0, Integer num) {
        PickerSlideLayer pickerSlideLayer;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (!this$0.isResume && (pickerSlideLayer = this$0.mSlideLayer) != null) {
            Intrinsics.checkNotNull(pickerSlideLayer);
            Intrinsics.checkNotNull(num);
            pickerSlideLayer.followOtherActivitySlide(num.intValue());
        }
        BlurAnimationController blurAnimationController = this$0.mBlurAnimController;
        if (blurAnimationController != null) {
            Intrinsics.checkNotNull(blurAnimationController);
            boolean z = this$0.isBlurShowing;
            Intrinsics.checkNotNull(num);
            blurAnimationController.checkAndInvalidateBlurAlpha(z, num.intValue());
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if (PICKER_ACTIVITY_COUNT >= 1) {
            overridePendingTransition(0, R$anim.anim_slide_when_open_other_activity);
        }
        PickerSlideBackHelper.Companion companion = PickerSlideBackHelper.Companion;
        companion.getMSlideDistance().setValue(0);
        super.onCreate(bundle);
        mPickerActivityStacker.add(this);
        int i = PICKER_ACTIVITY_COUNT + 1;
        PICKER_ACTIVITY_COUNT = i;
        this.levelInPickerStack = i;
        setContentView(R$layout.layout_base_slide_picker_activity);
        this.mDecorContentContainer = (LinearLayout) findViewById(R$id.content_container);
        this.mSmoothContentContainer = (SmoothFrameLayout) findViewById(R$id.smooth_container);
        this.mContentContainer = (FrameLayout) findViewById(R$id.content);
        this.mSlideLayer = (PickerSlideLayer) findViewById(R$id.dragLayer);
        this.mBottomSpaceHolder = findViewById(R$id.viewBottomSpaceHolder);
        this.mBlackBackground = (FrameLayout) findViewById(R$id.picker_black_background);
        initAnimationController();
        companion.getMSlideDistance().observeForever(this.mSlideAnimObserver);
        initOnActivityCreate();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onNewIntent(intent);
        initOnActivityCreate();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= 29) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        int i = newConfig.uiMode & 48;
        if (i == 16) {
            onDarkModeChanged(false);
        } else if (i != 32) {
        } else {
            onDarkModeChanged(true);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.isResume = false;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.isResume = true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        ActivityAnimationController activityAnimationController = this.mAnimationController;
        if (activityAnimationController != null) {
            Intrinsics.checkNotNull(activityAnimationController);
            activityAnimationController.onUnBind();
        }
        super.onDestroy();
        Companion.remove(this);
        PICKER_ACTIVITY_COUNT--;
        PickerSlideBackHelper.Companion.getMSlideDistance().removeObserver(this.mSlideAnimObserver);
        GestureSlideUpHelper gestureSlideUpHelper = this.mGestureSlideUpHelper;
        if (gestureSlideUpHelper != null) {
            Intrinsics.checkNotNull(gestureSlideUpHelper);
            Context applicationContext = getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
            gestureSlideUpHelper.unregister(applicationContext);
        }
        KeyDispatchHelper keyDispatchHelper = this.mKeyDispatchHelper;
        if (keyDispatchHelper != null) {
            Intrinsics.checkNotNull(keyDispatchHelper);
            Context applicationContext2 = getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext2, "applicationContext");
            keyDispatchHelper.unregister(applicationContext2);
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        boolean z = true;
        if (PICKER_ACTIVITY_COUNT != 1) {
            z = false;
        }
        checkOrStartActivityFinishAnimation(z);
    }

    public final void checkOrStartActivityFinishAnimation(boolean z) {
        boolean z2;
        ActivityAnimationController activityAnimationController = this.mAnimationController;
        if (activityAnimationController != null) {
            Intrinsics.checkNotNull(activityAnimationController);
            z2 = activityAnimationController.onActivityFinish(z);
        } else {
            z2 = false;
        }
        if (!z2) {
            clearWindowBlurForFirstPickerPage();
            super.finish();
        }
    }

    @Override // com.miui.pickdrag.PickerSlideLayer.PickerSlideExit
    public void onSlideExit() {
        finishWithoutAnimation();
    }

    @Override // com.miui.pickdrag.PickerSlideLayer.PickerSlideExit
    public void onFlingExitStart() {
        View view = this.mBottomSpaceHolder;
        Intrinsics.checkNotNull(view);
        view.setVisibility(4);
    }

    @Override // com.miui.pickdrag.anim.PageAnimationListener
    public void onPageOutAnimationStart(boolean z) {
        View view = this.mBottomSpaceHolder;
        Intrinsics.checkNotNull(view);
        view.setVisibility(4);
    }

    public final void finishWithoutAnimation() {
        clearWindowBlurForFirstPickerPage();
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override // com.miui.pickdrag.anim.PageAnimationListener
    public void onPageInAnimationEnd(boolean z) {
        View view = this.mBottomSpaceHolder;
        Intrinsics.checkNotNull(view);
        view.setVisibility(0);
    }

    @Override // com.miui.pickdrag.anim.PickerStackAnimListener
    public void onPageHorizontalAnimUpdate(int i, float f, boolean z) {
        if (i == this.levelInPickerStack) {
            boolean z2 = true;
            if (Device.isHighDevice()) {
                FrameLayout frameLayout = this.mBlackBackground;
                Intrinsics.checkNotNull(frameLayout);
                frameLayout.setAlpha((1 - f) * 0.4f);
                return;
            }
            FrameLayout frameLayout2 = this.mBlackBackground;
            Intrinsics.checkNotNull(frameLayout2);
            if (frameLayout2.getAlpha() != 0.4f) {
                z2 = false;
            }
            if (z2) {
                return;
            }
            FrameLayout frameLayout3 = this.mBlackBackground;
            Intrinsics.checkNotNull(frameLayout3);
            frameLayout3.setAlpha(0.4f);
        }
    }

    @Override // com.miui.pickdrag.anim.PickerStackAnimListener
    public void onPageVerticalAnimUpdate(int i, float f, boolean z) {
        updateBlurProgressIfNeed(i, f, z);
    }

    public final void updateBlurProgressIfNeed(int i, float f, boolean z) {
        BlurAnimationController blurAnimationController = this.mBlurAnimController;
        if (blurAnimationController == null) {
            return;
        }
        float f2 = 0.0f;
        if (i == 3 || i == 4) {
            f = z ? 1.0f : 0.0f;
        } else if (!z) {
            f = 1.0f - f;
        }
        if (z || f >= 0.05d) {
            f2 = f;
        }
        Intrinsics.checkNotNull(blurAnimationController);
        blurAnimationController.updateBlurAlpha(f2);
    }

    public final void initOnActivityCreate() {
        boolean z = true;
        if (PICKER_ACTIVITY_COUNT != 1) {
            z = false;
        }
        this.isBlurShowing = z;
        ActivityAnimationController activityAnimationController = this.mAnimationController;
        if (activityAnimationController != null) {
            Intrinsics.checkNotNull(activityAnimationController);
            activityAnimationController.onActivityCreated(z);
        }
        initGestureSlideUp();
        initKeyDispatch();
    }

    public final void initGestureSlideUp() {
        if (this.mGestureSlideUpHelper != null) {
            return;
        }
        GestureSlideUpHelper gestureSlideUpHelper = new GestureSlideUpHelper(this);
        this.mGestureSlideUpHelper = gestureSlideUpHelper;
        Intrinsics.checkNotNull(gestureSlideUpHelper);
        Context applicationContext = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
        gestureSlideUpHelper.register(applicationContext);
    }

    public final void initKeyDispatch() {
        if (this.mKeyDispatchHelper != null) {
            return;
        }
        KeyDispatchHelper keyDispatchHelper = new KeyDispatchHelper(new KeyDispatchCallback() { // from class: com.miui.pickdrag.base.BasePickerDragActivity$initKeyDispatch$1
            @Override // com.miui.pickdrag.base.gesture.KeyDispatchCallback
            public void onKeyDispatched(int i) {
                if (i == 3) {
                    BasePickerDragActivity.this.finishWithoutAnimation();
                    if (BasePickerDragActivity.access$getMBlurAnimController$p(BasePickerDragActivity.this) == null) {
                        return;
                    }
                    BlurAnimationController access$getMBlurAnimController$p = BasePickerDragActivity.access$getMBlurAnimController$p(BasePickerDragActivity.this);
                    Intrinsics.checkNotNull(access$getMBlurAnimController$p);
                    access$getMBlurAnimController$p.updateBlurAlpha(0.0f);
                }
            }
        });
        this.mKeyDispatchHelper = keyDispatchHelper;
        Intrinsics.checkNotNull(keyDispatchHelper);
        Context applicationContext = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
        keyDispatchHelper.register(applicationContext);
    }

    @Override // com.miui.pickdrag.base.gesture.GestureSlideUpCallback
    public void onGestureSlideUp() {
        exitPicker();
    }

    public final void initAnimationController() {
        this.mBlurController = BlurControllerFactory.INSTANCE.createBlurController(this);
        Context applicationContext = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
        BlurController blurController = this.mBlurController;
        Intrinsics.checkNotNull(blurController);
        LinearLayout linearLayout = this.mDecorContentContainer;
        Intrinsics.checkNotNull(linearLayout);
        this.mBlurAnimController = new BlurAnimationController(applicationContext, blurController, linearLayout);
        this.mAnimationController = new ActivityAnimationController(this, this.mSmoothContentContainer, this);
    }

    public final void adaptNavigationBarByDarkMode(boolean z) {
        SystemBarManager.setNavigationBar(this, !z, z ? -16777216 : -1);
    }

    public final void onDarkModeChanged(boolean z) {
        adaptNavigationBarByDarkMode(z);
    }

    public final View setDragContentView(int i) {
        View inflate = LayoutInflater.from(this).inflate(i, (ViewGroup) this.mContentContainer, true);
        this.dragContentView = inflate;
        this.isContentViewInited = true;
        return inflate;
    }

    public final void clearWindowBlurForFirstPickerPage() {
        BlurController blurController;
        if (PICKER_ACTIVITY_COUNT != 1 || (blurController = this.mBlurController) == null) {
            return;
        }
        Intrinsics.checkNotNull(blurController);
        blurController.setBlurEnabled(false);
    }

    public final boolean isScheduleExitAnim() {
        ActivityAnimationController activityAnimationController = this.mAnimationController;
        if (activityAnimationController != null) {
            Intrinsics.checkNotNull(activityAnimationController);
            if (activityAnimationController.isExitAnimStart()) {
                return true;
            }
        }
        return false;
    }

    public final void exitPicker() {
        FrameLayout frameLayout = this.mBlackBackground;
        Intrinsics.checkNotNull(frameLayout);
        frameLayout.setAlpha(0.0f);
        checkOrStartActivityFinishAnimation(true);
    }

    /* compiled from: BasePickerDragActivity.kt */
    /* loaded from: classes3.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final void remove(BasePickerDragActivity basePickerDragActivity) {
            BasePickerDragActivity.mPickerActivityStacker.remove(basePickerDragActivity);
        }
    }
}
