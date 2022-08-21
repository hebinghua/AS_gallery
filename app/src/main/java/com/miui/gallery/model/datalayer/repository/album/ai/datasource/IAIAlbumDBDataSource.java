package com.miui.gallery.model.datalayer.repository.album.ai.datasource;

import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.util.face.PeopleItem;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface IAIAlbumDBDataSource {
    Flowable<CoverList> queryAIAlbumCover(Integer num, Integer num2, Integer num3);

    Flowable<List<SuggestionData>> queryLocationsAlbum(Integer num);

    Flowable<List<PeopleItem>> queryPeopleFaceSnapList(int i);

    Flowable<List<PeopleItem>> queryPersons(int i, boolean z);

    Flowable<List<SuggestionData>> queryTagsAlbum(Integer num);
}
