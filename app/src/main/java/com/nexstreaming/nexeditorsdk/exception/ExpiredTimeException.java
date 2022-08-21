package com.nexstreaming.nexeditorsdk.exception;

/* loaded from: classes3.dex */
public class ExpiredTimeException extends Exception {
    private static final long serialVersionUID = 1;

    public ExpiredTimeException() {
        super("Asset has expired.");
    }

    public ExpiredTimeException(String str) {
        super(str + " has expired.");
    }
}
