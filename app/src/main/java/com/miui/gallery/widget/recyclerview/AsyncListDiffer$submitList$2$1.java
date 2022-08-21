package com.miui.gallery.widget.recyclerview;

import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.DiffUtil;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: AsyncListDiffer.kt */
@DebugMetadata(c = "com.miui.gallery.widget.recyclerview.AsyncListDiffer$submitList$2$1", f = "AsyncListDiffer.kt", l = {145}, m = "invokeSuspend")
/* loaded from: classes3.dex */
public final class AsyncListDiffer$submitList$2$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super DiffUtil.DiffResult>, Object> {
    public final /* synthetic */ List<T> $newList;
    public final /* synthetic */ List<T> $oldList;
    public final /* synthetic */ int $runGeneration;
    public final /* synthetic */ AsyncListDiffer<T> $this_runCatching;
    public long J$0;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public AsyncListDiffer$submitList$2$1(AsyncListDiffer<T> asyncListDiffer, List<? extends T> list, List<? extends T> list2, int i, Continuation<? super AsyncListDiffer$submitList$2$1> continuation) {
        super(2, continuation);
        this.$this_runCatching = asyncListDiffer;
        this.$oldList = list;
        this.$newList = list2;
        this.$runGeneration = i;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AsyncListDiffer$submitList$2$1(this.$this_runCatching, this.$oldList, this.$newList, this.$runGeneration, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super DiffUtil.DiffResult> continuation) {
        return ((AsyncListDiffer$submitList$2$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        long j;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            long currentTimeMillis = System.currentTimeMillis();
            DiffUtil diffUtil = DiffUtil.INSTANCE;
            final List<T> list = this.$oldList;
            final List<T> list2 = this.$newList;
            final AsyncListDiffer<T> asyncListDiffer = this.$this_runCatching;
            DiffUtil.Callback callback = new DiffUtil.Callback() { // from class: com.miui.gallery.widget.recyclerview.AsyncListDiffer$submitList$2$1$result$1
                @Override // com.miui.gallery.widget.recyclerview.DiffUtil.Callback
                public int getOldListSize() {
                    return list.size();
                }

                @Override // com.miui.gallery.widget.recyclerview.DiffUtil.Callback
                public int getNewListSize() {
                    return list2.size();
                }

                @Override // com.miui.gallery.widget.recyclerview.DiffUtil.Callback
                public boolean areItemsTheSame(int i2, int i3) {
                    Object obj2 = list.get(i2);
                    Object obj3 = list2.get(i3);
                    if (obj2 == null || obj3 == null) {
                        return obj2 == null && obj3 == null;
                    }
                    return asyncListDiffer.getConfig().getDiffCallback().areItemsTheSame(obj2, obj3);
                }

                @Override // com.miui.gallery.widget.recyclerview.DiffUtil.Callback
                public boolean areContentsTheSame(int i2, int i3) {
                    Object obj2 = list.get(i2);
                    Object obj3 = list2.get(i3);
                    if (obj2 == null || obj3 == null) {
                        if (obj2 != null || obj3 != null) {
                            throw new AssertionError();
                        }
                        return true;
                    }
                    return asyncListDiffer.getConfig().getDiffCallback().areContentsTheSame(obj2, obj3);
                }

                @Override // com.miui.gallery.widget.recyclerview.DiffUtil.Callback
                public Object getChangePayload(int i2, int i3) {
                    Object obj2 = list.get(i2);
                    Object obj3 = list2.get(i3);
                    if (obj2 != null && obj3 != null) {
                        return asyncListDiffer.getConfig().getDiffCallback().getChangePayload(obj2, obj3);
                    }
                    throw new AssertionError();
                }
            };
            CoroutineDispatcher backgroundDispatcher = this.$this_runCatching.getConfig().getBackgroundDispatcher();
            this.J$0 = currentTimeMillis;
            this.label = 1;
            obj = DiffUtil.calculateDiff$default(diffUtil, callback, backgroundDispatcher, false, this, 4, null);
            if (obj == coroutine_suspended) {
                return coroutine_suspended;
            }
            j = currentTimeMillis;
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            j = this.J$0;
            ResultKt.throwOnFailure(obj);
        }
        DiffUtil.DiffResult diffResult = (DiffUtil.DiffResult) obj;
        DefaultLogger.d("AsyncListDiffer", "Calc diff costs [" + (System.currentTimeMillis() - j) + "] ms, size: " + this.$oldList.size() + " -> " + this.$newList.size() + ", generation: " + this.$runGeneration);
        return diffResult;
    }
}
