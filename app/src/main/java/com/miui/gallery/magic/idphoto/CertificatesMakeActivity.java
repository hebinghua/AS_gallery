package com.miui.gallery.magic.idphoto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.magic.idphoto.menu.CertificatesMenuFragment;
import com.miui.gallery.magic.idphoto.preview.PreviewFragment;
import com.miui.gallery.magic.util.IDPhotoInvokeSingleton;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.SystemUiUtil;
import java.io.IOException;

/* loaded from: classes2.dex */
public class CertificatesMakeActivity extends BaseFragmentActivity {
    public static boolean isFirst = true;
    public static boolean isOperation = false;
    public static Bitmap mOriginBitPhoto;
    public boolean isFromCreation;
    public BaseBroadcastReceiver mBroadcastReceiver;
    public Configuration mConfiguration;
    public CertificatesMenuFragment mMenuFragment;
    public PreviewFragment mPreviewFragment;

    public static void clear() {
        Bitmap bitmap = mOriginBitPhoto;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        mOriginBitPhoto.recycle();
        mOriginBitPhoto = null;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        IDPhotoInvokeSingleton.getInstance().destroyIDPhotoInvoker();
        clear();
        setIsOperation(false);
        setIsFirst(true);
        super.onDestroy();
        unregisterReceiver(this.mBroadcastReceiver);
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mConfiguration = new Configuration(getResources().getConfiguration());
        setOrientation();
        this.mMenuFragment = new CertificatesMenuFragment();
        this.mPreviewFragment = new PreviewFragment();
        IDPhotoInvokeSingleton.getInstance().createIDPhotoInvoker();
        if (mOriginBitPhoto == null) {
            Intent intent = getIntent();
            try {
                mOriginBitPhoto = MagicFileUtil.getBitmap(this, intent.getData(), 1200, intent.getStringExtra("image_path"));
            } catch (IOException e) {
                e.printStackTrace();
                MagicLog.INSTANCE.showLog("get bitmap throw an exception, must be exit current page, otherwise app can crash.");
                finish();
                return;
            }
        }
        addMenu(this.mMenuFragment);
        addPreview(this.mPreviewFragment);
        initBroadcastReceiver();
        boolean booleanExtra = getIntent().getBooleanExtra("from_creation", false);
        this.isFromCreation = booleanExtra;
        PreviewFragment previewFragment = this.mPreviewFragment;
        if (previewFragment != null) {
            previewFragment.setFromCreation(booleanExtra);
        }
        getWindow().getDecorView().setSystemUiVisibility(6);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if ((this.mConfiguration.updateFrom(configuration) & 1024) != 0) {
            setOrientation();
        }
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity
    public Object event(int i, Object obj) {
        if (i != 1) {
            if (i == 2) {
                this.mPreviewFragment.getContract().sizeChange((PhotoStyle) obj);
                return null;
            } else if (i != 3) {
                return null;
            } else {
                this.mPreviewFragment.getContract().initBlending((PhotoStyle) obj);
                return null;
            }
        }
        return this.mMenuFragment.getContract().getCurrentSize();
    }

    public static boolean isIsOperation() {
        return isOperation;
    }

    public static void setIsOperation(boolean z) {
        isOperation = z;
    }

    public static void setIsFirst(boolean z) {
        isFirst = z;
    }

    public final void setOrientation() {
        if (OrientationCheckHelper.isSupportOrientationChange()) {
            SystemUiUtil.setRequestedOrientation(2, this);
        } else {
            SystemUiUtil.setRequestedOrientation(1, this);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        this.mPreviewFragment.cancelEdit();
    }

    public final void initBroadcastReceiver() {
        this.mBroadcastReceiver = new BaseBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.gallery.search");
        registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    /* loaded from: classes2.dex */
    public class BaseBroadcastReceiver extends BroadcastReceiver {
        public BaseBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (!action.equals("com.miui.gallery.search")) {
                return;
            }
            CertificatesMakeActivity.this.mMenuFragment.onActivityResult(201, 201, intent);
        }
    }
}
