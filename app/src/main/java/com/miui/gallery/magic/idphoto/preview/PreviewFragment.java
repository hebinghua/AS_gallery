package com.miui.gallery.magic.idphoto.preview;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.Contact;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.idphoto.CertificatesMakeActivity;
import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.magic.ui.ConfirmDialog;
import com.miui.gallery.magic.util.DialogUtil;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicSamplerSingleton;
import com.miui.gallery.magic.widget.ExportImageFragment;
import com.miui.gallery.magic.widget.PhotoPaper;
import com.miui.gallery.magic.widget.idphoto.IDPhotoView;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class PreviewFragment extends BaseFragment<PreviewPresenter, IPreview$VP> {
    public static long lastClickTime;
    public boolean isFromCreation;
    public ExportImageFragment mExportImageFragment;
    public PhotoStyle mLastPhotoStyle;
    public LinearLayout mLlMagicImage;
    public IDPhotoView mMagicImage;
    public PhotoPaper mPhotoMultiple;
    public RadioGroup mRadioGroup;
    public RadioButton mRadioMultiple;
    public RadioButton mRadioSingle;
    public TextView tvCancel;
    public TextView tvOk;

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mExportImageFragment = new ExportImageFragment();
        this.tvCancel = (TextView) findViewById(R$id.idp_make_cancel);
        this.tvOk = (TextView) findViewById(R$id.idp_make_save);
        this.mLlMagicImage = (LinearLayout) findViewById(R$id.idp_make_photo_ll);
        this.mMagicImage = (IDPhotoView) findViewById(R$id.idp_make_photo);
        this.mPhotoMultiple = (PhotoPaper) findViewById(R$id.idp_make_photo_multiple_layout);
        this.mRadioGroup = (RadioGroup) findViewById(R$id.magic_idp_make_change);
        this.mRadioSingle = (RadioButton) findViewById(R$id.magic_idp_make_single);
        this.mRadioMultiple = (RadioButton) findViewById(R$id.magic_idp_make_multiple);
        this.mRadioSingle.setFocusable(true);
        this.mRadioSingle.setFocusableInTouchMode(true);
        this.mRadioSingle.performClick();
        this.mRadioSingle.setOnTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.magic.idphoto.preview.PreviewFragment.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    view.performClick();
                    return false;
                }
                return false;
            }
        });
        this.mRadioMultiple.setOnTouchListener(PreviewFragment$$ExternalSyntheticLambda0.INSTANCE);
        this.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.miui.gallery.magic.idphoto.preview.PreviewFragment.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!PreviewFragment.this.isOneInstance()) {
                    return;
                }
                CertificatesMakeActivity.setIsOperation(true);
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R$id.magic_idp_make_single) {
                    Contact.mPhotoMultiple = false;
                    PreviewFragment.this.selectPreview(false);
                    HashMap hashMap = new HashMap();
                    hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "sola");
                    MagicSampler.getInstance().recordCategory("idcard", "preview", hashMap);
                } else if (checkedRadioButtonId != R$id.magic_idp_make_multiple) {
                } else {
                    Contact.mPhotoMultiple = true;
                    PreviewFragment.this.selectPreview(true);
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, "multiple");
                    MagicSampler.getInstance().recordCategory("idcard", "preview", hashMap2);
                }
            }
        });
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.tvCancel, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.tvOk, build, null, null, null, true);
        if (SystemUiUtil.isWaterFallScreen()) {
            LinearLayout linearLayout = this.mLlMagicImage;
            Resources resources = getContext().getResources();
            int i = R$dimen.magic_px_65;
            linearLayout.setPadding((int) resources.getDimension(i), (int) getContext().getResources().getDimension(R$dimen.magic_px_36), (int) getContext().getResources().getDimension(i), 0);
            return;
        }
        this.mLlMagicImage.setPadding(0, (int) getContext().getResources().getDimension(R$dimen.magic_px_36), 0, 0);
    }

    public static /* synthetic */ boolean lambda$initView$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            view.performClick();
            return false;
        }
        return false;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if ((activity instanceof CertificatesMakeActivity) && ((CertificatesMakeActivity) activity).mMenuFragment.mPresenter == 0) {
            return;
        }
        PhotoStyle photoStyle = new PhotoStyle();
        this.mLastPhotoStyle = photoStyle;
        photoStyle.setBgColor(0);
        this.mLastPhotoStyle.setWidth(0);
        this.mLastPhotoStyle.setHeight(0);
        getContract().initIdpData(CertificatesMakeActivity.mOriginBitPhoto);
        getContract().initFaceInvoker();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public PreviewPresenter getPresenterInstance() {
        return new PreviewPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_id_photo_make_preview;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IPreview$VP mo1066initContract() {
        return new IPreview$VP() { // from class: com.miui.gallery.magic.idphoto.preview.PreviewFragment.3
            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void initIdpData(Bitmap bitmap) {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().initIdpData(bitmap);
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void initFaceInvoker() {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().initFaceInvoker();
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void setPhotoPaper() {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().setPhotoPaper();
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void loadPhotoPaper(Bitmap bitmap, PhotoStyle photoStyle) {
                PreviewFragment.this.mPhotoMultiple.setBitmap(bitmap, photoStyle);
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void saveImage(int i) {
                loadProcessBitmap();
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().saveImage(i);
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void sizeChange(PhotoStyle photoStyle) {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().sizeChange(photoStyle);
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void initBlending(PhotoStyle photoStyle) {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().initBlending(photoStyle);
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void loadPreview(Bitmap bitmap, PhotoStyle photoStyle, Rect rect) {
                PreviewFragment.this.mMagicImage.setBitmap(bitmap, (PreviewFragment.this.mLastPhotoStyle != null && PreviewFragment.this.mLastPhotoStyle.getNormalWidth() == photoStyle.getNormalWidth() && PreviewFragment.this.mLastPhotoStyle.getNormalHeight() == photoStyle.getNormalHeight()) ? false : true, photoStyle.getBgWidth(), photoStyle.getBgHeight(), rect);
                PreviewFragment.this.mMagicImage.setCurrentBgColor(photoStyle.getColor());
                PreviewFragment.this.mLastPhotoStyle.setBgColor(photoStyle.getBgColor());
                PreviewFragment.this.mLastPhotoStyle.setWidth(photoStyle.getNormalWidth());
                PreviewFragment.this.mLastPhotoStyle.setHeight(photoStyle.getNormalHeight());
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public Bitmap getPhotoPaper(Bitmap bitmap) {
                return PreviewFragment.this.mPhotoMultiple.getBitmap(bitmap);
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public IDPhotoView getIDPhotoView() {
                return PreviewFragment.this.mMagicImage;
            }

            @Override // com.miui.gallery.magic.idphoto.preview.IPreview$VP
            public void loadProcessBitmap() {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().loadProcessBitmap();
            }
        };
    }

    public void selectPreview(boolean z) {
        int i = 8;
        findViewById(R$id.idp_make_photo).setVisibility(z ? 8 : 0);
        View findViewById = findViewById(R$id.idp_make_photo_multiple_layout);
        if (z) {
            i = 0;
        }
        findViewById.setVisibility(i);
        if (z) {
            getContract().setPhotoPaper();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.idp_make_save) {
            CertificatesMakeActivity.setIsOperation(true);
            DialogUtil.magicSelectDialog(getActivity(), this);
        } else if (id == R$id.idp_make_cancel) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime <= 500) {
                return;
            }
            lastClickTime = currentTimeMillis;
            MagicSampler.getInstance().recordCategory("idcard", "cancel");
            cancelEdit();
        } else if (id == R$id.magic_idp_save_one) {
            getContract().saveImage(1);
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "sola");
            hashMap.put("color", MagicSamplerSingleton.getInstance().getSelectIdCardColor());
            hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, MagicSamplerSingleton.getInstance().getSelectIdCardSize());
            MagicSampler.getInstance().recordCategory("idcard", "save", hashMap);
        } else if (id == R$id.magic_idp_save_two) {
            getContract().saveImage(2);
            HashMap hashMap2 = new HashMap();
            hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, "multiple");
            hashMap2.put("color", MagicSamplerSingleton.getInstance().getSelectIdCardColor());
            hashMap2.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, MagicSamplerSingleton.getInstance().getSelectIdCardSize());
            MagicSampler.getInstance().recordCategory("idcard", "save", hashMap2);
        } else if (id != R$id.magic_idp_save_m) {
        } else {
            getContract().saveImage(3);
            HashMap hashMap3 = new HashMap();
            hashMap3.put(nexExportFormat.TAG_FORMAT_TYPE, "all");
            hashMap3.put("color", MagicSamplerSingleton.getInstance().getSelectIdCardColor());
            hashMap3.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, MagicSamplerSingleton.getInstance().getSelectIdCardSize());
            MagicSampler.getInstance().recordCategory("idcard", "save", hashMap3);
        }
    }

    public void cancelEdit() {
        if (!CertificatesMakeActivity.isIsOperation()) {
            endCertificatesPage();
        } else {
            ConfirmDialog.showConfirmDialog(getActivity().getSupportFragmentManager(), getStringById(R$string.magic_edit_cancel), getStringById(R$string.magic_edit_dsc), getStringById(R$string.magic_cancel), getStringById(R$string.magic_ok), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.magic.idphoto.preview.PreviewFragment.4
                @Override // com.miui.gallery.magic.ui.ConfirmDialog.ConfirmDialogInterface
                public void onCancel(DialogFragment dialogFragment) {
                }

                @Override // com.miui.gallery.magic.ui.ConfirmDialog.ConfirmDialogInterface
                public void onConfirm(DialogFragment dialogFragment) {
                    CertificatesMakeActivity.setIsOperation(false);
                    PreviewFragment.this.endCertificatesPage();
                }
            });
        }
    }

    public final void endCertificatesPage() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (this.isFromCreation) {
            Intent intent = new Intent();
            intent.setAction("receiver_action_save_finish");
            activity.sendBroadcast(intent);
        }
        activity.finish();
    }

    public final boolean isOneInstance() {
        FragmentActivity activity = getActivity();
        return activity != null && (activity instanceof CertificatesMakeActivity) && this == ((CertificatesMakeActivity) activity).mPreviewFragment;
    }

    public void setFromCreation(boolean z) {
        this.isFromCreation = z;
    }
}
