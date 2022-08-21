package com.miui.gallery.signature.dialog.manage;

import java.io.File;
import java.io.FilenameFilter;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class SignatureManagerDialog$$ExternalSyntheticLambda5 implements FilenameFilter {
    public static final /* synthetic */ SignatureManagerDialog$$ExternalSyntheticLambda5 INSTANCE = new SignatureManagerDialog$$ExternalSyntheticLambda5();

    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        boolean startsWith;
        startsWith = str.startsWith("signature_file");
        return startsWith;
    }
}
