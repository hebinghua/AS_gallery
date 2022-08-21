package com.miui.gallery.biz.story.data.source;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

/* compiled from: CardLocalDataSource.kt */
/* loaded from: classes.dex */
public final class CardLocalDataSource implements CardDataSource {
    @Override // com.miui.gallery.biz.story.data.source.CardDataSource
    public Object updateCard(Card card, Continuation<? super Unit> continuation) {
        CardManager.getInstance().updateCard(card, false);
        return Unit.INSTANCE;
    }

    @Override // com.miui.gallery.biz.story.data.source.CardDataSource
    public Object deleteCard(Card card, boolean z, Continuation<? super Unit> continuation) {
        CardManager.getInstance().delete(card, z);
        return Unit.INSTANCE;
    }
}
