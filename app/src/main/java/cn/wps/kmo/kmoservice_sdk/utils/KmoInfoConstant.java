package cn.wps.kmo.kmoservice_sdk.utils;

import android.text.TextUtils;
import java.util.HashMap;

/* loaded from: classes.dex */
public class KmoInfoConstant {
    public static final HashMap<String, ServiceConfigBean> serviceConfigHm = new HashMap<>();
    public static final HashMap<String, ServiceConfigBean> liteServiceConfigHm = new HashMap<String, ServiceConfigBean>() { // from class: cn.wps.kmo.kmoservice_sdk.utils.KmoInfoConstant.1
        {
            put(TypeUtils.IMAGE_CONVERTER_PDF, new ServiceConfigBean("cn.wps.moffice.service.lite.picconverterpdf.action", "cn.wps.moffice.service.lite.imageconverterpdf.PicConverterPdfLiteService"));
        }
    };
    public static final HashMap<String, ServiceConfigBean> interLiteServiceConfigHm = new HashMap<>();

    /* loaded from: classes.dex */
    public static class ServiceConfigBean {
        public String service_wps_lite;
        public String wps_lite_action;

        public ServiceConfigBean(String str, String str2) {
            this.wps_lite_action = str;
            this.service_wps_lite = str2;
        }
    }

    public static String getWpsLiteAction(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (str.equals(TypeUtils.KMO)) {
                HashMap<String, ServiceConfigBean> hashMap = serviceConfigHm;
                if (hashMap.containsKey(str2)) {
                    return hashMap.get(str2).wps_lite_action;
                }
            } else if (str.equals(TypeUtils.KMO_LITE)) {
                HashMap<String, ServiceConfigBean> hashMap2 = liteServiceConfigHm;
                if (hashMap2.containsKey(str2)) {
                    return hashMap2.get(str2).wps_lite_action;
                }
            } else if (str.equals(TypeUtils.KMO_INTER_LITE)) {
                HashMap<String, ServiceConfigBean> hashMap3 = interLiteServiceConfigHm;
                if (hashMap3.containsKey(str2)) {
                    return hashMap3.get(str2).wps_lite_action;
                }
            }
        }
        return "";
    }

    public static String getServiceWpsLite(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (str.equals(TypeUtils.KMO)) {
                HashMap<String, ServiceConfigBean> hashMap = serviceConfigHm;
                if (hashMap.containsKey(str2)) {
                    return hashMap.get(str2).service_wps_lite;
                }
            } else if (str.equals(TypeUtils.KMO_LITE)) {
                HashMap<String, ServiceConfigBean> hashMap2 = liteServiceConfigHm;
                if (hashMap2.containsKey(str2)) {
                    return hashMap2.get(str2).service_wps_lite;
                }
            } else if (str.equals(TypeUtils.KMO_INTER_LITE)) {
                HashMap<String, ServiceConfigBean> hashMap3 = interLiteServiceConfigHm;
                if (hashMap3.containsKey(str2)) {
                    return hashMap3.get(str2).service_wps_lite;
                }
            }
        }
        return "";
    }

    public static String getPackageWpsName(String str) {
        if (!TextUtils.isEmpty(str) && !str.equals(TypeUtils.KMO)) {
            if (str.equals(TypeUtils.KMO_LITE)) {
                return "cn.wps.moffice_eng.xiaomi.lite";
            }
            str.equals(TypeUtils.KMO_INTER_LITE);
            return "";
        }
        return "";
    }
}
