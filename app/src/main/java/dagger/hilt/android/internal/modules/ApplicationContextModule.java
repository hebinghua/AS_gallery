package dagger.hilt.android.internal.modules;

import android.app.Application;
import android.content.Context;
import dagger.hilt.android.internal.Contexts;

/* loaded from: classes3.dex */
public final class ApplicationContextModule {
    public final Context applicationContext;

    public ApplicationContextModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Context provideContext() {
        return this.applicationContext;
    }

    public Application provideApplication() {
        return Contexts.getApplication(this.applicationContext);
    }
}
