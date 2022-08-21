package com.miui.gallery.preference;

import android.content.Intent;
import androidx.annotation.Keep;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.deprecated.BaseDeprecatedPreference;
import java.util.Random;

/* loaded from: classes2.dex */
public class BaseGalleryPreferences {

    @Keep
    /* loaded from: classes2.dex */
    public interface PrefKeys {
        public static final String AUTHEN_TOKEN_EXPIRED_TIME = "authen_token_expired_time";
        public static final String AUTHEN_TOKEN_STRING = "authen_token_string";
        public static final String CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY = "can_connect_network";
        public static final String CTA_IMPUNITY_DECLARATION_EVERY_TIME = "impunity_declaration_every_time";
        public static final String CTA_PRIVACY_PERMISSIONS_ALLOWED = "cta_privacy_permissions_allowed";
        public static final String DEBUG_PRINT_LOG = "debug_print_log";
        public static final String DISPOSABLE_NEW_USER = "disposable_new_user";
        public static final String DOCUMENT_PROVIDER_URI_PREFIX = "document_provider_uri__";
        public static final String HAS_SHOWN_NETWORKING_AGREEMENTS = "has_shown_networking_agreements";
        public static final String HOOK_HEIF_DECODER = "hook_heif_decoder";
        public static final String MAX_TEXTURE_SIZE = "max_texture_size";
        public static final String OCR_SUPPORT_KEY = "ocr_support_key";
        public static final String REQUIRED_AGREEMENTS_ALLOWED = "required_agreements_allowed";
        public static final String RUNTIME_PERMISSIONS_INTRODUCED = "runtime_permissions_introduced";
        public static final String SCANNER_LAST_IMAGES_SCAN_TIME = "last_images_scan_time";
        public static final String STAT_USER_INFO_FROM_SELF_SHOOT = "user_info_self_shoot";
        public static final String STAT_USER_UNIQ_ID = "user_uniq_id";
        public static final String SYSTEM_CTA_PERMISSION_ALLOWED = "system_cta_permission_allowed";
        public static final String SYSTEM_CTA_PERMISSION_SHOWN = "system_cta_permission_shown";
    }

    /* loaded from: classes2.dex */
    public static class SampleStatistic {
        public static long getUniqId() {
            long j = PreferenceHelper.getLong(PrefKeys.STAT_USER_UNIQ_ID, 0L);
            if (j == 0) {
                long nextLong = new Random().nextLong();
                PreferenceHelper.putLong(PrefKeys.STAT_USER_UNIQ_ID, nextLong);
                PreferenceHelper.putBoolean(PrefKeys.DISPOSABLE_NEW_USER, true);
                return nextLong;
            }
            return j;
        }
    }

    /* loaded from: classes2.dex */
    public static class CTA {
        public static boolean sAllowUseOnOfflineGlobal = false;
        public static boolean sCanConnectToNetworkTemp = false;

        public static void setRemindConnectNetworkEveryTime(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.CTA_IMPUNITY_DECLARATION_EVERY_TIME, z);
            if (z) {
                PreferenceHelper.putBoolean(PrefKeys.CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY, false);
            }
        }

        public static boolean remindConnectNetworkEveryTime() {
            return PreferenceHelper.getBoolean(PrefKeys.CTA_IMPUNITY_DECLARATION_EVERY_TIME, true);
        }

        public static void setCanConnectNetwork(boolean z) {
            if (z) {
                sCanConnectToNetworkTemp = false;
                PreferenceHelper.putBoolean(PrefKeys.CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY, true);
                PreferenceHelper.putBoolean(PrefKeys.CTA_IMPUNITY_DECLARATION_EVERY_TIME, false);
            } else {
                sCanConnectToNetworkTemp = true;
                PreferenceHelper.putBoolean(PrefKeys.CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY, false);
            }
            LocalBroadcastManager.getInstance(StaticContext.sGetAndroidContext()).sendBroadcast(new Intent("com.miui.gallery.action.CTA_CHANGED"));
        }

        public static boolean canConnectNetwork() {
            return PreferenceHelper.getBoolean(PrefKeys.CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY, BaseDeprecatedPreference.sCanConnectNetworkByImpunity()) || sCanConnectToNetworkTemp;
        }

        public static boolean containCTACanConnectNetworkKey() {
            return PreferenceHelper.contains(PrefKeys.CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY);
        }

        public static boolean hasUsedGalleryBefore() {
            return PreferenceHelper.contains(PrefKeys.CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY) || !isDisposableNewUser() || hasSystemCTAPermissionShown();
        }

        public static boolean isDisposableNewUser() {
            SampleStatistic.getUniqId();
            boolean z = PreferenceHelper.getBoolean(PrefKeys.DISPOSABLE_NEW_USER, false);
            PreferenceHelper.putBoolean(PrefKeys.DISPOSABLE_NEW_USER, false);
            return z;
        }

        public static void setToAllowUseOnOfflineGlobal(boolean z) {
            sAllowUseOnOfflineGlobal = z;
        }

