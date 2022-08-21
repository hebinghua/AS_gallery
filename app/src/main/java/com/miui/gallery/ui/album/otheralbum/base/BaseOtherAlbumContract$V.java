package com.miui.gallery.ui.album.otheralbum.base;

import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment;
import com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$P;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseOtherAlbumContract$V<PRESENTER extends BaseOtherAlbumContract$P> extends BaseAlbumPageFragment<PRESENTER> {
    public abstract void refreshRubbishTipView();

    public abstract void showOthersAlbumResult(List<CommonAlbumItemViewBean> list);

    public abstract void showRubbishAlbumResult(List<BaseAlbumCover> list);
}
