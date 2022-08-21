package miuix.internal.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/* loaded from: classes3.dex */
public final class DisplayHelper {
    public float mDensity;
    public int mDensityDpi;
    public DisplayMetrics mDisplayMetrics;
    public int mHeightDps;
    public int mHeightPixels;
    public int mWidthDps;
    public int mWidthPixels;

    public DisplayHelper(Context context) {
        getAndroidScreenProperty(context);
    }

    public final void getAndroidScreenProperty(Context context) {
        this.mDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(this.mDisplayMetrics);
        DisplayMetrics displayMetrics = this.mDisplayMetrics;
        int i = displayMetrics.widthPixels;
        this.mWidthPixels = i;
        int i2 = displayMetrics.heightPixels;
        this.mHeightPixels = i2;
        float f = displayMetrics.density;
        this.mDensity = f;
        this.mDensityDpi = displayMetrics.densityDpi;
        this.mWidthDps = (int) (i / f);
        this.mHeightDps = (int) (i2 / f);
    }

    public int getWidthPixels() {
        return this.mWidthPixels;
    }

    public int getHeightPixels() {
        return this.mHeightPixels;
    }

    public float getDensity() {
        return this.mDensity;
    }
}
