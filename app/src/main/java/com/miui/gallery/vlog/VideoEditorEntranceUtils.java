package com.miui.gallery.vlog;

import android.os.Build;
import com.miui.gallery.util.BaseBuildUtil;

/* loaded from: classes2.dex */
public class VideoEditorEntranceUtils {
    public static boolean sLoaded;
    public static final String[] sWhiteList;

    static {
        String[] strArr = {"vangogh", "toco", "tocoin", "cas", "apollo", "curtana", "durandal", "excalibur", "joyeuse", "gram", "gauguin", "surya", "gauguinpro", "karna", "lime", "citrus", "lemon", "pomelo", "venus", "gauguininpro", "star", "cetus", "courbet", "courbetin", "sweet", "sweetin", "haydn", "alioth", "mojito", "rainbow", "sunny", "mars", "renoir", "thyme", "vayu", "bhima", "odin", "vili", "XIG02", "iris", "enuma", "elish", "nabu", "argo", "lisa", "mona", "zeus", "cupid", "psyche", "merlin", "merlinnfc", "atom", "bomb", "lancelot", "galahad", "shiva", "dandelion", "angelica", "angelicain", "cattail", "angelican", "apricot", "banana", "cezanne", "cannon", "cannong", "rosemary", "maltose", "secret", "ares", "aresin", "haydnin", "aliothin", "chopin", "camellia", "camellian", "biloba", "agate", "agatein", "selene", "eos", "amber", "evergo", "evergreen", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "lilac", "A101XM", "ingres", "poussin", "fleur", "miel", "veux", "peux", "spes", "spesn", "viva", "vida", "munch", "rubens", "matisse", "fog", "thor", "loki", "light", "thunder", "zizhan", "zijin", "taoyao", "opal", "frost", "xaga", "xagain", "xagapro", "xagaproin", "wind", "rain", "mayfly", "unicorn", "plato", "daumier", "diting", "ice"};
        sWhiteList = strArr;
        sLoaded = false;
        if (BaseBuildUtil.isBlackShark()) {
            sLoaded = true;
            return;
        }
        for (String str : strArr) {
            if (Build.DEVICE.equals(str)) {
                sLoaded = true;
                return;
            }
        }
    }

    public static boolean isAvailable() {
        return sLoaded;
    }
}
