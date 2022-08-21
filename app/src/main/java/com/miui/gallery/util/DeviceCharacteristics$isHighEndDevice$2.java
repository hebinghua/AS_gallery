package com.miui.gallery.util;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: DeviceCharacteristics.kt */
/* loaded from: classes2.dex */
public final class DeviceCharacteristics$isHighEndDevice$2 extends Lambda implements Function0<Boolean> {
    public static final DeviceCharacteristics$isHighEndDevice$2 INSTANCE = new DeviceCharacteristics$isHighEndDevice$2();

    public DeviceCharacteristics$isHighEndDevice$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final Boolean mo1738invoke() {
        boolean z;
        String[] strArr;
        boolean belongsTo;
        if (!BuildUtil.isBlackShark()) {
            DeviceCharacteristics deviceCharacteristics = DeviceCharacteristics.INSTANCE;
            strArr = DeviceCharacteristics.HIGH_END_DEVICES;
            belongsTo = deviceCharacteristics.belongsTo(strArr);
            if (!belongsTo) {
                z = false;
                return Boolean.valueOf(z);
            }
        }
        z = true;
        return Boolean.valueOf(z);
    }
}
