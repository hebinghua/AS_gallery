package com.miui.gallery.ui.album.main.utils.splitgroup.version2;

import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.ExtraSourceProvider;
import com.miui.gallery.picker.PickerBaseActivity;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import com.miui.gallery.ui.album.common.MediaGroupTypeViewBean;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$V;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumTabToolItemComparator;
import com.miui.gallery.ui.album.main.utils.splitgroup.IGroupSettingInfo;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class AlbumTabDataProcessingCallback implements AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback {
    public LazyValue<Void, AlbumTabToolItemComparator> mAlbumTabToolItemComparator = new LazyValue<Void, AlbumTabToolItemComparator>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumTabDataProcessingCallback.1
        {
            AlbumTabDataProcessingCallback.this = this;
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public AlbumTabToolItemComparator mo1272onInit(Void r1) {
            return new AlbumTabToolItemComparator();
        }
    };
    public IGroupSettingInfo mGroupSettingInfo;
    public WeakReference<BaseAlbumTabContract$P> mPresenterRef;

    public static /* synthetic */ boolean $r8$lambda$M5tpPxUVzMe7QcAqu9IxqXty1yk(AlbumTabDataProcessingCallback albumTabDataProcessingCallback, Object obj) {
        return albumTabDataProcessingCallback.lambda$addImmutableGroupIfNeed$0(obj);
    }

    public AlbumTabDataProcessingCallback(WeakReference weakReference, IGroupSettingInfo iGroupSettingInfo) {
        this.mPresenterRef = weakReference;
        this.mGroupSettingInfo = iGroupSettingInfo;
    }

    public final String getGroupType(Album album) {
        return AlbumSplitGroupHelper.getSplitGroupMode().getGroupType(album);
    }

    public final BaseAlbumTabContract$P getPresenter() {
        return this.mPresenterRef.get();
    }

    public final BaseAlbumTabContract$V<BaseAlbumTabContract$P> getView() {
        if (this.mPresenterRef.get() != null) {
            return (BaseAlbumTabContract$V) this.mPresenterRef.get().getView();
        }
        return null;
    }

    @Override // com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback
    public void onProcessStart(List<Album> list) {
        if (getPresenter() == null || getView() == null) {
            return;
        }
        BaseViewBean otherAlbumBean = getPresenter().getOtherAlbumBean();
        BaseViewBean aIAlbumBean = getPresenter().getAIAlbumBean();
        list.removeIf(new Predicate<Album>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumTabDataProcessingCallback.2
            {
                AlbumTabDataProcessingCallback.this = this;
            }

            @Override // java.util.function.Predicate
            public boolean test(Album album) {
                long albumId = album.getAlbumId();
                return albumId == 2147483639 || albumId == 2147483638 || albumId == 2147483641;
            }
        });
        if (!AlbumSortHelper.isCustomSortOrder()) {
            return;
        }
        if (otherAlbumBean instanceof ExtraSourceProvider) {
            list.add((Album) ((ExtraSourceProvider) otherAlbumBean).mo1601provider());
        }
        if (!isEnableAIAlbum() || !(aIAlbumBean instanceof ExtraSourceProvider)) {
            return;
        }
        list.add((Album) ((ExtraSourceProvider) aIAlbumBean).mo1601provider());
    }

    @Override // com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback
    public void onSplitGroupFinish(AlbumDataListResult albumDataListResult) {
        if (getPresenter() == null || getView() == null) {
            return;
        }
        if (getPresenter().getOtherAlbumBean() instanceof ExtraSourceProvider) {
            albumDataListResult.addOrUpdateGroupItemById(getGroupType((Album) ((ExtraSourceProvider) getPresenter().getOtherAlbumBean()).mo1601provider()), getPresenter().getOtherAlbumBean(), false);
        }
        if (isEnableAIAlbum() && (getPresenter().getAIAlbumBean() instanceof ExtraSourceProvider)) {
            albumDataListResult.addOrUpdateGroupItemById(getGroupType((Album) ((ExtraSourceProvider) getPresenter().getAIAlbumBean()).mo1601provider()), getPresenter().getAIAlbumBean(), false);
        }
        addMediaGroupIfNeed(albumDataListResult);
        addImmutableGroupIfNeed(albumDataListResult);
        fillGroupDecorator(albumDataListResult);
        int i = 2;
        BaseViewBean userAlbumGroupTipBean = getUserAlbumGroupTipBean(albumDataListResult.isHaveGroupDatas("group_user") ? 2 : 1);
        if (userAlbumGroupTipBean != null) {
            albumDataListResult.addGroupGapDecorator("group_user", 1, userAlbumGroupTipBean);
        }
        if (!albumDataListResult.isHaveGroupDatas("group_third")) {
            i = 1;
        }
        BaseViewBean thirdAlbumGroupTipBean = getThirdAlbumGroupTipBean(i);
        if (thirdAlbumGroupTipBean != null) {
            albumDataListResult.addGroupGapDecorator("group_third", 1, thirdAlbumGroupTipBean);
        }
        albumDataListResult.getDatas(true);
        albumDataListResult.setModels(albumDataListResult.getModels());
    }

    @Override // com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback
    public void onProcessEnd(AlbumDataListResult albumDataListResult) {
        DefaultLogger.d("TabDelegateVersion2", "onProcessEnd albums:");
        DefaultLogger.d("TabDelegateVersion2", albumDataListResult.getDatas());
    }

    public final void addImmutableGroupIfNeed(AlbumDataListResult albumDataListResult) {
        try {
            if (getView() != null && getView().getActivity() != null && (getView().getActivity() instanceof PickerBaseActivity)) {
                return;
            }
            LinkedList linkedList = new LinkedList();
            BaseViewBean trashAlbumBean = getPresenter().getTrashAlbumBean();
            BaseViewBean rubbishAlbumBean = getPresenter().getRubbishAlbumBean();
            BaseViewBean cleanerBean = getPresenter().getCleanerBean();
            if (trashAlbumBean != null) {
                linkedList.add(trashAlbumBean);
            }
            if (rubbishAlbumBean != null) {
                linkedList.add(rubbishAlbumBean);
            }
            if (cleanerBean != null) {
                linkedList.add(cleanerBean);
            }
            List list = (List) linkedList.stream().filter(new Predicate() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumTabDataProcessingCallback$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return AlbumTabDataProcessingCallback.$r8$lambda$M5tpPxUVzMe7QcAqu9IxqXty1yk(AlbumTabDataProcessingCallback.this, obj);
                }
            }).collect(Collectors.toList());
            list.sort(this.mAlbumTabToolItemComparator.get(null));
            albumDataListResult.addGroup("group_immutable", list, false);
        } catch (Exception e) {
            DefaultLogger.e("TabDelegateVersion2", "addImmutableGroup failed %s", e);
        }
    }

    public /* synthetic */ boolean lambda$addImmutableGroupIfNeed$0(Object obj) {
        return ((AlbumTabToolItemBean) obj).getId() != 2147483638 || getPresenter().isCanShowTrashAlbum();
    }

    public final void addMediaGroupIfNeed(AlbumDataListResult albumDataListResult) {
        List<MediaGroupTypeViewBean> mediaTypeGroups = getPresenter().getMediaTypeGroups();
        if (mediaTypeGroups == null || mediaTypeGroups.isEmpty()) {
            return;
        }
        albumDataListResult.addGroup("group_media_group", mediaTypeGroups, false);
    }

    public final BaseViewBean getUserAlbumGroupTipBean(int i) {
        if (getView() == null) {
            return null;
        }
        return getView().getOrGenerateTitleBean(2131362624L, R.string.group_header_title_user_create_album, i);
    }

    public boolean isEnableAIAlbum() {
        return getPresenter().isEnableAlbumById(2147483639);
    }

    public final void fillGroupDecorator(AlbumDataListResult albumDataListResult) {
        this.mGroupSettingInfo.fillGroupGap(albumDataListResult);
    }

    public final BaseViewBean getThirdAlbumGroupTipBean(int i) {
        if (getView() == null) {
            return null;
        }
        return getView().getOrGenerateTitleBean(2131362621L, R.string.group_header_title_third_album, i);
    }
}
