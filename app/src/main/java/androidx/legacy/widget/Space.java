package androidx.legacy.widget;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.View;

@Deprecated
/* loaded from: classes.dex */
public class Space extends View {
    @Override // android.view.View
    @SuppressLint({"MissingSuperCall"})
    @Deprecated
    public void draw(Canvas canvas) {
    }

    public static int getDefaultSize2(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        if (mode != Integer.MIN_VALUE) {
            return mode != 1073741824 ? i : size;
        }
        return Math.min(i, size);
    }

    @Override // android.view.View
    @Deprecated
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(getDefaultSize2(getSuggestedMinimumWidth(), i), getDefaultSize2(getSuggestedMinimumHeight(), i2));
    }
}
