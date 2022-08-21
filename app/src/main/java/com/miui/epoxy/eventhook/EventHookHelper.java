package com.miui.epoxy.eventhook;

import android.util.Log;
import android.view.View;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyViewHolder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class EventHookHelper<VH extends EpoxyViewHolder> {
    public final EpoxyAdapter cementAdapter;
    public boolean isAfterBind = false;
    public final List<EventHook<VH>> eventHooks = new ArrayList();

    public EventHookHelper(EpoxyAdapter epoxyAdapter) {
        this.cementAdapter = epoxyAdapter;
    }

    public void add(EventHook<VH> eventHook) {
        if (this.isAfterBind) {
            Log.e("EventHookHelper", "can not add event hook after bind");
        } else {
            this.eventHooks.add(eventHook);
        }
    }

    public void bind(EpoxyViewHolder epoxyViewHolder) {
        for (EventHook<VH> eventHook : this.eventHooks) {
            if (eventHook.clazz.isInstance(epoxyViewHolder)) {
                VH cast = eventHook.clazz.cast(epoxyViewHolder);
                View onBind = eventHook.onBind(cast);
                if (onBind != null) {
                    attachToView(eventHook, cast, onBind);
                }
                List<? extends View> onBindMany = eventHook.onBindMany(cast);
                if (onBindMany != null) {
                    for (View view : onBindMany) {
                        attachToView(eventHook, cast, view);
                    }
                }
            }
        }
    }

    public final void attachToView(EventHook<VH> eventHook, VH vh, View view) {
        if (view == null) {
            return;
        }
        eventHook.onEvent(view, vh, this.cementAdapter);
        this.isAfterBind = true;
    }
}
