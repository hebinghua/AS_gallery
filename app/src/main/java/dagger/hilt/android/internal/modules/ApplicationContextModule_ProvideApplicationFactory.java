package dagger.hilt.android.internal.modules;

import android.app.Application;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* loaded from: classes3.dex */
public final class ApplicationContextModule_ProvideApplicationFactory implements Provider {
    public static Application provideApplication(ApplicationContextModule instance) {
        return (Application) Preconditions.checkNotNullFromProvides(instance.provideApplication());
    }
}
