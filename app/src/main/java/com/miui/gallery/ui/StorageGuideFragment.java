package com.miui.gallery.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.util.IntentUtil;

/* loaded from: classes2.dex */
public class StorageGuideFragment extends BaseFragment implements ImmersionMenuSupport {
    public StorageGuideCallback mHomePageGuideCallback;

    /* renamed from: $r8$lambda$-6lQf5mLJtrHUrGrW90m3HIpmt8 */
    public static /* synthetic */ void m1566$r8$lambda$6lQf5mLJtrHUrGrW90m3HIpmt8(StorageGuideFragment storageGuideFragment, boolean z, View view) {
        storageGuideFragment.lambda$onInflateView$0(z, view);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "guide";
    }

    @Override // com.miui.gallery.ui.ImmersionMenuSupport
    public int getSupportedAction() {
        return 0;
    }

    @Override // com.miui.gallery.ui.ImmersionMenuSupport
    public void onActionClick(int i) {
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof StorageGuideCallback) {
            this.mHomePageGuideCallback = (StorageGuideCallback) activity;
        }
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        final boolean z = false;
        View inflate = LayoutInflater.from(this.mActivity).inflate(R.layout.fragment_guide_storage, (ViewGroup) null, false);
        Button button = (Button) inflate.findViewById(R.id.action_button);
        TextView textView = (TextView) inflate.findViewById(R.id.title);
        boolean booleanExtra = getActivity().getIntent().getBooleanExtra("StartActivityWhenLocked", false);
        if (Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
            z = true;
        }
        if (z) {
            textView.setText(booleanExtra ? R.string.grant_permission_manage_storage_unlock : R.string.grant_permission_manage_storage);
        } else {
            textView.setText(booleanExtra ? R.string.grant_permission_storage_unlock : R.string.grant_permission_storage);
        }
        button.setText(booleanExtra ? R.string.grant_permission_unlock_and_set : R.string.grant_permission_go_and_set_2);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.StorageGuideFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StorageGuideFragment.m1566$r8$lambda$6lQf5mLJtrHUrGrW90m3HIpmt8(StorageGuideFragment.this, z, view);
            }
        });
        return inflate;
    }

    public /* synthetic */ void lambda$onInflateView$0(boolean z, View view) {
        TrackController.trackClick("403.59.1.1.14911");
        if (z) {
            IntentUtil.enterManageExternalStoragePermission(this.mActivity);
        } else {
            IntentUtil.enterGalleryPermissionSetting(this.mActivity);
        }
        StorageGuideCallback storageGuideCallback = this.mHomePageGuideCallback;
        if (storageGuideCallback != null) {
            storageGuideCallback.onGuideFollowed();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        HomePageActivity.HomeTabActionBarHelper homePageActionBarHelper = getActivity() instanceof HomePageActivity ? ((HomePageActivity) getActivity()).getHomePageActionBarHelper() : null;
        if (homePageActionBarHelper != null) {
            homePageActionBarHelper.setupActionBar();
        }
    }
}
