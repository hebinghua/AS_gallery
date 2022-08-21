package com.miui.gallery.ui.album.main.base;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.MediaGroupTypeViewBean;
import com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$V;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseAlbumTabContract$P<VIEW extends BaseAlbumTabContract$V> extends BaseAlbumPagePresenter<VIEW> {
    public abstract void addOnDataProcessingCallback(AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback onDataProcessingCallback);

    public abstract BaseViewBean getAIAlbumBean();

    public abstract AlbumDataListResult getAlbumDataResult();

    public abstract BaseViewBean getCleanerBean();

    public abstract <T extends BaseViewBean> List<T> getGroupDatas(String str);

    public abstract String getGroupType(Album album);

    public abstract List<MediaGroupTypeViewBean> getMediaTypeGroups();

    public abstract BaseViewBean getOtherAlbumBean();

    public abstract BaseViewBean getRubbishAlbumBean();

    public abstract String[] getSupportGroups();

    public abstract BaseViewBean getTrashAlbumBean();

    public abstract void initAll();

    public abstract void initPart();

    public abstract boolean isCanShowTrashAlbum();

    public abstract boolean isEnableAlbumById(int i);

    public abstract boolean isEnableDragMode();

    public abstract boolean isNeedLoadAdvanceAIAlbum();

    public abstract boolean isNeedLoadAdvanceOtherAlbum();

    public abstract void setEnableAlbumById(int i, boolean z);
}
