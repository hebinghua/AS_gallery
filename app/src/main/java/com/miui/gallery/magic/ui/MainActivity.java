package com.miui.gallery.magic.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseActivity;
import com.miui.gallery.magic.idphoto.CertificatesGuideActivity;
import com.miui.gallery.magic.matting.MattingActivity;
import com.miui.gallery.magic.special.effects.image.SpecialEffectsActivity;
import com.miui.gallery.magic.special.effects.image.SpecialEffectsGuideActivity;
import com.miui.gallery.magic.special.effects.video.VideoCutActivity;
import com.miui.gallery.magic.special.effects.video.VideoEffectsActivity;
import com.miui.gallery.magic.special.effects.video.VideoEffectsGuideActivity;
import com.miui.gallery.magic.util.ImageFormatUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.util.ToastUtils;

/* loaded from: classes2.dex */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public boolean simulator;
    public ToggleButton toggleButton;
    public final int GALLERY_CODE_SE = 1023;
    public final int GALLERY_CODE_FILTER = 1022;
    public final int GALLERY_CODE_VIDEO = 1024;
    public BitmapFactory.Options option = new BitmapFactory.Options();
    public int simulatorFromAlbum = 0;

    @Override // com.miui.gallery.magic.base.BaseActivity, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getLayoutId());
        this.option.outConfig = Bitmap.Config.ARGB_8888;
        verifyStoragePermissions();
        ToggleButton toggleButton = (ToggleButton) findViewById(R$id.toggle_btn);
        this.toggleButton = toggleButton;
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.magic.ui.MainActivity.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MainActivity.this.simulator = z;
            }
        });
    }

    public int getLayoutId() {
        return R$layout.ts_magic_main;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.magic_filter) {
            this.simulatorFromAlbum = 0;
            startActivityForResult(MagicFileUtil.getSelectImageIntent(), 1022);
        } else if (id == R$id.magic_id_photo) {
            if (this.simulator) {
                this.simulatorFromAlbum = 1;
                startActivityForResult(MagicFileUtil.getSelectImageIntent(), 1022);
                return;
            }
            startActivity(new Intent(this, CertificatesGuideActivity.class));
        } else if (id == R$id.magic_protrait_art) {
            if (this.simulator) {
                this.simulatorFromAlbum = 2;
                startActivityForResult(MagicFileUtil.getSelectImageIntent(), 1022);
                return;
            }
            startActivity(new Intent(this, SpecialEffectsGuideActivity.class));
        } else if (id != R$id.video_test && id != R$id.video_test1) {
        } else {
            startActivity(new Intent(this, VideoEffectsGuideActivity.class));
        }
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        switch (i) {
            case 1022:
                if (i2 != -1) {
                    return;
                }
                if (ImageFormatUtils.isSupportImageFormat(intent.getData())) {
                    int i3 = this.simulatorFromAlbum;
                    if (i3 == 0) {
                        openMakeActivity(intent, MattingActivity.class);
                        return;
                    } else if (i3 == 1) {
                        openCertificatesGuideActivity(intent, CertificatesGuideActivity.class);
                        return;
                    } else if (i3 != 2) {
                        return;
                    } else {
                        Intent intent2 = new Intent(getApplicationContext(), SpecialEffectsActivity.class);
                        intent2.setData(intent.getData());
                        intent2.putExtra(MapBundleKey.MapObjKey.OBJ_SL_INDEX, 0);
                        startActivity(intent2);
                        return;
                    }
                }
                ToastUtils.makeText(this, getResources().getString(R$string.magic_cut_video_no_support_image_edit));
                return;
            case 1023:
                openMakeActivity(intent, SpecialEffectsActivity.class);
                return;
            case 1024:
                openMakeActivity(intent, VideoEffectsActivity.class);
                return;
            case 1025:
                openMakeActivity(intent, VideoCutActivity.class);
                return;
            default:
                return;
        }
    }

    public final void openMakeActivity(Intent intent, Class<?> cls) {
        if (intent == null || intent.getData() == null) {
            MagicToast.showToast(this, "魔法抠图", 1);
            return;
        }
        Intent intent2 = new Intent(this, cls);
        intent2.setData(intent.getData());
        startActivity(intent2);
    }

    public final void openCertificatesGuideActivity(Intent intent, Class<?> cls) {
        Intent intent2 = new Intent(this, cls);
        intent2.putExtra("from_creation", true);
        intent2.setData(intent.getData());
        startActivity(intent2);
    }

    public void verifyStoragePermissions() {
        try {
            if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                return;
            }
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1021);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
