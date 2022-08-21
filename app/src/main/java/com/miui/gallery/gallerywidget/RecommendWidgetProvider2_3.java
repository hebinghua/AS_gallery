package com.miui.gallery.gallerywidget;

import com.miui.gallery.R;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.common.WidgetInstallManager;

/* loaded from: classes2.dex */
public class RecommendWidgetProvider2_3 extends RecommendWidgetProviderBase {
    @Override // com.miui.gallery.gallerywidget.common.IWidgetProviderConfig
    public IWidgetProviderConfig.WidgetSize getWidgetType() {
        return IWidgetProviderConfig.WidgetSize.SIZE_2_3;
    }

    @Override // com.miui.gallery.gallerywidget.common.IWidgetProviderConfig
    public int getRemoteViewID() {
        return WidgetInstallManager.isUseSmallLayout() ? R.layout.recommend_widget_small_content2_3 : R.layout.recommend_widget_content2_3;
    }
}
