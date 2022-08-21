package com.miui.gallery.widget.recyclerview.transition;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes3.dex */
public interface ITransitionOverlay {
    void attach(RecyclerView recyclerView);

    void detach(RecyclerView recyclerView);

    void prepare(RecyclerView recyclerView, List<ITransitionRender> list);

    void release();

    void updateProgress(RecyclerView recyclerView, boolean z, float f);
}
