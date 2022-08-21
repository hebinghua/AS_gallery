package com.miui.gallery.ui;

import com.miui.gallery.adapter.CheckableAdapter;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class BaseProduceFilter$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ BaseProduceFilter$$ExternalSyntheticLambda2 INSTANCE = new BaseProduceFilter$$ExternalSyntheticLambda2();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$filter$1;
        lambda$filter$1 = BaseProduceFilter.lambda$filter$1((CheckableAdapter.CheckedItem) obj);
        return lambda$filter$1;
    }
}
