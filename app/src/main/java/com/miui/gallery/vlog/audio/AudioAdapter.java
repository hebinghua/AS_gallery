package com.miui.gallery.vlog.audio;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.entity.AudioData;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class AudioAdapter extends Adapter<RecyclerView.ViewHolder> {
    public boolean mEditMode;
    public List<AudioData> mEffects;
    public int mHighlightColor;
    public boolean mIsSingleVideo;
    public int mSelectedIndex = 0;
    public int mSubHighlightColor;
    public int mSubItemSize;

    public AudioAdapter(List<AudioData> list, int i, int i2, int i3, boolean z) {
        this.mEffects = list;
        this.mHighlightColor = i;
        this.mSubHighlightColor = i2;
        this.mSubItemSize = i3;
        this.mIsSingleVideo = z;
    }

    public AudioData getItemData(int i) {
        List<AudioData> list = this.mEffects;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mEffects.get(i);
    }

    public List<AudioData> getDataList() {
        return this.mEffects;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new AudioNoneOrLocalHolder(getInflater().inflate(R$layout.vlog_audio_menu_item_by_type_none_or_local, viewGroup, false));
        }
        return new AudioHolder(getInflater().inflate(R$layout.vlog_audio_menu_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (getItemCount() < 1) {
            return 0;
        }
        String str = this.mEffects.get(i).type;
        return (str.equals("type_none") || str.equals("type_local")) ? 0 : 1;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        boolean z = true;
        boolean z2 = i == this.mSelectedIndex;
        AudioData audioData = this.mEffects.get(i);
        if (viewHolder instanceof AudioNoneOrLocalHolder) {
            AudioNoneOrLocalHolder audioNoneOrLocalHolder = (AudioNoneOrLocalHolder) viewHolder;
            if (this.mIsSingleVideo) {
                int i2 = audioData.imageId;
                if (getSelectedIndex() != i) {
                    z = false;
                }
                audioNoneOrLocalHolder.setIconPath(audioData, i2, i, z);
                return;
            }
            int i3 = audioData.imageId;
            if (getSelectedIndex() != i) {
                z = false;
            }
            audioNoneOrLocalHolder.setIconPath(audioData, i3, i, z);
        } else if (!(viewHolder instanceof AudioHolder)) {
        } else {
            AudioHolder audioHolder = (AudioHolder) viewHolder;
            if (this.mIsSingleVideo) {
                audioHolder.setSingleVideoHolder(audioData, z2);
                return;
            }
            audioHolder.setName(audioData);
            audioHolder.setDownloadViewState(audioData);
            if (!z2) {
                audioHolder.setIcon(audioData);
                audioHolder.showTitleView();
            } else if (this.mEditMode) {
                audioHolder.hideTitleView();
                audioHolder.setIcon();
            }
            if (!audioData.isDownloadSuccess()) {
                return;
            }
            audioData.setDownloadState(17);
        }
    }

    public AudioData getSelectedItem() {
        int i = this.mSelectedIndex;
        if (i < 0 || i >= this.mEffects.size()) {
            return null;
        }
        return this.mEffects.get(this.mSelectedIndex);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<AudioData> list = this.mEffects;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setSelectedIndex(int i) {
        int i2 = this.mSelectedIndex;
        if (i == i2) {
            return;
        }
        this.mSelectedIndex = i;
        if (i2 >= 0) {
            notifyItemChanged(i2);
        }
        int i3 = this.mSelectedIndex;
        if (i3 < 0) {
            return;
        }
        notifyItemChanged(i3);
    }

    public void clearSelectedIndex() {
        int i = this.mSelectedIndex;
        this.mSelectedIndex = 0;
        if (i >= 0) {
            notifyItemChanged(i);
        }
        int i2 = this.mSelectedIndex;
        if (i2 >= 0) {
            notifyItemChanged(i2);
        }
    }

    public void enterEditMode() {
        this.mEditMode = true;
        int i = this.mSelectedIndex;
        if (i >= 0) {
            notifyItemChanged(i);
        }
    }

    public void exitEditMode() {
        this.mEditMode = false;
        int i = this.mSelectedIndex;
        if (i > 0) {
            notifyItemChanged(i);
        }
    }

    public int getSelectedIndex() {
        return this.mSelectedIndex;
    }
}
