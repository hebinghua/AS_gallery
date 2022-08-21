package com.miui.gallery.util.logger;

import android.text.TextUtils;
import android.util.Printer;
import com.miui.gallery.preference.BaseGalleryPreferences;
import java.util.Stack;

/* loaded from: classes2.dex */
public class TimingTracing {
    public static boolean ENABLED = false;
    public static final ThreadLocal<Stack<TimingLogger>> mTracings = ThreadLocal.withInitial(TimingTracing$$ExternalSyntheticLambda0.INSTANCE);

    public static void setEnabled(boolean z) {
        ENABLED = z;
    }

    public static boolean isEnabled() {
        return ENABLED || BaseGalleryPreferences.Debug.isPrintLog();
    }

    public static void beginTracing(String str, String str2) {
        if (!isEnabled()) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.e("TimingTracing", "beginTracing: the trace tag shouldn't be empty");
            return;
        }
        mTracings.get().push(new TimingLogger(str, str2));
    }

    public static long stopTracing(Printer printer) {
        if (!isEnabled()) {
            return -1L;
        }
        ThreadLocal<Stack<TimingLogger>> threadLocal = mTracings;
        if (threadLocal.get().isEmpty()) {
            DefaultLogger.e("TimingTracing", "stopTracing error: empty stack");
            return -1L;
        }
        TimingLogger pop = threadLocal.get().pop();
        if (pop != null) {
            return pop.dump(printer);
        }
        DefaultLogger.e("TimingTracing", "stopTracing: Did you have called the beginTracing?");
        return -1L;
    }

    public static void addSplit(String str) {
        Stack<TimingLogger> stack;
        if (isEnabled() && (stack = mTracings.get()) != null && !stack.empty()) {
            TimingLogger peek = stack.peek();
            if (peek != null) {
                peek.addSplit(str);
            } else {
                DefaultLogger.e("TimingTracing", "addSplit: Did you have called the beginTracing?");
            }
        }
    }
}
