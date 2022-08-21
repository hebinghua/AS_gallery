package com.miui.gallery.util;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Button;
import miuix.appcompat.R$drawable;
import miuix.appcompat.R$string;
import miuix.view.EditActionMode;

/* loaded from: classes2.dex */
public class MiuiSdkCompat {
    public static int getMIUISdkLevel(Context context) {
        return 23;
    }

    public static boolean isSupportCutoutModeShortEdges(Context context) {
        return true;
    }

    public static void setEditActionModeButton(Context context, Button button, int i) {
        if (context == null || button == null) {
            return;
        }
        setEditActionModeButtonByIcon(context, button, i);
    }

    public static void setEditActionModeButtonByIcon(Context context, Button button, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        button.setText("");
        Resources resources = context.getResources();
        if (i == 0) {
            if (isNightMode(context)) {
                i2 = R$drawable.miuix_appcompat_action_mode_title_button_select_all_dark;
            } else {
                i2 = R$drawable.miuix_appcompat_action_mode_title_button_select_all_light;
            }
            button.setBackgroundResource(i2);
            button.setContentDescription(resources.getString(R$string.miuix_appcompat_select_all));
        } else if (i == 1) {
            if (isNightMode(context)) {
                i3 = R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_dark;
            } else {
                i3 = R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_light;
            }
            button.setBackgroundResource(i3);
            button.setContentDescription(resources.getString(R$string.miuix_appcompat_deselect_all));
        } else if (i == 2) {
            if (isNightMode(context)) {
                i4 = R$drawable.miuix_appcompat_action_mode_title_button_confirm_dark;
            } else {
                i4 = R$drawable.miuix_appcompat_action_mode_title_button_confirm_light;
            }
            button.setBackgroundResource(i4);
            button.setContentDescription(resources.getString(17039370));
        } else if (i != 3) {
        } else {
            if (isNightMode(context)) {
                i5 = R$drawable.miuix_appcompat_action_mode_title_button_cancel_dark;
            } else {
                i5 = R$drawable.miuix_appcompat_action_mode_title_button_cancel_light;
            }
            button.setBackgroundResource(i5);
            button.setContentDescription(resources.getString(17039360));
        }
    }

    public static void setEditActionModeButton(Context context, EditActionMode editActionMode, int i, int i2) {
        if (context == null || editActionMode == null) {
            return;
        }
        setEditActionModeButtonByIcon(context, editActionMode, i, i2);
    }

    public static void setEditActionModeButtonByIcon(Context context, EditActionMode editActionMode, int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        if (i2 == 0) {
            if (isNightMode(context)) {
                i3 = R$drawable.miuix_appcompat_action_mode_title_button_select_all_dark;
            } else {
                i3 = R$drawable.miuix_appcompat_action_mode_title_button_select_all_light;
            }
            editActionMode.setButton(i, "", i3);
        } else if (i2 == 1) {
            if (isNightMode(context)) {
                i4 = R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_dark;
            } else {
                i4 = R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_light;
            }
            editActionMode.setButton(i, "", i4);
        } else if (i2 == 2) {
            if (isNightMode(context)) {
                i5 = R$drawable.miuix_appcompat_action_mode_title_button_confirm_dark;
            } else {
                i5 = R$drawable.miuix_appcompat_action_mode_title_button_confirm_light;
            }
            editActionMode.setButton(i, "", i5);
        } else if (i2 != 3) {
        } else {
            if (isNightMode(context)) {
                i6 = R$drawable.miuix_appcompat_action_mode_title_button_cancel_dark;
            } else {
                i6 = R$drawable.miuix_appcompat_action_mode_title_button_cancel_light;
            }
            editActionMode.setButton(i, "", i6);
        }
    }

    public static boolean isNightMode(Context context) {
        return context != null && (context.getResources().getConfiguration().uiMode & 48) == 32;
    }
}
