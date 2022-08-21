package com.jakewharton.picnic;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public interface RowDsl {
    void cell(Object obj, Function1<? super CellDsl, Unit> function1);

    /* compiled from: dsl.kt */
    /* loaded from: classes.dex */
    public static final class DefaultImpls {
        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void cell$default(RowDsl rowDsl, Object obj, Function1 function1, int i, Object obj2) {
            if (obj2 == null) {
                if ((i & 2) != 0) {
                    function1 = RowDsl$cell$1.INSTANCE;
                }
                rowDsl.cell(obj, function1);
                return;
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: cell");
        }
    }
}
