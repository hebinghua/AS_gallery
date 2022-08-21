package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud;

import com.miui.gallery.provider.cache.ShareMediaManager;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class DeleteShare$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ DeleteShare$$ExternalSyntheticLambda0 INSTANCE = new DeleteShare$$ExternalSyntheticLambda0();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Long.valueOf(ShareMediaManager.getOriginalMediaId(((Long) obj).longValue()));
    }
}
