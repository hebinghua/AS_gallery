package com.miui.gallery.video.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.video.editor.LocalAudio;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import java.util.List;

/* loaded from: classes2.dex */
public class AudioRecyclerViewAdapter extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter<AudioViewHolder> {
    public final int VIEW_TYPE_NORMAL = 0;
    public List<LocalAudio> mAudioList;
    public int mFirstMarginLeft;
    public LayoutInflater mLayoutInflater;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List list) {
        onBindViewHolder((AudioViewHolder) viewHolder, i, (List<Object>) list);
    }

    public AudioRecyclerViewAdapter(Context context, List<LocalAudio> list) {
        this.mFirstMarginLeft = 0;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mAudioList = list;
        this.mFirstMarginLeft = context.getResources().getDimensionPixelSize(R.dimen.video_editor_list_item_margin_left);
    }

    public LocalAudio getAudio(int i) {
        List<LocalAudio> list = this.mAudioList;
        if (list == null || i >= list.size()) {
            return null;
        }
        return this.mAudioList.get(i);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    /* renamed from: onCreateSingleChoiceViewHolder */
    public AudioViewHolder mo1754onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i) {
        return new AudioViewHolder(this.mLayoutInflater.inflate(R.layout.video_editor_smart_effect_download_item, viewGroup, false));
    }

    public void onBindViewHolder(AudioViewHolder audioViewHolder, int i, List<Object> list) {
        if (list.isEmpty()) {
            onBindViewHolder((AudioRecyclerViewAdapter) audioViewHolder, i);
            return;
        }
        LocalAudio localAudio = this.mAudioList.get(i);
        audioViewHolder.setStateImage(localAudio.getDownloadState());
        audioViewHolder.updateSelected(getSelectedItemPosition() == i, localAudio.isDownloaded());
        if (!localAudio.isDownloadSuccess()) {
            return;
        }
        localAudio.setDownloadState(17);
    }

    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    public void onBindView(AudioViewHolder audioViewHolder, int i) {
        boolean z = false;
        audioViewHolder.setMarginLeft(i == 0 ? this.mFirstMarginLeft : 0);
        LocalAudio localAudio = this.mAudioList.get(i);
        audioViewHolder.setName(localAudio.getNameResId());
        audioViewHolder.setIcon(localAudio.getIconUrl(), localAudio.getIconResId(), localAudio.getBgColor());
        if (getSelectedItemPosition() == i) {
            z = true;
        }
        audioViewHolder.updateSelected(z, localAudio.isDownloaded());
        audioViewHolder.setStateImage(localAudio.getDownloadState());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<LocalAudio> list = this.mAudioList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
