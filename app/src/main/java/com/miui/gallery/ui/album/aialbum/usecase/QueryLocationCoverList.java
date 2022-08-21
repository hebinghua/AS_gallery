package com.miui.gallery.ui.album.aialbum.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.ui.album.aialbum.viewbean.AIAlbumPageViewBean$LocationList;
import com.miui.gallery.ui.album.aialbum.viewbean.LocationAndTagsAlbumItemViewBean;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryLocationCoverList extends BaseUseCase<AIAlbumPageViewBean$LocationList, Integer> {
    public final AbstractAlbumRepository mAlbumRepository;

    public QueryLocationCoverList(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<AIAlbumPageViewBean$LocationList> buildUseCaseFlowable(Integer num) {
        return this.mAlbumRepository.queryLocationsAlbum(num).map(new Function<List<SuggestionData>, AIAlbumPageViewBean$LocationList>() { // from class: com.miui.gallery.ui.album.aialbum.usecase.QueryLocationCoverList.1
            /* JADX WARN: Type inference failed for: r0v0, types: [com.miui.gallery.ui.album.aialbum.viewbean.AIAlbumPageViewBean$LocationList, java.util.ArrayList] */
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public AIAlbumPageViewBean$LocationList mo2564apply(List<SuggestionData> list) throws Exception {
                ?? r0 = new ArrayList<LocationAndTagsAlbumItemViewBean>(list.size()) { // from class: com.miui.gallery.ui.album.aialbum.viewbean.AIAlbumPageViewBean$LocationList
                };
                for (SuggestionData suggestionData : list) {
                    LocationAndTagsAlbumItemViewBean locationAndTagsAlbumItemViewBean = new LocationAndTagsAlbumItemViewBean();
                    locationAndTagsAlbumItemViewBean.mapping(suggestionData);
                    r0.add(locationAndTagsAlbumItemViewBean);
                }
                return r0;
            }
        });
    }
}
