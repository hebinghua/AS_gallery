package com.miui.gallery.vlog.clip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ClipEditNavAdapter extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter<ClipEditNavHolder> {
    public ArrayList<ClipEditNavItem> mClipEditNavItems = new ArrayList<>();
    public Context mContext;
    public IVideoClip mCurrentVideoClip;
    public int mItemWidth;
    public int mItemWidthMax;
    public LayoutInflater mLayoutInflater;

    public ClipEditNavAdapter(Context context, ArrayList<ClipEditNavItem> arrayList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mClipEditNavItems.addAll(arrayList);
        this.mItemWidthMax = context.getResources().getDimensionPixelSize(R$dimen.vlog_clip_edit_nav_item_width_max);
        calcItemWidth();
    }

    public final void calcItemWidth() {
        if (getItemCount() != 0) {
            int curScreenWidth = (int) ((ScreenUtils.getCurScreenWidth() - this.mContext.getResources().getDimension(R$dimen.clip_edit_nav_back_width)) / getItemCount());
            this.mItemWidth = curScreenWidth;
            int i = this.mItemWidthMax;
            if (curScreenWidth <= i) {
                return;
            }
            this.mItemWidth = i;
        }
    }

    public void updateItemWidth() {
        calcItemWidth();
        notifyDataSetChanged();
    }

    public void setEnableDelete(boolean z) {
        ClipEditNavItem clipEditNavItem = this.mClipEditNavItems.get(3);
        if (clipEditNavItem == null || z == clipEditNavItem.isEnable()) {
            return;
        }
        clipEditNavItem.setEnable(z);
        notifyItemChanged(3, 1);
    }

    public void updateCutItemState(IVideoClip iVideoClip) {
        if (iVideoClip == null) {
            return;
        }
        this.mCurrentVideoClip = iVideoClip;
        setEnableCut(iVideoClip.getTimelineDuration() >= 1000);
    }

    public final void setEnableCut(boolean z) {
        ClipEditNavItem clipEditNavItem = this.mClipEditNavItems.get(0);
        if (clipEditNavItem == null || z == clipEditNavItem.isEnable()) {
            return;
        }
        clipEditNavItem.setEnable(z);
        notifyItemChanged(0, 1);
    }

    public boolean isEnableDelete() {
        ClipEditNavItem clipEditNavItem = this.mClipEditNavItems.get(3);
        if (clipEditNavItem != null) {
            return clipEditNavItem.isEnable();
        }
        return false;
    }

    public ClipEditNavItem getItem(int i) {
        if (i <= 0 || i >= getItemCount()) {
            return null;
        }
        return this.mClipEditNavItems.get(i);
    }

    public void updateSpeedXItemState(IVideoClip iVideoClip) {
        if (iVideoClip == null) {
            return;
        }
        this.mCurrentVideoClip = iVideoClip;
        boolean z = true;
        notifyItemChanged(1, 1);
        if (iVideoClip.getTimelineDuration() < 1000) {
            z = false;
        }
        setEnableCut(z);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    /* renamed from: onCreateSingleChoiceViewHolder */
    public ClipEditNavHolder mo1797onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i) {
        return new ClipEditNavHolder(this.mLayoutInflater.inflate(R$layout.vlog_clip_menu_nav_item_layout, viewGroup, false));
    }

    public ClipEditNavItem getSelectedItem() {
        return this.mClipEditNavItems.get(getSelectedItemPosition());
    }

    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    public void onBindView(ClipEditNavHolder clipEditNavHolder, int i) {
        clipEditNavHolder.itemView.getLayoutParams().width = this.mItemWidth;
        ClipEditNavItem clipEditNavItem = this.mClipEditNavItems.get(i);
        clipEditNavHolder.setImage(clipEditNavItem.getImgId());
        IVideoClip iVideoClip = this.mCurrentVideoClip;
        double speed = (iVideoClip == null || iVideoClip.isDeleted()) ? SearchStatUtils.POW : this.mCurrentVideoClip.getSpeed();
        if (clipEditNavItem.isSpeedx()) {
            IVideoClip iVideoClip2 = this.mCurrentVideoClip;
            if (iVideoClip2 == null) {
                clipEditNavHolder.setTitle(clipEditNavItem.getTitleResId());
            } else if (iVideoClip2 != null && !iVideoClip2.isDeleted() && ClipMenuFragment.containSpeed(speed)) {
                clipEditNavHolder.setTitle(speed + "X");
            } else {
                clipEditNavHolder.setTitle(clipEditNavItem.getTitleResId());
            }
        } else {
            clipEditNavHolder.setTitle(clipEditNavItem.getTitleResId());
        }
        clipEditNavHolder.setItemState(this.mClipEditNavItems.get(i), clipEditNavItem.isEnable());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mClipEditNavItems.size();
    }
}
