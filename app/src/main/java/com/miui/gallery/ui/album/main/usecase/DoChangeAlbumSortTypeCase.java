package com.miui.gallery.ui.album.main.usecase;

import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.album.AlbumSnapshotHelper;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.ui.album.main.utils.splitgroup.SplitGroupResult;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class DoChangeAlbumSortTypeCase extends BaseUseCase<SortResult, RequestParam> {

    /* loaded from: classes2.dex */
    public interface IllegaDataHandler {
        void onHandle(Map<String, List<BaseViewBean>> map, List<BaseViewBean> list);
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<SortResult> buildUseCaseFlowable(final RequestParam requestParam) {
        return AlbumSplitGroupHelper.getSplitGroupMode().splitGroupByViewBean(requestParam.getDatas()).map(new Function<SplitGroupResult<BaseViewBean>, SortResult>() { // from class: com.miui.gallery.ui.album.main.usecase.DoChangeAlbumSortTypeCase.2
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public SortResult mo2564apply(SplitGroupResult<BaseViewBean> splitGroupResult) throws Exception {
                Map<String, List<BaseViewBean>> groups = splitGroupResult.getGroups();
                if (splitGroupResult.getIllegalDatas() != null && requestParam.getIllegaDataHandler() != null) {
                    requestParam.getIllegaDataHandler().onHandle(groups, splitGroupResult.getIllegalDatas());
                }
                LinkedList linkedList = new LinkedList();
                for (String str : groups.keySet()) {
                    linkedList.addAll(groups.get(str));
                }
                return new SortResult(linkedList, groups);
            }
        }).doAfterNext(new AnonymousClass1());
    }

    /* renamed from: com.miui.gallery.ui.album.main.usecase.DoChangeAlbumSortTypeCase$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Consumer<SortResult> {
        public AnonymousClass1() {
        }

        @Override // io.reactivex.functions.Consumer
        public void accept(SortResult sortResult) throws Exception {
            AlbumSnapshotHelper.persist((List) sortResult.getDatas().stream().filter(new Predicate<BaseViewBean>() { // from class: com.miui.gallery.ui.album.main.usecase.DoChangeAlbumSortTypeCase.1.1
                @Override // java.util.function.Predicate
                public boolean test(BaseViewBean baseViewBean) {
                    return baseViewBean.getSource() instanceof Album;
                }
            }).map(DoChangeAlbumSortTypeCase$1$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList()));
        }

        public static /* synthetic */ Album lambda$accept$0(BaseViewBean baseViewBean) {
            return (Album) baseViewBean.getSource();
        }
    }

    /* loaded from: classes2.dex */
    public static class RequestParam {
        public List<BaseViewBean> mCurrentDatas;
        public IllegaDataHandler mIllegaDataHandler;

        public RequestParam(IllegaDataHandler illegaDataHandler, List<BaseViewBean> list) {
            this.mIllegaDataHandler = illegaDataHandler;
            this.mCurrentDatas = list;
        }

        public RequestParam(List<BaseViewBean> list) {
            this.mCurrentDatas = list;
        }

        public IllegaDataHandler getIllegaDataHandler() {
            return this.mIllegaDataHandler;
        }

        public List<BaseViewBean> getDatas() {
            return this.mCurrentDatas;
        }
    }

    /* loaded from: classes2.dex */
    public static class SortResult {
        public List<BaseViewBean> mData;
        public Map<String, List<BaseViewBean>> mGroupResult;

        public SortResult(List<BaseViewBean> list, Map<String, List<BaseViewBean>> map) {
            this.mData = list;
            this.mGroupResult = map;
        }

        public List<BaseViewBean> getDatas() {
            return this.mData;
        }

        public Map<String, List<BaseViewBean>> getGroups() {
            return this.mGroupResult;
        }
    }
}
