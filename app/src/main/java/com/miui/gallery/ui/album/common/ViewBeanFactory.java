package com.miui.gallery.ui.album.common;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.base.BaseViewBean;

/* loaded from: classes2.dex */
public interface ViewBeanFactory<T extends BaseViewBean> {
    T factory(Album album);
}
