package com.miui.gallery.ui.album.common.usecase;

import android.net.Uri;
import android.util.Pair;
import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.model.dto.PageResults;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryItemFilePath extends BaseUseCase<List<Pair<String, Byte[]>>, RequestParam> {
    public final AbstractCloudRepository mRepository;

    public QueryItemFilePath(AbstractCloudRepository abstractCloudRepository) {
        this.mRepository = abstractCloudRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<List<Pair<String, Byte[]>>> buildUseCaseFlowable(RequestParam requestParam) {
        if (requestParam.ids != null) {
            return this.mRepository.queryItemPath(requestParam.ids).map(new Function<PageResults<List<Pair<String, Byte[]>>>, List<Pair<String, Byte[]>>>() { // from class: com.miui.gallery.ui.album.common.usecase.QueryItemFilePath.1
                @Override // io.reactivex.functions.Function
                /* renamed from: apply  reason: avoid collision after fix types in other method */
                public List<Pair<String, Byte[]>> mo2564apply(PageResults<List<Pair<String, Byte[]>>> pageResults) throws Exception {
                    return pageResults.getResult();
                }
            });
        }
        if (requestParam.uriList == null) {
            return Flowable.error(new IllegalArgumentException());
        }
        return this.mRepository.queryItemPath(requestParam.uriList);
    }

    /* loaded from: classes2.dex */
    public static class RequestParam {
        public Long[] ids;
        public Uri[] uriList;

        public RequestParam(Long... lArr) {
            this.ids = lArr;
        }

        public RequestParam(Uri... uriArr) {
            this.uriList = uriArr;
        }
    }
}
