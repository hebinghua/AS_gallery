package com.miui.gallery.movie;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import java.io.File;
import miuix.core.util.FileUtils;

/* loaded from: classes2.dex */
public class MovieConfig {
    public static String sAudioDir = null;
    public static float sHeightToWidth = 0.0f;
    public static boolean sInited = false;
    public static boolean sIsLowDevice = false;
    public static final String[] sLowDeviceList = {"veux", "peux", "viva", "vida", "spes", "spesn"};
    public static final String[] sSupportImageMimeType = {"image/jpeg", "image/jpg", "image/png", "image/x-ms-bmp", "image/vnd.wap.wbmp", "image/heic", "image/webp", "image/gif", "image/heif"};
    public static String sTemplateDir = null;
    public static int sTestLocationType = -1;
    public static boolean sUseXmSdk;
    public static final String[] sXmSDKList;

    static {
        String[] strArr = {"merlin", "merlinin", "merlinnfc", "monet", "monetin", "vangogh", "violet", "joyeuse", "excalibur", "durandal", "curtanacn", "curtana", "tucana", "phoenixin", "phoenix", "picasso", "picassoin", "cmi", "umi", "lmi", "lmipro", "lmiin", "lmiinpro", "pyxis", "laurus", "toco", "tocoin", "lancelot", "atom", "bomb", "gram", "galahad", "apollo", "shiva", "cas", "apricot", "banana", "cezanne", "gauguin", "surya", "gauguinpro", "karna", "cannon", "cannong", "venus", "gauguininpro", "star", "cetus", "courbet", "courbetin", "sweet", "sweetin", "haydn", "alioth", "mojito", "rainbow", "sunny", "rosemary", "maltose", "secret", "mars", "renoir", "ares", "aresin", "thyme", "haydnin", "aliothin", "chopin", "camellia", "camellian", "vayu", "bhima", "biloba", "odin", "vili", "enuma", "elish", "nabu", "argo", "agate", "agatein", "selene", "eos", "amber", "lisa", "mona", "evergo", "evergreen", "zeus", "cupid", "psyche", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "angelicain", "ingres", "poussin", "fleur", "miel", "veux", "peux", "spes", "spesn", "viva", "vida", "munch", "rubens", "matisse", "fog", "thor", "loki", "light", "thunder", "taoyao", "opal", "zijin", "zizhan", "frost", "xaga", "xagain", "xagapro", "xagaproin", "wind", "rain", "mayfly", "unicorn", "plato", "daumier", "diting", "ice"};
        sXmSDKList = strArr;
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("_");
            if (split != null && split.length > 0) {
                str = split[0];
            }
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (strArr[i].equalsIgnoreCase(str)) {
                    sUseXmSdk = true;
                    break;
                } else {
                    i++;
                }
            }
            for (String str2 : sLowDeviceList) {
                if (str2.equalsIgnoreCase(str)) {
                    sIsLowDevice = true;
                    return;
                }
            }
        }
    }

    public static boolean isUserXmSdk() {
        return sUseXmSdk;
    }

    public static boolean isUseLowResolution() {
        return sIsLowDevice;
    }

    public static void init(Context context) {
        if (!sInited) {
            String str = isUserXmSdk() ? "xm_" : "";
            String absolutePath = context.getExternalFilesDir(str + "movie").getAbsolutePath();
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            String str2 = File.separator;
            sb.append(str2);
            sb.append("template");
            sTemplateDir = sb.toString();
            sAudioDir = absolutePath + str2 + "audio";
            FileUtils.addNoMedia(absolutePath);
            sInited = true;
        }
    }

    public static void setHeightToWidth(float f) {
        sHeightToWidth = f;
    }

    public static float getHeightToWidth() {
        return sHeightToWidth;
    }

    public static boolean isMimeTypeSupport(String str) {
        for (String str2 : sSupportImageMimeType) {
            if (str2.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
