package com.miui.gallery.magic.special.effects.image.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.special.effects.image.bean.SpecialIconItem;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class SpecialEffectsAdapter extends Adapter<ViewHolder> {
    public Context mContext;
    public List<SpecialIconItem> mList;
    public OnItemTouchListener mOnItemTouchListener = null;
    public int mCurrentIndex = 0;

    /* loaded from: classes2.dex */
    public interface OnItemTouchListener {
    }

    public SpecialEffectsAdapter(List<SpecialIconItem> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    public void selectItem(int i, boolean z) {
        int i2 = this.mCurrentIndex;
        this.mList.get(i2).setSelected(!z);
        this.mCurrentIndex = i;
        this.mList.get(i).setSelected(z);
        notifyItemChanged(i);
        notifyItemChanged(i2);
    }

    public void notifyItem(int i, boolean z, boolean z2) {
        SpecialIconItem.setDownload(z);
        SpecialIconItem.setDownloading(z2);
        notifyItemChanged(i);
    }

    public void notifyDownloading(int i, boolean z) {
        SpecialIconItem.setDownloading(z);
        notifyItemChanged(i);
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView magicImage;
        public ImageView magicSelectImage;
        public TextView magicText;
        public ImageView magic_download_bg_thumb;
        public ImageView magic_download_thumb;
        public ImageView magic_downloading_thumb;
        public RelativeLayout rlColorItem;

        public ViewHolder(View view) {
            super(view);
            this.rlColorItem = (RelativeLayout) view.findViewById(R$id.magic_image_select);
            this.magicImage = (ImageView) view.findViewById(R$id.magic_image);
            this.magicSelectImage = (ImageView) view.findViewById(R$id.magic_selected);
            this.magicText = (TextView) view.findViewById(R$id.magic_text);
            this.magic_download_bg_thumb = (ImageView) view.findViewById(R$id.magic_download_bg_thumb);
            this.magic_download_thumb = (ImageView) view.findViewById(R$id.magic_download_thumb);
            this.magic_downloading_thumb = (ImageView) view.findViewById(R$id.magic_downloading_thumb);
        }
    }

    public void rotateAnim(View view) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, 0.0f, 360.0f);
        ofFloat.setDuration(500L);
        ofFloat.setRepeatCount(100);
        ofFloat.setInterpolator(new LinearInterpolator());
        ofFloat.start();
        view.setTag(ofFloat);
    }

    public void cancelAnim(View view) {
        Object tag = view.getTag();
        if (tag == null || !(tag instanceof ObjectAnimator)) {
            return;
        }
        ((ObjectAnimator) tag).cancel();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_special_effects_recyle_item, viewGroup, false);
        FolmeUtil.setDefaultTouchAnim(inflate, null, true);
        return new ViewHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mList.size();
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        super.onBindViewHolder((SpecialEffectsAdapter) viewHolder, i);
        SpecialIconItem specialIconItem = this.mList.get(i);
        if (specialIconItem.isSelected()) {
            viewHolder.magicSelectImage.setVisibility(0);
        } else {
            viewHolder.magicSelectImage.setVisibility(8);
        }
        if (i == 4 && SpecialIconItem.getDownLoad()) {
            viewHolder.magic_download_bg_thumb.setVisibility(0);
            if (SpecialIconItem.isDownloading()) {
                viewHolder.magic_downloading_thumb.setVisibility(0);
                viewHolder.magic_download_thumb.setVisibility(8);
                rotateAnim(viewHolder.magic_downloading_thumb);
            } else {
                viewHolder.magic_download_thumb.setVisibility(0);
                viewHolder.magic_downloading_thumb.setVisibility(8);
                cancelAnim(viewHolder.magic_downloading_thumb);
            }
        } else {
            viewHolder.magic_download_bg_thumb.setVisibility(8);
            viewHolder.magic_download_thumb.setVisibility(8);
            viewHolder.magic_downloading_thumb.setVisibility(8);
            cancelAnim(viewHolder.magic_downloading_thumb);
        }
        viewHolder.magicText.setText(specialIconItem.getTitle());
        viewHolder.magicImage.setImageResource(ResourceUtil.getDrawableId(specialIconItem.getIcon()));
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.rlColorItem.getLayoutParams();
        if (BaseMiscUtil.isRTLDirection()) {
            if (i == 0) {
                ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = (int) this.mContext.getResources().getDimension(R$dimen.magic_effects_text_layout_margin_20);
            } else {
                ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = 0;
            }
        } else if (i == this.mList.size() - 1) {
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = (int) this.mContext.getResources().getDimension(R$dimen.magic_effects_text_layout_margin_20);
        } else {
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = 0;
        }
        viewHolder.rlColorItem.setLayoutParams(layoutParams);
    }

    public SpecialIconItem getItem(int i) {
        return this.mList.get(i);
    }
}
