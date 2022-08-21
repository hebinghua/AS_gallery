package com.miui.gallery.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryDialogFragment extends DialogFragment {
    public boolean mIsShowing = false;

    /* loaded from: classes2.dex */
    public interface OnCancelListener {
        void onCancel(Fragment fragment);
    }

    /* loaded from: classes2.dex */
    public interface OnClickListener {
        void onClick(Fragment fragment, int i);
    }

    public void showAllowingStateLoss(FragmentManager fragmentManager, String str) {
        if (this.mIsShowing) {
            return;
        }
        try {
            if (fragmentManager != null) {
                this.mIsShowing = true;
                show(fragmentManager, str);
            } else {
                DefaultLogger.e("GalleryDialogFragment", "null FragmentManager");
            }
        } catch (IllegalStateException e) {
            DefaultLogger.w("GalleryDialogFragment", "%s : showAllowingStateLoss ignore:%s", getClass().getSimpleName(), e);
        }
    }

    public void dismissSafely() {
        this.mIsShowing = false;
        Dialog dialog = getDialog();
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        dismissAllowingStateLoss();
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        this.mIsShowing = false;
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Dialog dialog = getDialog();
        if (dialog != null) {
            if (configuration.smallestScreenWidthDp >= BaseBuildUtil.BIG_HORIZONTAL_WINDOW_STANDARD || configuration.orientation == 1) {
                SystemUiUtil.clearWindowFullScreenFlag(dialog.getWindow());
            } else {
                SystemUiUtil.setWindowFullScreenFlag(dialog.getWindow());
            }
        }
    }
}
