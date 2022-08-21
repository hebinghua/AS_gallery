package com.miui.gallery.ui.album.main.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.common.usecase.QueryAlbumsCase;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.List;

/* loaded from: classes2.dex */
public class QuerySnapAlbumList extends BaseUseCase<AlbumDataListResult, AlbumGroupByAlbumTypeFunction.Config> {
    public final QueryAlbumsCase mInternalQuery;

    public QuerySnapAlbumList(AbstractAlbumRepository abstractAlbumRepository) {
        this.mInternalQuery = new QueryAlbumsCase(this.mSubScribeThreadExecutor, this.mObserveThreadExecutor, abstractAlbumRepository);
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<AlbumDataListResult> buildUseCaseFlowable(final AlbumGroupByAlbumTypeFunction.Config config) {
        return ((Flowable) this.mInternalQuery.buildFlowable(new QueryAlbumsCase.ParamBean.Builder().selection("(coverSize > 0 OR (attributes & 16 = 0 ))").queryFromSnapSource().build()).map(new Function<PageResults<List<Album>>, List<Album>>() { // from class: com.miui.gallery.ui.album.main.usecase.QuerySnapAlbumList.2
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public List<Album> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
                return pageResults.getResult();
            }
        }).to(new AlbumGroupByAlbumTypeFunction(config))).doAfterNext(new Consumer<AlbumDataListResult>() { // from class: com.miui.gallery.ui.album.main.usecase.QuerySnapAlbumList.1
            @Override // io.reactivex.functions.Consumer
            public void accept(AlbumDataListResult albumDataListResult) throws Exception {
                if (config.getCallback() != null) {
                    config.getCallback().onProcessEnd(albumDataListResult);
                }
            }
        });
    }
}
