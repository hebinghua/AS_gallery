package com.miui.gallery.error;

import android.content.Context;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.error.core.ErrorActionCallback;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.error.core.ErrorTip;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.IntentUtil;
import java.io.File;

/* loaded from: classes2.dex */
public class ErrorStorageNoWritePermissionTip extends ErrorTip {
    public final String mDesc;

    @Override // com.miui.gallery.error.core.ErrorTip
    public CharSequence getMessage(Context context) {
        return null;
    }

    public ErrorStorageNoWritePermissionTip(ErrorCode errorCode, String str) {
        super(errorCode);
        this.mDesc = str;
    }

    @Override // com.miui.gallery.error.core.ErrorTip
    public CharSequence getTitle(Context context) {
        return context.getString(R.string.error_storage_no_write_permission_tip_title);
    }

    @Override // com.miui.gallery.error.core.ErrorTip
    public CharSequence getActionStr(Context context) {
        return context.getString(R.string.error_storage_no_write_permission_tip_action);
    }

    @Override // com.miui.gallery.error.core.ErrorTip
    public void action(Context context, ErrorActionCallback errorActionCallback) {
        if (TextUtils.isEmpty(this.mDesc)) {
            IntentUtil.gotoAlbumPermissionActivity(context);
        } else if (this.mDesc.endsWith(File.separator)) {
            StorageSolutionProvider.get().requestPermission((FragmentActivity) context, this.mDesc, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY);
        } else {
            StorageSolutionProvider.get().requestPermission((FragmentActivity) context, this.mDesc, IStoragePermissionStrategy.Permission.INSERT);
        }
    }
}
