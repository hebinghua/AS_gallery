package com.miui.gallery.ui.album.common;

import android.util.ArrayMap;
import android.util.Pair;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/* loaded from: classes2.dex */
public class GroupDatasResult<T> {
    public ArrayMap<String, List<T>> mDataGroups;
    public List<T> mDatas;
    public ArrayMap<String, Pair<Integer, Integer>> mGroupDataSegment;
    public ArrayMap<String, Pair<Integer, T>> mGroupGapBeans;
    public final List<String> mTypes;

    public GroupDatasResult(List<String> list) {
        ArrayList arrayList = new ArrayList(list.size());
        this.mTypes = arrayList;
        arrayList.addAll(list);
        int size = arrayList.size();
        this.mDataGroups = new ArrayMap<>(size);
        this.mGroupDataSegment = new ArrayMap<>(size);
    }

    public GroupDatasResult(Map<String, List<T>> map) {
        ArrayList arrayList = new ArrayList(map.keySet());
        this.mTypes = arrayList;
        int size = arrayList.size();
        this.mDataGroups = new ArrayMap<>(size);
        this.mGroupDataSegment = new ArrayMap<>(size);
        internalInit(map);
    }

    public GroupDatasResult(GroupDatasResult<T> groupDatasResult) {
        LinkedList linkedList = new LinkedList(groupDatasResult.mTypes);
        this.mTypes = linkedList;
        int size = linkedList.size();
        this.mDataGroups = new ArrayMap<>(size);
        this.mGroupDataSegment = new ArrayMap<>(size);
        internalInit(groupDatasResult.mDataGroups);
    }

    public final void internalInit(Map<String, List<T>> map) {
        this.mDatas = new ArrayList(map.size());
        for (Map.Entry<String, List<T>> entry : map.entrySet()) {
            List<T> value = entry.getValue();
            this.mDataGroups.put(entry.getKey(), value);
            this.mDatas.addAll(value);
        }
    }

    @Deprecated
    public List<T> addOrUpdateGroupItem(String str, T t, boolean z) {
        return addOrUpdateGroupItem(str, -1, t, z);
    }

    @Deprecated
    public List<T> addOrUpdateGroupItem(String str, int i, T t, boolean z) {
        if (t == null) {
            return null;
        }
        if (!containsKey(str)) {
            this.mDataGroups.put(str, new LinkedList());
        }
        List<T> groupDatas = getGroupDatas(str);
        int indexOf = groupDatas.indexOf(t);
        if (-1 != indexOf) {
            groupDatas.set(indexOf, t);
        } else if (-1 == i || i > groupDatas.size()) {
            groupDatas.add(t);
        } else {
            groupDatas.add(i, t);
        }
        if (z) {
            refreshReleaseDatas();
        }
        return groupDatas;
    }

    public List<T> addGroupItem(String str, int i, T t, boolean z) {
        if (t == null) {
            return null;
        }
        if (!containsKey(str)) {
            this.mDataGroups.put(str, new LinkedList());
        }
        List<T> groupDatas = getGroupDatas(str);
        if (-1 != i) {
            groupDatas.add(i, t);
        } else {
            groupDatas.add(t);
        }
        if (z) {
            refreshReleaseDatas();
        }
        return groupDatas;
    }

    public List<T> addGroupItem(String str, T t, boolean z) {
        return addGroupItem(str, -1, t, z);
    }

    public void addOrUpdateGroupDatas(String str, List<T> list) {
        try {
            if (!containsKey(str)) {
                this.mDataGroups.put(str, new LinkedList(list));
            } else {
                this.mDataGroups.setValueAt(this.mDataGroups.indexOfKey(str), new LinkedList(list));
            }
            refreshReleaseDatas();
        } catch (Exception e) {
            DefaultLogger.e("AlbumCoverResult", e.getMessage());
        }
    }

    public void removeGroup(String str) {
        ArrayMap<String, List<T>> arrayMap = this.mDataGroups;
        if (arrayMap != null) {
            arrayMap.remove(str);
            refreshReleaseDatas();
        }
    }

    public void replaceGroup(String str, List<T> list) {
        ArrayMap<String, List<T>> arrayMap = this.mDataGroups;
        if (arrayMap != null) {
            arrayMap.replace(str, list);
            refreshReleaseDatas();
        }
    }

    public void addGroup(String str, List<T> list, boolean z) {
        addGroup(str, list, true, z);
    }

    public void addGroup(String str, List<T> list, boolean z, boolean z2) {
        if (this.mDataGroups != null) {
            if (!this.mTypes.contains(str)) {
                if (z2) {
                    this.mTypes.add(0, str);
                } else {
                    this.mTypes.add(str);
                }
            } else if (z2) {
                this.mTypes.remove(str);
                this.mTypes.add(0, str);
            }
            if (this.mDataGroups.containsKey(str)) {
                this.mDataGroups.replace(str, list);
            } else {
                this.mDataGroups.put(str, list);
            }
            if (!z) {
                return;
            }
            refreshReleaseDatas();
        }
    }

