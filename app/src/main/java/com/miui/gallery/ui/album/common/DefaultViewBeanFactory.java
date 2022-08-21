package com.miui.gallery.ui.album.common;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.common.viewbean.ShareAlbumViewBean;
import com.miui.gallery.ui.album.common.viewbean.SystemAlbumViewBean;
import com.miui.gallery.ui.album.hiddenalbum.viewbean.HiddenAlbumItemViewBean;
import com.miui.gallery.ui.album.rubbishalbum.viewbean.RubbishItemItemViewBean;

/* loaded from: classes2.dex */
public class DefaultViewBeanFactory implements ViewBeanFactory<BaseViewBean> {
    public DefaultViewBeanFactory() {
    }

    public static DefaultViewBeanFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final DefaultViewBeanFactory INSTANCE = new DefaultViewBeanFactory();
    }

    @Override // com.miui.gallery.ui.album.common.ViewBeanFactory
    public BaseViewBean factory(Album album) {
        BaseViewBean shareAlbumViewBean;
        if (album.isSystemAlbum()) {
            shareAlbumViewBean = new SystemAlbumViewBean();
        } else if (album.isRubbishAlbum()) {
            shareAlbumViewBean = new RubbishItemItemViewBean();
        } else if (album.isHiddenAlbum()) {
            shareAlbumViewBean = new HiddenAlbumItemViewBean();
        } else if (album.isShareAlbum() || album.isShareToDevice()) {
            shareAlbumViewBean = new ShareAlbumViewBean();
        } else {
            shareAlbumViewBean = new CommonAlbumItemViewBean();
        }
        shareAlbumViewBean.mapping(album);
        return shareAlbumViewBean;
    }
}
