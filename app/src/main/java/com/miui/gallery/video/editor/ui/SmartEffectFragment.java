package com.miui.gallery.video.editor.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.ui.StartEndSmoothScrollerController;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.SmartEffect;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.adapter.SmartEffectRecyclerViewAdapter;
import com.miui.gallery.video.editor.factory.SmartEffectFactory;
import com.miui.gallery.video.editor.manager.NexAssetTemplateManager;
import com.miui.gallery.video.editor.manager.SmartEffectManager;
import com.miui.gallery.video.editor.model.VideoEditorBaseLocalResource;
import com.miui.gallery.video.editor.model.VideoEditorBaseModel;
import com.miui.gallery.video.editor.net.ResourceDownloadManager;
import com.miui.gallery.video.editor.net.VideoEditorResourceRequest;
import com.miui.gallery.video.editor.ui.SmartEffectFragment;
import com.miui.gallery.video.editor.ui.menu.SmartEffectView;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.ScrollHelper;
import com.nexstreaming.nexeditorsdk.nexTemplateManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class SmartEffectFragment extends MenuFragment {
    public SmartEffectRecyclerViewAdapter mAdapter;
    public View mCancelView;
    public View mLayout;
    public LoadSmartEffectTemplateTask mLoadSmartEffectTemplateTask;
    public View mOkView;
    public ResourceDownloadManager mResourceDownloadManager;
    public int mSavedSelectedSmartEffectIndex;
    public SingleChoiceRecyclerView mSingleChoiceRecyclerView;
    public SmartEffectManager mSmartEffectManager;
    public TextView mTitleView;
    public Toast mToast;
    public VideoEditorResourceRequest mVideoEditorResourceRequest;
    public int mVideoTotalTime;
    public final String TAG = "SmartEffectFragment";
    public ArrayList<SmartEffect> mSmartEffects = new ArrayList<>();

    /* renamed from: $r8$lambda$BOkdInpV7t4md5Lu8lBiP-o6F7Y */
    public static /* synthetic */ void m1758$r8$lambda$BOkdInpV7t4md5Lu8lBiPo6F7Y(SmartEffectFragment smartEffectFragment, View view) {
        smartEffectFragment.lambda$initListener$1(view);
    }

    /* renamed from: $r8$lambda$ZiHe6nBe-j6Ao4cRtfMJTdNLQnQ */
    public static /* synthetic */ void m1759$r8$lambda$ZiHe6nBej6Ao4cRtfMJTdNLQnQ(SmartEffectFragment smartEffectFragment, View view) {
        smartEffectFragment.lambda$initListener$0(view);
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public int getEffectId() {
        return R.id.video_editor_smart_effect;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSmartEffectManager = new SmartEffectManager();
        this.mSmartEffects = SmartEffectManager.initDataWithBgColor();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        SmartEffectView smartEffectView = new SmartEffectView(viewGroup.getContext());
        this.mLayout = smartEffectView;
        this.mCancelView = smartEffectView.findViewById(R.id.cancel);
        this.mOkView = this.mLayout.findViewById(R.id.ok);
        TextView textView = (TextView) this.mLayout.findViewById(R.id.title);
        this.mTitleView = textView;
        textView.setText(this.mContext.getResources().getString(R.string.video_editor_intelligent_special_effect));
        this.mVideoTotalTime = this.mVideoEditor.getProjectTotalTime();
        this.mModuleFactory = new SmartEffectFactory();
        this.mResourceDownloadManager = new ResourceDownloadManager(viewGroup.getContext(), getFragmentManager(), this.mModuleFactory);
        this.mLoadSmartEffectTemplateTask = new LoadSmartEffectTemplateTask();
        initRecyclerView(this.mLayout);
        initListener();
        loadResourceData();
        return this.mLayout;
    }

    public final void initRecyclerView(View view) {
        this.mSingleChoiceRecyclerView = (SingleChoiceRecyclerView) view.findViewById(R.id.recycler_view);
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(this.mContext, 0, false);
        customScrollerLinearLayoutManager.setSmoothScroller(new StartEndSmoothScrollerController(this.mContext));
        this.mSingleChoiceRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_smart_effect_item_start);
        this.mSingleChoiceRecyclerView.addItemDecoration(new BlankDivider(dimensionPixelSize, dimensionPixelSize, getResources().getDimensionPixelSize(R.dimen.editor_smart_effect_item_horizontal_interval), 0, 0));
        SmartEffectRecyclerViewAdapter smartEffectRecyclerViewAdapter = new SmartEffectRecyclerViewAdapter(this.mContext, this.mSmartEffects);
        this.mAdapter = smartEffectRecyclerViewAdapter;
        smartEffectRecyclerViewAdapter.setItemSelectChangeListener(new SmartEffectItemSelectChangeListener(this, null));
        this.mSingleChoiceRecyclerView.setAdapter(this.mAdapter);
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mSingleChoiceRecyclerView);
        this.mAdapter.setSelectedItemPosition(this.mSavedSelectedSmartEffectIndex);
    }

    public /* synthetic */ void lambda$initListener$0(View view) {
        doApply();
    }

    public final void initListener() {
        this.mOkView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SmartEffectFragment.m1759$r8$lambda$ZiHe6nBej6Ao4cRtfMJTdNLQnQ(SmartEffectFragment.this, view);
            }
        });
        this.mCancelView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SmartEffectFragment.m1758$r8$lambda$BOkdInpV7t4md5Lu8lBiPo6F7Y(SmartEffectFragment.this, view);
            }
        });
        this.mResourceDownloadManager.setDownloadTaskListener(new AnonymousClass1());
    }

    /* renamed from: com.miui.gallery.video.editor.ui.SmartEffectFragment$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ResourceDownloadManager.IDownloadTaskListener {
        /* renamed from: $r8$lambda$CD97bPMfpsb-8BHe951Xul3jHtk */
        public static /* synthetic */ void m1760$r8$lambda$CD97bPMfpsb8BHe951Xul3jHtk(AnonymousClass1 anonymousClass1, VideoEditorBaseModel videoEditorBaseModel, int i, List list) {
            anonymousClass1.lambda$onCommandSuccess$0(videoEditorBaseModel, i, list);
        }

        public AnonymousClass1() {
            SmartEffectFragment.this = r1;
        }

        @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandStart(VideoEditorBaseModel videoEditorBaseModel, int i) {
            if (videoEditorBaseModel != null) {
                videoEditorBaseModel.setDownloadState(18);
                SmartEffectFragment.this.notifyDateSetChanged(i);
            }
        }

        @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandFail(VideoEditorBaseModel videoEditorBaseModel, int i) {
            if (videoEditorBaseModel != null) {
                videoEditorBaseModel.setDownloadState(20);
                SmartEffectFragment.this.notifyDateSetChanged(i);
            }
        }

        @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandSuccess(final VideoEditorBaseModel videoEditorBaseModel, final int i) {
            SmartEffectFragment.this.mLoadSmartEffectTemplateTask.execute(new LoadSmartEffectTemplateTask.Callback() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment$1$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.video.editor.ui.SmartEffectFragment.LoadSmartEffectTemplateTask.Callback
                public final void onLoadTemplateSuccess(List list) {
                    SmartEffectFragment.AnonymousClass1.m1760$r8$lambda$CD97bPMfpsb8BHe951Xul3jHtk(SmartEffectFragment.AnonymousClass1.this, videoEditorBaseModel, i, list);
                }
            });
        }

        public /* synthetic */ void lambda$onCommandSuccess$0(VideoEditorBaseModel videoEditorBaseModel, int i, List list) {
            SmartEffectFragment.this.mSmartEffectManager.updateDataWithTemplate(list, (SmartEffect) videoEditorBaseModel);
            SmartEffectFragment.this.notifyDateSetChanged(i);
        }

        @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
        public void onTaskCancel(VideoEditorBaseModel videoEditorBaseModel, int i) {
            int downloadState;
            if (videoEditorBaseModel == null || (downloadState = videoEditorBaseModel.getDownloadState()) == 0 || downloadState == 17) {
                return;
            }
            videoEditorBaseModel.setDownloadState(20);
            SmartEffectFragment.this.notifyDateSetChanged(i);
        }
    }

    public /* synthetic */ void lambda$initListener$1(View view) {
        doCancel();
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public List<String> getCurrentEffect() {
        SmartEffect smartEffect;
        SmartEffectRecyclerViewAdapter smartEffectRecyclerViewAdapter = this.mAdapter;
        if (smartEffectRecyclerViewAdapter == null || (smartEffect = smartEffectRecyclerViewAdapter.getSmartEffect(smartEffectRecyclerViewAdapter.getSelectedItemPosition())) == null) {
            return null;
        }
        return Arrays.asList(smartEffect.getLabel());
    }

    public boolean doApply() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor == null) {
            DefaultLogger.d("SmartEffectFragment", "doApply: videoEditor is null.");
            return false;
        }
        videoEditor.saveEditState();
        this.mSavedSelectedSmartEffectIndex = this.mAdapter.getSelectedItemPosition();
        recordEventWithApply();
        onExitMode();
        return true;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public boolean doCancel() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor == null) {
            DefaultLogger.d("SmartEffectFragment", "doCancel: videoEditor is null.");
            return false;
        }
        videoEditor.restoreEditState();
        return this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.2
            {
                SmartEffectFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public void onCompleted() {
                VideoEditor videoEditor2 = SmartEffectFragment.this.mVideoEditor;
                if (videoEditor2 == null) {
                    return;
                }
                videoEditor2.play();
                SmartEffectFragment.this.recordEventWithCancel();
                SmartEffectFragment.this.onExitMode();
            }
        });
    }

    public void loadResourceData() {
        this.mHasLoadData = false;
        VideoEditorResourceRequest videoEditorResourceRequest = new VideoEditorResourceRequest(getEffectId(), this.mModuleFactory);
        this.mVideoEditorResourceRequest = videoEditorResourceRequest;
        videoEditorResourceRequest.execute(new AnonymousClass3());
    }

    /* renamed from: com.miui.gallery.video.editor.ui.SmartEffectFragment$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements ResponseListener {
        public AnonymousClass3() {
            SmartEffectFragment.this = r1;
        }

        @Override // com.miui.gallery.net.base.ResponseListener
        public void onResponse(Object... objArr) {
            SmartEffectFragment smartEffectFragment = SmartEffectFragment.this;
            smartEffectFragment.mHasLoadData = true;
            smartEffectFragment.refreshData((List) objArr[0]);
        }

        @Override // com.miui.gallery.net.base.ResponseListener
        public void onResponseError(final ErrorCode errorCode, String str, Object obj) {
            DefaultLogger.d("SmartEffectFragment", "errorCode: %s", errorCode);
            SmartEffectFragment.this.mHasLoadData = true;
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.3.1
                {
                    AnonymousClass3.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (SmartEffectFragment.this.mSmartEffects != null && SmartEffectFragment.this.mSmartEffects.size() == 0) {
                        SmartEffectFragment.this.mSmartEffects.add(new SmartEffect("smart_effect_none", R.drawable.video_editor_icon_smart_effect_none, "ve_type_none", false));
                        if (SmartEffectFragment.this.mAdapter != null) {
                            SmartEffectFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                    if (errorCode == ErrorCode.NETWORK_NOT_CONNECTED) {
                        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.3.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                ToastUtils.makeText(SmartEffectFragment.this.mContext, (int) R.string.video_editor_download_failed_for_notwork);
                            }
                        });
                    }
                }
            });
        }
    }

    public final void refreshData(List<VideoEditorBaseLocalResource> list) {
        List<LocalResource> localTemplateEntities = this.mModuleFactory.getLocalTemplateEntities(this.mContext);
        if (list != null) {
            localTemplateEntities.addAll(list);
        }
        final ArrayList arrayList = new ArrayList();
        arrayList.addAll(SmartEffectManager.loadSmartEffects(localTemplateEntities));
        NexAssetTemplateManager.getInstance().checkExpiredAsset(new NexAssetTemplateManager.ICheckExpiredAssetListener() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.4
            {
                SmartEffectFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.manager.NexAssetTemplateManager.ICheckExpiredAssetListener
            public void onFinished(List<nexTemplateManager.Template> list2) {
                SmartEffectFragment.this.mSmartEffectManager.initDataWithTemplate(list2, arrayList);
                ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.4.1
                    {
                        AnonymousClass4.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        SmartEffectFragment.this.mSmartEffects.clear();
                        SmartEffectFragment.this.mSmartEffects.addAll(arrayList);
                        if (SmartEffectFragment.this.mAdapter != null) {
                            SmartEffectFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    /* loaded from: classes2.dex */
    public class SmartEffectItemSelectChangeListener implements SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener {
        public SmartEffectItemSelectChangeListener() {
            SmartEffectFragment.this = r1;
        }

        public /* synthetic */ SmartEffectItemSelectChangeListener(SmartEffectFragment smartEffectFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener
        public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z) {
            if (z) {
                SmartEffectFragment smartEffectFragment = SmartEffectFragment.this;
                if (smartEffectFragment.mHasLoadData) {
                    ScrollHelper.onItemClick(smartEffectFragment.mSingleChoiceRecyclerView, i);
                    SmartEffect smartEffect = ((SmartEffectRecyclerViewAdapter) singleChoiceRecyclerViewAdapter).getSmartEffect(i);
                    if (smartEffect != null) {
                        if (smartEffect.isNone()) {
                            SmartEffectFragment.this.updateSelectedItemPosition(i);
                            SmartEffectFragment.this.mVideoEditor.setSmartEffectTemplate(smartEffect);
                            return SmartEffectFragment.this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.SmartEffectItemSelectChangeListener.1
                                {
                                    SmartEffectItemSelectChangeListener.this = this;
                                }

                                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                                public void onCompleted() {
                                    SmartEffectFragment.this.mVideoEditor.play();
                                    SmartEffectFragment.this.recordEventWithEffectChanged();
                                    SmartEffectFragment.this.updatePlayBtnView();
                                }
                            });
                        } else if (smartEffect.isExtra()) {
                            if (smartEffect.isDownloaded()) {
                                if (SmartEffectFragment.this.mToast != null) {
                                    SmartEffectFragment.this.mToast.cancel();
                                }
                                int dimensionPixelSize = SmartEffectFragment.this.getResources().getDimensionPixelSize(R.dimen.video_editor_video_smart_effect_toast_gap);
                                if (SmartEffectFragment.this.mVideoTotalTime < smartEffect.getShortTime()) {
                                    SmartEffectFragment smartEffectFragment2 = SmartEffectFragment.this;
                                    smartEffectFragment2.mToast = Toast.makeText(smartEffectFragment2.mContext, smartEffectFragment2.getString(R.string.video_editor_smart_effect_time_limit_txt_6, 6), 0);
                                    SmartEffectFragment.this.mToast.setGravity(80, 0, SmartEffectFragment.this.mLayout.getHeight() - dimensionPixelSize);
                                    SmartEffectFragment.this.mToast.show();
                                    return false;
                                }
                                if (!smartEffect.isHasSpeedChange() || SmartEffectFragment.this.isHighFrames()) {
                                    if (SmartEffectFragment.this.mVideoTotalTime <= smartEffect.getLongTime() || !smartEffect.isLimitSixtySeconds()) {
                                        if (SmartEffectFragment.this.mVideoTotalTime > smartEffect.getLongTime() && smartEffect.isLimitFourtySeconds()) {
                                            SmartEffectFragment smartEffectFragment3 = SmartEffectFragment.this;
                                            smartEffectFragment3.mToast = Toast.makeText(smartEffectFragment3.mContext, smartEffectFragment3.getString(R.string.video_editor_smart_effect_time_limit_txt_40, 40), 0);
                                            SmartEffectFragment.this.mToast.setGravity(80, 0, SmartEffectFragment.this.mLayout.getHeight() - dimensionPixelSize);
                                            SmartEffectFragment.this.mToast.show();
                                        }
                                    } else {
                                        SmartEffectFragment smartEffectFragment4 = SmartEffectFragment.this;
                                        smartEffectFragment4.mToast = Toast.makeText(smartEffectFragment4.mContext, smartEffectFragment4.getString(R.string.video_editor_smart_effect_time_limit_txt_60, 60), 0);
                                        SmartEffectFragment.this.mToast.setGravity(80, 0, SmartEffectFragment.this.mLayout.getHeight() - dimensionPixelSize);
                                        SmartEffectFragment.this.mToast.show();
                                    }
                                } else {
                                    SmartEffectFragment smartEffectFragment5 = SmartEffectFragment.this;
                                    smartEffectFragment5.mToast = Toast.makeText(smartEffectFragment5.mContext, smartEffectFragment5.getString(R.string.video_editor_smart_effect_high_iframes_text, 120), 0);
                                    SmartEffectFragment.this.mToast.setGravity(80, 0, SmartEffectFragment.this.mLayout.getHeight() - dimensionPixelSize);
                                    SmartEffectFragment.this.mToast.show();
                                }
                                SmartEffectFragment.this.updateSelectedItemPosition(i);
                                SmartEffectFragment.this.mVideoEditor.setSmartEffectTemplate(smartEffect);
                                return SmartEffectFragment.this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.SmartEffectItemSelectChangeListener.2
                                    {
                                        SmartEffectItemSelectChangeListener.this = this;
                                    }

                                    @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                                    public void onCompleted() {
                                        SmartEffectFragment.this.mVideoEditor.play();
                                        SmartEffectFragment.this.recordEventWithEffectChanged();
                                        SmartEffectFragment.this.updatePlayBtnView();
                                    }
                                });
                            }
                            SmartEffectFragment.this.mResourceDownloadManager.createDownloadCommand(smartEffect, i);
                        }
                    }
                }
            }
            return false;
        }
    }

    public final void updateSelectedItemPosition(int i) {
        SmartEffectRecyclerViewAdapter smartEffectRecyclerViewAdapter = this.mAdapter;
        if (smartEffectRecyclerViewAdapter != null) {
            smartEffectRecyclerViewAdapter.setSelectedItemPosition(i);
            this.mAdapter.clearLastSelectedPostion();
        }
    }

    public void notifyDateSetChanged(int i) {
        SmartEffectRecyclerViewAdapter smartEffectRecyclerViewAdapter = this.mAdapter;
        if (smartEffectRecyclerViewAdapter != null) {
            smartEffectRecyclerViewAdapter.notifyItemChanged(i, 1);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        Toast toast = this.mToast;
        if (toast != null) {
            toast.cancel();
        }
        SmartEffectRecyclerViewAdapter smartEffectRecyclerViewAdapter = this.mAdapter;
        if (smartEffectRecyclerViewAdapter != null) {
            smartEffectRecyclerViewAdapter.setItemSelectChangeListener(null);
            this.mAdapter = null;
        }
        VideoEditorResourceRequest videoEditorResourceRequest = this.mVideoEditorResourceRequest;
        if (videoEditorResourceRequest != null) {
            videoEditorResourceRequest.cancel();
            this.mVideoEditorResourceRequest = null;
        }
        ResourceDownloadManager resourceDownloadManager = this.mResourceDownloadManager;
        if (resourceDownloadManager != null) {
            resourceDownloadManager.cancel();
            this.mResourceDownloadManager = null;
        }
        LoadSmartEffectTemplateTask loadSmartEffectTemplateTask = this.mLoadSmartEffectTemplateTask;
        if (loadSmartEffectTemplateTask != null) {
            loadSmartEffectTemplateTask.cancel();
            this.mLoadSmartEffectTemplateTask = null;
        }
    }

    public boolean isHighFrames() {
        VideoEditor videoEditor = this.mVideoEditor;
        return videoEditor != null && videoEditor.getVideoFrames() >= 100;
    }

    /* loaded from: classes2.dex */
    public static class LoadSmartEffectTemplateTask {
        public Callback mCallback;
        public Future<List<nexTemplateManager.Template>> mFuture;

        /* loaded from: classes2.dex */
        public interface Callback {
            void onLoadTemplateSuccess(List<nexTemplateManager.Template> list);
        }

        public void execute(Callback callback) {
            Future<List<nexTemplateManager.Template>> future = this.mFuture;
            if (future != null) {
                future.cancel();
            }
            this.mCallback = callback;
            this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<List<nexTemplateManager.Template>>() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.LoadSmartEffectTemplateTask.1
                {
                    LoadSmartEffectTemplateTask.this = this;
                }

                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public List<nexTemplateManager.Template> mo1807run(ThreadPool.JobContext jobContext) {
                    return NexAssetTemplateManager.getInstance().loadSmartEffectTemplateList();
                }
            }, new FutureHandler<List<nexTemplateManager.Template>>() { // from class: com.miui.gallery.video.editor.ui.SmartEffectFragment.LoadSmartEffectTemplateTask.2
                {
                    LoadSmartEffectTemplateTask.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<List<nexTemplateManager.Template>> future2) {
                    if (future2.isCancelled() || LoadSmartEffectTemplateTask.this.mCallback == null) {
                        return;
                    }
                    LoadSmartEffectTemplateTask.this.mCallback.onLoadTemplateSuccess(future2.get());
                }
            });
        }

        public void cancel() {
            Future<List<nexTemplateManager.Template>> future = this.mFuture;
            if (future != null) {
                future.cancel();
                this.mFuture = null;
            }
            this.mCallback = null;
        }
    }
}
