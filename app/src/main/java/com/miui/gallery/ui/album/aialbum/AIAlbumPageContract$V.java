package com.miui.gallery.ui.album.aialbum;

import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.app.base.BaseListPageFragment;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.ui.album.common.CustomViewItemViewBean;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class AIAlbumPageContract$V<P extends IPresenter> extends BaseListPageFragment<BaseViewBean, P> {
    public abstract CustomViewItemViewBean generateLocationGroupTitle();

    public abstract CustomViewItemViewBean generatePeopleGroupTitle();

    public abstract CustomViewItemViewBean generateTagsGroupTitle();

    public abstract int getFaceAlbumLoadNumber();

    public abstract int getLocationAlbumLoadNumber();

    public abstract int getTagAlbumLoadNumber();

    public abstract boolean isMapAlbumAvailable();

    public abstract void loadPageDatasIsSuccess();

    public abstract void showPageDatas(List<BaseViewBean> list, List<EpoxyModel<?>> list2);
}
