package com.miui.gallery.magic.special.effects.image.menu;

import android.graphics.Bitmap;
import com.miui.gallery.widget.recyclerview.Adapter;

/* loaded from: classes2.dex */
public interface IMenu$VP {
    boolean getNotFace();

    void loadFinish(Bitmap bitmap);

    void loadListData();

    void scrollTo(int i);

    void setAdapter(Adapter adapter);
}
