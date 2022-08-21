package com.miui.gallery.assistant.jni.cluster;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class SimilarAndSameCluster {
    private ArrayList<ClusterNode> sameCluster;
    private ArrayList<ClusterNode> similarCluster;

    public ArrayList<ClusterNode> getSimilarCluster() {
        return this.similarCluster;
    }

    public void setSimilarCluster(ArrayList<ClusterNode> arrayList) {
        this.similarCluster = arrayList;
    }

    public ArrayList<ClusterNode> getSameCluster() {
        return this.sameCluster;
    }

    public void setSameCluster(ArrayList<ClusterNode> arrayList) {
        this.sameCluster = arrayList;
    }
}
