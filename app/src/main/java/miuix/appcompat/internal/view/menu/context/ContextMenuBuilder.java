package miuix.appcompat.internal.view.menu.context;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.EventLog;
import android.view.ContextMenu;
import android.view.View;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuDialogHelper;

/* loaded from: classes3.dex */
public class ContextMenuBuilder extends MenuBuilder implements ContextMenu {
    public ContextMenuPopupWindowHelper mHelper;

    public ContextMenuBuilder(Context context) {
        super(context);
    }

    @Override // android.view.ContextMenu
    public ContextMenu setHeaderIcon(Drawable drawable) {
        return (ContextMenu) super.setHeaderIconInt(drawable);
    }

    @Override // android.view.ContextMenu
    public ContextMenu setHeaderIcon(int i) {
        return (ContextMenu) super.setHeaderIconInt(i);
    }

    @Override // android.view.ContextMenu
    public ContextMenu setHeaderTitle(CharSequence charSequence) {
        return (ContextMenu) super.setHeaderTitleInt(charSequence);
    }

    @Override // android.view.ContextMenu
    public ContextMenu setHeaderTitle(int i) {
        return (ContextMenu) super.setHeaderTitleInt(i);
    }

    @Override // android.view.ContextMenu
    public ContextMenu setHeaderView(View view) {
        return (ContextMenu) super.setHeaderViewInt(view);
    }

    public MenuDialogHelper show(View view, IBinder iBinder) {
        if (view != null) {
            ContextMenuHelper.createContextMenu(view, this);
        }
        if (getVisibleItems().size() > 0) {
            EventLog.writeEvent(50001, 1);
            MenuDialogHelper menuDialogHelper = new MenuDialogHelper(this);
            menuDialogHelper.show(iBinder);
            return menuDialogHelper;
        }
        return null;
    }

    public ContextMenuPopupWindowHelper show(View view, IBinder iBinder, float f, float f2) {
        if (view != null) {
            ContextMenuHelper.createContextMenu(view, this);
        }
        if (getVisibleItems().size() > 0) {
            EventLog.writeEvent(50001, 1);
            ContextMenuPopupWindowHelper contextMenuPopupWindowHelper = new ContextMenuPopupWindowHelper(this);
            this.mHelper = contextMenuPopupWindowHelper;
            contextMenuPopupWindowHelper.show(iBinder, view, f, f2);
            return this.mHelper;
        }
        return null;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder, android.view.Menu
    public void close() {
        super.close();
        ContextMenuPopupWindowHelper contextMenuPopupWindowHelper = this.mHelper;
        if (contextMenuPopupWindowHelper != null) {
            contextMenuPopupWindowHelper.dismiss();
            this.mHelper = null;
        }
    }
}
