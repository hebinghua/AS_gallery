package com.miui.gallery.ui;

import android.content.Context;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.app.screenChange.ScreenSize;
import com.miui.gallery.model.DiscoveryMessage;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.SwitchView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DiscoverChangeManager implements IScreenChange.OnLargeScreenChangeListener {
    public ActionBarDiscoveryWidget mActionBarDiscoveryIcon;
    public HomePageActivity.HomeTabActionBarHelper mActionBarHelper;
    public Context mContext;
    public DiscoveryDot mDiscoveryDot;
    public List<DiscoveryMessage> mDiscoveryMessages;
    public DiscoveryWidget mDiscoveryWidgetTarget;
    public SwitchView mSwitchView;

    public DiscoverChangeManager(Context context, HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper, SwitchView switchView) {
        this.mContext = context;
        this.mActionBarHelper = homeTabActionBarHelper;
        this.mSwitchView = switchView;
    }

    public void setDiscoverDotShowEnable(boolean z) {
        DiscoveryDot discoveryDot = this.mDiscoveryDot;
        if (discoveryDot != null) {
            discoveryDot.setShowEnable(z);
        }
    }

    public void refreshUiVisible(boolean z) {
        ActionBarDiscoveryWidget actionBarDiscoveryWidget = this.mActionBarDiscoveryIcon;
        if (actionBarDiscoveryWidget != null) {
            actionBarDiscoveryWidget.refreshIconVisibility(z);
        }
    }

    public void setDiscoveryMessage(ArrayList<DiscoveryMessage> arrayList) {
        if (arrayList != null) {
            DefaultLogger.i("DiscoverWidgetManager", "messages.size()=%s", Integer.valueOf(arrayList.size()));
        }
        this.mDiscoveryMessages = arrayList;
        this.mDiscoveryWidgetTarget.setMessages(arrayList);
    }

    public void refreshDiscoveryView() {
        this.mDiscoveryWidgetTarget.setMessages(this.mDiscoveryMessages);
    }

    public DiscoveryDot getDiscoveryDot() {
        return this.mDiscoveryDot;
    }

    @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
    public void onCreatedWhileLargeDevice(ScreenSize screenSize) {
        this.mDiscoveryDot = new DiscoveryDot(this.mContext, this.mSwitchView);
        this.mActionBarDiscoveryIcon = new ActionBarDiscoveryWidget(this.mContext, this.mActionBarHelper);
        if (screenSize.isWindowHorizontalLarge()) {
            this.mDiscoveryWidgetTarget = this.mActionBarDiscoveryIcon;
        } else {
            this.mDiscoveryWidgetTarget = this.mDiscoveryDot;
        }
        refreshInformation(true);
    }

    @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
    public void onCreatedWhileNormalDevice(ScreenSize screenSize) {
        DiscoveryDot discoveryDot = new DiscoveryDot(this.mContext, this.mSwitchView);
        this.mDiscoveryDot = discoveryDot;
        this.mDiscoveryWidgetTarget = discoveryDot;
        refreshInformation(false);
    }

    @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
    public void onScreenSizeToLargeOrNormal(ScreenSize screenSize) {
        if (screenSize.isWindowHorizontalLarge()) {
            this.mDiscoveryWidgetTarget = this.mActionBarDiscoveryIcon;
            refreshInformation(true);
            return;
        }
        this.mDiscoveryWidgetTarget = this.mDiscoveryDot;
        refreshInformation(false);
    }

    public void refreshInformation(boolean z) {
        refreshUiVisible(z && this.mActionBarHelper.getCurrentPosition() == 0);
        this.mDiscoveryWidgetTarget.setMessages(this.mDiscoveryMessages);
    }
}
