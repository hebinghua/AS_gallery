package splitties.init;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.app.backup.BackupAgent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import ch.qos.logback.core.CoreConstants;
import java.util.List;
import java.util.Objects;
import kotlin.KotlinNothingValueException;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;

/* compiled from: AppCtx.kt */
/* loaded from: classes3.dex */
public final class AppCtxKt {
    @SuppressLint({"StaticFieldLeak"})
    public static Context internalCtx;

    public static final Context getAppCtx() {
        Context context = internalCtx;
        if (context != null) {
            return context;
        }
        internalCtxUninitialized();
        throw new KotlinNothingValueException();
    }

    public static final Void internalCtxUninitialized() {
        Pair pair;
        int i = 0;
        if (!StringsKt__StringsKt.contains$default((CharSequence) getProcessName(), (char) CoreConstants.COLON_CHAR, false, 2, (Object) null)) {
            pair = TuplesKt.to("App Startup didn't run", CollectionsKt__CollectionsKt.listOf((Object[]) new String[]{"If App Startup has been disabled, enable it back in the AndroidManifest.xml file of the app.", "For other cases, call injectAsAppCtx() in the app's Application subclass in its initializer or in its onCreate function."}));
        } else {
            pair = TuplesKt.to("App Startup is not enabled for non default processes", CollectionsKt__CollectionsJVMKt.listOf("Call injectAsAppCtx() in the app's Application subclass in its initializer or in its onCreate function."));
        }
        String str = (String) pair.component1();
        List list = (List) pair.component2();
        StringBuilder sb = new StringBuilder();
        sb.append("appCtx has not been initialized!");
        Intrinsics.checkNotNullExpressionValue(sb, "append(value)");
        sb.append('\n');
        Intrinsics.checkNotNullExpressionValue(sb, "append('\\n')");
        if (list.size() == 1) {
            sb.append(Intrinsics.stringPlus("Possible solution: ", CollectionsKt___CollectionsKt.single(list)));
            Intrinsics.checkNotNullExpressionValue(sb, "append(value)");
            sb.append('\n');
            Intrinsics.checkNotNullExpressionValue(sb, "append('\\n')");
        } else {
            sb.append(Intrinsics.stringPlus(str, ". Possible solutions:"));
            Intrinsics.checkNotNullExpressionValue(sb, "append(value)");
            sb.append('\n');
            Intrinsics.checkNotNullExpressionValue(sb, "append('\\n')");
            for (Object obj : list) {
                int i2 = i + 1;
                if (i < 0) {
                    CollectionsKt__CollectionsKt.throwIndexOverflow();
                }
                sb.append(i2);
                sb.append(". ");
                sb.append((String) obj);
                i = i2;
            }
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder().apply(builderAction).toString()");
        throw new IllegalStateException(sb2.toString());
    }

    public static final void injectAsAppCtx(Context context) {
        Intrinsics.checkNotNullParameter(context, "<this>");
        if (!(!canLeakMemory(context))) {
            throw new IllegalArgumentException(("The passed Context(" + context + ") would leak memory!").toString());
        }
        internalCtx = context;
    }

    public static final boolean canLeakMemory(Context context) {
        Intrinsics.checkNotNullParameter(context, "<this>");
        if (context instanceof Application) {
            return false;
        }
        if (!(context instanceof Activity ? true : context instanceof Service ? true : context instanceof BackupAgent)) {
            if (context instanceof ContextWrapper) {
                ContextWrapper contextWrapper = (ContextWrapper) context;
                if (contextWrapper.getBaseContext() != context) {
                    Context baseContext = contextWrapper.getBaseContext();
                    Intrinsics.checkNotNullExpressionValue(baseContext, "baseContext");
                    return canLeakMemory(baseContext);
                }
            } else if (context.getApplicationContext() != null) {
                return false;
            }
        }
        return true;
    }

    public static final String getProcessName() {
        int i = Build.VERSION.SDK_INT;
        if (i >= 28) {
            String processName = Application.getProcessName();
            Intrinsics.checkNotNullExpressionValue(processName, "getProcessName()");
            return processName;
        }
        Object invoke = Class.forName("android.app.ActivityThread").getDeclaredMethod(i >= 18 ? "currentProcessName" : "currentPackageName", new Class[0]).invoke(null, new Object[0]);
        Objects.requireNonNull(invoke, "null cannot be cast to non-null type kotlin.String");
        return (String) invoke;
    }
}
