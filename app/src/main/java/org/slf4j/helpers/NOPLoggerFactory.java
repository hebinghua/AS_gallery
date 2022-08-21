package org.slf4j.helpers;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/* loaded from: classes3.dex */
public class NOPLoggerFactory implements ILoggerFactory {
    @Override // org.slf4j.ILoggerFactory
    /* renamed from: getLogger */
    public Logger mo166getLogger(String str) {
        return NOPLogger.NOP_LOGGER;
    }
}