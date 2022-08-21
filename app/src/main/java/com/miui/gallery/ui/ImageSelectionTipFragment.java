package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class ImageSelectionTipFragment extends GalleryDialogFragment {
    public ImageSelectionTipDialog mImageSelectionTipDialog;

    /* renamed from: $r8$lambda$Ad-DR-MA-rMFLTI26Mv6rUX49dM */
    public static /* synthetic */ void m1502$r8$lambda$AdDRMArMFLTI26Mv6rUX49dM(ImageSelectionTipFragment imageSelectionTipFragment, View view) {
        imageSelectionTipFragment.lambda$onCreateDialog$0(view);
    }

    public static void showImageSelectionTipDialogIfNecessary(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            DefaultLogger.d("ImageSelectionTipFragment", "activity is null or finishing ,no need to show selection tip dialog");
        } else if (!MediaFeatureManager.isImageFeatureSelectionVisiable() || !MediaFeatureCacheManager.getInstance().shouldShowImageSelectionTip()) {
        } else {
            GalleryPreferences.Assistant.setIsFirstShowImageSelection(false);
            MediaFeatureCacheManager.getInstance().setFirstShowImageSelection(false);
            new ImageSelectionTipFragment().showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "ImageSelectionTip");
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ImageSelectionTipDialog positiveButtonOnClickListener = new ImageSelectionTipDialog(getActivity()).setPositiveButtonOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.ImageSelectionTipFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ImageSelectionTipFragment.m1502$r8$lambda$AdDRMArMFLTI26Mv6rUX49dM(ImageSelectionTipFragment.this, view);
            }
        });
        this.mImageSelectionTipDialog = positiveButtonOnClickListener;
        return positiveButtonOnClickListener;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(View view) {
        onDone(this.mImageSelectionTipDialog);
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mImageSelectionTipDialog.updateConfiguration(configuration);
    }

    public final void onDone(DialogInterface dialogInterface) {
        if (getActivity() == null) {
            return;
        }
        dismissAllowingStateLoss();
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        onDone(dialogInterface);
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        onDone(dialogInterface);
    }

    /* loaded from: classes2.dex */
    public class ImageSelectionTipDialog extends AlertDialog implements View.OnClickListener {
        public LinearLayout mButtonGroup;
        public LinearLayout mPictureGroup;
        public Button mPositiveBtn;
        public View.OnClickListener mPositiveListener;
        public TextView mSubtitle;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ImageSelectionTipDialog(Context context) {
            super(context);
            ImageSelectionTipFragment.this = r3;
            View inflate = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.image_selection_tips_dialog, (ViewGroup) null);
            setView(inflate);
            TextView textView = (TextView) inflate.findViewById(R.id.sub_title);
            this.mSubtitle = textView;
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            this.mPictureGroup = (LinearLayout) inflate.findViewById(R.id.pictureGroup);
            Button button = (Button) inflate.findViewById(R.id.positive_btn);
            this.mPositiveBtn = button;
            button.setOnClickListener(this);
            this.mButtonGroup = (LinearLayout) inflate.findViewById(R.id.buttonGroup);
            updateConfiguration(r3.getResources().getConfiguration());
            setCanceledOnTouchOutside(false);
        }

        @Override // android.app.Dialog
        public void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            super.setOnShowListener(onShowListener);
        }

        public ImageSelectionTipDialog setPositiveButtonOnClickListener(View.OnClickListener onClickListener) {
            this.mPositiveListener = onClickListener;
            return this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            View.OnClickListener onClickListener;
            if (view.getId() != R.id.positive_btn || (onClickListener = this.mPositiveListener) == null) {
                return;
            }
            onClickListener.onClick(view);
        }

        public void updateConfiguration(Configuration configuration) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mSubtitle.getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mButtonGroup.getLayoutParams();
            if (configuration.orientation == 2 || (ImageSelectionTipFragment.this.getActivity().isInMultiWindowMode() && !BaseBuildUtil.isLargeScreenIndependentOrientation())) {
                layoutParams2.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.image_selection_tips_dialog_land_button_margin_top);
                this.mPictureGroup.setVisibility(8);
                layoutParams.leftMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.image_selection_tips_dialog_land_detail_margin_left);
                layoutParams.rightMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.image_selection_tips_dialog_land_detail_margin_right);
            } else {
                this.mPictureGroup.setVisibility(0);
                layoutParams.leftMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.image_selection_tips_dialog_detail_margin_left);
                layoutParams.rightMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.image_selection_tips_dialog_detail_margin_right);
                layoutParams2.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.image_selection_tips_dialog_button_top_margin);
            }
            this.mSubtitle.setLayoutParams(layoutParams);
            this.mButtonGroup.setLayoutParams(layoutParams2);
        }
    }
}
