package com.miui.gallery.magic.idphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.magic.R$array;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.idphoto.bean.CategoryColorItem;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class CategoryColorAdapter extends Adapter<ViewHolder> {
    public String[] colorDescriptions;
    public int lastItemPosition;
    public List<CategoryColorItem> list;
    public Context mContext;
    public int[] colorArray = {R$drawable.magic_idp_make_color_white_btn_selector, R$drawable.magic_idp_make_color_blue_btn_selector, R$drawable.magic_idp_make_color_cyan_btn_selector, R$drawable.magic_idp_make_color_red_btn_selector, R$drawable.magic_idp_make_color_morandi_green_btn_selector, R$drawable.magic_idp_make_color_morandi_pink_btn_selector, R$drawable.magic_idp_make_color_morandi_orange_btn_selector, R$drawable.magic_idp_make_color_morandi_grey_btn_selector};
    public int selectIndex = 0;

    public CategoryColorAdapter(List<CategoryColorItem> list, Context context) {
        this.list = list;
        this.mContext = context;
        this.colorDescriptions = context.getResources().getStringArray(R$array.acc_category_color_description);
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivColorItem;

        public ViewHolder(View view) {
            super(view);
            this.ivColorItem = (ImageView) view.findViewById(R$id.iv_color_item);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_id_photo_make_bottom_recycler_color, viewGroup, false);
        FolmeUtil.setDefaultTouchAnim(inflate, null, true);
        return new ViewHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String str;
        super.onBindViewHolder((CategoryColorAdapter) viewHolder, i);
        CategoryColorItem categoryColorItem = this.list.get(i);
        viewHolder.ivColorItem.setBackgroundResource(this.colorArray[i]);
        viewHolder.ivColorItem.setSelected(categoryColorItem.check);
        ImageView imageView = viewHolder.ivColorItem;
        if (categoryColorItem.check) {
            str = this.mContext.getResources().getString(R$string.acc_selected) + this.colorDescriptions[i];
        } else {
            str = this.mContext.getResources().getString(R$string.acc_unselected) + this.colorDescriptions[i];
        }
        imageView.setContentDescription(str);
        boolean isRTLDirection = BaseMiscUtil.isRTLDirection();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.ivColorItem.getLayoutParams();
        if (isRTLDirection) {
            if (i == 0) {
                layoutParams.rightMargin = (int) this.mContext.getResources().getDimension(R$dimen.magic_effects_text_layout_margin_20);
            } else {
                layoutParams.rightMargin = 0;
            }
        } else if (i == this.list.size() - 1) {
            layoutParams.rightMargin = (int) this.mContext.getResources().getDimension(R$dimen.magic_effects_text_layout_margin_20);
        } else {
            layoutParams.rightMargin = 0;
        }
        viewHolder.ivColorItem.setLayoutParams(layoutParams);
    }

    public Boolean selectIndex(int i) {
        int i2 = this.selectIndex;
        if (i != i2 || i == 0) {
            this.list.get(i2).check = false;
            this.selectIndex = i;
            this.list.get(i).check = true;
            notifyItemChanged(this.lastItemPosition);
            notifyItemChanged(i);
            this.lastItemPosition = i;
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
