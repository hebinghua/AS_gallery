package com.miui.gallery.vlog.clip.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$drawable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoTransitionEnterView extends ViewGroup {
    public List<ItemView> mActiveItems;
    public List<VideoClipInfo> mDataList;
    public int mEnterWidth;
    public OnTransitionItemClipListener mListener;
    public int mMinDuration;
    public SparseIntArray mPositions;
    public int mScrollX;

    /* loaded from: classes2.dex */
    public interface OnTransitionItemClipListener {
        void onTransitionItemClick(VideoClipInfo videoClipInfo);
    }

    public VideoTransitionEnterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mActiveItems = new ArrayList();
        init();
    }

    public final void init() {
        this.mEnterWidth = getResources().getDimensionPixelSize(R$dimen.vlog_clip_enter_width);
    }

    public void setListener(OnTransitionItemClipListener onTransitionItemClipListener) {
        this.mListener = onTransitionItemClipListener;
    }

    public void setMinDuration(int i) {
        this.mMinDuration = i;
    }

    public void updateVideoClipList(List<VideoClipInfo> list, SparseIntArray sparseIntArray) {
        this.mDataList = list;
        this.mPositions = sparseIntArray;
    }

    public void scrollTo(int i) {
        this.mScrollX = i;
        requestLayout();
    }

    public boolean performClick(MotionEvent motionEvent) {
        if (this.mActiveItems != null) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            for (ItemView itemView : this.mActiveItems) {
                if (itemView.getVisibility() == 0 && x >= itemView.getLeft() && x <= itemView.getRight() && y >= itemView.getTop() && y <= itemView.getBottom()) {
                    itemView.onClick(itemView);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        layoutItems();
    }

    public void layoutItems() {
        ItemView itemView;
        int size = this.mActiveItems.size();
        for (int i = 0; i < size; i++) {
            this.mActiveItems.get(i).isActive = false;
        }
        if (this.mDataList != null) {
            int size2 = this.mActiveItems.size();
            int height = getHeight();
            int i2 = this.mEnterWidth;
            int i3 = (height - i2) / 2;
            int i4 = i2 / 2;
            int size3 = this.mDataList.size() - 1;
            int i5 = 0;
            for (int i6 = 0; i6 < size3; i6++) {
                int i7 = this.mPositions.get(i6) - this.mScrollX;
                VideoClipInfo videoClipInfo = this.mDataList.get(i6);
                if (i7 >= (-i4) && i7 <= getWidth() + i4) {
                    if (i5 < size2) {
                        itemView = this.mActiveItems.get(i5);
                    } else {
                        itemView = new ItemView(getContext());
                        addView(itemView);
                        this.mActiveItems.add(itemView);
                    }
                    itemView.isActive = true;
                    itemView.data = videoClipInfo;
                    onBindView(itemView, videoClipInfo);
                    itemView.layout(i7 - i4, i3, i7 + i4, this.mEnterWidth + i3);
                    i5++;
                }
            }
        }
        Iterator<ItemView> it = this.mActiveItems.iterator();
        while (it.hasNext()) {
            ItemView next = it.next();
            if (next != null && !next.isActive) {
                it.remove();
                removeView(next);
            }
        }
    }

    public final void onBindView(ItemView itemView, VideoClipInfo videoClipInfo) {
        int i = 0;
        if (!videoClipInfo.hasTransition() && !videoClipInfo.isTransitionEnable(this.mMinDuration)) {
            i = 8;
        }
        itemView.setVisibility(i);
        itemView.setBackgroundResource(videoClipInfo.hasTransition() ? R$drawable.vlog_transition_change : R$drawable.vlog_transition_none);
    }

    /* loaded from: classes2.dex */
    public class ItemView extends View implements View.OnClickListener {
        public VideoClipInfo data;
        public boolean isActive;

        public ItemView(Context context) {
            super(context);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (VideoTransitionEnterView.this.mListener != null) {
                VideoTransitionEnterView.this.mListener.onTransitionItemClick(this.data);
            }
        }
    }
}
