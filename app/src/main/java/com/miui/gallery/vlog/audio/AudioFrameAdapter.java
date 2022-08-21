package com.miui.gallery.vlog.audio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$drawable;

/* loaded from: classes2.dex */
public class AudioFrameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context mContext;
    public int mEmptyViewWidth;
    public int mItemCount;
    public int mItemWidth;
    public int mLastItemWidth;
    public LinearLayoutManager mLayoutManager;
    public int mPaddingStart;
    public float mRatio;
    public int mSelectedWidth;
    public int mTotalWidth;

    public AudioFrameAdapter(Context context, LinearLayoutManager linearLayoutManager) {
        this.mContext = context;
        this.mLayoutManager = linearLayoutManager;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.audio_frame_item_width);
        this.mItemWidth = dimensionPixelSize;
        this.mLastItemWidth = dimensionPixelSize;
        this.mEmptyViewWidth = this.mContext.getResources().getDimensionPixelSize(R$dimen.audio_frame_margin_right);
        this.mSelectedWidth = ScreenUtils.getCurScreenWidth() - (this.mEmptyViewWidth * 2);
        this.mPaddingStart = context.getResources().getDimensionPixelOffset(R$dimen.audio_frame_padding_start);
    }

    public void setRatio(float f) {
        float max = Math.max(f, 1.0f);
        this.mRatio = max;
        int i = (int) (this.mSelectedWidth * max);
        this.mTotalWidth = i;
        int i2 = this.mItemWidth;
        int i3 = i / i2;
        int i4 = i % i2;
        this.mLastItemWidth = i4;
        if (i4 == 0) {
            this.mLastItemWidth = i2;
        } else {
            i3++;
        }
        this.mItemCount = i3;
        notifyDataSetChanged();
    }

    public float getRatio() {
        return this.mRatio;
    }

    public Pair<Float, Float> getTrimInOut() {
        float f = this.mRatio;
        Float valueOf = Float.valueOf(1.0f);
        int i = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
        Float valueOf2 = Float.valueOf(0.0f);
        if (i <= 0) {
            return new Pair<>(valueOf2, valueOf);
        }
        int findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition();
        if (findFirstVisibleItemPosition == -1 || findFirstVisibleItemPosition == getItemCount() - 1) {
            return new Pair<>(valueOf2, valueOf);
        }
        View findViewByPosition = this.mLayoutManager.findViewByPosition(findFirstVisibleItemPosition);
        if (findViewByPosition == null) {
            return new Pair<>(valueOf2, valueOf);
        }
        int i2 = -(findViewByPosition.getLeft() - this.mPaddingStart);
        if (findFirstVisibleItemPosition > 0) {
            i2 = i2 + this.mEmptyViewWidth + ((findFirstVisibleItemPosition - 1) * this.mItemWidth);
        }
        int i3 = this.mSelectedWidth + i2;
        DefaultLogger.d("AudioFrameAdapter", "trimIn: %s , trimOut: %s", Integer.valueOf(i2), Integer.valueOf(i3));
        return new Pair<>(Float.valueOf((i2 * 1.0f) / this.mTotalWidth), Float.valueOf((i3 * 1.0f) / this.mTotalWidth));
    }

    public int getTotalWidth() {
        return this.mTotalWidth;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            View view = new View(viewGroup.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(this.mEmptyViewWidth, -1));
            return new EmptyHolder(view);
        }
        return new AudioFrameHolder(viewGroup);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType != 0) {
            if (itemViewType != 1) {
                return;
            }
            viewHolder.itemView.getLayoutParams().width = this.mEmptyViewWidth;
            return;
        }
        AudioFrameHolder audioFrameHolder = (AudioFrameHolder) viewHolder;
        audioFrameHolder.setWidth(i == getItemCount() + (-2) ? this.mLastItemWidth : this.mItemWidth);
        if (this.mRatio > 1.0f || i != getItemCount() - 2) {
            return;
        }
        int dimensionPixelSize = this.mLastItemWidth - (this.mContext.getResources().getDimensionPixelSize(R$dimen.audio_frame_padding_start) * 2);
        if (dimensionPixelSize <= 0) {
            dimensionPixelSize = 0;
        }
        audioFrameHolder.setWidth(dimensionPixelSize);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mItemCount + 2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return (i == 0 || i == this.mItemCount + 1) ? 1 : 0;
    }

    /* loaded from: classes2.dex */
    public class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View view) {
            super(view);
        }
    }

    /* loaded from: classes2.dex */
    public class AudioFrameHolder extends RecyclerView.ViewHolder {
        public ItemView mItemView;

        public AudioFrameHolder(View view) {
            super(new ItemView(view.getContext()));
            ItemView itemView = (ItemView) this.itemView;
            this.mItemView = itemView;
            itemView.setDrawable(view.getContext().getResources().getDrawable(R$drawable.vlog_audio_frame));
        }

        public void setWidth(int i) {
            this.mItemView.setWidth(i);
        }
    }

    /* loaded from: classes2.dex */
    public static class ItemView extends View {
        public Drawable mDrawable;
        public int mWidth;

        public ItemView(Context context) {
            super(context);
        }

        public void setWidth(int i) {
            this.mWidth = i;
        }

        public void setDrawable(Drawable drawable) {
            this.mDrawable = drawable;
            drawable.setBounds(0, 0, getResources().getDimensionPixelSize(R$dimen.audio_frame_item_width), this.mDrawable.getIntrinsicHeight());
        }

        @Override // android.view.View
        public void onMeasure(int i, int i2) {
            setMeasuredDimension(this.mWidth, View.getDefaultSize(getSuggestedMinimumHeight(), i2));
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Drawable drawable = this.mDrawable;
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}
