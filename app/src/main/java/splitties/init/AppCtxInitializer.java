package splitties.init;

import android.content.Context;
import androidx.annotation.Keep;
import androidx.startup.Initializer;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AppCtxInitializer.kt */
@Keep
/* loaded from: classes3.dex */
public final class AppCtxInitializer implements Initializer<AppCtxInitializer> {
    @Override // androidx.startup.Initializer
    /* renamed from: create  reason: collision with other method in class */
    public AppCtxInitializer mo2646create(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        AppCtxKt.injectAsAppCtx(context);
        return this;
    }

    @Override // androidx.startup.Initializer
    public List dependencies() {
        return CollectionsKt__CollectionsKt.emptyList();
    }
}
