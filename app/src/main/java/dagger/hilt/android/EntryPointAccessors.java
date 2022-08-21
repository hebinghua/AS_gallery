package dagger.hilt.android;

import android.content.Context;
import dagger.hilt.EntryPoints;
import dagger.hilt.android.internal.Contexts;

/* loaded from: classes3.dex */
public final class EntryPointAccessors {
    public static <T> T fromApplication(Context context, Class<T> entryPoint) {
        return (T) EntryPoints.get(Contexts.getApplication(context.getApplicationContext()), entryPoint);
    }
}
