package com.miui.gallery.ui.recentdiscovery;

import com.miui.gallery.adapter.IListAdapter;
import com.miui.gallery.adapter.RecentDiscoveryAdapter2;
import com.miui.gallery.provider.cache.IRecord;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: RecentDiscoveryFragment.kt */
@DebugMetadata(c = "com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment$onActivityCreated$1$1", f = "RecentDiscoveryFragment.kt", l = {182}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class RecentDiscoveryFragment$onActivityCreated$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ List<IRecord> $it;
    public int label;
    public final /* synthetic */ RecentDiscoveryFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public RecentDiscoveryFragment$onActivityCreated$1$1(RecentDiscoveryFragment recentDiscoveryFragment, List<? extends IRecord> list, Continuation<? super RecentDiscoveryFragment$onActivityCreated$1$1> continuation) {
        super(2, continuation);
        this.this$0 = recentDiscoveryFragment;
        this.$it = list;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new RecentDiscoveryFragment$onActivityCreated$1$1(this.this$0, this.$it, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((RecentDiscoveryFragment$onActivityCreated$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            RecentDiscoveryAdapter2 adapter = this.this$0.getAdapter();
            List<IRecord> list = this.$it;
            this.label = 1;
            if (IListAdapter.submitList$default(adapter, list, false, null, this, 6, null) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            ResultKt.throwOnFailure(obj);
        }
        return Unit.INSTANCE;
    }
}
