package com.miui.gallery;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.baseui.R$dimen;
import com.miui.gallery.baseui.R$drawable;
import com.miui.gallery.permission.R$id;
import com.miui.gallery.permission.R$layout;
import com.miui.gallery.permission.R$string;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.R$style;

/* loaded from: classes.dex */
public abstract class BaseTermsDialogFragment extends DialogFragment {
    public OnAgreementInvokedListener mAgreementListener;
    public AlertDialog mDialog;

    public static /* synthetic */ void $r8$lambda$Si_XttBtY63t4qXZILnJpNkPi7I(BaseTermsDialogFragment baseTermsDialogFragment, DialogInterface dialogInterface, int i) {
        baseTermsDialogFragment.lambda$getClickListener$0(dialogInterface, i);
    }

    public abstract void configList(View view);

    public abstract CharSequence getNegativeButtonText();

    public abstract CharSequence getPositiveButtonText();

    public abstract CharSequence getSummary();

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requireActivity().getTheme().applyStyle(R$style.Theme_PreferenceOverlay_DayNight, false);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R$layout.user_agreement, (ViewGroup) null);
        initView(inflate);
        AlertDialog.Builder positiveButton = new AlertDialog.Builder(getActivity()).setView(inflate).setHapticFeedbackEnabled(true).setTitle(getSummary()).setCancelable(false).setPositiveButton(getContext().getResources().getString(R$string.agreement_agree), getClickListener());
        if (getPositiveButtonText() != null) {
            positiveButton.setPositiveButton(getPositiveButtonText(), getClickListener());
        }
        if (getNegativeButtonText() != null) {
            positiveButton.setNegativeButton(getNegativeButtonText(), getClickListener());
        }
        AlertDialog create = positiveButton.create();
        this.mDialog = create;
        return create;
    }

    public final void initView(View view) {
        ((TextView) view.findViewById(R$id.summary)).setText(getSummary());
        configList(view);
        ((ImageView) view.findViewById(R$id.app_icon)).setImageDrawable(getContext().getApplicationInfo().loadIcon(getContext().getPackageManager()));
    }

    public DialogInterface.OnClickListener getClickListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.BaseTermsDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                BaseTermsDialogFragment.$r8$lambda$Si_XttBtY63t4qXZILnJpNkPi7I(BaseTermsDialogFragment.this, dialogInterface, i);
            }
        };
    }

    public /* synthetic */ void lambda$getClickListener$0(DialogInterface dialogInterface, int i) {
        OnAgreementInvokedListener onAgreementInvokedListener;
        if (this.mDialog.getButton(i).isEnabled() && (onAgreementInvokedListener = this.mAgreementListener) != null) {
            onAgreementInvokedListener.onAgreementInvoked(i == -1);
        }
    }

    /* loaded from: classes.dex */
    public static class BaseDividerDecoration extends RecyclerView.ItemDecoration {
        public Drawable mDividerDrawable;
        public int mDividerHeight;
        public int mPaddingStart = -1;

        public void drawTop(Canvas canvas, View view, int i, int i2) {
            float top = (view.getTop() - ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).topMargin) + view.getTranslationY();
            int i3 = this.mDividerHeight;
            int i4 = (int) (top - i3);
            this.mDividerDrawable.setBounds(i, i4, i2, i3 + i4);
            this.mDividerDrawable.draw(canvas);
        }

        public void drawBottom(Canvas canvas, View view, int i, int i2) {
            int bottom = (int) (view.getBottom() + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).bottomMargin + view.getTranslationY());
            this.mDividerDrawable.setBounds(i, bottom, i2, this.mDividerHeight + bottom);
            this.mDividerDrawable.draw(canvas);
        }

        public final void init(Context context) {
            if (this.mDividerDrawable == null) {
                this.mDividerDrawable = context.getResources().getDrawable(R$drawable.item_divider_bg);
                this.mDividerHeight = context.getResources().getDimensionPixelSize(R$dimen.divider_line_height);
                this.mPaddingStart = context.getResources().getDimensionPixelSize(com.miui.gallery.permission.R$dimen.agreement_item_padding_start);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int childAdapterPosition;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null || (childAdapterPosition = recyclerView.getChildAdapterPosition(view)) == -1) {
                return;
            }
            init(view.getContext());
            rect.set(0, this.mDividerHeight, 0, childAdapterPosition == adapter.getItemCount() + (-1) ? this.mDividerHeight : 0);
        }
    }
}
