package com.miui.gallery.ui.album.picker;

import androidx.fragment.app.Fragment;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.provider.album.config.QueryFlagsBuilder;
import com.miui.gallery.ui.album.aialbum.usecase.QueryMediaTypeGroupCase;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.common.usecase.QueryAlbumsCase;
import com.miui.gallery.ui.album.main.usecase.QueryAIAlbumCase;
import com.miui.gallery.ui.album.main.usecase.QueryAlbumsByAlbumTabScene;
import com.miui.gallery.ui.album.main.usecase.QueryOtherAlbumCovers;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.viewbean.AIAlbumGridCoverViewBean;
import java.util.Objects;

/* loaded from: classes2.dex */
public class PickAlbumPagePresenter extends PickAlbumPageContract$P {
    private static final String TAG = "PickAlbumPagePresenter";
    private long mQueryFlags;
    private Picker options;

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter, com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public boolean isNeedLoadAdvanceAIAlbum() {
        return false;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter, com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P
    public boolean isEnableAlbumById(int i) {
        if (i == 2147483639) {
            Picker picker = this.options;
            return picker == null ? super.isEnableAlbumById(i) : !Objects.equals(picker.getMediaType().name(), Picker.MediaType.VIDEO.name());
        } else if (i != 2147483638) {
            return super.isEnableAlbumById(i);
        } else {
            return false;
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter
    public void initUseCases(AbstractAlbumRepository abstractAlbumRepository) {
        if (PickerPageUtils.isCrossUserPick((Fragment) getView())) {
            abstractAlbumRepository = (AbstractAlbumRepository) ModelManager.getInstance().getModel(AbstractAlbumRepository.class, new CrossUserAlbumRepositoryModelInstance(((PickAlbumPageContract$V) getView()).getSafeActivity()));
        }
        super.initUseCases(abstractAlbumRepository);
    }

    @Override // com.miui.gallery.ui.album.picker.PickAlbumPageContract$P
    public void setPicker(Picker picker) {
        int fromType = picker.getFromType();
        if (fromType == 1009) {
            this.mQueryFlags = AlbumConstants.QueryScene.SCENE_MI_CLIP;
        } else if (fromType == 1010) {
            this.mQueryFlags = AlbumConstants.QueryScene.SCENE_PHOTO_MOVIE;
        } else if (fromType == 1011) {
            this.mQueryFlags = AlbumConstants.QueryScene.SCENE_COLLAGE;
        } else if (fromType == 1012) {
            this.mQueryFlags = AlbumConstants.QueryScene.SCENE_ALBUM_DETAIL_ADD_TO_ALBUM;
        } else if (fromType == 1015) {
            this.mQueryFlags = AlbumConstants.QueryScene.SCENE_ALBUM_DETAIL_GALLERY_WIDGET;
        } else {
            QueryFlagsBuilder queryFlagsBuilder = new QueryFlagsBuilder(AlbumConstants.QueryScene.SCENE_DEFAULT_PICKER);
            Picker.MediaType mediaType = picker.getMediaType();
            if (mediaType == Picker.MediaType.IMAGE) {
                queryFlagsBuilder = queryFlagsBuilder.onlyImageMediaType();
            } else if (mediaType == Picker.MediaType.VIDEO) {
                queryFlagsBuilder = queryFlagsBuilder.onlyVideoMediaType();
            }
            this.mQueryFlags = queryFlagsBuilder.build();
        }
        this.options = picker;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter
    public QueryAlbumsByAlbumTabScene.RequestBean getQueryAllAlbumListParam() {
        return new QueryAlbumsByAlbumTabScene.RequestBean(new QueryAlbumsCase.ParamBean.Builder().queryFlags(this.mQueryFlags).build(), new AlbumGroupByAlbumTypeFunction.Config(getQueryAllAlbumsLoadComplateListener()));
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter
    public void queryOtherAlbumCover(QueryOtherAlbumCovers.RequestParam requestParam) {
        if (PickerPageUtils.isCrossUserPick((Fragment) getView())) {
            requestParam = new QueryOtherAlbumCovers.RequestParam(true);
        }
        super.queryOtherAlbumCover(requestParam);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter
    public QueryMediaTypeGroupCase.RequestBean getQueryMediaTypeParamBean() {
        Picker picker = this.options;
        if (picker != null) {
            return new QueryMediaTypeGroupCase.RequestBean(AlbumConstants.MedidTypeScene.SCENE_ALBUM_TAB_PAGE, picker.getMediaType() == Picker.MediaType.IMAGE ? 1 : this.options.getMediaType() == Picker.MediaType.VIDEO ? 2 : -1, this.options.getFilterMimeTypes(), false);
        }
        return super.getQueryMediaTypeParamBean();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter
    public void queryAIAlbumCover() {
        if (PickerPageUtils.isCrossUserPick((Fragment) getView())) {
            QueryAIAlbumCase.RequestParam generatorQueryParamBean = QueryAIAlbumCase.generatorQueryParamBean(4, 0, 0);
            generatorQueryParamBean.setForceQuery(true);
            generatorQueryParamBean.setIgnoreCache(true);
            startQueryAIAlbumCover(generatorQueryParamBean);
            return;
        }
        super.queryAIAlbumCover();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter
    public void dispatchAIAlbum(AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean) {
        if (PickerPageUtils.isCrossUserPick((Fragment) getView())) {
            this.isEnableAIAlbum = true;
        }
        super.dispatchAIAlbum(aIAlbumGridCoverViewBean);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter
    public void dispatchEmptyAIAlbumEvent(AIAlbumGridCoverViewBean aIAlbumGridCoverViewBean) {
        if (PickerPageUtils.isCrossUserPick((Fragment) getView())) {
            return;
        }
        super.dispatchEmptyAIAlbumEvent(aIAlbumGridCoverViewBean);
    }
}
