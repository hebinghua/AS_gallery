package androidx.core.view;

import android.os.Build;
import android.view.DisplayCutout;
import androidx.core.util.ObjectsCompat;

/* loaded from: classes.dex */
public final class DisplayCutoutCompat {
    public final Object mDisplayCutout;

    public DisplayCutoutCompat(Object obj) {
        this.mDisplayCutout = obj;
    }

    public int getSafeInsetTop() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout) this.mDisplayCutout).getSafeInsetTop();
        }
        return 0;
    }

    public int getSafeInsetBottom() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout) this.mDisplayCutout).getSafeInsetBottom();
        }
        return 0;
    }

    public int getSafeInsetLeft() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout) this.mDisplayCutout).getSafeInsetLeft();
        }
        return 0;
    }

    public int getSafeInsetRight() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout) this.mDisplayCutout).getSafeInsetRight();
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && DisplayCutoutCompat.class == obj.getClass()) {
            return ObjectsCompat.equals(this.mDisplayCutout, ((DisplayCutoutCompat) obj).mDisplayCutout);
        }
        return false;
    }

    public int hashCode() {
        Object obj = this.mDisplayCutout;
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public String toString() {
        return "DisplayCutoutCompat{" + this.mDisplayCutout + "}";
    }

    public static DisplayCutoutCompat wrap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new DisplayCutoutCompat(obj);
    }
}
