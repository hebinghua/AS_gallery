package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Size;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.ListPreloader;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.ui.pictures.ScrollingCalculator;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleCalculator;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleContentProvider;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseMediaAdapter<M, S, VH extends BaseViewHolder> extends BaseRecyclerAdapter<M, VH> implements IMediaAdapter, ScrollingCalculator, FastScrollerCapsuleCalculator, ListPreloader.PreloadSizeProvider<PreloadItem> {
    public Context mContext;
    public MicroThumbGridItem mGridItem;
    public int mScrollerBarMarginTop;
    public int mSpacing;
    public int mSpanCount;
    public int mViewScrollState = 0;
    public float mGridItemHeight = -1.0f;

    public abstract void doBindViewHolder(VH vh, int i);

    public FastScrollerCapsuleContentProvider getCapsuleContent(int i) {
        return null;
    }

    public void onViewScrolled(RecyclerView recyclerView, int i, int i2) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        onBindViewHolder((BaseMediaAdapter<M, S, VH>) ((BaseViewHolder) viewHolder), i);
    }

    public BaseMediaAdapter(Context context) {
        this.mContext = context;
        this.mScrollerBarMarginTop = context.getResources().getDimensionPixelOffset(R.dimen.fast_scroller_margin_top_to_time_line);
    }

    public void setSpanCount(int i) {
        this.mSpanCount = i;
    }

    public void setSpacing(int i) {
        this.mSpacing = i;
    }

    public Size getDisplayImageSize(int i) {
        return Config$ThumbConfig.get().sMicroTargetSize;
    }

    public GlideOptions getRequestOptions(int i) {
        Size displayImageSize = getDisplayImageSize(i);
        return GlideOptions.microThumbOf(getFileLength(i)).mo971override(displayImageSize.getWidth(), displayImageSize.getHeight());
    }

    public GlideOptions getPreviewRequestOptions(int i) {
        return GlideOptions.tinyThumbOf(getFileLength(i));
    }

    public static String getMicroPath(Cursor cursor, int i, int i2) {
        String string = cursor.getString(i);
        return (!TextUtils.isEmpty(string) || i2 < 0) ? string : CloudUtils.getMicroPath(cursor.getString(i2));
    }

    public static String getMicroPath(String str, String str2) {
        return TextUtils.isEmpty(str) ? CloudUtils.getMicroPath(str2) : str;
    }

    public static Uri getDownloadUri(Cursor cursor, int i, int i2) {
        return getDownloadUri(cursor.getInt(i), cursor.getLong(i2));
    }

    public static Uri getDownloadUri(Cursor cursor, int i) {
        return getDownloadUri(cursor.getLong(i));
    }

    public static Uri getDownloadUri(int i, long j) {
        if (i == 0) {
            return getDownloadUri(j);
        }
        return null;
    }

    public static Uri getDownloadUri(long j) {
        return CloudUriAdapter.getDownloadUri(j);
    }

    public void onViewScrollStateChanged(RecyclerView recyclerView, int i) {
        this.mViewScrollState = i;
    }

    public RecyclerView.OnScrollListener generateWrapScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        return new OnScrollListenerWrapper(onScrollListener) { // from class: com.miui.gallery.adapter.BaseMediaAdapter.1
            @Override // com.miui.gallery.adapter.BaseMediaAdapter.OnScrollListenerWrapper, androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                BaseMediaAdapter.this.onViewScrollStateChanged(recyclerView, i);
            }

            @Override // com.miui.gallery.adapter.BaseMediaAdapter.OnScrollListenerWrapper, androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                BaseMediaAdapter.this.onViewScrolled(recyclerView, i, i2);
            }
        };
    }

    public final void onBindViewHolder(VH vh, int i) {
        vh.itemView.setTag(R.id.tag_item_unique_id, Long.valueOf(getItemKey(i)));
        doBindViewHolder(vh, i);
    }

    public CheckableAdapter.CheckedItem getCheckedItem(int i) {
        if (i >= getItemCount() || i < 0) {
            return null;
        }
        return new CheckableAdapter.CheckedItem.Builder().setId(getItemKey(i)).setSha1(getSha1(i)).setMicroThumbnailFile(getMicroThumbFilePath(i)).setThumbnailFile(getThumbFilePath(i)).setOriginFile(getOriginFilePath(i)).setMimeType(getMimeType(i)).build();
    }

    public float getMicroItemHeight() {
        MicroThumbGridItem microThumbGridItem;
        if (this.mGridItemHeight < 0.0f && (microThumbGridItem = this.mGridItem) != null) {
            this.mGridItemHeight = microThumbGridItem.getHeight();
            this.mGridItem = null;
        }
        return this.mGridItemHeight;
    }

    @Override // com.miui.gallery.ui.pictures.ScrollingCalculator
    public int computeScrollRange(int i) {
        if (this.mSpanCount == 0) {
            return 0;
        }
        int itemCount = getItemCount();
        float microItemHeight = getMicroItemHeight();
        int i2 = this.mSpanCount;
        int i3 = itemCount % i2;
        int i4 = itemCount / i2;
        if (i3 != 0) {
            i4++;
        }
        return (int) (0 + (i4 * (microItemHeight + this.mSpacing)));
    }

    @Override // com.miui.gallery.ui.pictures.ScrollingCalculator
    public int computeScrollOffset(int i, int i2) {
        if (this.mSpanCount != 0 && i < getItemCount()) {
            float microItemHeight = getMicroItemHeight();
            int i3 = i + 1;
            int i4 = this.mSpanCount;
            int i5 = i3 % i4;
            int i6 = i3 / i4;
            if (i5 != 0) {
                i6++;
            }
            return (int) (0 + (i6 * (microItemHeight + this.mSpacing)));
        }
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.ScrollingCalculator
    public int[] computeScrollPositionAndOffset(int i, int i2, float f) {
        int computeScrollRange = computeScrollRange(i) - i2;
        if (computeScrollRange <= 0) {
            return new int[]{0, 0};
        }
        float microItemHeight = this.mSpacing + getMicroItemHeight();
        float f2 = (int) (computeScrollRange * f);
        return new int[]{(((int) (f2 / microItemHeight)) * this.mSpanCount) + 0, -((int) (f2 % microItemHeight))};
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleCalculator
    public int getDataPositionByDrag(int i, int i2, float f, int i3) {
        int computeScrollRange = computeScrollRange(i) - i2;
        if (computeScrollRange <= 0) {
            return 0;
        }
        return (((int) ((((int) ((computeScrollRange * f) + (i3 * f))) + this.mScrollerBarMarginTop) / (this.mSpacing + getMicroItemHeight()))) * this.mSpanCount) + 0;
    }

    public List<PreloadItem> getPreloadItems(int i) {
        if (i >= 0 && i < getItemCount()) {
            return Collections.singletonList(new PreloadItem(getOptimalThumbFilePath(i), getFileLength(i), getItemDecodeRectF(i), getItemSecretKey(i)));
        }
        return Collections.emptyList();
    }

    @Override // com.bumptech.glide.ListPreloader.PreloadSizeProvider
    public int[] getPreloadSize(PreloadItem preloadItem, int i, int i2) {
        Size displayImageSize = getDisplayImageSize(i);
        return new int[]{displayImageSize.getWidth(), displayImageSize.getHeight()};
    }

    /* loaded from: classes.dex */
    public static class OnScrollListenerWrapper extends RecyclerView.OnScrollListener {
        public final RecyclerView.OnScrollListener mWrapped;

        public OnScrollListenerWrapper(RecyclerView.OnScrollListener onScrollListener) {
            this.mWrapped = onScrollListener;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            RecyclerView.OnScrollListener onScrollListener = this.mWrapped;
            if (onScrollListener != null) {
                onScrollListener.onScrollStateChanged(recyclerView, i);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            RecyclerView.OnScrollListener onScrollListener = this.mWrapped;
            if (onScrollListener != null) {
                onScrollListener.onScrolled(recyclerView, i, i2);
            }
        }
    }
}
