package com.miui.gallery.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.ui.NormalTimeLineGridHeaderItem;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.ui.pictures.cluster.TimelineCluster;
import com.miui.gallery.ui.pictures.cluster.TimelineClusterAdapter;
import com.miui.gallery.ui.pictures.cluster.TransitFrame;
import com.miui.gallery.ui.pictures.view.IMultiImageView;
import com.miui.gallery.ui.pictures.view.ImageCellData;
import com.miui.gallery.ui.pictures.view.MultiImageViewFactory;
import com.miui.gallery.ui.pictures.view.SharedRecycledCellPool;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.AbsMultiImageViewHolder;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.miui.gallery.widget.recyclerview.AbsViewHolder;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.IMatchItemAdapter;
import com.miui.gallery.widget.recyclerview.transition.FuzzyMatchItem;
import com.miui.gallery.widget.recyclerview.transition.ITransitItem;
import com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter;
import com.miui.gallery.widget.recyclerview.transition.ThumbTransitionalItem;
import com.miui.gallery.widget.recyclerview.transition.TimelineTransitionalItem;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/* loaded from: classes.dex */
public abstract class MultiViewMediaAdapter<M, S> extends GroupedMediaAdapter<M, S, BaseViewHolder, AbsViewHolder> implements ITransitionalAdapter, IMatchItemAdapter {
    public static final String SEPARATOR = GalleryApp.sGetAndroidContext().getString(R.string.name_split);
    public TimelineClusterAdapter mClusterAdapter;
    public Configuration mConfig;
    public final Function<Integer, Integer> mHeaderHeightFunction;
    public int mHorizontalSpacing;
    public Lifecycle mLifecycle;
    public PictureViewMode mViewMode;

    public static /* synthetic */ Integer $r8$lambda$i3RO8Z4EhWoihmflirqrB8htnJs(MultiViewMediaAdapter multiViewMediaAdapter, Integer num) {
        return multiViewMediaAdapter.lambda$new$0(num);
    }

