package com.miui.gallery.magic.idphoto.menu;

import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.widget.recyclerview.Adapter;

/* loaded from: classes2.dex */
public interface IMenu$VP {
    PhotoStyle getCurrentSize();

    void initListData();

    void initTabsData();

    void openSearch();

    void scrollToPosition(int i);

    void scrollToPositionColorItem(int i);

    void scrollToPositionItem(int i);

    void setAdapter(Adapter adapter);

    void setBgColor(int i);

    void setColorAdapter(Adapter adapter);

    void setCurrentTab(int i);

    void setTabs(String[] strArr);
}
