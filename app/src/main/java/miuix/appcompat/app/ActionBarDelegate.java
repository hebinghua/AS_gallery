package miuix.appcompat.app;

import android.view.MenuItem;

/* loaded from: classes3.dex */
public interface ActionBarDelegate {
    ActionBar createActionBar();

    void invalidateOptionsMenu();

    boolean onMenuItemSelected(int i, MenuItem menuItem);
}
