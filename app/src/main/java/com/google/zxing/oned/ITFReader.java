package com.google.zxing.oned;

/* loaded from: classes.dex */
public final class ITFReader extends OneDReader {
    public static final int[] DEFAULT_ALLOWED_LENGTHS = {6, 8, 10, 12, 14};
    public static final int[] START_PATTERN = {1, 1, 1, 1};
    public static final int[] END_PATTERN_REVERSED = {1, 1, 3};
    public static final int[][] PATTERNS = {new int[]{1, 1, 3, 3, 1}, new int[]{3, 1, 1, 1, 3}, new int[]{1, 3, 1, 1, 3}, new int[]{3, 3, 1, 1, 1}, new int[]{1, 1, 3, 1, 3}, new int[]{3, 1, 3, 1, 1}, new int[]{1, 3, 3, 1, 1}, new int[]{1, 1, 1, 3, 3}, new int[]{3, 1, 1, 3, 1}, new int[]{1, 3, 1, 3, 1}};
}
