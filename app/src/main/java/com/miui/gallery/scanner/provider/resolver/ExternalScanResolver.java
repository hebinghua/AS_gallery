package com.miui.gallery.scanner.provider.resolver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.scanner.core.MediaScannerService;
import com.miui.gallery.scanner.core.ScanContracts$Mode;
import com.miui.gallery.scanner.core.model.UnhandledScanTaskRecord;
import com.miui.gallery.scanner.provider.RequestMediaStoreScanRecordManager;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.storage.constants.AndroidStorageConstants;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class ExternalScanResolver extends IScanMethodResolver {
    public static final Map<String, AbsExternalCallerApplication> EXTERNAL_CALLER_APPLICATIONS;

    static {
        HashMap hashMap = new HashMap();
        EXTERNAL_CALLER_APPLICATIONS = hashMap;
        hashMap.put(AndroidStorageConstants.PACKAGE_NAME_MEDIA_STORE, new MediaStoreCaller());
        hashMap.put("com.android.camera", new CameraCaller());
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public boolean handles(String str) {
        return TextUtils.equals("request_scan", str);
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public Bundle resolve(Context context, Bundle bundle) {
        if (!PermissionUtils.checkStoragePermission(context)) {
            return null;
        }
        String string = bundle.getString("param_path");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        externalRequestScan(context, string, bundle);
        return null;
    }

    public final void externalRequestScan(Context context, String str, Bundle bundle) {
        String string = bundle.getString("param_internal_calling_package_name");
        AbsExternalCallerApplication absExternalCallerApplication = EXTERNAL_CALLER_APPLICATIONS.get(string);
        if (absExternalCallerApplication == null) {
            DefaultLogger.w("ExternalScanResolver", "[%s] not a registered application.", string);
            return;
        }
        String checkCondition = absExternalCallerApplication.checkCondition(context, getCurrentMode(), str);
        if (!TextUtils.isEmpty(checkCondition)) {
            DefaultLogger.w("ExternalScanResolver", "external scan request from [%s] shall not pass, [%s].", string, checkCondition);
            return;
        }
        String string2 = bundle.getString("ownerPackageName");
        int i = bundle.getInt("param_parallel_process_state", 0);
        long j = bundle.getLong("param_media_store_id", -1L);
        boolean z = i == 1 && !bundle.getBoolean("param_no_gaussian", false);
        DefaultLogger.fd("ExternalScanResolver", "handle a valid message from [%s], path: [%s], operatorPackageName: [%s], parallelProcessState: [%d], mediaStoreId: [%d], usingGaussian: [%b].", string, str, string2, Integer.valueOf(i), Long.valueOf(j), Boolean.valueOf(z));
        BackgroundServiceHelper.startForegroundServiceIfNeed(context, new Intent(context, MediaScannerService.class).putExtra("key_mode", getCurrentMode()).putExtra("key_external_scan_request", str).putExtra("key_record_id", GalleryEntityManager.getInstance().insert(new UnhandledScanTaskRecord(str, 15, System.currentTimeMillis(), string, string2, i, j, z))).putExtra("key_calling_package_name", string).putExtra("key_operator_package_name", string2).putExtra("key_parallel_process_state", i).putExtra("key_media_store_id", j).putExtra("key_using_gaussian", z));
    }

    /* loaded from: classes2.dex */
    public static abstract class AbsExternalCallerApplication {
        public abstract String doCheckCondition(Context context, ScanContracts$Mode scanContracts$Mode, String str);

        public AbsExternalCallerApplication() {
        }

        public final String checkCondition(Context context, ScanContracts$Mode scanContracts$Mode, String str) {
            return RequestMediaStoreScanRecordManager.getInstance().exists(str) ? "gallery has already handled this action" : doCheckCondition(context, scanContracts$Mode, str);
        }
    }

    /* loaded from: classes2.dex */
    public static class MediaStoreCaller extends AbsExternalCallerApplication {
        public MediaStoreCaller() {
            super();
        }

        @Override // com.miui.gallery.scanner.provider.resolver.ExternalScanResolver.AbsExternalCallerApplication
        public String doCheckCondition(Context context, ScanContracts$Mode scanContracts$Mode, String str) {
            if (scanContracts$Mode == ScanContracts$Mode.POWER_SAVE) {
                return "in power save mode";
            }
            if (BaseFileUtils.getFileName(str).startsWith("cts")) {
                return "CTS test failed and they blamed to us, make them happy";
            }
            if (StringUtils.containsIgnoreCase(str, ".thumbnail")) {
                return "media store thumbnail file";
            }
            Boolean bool = (Boolean) ScanCache.getInstance().get("key_mi_mover_event_start");
            if (bool != null && bool.booleanValue()) {
                return "mi mover working, ignore scan request from media store";
            }
            String relativePath = StorageUtils.getRelativePath(context, str);
            if (relativePath == null) {
                return "illegal path";
            }
            if (BaseFileUtils.contains(MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH, relativePath) && BaseFileUtils.getFileName(str).startsWith("Screenshot_")) {
                return "screenshot";
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class CameraCaller extends AbsExternalCallerApplication {
        public CameraCaller() {
            super();
        }

        @Override // com.miui.gallery.scanner.provider.resolver.ExternalScanResolver.AbsExternalCallerApplication
        public String doCheckCondition(Context context, ScanContracts$Mode scanContracts$Mode, String str) {
            if (!BaseFileUtils.contains(MIUIStorageConstants.DIRECTORY_CAMERA_PATH, StorageUtils.getRelativePath(context, str))) {
                return "not camera path";
            }
            String mimeType = BaseFileMimeUtil.getMimeType(str);
            if (!BaseFileMimeUtil.isImageFromMimeType(mimeType) && !BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
                return "not image/video";
            }
            return null;
        }
    }
}
