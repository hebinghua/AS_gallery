package com.miui.gallery.video.editor.ui;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.ui.StartEndSmoothScrollerController;
import com.miui.gallery.util.ConvertFilepathUtil;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.LocalAudio;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.adapter.AudioRecyclerViewAdapter;
import com.miui.gallery.video.editor.factory.AudioFactory;
import com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback;
import com.miui.gallery.video.editor.manager.AudioManager;
import com.miui.gallery.video.editor.model.VideoEditorBaseLocalResource;
import com.miui.gallery.video.editor.model.VideoEditorBaseModel;
import com.miui.gallery.video.editor.net.ResourceDownloadManager;
import com.miui.gallery.video.editor.net.VideoEditorResourceRequest;
import com.miui.gallery.video.editor.ui.AudioFragment;
import com.miui.gallery.video.editor.ui.menu.AudioView;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.ScrollHelper;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class AudioFragment extends MenuFragment {
    public AudioRecyclerViewAdapter mAdapter;
    public View mCancelView;
    public boolean mInitBgSoundEnable;
    public View mOkView;
    public ResourceDownloadManager mResourceDownloadManager;
    public int mSavedSelectedAudioIndex;
    public SingleChoiceRecyclerView mSingleChoiceRecyclerView;
    public TextView mTitleView;
    public VideoEditorResourceRequest mVideoEditorResourceRequest;
    public ArrayList<LocalAudio> mAudios = new ArrayList<>();
    public boolean mBgSoundEnable = true;
    public boolean mVoiceClicked = false;

    /* renamed from: $r8$lambda$PGkFDVZ7A9-zJ3BrcS_RGI_52yI */
    public static /* synthetic */ void m1755$r8$lambda$PGkFDVZ7A9zJ3BrcS_RGI_52yI(AudioFragment audioFragment, View view) {
        audioFragment.lambda$initListener$1(view);
    }

    public static /* synthetic */ void $r8$lambda$dBbEP47QLcjWdiYO5qJSKg7APJw(AudioFragment audioFragment, ArrayList arrayList) {
        audioFragment.lambda$refreshData$3(arrayList);
    }

    public static /* synthetic */ void $r8$lambda$gGTAiJlNsykAQ04nDlnWvONkCrQ(AudioFragment audioFragment) {
        audioFragment.lambda$updateVoiceState$2();
    }

    public static /* synthetic */ void $r8$lambda$vY0dYpKd2ob80crWIY__8lDnxxI(AudioFragment audioFragment, View view) {
        audioFragment.lambda$initListener$0(view);
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public int getEffectId() {
        return R.id.video_editor_audio;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAudios = AudioManager.initDataWithBgColor();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        AudioView audioView = new AudioView(viewGroup.getContext());
        initRecyclerView(audioView);
        this.mCancelView = audioView.findViewById(R.id.cancel);
        this.mOkView = audioView.findViewById(R.id.ok);
        TextView textView = (TextView) audioView.findViewById(R.id.title);
        this.mTitleView = textView;
        textView.setText(this.mContext.getResources().getString(R.string.video_editor_audio));
        this.mModuleFactory = new AudioFactory();
        this.mResourceDownloadManager = new ResourceDownloadManager(viewGroup.getContext(), getFragmentManager(), this.mModuleFactory);
        boolean z = this.mBgSoundEnable;
        this.mInitBgSoundEnable = z;
        updateAudioVoiceView(z);
        initListener();
        loadResourceData();
        return audioView;
    }

    public final void initRecyclerView(View view) {
        this.mSingleChoiceRecyclerView = (SingleChoiceRecyclerView) view.findViewById(R.id.recycler_view);
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(this.mContext, 0, false);
        customScrollerLinearLayoutManager.setSmoothScroller(new StartEndSmoothScrollerController(this.mContext));
        this.mSingleChoiceRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_smart_effect_item_start);
        this.mSingleChoiceRecyclerView.addItemDecoration(new BlankDivider(dimensionPixelSize, dimensionPixelSize, getResources().getDimensionPixelSize(R.dimen.editor_smart_effect_item_horizontal_interval), 0, 0));
        AudioRecyclerViewAdapter audioRecyclerViewAdapter = new AudioRecyclerViewAdapter(this.mContext, this.mAudios);
        this.mAdapter = audioRecyclerViewAdapter;
        audioRecyclerViewAdapter.setItemSelectChangeListener(new MyAudioItemSelectChangeListener());
        this.mSingleChoiceRecyclerView.setAdapter(this.mAdapter);
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mSingleChoiceRecyclerView);
        this.mAdapter.setSelectedItemPosition(this.mSavedSelectedAudioIndex);
    }

    public /* synthetic */ void lambda$initListener$0(View view) {
        doApply();
    }

    public final void initListener() {
        this.mOkView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.AudioFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AudioFragment.$r8$lambda$vY0dYpKd2ob80crWIY__8lDnxxI(AudioFragment.this, view);
            }
        });
        this.mCancelView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.AudioFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AudioFragment.m1755$r8$lambda$PGkFDVZ7A9zJ3BrcS_RGI_52yI(AudioFragment.this, view);
            }
        });
        this.mResourceDownloadManager.setDownloadTaskListener(new ResourceDownloadManager.IDownloadTaskListener() { // from class: com.miui.gallery.video.editor.ui.AudioFragment.1
            {
                AudioFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
            public void onCommandStart(VideoEditorBaseModel videoEditorBaseModel, int i) {
                if (videoEditorBaseModel != null) {
                    videoEditorBaseModel.setDownloadState(18);
                    AudioFragment.this.notifyDateSetChanged(i);
                }
            }

            @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
            public void onCommandSuccess(VideoEditorBaseModel videoEditorBaseModel, int i) {
                if (videoEditorBaseModel != null) {
                    videoEditorBaseModel.setDownloadState(0);
                    AudioFragment.this.notifyDateSetChanged(i);
                }
            }

            @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
            public void onCommandFail(VideoEditorBaseModel videoEditorBaseModel, int i) {
                if (videoEditorBaseModel != null) {
                    videoEditorBaseModel.setDownloadState(20);
                    AudioFragment.this.notifyDateSetChanged(i);
                }
            }

            @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
            public void onTaskCancel(VideoEditorBaseModel videoEditorBaseModel, int i) {
                int downloadState;
                if (videoEditorBaseModel == null || (downloadState = videoEditorBaseModel.getDownloadState()) == 0 || downloadState == 17) {
                    return;
                }
                videoEditorBaseModel.setDownloadState(20);
                AudioFragment.this.notifyDateSetChanged(i);
            }
        });
    }

    public /* synthetic */ void lambda$initListener$1(View view) {
        doCancel();
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public void updateVoiceState(boolean z) {
        this.mBgSoundEnable = z;
        this.mVideoEditor.setSourceAudioEnable(Boolean.valueOf(z));
        this.mVoiceClicked = true;
        this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.AudioFragment$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public final void onCompleted() {
                AudioFragment.$r8$lambda$gGTAiJlNsykAQ04nDlnWvONkCrQ(AudioFragment.this);
            }
        });
    }

    public /* synthetic */ void lambda$updateVoiceState$2() {
        this.mVideoEditor.play();
        updatePlayBtnView();
        recordEventWithEffectChanged();
    }

    public void loadResourceData() {
        this.mHasLoadData = false;
        VideoEditorResourceRequest videoEditorResourceRequest = new VideoEditorResourceRequest(getEffectId(), this.mModuleFactory);
        this.mVideoEditorResourceRequest = videoEditorResourceRequest;
        videoEditorResourceRequest.execute(new AnonymousClass2());
    }

    /* renamed from: com.miui.gallery.video.editor.ui.AudioFragment$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements ResponseListener {
        /* renamed from: $r8$lambda$TWN5b-_cWQLrYjdKopsdY4vi-j0 */
        public static /* synthetic */ void m1756$r8$lambda$TWN5b_cWQLrYjdKopsdY4vij0(AnonymousClass2 anonymousClass2) {
            anonymousClass2.lambda$onResponseError$0();
        }

        /* renamed from: $r8$lambda$X6-bdZbHCDf1W2iBfjE7Eci9DXk */
        public static /* synthetic */ void m1757$r8$lambda$X6bdZbHCDf1W2iBfjE7Eci9DXk(AnonymousClass2 anonymousClass2, ErrorCode errorCode) {
            anonymousClass2.lambda$onResponseError$1(errorCode);
        }

        public AnonymousClass2() {
            AudioFragment.this = r1;
        }

        @Override // com.miui.gallery.net.base.ResponseListener
        public void onResponse(Object... objArr) {
            AudioFragment audioFragment = AudioFragment.this;
            audioFragment.mHasLoadData = true;
            audioFragment.refreshData((List) objArr[0]);
        }

        @Override // com.miui.gallery.net.base.ResponseListener
        public void onResponseError(final ErrorCode errorCode, String str, Object obj) {
            DefaultLogger.d("AudioFragment", "errorCode: %s", errorCode);
            AudioFragment.this.mHasLoadData = true;
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.AudioFragment$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AudioFragment.AnonymousClass2.m1757$r8$lambda$X6bdZbHCDf1W2iBfjE7Eci9DXk(AudioFragment.AnonymousClass2.this, errorCode);
                }
            });
        }

        public /* synthetic */ void lambda$onResponseError$1(ErrorCode errorCode) {
            if (AudioFragment.this.mAudios != null && AudioFragment.this.mAudios.size() == 0) {
                AudioFragment.this.mAudios.add(new LocalAudio("audio_none", R.drawable.video_editor_icon_audio_none, "ve_type_none", false));
                if (AudioFragment.this.mAdapter != null) {
                    AudioFragment.this.mAdapter.notifyDataSetChanged();
                }
            }
            if (errorCode == ErrorCode.NETWORK_NOT_CONNECTED) {
                ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.AudioFragment$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AudioFragment.AnonymousClass2.m1756$r8$lambda$TWN5b_cWQLrYjdKopsdY4vij0(AudioFragment.AnonymousClass2.this);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onResponseError$0() {
            ToastUtils.makeText(AudioFragment.this.mContext, (int) R.string.video_editor_download_failed_for_notwork);
        }
    }

    public final void refreshData(List<VideoEditorBaseLocalResource> list) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (VideoEditorBaseLocalResource videoEditorBaseLocalResource : list) {
            if (videoEditorBaseLocalResource != null) {
                if ("ve_type_none".equals(videoEditorBaseLocalResource.type)) {
                    arrayList.add(videoEditorBaseLocalResource);
                } else if ("ve_type_extra".equals(videoEditorBaseLocalResource.type)) {
                    arrayList2.add(videoEditorBaseLocalResource);
                } else if ("ve_type_custom".equals(videoEditorBaseLocalResource.type)) {
                    arrayList3.add(videoEditorBaseLocalResource);
                }
            }
        }
        List<LocalResource> localTemplateEntities = this.mModuleFactory.getLocalTemplateEntities(this.mContext);
        localTemplateEntities.addAll(arrayList);
        localTemplateEntities.addAll(arrayList2);
        localTemplateEntities.addAll(arrayList3);
        final ArrayList arrayList4 = new ArrayList();
        arrayList4.addAll(AudioManager.loadAudioData(this.mModuleFactory, localTemplateEntities));
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.AudioFragment$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                AudioFragment.$r8$lambda$dBbEP47QLcjWdiYO5qJSKg7APJw(AudioFragment.this, arrayList4);
            }
        });
    }

    public /* synthetic */ void lambda$refreshData$3(ArrayList arrayList) {
        this.mAudios.clear();
        this.mAudios.addAll(arrayList);
        AudioRecyclerViewAdapter audioRecyclerViewAdapter = this.mAdapter;
        if (audioRecyclerViewAdapter != null) {
            audioRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public List<String> getCurrentEffect() {
        LocalAudio audio;
        AudioRecyclerViewAdapter audioRecyclerViewAdapter = this.mAdapter;
        if (audioRecyclerViewAdapter == null || (audio = audioRecyclerViewAdapter.getAudio(audioRecyclerViewAdapter.getSelectedItemPosition())) == null) {
            return null;
        }
        String str = "source";
        if (audio.isCustom()) {
            String[] strArr = new String[2];
            strArr[0] = "custom";
            if (!this.mVideoEditor.isSourceAudioEnable()) {
                str = "mute";
            }
            strArr[1] = str;
            return Arrays.asList(strArr);
        } else if (!audio.isExtra()) {
            return null;
        } else {
            String[] strArr2 = new String[2];
            strArr2[0] = audio.getLabel();
            if (!this.mVideoEditor.isSourceAudioEnable()) {
                str = "mute";
            }
            strArr2[1] = str;
            return Arrays.asList(strArr2);
        }
    }

    public boolean doApply() {
        if (this.mVideoEditor == null) {
            DefaultLogger.d("AudioFragment", "doApply: videoEditor is null.");
            return false;
        }
        this.mSavedSelectedAudioIndex = this.mAdapter.getSelectedItemPosition();
        this.mVideoEditor.saveEditState();
        recordEventWithApply();
        onExitMode();
        return true;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public boolean doCancel() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor == null) {
            DefaultLogger.d("AudioFragment", "doCancel: videoEditor is null.");
            return false;
        }
        if (this.mVoiceClicked) {
            this.mBgSoundEnable = this.mInitBgSoundEnable;
        }
        videoEditor.restoreEditState();
        return this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.AudioFragment.3
            {
                AudioFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public void onCompleted() {
                VideoEditor videoEditor2 = AudioFragment.this.mVideoEditor;
                if (videoEditor2 == null) {
                    return;
                }
                videoEditor2.play();
                AudioFragment.this.recordEventWithCancel();
                AudioFragment.this.onExitMode();
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        String path;
        super.onActivityResult(i, i2, intent);
        if (i == 1000 && i2 == -1) {
            ClipData clipData = intent.getClipData();
            if (clipData == null) {
                path = ConvertFilepathUtil.getPath(getActivity(), intent.getData());
            } else {
                path = ConvertFilepathUtil.getPath(getActivity(), clipData.getItemAt(0).getUri());
            }
            if (TextUtils.isEmpty(path)) {
                return;
            }
            if (nexClip.getSupportedClip(path) != null) {
                this.mVideoEditor.setBackgroundMusic(Scheme.FILE.wrap(path));
                this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.AudioFragment.4
                    {
                        AudioFragment.this = this;
                    }

                    @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                    public void onCompleted() {
                        AudioFragment.this.mVideoEditor.play();
                        AudioFragment.this.recordEventWithEffectChanged();
                        AudioFragment.this.updatePlayBtnView();
                        if (AudioFragment.this.mAdapter != null) {
                            AudioFragment audioFragment = AudioFragment.this;
                            audioFragment.updateSelectedItemPosition(audioFragment.mAdapter.getItemCount() - 1);
                        }
                    }
                });
                return;
            }
            ToastUtils.makeText(getActivity(), (int) R.string.video_editor_unsupport_audio_file);
        }
    }

    /* loaded from: classes2.dex */
    public class MyAudioItemSelectChangeListener implements SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener {
        public MyAudioItemSelectChangeListener() {
            AudioFragment.this = r1;
        }

        @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener
        public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z) {
            AudioFragment audioFragment = AudioFragment.this;
            if (!audioFragment.mHasLoadData) {
                return false;
            }
            ScrollHelper.onItemClick(audioFragment.mSingleChoiceRecyclerView, i);
            LocalAudio audio = ((AudioRecyclerViewAdapter) singleChoiceRecyclerViewAdapter).getAudio(i);
            if (audio != null) {
                if (audio.isCustom()) {
                    Intent intent = new Intent();
                    intent.setType("audio/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    intent.setPackage("com.miui.player");
                    try {
                        AudioFragment.this.startActivityForResult(intent, 1000);
                    } catch (ActivityNotFoundException unused) {
                        DefaultLogger.e("AudioFragment", "com.miui.player not found,try all picker");
                        try {
                            Intent intent2 = new Intent();
                            intent2.setType("audio/*");
                            intent2.setAction("android.intent.action.GET_CONTENT");
                            AudioFragment.this.startActivityForResult(intent2, 1000);
                        } catch (ActivityNotFoundException unused2) {
                            DefaultLogger.e("AudioFragment", "picker not found");
                        }
                    }
                    AudioFragment.this.updateSelectedItemPosition(i);
                    return false;
                } else if (audio.isNone() && z) {
                    AudioFragment.this.updateSelectedItemPosition(i);
                    AudioFragment.this.successProcess(audio);
                } else if (audio.isExtra() && z) {
                    if (audio.isDownloaded()) {
                        AudioFragment.this.updateSelectedItemPosition(i);
                        AudioFragment.this.successProcess(audio);
                        return true;
                    }
                    AudioFragment.this.mResourceDownloadManager.createDownloadCommand(audio, i);
                }
            }
            return false;
        }
    }

    public final void updateSelectedItemPosition(int i) {
        AudioRecyclerViewAdapter audioRecyclerViewAdapter = this.mAdapter;
        if (audioRecyclerViewAdapter != null) {
            audioRecyclerViewAdapter.setSelectedItemPosition(i);
            this.mAdapter.clearLastSelectedPostion();
        }
    }

    public void notifyDateSetChanged(int i) {
        AudioRecyclerViewAdapter audioRecyclerViewAdapter = this.mAdapter;
        if (audioRecyclerViewAdapter != null) {
            audioRecyclerViewAdapter.notifyItemChanged(i, 1);
        }
    }

    public final void successProcess(LocalAudio localAudio) {
        this.mVideoEditor.setBackgroundMusic(localAudio.getSrcPath());
        this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.AudioFragment.5
            {
                AudioFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public void onCompleted() {
                AudioFragment.this.mVideoEditor.play();
                AudioFragment.this.recordEventWithEffectChanged();
                AudioFragment.this.updatePlayBtnView();
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        updateAudioVoiceView(false);
        AudioRecyclerViewAdapter audioRecyclerViewAdapter = this.mAdapter;
        if (audioRecyclerViewAdapter != null) {
            audioRecyclerViewAdapter.setItemSelectChangeListener(null);
            this.mAdapter = null;
        }
        VideoEditorResourceRequest videoEditorResourceRequest = this.mVideoEditorResourceRequest;
        if (videoEditorResourceRequest != null) {
            videoEditorResourceRequest.cancel();
        }
        this.mVoiceClicked = false;
        ResourceDownloadManager resourceDownloadManager = this.mResourceDownloadManager;
        if (resourceDownloadManager != null) {
            resourceDownloadManager.cancel();
            this.mResourceDownloadManager = null;
        }
    }

    public final void updateAudioVoiceView(boolean z) {
        IVideoEditorListener$IVideoEditorFragmentCallback iVideoEditorListener$IVideoEditorFragmentCallback = this.mCallback;
        if (iVideoEditorListener$IVideoEditorFragmentCallback != null) {
            iVideoEditorListener$IVideoEditorFragmentCallback.updateAudioVoiceView(z);
        }
    }
}
