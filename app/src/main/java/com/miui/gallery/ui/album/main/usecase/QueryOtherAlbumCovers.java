package com.miui.gallery.ui.album.main.usecase;

import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.main.viewbean.OtherAlbumGridCoverViewBean;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryOtherAlbumCovers extends HotUseCase<OtherAlbumGridCoverViewBean, RequestParam> {
    public AbstractAlbumRepository mAlbumRepository;

    public QueryOtherAlbumCovers(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<OtherAlbumGridCoverViewBean> buildFlowable(final RequestParam requestParam) {
        return this.mAlbumRepository.queryOtherAlbumCovers().map(new Function<PageResults<CoverList>, OtherAlbumGridCoverViewBean>() { // from class: com.miui.gallery.ui.album.main.usecase.QueryOtherAlbumCovers.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public OtherAlbumGridCoverViewBean mo2564apply(PageResults<CoverList> pageResults) throws Exception {
                CoverList result = pageResults.getResult();
                RequestParam requestParam2 = requestParam;
                if ((requestParam2 != null && requestParam2.isIgnoreCache() && pageResults.isFromFile()) || !result.isValid()) {
                    return new OtherAlbumGridCoverViewBean();
                }
                OtherAlbumGridCoverViewBean otherAlbumGridCoverViewBean = new OtherAlbumGridCoverViewBean();
                otherAlbumGridCoverViewBean.mapping(result);
                List<BaseAlbumCover> covers = result.getCovers();
                int i = 0;
                for (BaseAlbumCover baseAlbumCover : covers) {
                    if (!baseAlbumCover.isValid()) {
                        i++;
                    }
                }
                if (i == covers.size()) {
                    otherAlbumGridCoverViewBean.setCovers(Collections.emptyList());
                }
                return otherAlbumGridCoverViewBean;
            }
        });
    }

    /* loaded from: classes2.dex */
    public static class RequestParam {
        public boolean isIgnoreCache;

        public RequestParam(boolean z) {
            this.isIgnoreCache = z;
        }

        public boolean isIgnoreCache() {
            return this.isIgnoreCache;
        }
    }
}
