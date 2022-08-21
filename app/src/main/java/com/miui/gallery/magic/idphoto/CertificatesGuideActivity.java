package com.miui.gallery.magic.idphoto;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MagicDependsModule;
import com.miui.gallery.magic.R$color;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseGuideActivity;
import com.miui.gallery.magic.util.IDPhotoInvokeSingleton;
import com.miui.gallery.magic.util.ImageFormatUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicSamplerConstants;
import com.miui.gallery.magic.util.MagicSamplerSingleton;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.magic.widget.MarqueeTextView;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import miuix.animation.listener.TransitionListener;

/* loaded from: classes2.dex */
public class CertificatesGuideActivity extends BaseGuideActivity implements View.OnClickListener {
    public LinearLayout idpCamera;
    public LinearLayout idpGallery;
    public boolean isFromCreation;
    public ImageView ivGuideBg;
    public ImageView ivTittleBack;
    public Configuration mConfiguration;
    public MarqueeTextView mContentBackground;
    public TextView mContentClothes;
    public MarqueeTextView mContentHair;
    public MarqueeTextView mContentLight;
    public MarqueeTextView mContentTitle;
    public ImageView mIconCamera;
    public ImageView mIconGallery;
    public String mImagePath;
    public ImageView mImagePhoto;
    public Uri mImageUri;
    public LinearLayout mLayoutBottom;
    public LinearLayout mLayoutButton;
    public LinearLayout mLayoutTopBar;
    public Bitmap mOriginBitPhoto;
    public TextView mTextCamera;
    public TextView mTextGallery;
    public RelativeLayout rlCancel;
    public ConstraintLayout rlDoorBg;
    public TextView tvBaseTittle;
    public final int PHOTO_CODE = 1021;
    public final int GALLERY_CODE = 1022;

