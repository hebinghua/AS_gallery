package com.miui.gallery.ui.addtoalbum;

import android.text.TextUtils;
import androidx.core.util.Pair;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.adapter.itemmodel.common.config.CommonGridItemViewDisplaySetting;
import com.miui.gallery.adapter.itemmodel.trans.ItemModelTransManager;
import com.miui.gallery.ui.addtoalbum.viewbean.AddToAlbumItemViewBean;
import com.miui.gallery.ui.album.common.GroupDatasResult;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/* loaded from: classes2.dex */
public class AddToAlbumGroupResult extends GroupDatasResult<BaseViewBean> {
    public static final CommonGridItemViewDisplaySetting sDisplaySetting = AlbumPageConfig.getAddToAlbumConfig().getNormalGroupItemDisplaySetting();
    public static final CommonGridItemViewDisplaySetting sHeaderDisplaySetting = AlbumPageConfig.getAddToAlbumConfig().getHeaderGroupSetting();
    public List<EpoxyModel<?>> mModels;

    /* renamed from: $r8$lambda$u9A8fThMOjRzpeFJ8MUka-LTC_M */
    public static /* synthetic */ boolean m1573$r8$lambda$u9A8fThMOjRzpeFJ8MUkaLTC_M(List list, BaseViewBean baseViewBean) {
        return lambda$findItemByIds$0(list, baseViewBean);
    }

    public AddToAlbumGroupResult(GroupDatasResult<BaseViewBean> groupDatasResult) {
        super(groupDatasResult);
    }

    @Override // com.miui.gallery.ui.album.common.GroupDatasResult
    public void refreshReleaseDatas() {
        super.refreshReleaseDatas();
        setModels(transToModel(this.mDatas));
    }

    public final List<EpoxyModel<?>> transToModel(List<BaseViewBean> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (BaseViewBean baseViewBean : list) {
            arrayList.add(transSingleItem(baseViewBean));
        }
        return arrayList;
    }

    public final EpoxyModel<?> transSingleItem(BaseViewBean baseViewBean) {
        EpoxyModel<?> transDataToModel = ItemModelTransManager.getInstance().transDataToModel(baseViewBean);
        if (transDataToModel instanceof AddToAlbumItemModel) {
            AddToAlbumItemModel addToAlbumItemModel = (AddToAlbumItemModel) transDataToModel;
            if (((AddToAlbumItemViewBean) addToAlbumItemModel.getItemData()).isRecentItem()) {
                addToAlbumItemModel.setDisplaySetting(sHeaderDisplaySetting);
            } else {
                addToAlbumItemModel.setDisplaySetting(sDisplaySetting);
            }
        }
        return transDataToModel;
    }

    public Pair<List<BaseViewBean>, List<EpoxyModel<?>>> getGroupDataAndModels(String str) {
        if (TextUtils.isEmpty(str) || !containsKey(str)) {
            return null;
        }
        List<BaseViewBean> groupDatas = getGroupDatas(str);
        return Pair.create(groupDatas, transToModel(groupDatas));
    }

    public List<BaseViewBean> findItemByIds(final List<Long> list) {
        if (list == null || list.size() <= 0 || this.mDataGroups == null) {
            return null;
        }
        return findItemBy(new Predicate() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumGroupResult$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return AddToAlbumGroupResult.m1573$r8$lambda$u9A8fThMOjRzpeFJ8MUkaLTC_M(list, (BaseViewBean) obj);
            }
        });
    }

    public static /* synthetic */ boolean lambda$findItemByIds$0(List list, BaseViewBean baseViewBean) {
        return list.contains(Long.valueOf(baseViewBean.getId()));
    }

    public void setModels(List<EpoxyModel<?>> list) {
        this.mModels = list;
    }

    public List<EpoxyModel<?>> getModels() {
        return this.mModels;
    }
}
