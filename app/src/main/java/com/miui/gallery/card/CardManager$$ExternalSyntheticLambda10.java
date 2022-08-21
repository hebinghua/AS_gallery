package com.miui.gallery.card;

import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CardManager$$ExternalSyntheticLambda10 implements Function {
    public static final /* synthetic */ CardManager$$ExternalSyntheticLambda10 INSTANCE = new CardManager$$ExternalSyntheticLambda10();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Long.valueOf(((Card) obj).getRowId());
    }
}
