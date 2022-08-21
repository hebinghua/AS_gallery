package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes3.dex */
public interface TransitionListener {
    List<ITransitItem> calculateFromTransitItems(RecyclerView recyclerView, TransitionAnchor transitionAnchor, int i, int i2, Rect rect);

    List<ITransitItem> calculateToTransitItems(RecyclerView recyclerView, TransitionAnchor transitionAnchor, int i, int i2, Rect rect);

    void onPostTransition(RecyclerView recyclerView);

    void onPreTransition(RecyclerView recyclerView);

    void onTransitionFinish(RecyclerView recyclerView, boolean z);

    TransitionAnchor onTransitionPrepare(RecyclerView recyclerView, int i, float f, float f2);

    void onTransitionUpdate(RecyclerView recyclerView, float f);
}
