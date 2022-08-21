package com.miui.gallery.ui.album.hiddenalbum;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.eventhook.OnClickEventHook;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.HiddenAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.hiddenalbum.viewbean.HiddenAlbumItemViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class HiddenAlbumFragment extends HiddenAlbumContract$V<HiddenAlbumItemViewBean, HiddenAlbumPresenter> {
    public boolean isFirstAddEventHook = true;
    public boolean mIsForceSplit;

    @Override // com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumContract$V
    public void showUnHiddenAlbumIsFailed() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((HiddenAlbumPresenter) getPresenter()).initData();
    }

    public static HiddenAlbumFragment newInstance(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_force_split", z);
        HiddenAlbumFragment hiddenAlbumFragment = new HiddenAlbumFragment();
        hiddenAlbumFragment.setArguments(bundle);
        return hiddenAlbumFragment;
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.base_optimization.mvp.view.Fragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mIsForceSplit = getArguments().getBoolean("is_force_split", false);
        }
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getSafeActivity(), 1, false));
        setEmptyPage(R.id.album_hidden_empty_view, getDefaultEmptyConfig().setTitle(R.string.empty_album_hidden_list_title).setActionButtonVisible(false));
        setRecyclerviewPadding(AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMargin(this.mIsForceSplit), 0, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMargin(this.mIsForceSplit), AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMarginBottom());
        recyclerView.setClipToPadding(false);
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getItemDecorations();
    }

    @Override // com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumContract$V
    public void showHiddenListData(List<HiddenAlbumItemViewBean> list) {
        setDatas(list);
        addEventHook(list);
    }

    public final void addEventHook(List<HiddenAlbumItemViewBean> list) {
        if (list == null || list.isEmpty() || !this.isFirstAddEventHook) {
            return;
        }
        this.mAdapter.addEventHook(new OnClickEventHook<HiddenAlbumItemModel.HiddenAlbumViewHolder>(HiddenAlbumItemModel.HiddenAlbumViewHolder.class) { // from class: com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumFragment.1
            @Override // com.miui.epoxy.eventhook.OnClickEventHook
            public void onClick(View view, HiddenAlbumItemModel.HiddenAlbumViewHolder hiddenAlbumViewHolder, int i, EpoxyModel epoxyModel) {
                if (epoxyModel instanceof HiddenAlbumItemModel) {
                    if (view.getId() == R.id.btnCancelHidden) {
                        HiddenAlbumFragment.this.showConfirmDialog((HiddenAlbumItemModel) epoxyModel);
                    } else if (view.getId() != CommonAlbumItemModel.ViewHolder.getCoverViewId()) {
                    } else {
                        IntentUtil.gotoAlbumDetailPage(HiddenAlbumFragment.this.getSafeActivity(), (Album) ((HiddenAlbumItemViewBean) ((HiddenAlbumItemModel) epoxyModel).getItemData()).getSource(), 1001, -1);
                    }
                }
            }

            @Override // com.miui.epoxy.eventhook.EventHook
            public List<? extends View> onBindMany(HiddenAlbumItemModel.HiddenAlbumViewHolder hiddenAlbumViewHolder) {
                ArrayList arrayList = new ArrayList(2);
                arrayList.add(hiddenAlbumViewHolder.mBtnCancelButton);
                arrayList.add(hiddenAlbumViewHolder.mAlbumCover);
                return arrayList;
            }
        });
        this.isFirstAddEventHook = false;
    }

    public final void showConfirmDialog(final HiddenAlbumItemModel hiddenAlbumItemModel) {
        DialogUtil.showInfoDialog(getSafeActivity(), (int) R.string.remove_hidden_status_tip, (int) R.string.remove_hidden_status_title, (int) R.string.ok, (int) R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ((HiddenAlbumPresenter) HiddenAlbumFragment.this.getPresenter()).unAlbumHide((HiddenAlbumItemViewBean) hiddenAlbumItemModel.getItemData());
            }
        }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }

    @Override // com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumContract$V
    public void showUnHiddenAlbumIsSuccess(HiddenAlbumItemViewBean hiddenAlbumItemViewBean) {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), getString(R.string.remove_hidden_status_success, hiddenAlbumItemViewBean.getTitle()));
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mIsForceSplit) {
            setRecyclerviewPadding(AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMargin(this.mIsForceSplit), 0, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMargin(this.mIsForceSplit), AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMarginBottom());
        }
    }
}
