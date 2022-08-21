package com.miui.gallery.editor.photo.screen.shell;

import android.graphics.RectF;
import com.miui.gallery.editor.photo.screen.base.IScreenOperationEditor;

/* loaded from: classes2.dex */
public interface IScreenShellOperation extends IScreenOperationEditor {
    RectF getShellFitMargin();

    boolean isShellStatusChangedForLastRequest();

    boolean isWithShell();
}
