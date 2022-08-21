package com.nexstreaming.nexeditorsdk.module;

import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes3.dex */
public interface nexExternalExportMethod {
    boolean OnEnd(int i);

    int OnLastProcess();

    boolean OnPrepare(nexExportFormat nexexportformat);

    boolean OnPushData(int i, byte[] bArr);
}
