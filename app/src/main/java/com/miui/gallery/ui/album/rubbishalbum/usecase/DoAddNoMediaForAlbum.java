package com.miui.gallery.ui.album.rubbishalbum.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import com.miui.gallery.util.BaseMiscUtil;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public class DoAddNoMediaForAlbum extends BaseUseCase<RubbishAlbumManualHideResult, List<String>> {
    public AbstractAlbumRepository mAlbumRepository;

    public DoAddNoMediaForAlbum(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<RubbishAlbumManualHideResult> buildUseCaseFlowable(List<String> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return Flowable.error(new IllegalArgumentException("albumIds can't be null!"));
        }
        return this.mAlbumRepository.doAddNoMediaForRubbishAlbum(list);
    }
}
