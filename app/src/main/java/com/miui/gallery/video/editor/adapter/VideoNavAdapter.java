package com.miui.gallery.video.editor.adapter;

import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.video.editor.model.MenuFragmentData;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoNavAdapter extends Adapter<VideoNavHolder> {
    public List<MenuFragmentData> mNavigatorData;

    public VideoNavAdapter(List<MenuFragmentData> list) {
        this.mNavigatorData = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public VideoNavHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoNavHolder(getInflater().inflate(R.layout.common_editor_navigator_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(VideoNavHolder videoNavHolder, int i) {
        super.onBindViewHolder((VideoNavAdapter) videoNavHolder, i);
        videoNavHolder.bind(this.mNavigatorData.get(i));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mNavigatorData.size();
    }
}