    @Override // com.miui.gallery.magic.base.BaseGuideActivity, com.miui.gallery.magic.base.BaseActivity, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mConfiguration = new Configuration(getResources().getConfiguration());
        setOrientation();
        MagicSampler.getInstance().recordCategory("idcard", "enter");
        MagicSamplerSingleton.getInstance().setSelectIdCardColor(MagicSamplerConstants.COLORS[0]);
        getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R$color.color_magic_idp_guide_navigationbar)));
        setContentView(R$layout.ts_magic_id_photo_guide);
        int i = R$id.idp_gallery;
        this.idpGallery = (LinearLayout) findViewById(i);
        this.rlDoorBg = (ConstraintLayout) findViewById(R$id.rl_door_bg);
        int i2 = R$id.idp_camera;
        this.idpCamera = (LinearLayout) findViewById(i2);
        this.ivGuideBg = (ImageView) findViewById(R$id.iv_id_guide_bg);
        this.rlCancel = (RelativeLayout) findViewById(R$id.idp_se_cancel);
        TextView textView = (TextView) findViewById(R$id.tv_base_tittle);
        this.tvBaseTittle = textView;
        textView.setText(R$string.magic_idp_guide_text);
        this.ivTittleBack = (ImageView) findViewById(R$id.iv_tittle_back);
        this.mImagePhoto = (ImageView) findViewById(R$id.iv_id_guide_photo);
        this.mLayoutBottom = (LinearLayout) findViewById(R$id.layout_bottom_area);
        this.mContentTitle = (MarqueeTextView) findViewById(R$id.text_content_title);
        this.mContentBackground = (MarqueeTextView) findViewById(R$id.text_content_background);
        this.mContentLight = (MarqueeTextView) findViewById(R$id.text_content_light);
        this.mContentClothes = (TextView) findViewById(R$id.text_content_clothes);
        this.mContentHair = (MarqueeTextView) findViewById(R$id.text_content_hair);
        this.mLayoutButton = (LinearLayout) findViewById(R$id.layout_bottom_button);
        this.mLayoutTopBar = (LinearLayout) findViewById(R$id.layout_top_bar);
        this.mIconGallery = (ImageView) findViewById(R$id.idp_gallery_icon);
        this.mIconCamera = (ImageView) findViewById(R$id.idp_camera_icon);
        this.mTextGallery = (TextView) findViewById(R$id.idp_gallery_text);
        this.mTextCamera = (TextView) findViewById(R$id.idp_camera_text);
        if (BaseMiscUtil.isRTLDirection()) {
            this.ivTittleBack.setRotation(180.0f);
        }
        IDPhotoInvokeSingleton.getInstance().createIDPhotoInvoker();
        FolmeUtil.setCustomTouchAnim(this.rlCancel, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, null, true);
        FolmeUtil.setDefaultTouchAnim(findViewById(i2), new TransitionListener(), true);
        FolmeUtil.setDefaultTouchAnim(findViewById(i), new TransitionListener(), true);
        boolean booleanExtra = getIntent().getBooleanExtra("from_creation", false);
        this.isFromCreation = booleanExtra;
        if (booleanExtra) {
            this.idpGallery.setEnabled(false);
            this.idpCamera.setEnabled(false);
            Uri data = getIntent().getData();
            if (data != null) {
                if (ImageFormatUtils.isSupportImageFormat(data)) {
                    if (!MagicFileUtil.checkIsBitmap(this, data)) {
                        MagicToast.showToast(this, R$string.magic_bitmap_damaged);
                        finish();
                        return;
                    } else if (MagicFileUtil.checkMaxPX(this, data)) {
                        MagicToast.showToast(this, R$string.magic_max_px);
                        finish();
                    } else if (MagicFileUtil.checkIdPhotoMinPX(this, data)) {
                        MagicToast.showToast(this, R$string.magic_mix_px);
                        finish();
                    } else {
                        openMakeActivity(data);
                        finish();
                    }
                } else {
                    HashMap hashMap = new HashMap();
                    hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "格式不支持 ");
                    MagicSampler.getInstance().recordCategory("idcard", "processing_failed", hashMap);
                    ToastUtils.makeText(this, getResources().getString(R$string.magic_cut_video_no_support_image_edit));
                    finish();
                }
            } else {
                finish();
            }
        }
        getWindow().getDecorView().setSystemUiVisibility(6);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        fitGuideBackground();
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if ((this.mConfiguration.updateFrom(configuration) & 1024) != 0) {
            setOrientation();
        }
        fitGuideBackground();
    }

    public final void fitGuideBackground() {
        final boolean z = ((MagicDependsModule) ModuleRegistry.getModule(MagicDependsModule.class)).isInFreeFormWindow(this) && !BaseBuildUtil.isPad();
        DefaultLogger.d("CertificatesGuideActivity", "isFreeForm: " + z + " " + this.mConfiguration.toString());
        this.rlDoorBg.post(new Runnable() { // from class: com.miui.gallery.magic.idphoto.CertificatesGuideActivity.1
            @Override // java.lang.Runnable
            public void run() {
                Resources resources;
                int i;
                Resources resources2;
                int i2;
                Resources resources3;
                int i3;
                Resources resources4;
                int i4;
                Resources resources5;
                int i5;
                Resources resources6;
                int i6;
                Resources resources7;
                int i7;
                Resources resources8;
                int i8;
                Resources resources9;
                int i9;
                Resources resources10;
                int i10;
                Resources resources11;
                int i11;
                Resources resources12;
                int i12;
                Resources resources13;
                int i13;
                Resources resources14;
                int i14;
                Resources resources15;
                int i15;
                Resources resources16;
                int i16;
                Resources resources17;
                int i17;
                Resources resources18;
                int i18;
                Resources resources19;
                int i19;
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) CertificatesGuideActivity.this.mImagePhoto.getLayoutParams();
                if (z) {
                    resources = CertificatesGuideActivity.this.getResources();
                    i = R$dimen.px_648;
                } else {
                    resources = CertificatesGuideActivity.this.getResources();
                    i = com.miui.gallery.magic.R$dimen.magic_idp_guide_photo_width;
                }
                ((ViewGroup.MarginLayoutParams) layoutParams).width = resources.getDimensionPixelSize(i);
                if (z) {
                    resources2 = CertificatesGuideActivity.this.getResources();
                    i2 = R$dimen.px_470;
                } else {
                    resources2 = CertificatesGuideActivity.this.getResources();
                    i2 = com.miui.gallery.magic.R$dimen.magic_idp_guide_photo_height;
                }
                ((ViewGroup.MarginLayoutParams) layoutParams).height = resources2.getDimensionPixelSize(i2);
                CertificatesGuideActivity.this.mImagePhoto.setLayoutParams(layoutParams);
                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) CertificatesGuideActivity.this.mLayoutBottom.getLayoutParams();
                if (z) {
                    resources3 = CertificatesGuideActivity.this.getResources();
                    i3 = R$dimen.px_944;
                } else {
                    resources3 = CertificatesGuideActivity.this.getResources();
                    i3 = com.miui.gallery.magic.R$dimen.magic_idp_bottom_height;
                }
                ((ViewGroup.MarginLayoutParams) layoutParams2).height = resources3.getDimensionPixelSize(i3);
                CertificatesGuideActivity.this.mLayoutBottom.setLayoutParams(layoutParams2);
                if (z) {
                    resources4 = CertificatesGuideActivity.this.getResources();
                    i4 = R$dimen.px_750;
                } else {
                    resources4 = CertificatesGuideActivity.this.getResources();
                    i4 = com.miui.gallery.magic.R$dimen.magic_id_photo_guide_text_width;
                }
                int dimensionPixelSize = resources4.getDimensionPixelSize(i4);
                if (z) {
                    resources5 = CertificatesGuideActivity.this.getResources();
                    i5 = R$dimen.px_220;
                } else {
                    resources5 = CertificatesGuideActivity.this.getResources();
                    i5 = com.miui.gallery.magic.R$dimen.magic_id_photo_guide_text_margin_left;
                }
                int dimensionPixelSize2 = resources5.getDimensionPixelSize(i5);
                if (z) {
                    resources6 = CertificatesGuideActivity.this.getResources();
                    i6 = R$dimen.px_60;
                } else {
                    resources6 = CertificatesGuideActivity.this.getResources();
                    i6 = com.miui.gallery.magic.R$dimen.magic_id_photo_guide_text_title_size;
                }
                float dimensionPixelSize3 = resources6.getDimensionPixelSize(i6);
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mContentTitle.getLayoutParams();
                layoutParams3.width = dimensionPixelSize;
                if (z) {
                    resources7 = CertificatesGuideActivity.this.getResources();
                    i7 = R$dimen.px_80;
                } else {
                    resources7 = CertificatesGuideActivity.this.getResources();
                    i7 = com.miui.gallery.magic.R$dimen.magic_id_photo_guide_text_height;
                }
                layoutParams3.height = resources7.getDimensionPixelSize(i7);
                layoutParams3.topMargin = z ? CertificatesGuideActivity.this.getResources().getDimensionPixelSize(R$dimen.px_100) : CertificatesGuideActivity.this.getResources().getDimensionPixelOffset(com.miui.gallery.magic.R$dimen.magic_id_photo_guide_text_margin_top);
                layoutParams3.setMarginStart(dimensionPixelSize2);
                CertificatesGuideActivity.this.mContentTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                CertificatesGuideActivity.this.mContentTitle.setTextSize(0, dimensionPixelSize3);
                CertificatesGuideActivity.this.mContentTitle.setLayoutParams(layoutParams3);
                if (z) {
                    resources8 = CertificatesGuideActivity.this.getResources();
                    i8 = R$dimen.px_48;
                } else {
                    resources8 = CertificatesGuideActivity.this.getResources();
                    i8 = com.miui.gallery.magic.R$dimen.magic_id_photo_guide_text_content_height;
                }
                int dimensionPixelSize4 = resources8.getDimensionPixelSize(i8);
                if (z) {
                    resources9 = CertificatesGuideActivity.this.getResources();
                    i9 = R$dimen.px_36;
                } else {
                    resources9 = CertificatesGuideActivity.this.getResources();
                    i9 = com.miui.gallery.magic.R$dimen.magic_id_photo_guide_text_size;
                }
                float dimensionPixelSize5 = resources9.getDimensionPixelSize(i9);
                LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mContentBackground.getLayoutParams();
                layoutParams4.width = dimensionPixelSize;
                layoutParams4.height = dimensionPixelSize4;
                if (z) {
                    resources10 = CertificatesGuideActivity.this.getResources();
                    i10 = R$dimen.px_75;
                } else {
                    resources10 = CertificatesGuideActivity.this.getResources();
                    i10 = com.miui.gallery.magic.R$dimen.magic_idp_guide_text_margin_2;
                }
                layoutParams4.topMargin = resources10.getDimensionPixelSize(i10);
                layoutParams4.setMarginStart(dimensionPixelSize2);
                CertificatesGuideActivity.this.mContentBackground.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                CertificatesGuideActivity.this.mContentBackground.setTextSize(0, dimensionPixelSize5);
                CertificatesGuideActivity.this.mContentBackground.setLayoutParams(layoutParams4);
                if (z) {
                    resources11 = CertificatesGuideActivity.this.getResources();
                    i11 = R$dimen.px_60;
                } else {
                    resources11 = CertificatesGuideActivity.this.getResources();
                    i11 = com.miui.gallery.magic.R$dimen.magic_idp_guide_text_margin_3;
                }
                int dimensionPixelSize6 = resources11.getDimensionPixelSize(i11);
                LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mContentLight.getLayoutParams();
                layoutParams5.width = dimensionPixelSize;
                layoutParams5.height = dimensionPixelSize4;
                layoutParams5.setMarginStart(dimensionPixelSize2);
                layoutParams5.topMargin = dimensionPixelSize6;
                CertificatesGuideActivity.this.mContentLight.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                CertificatesGuideActivity.this.mContentLight.setTextSize(0, dimensionPixelSize5);
                CertificatesGuideActivity.this.mContentLight.setLayoutParams(layoutParams5);
                LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mContentClothes.getLayoutParams();
                layoutParams6.width = dimensionPixelSize;
                layoutParams6.height = dimensionPixelSize4;
                layoutParams6.setMarginStart(dimensionPixelSize2);
                layoutParams6.topMargin = dimensionPixelSize6;
                CertificatesGuideActivity.this.mContentClothes.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                CertificatesGuideActivity.this.mContentClothes.setTextSize(0, dimensionPixelSize5);
                CertificatesGuideActivity.this.mContentClothes.setLayoutParams(layoutParams6);
                LinearLayout.LayoutParams layoutParams7 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mContentHair.getLayoutParams();
                layoutParams7.width = dimensionPixelSize;
                layoutParams7.height = dimensionPixelSize4;
                layoutParams7.setMarginStart(dimensionPixelSize2);
                layoutParams7.topMargin = dimensionPixelSize6;
                CertificatesGuideActivity.this.mContentHair.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                CertificatesGuideActivity.this.mContentHair.setTextSize(0, dimensionPixelSize5);
                CertificatesGuideActivity.this.mContentHair.setLayoutParams(layoutParams7);
                LinearLayout.LayoutParams layoutParams8 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mLayoutButton.getLayoutParams();
                if (z) {
                    resources12 = CertificatesGuideActivity.this.getResources();
                    i12 = R$dimen.px_107;
                } else {
                    resources12 = CertificatesGuideActivity.this.getResources();
                    i12 = com.miui.gallery.magic.R$dimen.magic_idp_bottom_margin_top;
                }
                layoutParams8.topMargin = resources12.getDimensionPixelSize(i12);
                CertificatesGuideActivity.this.mLayoutButton.setLayoutParams(layoutParams8);
                if (z) {
                    resources13 = CertificatesGuideActivity.this.getResources();
                    i13 = R$dimen.px_310;
                } else {
                    resources13 = CertificatesGuideActivity.this.getResources();
                    i13 = com.miui.gallery.magic.R$dimen.magic_idp_guide_btn1w;
                }
                int dimensionPixelSize7 = resources13.getDimensionPixelSize(i13);
                if (z) {
                    resources14 = CertificatesGuideActivity.this.getResources();
                    i14 = R$dimen.px_190;
                } else {
                    resources14 = CertificatesGuideActivity.this.getResources();
                    i14 = com.miui.gallery.magic.R$dimen.magic_idp_guide_btn1h;
                }
                int dimensionPixelSize8 = resources14.getDimensionPixelSize(i14);
                if (z) {
                    resources15 = CertificatesGuideActivity.this.getResources();
                    i15 = R$dimen.px_22;
                } else {
                    resources15 = CertificatesGuideActivity.this.getResources();
                    i15 = com.miui.gallery.magic.R$dimen.magic_idp_guide_btn_halfGap;
                }
                int dimensionPixelSize9 = resources15.getDimensionPixelSize(i15);
                LinearLayout.LayoutParams layoutParams9 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.idpGallery.getLayoutParams();
                layoutParams9.width = dimensionPixelSize7;
                layoutParams9.height = dimensionPixelSize8;
                layoutParams9.setMarginEnd(dimensionPixelSize9);
                CertificatesGuideActivity.this.idpGallery.setLayoutParams(layoutParams9);
                LinearLayout.LayoutParams layoutParams10 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.idpCamera.getLayoutParams();
                layoutParams10.width = dimensionPixelSize7;
                layoutParams10.height = dimensionPixelSize8;
                layoutParams10.setMarginStart(dimensionPixelSize9);
                CertificatesGuideActivity.this.idpCamera.setLayoutParams(layoutParams10);
                if (z) {
                    resources16 = CertificatesGuideActivity.this.getResources();
                    i16 = R$dimen.px_70;
                } else {
                    resources16 = CertificatesGuideActivity.this.getResources();
                    i16 = com.miui.gallery.magic.R$dimen.magic_idp_icon;
                }
                int dimensionPixelSize10 = resources16.getDimensionPixelSize(i16);
                if (z) {
                    resources17 = CertificatesGuideActivity.this.getResources();
                    i17 = R$dimen.px_37;
                } else {
                    resources17 = CertificatesGuideActivity.this.getResources();
                    i17 = com.miui.gallery.magic.R$dimen.magic_galery_top;
                }
                int dimensionPixelSize11 = resources17.getDimensionPixelSize(i17);
                LinearLayout.LayoutParams layoutParams11 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mIconGallery.getLayoutParams();
                layoutParams11.width = dimensionPixelSize10;
                layoutParams11.height = dimensionPixelSize10;
                layoutParams11.topMargin = dimensionPixelSize11;
                CertificatesGuideActivity.this.mIconGallery.setLayoutParams(layoutParams11);
                LinearLayout.LayoutParams layoutParams12 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mIconCamera.getLayoutParams();
                layoutParams12.width = dimensionPixelSize10;
                layoutParams12.height = dimensionPixelSize10;
                layoutParams12.topMargin = dimensionPixelSize11;
                CertificatesGuideActivity.this.mIconCamera.setLayoutParams(layoutParams12);
                if (z) {
                    resources18 = CertificatesGuideActivity.this.getResources();
                    i18 = R$dimen.px_0;
                } else {
                    resources18 = CertificatesGuideActivity.this.getResources();
                    i18 = com.miui.gallery.magic.R$dimen.magic_text_top;
                }
                int dimensionPixelSize12 = resources18.getDimensionPixelSize(i18);
                LinearLayout.LayoutParams layoutParams13 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mTextGallery.getLayoutParams();
                layoutParams13.topMargin = dimensionPixelSize12;
                CertificatesGuideActivity.this.mTextGallery.setLayoutParams(layoutParams13);
                LinearLayout.LayoutParams layoutParams14 = (LinearLayout.LayoutParams) CertificatesGuideActivity.this.mTextCamera.getLayoutParams();
                layoutParams14.topMargin = dimensionPixelSize12;
                CertificatesGuideActivity.this.mTextCamera.setLayoutParams(layoutParams14);
                if (z) {
                    resources19 = CertificatesGuideActivity.this.getResources();
                    i19 = R$dimen.px_27;
                } else {
                    resources19 = CertificatesGuideActivity.this.getResources();
                    i19 = com.miui.gallery.magic.R$dimen.magic_status_bar_height;
                }
                CertificatesGuideActivity.this.mLayoutTopBar.setPadding(0, resources19.getDimensionPixelSize(i19), 0, 0);
            }
        });
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z, configuration);
        this.mConfiguration.updateFrom(configuration);
        fitGuideBackground();
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        Window window = getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.setNavigationBarColor(getColor(R$color.color_magic_idp_guide_navigationbar));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.idp_camera) {
            this.mImagePath = MagicFileUtil.generateFilePath();
            Uri imageUri = MagicFileUtil.getImageUri(new File(this.mImagePath));
            this.mImageUri = imageUri;
            startActivityForResult(MagicFileUtil.getTakePhotoIntentNew(this, imageUri), 1021);
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "camera");
            MagicSampler.getInstance().recordCategory("idcard", "tab_enter", hashMap);
        } else if (id == R$id.idp_gallery) {
            startActivityForResult(MagicFileUtil.getSelectImageIntent(), 1022);
            HashMap hashMap2 = new HashMap();
            hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, "picker");
            MagicSampler.getInstance().recordCategory("idcard", "tab_enter", hashMap2);
        } else if (id != R$id.idp_se_cancel) {
        } else {
            finish();
        }
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1021) {
            if (i2 != -1) {
                return;
            }
            if (ImageFormatUtils.isSupportImageFormat(this.mImageUri)) {
                openMakeActivity(this.mImageUri);
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "格式不支持 ");
            MagicSampler.getInstance().recordCategory("idcard", "processing_failed", hashMap);
            MagicToast.showToast(this, getResources().getString(R$string.magic_cut_video_no_support_image_edit));
        } else if (i != 1022 || intent == null || intent.getData() == null) {
        } else {
            if (ImageFormatUtils.isSupportImageFormat(intent.getData())) {
                if (!MagicFileUtil.checkIsBitmap(this, intent.getData())) {
                    MagicToast.showToast(this, R$string.magic_bitmap_damaged);
                    return;
                } else if (MagicFileUtil.checkMaxPX(this, intent.getData())) {
                    MagicToast.showToast(this, R$string.magic_max_px);
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, "图片尺寸过大");
                    MagicSampler.getInstance().recordCategory("idcard", "processing_failed", hashMap2);
                    return;
                } else if (MagicFileUtil.checkIdPhotoMinPX(this, intent.getData())) {
                    MagicToast.showToast(this, R$string.magic_mix_px);
                    HashMap hashMap3 = new HashMap();
                    hashMap3.put(nexExportFormat.TAG_FORMAT_TYPE, "图片尺寸过小");
                    MagicSampler.getInstance().recordCategory("idcard", "processing_failed", hashMap3);
                    return;
                } else {
                    openMakeActivity(intent.getData());
                    return;
                }
            }
            HashMap hashMap4 = new HashMap();
            hashMap4.put(nexExportFormat.TAG_FORMAT_TYPE, "格式不支持 ");
            MagicSampler.getInstance().recordCategory("idcard", "processing_failed", hashMap4);
            MagicToast.showToast(this, getResources().getString(R$string.magic_cut_video_no_support_image_edit));
        }
    }

    public final void openMakeActivity(Uri uri) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            this.mOriginBitPhoto = MagicFileUtil.getBitmap(this, uri, 1200, this.mImagePath);
            Rect[] detectFace = IDPhotoInvokeSingleton.getInstance().getIDPhotoInvoker().detectFace(this.mOriginBitPhoto);
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(currentTimeMillis2));
            MagicSampler.getInstance().recordCategory("idcard", "time_consuming", hashMap);
            if (detectFace.length > 1) {
                MagicToast.showToast(this, R$string.magic_more_fact);
                hashMap.clear();
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(detectFace.length));
                MagicSampler.getInstance().recordCategory("idcard", "number_people", hashMap);
                hashMap.clear();
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "人数超过一个，人数 " + detectFace.length);
                MagicSampler.getInstance().recordCategory("idcard", "processing_failed", hashMap);
                if (!this.isFromCreation) {
                    return;
                }
                finish();
                return;
            }
            hashMap.clear();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(detectFace.length));
            MagicSampler.getInstance().recordCategory("idcard", "number_people", hashMap);
            Intent intent = new Intent(this, CertificatesMakeActivity.class);
            intent.putExtra("image_path", this.mImagePath);
            intent.setData(uri);
            intent.putExtra("from_creation", this.isFromCreation);
            startActivity(intent);
        } catch (FileNotFoundException unused) {
            if (!this.isFromCreation) {
                return;
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            if (TextUtils.equals("0", e.getMessage())) {
                MagicToast.showToast(this, getResources().getString(R$string.magic_more_no_fact));
            } else {
                MagicToast.showToast(this, getResources().getString(R$string.magic_cut_video_no_support_image_edit));
            }
            HashMap hashMap2 = new HashMap();
            hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, "格式不支持 " + e.getMessage());
            MagicSampler.getInstance().recordCategory("idcard", "processing_failed", hashMap2);
            if (!this.isFromCreation) {
                return;
            }
            finish();
        }
    }

    @Override // com.miui.gallery.magic.base.BaseGuideActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        IDPhotoInvokeSingleton.getInstance().destroyIDPhotoInvoker();
    }
}
