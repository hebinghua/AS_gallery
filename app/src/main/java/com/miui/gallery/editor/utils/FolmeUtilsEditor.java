package com.miui.gallery.editor.utils;

import android.view.View;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class FolmeUtilsEditor {
    public static void animButton(View view) {
        FolmeUtil.setCustomTouchAnim(view, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), new AnimParams.Builder().setAlpha(1.0f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
    }
}
