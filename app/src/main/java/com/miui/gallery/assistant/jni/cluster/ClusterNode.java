package com.miui.gallery.assistant.jni.cluster;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class ClusterNode {
    private int clusterID;
    private ArrayList<String> imgs;

    public int getClusterID() {
        return this.clusterID;
    }

    public void setClusterID(int i) {
        this.clusterID = i;
    }

    public ArrayList<String> getImgs() {
        return this.imgs;
    }

    public void setImgs(ArrayList<String> arrayList) {
        this.imgs = arrayList;
    }
}
