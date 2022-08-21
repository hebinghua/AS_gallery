package org.sqlite.database;

import android.util.Printer;

/* loaded from: classes3.dex */
public class PrefixPrinter implements Printer {
    public final String mPrefix;
    public final Printer mPrinter;

    public static Printer create(Printer printer, String str) {
        return (str == null || str.equals("")) ? printer : new PrefixPrinter(printer, str);
    }

    public PrefixPrinter(Printer printer, String str) {
        this.mPrinter = printer;
        this.mPrefix = str;
    }

    @Override // android.util.Printer
    public void println(String str) {
        Printer printer = this.mPrinter;
        printer.println(this.mPrefix + str);
    }
}
