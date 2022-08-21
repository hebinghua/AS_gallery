package com.google.common.util.concurrent;

/* loaded from: classes.dex */
public class ExecutionError extends Error {
    private static final long serialVersionUID = 0;

    public ExecutionError(Error error) {
        super(error);
    }
}
