package com.miui.gallery.net.library;

import android.os.Build;
import com.miui.gallery.assistant.library.LibraryConstantsHelper;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: classes2.dex */
public class LibraryStrategyUtils {
    public static final String[] sMtkList = {"begonia", "begoniain", "merlin", "merlinin", "merlinnfc", "lancelot", "atom", "bomb", "galahad", "shiva", "apricot", "banana", "cezanne", "cannon", "cannong", "rosemary", "maltose", "secret", "ares", "aresin", "chopin", "camellia", "camellian", "biloba", "agate", "agatein", "selene", "eos", "amber", "evergo", "evergreen", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "angelicain", "fleur", "miel", "viva", "vida", "rubens", "matisse", "light", "thunder", "opal", "xaga", "xagain", "xagapro", "xagaproin", "plato", "daumier", "ice"};
    public static final String[] s8450 = {"zeus", "cupid", "ingres", "thor", "loki", "zizhan", "unicorn", "mayfly", "diting"};
    public static final String[] s8350 = {"cetus", "venus", "star", "mars", "haydn", "haydnin", "odin", "vili", "argo", "apollo", "cas", "cmi", "umi", "lmi", "munch", "psyche", "enuma", "elish", "thyme", "aliothin", "alioth"};
    public static final String[] s7350 = {"lisa", "mona", "renoir", "zijin"};

    public static String getKeyForLibraryId(long j) {
        if (j == 3414) {
            if (isMtk()) {
                return String.format(Locale.US, "%s_%s", "mtk", LibraryConstantsHelper.CURRENT_ABI);
            }
            if (is7350() || is8350()) {
                return String.format(Locale.US, "%s_%s", "7350", LibraryConstantsHelper.CURRENT_ABI);
            }
            if (is8450()) {
                return String.format(Locale.US, "%s_%s", "8450", LibraryConstantsHelper.CURRENT_ABI);
            }
        }
        if (j == 10286) {
            if (is7350() || is8350()) {
                return String.format(Locale.US, "%s_%s", "7350", LibraryConstantsHelper.CURRENT_ABI);
            }
            if (is8450()) {
                return String.format(Locale.US, "%s_%s", "8450", LibraryConstantsHelper.CURRENT_ABI);
            }
        }
        return (j != 20040004 || !is8450()) ? LibraryConstantsHelper.CURRENT_ABI : String.format(Locale.US, "%s_%s", "8450", LibraryConstantsHelper.CURRENT_ABI);
    }

    public static boolean isMtk() {
        return Arrays.asList(sMtkList).contains(Build.DEVICE);
    }

    public static boolean is8350() {
        return Arrays.asList(s8350).contains(Build.DEVICE);
    }

    public static boolean is7350() {
        return Arrays.asList(s7350).contains(Build.DEVICE);
    }

    public static boolean is8450() {
        return Arrays.asList(s8450).contains(Build.DEVICE);
    }
}
