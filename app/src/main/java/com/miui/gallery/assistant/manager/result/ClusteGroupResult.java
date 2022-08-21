package com.miui.gallery.assistant.manager.result;

import com.miui.gallery.assistant.jni.cluster.ClusterNode;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ClusteGroupResult extends AlgorithmResult {
    public List<ClusterNode> mClusters;

    public ClusteGroupResult(ArrayList<ClusterNode> arrayList) {
        super(0);
        this.mClusters = arrayList;
    }

    public ClusteGroupResult(int i) {
        super(i);
    }

    public List<ClusterNode> getClusters() {
        return this.mClusters;
    }
}
