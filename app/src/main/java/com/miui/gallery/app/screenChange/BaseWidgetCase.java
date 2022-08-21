package com.miui.gallery.app.screenChange;

import android.content.res.Configuration;
import android.os.Bundle;

/* loaded from: classes.dex */
public abstract class BaseWidgetCase extends WidgetCase {
    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public void handleRestoreInstance(Bundle bundle, Configuration configuration) {
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public void handleWhenSaveInstance(Bundle bundle, Configuration configuration) {
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public boolean needHandleInstance() {
        return false;
    }
}
