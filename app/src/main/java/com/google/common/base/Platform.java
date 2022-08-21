package com.google.common.base;

import java.util.Locale;
import java.util.logging.Logger;

/* loaded from: classes.dex */
public final class Platform {
    public static final Logger logger = Logger.getLogger(Platform.class.getName());
    public static final PatternCompiler patternCompiler = loadPatternCompiler();

    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static long systemNanoTime() {
        return System.nanoTime();
    }

    public static String formatCompact4Digits(double d) {
        return String.format(Locale.ROOT, "%.4g", Double.valueOf(d));
    }

    public static boolean stringIsNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String emptyToNull(String str) {
        if (stringIsNullOrEmpty(str)) {
            return null;
        }
        return str;
    }

    public static PatternCompiler loadPatternCompiler() {
        return new JdkPatternCompiler();
    }

    /* loaded from: classes.dex */
    public static final class JdkPatternCompiler implements PatternCompiler {
        public JdkPatternCompiler() {
        }
    }
}
