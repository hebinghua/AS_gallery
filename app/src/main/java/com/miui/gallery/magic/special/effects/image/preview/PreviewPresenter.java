package com.miui.gallery.magic.special.effects.image.preview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.special.effects.image.preview.PreviewPresenter;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.FileUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.magic.widget.ExportImageFragment;
import com.miui.gallery.util.ToastUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.milab.videosdk.utils.StringUtils;
import java.io.File;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class PreviewPresenter extends BasePresenter<PreviewFragment, PreviewModel, IPreview$VP> {
    public static boolean mIsStartSave;
    public Bitmap mCurrentPhoto;
    public boolean mIsCancelSave;
    public Bitmap mOriginPhoto;
    public final int SET_PROGRESS = 1;
    public final int END_PROGRESS = 2;
    public final int GALLERY_CODE = 1022;
    public Handler mHandler = new Handler() { // from class: com.miui.gallery.magic.special.effects.image.preview.PreviewPresenter.2
        {
            PreviewPresenter.this = this;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            BaseFragmentActivity activityWithSync;
            super.handleMessage(message);
            if (message.what == 1 && (activityWithSync = PreviewPresenter.this.getActivityWithSync()) != null) {
                activityWithSync.setExportProgress(message.arg1);
                if (message.arg1 != 100) {
                    return;
                }
                activityWithSync.removeExportFragment();
            }
        }
    };

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public PreviewModel getModelInstance() {
        return new PreviewModel(this);
    }

    /* renamed from: com.miui.gallery.magic.special.effects.image.preview.PreviewPresenter$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements IPreview$VP {
        /* renamed from: $r8$lambda$Ktiy2K5XoDlua-XmbWYqZ5BERpU */
        public static /* synthetic */ void m1053$r8$lambda$Ktiy2K5XoDluaXmbWYqZ5BERpU(AnonymousClass1 anonymousClass1) {
            anonymousClass1.lambda$saveImage$0();
        }

        public AnonymousClass1() {
            PreviewPresenter.this = r1;
        }

        @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
        public void loadPreview(Bitmap bitmap) {
            ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().loadPreview(bitmap);
            PreviewPresenter.this.mCurrentPhoto = bitmap;
        }

        @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
        public void contrastImage() {
            if (PreviewPresenter.this.mOriginPhoto != null) {
                ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().loadPreview(PreviewPresenter.this.mOriginPhoto);
            }
        }

        @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
        public void selectPhotos(Uri uri) {
            BaseFragmentActivity activityWithSync = PreviewPresenter.this.getActivityWithSync();
            if (activityWithSync == null) {
                return;
            }
            if (uri == null) {
                PreviewPresenter.this.getActivity().finish();
            } else if (MagicFileUtil.checkMaxPX(activityWithSync, uri)) {
                MagicToast.showToast(PreviewPresenter.this.getActivity().getApplicationContext(), R$string.magic_max_px);
                PreviewPresenter.this.getActivity().finish();
            } else {
                PreviewPresenter previewPresenter = PreviewPresenter.this;
                previewPresenter.mOriginPhoto = ((PreviewModel) previewPresenter.mModel).getContract().decode(uri);
                if (PreviewPresenter.this.mOriginPhoto != null) {
                    if (!MagicFileUtil.checkMinPX(PreviewPresenter.this.mOriginPhoto)) {
                        loadPreview(PreviewPresenter.this.mOriginPhoto);
                        activityWithSync.event(4, PreviewPresenter.this.mOriginPhoto);
                        return;
                    }
                    MagicToast.showToast(activityWithSync.getApplicationContext(), R$string.magic_mix_px);
                    activityWithSync.finish();
                    return;
                }
                MagicToast.showToast(PreviewPresenter.this.getActivity().getApplicationContext(), R$string.magic_bitmap_damaged);
                PreviewPresenter.this.getActivity().finish();
            }
        }

        public /* synthetic */ void lambda$saveImage$0() {
            PreviewPresenter.this.saveToGallery();
        }

        @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
        public void saveImage() {
            Thread thread = new Thread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.image.preview.PreviewPresenter$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PreviewPresenter.AnonymousClass1.m1053$r8$lambda$Ktiy2K5XoDluaXmbWYqZ5BERpU(PreviewPresenter.AnonymousClass1.this);
                }
            });
            final BaseFragmentActivity activityWithSync = PreviewPresenter.this.getActivityWithSync();
            if (activityWithSync != null) {
                activityWithSync.showExportImageFragment(new ExportImageFragment.Callbacks() { // from class: com.miui.gallery.magic.special.effects.image.preview.PreviewPresenter.1.1
                    @Override // com.miui.gallery.magic.widget.ExportImageFragment.Callbacks
                    public int doExport() {
                        return 0;
                    }

                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // com.miui.gallery.magic.widget.ExportImageFragment.Callbacks
                    public void onExported(boolean z) {
                        MagicLog.INSTANCE.showLog("onExported");
                    }

                    @Override // com.miui.gallery.magic.widget.ExportImageFragment.Callbacks
                    public void doCancel() {
                        PreviewPresenter.this.mIsCancelSave = true;
                        activityWithSync.removeExportImageFragment();
                    }

                    @Override // com.miui.gallery.magic.widget.ExportImageFragment.Callbacks
                    public void onCancelled() {
                        MagicLog.INSTANCE.showLog("onCancelled");
                    }
                });
            }
            thread.start();
        }

        @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
        public void setPreviewBitmap(Bitmap bitmap) {
            loadPreview(bitmap);
        }

        @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
        public Bitmap getOriginBitmap() {
            return PreviewPresenter.this.mOriginPhoto;
        }

        @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
        public void setPreviewBitmap(Bitmap bitmap, boolean z) {
            PreviewPresenter.this.mCurrentPhoto = bitmap;
            ((PreviewFragment) PreviewPresenter.this.mView.get()).getContract().loadPreview(bitmap);
        }
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IPreview$VP mo1070initContract() {
        return new AnonymousClass1();
    }

    public final void saveToGallery() {
        long currentTimeMillis = System.currentTimeMillis();
        MagicLog.INSTANCE.startLog("effects_save", "人像滤镜保存");
        mIsStartSave = true;
        Bitmap decode = ((PreviewModel) this.mModel).getContract().decode();
        Bitmap createBitmap = Bitmap.createBitmap(decode.getWidth(), decode.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(this.mCurrentPhoto, new Rect(0, 0, this.mCurrentPhoto.getWidth(), this.mCurrentPhoto.getHeight()), new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight()), new Paint());
        decode.recycle();
        BaseFragmentActivity activityWithSync = getActivityWithSync();
        if (activityWithSync == null) {
            Log.d("saveToGallery", "illegal end save process.");
            return;
        }
        MagicLog.INSTANCE.startLog("effects_save_app", "app保存时间");
        Uri saveBitmap = MagicFileUtil.saveBitmap(activityWithSync, createBitmap, "ARTPHOTO");
        MagicLog.INSTANCE.endLog("effects_save_app", "app保存时间");
        if (this.mIsCancelSave && saveBitmap != null) {
            MagicFileUtil.deleteImage(MagicUtils.getGalleryApp(), saveBitmap);
            this.mIsCancelSave = false;
            mIsStartSave = false;
            return;
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(currentTimeMillis2));
        MagicSampler.getInstance().recordCategory("art", "save_time_consuming", hashMap);
        if (saveBitmap != null) {
            BaseFragmentActivity activityWithSync2 = getActivityWithSync();
            if (activityWithSync2 == null) {
                return;
            }
            activityWithSync2.removeExportImageFragment();
            ToastUtils.makeText(activityWithSync2, R$string.magic_save_ok, 1);
            clearBitmap(decode);
            clearBitmap(createBitmap);
            String path = FileUtils.getPath(activityWithSync2, saveBitmap);
            if (!StringUtils.isEmpty(path)) {
                MagicUtils.scanSingleFile(activityWithSync2, path);
                saveBitmap = Uri.fromFile(new File(path));
            }
            MagicFileUtil.openPreviewImage(activityWithSync2, saveBitmap);
            MagicLog.INSTANCE.endLog("effects_save", "人像滤镜保存");
            mIsStartSave = false;
            activityWithSync2.finish();
            Intent intent = new Intent();
            intent.setAction("receiver_action_save_finish");
            activityWithSync2.sendBroadcast(intent);
            return;
        }
        hashMap.clear();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "保存生成的uri为null。");
        MagicSampler.getInstance().recordCategory("art", "save_error", hashMap);
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void unBindView() {
        super.unBindView();
        clearBitmap(this.mCurrentPhoto);
        clearBitmap(this.mOriginPhoto);
    }

    public final void clearBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        bitmap.recycle();
    }

    public static boolean isStartSave() {
        return mIsStartSave;
    }
}
