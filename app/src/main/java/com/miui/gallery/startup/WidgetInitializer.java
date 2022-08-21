package com.miui.gallery.startup;

import android.content.Context;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.request.utils.Request;

/* loaded from: classes2.dex */
public class WidgetInitializer {
    public void initialize(Context context) {
        DefaultLogger.d("WidgetInitializer", "---log---widgetProvider progress start");
        Request.init(context);
        TrackController.init(context);
        StorageSolutionProvider.init(context);
        LibraryManager.getInstance().init(context);
    }
}
