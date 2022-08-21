package com.miui.gallery.vlog.nav;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$layout;
import java.util.List;

/* loaded from: classes2.dex */
public class VlogNavLandscapeAdapter extends VlogNavBaseAdapter {
    public int mItemHeight;

    public VlogNavLandscapeAdapter(Context context, List<VlogNavItem> list) {
        super(context, list);
        this.mItemHeight = calcItemHeight();
    }

    @Override // com.miui.gallery.vlog.nav.VlogNavBaseAdapter
    public void updateItemSize() {
        int calcItemHeight = calcItemHeight();
        if (this.mItemHeight != calcItemHeight) {
            this.mItemHeight = calcItemHeight;
            notifyDataSetChanged();
        }
    }

    public final int calcItemHeight() {
        int i;
        int screenWidth = ScreenUtils.getScreenWidth() - (this.mContext.getResources().getDimensionPixelSize(R$dimen.vlog_menu_nav_item_padding_start) * 2);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.vlog_main_menu_navi_item_width);
        if (this.mList.size() < 5) {
            i = screenWidth / this.mList.size();
        } else {
            i = screenWidth / 5;
        }
        if (i <= dimensionPixelSize) {
            dimensionPixelSize = i;
        }
        DefaultLogger.d("VlogNavAdapter", "calcItemWidth=%d", Integer.valueOf(dimensionPixelSize));
        return dimensionPixelSize;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    /* renamed from: onCreateSingleChoiceViewHolder */
    public VlogNavViewHolder mo1797onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i) {
        View inflate = this.mLayoutInflater.inflate(R$layout.vlog_nav_item_layout, viewGroup, false);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = this.mItemHeight;
        return new VlogNavViewHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    public void onBindView(VlogNavViewHolder vlogNavViewHolder, int i) {
        ViewGroup.LayoutParams layoutParams = vlogNavViewHolder.itemView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = this.mItemHeight;
        vlogNavViewHolder.itemView.setLayoutParams(layoutParams);
        vlogNavViewHolder.setTitle(this.mList.get(i).getItemNameId());
    }
}
