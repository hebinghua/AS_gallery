package miui.gallery.support;

import android.content.Context;
import android.widget.Button;
import miuix.view.EditActionMode;

/* loaded from: classes3.dex */
public class MiuiSdkCompat extends com.miui.gallery.util.MiuiSdkCompat {
    public static int getMIUISdkLevel(Context context) {
        return 23;
    }

    public static void setEditActionModeButton(Context context, Button button, int i) {
        com.miui.gallery.util.MiuiSdkCompat.setEditActionModeButton(context, button, i);
    }

    public static void setEditActionModeButton(Context context, EditActionMode editActionMode, int i, int i2) {
        com.miui.gallery.util.MiuiSdkCompat.setEditActionModeButton(context, editActionMode, i, i2);
    }

    public static boolean isSupportCutoutModeShortEdges(Context context) {
        return com.miui.gallery.util.MiuiSdkCompat.isSupportCutoutModeShortEdges(context);
    }
}
