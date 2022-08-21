package com.miui.gallery.ui.album.main.viewbean;

import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.adapter.itemmodel.trans.ItemModelTransManager;
import com.miui.gallery.ui.album.common.GroupDatasResult;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class AlbumDataListResult extends GroupDatasResult<BaseViewBean> {
    public List<EpoxyModel<?>> mModels;

    public AlbumDataListResult() {
        this(Arrays.asList(AlbumSplitGroupHelper.getSplitGroupMode().getSupportGroups()));
    }

    public AlbumDataListResult(Map<String, List<BaseViewBean>> map) {
        super(map);
    }

    public AlbumDataListResult(List<String> list) {
        super(list == null ? Arrays.asList(AlbumSplitGroupHelper.getSplitGroupMode().getSupportGroups()) : list);
    }

    public List<BaseViewBean> addOrUpdateGroupItemById(String str, BaseViewBean baseViewBean, boolean z) {
        return addOrUpdateGroupItemById(str, -1, baseViewBean, z);
    }

    public List<BaseViewBean> addOrUpdateGroupItemById(String str, int i, BaseViewBean baseViewBean, boolean z) {
        if (baseViewBean == null) {
            return null;
        }
        if (!containsKey(str)) {
            addOrUpdateGroupDatas(str, new LinkedList());
        }
        List<BaseViewBean> groupDatas = getGroupDatas(str);
        long id = baseViewBean.getId();
        int i2 = 0;
        while (true) {
            if (i2 >= groupDatas.size()) {
                i2 = -1;
                break;
            } else if (groupDatas.get(i2).getId() == id) {
                break;
            } else {
                i2++;
            }
        }
        if (-1 != i2) {
            groupDatas.set(i2, baseViewBean);
        } else if (-1 == i || i > groupDatas.size()) {
            groupDatas.add(baseViewBean);
        } else {
            groupDatas.add(i, baseViewBean);
        }
        if (z) {
            refreshReleaseDatas();
        }
        return groupDatas;
    }

    public BaseViewBean removeDataById(String str, BaseViewBean baseViewBean, boolean z) {
        if (baseViewBean != null && containsKey(str)) {
            return removeDataById(str, baseViewBean.getId(), z);
        }
        return null;
    }

    public BaseViewBean removeDataById(String str, long j, boolean z) {
        List<BaseViewBean> groupDatas;
        int findIndexById;
        if (containsKey(str) && -1 != (findIndexById = findIndexById((groupDatas = getGroupDatas(str)), j))) {
            BaseViewBean remove = groupDatas.remove(findIndexById);
            if (z) {
                refreshReleaseDatas();
            }
            return remove;
        }
        return null;
    }

    public final int findIndexById(List<BaseViewBean> list, long j) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == j) {
                return i;
            }
        }
        return -1;
    }

    @Override // com.miui.gallery.ui.album.common.GroupDatasResult
    public void refreshReleaseDatas() {
        super.refreshReleaseDatas();
        setModels((List) this.mDatas.parallelStream().map(new Function<BaseViewBean, EpoxyModel<?>>() { // from class: com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult.1
            @Override // java.util.function.Function
            public EpoxyModel<?> apply(BaseViewBean baseViewBean) {
                return ItemModelTransManager.getInstance().transDataToModel(baseViewBean);
            }
        }).collect(Collectors.toList()));
    }

    public void setModels(List<EpoxyModel<?>> list) {
        this.mModels = list;
    }

    public List<EpoxyModel<?>> getModels() {
        return this.mModels;
    }
}
