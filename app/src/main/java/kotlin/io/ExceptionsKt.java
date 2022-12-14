package kotlin.io;

import java.io.File;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Exceptions.kt */
/* loaded from: classes3.dex */
public final class ExceptionsKt {
    public static final String constructMessage(File file, File file2, String str) {
        StringBuilder sb = new StringBuilder(file.toString());
        if (file2 != null) {
            sb.append(Intrinsics.stringPlus(" -> ", file2));
        }
        if (str != null) {
            sb.append(Intrinsics.stringPlus(": ", str));
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }
}
