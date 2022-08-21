package com.miui.gallery.app.fragment;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts$OpenDocumentTree;
import androidx.core.util.Pair;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.listener.OnVisibilityChangeListener;
import com.miui.gallery.storage.CreateDocumentDir;
import com.miui.gallery.storage.IDocumentUILauncherOwner;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.strategy.FragmentStrategyTemplateImpl;
import com.miui.gallery.strategy.IStrategyFollower;
import miuix.appcompat.app.Fragment;

/* loaded from: classes.dex */
public class MiuiFragment extends Fragment implements IStrategyFollower, OnVisibilityChangeListener, IDocumentUILauncherOwner {
    public ActivityResultLauncher<Pair<String, Uri>> mCreateDocumentLauncher;
    public ActivityResultLauncher<Uri> mOpenDocumentTreeLauncher;
    public FragmentStrategyTemplateImpl mStrategyTemplate;

    public static /* synthetic */ void $r8$lambda$5daJXuJznEM2sKetSzmNNypW5CI(MiuiFragment miuiFragment, Uri uri) {
        miuiFragment.lambda$onCreate$1(uri);
    }

    public static /* synthetic */ void $r8$lambda$US59q3YbBefWXEej1kudE70x5w0(MiuiFragment miuiFragment, Uri uri) {
        miuiFragment.lambda$onCreate$0(uri);
    }

    @Override // com.miui.gallery.listener.OnVisibilityChangeListener
    public void onVisibleChange(boolean z) {
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mStrategyTemplate = new FragmentStrategyTemplateImpl(this);
        this.mOpenDocumentTreeLauncher = registerForActivityResult(new ActivityResultContracts$OpenDocumentTree(), new ActivityResultCallback() { // from class: com.miui.gallery.app.fragment.MiuiFragment$$ExternalSyntheticLambda1
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                MiuiFragment.$r8$lambda$US59q3YbBefWXEej1kudE70x5w0(MiuiFragment.this, (Uri) obj);
            }
        });
        this.mCreateDocumentLauncher = registerForActivityResult(new CreateDocumentDir(), new ActivityResultCallback() { // from class: com.miui.gallery.app.fragment.MiuiFragment$$ExternalSyntheticLambda0
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                MiuiFragment.$r8$lambda$5daJXuJznEM2sKetSzmNNypW5CI(MiuiFragment.this, (Uri) obj);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0(Uri uri) {
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, uri);
    }

    public /* synthetic */ void lambda$onCreate$1(Uri uri) {
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, uri);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mStrategyTemplate.onConfigurationChanged(configuration);
    }

    @Override // androidx.fragment.app.Fragment
    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        this.mStrategyTemplate.onMultiWindowModeChanged(z);
    }

    public void requestDisableStrategy(StrategyContext.DisableStrategyType disableStrategyType) {
        this.mStrategyTemplate.requestDisableStrategy(disableStrategyType);
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
