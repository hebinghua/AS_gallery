package com.miui.gallery.vlog.template;

import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.miui.gallery.util.BaseBuildUtil;

/* loaded from: classes2.dex */
public class TemplateProfileUtils {
    public static float MAX_SPEED_FOR_LOW_END_TEMPLATE = 1.5f;
    public static long TEMPLATE_INTERNAL_NORMAL = 16314109526409408L;
    public static long TEMPLATE_INTERNATIONAL_NORMAL = 16314450466767008L;
    public static boolean mIsNeedTransToLowTemplate;
    public static final String[] mNeedTransToLowTemplateModelList;
    public static final ArrayMap<String, String> mTemplateHeaderTailMap;
    public static boolean sIsNeedHideHighTemplate;
    public static final String[] sNeedHideHighTemplateModelList;

    static {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        mTemplateHeaderTailMap = arrayMap;
        String[] strArr = {"phoenix", "phoenixin", "davinci", "davinciin", "tucana", "toco", "tocoin", "gauguin", "gauguinpro", "gauguininpro", "lime", "citrus", "atom", "apricot", "bomb", "banana", "lancelot", "galahad", "shiva", "dandelion", "angelica", "cattail", "angelican", "angelicain", "cezanne"};
        mNeedTransToLowTemplateModelList = strArr;
        sNeedHideHighTemplateModelList = new String[0];
        arrayMap.put("pressconference", "misdk_dynamic_travel");
        arrayMap.put("oldmovie", "misdk_dynamic_vlog");
        arrayMap.put("baby1", "misdk_dynamic_baby_diary");
        arrayMap.put("baby2", "misdk_dynamic_baby_diary_horse");
        mIsNeedTransToLowTemplate = false;
        sIsNeedHideHighTemplate = false;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (strArr[i].equalsIgnoreCase(Build.DEVICE)) {
                mIsNeedTransToLowTemplate = true;
                break;
            } else {
                i++;
            }
        }
        for (String str : sNeedHideHighTemplateModelList) {
            if (Build.DEVICE.equals(str)) {
                sIsNeedHideHighTemplate = true;
                return;
            }
        }
    }

    public static boolean isNeedTransToLowTemplate() {
        return mIsNeedTransToLowTemplate;
    }

    public static boolean isNeedHideHighTemplate() {
        return sIsNeedHideHighTemplate;
    }

    public static long getTemplateId() {
        if (BaseBuildUtil.isInternational()) {
            return TEMPLATE_INTERNATIONAL_NORMAL;
        }
        return TEMPLATE_INTERNAL_NORMAL;
    }

    public static String getHeaderTailLabelFromTemplateKey(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return mTemplateHeaderTailMap.get(str);
    }
}
