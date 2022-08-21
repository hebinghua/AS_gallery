package com.miui.gallery.vlog.caption;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.base.net.VlogResource;
import com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager;
import com.miui.gallery.vlog.entity.CaptionStyleData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.ICaptionManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoCaptionManager;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.io.File;
import java.util.List;

/* loaded from: classes2.dex */
public class CaptionStyleFragment extends MenuFragment implements View.OnClickListener, CaptionStyleContract$ICaptionStyleView {
    public CaptionStyleAdapter mAdapter;
    public int mCachedStylePosition;
    public Callback mCallback;
    public ImageView mCancel;
    public ICaptionManager mCaptionManager;
    public CaptionStyleZipFileConfig mCaptionStyleZipFileConfig;
    public CustomScrollerLinearLayoutManager mLinearLayoutManager;
    public MiVideoSdkManager mNvSdkManager;
    public ImageView mOk;
    public CaptionStylePresenter mPresenter;
    public SimpleRecyclerView mRecyclerView;
    public ResourceDownloadManager mResourceDownloadManager;
    public int mSelectedStylePosition;
    public ViewTreeObserver.OnGlobalLayoutListener mRecyclerViewLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.caption.CaptionStyleFragment.1
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (CaptionStyleFragment.this.getRecyclerViewContentWidth() > CaptionStyleFragment.this.mRecyclerView.getWidth()) {
                CaptionStyleFragment.this.mLinearLayoutManager.setScrollEnable(true);
            } else {
                CaptionStyleFragment.this.mLinearLayoutManager.setScrollEnable(false);
            }
        }
    };
    public OnItemClickListener mItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.vlog.caption.CaptionStyleFragment.2
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            CaptionStyleFragment.this.mAdapter.setSelection(i);
            recyclerView.smoothScrollToPosition(i);
            CaptionStyleData itemData = CaptionStyleFragment.this.mAdapter.getItemData(i);
            if (itemData == null) {
                return false;
            }
            if (itemData.isDownloaded()) {
                CaptionStyleFragment.this.applyStyle(itemData);
                CaptionStyleFragment.this.mSelectedStylePosition = i;
                if (CaptionStyleFragment.this.mCallback == null) {
                    return true;
                }
                CaptionStyleFragment.this.mCallback.onApplyStyle();
                return true;
            }
            CaptionStyleFragment.this.mResourceDownloadManager.createDownloadCommand(itemData, i);
            return true;
        }
    };
    public ResourceDownloadManager.IDownloadTaskListener mIDownloadTaskListener = new ResourceDownloadManager.IDownloadTaskListener<VlogResource>() { // from class: com.miui.gallery.vlog.caption.CaptionStyleFragment.3
        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandStart(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(18);
                CaptionStyleFragment.this.notifyDateSetChanged(i);
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandSuccess(VlogResource vlogResource, int i) {
            vlogResource.setDownloadState(0);
            CaptionStyleFragment.this.notifyDateSetChanged(i);
            CaptionStyleFragment.this.applyStyle((CaptionStyleData) vlogResource);
            if (CaptionStyleFragment.this.mCallback != null) {
                CaptionStyleFragment.this.mCallback.onApplyStyle();
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandFail(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(20);
                CaptionStyleFragment.this.notifyDateSetChanged(i);
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onTaskCancel(VlogResource vlogResource, int i) {
            int downloadState;
            if (vlogResource == null || (downloadState = vlogResource.getDownloadState()) == 0 || downloadState == 17) {
                return;
            }
            vlogResource.setDownloadState(20);
            CaptionStyleFragment.this.notifyDateSetChanged(i);
        }
    };

    /* loaded from: classes2.dex */
    public interface Callback {
        void onApplyStyle();

        void onCancel();

        void onSave();

        void onUpdateProgressView(boolean z);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public boolean isSetTopMenuView() {
        return false;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R$layout.vlog_caption_style_layout, viewGroup, false);
        changeTitleViewStatus(true);
        return inflate;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        this.mRecyclerView = (SimpleRecyclerView) view.findViewById(R$id.recycler_view);
        this.mCancel = (ImageView) view.findViewById(R$id.cancel);
        this.mOk = (ImageView) view.findViewById(R$id.ok);
        this.mCancel.setOnClickListener(this);
        this.mOk.setOnClickListener(this);
        FolmeUtilsEditor.animButton(this.mOk);
        FolmeUtilsEditor.animButton(this.mCancel);
        this.mCaptionStyleZipFileConfig = new CaptionStyleZipFileConfig();
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter */
    public BasePresenter mo1801createPresenter() {
        CaptionStylePresenter captionStylePresenter = new CaptionStylePresenter(getActivity(), this);
        this.mPresenter = captionStylePresenter;
        return captionStylePresenter;
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mNvSdkManager = ((VlogModel) VlogUtils.getViewModel(getActivity(), VlogModel.class)).getSdkManager();
        this.mCaptionManager = (MiVideoCaptionManager) ((VlogModel) VlogUtils.getViewModel(getActivity(), VlogModel.class)).getSdkManager().getManagerService(1);
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onUpdateProgressView(false);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            changeTitleViewStatus(true);
            int determineStylePositon = determineStylePositon();
            this.mSelectedStylePosition = determineStylePositon;
            this.mCachedStylePosition = determineStylePositon;
            CaptionStyleAdapter captionStyleAdapter = this.mAdapter;
            if (captionStyleAdapter != null) {
                captionStyleAdapter.setSelection(determineStylePositon);
                this.mRecyclerView.scrollToPosition(this.mCachedStylePosition);
            }
            Callback callback = this.mCallback;
            if (callback == null) {
                return;
            }
            callback.onUpdateProgressView(false);
            return;
        }
        Callback callback2 = this.mCallback;
        if (callback2 == null) {
            return;
        }
        callback2.onUpdateProgressView(false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        changeTitleViewStatus(false);
        super.onDestroyView();
        this.mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this.mRecyclerViewLayoutListener);
    }

    public final void changeTitleViewStatus(boolean z) {
        if (z) {
            getIVlogView().hideApplyView();
            getIVlogView().showCustomTitleView(getTitleViewWithCustomTitle(getContext().getResources().getString(R$string.vlog_caption_style)));
            return;
        }
        getIVlogView().hideCustomTitleView();
        getIVlogView().showApplyView();
    }

    public final int determineStylePositon() {
        ICaptionManager iCaptionManager = this.mCaptionManager;
        if (iCaptionManager == null || this.mAdapter == null) {
            return 0;
        }
        String singleStyleId = iCaptionManager.getSingleStyleId();
        if (TextUtils.isEmpty(singleStyleId)) {
            return 0;
        }
        for (int i = 0; i < this.mAdapter.getItemCount(); i++) {
            CaptionStyleData itemData = this.mAdapter.getItemData(i);
            if (itemData != null && !TextUtils.isEmpty(itemData.label) && itemData.label.equals(singleStyleId)) {
                return i;
            }
        }
        return 0;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.miui.gallery.vlog.caption.CaptionStyleContract$ICaptionStyleView
    public void loadRecyclerView(List<CaptionStyleData> list) {
        DefaultLogger.d("CaptionStyleFragment", "loadRecyclerView");
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        this.mLinearLayoutManager = customScrollerLinearLayoutManager;
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        this.mLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(this.mLinearLayoutManager);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        if (this.mRecyclerView.getItemDecorationCount() == 0) {
            this.mRecyclerView.addItemDecoration(new BlankDivider(getResources(), R$dimen.vlog_menu_template_item_gap));
        }
        refreshData(list);
        CaptionStyleAdapter captionStyleAdapter = new CaptionStyleAdapter(getActivity(), list);
        this.mAdapter = captionStyleAdapter;
        this.mRecyclerView.setAdapter(captionStyleAdapter);
        this.mAdapter.setOnItemClickListener(this.mItemClickListener);
        ResourceDownloadManager resourceDownloadManager = new ResourceDownloadManager(getActivity(), getFragmentManager(), this.mCaptionStyleZipFileConfig);
        this.mResourceDownloadManager = resourceDownloadManager;
        resourceDownloadManager.setDownloadTaskListener(this.mIDownloadTaskListener);
        int determineStylePositon = determineStylePositon();
        this.mSelectedStylePosition = determineStylePositon;
        this.mAdapter.setSelection(determineStylePositon);
        this.mRecyclerView.scrollToPosition(this.mCachedStylePosition);
        this.mCachedStylePosition = this.mSelectedStylePosition;
        this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this.mRecyclerViewLayoutListener);
    }

    public final int getRecyclerViewContentWidth() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.vlog_caption_common_recycler_item_size);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R$dimen.vlog_menu_template_item_gap);
        int itemCount = this.mAdapter.getItemCount();
        return (dimensionPixelSize * itemCount) + ((itemCount + 1) * dimensionPixelSize2);
    }

    public final void hideSelf() {
        getFragmentManager().beginTransaction().hide(this).commit();
    }

    public boolean onCallPressed() {
        changeTitleViewStatus(false);
        CaptionStyleAdapter captionStyleAdapter = this.mAdapter;
        if (captionStyleAdapter != null) {
            applyStyle(captionStyleAdapter.getItemData(this.mCachedStylePosition));
        }
        hideSelf();
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCancel();
            return true;
        }
        return true;
    }

    public void onSavePressed() {
        changeTitleViewStatus(false);
        hideSelf();
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onSave();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.cancel) {
            onCallPressed();
        } else if (view.getId() != R$id.ok) {
        } else {
            onSavePressed();
        }
    }

    public final void applyStyle(CaptionStyleData captionStyleData) {
        if (captionStyleData == null) {
            return;
        }
        CaptionStyleData.install(this.mNvSdkManager, captionStyleData, "CaptionStyleFragment");
        this.mCaptionManager.applyCaptionStyle(captionStyleData.getLabel(), captionStyleData.assetDoubleId);
    }

    public final void notifyDateSetChanged(int i) {
        CaptionStyleAdapter captionStyleAdapter = this.mAdapter;
        if (captionStyleAdapter != null) {
            captionStyleAdapter.notifyItemChanged(i, 1);
        }
    }

    public final void refreshData(List<CaptionStyleData> list) {
        for (CaptionStyleData captionStyleData : list) {
            if (captionStyleData != null) {
                if (new File(VlogConfig.CAPTION_ASSET_PATH + File.separator + captionStyleData.assetSingleName).exists()) {
                    captionStyleData.setDownloadState(17);
                }
            }
        }
    }
}
