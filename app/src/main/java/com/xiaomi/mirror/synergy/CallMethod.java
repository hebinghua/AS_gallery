package com.xiaomi.mirror.synergy;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.xiaomi.stat.MiStat;

/* loaded from: classes3.dex */
class CallMethod {
    public static final String ARG_AP_CALLBACK = "apCallback";
    public static final String ARG_AP_SSID = "apSsid";
    public static final String ARG_CLIP_DATA = "clipData";
    public static final String ARG_DISPLAY_ID = "displayId";
    public static final String ARG_EXTENSION = "extension";
    public static final String ARG_EXTRA_STRING = "extra";
    public static final String ARG_ID = "id";
    public static final String ARG_TITLE = "title";
    public static final String ARG_URI = "uri";
    public static final String CALL_PROVIDER_AUTHORITY = "com.xiaomi.mirror.callprovider";
    public static final String METHOD_CHOOSE_FILE_FROM_SYNERGY = "chooseFileFromSynergy";
    public static final String METHOD_CONNECT_SAME_ACCOUNT_AP = "connectSameAccountAp";
    public static final String METHOD_GET = "get";
    public static final String METHOD_GET_ALIVE_BINDER = "getAliveBinder";
    public static final String METHOD_GET_CALL_RELAY_SERVICE = "getCallRelayService";
    public static final String METHOD_GET_UPDATE_ICON = "getUpdateIcon";
    public static final String METHOD_IS_FLOAT_WINDOW_SHOW = "isFloatWindowShow";
    public static final String METHOD_IS_P2P_WORKING = "isP2PWorking";
    public static final String METHOD_IS_SUPPORT_TAKE_PHOTO = "isSupportTakePhoto";
    public static final String METHOD_IS_SYNERGY_ENABLE = "isSynergyEnable";
    public static final String METHOD_NOTIFY_UPDATE_ICON = "notifyUpdateIcon";
    public static final String METHOD_OPEN_DIRECT = "openDirect";
    public static final String METHOD_OPEN_MI_CLOUD_ON_SYNERGY = "openMiCloudOnSynergy";
    public static final String METHOD_OPEN_ON_SYNERGY = "openOnSynergy";
    public static final String METHOD_PERFORM_RELAY_ICON_CLICK = "performRelayIconClick";
    public static final String METHOD_QUERY_OPEN_ON_SYNERGY = "queryOpenOnSynergy";
    public static final String METHOD_QUERY_SAME_ACCOUNT_AP = "querySameAccountAp";
    public static final String METHOD_REGISTER_AP_CALLBACK = "registerApCallback";
    public static final String METHOD_SAVE_TO_SYNERGY = "saveToSynergy";
    public static final String METHOD_TAKE_PHOTO_CANCEL = "takePhotoCancel";
    public static final String METHOD_TAKE_PHOTO_FROM_SYNERGY = "takePhotoFromSynergy";
    public static final String METHOD_UNREGISTER_AP_CALLBACK = "unRegisterApCallback";
    public static final String METHOD_UPDATE_TITLE = "updateTitle";
    public static final String RESULT_AP_IS5G = "apId5G";
    public static final String RESULT_AP_SSID = "apSsid";
    public static final String RESULT_BATTERY_PERCENT = "batteryPercent";
    public static final String RESULT_BINDER = "binder";
    public static final String RESULT_CLIP_DATA = "clipData";
    public static final String RESULT_ENABLE_BOOLEAN = "enable";
    public static final String RESULT_FILE_DESCRIPTOR = "fileDescriptor";
    public static final String RESULT_ICON = "icon";
    public static final String RESULT_ID = "id";
    public static final String RESULT_IS_FLOAT_WINDOW_SHOW = "isFloatWindowShow";
    public static final String RESULT_OPTION_LIST = "optionList";
    public static final String RESULT_SOFTAP_STATE = "softApState";
    public static final String RESULT_TITLE = "title";
    public static final String RESULT_VALUE = "value";

    public static Bundle doCall(ContentResolver contentResolver, String str, String str2, Bundle bundle) {
        try {
            return Build.VERSION.SDK_INT >= 29 ? contentResolver.call(CALL_PROVIDER_AUTHORITY, str, str2, bundle) : contentResolver.call(new Uri.Builder().scheme(MiStat.Param.CONTENT).authority(CALL_PROVIDER_AUTHORITY).build(), str, str2, bundle);
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
