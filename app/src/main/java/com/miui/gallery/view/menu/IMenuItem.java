package com.miui.gallery.view.menu;

import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;

/* loaded from: classes2.dex */
public interface IMenuItem extends MenuItem {
    int getIconRes();

    int getOrdering();

    char getShortcut();

    String getShortcutLabel();

    ActionProvider getSupportActionProvider();

    CharSequence getTitleForItemView(MenuView$ItemView menuView$ItemView);

    int getTitleId();

    boolean hasCollapsibleActionView();

    boolean invoke();

    boolean isExclusiveCheckable();

    default boolean isNeedFolmeAnim() {
        return true;
    }

    boolean isResident();

    default boolean isSelected() {
        return false;
    }

    boolean isSupport();

    void setCheckedInt(boolean z);

    void setExclusiveCheckable(boolean z);

    void setMenuInfo(ContextMenu.ContextMenuInfo contextMenuInfo);

    default void setNeedFolmeAnim(boolean z) {
    }

    void setResident(boolean z);

    default void setSelected(boolean z) {
    }

    void setSubMenu(SubMenuBuilder subMenuBuilder);

    void setSupport(boolean z);

    boolean setVisibleInt(boolean z);

    boolean shouldShowIcon();

    boolean shouldShowShortcut();
}
