package com.miui.gallery.card;

import com.miui.gallery.util.logger.DefaultLogger;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CardManager$$ExternalSyntheticLambda6 implements Consumer {
    public static final /* synthetic */ CardManager$$ExternalSyntheticLambda6 INSTANCE = new CardManager$$ExternalSyntheticLambda6();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        DefaultLogger.d("CardManager", "| Recommendation |select Cards,remainCard:card=%s", (Card) obj);
    }
}
