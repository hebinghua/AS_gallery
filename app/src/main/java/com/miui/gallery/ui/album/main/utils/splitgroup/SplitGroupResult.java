package com.miui.gallery.ui.album.main.utils.splitgroup;

import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class SplitGroupResult<T> {
    public Map<String, List<T>> mGroups;
    public List<T> mIllegalDatas;

    public SplitGroupResult(Map<String, List<T>> map) {
        this.mGroups = map;
    }

    public SplitGroupResult(Map<String, List<T>> map, List<T> list) {
        this.mGroups = map;
        this.mIllegalDatas = list;
    }

    public Map<String, List<T>> getGroups() {
        return this.mGroups;
    }

    public List<T> getIllegalDatas() {
        return this.mIllegalDatas;
    }
}
