package com.miui.gallery.magic.special.effects.image;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.special.effects.image.menu.MagicMenuFragment;
import com.miui.gallery.magic.special.effects.image.preview.PreviewFragment;
import com.miui.gallery.magic.util.ImageFormatUtils;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.ToastUtils;

/* loaded from: classes2.dex */
public class SpecialEffectsActivity extends BaseFragmentActivity {
    public MagicMenuFragment mMenuFragment;
    public PreviewFragment mPreviewFragment;

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (BaseBuildUtil.isPad()) {
            setRequestedOrientation(4);
        } else {
            setRequestedOrientation(1);
        }
        Uri data = getIntent().getData();
        if (data != null && !ImageFormatUtils.isSupportImageFormat(data)) {
            ToastUtils.makeText(this, getResources().getString(R$string.magic_cut_video_no_support_image_edit));
            finish();
            return;
        }
        this.mPreviewFragment = new PreviewFragment();
        MagicMenuFragment magicMenuFragment = new MagicMenuFragment();
        this.mMenuFragment = magicMenuFragment;
        addMenu(magicMenuFragment);
        addPreview(this.mPreviewFragment);
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, android.view.View.OnClickListener
    public void onClick(View view) {
        this.mPreviewFragment.onClick(view);
        this.mMenuFragment.onClick(view);
    }

    public boolean isMenuFragmentPresenterCreated() {
        MagicMenuFragment magicMenuFragment = this.mMenuFragment;
        return (magicMenuFragment == null || magicMenuFragment.mPresenter == 0) ? false : true;
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity
    public Object event(int i, Object obj) {
        if (i == 0) {
            this.mPreviewFragment.getContract().setPreviewBitmap((Bitmap) obj);
            return null;
        } else if (i == 1) {
            return this.mPreviewFragment.getContract().getOriginBitmap();
        } else {
            if (i == 3) {
                this.mPreviewFragment.getContract().setPreviewBitmap((Bitmap) obj, false);
                return null;
            } else if (i == 4) {
                this.mMenuFragment.getContract().loadFinish((Bitmap) obj);
                return null;
            } else if (i == 6) {
                return Boolean.valueOf(this.mMenuFragment.getContract().getNotFace());
            } else {
                return null;
            }
        }
    }
}
