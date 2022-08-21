package miuix.appcompat.internal.app.widget;

import android.content.Context;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$layout;

/* loaded from: classes3.dex */
public class SecondaryExpandTabContainer extends ScrollingTabContainerView {
    @Override // miuix.appcompat.internal.app.widget.ScrollingTabContainerView
    public int getTabContainerHeight() {
        return -2;
    }

    public SecondaryExpandTabContainer(Context context) {
        super(context);
        setContentHeight(getTabContainerHeight());
    }

    @Override // miuix.appcompat.internal.app.widget.ScrollingTabContainerView
    public int getTabBarLayoutRes() {
        return R$layout.miuix_appcompat_action_bar_tabbar_secondary;
    }

    @Override // miuix.appcompat.internal.app.widget.ScrollingTabContainerView
    public int getTabViewLayoutRes() {
        return R$layout.miuix_appcompat_action_bar_tab_secondary;
    }

    @Override // miuix.appcompat.internal.app.widget.ScrollingTabContainerView
    public int getTabViewMarginHorizontal() {
        return getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_tab_secondary_margin);
    }

    @Override // miuix.appcompat.internal.app.widget.ScrollingTabContainerView
    public int getTabTextStyle() {
        return R$attr.actionBarTabTextSecondaryExpandStyle;
    }
}
