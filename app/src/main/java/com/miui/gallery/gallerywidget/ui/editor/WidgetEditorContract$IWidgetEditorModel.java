package com.miui.gallery.gallerywidget.ui.editor;

import android.content.Intent;
import java.util.List;

/* loaded from: classes2.dex */
public interface WidgetEditorContract$IWidgetEditorModel<T> {
    List<T> getDataList();

    void loadData(Intent intent);
}
