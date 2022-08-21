package miuix.internal.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;

/* loaded from: classes3.dex */
public class AttributeResolver {
    public static final TypedValue TYPED_VALUE = new TypedValue();
    public static final ThreadLocal<TypedValue> TYPED_VALUE_THREAD_LOCAL = new ThreadLocal<>();

    public static TypedValue getTypedValue(Context context) {
        if (context.getMainLooper().getThread() == Thread.currentThread()) {
            return TYPED_VALUE;
        }
        ThreadLocal<TypedValue> threadLocal = TYPED_VALUE_THREAD_LOCAL;
        TypedValue typedValue = threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }

    public static int resolve(Context context, int i) {
        TypedValue typedValue = getTypedValue(context);
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue.resourceId;
        }
        return -1;
    }

    public static Drawable resolveDrawable(Context context, int i) {
        TypedValue typedValue = getTypedValue(context);
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            if (typedValue.resourceId > 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    return context.getResources().getDrawable(typedValue.resourceId, context.getTheme());
                }
                return context.getResources().getDrawable(typedValue.resourceId);
            }
            int i2 = typedValue.type;
            if (i2 >= 28 && i2 <= 31) {
                return new ColorDrawable(typedValue.data);
            }
            return null;
        }
        return null;
    }

    public static int resolveColor(Context context, int i) {
        Integer innerResolveColor = innerResolveColor(context, i);
        if (innerResolveColor != null) {
            return innerResolveColor.intValue();
        }
        return context.getResources().getColor(-1);
    }

    public static Integer innerResolveColor(Context context, int i) {
        TypedValue typedValue = getTypedValue(context);
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            if (typedValue.resourceId > 0) {
                return Integer.valueOf(context.getResources().getColor(typedValue.resourceId));
            }
            int i2 = typedValue.type;
            if (i2 >= 28 && i2 <= 31) {
                return Integer.valueOf(typedValue.data);
            }
            return null;
        }
        return null;
    }

    public static boolean resolveBoolean(Context context, int i, boolean z) {
        TypedValue typedValue = getTypedValue(context);
        return context.getTheme().resolveAttribute(i, typedValue, true) ? typedValue.type == 18 && typedValue.data != 0 : z;
    }

    public static int resolveDimensionPixelSize(Context context, int i) {
        return context.getResources().getDimensionPixelSize(resolve(context, i));
    }

    public static int resolveInt(Context context, int i, int i2) {
        TypedValue typedValue = getTypedValue(context);
        return (!context.getTheme().resolveAttribute(i, typedValue, true) || typedValue.type != 16) ? i2 : typedValue.data;
    }

    public static float resolveFloat(Context context, int i, float f) {
        TypedValue typedValue = getTypedValue(context);
        return (!context.getTheme().resolveAttribute(i, typedValue, true) || typedValue.type != 4) ? f : typedValue.data;
    }

    public static TypedValue resolveTypedValue(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue;
        }
        return null;
    }
}
