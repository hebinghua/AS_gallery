package com.miui.gallery.error;

import android.content.Context;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.error.core.ErrorActionCallback;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.IntentUtil;

/* loaded from: classes2.dex */
public class BatchErrorStorageNoWritePermissionTip extends ErrorStorageNoWritePermissionTip {
    public BatchErrorStorageNoWritePermissionTip(ErrorCode errorCode, String str) {
        super(errorCode, str);
    }

    @Override // com.miui.gallery.error.ErrorStorageNoWritePermissionTip, com.miui.gallery.error.core.ErrorTip
    public void action(Context context, ErrorActionCallback errorActionCallback) {
        if (!TextUtils.isEmpty(this.mDesc) && this.mDesc.contains("MIUI/Gallery/cloud/secretAlbum")) {
            StorageSolutionProvider.get().requestPermission((FragmentActivity) context, this.mDesc, IStoragePermissionStrategy.Permission.INSERT);
        } else {
            IntentUtil.gotoAlbumPermissionActivity(context);
        }
    }
}
