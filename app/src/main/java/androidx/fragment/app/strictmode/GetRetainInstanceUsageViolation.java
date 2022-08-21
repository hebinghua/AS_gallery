package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GetRetainInstanceUsageViolation.kt */
/* loaded from: classes.dex */
public final class GetRetainInstanceUsageViolation extends RetainInstanceUsageViolation {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public GetRetainInstanceUsageViolation(Fragment fragment) {
        super(fragment, Intrinsics.stringPlus("Attempting to get retain instance for fragment ", fragment));
        Intrinsics.checkNotNullParameter(fragment, "fragment");
    }
}
