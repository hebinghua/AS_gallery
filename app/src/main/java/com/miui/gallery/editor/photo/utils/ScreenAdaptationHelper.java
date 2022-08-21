package com.miui.gallery.editor.photo.utils;

import android.content.Context;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;

/* loaded from: classes2.dex */
public class ScreenAdaptationHelper {
    public static void updateBSBWidth(Context context, BubbleSeekBar bubbleSeekBar) {
        if (bubbleSeekBar != null) {
            bubbleSeekBar.updateWidth(context.getResources().getDimensionPixelSize(R$dimen.text_edit_dialog_tab_style_bsb_visibility_width), context.getResources().getDimensionPixelOffset(R$dimen.text_edit_dialog_tab_style_bsb_empty_width));
        }
    }
}
