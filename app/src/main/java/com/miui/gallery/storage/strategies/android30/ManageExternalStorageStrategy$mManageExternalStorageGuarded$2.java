package com.miui.gallery.storage.strategies.android30;

import android.os.Environment;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ManageExternalStorageStrategy.kt */
/* loaded from: classes2.dex */
public final class ManageExternalStorageStrategy$mManageExternalStorageGuarded$2 extends Lambda implements Function0<Boolean> {
    public static final ManageExternalStorageStrategy$mManageExternalStorageGuarded$2 INSTANCE = new ManageExternalStorageStrategy$mManageExternalStorageGuarded$2();

    public ManageExternalStorageStrategy$mManageExternalStorageGuarded$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final Boolean mo1738invoke() {
        return Boolean.valueOf(Environment.isExternalStorageManager());
    }
}
