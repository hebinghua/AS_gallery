package com.jakewharton.picnic;

import com.jakewharton.picnic.Cell;
import com.jakewharton.picnic.CellStyle;
import com.jakewharton.picnic.Table;
import com.jakewharton.picnic.TableStyle;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: model.kt */
/* loaded from: classes.dex */
public final class ModelKt {
    public static final /* synthetic */ CellStyle access$plus(CellStyle cellStyle, CellStyle cellStyle2) {
        return plus(cellStyle, cellStyle2);
    }

    public static final /* synthetic */ Table Table(Function1<? super Table.Builder, Unit> initializer) {
        Intrinsics.checkNotNullParameter(initializer, "initializer");
        Table.Builder builder = new Table.Builder();
        initializer.mo2577invoke(builder);
        return builder.build();
    }

    public static final /* synthetic */ TableStyle TableStyle(Function1<? super TableStyle.Builder, Unit> initializer) {
        Intrinsics.checkNotNullParameter(initializer, "initializer");
        TableStyle.Builder builder = new TableStyle.Builder();
        initializer.mo2577invoke(builder);
        return builder.build();
    }

    public static final /* synthetic */ Cell Cell(Object obj, Function1<? super Cell.Builder, Unit> initializer) {
        Intrinsics.checkNotNullParameter(initializer, "initializer");
        Cell.Builder builder = new Cell.Builder(obj);
        initializer.mo2577invoke(builder);
        return builder.build();
    }

    public static final /* synthetic */ CellStyle CellStyle(Function1<? super CellStyle.Builder, Unit> initializer) {
        Intrinsics.checkNotNullParameter(initializer, "initializer");
        CellStyle.Builder builder = new CellStyle.Builder();
        initializer.mo2577invoke(builder);
        return builder.build();
    }

    public static final CellStyle plus(CellStyle cellStyle, CellStyle cellStyle2) {
        return cellStyle == null ? cellStyle2 : cellStyle2 == null ? cellStyle : CellStyle(new ModelKt$plus$1(cellStyle, cellStyle2));
    }
}
