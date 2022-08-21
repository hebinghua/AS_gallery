package com.miui.gallery.biz.story.data.source;

import com.miui.gallery.card.Card;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

/* compiled from: CardRepository.kt */
/* loaded from: classes.dex */
public interface CardRepository {
    Object deleteCard(Card card, Continuation<? super Unit> continuation);

    Object updateCard(Card card, Continuation<? super Unit> continuation);
}
