package com.miui.gallery.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.projection.ProjectionVideoController;
import com.miui.gallery.util.BaseFileUtils;

/* loaded from: classes2.dex */
public class ProjectVideoFragment extends AndroidFragment implements ProjectionVideoController.OnFinishListener {
    public ProjectionVideoController mRemoteVideoControl;

    public static /* synthetic */ Fragment $r8$lambda$jZtIQXavDN9ohAXJV1kw__0modU(BaseDataItem baseDataItem, String str) {
        return lambda$showProjectVideoFragment$0(baseDataItem, str);
    }

    public static void showProjectVideoFragment(BaseActivity baseActivity, final BaseDataItem baseDataItem) {
        baseActivity.startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.ui.ProjectVideoFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                return ProjectVideoFragment.$r8$lambda$jZtIQXavDN9ohAXJV1kw__0modU(BaseDataItem.this, str);
            }
        }, null, true, true);
    }

    public static /* synthetic */ Fragment lambda$showProjectVideoFragment$0(BaseDataItem baseDataItem, String str) {
        ProjectVideoFragment projectVideoFragment = new ProjectVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("photo_data_item", baseDataItem);
        projectVideoFragment.setArguments(bundle);
        return projectVideoFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ProjectionVideoController projectionVideoController = (ProjectionVideoController) layoutInflater.inflate(R.layout.projection_video, viewGroup, false);
        this.mRemoteVideoControl = projectionVideoController;
        projectionVideoController.initView();
        return this.mRemoteVideoControl;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        BaseDataItem baseDataItem = (BaseDataItem) getArguments().getSerializable("photo_data_item");
        String originalPath = baseDataItem.getOriginalPath();
        this.mRemoteVideoControl.startPlay(originalPath, TextUtils.isEmpty(baseDataItem.getTitle()) ? BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(originalPath)) : baseDataItem.getTitle());
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mRemoteVideoControl.setOnFinishListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        this.mRemoteVideoControl.setOnFinishListener(null);
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        setResult();
        super.onDestroy();
    }

    public final void setResult() {
        AndroidFragment androidFragment = (AndroidFragment) getTargetFragment();
        if (androidFragment != null) {
            androidFragment.onActivityResult(25, -1, null);
        }
    }

    @Override // com.miui.gallery.projection.ProjectionVideoController.OnFinishListener
    public void onFinish() {
        if (!isResumed() || requireActivity().getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            return;
        }
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
