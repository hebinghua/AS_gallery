package androidx.core.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContentInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.collection.SimpleArrayMap;
import androidx.core.R$id;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressLint({"PrivateConstructorForUtilityClass"})
/* loaded from: classes.dex */
public class ViewCompat {
    public static Field sAccessibilityDelegateField;
    public static Field sMinHeightField;
    public static boolean sMinHeightFieldFetched;
    public static Field sMinWidthField;
    public static boolean sMinWidthFieldFetched;
    public static ThreadLocal<Rect> sThreadLocalRect;
    public static WeakHashMap<View, String> sTransitionNameMap;
    public static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap = null;
    public static boolean sAccessibilityDelegateCheckFailed = false;
    public static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS = {R$id.accessibility_custom_action_0, R$id.accessibility_custom_action_1, R$id.accessibility_custom_action_2, R$id.accessibility_custom_action_3, R$id.accessibility_custom_action_4, R$id.accessibility_custom_action_5, R$id.accessibility_custom_action_6, R$id.accessibility_custom_action_7, R$id.accessibility_custom_action_8, R$id.accessibility_custom_action_9, R$id.accessibility_custom_action_10, R$id.accessibility_custom_action_11, R$id.accessibility_custom_action_12, R$id.accessibility_custom_action_13, R$id.accessibility_custom_action_14, R$id.accessibility_custom_action_15, R$id.accessibility_custom_action_16, R$id.accessibility_custom_action_17, R$id.accessibility_custom_action_18, R$id.accessibility_custom_action_19, R$id.accessibility_custom_action_20, R$id.accessibility_custom_action_21, R$id.accessibility_custom_action_22, R$id.accessibility_custom_action_23, R$id.accessibility_custom_action_24, R$id.accessibility_custom_action_25, R$id.accessibility_custom_action_26, R$id.accessibility_custom_action_27, R$id.accessibility_custom_action_28, R$id.accessibility_custom_action_29, R$id.accessibility_custom_action_30, R$id.accessibility_custom_action_31};
    public static final OnReceiveContentViewBehavior NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR = ViewCompat$$ExternalSyntheticLambda0.INSTANCE;
    public static final AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();

