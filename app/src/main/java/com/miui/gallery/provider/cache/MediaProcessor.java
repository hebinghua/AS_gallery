package com.miui.gallery.provider.cache;

import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseArray;
import com.miui.gallery.data.Cluster;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.data.MediaCluster;
import com.miui.gallery.provider.TimelineHeadersGroup;
import com.miui.gallery.provider.cache.RecentMediaHeadersHelper;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.text.StringsKt___StringsKt;

/* compiled from: MediaProcessor.kt */
/* loaded from: classes2.dex */
public final class MediaProcessor implements IMediaProcessor<MediaCacheItem, IRecord> {
    public static final Companion Companion = new Companion(null);
    public final boolean foldBurst;

    public static /* synthetic */ int $r8$lambda$8wMN9RZgRyq8w2t2nniRv6h4cqI(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        return m1214processCache$lambda2(i, mediaCacheItem, mediaCacheItem2);
    }

    public static /* synthetic */ LinkedList $r8$lambda$H0FQDIHJXgpmce2ZDuf7ndiWLOk(LinkedList linkedList, Long l) {
        return m1216walkCursor$lambda16(linkedList, l);
    }

    /* renamed from: $r8$lambda$YemxGp-Bv4qd-Y3bbWx-HvfhFxY */
    public static /* synthetic */ LinkedList m1209$r8$lambda$YemxGpBv4qdY3bbWxHvfhFxY(LinkedList linkedList, Long l) {
        return m1212foldMedias$lambda12(linkedList, l);
    }

    /* renamed from: $r8$lambda$gVbIVPKNZIb4yNXxfiRrag3-l00 */
    public static /* synthetic */ int m1210$r8$lambda$gVbIVPKNZIb4yNXxfiRrag3l00(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        return m1215processCache$lambda3(i, mediaCacheItem, mediaCacheItem2);
    }

    /* renamed from: $r8$lambda$sYoMSGmQ-tn2ub02tRnsYSP2ZYA */
    public static /* synthetic */ int m1211$r8$lambda$sYoMSGmQtn2ub02tRnsYSP2ZYA(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        return m1213processCache$lambda1(i, mediaCacheItem, mediaCacheItem2);
    }

    public MediaProcessor(boolean z) {
        this.foldBurst = z;
    }

