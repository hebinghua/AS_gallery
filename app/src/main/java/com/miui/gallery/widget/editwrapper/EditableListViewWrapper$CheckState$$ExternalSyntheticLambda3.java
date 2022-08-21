package com.miui.gallery.widget.editwrapper;

import java.util.LinkedList;
import java.util.function.ObjIntConsumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class EditableListViewWrapper$CheckState$$ExternalSyntheticLambda3 implements ObjIntConsumer {
    public static final /* synthetic */ EditableListViewWrapper$CheckState$$ExternalSyntheticLambda3 INSTANCE = new EditableListViewWrapper$CheckState$$ExternalSyntheticLambda3();

    @Override // java.util.function.ObjIntConsumer
    public final void accept(Object obj, int i) {
        ((LinkedList) obj).add(Integer.valueOf(i));
    }
}
