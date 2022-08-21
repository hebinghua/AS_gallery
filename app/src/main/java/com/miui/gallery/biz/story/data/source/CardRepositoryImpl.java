package com.miui.gallery.biz.story.data.source;

import com.miui.gallery.card.Card;
import com.miui.gallery.util.concurrent.CoroutineDispatcherProvider;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;

/* compiled from: CardRepositoryImpl.kt */
/* loaded from: classes.dex */
public final class CardRepositoryImpl implements CardRepository {
    public final CardDataSource dataSource;
    public final CoroutineDispatcherProvider dispatchers;

    public CardRepositoryImpl(CardDataSource dataSource, CoroutineDispatcherProvider dispatchers) {
        Intrinsics.checkNotNullParameter(dataSource, "dataSource");
        Intrinsics.checkNotNullParameter(dispatchers, "dispatchers");
        this.dataSource = dataSource;
        this.dispatchers = dispatchers;
    }

    @Override // com.miui.gallery.biz.story.data.source.CardRepository
    public Object updateCard(Card card, Continuation<? super Unit> continuation) {
        Object withContext = BuildersKt.withContext(this.dispatchers.getIo(), new CardRepositoryImpl$updateCard$2(this, card, null), continuation);
        return withContext == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? withContext : Unit.INSTANCE;
    }

    @Override // com.miui.gallery.biz.story.data.source.CardRepository
    public Object deleteCard(Card card, Continuation<? super Unit> continuation) {
        Object withContext = BuildersKt.withContext(this.dispatchers.getIo(), new CardRepositoryImpl$deleteCard$2(this, card, null), continuation);
        return withContext == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? withContext : Unit.INSTANCE;
    }
}
