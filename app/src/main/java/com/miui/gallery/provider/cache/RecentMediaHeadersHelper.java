package com.miui.gallery.provider.cache;

import android.database.Cursor;
import android.os.Bundle;
import com.miui.gallery.util.StaticContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import miuix.pickerwidget.date.DateUtils;

/* compiled from: RecentMediaHeadersHelper.kt */
/* loaded from: classes2.dex */
public final class RecentMediaHeadersHelper {
    public static final RecentMediaHeadersHelper INSTANCE = new RecentMediaHeadersHelper();

    public static final void bindGroup2Cursor(List<Header> groups, Cursor cursor) {
        Intrinsics.checkNotNullParameter(groups, "groups");
        if (cursor == null || groups.isEmpty()) {
            return;
        }
        ArrayList<Integer> arrayList = new ArrayList<>(groups.size());
        ArrayList<Integer> arrayList2 = new ArrayList<>(groups.size());
        ArrayList<String> arrayList3 = new ArrayList<>(groups.size());
        for (Header header : groups) {
            arrayList2.add(Integer.valueOf(header.getStartPos()));
            arrayList.add(Integer.valueOf(header.getMediaCount()));
            arrayList3.add(header.getTimeLabel());
        }
        Bundle extras = cursor.getExtras();
        if (extras == null || Intrinsics.areEqual(extras, Bundle.EMPTY)) {
            extras = new Bundle();
            cursor.setExtras(extras);
        }
        extras.putIntegerArrayList("extra_timeline_item_count_in_group", arrayList);
        extras.putIntegerArrayList("extra_timeline_group_start_pos", arrayList2);
        extras.putStringArrayList("extra_timeline_group_labels", arrayList3);
    }

    public static final List<Header> resortAndGenerateHeaders(List<? extends MediaCacheItem> original, List<MediaCacheItem> sorted) {
        Intrinsics.checkNotNullParameter(original, "original");
        Intrinsics.checkNotNullParameter(sorted, "sorted");
        ArrayList<Group> arrayList = new ArrayList<>();
        int i = 0;
        for (MediaCacheItem mediaCacheItem : original) {
            INSTANCE.insertItemToGroups(mediaCacheItem.getDateModified(), mediaCacheItem.getAliasModifyDate(), mediaCacheItem.getAlbumId(), i, arrayList);
            i++;
        }
        LinkedList linkedList = new LinkedList();
        Iterator<Group> it = arrayList.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            Group next = it.next();
            long dateModified = original.get(next.getPositionList().get(0).intValue()).getDateModified();
            int size = next.getPositionList().size();
            String formatRelativeTime = DateUtils.formatRelativeTime(StaticContext.sGetAndroidContext(), dateModified, false);
            Intrinsics.checkNotNullExpressionValue(formatRelativeTime, "formatRelativeTime(Stati…t(), dateModified, false)");
            linkedList.add(new Header(i2, size, formatRelativeTime));
            i2 += next.getPositionList().size();
            for (Integer num : next.getPositionList()) {
                sorted.add(original.get(num.intValue()));
            }
        }
        return linkedList;
    }

    public final void insertItemToGroups(long j, int i, Long l, int i2, ArrayList<Group> arrayList) {
        boolean z = true;
        if (!arrayList.isEmpty()) {
            int size = arrayList.size() - 1;
            if (size >= 0) {
                while (true) {
                    int i3 = size - 1;
                    Group group = arrayList.get(size);
                    Intrinsics.checkNotNullExpressionValue(group, "groupList[i]");
                    Group group2 = group;
                    if (group2.getEndDate() - j >= 3600000) {
                        if (z && Intrinsics.areEqual(l, group2.getAlbumId()) && i == group2.getJulianDay()) {
                            group2.mergeItem(j, i2);
                            return;
                        } else {
                            arrayList.add(new Group(j, i, l, i2));
                            return;
                        }
                    } else if (Intrinsics.areEqual(l, group2.getAlbumId())) {
                        group2.mergeItem(j, i2);
                        return;
                    } else {
                        z = false;
                        if (i3 < 0) {
                            break;
                        }
                        size = i3;
                    }
                }
            }
            arrayList.add(new Group(j, i, l, i2));
            return;
        }
        arrayList.add(new Group(j, i, l, i2));
    }

    /* compiled from: RecentMediaHeadersHelper.kt */
    /* loaded from: classes2.dex */
    public static final class Group {
        public Long albumId;
        public long endDate;
        public int julianDay;
        public List<Integer> positionList;

        public Group(long j, int i, Long l, int i2) {
            this.endDate = j;
            this.julianDay = i;
            this.albumId = l;
            ArrayList arrayList = new ArrayList();
            this.positionList = arrayList;
            arrayList.add(Integer.valueOf(i2));
        }

        public final long getEndDate() {
            return this.endDate;
        }

        public final int getJulianDay() {
            return this.julianDay;
        }

        public final Long getAlbumId() {
            return this.albumId;
        }

        public final List<Integer> getPositionList() {
            return this.positionList;
        }

        public final void mergeItem(long j, int i) {
            this.endDate = j;
            this.positionList.add(Integer.valueOf(i));
        }
    }

    /* compiled from: RecentMediaHeadersHelper.kt */
    /* loaded from: classes2.dex */
    public static final class Header {
        public int mediaCount;
        public int startPos;
        public String timeLabel;

        public Header(int i, int i2, String timeLabel) {
            Intrinsics.checkNotNullParameter(timeLabel, "timeLabel");
            this.startPos = i;
            this.mediaCount = i2;
            this.timeLabel = timeLabel;
        }

        public final int getMediaCount() {
            return this.mediaCount;
        }

        public final int getStartPos() {
            return this.startPos;
        }

        public final String getTimeLabel() {
            return this.timeLabel;
        }
    }
}
