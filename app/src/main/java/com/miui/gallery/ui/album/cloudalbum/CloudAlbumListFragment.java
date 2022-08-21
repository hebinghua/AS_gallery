package com.miui.gallery.ui.album.cloudalbum;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.eventhook.EventHook;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.CloudAlbumItemModel;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.ui.album.cloudalbum.viewbean.CloudAlbumItemViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import java.util.List;
import miuix.slidingwidget.widget.SlidingButton;

/* loaded from: classes2.dex */
public class CloudAlbumListFragment extends CloudAlbumListContract$V<CloudAlbumItemViewBean, CloudAlbumListPresenter> {
    public boolean isFirstAddEventHook = true;
    public boolean mIsForceSplit;

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((CloudAlbumListPresenter) getPresenter()).initData();
    }

    public static CloudAlbumListFragment newInstance(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_force_split", z);
        CloudAlbumListFragment cloudAlbumListFragment = new CloudAlbumListFragment();
        cloudAlbumListFragment.setArguments(bundle);
        return cloudAlbumListFragment;
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
        setEmptyPage(R.id.album_hidden_empty_view, getDefaultEmptyConfig().setTitle(R.string.empty_album_cloud_list_title));
        setRecyclerviewPadding(AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMargin(this.mIsForceSplit), 0, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMargin(this.mIsForceSplit), AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMarginBottom());
        recyclerView.setClipToPadding(false);
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$V
    public void showCloudList(List<CloudAlbumItemViewBean> list) {
        setDatas(list);
        addEventHook(list);
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getItemDecorations();
    }

    public final void addEventHook(List<CloudAlbumItemViewBean> list) {
        if (list == null || list.isEmpty() || !this.isFirstAddEventHook) {
            return;
        }
        this.mAdapter.addEventHook(new EventHook<CloudAlbumItemModel.CloudAlbumViewHolder>(CloudAlbumItemModel.CloudAlbumViewHolder.class) { // from class: com.miui.gallery.ui.album.cloudalbum.CloudAlbumListFragment.1
            @Override // com.miui.epoxy.eventhook.EventHook
            public void onEvent(View view, final CloudAlbumItemModel.CloudAlbumViewHolder cloudAlbumViewHolder, EpoxyAdapter epoxyAdapter) {
                if (view.getId() == R.id.item_cloud_album) {
                    cloudAlbumViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.album.cloudalbum.CloudAlbumListFragment.1.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view2) {
                            SlidingButton slidingButton = (SlidingButton) view2.findViewById(R.id.cbBackup);
                            if (slidingButton != null) {
                                slidingButton.toggle();
                            }
                        }
                    });
                    cloudAlbumViewHolder.mCbBackup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.ui.album.cloudalbum.CloudAlbumListFragment.1.2
                        @Override // android.widget.CompoundButton.OnCheckedChangeListener
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                            if (!compoundButton.isPressed() || !compoundButton.isInTouchMode()) {
                                return;
                            }
                            CloudAlbumListFragment.this.doChangeAutoUpload(cloudAlbumViewHolder.itemView.getTag(), z);
                        }
                    });
                    cloudAlbumViewHolder.mCbBackup.setOnPerformCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.ui.album.cloudalbum.CloudAlbumListFragment.1.3
                        @Override // android.widget.CompoundButton.OnCheckedChangeListener
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                            CloudAlbumListFragment.this.doChangeAutoUpload(cloudAlbumViewHolder.itemView.getTag(), z);
                        }
                    });
                }
            }

            @Override // com.miui.epoxy.eventhook.EventHook
            public View onBind(CloudAlbumItemModel.CloudAlbumViewHolder cloudAlbumViewHolder) {
                return cloudAlbumViewHolder.itemView;
            }
        });
        this.isFirstAddEventHook = false;
    }

    public void doChangeAutoUpload(Object obj, boolean z) {
        Account account = AccountCache.getAccount();
        if (account != null) {
            if (z && !ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider") && !SyncUtil.setSyncAutomatically(getActivity(), true)) {
                showChangeAlbumUploadStatusFailed(((CloudAlbumListPresenter) getPresenter()).converterTagBeanToCloudAlbumItemItemViewBean(obj));
            } else {
                ((CloudAlbumListPresenter) getPresenter()).doChangeAlbumUploadStatus(obj, z);
            }
        }
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$V
    public void showChangeAlbumCantBeShareAlbum(CloudAlbumItemViewBean cloudAlbumItemViewBean) {
        ToastUtils.makeText(getActivity(), (int) R.string.share_album_needs_auto_upload_tip);
        notifyDataChange(cloudAlbumItemViewBean);
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$V
    public void showChangeAlbumCantBeRubbishAlbum(CloudAlbumItemViewBean cloudAlbumItemViewBean) {
        ToastUtils.makeText(getActivity(), (int) R.string.album_rubbish_cant_change_status);
        notifyDataChange(cloudAlbumItemViewBean);
    }

    public final void notifyDataChange(final CloudAlbumItemViewBean cloudAlbumItemViewBean) {
        if (isComputingLayout()) {
            postRunnableToRecycleView(new Runnable() { // from class: com.miui.gallery.ui.album.cloudalbum.CloudAlbumListFragment.2
                @Override // java.lang.Runnable
                public void run() {
                    CloudAlbumListFragment.this.notifyDataChange(cloudAlbumItemViewBean);
                }
            });
        } else {
            this.mAdapter.notifyDataChanged(cloudAlbumItemViewBean);
        }
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$V
    public void showChangeAlbumUploadStatusSuccess(CloudAlbumItemViewBean cloudAlbumItemViewBean, boolean z) {
        ToastUtils.makeTextLong(getActivity(), z ? R.string.auto_upload_enable_toast_long_press_menu : R.string.auto_upload_disable_toast_long_press_menu);
    }

    @Override // com.miui.gallery.ui.album.cloudalbum.CloudAlbumListContract$V
    public void showChangeAlbumUploadStatusFailed(CloudAlbumItemViewBean cloudAlbumItemViewBean) {
        if (cloudAlbumItemViewBean == null) {
            return;
        }
        this.mAdapter.notifyDataChanged(cloudAlbumItemViewBean);
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mIsForceSplit) {
            setRecyclerviewPadding(AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMargin(this.mIsForceSplit), 0, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMargin(this.mIsForceSplit), AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMarginBottom());
        }
    }
}
