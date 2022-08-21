package com.miui.gallery;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.miui.gallery.activity.AlbumPermissionActivity;
import com.miui.gallery.analytics.AnalyticsDependsModule;
import com.miui.gallery.biz.albumpermission.AlbumPermissionFragment;
import com.miui.gallery.biz.albumpermission.AlbumPermissionFragment_MembersInjector;
import com.miui.gallery.biz.albumpermission.AlbumPermissionViewModel;
import com.miui.gallery.biz.story.StoryAlbumFragment;
import com.miui.gallery.biz.story.StoryAlbumFragment_MembersInjector;
import com.miui.gallery.biz.story.StoryAlbumModule_ProvideCardRepositoryFactory;
import com.miui.gallery.biz.story.StoryAlbumViewModel;
import com.miui.gallery.biz.story.data.source.CardRepository;
import com.miui.gallery.biz.story.domain.DeleteCard;
import com.miui.gallery.biz.story.domain.ParseDownloadTasks;
import com.miui.gallery.biz.story.domain.RenameCard;
import com.miui.gallery.biz.story.domain.UpdateCard;
import com.miui.gallery.card.ui.detail.StoryAlbumActivity;
import com.miui.gallery.imodule.base.IModule;
import com.miui.gallery.imodule.modules.MagicDependsModule;
import com.miui.gallery.imodule.modules.MovieDependsModule;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.inject.AppModule_ProvideCoroutineDispatchersFactory;
import com.miui.gallery.inject.AppModule_ProvideGlobalMainScopeFactory;
import com.miui.gallery.inject.LoggerModule_ProvideConfiguratorFactory;
import com.miui.gallery.magic.MagicDependsModuleImpl;
import com.miui.gallery.movie.picker.MovieDependsModuleImpl;
import com.miui.gallery.stat.StatsDependsImpl;
import com.miui.gallery.stat.StatsDependsModule;
import com.miui.gallery.storage.GalleryFilePathResolver;
import com.miui.gallery.storage.android28.SAFStoragePermissionRequester;
import com.miui.gallery.storage.utils.IFilePathResolver;
import com.miui.gallery.storage.utils.IMediaStoreIdResolver;
import com.miui.gallery.storage.utils.ISAFStoragePermissionRequester;
import com.miui.gallery.track.AnalyticsDependsImpl;
import com.miui.gallery.util.concurrent.CoroutineDispatcherProvider;
import com.miui.gallery.util.logger.LoggerConfigurator;
import com.miui.gallery.vlog.VlogDependsModuleImpl;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_Lifecycle_Factory;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideApplicationFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import java.util.Map;
import java.util.Set;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes.dex */
public final class DaggerGalleryApp_HiltComponents_SingletonC extends GalleryApp_HiltComponents$SingletonC {
    public Provider<AnalyticsDependsImpl> analyticsDependsImplProvider;
    public final ApplicationContextModule applicationContextModule;
    public Provider<GalleryFilePathResolver> galleryFilePathResolverProvider;
    public Provider<MagicDependsModuleImpl> magicDependsModuleImplProvider;
    public Provider<MovieDependsModuleImpl> movieDependsModuleImplProvider;
    public Provider<LoggerConfigurator> provideConfiguratorProvider;
    public Provider<CoroutineDispatcherProvider> provideCoroutineDispatchersProvider;
    public Provider<CoroutineScope> provideGlobalMainScopeProvider;
    public Provider<SAFStoragePermissionRequester> sAFStoragePermissionRequesterProvider;
    public Provider<com.miui.gallery.storage.android30.SAFStoragePermissionRequester> sAFStoragePermissionRequesterProvider2;
    public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;
    public Provider<StatsDependsImpl> statsDependsImplProvider;
    public Provider<VlogDependsModuleImpl> vlogDependsModuleImplProvider;

    @Override // com.miui.gallery.GalleryApp_GeneratedInjector
    public void injectGalleryApp(GalleryApp galleryApp) {
    }

    public DaggerGalleryApp_HiltComponents_SingletonC(ApplicationContextModule applicationContextModule) {
        this.singletonC = this;
        this.applicationContextModule = applicationContextModule;
        initialize(applicationContextModule);
    }

