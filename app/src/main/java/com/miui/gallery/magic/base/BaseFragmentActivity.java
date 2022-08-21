package com.miui.gallery.magic.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.widget.ExportImageFragment;
import com.miui.gallery.magic.widget.ExportVideoFragment;
import com.miui.gallery.magic.widget.MagicLoadingDialog;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public abstract class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener {
    public ExportImageFragment mExportImageFragment;
    public ExportVideoFragment mExportVideoFragment;
    public MagicLoadingDialog mLoadingDialog;
    public BaseFragment mMenuFragment;
    public BaseFragment mPreviewFragment;

    public static /* synthetic */ void $r8$lambda$ZLnbCn3v0qlf6LoOSw8kdBJSKaY(BaseFragmentActivity baseFragmentActivity) {
        baseFragmentActivity.lambda$removeExportImageFragment$1();
    }

    public static /* synthetic */ void $r8$lambda$ywEn8xkp1tORhl18NsDHASY6CqM(BaseFragmentActivity baseFragmentActivity) {
        baseFragmentActivity.lambda$removeExportFragment$0();
    }

    public abstract Object event(int i, Object obj);

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = 1;
            getWindow().setAttributes(attributes);
        }
        setContentView(R$layout.ts_magic_main_layout);
    }

    public void addPreview(BaseFragment baseFragment) {
        this.mPreviewFragment = baseFragment;
        getSupportFragmentManager().beginTransaction().replace(R$id.magic_preview_container, baseFragment).commit();
    }

    public void addPreviewForEffectVideo(BaseFragment baseFragment) {
        this.mPreviewFragment = baseFragment;
        getSupportFragmentManager().beginTransaction().replace(R$id.magic_preview_container, baseFragment).commitAllowingStateLoss();
    }

    public void showPreviewFragment(BaseFragment baseFragment) {
        if (this.mPreviewFragment != baseFragment) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.hide(this.mPreviewFragment);
            this.mPreviewFragment = baseFragment;
            if (!baseFragment.isAdded()) {
                beginTransaction.add(R$id.magic_preview_container, baseFragment).show(baseFragment).commit();
            } else {
                beginTransaction.show(baseFragment).commit();
            }
        }
    }

    public void addMenu(BaseFragment baseFragment) {
        this.mMenuFragment = baseFragment;
        getSupportFragmentManager().beginTransaction().replace(R$id.magic_menu_container, baseFragment).commit();
    }

    public void addMenuForEffectVideo(BaseFragment baseFragment) {
        this.mMenuFragment = baseFragment;
        getSupportFragmentManager().beginTransaction().replace(R$id.magic_menu_container, baseFragment).commitAllowingStateLoss();
    }

    public void showLoading() {
        if (this.mLoadingDialog == null) {
            this.mLoadingDialog = new MagicLoadingDialog(this);
        }
        if (this.mLoadingDialog.isShowing()) {
            this.mLoadingDialog.dismiss();
        }
        this.mLoadingDialog.showLoadingDialog();
    }

    public void removeLoadingDialog() {
        this.mLoadingDialog.removeLoadingDialog();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        BaseFragment baseFragment = this.mPreviewFragment;
        if (baseFragment != null) {
            baseFragment.onClick(view);
        }
        BaseFragment baseFragment2 = this.mMenuFragment;
        if (baseFragment2 != null) {
            baseFragment2.onClick(view);
        }
    }

    public void showExportVideoFragment(ExportVideoFragment.Callbacks callbacks) {
        if (this.mExportVideoFragment == null) {
            this.mExportVideoFragment = new ExportVideoFragment();
        }
        this.mExportVideoFragment.setCallbacks(callbacks);
        this.mExportVideoFragment.show(getSupportFragmentManager(), "showExportFragment");
    }

    public void showExportImageFragment(ExportImageFragment.Callbacks callbacks) {
        if (this.mExportImageFragment == null) {
            this.mExportImageFragment = new ExportImageFragment();
        }
        this.mExportImageFragment.setCallbacks(callbacks);
        this.mExportImageFragment.show(getSupportFragmentManager(), "showExportFragment");
    }

    public void setExportProgress(int i) {
        this.mExportVideoFragment.setProgress(i);
    }

    public void removeExportFragment() {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.magic.base.BaseFragmentActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BaseFragmentActivity.$r8$lambda$ywEn8xkp1tORhl18NsDHASY6CqM(BaseFragmentActivity.this);
            }
        });
    }

    public /* synthetic */ void lambda$removeExportFragment$0() {
        ExportVideoFragment exportVideoFragment = this.mExportVideoFragment;
        if (exportVideoFragment != null) {
            exportVideoFragment.dismissAllowingStateLoss();
        }
    }

    public void removeExportImageFragment() {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.magic.base.BaseFragmentActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BaseFragmentActivity.$r8$lambda$ZLnbCn3v0qlf6LoOSw8kdBJSKaY(BaseFragmentActivity.this);
            }
        });
    }

    public /* synthetic */ void lambda$removeExportImageFragment$1() {
        ExportImageFragment exportImageFragment = this.mExportImageFragment;
        if (exportImageFragment != null) {
            exportImageFragment.dismissAllowingStateLoss();
        }
    }

    public Object event(int i) {
        return event(i, null);
    }

    public void showLoading(boolean z, MagicLoadingDialog.Callback callback) {
        showLoading();
        this.mLoadingDialog.setCancelable(z);
        this.mLoadingDialog.setDoCancelBack(callback);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, i, i2, intent);
    }
}
