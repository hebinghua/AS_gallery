package com.miui.gallery.ui.album.main.utils;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.DefaultViewBeanFactory;
import com.miui.gallery.ui.album.common.ViewBeanFactory;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.ui.album.main.utils.splitgroup.SplitGroupResult;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class AlbumGroupByAlbumTypeFunction implements Function<Flowable<List<Album>>, Flowable<AlbumDataListResult>> {
    public Function<Album, String> mExtraGroupBy;
    public OnDataProcessingCallback mLoadFinshCallback;
    public final String TAG = "AlbumGroupByAlbumTypeFunction";
    public final ViewBeanFactory<BaseViewBean> mViewBeanFactory = DefaultViewBeanFactory.getInstance();

    /* loaded from: classes2.dex */
    public interface OnDataProcessingCallback {
        default void onProcessEnd(AlbumDataListResult albumDataListResult) {
        }

        default void onProcessStart(List<Album> list) {
        }

        void onSplitGroupFinish(AlbumDataListResult albumDataListResult);
    }

    public AlbumGroupByAlbumTypeFunction(Config config) {
        if (config != null) {
            this.mLoadFinshCallback = config.getCallback();
            this.mExtraGroupBy = config.getExtraGroupBy();
        }
    }

    @Override // io.reactivex.functions.Function
    /* renamed from: apply  reason: avoid collision after fix types in other method */
    public Flowable<AlbumDataListResult> mo2564apply(Flowable<List<Album>> flowable) throws Exception {
        return flowable.flatMap(new Function<List<Album>, Publisher<AlbumDataListResult>>() { // from class: com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<AlbumDataListResult> mo2564apply(List<Album> list) throws Exception {
                if (AlbumGroupByAlbumTypeFunction.this.mLoadFinshCallback != null) {
                    AlbumGroupByAlbumTypeFunction.this.mLoadFinshCallback.onProcessStart(list);
                }
                return AlbumSplitGroupHelper.getSplitGroupMode().splitGroup(list, true, AlbumGroupByAlbumTypeFunction.this.mExtraGroupBy).map(new Function<SplitGroupResult<Album>, AlbumDataListResult>() { // from class: com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction.1.1
                    @Override // io.reactivex.functions.Function
                    /* renamed from: apply  reason: avoid collision after fix types in other method */
                    public AlbumDataListResult mo2564apply(SplitGroupResult<Album> splitGroupResult) throws Exception {
                        Map<String, List<Album>> groups = splitGroupResult.getGroups();
                        LinkedHashMap linkedHashMap = new LinkedHashMap(groups.size());
                        for (Map.Entry<String, List<Album>> entry : groups.entrySet()) {
                            LinkedList linkedList = new LinkedList();
                            for (Album album : entry.getValue()) {
                                linkedList.add(AlbumGroupByAlbumTypeFunction.this.mViewBeanFactory.factory(album));
                            }
                            linkedHashMap.put(entry.getKey(), linkedList);
                        }
                        AlbumDataListResult albumDataListResult = new AlbumDataListResult(linkedHashMap);
                        if (AlbumGroupByAlbumTypeFunction.this.mLoadFinshCallback != null) {
                            AlbumGroupByAlbumTypeFunction.this.mLoadFinshCallback.onSplitGroupFinish(albumDataListResult);
                        }
                        return albumDataListResult;
                    }
                });
            }
        });
    }

    /* loaded from: classes2.dex */
    public static class Config {
        public final OnDataProcessingCallback mCallback;
        public Function<Album, String> mExtraGroupBy;

        public Config(OnDataProcessingCallback onDataProcessingCallback) {
            this.mCallback = onDataProcessingCallback;
        }

        public Function<Album, String> getExtraGroupBy() {
            return this.mExtraGroupBy;
        }

        public OnDataProcessingCallback getCallback() {
            return this.mCallback;
        }
    }
}
