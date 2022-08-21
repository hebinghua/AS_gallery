package com.miui.gallery.error;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.error.core.ErrorActionCallback;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.error.core.ErrorTip;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.IntentUtil;

/* loaded from: classes2.dex */
public class ErrorNoAccountTip extends ErrorTip {
    public ErrorNoAccountTip(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override // com.miui.gallery.error.core.ErrorTip
    public CharSequence getTitle(Context context) {
        return context.getResources().getString(R.string.error_no_account_tip);
    }

    @Override // com.miui.gallery.error.core.ErrorTip
    public CharSequence getMessage(Context context) {
        return context.getResources().getString(R.string.error_no_account_msg);
    }

    @Override // com.miui.gallery.error.core.ErrorTip
    public CharSequence getActionStr(Context context) {
        return context.getResources().getString(R.string.error_no_account_action);
    }

    @Override // com.miui.gallery.error.core.ErrorTip
    public void action(Context context, ErrorActionCallback errorActionCallback) {
        IntentUtil.guideToLoginXiaomiAccount(context, GalleryIntent$CloudGuideSource.AUTOBACKUP);
        if (errorActionCallback != null) {
            errorActionCallback.onAction(this.mCode, true);
        }
    }
}
