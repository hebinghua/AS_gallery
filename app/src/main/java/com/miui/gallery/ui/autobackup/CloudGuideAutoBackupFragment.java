package com.miui.gallery.ui.autobackup;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.eventhook.EventHook;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.autobackup.itemmodel.CloudGuideAutoBackupItemModel;
import com.miui.gallery.ui.autobackup.viewbean.CloudGuideAutoBackupItemViewBean;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class CloudGuideAutoBackupFragment extends CloudGuideAutoBackupContract$V {
    public GalleryIntent$CloudGuideSource mSource;

    @Override // com.miui.gallery.app.base.BaseListPageFragment, com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getLayoutId() {
        return R.layout.cloud_guide_auto_backup;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((CloudGuideAutoBackupPresenter) getPresenter()).initAll();
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public RecyclerView findRecyclerView() {
        return (RecyclerView) findViewById(R.id.list);
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        findViewById(R.id.enable_service_button).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.autobackup.CloudGuideAutoBackupFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SyncUtil.existXiaomiAccount(CloudGuideAutoBackupFragment.this.getActivity())) {
                    if (!SyncUtil.setSyncAutomatically(CloudGuideAutoBackupFragment.this.getActivity(), true)) {
                        return;
                    }
                    CloudGuideAutoBackupFragment.this.getActivity().setResult(-1);
                    CloudGuideAutoBackupFragment.this.getActivity().finish();
                    return;
                }
                AccountManager accountManager = AccountManager.get(CloudGuideAutoBackupFragment.this.getActivity());
                Bundle bundle = new Bundle();
                if (CloudGuideAutoBackupFragment.this.mSource != null) {
                    bundle.putString("stat_key_source", CloudGuideAutoBackupFragment.this.mSource.name());
                }
                accountManager.addAccount("com.xiaomi", null, null, bundle, CloudGuideAutoBackupFragment.this.getActivity(), new AccountManagerCallback<Bundle>() { // from class: com.miui.gallery.ui.autobackup.CloudGuideAutoBackupFragment.1.1
                    @Override // android.accounts.AccountManagerCallback
                    public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
                        if (CloudGuideAutoBackupFragment.this.getActivity() != null) {
                            CloudGuideAutoBackupFragment.this.getActivity().setResult(SyncUtil.existXiaomiAccount(CloudGuideAutoBackupFragment.this.getActivity()) ? -1 : 1);
                            CloudGuideAutoBackupFragment.this.getActivity().finish();
                        }
                    }
                }, null);
            }
        });
        this.mAdapter.addEventHook(new EventHook<CloudGuideAutoBackupItemModel.VH>(CloudGuideAutoBackupItemModel.VH.class) { // from class: com.miui.gallery.ui.autobackup.CloudGuideAutoBackupFragment.2
            @Override // com.miui.epoxy.eventhook.EventHook
            public void onEvent(View view, final CloudGuideAutoBackupItemModel.VH vh, final EpoxyAdapter epoxyAdapter) {
                long id = view.getId();
                if (id == 2131362114) {
                    vh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.ui.autobackup.CloudGuideAutoBackupFragment.2.1
                        @Override // android.widget.CompoundButton.OnCheckedChangeListener
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                            EpoxyModel<?> model = epoxyAdapter.getModel(vh.getAdapterPosition());
                            if (model instanceof CloudGuideAutoBackupItemModel) {
                                CloudGuideAutoBackupItemModel cloudGuideAutoBackupItemModel = (CloudGuideAutoBackupItemModel) model;
                                if (cloudGuideAutoBackupItemModel.getItemData() == 0) {
                                    return;
                                }
                                ((CloudGuideAutoBackupPresenter) CloudGuideAutoBackupFragment.this.getPresenter()).doChangeAlbumsBackupStatus(z, (Album) ((CloudGuideAutoBackupItemViewBean) cloudGuideAutoBackupItemModel.getItemData()).getSource());
                            }
                        }
                    });
                } else if (id != 2131362606) {
                } else {
                    view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.autobackup.CloudGuideAutoBackupFragment.2.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view2) {
                            CloudGuideAutoBackupFragment.this.onItemClicked(vh, epoxyAdapter);
                        }
                    });
                }
            }

            @Override // com.miui.epoxy.eventhook.EventHook
            public List<? extends View> onBindMany(CloudGuideAutoBackupItemModel.VH vh) {
                LinkedList linkedList = new LinkedList();
                linkedList.add(vh.itemView);
                linkedList.add(vh.getCheckBox());
                return linkedList;
            }
        });
        Intent intent = getActivity().getIntent();
        long longExtra = intent.getLongExtra("autobackup_album_id", -1L);
        this.mSource = (GalleryIntent$CloudGuideSource) intent.getSerializableExtra("cloud_guide_source");
        if (longExtra != -1) {
            ((CloudGuideAutoBackupPresenter) getPresenter()).doChangeAlbumsBackupStatus(true, longExtra);
        }
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getItemDecorations();
    }

    public final void onItemClicked(CloudGuideAutoBackupItemModel.VH vh, EpoxyAdapter epoxyAdapter) {
        Album albumEntity = ((CloudGuideAutoBackupPresenter) getPresenter()).getAlbumEntity(epoxyAdapter.getModel(vh.getAdapterPosition()));
        if (albumEntity == null) {
            return;
        }
        if (!albumEntity.isAutoUploadedAlbum()) {
            vh.getCheckBox().setChecked(!albumEntity.isAutoUploadedAlbum());
        } else if (albumEntity.isCameraAlbum()) {
            ToastUtils.makeText(getActivity(), (int) R.string.camera_needs_auto_backup_tip);
        } else if (albumEntity.isBabyAlbum()) {
            ToastUtils.makeText(getActivity(), (int) R.string.baby_album_needs_auto_backup_tip);
        } else if (albumEntity.isOwnerShareAlbum()) {
            ToastUtils.makeText(getActivity(), (int) R.string.share_album_needs_auto_upload_tip);
        } else {
            vh.getCheckBox().setChecked(!albumEntity.isAutoUploadedAlbum());
        }
    }
}
