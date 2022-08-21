package com.miui.gallery.vlog.clip.sort;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.text.TextUtils;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.base.widget.EditTimelineSortView;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> implements ItemTouchHelperAdapter {
    public Context mContext;
    public int mItemwidth;
    public int mSelectedIndex;
    public EditTimelineSortView.SortCallback mSortCallback;
    public VideoFrameLoader mVideoFrameLoader;
    public String TAG = "RecyclerViewAdapter";
    public List<IVideoClip> mDataList = new ArrayList();
    public boolean mIsDragging = false;

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.mItemwidth = context.getResources().getDimensionPixelSize(R$dimen.vlog_sort_item_width);
    }

    public void updateData(List<IVideoClip> list) {
        if (list != null) {
            this.mDataList = list;
            notifyDataSetChanged();
        }
    }

    public void setSortCallback(EditTimelineSortView.SortCallback sortCallback) {
        this.mSortCallback = sortCallback;
    }

    public void setVideoFrameLoader(VideoFrameLoader videoFrameLoader) {
        this.mVideoFrameLoader = videoFrameLoader;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.vlog_clip_edit_sort_item_layout, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        VideoFrameLoader videoFrameLoader;
        IVideoClip iVideoClip = this.mDataList.get(i);
        String filePath = iVideoClip.getFilePath();
        if (!TextUtils.isEmpty(filePath) && (videoFrameLoader = this.mVideoFrameLoader) != null) {
            videoFrameLoader.loadImage(viewHolder.mThumbnailView, filePath, this.mItemwidth, iVideoClip.getTrimIn());
        }
        viewHolder.setSelected(i == this.mSelectedIndex);
        viewHolder.setTimeText(iVideoClip.getClipDurationWithTransition() / 1000);
    }

    public IVideoClip getSelectedItem() {
        if (!BaseMiscUtil.isValid(this.mDataList)) {
            return null;
        }
        return this.mDataList.get(this.mSelectedIndex);
    }

    public int getSelectedIndex() {
        return this.mSelectedIndex;
    }

    public void setSelectedIndex(int i) {
        notifyItemChanged(this.mSelectedIndex, 1);
        this.mSelectedIndex = i;
        notifyItemChanged(i, 1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size();
    }

    @Override // com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
    public void onMove(int i, int i2) {
        Collections.swap(this.mDataList, i, i2);
        notifyItemMoved(i, i2);
    }

    @Override // com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
        this.mIsDragging = true;
        if (i != this.mSelectedIndex) {
            setSelectedIndex(i);
        }
        doScaleAnimal(viewHolder.itemView, true);
        EditTimelineSortView.SortCallback sortCallback = this.mSortCallback;
        if (sortCallback != null) {
            sortCallback.onSelectedChanged(viewHolder, this.mSelectedIndex);
        }
    }

    public final void doScaleAnimal(View view, boolean z) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        Property property = View.SCALE_X;
        float[] fArr = new float[2];
        fArr[0] = view.getScaleX();
        float f = 1.2f;
        fArr[1] = z ? 1.2f : 1.0f;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(property, fArr);
        Property property2 = View.SCALE_Y;
        float[] fArr2 = new float[2];
        fArr2[0] = view.getScaleY();
        if (!z) {
            f = 1.0f;
        }
        fArr2[1] = f;
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat(property2, fArr2);
        objectAnimator.setTarget(view);
        objectAnimator.setValues(ofFloat, ofFloat2);
        objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
        objectAnimator.setDuration(200L);
        objectAnimator.start();
    }

    @Override // com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
    public void onMoveFinished(RecyclerView.ViewHolder viewHolder, int i) {
        this.mIsDragging = false;
        setSelectedIndex(i);
        doScaleAnimal(viewHolder.itemView, false);
        EditTimelineSortView.SortCallback sortCallback = this.mSortCallback;
        if (sortCallback != null) {
            sortCallback.onMoveFinished(viewHolder, i);
        }
    }

    public boolean isDragging() {
        return this.mIsDragging;
    }

    @Override // com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
    public void onChildDraw(RecyclerView.ViewHolder viewHolder, boolean z) {
        doScaleAnimal(viewHolder.itemView, false);
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mSelector;
        public VlogSortView mThumbnailView;
        public TextView mTime;

        public ViewHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mThumbnailView = (VlogSortView) view.findViewById(R$id.NvsThumbnailView);
            this.mSelector = view.findViewById(R$id.iv_selected);
            this.mTime = (TextView) view.findViewById(R$id.time);
        }

        public void setSelected(boolean z) {
            this.mSelector.setVisibility(z ? 0 : 8);
        }

        public void setTimeText(long j) {
            this.mTime.setText(VlogUtils.getClipDuration(((float) j) + 0.5f));
        }
    }
}
