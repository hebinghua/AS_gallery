package com.airbnb.lottie;

import androidx.core.os.TraceCompat;

/* loaded from: classes.dex */
public class L {
    public static boolean DBG = false;
    public static int depthPastMaxDepth = 0;
    public static String[] sections = null;
    public static long[] startTimeNs = null;
    public static int traceDepth = 0;
    public static boolean traceEnabled = false;

    public static void beginSection(String str) {
        if (!traceEnabled) {
            return;
        }
        int i = traceDepth;
        if (i == 20) {
            depthPastMaxDepth++;
            return;
        }
        sections[i] = str;
        startTimeNs[i] = System.nanoTime();
        TraceCompat.beginSection(str);
        traceDepth++;
    }

    public static float endSection(String str) {
        int i = depthPastMaxDepth;
        if (i > 0) {
            depthPastMaxDepth = i - 1;
            return 0.0f;
        } else if (!traceEnabled) {
            return 0.0f;
        } else {
            int i2 = traceDepth - 1;
            traceDepth = i2;
            if (i2 == -1) {
                throw new IllegalStateException("Can't end trace section. There are none.");
            }
            if (!str.equals(sections[i2])) {
                throw new IllegalStateException("Unbalanced trace call " + str + ". Expected " + sections[traceDepth] + ".");
            }
            TraceCompat.endSection();
            return ((float) (System.nanoTime() - startTimeNs[traceDepth])) / 1000000.0f;
        }
    }
}
