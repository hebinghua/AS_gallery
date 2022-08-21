package com.miui.gallery.magic.matting.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicSamplerSingleton;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class BackgroundAdapter extends Adapter<ViewHolder> {
    public Context mContext;
    public List<IconItem> mList;
    public HashMap params;
    public int selectIndex;

    public BackgroundAdapter(List<IconItem> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_matting_video_recyle_item, viewGroup, false);
        FolmeUtil.setDefaultTouchAnim(inflate, null, true);
        return new ViewHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        super.onBindViewHolder((BackgroundAdapter) viewHolder, i);
        BackgroundIconItem backgroundIconItem = (BackgroundIconItem) this.mList.get(i);
        viewHolder.magicImage.setImageResource(ResourceUtil.getDrawableId(backgroundIconItem.getIcon()));
        viewHolder.magicImage.setFocusable(true);
        viewHolder.magicImage.requestFocus();
        viewHolder.magicImage.setContentDescription(backgroundIconItem.getAccessibilityText());
        if (backgroundIconItem.isCheck()) {
            viewHolder.selectImage.setVisibility(0);
        } else {
            viewHolder.selectImage.setVisibility(8);
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
        if (i <= 1 || !backgroundIconItem.isDownload()) {
            viewHolder.magic_download_bg_video.setVisibility(8);
            viewHolder.magic_download_video.setVisibility(8);
            viewHolder.magic_downloading_video.setVisibility(8);
            cancelAnim(viewHolder.magic_downloading_video);
            return;
        }
        viewHolder.magic_download_bg_video.setVisibility(0);
        if (backgroundIconItem.isDownLoading()) {
            viewHolder.magic_downloading_video.setVisibility(0);
            viewHolder.magic_download_video.setVisibility(8);
            rotateAnim(viewHolder.magic_downloading_video);
            return;
        }
        viewHolder.magic_download_video.setVisibility(0);
        viewHolder.magic_downloading_video.setVisibility(8);
        cancelAnim(viewHolder.magic_downloading_video);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<IconItem> list = this.mList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public IconItem getItem(int i) {
        return this.mList.get(i);
    }

    public void selectIndex(int i) {
        int i2 = this.selectIndex;
        if (i == i2) {
            return;
        }
        this.mList.get(i2).setCheck(false);
        this.selectIndex = i;
        this.mList.get(i).setCheck(true);
        notifyItemChanged(i2);
        notifyItemChanged(this.selectIndex);
        if (this.params == null) {
            this.params = new HashMap();
        }
        MagicSamplerSingleton.getInstance().setSelectMagicIndex(i);
        this.params.clear();
        HashMap hashMap = this.params;
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "背景" + i);
        MagicSampler.getInstance().recordCategory("matting", "background", this.params);
    }

    public void notifyItem(int i, boolean z, boolean z2) {
        ((BackgroundIconItem) this.mList.get(i)).setDownload(z);
        ((BackgroundIconItem) this.mList.get(i)).setDownLoading(z2);
        notifyItemChanged(i);
    }

    public void notifyDownloading(int i, boolean z) {
        ((BackgroundIconItem) this.mList.get(i)).setDownLoading(z);
        notifyItemChanged(i);
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

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mMagicVideoRecyclerItem;
        public ImageView magicImage;
        public ImageView magic_download_bg_video;
        public ImageView magic_download_video;
        public ImageView magic_downloading_video;
        public View selectImage;

        public ViewHolder(View view) {
            super(view);
            this.magicImage = (ImageView) view.findViewById(R$id.magic_image);
            this.selectImage = view.findViewById(R$id.magic_image_select);
            this.mMagicVideoRecyclerItem = view.findViewById(R$id.magic_video_recycler_item);
            this.magic_download_bg_video = (ImageView) view.findViewById(R$id.magic_download_bg_video);
            this.magic_download_video = (ImageView) view.findViewById(R$id.magic_download_video);
            this.magic_downloading_video = (ImageView) view.findViewById(R$id.magic_downloading_video);
        }
    }
}
