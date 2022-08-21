package com.miui.gallery.vlog.caption;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.base.widget.TextEditorView;
import com.miui.gallery.vlog.caption.AddCaptionFragment;
import com.miui.gallery.vlog.caption.CaptionStyleFragment;
import com.miui.gallery.vlog.caption.HeaderTailFragment;
import com.miui.gallery.vlog.caption.ai.AutoCaptionFragment;
import com.miui.gallery.vlog.caption.dialog.CaptionEditorDialogFragment;
import com.miui.gallery.vlog.entity.Caption;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IClipAudioManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoCaptionManager;
import com.miui.gallery.vlog.sdk.models.NvsCompoundCaptionWrapper;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.xiaomi.milab.videosdk.XmsTextureView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CaptionMenuFragment extends MenuFragment implements View.OnClickListener {
    public AddCaptionFragment mAddCaptionFragment;
    public AutoCaptionFragment mAutoCaptionFragment;
    public View mBtnAdd;
    public View mBtnAuto;
    public View mBtnTitle;
    public Button mCaptionClearBtn;
    public MiVideoCaptionManager mCaptionManager;
    public CaptionStyleFragment mCaptionStyleFragment;
    public View mChoosePanel;
    public View mClearCaptionView;
    public IClipAudioManager mClipAudioManager;
    public NvsCompoundCaptionWrapper mCurrentCaption;
    public CaptionEditorDialogFragment mEditorDialogFragment;
    public FrameLayout mEditorPanel;
    public FragmentManager mFragments;
    public HeaderTailFragment mHeaderTailFragment;
    public Fragment mLastFragment;
    public MiVideoSdkManager mMiVideoSdkManager;
    public View mOperationView;
    public FrameLayout mPlayLayout;
    public ImageView mPlayView;
    public XmsTextureView mSurface;
    public TextEditorView mTextEditorView;
    public HeaderTailFragment.Callback mHeaderTailCallback = new HeaderTailFragment.Callback() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.2
        @Override // com.miui.gallery.vlog.caption.HeaderTailFragment.Callback
        public void onCancel() {
            CaptionMenuFragment.this.showMenu();
        }

        @Override // com.miui.gallery.vlog.caption.HeaderTailFragment.Callback
        public void onSave() {
            CaptionMenuFragment.this.showMenu();
        }
    };
    public AddCaptionFragment.Callback mAddCaptionCallback = new AddCaptionFragment.Callback() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.3
        @Override // com.miui.gallery.vlog.caption.AddCaptionFragment.Callback
        public void onCancel() {
            CaptionMenuFragment.this.mCurrentCaption = null;
            CaptionMenuFragment.this.showMenu();
        }

        @Override // com.miui.gallery.vlog.caption.AddCaptionFragment.Callback
        public void onSave() {
            CaptionMenuFragment.this.mCurrentCaption = null;
            CaptionMenuFragment.this.showMenu();
        }

        @Override // com.miui.gallery.vlog.caption.AddCaptionFragment.Callback
        public void onSelectCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
            CaptionMenuFragment.this.mCurrentCaption = nvsCompoundCaptionWrapper;
            if (CaptionMenuFragment.this.mCurrentCaption != null) {
                CaptionMenuFragment.this.updateCaptinEditorCoordinate();
                CaptionMenuFragment.this.showCaptionEditorView(true);
                return;
            }
            CaptionMenuFragment.this.showCaptionEditorView(false);
        }

        @Override // com.miui.gallery.vlog.caption.AddCaptionFragment.Callback
        public void onAddCaption(long j) {
            CaptionMenuFragment.this.showEditCaptionDialog(j);
        }
    };
    public AutoCaptionFragment.Callback mAutoCaptionCallback = new AutoCaptionFragment.Callback() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.4
        @Override // com.miui.gallery.vlog.caption.ai.AutoCaptionFragment.Callback
        public void onCancel() {
        }

        @Override // com.miui.gallery.vlog.caption.ai.AutoCaptionFragment.Callback
        public void onExtracted(List<Caption> list) {
            if (list == null || list.size() <= 0) {
                ToastUtils.makeText(CaptionMenuFragment.this.mContext, R$string.vlog_caption_ai_no_detect);
                return;
            }
            CaptionMenuFragment.this.mCaptionManager.updateLiveWindowSize(CaptionMenuFragment.this.mIVlogView.getLiveWindow().getWidth(), CaptionMenuFragment.this.mIVlogView.getLiveWindow().getHeight());
            CaptionMenuFragment.this.mCaptionManager.replaceCaptions(list);
            CaptionMenuFragment.this.getMiVideoSdkManager().seek(list.get(0).inPoint);
            CaptionMenuFragment.this.showAddCaptionFragment();
        }

        @Override // com.miui.gallery.vlog.caption.ai.AutoCaptionFragment.Callback
        public void onError(String str) {
            ToastUtils.makeText(CaptionMenuFragment.this.getActivity(), R$string.vlog_caption_ai_error);
        }
    };
    public CaptionEditorDialogFragment.CaptionEditorCallback mCaptionEditorCallback = new CaptionEditorDialogFragment.CaptionEditorCallback() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.5
        @Override // com.miui.gallery.vlog.caption.dialog.CaptionEditorDialogFragment.CaptionEditorCallback
        public void onCancel() {
        }

        @Override // com.miui.gallery.vlog.caption.dialog.CaptionEditorDialogFragment.CaptionEditorCallback
        public void onCaptionAdd(String str, long j) {
            CaptionMenuFragment.this.mMiVideoSdkManager.disconnect();
            CaptionMenuFragment captionMenuFragment = CaptionMenuFragment.this;
            captionMenuFragment.mCurrentCaption = captionMenuFragment.mCaptionManager.addCaption(str, j, 0L);
            if (CaptionMenuFragment.this.mCurrentCaption != null) {
                CaptionMenuFragment.this.updateCaptinEditorCoordinate();
                CaptionMenuFragment.this.mAddCaptionFragment.onAddCaption(CaptionMenuFragment.this.mCurrentCaption);
                CaptionMenuFragment.this.mCaptionClearBtn.setEnabled(true);
            }
            CaptionMenuFragment.this.mMiVideoSdkManager.reconnect();
        }

        @Override // com.miui.gallery.vlog.caption.dialog.CaptionEditorDialogFragment.CaptionEditorCallback
        public void onCaptionUpdate(String str) {
            if (CaptionMenuFragment.this.mCurrentCaption != null) {
                CaptionMenuFragment.this.mMiVideoSdkManager.disconnect();
                CaptionMenuFragment.this.mCaptionManager.updateCaptionText(CaptionMenuFragment.this.mCurrentCaption, str);
                CaptionMenuFragment.this.mMiVideoSdkManager.reconnect();
                CaptionMenuFragment.this.updateCaptinEditorCoordinate();
                CaptionMenuFragment.this.mAddCaptionFragment.onEditCaption(CaptionMenuFragment.this.mCurrentCaption);
            }
        }
    };
    public TextEditorView.OnTouchListener mTextEditorTouchListener = new TextEditorView.OnTouchListener() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.8
        @Override // com.miui.gallery.vlog.base.widget.TextEditorView.OnTouchListener
        public void onBeyondDrawRectClick() {
        }

        @Override // com.miui.gallery.vlog.base.widget.TextEditorView.OnTouchListener
        public void onDrag(PointF pointF, PointF pointF2) {
        }

        @Override // com.miui.gallery.vlog.base.widget.TextEditorView.OnTouchListener
        public void onScaleAndRotate(float f, PointF pointF, float f2) {
        }

        @Override // com.miui.gallery.vlog.base.widget.TextEditorView.OnTouchListener
        public void onTouchDown(PointF pointF) {
        }

        @Override // com.miui.gallery.vlog.base.widget.TextEditorView.OnTouchListener
        public void onDel() {
            CaptionMenuFragment.this.mMiVideoSdkManager.disconnect();
            CaptionMenuFragment.this.mCaptionManager.removeCaption(CaptionMenuFragment.this.mCurrentCaption);
            CaptionMenuFragment.this.mMiVideoSdkManager.reconnect();
            CaptionMenuFragment.this.mCaptionClearBtn.setEnabled(CaptionMenuFragment.this.mCaptionManager.hasCaption());
            CaptionMenuFragment.this.showCaptionEditorView(false);
            if (CaptionMenuFragment.this.mAddCaptionFragment != null) {
                CaptionMenuFragment.this.mAddCaptionFragment.onDeleteCaption(CaptionMenuFragment.this.mCurrentCaption);
            }
            CaptionMenuFragment.this.mCurrentCaption = null;
        }

        @Override // com.miui.gallery.vlog.base.widget.TextEditorView.OnTouchListener
        public void onStyleClick() {
            CaptionMenuFragment.this.showCaptionStyleFragment();
        }

        @Override // com.miui.gallery.vlog.base.widget.TextEditorView.OnTouchListener
        public void onEditorClick() {
            CaptionMenuFragment captionMenuFragment = CaptionMenuFragment.this;
            captionMenuFragment.showEditCaptionDialog(captionMenuFragment.getMiVideoSdkManager().getCurrentTimeMicro());
        }
    };
    public CaptionStyleFragment.Callback mStyleCallback = new CaptionStyleFragment.Callback() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.9
        @Override // com.miui.gallery.vlog.caption.CaptionStyleFragment.Callback
        public void onCancel() {
            CaptionMenuFragment.this.updateCaptinEditorCoordinate();
            onSave();
        }

        @Override // com.miui.gallery.vlog.caption.CaptionStyleFragment.Callback
        public void onSave() {
            FragmentTransaction beginTransaction = CaptionMenuFragment.this.mFragments.beginTransaction();
            if (CaptionMenuFragment.this.mLastFragment != null) {
                beginTransaction.hide(CaptionMenuFragment.this.mLastFragment);
            }
            beginTransaction.show(CaptionMenuFragment.this.mAddCaptionFragment).commitAllowingStateLoss();
            CaptionMenuFragment.this.mFragments.executePendingTransactions();
            CaptionMenuFragment captionMenuFragment = CaptionMenuFragment.this;
            captionMenuFragment.mLastFragment = captionMenuFragment.mAddCaptionFragment;
            CaptionMenuFragment.this.mIVlogView.updateCaptionClearView(CaptionMenuFragment.this.mClearCaptionView, true);
        }

        @Override // com.miui.gallery.vlog.caption.CaptionStyleFragment.Callback
        public void onApplyStyle() {
            CaptionMenuFragment.this.updateCaptinEditorCoordinate();
        }

        @Override // com.miui.gallery.vlog.caption.CaptionStyleFragment.Callback
        public void onUpdateProgressView(boolean z) {
            if (z) {
                CaptionMenuFragment.this.mIVlogView.setPlayProgressEnable(true);
                CaptionMenuFragment.this.mIVlogView.showProgressView();
                return;
            }
            CaptionMenuFragment.this.mIVlogView.setPlayProgressEnable(false);
            CaptionMenuFragment.this.mIVlogView.hideProgressView();
        }
    };

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter */
    public BasePresenter mo1801createPresenter() {
        return null;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R$layout.vlog_menu_caption_layout, (ViewGroup) null);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        this.mOperationView = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_caption_operation_layout, (ViewGroup) null, false);
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_caption_clear_btn, (ViewGroup) null, false);
        this.mClearCaptionView = inflate;
        Button button = (Button) inflate.findViewById(R$id.caption_clear_btn);
        this.mCaptionClearBtn = button;
        button.setOnClickListener(this);
        this.mCaptionClearBtn.setEnabled(false);
        FolmeUtil.setCustomTouchAnim(this.mCaptionClearBtn, new AnimParams.Builder().setTint(0.1f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        this.mChoosePanel = getViewById(R$id.choose_panel);
        this.mEditorPanel = (FrameLayout) getViewById(R$id.editor_panel);
        View viewById = getViewById(R$id.btn_caption_add);
        this.mBtnAdd = viewById;
        viewById.setOnClickListener(this);
        View viewById2 = getViewById(R$id.btn_caption_header_tail);
        this.mBtnTitle = viewById2;
        viewById2.setOnClickListener(this);
        View viewById3 = getViewById(R$id.btn_caption_auto);
        this.mBtnAuto = viewById3;
        viewById3.setOnClickListener(this);
        FrameLayout frameLayout = (FrameLayout) this.mOperationView.findViewById(R$id.play_layout);
        this.mPlayLayout = frameLayout;
        frameLayout.setOnClickListener(this);
        FolmeUtil.setCustomTouchAnim(this.mPlayLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
        this.mPlayView = (ImageView) this.mOperationView.findViewById(R$id.play_view);
        TextEditorView textEditorView = new TextEditorView(this.mContext);
        this.mTextEditorView = textEditorView;
        textEditorView.setOnTouchListener(this.mTextEditorTouchListener);
        this.mFragments = getActivity().getSupportFragmentManager();
        MiVideoSdkManager sdkManager = ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).getSdkManager();
        this.mMiVideoSdkManager = sdkManager;
        sdkManager.setOnLiveWindowLayoutUpdatedListener(new MiVideoSdkManager.OnLiveWindowLayoutUpdatedListener() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.1
            @Override // com.miui.gallery.vlog.base.manager.MiVideoSdkManager.OnLiveWindowLayoutUpdatedListener
            public void onLiveWindowLayoutUpdated() {
                if (CaptionMenuFragment.this.mTextEditorView.isShown()) {
                    CaptionMenuFragment.this.updateCaptinEditorCoordinate();
                }
            }
        });
        this.mSurface = getIVlogView().getVlogPlayView().getDisplayView();
        if (BaseBuildUtil.isInternational() || BaseBuildUtil.isBlackShark()) {
            this.mBtnAuto.setVisibility(8);
        } else {
            this.mBtnAuto.setVisibility(0);
        }
        FolmeUtil.setDefaultTouchAnim(this.mBtnAdd, null, true);
        FolmeUtil.setDefaultTouchAnim(this.mBtnTitle, null, true);
        FolmeUtil.setDefaultTouchAnim(this.mBtnAuto, null, true);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getOperationView() {
        return this.mOperationView;
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mCaptionManager = (MiVideoCaptionManager) getMiVideoSdkManager().getManagerService(1);
        this.mClipAudioManager = (IClipAudioManager) getMiVideoSdkManager().getManagerService(7);
        updatePlayViewState(this.mMiVideoSdkManager.isPlay());
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            updatePlayViewState(this.mMiVideoSdkManager.isPlay());
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.btn_caption_add) {
            showAddCaptionFragment();
        } else if (view.getId() == R$id.btn_caption_header_tail) {
            showHeaderTailFragment();
        } else if (view.getId() == R$id.caption_clear_btn) {
            captionClear();
        } else if (view.getId() == R$id.btn_caption_auto) {
            showAutoCaptionFragment();
        } else if (view.getId() != R$id.play_layout) {
        } else {
            doPlayViewClickEvent();
        }
    }

    public final void showAutoCaptionFragment() {
        AutoCaptionFragment autoCaptionFragment = new AutoCaptionFragment();
        this.mAutoCaptionFragment = autoCaptionFragment;
        autoCaptionFragment.setClipAudioManager(this.mClipAudioManager);
        this.mAutoCaptionFragment.setHasCaption(this.mCaptionManager.hasCaption());
        this.mAutoCaptionFragment.setCallback(this.mAutoCaptionCallback);
        this.mAutoCaptionFragment.setVideoClips(getMiVideoSdkManager().getVideoClips());
        this.mAutoCaptionFragment.show(this.mFragments, "ai_caption");
        getMiVideoSdkManager().pause();
    }

    public final void showHeaderTailFragment() {
        this.mCaptionManager.updateLiveWindowSize(this.mIVlogView.getLiveWindow().getWidth(), this.mIVlogView.getLiveWindow().getHeight());
        showEditorPanel(true);
        FragmentTransaction beginTransaction = this.mFragments.beginTransaction();
        Fragment fragment = this.mLastFragment;
        if (fragment != null) {
            beginTransaction.hide(fragment);
        }
        HeaderTailFragment headerTailFragment = (HeaderTailFragment) this.mFragments.findFragmentByTag("title");
        this.mHeaderTailFragment = headerTailFragment;
        if (headerTailFragment == null) {
            HeaderTailFragment headerTailFragment2 = new HeaderTailFragment();
            this.mHeaderTailFragment = headerTailFragment2;
            headerTailFragment2.setCallback(this.mHeaderTailCallback);
            beginTransaction.add(R$id.editor_panel, this.mHeaderTailFragment, "title");
        }
        beginTransaction.show(this.mHeaderTailFragment).commitAllowingStateLoss();
        this.mFragments.executePendingTransactions();
        this.mLastFragment = this.mHeaderTailFragment;
    }

    public final void showCaptionEditorView(boolean z) {
        this.mIVlogView.updateTextEditorView(this.mTextEditorView, z);
    }

    public final void showEditCaptionDialog(long j) {
        if (this.mFragments.findFragmentByTag("edit_caption") != null) {
            return;
        }
        if (this.mEditorDialogFragment == null) {
            CaptionEditorDialogFragment newInstance = CaptionEditorDialogFragment.newInstance();
            this.mEditorDialogFragment = newInstance;
            newInstance.setCaptionEditorCallback(this.mCaptionEditorCallback);
        }
        this.mEditorDialogFragment.setInPoint(j);
        this.mEditorDialogFragment.setContent(MiVideoCaptionManager.getWholeText(this.mCurrentCaption));
        this.mEditorDialogFragment.showAllowingStateLoss(((FragmentActivity) this.mContext).getSupportFragmentManager(), "edit_caption");
    }

    public final void showAddCaptionFragment() {
        this.mCaptionManager.updateLiveWindowSize(this.mIVlogView.getLiveWindow().getWidth(), this.mIVlogView.getLiveWindow().getHeight());
        this.mCaptionManager.captureSnapshot();
        FragmentTransaction beginTransaction = this.mFragments.beginTransaction();
        Fragment fragment = this.mLastFragment;
        if (fragment != null) {
            beginTransaction.hide(fragment);
        }
        AddCaptionFragment addCaptionFragment = (AddCaptionFragment) this.mFragments.findFragmentByTag("add");
        this.mAddCaptionFragment = addCaptionFragment;
        if (addCaptionFragment == null) {
            AddCaptionFragment addCaptionFragment2 = new AddCaptionFragment();
            this.mAddCaptionFragment = addCaptionFragment2;
            addCaptionFragment2.setCaptionManager(this.mCaptionManager);
            this.mAddCaptionFragment.setCallback(this.mAddCaptionCallback);
            beginTransaction.add(R$id.editor_panel, this.mAddCaptionFragment, "add");
        }
        beginTransaction.show(this.mAddCaptionFragment).commitAllowingStateLoss();
        this.mFragments.executePendingTransactions();
        this.mLastFragment = this.mAddCaptionFragment;
        showEditorPanel(true);
        this.mIVlogView.updateCaptionClearView(this.mClearCaptionView, true);
        this.mCaptionClearBtn.setEnabled(this.mCaptionManager.hasCaption());
    }

    public void showTopPanel(boolean z) {
        if (z) {
            updatePlayViewState(this.mMiVideoSdkManager.isPlay());
        }
        this.mIVlogView.updateEffectMenuView(z);
    }

    public final List<PointF> getAssetViewVerticesList(List<PointF> list) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(list);
        int top = this.mIVlogView.getLiveWindow().getTop();
        int left = this.mIVlogView.getLiveWindow().getLeft();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.vlog_caption_edit_view_padding_horizontal);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.vlog_caption_edit_view_padding_vertical);
        float f = dimensionPixelSize;
        ((PointF) arrayList.get(0)).x -= f;
        float f2 = dimensionPixelSize2;
        ((PointF) arrayList.get(0)).y -= f2;
        float f3 = left;
        ((PointF) arrayList.get(0)).x += f3;
        float f4 = top;
        ((PointF) arrayList.get(0)).y += f4;
        ((PointF) arrayList.get(1)).x -= f;
        ((PointF) arrayList.get(1)).y += f2;
        ((PointF) arrayList.get(1)).x += f3;
        ((PointF) arrayList.get(1)).y += f4;
        ((PointF) arrayList.get(2)).x += f;
        ((PointF) arrayList.get(2)).y += f2;
        ((PointF) arrayList.get(2)).x += f3;
        ((PointF) arrayList.get(2)).y += f4;
        ((PointF) arrayList.get(3)).x += f;
        ((PointF) arrayList.get(3)).y -= f2;
        ((PointF) arrayList.get(3)).x += f3;
        ((PointF) arrayList.get(3)).y += f4;
        return arrayList;
    }

    public final void updateCaptinEditorCoordinate() {
        NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper = this.mCurrentCaption;
        if (nvsCompoundCaptionWrapper == null || nvsCompoundCaptionWrapper.mMiCaption == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        float bitmapWidth = this.mCurrentCaption.mMiCaption.getBitmapWidth();
        float bitmapHeight = this.mCurrentCaption.mMiCaption.getBitmapHeight();
        float width = getIVlogView().getVlogPlayView().getDisplayView().getWidth();
        float height = getIVlogView().getVlogPlayView().getDisplayView().getHeight();
        float translationYByRatio = this.mCurrentCaption.mMiCaption.getTranslationYByRatio(this.mMiVideoSdkManager.getCurrentAspectRatio()) * height;
        float scaleByRatio = (width / 1080.0f) * this.mCurrentCaption.mMiCaption.getScaleByRatio(this.mMiVideoSdkManager.getCurrentAspectRatio());
        float f = bitmapWidth * scaleByRatio;
        float f2 = bitmapHeight * scaleByRatio;
        float f3 = (width - f) / 2.0f;
        float dimensionPixelSize = SystemUiUtil.isWaterFallScreen() ? getResources().getDimensionPixelSize(com.miui.gallery.editor.R$dimen.editor_waterfall_screen_horizontal_protect_size) : 0;
        float f4 = f3 + dimensionPixelSize;
        float f5 = (height - f2) / 2.0f;
        float f6 = f5 - translationYByRatio;
        float f7 = f3 + f + dimensionPixelSize;
        float f8 = (f5 + f2) - translationYByRatio;
        PointF pointF = new PointF(f4, f6);
        PointF pointF2 = new PointF(f4, f8);
        PointF pointF3 = new PointF(f7, f8);
        PointF pointF4 = new PointF(f7, f6);
        arrayList.add(pointF);
        arrayList.add(pointF2);
        arrayList.add(pointF3);
        arrayList.add(pointF4);
        this.mTextEditorView.setDrawRect(getAssetViewVerticesList(arrayList), 0);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackTimelinePosition(long j) {
        super.onPlaybackTimelinePosition(j);
    }

    public final void captionClear() {
        Context context = this.mContext;
        DialogUtil.showInfoDialog(context, context.getResources().getString(R$string.vlog_caption_clear_sure_title), this.mContext.getResources().getString(R$string.vlog_caption_clear), 17039370, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                CaptionMenuFragment.this.mMiVideoSdkManager.disconnect();
                CaptionMenuFragment.this.mCaptionManager.clearAllCaption();
                CaptionMenuFragment.this.mMiVideoSdkManager.reconnect();
                CaptionMenuFragment.this.showCaptionEditorView(false);
                if (CaptionMenuFragment.this.mAddCaptionFragment != null) {
                    CaptionMenuFragment.this.mAddCaptionFragment.onClearCaptions();
                }
                CaptionMenuFragment.this.mCaptionClearBtn.setEnabled(false);
                CaptionMenuFragment.this.mCurrentCaption = null;
            }
        }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.vlog.caption.CaptionMenuFragment.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }

    public final void showMenu() {
        showEditorPanel(false);
        this.mIVlogView.updateCaptionClearView(this.mClearCaptionView, false);
        showCaptionEditorView(false);
        this.mIVlogView.setPlayProgressEnable(true);
        this.mIVlogView.showProgressView();
    }

    public void showEditorPanel(boolean z) {
        showTopPanel(!z);
        if (z) {
            this.mChoosePanel.setVisibility(8);
            this.mEditorPanel.setVisibility(0);
            return;
        }
        this.mChoosePanel.setVisibility(0);
        this.mEditorPanel.setVisibility(8);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public boolean onBackPressed() {
        this.mCurrentCaption = null;
        this.mMiVideoSdkManager.pause();
        AddCaptionFragment addCaptionFragment = this.mAddCaptionFragment;
        if (addCaptionFragment != null && addCaptionFragment.isVisible()) {
            return this.mAddCaptionFragment.onCancelPressed();
        }
        CaptionStyleFragment captionStyleFragment = this.mCaptionStyleFragment;
        if (captionStyleFragment != null && captionStyleFragment.isVisible()) {
            return this.mCaptionStyleFragment.onCallPressed();
        }
        HeaderTailFragment headerTailFragment = this.mHeaderTailFragment;
        if (headerTailFragment != null && headerTailFragment.isVisible()) {
            return this.mHeaderTailFragment.onCancelPressed();
        }
        return super.onBackPressed();
    }

    public final void showCaptionStyleFragment() {
        AddCaptionFragment addCaptionFragment = this.mAddCaptionFragment;
        if (addCaptionFragment != null) {
            addCaptionFragment.changeTitleViewStatus(false);
        }
        showEditorPanel(true);
        FragmentTransaction beginTransaction = this.mFragments.beginTransaction();
        Fragment fragment = this.mLastFragment;
        if (fragment != null) {
            beginTransaction.hide(fragment);
        }
        CaptionStyleFragment captionStyleFragment = (CaptionStyleFragment) getActivity().getSupportFragmentManager().findFragmentByTag("style");
        this.mCaptionStyleFragment = captionStyleFragment;
        if (captionStyleFragment == null) {
            CaptionStyleFragment captionStyleFragment2 = new CaptionStyleFragment();
            this.mCaptionStyleFragment = captionStyleFragment2;
            beginTransaction.add(R$id.editor_panel, captionStyleFragment2, "style");
            this.mCaptionStyleFragment.setCallback(this.mStyleCallback);
        }
        beginTransaction.show(this.mCaptionStyleFragment).commitAllowingStateLoss();
        this.mFragments.executePendingTransactions();
        this.mLastFragment = this.mCaptionStyleFragment;
        this.mIVlogView.updateCaptionClearView(this.mClearCaptionView, false);
        showCaptionEditorView(false);
    }

    public void updatePlayViewState(boolean z) {
        if (this.mPlayView.isSelected() != z) {
            this.mPlayView.setSelected(z);
        }
    }

    public final void doPlayViewClickEvent() {
        MiVideoSdkManager miVideoSdkManager = this.mMiVideoSdkManager;
        if (miVideoSdkManager == null) {
            return;
        }
        if (miVideoSdkManager.isPlay()) {
            updatePlayViewState(false);
            this.mPlayLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_play));
            this.mMiVideoSdkManager.pause();
            return;
        }
        updatePlayViewState(true);
        this.mPlayLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_pause));
        this.mMiVideoSdkManager.resume();
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
    public void onDestroy() {
        super.onDestroy();
        CaptionEditorDialogFragment captionEditorDialogFragment = this.mEditorDialogFragment;
        if (captionEditorDialogFragment != null) {
            captionEditorDialogFragment.unRegisterCallback();
            this.mEditorDialogFragment.dismissSafely();
        }
    }
}
