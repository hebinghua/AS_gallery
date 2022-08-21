package com.miui.gallery.card;

import com.miui.gallery.util.logger.DefaultLogger;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CardManager$$ExternalSyntheticLambda5 implements Consumer {
    public static final /* synthetic */ CardManager$$ExternalSyntheticLambda5 INSTANCE = new CardManager$$ExternalSyntheticLambda5();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        DefaultLogger.d("CardManager", "| Recommendation |select Cards,todayCards:card=%s", (Card) obj);
    }
}
