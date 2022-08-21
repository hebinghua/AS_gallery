package com.miui.gallery.magic.matting.preview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MagicDependsModule;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.matting.MattingActivity;
import com.miui.gallery.magic.matting.adapter.StrokeIconItem;
import com.miui.gallery.magic.matting.bean.BackgroundItem;
import com.miui.gallery.magic.matting.bean.MattingItem;
import com.miui.gallery.magic.matting.preview.PreviewFragment;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicMainHandler;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicSamplerSingleton;
import com.miui.gallery.magic.widget.MagicPortraitEditorView;
import com.miui.gallery.magic.widget.portrait.PortraitEditView;
import com.miui.gallery.magic.widget.portrait.PortraitForegroundView;
import com.miui.gallery.magic.widget.portrait.PortraitNode;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class PreviewFragment extends BaseFragment<PreviewPresenter, IPreview$VP> {
    public static long lastClickTime;
    public AnimParams downParams;
    public LottieAnimationView mDownloadMediaEditorAppView;
    public PortraitForegroundView mForeground;
    public LinearLayout mLlMattingView;
    public View mLoading;
    public MagicPortraitEditorView mMagicPortraitEditorView;
    public MattingInvoker mMattingInvoker;
    public Bitmap mOriginBitmap;
    public MattingActivity mRootActivity;
    public MattingInvoker.SegmentResult mSegmentResult;
    public View magicRedo;
    public View magicUndo;
    public TextView tvCancel;
    public TextView tvOk;
    public AnimParams upParams;
    public boolean mIsEdit = false;
    public MagicDependsModule.Callback mInstallMediaEditorCallback = new MagicDependsModule.Callback() { // from class: com.miui.gallery.magic.matting.preview.PreviewFragment.3
        {
            PreviewFragment.this = this;
        }

        @Override // com.miui.gallery.imodule.modules.MagicDependsModule.Callback
        public void onInstallSuccess() {
            if (PreviewFragment.this.mDownloadMediaEditorAppView != null) {
                PreviewFragment.this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$DUU61C6SiXxPRpzZgiqxNaw7ICE(PreviewFragment previewFragment, View view) {
        previewFragment.lambda$initView$0(view);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mMagicPortraitEditorView = (MagicPortraitEditorView) findViewById(R$id.idp_make_photo);
        this.mForeground = (PortraitForegroundView) findViewById(R$id.idp_make_photo_foreground);
        this.mLlMattingView = (LinearLayout) findViewById(R$id.magic_matting_make_ll);
        this.mLoading = findViewById(R$id.magic_matting_loading);
        this.magicRedo = findViewById(R$id.magic_redo);
        this.magicUndo = findViewById(R$id.magic_undo);
        this.tvCancel = (TextView) findViewById(R$id.magic_cancel);
        this.tvOk = (TextView) findViewById(R$id.magic_save);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        this.downParams = build;
        FolmeUtil.setCustomTouchAnim(this.tvCancel, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.tvOk, this.downParams, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.magicRedo, this.downParams, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.magicUndo, this.downParams, null, null, null, true);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(com.miui.gallery.editor.R$id.download_mediaeditor_app_view);
        this.mDownloadMediaEditorAppView = lottieAnimationView;
        lottieAnimationView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.matting.preview.PreviewFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PreviewFragment.$r8$lambda$DUU61C6SiXxPRpzZgiqxNaw7ICE(PreviewFragment.this, view);
            }
        });
        this.mMagicPortraitEditorView.addManualMattingOnLister(new PortraitEditView.OnManualMattingOnLister() { // from class: com.miui.gallery.magic.matting.preview.PreviewFragment.1
            {
                PreviewFragment.this = this;
            }

            @Override // com.miui.gallery.magic.widget.portrait.PortraitEditView.OnManualMattingOnLister
            public void onClick(int i) {
                PreviewFragment previewFragment = PreviewFragment.this;
                previewFragment.mRootActivity.openDoodleFragment(previewFragment.getContract().getOriginBitmap(), PreviewFragment.this.getContract().getSegmentResult(), true, i);
            }

            @Override // com.miui.gallery.magic.widget.portrait.PortraitEditView.OnManualMattingOnLister
            public void firstMove(int i) {
                PreviewFragment.this.mIsEdit = true;
                PreviewFragment.this.getContract().backgroundPaintingSegment(true, i);
            }

            @Override // com.miui.gallery.magic.widget.portrait.PortraitEditView.OnManualMattingOnLister
            public void onCanvasMatrixChange(long j, List<PortraitNode> list, int i, int[] iArr) {
                PreviewFragment previewFragment = PreviewFragment.this;
                previewFragment.mRootActivity.addRedoList(list, previewFragment.mSegmentResult, i, iArr);
            }

            @Override // com.miui.gallery.magic.widget.portrait.PortraitEditView.OnManualMattingOnLister
            public PortraitNode addNewNode(PortraitNode portraitNode) {
                return PortraitNode.copyStickerNode(portraitNode, PreviewFragment.this.getContract().mirrorPerson(portraitNode.getPersonIndex()));
            }
        });
        this.mMagicPortraitEditorView.setMatrixUpdateListener(new MagicPortraitEditorView.OnMatrixUpdateListener() { // from class: com.miui.gallery.magic.matting.preview.PreviewFragment.2
            {
                PreviewFragment.this = this;
            }

            @Override // com.miui.gallery.magic.widget.MagicPortraitEditorView.OnMatrixUpdateListener
            public void onUpdate(Matrix matrix, float f, float f2) {
                if (PreviewFragment.this.mForeground != null) {
                    PreviewFragment.this.mForeground.updateMatrixWithWidthAndHeight(matrix, f, f2);
                }
            }
        });
        if (SystemUiUtil.isWaterFallScreen()) {
            LinearLayout linearLayout = this.mLlMattingView;
            Resources resources = getContext().getResources();
            int i = R$dimen.magic_px_65;
            linearLayout.setPadding((int) resources.getDimension(i), (int) getContext().getResources().getDimension(R$dimen.magic_px_36), (int) getContext().getResources().getDimension(i), 0);
            return;
        }
        this.mLlMattingView.setPadding(0, (int) getContext().getResources().getDimension(R$dimen.magic_px_36), 0, 0);
    }

    public /* synthetic */ void lambda$initView$0(View view) {
        ((MagicDependsModule) ModuleRegistry.getModule(MagicDependsModule.class)).installIfNotExist(getActivity(), this.mInstallMediaEditorCallback, true);
    }

    public void playDownloadMediaEditorAppAnimation() {
        this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
        this.mDownloadMediaEditorAppView.playAnimation();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        this.mRootActivity = (MattingActivity) getBaseActivity();
        if (getContract().selectPhotos(getActivity().getIntent().getData(), this.mOriginBitmap)) {
            getContract().faceDetect(this.mMattingInvoker, this.mSegmentResult);
        }
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public PreviewPresenter getPresenterInstance() {
        return new PreviewPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_matting_preview;
    }

    /* renamed from: com.miui.gallery.magic.matting.preview.PreviewFragment$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements IPreview$VP {
        /* renamed from: $r8$lambda$0Lcs_--Be8NiJiyLdfCX8Ztps1U */
        public static /* synthetic */ void m1037$r8$lambda$0Lcs_Be8NiJiyLdfCX8Ztps1U(AnonymousClass4 anonymousClass4, Bitmap bitmap) {
            anonymousClass4.lambda$loadPreview$0(bitmap);
        }

        public static /* synthetic */ void $r8$lambda$ZX4QSgysmFAzVZrMBpv4PxIXBxQ(AnonymousClass4 anonymousClass4, Bitmap bitmap) {
            anonymousClass4.lambda$loadPreview$1(bitmap);
        }

        public AnonymousClass4() {
            PreviewFragment.this = r1;
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void loadPreview(final Bitmap bitmap, boolean z) {
            if (z) {
                if (Thread.currentThread().getName() == Looper.getMainLooper().getThread().getName()) {
                    autoFuse(PreviewFragment.this.mMagicPortraitEditorView.getNodes());
                    ((BackgroundItem) PreviewFragment.this.getBaseActivity().event(7)).getBackgroundIndex();
                    PreviewFragment.this.mMagicPortraitEditorView.setBitmap(bitmap, false);
                    PreviewFragment previewFragment = PreviewFragment.this;
                    previewFragment.mRootActivity.addRedoList(previewFragment.mMagicPortraitEditorView.getNodes(), PreviewFragment.this.mSegmentResult, -1, 1);
                    return;
                }
                autoFuse(PreviewFragment.this.mMagicPortraitEditorView.getNodes());
                ((BackgroundItem) PreviewFragment.this.getBaseActivity().event(7)).getBackgroundIndex();
                MagicMainHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.preview.PreviewFragment$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PreviewFragment.AnonymousClass4.m1037$r8$lambda$0Lcs_Be8NiJiyLdfCX8Ztps1U(PreviewFragment.AnonymousClass4.this, bitmap);
                    }
                });
            } else if (Thread.currentThread().getName() == Looper.getMainLooper().getThread().getName()) {
                PreviewFragment.this.mMagicPortraitEditorView.setBitmap(bitmap, false);
            } else {
                MagicMainHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.preview.PreviewFragment$4$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        PreviewFragment.AnonymousClass4.$r8$lambda$ZX4QSgysmFAzVZrMBpv4PxIXBxQ(PreviewFragment.AnonymousClass4.this, bitmap);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$loadPreview$0(Bitmap bitmap) {
            PreviewFragment.this.mMagicPortraitEditorView.setBitmap(bitmap, false);
            PreviewFragment previewFragment = PreviewFragment.this;
            previewFragment.mRootActivity.addRedoList(previewFragment.mMagicPortraitEditorView.getNodes(), PreviewFragment.this.mSegmentResult, -1, 1);
        }

        public /* synthetic */ void lambda$loadPreview$1(Bitmap bitmap) {
            PreviewFragment.this.mMagicPortraitEditorView.setBitmap(bitmap, false);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public boolean selectPhotos(Uri uri, Bitmap bitmap) {
            return ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().selectPhotos(uri, bitmap);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void faceDetect(MattingInvoker mattingInvoker, MattingInvoker.SegmentResult segmentResult) {
            ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().faceDetect(mattingInvoker, segmentResult);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void setBackground(Bitmap bitmap) {
            boolean z = true;
            PreviewFragment.this.mIsEdit = true;
            if (bitmap != null) {
                MagicPortraitEditorView magicPortraitEditorView = PreviewFragment.this.mMagicPortraitEditorView;
                if (bitmap != getOriginBitmap()) {
                    z = false;
                }
                magicPortraitEditorView.isBackgroundOrigin(z);
            } else {
                PreviewFragment.this.mMagicPortraitEditorView.isBackgroundOrigin(true);
            }
            ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().setBackground(bitmap);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void addPortraitNode(Bitmap bitmap, long j, Rect rect, int i) {
            PreviewFragment.this.mMagicPortraitEditorView.add(bitmap, j, rect, i);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void setStrokeLine(StrokeIconItem strokeIconItem) {
            ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().setStrokeLine(strokeIconItem);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void changeSticker(PortraitNode portraitNode, Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            PreviewFragment.this.mMagicPortraitEditorView.changePersonBitmap(bitmap);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public Bitmap getOriginBitmap() {
            return ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().getOriginBitmap();
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public MattingInvoker.SegmentResult getSegmentResult() {
            return ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().getSegmentResult();
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void save(PortraitEditView.Portrait portrait) {
            ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().save(portrait);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public Bitmap getBackgroundBit() {
            return ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().getBackgroundBit();
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void addPersonForegroundToView(MattingInvoker.SegmentResult segmentResult, Bitmap bitmap) {
            PreviewFragment.this.mForeground.addPersonForegroundToView(segmentResult, bitmap);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void backgroundPaintingSegment(boolean z, int i) {
            ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().backgroundPaintingSegment(true, i);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public PortraitNode getCurrentPerson() {
            return PreviewFragment.this.mMagicPortraitEditorView.getCurrentItem();
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void firstAddNode() {
            if (PreviewFragment.this.mRootActivity.mRedoList.size() == 0) {
                PreviewFragment.this.mMagicPortraitEditorView.firstAddNode();
            }
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public int mirrorPerson(int i) {
            return ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().mirrorPerson(i);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public Bitmap getPersonBitmapByNode(PortraitNode portraitNode) {
            return ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().getPersonBitmapByNode(portraitNode);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void autoFuse(List<PortraitNode> list) {
            ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().autoFuse(list);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void redoNotifyCurrentBg(Bitmap bitmap) {
            ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().redoNotifyCurrentBg(bitmap);
        }

        @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
        public void onBackPressed() {
            PreviewFragment.this.onBackPressed();
        }
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IPreview$VP mo1066initContract() {
        return new AnonymousClass4();
    }

    public void onBackPressed() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) activity).removeLoadingDialog();
        }
        if (activity != null) {
            activity.finish();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.magic_cancel) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime <= 500) {
                return;
            }
            lastClickTime = currentTimeMillis;
            getActivity().onBackPressed();
        } else if (id == R$id.magic_save) {
            HashMap hashMap = new HashMap();
            int selectMagicIndex = MagicSamplerSingleton.getInstance().getSelectMagicIndex();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "背景" + selectMagicIndex);
            MagicSampler.getInstance().recordCategory("matting", "save", hashMap);
            getContract().save(this.mMagicPortraitEditorView.export());
        } else if (id == R$id.magic_redo) {
            this.mLoading.setVisibility(0);
            this.mRootActivity.redo();
            this.mLoading.setVisibility(8);
        } else if (id != R$id.magic_undo) {
        } else {
            this.mLoading.setVisibility(0);
            this.mRootActivity.undo();
            this.mLoading.setVisibility(8);
        }
    }

    public void setData(Bitmap bitmap, MattingInvoker mattingInvoker, MattingInvoker.SegmentResult segmentResult) {
        this.mMattingInvoker = mattingInvoker;
        this.mOriginBitmap = bitmap;
        this.mSegmentResult = segmentResult;
    }

    public void changeBackGround(Bitmap bitmap) {
        boolean z = true;
        this.mMagicPortraitEditorView.setBitmap(bitmap, true);
        getContract().redoNotifyCurrentBg(bitmap);
        MagicPortraitEditorView magicPortraitEditorView = this.mMagicPortraitEditorView;
        if (bitmap != getContract().getOriginBitmap()) {
            z = false;
        }
        magicPortraitEditorView.isBackgroundOrigin(z);
    }

    public final void addStroke(PortraitNode portraitNode) {
        this.mMagicPortraitEditorView.reAdd(portraitNode);
    }

    public void showRedo(boolean z, boolean z2) {
        if (!z2) {
            this.magicRedo.setVisibility(0);
            if (((PreviewPresenter) this.mPresenter).getActivityWithSync() != null) {
                ((PreviewPresenter) this.mPresenter).getActivityWithSync().event(8);
            }
        }
        if (z) {
            this.magicRedo.setAlpha(1.0f);
        } else {
            this.magicRedo.setAlpha(0.3f);
        }
    }

    public void showUndo(boolean z, boolean z2) {
        if (!z2) {
            this.magicUndo.setVisibility(0);
        }
        if (z) {
            this.magicUndo.setAlpha(1.0f);
        } else {
            this.magicUndo.setAlpha(0.3f);
        }
    }

    public void clear() {
        ((PreviewPresenter) this.mPresenter).clear();
    }

    public void setLastAnim(int i, int i2) {
        this.upParams = new AnimParams.Builder().setAlpha(0.3f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        AnimParams build = new AnimParams.Builder().setAlpha(0.3f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        if (i <= 1) {
            FolmeUtil.setCustomTouchAnim(this.magicRedo, build, this.upParams, null, null, true);
        } else {
            FolmeUtil.setCustomTouchAnim(this.magicRedo, this.downParams, null, null, null, true);
        }
        if (i2 <= 2) {
            FolmeUtil.setCustomTouchAnim(this.magicUndo, build, this.upParams, null, null, true);
        } else {
            FolmeUtil.setCustomTouchAnim(this.magicUndo, this.downParams, null, null, null, true);
        }
    }

    public void removeNodeByPersonIndex(int i) {
        this.mMagicPortraitEditorView.removeNodeByPersonIndex(i);
    }

    public void refreshNewMattingItem(MattingItem mattingItem) {
        this.mMagicPortraitEditorView.clear();
        List<PortraitNode> portraitNodeList = mattingItem.getPortraitNodeList();
        Collections.sort(portraitNodeList);
        for (PortraitNode portraitNode : portraitNodeList) {
            addStroke(portraitNode.cloneNode());
        }
    }

    public void onChangeSegmentResult(String str) {
        MagicLog.INSTANCE.startLog("matting_getSegment", "根据ID 获取segment对象");
        this.mSegmentResult = MattingInvoker.SegmentResult.loadFromFile(getActivity(), str);
        MagicLog.INSTANCE.endLog("matting_getSegment", "根据ID 获取segment对象");
    }

    public void addNodeToView(PortraitNode portraitNode) {
        portraitNode.setPersonBitmap(getContract().getPersonBitmapByNode(portraitNode));
        this.mMagicPortraitEditorView.addNodeToView(portraitNode);
    }

    public void removeIndex(int i) {
        this.mMagicPortraitEditorView.removeIndex(i);
    }

    public void checkoutPerson(int i) {
        this.mMagicPortraitEditorView.bindItemByPersonIndex(i);
    }

    public void checkIsChangeBackground(Bitmap bitmap, Bitmap bitmap2) {
        if (this.mMagicPortraitEditorView.isInPainting() || bitmap2 != getContract().getOriginBitmap()) {
            return;
        }
        this.mMagicPortraitEditorView.setBitmap(bitmap, true);
        getContract().redoNotifyCurrentBg(bitmap);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        ((MagicDependsModule) ModuleRegistry.getModule(MagicDependsModule.class)).removeInstallListener();
        super.onDestroyView();
    }
}
