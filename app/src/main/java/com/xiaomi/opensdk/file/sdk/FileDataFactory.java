package com.xiaomi.opensdk.file.sdk;

import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.kss.IDataFactory;
import cn.kuaipan.android.kss.IKssUploadRequestResult;

/* loaded from: classes3.dex */
public class FileDataFactory implements IDataFactory {
    @Override // cn.kuaipan.android.kss.IDataFactory
    public IKssUploadRequestResult createUploadRequestResult(String str) throws KscException {
        return FileUploadRequestResult.create(str);
    }
}
