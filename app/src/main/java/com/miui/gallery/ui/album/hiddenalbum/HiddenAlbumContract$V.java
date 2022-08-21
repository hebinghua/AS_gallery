package com.miui.gallery.ui.album.hiddenalbum;

import com.miui.gallery.app.base.BaseListPageFragment;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.ui.album.hiddenalbum.viewbean.HiddenAlbumItemViewBean;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class HiddenAlbumContract$V<T, P extends IPresenter> extends BaseListPageFragment<T, P> {
    public abstract void showHiddenListData(List<HiddenAlbumItemViewBean> list);

    public abstract void showUnHiddenAlbumIsFailed();

    public abstract void showUnHiddenAlbumIsSuccess(HiddenAlbumItemViewBean hiddenAlbumItemViewBean);
}
