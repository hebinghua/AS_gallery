package com.miui.gallery.ui.album.picker;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import com.miui.security.CrossUserCompatActivity;

/* loaded from: classes2.dex */
public class PickerPageUtils {
    public static boolean isCrossUserPick(Activity activity) {
        return (activity instanceof CrossUserCompatActivity) && ((CrossUserCompatActivity) activity).isCrossUserPick();
    }

    public static boolean isCrossUserPick(Fragment fragment) {
        return isCrossUserPick(fragment.getActivity());
    }
}
