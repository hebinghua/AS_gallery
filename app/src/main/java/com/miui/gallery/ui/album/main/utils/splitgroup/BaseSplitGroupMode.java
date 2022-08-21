package com.miui.gallery.ui.album.main.utils.splitgroup;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import io.reactivex.Flowable;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public abstract class BaseSplitGroupMode implements ISplitGroupMode {
    public static <T> Flowable<HashMap<String, List<T>>> groupAlbumBy(List<T> list, Callable<HashMap<String, List<T>>> callable, Function<T, String> function) {
        return Flowable.fromIterable(list).groupBy(function).collect(callable, new AnonymousClass3()).toFlowable();
    }

    /* renamed from: com.miui.gallery.ui.album.main.utils.splitgroup.BaseSplitGroupMode$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements BiConsumer<HashMap<String, List<T>>, GroupedFlowable<String, T>> {
        @Override // io.reactivex.functions.BiConsumer
        public void accept(final HashMap<String, List<T>> hashMap, final GroupedFlowable<String, T> groupedFlowable) throws Exception {
            groupedFlowable.subscribe(new Consumer<T>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.BaseSplitGroupMode.3.1
                @Override // io.reactivex.functions.Consumer
                public void accept(T t) throws Exception {
                    String str = (String) groupedFlowable.getKey();
                    if (hashMap.containsKey(str)) {
                        ((List) hashMap.get(str)).add(t);
                        return;
                    }
                    LinkedList linkedList = new LinkedList();
                    linkedList.add(t);
                    hashMap.put(str, linkedList);
                }
            }, BaseSplitGroupMode$3$$ExternalSyntheticLambda0.INSTANCE);
        }
    }

    public static Album getAlbumSource(BaseViewBean baseViewBean) {
        return AlbumSplitGroupHelper.getAlbumSource(baseViewBean);
    }

    public static String packSortInfo(double d, String str) {
        return AlbumSplitGroupHelper.packSortInfo(d, str);
    }
}
