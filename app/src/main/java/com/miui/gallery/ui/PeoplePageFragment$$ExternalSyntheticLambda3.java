package com.miui.gallery.ui;

import com.miui.gallery.provider.PeopleFaceSnapshotHelper;
import io.reactivex.functions.Function;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class PeoplePageFragment$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ PeoplePageFragment$$ExternalSyntheticLambda3 INSTANCE = new PeoplePageFragment$$ExternalSyntheticLambda3();

    @Override // io.reactivex.functions.Function
    /* renamed from: apply */
    public final Object mo2564apply(Object obj) {
        return Integer.valueOf(PeopleFaceSnapshotHelper.persist((List) obj));
    }
}