    public boolean containsKey(String str) {
        ArrayMap<String, List<T>> arrayMap = this.mDataGroups;
        return arrayMap != null && arrayMap.indexOfKey(str) >= 0;
    }

    public final int calculateDataSize() {
        int i = 0;
        int i2 = 0;
        for (String str : this.mDataGroups.keySet()) {
            i2 += this.mDataGroups.get(str).size();
        }
        ArrayMap<String, Pair<Integer, T>> arrayMap = this.mGroupGapBeans;
        if (arrayMap != null) {
            i = arrayMap.size();
        }
        return i2 + i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void refreshReleaseDatas() {
        if (this.mDataGroups != null) {
            this.mDatas = new ArrayList(calculateDataSize());
            this.mGroupDataSegment.clear();
            for (String str : this.mTypes) {
                List<T> list = this.mDataGroups.get(str);
                int size = this.mDatas.size();
                int i = 0;
                int size2 = (list != null ? list.size() : 0) + size;
                ArrayMap<String, Pair<Integer, T>> arrayMap = this.mGroupGapBeans;
                Pair<Integer, T> pair = arrayMap != null ? arrayMap.get(str) : null;
                if (list != null && !list.isEmpty()) {
                    if (pair != null && ((Integer) pair.first).intValue() == 1) {
                        this.mDatas.add(pair.second);
                    }
                    this.mDatas.addAll(list);
                    if (pair != null && ((Integer) pair.first).intValue() == 2) {
                        this.mDatas.add(pair.second);
                    }
                } else if (pair != null) {
                    this.mDatas.add(pair.second);
                }
                ArrayMap<String, Pair<Integer, Integer>> arrayMap2 = this.mGroupDataSegment;
                Integer valueOf = Integer.valueOf(size);
                if (size2 > 0) {
                    i = size2 - 1;
                }
                arrayMap2.put(str, new Pair<>(valueOf, Integer.valueOf(i)));
            }
        }
    }

    public boolean isEmpty() {
        List<T> list = this.mDatas;
        return list == null || list.isEmpty();
    }

    public List<T> getGroupDatas(String str) {
        ArrayMap<String, List<T>> arrayMap = this.mDataGroups;
        if (arrayMap != null) {
            return arrayMap.get(str);
        }
        return null;
    }

    public int groupSize() {
        ArrayMap<String, List<T>> arrayMap = this.mDataGroups;
        if (arrayMap != null) {
            return arrayMap.keySet().size();
        }
        return 0;
    }

    public boolean isHaveGroupDatas(String str) {
        List<T> groupDatas = getGroupDatas(str);
        if (groupDatas == null) {
            return false;
        }
        ArrayMap<String, Pair<Integer, T>> arrayMap = this.mGroupGapBeans;
        if (arrayMap != null && arrayMap.containsKey(str)) {
            groupDatas.remove(this.mGroupGapBeans.get(str).second);
        }
        return !groupDatas.isEmpty();
    }

    public void addGroupGapDecorator(String str, int i, T t) {
        if (this.mGroupGapBeans == null) {
            this.mGroupGapBeans = new ArrayMap<>(2);
        }
        this.mGroupGapBeans.put(str, new Pair<>(Integer.valueOf(i), t));
    }

    public Pair<Integer, T> getGroupGapDecorator(String str) {
        ArrayMap<String, Pair<Integer, T>> arrayMap = this.mGroupGapBeans;
        if (arrayMap == null) {
            return null;
        }
        return arrayMap.get(str);
    }

    public List<T> getDatas() {
        return getDatas(false);
    }

    public List<T> getDatas(boolean z) {
        if (z) {
            refreshReleaseDatas();
        }
        return this.mDatas;
    }

    public int getDataSize() {
        List<T> list = this.mDatas;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public void sortBy(String str, Comparator<T> comparator, boolean z) {
        ArrayMap<String, List<T>> arrayMap;
        if (this.mDatas == null || (arrayMap = this.mDataGroups) == null || !arrayMap.containsKey(str)) {
            return;
        }
        List<T> list = this.mDataGroups.get(str);
        ArrayMap<String, Pair<Integer, T>> arrayMap2 = this.mGroupGapBeans;
        if (arrayMap2 != null && arrayMap2.containsKey(str)) {
            list.remove(this.mGroupGapBeans.get(str).second);
        }
        list.sort(comparator);
        if (!z) {
            return;
        }
        refreshReleaseDatas();
    }

    public List<T> findItemBy(Predicate<T> predicate) {
        if (this.mDataGroups == null || predicate == null) {
            return null;
        }
        LinkedList linkedList = new LinkedList();
        for (List<T> list : this.mDataGroups.values()) {
            for (T t : list) {
                if (predicate.test(t)) {
                    linkedList.add(t);
                }
            }
        }
        return linkedList;
    }
}
