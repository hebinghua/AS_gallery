package com.miui.gallery.util;

import android.os.Build;
import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class IDPhotoEntranceUtils {
    public static final String[] sHighLevelList = {"venus", "star", "mars", "haydn", "haydnin", "odin", "lisa", "mona", "zeus", "cupid", "psyche", "enuma", "elish", "nabu", "ingres", "poussin", "munch", "rubens", "matisse", "renoir", "thor", "loki", "zizhan", "zijin", "chopin", "ares", "apollo", "thyme", "umi", "cas", "lmipro", "lmi", "cezanne", "cmi", "cetus", "xaga", "xagapro", "crux", "raphael", "cepheus", "ursa", "andromeda", "perseus", "equuleus", "dipper", "polaris", "mayfly", "unicorn", "plato", "daumier", "diting"};
    public static final String[] sLowLevelList = {"veux", "mojito", "rainbow", "sunny", "sweet", "vangogh"};
    public static final LazyValue<Void, Integer> ID_MODEL_TYPE = new LazyValue<Void, Integer>() { // from class: com.miui.gallery.util.IDPhotoEntranceUtils.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Integer mo1272onInit(Void r1) {
            return Integer.valueOf(IDPhotoEntranceUtils.getIdType());
        }
    };

    public static int getIdType() {
        if (!BuildUtil.isEditorProcess() && !BaseBuildUtil.isInternational()) {
            String str = Build.DEVICE;
            if (!TextUtils.isEmpty(str)) {
                for (String str2 : sHighLevelList) {
                    if (str2.equalsIgnoreCase(str)) {
                        return 2;
                    }
                }
                for (String str3 : sLowLevelList) {
                    if (str3.equalsIgnoreCase(str)) {
                        return 3;
                    }
                }
            }
            return 1;
        }
        return 1;
    }

    public static boolean isAvailable() {
        return ID_MODEL_TYPE.get(null).intValue() != 1;
    }

    public static long getLibraryId() {
        int intValue = ID_MODEL_TYPE.get(null).intValue();
        long j = intValue != 2 ? intValue != 3 ? Long.MIN_VALUE : 20021005L : 20020005L;
        DefaultLogger.d("IDPhotoEntranceUtils", "IdPhoto Algorithm Id %d", Long.valueOf(j));
        return j;
    }
}