    public abstract AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle);

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getGroupItemViewType(int i) {
        return 2;
    }

    public abstract String getOptimalThumbFilePath(int i, boolean z);

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter
    public /* bridge */ /* synthetic */ void doBindChildViewHolder(BaseViewHolder baseViewHolder, int i, int i2, int i3, List list) {
        doBindChildViewHolder((AbsViewHolder) baseViewHolder, i, i2, i3, (List<Object>) list);
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter
    public /* bridge */ /* synthetic */ void doBindChildViewHolder(BaseViewHolder baseViewHolder, int i, int i2, List list) {
        doBindChildViewHolder((AbsViewHolder) baseViewHolder, i, i2, (List<Object>) list);
    }

    public /* synthetic */ Integer lambda$new$0(Integer num) {
        return Integer.valueOf(this.mShowTimeLine ? getHeaderHeight(num.intValue()) : 0);
    }

    public MultiViewMediaAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, int i, Lifecycle lifecycle) {
        super(context, syncStateDisplay$DisplayScene, i);
        this.mViewMode = PictureViewMode.LARGE_THUMB;
        this.mHeaderHeightFunction = new Function() { // from class: com.miui.gallery.adapter.MultiViewMediaAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return MultiViewMediaAdapter.$r8$lambda$i3RO8Z4EhWoihmflirqrB8htnJs(MultiViewMediaAdapter.this, (Integer) obj);
            }
        };
        this.mLifecycle = lifecycle;
        this.mClusterAdapter = new TimelineClusterAdapter();
        this.mConfig = context.getResources().getConfiguration();
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getChildItemViewType(int i, int i2) {
        return this.mViewMode.isAggregated() ? 3 : 1;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateGroupViewHolder */
    public BaseViewHolder mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        return BaseViewHolder.create(viewGroup, R.layout.normal_time_line_grid_header_item);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateChildViewHolder */
    public AbsViewHolder mo1337onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        View createSingleImageView;
        if (i == 3) {
            createSingleImageView = MultiImageViewFactory.create(viewGroup.getContext()).asView();
            IMultiImageView iMultiImageView = (IMultiImageView) createSingleImageView;
            iMultiImageView.setRecycledCellPoll(SharedRecycledCellPool.obtain());
            iMultiImageView.setSpanCount(this.mViewMode.getSpan());
            iMultiImageView.setSpacing(this.mHorizontalSpacing);
        } else {
            createSingleImageView = createSingleImageView(viewGroup);
        }
        if (i == 3) {
            return createMultiImageViewHolder(createSingleImageView, this.mLifecycle);
        }
        return createSingleImageViewHolder(createSingleImageView, this.mLifecycle);
    }

    public View createSingleImageView(ViewGroup viewGroup) {
        return BaseViewHolder.getView(viewGroup, R.layout.base_image_grid_item);
    }

    public AbsMultiImageViewHolder createMultiImageViewHolder(View view, Lifecycle lifecycle) {
        return new MultiImageViewHolder(view, lifecycle);
    }

    public void doBindChildViewHolder(AbsViewHolder absViewHolder, int i, int i2, int i3, List<Object> list) {
        absViewHolder.bindData(i, i2, list);
    }

    public final void doBindChildViewHolder(AbsViewHolder absViewHolder, int i, int i2, List<Object> list) {
        throw new IllegalArgumentException("Shouldn't reach here");
    }

    public void setPictureViewMode(PictureViewMode pictureViewMode) {
        this.mViewMode = pictureViewMode;
        this.mClusterAdapter.setViewMode(pictureViewMode);
        this.mHorizontalSpacing = pictureViewMode.getSpacing();
    }

    public void setConfiguration(Configuration configuration) {
        this.mConfig = configuration;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onViewRecycled(BaseViewHolder baseViewHolder) {
        DefaultLogger.v("MultiViewMediaAdapter", "onViewRecycled, ViewType: %d", Integer.valueOf(baseViewHolder.getItemViewType()));
        if (baseViewHolder instanceof AbsViewHolder) {
            ((AbsViewHolder) baseViewHolder).recycle();
        }
        super.onViewRecycled((MultiViewMediaAdapter<M, S>) baseViewHolder);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getRequestOptions(int i) {
        return getRequestOptions(i, this.mViewMode);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getPreviewRequestOptions(int i) {
        return getPreviewRequestOptions(i, this.mViewMode);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public Size getDisplayImageSize(int i) {
        return this.mViewMode.getImageSize();
    }

    public GlideOptions getRequestOptions(int i, PictureViewMode pictureViewMode) {
        return pictureViewMode.getRequestOptions().fileLength(getFileLength(i));
    }

    public GlideOptions getPreviewRequestOptions(int i, PictureViewMode pictureViewMode) {
        if (!PictureViewMode.isYearMode(pictureViewMode)) {
            if (i >= 0 && i < getItemCount()) {
                return GlideOptions.tinyThumbOf(getFileLength(i));
            }
            return GlideOptions.tinyThumbOf();
        }
        return null;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public final String getOptimalThumbFilePath(int i) {
        return getOptimalThumbFilePath(i, this.mViewMode == PictureViewMode.LARGE_THUMB);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter
    public FuzzyMatchItem packageMatchItem(int i, PictureViewMode pictureViewMode) {
        int[] unpackAdapterPosition = this.mClusterAdapter.getTimelineCluster(pictureViewMode).unpackAdapterPosition(i, this.mShowTimeLine);
        int packDataPosition = this.mClusterAdapter.getTimelineCluster(pictureViewMode).packDataPosition(unpackAdapterPosition[0], unpackAdapterPosition[1], this.mShowTimeLine);
        return new FuzzyMatchItem(i, getItemKey(packDataPosition), getLocation(packDataPosition), getItemSortTime(packDataPosition));
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter
    public FuzzyMatchItem fuzzyMatchItem(FuzzyMatchItem fuzzyMatchItem, PictureViewMode pictureViewMode) {
        TimelineCluster timelineCluster;
        int i;
        String str;
        long j;
        long j2;
        if (fuzzyMatchItem == null || getItemCount() == 0 || (timelineCluster = this.mClusterAdapter.getTimelineCluster(pictureViewMode)) == null) {
            return null;
        }
        int groupCount = timelineCluster.getGroupCount(this.mShowTimeLine);
        int i2 = 1;
        while (true) {
            if (i2 >= groupCount) {
                i = -1;
                break;
            } else if (fuzzyMatchItem.mDate > getItemSortTime(timelineCluster.getGroupStartPosition(i2, this.mShowTimeLine))) {
                i = i2 - 1;
                break;
            } else {
                i2++;
            }
        }
        if (i == -1) {
            i = groupCount - 1;
        }
        int i3 = 0;
        if (i != -1 && i < groupCount) {
            int[] groupPositions = timelineCluster.getGroupPositions(i, this.mShowTimeLine);
            if (groupPositions.length > 0) {
                int i4 = groupPositions[groupPositions.length - 1];
                int length = groupPositions.length;
                int i5 = 0;
                while (i3 < length) {
                    int i6 = groupPositions[i3];
                    long itemKey = getItemKey(i6);
                    if (itemKey == fuzzyMatchItem.mId || fuzzyMatchItem.mDate > getItemSortTime(i6) || i6 == i4) {
                        str = getLocation(i6);
                        j = getItemSortTime(i6);
                        i3 = i5;
                        j2 = itemKey;
                        break;
                    }
                    i5++;
                    i3++;
                }
                str = null;
                j = 0;
                i3 = i5;
                j2 = -1;
                return new FuzzyMatchItem(timelineCluster.packAdapterPosition(i, i3, this.mShowTimeLine), j2, str, j);
            }
        }
        str = null;
        j = 0;
        j2 = -1;
        return new FuzzyMatchItem(timelineCluster.packAdapterPosition(i, i3, this.mShowTimeLine), j2, str, j);
    }

    @Override // com.miui.gallery.widget.recyclerview.IMatchItemAdapter
    public FuzzyMatchItem fuzzyMatchToItem(FuzzyMatchItem fuzzyMatchItem, PictureViewMode pictureViewMode) {
        return fuzzyMatchItem(fuzzyMatchItem, pictureViewMode);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter
    public Rect estimateItemFrame(int i, int i2, long j, PictureViewMode pictureViewMode) {
        return this.mClusterAdapter.getTimelineCluster(pictureViewMode).estimateItemRect(i, i2, j, pictureViewMode.getSpan(), pictureViewMode.getAggregatedLines(), pictureViewMode.getSpacing(), this.mShowTimeLine);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter
    public int calculateMatchItemPosition(PictureViewMode pictureViewMode, int i, int i2, float f, float f2, int i3) {
        return this.mClusterAdapter.getTimelineCluster(pictureViewMode).calculateMatchItemPosition(i, i2, f, f2, i3, pictureViewMode.getSpan(), pictureViewMode.getAggregatedLines(), pictureViewMode.getSpacing(), this.mShowTimeLine);
    }

    @Override // com.miui.gallery.widget.recyclerview.IMatchItemAdapter
    public FuzzyMatchItem getMatchItemByGroupAndChildIndex(PictureViewMode pictureViewMode, int i, int i2, int i3) {
        int calculateMatchItemPosition = calculateMatchItemPosition(pictureViewMode, i, i2, 0.0f, 0.0f, i3);
        return new FuzzyMatchItem(calculateMatchItemPosition, getItemKey(calculateMatchItemPosition), getLocation(calculateMatchItemPosition), getItemSortTime(calculateMatchItemPosition));
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter
    public int calculateScrollPosition(PictureViewMode pictureViewMode, int i) {
        return this.mClusterAdapter.getTimelineCluster(pictureViewMode).calculateScrollPosition(i, pictureViewMode.getSpan(), pictureViewMode.getAggregatedLines(), this.mShowTimeLine);
    }

    @Override // com.miui.gallery.widget.recyclerview.IMatchItemAdapter
    public int calculateViewPosition(PictureViewMode pictureViewMode, int i) {
        return calculateScrollPosition(pictureViewMode, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter
    public List<ITransitItem> calculateTransitItems(RecyclerView recyclerView, int i, long j, int i2, int i3, int i4, Rect rect, PictureViewMode pictureViewMode, boolean z, boolean z2) {
        List<TransitFrame> calculateTransitFrames = this.mClusterAdapter.getTimelineCluster(pictureViewMode).calculateTransitFrames(i, j, i2, pictureViewMode.getSpan(), pictureViewMode.getAggregatedLines(), pictureViewMode.getSpacing(), this.mHeaderHeightFunction, i3, i4, rect, z, z2, this.mShowTimeLine);
        LinkedList linkedList = new LinkedList();
        for (TransitFrame transitFrame : calculateTransitFrames) {
            if (transitFrame.isHeader) {
                if (this.mShowTimeLine) {
                    int i5 = this.mClusterAdapter.getTimelineCluster(pictureViewMode).unpackAdapterPosition(transitFrame.adapterPosition, this.mShowTimeLine)[0];
                    BaseViewHolder mo1338onCreateGroupViewHolder = mo1338onCreateGroupViewHolder((ViewGroup) recyclerView, 2);
                    doBindGroupViewHolder(mo1338onCreateGroupViewHolder, i5, pictureViewMode);
                    linkedList.add(new TimelineTransitionalItem(getItemKey(transitFrame.dataPosition), transitFrame.frame, mo1338onCreateGroupViewHolder, transitFrame.isSticky));
                }
            } else {
                linkedList.add(new ThumbTransitionalItem(getItemKey(transitFrame.dataPosition), transitFrame.frame, getOptimalThumbFilePath(transitFrame.dataPosition, pictureViewMode == PictureViewMode.LARGE_THUMB), getOptimalThumbFilePath(transitFrame.dataPosition, false), null, ImageView.ScaleType.CENTER_CROP, getRequestOptions(transitFrame.dataPosition, pictureViewMode), getPreviewRequestOptions(transitFrame.dataPosition, pictureViewMode), pictureViewMode.getImageSize()));
            }
        }
        return linkedList;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public final void onBindGroupViewHolder(BaseViewHolder baseViewHolder, int i, int i2) {
        doBindGroupViewHolder(baseViewHolder, i, this.mViewMode);
    }

    public void doBindGroupViewHolder(BaseViewHolder baseViewHolder, int i, PictureViewMode pictureViewMode) {
        String formatRelativeDate;
        TimelineCluster timelineCluster = this.mClusterAdapter.getTimelineCluster(pictureViewMode);
        String groupLabel = !PictureViewMode.isYearMode(pictureViewMode) ? timelineCluster.getGroupLabel(i, this.mShowTimeLine) : null;
        int groupStartPosition = timelineCluster.getGroupStartPosition(i, this.mShowTimeLine);
        if (PictureViewMode.isYearMode(pictureViewMode)) {
            formatRelativeDate = GalleryDateUtils.formatRelativeOnlyYear(getItemSortTime(groupStartPosition));
        } else if (PictureViewMode.isMonthMode(pictureViewMode)) {
            formatRelativeDate = GalleryDateUtils.formatRelativeMonth(getItemSortTime(groupStartPosition));
        } else {
            formatRelativeDate = GalleryDateUtils.formatRelativeDate(getItemSortTime(groupStartPosition));
        }
        ((NormalTimeLineGridHeaderItem) baseViewHolder.itemView).bindData(formatRelativeDate, groupLabel, this.mShowTimeLine);
    }

    public String simplifiedLocation(String str) {
        if (str == null) {
            return null;
        }
        return str.split(SEPARATOR)[0];
    }

    public long getItemSortTime(int i) {
        return getCreateTime(i);
    }

    public List<ImageCellData> packageCells(int i, int i2) {
        if (this.mClusterAdapter.getGroupCount() <= 0) {
            return Collections.emptyList();
        }
        int span = this.mViewMode.getSpan();
        int aggregatedLines = this.mViewMode.getAggregatedLines();
        int[] groupPositions = this.mClusterAdapter.getGroupPositions(i);
        int i3 = span * aggregatedLines;
        int i4 = i2 * i3;
        int min = Math.min(i3 + i4, groupPositions.length);
        LinkedList linkedList = new LinkedList();
        while (i4 < min) {
            int i5 = groupPositions[i4];
            linkedList.add(new ImageCellData.Builder().setId(getItemKey(i5)).setPosition(i5).setLocalPath(getBindImagePath(i5)).setDownloadUri(getDownloadUri(i5)).setDate(getItemSortTime(i5)).setFileLength(getFileLength(i5)).build());
            i4++;
        }
        return linkedList;
    }

    public GlideOptions getPreviewRequestOptions() {
        return getPreviewRequestOptions(-1);
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter
    public void setShowTimeLine(boolean z) {
        super.setShowTimeLine(z);
        this.mClusterAdapter.setShowTimeLine(z);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getGroupCount() {
        return this.mClusterAdapter.getGroupCount();
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getChildCount(int i) {
        return this.mClusterAdapter.getChildCount(i, this.mViewMode.getSpan());
    }

    @Override // com.miui.gallery.widget.recyclerview.GroupedItemAdapter
    public int packDataPosition(int i, int i2) {
        return this.mClusterAdapter.getTimelineCluster(this.mViewMode).packDataPosition(i, i2, this.mShowTimeLine);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.ui.pictures.ScrollingCalculator
    public int computeScrollRange(int i) {
        return this.mClusterAdapter.computeScrollRange(this.mViewMode.getSpan(), this.mHeaderHeightFunction, i);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.ui.pictures.ScrollingCalculator
    public int computeScrollOffset(int i, int i2) {
        return this.mClusterAdapter.computeScrollOffset(i, this.mViewMode.getSpan(), this.mHeaderHeightFunction, i2);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.ui.pictures.ScrollingCalculator
    public int[] computeScrollPositionAndOffset(int i, int i2, float f) {
        return this.mClusterAdapter.computeScrollPositionAndOffset(this.mViewMode.getSpan(), this.mHeaderHeightFunction, i, i2, f);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.widget.recyclerview.FastScrollerCapsuleCalculator
    public int getDataPositionByDrag(int i, int i2, float f, int i3) {
        return this.mClusterAdapter.computeDataPosition(this.mViewMode.getSpan(), this.mHeaderHeightFunction, this.mContext.getResources().getDimensionPixelOffset(R.dimen.fast_scroller_margin_top_to_time_line), i, i2, f, i3);
    }

    /* loaded from: classes.dex */
    public class MultiImageViewHolder extends AbsMultiImageViewHolder {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MultiImageViewHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
            MultiViewMediaAdapter.this = r1;
        }

        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<Object> list) {
            List<ImageCellData> packageCells = MultiViewMediaAdapter.this.packageCells(i, i2);
            this.mView.setSpanCount(MultiViewMediaAdapter.this.mViewMode.getSpan());
            this.mView.setSpacing(MultiViewMediaAdapter.this.mHorizontalSpacing);
            int packDataPosition = MultiViewMediaAdapter.this.packDataPosition(i, i2);
            this.mView.setRequestOptions(MultiViewMediaAdapter.this.getRequestOptions(packDataPosition));
            this.mView.setPreviewOptions(MultiViewMediaAdapter.this.getPreviewRequestOptions(packDataPosition));
            this.mView.bindData(packageCells);
        }
    }
}
