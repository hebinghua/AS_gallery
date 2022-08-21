package com.miui.gallery.ui.album.common.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import io.reactivex.Flowable;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class DoReplaceAlbumCoverCase extends BaseUseCase<ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>, DoReplaceAlbumCoverRequestBean> {
    public AbstractAlbumRepository mRepository;

    public DoReplaceAlbumCoverCase(AbstractAlbumRepository abstractAlbumRepository) {
        this.mRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> buildUseCaseFlowable(DoReplaceAlbumCoverRequestBean doReplaceAlbumCoverRequestBean) {
        long[] jArr;
        if (doReplaceAlbumCoverRequestBean == null || (jArr = doReplaceAlbumCoverRequestBean.mAlbumIds) == null) {
            throw new IllegalArgumentException("args error");
        }
        return this.mRepository.doReplaceAlbumCover(doReplaceAlbumCoverRequestBean.mCoverId, jArr);
    }

    /* loaded from: classes2.dex */
    public static class DoReplaceAlbumCoverRequestBean {
        public long[] mAlbumIds;
        public long mCoverId;

        public DoReplaceAlbumCoverRequestBean(long j, long[] jArr) {
            this.mCoverId = j;
            this.mAlbumIds = jArr;
        }
    }
}
