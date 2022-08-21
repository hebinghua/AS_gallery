package kotlin.text;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: _Strings.kt */
/* loaded from: classes3.dex */
public class StringsKt___StringsKt extends StringsKt___StringsJvmKt {
    public static final Character firstOrNull(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "<this>");
        if (charSequence.length() == 0) {
            return null;
        }
        return Character.valueOf(charSequence.charAt(0));
    }
}
