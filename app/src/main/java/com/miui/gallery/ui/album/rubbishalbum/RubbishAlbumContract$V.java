package com.miui.gallery.ui.album.rubbishalbum;

import com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment;
import com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.rubbishalbum.viewbean.RubbishItemItemViewBean;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class RubbishAlbumContract$V<P extends BaseAlbumPagePresenter> extends BaseAlbumPageFragment<P> {
    public abstract void doAddRemoveNoMediaForRubbishAlbum(List<Integer> list);

    public abstract void exitActionMode();

    public abstract void removeAlbumFromRubbishIsSuccess(long j, RubbishItemItemViewBean rubbishItemItemViewBean);

    public abstract void showRubbishListResult(List<RubbishItemItemViewBean> list);

    public abstract void startChoiceMode();

    public abstract void stopChoiceMode();

    public abstract void update(List<BaseViewBean> list);
}
