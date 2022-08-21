package com.miui.gallery.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts$OpenDocumentTree;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;
import androidx.tracing.Trace;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.storage.CreateDocumentDir;
import com.miui.gallery.storage.IDocumentUILauncherOwner;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.strategy.ActivityStrategyTemplateImpl;
import com.miui.gallery.strategy.IStrategyFollower;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes.dex */
public class MiuiActivity extends AppCompatActivity implements IStrategyFollower, IDocumentUILauncherOwner {
    public ActivityResultLauncher<Pair<String, Uri>> mCreateDocumentLauncher;
    public ActivityResultLauncher<Uri> mOpenDocumentTreeLauncher;
    public ActivityStrategyTemplateImpl mStrategyTemplate;

    public static /* synthetic */ void $r8$lambda$_hygOhIAupqUuhiglqG5XlklqL8(MiuiActivity miuiActivity, Uri uri) {
        miuiActivity.lambda$onCreate$1(uri);
    }

    /* renamed from: $r8$lambda$tcGFp4Mq-8VAvQKWk91FrZeoGTE */
    public static /* synthetic */ void m547$r8$lambda$tcGFp4Mq8VAvQKWk91FrZeoGTE(MiuiActivity miuiActivity, Uri uri) {
        miuiActivity.lambda$onCreate$0(uri);
    }

    /* renamed from: getActivity */
    public FragmentActivity mo546getActivity() {
        return this;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        Trace.beginSection("MisdkActivityCreate");
        super.onCreate(bundle);
        Trace.endSection();
        this.mStrategyTemplate = new ActivityStrategyTemplateImpl(this);
        this.mOpenDocumentTreeLauncher = registerForActivityResult(new ActivityResultContracts$OpenDocumentTree(), new ActivityResultCallback() { // from class: com.miui.gallery.app.activity.MiuiActivity$$ExternalSyntheticLambda1
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                MiuiActivity.m547$r8$lambda$tcGFp4Mq8VAvQKWk91FrZeoGTE(MiuiActivity.this, (Uri) obj);
            }
        });
        this.mCreateDocumentLauncher = registerForActivityResult(new CreateDocumentDir(), new ActivityResultCallback() { // from class: com.miui.gallery.app.activity.MiuiActivity$$ExternalSyntheticLambda0
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                MiuiActivity.$r8$lambda$_hygOhIAupqUuhiglqG5XlklqL8(MiuiActivity.this, (Uri) obj);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0(Uri uri) {
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, uri);
    }

    public /* synthetic */ void lambda$onCreate$1(Uri uri) {
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, uri);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.mStrategyTemplate.onWindowFocusChanged(z);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.mStrategyTemplate.onActionModeStarted(actionMode);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        this.mStrategyTemplate.onActionModeFinished(actionMode);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mStrategyTemplate.onConfigurationChanged(configuration);
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z);
        this.mStrategyTemplate.onMultiWindowModeChanged(z, configuration);
    }

    public void requestDisableStrategy(StrategyContext.DisableStrategyType disableStrategyType) {
        this.mStrategyTemplate.requestDisableStrategy(disableStrategyType);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, i, i2, intent);
    }

    @Override // com.miui.gallery.storage.IDocumentUILauncherOwner
    public ActivityResultLauncher<Uri> getOpenDocumentTreeLauncher() {
        return this.mOpenDocumentTreeLauncher;
    }

    @Override // com.miui.gallery.storage.IDocumentUILauncherOwner
    public ActivityResultLauncher<Pair<String, Uri>> getCreateDocumentDirLauncher() {
        return this.mCreateDocumentLauncher;
    }
}
