package com.miui.gallery.ui.album.otheralbum.usecase;

import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.common.DefaultViewBeanFactory;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class QueryOtherAlbumList extends HotUseCase<PageResults<List<BaseViewBean>>, Integer> {
    public boolean isNeedCacheSourceDatas;
    public AbstractAlbumRepository mAlbumRepository;

    public QueryOtherAlbumList(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.isNeedCacheSourceDatas = true;
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<PageResults<List<BaseViewBean>>> buildFlowable(Integer num) {
        return this.mAlbumRepository.queryOthersAlbum(num).flatMap(new Function<PageResults<List<Album>>, Publisher<PageResults<List<Album>>>>() { // from class: com.miui.gallery.ui.album.otheralbum.usecase.QueryOtherAlbumList.2
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<PageResults<List<Album>>> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
                if (!QueryOtherAlbumList.this.isNeedCacheSourceDatas && pageResults.getFromType() == 4) {
                    return Flowable.empty();
                }
                return Flowable.just(pageResults);
            }
        }).map(new Function<PageResults<List<Album>>, PageResults<List<BaseViewBean>>>() { // from class: com.miui.gallery.ui.album.otheralbum.usecase.QueryOtherAlbumList.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public PageResults<List<BaseViewBean>> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
                if (pageResults == null || pageResults.isEmpty()) {
                    return new PageResults<>(CollectionUtils.emptyList());
                }
                List<Album> result = pageResults.getResult();
                result.sort(AlbumSortHelper.getCurrentComparator());
                ArrayList arrayList = new ArrayList(result.size());
                for (Album album : result) {
                    BaseViewBean factory = DefaultViewBeanFactory.getInstance().factory(album);
                    if (factory != null) {
                        arrayList.add(factory);
                    }
                }
                return new PageResults<>(pageResults.getFromType(), arrayList);
            }
        });
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase, com.miui.gallery.base_optimization.clean.LifecycleUseCase
    public void onStart() {
        this.isNeedCacheSourceDatas = false;
        super.onStart();
    }
}
