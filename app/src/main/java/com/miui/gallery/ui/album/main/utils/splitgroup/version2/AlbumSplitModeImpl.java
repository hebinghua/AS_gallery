package com.miui.gallery.ui.album.main.utils.splitgroup.version2;

import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.ExtraSourceProvider;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.ui.album.main.utils.splitgroup.BaseSplitGroupMode;
import com.miui.gallery.ui.album.main.utils.splitgroup.SplitGroupResult;
import com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.LazyValue;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class AlbumSplitModeImpl<P> extends BaseSplitGroupMode {
    public final LazyValue<Void, Map<String, Integer>> mCloudControlSettingSorts = new LazyValue<Void, Map<String, Integer>>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl.1
        {
            AlbumSplitModeImpl.this = this;
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Map<String, Integer> mo1272onInit(Void r1) {
            return CloudControlStrategyHelper.getThirdAlbumSorts().getSorts();
        }
    };

    public static /* synthetic */ Boolean $r8$lambda$c_mC_cDJP7AYm2ugLiYo9lRIHuk(Map map, Album album) {
        return lambda$hookThirdAlbumSort$0(map, album);
    }

    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.ISplitGroupMode
    public <T extends Album> Flowable<SplitGroupResult<T>> splitGroup(List<T> list, boolean z, Function<T, String> function) {
        if (list == null) {
            return Flowable.just(new SplitGroupResult(new LinkedHashMap(0)));
        }
        return Flowable.just(list).flatMap(new AnonymousClass3(function)).map(new Function<HashMap<String, List<T>>, SplitGroupResult<T>>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl.2
            {
                AlbumSplitModeImpl.this = this;
            }

            @Override // io.reactivex.functions.Function
            /* renamed from: apply */
            public SplitGroupResult<T> mo2564apply(HashMap<String, List<T>> hashMap) throws Exception {
                String[] supportGroups;
                Comparator<Album> currentComparator = AlbumSortHelper.getCurrentComparator();
                for (String str : AlbumSplitModeImpl.this.getSupportGroups()) {
                    List list2 = (List) hashMap.get(str);
                    if (list2 != null && !list2.isEmpty()) {
                        if (str.equals("group_third") && AlbumSortHelper.isCustomSortOrder()) {
                            AlbumSplitModeImpl.this.hookThirdAlbumSort(list2, currentComparator);
                        } else {
                            list2.sort(currentComparator);
                        }
                    }
                }
                return new SplitGroupResult<>(hashMap);
            }
        });
    }

    /* renamed from: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Function<List<T>, Publisher<HashMap<String, List<T>>>> {
        public final /* synthetic */ Function val$groupBy;

        public static /* synthetic */ String $r8$lambda$y2DmOr9kW7WXjyrOmcVm6oNufm8(AnonymousClass3 anonymousClass3, Function function, Album album) {
            return anonymousClass3.lambda$apply$0(function, album);
        }

        public AnonymousClass3(Function function) {
            AlbumSplitModeImpl.this = r1;
            this.val$groupBy = function;
        }

        @Override // io.reactivex.functions.Function
        /* renamed from: apply */
        public Publisher<HashMap<String, List<T>>> mo2564apply(List<T> list) throws Exception {
            Callable generateGroupContainerCallable = AlbumSplitModeImpl.this.getGenerateGroupContainerCallable(list);
            final Function function = this.val$groupBy;
            return BaseSplitGroupMode.groupAlbumBy(list, generateGroupContainerCallable, new Function() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl$3$$ExternalSyntheticLambda0
                @Override // io.reactivex.functions.Function
                /* renamed from: apply */
                public final Object mo2564apply(Object obj) {
                    return AlbumSplitModeImpl.AnonymousClass3.$r8$lambda$y2DmOr9kW7WXjyrOmcVm6oNufm8(AlbumSplitModeImpl.AnonymousClass3.this, function, (Album) obj);
                }
            });
        }

        public /* synthetic */ String lambda$apply$0(Function function, Album album) throws Exception {
            String str = function != null ? (String) function.mo2564apply(album) : null;
            return str == null ? AlbumSplitModeImpl.this.getGroupType(album) : str;
        }
    }

    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.ISplitGroupMode
    public <T extends BaseViewBean> Flowable<SplitGroupResult<T>> splitGroupByViewBean(List<T> list, boolean z) {
        if (list == null) {
            return Flowable.just(new SplitGroupResult(new LinkedHashMap(0)));
        }
        final LinkedList linkedList = new LinkedList();
        return Flowable.just((List) list.stream().filter((Predicate<? super T>) new Predicate<T>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl.6
            {
                AlbumSplitModeImpl.this = this;
            }

            @Override // java.util.function.Predicate
            public boolean test(BaseViewBean baseViewBean) {
                boolean z2 = (baseViewBean.getSource() instanceof Album) || (AlbumSortHelper.isCustomSortOrder() && (baseViewBean instanceof ExtraSourceProvider) && (((ExtraSourceProvider) baseViewBean).mo1601provider() instanceof Album));
                if (!z2) {
                    linkedList.add(baseViewBean);
                }
                return z2;
            }
        }).collect(Collectors.toList())).flatMap(new AnonymousClass5()).map(new Function<HashMap<String, List<T>>, SplitGroupResult<T>>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl.4
            {
                AlbumSplitModeImpl.this = this;
            }

            @Override // io.reactivex.functions.Function
            /* renamed from: apply */
            public SplitGroupResult<T> mo2564apply(HashMap<String, List<T>> hashMap) throws Exception {
                final Comparator<Album> currentComparator = AlbumSortHelper.getCurrentComparator();
                Object obj = new Comparator<T>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl.4.1
                    {
                        AnonymousClass4.this = this;
                    }

                    @Override // java.util.Comparator
                    public int compare(BaseViewBean baseViewBean, BaseViewBean baseViewBean2) {
                        return currentComparator.compare(BaseSplitGroupMode.getAlbumSource(baseViewBean), BaseSplitGroupMode.getAlbumSource(baseViewBean2));
                    }
                };
                for (String str : AlbumSplitModeImpl.this.getSupportGroups()) {
                    List list2 = (List) hashMap.get(str);
                    if (list2 != null && !list2.isEmpty()) {
                        list2.sort(obj);
                    }
                }
                return new SplitGroupResult<>(hashMap, linkedList);
            }
        });
    }

    /* renamed from: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements Function<List<T>, Publisher<HashMap<String, List<T>>>> {
        public static /* synthetic */ String $r8$lambda$DrRDJR96CktSI9Y47hko23b0eok(AnonymousClass5 anonymousClass5, BaseViewBean baseViewBean) {
            return anonymousClass5.lambda$apply$0(baseViewBean);
        }

        public AnonymousClass5() {
            AlbumSplitModeImpl.this = r1;
        }

        public /* synthetic */ String lambda$apply$0(BaseViewBean baseViewBean) throws Exception {
            return AlbumSplitModeImpl.this.getGroupType(BaseSplitGroupMode.getAlbumSource(baseViewBean));
        }

        @Override // io.reactivex.functions.Function
        /* renamed from: apply */
        public Publisher<HashMap<String, List<T>>> mo2564apply(List<T> list) throws Exception {
            return BaseSplitGroupMode.groupAlbumBy(list, AlbumSplitModeImpl.this.getGenerateGroupContainerCallable(list), new Function() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl$5$$ExternalSyntheticLambda0
                @Override // io.reactivex.functions.Function
                /* renamed from: apply */
                public final Object mo2564apply(Object obj) {
                    return AlbumSplitModeImpl.AnonymousClass5.$r8$lambda$DrRDJR96CktSI9Y47hko23b0eok(AlbumSplitModeImpl.AnonymousClass5.this, (BaseViewBean) obj);
                }
            });
        }
    }

    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.ISplitGroupMode
    public String getGroupType(Album album) {
        return internalGetGroupType(album);
    }

    public static String internalGetGroupTypeBySortInfo(String str) {
        String[] strArr;
        for (String str2 : Contracts.SUPPORT_GROUPS) {
            if (str.contains(str2)) {
                return str2;
            }
        }
        return "unknow";
    }

    public static String internalGetGroupType(Album album) {
        String albumSortInfo = album.getAlbumSortInfo();
        String internalGetGroupTypeBySortInfo = internalGetGroupTypeBySortInfo(albumSortInfo);
        return !AlbumSplitGroupHelper.isInValidGroup(internalGetGroupTypeBySortInfo) ? internalGetGroupTypeBySortInfo : Double.compare(Double.parseDouble(albumSortInfo), 2.147483647E9d) == -1 ? "group_head" : (album.isUserCreateAlbum() || album.isShareAlbum() || album.isUserCreative()) ? "group_user" : (!album.isThirdAlbum() && isImmutableAlbum(album)) ? "group_immutable" : "group_third";
    }

    public static boolean isImmutableAlbum(Album album) {
        int[] iArr;
        long albumId = album.getAlbumId();
        int length = Contracts.IMMUTABLE_ALBUM_IDS.length;
        for (int i = 0; i < length; i++) {
            if (iArr[i] == albumId) {
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.ISplitGroupMode
    public String[] getSupportGroups() {
        return Contracts.SUPPORT_GROUPS;
    }

    public final <T extends Album> void hookThirdAlbumSort(List<T> list, Comparator<Album> comparator) {
        final Map<String, Integer> map = this.mCloudControlSettingSorts.get(null);
        if (map == null) {
            list.sort(comparator);
            return;
        }
        Map map2 = (Map) list.stream().collect(Collectors.groupingBy(new java.util.function.Function() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return AlbumSplitModeImpl.$r8$lambda$c_mC_cDJP7AYm2ugLiYo9lRIHuk(map, (Album) obj);
            }
        }));
        List list2 = (List) map2.get(Boolean.TRUE);
        List list3 = (List) map2.get(Boolean.FALSE);
        list.clear();
        if (list2 != null && !list2.isEmpty()) {
            list2.sort(new Comparator<T>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl.7
                {
                    AlbumSplitModeImpl.this = this;
                }

                @Override // java.util.Comparator
                public int compare(Album album, Album album2) {
                    return Integer.compare(((Integer) map.get(album.getLocalPath().toLowerCase())).intValue(), ((Integer) map.get(album2.getLocalPath().toLowerCase())).intValue());
                }
            });
            list.addAll(list2);
        }
        if (list3 == null || list3.isEmpty()) {
            return;
        }
        list3.sort(comparator);
        list.addAll(list3);
    }

    public static /* synthetic */ Boolean lambda$hookThirdAlbumSort$0(Map map, Album album) {
        boolean z;
        if (!album.isManualMovePosition()) {
            if (map.containsKey(album.getLocalPath() == null ? null : album.getLocalPath().toLowerCase())) {
                z = true;
                return Boolean.valueOf(z);
            }
        }
        z = false;
        return Boolean.valueOf(z);
    }

    public final <T> Callable<HashMap<String, List<T>>> getGenerateGroupContainerCallable(final List<T> list) {
        return new Callable<HashMap<String, List<T>>>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumSplitModeImpl.8
            {
                AlbumSplitModeImpl.this = this;
            }

            @Override // java.util.concurrent.Callable
            public HashMap<String, List<T>> call() throws Exception {
                LinkedHashMap linkedHashMap = new LinkedHashMap(((int) (list.size() / 0.75f)) + 1);
                for (String str : AlbumSplitModeImpl.this.getSupportGroups()) {
                    linkedHashMap.put(str, new LinkedList());
                }
                return linkedHashMap;
            }
        };
    }
}
