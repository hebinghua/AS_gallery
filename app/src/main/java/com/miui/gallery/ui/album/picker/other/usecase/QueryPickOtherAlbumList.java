package com.miui.gallery.ui.album.picker.other.usecase;

import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.provider.album.config.QueryFlagsBuilder;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.common.DefaultViewBeanFactory;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryPickOtherAlbumList extends HotUseCase<List<BaseViewBean>, PickOtherAlbumOptions> {
    public final AbstractAlbumRepository mAlbumRepository;

    /* loaded from: classes2.dex */
    public static class PickOtherAlbumOptions {
        public boolean isPickOtherShareAlbum;
        public String mediaType;
    }

    public QueryPickOtherAlbumList(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<List<BaseViewBean>> buildFlowable(PickOtherAlbumOptions pickOtherAlbumOptions) {
        QueryFlagsBuilder excludeEmptyAlbum = new QueryFlagsBuilder(AlbumConstants.QueryScene.SCENE_OTHER_ALBUM_LIST).excludeRealScreenshotsAndRecorders().excludeEmptyAlbum();
        if (pickOtherAlbumOptions.isPickOtherShareAlbum) {
            excludeEmptyAlbum = excludeEmptyAlbum.joinOtherShareAlbums();
        }
        if (BaseFileMimeUtil.isImageFromMimeType(pickOtherAlbumOptions.mediaType)) {
            excludeEmptyAlbum = excludeEmptyAlbum.onlyImageMediaType();
        } else if (BaseFileMimeUtil.isVideoFromMimeType(pickOtherAlbumOptions.mediaType)) {
            excludeEmptyAlbum = excludeEmptyAlbum.onlyVideoMediaType();
        }
        return this.mAlbumRepository.queryAlbums(excludeEmptyAlbum.build(), null).map(new Function<PageResults<List<Album>>, List<BaseViewBean>>() { // from class: com.miui.gallery.ui.album.picker.other.usecase.QueryPickOtherAlbumList.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public List<BaseViewBean> mo2564apply(PageResults<List<Album>> pageResults) {
                List<Album> result = pageResults.getResult();
                if (result == null || result.isEmpty()) {
                    return CollectionUtils.emptyList();
                }
                result.sort(AlbumSortHelper.getCurrentComparator());
                DefaultViewBeanFactory defaultViewBeanFactory = DefaultViewBeanFactory.getInstance();
                ArrayList arrayList = new ArrayList(result.size());
                for (Album album : result) {
                    arrayList.add(defaultViewBeanFactory.factory(album));
                }
                return arrayList;
            }
        });
    }
}
