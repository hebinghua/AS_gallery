package miuix.appcompat.app.floatingactivity.helper;

import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.SplitUtils;
import miuix.internal.util.DeviceHelper;

/* loaded from: classes3.dex */
public class FloatingHelperFactory {
    public static BaseFloatingActivityHelper get(AppCompatActivity appCompatActivity) {
        boolean isIntentFromSettingsSplit = SplitUtils.isIntentFromSettingsSplit(appCompatActivity.getIntent());
        if (!isIntentFromSettingsSplit && DeviceHelper.isFoldDevice()) {
            return new FoldFloatingActivityHelper(appCompatActivity);
        }
        if (!isIntentFromSettingsSplit && DeviceHelper.isTablet(appCompatActivity)) {
            return new PadFloatingActivityHelper(appCompatActivity);
        }
        return new PhoneFloatingActivityHelper(appCompatActivity);
    }
}
