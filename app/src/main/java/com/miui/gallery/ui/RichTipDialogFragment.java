package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.R;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.RichTextUtil;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class RichTipDialogFragment extends GalleryDialogFragment {
    public RichTipDialog mDialog;
    public View.OnClickListener mHyperLinkClickListener;
    public CharSequence mHyperLinkText;
    public LottieAnimationView mLottieAnimationView;
    public CharSequence mNegativeButtonText;
    public DialogInterface.OnClickListener mNegativeClickListener;
    public DialogInterface.OnCancelListener mOnCancelListener;
    public DialogInterface.OnDismissListener mOnDismissListener;
    public CharSequence mPositiveButtonText;
    public DialogInterface.OnClickListener mPositiveClickListener;
    public int mResourceId;
    public CharSequence mSubTitleText;
    public CharSequence mTitleText;

    public static /* synthetic */ void $r8$lambda$HNGSFet73VKmWx8wkb6lEFh8rdQ(RichTipDialogFragment richTipDialogFragment, View view) {
        richTipDialogFragment.lambda$onCreateDialog$1(view);
    }

    public static /* synthetic */ void $r8$lambda$QsNYgQyRlDZaVNQT0929V7rgHoI(RichTipDialogFragment richTipDialogFragment, View view) {
        richTipDialogFragment.lambda$onCreateDialog$0(view);
    }

    /* renamed from: $r8$lambda$hpkWxNCT5VXw_W-V8Ntd7VGuHSg */
    public static /* synthetic */ void m1562$r8$lambda$hpkWxNCT5VXw_WV8Ntd7VGuHSg(RichTipDialogFragment richTipDialogFragment, View view) {
        richTipDialogFragment.lambda$onCreateDialog$2(view);
    }

    public static RichTipDialogFragment newInstance(int i, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, CharSequence charSequence5) {
        RichTipDialogFragment richTipDialogFragment = new RichTipDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("resourceId", i);
        bundle.putCharSequence("title", charSequence);
        bundle.putCharSequence("subTitle", charSequence2);
        bundle.putCharSequence("hyper_link_text", charSequence3);
        bundle.putCharSequence("positiveButton", charSequence4);
        bundle.putCharSequence("negativeButton", charSequence5);
        richTipDialogFragment.setArguments(bundle);
        return richTipDialogFragment;
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }
        GalleryPreferences.MultiView.setHasShownTip(true);
        showAllowingStateLoss(fragmentManager, "richTipDialog" + hashCode());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mTitleText = getArguments().getCharSequence("title");
        this.mSubTitleText = getArguments().getCharSequence("subTitle");
        this.mHyperLinkText = getArguments().getCharSequence("hyper_link_text");
        this.mPositiveButtonText = getArguments().getCharSequence("positiveButton");
        this.mNegativeButtonText = getArguments().getCharSequence("negativeButton");
        int i = getArguments().getInt("resourceId");
        this.mResourceId = i;
        if (i > 0) {
            LottieAnimationView lottieAnimationView = new LottieAnimationView(getActivity());
            this.mLottieAnimationView = lottieAnimationView;
            lottieAnimationView.setVisibility(8);
            this.mLottieAnimationView.setAnimation(this.mResourceId);
            this.mLottieAnimationView.loop(true);
            this.mLottieAnimationView.setScale(1.0f / getResources().getDisplayMetrics().density);
            this.mLottieAnimationView.playAnimation();
        }
        this.mDialog.updateConfiguration(getResources().getConfiguration());
        this.mDialog.initValue();
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public final Dialog mo1072onCreateDialog(Bundle bundle) {
        RichTipDialog richTipDialog = new RichTipDialog(getActivity());
        richTipDialog.setHyperLinkViewOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.RichTipDialogFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RichTipDialogFragment.$r8$lambda$QsNYgQyRlDZaVNQT0929V7rgHoI(RichTipDialogFragment.this, view);
            }
        });
        richTipDialog.setPositiveButtonOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.RichTipDialogFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RichTipDialogFragment.$r8$lambda$HNGSFet73VKmWx8wkb6lEFh8rdQ(RichTipDialogFragment.this, view);
            }
        });
        richTipDialog.setNegativeButtonOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.RichTipDialogFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RichTipDialogFragment.m1562$r8$lambda$hpkWxNCT5VXw_WV8Ntd7VGuHSg(RichTipDialogFragment.this, view);
            }
        });
        richTipDialog.setCancelable(true);
        richTipDialog.setCanceledOnTouchOutside(false);
        this.mDialog = richTipDialog;
        return richTipDialog;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(View view) {
        View.OnClickListener onClickListener = this.mHyperLinkClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
        DialogInterface.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(this.mDialog);
        }
        dismissSafely();
    }

    public /* synthetic */ void lambda$onCreateDialog$1(View view) {
        DialogInterface.OnClickListener onClickListener = this.mPositiveClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this.mDialog, -1);
        }
        dismissSafely();
    }

    public /* synthetic */ void lambda$onCreateDialog$2(View view) {
        DialogInterface.OnClickListener onClickListener = this.mNegativeClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this.mDialog, -2);
        }
        DialogInterface.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(this.mDialog);
        }
        dismissSafely();
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDialog.updateConfiguration(configuration);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        LottieAnimationView lottieAnimationView = this.mLottieAnimationView;
        if (lottieAnimationView != null) {
            lottieAnimationView.playAnimation();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        LottieAnimationView lottieAnimationView = this.mLottieAnimationView;
        if (lottieAnimationView != null) {
            lottieAnimationView.pauseAnimation();
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LottieAnimationView lottieAnimationView = this.mLottieAnimationView;
        if (lottieAnimationView != null) {
            lottieAnimationView.cancelAnimation();
            this.mLottieAnimationView = null;
        }
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        DialogInterface.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        onDone(dialogInterface);
    }

    public final void onDone(DialogInterface dialogInterface) {
        if (getActivity() == null) {
            return;
        }
        DialogInterface.OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        } else {
            dismissAllowingStateLoss();
        }
    }

    /* loaded from: classes2.dex */
    public class RichTipDialog extends AlertDialog implements View.OnClickListener {
        public LinearLayout mButtonGroup;
        public FrameLayout mContentContainer;
        public View.OnClickListener mHyperLinkClickListener;
        public TextView mHyperLinkView;
        public Button mNegativeBtn;
        public View.OnClickListener mNegativeClickListener;
        public Button mPositiveBtn;
        public View.OnClickListener mPositiveClickListener;
        public TextView mSubTitleView;
        public TextView mTitleView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public RichTipDialog(Context context) {
            super(context);
            RichTipDialogFragment.this = r2;
            View inflate = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.rich_tip_dialog, (ViewGroup) null);
            setView(inflate);
            this.mTitleView = (TextView) inflate.findViewById(R.id.title);
            TextView textView = (TextView) inflate.findViewById(R.id.sub_title);
            this.mSubTitleView = textView;
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            TextView textView2 = (TextView) inflate.findViewById(R.id.hyper_link);
            this.mHyperLinkView = textView2;
            textView2.setOnClickListener(this);
            this.mContentContainer = (FrameLayout) inflate.findViewById(R.id.content);
            Button button = (Button) inflate.findViewById(R.id.positive_btn);
            this.mPositiveBtn = button;
            button.setOnClickListener(this);
            Button button2 = (Button) inflate.findViewById(R.id.negative_btn);
            this.mNegativeBtn = button2;
            button2.setOnClickListener(this);
            this.mButtonGroup = (LinearLayout) inflate.findViewById(R.id.buttonGroup);
        }

        public final void initValue() {
            if (!TextUtils.isEmpty(RichTipDialogFragment.this.mTitleText)) {
                this.mTitleView.setText(RichTipDialogFragment.this.mTitleText);
            }
            if (!TextUtils.isEmpty(RichTipDialogFragment.this.mSubTitleText)) {
                this.mSubTitleView.setText(RichTipDialogFragment.this.mSubTitleText);
            }
            if (!TextUtils.isEmpty(RichTipDialogFragment.this.mHyperLinkText)) {
                this.mHyperLinkView.setVisibility(0);
                this.mHyperLinkView.setText(RichTextUtil.appendDrawable(getContext(), RichTipDialogFragment.this.mHyperLinkText, R.drawable.sync_blue_arrow));
            }
            if (!TextUtils.isEmpty(RichTipDialogFragment.this.mPositiveButtonText)) {
                this.mPositiveBtn.setText(RichTipDialogFragment.this.mPositiveButtonText);
            }
            if (!TextUtils.isEmpty(RichTipDialogFragment.this.mNegativeButtonText)) {
                this.mNegativeBtn.setVisibility(0);
                this.mNegativeBtn.setText(RichTipDialogFragment.this.mNegativeButtonText);
            }
            if (RichTipDialogFragment.this.mLottieAnimationView != null) {
                this.mContentContainer.removeAllViews();
                this.mContentContainer.addView(RichTipDialogFragment.this.mLottieAnimationView);
            }
        }

        public RichTipDialog setHyperLinkViewOnClickListener(View.OnClickListener onClickListener) {
            this.mHyperLinkClickListener = onClickListener;
            return this;
        }

        public RichTipDialog setPositiveButtonOnClickListener(View.OnClickListener onClickListener) {
            this.mPositiveClickListener = onClickListener;
            return this;
        }

        public RichTipDialog setNegativeButtonOnClickListener(View.OnClickListener onClickListener) {
            this.mNegativeClickListener = onClickListener;
            return this;
        }

        public void updateConfiguration(Configuration configuration) {
            FrameLayout.LayoutParams layoutParams;
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mButtonGroup.getLayoutParams();
            if (RichTipDialogFragment.this.mLottieAnimationView != null) {
                if (RichTipDialogFragment.this.mLottieAnimationView.getLayoutParams() != null) {
                    layoutParams = (FrameLayout.LayoutParams) RichTipDialogFragment.this.mLottieAnimationView.getLayoutParams();
                } else {
                    layoutParams = new FrameLayout.LayoutParams(-2, -2, 17);
                }
                if (configuration.orientation == 2 && !BaseBuildUtil.isLargeScreenDevice()) {
                    layoutParams.height = -2;
                    layoutParams.width = -2;
                    RichTipDialogFragment.this.mLottieAnimationView.setVisibility(8);
                } else {
                    layoutParams.width = -1;
                    layoutParams.height = Math.round((-1) * 0.50859374f);
                    RichTipDialogFragment.this.mLottieAnimationView.setVisibility(0);
                }
                RichTipDialogFragment.this.mLottieAnimationView.setLayoutParams(layoutParams);
            }
            if (configuration.orientation == 2 && !BaseBuildUtil.isLargeScreenDevice()) {
                layoutParams2.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.rich_tip_dialog_button_land_top_margin);
            } else {
                layoutParams2.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.rich_tip_dialog_button_top_margin);
            }
            this.mButtonGroup.setLayoutParams(layoutParams2);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            View.OnClickListener onClickListener;
            if (view.getId() == R.id.positive_btn) {
                View.OnClickListener onClickListener2 = this.mPositiveClickListener;
                if (onClickListener2 == null) {
                    return;
                }
                onClickListener2.onClick(view);
            } else if (view.getId() == R.id.negative_btn) {
                View.OnClickListener onClickListener3 = this.mNegativeClickListener;
                if (onClickListener3 == null) {
                    return;
                }
                onClickListener3.onClick(view);
            } else if (view.getId() != R.id.hyper_link || (onClickListener = this.mHyperLinkClickListener) == null) {
            } else {
                onClickListener.onClick(view);
            }
        }
    }
}
