package com.miui.gallery.ui.pictures.cluster;

import android.database.Cursor;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.data.MediaCluster;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.ui.pictures.PictureViewMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/* loaded from: classes2.dex */
public class TimelineClusterAdapter {
    public TimelineCluster[] mClusters;
    public TimelineCluster mCurrent;
    public PictureViewMode mCurrentMode;
    public boolean mShowTimeLine = true;
    public final TimelineModeLookup mTimelineModeLookup = new DefaultTimelineModeLookup();

    /* loaded from: classes2.dex */
    public interface TimelineModeLookup {
        TimelineMode getTimelineMode(PictureViewMode pictureViewMode);
    }

    public void swapData(Cursor cursor, ArrayList<Integer> arrayList) {
        TimelineCluster[] create = TimelineClusterFactory.create(cursor, arrayList);
        this.mClusters = create;
        this.mCurrent = create[this.mTimelineModeLookup.getTimelineMode(this.mCurrentMode).ordinal()];
    }

    public void swapData(List<IRecord> list) {
        TimelineCluster[] timelineClusterArr = new TimelineCluster[TimelineMode.values().length];
        this.mClusters = timelineClusterArr;
        if (list instanceof ClusteredList) {
            ClusteredList clusteredList = (ClusteredList) list;
            if (clusteredList.hasCluster(1)) {
                this.mClusters[TimelineMode.DAY.ordinal()] = new StandardTimelineCluster2(clusteredList.clusterOf(1));
            } else {
                this.mClusters[TimelineMode.DAY.ordinal()] = TimelineCluster.DUMMY;
            }
            if (clusteredList.hasCluster(2)) {
                this.mClusters[TimelineMode.MONTH_AGGREGATED.ordinal()] = new AggregatedTimelineCluster2(clusteredList.clusterOf(2));
                this.mClusters[TimelineMode.MONTH.ordinal()] = new StandardTimelineCluster2(clusteredList.clusterOf(2));
            } else {
                TimelineCluster[] timelineClusterArr2 = this.mClusters;
                int ordinal = TimelineMode.MONTH_AGGREGATED.ordinal();
                TimelineCluster timelineCluster = TimelineCluster.DUMMY;
                timelineClusterArr2[ordinal] = timelineCluster;
                this.mClusters[TimelineMode.MONTH.ordinal()] = timelineCluster;
            }
            if (clusteredList.hasCluster(4)) {
                this.mClusters[TimelineMode.YEAR_AGGREGATED.ordinal()] = new AggregatedTimelineCluster2(clusteredList.clusterOf(4));
            } else {
                this.mClusters[TimelineMode.YEAR_AGGREGATED.ordinal()] = TimelineCluster.DUMMY;
            }
        } else {
            timelineClusterArr[TimelineMode.DAY.ordinal()] = new StandardTimelineCluster2(new MediaCluster(list));
            TimelineCluster[] timelineClusterArr3 = this.mClusters;
            int ordinal2 = TimelineMode.MONTH_AGGREGATED.ordinal();
            TimelineCluster timelineCluster2 = TimelineCluster.DUMMY;
            timelineClusterArr3[ordinal2] = timelineCluster2;
            this.mClusters[TimelineMode.MONTH.ordinal()] = timelineCluster2;
            this.mClusters[TimelineMode.YEAR_AGGREGATED.ordinal()] = timelineCluster2;
        }
        this.mCurrent = this.mClusters[this.mTimelineModeLookup.getTimelineMode(this.mCurrentMode).ordinal()];
    }

    public void setViewMode(PictureViewMode pictureViewMode) {
        this.mCurrentMode = pictureViewMode;
        TimelineCluster[] timelineClusterArr = this.mClusters;
        if (timelineClusterArr != null) {
            this.mCurrent = timelineClusterArr[this.mTimelineModeLookup.getTimelineMode(pictureViewMode).ordinal()];
        }
    }

    public void setShowTimeLine(boolean z) {
        this.mShowTimeLine = z;
    }

    public int getGroupCount() {
        TimelineCluster timelineCluster = this.mCurrent;
        if (timelineCluster == null) {
            return 0;
        }
        return timelineCluster.getGroupCount(this.mShowTimeLine);
    }

    public int getChildCount(int i, int i2) {
        TimelineCluster timelineCluster = this.mCurrent;
        if (timelineCluster == null) {
            return 0;
        }
        return timelineCluster.getChildCount(i, i2, this.mCurrentMode.getAggregatedLines(), this.mShowTimeLine);
    }

