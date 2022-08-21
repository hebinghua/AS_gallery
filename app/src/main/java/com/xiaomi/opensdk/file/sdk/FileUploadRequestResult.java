package com.xiaomi.opensdk.file.sdk;

import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.kss.UploadRequestResult;
import cn.kuaipan.android.utils.ApiDataHelper;
import cn.kuaipan.android.utils.ApiDataHelper$IKscData$Parser;
import cn.kuaipan.android.utils.IObtainable;
import cn.kuaipan.android.utils.JsonUtils;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class FileUploadRequestResult extends UploadRequestResult {
    public static final ApiDataHelper$IKscData$Parser<FileUploadRequestResult> PARSER = new ApiDataHelper$IKscData$Parser<FileUploadRequestResult>() { // from class: com.xiaomi.opensdk.file.sdk.FileUploadRequestResult.1
    };
    public final String requestId;

    public FileUploadRequestResult(Map<String, Object> map) throws KscException {
        super(map);
        this.requestId = ApiDataHelper.asString(map, "requestId");
    }

    public static FileUploadRequestResult create(String str) throws KscException {
        Throwable th;
        JSONException e;
        IOException e2;
        Map map;
        try {
            try {
                map = (Map) JsonUtils.parser(new StringReader(str));
            } catch (Throwable th2) {
                th = th2;
                if (str != null && (str instanceof IObtainable)) {
                    ((IObtainable) str).recycle();
                }
                throw th;
            }
        } catch (IOException e3) {
            e2 = e3;
        } catch (JSONException e4) {
            e = e4;
        } catch (Throwable th3) {
            th = th3;
            str = null;
            if (str != null) {
                ((IObtainable) str).recycle();
            }
            throw th;
        }
        try {
            FileUploadRequestResult fileUploadRequestResult = new FileUploadRequestResult(map);
            if (map != null && (map instanceof IObtainable)) {
                ((IObtainable) map).recycle();
            }
            return fileUploadRequestResult;
        } catch (IOException e5) {
            e2 = e5;
            throw new KscException(501004, "kss is null", e2);
        } catch (JSONException e6) {
            e = e6;
            throw new KscException(501001, "kss is not json", e);
        }
    }

    @Override // cn.kuaipan.android.kss.UploadRequestResult
    public String toString() {
        String uploadRequestResult = super.toString();
        try {
            return new JSONObject(uploadRequestResult).put("requestId", this.requestId).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return uploadRequestResult;
        }
    }
}
