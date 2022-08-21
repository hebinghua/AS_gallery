package com.miui.gallery.vlog.audio;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.BaseIntentUtil;
import com.miui.gallery.util.ConvertFilepathUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.R$color;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$raw;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.entity.AudioData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogContract$IVlogView;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IAudioManager;
import com.miui.gallery.vlog.tools.VlogOrientationProvider;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.io.File;
import java.util.List;

/* loaded from: classes2.dex */
public class AudioMenuFragment extends MenuFragment<AudioMenuPresenter> implements AudioMenuContract$IAudioMenuView, View.OnClickListener {
    public AudioAdapter mAdapter;
    public ViewGroup mAudioListLayout;
    public IAudioManager mAudioManager;
    public ConstraintLayout mAudioOperationLayout;
    public FrameLayout mBottomPanel;
    public TextView mChangeAudioBtn;
    public AudioFrameAdapter mFrameAdapter;
    public RecyclerView mFrameRecyclerView;
    public boolean mHasLocalAudio;
    public boolean mInLocalAudio;
    public boolean mIsSingleVideo;
    public String mLocalAudioPath;
    public LottieAnimationView mMaxVoiceLottieView;
    public LottieAnimationView mMinVoiceLottieView;
    public View mOperationView;
    public FrameLayout mPlayLayout;
    public ImageView mPlayView;
    public SimpleRecyclerView mRecyclerView;
    public ResourceDownloadManager<AudioData> mResourceDownloadManager;
    public BubbleSeekBar mSeekBar;
    public String mSelectAudioRangeTitle;
    public View mSeparatedView;
    public int mTempVolumeValue;
    public FrameLayout mVoiceLayout;
    public ImageView mVoiceView;
    public AudioZipFileConfig mZipFileConfig;
    public int mAudioVolumeValue = 100;
    public int mLocalAudioPosition = -1;
    public ResourceDownloadManager.IDownloadTaskListener mIDownloadTaskListener = new ResourceDownloadManager.IDownloadTaskListener<VlogResource>() { // from class: com.miui.gallery.vlog.audio.AudioMenuFragment.2
        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandStart(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(18);
                AudioMenuFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("AudioMenuFragment", "download start: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandSuccess(VlogResource vlogResource, int i) {
            if (vlogResource instanceof AudioData) {
                vlogResource.setDownloadState(17);
                StringBuilder sb = new StringBuilder();
                sb.append(VlogConfig.AUDIO_PATH);
                sb.append(File.separator);
                AudioData audioData = (AudioData) vlogResource;
                sb.append(audioData.getFileName());
                audioData.setAudioPath(sb.toString());
                AudioMenuFragment.this.notifyDateSetChanged(i);
                AudioMenuFragment.this.performSelectedItem(audioData, i);
                DefaultLogger.d("AudioMenuFragment", "download success: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandFail(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(20);
                AudioMenuFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("AudioMenuFragment", "download fail: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onTaskCancel(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                int downloadState = vlogResource.getDownloadState();
                if (downloadState != 0 && downloadState != 17) {
                    vlogResource.setDownloadState(20);
                    AudioMenuFragment.this.notifyDateSetChanged(i);
                }
                DefaultLogger.d("AudioMenuFragment", "download cancel: %s", vlogResource.getLabel());
            }
        }
    };
    public RecyclerView.OnScrollListener mFrameScrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.vlog.audio.AudioMenuFragment.3
        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            AudioData selectedItem;
            if (i == 1 && (selectedItem = AudioMenuFragment.this.mAdapter.getSelectedItem()) != null) {
                selectedItem.setHasChanged(true);
            }
            if (i == 0) {
                Pair<Float, Float> trimInOut = AudioMenuFragment.this.mFrameAdapter.getTrimInOut();
                AudioMenuFragment.this.audioClip(((Float) trimInOut.first).floatValue(), ((Float) trimInOut.second).floatValue());
                AudioMenuFragment.this.getMiVideoSdkManager().seek(0L);
                AudioMenuFragment.this.getMiVideoSdkManager().play();
                return;
            }
            AudioMenuFragment.this.getMiVideoSdkManager().pause();
        }
    };
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.vlog.audio.AudioMenuFragment.4
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            recyclerView.smoothScrollToPosition(i);
            AudioData itemData = AudioMenuFragment.this.mAdapter.getItemData(i);
            if (itemData.isNone()) {
                AudioMenuFragment.this.mAdapter.exitEditMode();
                ((AudioMenuPresenter) AudioMenuFragment.this.mMenuPresenter).removeAudio();
                AudioMenuFragment.this.mAdapter.setSelectedIndex(i);
            } else if (itemData.isExtra()) {
                AudioMenuFragment.this.performSelectedItem(itemData, i);
            } else if (itemData.isLocal()) {
                AudioMenuFragment.this.mLocalAudioPosition = i;
                if (!AudioMenuFragment.this.mHasLocalAudio || AudioMenuFragment.this.mIsSingleVideo) {
                    if (AudioMenuFragment.this.mAdapter.getSelectedIndex() == i || TextUtils.isEmpty(AudioMenuFragment.this.mLocalAudioPath)) {
                        AudioMenuFragment.this.selectLocalAudio();
                    } else {
                        AudioMenuFragment.this.mAdapter.setSelectedIndex(i);
                        if (!TextUtils.isEmpty(AudioMenuFragment.this.mLocalAudioPath)) {
                            ((AudioMenuPresenter) AudioMenuFragment.this.mMenuPresenter).applyAudio(AudioMenuFragment.this.mLocalAudioPath);
                            AudioMenuFragment.this.doConfigAudioVolume(100);
                            AudioMenuFragment.this.getMiVideoSdkManager().seek(0L);
                            AudioMenuFragment.this.getMiVideoSdkManager().play();
                        }
                    }
                } else {
                    AudioMenuFragment.this.mAdapter.enterEditMode();
                    if (i != AudioMenuFragment.this.mAdapter.getSelectedIndex()) {
                        if (!TextUtils.isEmpty(AudioMenuFragment.this.mLocalAudioPath)) {
                            itemData.setScrollX(0);
                            ((AudioMenuPresenter) AudioMenuFragment.this.mMenuPresenter).applyAudio(AudioMenuFragment.this.mLocalAudioPath);
                            AudioMenuFragment.this.mAdapter.setSelectedIndex(i);
                            AudioMenuFragment.this.mFrameRecyclerView.scrollToPosition(0);
                            AudioMenuFragment.this.getMiVideoSdkManager().play();
                        }
                    } else {
                        AudioMenuFragment.this.showApplyView(true);
                    }
                }
            }
            VlogStatUtils.statEvent("audio", itemData.getNameKey());
            return true;
        }
    };
    public BubbleSeekBar.ProgressListener mSeekBarProgressListener = new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.vlog.audio.AudioMenuFragment.5
        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
            if (i == 0 || i == 100) {
                LinearMotorHelper.performHapticFeedback(AudioMenuFragment.this.mSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
            }
            if (AudioMenuFragment.this.mAdapter != null) {
                AudioMenuFragment.this.mTempVolumeValue = i;
                float f = i;
                AudioMenuFragment.this.mAudioManager.setAudioTrackVolumeGain(f, f);
            }
            if (i == bubbleSeekBar.getMinProgress()) {
                AudioMenuFragment.this.mMinVoiceLottieView.playAnimation();
            } else if (i != bubbleSeekBar.getMaxProgress()) {
            } else {
                AudioMenuFragment.this.mMaxVoiceLottieView.playAnimation();
            }
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
            AudioMenuFragment.this.mSeekBar.setContentDescription(AudioMenuFragment.this.getResources().getString(R$string.vlog_talkback_audio_seekbar_adjust, Integer.valueOf((int) AudioMenuFragment.this.mSeekBar.getCurrentProgress())));
        }
    };

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R$layout.vlog_menu_audio_layout, viewGroup, false);
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mIsSingleVideo = ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).isSingleVideoEdit();
        this.mAudioManager = (IAudioManager) getMiVideoSdkManager().getManagerService(2);
        this.mAudioListLayout = (ViewGroup) view.findViewById(R$id.audio_list_layout);
        this.mAudioOperationLayout = (ConstraintLayout) view.findViewById(R$id.audio_operation_layout);
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_audio_apply_layout, (ViewGroup) null, false);
        TextView textView = (TextView) inflate.findViewById(R$id.change_audio);
        this.mChangeAudioBtn = textView;
        textView.setOnClickListener(this);
        FolmeUtil.setCustomTouchAnim(this.mChangeAudioBtn, new AnimParams.Builder().setAlpha(0.6f).setScale(1.0f).build(), null, null, true);
        this.mBottomPanel.addView(inflate);
        initAudioSeekBarConfig();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext, 0, false) { // from class: com.miui.gallery.vlog.audio.AudioMenuFragment.1
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean canScrollHorizontally() {
                return AudioMenuFragment.this.mFrameAdapter.getRatio() > 1.0f;
            }
        };
        this.mFrameAdapter = new AudioFrameAdapter(this.mContext, linearLayoutManager);
        this.mFrameRecyclerView.setLayoutManager(linearLayoutManager);
        this.mFrameRecyclerView.setOverScrollMode(2);
        this.mFrameRecyclerView.setAdapter(this.mFrameAdapter);
        this.mFrameRecyclerView.addOnScrollListener(this.mFrameScrollListener);
        View findViewById = view.findViewById(R$id.ok);
        View findViewById2 = view.findViewById(R$id.cancel);
        findViewById.setOnClickListener(this);
        findViewById2.setOnClickListener(this);
        FolmeUtilsEditor.animButton(findViewById);
        FolmeUtilsEditor.animButton(findViewById2);
        updateVideoVoiceState();
        FolmeUtil.setCustomTouchAnim(this.mChangeAudioBtn, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mFrameRecyclerView);
        updatePlayViewState(((AudioMenuPresenter) this.mMenuPresenter).isPlaying());
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        initDirectionView();
        initRecyclerView();
        VlogUtils.hideViews(this.mChangeAudioBtn);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) getViewById(R$id.min_voice_view);
        this.mMinVoiceLottieView = lottieAnimationView;
        lottieAnimationView.setAnimation(R$raw.min_voice);
        LottieAnimationView lottieAnimationView2 = (LottieAnimationView) getViewById(R$id.max_voice_view);
        this.mMaxVoiceLottieView = lottieAnimationView2;
        lottieAnimationView2.setAnimation(R$raw.max_voice);
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) getViewById(R$id.seek_bar);
        this.mSeekBar = bubbleSeekBar;
        bubbleSeekBar.setProgressListener(this.mSeekBarProgressListener);
        this.mSeekBar.setContentDescription(getResources().getString(R$string.vlog_talkback_audio_seekbar));
        this.mBottomPanel = (FrameLayout) getViewById(R$id.bottom_panel);
        this.mFrameRecyclerView = (RecyclerView) getViewById(R$id.audio_recycler_view);
        this.mZipFileConfig = new AudioZipFileConfig();
        ResourceDownloadManager<AudioData> resourceDownloadManager = new ResourceDownloadManager<>(this.mContext, getFragmentManager(), this.mZipFileConfig);
        this.mResourceDownloadManager = resourceDownloadManager;
        resourceDownloadManager.setDownloadTaskListener(this.mIDownloadTaskListener);
        this.mPlayLayout.setOnClickListener(this);
        FolmeUtil.setCustomTouchAnim(this.mPlayLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
        FrameLayout frameLayout = (FrameLayout) this.mOperationView.findViewById(R$id.voice_layout);
        this.mVoiceLayout = frameLayout;
        frameLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_audio_voice));
        this.mVoiceLayout.setOnClickListener(this);
        FolmeUtil.setCustomTouchAnim(this.mVoiceLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
        this.mVoiceView = (ImageView) this.mOperationView.findViewById(R$id.voice_view);
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
            this.mOperationView = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_audio_operation_layout_land, (ViewGroup) null, false);
            View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_audio_separated_layout, (ViewGroup) null, false);
            this.mSeparatedView = inflate;
            this.mPlayLayout = (FrameLayout) inflate.findViewById(R$id.play_layout);
            this.mPlayView = (ImageView) this.mSeparatedView.findViewById(R$id.play_view);
            return;
        }
        View inflate2 = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_audio_operation_layout, (ViewGroup) null, false);
        this.mOperationView = inflate2;
        this.mPlayLayout = (FrameLayout) inflate2.findViewById(R$id.play_layout);
        this.mPlayView = (ImageView) this.mOperationView.findViewById(R$id.play_view);
    }

    public final void initRecyclerView() {
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) getViewById(R$id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) simpleRecyclerView.getLayoutParams();
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        int i = R$dimen.vlog_common_menu_recyclerview_item_gap;
        if (VlogUtils.isLandscape(getContext())) {
            customScrollerLinearLayoutManager.setOrientation(1);
            layoutParams.width = (int) getResources().getDimension(com.miui.gallery.editor.R$dimen.editor_menu_filter_item_height);
            layoutParams.height = -1;
            layoutParams.setMarginStart(getResources().getDimensionPixelSize(R$dimen.vlog_filter_recycler_view_padding_top));
        } else {
            customScrollerLinearLayoutManager.setOrientation(0);
            layoutParams.width = -1;
            layoutParams.height = (int) getResources().getDimension(com.miui.gallery.editor.R$dimen.editor_menu_filter_item_height);
            layoutParams.setMargins(0, getResources().getDimensionPixelSize(R$dimen.vlog_filter_recycler_view_padding_top), 0, 0);
        }
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        if (this.mRecyclerView.getItemDecorationCount() == 0) {
            BlankDivider blankDivider = new BlankDivider(getResources(), i);
            blankDivider.setOrientationProvider(new VlogOrientationProvider());
            this.mRecyclerView.addItemDecoration(blankDivider);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter */
    public AudioMenuPresenter mo1801createPresenter() {
        return new AudioMenuPresenter(this.mContext, this, this.mZipFileConfig);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onScreenSizeChanged(int i) {
        super.onScreenSizeChanged(i);
        ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mSeekBar);
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (this.mIVlogView != null) {
            this.mVoiceLayout.setVisibility(z ? 8 : 0);
        }
        T t = this.mMenuPresenter;
        if (t == 0 || z) {
            return;
        }
        ((AudioMenuPresenter) t).mStopForApplyAvailableCount = 0;
        updateVideoVoiceState();
        int audioTrackVolumeGain = (int) this.mAudioManager.getAudioTrackVolumeGain();
        this.mAudioVolumeValue = audioTrackVolumeGain;
        doConfigAudioVolume(audioTrackVolumeGain);
        AudioAdapter audioAdapter = this.mAdapter;
        if (audioAdapter != null) {
            audioAdapter.exitEditMode();
        }
        if (((AudioMenuPresenter) this.mMenuPresenter).hasAudioLabel()) {
            this.mRecyclerView.scrollToPosition(0);
            this.mAdapter.clearSelectedIndex();
        } else {
            ((AudioMenuPresenter) this.mMenuPresenter).updateSelectedItem();
        }
        updatePlayViewState(((AudioMenuPresenter) this.mMenuPresenter).isPlaying());
    }

    public void updateSelectItem() {
        AudioAdapter audioAdapter;
        int findIndexByAudioPath = ((AudioMenuPresenter) this.mMenuPresenter).findIndexByAudioPath(this.mAdapter.getDataList());
        if (findIndexByAudioPath == -1 || (audioAdapter = this.mAdapter) == null) {
            return;
        }
        audioAdapter.setSelectedIndex(findIndexByAudioPath);
        this.mRecyclerView.scrollToPosition(findIndexByAudioPath);
    }

    @Override // com.miui.gallery.vlog.audio.AudioMenuContract$IAudioMenuView
    public void updateSelectedItem(String str) {
        AudioAdapter audioAdapter = this.mAdapter;
        if (audioAdapter == null) {
            return;
        }
        if (this.mIsSingleVideo && audioAdapter.getSelectedIndex() == this.mLocalAudioPosition) {
            return;
        }
        AudioData selectedItem = this.mAdapter.getSelectedItem();
        if (selectedItem == null || TextUtils.isEmpty(selectedItem.getAudioPath()) || !selectedItem.getAudioPath().equals(str)) {
            this.mAdapter.clearSelectedIndex();
        } else if (selectedItem.getAudioPath().equals(str)) {
        } else {
            notifyDateSetChanged(this.mAdapter.getSelectedIndex());
        }
    }

    public final void selectLocalAudio() {
        BaseIntentUtil.selectLocalAudio(getActivity(), 1000);
    }

    public final void performSelectedItem(AudioData audioData, int i) {
        T t = this.mMenuPresenter;
        if (t == 0 || ((AudioMenuPresenter) t).isSaving()) {
            DefaultLogger.d("AudioMenuFragment", "in save progress");
        } else if (audioData.isDownloaded()) {
            AudioAdapter audioAdapter = this.mAdapter;
            if (audioAdapter == null) {
                return;
            }
            audioAdapter.enterEditMode();
            if (i == this.mAdapter.getSelectedIndex()) {
                if (!this.mIsSingleVideo) {
                    showApplyView(true);
                }
            } else {
                ((AudioMenuPresenter) this.mMenuPresenter).applyAudio(audioData.getAudioPath());
                audioData.setScrollX(0);
                getMiVideoSdkManager().seek(0L);
                getMiVideoSdkManager().play();
            }
            this.mAdapter.setSelectedIndex(i);
        } else {
            this.mResourceDownloadManager.createDownloadCommand(audioData, i);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1000 && i2 == -1) {
            ClipData clipData = intent.getClipData();
            if (clipData == null) {
                this.mLocalAudioPath = ConvertFilepathUtil.getPath(getActivity(), intent.getData());
            } else {
                this.mLocalAudioPath = ConvertFilepathUtil.getPath(getActivity(), clipData.getItemAt(0).getUri());
            }
            if (TextUtils.isEmpty(this.mLocalAudioPath)) {
                return;
            }
            if (((AudioMenuPresenter) this.mMenuPresenter).isSupportMusic(this.mLocalAudioPath)) {
                this.mInLocalAudio = true;
                ((AudioMenuPresenter) this.mMenuPresenter).applyAudio(this.mLocalAudioPath);
                this.mAdapter.setSelectedIndex(this.mLocalAudioPosition);
                VlogUtils.showViews(this.mChangeAudioBtn);
                doConfigAudioVolume(100);
                getMiVideoSdkManager().play();
                if (this.mIsSingleVideo) {
                    return;
                }
                showApplyView(true);
                return;
            }
            this.mLocalAudioPath = null;
            ToastUtils.makeText(getActivity(), R$string.vlog_audio_unsupport_audio_file);
        }
    }

    public final void notifyDateSetChanged(int i) {
        AudioAdapter audioAdapter = this.mAdapter;
        if (audioAdapter != null) {
            audioAdapter.notifyItemChanged(i, 1);
        }
    }

    public final void initAudioSeekBarConfig() {
        this.mSeekBar.setMaxProgress(100);
        this.mSeekBar.setCurrentProgress(100.0f);
    }

    public void showApplyView(boolean z) {
        if (z) {
            ((AudioMenuPresenter) this.mMenuPresenter).updateApplyViewPlayState();
            VlogUtils.showViews(this.mChangeAudioBtn, this.mAudioOperationLayout);
            VlogUtils.hideViews(this.mAudioListLayout);
            if (this.mLocalAudioPosition == this.mAdapter.getSelectedIndex()) {
                VlogUtils.showViews(this.mChangeAudioBtn);
            } else {
                VlogUtils.hideViews(this.mChangeAudioBtn);
            }
            long duration = getMiVideoSdkManager().getDuration();
            long audioLength = this.mAudioManager.getAudioLength();
            DefaultLogger.d("AudioMenuFragment", "video %s, audio %s", Long.valueOf(duration), Long.valueOf(audioLength));
            AudioFrameAdapter audioFrameAdapter = this.mFrameAdapter;
            float f = 1.0f;
            if (duration > 0 && audioLength > 0) {
                f = (((float) audioLength) * 1.0f) / ((float) duration);
            }
            audioFrameAdapter.setRatio(f);
            this.mFrameRecyclerView.scrollToPosition(0);
            AudioData selectedItem = this.mAdapter.getSelectedItem();
            if (selectedItem != null) {
                this.mFrameRecyclerView.scrollBy(selectedItem.getScrollX(), 0);
            }
            this.mIVlogView.hideApplyView();
            if (TextUtils.isEmpty(this.mSelectAudioRangeTitle)) {
                this.mSelectAudioRangeTitle = getResources().getString(R$string.vlog_audio_select_range_title);
            }
            this.mIVlogView.showCustomTitleView(getTitleViewWithCustomTitle(this.mSelectAudioRangeTitle));
        } else {
            this.mIVlogView.hideCustomTitleView();
            this.mIVlogView.showApplyView();
            VlogUtils.hideViews(this.mChangeAudioBtn, this.mAudioOperationLayout);
            VlogUtils.showViews(this.mAudioListLayout);
        }
        if (z) {
            this.mIVlogView.getTopView().removeView(this.mOperationView);
        }
        this.mIVlogView.updateDisplayOperationView(this.mOperationView, z);
        if (!z) {
            setMenuTopView();
        }
        getIVlogView().updateEffectMenuView(!z);
    }

    public final void doConfigAudioVolume(int i) {
        float f = i;
        this.mAudioManager.setAudioTrackVolumeGain(f, f);
        this.mSeekBar.setCurrentProgress(f);
    }

    public final void updateVideoVoiceState() {
        this.mVoiceView.setSelected(!((AudioMenuPresenter) this.mMenuPresenter).hasVideoVoice());
    }

    @Override // com.miui.gallery.vlog.audio.AudioMenuContract$IAudioMenuView
    public void loadRecyclerView(List<AudioData> list) {
        AudioAdapter audioAdapter = new AudioAdapter(list, getActivity().getResources().getColor(R$color.vlog_filter_item_high_color), 0, 0, ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).isSingleVideoEdit());
        this.mAdapter = audioAdapter;
        this.mRecyclerView.setAdapter(audioAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        updateSelectItem();
    }

    @Override // com.miui.gallery.vlog.audio.AudioMenuContract$IAudioMenuView
    public void updatePlayViewState(boolean z) {
        Resources resources;
        int i;
        if (this.mPlayView.isSelected() != z) {
            this.mPlayView.setSelected(z);
        }
        ImageView imageView = this.mPlayView;
        if (z) {
            resources = getResources();
            i = R$string.vlog_talkback_view_pause;
        } else {
            resources = getResources();
            i = R$string.vlog_talkback_view_play;
        }
        imageView.setContentDescription(resources.getString(i));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.cancel) {
            this.mInLocalAudio = false;
            doCancelEvent();
            this.mTempVolumeValue = 0;
            handleItemTrimInOut(false);
            VlogStatUtils.statEvent("audio", "cancel");
        } else if (view.getId() == R$id.ok) {
            if (this.mInLocalAudio) {
                this.mHasLocalAudio = true;
                this.mAdapter.setSelectedIndex(this.mLocalAudioPosition);
            }
            this.mInLocalAudio = false;
            showApplyView(false);
            this.mAudioVolumeValue = this.mTempVolumeValue;
            handleItemTrimInOut(true);
            VlogStatUtils.statEvent("audio", "ok");
        } else if (view.getId() == R$id.play_layout) {
            ((AudioMenuPresenter) this.mMenuPresenter).doPlayViewClickEvent();
        } else if (view.getId() == R$id.voice_layout) {
            clickVoice();
        } else if (view.getId() != R$id.change_audio) {
        } else {
            selectLocalAudio();
        }
    }

    public final void clickVoice() {
        boolean isSelected = this.mVoiceView.isSelected();
        this.mIVlogView.changeVoiceState(isSelected);
        if (isSelected) {
            this.mVoiceView.setSelected(false);
            VlogContract$IVlogView iVlogView = getIVlogView();
            Resources resources = getResources();
            int i = R$string.vlog_audio_open_toast;
            iVlogView.showToast(resources.getString(i));
            this.mVoiceView.setContentDescription(getResources().getString(i));
            return;
        }
        this.mVoiceView.setSelected(true);
        VlogContract$IVlogView iVlogView2 = getIVlogView();
        Resources resources2 = getResources();
        int i2 = R$string.vlog_audio_close_toast;
        iVlogView2.showToast(resources2.getString(i2));
        this.mVoiceView.setContentDescription(getResources().getString(i2));
    }

    public final void handleItemTrimInOut(boolean z) {
        AudioData selectedItem = this.mAdapter.getSelectedItem();
        if (selectedItem != null && selectedItem.isHasChanged()) {
            if (z) {
                Pair<Float, Float> trimInOut = this.mFrameAdapter.getTrimInOut();
                selectedItem.setTrimInOut(((Float) trimInOut.first).floatValue(), ((Float) trimInOut.second).floatValue());
                selectedItem.setScrollX((int) (((Float) trimInOut.first).floatValue() * this.mFrameAdapter.getTotalWidth()));
            } else {
                audioClip(selectedItem.getTrimInOutFirst(), selectedItem.getTrimInOutSecond());
                ((AudioMenuPresenter) this.mMenuPresenter).play();
            }
            selectedItem.setHasChanged(false);
        }
    }

    public final void doCancelEvent() {
        showApplyView(false);
        AudioData selectedItem = this.mAdapter.getSelectedItem();
        if (selectedItem != null && !TextUtils.isEmpty(selectedItem.getAudioPath())) {
            ((AudioMenuPresenter) this.mMenuPresenter).applyAudio(selectedItem.getAudioPath());
        }
        doConfigAudioVolume(this.mAudioVolumeValue);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public boolean onBackPressed() {
        if (this.mAudioOperationLayout.getVisibility() == 0) {
            doCancelEvent();
            return true;
        }
        return super.onBackPressed();
    }

    public final void audioClip(float f, float f2) {
        long audioLength = this.mAudioManager.getAudioLength();
        if (audioLength <= 0) {
            return;
        }
        float f3 = (float) audioLength;
        ((AudioMenuPresenter) this.mMenuPresenter).audioClip(f * f3, f3 * f2);
        DefaultLogger.d("AudioMenuFragment", "trim from %s to %s", Float.valueOf(f), Float.valueOf(f2));
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackStopped() {
        T t = this.mMenuPresenter;
        if (((AudioMenuPresenter) t).mStopForApplyAvailableCount > 0) {
            AudioMenuPresenter audioMenuPresenter = (AudioMenuPresenter) t;
            audioMenuPresenter.mStopForApplyAvailableCount--;
            return;
        }
        updatePlayViewState(false);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackEOF() {
        updatePlayViewState(false);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackTimelinePosition(long j) {
        updatePlayViewState(true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        AudioAdapter audioAdapter = this.mAdapter;
        if (audioAdapter != null) {
            audioAdapter.setOnItemClickListener(null);
            this.mAdapter = null;
        }
        ((AudioMenuPresenter) this.mMenuPresenter).destroy();
    }
}
