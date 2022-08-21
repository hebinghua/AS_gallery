package androidx.constraintlayout.core.motion.utils;

/* loaded from: classes.dex */
public class Easing {
    public String str = "identity";
    public static Easing sDefault = new Easing();
    public static String[] NAMED_EASING = {"standard", "accelerate", "decelerate", "linear"};

    public double get(double d) {
        return d;
    }

    public double getDiff(double d) {
        return 1.0d;
    }

    public String toString() {
        return this.str;
    }
}
