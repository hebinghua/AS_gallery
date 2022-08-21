package com.miui.gallery.ui.album.aialbum.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.ui.album.aialbum.viewbean.AIAlbumPageViewBean$TagList;
import com.miui.gallery.ui.album.aialbum.viewbean.LocationAndTagsAlbumItemViewBean;
import com.miui.gallery.ui.album.aialbum.viewbean.TagsAlbumItemViewBean;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryTagsCoverList extends BaseUseCase<AIAlbumPageViewBean$TagList, Integer> {
    public final AbstractAlbumRepository mAlbumRepository;

    public QueryTagsCoverList(AbstractAlbumRepository abstractAlbumRepository) {
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<AIAlbumPageViewBean$TagList> buildUseCaseFlowable(Integer num) {
        return this.mAlbumRepository.queryTagsAlbum(num).map(new Function<List<SuggestionData>, AIAlbumPageViewBean$TagList>() { // from class: com.miui.gallery.ui.album.aialbum.usecase.QueryTagsCoverList.1
            /* JADX WARN: Type inference failed for: r0v0, types: [com.miui.gallery.ui.album.aialbum.viewbean.AIAlbumPageViewBean$TagList, java.util.ArrayList] */
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public AIAlbumPageViewBean$TagList mo2564apply(List<SuggestionData> list) throws Exception {
                ?? r0 = new ArrayList<LocationAndTagsAlbumItemViewBean>(list.size()) { // from class: com.miui.gallery.ui.album.aialbum.viewbean.AIAlbumPageViewBean$TagList
                };
                for (SuggestionData suggestionData : list) {
                    TagsAlbumItemViewBean tagsAlbumItemViewBean = new TagsAlbumItemViewBean();
                    tagsAlbumItemViewBean.mapping(suggestionData);
                    r0.add(tagsAlbumItemViewBean);
                }
                return r0;
            }
        });
    }
}
