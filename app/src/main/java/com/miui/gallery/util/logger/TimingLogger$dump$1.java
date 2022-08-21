package com.miui.gallery.util.logger;

import android.os.SystemClock;
import com.jakewharton.picnic.BorderStyle;
import com.jakewharton.picnic.CellDsl;
import com.jakewharton.picnic.CellStyleDsl;
import com.jakewharton.picnic.RowDsl;
import com.jakewharton.picnic.TableDsl;
import com.jakewharton.picnic.TableSectionDsl;
import com.jakewharton.picnic.TableStyleDsl;
import com.jakewharton.picnic.TextAlignment;
import java.util.ArrayList;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref$LongRef;

/* compiled from: TimingLogger.kt */
/* loaded from: classes2.dex */
public final class TimingLogger$dump$1 extends Lambda implements Function1<TableDsl, Unit> {
    public final /* synthetic */ TimingLogger this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TimingLogger$dump$1(TimingLogger timingLogger) {
        super(1);
        this.this$0 = timingLogger;
    }

    /* compiled from: TimingLogger.kt */
    /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends Lambda implements Function1<TableStyleDsl, Unit> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        public AnonymousClass1() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo2577invoke(TableStyleDsl tableStyleDsl) {
            invoke2(tableStyleDsl);
            return Unit.INSTANCE;
        }

        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final void invoke2(TableStyleDsl style) {
            Intrinsics.checkNotNullParameter(style, "$this$style");
            style.setBorderStyle(BorderStyle.Solid);
        }
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(TableDsl tableDsl) {
        invoke2(tableDsl);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(TableDsl table) {
        ArrayList arrayList;
        ArrayList arrayList2;
        Intrinsics.checkNotNullParameter(table, "$this$table");
        table.style(AnonymousClass1.INSTANCE);
        table.cellStyle(AnonymousClass2.INSTANCE);
        arrayList = this.this$0.mSplits;
        Object obj = arrayList.get(0);
        Intrinsics.checkNotNullExpressionValue(obj, "mSplits[0]");
        long longValue = ((Number) obj).longValue();
        Ref$LongRef ref$LongRef = new Ref$LongRef();
        arrayList2 = this.this$0.mSplits;
        ref$LongRef.element = arrayList2.size() > 1 ? longValue : SystemClock.elapsedRealtime();
        table.header(new AnonymousClass3(this.this$0));
        table.body(new AnonymousClass4(this.this$0, ref$LongRef));
        table.footer(new AnonymousClass5(ref$LongRef, longValue));
    }

    /* compiled from: TimingLogger.kt */
    /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass2 extends Lambda implements Function1<CellStyleDsl, Unit> {
        public static final AnonymousClass2 INSTANCE = new AnonymousClass2();

        public AnonymousClass2() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo2577invoke(CellStyleDsl cellStyleDsl) {
            invoke2(cellStyleDsl);
            return Unit.INSTANCE;
        }

        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final void invoke2(CellStyleDsl cellStyle) {
            Intrinsics.checkNotNullParameter(cellStyle, "$this$cellStyle");
            cellStyle.setAlignment(TextAlignment.MiddleRight);
            cellStyle.setPaddingLeft(1);
            cellStyle.setPaddingRight(1);
            Boolean bool = Boolean.TRUE;
            cellStyle.setBorderLeft(bool);
            cellStyle.setBorderRight(bool);
        }
    }

    /* compiled from: TimingLogger.kt */
    /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass3 extends Lambda implements Function1<TableSectionDsl, Unit> {
        public final /* synthetic */ TimingLogger this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass3(TimingLogger timingLogger) {
            super(1);
            this.this$0 = timingLogger;
        }

        /* compiled from: TimingLogger.kt */
        /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$3$1  reason: invalid class name */
        /* loaded from: classes2.dex */
        public static final class AnonymousClass1 extends Lambda implements Function1<CellStyleDsl, Unit> {
            public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

            public AnonymousClass1() {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            /* renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo2577invoke(CellStyleDsl cellStyleDsl) {
                invoke2(cellStyleDsl);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke  reason: avoid collision after fix types in other method */
            public final void invoke2(CellStyleDsl cellStyle) {
                Intrinsics.checkNotNullParameter(cellStyle, "$this$cellStyle");
                cellStyle.setBorder(true);
                cellStyle.setAlignment(TextAlignment.MiddleCenter);
            }
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo2577invoke(TableSectionDsl tableSectionDsl) {
            invoke2(tableSectionDsl);
            return Unit.INSTANCE;
        }

        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final void invoke2(TableSectionDsl header) {
            Intrinsics.checkNotNullParameter(header, "$this$header");
            header.cellStyle(AnonymousClass1.INSTANCE);
            header.row(new AnonymousClass2(this.this$0));
        }

        /* compiled from: TimingLogger.kt */
        /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$3$2  reason: invalid class name */
        /* loaded from: classes2.dex */
        public static final class AnonymousClass2 extends Lambda implements Function1<RowDsl, Unit> {
            public final /* synthetic */ TimingLogger this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public AnonymousClass2(TimingLogger timingLogger) {
                super(1);
                this.this$0 = timingLogger;
            }

            /* compiled from: TimingLogger.kt */
            /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$3$2$1  reason: invalid class name */
            /* loaded from: classes2.dex */
            public static final class AnonymousClass1 extends Lambda implements Function1<CellDsl, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                public AnonymousClass1() {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                /* renamed from: invoke */
                public /* bridge */ /* synthetic */ Unit mo2577invoke(CellDsl cellDsl) {
                    invoke2(cellDsl);
                    return Unit.INSTANCE;
                }

                /* renamed from: invoke  reason: avoid collision after fix types in other method */
                public final void invoke2(CellDsl cell) {
                    Intrinsics.checkNotNullParameter(cell, "$this$cell");
                    cell.setColumnSpan(2);
                }
            }

            @Override // kotlin.jvm.functions.Function1
            /* renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo2577invoke(RowDsl rowDsl) {
                invoke2(rowDsl);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke  reason: avoid collision after fix types in other method */
            public final void invoke2(RowDsl row) {
                String str;
                Intrinsics.checkNotNullParameter(row, "$this$row");
                str = this.this$0.mLabel;
                row.cell(str, AnonymousClass1.INSTANCE);
            }
        }
    }

    /* compiled from: TimingLogger.kt */
    /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass4 extends Lambda implements Function1<TableSectionDsl, Unit> {
        public final /* synthetic */ Ref$LongRef $now;
        public final /* synthetic */ TimingLogger this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass4(TimingLogger timingLogger, Ref$LongRef ref$LongRef) {
            super(1);
            this.this$0 = timingLogger;
            this.$now = ref$LongRef;
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo2577invoke(TableSectionDsl tableSectionDsl) {
            invoke2(tableSectionDsl);
            return Unit.INSTANCE;
        }

        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final void invoke2(TableSectionDsl body) {
            ArrayList arrayList;
            ArrayList arrayList2;
            ArrayList arrayList3;
            ArrayList arrayList4;
            Intrinsics.checkNotNullParameter(body, "$this$body");
            arrayList = this.this$0.mSplits;
            int size = arrayList.size();
            int i = 1;
            while (i < size) {
                int i2 = i + 1;
                Ref$LongRef ref$LongRef = this.$now;
                arrayList2 = this.this$0.mSplits;
                Object obj = arrayList2.get(i);
                Intrinsics.checkNotNullExpressionValue(obj, "mSplits[i]");
                ref$LongRef.element = ((Number) obj).longValue();
                arrayList3 = this.this$0.mSplitLabels;
                arrayList4 = this.this$0.mSplits;
                Object obj2 = arrayList4.get(i - 1);
                Intrinsics.checkNotNullExpressionValue(obj2, "mSplits[i - 1]");
                long longValue = ((Number) obj2).longValue();
                body.row((String) arrayList3.get(i), (this.$now.element - longValue) + " ms");
                i = i2;
            }
        }
    }

    /* compiled from: TimingLogger.kt */
    /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$5  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass5 extends Lambda implements Function1<TableSectionDsl, Unit> {
        public final /* synthetic */ long $first;
        public final /* synthetic */ Ref$LongRef $now;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass5(Ref$LongRef ref$LongRef, long j) {
            super(1);
            this.$now = ref$LongRef;
            this.$first = j;
        }

        /* compiled from: TimingLogger.kt */
        /* renamed from: com.miui.gallery.util.logger.TimingLogger$dump$1$5$1  reason: invalid class name */
        /* loaded from: classes2.dex */
        public static final class AnonymousClass1 extends Lambda implements Function1<CellStyleDsl, Unit> {
            public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

            public AnonymousClass1() {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            /* renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo2577invoke(CellStyleDsl cellStyleDsl) {
                invoke2(cellStyleDsl);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke  reason: avoid collision after fix types in other method */
            public final void invoke2(CellStyleDsl cellStyle) {
                Intrinsics.checkNotNullParameter(cellStyle, "$this$cellStyle");
                cellStyle.setBorder(true);
            }
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo2577invoke(TableSectionDsl tableSectionDsl) {
            invoke2(tableSectionDsl);
            return Unit.INSTANCE;
        }

        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final void invoke2(TableSectionDsl footer) {
            Intrinsics.checkNotNullParameter(footer, "$this$footer");
            footer.cellStyle(AnonymousClass1.INSTANCE);
            footer.row("total", (this.$now.element - this.$first) + " ms");
        }
    }
}
