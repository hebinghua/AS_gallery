package com.miui.gallery.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryMamlView extends BaseMamlView {
    public boolean isActive;

    public GalleryMamlView(Context context, View view, String str) {
        super(context, str);
        if (getView() != null) {
            ((ViewGroup) view).addView(getView(), 0, new ViewGroup.LayoutParams(-2, -2));
            getView().setClickable(false);
        }
    }

    public void switchActive() {
        if (this.isActive) {
            unActive();
        } else {
            active();
        }
    }

    public void active() {
        if (getView() == null) {
            return;
        }
        this.isActive = true;
        getView().putVariableNumber("isActive", 1.0d);
        DefaultLogger.d("GalleryMamlView", "active");
    }

    public void unActive() {
        if (getView() == null) {
            return;
        }
        this.isActive = false;
        getView().putVariableNumber("isActive", SearchStatUtils.POW);
        DefaultLogger.d("GalleryMamlView", "unActive");
    }
}
