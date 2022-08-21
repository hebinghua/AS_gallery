package com.miui.gallery.vlog.audio;

import com.miui.gallery.vlog.entity.AudioData;
import java.util.List;

/* loaded from: classes2.dex */
public interface AudioMenuContract$IAudioMenuView {
    void loadRecyclerView(List<AudioData> list);

    void updatePlayViewState(boolean z);

    void updateSelectedItem(String str);
}
