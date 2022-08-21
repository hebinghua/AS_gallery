package com.miui.gallery.ui.album.otheralbum;

import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.MultiChoiceModeDataProvider;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public class OtherAlbumPresenter extends OtherAlbumContract$P {
    private static final String TAG = "OtherAlbumPresenter";
    private boolean isNeedDiscardMemoryDatas;
    private MultiChoiceModeDataProvider mActionItemClickDataProvider = new MultiChoiceModeDataProvider() { // from class: com.miui.gallery.ui.album.otheralbum.OtherAlbumPresenter.1
        @Override // com.miui.gallery.ui.album.common.MultiChoiceModeDataProvider
        public int providerCheckedCount() {
            return ((OtherAlbumContract$V) OtherAlbumPresenter.this.getView()).getCheckedCount();
        }

        @Override // com.miui.gallery.ui.album.common.MultiChoiceModeDataProvider
        public Collection<Album> providerCurrentOperationAlbums() {
            int length = ((OtherAlbumContract$V) OtherAlbumPresenter.this.getView()).getCheckedItemIds().length;
            if (length == 0) {
                return null;
            }
            ArrayList arrayList = new ArrayList(length);
            for (int i : ((OtherAlbumContract$V) OtherAlbumPresenter.this.getView()).getCheckedItemOrderedPositions()) {
                BaseViewBean data = ((OtherAlbumContract$V) OtherAlbumPresenter.this.getView()).getData(i);
                if (data instanceof CommonAlbumItemViewBean) {
                    CommonAlbumItemViewBean commonAlbumItemViewBean = (CommonAlbumItemViewBean) data;
                    if (AlbumConstants.isAlbumCheckable((int) commonAlbumItemViewBean.getId())) {
                        arrayList.add((Album) commonAlbumItemViewBean.getSource());
                    }
                }
            }
            return arrayList;
        }
    };
    private HotUseCase[] mHotUseCases;

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter
    public void onAttachView(OtherAlbumContract$V otherAlbumContract$V) {
        super.onAttachView((OtherAlbumPresenter) otherAlbumContract$V);
        this.mHotUseCases = new HotUseCase[]{this.mQueryOtherAlbumList, this.mQueryRubbishAlbumCoverList};
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$P
    public MultiChoiceModeDataProvider getMultiChoiceDataProvider() {
        return this.mActionItemClickDataProvider;
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$P
    public void onStartChoiceMode() {
        TrackController.trackExpose("403.40.2.1.11129", AutoTracking.getRef());
        for (HotUseCase hotUseCase : this.mHotUseCases) {
            hotUseCase.onStop();
        }
        this.isNeedDiscardMemoryDatas = true;
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$P
    public void onStopChoiceMode() {
        for (HotUseCase hotUseCase : this.mHotUseCases) {
            hotUseCase.onStart();
        }
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter
    public void dispatchAlbumDatas(PageResults<List<CommonAlbumItemViewBean>> pageResults) {
        if (this.isNeedDiscardMemoryDatas && pageResults.isFromFile()) {
            this.isNeedDiscardMemoryDatas = false;
        } else {
            super.dispatchAlbumDatas(pageResults);
        }
    }
}
