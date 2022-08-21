package dagger.hilt.android.internal.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.AbstractSavedStateViewModelFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.savedstate.SavedStateRegistryOwner;
import dagger.hilt.EntryPoints;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import java.util.Map;
import java.util.Set;
import javax.inject.Provider;

/* loaded from: classes3.dex */
public final class HiltViewModelFactory implements ViewModelProvider.Factory {
    public final ViewModelProvider.Factory delegateFactory;
    public final AbstractSavedStateViewModelFactory hiltViewModelFactory;
    public final Set<String> hiltViewModelKeys;

    /* loaded from: classes3.dex */
    public interface ViewModelFactoriesEntryPoint {
        Map<String, Provider<ViewModel>> getHiltViewModelMap();
    }

    public HiltViewModelFactory(SavedStateRegistryOwner owner, Bundle defaultArgs, Set<String> hiltViewModelKeys, ViewModelProvider.Factory delegateFactory, final ViewModelComponentBuilder viewModelComponentBuilder) {
        this.hiltViewModelKeys = hiltViewModelKeys;
        this.delegateFactory = delegateFactory;
        this.hiltViewModelFactory = new AbstractSavedStateViewModelFactory(owner, defaultArgs) { // from class: dagger.hilt.android.internal.lifecycle.HiltViewModelFactory.1
            @Override // androidx.lifecycle.AbstractSavedStateViewModelFactory
            public <T extends ViewModel> T create(String key, Class<T> modelClass, SavedStateHandle handle) {
                Provider<ViewModel> provider = ((ViewModelFactoriesEntryPoint) EntryPoints.get(viewModelComponentBuilder.mo454savedStateHandle(handle).mo453build(), ViewModelFactoriesEntryPoint.class)).getHiltViewModelMap().get(modelClass.getName());
                if (provider == null) {
                    throw new IllegalStateException("Expected the @HiltViewModel-annotated class '" + modelClass.getName() + "' to be available in the multi-binding of @HiltViewModelMap but none was found.");
                }
                return (T) provider.get();
            }
        };
    }

    @Override // androidx.lifecycle.ViewModelProvider.Factory
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (this.hiltViewModelKeys.contains(modelClass.getName())) {
            return (T) this.hiltViewModelFactory.create(modelClass);
        }
        return (T) this.delegateFactory.create(modelClass);
    }
}
