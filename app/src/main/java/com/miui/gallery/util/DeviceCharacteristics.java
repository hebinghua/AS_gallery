package com.miui.gallery.util;

import android.os.Build;
import android.text.TextUtils;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt__StringsJVMKt;

/* compiled from: DeviceCharacteristics.kt */
/* loaded from: classes2.dex */
public final class DeviceCharacteristics {
    public static final DeviceCharacteristics INSTANCE = new DeviceCharacteristics();
    public static final String[] MIDDLE_END_DEVICES = {"jason", "wayne", "platina", "chiron", "sagit", "sirius", "comet", "lavender", "capricorn", "lithium", "natrium", "scorpio", "gemini", "davinci", "davinciin", "grus", "pyxis", "vela", "begonia", "begoniain", "tucana", "ginkgo", "violet", "laurus", "phoenix", "phoenixin", "picasso", "picassoin", "joyeuse", "excalibur", "durandal", "curtanacn", "curtana", "monet", "monetin", "vangogh", "toco", "tocoin", "gram", "gauguin", "surya", "karna", "lime", "citrus", "lemon", "pomelo", "gauguininpro", "courbet", "courbetin", "sweet", "sweetin", "rosemary", "maltose", "secret", "mojito", "rainbow", "sunny", "renoir", "ares", "aresin", "thyme", "chopin", "camellia", "camellian", "vayu", "bhima", "biloba", "odin", "vili", "XIG02", "iris", "enuma", "elish", "nabu", "argo", "agate", "agatein", "selene", "eos", "amber", "lisa", "mona", "evergo", "evergreen", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "cezanne", "lilac", "A101XM", "angelicain", "fleur", "miel", "veux", "peux", "spes", "spesn", "viva", "vida", "munch", "rubens", "matisse", "ice"};
    public static final String[] HIGH_END_DEVICES = {"dipper", "ursa", "equuleus", "polaris", "perseus", "andromeda", "beryllium", "cepheus", "crux", "raphael", "raphaelin", "cmi", "umi", "draco", "lmi", "lmiin", "lmiinpro", "lmipro", "apollo", "cas", "venus", "cetus", "star", "mars", "haydn", "haydnin", "alioth", "aliothin", "zeus", "cupid", "psyche", "thor", "loki", "zizhan", "diting", "mayfly"};
    public static final Lazy isHighEndDevice$delegate = LazyKt__LazyJVMKt.lazy(DeviceCharacteristics$isHighEndDevice$2.INSTANCE);
    public static final Lazy isMiddleEndDevice$delegate = LazyKt__LazyJVMKt.lazy(DeviceCharacteristics$isMiddleEndDevice$2.INSTANCE);

    public static final boolean isNonLowEndDevice() {
        return isHighEndDevice() || isMiddleEndDevice();
    }

    public static final boolean isHighEndDevice() {
        return ((Boolean) isHighEndDevice$delegate.mo119getValue()).booleanValue();
    }

    public static final boolean isMiddleEndDevice() {
        return ((Boolean) isMiddleEndDevice$delegate.mo119getValue()).booleanValue();
    }

    public final boolean belongsTo(String[] strArr) {
        String deviceName = Build.DEVICE;
        if (!TextUtils.isEmpty(deviceName)) {
            Intrinsics.checkNotNullExpressionValue(deviceName, "deviceName");
            Object[] array = new Regex("_").split(deviceName, 0).toArray(new String[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            String[] strArr2 = (String[]) array;
            if (!(strArr2.length == 0)) {
                deviceName = strArr2[0];
            }
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String str = strArr[i];
                i++;
                if (StringsKt__StringsJVMKt.equals(str, deviceName, true)) {
                    return true;
                }
            }
        }
        return false;
    }
}
