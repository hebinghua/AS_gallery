package com.miui.gallery.ui.album.rubbishalbum.usecase;

import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.rubbishalbum.viewbean.RubbishItemItemViewBean;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryRubbishAlbumList extends HotUseCase<List<RubbishItemItemViewBean>, Integer> {
    public AbstractAlbumRepository mAlbumRepository;

    public QueryRubbishAlbumList(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<List<RubbishItemItemViewBean>> buildFlowable(Integer num) {
        return this.mAlbumRepository.queryRubbishAlbum(num).map(new Function<PageResults<List<Album>>, List<RubbishItemItemViewBean>>() { // from class: com.miui.gallery.ui.album.rubbishalbum.usecase.QueryRubbishAlbumList.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public List<RubbishItemItemViewBean> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
                List<Album> result = pageResults.getResult();
                ArrayList arrayList = new ArrayList(result.size());
                for (Album album : result) {
                    RubbishItemItemViewBean rubbishItemItemViewBean = new RubbishItemItemViewBean();
                    rubbishItemItemViewBean.mapping(album);
                    arrayList.add(rubbishItemItemViewBean);
                }
                return arrayList;
            }
        });
    }
}
