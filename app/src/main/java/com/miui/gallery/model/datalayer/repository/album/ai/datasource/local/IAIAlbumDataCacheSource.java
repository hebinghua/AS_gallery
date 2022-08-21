package com.miui.gallery.model.datalayer.repository.album.ai.datasource.local;

import com.miui.gallery.model.datalayer.utils.CacheBean;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.util.face.PeopleItem;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface IAIAlbumDataCacheSource {
    Flowable<CoverList> queryAIAlbumCover(Integer num, Integer num2, Integer num3);

    Flowable<List<SuggestionData>> queryLocationsAlbum(Integer num);

    Flowable<List<PeopleItem>> queryPersons(int i);

    Flowable<List<SuggestionData>> queryTagsAlbum(Integer num);

    void saveCache(CacheBean cacheBean);
}
