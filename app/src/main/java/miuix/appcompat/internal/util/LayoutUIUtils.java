package miuix.appcompat.internal.util;

import android.content.Context;
import miuix.appcompat.R$dimen;

/* loaded from: classes3.dex */
public class LayoutUIUtils {
    public static boolean isLevelValid(int i) {
        return i >= 0 && i <= 2;
    }

    public static int getExtraPaddingByLevel(Context context, int i) {
        if (i != 1) {
            if (i == 2) {
                return context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_window_extra_padding_horizontal_large);
            }
            return 0;
        }
        return context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_window_extra_padding_horizontal_small);
    }
}
