package com.miui.gallery.ui.album.main.base;

import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseAlbumTabContract$V<PRESENTER extends BaseAlbumTabContract$P> extends BaseAlbumPageFragment<PRESENTER> {
    public abstract BaseViewBean getOrGenerateTitleBean(long j, int i, int i2);

    public abstract void showAlbumDatas(List<BaseViewBean> list, List<EpoxyModel<?>> list2, boolean z);
}
