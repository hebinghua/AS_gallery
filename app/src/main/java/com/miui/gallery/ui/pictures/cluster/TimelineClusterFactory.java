package com.miui.gallery.ui.pictures.cluster;

import android.database.Cursor;
import android.os.Bundle;
import ch.qos.logback.core.CoreConstants;
import com.google.common.base.Splitter;
import com.miui.gallery.Config$PictureView;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BurstFilterCursor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class TimelineClusterFactory {
    public static TimelineCluster[] create(Cursor cursor, ArrayList<Integer> arrayList) {
        TimelineCluster[] timelineClusterArr = new TimelineCluster[TimelineMode.values().length];
        Bundle extras = cursor.getExtras();
        ArrayList<Integer> integerArrayList = extras.getIntegerArrayList("extra_timeline_item_count_in_group");
        ArrayList<Integer> integerArrayList2 = extras.getIntegerArrayList("extra_timeline_group_start_pos");
        Collection stringArrayList = extras.getStringArrayList("extra_timeline_group_labels");
        if (integerArrayList != null && integerArrayList2 != null) {
            ArrayList<Integer> arrayList2 = new ArrayList<>(integerArrayList);
            ArrayList<Integer> arrayList3 = new ArrayList<>(integerArrayList2);
            if (stringArrayList == null) {
                stringArrayList = Collections.emptyList();
            }
            ArrayList arrayList4 = new ArrayList(stringArrayList);
            if (BaseMiscUtil.isValid(arrayList) && BaseMiscUtil.isValid(arrayList3)) {
                BurstFilterCursor.wrapGroupInfos(arrayList, cursor.getCount(), arrayList3, arrayList2);
            }
            timelineClusterArr[TimelineMode.DAY.ordinal()] = new StandardTimelineCluster(arrayList2, arrayList3, arrayList4);
            integerArrayList2 = arrayList3;
            integerArrayList = arrayList2;
        } else {
            timelineClusterArr[TimelineMode.DAY.ordinal()] = new BunchTimelineCluster(cursor);
        }
        if (Config$PictureView.isFilterImagesForMonthView()) {
            ArrayList<String> stringArrayList2 = extras.getStringArrayList("extra_month_timeline_group_positions");
            List stringArrayList3 = extras.getStringArrayList("extra_month_timeline_group_start_locations");
            if (stringArrayList2 != null) {
                List list = (List) stringArrayList2.stream().map(TimelineClusterFactory$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
                if (BaseMiscUtil.isValid(arrayList) && BaseMiscUtil.isValid(stringArrayList2)) {
                    BurstFilterCursor.wrapGroupInfos(arrayList, list);
                }
                timelineClusterArr[TimelineMode.MONTH.ordinal()] = new StandardTimelineCluster(integerArrayList, integerArrayList2, stringArrayList3 == null ? Collections.emptyList() : stringArrayList3);
                int ordinal = TimelineMode.MONTH_AGGREGATED.ordinal();
                if (stringArrayList3 == null) {
                    stringArrayList3 = Collections.emptyList();
                }
                timelineClusterArr[ordinal] = new FilteredAggregatedTimelineCluster(list, stringArrayList3);
            } else {
                timelineClusterArr[TimelineMode.MONTH_AGGREGATED.ordinal()] = TimelineCluster.DUMMY;
            }
        } else {
            ArrayList<Integer> integerArrayList3 = extras.getIntegerArrayList("extra_month_timeline_item_count_in_group");
            ArrayList<Integer> integerArrayList4 = extras.getIntegerArrayList("extra_month_timeline_group_start_pos");
            Collection stringArrayList4 = extras.getStringArrayList("extra_month_timeline_group_start_locations");
            if (integerArrayList3 != null && integerArrayList4 != null) {
                ArrayList arrayList5 = new ArrayList(integerArrayList3);
                ArrayList arrayList6 = new ArrayList(integerArrayList4);
                if (stringArrayList4 == null) {
                    stringArrayList4 = Collections.emptyList();
                }
                ArrayList arrayList7 = new ArrayList(stringArrayList4);
                if (BaseMiscUtil.isValid(arrayList) && BaseMiscUtil.isValid(arrayList6)) {
                    BurstFilterCursor.wrapGroupInfos(arrayList, cursor.getCount(), arrayList6, arrayList5);
                }
                timelineClusterArr[TimelineMode.MONTH.ordinal()] = new StandardTimelineCluster(arrayList5, arrayList6, arrayList7);
                timelineClusterArr[TimelineMode.MONTH_AGGREGATED.ordinal()] = new AggregatedTimelineCluster(arrayList5, arrayList6, arrayList7);
            } else {
                timelineClusterArr[TimelineMode.MONTH_AGGREGATED.ordinal()] = TimelineCluster.DUMMY;
            }
        }
        if (Config$PictureView.isFilterImagesForMonthView()) {
            ArrayList<String> stringArrayList5 = extras.getStringArrayList("extra_year_timeline_group_positions");
            List stringArrayList6 = extras.getStringArrayList("extra_year_timeline_group_start_locations");
            if (stringArrayList5 != null) {
                List list2 = (List) stringArrayList5.stream().map(TimelineClusterFactory$$ExternalSyntheticLambda2.INSTANCE).collect(Collectors.toList());
                if (BaseMiscUtil.isValid(arrayList) && BaseMiscUtil.isValid(stringArrayList5)) {
                    BurstFilterCursor.wrapGroupInfos(arrayList, list2);
                }
                int ordinal2 = TimelineMode.YEAR_AGGREGATED.ordinal();
                if (stringArrayList6 == null) {
                    stringArrayList6 = Collections.emptyList();
                }
                timelineClusterArr[ordinal2] = new FilteredAggregatedTimelineCluster(list2, stringArrayList6);
            } else {
                timelineClusterArr[TimelineMode.YEAR_AGGREGATED.ordinal()] = TimelineCluster.DUMMY;
            }
        } else {
            ArrayList<Integer> integerArrayList5 = extras.getIntegerArrayList("extra_year_timeline_item_count_in_group");
            ArrayList<Integer> integerArrayList6 = extras.getIntegerArrayList("extra_year_timeline_group_start_pos");
            Collection stringArrayList7 = extras.getStringArrayList("extra_year_timeline_group_start_locations");
            if (integerArrayList5 != null && integerArrayList6 != null) {
                ArrayList arrayList8 = new ArrayList(integerArrayList5);
                ArrayList arrayList9 = new ArrayList(integerArrayList6);
                if (stringArrayList7 == null) {
                    stringArrayList7 = Collections.emptyList();
                }
                ArrayList arrayList10 = new ArrayList(stringArrayList7);
                if (BaseMiscUtil.isValid(arrayList) && BaseMiscUtil.isValid(arrayList9)) {
                    BurstFilterCursor.wrapGroupInfos(arrayList, cursor.getCount(), arrayList9, arrayList8);
                }
                timelineClusterArr[TimelineMode.YEAR_AGGREGATED.ordinal()] = new AggregatedTimelineCluster(arrayList8, arrayList9, arrayList10);
            } else {
                timelineClusterArr[TimelineMode.YEAR_AGGREGATED.ordinal()] = TimelineCluster.DUMMY;
            }
        }
        return timelineClusterArr;
    }

    public static /* synthetic */ LinkedList lambda$create$0(String str) {
        return (LinkedList) Splitter.on((char) CoreConstants.COMMA_CHAR).splitToList(str).stream().mapToInt(TimelineClusterFactory$$ExternalSyntheticLambda5.INSTANCE).collect(TimelineClusterFactory$$ExternalSyntheticLambda4.INSTANCE, TimelineClusterFactory$$ExternalSyntheticLambda3.INSTANCE, TimelineClusterFactory$$ExternalSyntheticLambda0.INSTANCE);
    }

    public static /* synthetic */ LinkedList lambda$create$1(String str) {
        return (LinkedList) Splitter.on((char) CoreConstants.COMMA_CHAR).splitToList(str).stream().mapToInt(TimelineClusterFactory$$ExternalSyntheticLambda5.INSTANCE).collect(TimelineClusterFactory$$ExternalSyntheticLambda4.INSTANCE, TimelineClusterFactory$$ExternalSyntheticLambda3.INSTANCE, TimelineClusterFactory$$ExternalSyntheticLambda0.INSTANCE);
    }

    public static TimelineClusters createAndPack(Cursor cursor, ArrayList<Integer> arrayList) {
        return new TimelineClusters(create(cursor, arrayList));
    }

    /* loaded from: classes2.dex */
    public static class TimelineClusters implements Serializable {
        private TimelineCluster[] mClusters;

        public TimelineClusters(TimelineCluster[] timelineClusterArr) {
            this.mClusters = timelineClusterArr;
        }
    }
}
