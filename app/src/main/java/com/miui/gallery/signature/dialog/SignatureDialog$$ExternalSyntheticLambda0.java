package com.miui.gallery.signature.dialog;

import android.content.DialogInterface;
import com.miui.gallery.stat.SamplingStatHelper;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class SignatureDialog$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public static final /* synthetic */ SignatureDialog$$ExternalSyntheticLambda0 INSTANCE = new SignatureDialog$$ExternalSyntheticLambda0();

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        SamplingStatHelper.recordCountEvent("photo_editor", "signature_cancel_click");
    }
}
