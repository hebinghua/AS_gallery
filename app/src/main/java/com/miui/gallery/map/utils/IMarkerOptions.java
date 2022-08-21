package com.miui.gallery.map.utils;

import com.miui.gallery.map.view.BitmapDescriptorWrapper;

/* loaded from: classes2.dex */
public interface IMarkerOptions<T> {
    BitmapDescriptorWrapper getIcon();

    /* renamed from: getOptions */
    T mo1087getOptions();

    IMarkerOptions<T> icon(BitmapDescriptorWrapper bitmapDescriptorWrapper);
}
