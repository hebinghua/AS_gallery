package com.xiaomi.push;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: classes3.dex */
public class gh extends Exception {
    private gq a;

    /* renamed from: a  reason: collision with other field name */
    private gr f412a;

    /* renamed from: a  reason: collision with other field name */
    private Throwable f413a;

    public gh() {
        this.a = null;
        this.f412a = null;
        this.f413a = null;
    }

    public gh(gq gqVar) {
        this.a = null;
        this.f412a = null;
        this.f413a = null;
        this.a = gqVar;
    }

    public gh(String str) {
        super(str);
        this.a = null;
        this.f412a = null;
        this.f413a = null;
    }

    public gh(String str, Throwable th) {
        super(str);
        this.a = null;
        this.f412a = null;
        this.f413a = null;
        this.f413a = th;
    }

    public gh(Throwable th) {
        this.a = null;
        this.f412a = null;
        this.f413a = null;
        this.f413a = th;
    }

    public Throwable a() {
        return this.f413a;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        gq gqVar;
        gr grVar;
        String message = super.getMessage();
        return (message != null || (grVar = this.f412a) == null) ? (message != null || (gqVar = this.a) == null) ? message : gqVar.toString() : grVar.toString();
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        if (this.f413a != null) {
            printStream.println("Nested Exception: ");
            this.f413a.printStackTrace(printStream);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        if (this.f413a != null) {
            printWriter.println("Nested Exception: ");
            this.f413a.printStackTrace(printWriter);
        }
    }

    @Override // java.lang.Throwable
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String message = super.getMessage();
        if (message != null) {
            sb.append(message);
            sb.append(": ");
        }
        gr grVar = this.f412a;
        if (grVar != null) {
            sb.append(grVar);
        }
        gq gqVar = this.a;
        if (gqVar != null) {
            sb.append(gqVar);
        }
        if (this.f413a != null) {
            sb.append("\n  -- caused by: ");
            sb.append(this.f413a);
        }
        return sb.toString();
    }
}
