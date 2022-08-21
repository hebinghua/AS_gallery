package com.miui.gallery.card;

import com.miui.gallery.assistant.model.MediaFeatureItem;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CardManager$$ExternalSyntheticLambda9 implements Function {
    public static final /* synthetic */ CardManager$$ExternalSyntheticLambda9 INSTANCE = new CardManager$$ExternalSyntheticLambda9();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Long.valueOf(((MediaFeatureItem) obj).getMediaId());
    }
}
