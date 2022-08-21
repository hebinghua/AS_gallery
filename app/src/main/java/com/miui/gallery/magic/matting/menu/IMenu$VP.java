package com.miui.gallery.magic.matting.menu;

import android.graphics.Bitmap;
import com.miui.gallery.magic.matting.bean.BackgroundItem;
import com.miui.gallery.widget.recyclerview.Adapter;

/* loaded from: classes2.dex */
public interface IMenu$VP {
    Bitmap getBackgroundBitmap(String str);

    BackgroundItem getBackgroundItem();

    void initListData();

    void onPaintColorSelected(int i);

    void onPaintSizeSelected(int i);

    void scrollTo(int i);

    void scrollToPosition(int i);

    void setAdapter(Adapter adapter);

    void setSelectBackgroundIndex(int i);

    void setStrokeWidthToProgress(int i);

    void showPaintSelect(boolean z);
}