    /* JADX WARN: Type inference failed for: r3v5, types: [java.util.List, T, java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r6v0, types: [T, java.lang.Object] */
    @Override // com.miui.gallery.provider.cache.IMediaProcessor
    public List<IRecord> processCache(List<? extends MediaCacheItem> result, Bundle bundle) {
        Intrinsics.checkNotNullParameter(result, "result");
        TimingTracing.beginTracing("MediaProcessor", "process");
        try {
            Pair<List<IMedia>, List<Integer>> foldMedias = foldMedias(result);
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            ref$ObjectRef.element = foldMedias.component1();
            List<Integer> component2 = foldMedias.component2();
            TimingTracing.addSplit("foldMedias");
            if (bundle != null && bundle.getBoolean("extra_generate_header")) {
                boolean z = bundle.getBoolean("extra_show_headers", true);
                boolean z2 = bundle.getBoolean("extra_timeline_only_show_valid_location", true);
                final int i = bundle.getInt("extra_group_sort_column", -1);
                if (!(i >= 0)) {
                    throw new IllegalArgumentException("invalid group sort column".toString());
                }
                int i2 = bundle.getInt("extra_media_group_by");
                DefaultLogger.d("MediaProcessor", "caller need " + ((Object) MediaManager.groupByFlags2String(i2)) + " headers, start generate for " + result.size() + " items");
                SparseArray sparseArray = new SparseArray(3);
                if ((i2 & 7) != 0) {
                    ArrayList arrayList = new ArrayList(3);
                    if ((i2 & 1) == 1) {
                        arrayList.add(TuplesKt.to(1, new Comparator() { // from class: com.miui.gallery.provider.cache.MediaProcessor$$ExternalSyntheticLambda2
                            @Override // java.util.Comparator
                            public final int compare(Object obj, Object obj2) {
                                return MediaProcessor.m1211$r8$lambda$sYoMSGmQtn2ub02tRnsYSP2ZYA(i, (MediaCacheItem) obj, (MediaCacheItem) obj2);
                            }
                        }));
                    }
                    if ((i2 & 2) == 2) {
                        arrayList.add(TuplesKt.to(2, new Comparator() { // from class: com.miui.gallery.provider.cache.MediaProcessor$$ExternalSyntheticLambda0
                            @Override // java.util.Comparator
                            public final int compare(Object obj, Object obj2) {
                                return MediaProcessor.$r8$lambda$8wMN9RZgRyq8w2t2nniRv6h4cqI(i, (MediaCacheItem) obj, (MediaCacheItem) obj2);
                            }
                        }));
                    }
                    if ((i2 & 4) == 4) {
                        arrayList.add(TuplesKt.to(4, new Comparator() { // from class: com.miui.gallery.provider.cache.MediaProcessor$$ExternalSyntheticLambda1
                            @Override // java.util.Comparator
                            public final int compare(Object obj, Object obj2) {
                                return MediaProcessor.m1210$r8$lambda$gVbIVPKNZIb4yNXxfiRrag3l00(i, (MediaCacheItem) obj, (MediaCacheItem) obj2);
                            }
                        }));
                    }
                    Pair<Integer, List<TimelineHeadersGroup>>[] generateGroups = MediaGroupingHelper.generateGroups(result, arrayList, z2);
                    TimingTracing.addSplit("generateGroups");
                    int length = generateGroups.length;
                    int i3 = 0;
                    while (i3 < length) {
                        Pair<Integer, List<TimelineHeadersGroup>> pair = generateGroups[i3];
                        i3++;
                        int intValue = pair.component1().intValue();
                        List<TimelineHeadersGroup> component22 = pair.component2();
                        ArrayList arrayList2 = new ArrayList(component22.size());
                        ArrayList arrayList3 = new ArrayList(component22.size());
                        Pair<Integer, List<TimelineHeadersGroup>>[] pairArr = generateGroups;
                        ArrayList arrayList4 = new ArrayList(component22.size());
                        for (Iterator<TimelineHeadersGroup> it = component22.iterator(); it.hasNext(); it = it) {
                            TimelineHeadersGroup next = it.next();
                            arrayList3.add(Integer.valueOf(next.start));
                            arrayList2.add(Integer.valueOf(next.count));
                            arrayList4.add(LocationManager.getInstance().generateTitleLine(next.itemLocations));
                        }
                        if (component2 != null) {
                            MediaProcessorHelper.INSTANCE.wrapGroupInfos(component2, result.size(), arrayList3, arrayList2);
                        }
                        MediaProcessorHelper.INSTANCE.removeEmptyGroup(arrayList2, arrayList3, arrayList4);
                        sparseArray.append(intValue, new MediaCluster(arrayList2, arrayList3, arrayList4));
                        generateGroups = pairArr;
                    }
                    TimingTracing.addSplit("wrapGroupInfos");
                } else if ((i2 & 8) == 8) {
                    ?? arrayList5 = new ArrayList(result.size());
                    List<RecentMediaHeadersHelper.Header> resortAndGenerateHeaders = RecentMediaHeadersHelper.resortAndGenerateHeaders(result, arrayList5);
                    ref$ObjectRef.element = arrayList5;
                    TimingTracing.addSplit("resortAndGenerateHeaders");
                    ArrayList arrayList6 = new ArrayList(resortAndGenerateHeaders.size());
                    ArrayList arrayList7 = new ArrayList(resortAndGenerateHeaders.size());
                    ArrayList arrayList8 = new ArrayList(resortAndGenerateHeaders.size());
                    for (RecentMediaHeadersHelper.Header header : resortAndGenerateHeaders) {
                        arrayList7.add(Integer.valueOf(header.getStartPos()));
                        arrayList6.add(Integer.valueOf(header.getMediaCount()));
                        arrayList8.add(header.getTimeLabel());
                    }
                    if (component2 != null) {
                        MediaProcessorHelper.INSTANCE.wrapGroupInfos(component2, result.size(), arrayList7, arrayList6);
                    }
                    MediaProcessorHelper.INSTANCE.removeEmptyGroup(arrayList6, arrayList7, arrayList8);
                    sparseArray.append(1, new MediaCluster(arrayList6, arrayList7, arrayList8));
                    TimingTracing.addSplit("wrapGroupInfos");
                } else {
                    throw new IllegalArgumentException(Intrinsics.stringPlus("Unsupported groupBy flags: ", Integer.valueOf(i2)));
                }
                SparseArray sparseArray2 = new SparseArray(3);
                List<? extends IMedia> list = (List) ref$ObjectRef.element;
                Object obj = sparseArray.get(1);
                Intrinsics.checkNotNullExpressionValue(obj, "clusters.get(MEDIA_GROUP_BY_DAY)");
                sparseArray2.append(1, buildSections(list, (Cluster) obj, i, -1L, (i2 & 8) == 8));
                Cluster cluster = (Cluster) sparseArray.get(2);
                if (cluster != null) {
                    sparseArray2.append(2, buildSections((List) ref$ObjectRef.element, cluster, i, -32L, false));
                }
                Cluster cluster2 = (Cluster) sparseArray.get(4);
                if (cluster2 != null) {
                    sparseArray2.append(4, buildSections((List) ref$ObjectRef.element, cluster2, i, -512L, false));
                }
                TimingTracing.addSplit("buildSections");
                ClusteredList clusteredList = new ClusteredList((List) ref$ObjectRef.element, sparseArray2, sparseArray, !z, 0, 16, null);
                if (sparseArray2.size() == 1) {
                    clusteredList.select(sparseArray2.keyAt(0), !z);
                }
                return clusteredList;
            }
            return (List) ref$ObjectRef.element;
        } finally {
            TimingTracing.stopTracing(null);
        }
    }