    public int[] getGroupPositions(int i) {
        TimelineCluster timelineCluster = this.mCurrent;
        return timelineCluster == null ? new int[0] : timelineCluster.getGroupPositions(i, this.mShowTimeLine);
    }

    public String getGroupLabel(int i) {
        TimelineCluster timelineCluster = this.mCurrent;
        return timelineCluster == null ? "" : timelineCluster.getGroupLabel(i, this.mShowTimeLine);
    }

    public int getGroupStartPosition(int i) {
        TimelineCluster timelineCluster = this.mCurrent;
        if (timelineCluster == null) {
            return 0;
        }
        return timelineCluster.getGroupStartPosition(i, this.mShowTimeLine);
    }

    public List<Integer> getGroupStartPositions() {
        TimelineCluster timelineCluster = this.mCurrent;
        if (timelineCluster == null) {
            return Collections.emptyList();
        }
        return timelineCluster.getGroupStartPositions(this.mShowTimeLine);
    }

    public float getGroupHeight(int i, int i2, int i3, int i4) {
        TimelineCluster timelineCluster = this.mCurrent;
        if (timelineCluster == null) {
            return 0.0f;
        }
        return timelineCluster.getGroupHeight(i, i2, i3, this.mCurrentMode.getSpacing(), i4, this.mShowTimeLine);
    }

    public int computeScrollRange(int i, Function<Integer, Integer> function, int i2) {
        TimelineCluster timelineCluster = this.mCurrent;
        if (timelineCluster == null) {
            return 0;
        }
        return timelineCluster.computeScrollRange(i, this.mCurrentMode.getAggregatedLines(), this.mCurrentMode.getSpacing(), function, i2, this.mShowTimeLine);
    }

    public int computeScrollOffset(int i, int i2, Function<Integer, Integer> function, int i3) {
        TimelineCluster timelineCluster = this.mCurrent;
        if (timelineCluster == null) {
            return 0;
        }
        return timelineCluster.computeScrollOffset(i, i2, this.mCurrentMode.getAggregatedLines(), this.mCurrentMode.getSpacing(), function, i3, this.mShowTimeLine);
    }

    public int[] computeScrollPositionAndOffset(int i, Function<Integer, Integer> function, int i2, int i3, float f) {
        TimelineCluster timelineCluster = this.mCurrent;
        return timelineCluster == null ? new int[]{0, 0} : timelineCluster.computeScrollPositionAndOffset(i, this.mCurrentMode.getAggregatedLines(), this.mCurrentMode.getSpacing(), function, i2, i3, f, this.mShowTimeLine);
    }

    public int computeDataPosition(int i, Function<Integer, Integer> function, int i2, int i3, int i4, float f, int i5) {
        TimelineCluster timelineCluster = this.mCurrent;
        if (timelineCluster == null) {
            return 0;
        }
        return timelineCluster.computeDataPosition(i, this.mCurrentMode.getAggregatedLines(), this.mCurrentMode.getSpacing(), function, i2, i3, i4, f, i5, this.mShowTimeLine);
    }

    public TimelineCluster getTimelineCluster(PictureViewMode pictureViewMode) {
        return this.mClusters[this.mTimelineModeLookup.getTimelineMode(pictureViewMode).ordinal()];
    }

    /* loaded from: classes2.dex */
    public static final class DefaultTimelineModeLookup implements TimelineModeLookup {
        public DefaultTimelineModeLookup() {
        }

        @Override // com.miui.gallery.ui.pictures.cluster.TimelineClusterAdapter.TimelineModeLookup
        public TimelineMode getTimelineMode(PictureViewMode pictureViewMode) {
            if (PictureViewMode.isLargeDevice()) {
                if (pictureViewMode == PictureViewMode.LARGE_THUMB) {
                    return TimelineMode.DAY;
                }
                if (pictureViewMode == PictureViewMode.MICRO_THUMB) {
                    return TimelineMode.MONTH;
                }
                if (pictureViewMode == PictureViewMode.MINI_THUMB) {
                    return TimelineMode.MONTH_AGGREGATED;
                }
            } else if (pictureViewMode == PictureViewMode.LARGE_THUMB || pictureViewMode == PictureViewMode.MICRO_THUMB) {
                return TimelineMode.DAY;
            } else {
                if (pictureViewMode == PictureViewMode.MINI_THUMB) {
                    return TimelineMode.MONTH_AGGREGATED;
                }
            }
            return TimelineMode.YEAR_AGGREGATED;
        }
    }
}
