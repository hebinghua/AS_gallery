package com.miui.gallery.util;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: DeviceCharacteristics.kt */
/* loaded from: classes2.dex */
public final class DeviceCharacteristics$isMiddleEndDevice$2 extends Lambda implements Function0<Boolean> {
    public static final DeviceCharacteristics$isMiddleEndDevice$2 INSTANCE = new DeviceCharacteristics$isMiddleEndDevice$2();

    public DeviceCharacteristics$isMiddleEndDevice$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final Boolean mo1738invoke() {
        String[] strArr;
        boolean belongsTo;
        DeviceCharacteristics deviceCharacteristics = DeviceCharacteristics.INSTANCE;
        strArr = DeviceCharacteristics.MIDDLE_END_DEVICES;
        belongsTo = deviceCharacteristics.belongsTo(strArr);
        return Boolean.valueOf(belongsTo);
    }
}
