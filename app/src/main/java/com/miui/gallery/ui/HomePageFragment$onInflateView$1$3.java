package com.miui.gallery.ui;

import androidx.lifecycle.LifecycleOwnerKt;
import com.miui.gallery.activity.HomePageStartupHelper2;
import com.miui.gallery.adapter.HomePageAdapter2;
import com.miui.gallery.adapter.IListAdapter;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$onInflateView$1$3 extends Lambda implements Function1<List<? extends IRecord>, Unit> {
    public final /* synthetic */ HomePageFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$onInflateView$1$3(HomePageFragment homePageFragment) {
        super(1);
        this.this$0 = homePageFragment;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(List<? extends IRecord> list) {
        invoke2(list);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(List<? extends IRecord> list) {
        if (this.this$0.mActivity == null) {
            return;
        }
        DefaultLogger.d("HomePageFragment", "Got preload data from start helper");
        HomePageStartupHelper2 homePageStartupHelper2 = this.this$0.mHomePageStartHelper;
        if (homePageStartupHelper2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mHomePageStartHelper");
            homePageStartupHelper2 = null;
        }
        homePageStartupHelper2.setDataLoaderListener(null);
        if (list != null) {
            BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this.this$0), null, null, new AnonymousClass1(this.this$0, list, null), 3, null);
        } else {
            this.this$0.onDataBind();
        }
    }

    /* compiled from: HomePageFragment.kt */
    @DebugMetadata(c = "com.miui.gallery.ui.HomePageFragment$onInflateView$1$3$1", f = "HomePageFragment.kt", l = {316}, m = "invokeSuspend")
    /* renamed from: com.miui.gallery.ui.HomePageFragment$onInflateView$1$3$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        public final /* synthetic */ List<IRecord> $data;
        public int label;
        public final /* synthetic */ HomePageFragment this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(HomePageFragment homePageFragment, List<? extends IRecord> list, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.this$0 = homePageFragment;
            this.$data = list;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.this$0, this.$data, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                HomePageAdapter2 homePageAdapter2 = this.this$0.mHomePageAdapter;
                if (homePageAdapter2 != null) {
                    List<IRecord> list = this.$data;
                    this.label = 1;
                    if (IListAdapter.submitList$default(homePageAdapter2, list, false, null, this, 4, null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
            } else if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            } else {
                ResultKt.throwOnFailure(obj);
            }
            this.this$0.onDataBind();
            return Unit.INSTANCE;
        }
    }
}
