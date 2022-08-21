package com.miui.gallery.ui.album.main.utils.splitgroup;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.List;

/* loaded from: classes2.dex */
public interface ISplitGroupMode {
    String getGroupType(Album album);

    String[] getSupportGroups();

    <T extends Album> Flowable<SplitGroupResult<T>> splitGroup(List<T> list, boolean z, Function<T, String> function);

    <T extends BaseViewBean> Flowable<SplitGroupResult<T>> splitGroupByViewBean(List<T> list, boolean z);

    default <T extends BaseViewBean> Flowable<SplitGroupResult<T>> splitGroupByViewBean(List<T> list) {
        return splitGroupByViewBean(list, true);
    }
}
