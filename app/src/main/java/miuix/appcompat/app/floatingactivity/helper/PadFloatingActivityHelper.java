package miuix.appcompat.app.floatingactivity.helper;

import miuix.appcompat.app.AppCompatActivity;
import miuix.core.util.screenutils.FreeFormModeHelper;
import miuix.core.util.screenutils.SplitScreenModeHelper;

/* loaded from: classes3.dex */
public class PadFloatingActivityHelper extends TabletFloatingActivityHelper {
    public PadFloatingActivityHelper(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public boolean isFloatingModeSupport() {
        int detectScreenMode = SplitScreenModeHelper.detectScreenMode(this.mActivity);
        int detectFreeFormMode = FreeFormModeHelper.detectFreeFormMode(this.mActivity);
        return (detectFreeFormMode == 8192 && (detectScreenMode == 4100 || detectScreenMode == 4099)) || (detectFreeFormMode == 8195);
    }
}
