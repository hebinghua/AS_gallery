package miuix.appcompat.internal.view.menu.context;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import miuix.internal.widget.PopupMenuAdapter;

/* loaded from: classes3.dex */
public class ContextMenuAdapter extends PopupMenuAdapter {
    public MenuItem mLastCategorySystemOrderMenuItem;

    public ContextMenuAdapter(Context context, Menu menu) {
        super(context, menu);
    }

    @Override // miuix.internal.widget.PopupMenuAdapter
    public boolean checkMenuItem(MenuItem menuItem) {
        boolean checkMenuItem = super.checkMenuItem(menuItem);
        if (!checkMenuItem || menuItem.getOrder() != 131072) {
            return checkMenuItem;
        }
        if (this.mLastCategorySystemOrderMenuItem != null) {
            throw new IllegalStateException("Only one menu item is allowed to have CATEGORY_SYSTEM order!");
        }
        this.mLastCategorySystemOrderMenuItem = menuItem;
        return false;
    }

    public MenuItem getLastCategorySystemOrderMenuItem() {
        return this.mLastCategorySystemOrderMenuItem;
    }
}
