package com.miui.gallery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.fragment.AndroidFragment;

/* loaded from: classes2.dex */
public class ProjectSlideFragment extends AndroidFragment {
    public static /* synthetic */ Fragment $r8$lambda$zDgPyioOm_zBlNlZ8naGQ9U_wT4(String str, miuix.appcompat.app.Fragment fragment, String str2) {
        return lambda$showProjectSlideFragment$0(str, fragment, str2);
    }

    public static void showProjectSlideFragment(BaseActivity baseActivity, final miuix.appcompat.app.Fragment fragment, final String str) {
        baseActivity.startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.ui.ProjectSlideFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str2) {
                return ProjectSlideFragment.$r8$lambda$zDgPyioOm_zBlNlZ8naGQ9U_wT4(str, fragment, str2);
            }
        }, "ProjectSlideFragment", true, true);
    }

    public static /* synthetic */ Fragment lambda$showProjectSlideFragment$0(String str, miuix.appcompat.app.Fragment fragment, String str2) {
        ProjectSlideFragment projectSlideFragment = new ProjectSlideFragment();
        Bundle bundle = new Bundle();
        bundle.putString("photo_connected_device", str);
        projectSlideFragment.setArguments(bundle);
        if (fragment != null) {
            projectSlideFragment.setTargetFragment(fragment, 24);
        }
        return projectSlideFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.projection_slide, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.project_slide_title)).setText(getActivity().getResources().getString(R.string.projection_slide_playing, getArguments().getString("photo_connected_device")));
        inflate.findViewById(R.id.project_slide_exit).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.ProjectSlideFragment.1
            {
                ProjectSlideFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ProjectSlideFragment.this.dismiss();
            }
        });
        return inflate;
    }

    public void dismiss() {
        setResult();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        setResult();
        super.onDestroy();
    }

    public final void setResult() {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment == null || getFragmentManager() == null || getFragmentManager().isDestroyed()) {
            return;
        }
        targetFragment.onActivityResult(24, -1, null);
    }
}
