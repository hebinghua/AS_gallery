package com.miui.gallery.vlog;

import android.os.Build;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class VlogEntranceUtils {
    public static ArrayList<String> mAllAvailableDeviceList;
    public static boolean sLoaded;
    public static boolean sLowEndLoaded;
    public static final String[] sLowEndWhiteList;
    public static boolean sSuperLowLoaded;
    public static final String[] sSuperLowWhiteList = {"lilac", "A101XM"};
    public static final String[] sWhiteList;

    static {
        String[] strArr = {"cmi", "umi", "lmi", "lmipro", "lmiin", "lmiinpro", "polaris", "dipper", "equuleus", "perseus", "ursa", "andromeda", "cepheus", "crux", "raphael", "raphaelin", "beryllium", "vangogh", "andromeda", "beryllium", "sagit", "chiron", "monet", "monetin", "picasso", "picassoin", "phoenix", "phoenixin", "davinci", "davinciin", "tucana", "toco", "tocoin", "cas", "apollo", "curtana", "durandal", "excalibur", "joyeuse", "gram", "gauguin", "surya", "gauguinpro", "karna", "venus", "gauguininpro", "cezanne", "cannon", "cannong", "apricot", "banana", "atom", "bomb", "star", "mars", "sweet", "sweetin", "mojito", "rainbow", "sunny", "secret", "rosemary", "maltose", "renoir", "courbet", "courbetin", "ares", "aresin", "chopin", "haydn", "haydnin", "alioth", "aliothin", "camellia", "camellian", "thyme", "cetus", "vayu", "bhima", "biloba", "odin", "vili", "XIG02", "iris", "enuma", "elish", "nabu", "argo", "agate", "agatein", "selene", "eos", "amber", "lisa", "mona", "evergo", "evergreen", "zeus", "cupid", "psyche", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "ingres", "poussin", "fleur", "miel", "veux", "peux", "spes", "spesn", "viva", "vida", "munch", "rubens", "matisse", "fog", "thor", "loki", "light", "thunder", "zizhan", "zijin", "taoyao", "opal", "xaga", "xagain", "xagapro", "xagaproin", "wind", "rain", "mayfly", "unicorn", "plato", "daumier", "diting"};
        sWhiteList = strArr;
        String[] strArr2 = {"merlin", "merlinnfc", "lancelot", "galahad", "shiva", "dandelion", "angelica", "angelicain", "cattail", "angelican", "citrus", "lime", "lemon", "pomelo", "frost", "ice"};
        sLowEndWhiteList = strArr2;
        ArrayList<String> arrayList = new ArrayList<>();
        mAllAvailableDeviceList = arrayList;
        arrayList.clear();
        mAllAvailableDeviceList.addAll(Arrays.asList(strArr));
        mAllAvailableDeviceList.addAll(Arrays.asList(strArr2));
        sLoaded = false;
        sLowEndLoaded = false;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            if (Build.DEVICE.equals(strArr[i])) {
                sLoaded = true;
                break;
            }
            i++;
        }
        if (!sLoaded) {
            String[] strArr3 = sLowEndWhiteList;
            int length2 = strArr3.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length2) {
                    break;
                }
                if (Build.DEVICE.equals(strArr3[i2])) {
                    sLoaded = true;
                    sLowEndLoaded = true;
                    break;
                }
                i2++;
            }
        }
        if (!sLoaded) {
            for (String str : sSuperLowWhiteList) {
                if (Build.DEVICE.equals(str)) {
                    sLoaded = true;
                    sSuperLowLoaded = true;
                    return;
                }
            }
        }
    }

    public static boolean isAvailable() {
        return sLoaded && !VlogUtils.isBlackShark();
    }

    public static boolean isSuperLowLoaded() {
        return sSuperLowLoaded;
    }
}
