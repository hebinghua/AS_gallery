package com.miui.gallery.magic.idphoto.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.baseui.R$color;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.idphoto.bean.CategoryItem;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class CategoryAdapter extends Adapter<ViewHolder> {
    public List<CategoryItem> list;
    public Context mContext;
    public int selectIndex = 0;

    public CategoryAdapter(List<CategoryItem> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    public boolean checkSelectIndex(int i) {
        return i == this.selectIndex && i != 0;
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView editIcon;
        public View recyclerRrootView;
        public TextView textView;
        public TextView textViewImage;
        public TextView titleView;
        public View view1;
        public View view2;
        public TextView ziDiYiView;

        public ViewHolder(View view) {
            super(view);
            this.recyclerRrootView = view.findViewById(R$id.magic_idp_make_recycler_root);
            this.titleView = (TextView) view.findViewById(R$id.magic_idp_make_recycler_title);
            this.ziDiYiView = (TextView) view.findViewById(R$id.magic_idp_make_recycler_zidingyi);
            this.textView = (TextView) view.findViewById(R$id.magic_idp_make_recycler_text);
            this.textViewImage = (TextView) view.findViewById(R$id.magic_idp_make_recycler_text_2);
            this.view1 = view.findViewById(R$id.magic_idp_make_recycler_cus1);
            this.view2 = view.findViewById(R$id.magic_idp_make_recycler_cus2);
            this.editIcon = (ImageView) view.findViewById(R$id.magic_i);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_id_photo_make_bottom_recycler_item, viewGroup, false);
        FolmeUtil.setDefaultTouchAnim(inflate, null, true);
        return new ViewHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    public CategoryItem getItem(int i) {
        return this.list.get(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        super.onBindViewHolder((CategoryAdapter) viewHolder, i);
        CategoryItem categoryItem = this.list.get(i);
        boolean isRTLDirection = BaseMiscUtil.isRTLDirection();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        if (isRTLDirection) {
            if (i == 0) {
                Resources resources = this.mContext.getResources();
                int i2 = R$dimen.magic_effects_text_layout_margin_25;
                layoutParams.setMargins((int) resources.getDimension(i2), 0, (int) this.mContext.getResources().getDimension(i2), 0);
            } else {
                layoutParams.setMargins((int) this.mContext.getResources().getDimension(R$dimen.magic_effects_text_layout_margin_25), 0, 0, 0);
            }
        } else if (i == getItemCount() - 1) {
            Resources resources2 = this.mContext.getResources();
            int i3 = R$dimen.magic_effects_text_layout_margin_25;
            layoutParams.setMargins((int) resources2.getDimension(i3), 0, (int) this.mContext.getResources().getDimension(i3), 0);
        } else {
            layoutParams.setMargins((int) this.mContext.getResources().getDimension(R$dimen.magic_effects_text_layout_margin_25), 0, 0, 0);
        }
        viewHolder.recyclerRrootView.setLayoutParams(layoutParams);
        if (i == 0) {
            viewHolder.view1.setVisibility(0);
            viewHolder.view2.setVisibility(8);
            viewHolder.ziDiYiView.setVisibility(8);
            viewHolder.textViewImage.setText(categoryItem.text);
            if (!categoryItem.check.booleanValue()) {
                viewHolder.editIcon.setImageDrawable(this.mContext.getDrawable(R$drawable.magic_idp_make_edit_icon));
                viewHolder.textViewImage.setTextColor(this.mContext.getColor(R$color.white_80_transparent));
                if (!TextUtils.isEmpty(this.list.get(0).text)) {
                    viewHolder.editIcon.setVisibility(8);
                    viewHolder.ziDiYiView.setTextColor(-1);
                    viewHolder.ziDiYiView.setVisibility(0);
                    viewHolder.textViewImage.setVisibility(0);
                    viewHolder.textViewImage.setText(this.list.get(0).text);
                } else {
                    viewHolder.editIcon.setVisibility(0);
                    viewHolder.textViewImage.setVisibility(8);
                    viewHolder.ziDiYiView.setVisibility(8);
                }
            } else {
                TextView textView = viewHolder.textViewImage;
                Context context = this.mContext;
                int i4 = com.miui.gallery.magic.R$color.black_80;
                textView.setTextColor(context.getColor(i4));
                viewHolder.textViewImage.setText(this.list.get(0).text);
                viewHolder.textViewImage.setVisibility(0);
                if (!TextUtils.isEmpty(this.list.get(0).text)) {
                    viewHolder.editIcon.setVisibility(8);
                    viewHolder.textViewImage.setVisibility(0);
                    viewHolder.textViewImage.setText(this.list.get(0).text);
                    viewHolder.ziDiYiView.setTextColor(this.mContext.getColor(i4));
                    viewHolder.ziDiYiView.setVisibility(0);
                    viewHolder.recyclerRrootView.setBackgroundResource(R$drawable.magic_idp_make_item_btn_unchecked);
                } else {
                    viewHolder.editIcon.setVisibility(0);
                    viewHolder.textViewImage.setVisibility(8);
                    viewHolder.ziDiYiView.setVisibility(8);
                }
            }
        } else {
            viewHolder.view1.setVisibility(8);
            viewHolder.view2.setVisibility(0);
            viewHolder.titleView.setText(categoryItem.title);
            viewHolder.textView.setText(categoryItem.text);
        }
        if (!categoryItem.check.booleanValue()) {
            viewHolder.recyclerRrootView.setBackgroundResource(R$drawable.magic_idp_make_item_btn_checked);
            viewHolder.titleView.setTextColor(-1);
            viewHolder.textView.setTextColor(this.mContext.getColor(R$color.white_80_transparent));
            return;
        }
        if (i != 0) {
            viewHolder.recyclerRrootView.setBackgroundResource(R$drawable.magic_idp_make_item_btn_unchecked);
        }
        viewHolder.titleView.setTextColor(-16777216);
        viewHolder.textView.setTextColor(this.mContext.getColor(com.miui.gallery.magic.R$color.black_80));
    }

    public String getItemType(int i) {
        return this.list.get(i).type;
    }

    public Boolean selectIndex(int i) {
        int i2 = this.selectIndex;
        if (i != i2 || i == 0) {
            Boolean bool = Boolean.FALSE;
            this.list.get(i2).check = bool;
            this.selectIndex = i;
            this.list.get(i).check = Boolean.TRUE;
            notifyDataSetChanged();
            return bool;
        }
        return Boolean.TRUE;
    }

    public void setNewItem(int i, int i2, String str) {
        this.list.get(0).text = i + "x" + i2 + " " + str;
        notifyDataSetChanged();
    }
}