    public static Builder builder() {
        return new Builder();
    }

    public final VlogDependsModuleImpl vlogDependsModuleImpl() {
        return new VlogDependsModuleImpl(ApplicationContextModule_ProvideContextFactory.provideContext(this.applicationContextModule));
    }

    public final void initialize(ApplicationContextModule applicationContextModule) {
        this.provideGlobalMainScopeProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 0));
        this.movieDependsModuleImplProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 1));
        this.vlogDependsModuleImplProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 2));
        this.magicDependsModuleImplProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 3));
        this.analyticsDependsImplProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 4));
        this.statsDependsImplProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 5));
        this.sAFStoragePermissionRequesterProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 6));
        this.sAFStoragePermissionRequesterProvider2 = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 7));
        this.galleryFilePathResolverProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 8));
        this.provideConfiguratorProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 9));
        this.provideCoroutineDispatchersProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, 10));
    }

    @Override // com.miui.gallery.inject.ModuleEntryPoint
    public Map<Class<? extends IModule>, Provider<Object>> getModuleMapping() {
        return ImmutableMap.of(MovieDependsModule.class, (Provider<StatsDependsImpl>) this.movieDependsModuleImplProvider, VlogDependsModule.class, (Provider<StatsDependsImpl>) this.vlogDependsModuleImplProvider, MagicDependsModule.class, (Provider<StatsDependsImpl>) this.magicDependsModuleImplProvider, AnalyticsDependsModule.class, (Provider<StatsDependsImpl>) this.analyticsDependsImplProvider, StatsDependsModule.class, this.statsDependsImplProvider);
    }

    @Override // com.miui.gallery.storage.StorageSolutionEntryPoint
    public Set<ISAFStoragePermissionRequester> getSAFPermissionRequesterRegistry() {
        return ImmutableSet.of((com.miui.gallery.storage.android30.SAFStoragePermissionRequester) this.sAFStoragePermissionRequesterProvider.get(), this.sAFStoragePermissionRequesterProvider2.get());
    }

    @Override // com.miui.gallery.storage.StorageSolutionEntryPoint
    public Set<IFilePathResolver> getFilePathResolverRegistry() {
        return ImmutableSet.of(this.galleryFilePathResolverProvider.get());
    }

    @Override // com.miui.gallery.storage.StorageSolutionEntryPoint
    public Set<IMediaStoreIdResolver> getMediaStoreIdResolverRegistry() {
        return ImmutableSet.of();
    }

    @Override // com.miui.gallery.util.logger.LoggerEntryPoint
    public LoggerConfigurator configurator() {
        return this.provideConfiguratorProvider.get();
    }

    @Override // dagger.hilt.android.internal.managers.ActivityRetainedComponentManager.ActivityRetainedComponentBuilderEntryPoint
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
        return new ActivityRetainedCBuilder();
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        public ApplicationContextModule applicationContextModule;

        public Builder() {
        }

        public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
            this.applicationContextModule = (ApplicationContextModule) Preconditions.checkNotNull(applicationContextModule);
            return this;
        }

        public GalleryApp_HiltComponents$SingletonC build() {
            Preconditions.checkBuilderRequirement(this.applicationContextModule, ApplicationContextModule.class);
            return new DaggerGalleryApp_HiltComponents_SingletonC(this.applicationContextModule);
        }
    }

    /* loaded from: classes.dex */
    public static final class ActivityRetainedCBuilder implements ActivityRetainedComponentBuilder {
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

        public ActivityRetainedCBuilder(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC) {
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
        }

        @Override // dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder
        /* renamed from: build */
        public GalleryApp_HiltComponents$ActivityRetainedC mo450build() {
            return new ActivityRetainedCImpl();
        }
    }

    /* loaded from: classes.dex */
    public static final class ActivityCBuilder implements ActivityComponentBuilder {
        public Activity activity;
        public final ActivityRetainedCImpl activityRetainedCImpl;
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

        public ActivityCBuilder(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC, ActivityRetainedCImpl activityRetainedCImpl) {
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
            this.activityRetainedCImpl = activityRetainedCImpl;
        }

        @Override // dagger.hilt.android.internal.builders.ActivityComponentBuilder
        /* renamed from: activity */
        public ActivityCBuilder mo448activity(Activity activity) {
            this.activity = (Activity) Preconditions.checkNotNull(activity);
            return this;
        }

        @Override // dagger.hilt.android.internal.builders.ActivityComponentBuilder
        /* renamed from: build */
        public GalleryApp_HiltComponents$ActivityC mo449build() {
            Preconditions.checkBuilderRequirement(this.activity, Activity.class);
            return new ActivityCImpl(this.activityRetainedCImpl, this.activity);
        }
    }

    /* loaded from: classes.dex */
    public static final class FragmentCBuilder implements FragmentComponentBuilder {
        public final ActivityCImpl activityCImpl;
        public final ActivityRetainedCImpl activityRetainedCImpl;
        public Fragment fragment;
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

        public FragmentCBuilder(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC, ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
            this.activityRetainedCImpl = activityRetainedCImpl;
            this.activityCImpl = activityCImpl;
        }

        @Override // dagger.hilt.android.internal.builders.FragmentComponentBuilder
        /* renamed from: fragment */
        public FragmentCBuilder mo452fragment(Fragment fragment) {
            this.fragment = (Fragment) Preconditions.checkNotNull(fragment);
            return this;
        }

        @Override // dagger.hilt.android.internal.builders.FragmentComponentBuilder
        /* renamed from: build */
        public GalleryApp_HiltComponents$FragmentC mo451build() {
            Preconditions.checkBuilderRequirement(this.fragment, Fragment.class);
            return new FragmentCImpl(this.activityRetainedCImpl, this.activityCImpl, this.fragment);
        }
    }

    /* loaded from: classes.dex */
    public static final class ViewModelCBuilder implements ViewModelComponentBuilder {
        public final ActivityRetainedCImpl activityRetainedCImpl;
        public SavedStateHandle savedStateHandle;
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

        public ViewModelCBuilder(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC, ActivityRetainedCImpl activityRetainedCImpl) {
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
            this.activityRetainedCImpl = activityRetainedCImpl;
        }

        @Override // dagger.hilt.android.internal.builders.ViewModelComponentBuilder
        /* renamed from: savedStateHandle */
        public ViewModelCBuilder mo454savedStateHandle(SavedStateHandle savedStateHandle) {
            this.savedStateHandle = (SavedStateHandle) Preconditions.checkNotNull(savedStateHandle);
            return this;
        }

        @Override // dagger.hilt.android.internal.builders.ViewModelComponentBuilder
        /* renamed from: build */
        public GalleryApp_HiltComponents$ViewModelC mo453build() {
            Preconditions.checkBuilderRequirement(this.savedStateHandle, SavedStateHandle.class);
            return new ViewModelCImpl(this.activityRetainedCImpl, this.savedStateHandle);
        }
    }

    /* loaded from: classes.dex */
    public static final class FragmentCImpl extends GalleryApp_HiltComponents$FragmentC {
        public final ActivityCImpl activityCImpl;
        public final ActivityRetainedCImpl activityRetainedCImpl;
        public final FragmentCImpl fragmentCImpl;
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

        public FragmentCImpl(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC, ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl, Fragment fragment) {
            this.fragmentCImpl = this;
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
            this.activityRetainedCImpl = activityRetainedCImpl;
            this.activityCImpl = activityCImpl;
        }

        public final AlbumPermissionViewModel albumPermissionViewModel() {
            return new AlbumPermissionViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(this.singletonC.applicationContextModule), (CoroutineScope) this.singletonC.provideGlobalMainScopeProvider.get());
        }

        public final AlbumPermissionViewModel.AssistedVMFactory assistedVMFactory() {
            return new AlbumPermissionViewModel.AssistedVMFactory() { // from class: com.miui.gallery.DaggerGalleryApp_HiltComponents_SingletonC.FragmentCImpl.1
                @Override // com.miui.gallery.biz.albumpermission.AlbumPermissionViewModel.AssistedVMFactory
                public AlbumPermissionViewModel create() {
                    return FragmentCImpl.this.fragmentCImpl.albumPermissionViewModel();
                }
            };
        }

        public final UpdateCard updateCard() {
            return new UpdateCard(this.activityRetainedCImpl.cardRepository());
        }

        public final ParseDownloadTasks parseDownloadTasks() {
            return new ParseDownloadTasks((CoroutineDispatcherProvider) this.singletonC.provideCoroutineDispatchersProvider.get());
        }

        public final DeleteCard deleteCard() {
            return new DeleteCard(this.activityRetainedCImpl.cardRepository());
        }

        public final RenameCard renameCard() {
            return new RenameCard(this.activityRetainedCImpl.cardRepository());
        }

        public final StoryAlbumViewModel storyAlbumViewModel(long j) {
            return new StoryAlbumViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(this.singletonC.applicationContextModule), (CoroutineScope) this.singletonC.provideGlobalMainScopeProvider.get(), j, updateCard(), parseDownloadTasks(), deleteCard(), renameCard());
        }

        public final StoryAlbumViewModel.AssistedVMFactory assistedVMFactory2() {
            return new StoryAlbumViewModel.AssistedVMFactory() { // from class: com.miui.gallery.DaggerGalleryApp_HiltComponents_SingletonC.FragmentCImpl.2
                @Override // com.miui.gallery.biz.story.StoryAlbumViewModel.AssistedVMFactory
                public StoryAlbumViewModel create(long j) {
                    return FragmentCImpl.this.fragmentCImpl.storyAlbumViewModel(j);
                }
            };
        }

        @Override // com.miui.gallery.biz.albumpermission.AlbumPermissionFragment_GeneratedInjector
        public void injectAlbumPermissionFragment(AlbumPermissionFragment albumPermissionFragment) {
            injectAlbumPermissionFragment2(albumPermissionFragment);
        }

        @Override // com.miui.gallery.biz.story.StoryAlbumFragment_GeneratedInjector
        public void injectStoryAlbumFragment(StoryAlbumFragment storyAlbumFragment) {
            injectStoryAlbumFragment2(storyAlbumFragment);
        }

        @Override // dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories.FragmentEntryPoint
        public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
            return this.activityCImpl.getHiltInternalFactoryFactory();
        }

        public final AlbumPermissionFragment injectAlbumPermissionFragment2(AlbumPermissionFragment albumPermissionFragment) {
            AlbumPermissionFragment_MembersInjector.injectVmFactory(albumPermissionFragment, assistedVMFactory());
            return albumPermissionFragment;
        }

        public final StoryAlbumFragment injectStoryAlbumFragment2(StoryAlbumFragment storyAlbumFragment) {
            StoryAlbumFragment_MembersInjector.injectVmFactory(storyAlbumFragment, assistedVMFactory2());
            return storyAlbumFragment;
        }
    }

    /* loaded from: classes.dex */
    public static final class ActivityCImpl extends GalleryApp_HiltComponents$ActivityC {
        public final ActivityCImpl activityCImpl;
        public final ActivityRetainedCImpl activityRetainedCImpl;
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

        @Override // com.miui.gallery.activity.AlbumPermissionActivity_GeneratedInjector
        public void injectAlbumPermissionActivity(AlbumPermissionActivity albumPermissionActivity) {
        }

        @Override // com.miui.gallery.card.ui.detail.StoryAlbumActivity_GeneratedInjector
        public void injectStoryAlbumActivity(StoryAlbumActivity storyAlbumActivity) {
        }

        public ActivityCImpl(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC, ActivityRetainedCImpl activityRetainedCImpl, Activity activity) {
            this.activityCImpl = this;
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
            this.activityRetainedCImpl = activityRetainedCImpl;
        }

        @Override // dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories.ActivityEntryPoint
        public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
            return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(ApplicationContextModule_ProvideApplicationFactory.provideApplication(this.singletonC.applicationContextModule), ImmutableSet.of(), new ViewModelCBuilder(this.activityRetainedCImpl));
        }

        @Override // dagger.hilt.android.internal.managers.FragmentComponentManager.FragmentComponentBuilderEntryPoint
        public FragmentComponentBuilder fragmentComponentBuilder() {
            return new FragmentCBuilder(this.activityRetainedCImpl, this.activityCImpl);
        }
    }

    /* loaded from: classes.dex */
    public static final class ViewModelCImpl extends GalleryApp_HiltComponents$ViewModelC {
        public final ActivityRetainedCImpl activityRetainedCImpl;
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;
        public final ViewModelCImpl viewModelCImpl;

        public ViewModelCImpl(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC, ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandle) {
            this.viewModelCImpl = this;
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
            this.activityRetainedCImpl = activityRetainedCImpl;
        }

        @Override // dagger.hilt.android.internal.lifecycle.HiltViewModelFactory.ViewModelFactoriesEntryPoint
        public Map<String, Provider<ViewModel>> getHiltViewModelMap() {
            return ImmutableMap.of();
        }
    }

    /* loaded from: classes.dex */
    public static final class ActivityRetainedCImpl extends GalleryApp_HiltComponents$ActivityRetainedC {
        public final ActivityRetainedCImpl activityRetainedCImpl;
        public Provider lifecycleProvider;
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

        public ActivityRetainedCImpl(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC) {
            this.activityRetainedCImpl = this;
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
            initialize();
        }

        public final CardRepository cardRepository() {
            return StoryAlbumModule_ProvideCardRepositoryFactory.provideCardRepository((CoroutineDispatcherProvider) this.singletonC.provideCoroutineDispatchersProvider.get());
        }

        public final void initialize() {
            this.lifecycleProvider = DoubleCheck.provider(new SwitchingProvider(this.singletonC, this.activityRetainedCImpl, 0));
        }

        @Override // dagger.hilt.android.internal.managers.ActivityComponentManager.ActivityComponentBuilderEntryPoint
        public ActivityComponentBuilder activityComponentBuilder() {
            return new ActivityCBuilder(this.activityRetainedCImpl);
        }

        @Override // dagger.hilt.android.internal.managers.ActivityRetainedComponentManager.ActivityRetainedLifecycleEntryPoint
        public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
            return (ActivityRetainedLifecycle) this.lifecycleProvider.get();
        }

        /* loaded from: classes.dex */
        public static final class SwitchingProvider<T> implements Provider<T> {
            public final ActivityRetainedCImpl activityRetainedCImpl;
            public final int id;
            public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

            public SwitchingProvider(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC, ActivityRetainedCImpl activityRetainedCImpl, int i) {
                this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
                this.activityRetainedCImpl = activityRetainedCImpl;
                this.id = i;
            }

            @Override // javax.inject.Provider
            public T get() {
                if (this.id == 0) {
                    return (T) ActivityRetainedComponentManager_Lifecycle_Factory.newInstance();
                }
                throw new AssertionError(this.id);
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class SwitchingProvider<T> implements Provider<T> {
        public final int id;
        public final DaggerGalleryApp_HiltComponents_SingletonC singletonC;

        public SwitchingProvider(DaggerGalleryApp_HiltComponents_SingletonC daggerGalleryApp_HiltComponents_SingletonC, int i) {
            this.singletonC = daggerGalleryApp_HiltComponents_SingletonC;
            this.id = i;
        }

        @Override // javax.inject.Provider
        public T get() {
            switch (this.id) {
                case 0:
                    return (T) AppModule_ProvideGlobalMainScopeFactory.provideGlobalMainScope();
                case 1:
                    return (T) new MovieDependsModuleImpl();
                case 2:
                    return (T) this.singletonC.vlogDependsModuleImpl();
                case 3:
                    return (T) new MagicDependsModuleImpl();
                case 4:
                    return (T) new AnalyticsDependsImpl();
                case 5:
                    return (T) new StatsDependsImpl();
                case 6:
                    return (T) new SAFStoragePermissionRequester();
                case 7:
                    return (T) new com.miui.gallery.storage.android30.SAFStoragePermissionRequester();
                case 8:
                    return (T) new GalleryFilePathResolver();
                case 9:
                    return (T) LoggerModule_ProvideConfiguratorFactory.provideConfigurator();
                case 10:
                    return (T) AppModule_ProvideCoroutineDispatchersFactory.provideCoroutineDispatchers();
                default:
                    throw new AssertionError(this.id);
            }
        }
    }
}
