package com.miui.gallery.ui.album.picker.other;

import androidx.fragment.app.Fragment;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.picker.CrossUserAlbumRepositoryModelInstance;
import com.miui.gallery.ui.album.picker.PickerPageUtils;
import com.miui.gallery.ui.album.picker.other.usecase.QueryPickOtherAlbumList;
import com.miui.gallery.util.CheckEmptyDataSubscriber;
import java.util.List;

/* loaded from: classes2.dex */
public class PickOtherAlbumPresenter extends PickOtherAlbumContract$P {
    private HotUseCase mQueryOtherAlbumList;
    private QueryPickOtherAlbumList.PickOtherAlbumOptions options;

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter
    public void initUseCase(AbstractAlbumRepository abstractAlbumRepository) {
        if (PickerPageUtils.isCrossUserPick((Fragment) getView())) {
            abstractAlbumRepository = (AbstractAlbumRepository) ModelManager.getInstance().getModel(AbstractAlbumRepository.class, new CrossUserAlbumRepositoryModelInstance(((PickOtherAlbumContract$V) getView()).getSafeActivity()));
        }
        super.initUseCase(abstractAlbumRepository);
        this.mQueryOtherAlbumList = new QueryPickOtherAlbumList(abstractAlbumRepository);
    }

    @Override // com.miui.gallery.ui.album.picker.other.PickOtherAlbumContract$P
    public void setPicker(Picker picker) {
        QueryPickOtherAlbumList.PickOtherAlbumOptions pickOtherAlbumOptions = new QueryPickOtherAlbumList.PickOtherAlbumOptions();
        this.options = pickOtherAlbumOptions;
        pickOtherAlbumOptions.isPickOtherShareAlbum = !picker.isPickOwner();
        if (picker.getMediaType() == Picker.MediaType.IMAGE) {
            this.options.mediaType = "image/*";
        } else if (picker.getMediaType() != Picker.MediaType.VIDEO) {
        } else {
            this.options.mediaType = "video/*";
        }
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter, com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$P
    public void initData() {
        this.mQueryOtherAlbumList.executeWith(new CheckEmptyDataSubscriber<List<CommonAlbumItemViewBean>>() { // from class: com.miui.gallery.ui.album.picker.other.PickOtherAlbumPresenter.1
            @Override // com.miui.gallery.util.CheckEmptyDataSubscriber
            public void onEvent(List<CommonAlbumItemViewBean> list) {
                ((PickOtherAlbumContract$V) PickOtherAlbumPresenter.this.getView()).showOthersAlbumResult(list);
            }

            @Override // com.miui.gallery.util.CheckEmptyDataSubscriber
            public void onEventEmpty(List<CommonAlbumItemViewBean> list) {
                ((PickOtherAlbumContract$V) PickOtherAlbumPresenter.this.getView()).showOthersAlbumResult(list);
            }
        }, this.options, ((PickOtherAlbumContract$V) getView()).getLifecycle());
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter, com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        this.mQueryOtherAlbumList = null;
    }
}
