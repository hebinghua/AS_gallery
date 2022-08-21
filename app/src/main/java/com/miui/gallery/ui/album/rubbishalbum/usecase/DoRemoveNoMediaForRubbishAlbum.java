package com.miui.gallery.ui.album.rubbishalbum.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import com.miui.gallery.ui.album.rubbishalbum.viewbean.RubbishItemItemViewBean;
import com.miui.gallery.util.BaseMiscUtil;
import io.reactivex.Flowable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class DoRemoveNoMediaForRubbishAlbum extends BaseUseCase<RubbishAlbumManualHideResult, List<BaseViewBean>> {
    public AbstractAlbumRepository mAlbumRepository;

    public DoRemoveNoMediaForRubbishAlbum(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<RubbishAlbumManualHideResult> buildUseCaseFlowable(List<BaseViewBean> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return Flowable.error(new IllegalArgumentException("albumIds can't be null!"));
        }
        return this.mAlbumRepository.doRemoveNoMediaForRubbishAlbum(getRubbishAlbumPaths(list));
    }

    public final List<String> getRubbishAlbumPaths(List<BaseViewBean> list) {
        LinkedList linkedList = new LinkedList();
        Iterator<BaseViewBean> it = list.iterator();
        while (it.hasNext()) {
            linkedList.add(((Album) ((RubbishItemItemViewBean) it.next()).getSource()).getLocalPath());
        }
        return linkedList;
    }
}
