package com.miui.gallery.video.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.video.editor.SmartEffect;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import java.util.List;

/* loaded from: classes2.dex */
public class SmartEffectRecyclerViewAdapter extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter<SmartEffectViewHolder> {
    public int mFirstMarginLeft;
    public LayoutInflater mLayoutInflater;
    public List<SmartEffect> smartEffectList;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List list) {
        onBindViewHolder((SmartEffectViewHolder) viewHolder, i, (List<Object>) list);
    }

    public SmartEffectRecyclerViewAdapter(Context context, List<SmartEffect> list) {
        this.mFirstMarginLeft = 0;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.smartEffectList = list;
        this.mFirstMarginLeft = context.getResources().getDimensionPixelSize(R.dimen.video_editor_text_item_margin_left);
    }

    public SmartEffect getSmartEffect(int i) {
        List<SmartEffect> list = this.smartEffectList;
        if (list == null || i >= list.size()) {
            return null;
        }
        return this.smartEffectList.get(i);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    /* renamed from: onCreateSingleChoiceViewHolder */
    public SmartEffectViewHolder mo1754onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i) {
        return new SmartEffectViewHolder(this.mLayoutInflater.inflate(R.layout.video_editor_smart_effect_download_item, viewGroup, false));
    }

    public void onBindViewHolder(SmartEffectViewHolder smartEffectViewHolder, int i, List<Object> list) {
        if (list.isEmpty()) {
            onBindViewHolder((SmartEffectRecyclerViewAdapter) smartEffectViewHolder, i);
            return;
        }
        SmartEffect smartEffect = this.smartEffectList.get(i);
        smartEffectViewHolder.setStateImage(smartEffect.getDownloadState());
        smartEffectViewHolder.updateSelected(getSelectedItemPosition() == i, smartEffect.isDownloaded());
        if (!smartEffect.isDownloadSuccess()) {
            return;
        }
        smartEffect.setDownloadState(17);
    }

    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    public void onBindView(SmartEffectViewHolder smartEffectViewHolder, int i) {
        boolean z = false;
        smartEffectViewHolder.setMarginLeft(i == 0 ? this.mFirstMarginLeft : 0);
        SmartEffect smartEffect = this.smartEffectList.get(i);
        if (smartEffect.getNameResId() != 0) {
            smartEffectViewHolder.setName(smartEffect.getNameResId());
        } else if (BaseBuildUtil.isInternational()) {
            smartEffectViewHolder.setName(smartEffect.getEnName());
        } else {
            smartEffectViewHolder.setName(smartEffect.getLabel());
        }
        if (getSelectedItemPosition() == i) {
            z = true;
        }
        smartEffectViewHolder.updateSelected(z, smartEffect.isDownloaded());
        smartEffectViewHolder.setIcon(smartEffect.getIconUrl(), smartEffect.getIconResId(), smartEffect.getBgColor());
        smartEffectViewHolder.setStateImage(smartEffect.getDownloadState());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<SmartEffect> list = this.smartEffectList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
