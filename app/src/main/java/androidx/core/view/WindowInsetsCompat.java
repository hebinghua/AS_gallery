package androidx.core.view;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import androidx.core.graphics.Insets;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/* loaded from: classes.dex */
public class WindowInsetsCompat {
    public static final WindowInsetsCompat CONSUMED;
    public final Impl mImpl;

    static {
        if (Build.VERSION.SDK_INT >= 30) {
            CONSUMED = Impl30.CONSUMED;
        } else {
            CONSUMED = Impl.CONSUMED;
        }
    }

    public WindowInsetsCompat(WindowInsets windowInsets) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 30) {
            this.mImpl = new Impl30(this, windowInsets);
        } else if (i >= 29) {
            this.mImpl = new Impl29(this, windowInsets);
        } else if (i >= 28) {
            this.mImpl = new Impl28(this, windowInsets);
        } else if (i >= 21) {
            this.mImpl = new Impl21(this, windowInsets);
        } else if (i >= 20) {
            this.mImpl = new Impl20(this, windowInsets);
        } else {
            this.mImpl = new Impl(this);
        }
    }

    public WindowInsetsCompat(WindowInsetsCompat windowInsetsCompat) {
        if (windowInsetsCompat != null) {
            Impl impl = windowInsetsCompat.mImpl;
            int i = Build.VERSION.SDK_INT;
            if (i >= 30 && (impl instanceof Impl30)) {
                this.mImpl = new Impl30(this, (Impl30) impl);
            } else if (i >= 29 && (impl instanceof Impl29)) {
                this.mImpl = new Impl29(this, (Impl29) impl);
            } else if (i >= 28 && (impl instanceof Impl28)) {
                this.mImpl = new Impl28(this, (Impl28) impl);
            } else if (i >= 21 && (impl instanceof Impl21)) {
                this.mImpl = new Impl21(this, (Impl21) impl);
            } else if (i >= 20 && (impl instanceof Impl20)) {
                this.mImpl = new Impl20(this, (Impl20) impl);
            } else {
                this.mImpl = new Impl(this);
            }
            impl.copyWindowDataInto(this);
            return;
        }
        this.mImpl = new Impl(this);
    }

    public static WindowInsetsCompat toWindowInsetsCompat(WindowInsets windowInsets) {
        return toWindowInsetsCompat(windowInsets, null);
    }

    public static WindowInsetsCompat toWindowInsetsCompat(WindowInsets windowInsets, View view) {
        WindowInsetsCompat windowInsetsCompat = new WindowInsetsCompat((WindowInsets) Preconditions.checkNotNull(windowInsets));
        if (view != null && ViewCompat.isAttachedToWindow(view)) {
            windowInsetsCompat.setRootWindowInsets(ViewCompat.getRootWindowInsets(view));
            windowInsetsCompat.copyRootViewBounds(view.getRootView());
        }
        return windowInsetsCompat;
    }

    @Deprecated
    public int getSystemWindowInsetLeft() {
        return this.mImpl.getSystemWindowInsets().left;
    }

    @Deprecated
    public int getSystemWindowInsetTop() {
        return this.mImpl.getSystemWindowInsets().top;
    }

    @Deprecated
    public int getSystemWindowInsetRight() {
        return this.mImpl.getSystemWindowInsets().right;
    }

    @Deprecated
    public int getSystemWindowInsetBottom() {
        return this.mImpl.getSystemWindowInsets().bottom;
    }

    public boolean isConsumed() {
        return this.mImpl.isConsumed();
    }

    @Deprecated
    public WindowInsetsCompat consumeSystemWindowInsets() {
        return this.mImpl.consumeSystemWindowInsets();
    }

    @Deprecated
    public WindowInsetsCompat replaceSystemWindowInsets(int i, int i2, int i3, int i4) {
        return new Builder(this).setSystemWindowInsets(Insets.of(i, i2, i3, i4)).build();
    }

    @Deprecated
    public WindowInsetsCompat consumeStableInsets() {
        return this.mImpl.consumeStableInsets();
    }

    public DisplayCutoutCompat getDisplayCutout() {
        return this.mImpl.getDisplayCutout();
    }

    @Deprecated
    public WindowInsetsCompat consumeDisplayCutout() {
        return this.mImpl.consumeDisplayCutout();
    }

    @Deprecated
    public Insets getStableInsets() {
        return this.mImpl.getStableInsets();
    }

    public WindowInsetsCompat inset(int i, int i2, int i3, int i4) {
        return this.mImpl.inset(i, i2, i3, i4);
    }

    public Insets getInsets(int i) {
        return this.mImpl.getInsets(i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WindowInsetsCompat) {
            return ObjectsCompat.equals(this.mImpl, ((WindowInsetsCompat) obj).mImpl);
        }
        return false;
    }

    public int hashCode() {
        Impl impl = this.mImpl;
        if (impl == null) {
            return 0;
        }
        return impl.hashCode();
    }

    public WindowInsets toWindowInsets() {
        Impl impl = this.mImpl;
        if (impl instanceof Impl20) {
            return ((Impl20) impl).mPlatformInsets;
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static class Impl {
        public static final WindowInsetsCompat CONSUMED = new Builder().build().consumeDisplayCutout().consumeStableInsets().consumeSystemWindowInsets();
        public final WindowInsetsCompat mHost;

        public void copyRootViewBounds(View view) {
        }

        public void copyWindowDataInto(WindowInsetsCompat windowInsetsCompat) {
        }

        public DisplayCutoutCompat getDisplayCutout() {
            return null;
        }

        public boolean isConsumed() {
            return false;
        }

        public boolean isRound() {
            return false;
        }

        public void setOverriddenInsets(Insets[] insetsArr) {
        }

        public void setRootViewData(Insets insets) {
        }

        public void setRootWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        }

        public void setStableInsets(Insets insets) {
        }

        public Impl(WindowInsetsCompat windowInsetsCompat) {
            this.mHost = windowInsetsCompat;
        }

        public WindowInsetsCompat consumeSystemWindowInsets() {
            return this.mHost;
        }

        public WindowInsetsCompat consumeStableInsets() {
            return this.mHost;
        }

        public WindowInsetsCompat consumeDisplayCutout() {
            return this.mHost;
        }

        public Insets getSystemWindowInsets() {
            return Insets.NONE;
        }

        public Insets getStableInsets() {
            return Insets.NONE;
        }

        public Insets getSystemGestureInsets() {
            return getSystemWindowInsets();
        }

        public Insets getMandatorySystemGestureInsets() {
            return getSystemWindowInsets();
        }

        public Insets getTappableElementInsets() {
            return getSystemWindowInsets();
        }

        public WindowInsetsCompat inset(int i, int i2, int i3, int i4) {
            return CONSUMED;
        }

        public Insets getInsets(int i) {
            return Insets.NONE;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Impl)) {
                return false;
            }
            Impl impl = (Impl) obj;
            return isRound() == impl.isRound() && isConsumed() == impl.isConsumed() && ObjectsCompat.equals(getSystemWindowInsets(), impl.getSystemWindowInsets()) && ObjectsCompat.equals(getStableInsets(), impl.getStableInsets()) && ObjectsCompat.equals(getDisplayCutout(), impl.getDisplayCutout());
        }

        public int hashCode() {
            return ObjectsCompat.hash(Boolean.valueOf(isRound()), Boolean.valueOf(isConsumed()), getSystemWindowInsets(), getStableInsets(), getDisplayCutout());
        }
    }

    /* loaded from: classes.dex */
    public static class Impl20 extends Impl {
        public static Class<?> sAttachInfoClass = null;
        public static Field sAttachInfoField = null;
        public static Method sGetViewRootImplMethod = null;
        public static Field sVisibleInsetsField = null;
        public static boolean sVisibleRectReflectionFetched = false;
        public Insets[] mOverriddenInsets;
        public final WindowInsets mPlatformInsets;
        public Insets mRootViewVisibleInsets;
        public WindowInsetsCompat mRootWindowInsets;
        public Insets mSystemWindowInsets;

        public Impl20(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat);
            this.mSystemWindowInsets = null;
            this.mPlatformInsets = windowInsets;
        }

        public Impl20(WindowInsetsCompat windowInsetsCompat, Impl20 impl20) {
            this(windowInsetsCompat, new WindowInsets(impl20.mPlatformInsets));
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public boolean isRound() {
            return this.mPlatformInsets.isRound();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsets(int i) {
            return getInsets(i, false);
        }

        @SuppressLint({"WrongConstant"})
        private Insets getInsets(int i, boolean z) {
            Insets insets = Insets.NONE;
            for (int i2 = 1; i2 <= 256; i2 <<= 1) {
                if ((i & i2) != 0) {
                    insets = Insets.max(insets, getInsetsForType(i2, z));
                }
            }
            return insets;
        }

        public Insets getInsetsForType(int i, boolean z) {
            int i2;
            DisplayCutoutCompat displayCutout;
            if (i == 1) {
                if (z) {
                    return Insets.of(0, Math.max(getRootStableInsets().top, getSystemWindowInsets().top), 0, 0);
                }
                return Insets.of(0, getSystemWindowInsets().top, 0, 0);
            }
            Insets insets = null;
            if (i == 2) {
                if (z) {
                    Insets rootStableInsets = getRootStableInsets();
                    Insets stableInsets = getStableInsets();
                    return Insets.of(Math.max(rootStableInsets.left, stableInsets.left), 0, Math.max(rootStableInsets.right, stableInsets.right), Math.max(rootStableInsets.bottom, stableInsets.bottom));
                }
                Insets systemWindowInsets = getSystemWindowInsets();
                WindowInsetsCompat windowInsetsCompat = this.mRootWindowInsets;
                if (windowInsetsCompat != null) {
                    insets = windowInsetsCompat.getStableInsets();
                }
                int i3 = systemWindowInsets.bottom;
                if (insets != null) {
                    i3 = Math.min(i3, insets.bottom);
                }
                return Insets.of(systemWindowInsets.left, 0, systemWindowInsets.right, i3);
            } else if (i != 8) {
                if (i == 16) {
                    return getSystemGestureInsets();
                }
                if (i == 32) {
                    return getMandatorySystemGestureInsets();
                }
                if (i == 64) {
                    return getTappableElementInsets();
                }
                if (i == 128) {
                    WindowInsetsCompat windowInsetsCompat2 = this.mRootWindowInsets;
                    if (windowInsetsCompat2 != null) {
                        displayCutout = windowInsetsCompat2.getDisplayCutout();
                    } else {
                        displayCutout = getDisplayCutout();
                    }
                    if (displayCutout != null) {
                        return Insets.of(displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
                    }
                    return Insets.NONE;
                }
                return Insets.NONE;
            } else {
                Insets[] insetsArr = this.mOverriddenInsets;
                if (insetsArr != null) {
                    insets = insetsArr[Type.indexOf(8)];
                }
                if (insets != null) {
                    return insets;
                }
                Insets systemWindowInsets2 = getSystemWindowInsets();
                Insets rootStableInsets2 = getRootStableInsets();
                int i4 = systemWindowInsets2.bottom;
                if (i4 > rootStableInsets2.bottom) {
                    return Insets.of(0, 0, 0, i4);
                }
                Insets insets2 = this.mRootViewVisibleInsets;
                if (insets2 != null && !insets2.equals(Insets.NONE) && (i2 = this.mRootViewVisibleInsets.bottom) > rootStableInsets2.bottom) {
                    return Insets.of(0, 0, 0, i2);
                }
                return Insets.NONE;
            }
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public final Insets getSystemWindowInsets() {
            if (this.mSystemWindowInsets == null) {
                this.mSystemWindowInsets = Insets.of(this.mPlatformInsets.getSystemWindowInsetLeft(), this.mPlatformInsets.getSystemWindowInsetTop(), this.mPlatformInsets.getSystemWindowInsetRight(), this.mPlatformInsets.getSystemWindowInsetBottom());
            }
            return this.mSystemWindowInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public WindowInsetsCompat inset(int i, int i2, int i3, int i4) {
            Builder builder = new Builder(WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets));
            builder.setSystemWindowInsets(WindowInsetsCompat.insetInsets(getSystemWindowInsets(), i, i2, i3, i4));
            builder.setStableInsets(WindowInsetsCompat.insetInsets(getStableInsets(), i, i2, i3, i4));
            return builder.build();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void copyWindowDataInto(WindowInsetsCompat windowInsetsCompat) {
            windowInsetsCompat.setRootWindowInsets(this.mRootWindowInsets);
            windowInsetsCompat.setRootViewData(this.mRootViewVisibleInsets);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void setRootWindowInsets(WindowInsetsCompat windowInsetsCompat) {
            this.mRootWindowInsets = windowInsetsCompat;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void setRootViewData(Insets insets) {
            this.mRootViewVisibleInsets = insets;
        }

        private Insets getRootStableInsets() {
            WindowInsetsCompat windowInsetsCompat = this.mRootWindowInsets;
            if (windowInsetsCompat != null) {
                return windowInsetsCompat.getStableInsets();
            }
            return Insets.NONE;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void copyRootViewBounds(View view) {
            Insets visibleInsets = getVisibleInsets(view);
            if (visibleInsets == null) {
                visibleInsets = Insets.NONE;
            }
            setRootViewData(visibleInsets);
        }

        private Insets getVisibleInsets(View view) {
            if (Build.VERSION.SDK_INT >= 30) {
                throw new UnsupportedOperationException("getVisibleInsets() should not be called on API >= 30. Use WindowInsets.isVisible() instead.");
            }
            if (!sVisibleRectReflectionFetched) {
                loadReflectionField();
            }
            Method method = sGetViewRootImplMethod;
            if (method != null && sAttachInfoClass != null && sVisibleInsetsField != null) {
                try {
                    Object invoke = method.invoke(view, new Object[0]);
                    if (invoke == null) {
                        Log.w("WindowInsetsCompat", "Failed to get visible insets. getViewRootImpl() returned null from the provided view. This means that the view is either not attached or the method has been overridden", new NullPointerException());
                        return null;
                    }
                    Rect rect = (Rect) sVisibleInsetsField.get(sAttachInfoField.get(invoke));
                    if (rect == null) {
                        return null;
                    }
                    return Insets.of(rect);
                } catch (ReflectiveOperationException e) {
                    Log.e("WindowInsetsCompat", "Failed to get visible insets. (Reflection error). " + e.getMessage(), e);
                }
            }
            return null;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void setOverriddenInsets(Insets[] insetsArr) {
            this.mOverriddenInsets = insetsArr;
        }

        @SuppressLint({"PrivateApi"})
        private static void loadReflectionField() {
            try {
                sGetViewRootImplMethod = View.class.getDeclaredMethod("getViewRootImpl", new Class[0]);
                Class<?> cls = Class.forName("android.view.View$AttachInfo");
                sAttachInfoClass = cls;
                sVisibleInsetsField = cls.getDeclaredField("mVisibleInsets");
                sAttachInfoField = Class.forName("android.view.ViewRootImpl").getDeclaredField("mAttachInfo");
                sVisibleInsetsField.setAccessible(true);
                sAttachInfoField.setAccessible(true);
            } catch (ReflectiveOperationException e) {
                Log.e("WindowInsetsCompat", "Failed to get visible insets. (Reflection error). " + e.getMessage(), e);
            }
            sVisibleRectReflectionFetched = true;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            return Objects.equals(this.mRootViewVisibleInsets, ((Impl20) obj).mRootViewVisibleInsets);
        }
    }

    /* loaded from: classes.dex */
    public static class Impl21 extends Impl20 {
        public Insets mStableInsets;

        public Impl21(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
            this.mStableInsets = null;
        }

        public Impl21(WindowInsetsCompat windowInsetsCompat, Impl21 impl21) {
            super(windowInsetsCompat, impl21);
            this.mStableInsets = null;
            this.mStableInsets = impl21.mStableInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public boolean isConsumed() {
            return this.mPlatformInsets.isConsumed();
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public WindowInsetsCompat consumeStableInsets() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeStableInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public WindowInsetsCompat consumeSystemWindowInsets() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeSystemWindowInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public final Insets getStableInsets() {
            if (this.mStableInsets == null) {
                this.mStableInsets = Insets.of(this.mPlatformInsets.getStableInsetLeft(), this.mPlatformInsets.getStableInsetTop(), this.mPlatformInsets.getStableInsetRight(), this.mPlatformInsets.getStableInsetBottom());
            }
            return this.mStableInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public void setStableInsets(Insets insets) {
            this.mStableInsets = insets;
        }
    }

    /* loaded from: classes.dex */
    public static class Impl28 extends Impl21 {
        public Impl28(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        public Impl28(WindowInsetsCompat windowInsetsCompat, Impl28 impl28) {
            super(windowInsetsCompat, impl28);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public DisplayCutoutCompat getDisplayCutout() {
            return DisplayCutoutCompat.wrap(this.mPlatformInsets.getDisplayCutout());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public WindowInsetsCompat consumeDisplayCutout() {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeDisplayCutout());
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Impl28)) {
                return false;
            }
            Impl28 impl28 = (Impl28) obj;
            return Objects.equals(this.mPlatformInsets, impl28.mPlatformInsets) && Objects.equals(this.mRootViewVisibleInsets, impl28.mRootViewVisibleInsets);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public int hashCode() {
            return this.mPlatformInsets.hashCode();
        }
    }

    /* loaded from: classes.dex */
    public static class Impl29 extends Impl28 {
        public Insets mMandatorySystemGestureInsets;
        public Insets mSystemGestureInsets;
        public Insets mTappableElementInsets;

        @Override // androidx.core.view.WindowInsetsCompat.Impl21, androidx.core.view.WindowInsetsCompat.Impl
        public void setStableInsets(Insets insets) {
        }

        public Impl29(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
            this.mSystemGestureInsets = null;
            this.mMandatorySystemGestureInsets = null;
            this.mTappableElementInsets = null;
        }

        public Impl29(WindowInsetsCompat windowInsetsCompat, Impl29 impl29) {
            super(windowInsetsCompat, impl29);
            this.mSystemGestureInsets = null;
            this.mMandatorySystemGestureInsets = null;
            this.mTappableElementInsets = null;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public Insets getSystemGestureInsets() {
            if (this.mSystemGestureInsets == null) {
                this.mSystemGestureInsets = Insets.toCompatInsets(this.mPlatformInsets.getSystemGestureInsets());
            }
            return this.mSystemGestureInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public Insets getMandatorySystemGestureInsets() {
            if (this.mMandatorySystemGestureInsets == null) {
                this.mMandatorySystemGestureInsets = Insets.toCompatInsets(this.mPlatformInsets.getMandatorySystemGestureInsets());
            }
            return this.mMandatorySystemGestureInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl
        public Insets getTappableElementInsets() {
            if (this.mTappableElementInsets == null) {
                this.mTappableElementInsets = Insets.toCompatInsets(this.mPlatformInsets.getTappableElementInsets());
            }
            return this.mTappableElementInsets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public WindowInsetsCompat inset(int i, int i2, int i3, int i4) {
            return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.inset(i, i2, i3, i4));
        }
    }

    public static Insets insetInsets(Insets insets, int i, int i2, int i3, int i4) {
        int max = Math.max(0, insets.left - i);
        int max2 = Math.max(0, insets.top - i2);
        int max3 = Math.max(0, insets.right - i3);
        int max4 = Math.max(0, insets.bottom - i4);
        return (max == i && max2 == i2 && max3 == i3 && max4 == i4) ? insets : Insets.of(max, max2, max3, max4);
    }

    /* loaded from: classes.dex */
    public static class Impl30 extends Impl29 {
        public static final WindowInsetsCompat CONSUMED = WindowInsetsCompat.toWindowInsetsCompat(WindowInsets.CONSUMED);

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public final void copyRootViewBounds(View view) {
        }

        public Impl30(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        public Impl30(WindowInsetsCompat windowInsetsCompat, Impl30 impl30) {
            super(windowInsetsCompat, impl30);
        }

        @Override // androidx.core.view.WindowInsetsCompat.Impl20, androidx.core.view.WindowInsetsCompat.Impl
        public Insets getInsets(int i) {
            return Insets.toCompatInsets(this.mPlatformInsets.getInsets(TypeImpl30.toPlatformType(i)));
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        public final BuilderImpl mImpl;

        public Builder() {
            int i = Build.VERSION.SDK_INT;
            if (i >= 30) {
                this.mImpl = new BuilderImpl30();
            } else if (i >= 29) {
                this.mImpl = new BuilderImpl29();
            } else if (i >= 20) {
                this.mImpl = new BuilderImpl20();
            } else {
                this.mImpl = new BuilderImpl();
            }
        }

        public Builder(WindowInsetsCompat windowInsetsCompat) {
            int i = Build.VERSION.SDK_INT;
            if (i >= 30) {
                this.mImpl = new BuilderImpl30(windowInsetsCompat);
            } else if (i >= 29) {
                this.mImpl = new BuilderImpl29(windowInsetsCompat);
            } else if (i >= 20) {
                this.mImpl = new BuilderImpl20(windowInsetsCompat);
            } else {
                this.mImpl = new BuilderImpl(windowInsetsCompat);
            }
        }

        @Deprecated
        public Builder setSystemWindowInsets(Insets insets) {
            this.mImpl.setSystemWindowInsets(insets);
            return this;
        }

        @Deprecated
        public Builder setStableInsets(Insets insets) {
            this.mImpl.setStableInsets(insets);
            return this;
        }

        public WindowInsetsCompat build() {
            return this.mImpl.build();
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderImpl {
        public final WindowInsetsCompat mInsets;
        public Insets[] mInsetsTypeMask;

        public void setMandatorySystemGestureInsets(Insets insets) {
        }

        public void setStableInsets(Insets insets) {
        }

        public void setSystemGestureInsets(Insets insets) {
        }

        public void setSystemWindowInsets(Insets insets) {
        }

        public void setTappableElementInsets(Insets insets) {
        }

        public BuilderImpl() {
            this(new WindowInsetsCompat((WindowInsetsCompat) null));
        }

        public BuilderImpl(WindowInsetsCompat windowInsetsCompat) {
            this.mInsets = windowInsetsCompat;
        }

        public final void applyInsetTypes() {
            Insets[] insetsArr = this.mInsetsTypeMask;
            if (insetsArr != null) {
                Insets insets = insetsArr[Type.indexOf(1)];
                Insets insets2 = this.mInsetsTypeMask[Type.indexOf(2)];
                if (insets2 == null) {
                    insets2 = this.mInsets.getInsets(2);
                }
                if (insets == null) {
                    insets = this.mInsets.getInsets(1);
                }
                setSystemWindowInsets(Insets.max(insets, insets2));
                Insets insets3 = this.mInsetsTypeMask[Type.indexOf(16)];
                if (insets3 != null) {
                    setSystemGestureInsets(insets3);
                }
                Insets insets4 = this.mInsetsTypeMask[Type.indexOf(32)];
                if (insets4 != null) {
                    setMandatorySystemGestureInsets(insets4);
                }
                Insets insets5 = this.mInsetsTypeMask[Type.indexOf(64)];
                if (insets5 == null) {
                    return;
                }
                setTappableElementInsets(insets5);
            }
        }

        public WindowInsetsCompat build() {
            applyInsetTypes();
            return this.mInsets;
        }
    }

    public void setOverriddenInsets(Insets[] insetsArr) {
        this.mImpl.setOverriddenInsets(insetsArr);
    }

    /* loaded from: classes.dex */
    public static class BuilderImpl20 extends BuilderImpl {
        public static Constructor<WindowInsets> sConstructor = null;
        public static boolean sConstructorFetched = false;
        public static Field sConsumedField = null;
        public static boolean sConsumedFieldFetched = false;
        public WindowInsets mPlatformInsets;
        public Insets mStableInsets;

        public BuilderImpl20() {
            this.mPlatformInsets = createWindowInsetsInstance();
        }

        public BuilderImpl20(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
            this.mPlatformInsets = windowInsetsCompat.toWindowInsets();
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public void setSystemWindowInsets(Insets insets) {
            WindowInsets windowInsets = this.mPlatformInsets;
            if (windowInsets != null) {
                this.mPlatformInsets = windowInsets.replaceSystemWindowInsets(insets.left, insets.top, insets.right, insets.bottom);
            }
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public void setStableInsets(Insets insets) {
            this.mStableInsets = insets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public WindowInsetsCompat build() {
            applyInsetTypes();
            WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets);
            windowInsetsCompat.setOverriddenInsets(this.mInsetsTypeMask);
            windowInsetsCompat.setStableInsets(this.mStableInsets);
            return windowInsetsCompat;
        }

        private static WindowInsets createWindowInsetsInstance() {
            if (!sConsumedFieldFetched) {
                try {
                    sConsumedField = WindowInsets.class.getDeclaredField("CONSUMED");
                } catch (ReflectiveOperationException e) {
                    Log.i("WindowInsetsCompat", "Could not retrieve WindowInsets.CONSUMED field", e);
                }
                sConsumedFieldFetched = true;
            }
            Field field = sConsumedField;
            if (field != null) {
                try {
                    WindowInsets windowInsets = (WindowInsets) field.get(null);
                    if (windowInsets != null) {
                        return new WindowInsets(windowInsets);
                    }
                } catch (ReflectiveOperationException e2) {
                    Log.i("WindowInsetsCompat", "Could not get value from WindowInsets.CONSUMED field", e2);
                }
            }
            if (!sConstructorFetched) {
                try {
                    sConstructor = WindowInsets.class.getConstructor(Rect.class);
                } catch (ReflectiveOperationException e3) {
                    Log.i("WindowInsetsCompat", "Could not retrieve WindowInsets(Rect) constructor", e3);
                }
                sConstructorFetched = true;
            }
            Constructor<WindowInsets> constructor = sConstructor;
            if (constructor != null) {
                try {
                    return constructor.newInstance(new Rect());
                } catch (ReflectiveOperationException e4) {
                    Log.i("WindowInsetsCompat", "Could not invoke WindowInsets(Rect) constructor", e4);
                }
            }
            return null;
        }
    }

    public void setStableInsets(Insets insets) {
        this.mImpl.setStableInsets(insets);
    }

    /* loaded from: classes.dex */
    public static class BuilderImpl29 extends BuilderImpl {
        public final WindowInsets.Builder mPlatBuilder;

        public BuilderImpl29() {
            this.mPlatBuilder = new WindowInsets.Builder();
        }

        public BuilderImpl29(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
            WindowInsets.Builder builder;
            WindowInsets windowInsets = windowInsetsCompat.toWindowInsets();
            if (windowInsets != null) {
                builder = new WindowInsets.Builder(windowInsets);
            } else {
                builder = new WindowInsets.Builder();
            }
            this.mPlatBuilder = builder;
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public void setSystemWindowInsets(Insets insets) {
            this.mPlatBuilder.setSystemWindowInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public void setSystemGestureInsets(Insets insets) {
            this.mPlatBuilder.setSystemGestureInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public void setMandatorySystemGestureInsets(Insets insets) {
            this.mPlatBuilder.setMandatorySystemGestureInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public void setTappableElementInsets(Insets insets) {
            this.mPlatBuilder.setTappableElementInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public void setStableInsets(Insets insets) {
            this.mPlatBuilder.setStableInsets(insets.toPlatformInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.BuilderImpl
        public WindowInsetsCompat build() {
            applyInsetTypes();
            WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(this.mPlatBuilder.build());
            windowInsetsCompat.setOverriddenInsets(this.mInsetsTypeMask);
            return windowInsetsCompat;
        }
    }

    /* loaded from: classes.dex */
    public static class BuilderImpl30 extends BuilderImpl29 {
        public BuilderImpl30() {
        }

        public BuilderImpl30(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
        }
    }

    /* loaded from: classes.dex */
    public static final class Type {
        public static int indexOf(int i) {
            if (i != 1) {
                if (i == 2) {
                    return 1;
                }
                if (i == 4) {
                    return 2;
                }
                if (i == 8) {
                    return 3;
                }
                if (i == 16) {
                    return 4;
                }
                if (i == 32) {
                    return 5;
                }
                if (i == 64) {
                    return 6;
                }
                if (i == 128) {
                    return 7;
                }
                if (i == 256) {
                    return 8;
                }
                throw new IllegalArgumentException("type needs to be >= FIRST and <= LAST, type=" + i);
            }
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public static final class TypeImpl30 {
        public static int toPlatformType(int i) {
            int statusBars;
            int i2 = 0;
            for (int i3 = 1; i3 <= 256; i3 <<= 1) {
                if ((i & i3) != 0) {
                    if (i3 == 1) {
                        statusBars = WindowInsets.Type.statusBars();
                    } else if (i3 == 2) {
                        statusBars = WindowInsets.Type.navigationBars();
                    } else if (i3 == 4) {
                        statusBars = WindowInsets.Type.captionBar();
                    } else if (i3 == 8) {
                        statusBars = WindowInsets.Type.ime();
                    } else if (i3 == 16) {
                        statusBars = WindowInsets.Type.systemGestures();
                    } else if (i3 == 32) {
                        statusBars = WindowInsets.Type.mandatorySystemGestures();
                    } else if (i3 == 64) {
                        statusBars = WindowInsets.Type.tappableElement();
                    } else if (i3 == 128) {
                        statusBars = WindowInsets.Type.displayCutout();
                    }
                    i2 |= statusBars;
                }
            }
            return i2;
        }
    }

    public void setRootWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        this.mImpl.setRootWindowInsets(windowInsetsCompat);
    }

    public void setRootViewData(Insets insets) {
        this.mImpl.setRootViewData(insets);
    }

    public void copyRootViewBounds(View view) {
        this.mImpl.copyRootViewBounds(view);
    }

    @SuppressLint({"SoonBlockedPrivateApi"})
    /* loaded from: classes.dex */
    public static class Api21ReflectionHolder {
        public static Field sContentInsets;
        public static boolean sReflectionSucceeded;
        public static Field sStableInsets;
        public static Field sViewAttachInfoField;

        static {
            try {
                Field declaredField = View.class.getDeclaredField("mAttachInfo");
                sViewAttachInfoField = declaredField;
                declaredField.setAccessible(true);
                Class<?> cls = Class.forName("android.view.View$AttachInfo");
                Field declaredField2 = cls.getDeclaredField("mStableInsets");
                sStableInsets = declaredField2;
                declaredField2.setAccessible(true);
                Field declaredField3 = cls.getDeclaredField("mContentInsets");
                sContentInsets = declaredField3;
                declaredField3.setAccessible(true);
                sReflectionSucceeded = true;
            } catch (ReflectiveOperationException e) {
                Log.w("WindowInsetsCompat", "Failed to get visible insets from AttachInfo " + e.getMessage(), e);
            }
        }

        public static WindowInsetsCompat getRootWindowInsets(View view) {
            if (sReflectionSucceeded && view.isAttachedToWindow()) {
                try {
                    Object obj = sViewAttachInfoField.get(view.getRootView());
                    if (obj != null) {
                        Rect rect = (Rect) sStableInsets.get(obj);
                        Rect rect2 = (Rect) sContentInsets.get(obj);
                        if (rect != null && rect2 != null) {
                            WindowInsetsCompat build = new Builder().setStableInsets(Insets.of(rect)).setSystemWindowInsets(Insets.of(rect2)).build();
                            build.setRootWindowInsets(build);
                            build.copyRootViewBounds(view.getRootView());
                            return build;
                        }
                    }
                } catch (IllegalAccessException e) {
                    Log.w("WindowInsetsCompat", "Failed to get insets from AttachInfo. " + e.getMessage(), e);
                }
            }
            return null;
        }
    }
}
