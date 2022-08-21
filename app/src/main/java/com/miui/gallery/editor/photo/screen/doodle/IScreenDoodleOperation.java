package com.miui.gallery.editor.photo.screen.doodle;

import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.screen.base.IScreenOperationEditor;

/* loaded from: classes2.dex */
public interface IScreenDoodleOperation extends IScreenOperationEditor {
    void setAlpha(float f);

    void setColor(int i);

    void setDoodleData(DoodleData doodleData, int i);

    void setSize(int i);
}
