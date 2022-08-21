package com.miui.gallery.ui.album.main.utils.splitgroup.version2;

import com.miui.gallery.R;
import com.miui.gallery.ui.album.common.GroupDatasResult;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.common.viewbean.AlbumTabGroupTitleViewBean;
import com.miui.gallery.ui.album.main.utils.splitgroup.IGroupSettingInfo;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class GroupSettingInfo implements IGroupSettingInfo {
    public Map<Long, AlbumTabGroupTitleViewBean> mGroupBeanCaches = new HashMap(4, 1.0f);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.IGroupSettingInfo
    public <T extends BaseViewBean> void fillGroupGap(GroupDatasResult<T> groupDatasResult) {
        BaseViewBean userAlbumGroupTipBean = getUserAlbumGroupTipBean();
        if (userAlbumGroupTipBean != null && groupDatasResult.isHaveGroupDatas("group_user")) {
            groupDatasResult.addGroupGapDecorator("group_user", 1, userAlbumGroupTipBean);
        }
        BaseViewBean thirdAlbumGroupTipBean = getThirdAlbumGroupTipBean();
        if (thirdAlbumGroupTipBean != null && groupDatasResult.isHaveGroupDatas("group_third")) {
            groupDatasResult.addGroupGapDecorator("group_third", 1, thirdAlbumGroupTipBean);
        }
        BaseViewBean mediaGroupBean = getMediaGroupBean();
        if (mediaGroupBean != null && groupDatasResult.isHaveGroupDatas("group_media_group")) {
            groupDatasResult.addGroupGapDecorator("group_media_group", 1, mediaGroupBean);
        }
        BaseViewBean immutableGroupTipBean = getImmutableGroupTipBean();
        if (immutableGroupTipBean == null || !groupDatasResult.isHaveGroupDatas("group_immutable")) {
            return;
        }
        groupDatasResult.addGroupGapDecorator("group_immutable", 1, immutableGroupTipBean);
    }

    public final BaseViewBean getImmutableGroupTipBean() {
        return getOrGenerateTitleBean(2131362613L, R.string.group_header_title_immutable_album);
    }

    public final BaseViewBean getMediaGroupBean() {
        return getOrGenerateTitleBean(2131362616L, R.string.group_header_title_media_group);
    }

    public final BaseViewBean getUserAlbumGroupTipBean() {
        return getOrGenerateTitleBean(2131362624L, R.string.group_header_title_user_create_album);
    }

    public final BaseViewBean getThirdAlbumGroupTipBean() {
        return getOrGenerateTitleBean(2131362621L, R.string.group_header_title_third_album);
    }

    public BaseViewBean getOrGenerateTitleBean(long j, int i) {
        if (!this.mGroupBeanCaches.containsKey(Long.valueOf(j))) {
            AlbumTabGroupTitleViewBean albumTabGroupTitleViewBean = new AlbumTabGroupTitleViewBean(j, i);
            this.mGroupBeanCaches.put(Long.valueOf(j), albumTabGroupTitleViewBean);
            return albumTabGroupTitleViewBean;
        }
        return this.mGroupBeanCaches.get(Long.valueOf(j));
    }
}
