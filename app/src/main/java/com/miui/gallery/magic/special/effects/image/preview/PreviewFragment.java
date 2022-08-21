package com.miui.gallery.magic.special.effects.image.preview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.special.effects.image.SpecialEffectsActivity;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicSamplerSingleton;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class PreviewFragment extends BaseFragment<PreviewPresenter, IPreview$VP> {
    public BitmapGestureView mMagicImage;
    public TextView magic_contrast;

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mMagicImage = (BitmapGestureView) findViewById(R$id.magic_image);
        this.magic_contrast = (TextView) findViewById(R$id.magic_contrast);
        if (SystemUiUtil.isWaterFallScreen()) {
            BitmapGestureView bitmapGestureView = this.mMagicImage;
            Resources resources = getContext().getResources();
            int i = R$dimen.magic_px_65;
            bitmapGestureView.setPadding((int) resources.getDimension(i), (int) getContext().getResources().getDimension(R$dimen.magic_px_36), (int) getContext().getResources().getDimension(i), 0);
            return;
        }
        this.mMagicImage.setPadding(0, (int) getContext().getResources().getDimension(R$dimen.magic_px_36), 0, 0);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if ((activity instanceof SpecialEffectsActivity) && !((SpecialEffectsActivity) activity).isMenuFragmentPresenterCreated()) {
            return;
        }
        getContract().selectPhotos(getActivity().getIntent().getData());
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public PreviewPresenter getPresenterInstance() {
        return new PreviewPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_special_effects_preview;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IPreview$VP mo1066initContract() {
        return new IPreview$VP() { // from class: com.miui.gallery.magic.special.effects.image.preview.PreviewFragment.1
            @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
            public void loadPreview(Bitmap bitmap) {
                PreviewFragment.this.mMagicImage.setBitmap(bitmap);
            }

            @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
            public void contrastImage() {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().contrastImage();
            }

            @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
            public void selectPhotos(Uri uri) {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().selectPhotos(uri);
            }

            @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
            public void saveImage() {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().saveImage();
            }

            @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
            public void setPreviewBitmap(Bitmap bitmap) {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().setPreviewBitmap(bitmap);
            }

            @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
            public Bitmap getOriginBitmap() {
                return ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().getOriginBitmap();
            }

            @Override // com.miui.gallery.magic.special.effects.image.preview.IPreview$VP
            public void setPreviewBitmap(Bitmap bitmap, boolean z) {
                ((PreviewPresenter) PreviewFragment.this.mPresenter).getContract().setPreviewBitmap(bitmap, z);
            }
        };
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (isFastClick()) {
            return;
        }
        int id = view.getId();
        if (id == R$id.magic_contrast) {
            getContract().contrastImage();
        } else if (id == R$id.idp_se_save) {
            getContract().saveImage();
            String selectArtName = MagicSamplerSingleton.getInstance().getSelectArtName();
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, selectArtName);
            MagicSampler.getInstance().recordCategory("art", "save", hashMap);
        } else if (id != R$id.idp_se_cancel) {
        } else {
            getActivity().finish();
            MagicSampler.getInstance().recordCategory("art", "cancel");
        }
    }
}
