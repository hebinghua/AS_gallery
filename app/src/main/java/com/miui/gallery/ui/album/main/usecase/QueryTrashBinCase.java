package com.miui.gallery.ui.album.main.usecase;

import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import com.miui.gallery.ui.album.main.usecase.QueryTrashBinCase;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.Optional;

/* loaded from: classes2.dex */
public class QueryTrashBinCase extends HotUseCase<Optional<AlbumTabToolItemBean>, Void> {
    public AbstractAlbumRepository mAlbumRepository;

    public QueryTrashBinCase(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<Optional<AlbumTabToolItemBean>> buildFlowable(Void r2) {
        return this.mAlbumRepository.queryTrashAlbumCount().map(new AnonymousClass1());
    }

    /* renamed from: com.miui.gallery.ui.album.main.usecase.QueryTrashBinCase$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Function<Integer, Optional<AlbumTabToolItemBean>> {
        public static /* synthetic */ AlbumTabToolItemBean $r8$lambda$LQUb_zOLBwLd81E3jabLKl_9JV4(Integer num, AlbumTabToolItemBean albumTabToolItemBean) {
            return lambda$apply$0(num, albumTabToolItemBean);
        }

        public AnonymousClass1() {
            QueryTrashBinCase.this = r1;
        }

        @Override // io.reactivex.functions.Function
        /* renamed from: apply */
        public Optional<AlbumTabToolItemBean> mo2564apply(final Integer num) throws Exception {
            return Optional.ofNullable(QueryTrashBinCase.this.getTrashAlbumBean()).map(new java.util.function.Function() { // from class: com.miui.gallery.ui.album.main.usecase.QueryTrashBinCase$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return QueryTrashBinCase.AnonymousClass1.$r8$lambda$LQUb_zOLBwLd81E3jabLKl_9JV4(num, (AlbumTabToolItemBean) obj);
                }
            });
        }

        public static /* synthetic */ AlbumTabToolItemBean lambda$apply$0(Integer num, AlbumTabToolItemBean albumTabToolItemBean) {
            return albumTabToolItemBean.setSubTitle(StringUtils.getNumberStringInRange(num.intValue()));
        }
    }

    public final AlbumTabToolItemBean getTrashAlbumBean() {
        return CloudControlStrategyHelper.getAlbumTabToolsStrategy().getToolById(2147483638L);
    }
}
