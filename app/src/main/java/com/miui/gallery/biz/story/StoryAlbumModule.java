package com.miui.gallery.biz.story;

import com.miui.gallery.biz.story.data.source.CardLocalDataSource;
import com.miui.gallery.biz.story.data.source.CardRepository;
import com.miui.gallery.biz.story.data.source.CardRepositoryImpl;
import com.miui.gallery.util.concurrent.CoroutineDispatcherProvider;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: StoryAlbumModule.kt */
/* loaded from: classes.dex */
public final class StoryAlbumModule {
    public static final StoryAlbumModule INSTANCE = new StoryAlbumModule();

    public final CardRepository provideCardRepository(CoroutineDispatcherProvider dispatchers) {
        Intrinsics.checkNotNullParameter(dispatchers, "dispatchers");
        return new CardRepositoryImpl(new CardLocalDataSource(), dispatchers);
    }
}
