package com.miui.gallery.ui.album.aialbum.usecase;

import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.ui.album.aialbum.viewbean.AIAlbumPageViewBean$FaceList;
import com.miui.gallery.ui.album.main.viewbean.ai.PeopleFaceAlbumViewBean;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryPersonList extends HotUseCase<AIAlbumPageViewBean$FaceList, Integer> {
    public AbstractAlbumRepository mAlbumRepository;

    public QueryPersonList(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<AIAlbumPageViewBean$FaceList> buildFlowable(Integer num) {
        return this.mAlbumRepository.queryPersons(num.intValue(), false).map(new Function<List<PeopleItem>, AIAlbumPageViewBean$FaceList>() { // from class: com.miui.gallery.ui.album.aialbum.usecase.QueryPersonList.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public AIAlbumPageViewBean$FaceList mo2564apply(List<PeopleItem> list) throws Exception {
                AIAlbumPageViewBean$FaceList aIAlbumPageViewBean$FaceList = new AIAlbumPageViewBean$FaceList(list.size());
                for (PeopleItem peopleItem : list) {
                    PeopleFaceAlbumViewBean peopleFaceAlbumViewBean = new PeopleFaceAlbumViewBean();
                    peopleFaceAlbumViewBean.mapping(peopleItem);
                    aIAlbumPageViewBean$FaceList.add(peopleFaceAlbumViewBean);
                }
                return aIAlbumPageViewBean$FaceList;
            }
        });
    }
}
