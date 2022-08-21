package com.miui.gallery.widget.recyclerview;

import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.DiffUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CancellationException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;

/* compiled from: DiffUtil.kt */
@DebugMetadata(c = "com.miui.gallery.widget.recyclerview.DiffUtil$calculateDiff$2", f = "DiffUtil.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes3.dex */
public final class DiffUtil$calculateDiff$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super DiffUtil.DiffResult>, Object> {
    public final /* synthetic */ DiffUtil.Callback $cb;
    public final /* synthetic */ boolean $detectMoves;
    private /* synthetic */ Object L$0;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DiffUtil$calculateDiff$2(DiffUtil.Callback callback, boolean z, Continuation<? super DiffUtil$calculateDiff$2> continuation) {
        super(2, continuation);
        this.$cb = callback;
        this.$detectMoves = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        DiffUtil$calculateDiff$2 diffUtil$calculateDiff$2 = new DiffUtil$calculateDiff$2(this.$cb, this.$detectMoves, continuation);
        diffUtil$calculateDiff$2.L$0 = obj;
        return diffUtil$calculateDiff$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super DiffUtil.DiffResult> continuation) {
        return ((DiffUtil$calculateDiff$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Comparator comparator;
        DiffUtil.Snake diffPartial;
        ArrayList arrayList;
        DiffUtil.Range range;
        DiffUtil.Range range2;
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
            DefaultLogger.d("DiffUtil", "start calculateDiff");
            int oldListSize = this.$cb.getOldListSize();
            int newListSize = this.$cb.getNewListSize();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(new DiffUtil.Range(0, oldListSize, 0, newListSize));
            int abs = oldListSize + newListSize + Math.abs(oldListSize - newListSize);
            int i = abs * 2;
            int[] iArr = new int[i];
            int[] iArr2 = new int[i];
            ArrayList arrayList4 = new ArrayList();
            while (!arrayList3.isEmpty()) {
                if (!CoroutineScopeKt.isActive(coroutineScope)) {
                    DefaultLogger.i("DiffUtil", "cancel in calculateDiff");
                    throw new CancellationException();
                }
                DiffUtil.Range range3 = (DiffUtil.Range) arrayList3.remove(arrayList3.size() - 1);
                ArrayList arrayList5 = arrayList4;
                diffPartial = DiffUtil.INSTANCE.diffPartial(this.$cb, range3.getOldListStart(), range3.getOldListEnd(), range3.getNewListStart(), range3.getNewListEnd(), iArr, iArr2, abs, coroutineScope.getCoroutineContext());
                if (diffPartial != null) {
                    if (diffPartial.getSize() > 0) {
                        arrayList2.add(diffPartial);
                    }
                    diffPartial.setX(diffPartial.getX() + range3.getOldListStart());
                    diffPartial.setY(diffPartial.getY() + range3.getNewListStart());
                    if (arrayList5.isEmpty()) {
                        range = new DiffUtil.Range();
                        arrayList = arrayList5;
                    } else {
                        arrayList = arrayList5;
                        range = (DiffUtil.Range) arrayList.remove(arrayList5.size() - 1);
                    }
                    range.setOldListStart(range3.getOldListStart());
                    range.setNewListStart(range3.getNewListStart());
                    if (diffPartial.getReverse()) {
                        range.setOldListEnd(diffPartial.getX());
                        range.setNewListEnd(diffPartial.getY());
                    } else if (diffPartial.getRemoval()) {
                        range.setOldListEnd(diffPartial.getX() - 1);
                        range.setNewListEnd(diffPartial.getY());
                    } else {
                        range.setOldListEnd(diffPartial.getX());
                        range.setNewListEnd(diffPartial.getY() - 1);
                    }
                    arrayList3.add(range);
                    if (diffPartial.getReverse()) {
                        if (diffPartial.getRemoval()) {
                            range2 = range3;
                            range2.setOldListStart(diffPartial.getX() + diffPartial.getSize() + 1);
                            range2.setNewListStart(diffPartial.getY() + diffPartial.getSize());
                        } else {
                            range2 = range3;
                            range2.setOldListStart(diffPartial.getX() + diffPartial.getSize());
                            range2.setNewListStart(diffPartial.getY() + diffPartial.getSize() + 1);
                        }
                    } else {
                        range2 = range3;
                        range2.setOldListStart(diffPartial.getX() + diffPartial.getSize());
                        range2.setNewListStart(diffPartial.getY() + diffPartial.getSize());
                    }
                    arrayList3.add(range2);
                } else {
                    arrayList = arrayList5;
                    arrayList.add(range3);
                }
                arrayList4 = arrayList;
            }
            comparator = DiffUtil.SNAKE_COMPARATOR;
            Collections.sort(arrayList2, comparator);
            return new DiffUtil.DiffResult(this.$cb, arrayList2, iArr, iArr2, coroutineScope.getCoroutineContext(), this.$detectMoves);
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