    /* renamed from: processCache$lambda-1 */
    public static final int m1213processCache$lambda1(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        return Intrinsics.compare(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i));
    }

    /* renamed from: processCache$lambda-2 */
    public static final int m1214processCache$lambda2(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        if (mediaCacheItem.columnEquals(mediaCacheItem2, i) || GalleryDateUtils.isSameMonth(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i))) {
            return 0;
        }
        return Intrinsics.compare(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i));
    }

    /* renamed from: processCache$lambda-3 */
    public static final int m1215processCache$lambda3(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        if (mediaCacheItem.columnEquals(mediaCacheItem2, i) || GalleryDateUtils.isSameYear(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i))) {
            return 0;
        }
        return Intrinsics.compare(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i));
    }

    public final List<MediaSection> buildSections(List<? extends IMedia> list, Cluster cluster, int i, long j, boolean z) {
        ArrayList arrayList = new ArrayList(cluster.getGroupCount(true));
        int groupCount = cluster.getGroupCount(true);
        int i2 = 0;
        while (i2 < groupCount) {
            int i3 = i2 + 1;
            int groupStartPosition = cluster.getGroupStartPosition(i2, true);
            String groupLabel = cluster.getGroupLabel(i2, true);
            IMedia iMedia = list.get(groupStartPosition);
            if (z) {
                arrayList.add(new RecentMediaSection(generateGroupId4Recent(iMedia), cluster.getChildCount(i2, true), groupLabel, iMedia.getAlbumId()));
            } else {
                arrayList.add(new MediaSection(iMedia.getOrderDate(i) & j, cluster.getChildCount(i2, true), groupLabel));
            }
            i2 = i3;
        }
        return arrayList;
    }

    public final long generateGroupId4Recent(IMedia iMedia) {
        Calendar acquire = GalleryDateUtils.acquire();
        acquire.setTimeInMillis(iMedia.getDateModified());
        int i = acquire.get(5);
        int i2 = acquire.get(11);
        GalleryDateUtils.release(acquire);
        int i3 = (((((acquire.get(2) + 1) << 5) | i) << 5) | i2) << 15;
        Long albumId = iMedia.getAlbumId();
        return ((int) (albumId == null ? 0L : albumId.longValue())) | i3;
    }

    @Override // com.miui.gallery.provider.cache.IMediaProcessor
    public List<IRecord> processCursor(Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "cursor");
        int count = cursor.getCount();
        boolean z = cursor.getExtras().getBoolean("extra_show_headers", true);
        int i = cursor.getExtras().getInt("extra_group_sort_column", -1);
        if (!(i >= 0)) {
            throw new IllegalArgumentException("invalid group sort column".toString());
        }
        Pair<List<IMedia>, List<Integer>> walkCursor = walkCursor(cursor, i);
        List<IMedia> component1 = walkCursor.component1();
        List<Integer> component2 = walkCursor.component2();
        Pair<List<MediaSection>, Cluster> parseSectionsAndCluster = parseSectionsAndCluster(cursor, "extra_timeline_item_count_in_group", "extra_timeline_group_start_pos", "extra_timeline_group_labels", count, component1, component2, i);
        if (parseSectionsAndCluster == null) {
            return component1;
        }
        List<MediaSection> component12 = parseSectionsAndCluster.component1();
        Cluster component22 = parseSectionsAndCluster.component2();
        Cluster cluster = cluster(cursor, "extra_month_timeline_item_count_in_group", "extra_month_timeline_group_start_pos", "extra_month_timeline_group_start_locations", count, component2);
        Cluster cluster2 = cluster(cursor, "extra_year_timeline_item_count_in_group", "extra_year_timeline_group_start_pos", "extra_year_timeline_group_start_locations", count, component2);
        SparseArray sparseArray = new SparseArray(3);
        sparseArray.append(1, component22);
        if (cluster != null) {
            sparseArray.append(2, cluster);
        }
        if (cluster2 != null) {
            sparseArray.append(4, cluster2);
        }
        SparseArray sparseArray2 = new SparseArray(1);
        sparseArray2.put(1, component12);
        return new ClusteredList(component1, sparseArray2, sparseArray, !z, 0, 16, null);
    }

    public final Pair<List<IMedia>, List<Integer>> foldMedias(List<? extends MediaCacheItem> list) {
        if (this.foldBurst) {
            final LinkedList linkedList = new LinkedList();
            HashMap hashMap = new HashMap();
            long j = -1;
            int i = 0;
            for (MediaCacheItem mediaCacheItem : list) {
                int i2 = i + 1;
                if (mediaCacheItem.getId() == j) {
                    DefaultLogger.e("MediaProcessor", Intrinsics.stringPlus("Detected duplicate item: ", mediaCacheItem));
                } else {
                    j = mediaCacheItem.getId();
                    if (mediaCacheItem.getBurstGroupKey() > 0) {
                        ((LinkedList) hashMap.computeIfAbsent(Long.valueOf(mediaCacheItem.getBurstGroupKey()), new Function() { // from class: com.miui.gallery.provider.cache.MediaProcessor$$ExternalSyntheticLambda4
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return MediaProcessor.m1209$r8$lambda$YemxGpBv4qdY3bbWxHvfhFxY(linkedList, (Long) obj);
                            }
                        })).add(new BurstMedia(mediaCacheItem.getBurstIndex(), i, mediaCacheItem));
                    } else {
                        LinkedList linkedList2 = new LinkedList();
                        linkedList2.add(new BurstMedia(mediaCacheItem.getBurstIndex(), i, mediaCacheItem));
                        linkedList.add(linkedList2);
                    }
                }
                i = i2;
            }
            ArrayList arrayList = new ArrayList(linkedList.size());
            ArrayList arrayList2 = new ArrayList(linkedList.size());
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                LinkedList group = (LinkedList) it.next();
                if (group.size() > 1) {
                    int i3 = Integer.MAX_VALUE;
                    int i4 = Integer.MIN_VALUE;
                    LinkedList linkedList3 = new LinkedList();
                    Intrinsics.checkNotNullExpressionValue(group, "group");
                    int i5 = 0;
                    int i6 = 0;
                    for (Object obj : group) {
                        int i7 = i6 + 1;
                        if (i6 < 0) {
                            CollectionsKt__CollectionsKt.throwIndexOverflow();
                        }
                        BurstMedia burstMedia = (BurstMedia) obj;
                        i3 = RangesKt___RangesKt.coerceAtMost(i3, burstMedia.getPosition());
                        if (burstMedia.getBurstIndex() > i4) {
                            i4 = burstMedia.getBurstIndex();
                            i5 = i6;
                        }
                        linkedList3.add(burstMedia.getMedia());
                        i6 = i7;
                    }
                    arrayList.add(Integer.valueOf(i3));
                    arrayList2.add(new MediaGroup(linkedList3, i5));
                } else {
                    Intrinsics.checkNotNullExpressionValue(group, "group");
                    BurstMedia burstMedia2 = (BurstMedia) CollectionsKt___CollectionsKt.first(group);
                    arrayList.add(Integer.valueOf(burstMedia2.getPosition()));
                    if (burstMedia2.getMedia().getBurstGroupKey() > 0) {
                        arrayList2.add(new MediaGroup(CollectionsKt__CollectionsJVMKt.listOf(burstMedia2.getMedia()), 0));
                    } else {
                        arrayList2.add(burstMedia2.getMedia());
                    }
                }
            }
            return TuplesKt.to(arrayList2, arrayList);
        }
        return TuplesKt.to(list, null);
    }

    /* renamed from: foldMedias$lambda-12 */
    public static final LinkedList m1212foldMedias$lambda12(LinkedList groupList, Long it) {
        Intrinsics.checkNotNullParameter(groupList, "$groupList");
        Intrinsics.checkNotNullParameter(it, "it");
        LinkedList linkedList = new LinkedList();
        groupList.add(linkedList);
        return linkedList;
    }

    public final Pair<List<IMedia>, List<Integer>> walkCursor(Cursor cursor, int i) {
        if (this.foldBurst) {
            int count = cursor.getCount();
            final LinkedList linkedList = new LinkedList();
            HashMap hashMap = new HashMap();
            int columnIndex = cursor.getColumnIndex("burst_group_id");
            int columnIndex2 = cursor.getColumnIndex("burst_index");
            long j = -1;
            int i2 = 0;
            while (i2 < count) {
                int i3 = i2 + 1;
                if (cursor.moveToPosition(i2)) {
                    Media readCursorRow = readCursorRow(cursor, i);
                    if (readCursorRow.getId() == j) {
                        DefaultLogger.e("MediaProcessor", Intrinsics.stringPlus("Detected duplicate item: ", readCursorRow));
                    } else {
                        j = readCursorRow.getId();
                        if (columnIndex > 0 && cursor.getLong(columnIndex) > 0) {
                            ((LinkedList) hashMap.computeIfAbsent(Long.valueOf(cursor.getLong(columnIndex)), new Function() { // from class: com.miui.gallery.provider.cache.MediaProcessor$$ExternalSyntheticLambda3
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    return MediaProcessor.$r8$lambda$H0FQDIHJXgpmce2ZDuf7ndiWLOk(linkedList, (Long) obj);
                                }
                            })).add(new BurstMedia(cursor.getInt(columnIndex2), i2, readCursorRow));
                        } else {
                            LinkedList linkedList2 = new LinkedList();
                            linkedList2.add(new BurstMedia(cursor.getInt(columnIndex2), i2, readCursorRow));
                            linkedList.add(linkedList2);
                        }
                    }
                }
                i2 = i3;
            }
            ArrayList arrayList = new ArrayList(linkedList.size());
            ArrayList arrayList2 = new ArrayList(linkedList.size());
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                LinkedList group = (LinkedList) it.next();
                if (group.size() > 1) {
                    int i4 = Integer.MAX_VALUE;
                    int i5 = Integer.MIN_VALUE;
                    LinkedList linkedList3 = new LinkedList();
                    Intrinsics.checkNotNullExpressionValue(group, "group");
                    int i6 = 0;
                    int i7 = 0;
                    for (Object obj : group) {
                        int i8 = i7 + 1;
                        if (i7 < 0) {
                            CollectionsKt__CollectionsKt.throwIndexOverflow();
                        }
                        BurstMedia burstMedia = (BurstMedia) obj;
                        i4 = RangesKt___RangesKt.coerceAtMost(i4, burstMedia.getPosition());
                        if (burstMedia.getBurstIndex() > i5) {
                            i5 = burstMedia.getBurstIndex();
                            i6 = i7;
                        }
                        linkedList3.add(burstMedia.getMedia());
                        i7 = i8;
                    }
                    arrayList.add(Integer.valueOf(i4));
                    arrayList2.add(new MediaGroup(linkedList3, i6));
                } else {
                    Intrinsics.checkNotNullExpressionValue(group, "group");
                    BurstMedia burstMedia2 = (BurstMedia) CollectionsKt___CollectionsKt.first(group);
                    arrayList.add(Integer.valueOf(burstMedia2.getPosition()));
                    if (((Media) burstMedia2.getMedia()).getBurstGroupKey() > 0) {
                        arrayList2.add(new MediaGroup(CollectionsKt__CollectionsJVMKt.listOf(burstMedia2.getMedia()), 0));
                    } else {
                        arrayList2.add(burstMedia2.getMedia());
                    }
                }
            }
            return TuplesKt.to(arrayList2, arrayList);
        }
        ArrayList arrayList3 = new ArrayList(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                arrayList3.add(readCursorRow(cursor, i));
            } while (cursor.moveToNext());
            return TuplesKt.to(arrayList3, null);
        }
        return TuplesKt.to(arrayList3, null);
    }

    /* renamed from: walkCursor$lambda-16 */
    public static final LinkedList m1216walkCursor$lambda16(LinkedList groupList, Long it) {
        Intrinsics.checkNotNullParameter(groupList, "$groupList");
        Intrinsics.checkNotNullParameter(it, "it");
        LinkedList linkedList = new LinkedList();
        groupList.add(linkedList);
        return linkedList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.util.List] */
    public final Pair<List<MediaSection>, Cluster> parseSectionsAndCluster(Cursor cursor, String str, String str2, String str3, int i, List<? extends IMedia> list, List<Integer> list2, int i2) {
        Unit unit;
        Unit unit2;
        Unit unit3;
        Bundle extras = cursor.getExtras();
        if (!extras.containsKey(str) || !extras.containsKey(str2) || !extras.containsKey(str3)) {
            return null;
        }
        ?? emptyList = CollectionsKt__CollectionsKt.emptyList();
        List emptyList2 = CollectionsKt__CollectionsKt.emptyList();
        List emptyList3 = CollectionsKt__CollectionsKt.emptyList();
        ArrayList<Integer> integerArrayList = extras.getIntegerArrayList(str);
        if (integerArrayList == null) {
            integerArrayList = emptyList;
            unit = null;
        } else {
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            return null;
        }
        ArrayList<Integer> integerArrayList2 = extras.getIntegerArrayList(str2);
        if (integerArrayList2 == null) {
            unit2 = null;
        } else {
            unit2 = Unit.INSTANCE;
            emptyList2 = integerArrayList2;
        }
        if (unit2 == null) {
            return null;
        }
        ArrayList<String> stringArrayList = extras.getStringArrayList(str3);
        if (stringArrayList == null) {
            unit3 = null;
        } else {
            unit3 = Unit.INSTANCE;
            emptyList3 = stringArrayList;
        }
        if (unit3 == null) {
            return null;
        }
        if (this.foldBurst) {
            if (list2 == null) {
                return null;
            }
            MediaProcessorHelper mediaProcessorHelper = MediaProcessorHelper.INSTANCE;
            mediaProcessorHelper.wrapGroupInfos(list2, i, TypeIntrinsics.asMutableList(emptyList2), TypeIntrinsics.asMutableList(integerArrayList));
            mediaProcessorHelper.removeEmptyGroup(TypeIntrinsics.asMutableList(integerArrayList), TypeIntrinsics.asMutableList(emptyList2), TypeIntrinsics.asMutableList(emptyList3));
        }
        ArrayList arrayList = new ArrayList(integerArrayList.size());
        int i3 = 0;
        for (Integer num : integerArrayList) {
            arrayList.add(new MediaSection(list.get(((Number) emptyList2.get(i3)).intValue()).getOrderDate(i2), num.intValue(), (String) emptyList3.get(i3)));
            i3++;
        }
        return TuplesKt.to(arrayList, new MediaCluster(integerArrayList, emptyList2, emptyList3));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.util.List] */
    public final Cluster cluster(Cursor cursor, String str, String str2, String str3, int i, List<Integer> list) {
        Unit unit;
        Unit unit2;
        Unit unit3;
        Bundle extras = cursor.getExtras();
        if (!extras.containsKey(str) || !extras.containsKey(str2) || !extras.containsKey(str3)) {
            return null;
        }
        ?? emptyList = CollectionsKt__CollectionsKt.emptyList();
        List emptyList2 = CollectionsKt__CollectionsKt.emptyList();
        List emptyList3 = CollectionsKt__CollectionsKt.emptyList();
        ArrayList<Integer> integerArrayList = extras.getIntegerArrayList(str);
        if (integerArrayList == null) {
            integerArrayList = emptyList;
            unit = null;
        } else {
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            return null;
        }
        ArrayList<Integer> integerArrayList2 = extras.getIntegerArrayList(str2);
        if (integerArrayList2 == null) {
            unit2 = null;
        } else {
            unit2 = Unit.INSTANCE;
            emptyList2 = integerArrayList2;
        }
        if (unit2 == null) {
            return null;
        }
        ArrayList<String> stringArrayList = extras.getStringArrayList(str3);
        if (stringArrayList == null) {
            unit3 = null;
        } else {
            unit3 = Unit.INSTANCE;
            emptyList3 = stringArrayList;
        }
        if (unit3 == null) {
            return null;
        }
        if (this.foldBurst) {
            if (list == null) {
                return null;
            }
            MediaProcessorHelper mediaProcessorHelper = MediaProcessorHelper.INSTANCE;
            mediaProcessorHelper.wrapGroupInfos(list, i, TypeIntrinsics.asMutableList(emptyList2), TypeIntrinsics.asMutableList(integerArrayList));
            mediaProcessorHelper.removeEmptyGroup(TypeIntrinsics.asMutableList(integerArrayList), TypeIntrinsics.asMutableList(emptyList2), TypeIntrinsics.asMutableList(emptyList3));
        }
        return new MediaCluster(integerArrayList, emptyList2, emptyList3);
    }

    public final Media readCursorRow(Cursor cursor, int i) {
        long j = cursor.getLong(0);
        String string = cursor.getString(1);
        long j2 = cursor.getLong(2);
        String string2 = cursor.getString(3);
        String string3 = cursor.getString(4);
        String string4 = cursor.getString(5);
        int i2 = cursor.getInt(6);
        String string5 = cursor.getString(7);
        long j3 = cursor.getLong(8);
        long j4 = cursor.getLong(10);
        String string6 = cursor.getString(11);
        String string7 = cursor.getString(12);
        String string8 = cursor.getString(13);
        String string9 = cursor.getString(14);
        Character ch2 = null;
        Character firstOrNull = string9 == null ? null : StringsKt___StringsKt.firstOrNull(string9);
        String string10 = cursor.getString(15);
        String string11 = cursor.getString(16);
        if (string11 != null) {
            ch2 = StringsKt___StringsKt.firstOrNull(string11);
        }
        String string12 = cursor.getString(17);
        long j5 = cursor.getLong(18);
        int i3 = cursor.getInt(19);
        int i4 = cursor.getInt(20);
        byte[] blob = cursor.getBlob(21);
        int i5 = cursor.getInt(22);
        int i6 = cursor.getInt(23);
        Long l = CacheItem.TRUE;
        return new Media(j, string, j2, string2, string3, string4, i2, string5, j3, j4, string6, string7, string8, firstOrNull, string10, ch2, string12, j5, i3, i4, blob, i5, i6, l == Long.valueOf(cursor.getLong(24)), cursor.getString(25), cursor.getLong(26), l == Long.valueOf(cursor.getLong(27)), cursor.getLong(28), cursor.getString(29), cursor.getLong(30), cursor.getInt(31), cursor.getString(32), cursor.getInt(33) > 0, cursor.getString(34), cursor.getLong(35), cursor.getLong(36), cursor.getInt(37), cursor.getInt(38), cursor.getLong(39), cursor.getString(40), l == Long.valueOf(cursor.getLong(41)), l == Long.valueOf(cursor.getLong(42)), cursor.getInt(43), null, cursor.getString(46), cursor.getLong(i));
    }

    /* compiled from: MediaProcessor.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
