package com.miui.gallery.adapter;

import com.miui.gallery.provider.cache.IMediaSnapshot;
import kotlin.jvm.internal.PropertyReference1Impl;

/* compiled from: HomePageAdapter2.kt */
/* loaded from: classes.dex */
public /* synthetic */ class HomePageAdapter2$Companion$SORT_TIME_PROPS$1 extends PropertyReference1Impl {
    public static final HomePageAdapter2$Companion$SORT_TIME_PROPS$1 INSTANCE = new HomePageAdapter2$Companion$SORT_TIME_PROPS$1();

    public HomePageAdapter2$Companion$SORT_TIME_PROPS$1() {
        super(IMediaSnapshot.class, "sortTime", "getSortTime()J", 0);
    }

    @Override // kotlin.jvm.internal.PropertyReference1Impl, kotlin.reflect.KProperty1
    public Object get(Object obj) {
        return Long.valueOf(((IMediaSnapshot) obj).getSortTime());
    }
}
