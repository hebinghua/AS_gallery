package com.miui.gallery.biz.story.data.source;

import com.miui.gallery.card.Card;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

/* compiled from: CardDataSource.kt */
/* loaded from: classes.dex */
public interface CardDataSource {
    Object deleteCard(Card card, boolean z, Continuation<? super Unit> continuation);

    Object updateCard(Card card, Continuation<? super Unit> continuation);
}