        public static boolean allowUseOnOfflineGlobal() {
            return sAllowUseOnOfflineGlobal;
        }

        public static void setCanConnectToNetworkTemp(boolean z) {
            sCanConnectToNetworkTemp = z;
        }

        public static void setHasShownNetworkingAgreements(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.HAS_SHOWN_NETWORKING_AGREEMENTS, z);
        }

        public static boolean hasShownNetworkingAgreements() {
            return PreferenceHelper.getBoolean(PrefKeys.HAS_SHOWN_NETWORKING_AGREEMENTS, false);
        }

        public static boolean isSystemCTAPermissionAllowed() {
            return PreferenceHelper.getBoolean(PrefKeys.SYSTEM_CTA_PERMISSION_ALLOWED, false);
        }

        public static void setSystemCTAPermissionAllowed(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SYSTEM_CTA_PERMISSION_ALLOWED, z);
        }

        public static boolean hasSystemCTAPermissionShown() {
            return PreferenceHelper.getBoolean(PrefKeys.SYSTEM_CTA_PERMISSION_SHOWN, false);
        }

        public static void setSystemCTAPermissionShown(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SYSTEM_CTA_PERMISSION_SHOWN, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class Agreement {
        public static boolean isRequiredAgreementsAllowed() {
            return PreferenceHelper.getBoolean(PrefKeys.REQUIRED_AGREEMENTS_ALLOWED, false);
        }

        public static void setRequiredAgreementsAllowed(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.REQUIRED_AGREEMENTS_ALLOWED, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class PermissionIntroduction {
        public static String generateCtaPermissionKey(String str) {
            return PrefKeys.CTA_PRIVACY_PERMISSIONS_ALLOWED + str;
        }

        public static void setCtaPrivacyPermissionsAllowed(String str, boolean z) {
            PreferenceHelper.putBoolean(generateCtaPermissionKey(str), z);
        }

        public static boolean isCtaPrivacyPermissionsAllowed(String str) {
            return PreferenceHelper.getBoolean(generateCtaPermissionKey(str), false);
        }

        public static boolean containCtaPrivacyPermission(String str) {
            return PreferenceHelper.contains(generateCtaPermissionKey(str));
        }

        public static void setRuntimePermissionsIntroduced(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.RUNTIME_PERMISSIONS_INTRODUCED, z);
        }

        public static boolean isRuntimePermissionsIntroduced() {
            return PreferenceHelper.getBoolean(PrefKeys.RUNTIME_PERMISSIONS_INTRODUCED, false);
        }
    }

    /* loaded from: classes2.dex */
    public static class BaseDocumentProvider {
        public static String getUri(String str) {
            return PreferenceHelper.getString(PrefKeys.DOCUMENT_PROVIDER_URI_PREFIX + str, null);
        }

        public static void setUri(String str, String str2) {
            PreferenceHelper.putString(PrefKeys.DOCUMENT_PROVIDER_URI_PREFIX + str, str2);
        }

        public static String getExternalSDCardUri() {
            return getUri(StorageUtils.getSecondaryStoragePath());
        }

        public static void setExternalSDCardUri(String str) {
            setUri(StorageUtils.getSecondaryStoragePath(), str);
        }
    }

    /* loaded from: classes2.dex */
    public static class Debug {
        public static boolean isPrintLog() {
            return MemoryPreferenceHelper.getBoolean(PrefKeys.DEBUG_PRINT_LOG, false);
        }

        public static void printLog(boolean z) {
            MemoryPreferenceHelper.putBoolean(PrefKeys.DEBUG_PRINT_LOG, z);
        }

        public static boolean isHookHeifDecoder() {
            return PreferenceHelper.getBoolean(PrefKeys.HOOK_HEIF_DECODER, true);
        }

        public static void hookHeifDecoder(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.HOOK_HEIF_DECODER, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class AuthenToken {
        public static void setAthenTokenExpiredTime(long j) {
            PreferenceHelper.putLong(PrefKeys.AUTHEN_TOKEN_EXPIRED_TIME, j);
        }

        public static long getAthenTokenExpiredTime() {
            return PreferenceHelper.getLong(PrefKeys.AUTHEN_TOKEN_EXPIRED_TIME, 0L);
        }

        public static String getAuthenTokenString() {
            if (System.currentTimeMillis() < getAthenTokenExpiredTime()) {
                return PreferenceHelper.getString(PrefKeys.AUTHEN_TOKEN_STRING, null);
            }
            return null;
        }

        public static void setAuthenTokenString(String str, long j) {
            PreferenceHelper.putString(PrefKeys.AUTHEN_TOKEN_STRING, str);
            setAthenTokenExpiredTime(j + System.currentTimeMillis());
        }
    }

    /* loaded from: classes2.dex */
    public static class TextureSize {
        public static int getMaxTextureSize() {
            return PreferenceHelper.getInt(PrefKeys.MAX_TEXTURE_SIZE, -1);
        }

        public static void setMaxTextureSize(int i) {
            PreferenceHelper.putInt(PrefKeys.MAX_TEXTURE_SIZE, i);
        }
    }
}
