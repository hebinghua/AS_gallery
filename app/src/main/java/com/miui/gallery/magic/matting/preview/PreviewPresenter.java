package com.miui.gallery.magic.matting.preview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import com.miui.gallery.magic.BlendConfig;
import com.miui.gallery.magic.ContourHelper;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.matting.adapter.StrokeIconItem;
import com.miui.gallery.magic.matting.bean.BackgroundItem;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.FileUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicMatrixUtil;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicThreadHandler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.magic.widget.ExportImageFragment;
import com.miui.gallery.magic.widget.MagicLoadingDialog;
import com.miui.gallery.magic.widget.portrait.PortraitEditView;
import com.miui.gallery.magic.widget.portrait.PortraitNode;
import com.miui.gallery.util.ToastUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.milab.videosdk.utils.StringUtils;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class PreviewPresenter extends BasePresenter<PreviewFragment, PreviewModel, IPreview$VP> {
    public volatile boolean isMagicFinish;
    public Bitmap mBackgroundBit;
    public Bitmap mCurrentBitmap;
    public Bitmap mOriginPhoto;
    public String path = "magic_matting";
    public Map<Integer, Bitmap> mPersonBitmapMap = new HashMap();
    public boolean mIsFirst = false;

    /* renamed from: $r8$lambda$3QDX0S-5pcu7VZM_Sb1tk_6qqQ0 */
    public static /* synthetic */ void m1039$r8$lambda$3QDX0S5pcu7VZM_Sb1tk_6qqQ0(PreviewPresenter previewPresenter, int i, boolean z) {
        previewPresenter.lambda$backgroundSegment$4(i, z);
    }

    public static /* synthetic */ void $r8$lambda$6dEvVVkUn_ZOhwBiUwSkvyA94Nc(PreviewPresenter previewPresenter, boolean z) {
        previewPresenter.lambda$backgroundSegment$3(z);
    }

    /* renamed from: $r8$lambda$Dur93JizYfEeBnNN5MaSxpd2-ps */
    public static /* synthetic */ void m1040$r8$lambda$Dur93JizYfEeBnNN5MaSxpd2ps(PreviewPresenter previewPresenter) {
        previewPresenter.lambda$clear$5();
    }

    /* renamed from: $r8$lambda$FZCDnBavPiIJF-bLuoF0H_vKNX8 */
    public static /* synthetic */ void m1041$r8$lambda$FZCDnBavPiIJFbLuoF0H_vKNX8(PreviewPresenter previewPresenter, BaseFragmentActivity baseFragmentActivity, int i) {
        previewPresenter.lambda$segmentPredict$1(baseFragmentActivity, i);
    }

    public static /* synthetic */ void $r8$lambda$XwMLQshTnVHHWmpnGGyTPjGN_CM(PreviewPresenter previewPresenter) {
        previewPresenter.lambda$segmentPredict$0();
    }

    public static /* synthetic */ void $r8$lambda$bBMb26vIs9PzYr79EfnyHRDtGHQ(PreviewPresenter previewPresenter, long j) {
        previewPresenter.lambda$segmentPredict$2(j);
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public PreviewModel getModelInstance() {
        return new PreviewModel(this);
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IPreview$VP mo1070initContract() {
        return new IPreview$VP() { // from class: com.miui.gallery.magic.matting.preview.PreviewPresenter.1
            public boolean mIsSave = false;

            {
                PreviewPresenter.this = this;
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void loadPreview(Bitmap bitmap, boolean z) {
                ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().loadPreview(bitmap, z);
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public boolean selectPhotos(Uri uri, Bitmap bitmap) {
                PreviewPresenter.this.mOriginPhoto = bitmap;
                if (PreviewPresenter.this.mOriginPhoto == null) {
                    PreviewPresenter previewPresenter = PreviewPresenter.this;
                    previewPresenter.mOriginPhoto = ((PreviewModel) previewPresenter.mModel).getContract().decodeOrigin(uri);
                    if (PreviewPresenter.this.mOriginPhoto != null) {
                        if (MagicFileUtil.checkMinPX(PreviewPresenter.this.mOriginPhoto)) {
                            MagicToast.showToast(PreviewPresenter.this.getActivity().getApplicationContext(), R$string.magic_mix_px);
                            PreviewPresenter.this.getActivity().finish();
                            return false;
                        }
                        PreviewPresenter previewPresenter2 = PreviewPresenter.this;
                        previewPresenter2.mCurrentBitmap = previewPresenter2.mOriginPhoto;
                    } else {
                        MagicToast.showToast(PreviewPresenter.this.getActivity().getApplicationContext(), R$string.magic_bitmap_damaged);
                        PreviewPresenter.this.getActivity().finish();
                        return false;
                    }
                }
                PreviewPresenter.this.isMagicFinish = false;
                PreviewPresenter.this.getActivity().event(2, new int[]{PreviewPresenter.this.mOriginPhoto.getWidth(), PreviewPresenter.this.mOriginPhoto.getHeight()});
                loadPreview(PreviewPresenter.this.mOriginPhoto, false);
                return true;
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void faceDetect(MattingInvoker mattingInvoker, MattingInvoker.SegmentResult segmentResult) {
                ((PreviewFragment) PreviewPresenter.this.mView.get()).mMattingInvoker = mattingInvoker;
                if (((PreviewFragment) PreviewPresenter.this.mView.get()).mMattingInvoker == null) {
                    ((PreviewFragment) PreviewPresenter.this.mView.get()).mMattingInvoker = new MattingInvoker();
                }
                ((PreviewFragment) PreviewPresenter.this.mView.get()).mSegmentResult = segmentResult;
                loadPreview(PreviewPresenter.this.mOriginPhoto, false);
                PreviewPresenter.this.segmentPredict();
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void setBackground(Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = PreviewPresenter.this.mBackgroundBit;
                }
                if (PreviewPresenter.this.mCurrentBitmap == bitmap) {
                    return;
                }
                PreviewPresenter.this.mCurrentBitmap = bitmap;
                loadPreview(bitmap, true);
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void addPortraitNode(Bitmap bitmap, long j, Rect rect, int i) {
                ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().addPortraitNode(bitmap, j, rect, i);
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void setStrokeLine(StrokeIconItem strokeIconItem) {
                PortraitNode currentPerson = getCurrentPerson();
                if (currentPerson == null || currentPerson.getPersonIndex() < 0) {
                    return;
                }
                currentPerson.setIsEmpty(strokeIconItem.isEmpty());
                currentPerson.getConfigure(strokeIconItem, ((PreviewFragment) PreviewPresenter.this.mView.get()).mSegmentResult.getPersonBox(currentPerson.getPersonIndex()));
                PreviewPresenter.this.getContract().changeSticker(currentPerson, getPersonBitmapByNode(currentPerson));
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public Bitmap getPersonBitmapByNode(PortraitNode portraitNode) {
                int personIndex = portraitNode.getPersonIndex();
                Bitmap bitmap = (Bitmap) PreviewPresenter.this.mPersonBitmapMap.get(Integer.valueOf(personIndex));
                if (portraitNode.isUpdate() || bitmap == null) {
                    PreviewPresenter previewPresenter = PreviewPresenter.this;
                    bitmap = previewPresenter.getSegmentBitmapByNode(portraitNode, previewPresenter.mCurrentBitmap);
                }
                if (portraitNode.isEmpty()) {
                    return bitmap;
                }
                MagicLog magicLog = MagicLog.INSTANCE;
                magicLog.showLog("personIndex--->getStickerBitmap:  " + personIndex);
                if (portraitNode.getConfigure() == null) {
                    return bitmap;
                }
                PreviewPresenter previewPresenter2 = PreviewPresenter.this;
                return previewPresenter2.getStrokeBitmap(portraitNode.getConfigure(((PreviewFragment) previewPresenter2.mView.get()).mSegmentResult.getPersonBox(portraitNode.getPersonIndex())), bitmap, personIndex);
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void autoFuse(List<PortraitNode> list) {
                if (((PreviewFragment) PreviewPresenter.this.mView.get()).mSegmentResult == null || list == null) {
                    return;
                }
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    PortraitNode portraitNode = list.get(i);
                    PreviewPresenter previewPresenter = PreviewPresenter.this;
                    portraitNode.setPersonBitmap(previewPresenter.getSegmentBitmapByNode(portraitNode, previewPresenter.mCurrentBitmap));
                }
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void redoNotifyCurrentBg(Bitmap bitmap) {
                PreviewPresenter.this.mCurrentBitmap = bitmap;
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void onBackPressed() {
                ((PreviewFragment) PreviewPresenter.this.mView.get()).mo1066initContract().onBackPressed();
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void changeSticker(PortraitNode portraitNode, Bitmap bitmap) {
                ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().changeSticker(portraitNode, bitmap);
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public Bitmap getOriginBitmap() {
                return PreviewPresenter.this.mOriginPhoto;
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public MattingInvoker.SegmentResult getSegmentResult() {
                return ((PreviewFragment) PreviewPresenter.this.mView.get()).mSegmentResult;
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void save(PortraitEditView.Portrait portrait) {
                if (this.mIsSave) {
                    return;
                }
                boolean z = true;
                this.mIsSave = true;
                if (PreviewPresenter.this.mCurrentBitmap == null) {
                    PreviewPresenter previewPresenter = PreviewPresenter.this;
                    previewPresenter.mCurrentBitmap = previewPresenter.mBackgroundBit;
                    if (PreviewPresenter.this.mBackgroundBit == null) {
                        PreviewPresenter previewPresenter2 = PreviewPresenter.this;
                        previewPresenter2.mCurrentBitmap = previewPresenter2.mOriginPhoto;
                    }
                }
                MagicLog.INSTANCE.startLog("matting_save", "魔法抠图保存");
                PreviewPresenter previewPresenter3 = PreviewPresenter.this;
                if (previewPresenter3.mOriginPhoto != PreviewPresenter.this.mCurrentBitmap && PreviewPresenter.this.mBackgroundBit != PreviewPresenter.this.mCurrentBitmap) {
                    z = false;
                }
                final SaveThread saveThread = new SaveThread(portrait, z);
                PreviewPresenter.this.getActivity().showExportImageFragment(new ExportImageFragment.Callbacks() { // from class: com.miui.gallery.magic.matting.preview.PreviewPresenter.1.1
                    @Override // com.miui.gallery.magic.widget.ExportImageFragment.Callbacks
                    public int doExport() {
                        return 0;
                    }

                    @Override // com.miui.gallery.magic.widget.ExportImageFragment.Callbacks
                    public void onCancelled() {
                    }

                    @Override // com.miui.gallery.magic.widget.ExportImageFragment.Callbacks
                    public void onExported(boolean z2) {
                    }

                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // com.miui.gallery.magic.widget.ExportImageFragment.Callbacks
                    public void doCancel() {
                        saveThread.isCancel = true;
                        AnonymousClass1.this.mIsSave = false;
                        PreviewPresenter.this.getActivity().removeExportImageFragment();
                    }
                });
                saveThread.start();
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public Bitmap getBackgroundBit() {
                return PreviewPresenter.this.mBackgroundBit;
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void addPersonForegroundToView(MattingInvoker.SegmentResult segmentResult, Bitmap bitmap) {
                if (!PreviewPresenter.this.mIsFirst) {
                    return;
                }
                ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().addPersonForegroundToView(segmentResult, bitmap);
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void backgroundPaintingSegment(boolean z, int i) {
                if (i == -1) {
                    return;
                }
                PreviewPresenter.this.backgroundSegment(z, i);
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public PortraitNode getCurrentPerson() {
                return ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().getCurrentPerson();
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public void firstAddNode() {
                ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().firstAddNode();
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$VP
            public int mirrorPerson(int i) {
                if (((PreviewFragment) PreviewPresenter.this.mView.get()).mSegmentResult == null) {
                    return -1;
                }
                return ((PreviewFragment) PreviewPresenter.this.mView.get()).mSegmentResult.mirrorPerson(i);
            }
        };
    }

    public final Bitmap getBitmapByNode(boolean z, List<PortraitNode> list, Bitmap bitmap) {
        Collections.sort(list);
        Bitmap bitmap2 = bitmap;
        for (int i = 0; i < list.size(); i++) {
            PortraitNode portraitNode = list.get(i);
            bitmap2 = ((PreviewFragment) this.mView.get()).mMattingInvoker.blending(this.mOriginPhoto, bitmap2, ((PreviewFragment) this.mView.get()).mSegmentResult, portraitNode.getPersonIndex(), getBlendConfig(z, portraitNode));
        }
        return bitmap2;
    }

    public final BlendConfig getBlendConfig(boolean z, PortraitNode portraitNode) {
        boolean z2;
        Matrix matrix = new Matrix();
        float[] fArr = new float[9];
        portraitNode.mMatrix.getValues(fArr);
        matrix.setValues(fArr);
        if (portraitNode.getBlendMirror()) {
            matrix.preScale(-1.0f, 1.0f, portraitNode.mImageBounds.centerX(), portraitNode.mImageBounds.centerY());
            z2 = true;
        } else {
            z2 = false;
        }
        float[] translate = MagicMatrixUtil.getTranslate(matrix);
        float f = -MagicMatrixUtil.getRotate(matrix);
        float scale = MagicMatrixUtil.getScale(matrix);
        ContourHelper.Configure configure = portraitNode.getConfigure();
        if (configure != null) {
            MagicLog.INSTANCE.showLog("matting_save----- 保存描边信息 ： index: " + portraitNode.getPersonIndex() + "distance: " + configure.getDistance() + "style: " + configure.getStyle());
        }
        return new BlendConfig().setDegrees(f).setPoint(translate[0], translate[1]).setScale(scale).setMirrorImage(z2).setBlend(!z).setContourConfigure(configure);
    }

    public final Bitmap getSegmentBitmapByNode(PortraitNode portraitNode, Bitmap bitmap) {
        return getSegmentBitmapByIndex(((PreviewFragment) this.mView.get()).mMattingInvoker, portraitNode.getPersonIndex(), bitmap, getBlendConfig(false, portraitNode));
    }

    public final Bitmap getSegmentBitmapByIndex(MattingInvoker mattingInvoker, int i, Bitmap bitmap, BlendConfig blendConfig) {
        MagicLog.INSTANCE.startLog("matting_getSegmentBitmapByIndex", "重新获取人物");
        if (blendConfig == null) {
            blendConfig = new BlendConfig();
        }
        BlendConfig blendConfig2 = blendConfig;
        PreviewFragment realV = getRealV();
        if (realV == null) {
            return null;
        }
        Bitmap halfBlending = mattingInvoker.halfBlending(this.mOriginPhoto, bitmap, realV.mSegmentResult, i, blendConfig2);
        this.mPersonBitmapMap.put(Integer.valueOf(i), halfBlending);
        MagicLog.INSTANCE.endLog("matting_getSegmentBitmapByIndex", "重新获取人物");
        return halfBlending;
    }

    public final Bitmap getStrokeBitmap(ContourHelper.Configure configure, Bitmap bitmap, int i) {
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("matting_stroke", "人物索引index：" + i);
        MagicLog.INSTANCE.startLog("matting_stroke", "魔法抠图描边");
        Bitmap drawBitmap = ContourHelper.drawBitmap(this.mOriginPhoto, ((PreviewFragment) this.mView.get()).mSegmentResult, i, bitmap.copy(Bitmap.Config.ARGB_8888, true), configure);
        MagicLog.INSTANCE.endLog("matting_stroke", "魔法抠图描边");
        return drawBitmap;
    }

    public final void segmentPredict() {
        MagicLog.INSTANCE.startLog("matting_cut", "魔法抠图分割");
        final long currentTimeMillis = System.currentTimeMillis();
        getActivity().showLoading(false, new MagicLoadingDialog.Callback() { // from class: com.miui.gallery.magic.matting.preview.PreviewPresenter$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.magic.widget.MagicLoadingDialog.Callback
            public final void doCancel() {
                PreviewPresenter.$r8$lambda$XwMLQshTnVHHWmpnGGyTPjGN_CM(PreviewPresenter.this);
            }
        });
        MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.preview.PreviewPresenter$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                PreviewPresenter.$r8$lambda$bBMb26vIs9PzYr79EfnyHRDtGHQ(PreviewPresenter.this, currentTimeMillis);
            }
        });
    }

    public /* synthetic */ void lambda$segmentPredict$0() {
        this.isMagicFinish = true;
        getContract().onBackPressed();
    }

    public /* synthetic */ void lambda$segmentPredict$2(long j) {
        PreviewFragment realV = getRealV();
        if (realV == null) {
            return;
        }
        if (realV.mSegmentResult == null) {
            MagicLog.INSTANCE.startLog("matting_cut_init", "魔法抠图分割算法初始化");
            realV.mMattingInvoker.initModel();
            MagicLog.INSTANCE.endLog("matting_cut_init", "魔法抠图分割算法初始化");
            this.mIsFirst = true;
            Bitmap bitmap = this.mOriginPhoto;
            this.mBackgroundBit = bitmap;
            realV.mSegmentResult = realV.mMattingInvoker.segmentPredict(bitmap);
        }
        final BaseFragmentActivity activityWithSync = getActivityWithSync();
        if (activityWithSync == null || this.isMagicFinish) {
            return;
        }
        if (realV.mSegmentResult.isEmpty()) {
            activityWithSync.removeLoadingDialog();
            MagicToast.showToast(activityWithSync, activityWithSync.getString(R$string.magic_no_human_face));
            getContract().firstAddNode();
            MagicLog.INSTANCE.endLog("matting_cut", "魔法抠图分割");
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "没有检测到人物");
            MagicSampler.getInstance().recordCategory("matting", "check_error", hashMap);
            return;
        }
        final int personCount = realV.mSegmentResult.getPersonCount();
        PreviewFragment realV2 = getRealV();
        if (realV2 == null || this.isMagicFinish) {
            return;
        }
        MattingInvoker mattingInvoker = realV2.mMattingInvoker;
        for (int i = 0; i < personCount; i++) {
            if (getSegmentBitmapByIndex(mattingInvoker, i, this.mCurrentBitmap, null) == null) {
                return;
            }
        }
        MagicLog.INSTANCE.endLog("matting_cut", "魔法抠图分割");
        long currentTimeMillis = System.currentTimeMillis() - j;
        HashMap hashMap2 = new HashMap();
        hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, "时间" + currentTimeMillis);
        MagicSampler.getInstance().recordCategory("matting", "check_time", hashMap2);
        activityWithSync.runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.matting.preview.PreviewPresenter$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                PreviewPresenter.m1041$r8$lambda$FZCDnBavPiIJFbLuoF0H_vKNX8(PreviewPresenter.this, activityWithSync, personCount);
            }
        });
    }

    public /* synthetic */ void lambda$segmentPredict$1(BaseFragmentActivity baseFragmentActivity, int i) {
        baseFragmentActivity.removeLoadingDialog();
        if (this.isMagicFinish) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "数量" + i);
        MagicSampler.getInstance().recordCategory("matting", "character_number", hashMap);
        segmentPersonAndBackground(i);
    }

    public final void backgroundSegment(final boolean z, final int i) {
        getActivity().showLoading();
        new Thread(new Runnable() { // from class: com.miui.gallery.magic.matting.preview.PreviewPresenter$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                PreviewPresenter.m1039$r8$lambda$3QDX0S5pcu7VZM_Sb1tk_6qqQ0(PreviewPresenter.this, i, z);
            }
        }).start();
    }

    public /* synthetic */ void lambda$backgroundSegment$4(int i, final boolean z) {
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.startLog("matting_inPainting", "魔法抠图抽离背景: " + i);
        this.mBackgroundBit = this.mOriginPhoto.copy(Bitmap.Config.ARGB_8888, true);
        if (this.isMagicFinish) {
            return;
        }
        ((PreviewFragment) this.mView.get()).mMattingInvoker.inPainting(this.mBackgroundBit, ((PreviewFragment) this.mView.get()).mSegmentResult);
        if (this.isMagicFinish) {
            return;
        }
        getActivityWithSync().runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.matting.preview.PreviewPresenter$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PreviewPresenter.$r8$lambda$6dEvVVkUn_ZOhwBiUwSkvyA94Nc(PreviewPresenter.this, z);
            }
        });
        MagicLog magicLog2 = MagicLog.INSTANCE;
        magicLog2.endLog("matting_inPainting", "魔法抠图抽离背景: " + i);
    }

    public /* synthetic */ void lambda$backgroundSegment$3(boolean z) {
        getActivityWithSync().removeLoadingDialog();
        if (!this.isMagicFinish && z) {
            this.mCurrentBitmap = this.mBackgroundBit;
            getContract().loadPreview(this.mBackgroundBit, false);
        }
    }

    public final void segmentPersonAndBackground(int i) {
        if (this.isMagicFinish || this.mView == null) {
            return;
        }
        for (int i2 = 0; i2 < i; i2++) {
            getContract().addPortraitNode(this.mPersonBitmapMap.get(Integer.valueOf(i2)), System.currentTimeMillis() + i2, ((PreviewFragment) this.mView.get()).mSegmentResult.getPersonRect(i2), i2);
        }
        getContract().firstAddNode();
        if (this.isMagicFinish || this.mView == null) {
            return;
        }
        getContract().addPersonForegroundToView(((PreviewFragment) this.mView.get()).mSegmentResult, this.mOriginPhoto);
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void unBindView() {
        super.unBindView();
        this.isMagicFinish = true;
    }

    public void clear() {
        MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.preview.PreviewPresenter$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PreviewPresenter.m1040$r8$lambda$Dur93JizYfEeBnNN5MaSxpd2ps(PreviewPresenter.this);
            }
        });
    }

    public /* synthetic */ void lambda$clear$5() {
        clearBitmap(this.mBackgroundBit);
        clearBitmap(this.mOriginPhoto);
        clearBitmap(this.mCurrentBitmap);
        for (Bitmap bitmap : this.mPersonBitmapMap.values()) {
            clearBitmap(bitmap);
        }
    }

    public final void clearBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        bitmap.recycle();
    }

    /* loaded from: classes2.dex */
    public class SaveThread extends Thread {
        public boolean change;
        public PortraitEditView.Portrait export;
        public volatile boolean isCancel = false;

        public SaveThread(PortraitEditView.Portrait portrait, boolean z) {
            PreviewPresenter.this = r1;
            this.export = portrait;
            this.change = z;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Bitmap copy;
            BaseFragmentActivity activityWithSync;
            Uri saveBitmap;
            super.run();
            List<PortraitNode> items = this.export.getItems();
            BackgroundItem backgroundItem = (BackgroundItem) PreviewPresenter.this.getActivity().event(7);
            if (items == null) {
                return;
            }
            Bitmap decodeOrigin = ((PreviewModel) PreviewPresenter.this.mModel).getContract().decodeOrigin(backgroundItem);
            boolean isSuffixPng = PreviewPresenter.this.isSuffixPng(backgroundItem.getOriginUri());
            int width = decodeOrigin.getWidth();
            int height = decodeOrigin.getHeight();
            decodeOrigin.recycle();
            if (backgroundItem.getBackgroundIndex() == 2) {
                copy = Bitmap.createBitmap(PreviewPresenter.this.mCurrentBitmap.getWidth(), PreviewPresenter.this.mCurrentBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                new Canvas(copy).drawColor(0);
                isSuffixPng = true;
            } else {
                copy = PreviewPresenter.this.mCurrentBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
            Bitmap bitmapByNode = PreviewPresenter.this.getBitmapByNode(this.change, items, copy);
            if (this.isCancel || (activityWithSync = PreviewPresenter.this.getActivityWithSync()) == null) {
                return;
            }
            if (isSuffixPng) {
                saveBitmap = MagicFileUtil.saveSignImagePng(activityWithSync, MagicFileUtil.getFileNameByMode("MAGICCUTOUT", ".png"), MagicFileUtil.getResizeBitmap(bitmapByNode, width, height));
            } else {
                saveBitmap = MagicFileUtil.saveBitmap(activityWithSync, MagicFileUtil.getResizeBitmap(bitmapByNode, width, height), "MAGICCUTOUT");
            }
            String path = FileUtils.getPath(activityWithSync, saveBitmap);
            if (!StringUtils.isEmpty(path)) {
                MagicUtils.scanSingleFile(activityWithSync, path);
                saveBitmap = Uri.fromFile(new File(path));
            }
            ToastUtils.makeText(activityWithSync, R$string.magic_save_ok, 1);
            if (bitmapByNode != null && bitmapByNode.isRecycled()) {
                bitmapByNode.recycle();
            }
            activityWithSync.removeExportImageFragment();
            activityWithSync.setResult(-1);
            MagicFileUtil.openPreviewImage(activityWithSync, saveBitmap);
            activityWithSync.finish();
            MagicLog.INSTANCE.endLog("matting_save", "魔法抠图保存");
        }
    }

    public final boolean isSuffixPng(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        int lastIndexOf = lastPathSegment.lastIndexOf(".") + 1;
        int length = lastPathSegment.length();
        if (lastIndexOf >= length) {
            return false;
        }
        return "png".equals(lastPathSegment.substring(lastIndexOf, length));
    }
}
