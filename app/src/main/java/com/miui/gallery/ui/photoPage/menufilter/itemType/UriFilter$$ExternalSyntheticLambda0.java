package com.miui.gallery.ui.photoPage.menufilter.itemType;

import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import java.util.function.BiConsumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class UriFilter$$ExternalSyntheticLambda0 implements BiConsumer {
    public static final /* synthetic */ UriFilter$$ExternalSyntheticLambda0 INSTANCE = new UriFilter$$ExternalSyntheticLambda0();

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        PhotoPageMenuManager.MenuItemType menuItemType = (PhotoPageMenuManager.MenuItemType) obj;
        ((FilterResult) obj2).setSupport(false);
    }
}
