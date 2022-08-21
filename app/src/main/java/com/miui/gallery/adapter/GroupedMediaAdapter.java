package com.miui.gallery.adapter;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.ui.pictures.cluster.TimelineCluster;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import java.util.function.Function;

/* loaded from: classes.dex */
public abstract class GroupedMediaAdapter<M, S, GVH extends BaseViewHolder, CVH extends BaseViewHolder> extends BaseGroupedMediaAdapter<M, S, GVH, CVH> {
    public int mHeaderHeight;
    public final Function<Integer, Integer> mHeaderHeightFunction;
    public TimelineCluster mTimelineCluster;

    /* renamed from: $r8$lambda$Xuqc9r-8EC4WEda44-9YcGBXzSg */
    public static /* synthetic */ Integer m500$r8$lambda$Xuqc9r8EC4WEda449YcGBXzSg(GroupedMediaAdapter groupedMediaAdapter, Integer num) {
        return groupedMediaAdapter.lambda$new$0(num);
    }

    public abstract S processBursts(S s);

    public abstract void processClusters(S s, S s2);

    public /* synthetic */ Integer lambda$new$0(Integer num) {
        return Integer.valueOf(getHeaderHeight(num.intValue()) + this.mSpacing);
    }

    public GroupedMediaAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, int i) {
        super(context, syncStateDisplay$DisplayScene, i);
        this.mTimelineCluster = TimelineCluster.DUMMY;
        this.mHeaderHeightFunction = new Function() { // from class: com.miui.gallery.adapter.GroupedMediaAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return GroupedMediaAdapter.m500$r8$lambda$Xuqc9r8EC4WEda449YcGBXzSg(GroupedMediaAdapter.this, (Integer) obj);
            }
        };
        this.mHeaderHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.time_line_header_height);
    }

    public int getHeaderHeight(int i) {
        return this.mHeaderHeight;
    }

    public S preSwapData(S s) {
        S processBursts = processBursts(s);
        processClusters(s, processBursts);
        return processBursts;
    }
}
