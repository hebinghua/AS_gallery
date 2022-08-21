package com.miui.gallery.magic.idphoto.preview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.magic.idphoto.preview.PreviewPresenter;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.FileUtils;
import com.miui.gallery.magic.util.IDPhotoInvokeSingleton;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicMainHandler;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicThreadHandler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.magic.widget.idphoto.IDPhotoView;
import com.miui.gallery.util.ToastUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.milab.videosdk.utils.BitmapUtils;
import com.xiaomi.milab.videosdk.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class PreviewPresenter extends BasePresenter<PreviewFragment, PreviewModel, IPreview$VP> {
    public Bitmap mIdCache = null;
    public Bitmap mOriginBitPhoto;
    public Bitmap mProcessPhoto;

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public PreviewModel getModelInstance() {
        return new PreviewModel(this);
    }

    /* renamed from: com.miui.gallery.magic.idphoto.preview.PreviewPresenter$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements IPreview$VP {
        public Rect faceRect = null;
        public Rect faceRectForPreview = null;
        public PhotoStyle lastPhotoStyle = null;

        /* renamed from: $r8$lambda$DcaNAs7tWV-1QvTD8fOBi4acjoc */
        public static /* synthetic */ void m1029$r8$lambda$DcaNAs7tWV1QvTD8fOBi4acjoc(AnonymousClass1 anonymousClass1, PhotoStyle photoStyle) {
            anonymousClass1.lambda$initBlending$0(photoStyle);
        }

        public static /* synthetic */ void $r8$lambda$dhpc_ZMXYWSbjVONJGuNQJbCyxs(AnonymousClass1 anonymousClass1, PhotoStyle photoStyle) {
            anonymousClass1.lambda$initBlending$1(photoStyle);
        }

        public static /* synthetic */ void $r8$lambda$pR3D7bE5LMVJV8ByyT5U1W8QNPU(AnonymousClass1 anonymousClass1, PhotoStyle photoStyle) {
            anonymousClass1.lambda$sizeChange$3(photoStyle);
        }

        public static /* synthetic */ void $r8$lambda$rkZbWKHbfCAiP4M3JVDHAMJAatY(AnonymousClass1 anonymousClass1, PhotoStyle photoStyle) {
            anonymousClass1.lambda$sizeChange$2(photoStyle);
        }

        public AnonymousClass1() {
            PreviewPresenter.this = r1;
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void initIdpData(Bitmap bitmap) {
            PreviewPresenter.this.mOriginBitPhoto = bitmap;
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void initFaceInvoker() {
            ((BaseFragmentActivity) ((PreviewFragment) PreviewPresenter.this.mView.get()).getActivity()).event(1, Boolean.FALSE);
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void setPhotoPaper() {
            loadProcessBitmap();
            loadPhotoPaper(PreviewPresenter.this.mProcessPhoto, (PhotoStyle) ((BaseFragmentActivity) ((PreviewFragment) PreviewPresenter.this.mView.get()).getActivity()).event(1, Boolean.TRUE));
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void loadPhotoPaper(Bitmap bitmap, PhotoStyle photoStyle) {
            ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().loadPhotoPaper(bitmap, photoStyle);
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void saveImage(int i) {
            PhotoStyle photoStyle = (PhotoStyle) ((BaseFragmentActivity) ((PreviewFragment) PreviewPresenter.this.mView.get()).getActivity()).event(1, Boolean.FALSE);
            if (PreviewPresenter.this.mProcessPhoto == null) {
                MagicToast.showToast(PreviewPresenter.this.getActivity(), R$string.magic_idp_dsc);
                return;
            }
            Uri uri = null;
            MagicLog.INSTANCE.startLog("idp_save", "IdPhoto Save");
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, photoStyle.getWidth() + "x" + photoStyle.getHeight() + " " + photoStyle.getSizeType());
            MagicSampler.getInstance().recordCategory("idcard", "save_specs", hashMap);
            int saveWidth = photoStyle.isUseDpi() ? photoStyle.getSaveWidth(UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME) : photoStyle.getNormalWidth();
            int saveHeight = photoStyle.isUseDpi() ? photoStyle.getSaveHeight(UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME) : photoStyle.getHeight();
            if (i == 1) {
                uri = MagicFileUtil.saveBitmap(PreviewPresenter.this.getActivity(), BitmapUtils.zoomBitmap(PreviewPresenter.this.mProcessPhoto, saveWidth, saveHeight, false), "IDPHOTO");
                if (uri != null) {
                    ToastUtils.makeText(((PreviewFragment) PreviewPresenter.this.mView.get()).getActivity(), R$string.magic_save_ok, 1);
                }
                MagicLog.INSTANCE.endLog("idp_save", "IdPhoto Save Mode 1: One pic");
            } else if (i == 2) {
                uri = MagicFileUtil.saveBitmap(PreviewPresenter.this.getActivity(), PreviewPresenter.this.getContract().getPhotoPaper(PreviewPresenter.this.mProcessPhoto), UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME, "IDPHOTO");
                if (uri != null) {
                    ToastUtils.makeText(((PreviewFragment) PreviewPresenter.this.mView.get()).getActivity(), R$string.magic_save_ok, 1);
                }
                MagicLog.INSTANCE.endLog("idp_save", "IdPhoto Save Mode 2: 8 pics");
            } else if (i == 3) {
                MagicFileUtil.saveBitmap(PreviewPresenter.this.getActivity(), PreviewPresenter.this.getContract().getPhotoPaper(PreviewPresenter.this.mProcessPhoto), UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME, "IDPHOTO");
                uri = MagicFileUtil.saveBitmap(PreviewPresenter.this.getActivity(), BitmapUtils.zoomBitmap(PreviewPresenter.this.mProcessPhoto, saveWidth, saveHeight, false), "IDPHOTO");
                if (uri != null) {
                    ToastUtils.makeText(((PreviewFragment) PreviewPresenter.this.mView.get()).getActivity(), R$string.magic_save_ok, 1);
                }
                MagicLog.INSTANCE.endLog("idp_save", "IdPhoto Save Both Mode");
            }
            if (uri == null) {
                return;
            }
            String path = FileUtils.getPath(((PreviewFragment) PreviewPresenter.this.mView.get()).getContext(), uri);
            if (!StringUtils.isEmpty(path)) {
                MagicUtils.scanSingleFile(((PreviewFragment) PreviewPresenter.this.mView.get()).getContext(), path);
                uri = Uri.fromFile(new File(path));
            }
            MagicFileUtil.openPreviewImage(PreviewPresenter.this.getActivity(), uri);
            PreviewPresenter.this.getActivity().finish();
            Intent intent = new Intent();
            intent.setAction("receiver_action_save_finish");
            PreviewPresenter.this.getActivity().sendBroadcast(intent);
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void initBlending(final PhotoStyle photoStyle) {
            MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.idphoto.preview.PreviewPresenter$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PreviewPresenter.AnonymousClass1.$r8$lambda$dhpc_ZMXYWSbjVONJGuNQJbCyxs(PreviewPresenter.AnonymousClass1.this, photoStyle);
                }
            });
        }

        public /* synthetic */ void lambda$initBlending$1(final PhotoStyle photoStyle) {
            Rect[] detectFace;
            Rect[] detectFace2;
            MagicLog.INSTANCE.startLog("idp_cut", "IdPhoto Blending");
            try {
                detectFace2 = IDPhotoInvokeSingleton.getInstance().getIDPhotoInvoker().detectFace(PreviewPresenter.this.mOriginBitPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (detectFace2 == null || detectFace2.length < 1) {
                throw new IOException("no face");
            }
            this.faceRect = detectFace2[0];
            if (this.faceRect == null) {
                MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto Blending: NoFace!");
                return;
            }
            PreviewPresenter previewPresenter = PreviewPresenter.this;
            if (previewPresenter.isBitmapExist(previewPresenter.mOriginBitPhoto)) {
                PreviewPresenter.this.mIdCache = IDPhotoInvokeSingleton.getInstance().getIDPhotoInvoker().idBlending(PreviewPresenter.this.mOriginBitPhoto, photoStyle.getColor(), this.faceRect, Boolean.TRUE);
                if (PreviewPresenter.this.mIdCache == null) {
                    MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto Blending: idBlending Error!");
                    return;
                }
                this.lastPhotoStyle = photoStyle.clonePhotoStyle();
                try {
                    detectFace = IDPhotoInvokeSingleton.getInstance().getIDPhotoInvoker().detectFace(PreviewPresenter.this.mIdCache);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                if (detectFace == null || detectFace.length < 1) {
                    throw new IOException("Re-detect:no face");
                }
                this.faceRectForPreview = detectFace[0];
                if (this.faceRectForPreview == null) {
                    MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto Blending: Re-faceDetect NoFace!");
                    return;
                }
                MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto Blending");
                MagicMainHandler.post(new Runnable() { // from class: com.miui.gallery.magic.idphoto.preview.PreviewPresenter$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PreviewPresenter.AnonymousClass1.m1029$r8$lambda$DcaNAs7tWV1QvTD8fOBi4acjoc(PreviewPresenter.AnonymousClass1.this, photoStyle);
                    }
                });
                return;
            }
            MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto SizeChange: originBitmap lost!");
        }

        public /* synthetic */ void lambda$initBlending$0(PhotoStyle photoStyle) {
            WeakReference<V> weakReference = PreviewPresenter.this.mView;
            if (weakReference != 0) {
                ((PreviewFragment) weakReference.get()).getContract().loadPreview(PreviewPresenter.this.mIdCache, photoStyle, this.faceRectForPreview);
                loadProcessBitmap();
                PreviewPresenter.this.getContract().loadPhotoPaper(PreviewPresenter.this.mProcessPhoto, photoStyle);
                PreviewPresenter.this.getActivityWithSync().removeLoadingDialog();
                return;
            }
            MagicLog.INSTANCE.showLog("idp_cut", "View is already destroyed");
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void sizeChange(final PhotoStyle photoStyle) {
            MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.idphoto.preview.PreviewPresenter$1$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PreviewPresenter.AnonymousClass1.$r8$lambda$pR3D7bE5LMVJV8ByyT5U1W8QNPU(PreviewPresenter.AnonymousClass1.this, photoStyle);
                }
            });
        }

        public /* synthetic */ void lambda$sizeChange$3(final PhotoStyle photoStyle) {
            MagicLog.INSTANCE.startLog("idp_cut", "IdPhoto SizeChange");
            if (this.faceRect == null) {
                try {
                    Rect[] detectFace = IDPhotoInvokeSingleton.getInstance().getIDPhotoInvoker().detectFace(PreviewPresenter.this.mOriginBitPhoto);
                    if (detectFace == null || detectFace.length < 1) {
                        throw new IOException("no face");
                    }
                    this.faceRect = detectFace[0];
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.faceRect == null) {
                MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto SizeChange: NoFace!");
                return;
            }
            PhotoStyle photoStyle2 = this.lastPhotoStyle;
            if (photoStyle2 == null || photoStyle2.getColor() != photoStyle.getColor() || PreviewPresenter.this.mIdCache == null) {
                PreviewPresenter previewPresenter = PreviewPresenter.this;
                if (previewPresenter.isBitmapExist(previewPresenter.mOriginBitPhoto)) {
                    PreviewPresenter.this.mIdCache = IDPhotoInvokeSingleton.getInstance().getIDPhotoInvoker().idBlending(PreviewPresenter.this.mOriginBitPhoto, photoStyle.getColor(), this.faceRect, Boolean.FALSE);
                    if (PreviewPresenter.this.mIdCache == null) {
                        MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto SizeChange: idChangeBg Error!");
                        return;
                    }
                } else {
                    MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto SizeChange: originBitmap lost!");
                    return;
                }
            }
            this.lastPhotoStyle = photoStyle.clonePhotoStyle();
            if (this.faceRectForPreview == null) {
                try {
                    Rect[] detectFace2 = IDPhotoInvokeSingleton.getInstance().getIDPhotoInvoker().detectFace(PreviewPresenter.this.mIdCache);
                    if (detectFace2 == null || detectFace2.length < 1) {
                        throw new IOException("Re-detect:no face");
                    }
                    this.faceRectForPreview = detectFace2[0];
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (this.faceRectForPreview == null) {
                MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto SizeChange: Re-faceDetect NoFace!");
                return;
            }
            MagicLog.INSTANCE.endLog("idp_cut", "IdPhoto SizeChange");
            MagicMainHandler.post(new Runnable() { // from class: com.miui.gallery.magic.idphoto.preview.PreviewPresenter$1$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PreviewPresenter.AnonymousClass1.$r8$lambda$rkZbWKHbfCAiP4M3JVDHAMJAatY(PreviewPresenter.AnonymousClass1.this, photoStyle);
                }
            });
        }

        public /* synthetic */ void lambda$sizeChange$2(PhotoStyle photoStyle) {
            WeakReference<V> weakReference = PreviewPresenter.this.mView;
            if (weakReference != 0) {
                ((PreviewFragment) weakReference.get()).getContract().loadPreview(PreviewPresenter.this.mIdCache, photoStyle, this.faceRectForPreview);
                loadProcessBitmap();
                PreviewPresenter.this.getContract().loadPhotoPaper(PreviewPresenter.this.mProcessPhoto, photoStyle);
                return;
            }
            MagicLog.INSTANCE.showLog("idp_cut", "View is already destroyed");
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void loadPreview(Bitmap bitmap, PhotoStyle photoStyle, Rect rect) {
            ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().loadPreview(bitmap, photoStyle, rect);
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public Bitmap getPhotoPaper(Bitmap bitmap) {
            return ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().getPhotoPaper(bitmap);
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public IDPhotoView getIDPhotoView() {
            return ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().getIDPhotoView();
        }

        @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
        public void loadProcessBitmap() {
            PreviewPresenter.this.mProcessPhoto = getIDPhotoView().getProcessBitmap();
        }
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IPreview$VP mo1070initContract() {
        return new AnonymousClass1();
    }

    public final boolean isBitmapExist(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void unBindView() {
        super.unBindView();
    }
}
