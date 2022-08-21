package com.miui.gallery.vlog.caption;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.ui.view.switchview.SlideSwitchView2;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
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
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.caption.HeaderTailAdapter;
import com.miui.gallery.vlog.caption.dialog.HeaderTailDoubleDialogFragment;
import com.miui.gallery.vlog.caption.dialog.HeaderTailSingleDialogFragment;
import com.miui.gallery.vlog.entity.HeaderTailData;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback;
import com.miui.gallery.vlog.sdk.callbacks.SimplePlaybackCallback;
import com.miui.gallery.vlog.sdk.manager.MiVideoHeaderTailManager;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.miui.gallery.vlog.tools.VlogOrientationProvider;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class HeaderTailFragment extends MenuFragment implements View.OnClickListener, HeaderTailContract$ITitleStyleView {
    public HeaderTailAdapter mAdapter;
    public String mCachedCustomContent;
    public int mCachedPosition;
    public Callback mCallback;
    public ImageView mCancel;
    public ViewGroup mFragmentContainer;
    public TextView mHeaderButton;
    public HeaderTailDoubleDialogFragment mHeaderTailDoubleDialogFragment;
    public MiVideoHeaderTailManager mHeaderTailManager;
    public HeaderTailSingleDialogFragment mHeaderTailSingleDialogFragment;
    public HeaderTailZipFileConfig mHeaderTailZipFileConfig;
    public boolean mIsProcessingData;
    public MiVideoSdkManager mMiVideoSdkManager;
    public ImageView mOk;
    public View mOperationView;
    public FrameLayout mPlayLayout;
    public ImageView mPlayView;
    public SimpleRecyclerView mRecyclerView;
    public ResourceDownloadManager mResourceDownloadManager;
    public int mSelectPosition;
    public View mSeparatedView;
    public SlideSwitchView2 mSwitchView;
    public TextView mTailButton;
    public final int ITEM_CUSTOM_POSITION = 1;
    public boolean mCachedHeaderMode = true;
    public int mCurrentTabLayoutIndex = 0;
    public SlideSwitchView2.OnSelectChangeListener mOnSelectChangeListener = new SlideSwitchView2.OnSelectChangeListener() { // from class: com.miui.gallery.vlog.caption.HeaderTailFragment.1
        {
            HeaderTailFragment.this = this;
        }

        @Override // com.miui.gallery.editor.ui.view.switchview.SlideSwitchView2.OnSelectChangeListener
        public void onSelectChanged(int i) {
            if (HeaderTailFragment.this.mCurrentTabLayoutIndex == i) {
                return;
            }
            if (i == 0) {
                HeaderTailFragment.this.mHeaderButton.performClick();
                HeaderTailFragment.this.mSwitchView.setContentDescription(HeaderTailFragment.this.getResources().getString(R$string.vlog_talkback_text_head_tail_choose, HeaderTailFragment.this.getResources().getString(R$string.vlog_film_title)));
            } else if (i == 1) {
                HeaderTailFragment.this.mTailButton.performClick();
                HeaderTailFragment.this.mSwitchView.setContentDescription(HeaderTailFragment.this.getResources().getString(R$string.vlog_talkback_text_head_tail_choose, HeaderTailFragment.this.getResources().getString(R$string.vlog_end_of_film)));
            }
            HeaderTailFragment.this.mCurrentTabLayoutIndex = i;
        }
    };
    public PlaybackCallback mPlaybackCallback = new SimplePlaybackCallback() { // from class: com.miui.gallery.vlog.caption.HeaderTailFragment.2
        {
            HeaderTailFragment.this = this;
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackEOF() {
            HeaderTailFragment.this.playEffect();
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.SimplePlaybackCallback, com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackTimelinePositionMicro(long j) {
            super.onPlaybackTimelinePositionMicro(j);
            HeaderTailFragment.this.getIVlogView().setPlayViewProgress(j);
        }
    };
    public HeaderTailAdapter.OnItemSelectListener mOnItemSelectListener = new HeaderTailAdapter.OnItemSelectListener() { // from class: com.miui.gallery.vlog.caption.HeaderTailFragment$$ExternalSyntheticLambda0
        @Override // com.miui.gallery.vlog.caption.HeaderTailAdapter.OnItemSelectListener
        public final void itemSelected(int i) {
            HeaderTailFragment.m1778$r8$lambda$d_Jn3CsE6LnsaQaSHu0c568dz4(HeaderTailFragment.this, i);
        }
    };
    public OnItemClickListener mItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.vlog.caption.HeaderTailFragment.3
        {
            HeaderTailFragment.this = this;
        }

        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            if (!HeaderTailFragment.this.mIsProcessingData && HeaderTailFragment.this.mAdapter != null) {
                recyclerView.smoothScrollToPosition(i);
                HeaderTailData itemData = HeaderTailFragment.this.mAdapter.getItemData(i);
                if (itemData == null) {
                    return false;
                }
                if (itemData.isDownloaded()) {
                    if (itemData.type.equals("type_none")) {
                        HeaderTailFragment.this.printLogStart();
                        HeaderTailFragment.this.mHeaderTailManager.removeHeaderTail();
                        HeaderTailFragment.this.playEffect();
                        HeaderTailFragment.this.mSelectPosition = i;
                        HeaderTailFragment.this.mAdapter.setSelection(i);
                    } else {
                        HeaderTailFragment.this.performSelectedItem(itemData, i, true);
                    }
                } else if (itemData.type.equals("type_none")) {
                    HeaderTailFragment.this.printLogStart();
                    HeaderTailFragment.this.mAdapter.setSelection(i);
                    HeaderTailFragment.this.mHeaderTailManager.removeHeaderTail();
                    HeaderTailFragment.this.mSelectPosition = i;
                    HeaderTailFragment.this.playEffect();
                } else if (itemData.type.equals("type_custom")) {
                    if (HeaderTailFragment.this.mAdapter.getSelection() == i || TextUtils.isEmpty(HeaderTailFragment.this.getCustomItemContent())) {
                        HeaderTailFragment.this.openCustomHeaderTail();
                    } else {
                        ((HeaderTailPresenter) HeaderTailFragment.this.mMenuPresenter).setCustomHeaderTail(HeaderTailFragment.this.isHeaderMode(), HeaderTailFragment.this.getCustomItemContent());
                        HeaderTailFragment.this.playEffect();
                        HeaderTailFragment.this.mAdapter.setSelection(i);
                        HeaderTailFragment.this.mSelectPosition = i;
                    }
                } else {
                    HeaderTailFragment.this.mResourceDownloadManager.createDownloadCommand(itemData, i);
                }
                VlogStatUtils.statEvent("head_tail", itemData.label);
                return true;
            }
            return false;
        }
    };
    public HeaderTailDoubleDialogFragment.EditorCallback mDoubleEditorCallback = new HeaderTailDoubleDialogFragment.EditorCallback() { // from class: com.miui.gallery.vlog.caption.HeaderTailFragment.4
        @Override // com.miui.gallery.vlog.caption.dialog.HeaderTailDoubleDialogFragment.EditorCallback
        public void onHeaderTailEditorFinished(String str, long j) {
        }

        {
            HeaderTailFragment.this = this;
        }

        @Override // com.miui.gallery.vlog.caption.dialog.HeaderTailDoubleDialogFragment.EditorCallback
        public void onHeaderTailUpdateFinished(String str, String str2) {
            HeaderTailData itemData;
            ((HeaderTailPresenter) HeaderTailFragment.this.mMenuPresenter).setHeaderTailText(str, str2);
            if (HeaderTailFragment.this.mAdapter != null && (itemData = HeaderTailFragment.this.mAdapter.getItemData(HeaderTailFragment.this.mAdapter.getSelection())) != null) {
                itemData.setAutoContents(str, str2);
            }
            HeaderTailFragment.this.playEffect();
        }

        @Override // com.miui.gallery.vlog.caption.dialog.HeaderTailDoubleDialogFragment.EditorCallback
        public void onCancel() {
            HeaderTailFragment.this.resume();
        }
    };
    public HeaderTailSingleDialogFragment.EditorCallback mSingleEditorCallback = new HeaderTailSingleDialogFragment.EditorCallback() { // from class: com.miui.gallery.vlog.caption.HeaderTailFragment.5
        {
            HeaderTailFragment.this = this;
        }

        @Override // com.miui.gallery.vlog.caption.dialog.HeaderTailSingleDialogFragment.EditorCallback
        public void onCancel() {
            HeaderTailFragment.this.resume();
        }

        @Override // com.miui.gallery.vlog.caption.dialog.HeaderTailSingleDialogFragment.EditorCallback
        public void onTitleSingleEditorFinished(String str, long j) {
            HeaderTailFragment.this.onCustomItemSelected();
            ((HeaderTailPresenter) HeaderTailFragment.this.mMenuPresenter).setCustomHeaderTail(j == 0, str);
            HeaderTailFragment.this.onTitleSingleFinished(str);
        }

        @Override // com.miui.gallery.vlog.caption.dialog.HeaderTailSingleDialogFragment.EditorCallback
        public void onTitleSingleUpdateFinished(String str) {
            ((HeaderTailPresenter) HeaderTailFragment.this.mMenuPresenter).setHeaderTailText(str, null);
            HeaderTailFragment.this.onTitleSingleFinished(str);
        }
    };
    public ResourceDownloadManager.IDownloadTaskListener mIDownloadTaskListener = new ResourceDownloadManager.IDownloadTaskListener<VlogResource>() { // from class: com.miui.gallery.vlog.caption.HeaderTailFragment.6
        {
            HeaderTailFragment.this = this;
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandStart(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(18);
                HeaderTailFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("HeaderTailFragment", "download start: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandSuccess(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(17);
                HeaderTailFragment.this.notifyDateSetChanged(i);
                HeaderTailFragment.this.performSelectedItem((HeaderTailData) vlogResource, i, true);
                DefaultLogger.d("HeaderTailFragment", "download success: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandFail(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(20);
                HeaderTailFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("HeaderTailFragment", "download fail: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onTaskCancel(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                int downloadState = vlogResource.getDownloadState();
                if (downloadState != 0 && downloadState != 17) {
                    vlogResource.setDownloadState(20);
                    HeaderTailFragment.this.notifyDateSetChanged(i);
                }
                DefaultLogger.d("HeaderTailFragment", "download cancel: %s", vlogResource.getLabel());
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface Callback {
        void onCancel();

        void onSave();
    }

    /* renamed from: $r8$lambda$d_Jn-3CsE6LnsaQaSHu0c568dz4 */
    public static /* synthetic */ void m1778$r8$lambda$d_Jn3CsE6LnsaQaSHu0c568dz4(HeaderTailFragment headerTailFragment, int i) {
        headerTailFragment.lambda$new$0(i);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.mFragmentContainer = viewGroup;
        this.mHeaderTailZipFileConfig = new HeaderTailZipFileConfig();
        ResourceDownloadManager resourceDownloadManager = new ResourceDownloadManager(getActivity(), getFragmentManager(), this.mHeaderTailZipFileConfig);
        this.mResourceDownloadManager = resourceDownloadManager;
        resourceDownloadManager.setDownloadTaskListener(this.mIDownloadTaskListener);
        MiVideoSdkManager miVideoSdkManager = getMiVideoSdkManager();
        this.mMiVideoSdkManager = miVideoSdkManager;
        this.mHeaderTailManager = (MiVideoHeaderTailManager) miVideoSdkManager.getManagerService(8);
        this.mMiVideoSdkManager.addPlayCallback(this.mPlaybackCallback);
        View inflate = layoutInflater.inflate(isSingleVideoEdit() ? R$layout.vlog_caption_header_tail_single_edit_layout : R$layout.vlog_caption_header_tail_layout, viewGroup, false);
        changeTitleViewStatus(true);
        return inflate;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        this.mHeaderButton = (TextView) view.findViewById(R$id.vlog_caption_header);
        this.mTailButton = (TextView) view.findViewById(R$id.vlog_caption_tail);
        this.mCancel = (ImageView) view.findViewById(R$id.cancel);
        this.mOk = (ImageView) view.findViewById(R$id.ok);
        this.mHeaderButton.setOnClickListener(this);
        this.mTailButton.setOnClickListener(this);
        this.mCancel.setOnClickListener(this);
        this.mOk.setOnClickListener(this);
        FolmeUtilsEditor.animButton(this.mCancel);
        FolmeUtilsEditor.animButton(this.mOk);
        initDirectionView();
        initRecyclerView();
        initTopOperationView();
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        if (isSingleVideoEdit()) {
            return super.onCreateAnimator(i, z, i2);
        }
        return null;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter */
    public BasePresenter mo1801createPresenter() {
        return new HeaderTailPresenter(getActivity(), this);
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (!isSingleVideoEdit()) {
            playEffect();
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getOperationView() {
        return this.mOperationView;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getSeparatedView() {
        return this.mSeparatedView;
    }

    public final void initDirectionView() {
        if (isLandscape()) {
            View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_caption_title_top_view, this.mFragmentContainer, false);
            this.mSeparatedView = inflate;
            this.mPlayLayout = (FrameLayout) inflate.findViewById(R$id.play_layout);
            this.mPlayView = (ImageView) this.mSeparatedView.findViewById(R$id.play_view);
            initSlideSwitchView(this.mSeparatedView);
            return;
        }
        View inflate2 = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_caption_title_top_view, this.mFragmentContainer, false);
        this.mOperationView = inflate2;
        this.mPlayLayout = (FrameLayout) inflate2.findViewById(R$id.play_layout);
        this.mPlayView = (ImageView) this.mOperationView.findViewById(R$id.play_view);
        initSlideSwitchView(this.mOperationView);
    }

    public final void initRecyclerView() {
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) getViewById(R$id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) simpleRecyclerView.getLayoutParams();
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        int i = R$dimen.vlog_common_menu_recyclerview_item_gap;
        if (VlogUtils.isLandscape(getContext())) {
            customScrollerLinearLayoutManager.setOrientation(1);
            ((ViewGroup.MarginLayoutParams) layoutParams).width = (int) getResources().getDimension(R$dimen.vlog_caption_common_recycler_item_size);
            ((ViewGroup.MarginLayoutParams) layoutParams).height = -1;
            layoutParams.setMarginStart(getResources().getDimensionPixelSize(R$dimen.vlog_filter_recycler_view_padding_top));
        } else {
            customScrollerLinearLayoutManager.setOrientation(0);
            ((ViewGroup.MarginLayoutParams) layoutParams).width = -1;
            ((ViewGroup.MarginLayoutParams) layoutParams).height = (int) getResources().getDimension(R$dimen.vlog_caption_common_recycler_item_size);
            layoutParams.setMargins(0, isSingleVideoEdit() ? getResources().getDimensionPixelSize(R$dimen.vlog_filter_recycler_view_padding_top) : 0, 0, isSingleVideoEdit() ? 0 : getResources().getDimensionPixelSize(R$dimen.vlog_caption_head_tail_content_margin_bottom));
        }
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        if (this.mRecyclerView.getItemDecorationCount() == 0) {
            BlankDivider blankDivider = new BlankDivider(getResources(), i);
            blankDivider.setOrientationProvider(new VlogOrientationProvider());
            this.mRecyclerView.addItemDecoration(blankDivider);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public boolean isSetTopMenuView() {
        return isSingleVideoEdit();
    }

    public final void initTopOperationView() {
        this.mPlayLayout.setOnClickListener(this);
        FolmeUtil.setCustomTouchAnim(this.mPlayLayout, new AnimParams.Builder().setAlpha(0.6f).setScale(1.0f).build(), null, null, true);
    }

    public final void initSlideSwitchView(View view) {
        this.mCurrentTabLayoutIndex = this.mHeaderTailManager.hasTail() ? 1 : 0;
        SlideSwitchView2 slideSwitchView2 = (SlideSwitchView2) view.findViewById(R$id.slide_switch_view);
        this.mSwitchView = slideSwitchView2;
        Context context = this.mContext;
        int i = R$string.vlog_film_title;
        slideSwitchView2.initTexts(context.getString(i), this.mContext.getString(R$string.vlog_end_of_film));
        this.mSwitchView.setSelected(this.mCurrentTabLayoutIndex);
        this.mSwitchView.setContentDescription(getResources().getString(R$string.vlog_talkback_text_head_tail_choose, getResources().getString(i)));
        this.mSwitchView.setOnSelectChangeListener(this.mOnSelectChangeListener);
    }

    public final void changeTitleViewStatus(boolean z) {
        if (!isSingleVideoEdit()) {
            if (z) {
                getIVlogView().hideApplyView();
                getIVlogView().showCustomTitleView(getTitleViewWithCustomTitle(getContext().getResources().getString(R$string.vlog_caption_title)));
                return;
            }
            getIVlogView().hideCustomTitleView();
            getIVlogView().showApplyView();
        }
    }

    @Override // com.miui.gallery.vlog.caption.HeaderTailContract$ITitleStyleView
    public void loadRecyclerView(ArrayList<HeaderTailData> arrayList) {
        if (getActivity() == null) {
            return;
        }
        HeaderTailAdapter headerTailAdapter = new HeaderTailAdapter(getActivity(), arrayList);
        this.mAdapter = headerTailAdapter;
        this.mRecyclerView.setAdapter(headerTailAdapter);
        this.mAdapter.setOnItemClickListener(this.mItemClickListener);
        this.mAdapter.setOnItemSelectListener(this.mOnItemSelectListener);
        updateHeaderTailBtn(!this.mHeaderTailManager.hasTail());
        updateSelectItem();
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (z) {
            this.mMiVideoSdkManager.removePlayCallback(this.mPlaybackCallback);
            getIVlogView().setPlayProgressEnable(true);
            getIVlogView().showProgressView();
        } else {
            this.mMiVideoSdkManager.addPlayCallback(this.mPlaybackCallback);
            updateHeaderTailBtn(this.mHeaderTailManager.getHeaderTailStatus() != 2);
            updatePlayViewState(this.mMiVideoSdkManager.isPlay());
        }
        if (isSingleVideoEdit() || z) {
            return;
        }
        changeTitleViewStatus(true);
        this.mIsProcessingData = true;
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter == null) {
            return;
        }
        ((HeaderTailPresenter) this.mMenuPresenter).updateDataList(headerTailAdapter.getDataList());
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackStopped() {
        if (!this.mMiVideoSdkManager.isPlay()) {
            updatePlayViewState(false);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackEOF() {
        if (!this.mMiVideoSdkManager.isPlay()) {
            updatePlayViewState(false);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        changeTitleViewStatus(false);
        this.mMiVideoSdkManager.removePlayCallback(this.mPlaybackCallback);
        super.onDestroyView();
    }

    @Override // com.miui.gallery.vlog.caption.HeaderTailContract$ITitleStyleView
    public void onRemoveHeadTail() {
        this.mCachedPosition = 0;
        this.mSelectPosition = 0;
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter != null) {
            headerTailAdapter.setSelection(0);
        }
    }

    @Override // com.miui.gallery.vlog.caption.HeaderTailContract$ITitleStyleView
    public void updatePlayViewState(boolean z) {
        if (this.mPlayView.isSelected() != z) {
            this.mPlayView.setSelected(z);
        }
    }

    @Override // com.miui.gallery.vlog.caption.HeaderTailContract$ITitleStyleView
    public void updateSelectItem() {
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter == null) {
            return;
        }
        this.mIsProcessingData = false;
        int findIndexByLabel = ((HeaderTailPresenter) this.mMenuPresenter).findIndexByLabel(headerTailAdapter.getDataList());
        if (findIndexByLabel == -1) {
            findIndexByLabel = this.mCachedPosition;
            this.mAdapter.setSelection(findIndexByLabel);
            if (findIndexByLabel == 1) {
                playEffect();
            }
        } else if (isSingleVideoEdit()) {
            this.mAdapter.setSelection(findIndexByLabel);
            this.mSelectPosition = findIndexByLabel;
        } else {
            playEffect();
        }
        this.mRecyclerView.scrollToPosition(findIndexByLabel);
        this.mCachedPosition = findIndexByLabel;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public final void hideSelf() {
        getFragmentManager().beginTransaction().hide(this).commit();
    }

    public final boolean isHeaderMode() {
        if (isSingleVideoEdit()) {
            return this.mCurrentTabLayoutIndex == 0;
        }
        return this.mHeaderButton.isSelected();
    }

    public final void playEffect() {
        if (!isSingleVideoEdit()) {
            if (this.mHeaderTailManager.hasHeaderTail()) {
                getIVlogView().setPlayProgressEnable(false);
                getIVlogView().hideProgressView();
            } else {
                getIVlogView().setPlayProgressEnable(true);
                getIVlogView().showProgressView();
            }
        }
        long inPoint = this.mHeaderTailManager.getInPoint();
        long outPoint = this.mHeaderTailManager.getOutPoint();
        if (isSingleVideoEdit()) {
            DefaultLogger.d("HeaderTailFragment", "playEffect seek to:" + inPoint);
            this.mMiVideoSdkManager.seek(inPoint);
            this.mMiVideoSdkManager.play();
        } else {
            this.mMiVideoSdkManager.pause();
            this.mMiVideoSdkManager.setTimelineInout(inPoint / 1000, outPoint / 1000, true);
            this.mMiVideoSdkManager.playbackTimeline();
        }
        updatePlayViewState(true);
        printLogEnd();
    }

    public final void resume() {
        this.mMiVideoSdkManager.playbackTimeline();
    }

    public final int resourceType(HeaderTailData headerTailData) {
        if (headerTailData.type.equals("type_single")) {
            return 1;
        }
        if (headerTailData.type.equals("type_double")) {
            return 2;
        }
        if (headerTailData.type.equals("type_fixed")) {
            return 3;
        }
        return headerTailData.type.equals("type_custom") ? 4 : 0;
    }

    public boolean onCancelPressed() {
        changeTitleViewStatus(false);
        this.mMiVideoSdkManager.resetTimelineInout();
        int i = this.mCachedPosition;
        this.mSelectPosition = i;
        applyHeaderTail(this.mCachedHeaderMode, i);
        updateCustomItemContent(this.mCachedCustomContent);
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter != null) {
            headerTailAdapter.setSelection(this.mSelectPosition);
        }
        hideSelf();
        VlogStatUtils.statEvent("head_tail", "cancel");
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCancel();
            return true;
        }
        return true;
    }

    public void onSavePressed() {
        changeTitleViewStatus(false);
        this.mMiVideoSdkManager.pause();
        this.mMiVideoSdkManager.resetTimelineInout();
        this.mCachedPosition = this.mSelectPosition;
        this.mCachedHeaderMode = isHeaderMode();
        this.mCachedCustomContent = getCustomItemContent();
        hideSelf();
        VlogStatUtils.statEvent("head_tail", "ok");
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onSave();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.vlog_caption_header && !isHeaderMode()) {
            updateHeaderTailBtn(true);
            exchangeHeaderTail(true);
            VlogStatUtils.statEvent("head_tail", "header");
        } else if (view.getId() == R$id.vlog_caption_tail && isHeaderMode()) {
            updateHeaderTailBtn(false);
            exchangeHeaderTail(false);
            VlogStatUtils.statEvent("head_tail", "tail");
        } else if (view.getId() == R$id.cancel) {
            onCancelPressed();
        } else if (view.getId() == R$id.ok) {
            onSavePressed();
        } else if (view.getId() != R$id.play_layout) {
        } else {
            ((HeaderTailPresenter) this.mMenuPresenter).doPlayViewClickEvent();
        }
    }

    public final void applyHeaderTail(boolean z, int i) {
        HeaderTailData itemData;
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter == null || (itemData = headerTailAdapter.getItemData(i)) == null) {
            return;
        }
        if (resourceType(itemData) == 0) {
            this.mHeaderTailManager.removeHeaderTail();
        } else if (resourceType(itemData) == 4) {
            this.mHeaderTailManager.setCustomHeaderTail(z, this.mCachedCustomContent);
        } else {
            this.mHeaderTailManager.setHeaderTail(z, itemData.getFolderPath(), resourceType(itemData), itemData.getKey());
            if (!itemData.getAutoContents().isValid()) {
                return;
            }
            ((HeaderTailPresenter) this.mMenuPresenter).setHeaderTailText(itemData.getAutoContents().getContents(), itemData.getAutoContents().getSub());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void updateHeaderTailBtn(boolean z) {
        if (isSingleVideoEdit()) {
            if (z != 0 && this.mSwitchView.getSelected() == 0) {
                return;
            }
            if (z == 0 && this.mSwitchView.getSelected() == 1) {
                return;
            }
            this.mSwitchView.setOnSelectChangeListener(null);
            this.mSwitchView.setSelected(!z);
            this.mSwitchView.setOnSelectChangeListener(this.mOnSelectChangeListener);
            return;
        }
        this.mHeaderButton.setSelected(z);
        this.mTailButton.setSelected(!z ? 1 : 0);
    }

    public final boolean isSingleVideoEdit() {
        Context context = this.mContext;
        if (context == null) {
            return false;
        }
        return ((VlogModel) VlogUtils.getViewModel((FragmentActivity) context, VlogModel.class)).isSingleVideoEdit();
    }

    public final void exchangeHeaderTail(boolean z) {
        this.mHeaderTailManager.exchangeHeaderTail(z);
        playEffect();
    }

    public final void printLogStart() {
        DebugLogUtils.HAS_LOADED_SELECT_HEADTAIL = false;
        DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_HEADTAIL = false;
        DebugLogUtils.startDebugLogSpecialTime("HeaderTailFragment", "vlog applyHeadTail");
    }

    public final void printLogEnd() {
        DebugLogUtils.HAS_LOADED_SELECT_HEADTAIL = true;
    }

    public final boolean isClickSelectedData(int i) {
        return this.mSelectPosition == i;
    }

    public /* synthetic */ void lambda$new$0(int i) {
        if (!isSingleVideoEdit() || !isAdded() || isHidden()) {
            return;
        }
        boolean z = false;
        if (i == 0) {
            MiVideoSdkManager miVideoSdkManager = this.mMiVideoSdkManager;
            if (miVideoSdkManager != null && miVideoSdkManager.isPlay()) {
                z = true;
            }
            updatePlayViewState(z);
            this.mSwitchView.setVisibility(4);
            return;
        }
        this.mSwitchView.setVisibility(0);
    }

    public final void openCustomHeaderTail() {
        if (getFragmentManager().findFragmentByTag("single_header_tail") != null) {
            return;
        }
        if (this.mHeaderTailSingleDialogFragment == null) {
            HeaderTailSingleDialogFragment newInstance = HeaderTailSingleDialogFragment.newInstance();
            this.mHeaderTailSingleDialogFragment = newInstance;
            newInstance.setTitleSingleEditorCallback(this.mSingleEditorCallback);
        }
        this.mHeaderTailSingleDialogFragment.setWordLimit(10);
        HeaderTailData itemData = this.mAdapter.getItemData(1);
        if (itemData != null && itemData.getAutoContents().isValid()) {
            this.mHeaderTailSingleDialogFragment.setContent(itemData.getAutoContents().getContents());
        } else {
            this.mHeaderTailSingleDialogFragment.setContent(null);
        }
        this.mHeaderTailSingleDialogFragment.show(getFragmentManager(), "single_header_tail");
        this.mMiVideoSdkManager.pause();
    }

    public final void performSelectedItem(HeaderTailData headerTailData, int i, boolean z) {
        T t = this.mMenuPresenter;
        if (t == 0 || t.isSaving()) {
            DefaultLogger.d("HeaderTailFragment", "in save progress");
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null) {
            DefaultLogger.d("HeaderTailFragment", "fragmentManager is null");
        } else if (!headerTailData.isDownloaded() && !headerTailData.isDownloadSuccess()) {
        } else {
            if (headerTailData.type.equals("type_single")) {
                Fragment findFragmentByTag = fragmentManager.findFragmentByTag("single_header_tail");
                if (!isClickSelectedData(i) || !z) {
                    printLogStart();
                    this.mHeaderTailManager.setHeaderTail(isHeaderMode(), headerTailData.getFolderPath(), 1, headerTailData.getKey());
                    playEffect();
                } else if (findFragmentByTag != null) {
                    return;
                } else {
                    if (this.mHeaderTailSingleDialogFragment == null) {
                        HeaderTailSingleDialogFragment newInstance = HeaderTailSingleDialogFragment.newInstance();
                        this.mHeaderTailSingleDialogFragment = newInstance;
                        newInstance.setTitleSingleEditorCallback(this.mSingleEditorCallback);
                    }
                    this.mHeaderTailSingleDialogFragment.setWordLimit(headerTailData.mainTitleNumber);
                    this.mHeaderTailSingleDialogFragment.setContent(this.mHeaderTailManager.getHeaderTailText(0));
                    this.mHeaderTailSingleDialogFragment.show(fragmentManager, "single_header_tail");
                    this.mMiVideoSdkManager.pause();
                }
            } else if (headerTailData.type.equals("type_double")) {
                Fragment findFragmentByTag2 = fragmentManager.findFragmentByTag("double_header_tail");
                if (!isClickSelectedData(i) || !z) {
                    printLogStart();
                    this.mHeaderTailManager.setHeaderTail(isHeaderMode(), headerTailData.getFolderPath(), 2, headerTailData.getKey());
                    playEffect();
                } else if (findFragmentByTag2 != null) {
                    return;
                } else {
                    if (this.mHeaderTailDoubleDialogFragment == null) {
                        HeaderTailDoubleDialogFragment newInstance2 = HeaderTailDoubleDialogFragment.newInstance();
                        this.mHeaderTailDoubleDialogFragment = newInstance2;
                        newInstance2.setTitleSubEditorCallback(this.mDoubleEditorCallback);
                    }
                    this.mHeaderTailDoubleDialogFragment.setWordLimit(headerTailData.mainTitleNumber, headerTailData.subTitleNumber);
                    this.mHeaderTailDoubleDialogFragment.setContent(this.mHeaderTailManager.getHeaderTailText(0));
                    this.mHeaderTailDoubleDialogFragment.setSub(this.mHeaderTailManager.getHeaderTailText(1));
                    this.mHeaderTailDoubleDialogFragment.show(fragmentManager, "double_header_tail");
                    this.mMiVideoSdkManager.pause();
                }
            } else if (headerTailData.type.equals("type_fixed") && this.mSelectPosition != i) {
                printLogStart();
                this.mHeaderTailManager.setHeaderTail(isHeaderMode(), headerTailData.getFolderPath(), 3, headerTailData.getKey());
                playEffect();
            }
            HeaderTailAdapter headerTailAdapter = this.mAdapter;
            if (headerTailAdapter != null) {
                headerTailAdapter.setSelection(i);
            }
            this.mSelectPosition = i;
        }
    }

    public final void onTitleSingleFinished(String str) {
        HeaderTailData itemData;
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter != null && (itemData = headerTailAdapter.getItemData(headerTailAdapter.getSelection())) != null) {
            itemData.setAutoContents(str, null);
        }
        playEffect();
    }

    public final String getCustomItemContent() {
        HeaderTailData itemData;
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter == null || (itemData = headerTailAdapter.getItemData(1)) == null || !itemData.type.equals("type_custom")) {
            return null;
        }
        return itemData.getAutoContents().getContents();
    }

    public final void updateCustomItemContent(String str) {
        HeaderTailData itemData;
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter == null || (itemData = headerTailAdapter.getItemData(1)) == null || !itemData.type.equals("type_custom")) {
            return;
        }
        itemData.getAutoContents().setContents(str);
    }

    public final void onCustomItemSelected() {
        this.mAdapter.setSelection(1);
        this.mSelectPosition = 1;
    }

    public final void notifyDateSetChanged(int i) {
        HeaderTailAdapter headerTailAdapter = this.mAdapter;
        if (headerTailAdapter != null) {
            headerTailAdapter.notifyItemChanged(i, 1);
        }
    }
}
