package com.miui.gallery.adapter.itemmodel.trans;

import com.miui.epoxy.EpoxyModel;

/* loaded from: classes.dex */
public interface TransDataToModelSolver {
    Class[] supportTypes();

    EpoxyModel<?> transDataToModel(Object obj);
}
