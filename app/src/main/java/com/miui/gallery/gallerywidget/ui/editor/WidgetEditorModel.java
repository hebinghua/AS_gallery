package com.miui.gallery.gallerywidget.ui.editor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class WidgetEditorModel implements WidgetEditorContract$IWidgetEditorModel<ImageEntity> {
    public final Context mContext;
    public final List<ImageEntity> mImageEntityList = new ArrayList();

    public WidgetEditorModel(Context context) {
        this.mContext = context;
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorModel
    public void loadData(Intent intent) {
        if (intent == null) {
            return;
        }
        this.mImageEntityList.clear();
        int i = 0;
        boolean booleanExtra = intent.getBooleanExtra("is_from_widget_editor", false);
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("pick_sha1");
        if (!BaseMiscUtil.isValid(stringArrayListExtra)) {
            return;
        }
        if (booleanExtra) {
            ArrayList<String> stringArrayListExtra2 = intent.getStringArrayListExtra("selected_pic_path_list");
            if (stringArrayListExtra2 == null || stringArrayListExtra2.size() != stringArrayListExtra.size()) {
                return;
            }
            while (i < stringArrayListExtra.size()) {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setId(stringArrayListExtra.get(i));
                imageEntity.setPicPath(stringArrayListExtra2.get(i));
                this.mImageEntityList.add(imageEntity);
                i++;
            }
            return;
        }
        ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra("pick-result-data");
        if (parcelableArrayListExtra == null || parcelableArrayListExtra.size() != stringArrayListExtra.size()) {
            return;
        }
        while (i < stringArrayListExtra.size()) {
            ImageEntity imageEntity2 = new ImageEntity();
            imageEntity2.setId(stringArrayListExtra.get(i));
            imageEntity2.setPicPath(WidgetEditorManager.getPicPath(this.mContext, (Uri) parcelableArrayListExtra.get(i)));
            this.mImageEntityList.add(imageEntity2);
            i++;
        }
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorModel
    public List<ImageEntity> getDataList() {
        return this.mImageEntityList;
    }
}
