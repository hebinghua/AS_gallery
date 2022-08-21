package com.miui.gallery.magic.special.effects.video.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.special.effects.video.adapter.ListItem;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoSpecialAdapter extends Adapter<ViewHolder> {
    public Context mContext;
    public List<ListItem> mList;
    public boolean isHaveLocalMusic = false;
    public OnItemTouchListener mOnItemTouchListener = null;
    public int mCurrentIndex = 0;

    /* loaded from: classes2.dex */
    public interface OnItemTouchListener {
    }

    public VideoSpecialAdapter(List<ListItem> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mMagicVideoRecyclerItem;
        public ImageView magicImage;
        public ImageView magicSelectImage;
        public TextView magicText;
        public ImageView magic_download_bg_video;
        public ImageView magic_download_video;
        public ImageView magic_downloading_video;

        public ViewHolder(View view) {
            super(view);
            this.magicImage = (ImageView) view.findViewById(R$id.magic_image);
            this.magicSelectImage = (ImageView) view.findViewById(R$id.magic_image_select);
            this.magicText = (TextView) view.findViewById(R$id.magic_title);
            this.magic_download_bg_video = (ImageView) view.findViewById(R$id.magic_download_bg_video);
            this.magic_download_video = (ImageView) view.findViewById(R$id.magic_download_video);
            this.magic_downloading_video = (ImageView) view.findViewById(R$id.magic_downloading_video);
            this.mMagicVideoRecyclerItem = view.findViewById(R$id.magic_video_recycler_item);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_matting_video_recyle_item, viewGroup, false);
        FolmeUtil.setDefaultTouchAnim(inflate, null, true);
        return new ViewHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mList.size();
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        super.onBindViewHolder((VideoSpecialAdapter) viewHolder, i);
        ListItem listItem = this.mList.get(i);
        int drawableId = ResourceUtil.getDrawableId(listItem.getIcon());
        if (drawableId == 0) {
            drawableId = R$drawable.magic_recycler_video_0;
        }
        Resources resources = this.mContext.getResources();
        int i2 = R$dimen.magic_matting_width_210;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) resources.getDimension(i2), (int) this.mContext.getResources().getDimension(i2));
        if (BaseMiscUtil.isRTLDirection()) {
            if (i == 0) {
                Resources resources2 = this.mContext.getResources();
                int i3 = R$dimen.magic_margin_20;
                layoutParams.setMargins((int) resources2.getDimension(i3), 0, (int) this.mContext.getResources().getDimension(i3), 0);
            } else {
                layoutParams.setMargins((int) this.mContext.getResources().getDimension(R$dimen.magic_margin_20), 0, 0, 0);
            }
        } else if (i == getItemCount() - 1) {
            Resources resources3 = this.mContext.getResources();
            int i4 = R$dimen.magic_margin_20;
            layoutParams.setMargins((int) resources3.getDimension(i4), 0, (int) this.mContext.getResources().getDimension(i4), 0);
        } else {
            layoutParams.setMargins((int) this.mContext.getResources().getDimension(R$dimen.magic_margin_20), 0, 0, 0);
        }
        viewHolder.mMagicVideoRecyclerItem.setLayoutParams(layoutParams);
        ListItem.ItemType type = listItem.getType();
        ListItem.ItemType itemType = ListItem.ItemType.EDIT;
        if (type == itemType) {
            viewHolder.magicSelectImage.setVisibility(0);
        } else {
            viewHolder.magicSelectImage.setVisibility(8);
        }
        viewHolder.magicImage.setImageResource(drawableId);
        viewHolder.magicText.setText(listItem.getTitle());
        if (i == 0) {
            viewHolder.magicImage.setContentDescription(viewHolder.itemView.getResources().getString(R$string.acc_delete_video_special));
        }
        if (i == 1) {
            if (this.mList.get(i).getType() == itemType) {
                viewHolder.magicSelectImage.setVisibility(8);
                viewHolder.magicImage.setImageResource(R$drawable.magic_recycler_audio_2);
            } else {
                viewHolder.magicImage.setImageResource(drawableId);
            }
        }
        if (i > 1 && listItem.isDownLoaded()) {
            viewHolder.magic_download_bg_video.setVisibility(0);
            if (listItem.isDownLoading()) {
                viewHolder.magic_downloading_video.setVisibility(0);
                viewHolder.magic_download_video.setVisibility(8);
                rotateAnim(viewHolder.magic_downloading_video);
                return;
            }
            viewHolder.magic_download_video.setVisibility(0);
            viewHolder.magic_downloading_video.setVisibility(8);
            cancelAnim(viewHolder.magic_downloading_video);
            return;
        }
        viewHolder.magic_download_bg_video.setVisibility(8);
        viewHolder.magic_download_video.setVisibility(8);
        viewHolder.magic_downloading_video.setVisibility(8);
        cancelAnim(viewHolder.magic_downloading_video);
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

    public ListItem getItem(int i) {
        return this.mList.get(i);
    }

    public void selectItem(int i) {
        ListItem.ItemType itemType = ListItem.ItemType.NONE;
        this.mList.get(1).setType(itemType);
        this.mList.get(this.mCurrentIndex).setType(itemType);
        this.mList.get(i).setType(ListItem.ItemType.EDIT);
        this.mCurrentIndex = i;
        notifyDataSetChanged();
        this.isHaveLocalMusic = false;
    }

    public void notifyItem(int i, boolean z, boolean z2) {
        getItem(i).setDownLoaded(z);
        getItem(i).setDownLoading(z2);
        notifyItemChanged(i);
    }

    public void notifyDownloading(int i, boolean z) {
        getItem(i).setDownLoading(z);
        notifyItemChanged(i);
    }
}
