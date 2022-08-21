package miuix.appcompat.internal.view.menu;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import miuix.appcompat.app.ActionBarDelegateImpl;
import miuix.internal.util.ViewUtils;
import miuix.internal.widget.ListPopup;

/* loaded from: classes3.dex */
public class ImmersionMenuPopupWindowImpl extends ListPopup implements ImmersionMenuPopupWindow {
    public ActionBarDelegateImpl mActionBarDelegate;
    public ImmersionMenuAdapter mAdapter;
    public View mLastAnchor;
    public ViewGroup mLastParent;

    public ImmersionMenuPopupWindowImpl(ActionBarDelegateImpl actionBarDelegateImpl, Menu menu) {
        super(actionBarDelegateImpl.getThemedContext());
        Context themedContext = actionBarDelegateImpl.getThemedContext();
        this.mActionBarDelegate = actionBarDelegateImpl;
        ImmersionMenuAdapter immersionMenuAdapter = new ImmersionMenuAdapter(themedContext, menu);
        this.mAdapter = immersionMenuAdapter;
        setAdapter(immersionMenuAdapter);
        setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: miuix.appcompat.internal.view.menu.ImmersionMenuPopupWindowImpl.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                MenuItem mo2603getItem = ImmersionMenuPopupWindowImpl.this.mAdapter.mo2603getItem(i);
                if (!mo2603getItem.hasSubMenu()) {
                    ImmersionMenuPopupWindowImpl.this.mActionBarDelegate.onMenuItemSelected(0, mo2603getItem);
                } else {
                    final SubMenu subMenu = mo2603getItem.getSubMenu();
                    ImmersionMenuPopupWindowImpl.this.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: miuix.appcompat.internal.view.menu.ImmersionMenuPopupWindowImpl.1.1
                        @Override // android.widget.PopupWindow.OnDismissListener
                        public void onDismiss() {
                            ImmersionMenuPopupWindowImpl.this.setOnDismissListener(null);
                            ImmersionMenuPopupWindowImpl.this.update(subMenu);
                            ImmersionMenuPopupWindowImpl immersionMenuPopupWindowImpl = ImmersionMenuPopupWindowImpl.this;
                            immersionMenuPopupWindowImpl.fastShow(immersionMenuPopupWindowImpl.mLastAnchor, ImmersionMenuPopupWindowImpl.this.mLastParent);
                        }
                    });
                }
                ImmersionMenuPopupWindowImpl.this.dismiss(true);
            }
        });
    }

    @Override // miuix.appcompat.internal.view.menu.ImmersionMenuPopupWindow
    public void update(Menu menu) {
        this.mAdapter.update(menu);
    }

    @Override // miuix.internal.widget.ListPopup
    public void show(View view, ViewGroup viewGroup) {
        this.mLastAnchor = view;
        this.mLastParent = viewGroup;
        adjustOffset(view, viewGroup);
        super.show(view, viewGroup);
    }

    public final void adjustOffset(View view, ViewGroup viewGroup) {
        int width;
        if (viewGroup == null) {
            Log.w("ImmersionMenu", "ImmersionMenuPopupWindow offset can't be adjusted without parent");
            return;
        }
        int[] iArr = new int[2];
        viewGroup.getLocationInWindow(iArr);
        int[] iArr2 = new int[2];
        view.getLocationInWindow(iArr2);
        setVerticalOffset(-(view.getHeight() + ((iArr2[1] - iArr[1]) - getOffsetFromStatusBar())));
        if (ViewUtils.isLayoutRtl(viewGroup)) {
            width = getMinMarginScreen();
        } else {
            width = (viewGroup.getWidth() - ((iArr2[0] - iArr[0]) + view.getWidth())) - getMinMarginScreen();
        }
        setHorizontalOffset(width);
    }

    @Override // miuix.appcompat.internal.view.menu.ImmersionMenuPopupWindow
    public void dismiss(boolean z) {
        dismiss();
    }
}
