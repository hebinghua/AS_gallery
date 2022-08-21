package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id;

import com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch;
import java.util.function.ToLongFunction;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class LogicBranch$$ExternalSyntheticLambda2 implements ToLongFunction {
    public static final /* synthetic */ LogicBranch$$ExternalSyntheticLambda2 INSTANCE = new LogicBranch$$ExternalSyntheticLambda2();

    @Override // java.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        long j;
        j = ((LogicBranch.CloudItem) obj).id;
        return j;
    }
}
