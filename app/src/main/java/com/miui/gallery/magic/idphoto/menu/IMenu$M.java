package com.miui.gallery.magic.idphoto.menu;

import com.miui.gallery.magic.idphoto.bean.CategoryColorItem;
import com.miui.gallery.magic.idphoto.bean.CategoryItem;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public interface IMenu$M {
    ArrayList<CategoryColorItem> getListColorData();

    ArrayList<CategoryItem> getListData();

    int getMapCategorySizeFromTabs(int i);

    int getTabIndex(String str);

    String[] getTabsData();

    int[] getWidthHeight(String str, String str2);
}
