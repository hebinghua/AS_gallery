package com.miui.gallery.biz.story;

import com.miui.gallery.biz.story.data.source.CardRepository;
import com.miui.gallery.util.concurrent.CoroutineDispatcherProvider;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* loaded from: classes.dex */
public final class StoryAlbumModule_ProvideCardRepositoryFactory implements Provider {
    public static CardRepository provideCardRepository(CoroutineDispatcherProvider coroutineDispatcherProvider) {
        return (CardRepository) Preconditions.checkNotNullFromProvides(StoryAlbumModule.INSTANCE.provideCardRepository(coroutineDispatcherProvider));
    }
}
