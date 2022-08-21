package com.miui.gallery.ui;

import ch.qos.logback.core.net.SyslogConstants;
import com.miui.gallery.adapter.AlbumDetailAdapter2;
import com.miui.gallery.adapter.SortBy;
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

/* compiled from: ModernAlbumDetailFragment.kt */
@DebugMetadata(c = "com.miui.gallery.ui.ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1", f = "ModernAlbumDetailFragment.kt", l = {SyslogConstants.LOG_LOCAL7}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ AlbumDetailAdapter2 $adapter;
    public final /* synthetic */ List<IRecord> $t;
    public int label;
    public final /* synthetic */ ModernAlbumDetailFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1(AlbumDetailAdapter2 albumDetailAdapter2, List<? extends IRecord> list, ModernAlbumDetailFragment modernAlbumDetailFragment, Continuation<? super ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1> continuation) {
        super(2, continuation);
        this.$adapter = albumDetailAdapter2;
        this.$t = list;
        this.this$0 = modernAlbumDetailFragment;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1(this.$adapter, this.$t, this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final AlbumDetailAdapter2 albumDetailAdapter2 = this.$adapter;
            final ModernAlbumDetailFragment modernAlbumDetailFragment = this.this$0;
            Runnable runnable = new Runnable() { // from class: com.miui.gallery.ui.ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1.m1520invokeSuspend$lambda0(AlbumDetailAdapter2.this, modernAlbumDetailFragment);
                }
            };
            this.label = 1;
            if (this.$adapter.submitList(this.$t, !this.this$0.mViewMode.isAggregated(), runnable, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            ResultKt.throwOnFailure(obj);
        }
        return Unit.INSTANCE;
    }

    /* renamed from: invokeSuspend$lambda-0  reason: not valid java name */
    public static final void m1520invokeSuspend$lambda0(AlbumDetailAdapter2 albumDetailAdapter2, ModernAlbumDetailFragment modernAlbumDetailFragment) {
        SortBy lastSortBy = albumDetailAdapter2.getLastSortBy();
        SortBy sortBy = SortBy.SIZE;
        if ((lastSortBy == sortBy && modernAlbumDetailFragment.mViewModel.getSortBy() != sortBy) || (albumDetailAdapter2.getLastSortBy() != sortBy && modernAlbumDetailFragment.mViewModel.getSortBy() == sortBy)) {
            albumDetailAdapter2.notifyDataChanged();
        }
        albumDetailAdapter2.setLastSortBy(modernAlbumDetailFragment.mViewModel.getSortBy());
    }
}
