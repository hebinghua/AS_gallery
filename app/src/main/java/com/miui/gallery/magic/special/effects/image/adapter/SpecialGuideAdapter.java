package com.miui.gallery.magic.special.effects.image.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.special.effects.image.bean.SpecialIconItem;
import com.miui.gallery.magic.special.effects.image.bean.SpecialItem;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class SpecialGuideAdapter extends Adapter<ViewHolder> {
    public Context context;
    public List<SpecialItem> mList;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return 0;
    }

    public SpecialGuideAdapter(List<SpecialItem> list) {
        this.mList = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate;
        this.context = viewGroup.getContext();
        if (i == 0) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_special_effects_guide_list_item, viewGroup, false);
        } else {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_special_effects_guide_list_footer, viewGroup, false);
        }
        FolmeUtil.setDefaultTouchAnim(inflate, null, true);
        return new ViewHolder(inflate, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == 0) {
            super.onBindViewHolder((SpecialGuideAdapter) viewHolder, i);
            SpecialItem specialItem = this.mList.get(i);
            viewHolder.magicImage.setBackgroundResource(ResourceUtil.getDrawableId(specialItem.getIcon()));
            viewHolder.magicDescription.setText(specialItem.getDescription());
            viewHolder.magicTitle.setText(specialItem.getTitle());
            if (i != 4 || !SpecialIconItem.getDownLoad()) {
                viewHolder.magic_download_bg.setVisibility(8);
                viewHolder.magic_download.setVisibility(8);
                viewHolder.magic_downloading.setVisibility(8);
                cancelAnim(viewHolder.magic_downloading);
                return;
            }
            viewHolder.magic_download_bg.setVisibility(0);
            if (SpecialIconItem.isDownloading()) {
                viewHolder.magic_downloading.setVisibility(0);
                viewHolder.magic_download.setVisibility(8);
                rotateAnim(viewHolder.magic_downloading);
                return;
            }
            viewHolder.magic_download.setVisibility(0);
            viewHolder.magic_downloading.setVisibility(8);
            cancelAnim(viewHolder.magic_downloading);
        }
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

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<SpecialItem> list = this.mList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout footerView;
        public TextView magicDescription;
        public ImageView magicImage;
        public TextView magicTitle;
        public ImageView magic_download;
        public ImageView magic_download_bg;
        public ImageView magic_downloading;

        public ViewHolder(View view, int i) {
            super(view);
            if (i != 0) {
                if (i != 1) {
                    return;
                }
                this.footerView = (LinearLayout) view;
                return;
            }
            this.magicImage = (ImageView) view.findViewById(R$id.magic_image);
            this.magicTitle = (TextView) view.findViewById(R$id.magic_list_item_title);
            this.magicDescription = (TextView) view.findViewById(R$id.magic_list_item_dsc);
            this.magic_download_bg = (ImageView) view.findViewById(R$id.magic_download_bg);
            this.magic_download = (ImageView) view.findViewById(R$id.magic_download);
            this.magic_downloading = (ImageView) view.findViewById(R$id.magic_downloading);
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
}
