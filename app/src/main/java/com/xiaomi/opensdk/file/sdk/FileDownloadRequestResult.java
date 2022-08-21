package com.xiaomi.opensdk.file.sdk;

import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.kss.DownloadRequestResult;
import cn.kuaipan.android.utils.ApiDataHelper$IKscData$Parser;
import java.util.Map;

/* loaded from: classes3.dex */
public class FileDownloadRequestResult extends DownloadRequestResult {
    public static final ApiDataHelper$IKscData$Parser<FileDownloadRequestResult> PARSER = new ApiDataHelper$IKscData$Parser<FileDownloadRequestResult>() { // from class: com.xiaomi.opensdk.file.sdk.FileDownloadRequestResult.1
    };

    public FileDownloadRequestResult(Map<String, Object> map) throws KscException {
        super(map);
    }
}
