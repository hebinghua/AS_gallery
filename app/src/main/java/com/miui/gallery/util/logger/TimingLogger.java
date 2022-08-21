package com.miui.gallery.util.logger;

import android.os.SystemClock;
import android.util.Printer;
import com.jakewharton.picnic.DslKt;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: TimingLogger.kt */
/* loaded from: classes2.dex */
public final class TimingLogger {
    public String mLabel;
    public String mTag;
    public final ArrayList<Long> mSplits = new ArrayList<>();
    public final ArrayList<String> mSplitLabels = new ArrayList<>();

    public TimingLogger(String str, String str2) {
        reset(str, str2);
    }

    public final void reset(String str, String str2) {
        this.mTag = str;
        this.mLabel = str2;
        reset();
    }

    public final void reset() {
        this.mSplits.clear();
        this.mSplitLabels.clear();
        addSplit(null);
    }

    public final void addSplit(String str) {
        this.mSplits.add(Long.valueOf(SystemClock.elapsedRealtime()));
        this.mSplitLabels.add(str);
    }

    public final long dumpToLog() {
        DefaultLogger.d(this.mTag, dump());
        return cost();
    }

    public final long dump(Printer printer) {
        if (printer == null) {
            return dumpToLog();
        }
        printer.println(dump());
        return cost();
    }

    public final long cost() {
        ArrayList<Long> arrayList = this.mSplits;
        long longValue = arrayList.get(arrayList.size() - 1).longValue();
        Long l = this.mSplits.get(0);
        Intrinsics.checkNotNullExpressionValue(l, "mSplits[0]");
        return longValue - l.longValue();
    }

    public final String dump() {
        return Intrinsics.stringPlus("\n", DslKt.table(new TimingLogger$dump$1(this)));
    }
}
