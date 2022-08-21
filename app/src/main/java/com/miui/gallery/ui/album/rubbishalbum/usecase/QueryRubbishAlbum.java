package com.miui.gallery.ui.album.rubbishalbum.usecase;

import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import com.miui.gallery.ui.album.rubbishalbum.usecase.QueryRubbishAlbum;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/* loaded from: classes2.dex */
public class QueryRubbishAlbum extends HotUseCase<Optional<AlbumTabToolItemBean>, Integer> {
    public AbstractAlbumRepository mAlbumRepository;

    public QueryRubbishAlbum(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<Optional<AlbumTabToolItemBean>> buildFlowable(Integer num) {
        return this.mAlbumRepository.queryRubbishAlbum(num).map(new AnonymousClass1());
    }

    /* renamed from: com.miui.gallery.ui.album.rubbishalbum.usecase.QueryRubbishAlbum$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Function<PageResults<List<Album>>, Optional<AlbumTabToolItemBean>> {
        public static /* synthetic */ AlbumTabToolItemBean $r8$lambda$LteV1iAKLdjcEMFo14ultF2DB5A(List list, AlbumTabToolItemBean albumTabToolItemBean) {
            return lambda$apply$0(list, albumTabToolItemBean);
        }

        public AnonymousClass1() {
            QueryRubbishAlbum.this = r1;
        }

        @Override // io.reactivex.functions.Function
        /* renamed from: apply */
        public Optional<AlbumTabToolItemBean> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
            final List<Album> result = pageResults.getResult();
            return Optional.ofNullable(QueryRubbishAlbum.this.getRubbishAlbumBean()).map(new java.util.function.Function() { // from class: com.miui.gallery.ui.album.rubbishalbum.usecase.QueryRubbishAlbum$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return QueryRubbishAlbum.AnonymousClass1.$r8$lambda$LteV1iAKLdjcEMFo14ultF2DB5A(result, (AlbumTabToolItemBean) obj);
                }
            });
        }

        public static /* synthetic */ AlbumTabToolItemBean lambda$apply$0(List list, AlbumTabToolItemBean albumTabToolItemBean) {
            Iterator it = list.iterator();
            int i = 0;
            while (it.hasNext()) {
                i += ((Album) it.next()).getPhotoCount();
            }
            albumTabToolItemBean.setSubTitle(StringUtils.getNumberStringInRange(i));
            return albumTabToolItemBean;
        }
    }

    public final AlbumTabToolItemBean getRubbishAlbumBean() {
        return CloudControlStrategyHelper.getAlbumTabToolsStrategy().getToolById(2147483636L);
    }
}
