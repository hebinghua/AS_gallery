package dagger.hilt.android.internal.modules;

import android.content.Context;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* loaded from: classes3.dex */
public final class ApplicationContextModule_ProvideContextFactory implements Provider {
    public static Context provideContext(ApplicationContextModule instance) {
        return (Context) Preconditions.checkNotNullFromProvides(instance.provideContext());
    }
}
