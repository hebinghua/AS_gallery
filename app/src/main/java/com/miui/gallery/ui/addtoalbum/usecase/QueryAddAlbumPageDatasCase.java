package com.miui.gallery.ui.addtoalbum.usecase;

import androidx.core.util.Pair;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.album.config.QueryFlagsBuilder;
import com.miui.gallery.ui.addtoalbum.AddToAlbumGroupResult;
import com.miui.gallery.ui.addtoalbum.viewbean.AddToAlbumItemViewBean;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.common.GroupDatasResult;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.common.usecase.QueryAlbumsCase;
import com.miui.gallery.ui.album.common.viewbean.AlbumTabGroupTitleViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.utils.splitgroup.IGroupSettingInfo;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class QueryAddAlbumPageDatasCase extends BaseUseCase<GroupDatasResult<BaseViewBean>, ParamBean> {
    public BaseViewBean mCreateAlbum;
    public AlbumTabGroupTitleViewBean mHeadGroupGap;
    public final QueryAlbumsCase mInternalCase;
    public BaseViewBean mSecretAlbum;

    public QueryAddAlbumPageDatasCase(AbstractAlbumRepository abstractAlbumRepository) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mInternalCase = new QueryAlbumsCase(abstractAlbumRepository);
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<GroupDatasResult<BaseViewBean>> buildUseCaseFlowable(ParamBean paramBean) {
        long j = AlbumConstants.QueryScene.SCENE_ADD_TO_ALBUM;
        boolean z = true;
        boolean z2 = paramBean != null && paramBean.isShowOtherShareAlbum;
        final boolean z3 = paramBean != null && paramBean.isShowSecretAlbum;
        if (paramBean != null) {
            boolean z4 = paramBean.isShowPicToPdf;
        }
        if (paramBean == null || !paramBean.isShowFavorites) {
            z = false;
        }
        if (z2) {
            j = AlbumConstants.QueryScene.SCENE_ADD_TO_ALBUM_EXTRA_JOIN_SHARER;
        }
        if (!z) {
            j = new QueryFlagsBuilder(j).removeFlags(262144L).build();
        }
        DefaultLogger.v("QueryAddAlbumPageDatasCase", "start query addToAlbumPage datas");
        final List<Long> addToAlbumPageLastSelectedAlbum = GalleryPreferences.Album.getAddToAlbumPageLastSelectedAlbum();
        final Comparator<BaseViewBean> comparator = new Comparator<BaseViewBean>() { // from class: com.miui.gallery.ui.addtoalbum.usecase.QueryAddAlbumPageDatasCase.1
            @Override // java.util.Comparator
            public int compare(BaseViewBean baseViewBean, BaseViewBean baseViewBean2) {
                return Integer.compare(addToAlbumPageLastSelectedAlbum.indexOf(Long.valueOf(baseViewBean.getId())), addToAlbumPageLastSelectedAlbum.indexOf(Long.valueOf(baseViewBean2.getId())));
            }
        };
        final AlbumGroupByAlbumTypeFunction albumGroupByAlbumTypeFunction = new AlbumGroupByAlbumTypeFunction(null);
        final IGroupSettingInfo groupSettingInfo = AlbumPageConfig.getInstance().getComponent(null).getGroupSettingInfo();
        return this.mInternalCase.buildFlowable(new QueryAlbumsCase.ParamBean(j)).flatMap(new Function<PageResults<List<Album>>, Publisher<GroupDatasResult<BaseViewBean>>>() { // from class: com.miui.gallery.ui.addtoalbum.usecase.QueryAddAlbumPageDatasCase.2
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<GroupDatasResult<BaseViewBean>> mo2564apply(PageResults<List<Album>> pageResults) throws Exception {
                List<Album> result = pageResults.getResult();
                DefaultLogger.fd("QueryAddAlbumPageDatasCase", "query addToAlbumPage datas.now start split group,albumSize:%d", Integer.valueOf(result.size()));
                return ((Flowable) Flowable.just(result).to(albumGroupByAlbumTypeFunction)).map(new Function<AlbumDataListResult, AddToAlbumGroupResult>() { // from class: com.miui.gallery.ui.addtoalbum.usecase.QueryAddAlbumPageDatasCase.2.1
                    @Override // io.reactivex.functions.Function
                    /* renamed from: apply  reason: avoid collision after fix types in other method */
                    public AddToAlbumGroupResult mo2564apply(AlbumDataListResult albumDataListResult) throws Exception {
                        AddToAlbumGroupResult addToAlbumGroupResult = new AddToAlbumGroupResult(albumDataListResult);
                        groupSettingInfo.fillGroupGap(addToAlbumGroupResult);
                        addToAlbumGroupResult.addGroup("recent", new LinkedList(), false, true);
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        if (z3) {
                            addToAlbumGroupResult.addGroupItem("recent", 0, QueryAddAlbumPageDatasCase.this.getSecretAlbumBean(), false);
                        }
                        addToAlbumGroupResult.addGroupItem("recent", 0, QueryAddAlbumPageDatasCase.this.getCreateAlbumBean(), false);
                        List<BaseViewBean> findItemByIds = addToAlbumGroupResult.findItemByIds(addToAlbumPageLastSelectedAlbum);
                        if (findItemByIds != null) {
                            findItemByIds.sort(comparator);
                            for (BaseViewBean baseViewBean : findItemByIds) {
                                if (baseViewBean.getSource() instanceof Album) {
                                    AddToAlbumItemViewBean addToAlbumItemViewBean = new AddToAlbumItemViewBean();
                                    addToAlbumItemViewBean.mapping((AddToAlbumItemViewBean) ((Album) baseViewBean.getSource()));
                                    addToAlbumItemViewBean.setIsRecent(true);
                                    addToAlbumGroupResult.addGroupItem("recent", addToAlbumItemViewBean, false);
                                }
                            }
                        }
                        Pair<List<BaseViewBean>, List<EpoxyModel<?>>> groupDataAndModels = addToAlbumGroupResult.getGroupDataAndModels("recent");
                        if (groupDataAndModels != null) {
                            if (addToAlbumGroupResult.groupSize() > 1) {
                                addToAlbumGroupResult.addGroupGapDecorator("recent", 2, QueryAddAlbumPageDatasCase.this.getHeadGroupGap());
                            }
                            addToAlbumGroupResult.replaceGroup("recent", Collections.singletonList(AlbumPageConfig.getAddToAlbumConfig().getHeaderItemConfigBuilder(277L).setDatas(groupDataAndModels.first, groupDataAndModels.second).build()));
                        }
                        return addToAlbumGroupResult;
                    }
                });
            }
        });
    }

    public final BaseViewBean getCreateAlbumBean() {
        if (this.mCreateAlbum == null) {
            this.mCreateAlbum = AlbumPageConfig.getAddToAlbumConfig().getCreateAlbumButtonBean();
        }
        return this.mCreateAlbum;
    }

    public final BaseViewBean getSecretAlbumBean() {
        if (this.mSecretAlbum == null) {
            this.mSecretAlbum = AlbumPageConfig.getAddToAlbumConfig().getSecretAlbumButtonBean();
        }
        return this.mSecretAlbum;
    }

    public final AlbumTabGroupTitleViewBean getHeadGroupGap() {
        if (this.mHeadGroupGap == null) {
            this.mHeadGroupGap = AlbumPageConfig.getAddToAlbumConfig().getHeaderGroupGap();
        }
        return this.mHeadGroupGap;
    }

    /* loaded from: classes2.dex */
    public static class ParamBean {
        public boolean isShowCreate;
        public boolean isShowFavorites;
        public boolean isShowOtherShareAlbum;
        public boolean isShowPicToPdf;
        public boolean isShowSecretAlbum;

        public ParamBean(boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
            this.isShowSecretAlbum = z;
            this.isShowOtherShareAlbum = z2;
            this.isShowCreate = z3;
            this.isShowPicToPdf = z4;
            this.isShowFavorites = z5;
        }
    }
}
