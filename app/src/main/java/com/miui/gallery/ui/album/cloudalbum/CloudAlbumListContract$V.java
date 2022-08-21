package com.miui.gallery.ui.album.cloudalbum;

import com.miui.gallery.app.base.BaseListPageFragment;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.ui.album.cloudalbum.viewbean.CloudAlbumItemViewBean;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class CloudAlbumListContract$V<T, P extends IPresenter> extends BaseListPageFragment<T, P> {
    public abstract void showChangeAlbumCantBeRubbishAlbum(CloudAlbumItemViewBean cloudAlbumItemViewBean);

    public abstract void showChangeAlbumCantBeShareAlbum(CloudAlbumItemViewBean cloudAlbumItemViewBean);

    public abstract void showChangeAlbumUploadStatusFailed(CloudAlbumItemViewBean cloudAlbumItemViewBean);

    public abstract void showChangeAlbumUploadStatusSuccess(CloudAlbumItemViewBean cloudAlbumItemViewBean, boolean z);

    public abstract void showCloudList(List<CloudAlbumItemViewBean> list);
}
