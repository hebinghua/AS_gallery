package com.market.sdk.utils;

import android.text.TextUtils;
import com.market.sdk.ServerType;

/* loaded from: classes.dex */
public class Constants {
    public static String UPDATE_64_URL;
    public static String UPDATE_URL;
    public static volatile String URL_BASE;
    public static String customUrl;
    public static volatile boolean sUseInternationalUrl = Client.isInternationalMiui();
    public static volatile ServerType sServer = ServerType.PRODUCT;

    /* loaded from: classes.dex */
    public static final class Update {
        public static final String[] UPDATE_PROJECTION = {"update_download.package_name", "update_download.download_id", "update_download.version_code", "update_download.apk_url", "update_download.apk_hash", "update_download.diff_url", "update_download.diff_hash", "update_download.apk_path"};
    }

    /* loaded from: classes.dex */
    public enum UpdateMethod {
        MARKET,
        DOWNLOAD_MANAGER
    }

    public static void configURL() {
        if (TextUtils.isEmpty(customUrl)) {
            if (sUseInternationalUrl) {
                URL_BASE = sServer.getGlobalBaseUrl();
            } else {
                URL_BASE = sServer.getBaseUrl();
            }
            UPDATE_URL = URL_BASE + "updateself";
            UPDATE_64_URL = URL_BASE + "updateself/support64App";
            return;
        }
        UPDATE_URL = customUrl + "/autoupdate/updateself";
    }
}
