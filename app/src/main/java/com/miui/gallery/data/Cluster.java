package com.miui.gallery.data;

import java.util.List;

/* compiled from: Cluster.kt */
/* loaded from: classes.dex */
public interface Cluster {
    int getChildCount(int i, boolean z);

    int getGroupCount(boolean z);

    String getGroupLabel(int i, boolean z);

    int[] getGroupPositions(int i, boolean z);

    int getGroupStartPosition(int i, boolean z);

    List<Integer> getGroupStartPositions(boolean z);

    int getItemCount();

    int packDataPosition(int i, int i2, boolean z);

    int[] unpackAdapterPosition(int i, boolean z);
}
