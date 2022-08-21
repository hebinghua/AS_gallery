package com.miui.gallery.activity;

import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.functions.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class HomePageStartupHelper2$Companion$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ HomePageStartupHelper2$Companion$$ExternalSyntheticLambda0 INSTANCE = new HomePageStartupHelper2$Companion$$ExternalSyntheticLambda0();

    @Override // io.reactivex.functions.Consumer
    public final void accept(Object obj) {
        DefaultLogger.e("HomePageStartupHelper2", "snapshot update error %s", (Throwable) obj);
    }
}
