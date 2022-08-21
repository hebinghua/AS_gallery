package com.miui.gallery.threadpool;

import java.util.Comparator;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class PriorityTaskManager$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ PriorityTaskManager$$ExternalSyntheticLambda0 INSTANCE = new PriorityTaskManager$$ExternalSyntheticLambda0();

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int compareTo;
        compareTo = ((PriorityTask) obj2).compareTo((PriorityTask) obj);
        return compareTo;
    }
}
