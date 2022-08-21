package com.miui.gallery.ui;

import androidx.tracing.Trace;
import com.miui.gallery.adapter.HomePageAdapter2;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.provider.cache.IRecord;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: HomePageFragment.kt */
@DebugMetadata(c = "com.miui.gallery.ui.HomePageFragment$onStartup$3$1", f = "HomePageFragment.kt", l = {574}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class HomePageFragment$onStartup$3$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ List<IRecord> $it;
    public int label;
    public final /* synthetic */ HomePageFragment this$0;

    /* renamed from: $r8$lambda$WWc_CBL1UwMUe7YYfC3FBrXTW-Y */
    public static /* synthetic */ void m1495$r8$lambda$WWc_CBL1UwMUe7YYfC3FBrXTWY(HomePageFragment homePageFragment) {
        m1496invokeSuspend$lambda0(homePageFragment);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public HomePageFragment$onStartup$3$1(List<? extends IRecord> list, HomePageFragment homePageFragment, Continuation<? super HomePageFragment$onStartup$3$1> continuation) {
        super(2, continuation);
        this.$it = list;
        this.this$0 = homePageFragment;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new HomePageFragment$onStartup$3$1(this.$it, this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((HomePageFragment$onStartup$3$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        boolean z;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Trace.beginSection("HomePageFrag#submitList");
            List<IRecord> list = this.$it;
            int clusterKey = this.this$0.mViewMode.getClusterKey();
            boolean z2 = false;
            if (list instanceof ClusteredList) {
                ((ClusteredList) list).select(clusterKey, false);
            }
            HomePageAdapter2 homePageAdapter2 = this.this$0.mHomePageAdapter;
            if (homePageAdapter2 != null) {
                List<IRecord> list2 = this.$it;
                z = this.this$0.mDiffEnable;
                if (z && !this.this$0.mViewMode.isAggregated()) {
                    z2 = true;
                }
                final HomePageFragment homePageFragment = this.this$0;
                Runnable runnable = new Runnable() { // from class: com.miui.gallery.ui.HomePageFragment$onStartup$3$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        HomePageFragment$onStartup$3$1.m1495$r8$lambda$WWc_CBL1UwMUe7YYfC3FBrXTWY(HomePageFragment.this);
                    }
                };
                this.label = 1;
                if (homePageAdapter2.submitList(list2, z2, runnable, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            ResultKt.throwOnFailure(obj);
        }
        Trace.endSection();
        return Unit.INSTANCE;
    }

    /* renamed from: invokeSuspend$lambda-0 */
    public static final void m1496invokeSuspend$lambda0(HomePageFragment homePageFragment) {
        if (!homePageFragment.isDetached()) {
            Trace.beginSection("HomePageFrag#updateAfterLoadFinished");
            homePageFragment.mTagProportionChanged = true;
            HomePageAdapter2 homePageAdapter2 = homePageFragment.mHomePageAdapter;
            Intrinsics.checkNotNull(homePageAdapter2);
            homePageFragment.updateAfterLoadFinished(homePageAdapter2.getCurrentList());
            Trace.endSection();
        }
    }
}
