package com.miui.gallery.cloud;

import com.miui.gallery.cloud.ServerErrorCode;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CheckResult {
    public static int parseErrorCode(JSONObject jSONObject) throws JSONException {
        int i = jSONObject == null ? -6 : jSONObject.getInt("code");
        if (i != 0) {
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "parseErrorCode");
            hashMap.put("error", String.valueOf(i));
            hashMap.put("error_extra", jSONObject == null ? "null" : jSONObject.toString());
            SamplingStatHelper.recordCountEvent("Sync", "check_result", hashMap);
            DefaultLogger.e("CheckResult", "parseErrorCode:" + i);
        }
        return i;
    }

    public static GallerySyncResult<JSONObject> checkXMResultCode(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) throws JSONException {
        GallerySyncResult.Builder builder = new GallerySyncResult.Builder();
        if (jSONObject == null) {
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "checkXMResultCode");
            hashMap.put("error_extra", "result is null");
            SamplingStatHelper.recordCountEvent("Sync", "check_result", hashMap);
            builder.setCode(GallerySyncCode.NOT_RETRY_ERROR);
            DefaultLogger.e("CheckResult", "checkXMResultCode, result is null");
        } else if (!jSONObject.has("code")) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, "checkXMResultCode");
            hashMap2.put("error_extra", "result has no code");
            SamplingStatHelper.recordCountEvent("Sync", "check_result", hashMap2);
            builder.setCode(GallerySyncCode.RETRY_ERROR).setRetryAfter(0L);
            DefaultLogger.e("CheckResult", "checkXMResultCode, result has no code");
        } else {
            builder.clone(checkErrorCode(jSONObject.getInt("code"), jSONObject, requestItemBase, spaceFullListener));
        }
        return builder.setData(jSONObject).build();
    }

    public static GallerySyncResult checkErrorCode(int i, JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) throws JSONException {
        ServerErrorCode.GalleryErrorCodeItem galleryErrorCodeItem = ServerErrorCode.sGalleryServerErrors.get(Integer.valueOf(i));
        GallerySyncResult.Builder builder = new GallerySyncResult.Builder();
        if (galleryErrorCodeItem == null) {
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "checkErrorCode");
            String str = "null";
            hashMap.put("error_extra", jSONObject == null ? str : jSONObject.toString());
            SamplingStatHelper.recordCountEvent("Sync", "check_result", hashMap);
            builder.setCode(GallerySyncCode.NOT_RETRY_ERROR);
            StringBuilder sb = new StringBuilder();
            sb.append("checkErrorCode, errorExtra:");
            if (jSONObject != null) {
                str = jSONObject.toString();
            }
            sb.append(str);
            DefaultLogger.e("CheckResult", sb.toString());
        } else {
            if (galleryErrorCodeItem.code != 0) {
                DefaultLogger.e("CheckResult", "checkErrorCode, error code:" + galleryErrorCodeItem.code);
            }
            ServerErrorCode.GalleryErrorHandler galleryErrorHandler = galleryErrorCodeItem.errorHandler;
            if (galleryErrorHandler != null) {
                galleryErrorHandler.onError(jSONObject, requestItemBase, spaceFullListener);
            }
            if (ServerErrorCode.isRetryCode(galleryErrorCodeItem.code)) {
                long j = 5;
                JSONObject optJSONObject = jSONObject.optJSONObject("data");
                if (optJSONObject != null && optJSONObject.has("retryAfter")) {
                    j = CloudUtils.getLongAttributeFromJson(optJSONObject, "retryAfter");
                }
                builder.setRetryAfter(j);
            }
            builder.setCode(galleryErrorCodeItem.result);
        }
        return builder.setData(jSONObject).build();
    }

    public static GallerySyncResult<JSONObject> checkXMResultCodeForFaceRequest(JSONObject jSONObject, RequestItemBase requestItemBase) throws JSONException {
        GallerySyncResult.Builder builder = new GallerySyncResult.Builder();
        if (jSONObject == null) {
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "checkFaceErrorCode");
            hashMap.put("error_extra", "no result");
            SamplingStatHelper.recordCountEvent("Sync", "check_result", hashMap);
            builder.setCode(GallerySyncCode.NOT_RETRY_ERROR);
        } else if (!jSONObject.has("code")) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, "checkFaceErrorCode");
            hashMap2.put("error_extra", "result has no code");
            SamplingStatHelper.recordCountEvent("Sync", "check_result", hashMap2);
            builder.setCode(GallerySyncCode.RETRY_ERROR);
        } else {
            int i = jSONObject.getInt("code");
            if (i == 52000) {
                builder.setCode(GallerySyncCode.RESET_FACE_TAG);
            } else {
                builder.clone(checkErrorCode(i, jSONObject, requestItemBase, null));
            }
        }
        return builder.setData(jSONObject).build();
    }

    public static GallerySyncCode checkKSSThumbnailResult(int i) {
        DefaultLogger.e("CheckResult", "checkKSSThumbnailResult:" + i);
        if (i == 404 || i == 516 || i == 519 || i == 520) {
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return GallerySyncCode.RETRY_ERROR;
    }

    public static boolean isNotRetryCode(int i) {
        return i / 100 == 4;
    }
}
