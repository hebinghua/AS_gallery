package com.miui.gallery.editor.photo.app;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.miui.gallery.editor.photo.utils.Attachable;
import com.miui.gallery.util.SystemUiUtil;

/* loaded from: classes2.dex */
public class EditorFragment extends Fragment {
    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof Attachable) {
            ((Attachable) parentFragment).attach(this);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SystemUiUtil.setWindowFullScreenFlag(getActivity());
    }
}
