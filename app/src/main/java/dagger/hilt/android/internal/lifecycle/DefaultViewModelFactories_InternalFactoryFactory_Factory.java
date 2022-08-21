package dagger.hilt.android.internal.lifecycle;

import android.app.Application;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import java.util.Set;
import javax.inject.Provider;

/* loaded from: classes3.dex */
public final class DefaultViewModelFactories_InternalFactoryFactory_Factory implements Provider {
    public static DefaultViewModelFactories.InternalFactoryFactory newInstance(Application application, Set<String> keySet, ViewModelComponentBuilder viewModelComponentBuilder) {
        return new DefaultViewModelFactories.InternalFactoryFactory(application, keySet, viewModelComponentBuilder);
    }
}
