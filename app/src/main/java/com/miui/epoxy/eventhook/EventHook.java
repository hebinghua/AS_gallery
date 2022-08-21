package com.miui.epoxy.eventhook;

import android.view.View;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyViewHolder;
import java.util.List;

/* loaded from: classes.dex */
public abstract class EventHook<VH extends EpoxyViewHolder> {
    public final Class<VH> clazz;

    public View onBind(VH vh) {
        return null;
    }

    public List<? extends View> onBindMany(VH vh) {
        return null;
    }

    public abstract void onEvent(View view, VH vh, EpoxyAdapter epoxyAdapter);

    public EventHook(Class<VH> cls) {
        this.clazz = cls;
    }
}
