package com.miui.gallery.editor.photo.core.imports.sticker;

import com.miui.gallery.editor.photo.core.common.model.StickerCategory;
import com.miui.gallery.editor.photo.core.common.model.StickerData;
import java.util.List;

/* loaded from: classes2.dex */
public class CategoryData extends StickerCategory {
    public List<StickerData> stickerList;

    public CategoryData(int i, short s, String str, String str2, List<StickerData> list) {
        super(i, s, str, str2);
        this.stickerList = list;
    }
}
