package miuix.appcompat.app.floatingactivity.helper;

import miuix.appcompat.app.AppCompatActivity;
import miuix.core.util.screenutils.FreeFormModeHelper;

/* loaded from: classes3.dex */
public class FoldFloatingActivityHelper extends TabletFloatingActivityHelper {
    public FoldFloatingActivityHelper(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public boolean isFloatingModeSupport() {
        boolean z = this.mActivity.getResources().getConfiguration().smallestScreenWidthDp >= 600;
        int detectFreeFormMode = FreeFormModeHelper.detectFreeFormMode(this.mActivity);
        return z && (detectFreeFormMode == 8192 || detectFreeFormMode == 8195);
    }
}
