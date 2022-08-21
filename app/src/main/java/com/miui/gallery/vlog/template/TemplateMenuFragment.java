package com.miui.gallery.vlog.template;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.List;

/* loaded from: classes2.dex */
public class TemplateMenuFragment extends MenuFragment implements TemplateMenuContract$ITemplateMenuView {
    public TemplateAdapter mAdapter;
    public FrameLayout mOperationView;
    public FrameLayout mPlayLayout;
    public ImageView mPlayView;
    public int mPostponedPosition;
    public SimpleRecyclerView mRecyclerView;
    public ResourceDownloadManager mResourceDownloadManager;
    public int mTargetPosition;
    public boolean mTemplateItemClickPostponed;
    public TemplateZipFileConfig mZipFileConfig;
    public View.OnClickListener mPlayViewClickListener = new View.OnClickListener() { // from class: com.miui.gallery.vlog.template.TemplateMenuFragment$$ExternalSyntheticLambda0
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            TemplateMenuFragment.$r8$lambda$eA2jNVcQBThAiXI30_tVIyr7Nxc(TemplateMenuFragment.this, view);
        }
    };
    public OnItemClickListener mItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.vlog.template.TemplateMenuFragment.1
        {
            TemplateMenuFragment.this = this;
        }

        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            return TemplateMenuFragment.this.performTemplateItemClick(recyclerView, i, true);
        }
    };
    public ResourceDownloadManager.IDownloadTaskListener mIDownloadTaskListener = new ResourceDownloadManager.IDownloadTaskListener<VlogResource>() { // from class: com.miui.gallery.vlog.template.TemplateMenuFragment.2
        {
            TemplateMenuFragment.this = this;
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandStart(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(18);
                TemplateMenuFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("TemplateMenuFragment", "download start: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandSuccess(VlogResource vlogResource, int i) {
            if (vlogResource instanceof TemplateResource) {
                vlogResource.setDownloadState(0);
                TemplateMenuFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("TemplateMenuFragment", "download success: %s", vlogResource.getLabel());
                if (TemplateMenuFragment.this.mAdapter == null || TemplateMenuFragment.this.mMenuPresenter == null || TemplateMenuFragment.this.mTargetPosition != i || ((TemplateMenuPresenter) TemplateMenuFragment.this.mMenuPresenter).isBuildingTemplate() || TemplateMenuFragment.this.mMenuPresenter.isSaving()) {
                    return;
                }
                TemplateMenuFragment.this.mAdapter.setSelection(TemplateMenuFragment.this.mTargetPosition);
                ((TemplateMenuPresenter) TemplateMenuFragment.this.mMenuPresenter).loadTemplateFiles((TemplateResource) vlogResource);
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandFail(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(20);
                TemplateMenuFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("TemplateMenuFragment", "download fail: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onTaskCancel(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                int downloadState = vlogResource.getDownloadState();
                if (downloadState != 0 && downloadState != 17) {
                    vlogResource.setDownloadState(20);
                    TemplateMenuFragment.this.notifyDateSetChanged(i);
                }
                DefaultLogger.d("TemplateMenuFragment", "download cancel: %s", vlogResource.getLabel());
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$eA2jNVcQBThAiXI30_tVIyr7Nxc(TemplateMenuFragment templateMenuFragment, View view) {
        templateMenuFragment.lambda$new$0(view);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R$layout.vlog_menu_template_layout, (ViewGroup) null);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_template_operation_layout, (ViewGroup) null, false);
        this.mOperationView = frameLayout;
        FrameLayout frameLayout2 = (FrameLayout) frameLayout.findViewById(R$id.play_layout);
        this.mPlayLayout = frameLayout2;
        frameLayout2.setContentDescription(getResources().getString(R$string.vlog_talkback_view_play));
        this.mPlayLayout.setOnClickListener(this.mPlayViewClickListener);
        FolmeUtil.setCustomTouchAnim(this.mPlayLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
        this.mPlayView = (ImageView) this.mOperationView.findViewById(R$id.play_view);
        this.mRecyclerView = (SimpleRecyclerView) getViewById(R$id.recycler_view);
        this.mZipFileConfig = new TemplateZipFileConfig();
        ResourceDownloadManager resourceDownloadManager = new ResourceDownloadManager(this.mContext, getFragmentManager(), this.mZipFileConfig);
        this.mResourceDownloadManager = resourceDownloadManager;
        resourceDownloadManager.setDownloadTaskListener(this.mIDownloadTaskListener);
        this.mIVlogView.setTopView(this.mOperationView);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getOperationView() {
        return this.mOperationView;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter */
    public TemplateMenuPresenter mo1801createPresenter() {
        return new TemplateMenuPresenter(this.mContext, this);
    }

    @Override // com.miui.gallery.vlog.template.TemplateMenuContract$ITemplateMenuView
    public void loadRecyclerView(List<TemplateResource> list, int i) {
        if (BaseMiscUtil.isValid(list)) {
            CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
            customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
            customScrollerLinearLayoutManager.setOrientation(0);
            this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
            this.mRecyclerView.setEnableItemClickWhileSettling(true);
            if (this.mRecyclerView.getItemDecorationCount() == 0) {
                this.mRecyclerView.addItemDecoration(new BlankDivider(getResources(), R$dimen.vlog_common_menu_recyclerview_item_gap));
            }
            TemplateAdapter templateAdapter = new TemplateAdapter(this.mContext, list);
            this.mAdapter = templateAdapter;
            this.mRecyclerView.setAdapter(templateAdapter);
            this.mAdapter.setOnItemClickListener(this.mItemClickListener);
            this.mAdapter.setSelection(-1);
            if (getMiVideoSdkManager().isTimelineFirstStartCompleted()) {
                performTemplateItemClick(this.mRecyclerView, i, true);
            } else {
                postponePerformTemplateItemClick(i);
            }
            this.mRecyclerView.smoothScrollToPosition(i);
            updatePlayViewState(true);
        }
    }

    public final void postponePerformTemplateItemClick(int i) {
        DefaultLogger.d("TemplateMenuFragment", "postponePerformTemplateItemClick");
        this.mTemplateItemClickPostponed = true;
        this.mPostponedPosition = i;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackStopped() {
        if (!((TemplateMenuPresenter) this.mMenuPresenter).isBuildingTemplate() && !this.mMenuPresenter.isPlaying()) {
            updatePlayViewState(false);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackEOF() {
        if (!((TemplateMenuPresenter) this.mMenuPresenter).isBuildingTemplate() && !this.mMenuPresenter.isPlaying()) {
            updatePlayViewState(false);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onTimelineStarted() {
        super.onTimelineStarted();
        DefaultLogger.d("TemplateMenuFragment", "onTimelineStarted");
        doPostponedItemClick();
    }

    public final void doPostponedItemClick() {
        SimpleRecyclerView simpleRecyclerView;
        if (!this.mTemplateItemClickPostponed || (simpleRecyclerView = this.mRecyclerView) == null) {
            return;
        }
        performTemplateItemClick(simpleRecyclerView, this.mPostponedPosition, false);
        this.mTemplateItemClickPostponed = false;
    }

    @Override // com.miui.gallery.vlog.template.TemplateMenuContract$ITemplateMenuView
    public void updatePlayViewState(boolean z) {
        if (this.mPlayView.isSelected() != z) {
            this.mPlayView.setSelected(z);
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        if (this.mMenuPresenter.isPlaying()) {
            this.mPlayLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_pause));
            ((TemplateMenuPresenter) this.mMenuPresenter).pause();
            return;
        }
        this.mPlayLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_play));
        ((TemplateMenuPresenter) this.mMenuPresenter).play();
    }

    public final boolean performTemplateItemClick(RecyclerView recyclerView, int i, boolean z) {
        if (i != this.mAdapter.getSelection() && !((TemplateMenuPresenter) this.mMenuPresenter).isBuildingTemplate()) {
            this.mTargetPosition = i;
            if (z) {
                recyclerView.smoothScrollToPosition(i);
            }
            TemplateResource item = this.mAdapter.getItem(i);
            if (item.isExtra()) {
                if (item.isDownloaded()) {
                    this.mAdapter.setSelection(i);
                    ((TemplateMenuPresenter) this.mMenuPresenter).loadTemplateFiles(item);
                } else {
                    this.mResourceDownloadManager.createDownloadCommand(item, i);
                }
            } else if (item.isNone()) {
                this.mAdapter.setSelection(i);
                ((TemplateMenuPresenter) this.mMenuPresenter).loadNoneTemplate();
            }
            VlogStatUtils.statEvent("nav", item.getNameKey());
            return true;
        }
        return false;
    }

    public final void notifyDateSetChanged(int i) {
        TemplateAdapter templateAdapter = this.mAdapter;
        if (templateAdapter != null) {
            templateAdapter.notifyItemChanged(i, 1);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            updatePlayViewState(this.mMenuPresenter.isPlaying());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        TemplateAdapter templateAdapter = this.mAdapter;
        if (templateAdapter != null) {
            templateAdapter.setOnItemClickListener(null);
            this.mAdapter = null;
        }
        ResourceDownloadManager resourceDownloadManager = this.mResourceDownloadManager;
        if (resourceDownloadManager != null) {
            resourceDownloadManager.cancel();
        }
        this.mMenuPresenter.destroy();
    }
}
