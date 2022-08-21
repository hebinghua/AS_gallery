package com.miui.gallery.editor.photo.app.sky.sdk;

import android.os.Build;
import android.text.TextUtils;
import com.miui.gallery.net.library.LibraryStrategyUtils;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class SkyCheckHelper {
    public static SkyCheckHelper sInstance = new SkyCheckHelper();
    public static final String[] sSkyWhiteList = {"merlin", "merlinin", "merlinnfc", "monet", "monetin", "vangogh", "joyeuse", "excalibur", "durandal", "curtanacn", "curtana", "picassoin", "picasso", "lmi", "lmipro", "lmiin", "lmiinpro", "umi", "cmi", "dipper", "ursa", "jason", "wayne", "platina", "chiron", "sagit", "polaris", "perseus", "equuleus", "sirius", "comet", "lavender", "capricorn", "lithium", "natrium", "scorpio", "gemini", "cepheus", "davinci", "davinciin", "grus", "raphael", "raphaelin", "pyxis", "vela", "crux", "beryllium", "tucana", "violet", "phoenix", "phoenixin", "andromeda", "begonia", "begoniain", "draco", "toco", "tocoin", "lancelot", "atom", "bomb", "gram", "willow", "ginkgo", "laurus", "galahad", "apollo", "shiva", "cas", "apricot", "banana", "cezanne", "gauguin", "surya", "gauguinpro", "karna", "cannon", "cannong", "venus", "gauguininpro", "star", "cetus", "courbet", "courbetin", "sweet", "sweetin", "haydn", "alioth", "rosemary", "maltose", "secret", "mojito", "rainbow", "sunny", "mars", "renoir", "ares", "aresin", "thyme", "haydnin", "aliothin", "chopin", "vayu", "bhima", "biloba", "odin", "vili", "enuma", "elish", "nabu", "argo", "agate", "agatein", "selene", "eos", "amber", "lisa", "mona", "zeus", "cupid", "psyche", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "ingres", "poussin", "spes", "spesn", "munch", "rubens", "matisse", "thor", "loki", "zizhan", "zijin", "taoyao", "xaga", "xagain", "xagapro", "xagaproin", "mayfly", "unicorn", "plato", "daumier", "diting", "ice"};
    public static final String[] sSmallModelList = {"merlin", "merlinin", "merlinnfc", "lavender", "jason", "begonia", "begoniain", "atom", "bomb", "lancelot", "willow", "ginkgo", "laurus", "gemini", "lithium", "scorpio", "capricorn", "natrium", "galahad", "shiva", "apricot", "banana", "cezanne", "cannon", "cannong", "rosemary", "maltose", "secret", "ares", "aresin", "chopin", "biloba", "agate", "agatein", "selene", "eos", "amber", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "spes", "spesn", "rubens", "matisse", "xaga", "xagain", "xagapro", "xagaproin", "plato", "daumier", "ice"};
    public static final LazyValue<Void, Integer> SKY_MODEL_TYPE = new LazyValue<Void, Integer>() { // from class: com.miui.gallery.editor.photo.app.sky.sdk.SkyCheckHelper.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Integer mo1272onInit(Void r1) {
            return Integer.valueOf(SkyCheckHelper.getSkyType());
        }
    };

    public static boolean isSkyEnable() {
        return SKY_MODEL_TYPE.get(null).intValue() != 1;
    }

    public static int getSkyType() {
        boolean z;
        if (BuildUtil.isEditorProcess()) {
            return 1;
        }
        if (BuildUtil.isBlackShark()) {
            return 2;
        }
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            String[] strArr = sSkyWhiteList;
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = false;
                    break;
                } else if (strArr[i].equalsIgnoreCase(str)) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if (z) {
                for (String str2 : sSmallModelList) {
                    if (str2.equalsIgnoreCase(str)) {
                        return 3;
                    }
                }
                return 2;
            }
        }
        return 1;
    }

    public static long getLibraryId() {
        long j;
        LazyValue<Void, Integer> lazyValue = SKY_MODEL_TYPE;
        if (lazyValue.get(null).intValue() == 1) {
            j = Long.MIN_VALUE;
        } else {
            j = lazyValue.get(null).intValue() == 2 ? 9001019L : 9002019L;
        }
        DefaultLogger.d("SkyCheckHelper", "sky algorithm id %d", Long.valueOf(j));
        return j;
    }

    public static boolean isSupportTextYanhua() {
        return SKY_MODEL_TYPE.get(null).intValue() == 2 && !LibraryStrategyUtils.isMtk();
    }

    public static boolean isLargeType() {
        return SKY_MODEL_TYPE.get(null).intValue() == 2;
    }
}
