package com.miui.gallery.gallerywidget;

import com.miui.gallery.R;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.common.WidgetInstallManager;

/* loaded from: classes2.dex */
public class RecommendWidgetProvider4_2 extends RecommendWidgetProviderBase {
    @Override // com.miui.gallery.gallerywidget.common.IWidgetProviderConfig
    public IWidgetProviderConfig.WidgetSize getWidgetType() {
        return IWidgetProviderConfig.WidgetSize.SIZE_4_2;
    }

    @Override // com.miui.gallery.gallerywidget.common.IWidgetProviderConfig
    public int getRemoteViewID() {
        return WidgetInstallManager.isUseSmallLayout() ? R.layout.recommend_widget_small_content4_2 : R.layout.recommend_widget_content4_2;
    }
}
