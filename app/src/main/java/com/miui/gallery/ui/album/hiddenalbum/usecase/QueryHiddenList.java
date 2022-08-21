package com.miui.gallery.ui.album.hiddenalbum.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.common.DefaultViewBeanFactory;
import com.miui.gallery.ui.album.hiddenalbum.viewbean.HiddenAlbumItemViewBean;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryHiddenList extends BaseUseCase<List<HiddenAlbumItemViewBean>, Void> {
    public AbstractAlbumRepository mAlbumRepository;

    public QueryHiddenList(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<List<HiddenAlbumItemViewBean>> buildUseCaseFlowable(Void r2) {
        return this.mAlbumRepository.queryHiddenAlbum().map(new Function<PageResults<List<Album>>, List<HiddenAlbumItemViewBean>>() { // from class: com.miui.gallery.ui.album.hiddenalbum.usecase.QueryHiddenList.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public List<HiddenAlbumItemViewBean> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
                LinkedList linkedList = new LinkedList();
                for (Album album : pageResults.getResult()) {
                    linkedList.add((HiddenAlbumItemViewBean) DefaultViewBeanFactory.getInstance().factory(album));
                }
                return linkedList;
            }
        });
    }
}
