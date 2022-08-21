package com.miui.gallery.video.timeburst;

import android.os.Build;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class TimeBurstVideoChecker {
    public static final String[] SUPPORTED_DEVICES;
    public static boolean sIsDeviceSupported;

    static {
        String[] strArr = {"merlin", "merlinin", "merlinnfc", "monet", "monetin", "vangogh", "joyeuse", "excalibur", "durandal", "curtanacn", "curtana", "picassoin", "picasso", "lmi", "lmipro", "lmiin", "lmiinpro", "umi", "cmi", "dipper", "ursa", "jason", "wayne", "platina", "chiron", "sagit", "polaris", "perseus", "equuleus", "sirius", "comet", "lavender", "capricorn", "lithium", "natrium", "scorpio", "gemini", "cepheus", "davinci", "davinciin", "grus", "raphael", "raphaelin", "pyxis", "vela", "crux", "beryllium", "tucana", "violet", "phoenix", "phoenixin", "andromeda", "begonia", "begoniain", "draco", "toco", "tocoin", "lancelot", "atom", "bomb", "gram", "willow", "ginkgo", "laurus", "galahad", "apollo", "shiva", "cas", "apricot", "banana", "cezanne", "gauguin", "surya", "gauguinpro", "karna", "cannon", "cannong", "lime", "citrus", "lemon", "pomelo", "venus", "gauguininpro", "star", "cetus", "courbet", "courbetin", "sweet", "sweetin", "haydn", "alioth", "rosemary", "maltose", "secret", "mojito", "rainbow", "sunny", "mars", "renoir", "ares", "aresin", "thyme", "haydnin", "aliothin", "chopin", "camellia", "camellian", "vayu", "bhima", "biloba", "odin", "vili", "XIG02", "iris", "enuma", "elish", "nabu", "argo", "agate", "agatein", "amber", "lisa", "mona", "evergo", "evergreen", "zeus", "cupid", "psyche", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "lilac", "A101XM", "selene", "angelicain", "ingres", "poussin", "fleur", "miel", "veux", "peux", "spes", "spesn", "viva", "vida", "munch", "rubens", "matisse", "fog", "thor", "loki", "light", "thunder", "zizhan", "zijin", "taoyao", "opal", "xaga", "xagain", "xagapro", "xagaproin", "wind", "rain", "mayfly", "unicorn", "plato", "daumier", "diting", "ice"};
        SUPPORTED_DEVICES = strArr;
        sIsDeviceSupported = false;
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : strArr) {
                if (str2.equalsIgnoreCase(str)) {
                    sIsDeviceSupported = true;
                    return;
                }
            }
        }
    }

    public static boolean isDeviceSupport() {
        return sIsDeviceSupported;
    }
}
