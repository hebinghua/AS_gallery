package com.miui.gallery.ui.album.cloudalbum.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.cloudalbum.viewbean.CloudAlbumItemViewBean;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryCloudAlbumList extends BaseUseCase<List<CommonAlbumItemViewBean>, Void> {
    public AbstractAlbumRepository mRepository;

    public QueryCloudAlbumList(AbstractAlbumRepository abstractAlbumRepository) {
        this.mRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<List<CommonAlbumItemViewBean>> buildUseCaseFlowable(Void r2) {
        return this.mRepository.queryCloudAlbums().map(new Function<PageResults<List<Album>>, List<CommonAlbumItemViewBean>>() { // from class: com.miui.gallery.ui.album.cloudalbum.usecase.QueryCloudAlbumList.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public List<CommonAlbumItemViewBean> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
                ArrayList arrayList = new ArrayList();
                for (Album album : pageResults.getResult()) {
                    if (!album.isBabyAlbum() && !album.isCameraAlbum() && (!album.isRubbishAlbum() || album.isManualRubbishAlbum())) {
                        CloudAlbumItemViewBean cloudAlbumItemViewBean = new CloudAlbumItemViewBean();
                        cloudAlbumItemViewBean.mapping((CloudAlbumItemViewBean) album);
                        arrayList.add(cloudAlbumItemViewBean);
                    }
                }
                return arrayList;
            }
        });
    }
}
