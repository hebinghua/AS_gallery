package com.miui.gallery.ui.album.main.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.usecase.DoChangeAlbumSortTypeCase;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.List;

/* loaded from: classes2.dex */
public class DoChangeSortTypeCaseByAlbumTabSceneCase extends BaseUseCase<AlbumDataListResult, RequestParam> {
    public final DoChangeAlbumSortTypeCase mInternalCase = new DoChangeAlbumSortTypeCase();

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<AlbumDataListResult> buildUseCaseFlowable(final RequestParam requestParam) {
        return this.mInternalCase.buildUseCaseFlowable(new DoChangeAlbumSortTypeCase.RequestParam(requestParam.getDatas())).map(new Function<DoChangeAlbumSortTypeCase.SortResult, AlbumDataListResult>() { // from class: com.miui.gallery.ui.album.main.usecase.DoChangeSortTypeCaseByAlbumTabSceneCase.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public AlbumDataListResult mo2564apply(DoChangeAlbumSortTypeCase.SortResult sortResult) throws Exception {
                AlbumDataListResult albumDataListResult = new AlbumDataListResult(sortResult.getGroups());
                if (requestParam.getCallback() == null) {
                    return albumDataListResult;
                }
                requestParam.getCallback().onSplitGroupFinish(albumDataListResult);
                return albumDataListResult;
            }
        });
    }

    /* loaded from: classes2.dex */
    public static class RequestParam {
        public AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback mCallback;
        public List<BaseViewBean> mDatas;

        public RequestParam(AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback onDataProcessingCallback, List<BaseViewBean> list) {
            this.mCallback = onDataProcessingCallback;
            this.mDatas = list;
        }

        public AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback getCallback() {
            return this.mCallback;
        }

        public List<BaseViewBean> getDatas() {
            return this.mDatas;
        }
    }
}
