package com.miui.gallery.video.editor.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.ui.StartEndSmoothScrollerController;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.TextStyle;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.adapter.WatermarkRecyclerViewAdapter;
import com.miui.gallery.video.editor.factory.WaterMartFactory;
import com.miui.gallery.video.editor.manager.NexAssetTemplateManager;
import com.miui.gallery.video.editor.manager.WaterMarkManager;
import com.miui.gallery.video.editor.model.VideoEditorBaseLocalResource;
import com.miui.gallery.video.editor.model.VideoEditorBaseModel;
import com.miui.gallery.video.editor.net.ResourceDownloadManager;
import com.miui.gallery.video.editor.net.VideoEditorResourceRequest;
import com.miui.gallery.video.editor.ui.WaterMarkFragment;
import com.miui.gallery.video.editor.ui.menu.WaterMarkView;
import com.miui.gallery.video.editor.util.ToolsUtil;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.ScrollHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class WaterMarkFragment extends MenuFragment implements View.OnClickListener {
    public WatermarkRecyclerViewAdapter mAdapter;
    public TextView mAllActionButton;
    public EditText mAutoText;
    public int mCurrentSelectIndex;
    public TextView mEndActionButton;
    public ImageView mIvCancel;
    public ImageView mIvOk;
    public int mLastSelectIndex;
    public TextStyle mLastSelectedTextStyle;
    public LoadWaterMarkTemplateTask mLoadWaterMarkTemplateTask;
    public ResourceDownloadManager mResourceDownloadManager;
    public int mSavedSelectedWaterMarkIndex;
    public ConstraintLayout mSelectTimeLayout;
    public TextStyle mSelectedTextStyle;
    public SingleChoiceRecyclerView mSingleChoiceRecyclerView;
    public TextView mStartActionButton;
    public ObjectAnimator mTextAppearAnimator;
    public ImageView mTextCancel;
    public PopupWindow mTextPopWindow;
    public View mTextRoot;
    public ImageView mTextSave;
    public ConstraintLayout mTitleLayout;
    public VideoEditorResourceRequest mVideoEditorResourceRequest;
    public WaterMarkManager mWaterMarkManager;
    public final long mPopWindowEnterAnimTime = 280;
    public final int AUTO_TEXT_WATER_MARK_INDEX = 1;
    public int mSelectedTextTime = 0;
    public String mInputText = "";
    public ArrayList<TextStyle> mTextStyles = new ArrayList<>();
    public boolean mIsKeyboardShowing = false;
    public boolean mTextEditable = false;
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.9
        {
            WaterMarkFragment.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            DefaultLogger.d("WaterMarkFragment", "---onGlobalLayout start---");
            View rootView = WaterMarkFragment.this.mTextRoot.getRootView();
            int exactScreenHeight = ScreenUtils.getExactScreenHeight(WaterMarkFragment.this.getActivity());
            int height = rootView.getHeight();
            int[] iArr = new int[2];
            rootView.getLocationOnScreen(iArr);
            int i = (exactScreenHeight - height) - iArr[1];
            if (!WaterMarkFragment.this.mIsKeyboardShowing && i > 0) {
                DefaultLogger.d("WaterMarkFragment", "soft keyboard start to show!");
                WaterMarkFragment.this.mIsKeyboardShowing = true;
                WaterMarkFragment.this.mTextAppearAnimator.start();
            }
            if (i != 0 || !WaterMarkFragment.this.mIsKeyboardShowing) {
                return;
            }
            DefaultLogger.d("WaterMarkFragment", "soft keyboard start to dismiss!");
            WaterMarkFragment.this.dismissTextPopWindow();
            WaterMarkFragment waterMarkFragment = WaterMarkFragment.this;
            waterMarkFragment.mIsKeyboardShowing = true ^ waterMarkFragment.applyPlay();
        }
    };

    public static /* synthetic */ void $r8$lambda$388TRtIBaXaz0OqHZ9ZYIO6WGSs(WaterMarkFragment waterMarkFragment, View view) {
        waterMarkFragment.lambda$initListener$0(view);
    }

    public static /* synthetic */ void $r8$lambda$oK2wnUDMQ_ayaYzPHZXamjiXDAc(WaterMarkFragment waterMarkFragment, View view) {
        waterMarkFragment.lambda$initListener$1(view);
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public int getEffectId() {
        return R.id.video_editor_water_mark;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWaterMarkManager = new WaterMarkManager();
        this.mTextStyles = WaterMarkManager.initDataWithBgColor();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new WaterMarkView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mStartActionButton = (TextView) view.findViewById(R.id.video_editor_text_start);
        this.mEndActionButton = (TextView) view.findViewById(R.id.video_editor_text_end);
        this.mAllActionButton = (TextView) view.findViewById(R.id.video_editor_text_all);
        this.mSelectTimeLayout = (ConstraintLayout) view.findViewById(R.id.select_time_layout);
        this.mTitleLayout = (ConstraintLayout) view.findViewById(R.id.title_layout);
        this.mIvCancel = (ImageView) view.findViewById(R.id.cancel);
        this.mIvOk = (ImageView) view.findViewById(R.id.ok);
        if (ToolsUtil.isRTLDirection()) {
            this.mStartActionButton.setGravity(8388627);
            this.mEndActionButton.setGravity(8388629);
        }
        this.mModuleFactory = new WaterMartFactory();
        this.mResourceDownloadManager = new ResourceDownloadManager(view.getContext(), getFragmentManager(), this.mModuleFactory);
        this.mLoadWaterMarkTemplateTask = new LoadWaterMarkTemplateTask();
        initRecyclerView(view);
        updateBottomBtnState();
        initListener();
        initEditPopWindow();
        initEnterAnim();
        loadResourceData();
    }

    public final void initRecyclerView(View view) {
        this.mSingleChoiceRecyclerView = (SingleChoiceRecyclerView) view.findViewById(R.id.recycler_view);
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(this.mContext, 0, false);
        customScrollerLinearLayoutManager.setSmoothScroller(new StartEndSmoothScrollerController(this.mContext));
        this.mSingleChoiceRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        WatermarkRecyclerViewAdapter watermarkRecyclerViewAdapter = new WatermarkRecyclerViewAdapter(this.mContext, this.mTextStyles);
        this.mAdapter = watermarkRecyclerViewAdapter;
        watermarkRecyclerViewAdapter.setItemSelectChangeListener(new MyTextItemSelectChangeListener(this, null));
        this.mSingleChoiceRecyclerView.setAdapter(this.mAdapter);
        this.mSingleChoiceRecyclerView.addItemDecoration(new BlankDivider(getResources(), R.dimen.editor_water_mark_item_gap));
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mSingleChoiceRecyclerView);
        this.mAdapter.setSelectedItemPosition(this.mSavedSelectedWaterMarkIndex);
        updateBottomBtnTitle(this.mSavedSelectedWaterMarkIndex);
    }

    public final void initEditPopWindow() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.video_editor_fragment_text_pop_window, (ViewGroup) null);
        this.mTextRoot = inflate;
        this.mAutoText = (EditText) inflate.findViewById(R.id.et_pop_window);
        this.mTextCancel = (ImageView) this.mTextRoot.findViewById(R.id.video_editor_btn_cancel);
        this.mTextSave = (ImageView) this.mTextRoot.findViewById(R.id.video_editor_btn_ok);
        PopupWindow popupWindow = new PopupWindow(this.mTextRoot, -1, -1, true);
        this.mTextPopWindow = popupWindow;
        popupWindow.setFocusable(true);
        this.mTextPopWindow.setOutsideTouchable(true);
        this.mTextPopWindow.setBackgroundDrawable(new BitmapDrawable());
        this.mTextPopWindow.setSoftInputMode(16);
        this.mTextCancel.setOnClickListener(this);
        this.mTextSave.setOnClickListener(this);
    }

    public final void updateBottomBtnState() {
        int i = this.mSelectedTextTime;
        if (i == 0) {
            this.mStartActionButton.setSelected(true);
            this.mEndActionButton.setSelected(false);
            this.mAllActionButton.setSelected(false);
        } else if (i == 1) {
            this.mStartActionButton.setSelected(false);
            this.mEndActionButton.setSelected(true);
            this.mAllActionButton.setSelected(false);
        } else if (i != 2) {
        } else {
            this.mStartActionButton.setSelected(false);
            this.mEndActionButton.setSelected(false);
            this.mAllActionButton.setSelected(true);
        }
    }

    public final void initListener() {
        this.mStartActionButton.setOnClickListener(this);
        this.mEndActionButton.setOnClickListener(this);
        this.mAllActionButton.setOnClickListener(this);
        this.mIvCancel.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WaterMarkFragment.$r8$lambda$388TRtIBaXaz0OqHZ9ZYIO6WGSs(WaterMarkFragment.this, view);
            }
        });
        this.mIvOk.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WaterMarkFragment.$r8$lambda$oK2wnUDMQ_ayaYzPHZXamjiXDAc(WaterMarkFragment.this, view);
            }
        });
        this.mResourceDownloadManager.setDownloadTaskListener(new AnonymousClass1());
    }

    public /* synthetic */ void lambda$initListener$0(View view) {
        doCancel();
    }

    /* renamed from: com.miui.gallery.video.editor.ui.WaterMarkFragment$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ResourceDownloadManager.IDownloadTaskListener {
        public static /* synthetic */ void $r8$lambda$a9Jg3coaWbcKNHkTPvR4tOb6sHE(AnonymousClass1 anonymousClass1, VideoEditorBaseModel videoEditorBaseModel, int i, String[] strArr) {
            anonymousClass1.lambda$onCommandSuccess$0(videoEditorBaseModel, i, strArr);
        }

        public AnonymousClass1() {
            WaterMarkFragment.this = r1;
        }

        @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandStart(VideoEditorBaseModel videoEditorBaseModel, int i) {
            if (videoEditorBaseModel != null) {
                videoEditorBaseModel.setDownloadState(18);
                WaterMarkFragment.this.notifyDateSetChanged(i);
            }
        }

        @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandSuccess(final VideoEditorBaseModel videoEditorBaseModel, final int i) {
            WaterMarkFragment.this.mLoadWaterMarkTemplateTask.execute(new LoadWaterMarkTemplateTask.Callback() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment$1$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.video.editor.ui.WaterMarkFragment.LoadWaterMarkTemplateTask.Callback
                public final void onLoadTemplateSuccess(String[] strArr) {
                    WaterMarkFragment.AnonymousClass1.$r8$lambda$a9Jg3coaWbcKNHkTPvR4tOb6sHE(WaterMarkFragment.AnonymousClass1.this, videoEditorBaseModel, i, strArr);
                }
            });
        }

        public /* synthetic */ void lambda$onCommandSuccess$0(VideoEditorBaseModel videoEditorBaseModel, int i, String[] strArr) {
            WaterMarkManager.updateDataWithTemplate(strArr, (TextStyle) videoEditorBaseModel);
            WaterMarkFragment.this.notifyDateSetChanged(i);
        }

        @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandFail(VideoEditorBaseModel videoEditorBaseModel, int i) {
            if (videoEditorBaseModel != null) {
                videoEditorBaseModel.setDownloadState(20);
                WaterMarkFragment.this.notifyDateSetChanged(i);
            }
        }

        @Override // com.miui.gallery.video.editor.net.ResourceDownloadManager.IDownloadTaskListener
        public void onTaskCancel(VideoEditorBaseModel videoEditorBaseModel, int i) {
            int downloadState;
            if (videoEditorBaseModel == null || (downloadState = videoEditorBaseModel.getDownloadState()) == 0 || downloadState == 17) {
                return;
            }
            videoEditorBaseModel.setDownloadState(20);
            WaterMarkFragment.this.notifyDateSetChanged(i);
        }
    }

    public /* synthetic */ void lambda$initListener$1(View view) {
        doApply();
    }

    public void loadResourceData() {
        this.mHasLoadData = false;
        VideoEditorResourceRequest videoEditorResourceRequest = new VideoEditorResourceRequest(getEffectId(), this.mModuleFactory);
        this.mVideoEditorResourceRequest = videoEditorResourceRequest;
        videoEditorResourceRequest.execute(new AnonymousClass2());
    }

    /* renamed from: com.miui.gallery.video.editor.ui.WaterMarkFragment$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements ResponseListener {
        public AnonymousClass2() {
            WaterMarkFragment.this = r1;
        }

        @Override // com.miui.gallery.net.base.ResponseListener
        public void onResponse(Object... objArr) {
            WaterMarkFragment waterMarkFragment = WaterMarkFragment.this;
            waterMarkFragment.mHasLoadData = true;
            waterMarkFragment.refreshData((List) objArr[0]);
        }

        @Override // com.miui.gallery.net.base.ResponseListener
        public void onResponseError(final ErrorCode errorCode, String str, Object obj) {
            DefaultLogger.d("WaterMarkFragment", "errorCode: %s", errorCode);
            WaterMarkFragment.this.mHasLoadData = true;
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.2.1
                {
                    AnonymousClass2.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (WaterMarkFragment.this.mTextStyles != null && WaterMarkFragment.this.mTextStyles.size() == 0) {
                        TextStyle textStyle = new TextStyle(R.drawable.video_editor_icon_water_mark_none, "ve_type_none");
                        TextStyle textStyle2 = new TextStyle(R.drawable.video_editor_water_mark_text, "ve_type_local");
                        WaterMarkFragment.this.mTextStyles.add(textStyle);
                        WaterMarkFragment.this.mTextStyles.add(textStyle2);
                        if (WaterMarkFragment.this.mAdapter != null) {
                            WaterMarkFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                    if (errorCode == ErrorCode.NETWORK_NOT_CONNECTED) {
                        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.2.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                ToastUtils.makeText(WaterMarkFragment.this.mContext, (int) R.string.video_editor_download_failed_for_notwork);
                            }
                        });
                    }
                    WaterMarkFragment.this.mCurrentSelectIndex = 0;
                    WaterMarkFragment.this.mLastSelectIndex = 0;
                }
            });
        }
    }

    public final void refreshData(List<VideoEditorBaseLocalResource> list) {
        List<LocalResource> localTemplateEntities = this.mModuleFactory.getLocalTemplateEntities(this.mContext);
        if (list != null) {
            localTemplateEntities.addAll(list);
        }
        if (localTemplateEntities.size() > 0) {
            localTemplateEntities.add(1, new LocalResource(R.drawable.video_editor_water_mark_text, "ve_type_local"));
        }
        final ArrayList arrayList = new ArrayList();
        arrayList.addAll(WaterMarkManager.loadWaterMarks(localTemplateEntities));
        NexAssetTemplateManager.getInstance().loadWaterMarkTemplateList(new NexAssetTemplateManager.ILoadWaterMarkListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.3
            {
                WaterMarkFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.manager.NexAssetTemplateManager.ILoadWaterMarkListener
            public void onFinished(String[] strArr) {
                WaterMarkFragment.this.mWaterMarkManager.initDataWithTemplate(strArr, arrayList);
                ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.3.1
                    {
                        AnonymousClass3.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        WaterMarkFragment.this.mTextStyles.clear();
                        WaterMarkFragment.this.mTextStyles.addAll(arrayList);
                        if (WaterMarkFragment.this.mAdapter != null) {
                            WaterMarkFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        hideSoftInput();
        this.mTextPopWindow.dismiss();
        this.mIsKeyboardShowing = false;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public List<String> getCurrentEffect() {
        TextStyle textStyle;
        WatermarkRecyclerViewAdapter watermarkRecyclerViewAdapter = this.mAdapter;
        if (watermarkRecyclerViewAdapter == null || (textStyle = watermarkRecyclerViewAdapter.getTextStyle(watermarkRecyclerViewAdapter.getSelectedItemPosition())) == null) {
            return null;
        }
        return Arrays.asList(textStyle.getLabel());
    }

    public boolean doApply() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor == null) {
            DefaultLogger.d("WaterMarkFragment", "doApply: videoEditor is null.");
            return false;
        }
        return videoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.4
            {
                WaterMarkFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public void onCompleted() {
                WaterMarkFragment waterMarkFragment = WaterMarkFragment.this;
                if (waterMarkFragment.mVideoEditor == null) {
                    return;
                }
                waterMarkFragment.mSavedSelectedWaterMarkIndex = waterMarkFragment.mAdapter.getSelectedItemPosition();
                WaterMarkFragment.this.mVideoEditor.saveEditState();
                WaterMarkFragment.this.recordEventWithApply();
                WaterMarkFragment.this.onExitMode();
                WaterMarkFragment.this.mVideoEditor.play();
            }
        });
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public boolean doCancel() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor == null) {
            DefaultLogger.d("WaterMarkFragment", "doCancel: videoEditor is null.");
            return false;
        }
        videoEditor.restoreEditState();
        return this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.5
            {
                WaterMarkFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public void onCompleted() {
                VideoEditor videoEditor2 = WaterMarkFragment.this.mVideoEditor;
                if (videoEditor2 == null) {
                    return;
                }
                videoEditor2.play();
                WaterMarkFragment.this.recordEventWithCancel();
                WaterMarkFragment.this.onExitMode();
            }
        });
    }

    public void notifyDateSetChanged(int i) {
        WatermarkRecyclerViewAdapter watermarkRecyclerViewAdapter = this.mAdapter;
        if (watermarkRecyclerViewAdapter != null) {
            watermarkRecyclerViewAdapter.notifyItemChanged(i, 1);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.video_editor_text_start) {
            this.mSelectedTextTime = 0;
            updateBottomBtnState();
            updateEffectInfo();
        } else if (view.getId() == R.id.video_editor_text_end) {
            this.mSelectedTextTime = 1;
            updateBottomBtnState();
            updateEffectInfo();
        } else if (view.getId() == R.id.video_editor_text_all) {
            this.mSelectedTextTime = 2;
            updateBottomBtnState();
            updateEffectInfo();
        } else if (view.getId() == R.id.video_editor_btn_ok) {
            this.mIsKeyboardShowing = false;
            this.mInputText = this.mAutoText.getText().toString();
            updateEffectInfo();
            hideSoftInput();
            dismissTextPopWindow();
        } else if (view.getId() == R.id.video_editor_btn_cancel) {
            this.mIsKeyboardShowing = false;
            updateEffectInfo();
            hideSoftInput();
            dismissTextPopWindow();
        }
        applyPlay();
    }

    /* loaded from: classes2.dex */
    public class MyTextItemSelectChangeListener implements SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener {
        public MyTextItemSelectChangeListener() {
            WaterMarkFragment.this = r1;
        }

        public /* synthetic */ MyTextItemSelectChangeListener(WaterMarkFragment waterMarkFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener
        public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, final int i, boolean z) {
            WaterMarkFragment waterMarkFragment = WaterMarkFragment.this;
            if (!waterMarkFragment.mHasLoadData) {
                return false;
            }
            ScrollHelper.onItemClick(waterMarkFragment.mSingleChoiceRecyclerView, i);
            final TextStyle textStyle = ((WatermarkRecyclerViewAdapter) singleChoiceRecyclerViewAdapter).getTextStyle(i);
            if (textStyle != null && (z || textStyle.isLocal())) {
                if (textStyle.isNone()) {
                    WaterMarkFragment.this.updateBottomBtnTitle(i);
                    WaterMarkFragment.this.mTextEditable = false;
                    WaterMarkFragment waterMarkFragment2 = WaterMarkFragment.this;
                    waterMarkFragment2.mVideoEditor.setAutoWaterMark("", waterMarkFragment2.mSelectedTextTime);
                    WaterMarkFragment waterMarkFragment3 = WaterMarkFragment.this;
                    waterMarkFragment3.mVideoEditor.setWarterMark(waterMarkFragment3.mSelectedTextTime, "");
                    return WaterMarkFragment.this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.MyTextItemSelectChangeListener.1
                        {
                            MyTextItemSelectChangeListener.this = this;
                        }

                        @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                        public void onCompleted() {
                            WaterMarkFragment.this.mVideoEditor.play();
                            WaterMarkFragment.this.updateSelectTextStyleStatus(textStyle, i);
                            WaterMarkFragment.this.updateSelectedItemPosition(i);
                            WaterMarkFragment.this.recordEventWithEffectChanged();
                            WaterMarkFragment.this.updatePlayBtnView();
                        }
                    });
                } else if (textStyle.isExtra()) {
                    WaterMarkFragment.this.mTextEditable = false;
                    if (textStyle.isDownloaded()) {
                        WaterMarkFragment.this.updateBottomBtnTitle(i);
                        WaterMarkFragment.this.updateBottomBtnState();
                        WaterMarkFragment waterMarkFragment4 = WaterMarkFragment.this;
                        waterMarkFragment4.mVideoEditor.setAutoWaterMark("", waterMarkFragment4.mSelectedTextTime);
                        WaterMarkFragment waterMarkFragment5 = WaterMarkFragment.this;
                        waterMarkFragment5.mVideoEditor.setWarterMark(waterMarkFragment5.mSelectedTextTime, textStyle.getTemplateId());
                        return WaterMarkFragment.this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.MyTextItemSelectChangeListener.2
                            {
                                MyTextItemSelectChangeListener.this = this;
                            }

                            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                            public void onCompleted() {
                                WaterMarkFragment.this.mVideoEditor.play();
                                WaterMarkFragment.this.updateSelectTextStyleStatus(textStyle, i);
                                WaterMarkFragment.this.updateSelectedItemPosition(i);
                                WaterMarkFragment.this.recordEventWithEffectChanged();
                                WaterMarkFragment.this.updatePlayBtnView();
                            }
                        });
                    }
                    WaterMarkFragment.this.mResourceDownloadManager.createDownloadCommand(textStyle, i);
                } else if (textStyle.isLocal()) {
                    WaterMarkFragment.this.updateSelectTextStyleStatus(textStyle, i);
                    if (!TextUtils.isEmpty(WaterMarkFragment.this.mInputText)) {
                        if (!WaterMarkFragment.this.mTextEditable) {
                            WaterMarkFragment.this.updateEffectInfo();
                            WaterMarkFragment.this.applyPlay();
                        } else {
                            WaterMarkFragment.this.showEditPopWindow();
                            WaterMarkFragment.this.showSoftInput();
                            WaterMarkFragment.this.mVideoEditor.pause();
                        }
                    } else {
                        WaterMarkFragment.this.showEditPopWindow();
                        WaterMarkFragment.this.showSoftInput();
                        WaterMarkFragment.this.mVideoEditor.pause();
                    }
                    WaterMarkFragment.this.updateBottomBtnState();
                }
            }
            return false;
        }
    }

    public final void dismissTextPopWindow() {
        this.mTextPopWindow.dismiss();
    }

    public final void showEditPopWindow() {
        this.mTextRoot.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        this.mTextPopWindow.showAtLocation(this.mSingleChoiceRecyclerView, 51, 0, 0);
        this.mTextRoot.setAlpha(0.0f);
    }

    public final void initEnterAnim() {
        if (this.mTextRoot == null) {
            return;
        }
        this.mTextAppearAnimator = new ObjectAnimator();
        this.mTextAppearAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getResources().getDimensionPixelSize(R.dimen.video_editor_water_mark_in_distance), 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
        this.mTextAppearAnimator.setInterpolator(new CubicEaseOutInterpolator());
        this.mTextAppearAnimator.setDuration(280L);
        this.mTextAppearAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.6
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            {
                WaterMarkFragment.this = this;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                DefaultLogger.d("WaterMarkFragment", "water mark popwinow appear animation start!");
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!TextUtils.isEmpty(WaterMarkFragment.this.mInputText)) {
                    WaterMarkFragment.this.mAutoText.setText(WaterMarkFragment.this.mInputText);
                    WaterMarkFragment.this.mAutoText.setSelection(WaterMarkFragment.this.mInputText.length());
                }
                DefaultLogger.d("WaterMarkFragment", "water mark popwinow appear animation end!");
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                DefaultLogger.d("WaterMarkFragment", "water mark popwinow appear animation cancel!");
            }
        });
        this.mTextAppearAnimator.setTarget(this.mTextRoot);
    }

    public final void hideSoftInput() {
        InputMethodManager inputMethodManager;
        EditText editText = this.mAutoText;
        if (editText == null || (inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method")) == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(this.mAutoText.getWindowToken(), 0);
    }

    public final void showSoftInput() {
        EditText editText = this.mAutoText;
        if (editText == null) {
            return;
        }
        editText.requestFocus();
        this.mAutoText.requestFocusFromTouch();
        this.mAutoText.postDelayed(new Runnable() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.7
            {
                WaterMarkFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                Context context = WaterMarkFragment.this.mAutoText.getContext();
                WaterMarkFragment.this.getActivity();
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(WaterMarkFragment.this.mAutoText, 0);
                }
            }
        }, 100L);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        this.mSavedSelectedWaterMarkIndex = this.mAdapter.getSelectedItemPosition();
    }

    public final void updateWithInputNoText() {
        WatermarkRecyclerViewAdapter watermarkRecyclerViewAdapter = this.mAdapter;
        if (watermarkRecyclerViewAdapter == null) {
            return;
        }
        int selectedItemPosition = watermarkRecyclerViewAdapter.getSelectedItemPosition();
        TextStyle textStyle = this.mLastSelectedTextStyle;
        if (textStyle == null || textStyle.isLocal()) {
            updateSelectedItemPosition(selectedItemPosition);
            updateBottomBtnTitle(selectedItemPosition);
            this.mVideoEditor.setAutoWaterMark("", this.mSelectedTextTime);
            return;
        }
        this.mSelectedTextStyle = this.mLastSelectedTextStyle;
        updateSelectedItemPosition(this.mCurrentSelectIndex);
        updateBottomBtnTitle(this.mCurrentSelectIndex);
        this.mVideoEditor.setAutoWaterMark("", this.mSelectedTextTime);
        this.mVideoEditor.setWarterMark(this.mSelectedTextTime, this.mSelectedTextStyle.getTemplateId());
    }

    public final void updateEffectInfo() {
        TextStyle textStyle = this.mSelectedTextStyle;
        if (textStyle != null) {
            if (textStyle.isLocal()) {
                if (TextUtils.isEmpty(this.mInputText)) {
                    updateWithInputNoText();
                    return;
                }
                this.mVideoEditor.setWarterMark(this.mSelectedTextTime, "");
                this.mVideoEditor.setAutoWaterMark(this.mInputText, this.mSelectedTextTime);
                updateSelectedItemPosition(this.mCurrentSelectIndex);
                updateBottomBtnTitle(this.mCurrentSelectIndex);
                this.mTextEditable = true;
            } else if (this.mSelectedTextStyle.isNone()) {
                this.mVideoEditor.setAutoWaterMark("", this.mSelectedTextTime);
                this.mVideoEditor.setWarterMark(this.mSelectedTextTime, "");
            } else if (!this.mSelectedTextStyle.isExtra() || !this.mSelectedTextStyle.isDownloaded()) {
            } else {
                this.mVideoEditor.setWarterMark(this.mSelectedTextTime, this.mSelectedTextStyle.getTemplateId());
            }
        }
    }

    public final boolean applyPlay() {
        return this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.8
            {
                WaterMarkFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public void onCompleted() {
                WaterMarkFragment.this.mVideoEditor.play();
                WaterMarkFragment.this.recordEventWithEffectChanged();
                WaterMarkFragment.this.updatePlayBtnView();
            }
        });
    }

    public final void updateSelectedItemPosition(int i) {
        WatermarkRecyclerViewAdapter watermarkRecyclerViewAdapter = this.mAdapter;
        if (watermarkRecyclerViewAdapter != null) {
            watermarkRecyclerViewAdapter.setSelectedItemPosition(i);
            this.mAdapter.clearLastSelectedPostion();
        }
    }

    public final void updateSelectTextStyleStatus(TextStyle textStyle, int i) {
        if (textStyle == null || i < 0) {
            return;
        }
        this.mLastSelectedTextStyle = this.mSelectedTextStyle;
        this.mSelectedTextStyle = textStyle;
        this.mLastSelectIndex = this.mCurrentSelectIndex;
        this.mCurrentSelectIndex = i;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        WatermarkRecyclerViewAdapter watermarkRecyclerViewAdapter = this.mAdapter;
        if (watermarkRecyclerViewAdapter != null) {
            watermarkRecyclerViewAdapter.setItemSelectChangeListener(null);
            this.mAdapter = null;
        }
        VideoEditorResourceRequest videoEditorResourceRequest = this.mVideoEditorResourceRequest;
        if (videoEditorResourceRequest != null) {
            videoEditorResourceRequest.cancel();
        }
        View view = this.mTextRoot;
        if (view != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        }
        PopupWindow popupWindow = this.mTextPopWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        ObjectAnimator objectAnimator = this.mTextAppearAnimator;
        if (objectAnimator != null) {
            objectAnimator.removeAllListeners();
            this.mTextAppearAnimator.cancel();
        }
        ResourceDownloadManager resourceDownloadManager = this.mResourceDownloadManager;
        if (resourceDownloadManager != null) {
            resourceDownloadManager.cancel();
            this.mResourceDownloadManager = null;
        }
        LoadWaterMarkTemplateTask loadWaterMarkTemplateTask = this.mLoadWaterMarkTemplateTask;
        if (loadWaterMarkTemplateTask != null) {
            loadWaterMarkTemplateTask.cancel();
            this.mLoadWaterMarkTemplateTask = null;
        }
        super.onDestroyView();
    }

    public final void updateBottomBtnTitle(int i) {
        int i2 = 0;
        if (i == 0 && this.mAllActionButton.isSelected()) {
            this.mAllActionButton.setSelected(false);
        }
        this.mSelectTimeLayout.setVisibility(i == 0 ? 8 : 0);
        ConstraintLayout constraintLayout = this.mTitleLayout;
        if (i != 0) {
            i2 = 8;
        }
        constraintLayout.setVisibility(i2);
    }

    /* loaded from: classes2.dex */
    public static class LoadWaterMarkTemplateTask {
        public Callback mCallback;
        public Future<String[]> mFuture;

        /* loaded from: classes2.dex */
        public interface Callback {
            void onLoadTemplateSuccess(String[] strArr);
        }

        /* renamed from: $r8$lambda$MgJ92xTWf-kEX-rlyEkCKKWTiGw */
        public static /* synthetic */ void m1766$r8$lambda$MgJ92xTWfkEXrlyEkCKKWTiGw(String[][] strArr, String[] strArr2) {
            lambda$execute$0(strArr, strArr2);
        }

        public final void execute(Callback callback) {
            Future<String[]> future = this.mFuture;
            if (future != null) {
                future.cancel();
            }
            this.mCallback = callback;
            this.mFuture = ThreadManager.getMiscPool().submit(WaterMarkFragment$LoadWaterMarkTemplateTask$$ExternalSyntheticLambda0.INSTANCE, new FutureHandler<String[]>() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment.LoadWaterMarkTemplateTask.1
                {
                    LoadWaterMarkTemplateTask.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<String[]> future2) {
                    if (future2.isCancelled() || LoadWaterMarkTemplateTask.this.mCallback == null) {
                        return;
                    }
                    LoadWaterMarkTemplateTask.this.mCallback.onLoadTemplateSuccess(future2.get());
                }
            });
        }

        public static /* synthetic */ void lambda$execute$0(String[][] strArr, String[] strArr2) {
            strArr[0] = strArr2;
        }

        public static /* synthetic */ String[] lambda$execute$1(ThreadPool.JobContext jobContext) {
            final String[][] strArr = {null};
            NexAssetTemplateManager.getInstance().loadWaterMarkTemplateList(new NexAssetTemplateManager.ILoadWaterMarkListener() { // from class: com.miui.gallery.video.editor.ui.WaterMarkFragment$LoadWaterMarkTemplateTask$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.video.editor.manager.NexAssetTemplateManager.ILoadWaterMarkListener
                public final void onFinished(String[] strArr2) {
                    WaterMarkFragment.LoadWaterMarkTemplateTask.m1766$r8$lambda$MgJ92xTWfkEXrlyEkCKKWTiGw(strArr, strArr2);
                }
            });
            return strArr[0];
        }

        public void cancel() {
            Future<String[]> future = this.mFuture;
            if (future != null) {
                future.cancel();
                this.mFuture = null;
            }
            this.mCallback = null;
        }
    }
}
