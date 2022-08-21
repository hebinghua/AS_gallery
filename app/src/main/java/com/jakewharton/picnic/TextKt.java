package com.jakewharton.picnic;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt__StringsKt;

/* compiled from: text.kt */
/* loaded from: classes.dex */
public final class TextKt {
    public static final Regex ansiColorEscape = new Regex("\\u001B\\[\\d+(;\\d+)*m");

    public static final int visualIndex(CharSequence visualIndex, int i) {
        int codePointCount;
        Intrinsics.checkNotNullParameter(visualIndex, "$this$visualIndex");
        int i2 = 0;
        while (true) {
            MatchResult find = ansiColorEscape.find(visualIndex, i2);
            if (find == null || (codePointCount = codePointCount(visualIndex, i2, find.getRange().getFirst())) > i) {
                break;
            }
            i -= codePointCount;
            i2 = find.getRange().getLast() + 1;
        }
        while (i > 0) {
            i2 += getCharCount(codePointAt(visualIndex, i2));
            i--;
        }
        return i2;
    }

    public static final int getVisualCodePointCount(CharSequence visualCodePointCount) {
        Intrinsics.checkNotNullParameter(visualCodePointCount, "$this$visualCodePointCount");
        int indexOf$default = StringsKt__StringsKt.indexOf$default(visualCodePointCount, (char) 27, 0, false, 6, (Object) null);
        if (indexOf$default == -1) {
            return codePointCount$default(visualCodePointCount, 0, 0, 3, null);
        }
        int codePointCount$default = codePointCount$default(visualCodePointCount, 0, indexOf$default, 1, null);
        while (true) {
            MatchResult find = ansiColorEscape.find(visualCodePointCount, indexOf$default);
            if (find != null) {
                codePointCount$default += codePointCount(visualCodePointCount, indexOf$default, find.getRange().getFirst());
                indexOf$default = find.getRange().getLast() + 1;
            } else {
                return codePointCount$default + codePointCount$default(visualCodePointCount, indexOf$default, 0, 2, null);
            }
        }
    }

    public static /* synthetic */ int codePointCount$default(CharSequence charSequence, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = charSequence.length();
        }
        return codePointCount(charSequence, i, i2);
    }

    public static final int codePointCount(CharSequence charSequence, int i, int i2) {
        return Character.codePointCount(charSequence, i, i2);
    }

    public static final int codePointAt(CharSequence charSequence, int i) {
        return Character.codePointAt(charSequence, i);
    }

    public static final int getCharCount(int i) {
        return Character.charCount(i);
    }
}