    /* loaded from: classes.dex */
    public interface OnUnhandledKeyEventListenerCompat {
        boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent);
    }

    public static /* synthetic */ ContentInfoCompat lambda$static$0(ContentInfoCompat contentInfoCompat) {
        return contentInfoCompat;
    }

    public static Rect getEmptyTempRect() {
        if (sThreadLocalRect == null) {
            sThreadLocalRect = new ThreadLocal<>();
        }
        Rect rect = sThreadLocalRect.get();
        if (rect == null) {
            rect = new Rect();
            sThreadLocalRect.set(rect);
        }
        rect.setEmpty();
        return rect;
    }

    public static void saveAttributeDataForStyleable(View view, @SuppressLint({"ContextFirst"}) Context context, int[] iArr, AttributeSet attributeSet, TypedArray typedArray, int i, int i2) {
        if (Build.VERSION.SDK_INT >= 29) {
            Api29Impl.saveAttributeDataForStyleable(view, context, iArr, attributeSet, typedArray, i, i2);
        }
    }

    public static void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        view.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat.unwrap());
    }

    public static void setAccessibilityDelegate(View view, AccessibilityDelegateCompat accessibilityDelegateCompat) {
        if (accessibilityDelegateCompat == null && (getAccessibilityDelegateInternal(view) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter)) {
            accessibilityDelegateCompat = new AccessibilityDelegateCompat();
        }
        view.setAccessibilityDelegate(accessibilityDelegateCompat == null ? null : accessibilityDelegateCompat.getBridge());
    }

    @SuppressLint({"InlinedApi"})
    public static int getImportantForAutofill(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getImportantForAutofill(view);
        }
        return 0;
    }

    public static void setImportantForAutofill(View view, int i) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setImportantForAutofill(view, i);
        }
    }

    public static AccessibilityDelegateCompat getAccessibilityDelegate(View view) {
        View.AccessibilityDelegate accessibilityDelegateInternal = getAccessibilityDelegateInternal(view);
        if (accessibilityDelegateInternal == null) {
            return null;
        }
        if (accessibilityDelegateInternal instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
            return ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter) accessibilityDelegateInternal).mCompat;
        }
        return new AccessibilityDelegateCompat(accessibilityDelegateInternal);
    }

    public static void ensureAccessibilityDelegateCompat(View view) {
        AccessibilityDelegateCompat accessibilityDelegate = getAccessibilityDelegate(view);
        if (accessibilityDelegate == null) {
            accessibilityDelegate = new AccessibilityDelegateCompat();
        }
        setAccessibilityDelegate(view, accessibilityDelegate);
    }

    public static View.AccessibilityDelegate getAccessibilityDelegateInternal(View view) {
        if (Build.VERSION.SDK_INT >= 29) {
            return Api29Impl.getAccessibilityDelegate(view);
        }
        return getAccessibilityDelegateThroughReflection(view);
    }

    public static View.AccessibilityDelegate getAccessibilityDelegateThroughReflection(View view) {
        if (sAccessibilityDelegateCheckFailed) {
            return null;
        }
        if (sAccessibilityDelegateField == null) {
            try {
                Field declaredField = View.class.getDeclaredField("mAccessibilityDelegate");
                sAccessibilityDelegateField = declaredField;
                declaredField.setAccessible(true);
            } catch (Throwable unused) {
                sAccessibilityDelegateCheckFailed = true;
                return null;
            }
        }
        try {
            Object obj = sAccessibilityDelegateField.get(view);
            if (!(obj instanceof View.AccessibilityDelegate)) {
                return null;
            }
            return (View.AccessibilityDelegate) obj;
        } catch (Throwable unused2) {
            sAccessibilityDelegateCheckFailed = true;
            return null;
        }
    }

    public static boolean hasTransientState(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.hasTransientState(view);
        }
        return false;
    }

    public static void postInvalidateOnAnimation(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.postInvalidateOnAnimation(view);
        } else {
            view.postInvalidate();
        }
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.postOnAnimation(view, runnable);
        } else {
            view.postDelayed(runnable, ValueAnimator.getFrameDelay());
        }
    }

    @SuppressLint({"LambdaLast"})
    public static void postOnAnimationDelayed(View view, Runnable runnable, long j) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.postOnAnimationDelayed(view, runnable, j);
        } else {
            view.postDelayed(runnable, ValueAnimator.getFrameDelay() + j);
        }
    }

    public static int getImportantForAccessibility(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getImportantForAccessibility(view);
        }
        return 0;
    }

    public static void setImportantForAccessibility(View view, int i) {
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 19) {
            Api16Impl.setImportantForAccessibility(view, i);
        } else if (i2 < 16) {
        } else {
            if (i == 4) {
                i = 2;
            }
            Api16Impl.setImportantForAccessibility(view, i);
        }
    }

    public static boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.performAccessibilityAction(view, i, bundle);
        }
        return false;
    }

    public static void replaceAccessibilityAction(View view, AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
        if (accessibilityViewCommand == null && charSequence == null) {
            removeAccessibilityAction(view, accessibilityActionCompat.getId());
        } else {
            addAccessibilityAction(view, accessibilityActionCompat.createReplacementAction(charSequence, accessibilityViewCommand));
        }
    }

    public static void addAccessibilityAction(View view, AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat) {
        if (Build.VERSION.SDK_INT >= 21) {
            ensureAccessibilityDelegateCompat(view);
            removeActionWithId(accessibilityActionCompat.getId(), view);
            getActionList(view).add(accessibilityActionCompat);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }

    public static void removeAccessibilityAction(View view, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            removeActionWithId(i, view);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }

    public static void removeActionWithId(int i, View view) {
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = getActionList(view);
        for (int i2 = 0; i2 < actionList.size(); i2++) {
            if (actionList.get(i2).getId() == i) {
                actionList.remove(i2);
                return;
            }
        }
    }

    public static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(View view) {
        int i = R$id.tag_accessibility_actions;
        ArrayList arrayList = (ArrayList) view.getTag(i);
        if (arrayList == null) {
            ArrayList arrayList2 = new ArrayList();
            view.setTag(i, arrayList2);
            return arrayList2;
        }
        return arrayList;
    }

    public static void setStateDescription(View view, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 19) {
            stateDescriptionProperty().set(view, charSequence);
        }
    }

    public static CharSequence getStateDescription(View view) {
        return stateDescriptionProperty().get(view);
    }

    public static int getLayoutDirection(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getLayoutDirection(view);
        }
        return 0;
    }

    public static int getAccessibilityLiveRegion(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.getAccessibilityLiveRegion(view);
        }
        return 0;
    }

    public static int getPaddingStart(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getPaddingStart(view);
        }
        return view.getPaddingLeft();
    }

    public static int getPaddingEnd(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getPaddingEnd(view);
        }
        return view.getPaddingRight();
    }

    @Deprecated
    public static float getTranslationX(View view) {
        return view.getTranslationX();
    }

    @Deprecated
    public static float getTranslationY(View view) {
        return view.getTranslationY();
    }

    public static int getMinimumWidth(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getMinimumWidth(view);
        }
        if (!sMinWidthFieldFetched) {
            try {
                Field declaredField = View.class.getDeclaredField("mMinWidth");
                sMinWidthField = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException unused) {
            }
            sMinWidthFieldFetched = true;
        }
        Field field = sMinWidthField;
        if (field == null) {
            return 0;
        }
        try {
            return ((Integer) field.get(view)).intValue();
        } catch (Exception unused2) {
            return 0;
        }
    }

    public static int getMinimumHeight(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getMinimumHeight(view);
        }
        if (!sMinHeightFieldFetched) {
            try {
                Field declaredField = View.class.getDeclaredField("mMinHeight");
                sMinHeightField = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException unused) {
            }
            sMinHeightFieldFetched = true;
        }
        Field field = sMinHeightField;
        if (field == null) {
            return 0;
        }
        try {
            return ((Integer) field.get(view)).intValue();
        } catch (Exception unused2) {
            return 0;
        }
    }

    public static ViewPropertyAnimatorCompat animate(View view) {
        if (sViewPropertyAnimatorMap == null) {
            sViewPropertyAnimatorMap = new WeakHashMap<>();
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = sViewPropertyAnimatorMap.get(view);
        if (viewPropertyAnimatorCompat == null) {
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2 = new ViewPropertyAnimatorCompat(view);
            sViewPropertyAnimatorMap.put(view, viewPropertyAnimatorCompat2);
            return viewPropertyAnimatorCompat2;
        }
        return viewPropertyAnimatorCompat;
    }

    public static void setElevation(View view, float f) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setElevation(view, f);
        }
    }

    public static float getElevation(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getElevation(view);
        }
        return 0.0f;
    }

    public static void setTranslationZ(View view, float f) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setTranslationZ(view, f);
        }
    }

    public static float getTranslationZ(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getTranslationZ(view);
        }
        return 0.0f;
    }

    public static void setTransitionName(View view, String str) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setTransitionName(view, str);
            return;
        }
        if (sTransitionNameMap == null) {
            sTransitionNameMap = new WeakHashMap<>();
        }
        sTransitionNameMap.put(view, str);
    }

    public static String getTransitionName(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getTransitionName(view);
        }
        WeakHashMap<View, String> weakHashMap = sTransitionNameMap;
        if (weakHashMap != null) {
            return weakHashMap.get(view);
        }
        return null;
    }

    @Deprecated
    public static int getWindowSystemUiVisibility(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getWindowSystemUiVisibility(view);
        }
        return 0;
    }

    public static void requestApplyInsets(View view) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 20) {
            Api20Impl.requestApplyInsets(view);
        } else if (i < 16) {
        } else {
            Api16Impl.requestFitSystemWindows(view);
        }
    }

    public static void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setOnApplyWindowInsetsListener(view, onApplyWindowInsetsListener);
        }
    }

    public static WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        WindowInsets windowInsets;
        if (Build.VERSION.SDK_INT >= 21 && (windowInsets = windowInsetsCompat.toWindowInsets()) != null) {
            WindowInsets onApplyWindowInsets = Api20Impl.onApplyWindowInsets(view, windowInsets);
            if (!onApplyWindowInsets.equals(windowInsets)) {
                return WindowInsetsCompat.toWindowInsetsCompat(onApplyWindowInsets, view);
            }
        }
        return windowInsetsCompat;
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        WindowInsets windowInsets;
        if (Build.VERSION.SDK_INT >= 21 && (windowInsets = windowInsetsCompat.toWindowInsets()) != null) {
            WindowInsets dispatchApplyWindowInsets = Api20Impl.dispatchApplyWindowInsets(view, windowInsets);
            if (!dispatchApplyWindowInsets.equals(windowInsets)) {
                return WindowInsetsCompat.toWindowInsetsCompat(dispatchApplyWindowInsets, view);
            }
        }
        return windowInsetsCompat;
    }

    public static WindowInsetsCompat getRootWindowInsets(View view) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            return Api23Impl.getRootWindowInsets(view);
        }
        if (i < 21) {
            return null;
        }
        return Api21Impl.getRootWindowInsets(view);
    }

    public static WindowInsetsCompat computeSystemWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, Rect rect) {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.computeSystemWindowInsets(view, windowInsetsCompat, rect) : windowInsetsCompat;
    }

    public static String[] getOnReceiveContentMimeTypes(View view) {
        if (Build.VERSION.SDK_INT >= 31) {
            return Api31Impl.getReceiveContentMimeTypes(view);
        }
        return (String[]) view.getTag(R$id.tag_on_receive_content_mime_types);
    }

    public static ContentInfoCompat performReceiveContent(View view, ContentInfoCompat contentInfoCompat) {
        if (Log.isLoggable("ViewCompat", 3)) {
            Log.d("ViewCompat", "performReceiveContent: " + contentInfoCompat + ", view=" + view.getClass().getSimpleName() + "[" + view.getId() + "]");
        }
        if (Build.VERSION.SDK_INT >= 31) {
            return Api31Impl.performReceiveContent(view, contentInfoCompat);
        }
        OnReceiveContentListener onReceiveContentListener = (OnReceiveContentListener) view.getTag(R$id.tag_on_receive_content_listener);
        if (onReceiveContentListener != null) {
            ContentInfoCompat onReceiveContent = onReceiveContentListener.onReceiveContent(view, contentInfoCompat);
            if (onReceiveContent != null) {
                return getFallback(view).onReceiveContent(onReceiveContent);
            }
            return null;
        }
        return getFallback(view).onReceiveContent(contentInfoCompat);
    }

    public static OnReceiveContentViewBehavior getFallback(View view) {
        if (view instanceof OnReceiveContentViewBehavior) {
            return (OnReceiveContentViewBehavior) view;
        }
        return NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR;
    }

    /* loaded from: classes.dex */
    public static final class Api31Impl {
        public static void setOnReceiveContentListener(View view, String[] strArr, OnReceiveContentListener onReceiveContentListener) {
            if (onReceiveContentListener == null) {
                view.setOnReceiveContentListener(strArr, null);
            } else {
                view.setOnReceiveContentListener(strArr, new OnReceiveContentListenerAdapter(onReceiveContentListener));
            }
        }

        public static String[] getReceiveContentMimeTypes(View view) {
            return view.getReceiveContentMimeTypes();
        }

        public static ContentInfoCompat performReceiveContent(View view, ContentInfoCompat contentInfoCompat) {
            ContentInfo contentInfo = contentInfoCompat.toContentInfo();
            ContentInfo performReceiveContent = view.performReceiveContent(contentInfo);
            if (performReceiveContent == null) {
                return null;
            }
            return performReceiveContent == contentInfo ? contentInfoCompat : ContentInfoCompat.toContentInfoCompat(performReceiveContent);
        }
    }

    /* loaded from: classes.dex */
    public static final class OnReceiveContentListenerAdapter implements android.view.OnReceiveContentListener {
        public final OnReceiveContentListener mJetpackListener;

        public OnReceiveContentListenerAdapter(OnReceiveContentListener onReceiveContentListener) {
            this.mJetpackListener = onReceiveContentListener;
        }

        @Override // android.view.OnReceiveContentListener
        public ContentInfo onReceiveContent(View view, ContentInfo contentInfo) {
            ContentInfoCompat contentInfoCompat = ContentInfoCompat.toContentInfoCompat(contentInfo);
            ContentInfoCompat onReceiveContent = this.mJetpackListener.onReceiveContent(view, contentInfoCompat);
            if (onReceiveContent == null) {
                return null;
            }
            return onReceiveContent == contentInfoCompat ? contentInfo : onReceiveContent.toContentInfo();
        }
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.setBackground(view, drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static ColorStateList getBackgroundTintList(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getBackgroundTintList(view);
        }
        if (!(view instanceof TintableBackgroundView)) {
            return null;
        }
        return ((TintableBackgroundView) view).getSupportBackgroundTintList();
    }

    public static void setBackgroundTintList(View view, ColorStateList colorStateList) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            Api21Impl.setBackgroundTintList(view, colorStateList);
            if (i != 21) {
                return;
            }
            Drawable background = view.getBackground();
            boolean z = (Api21Impl.getBackgroundTintList(view) == null && Api21Impl.getBackgroundTintMode(view) == null) ? false : true;
            if (background == null || !z) {
                return;
            }
            if (background.isStateful()) {
                background.setState(view.getDrawableState());
            }
            Api16Impl.setBackground(view, background);
        } else if (!(view instanceof TintableBackgroundView)) {
        } else {
            ((TintableBackgroundView) view).setSupportBackgroundTintList(colorStateList);
        }
    }

    public static PorterDuff.Mode getBackgroundTintMode(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getBackgroundTintMode(view);
        }
        if (!(view instanceof TintableBackgroundView)) {
            return null;
        }
        return ((TintableBackgroundView) view).getSupportBackgroundTintMode();
    }

    public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            Api21Impl.setBackgroundTintMode(view, mode);
            if (i != 21) {
                return;
            }
            Drawable background = view.getBackground();
            boolean z = (Api21Impl.getBackgroundTintList(view) == null && Api21Impl.getBackgroundTintMode(view) == null) ? false : true;
            if (background == null || !z) {
                return;
            }
            if (background.isStateful()) {
                background.setState(view.getDrawableState());
            }
            Api16Impl.setBackground(view, background);
        } else if (!(view instanceof TintableBackgroundView)) {
        } else {
            ((TintableBackgroundView) view).setSupportBackgroundTintMode(mode);
        }
    }

    public static void stopNestedScroll(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.stopNestedScroll(view);
        } else if (!(view instanceof NestedScrollingChild)) {
        } else {
            ((NestedScrollingChild) view).stopNestedScroll();
        }
    }

    public static boolean isLaidOut(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.isLaidOut(view);
        }
        return view.getWidth() > 0 && view.getHeight() > 0;
    }

    public static void offsetTopAndBottom(View view, int i) {
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 23) {
            view.offsetTopAndBottom(i);
        } else if (i2 >= 21) {
            Rect emptyTempRect = getEmptyTempRect();
            boolean z = false;
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                View view2 = (View) parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                z = !emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            compatOffsetTopAndBottom(view, i);
            if (!z || !emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                return;
            }
            ((View) parent).invalidate(emptyTempRect);
        } else {
            compatOffsetTopAndBottom(view, i);
        }
    }

    public static void compatOffsetTopAndBottom(View view, int i) {
        view.offsetTopAndBottom(i);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            ViewParent parent = view.getParent();
            if (!(parent instanceof View)) {
                return;
            }
            tickleInvalidationFlag((View) parent);
        }
    }

    public static void offsetLeftAndRight(View view, int i) {
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 23) {
            view.offsetLeftAndRight(i);
        } else if (i2 >= 21) {
            Rect emptyTempRect = getEmptyTempRect();
            boolean z = false;
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                View view2 = (View) parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                z = !emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            compatOffsetLeftAndRight(view, i);
            if (!z || !emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                return;
            }
            ((View) parent).invalidate(emptyTempRect);
        } else {
            compatOffsetLeftAndRight(view, i);
        }
    }

    public static void compatOffsetLeftAndRight(View view, int i) {
        view.offsetLeftAndRight(i);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            ViewParent parent = view.getParent();
            if (!(parent instanceof View)) {
                return;
            }
            tickleInvalidationFlag((View) parent);
        }
    }

    public static void tickleInvalidationFlag(View view) {
        float translationY = view.getTranslationY();
        view.setTranslationY(1.0f + translationY);
        view.setTranslationY(translationY);
    }

    public static boolean isAttachedToWindow(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.isAttachedToWindow(view);
        }
        return view.getWindowToken() != null;
    }

    public static boolean hasOnClickListeners(View view) {
        if (Build.VERSION.SDK_INT >= 15) {
            return Api15Impl.hasOnClickListeners(view);
        }
        return false;
    }

    public static void setScrollIndicators(View view, int i, int i2) {
        if (Build.VERSION.SDK_INT >= 23) {
            Api23Impl.setScrollIndicators(view, i, i2);
        }
    }

    public static Display getDisplay(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getDisplay(view);
        }
        if (!isAttachedToWindow(view)) {
            return null;
        }
        return ((WindowManager) view.getContext().getSystemService("window")).getDefaultDisplay();
    }

    public static int generateViewId() {
        AtomicInteger atomicInteger;
        int i;
        int i2;
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.generateViewId();
        }
        do {
            atomicInteger = sNextGeneratedId;
            i = atomicInteger.get();
            i2 = i + 1;
            if (i2 > 16777215) {
                i2 = 1;
            }
        } while (!atomicInteger.compareAndSet(i, i2));
        return i;
    }

    public static boolean dispatchUnhandledKeyEventBeforeHierarchy(View view, KeyEvent keyEvent) {
        if (Build.VERSION.SDK_INT >= 28) {
            return false;
        }
        return UnhandledKeyEventManager.at(view).preDispatch(keyEvent);
    }

    public static boolean dispatchUnhandledKeyEventBeforeCallback(View view, KeyEvent keyEvent) {
        if (Build.VERSION.SDK_INT >= 28) {
            return false;
        }
        return UnhandledKeyEventManager.at(view).dispatch(view, keyEvent);
    }

    public static void setScreenReaderFocusable(View view, boolean z) {
        screenReaderFocusableProperty().set(view, Boolean.valueOf(z));
    }

    public static boolean isScreenReaderFocusable(View view) {
        Boolean bool = screenReaderFocusableProperty().get(view);
        return bool != null && bool.booleanValue();
    }

    public static AccessibilityViewProperty<Boolean> screenReaderFocusableProperty() {
        return new AccessibilityViewProperty<Boolean>(R$id.tag_screen_reader_focusable, Boolean.class, 28) { // from class: androidx.core.view.ViewCompat.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            /* renamed from: frameworkGet */
            public Boolean mo78frameworkGet(View view) {
                return Boolean.valueOf(Api28Impl.isScreenReaderFocusable(view));
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public void frameworkSet(View view, Boolean bool) {
                Api28Impl.setScreenReaderFocusable(view, bool.booleanValue());
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public boolean shouldUpdate(Boolean bool, Boolean bool2) {
                return !booleanNullToFalseEquals(bool, bool2);
            }
        };
    }

    public static void setAccessibilityPaneTitle(View view, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 19) {
            paneTitleProperty().set(view, charSequence);
            if (charSequence != null) {
                sAccessibilityPaneVisibilityManager.addAccessibilityPane(view);
            } else {
                sAccessibilityPaneVisibilityManager.removeAccessibilityPane(view);
            }
        }
    }

    public static CharSequence getAccessibilityPaneTitle(View view) {
        return paneTitleProperty().get(view);
    }

    public static AccessibilityViewProperty<CharSequence> paneTitleProperty() {
        return new AccessibilityViewProperty<CharSequence>(R$id.tag_accessibility_pane_title, CharSequence.class, 8, 28) { // from class: androidx.core.view.ViewCompat.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            /* renamed from: frameworkGet */
            public CharSequence mo78frameworkGet(View view) {
                return Api28Impl.getAccessibilityPaneTitle(view);
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public void frameworkSet(View view, CharSequence charSequence) {
                Api28Impl.setAccessibilityPaneTitle(view, charSequence);
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public boolean shouldUpdate(CharSequence charSequence, CharSequence charSequence2) {
                return !TextUtils.equals(charSequence, charSequence2);
            }
        };
    }

    public static AccessibilityViewProperty<CharSequence> stateDescriptionProperty() {
        return new AccessibilityViewProperty<CharSequence>(R$id.tag_state_description, CharSequence.class, 64, 30) { // from class: androidx.core.view.ViewCompat.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            /* renamed from: frameworkGet */
            public CharSequence mo78frameworkGet(View view) {
                return Api30Impl.getStateDescription(view);
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public void frameworkSet(View view, CharSequence charSequence) {
                Api30Impl.setStateDescription(view, charSequence);
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public boolean shouldUpdate(CharSequence charSequence, CharSequence charSequence2) {
                return !TextUtils.equals(charSequence, charSequence2);
            }
        };
    }

    public static boolean isAccessibilityHeading(View view) {
        Boolean bool = accessibilityHeadingProperty().get(view);
        return bool != null && bool.booleanValue();
    }

    public static void setAccessibilityHeading(View view, boolean z) {
        accessibilityHeadingProperty().set(view, Boolean.valueOf(z));
    }

    public static AccessibilityViewProperty<Boolean> accessibilityHeadingProperty() {
        return new AccessibilityViewProperty<Boolean>(R$id.tag_accessibility_heading, Boolean.class, 28) { // from class: androidx.core.view.ViewCompat.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            /* renamed from: frameworkGet */
            public Boolean mo78frameworkGet(View view) {
                return Boolean.valueOf(Api28Impl.isAccessibilityHeading(view));
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public void frameworkSet(View view, Boolean bool) {
                Api28Impl.setAccessibilityHeading(view, bool.booleanValue());
            }

            @Override // androidx.core.view.ViewCompat.AccessibilityViewProperty
            public boolean shouldUpdate(Boolean bool, Boolean bool2) {
                return !booleanNullToFalseEquals(bool, bool2);
            }
        };
    }

    /* loaded from: classes.dex */
    public static abstract class AccessibilityViewProperty<T> {
        public final int mContentChangeType;
        public final int mFrameworkMinimumSdk;
        public final int mTagKey;
        public final Class<T> mType;

        /* renamed from: frameworkGet */
        public abstract T mo78frameworkGet(View view);

        public abstract void frameworkSet(View view, T t);

        public abstract boolean shouldUpdate(T t, T t2);

        public AccessibilityViewProperty(int i, Class<T> cls, int i2) {
            this(i, cls, 0, i2);
        }

        public AccessibilityViewProperty(int i, Class<T> cls, int i2, int i3) {
            this.mTagKey = i;
            this.mType = cls;
            this.mContentChangeType = i2;
            this.mFrameworkMinimumSdk = i3;
        }

        public void set(View view, T t) {
            if (frameworkAvailable()) {
                frameworkSet(view, t);
            } else if (!extrasAvailable() || !shouldUpdate(get(view), t)) {
            } else {
                ViewCompat.ensureAccessibilityDelegateCompat(view);
                view.setTag(this.mTagKey, t);
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, this.mContentChangeType);
            }
        }

        public T get(View view) {
            if (frameworkAvailable()) {
                return mo78frameworkGet(view);
            }
            if (!extrasAvailable()) {
                return null;
            }
            T t = (T) view.getTag(this.mTagKey);
            if (!this.mType.isInstance(t)) {
                return null;
            }
            return t;
        }

        public final boolean frameworkAvailable() {
            return Build.VERSION.SDK_INT >= this.mFrameworkMinimumSdk;
        }

        public final boolean extrasAvailable() {
            return Build.VERSION.SDK_INT >= 19;
        }

        public boolean booleanNullToFalseEquals(Boolean bool, Boolean bool2) {
            return (bool != null && bool.booleanValue()) == (bool2 != null && bool2.booleanValue());
        }
    }

    public static void notifyViewAccessibilityStateChangedIfNeeded(View view, int i) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
        if (!accessibilityManager.isEnabled()) {
            return;
        }
        boolean z = getAccessibilityPaneTitle(view) != null && view.getVisibility() == 0;
        int i2 = 32;
        if (getAccessibilityLiveRegion(view) != 0 || z) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            if (!z) {
                i2 = 2048;
            }
            obtain.setEventType(i2);
            Api19Impl.setContentChangeTypes(obtain, i);
            if (z) {
                obtain.getText().add(getAccessibilityPaneTitle(view));
                setViewImportanceForAccessibilityIfNeeded(view);
            }
            view.sendAccessibilityEventUnchecked(obtain);
        } else if (i == 32) {
            AccessibilityEvent obtain2 = AccessibilityEvent.obtain();
            view.onInitializeAccessibilityEvent(obtain2);
            obtain2.setEventType(32);
            Api19Impl.setContentChangeTypes(obtain2, i);
            obtain2.setSource(view);
            view.onPopulateAccessibilityEvent(obtain2);
            obtain2.getText().add(getAccessibilityPaneTitle(view));
            accessibilityManager.sendAccessibilityEvent(obtain2);
        } else if (view.getParent() == null) {
        } else {
            try {
                Api19Impl.notifySubtreeAccessibilityStateChanged(view.getParent(), view, view, i);
            } catch (AbstractMethodError e) {
                Log.e("ViewCompat", view.getParent().getClass().getSimpleName() + " does not fully implement ViewParent", e);
            }
        }
    }

    public static void setViewImportanceForAccessibilityIfNeeded(View view) {
        if (getImportantForAccessibility(view) == 0) {
            setImportantForAccessibility(view, 1);
        }
        for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
            if (getImportantForAccessibility((View) parent) == 4) {
                setImportantForAccessibility(view, 2);
                return;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class AccessibilityPaneVisibilityManager implements ViewTreeObserver.OnGlobalLayoutListener, View.OnAttachStateChangeListener {
        public final WeakHashMap<View, Boolean> mPanesToVisible = new WeakHashMap<>();

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT < 28) {
                for (Map.Entry<View, Boolean> entry : this.mPanesToVisible.entrySet()) {
                    checkPaneVisibility(entry.getKey(), entry.getValue().booleanValue());
                }
            }
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            registerForLayoutCallback(view);
        }

        public void addAccessibilityPane(View view) {
            this.mPanesToVisible.put(view, Boolean.valueOf(view.getVisibility() == 0));
            view.addOnAttachStateChangeListener(this);
            if (Api19Impl.isAttachedToWindow(view)) {
                registerForLayoutCallback(view);
            }
        }

        public void removeAccessibilityPane(View view) {
            this.mPanesToVisible.remove(view);
            view.removeOnAttachStateChangeListener(this);
            unregisterForLayoutCallback(view);
        }

        public final void checkPaneVisibility(View view, boolean z) {
            boolean z2 = view.getVisibility() == 0;
            if (z != z2) {
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, z2 ? 16 : 32);
                this.mPanesToVisible.put(view, Boolean.valueOf(z2));
            }
        }

        public final void registerForLayoutCallback(View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        public final void unregisterForLayoutCallback(View view) {
            Api16Impl.removeOnGlobalLayoutListener(view.getViewTreeObserver(), this);
        }
    }

    /* loaded from: classes.dex */
    public static class UnhandledKeyEventManager {
        public static final ArrayList<WeakReference<View>> sViewsWithListeners = new ArrayList<>();
        public WeakHashMap<View, Boolean> mViewsContainingListeners = null;
        public SparseArray<WeakReference<View>> mCapturedKeys = null;
        public WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent = null;

        public final SparseArray<WeakReference<View>> getCapturedKeys() {
            if (this.mCapturedKeys == null) {
                this.mCapturedKeys = new SparseArray<>();
            }
            return this.mCapturedKeys;
        }

        public static UnhandledKeyEventManager at(View view) {
            int i = R$id.tag_unhandled_key_event_manager;
            UnhandledKeyEventManager unhandledKeyEventManager = (UnhandledKeyEventManager) view.getTag(i);
            if (unhandledKeyEventManager == null) {
                UnhandledKeyEventManager unhandledKeyEventManager2 = new UnhandledKeyEventManager();
                view.setTag(i, unhandledKeyEventManager2);
                return unhandledKeyEventManager2;
            }
            return unhandledKeyEventManager;
        }

        public boolean dispatch(View view, KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                recalcViewsWithUnhandled();
            }
            View dispatchInOrder = dispatchInOrder(view, keyEvent);
            if (keyEvent.getAction() == 0) {
                int keyCode = keyEvent.getKeyCode();
                if (dispatchInOrder != null && !KeyEvent.isModifierKey(keyCode)) {
                    getCapturedKeys().put(keyCode, new WeakReference<>(dispatchInOrder));
                }
            }
            return dispatchInOrder != null;
        }

        public final View dispatchInOrder(View view, KeyEvent keyEvent) {
            WeakHashMap<View, Boolean> weakHashMap = this.mViewsContainingListeners;
            if (weakHashMap != null && weakHashMap.containsKey(view)) {
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                        View dispatchInOrder = dispatchInOrder(viewGroup.getChildAt(childCount), keyEvent);
                        if (dispatchInOrder != null) {
                            return dispatchInOrder;
                        }
                    }
                }
                if (onUnhandledKeyEvent(view, keyEvent)) {
                    return view;
                }
            }
            return null;
        }

        public boolean preDispatch(KeyEvent keyEvent) {
            int indexOfKey;
            WeakReference<KeyEvent> weakReference = this.mLastDispatchedPreViewKeyEvent;
            if (weakReference == null || weakReference.get() != keyEvent) {
                this.mLastDispatchedPreViewKeyEvent = new WeakReference<>(keyEvent);
                WeakReference<View> weakReference2 = null;
                SparseArray<WeakReference<View>> capturedKeys = getCapturedKeys();
                if (keyEvent.getAction() == 1 && (indexOfKey = capturedKeys.indexOfKey(keyEvent.getKeyCode())) >= 0) {
                    weakReference2 = capturedKeys.valueAt(indexOfKey);
                    capturedKeys.removeAt(indexOfKey);
                }
                if (weakReference2 == null) {
                    weakReference2 = capturedKeys.get(keyEvent.getKeyCode());
                }
                if (weakReference2 == null) {
                    return false;
                }
                View view = weakReference2.get();
                if (view != null && ViewCompat.isAttachedToWindow(view)) {
                    onUnhandledKeyEvent(view, keyEvent);
                }
                return true;
            }
            return false;
        }

        public final boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent) {
            ArrayList arrayList = (ArrayList) view.getTag(R$id.tag_unhandled_key_listeners);
            if (arrayList != null) {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    if (((OnUnhandledKeyEventListenerCompat) arrayList.get(size)).onUnhandledKeyEvent(view, keyEvent)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        public final void recalcViewsWithUnhandled() {
            WeakHashMap<View, Boolean> weakHashMap = this.mViewsContainingListeners;
            if (weakHashMap != null) {
                weakHashMap.clear();
            }
            ArrayList<WeakReference<View>> arrayList = sViewsWithListeners;
            if (arrayList.isEmpty()) {
                return;
            }
            synchronized (arrayList) {
                if (this.mViewsContainingListeners == null) {
                    this.mViewsContainingListeners = new WeakHashMap<>();
                }
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    ArrayList<WeakReference<View>> arrayList2 = sViewsWithListeners;
                    View view = arrayList2.get(size).get();
                    if (view == null) {
                        arrayList2.remove(size);
                    } else {
                        this.mViewsContainingListeners.put(view, Boolean.TRUE);
                        for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
                            this.mViewsContainingListeners.put((View) parent, Boolean.TRUE);
                        }
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Api21Impl {
        public static WindowInsetsCompat getRootWindowInsets(View view) {
            return WindowInsetsCompat.Api21ReflectionHolder.getRootWindowInsets(view);
        }

        public static WindowInsetsCompat computeSystemWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, Rect rect) {
            WindowInsets windowInsets = windowInsetsCompat.toWindowInsets();
            if (windowInsets != null) {
                return WindowInsetsCompat.toWindowInsetsCompat(view.computeSystemWindowInsets(windowInsets, rect), view);
            }
            rect.setEmpty();
            return windowInsetsCompat;
        }

        public static void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
            if (Build.VERSION.SDK_INT < 30) {
                view.setTag(R$id.tag_on_apply_window_listener, onApplyWindowInsetsListener);
            }
            if (onApplyWindowInsetsListener == null) {
                view.setOnApplyWindowInsetsListener((View.OnApplyWindowInsetsListener) view.getTag(R$id.tag_window_insets_animation_callback));
            } else {
                view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: androidx.core.view.ViewCompat.Api21Impl.1
                    public WindowInsetsCompat mLastInsets = null;

                    @Override // android.view.View.OnApplyWindowInsetsListener
                    public WindowInsets onApplyWindowInsets(View view2, WindowInsets windowInsets) {
                        WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(windowInsets, view2);
                        int i = Build.VERSION.SDK_INT;
                        if (i < 30) {
                            Api21Impl.callCompatInsetAnimationCallback(windowInsets, view);
                            if (windowInsetsCompat.equals(this.mLastInsets)) {
                                return onApplyWindowInsetsListener.onApplyWindowInsets(view2, windowInsetsCompat).toWindowInsets();
                            }
                        }
                        this.mLastInsets = windowInsetsCompat;
                        WindowInsetsCompat onApplyWindowInsets = onApplyWindowInsetsListener.onApplyWindowInsets(view2, windowInsetsCompat);
                        if (i >= 30) {
                            return onApplyWindowInsets.toWindowInsets();
                        }
                        ViewCompat.requestApplyInsets(view2);
                        return onApplyWindowInsets.toWindowInsets();
                    }
                });
            }
        }

        public static void callCompatInsetAnimationCallback(WindowInsets windowInsets, View view) {
            View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = (View.OnApplyWindowInsetsListener) view.getTag(R$id.tag_window_insets_animation_callback);
            if (onApplyWindowInsetsListener != null) {
                onApplyWindowInsetsListener.onApplyWindowInsets(view, windowInsets);
            }
        }

        public static boolean dispatchNestedFling(View view, float f, float f2, boolean z) {
            return view.dispatchNestedFling(f, f2, z);
        }

        public static boolean dispatchNestedPreFling(View view, float f, float f2) {
            return view.dispatchNestedPreFling(f, f2);
        }

        public static float getZ(View view) {
            return view.getZ();
        }

        public static void setZ(View view, float f) {
            view.setZ(f);
        }

        public static void setElevation(View view, float f) {
            view.setElevation(f);
        }

        public static void setTranslationZ(View view, float f) {
            view.setTranslationZ(f);
        }

        public static float getTranslationZ(View view) {
            return view.getTranslationZ();
        }

        public static void setTransitionName(View view, String str) {
            view.setTransitionName(str);
        }

        public static boolean isImportantForAccessibility(View view) {
            return view.isImportantForAccessibility();
        }

        public static float getElevation(View view) {
            return view.getElevation();
        }

        public static String getTransitionName(View view) {
            return view.getTransitionName();
        }

        public static void setBackgroundTintList(View view, ColorStateList colorStateList) {
            view.setBackgroundTintList(colorStateList);
        }

        public static ColorStateList getBackgroundTintList(View view) {
            return view.getBackgroundTintList();
        }

        public static PorterDuff.Mode getBackgroundTintMode(View view) {
            return view.getBackgroundTintMode();
        }

        public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
            view.setBackgroundTintMode(mode);
        }

        public static void setNestedScrollingEnabled(View view, boolean z) {
            view.setNestedScrollingEnabled(z);
        }

        public static boolean isNestedScrollingEnabled(View view) {
            return view.isNestedScrollingEnabled();
        }

        public static boolean startNestedScroll(View view, int i) {
            return view.startNestedScroll(i);
        }

        public static void stopNestedScroll(View view) {
            view.stopNestedScroll();
        }

        public static boolean hasNestedScrollingParent(View view) {
            return view.hasNestedScrollingParent();
        }

        public static boolean dispatchNestedScroll(View view, int i, int i2, int i3, int i4, int[] iArr) {
            return view.dispatchNestedScroll(i, i2, i3, i4, iArr);
        }

        public static boolean dispatchNestedPreScroll(View view, int i, int i2, int[] iArr, int[] iArr2) {
            return view.dispatchNestedPreScroll(i, i2, iArr, iArr2);
        }
    }

    /* loaded from: classes.dex */
    public static class Api23Impl {
        public static WindowInsetsCompat getRootWindowInsets(View view) {
            WindowInsets rootWindowInsets = view.getRootWindowInsets();
            if (rootWindowInsets == null) {
                return null;
            }
            WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets);
            windowInsetsCompat.setRootWindowInsets(windowInsetsCompat);
            windowInsetsCompat.copyRootViewBounds(view.getRootView());
            return windowInsetsCompat;
        }

        public static void setScrollIndicators(View view, int i) {
            view.setScrollIndicators(i);
        }

        public static void setScrollIndicators(View view, int i, int i2) {
            view.setScrollIndicators(i, i2);
        }

        public static int getScrollIndicators(View view) {
            return view.getScrollIndicators();
        }
    }

    /* loaded from: classes.dex */
    public static class Api29Impl {
        public static void saveAttributeDataForStyleable(View view, Context context, int[] iArr, AttributeSet attributeSet, TypedArray typedArray, int i, int i2) {
            view.saveAttributeDataForStyleable(context, iArr, attributeSet, typedArray, i, i2);
        }

        public static View.AccessibilityDelegate getAccessibilityDelegate(View view) {
            return view.getAccessibilityDelegate();
        }

        public static void setSystemGestureExclusionRects(View view, List<Rect> list) {
            view.setSystemGestureExclusionRects(list);
        }

        public static List<Rect> getSystemGestureExclusionRects(View view) {
            return view.getSystemGestureExclusionRects();
        }
    }

    /* loaded from: classes.dex */
    public static class Api30Impl {
        public static void setStateDescription(View view, CharSequence charSequence) {
            view.setStateDescription(charSequence);
        }

        public static CharSequence getStateDescription(View view) {
            return view.getStateDescription();
        }
    }

    /* loaded from: classes.dex */
    public static class Api26Impl {
        public static void setAutofillHints(View view, String... strArr) {
            view.setAutofillHints(strArr);
        }

        public static void setTooltipText(View view, CharSequence charSequence) {
            view.setTooltipText(charSequence);
        }

        public static int getNextClusterForwardId(View view) {
            return view.getNextClusterForwardId();
        }

        public static void setNextClusterForwardId(View view, int i) {
            view.setNextClusterForwardId(i);
        }

        public static boolean isKeyboardNavigationCluster(View view) {
            return view.isKeyboardNavigationCluster();
        }

        public static void setKeyboardNavigationCluster(View view, boolean z) {
            view.setKeyboardNavigationCluster(z);
        }

        public static boolean isFocusedByDefault(View view) {
            return view.isFocusedByDefault();
        }

        public static void setFocusedByDefault(View view, boolean z) {
            view.setFocusedByDefault(z);
        }

        public static View keyboardNavigationClusterSearch(View view, View view2, int i) {
            return view.keyboardNavigationClusterSearch(view2, i);
        }

        public static void addKeyboardNavigationClusters(View view, Collection<View> collection, int i) {
            view.addKeyboardNavigationClusters(collection, i);
        }

        public static boolean restoreDefaultFocus(View view) {
            return view.restoreDefaultFocus();
        }

        public static boolean hasExplicitFocusable(View view) {
            return view.hasExplicitFocusable();
        }

        public static int getImportantForAutofill(View view) {
            return view.getImportantForAutofill();
        }

        public static void setImportantForAutofill(View view, int i) {
            view.setImportantForAutofill(i);
        }

        public static boolean isImportantForAutofill(View view) {
            return view.isImportantForAutofill();
        }
    }

    /* loaded from: classes.dex */
    public static class Api19Impl {
        public static boolean isLaidOut(View view) {
            return view.isLaidOut();
        }

        public static boolean isAttachedToWindow(View view) {
            return view.isAttachedToWindow();
        }

        public static boolean isLayoutDirectionResolved(View view) {
            return view.isLayoutDirectionResolved();
        }

        public static int getAccessibilityLiveRegion(View view) {
            return view.getAccessibilityLiveRegion();
        }

        public static void setAccessibilityLiveRegion(View view, int i) {
            view.setAccessibilityLiveRegion(i);
        }

        public static void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int i) {
            accessibilityEvent.setContentChangeTypes(i);
        }

        public static void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int i) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, i);
        }
    }

    /* loaded from: classes.dex */
    public static class Api15Impl {
        public static boolean hasOnClickListeners(View view) {
            return view.hasOnClickListeners();
        }
    }

    /* loaded from: classes.dex */
    public static class Api17Impl {
        public static Display getDisplay(View view) {
            return view.getDisplay();
        }

        public static int generateViewId() {
            return View.generateViewId();
        }

        public static int getLabelFor(View view) {
            return view.getLabelFor();
        }

        public static void setLabelFor(View view, int i) {
            view.setLabelFor(i);
        }

        public static void setLayerPaint(View view, Paint paint) {
            view.setLayerPaint(paint);
        }

        public static int getLayoutDirection(View view) {
            return view.getLayoutDirection();
        }

        public static void setLayoutDirection(View view, int i) {
            view.setLayoutDirection(i);
        }

        public static int getPaddingStart(View view) {
            return view.getPaddingStart();
        }

        public static int getPaddingEnd(View view) {
            return view.getPaddingEnd();
        }

        public static void setPaddingRelative(View view, int i, int i2, int i3, int i4) {
            view.setPaddingRelative(i, i2, i3, i4);
        }

        public static boolean isPaddingRelative(View view) {
            return view.isPaddingRelative();
        }
    }

    /* loaded from: classes.dex */
    public static class Api16Impl {
        public static boolean hasTransientState(View view) {
            return view.hasTransientState();
        }

        public static void setHasTransientState(View view, boolean z) {
            view.setHasTransientState(z);
        }

        public static void postInvalidateOnAnimation(View view) {
            view.postInvalidateOnAnimation();
        }

        public static void postInvalidateOnAnimation(View view, int i, int i2, int i3, int i4) {
            view.postInvalidateOnAnimation(i, i2, i3, i4);
        }

        public static void postOnAnimation(View view, Runnable runnable) {
            view.postOnAnimation(runnable);
        }

        public static void postOnAnimationDelayed(View view, Runnable runnable, long j) {
            view.postOnAnimationDelayed(runnable, j);
        }

        public static int getImportantForAccessibility(View view) {
            return view.getImportantForAccessibility();
        }

        public static void setImportantForAccessibility(View view, int i) {
            view.setImportantForAccessibility(i);
        }

        public static AccessibilityNodeProvider getAccessibilityNodeProvider(View view) {
            return view.getAccessibilityNodeProvider();
        }

        public static ViewParent getParentForAccessibility(View view) {
            return view.getParentForAccessibility();
        }

        public static int getMinimumWidth(View view) {
            return view.getMinimumWidth();
        }

        public static int getMinimumHeight(View view) {
            return view.getMinimumHeight();
        }

        public static int getWindowSystemUiVisibility(View view) {
            return view.getWindowSystemUiVisibility();
        }

        public static void requestFitSystemWindows(View view) {
            view.requestFitSystemWindows();
        }

        public static boolean getFitsSystemWindows(View view) {
            return view.getFitsSystemWindows();
        }

        public static boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            return view.performAccessibilityAction(i, bundle);
        }

        public static boolean hasOverlappingRendering(View view) {
            return view.hasOverlappingRendering();
        }

        public static void setBackground(View view, Drawable drawable) {
            view.setBackground(drawable);
        }

        public static void removeOnGlobalLayoutListener(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    /* loaded from: classes.dex */
    public static class Api28Impl {
        public static <T> T requireViewById(View view, int i) {
            return (T) view.requireViewById(i);
        }

        public static CharSequence getAccessibilityPaneTitle(View view) {
            return view.getAccessibilityPaneTitle();
        }

        public static void setAccessibilityPaneTitle(View view, CharSequence charSequence) {
            view.setAccessibilityPaneTitle(charSequence);
        }

        public static void setAccessibilityHeading(View view, boolean z) {
            view.setAccessibilityHeading(z);
        }

        public static boolean isAccessibilityHeading(View view) {
            return view.isAccessibilityHeading();
        }

        public static boolean isScreenReaderFocusable(View view) {
            return view.isScreenReaderFocusable();
        }

        public static void setScreenReaderFocusable(View view, boolean z) {
            view.setScreenReaderFocusable(z);
        }

        public static void addOnUnhandledKeyEventListener(View view, final OnUnhandledKeyEventListenerCompat onUnhandledKeyEventListenerCompat) {
            int i = R$id.tag_unhandled_key_listeners;
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) view.getTag(i);
            if (simpleArrayMap == null) {
                simpleArrayMap = new SimpleArrayMap();
                view.setTag(i, simpleArrayMap);
            }
            Objects.requireNonNull(onUnhandledKeyEventListenerCompat);
            View.OnUnhandledKeyEventListener onUnhandledKeyEventListener = new View.OnUnhandledKeyEventListener() { // from class: androidx.core.view.ViewCompat$Api28Impl$$ExternalSyntheticLambda0
                @Override // android.view.View.OnUnhandledKeyEventListener
                public final boolean onUnhandledKeyEvent(View view2, KeyEvent keyEvent) {
                    return ViewCompat.OnUnhandledKeyEventListenerCompat.this.onUnhandledKeyEvent(view2, keyEvent);
                }
            };
            simpleArrayMap.put(onUnhandledKeyEventListenerCompat, onUnhandledKeyEventListener);
            view.addOnUnhandledKeyEventListener(onUnhandledKeyEventListener);
        }

        public static void removeOnUnhandledKeyEventListener(View view, OnUnhandledKeyEventListenerCompat onUnhandledKeyEventListenerCompat) {
            View.OnUnhandledKeyEventListener onUnhandledKeyEventListener;
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) view.getTag(R$id.tag_unhandled_key_listeners);
            if (simpleArrayMap == null || (onUnhandledKeyEventListener = (View.OnUnhandledKeyEventListener) simpleArrayMap.get(onUnhandledKeyEventListenerCompat)) == null) {
                return;
            }
            view.removeOnUnhandledKeyEventListener(onUnhandledKeyEventListener);
        }
    }

    /* loaded from: classes.dex */
    public static class Api20Impl {
        public static void requestApplyInsets(View view) {
            view.requestApplyInsets();
        }

        public static WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            return view.onApplyWindowInsets(windowInsets);
        }

        public static WindowInsets dispatchApplyWindowInsets(View view, WindowInsets windowInsets) {
            return view.dispatchApplyWindowInsets(windowInsets);
        }
    }
}
