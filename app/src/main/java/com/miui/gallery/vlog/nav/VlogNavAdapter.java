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
public class VlogNavAdapter extends VlogNavBaseAdapter {
    public int mItemWidth;

    public VlogNavAdapter(Context context, List<VlogNavItem> list) {
        super(context, list);
        this.mItemWidth = calcItemWidth();
    }

    @Override // com.miui.gallery.vlog.nav.VlogNavBaseAdapter
    public void updateItemSize() {
        int calcItemWidth = calcItemWidth();
        if (this.mItemWidth != calcItemWidth) {
            this.mItemWidth = calcItemWidth;
            notifyDataSetChanged();
        }
    }

    public final int calcItemWidth() {
        int i;
        int curScreenWidth = ScreenUtils.getCurScreenWidth() - (this.mContext.getResources().getDimensionPixelSize(R$dimen.vlog_menu_nav_item_padding_start) * 2);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.vlog_main_menu_navi_item_width);
        if (this.mList.size() < 5) {
            i = curScreenWidth / this.mList.size();
        } else {
            i = curScreenWidth / 5;
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
        layoutParams.width = this.mItemWidth;
        layoutParams.height = -1;
        return new VlogNavViewHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    public void onBindView(VlogNavViewHolder vlogNavViewHolder, int i) {
        ViewGroup.LayoutParams layoutParams = vlogNavViewHolder.itemView.getLayoutParams();
        layoutParams.width = this.mItemWidth;
        layoutParams.height = -1;
        vlogNavViewHolder.itemView.setLayoutParams(layoutParams);
        vlogNavViewHolder.setTitle(this.mList.get(i).getItemNameId());
    }
}
