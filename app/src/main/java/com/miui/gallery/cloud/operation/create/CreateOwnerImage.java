package com.miui.gallery.cloud.operation.create;

import android.content.Context;
import cn.kuaipan.android.kss.upload.UploadDescriptorFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryKssManager;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.data.DBImage;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import com.xiaomi.stat.a.j;

/* loaded from: classes.dex */
public class CreateOwnerImage extends CreateImageBase {
    public CreateOwnerImage(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.create.CreateImageBase
    public void doUpload(RequestCloudItem requestCloudItem, UploadDescriptorFile uploadDescriptorFile) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        GalleryKssManager.doOwnerUpload(requestCloudItem, uploadDescriptorFile);
    }

    @Override // com.miui.gallery.cloud.operation.create.CreateImageBase
    public DBImage getDBImage(RequestCloudItem requestCloudItem) {
        return CloudUtils.getItem(requestCloudItem.dbCloud.getBaseUri(), GalleryApp.sGetAndroidContext(), j.c, requestCloudItem.dbCloud.getId());
    }
}
