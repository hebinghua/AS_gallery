package com.miui.epoxy.eventhook;

import android.view.View;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;

/* loaded from: classes.dex */
public abstract class OnClickEventHook<VH extends EpoxyViewHolder> extends EventHook<VH> {
    public abstract void onClick(View view, VH vh, int i, EpoxyModel epoxyModel);

    public OnClickEventHook(Class<VH> cls) {
        super(cls);
    }

    @Override // com.miui.epoxy.eventhook.EventHook
    public void onEvent(View view, final VH vh, final EpoxyAdapter epoxyAdapter) {
        view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.epoxy.eventhook.OnClickEventHook.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                int adapterPosition = vh.getAdapterPosition();
                EpoxyModel<?> model = epoxyAdapter.getModel(adapterPosition);
                if (adapterPosition == -1 || model == null) {
                    return;
                }
                OnClickEventHook.this.onClick(view2, vh, adapterPosition, model);
            }
        });
    }
}
