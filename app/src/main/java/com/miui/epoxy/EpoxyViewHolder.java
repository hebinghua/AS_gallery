package com.miui.epoxy;

import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes.dex */
public class EpoxyViewHolder extends RecyclerView.ViewHolder {
    public EpoxyModel model;

    public EpoxyViewHolder(View view) {
        super(view);
    }

    public void bind(EpoxyModel epoxyModel, List<Object> list) {
        if (list != null && !list.isEmpty()) {
            if (EpoxyModel.isValidPayload(list)) {
                epoxyModel.bindPartialData(this, list);
            } else {
                Log.w("EpoxyViewHolder", "bindPartialData called,but payloads is invalida");
            }
        } else {
            epoxyModel.bindData(this);
        }
        this.model = epoxyModel;
    }

    public void unbind() {
        EpoxyModel epoxyModel = this.model;
        if (epoxyModel == null) {
            return;
        }
        epoxyModel.unbind(this);
        this.model = null;
    }
}
