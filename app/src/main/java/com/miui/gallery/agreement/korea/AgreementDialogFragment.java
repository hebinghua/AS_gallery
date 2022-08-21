package com.miui.gallery.agreement.korea;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.BaseTermsDialogFragment;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.Agreement;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.permission.R$dimen;
import com.miui.gallery.permission.R$id;
import com.miui.gallery.permission.R$layout;
import com.miui.gallery.permission.R$string;
import java.util.ArrayList;
import java.util.List;
import miuix.preference.R$drawable;
import miuix.preference.R$style;

/* loaded from: classes.dex */
public class AgreementDialogFragment extends BaseTermsDialogFragment {
    public AgreementAdapter mAdapter;

    public static AgreementDialogFragment newInstance(ArrayList<Agreement> arrayList) {
        AgreementDialogFragment agreementDialogFragment = new AgreementDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("extra_agreements", arrayList);
        agreementDialogFragment.setCancelable(false);
        agreementDialogFragment.setArguments(bundle);
        return agreementDialogFragment;
    }

    @Override // com.miui.gallery.BaseTermsDialogFragment
    public void configList(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R$id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AgreementAdapter agreementAdapter = new AgreementAdapter();
        this.mAdapter = agreementAdapter;
        recyclerView.setAdapter(agreementAdapter);
        this.mAdapter.setAgreements(getArguments().getParcelableArrayList("extra_agreements"));
    }

    @Override // com.miui.gallery.BaseTermsDialogFragment
    public CharSequence getSummary() {
        return getContext().getResources().getString(R$string.agreement_summary);
    }

    @Override // com.miui.gallery.BaseTermsDialogFragment
    public CharSequence getPositiveButtonText() {
        return getContext().getResources().getString(R$string.agreement_agree);
    }

    @Override // com.miui.gallery.BaseTermsDialogFragment
    public CharSequence getNegativeButtonText() {
        return getContext().getResources().getString(R$string.agreement_quit);
    }

    public void invoke(FragmentActivity fragmentActivity, OnAgreementInvokedListener onAgreementInvokedListener) {
        this.mAgreementListener = onAgreementInvokedListener;
        Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag("AgreementDialogFragment");
        if (findFragmentByTag != this && (findFragmentByTag instanceof AgreementDialogFragment)) {
            ((DialogFragment) findFragmentByTag).dismiss();
        }
        show(fragmentActivity.getSupportFragmentManager(), "AgreementDialogFragment");
    }

    /* loaded from: classes.dex */
    public class AgreementAdapter extends RecyclerView.Adapter<AgreementViewHolder> implements AgreementViewHolder.OnCheckChangedListener {
        public List<AgreementWrapper> mAgreements;

        public AgreementAdapter() {
            this.mAgreements = new ArrayList();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public AgreementViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new AgreementViewHolder(AgreementViewHolder.getView(viewGroup, R$layout.user_agreement_item));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(AgreementViewHolder agreementViewHolder, int i) {
            agreementViewHolder.bindAgreement(this.mAgreements.get(i), this);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mAgreements.size();
        }

        public void setAgreements(List<Agreement> list) {
            this.mAgreements.clear();
            if (list != null) {
                for (Agreement agreement : list) {
                    AgreementWrapper agreementWrapper = new AgreementWrapper(agreement, false);
                    agreementWrapper.mChecked = true;
                    this.mAgreements.add(agreementWrapper);
                }
                if (!this.mAgreements.isEmpty()) {
                    AgreementWrapper agreementWrapper2 = new AgreementWrapper(new Agreement(AgreementDialogFragment.this.getContext().getResources().getString(R$string.select_all), null, false), true);
                    agreementWrapper2.mChecked = true;
                    this.mAgreements.add(0, agreementWrapper2);
                }
            }
            notifyDataSetChanged();
        }

        @Override // com.miui.gallery.agreement.korea.AgreementDialogFragment.AgreementViewHolder.OnCheckChangedListener
        public void onCheckChanged(AgreementWrapper agreementWrapper) {
            boolean z;
            boolean z2 = true;
            if (agreementWrapper.isSelectItem) {
                z = false;
                for (AgreementWrapper agreementWrapper2 : this.mAgreements) {
                    if (agreementWrapper2.mChecked != agreementWrapper.mChecked) {
                        agreementWrapper2.mChecked = agreementWrapper.mChecked;
                        z = true;
                    }
                }
            } else {
                AgreementWrapper agreementWrapper3 = null;
                boolean z3 = true;
                for (AgreementWrapper agreementWrapper4 : this.mAgreements) {
                    if (agreementWrapper4.isSelectItem) {
                        agreementWrapper3 = agreementWrapper4;
                    } else if (agreementWrapper.mChecked != agreementWrapper4.mChecked) {
                        z3 = false;
                    }
                }
                if (z3) {
                    z = agreementWrapper3.mChecked != agreementWrapper.mChecked;
                    agreementWrapper3.mChecked = agreementWrapper.mChecked;
                } else {
                    z = agreementWrapper3.mChecked;
                    agreementWrapper3.mChecked = false;
                }
            }
            if (z) {
                notifyDataSetChanged();
            }
            for (AgreementWrapper agreementWrapper5 : this.mAgreements) {
                if (agreementWrapper5.mRequired && !agreementWrapper5.mChecked) {
                    z2 = false;
                }
            }
            ((BaseTermsDialogFragment) AgreementDialogFragment.this).mDialog.getButton(-1).setEnabled(z2);
        }
    }

    /* loaded from: classes.dex */
    public static class AgreementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AgreementWrapper mAgreement;
        public CheckBox mCheckBox;
        public OnCheckChangedListener mCheckChangedListener;
        public TextView mText;

        /* loaded from: classes.dex */
        public interface OnCheckChangedListener {
            void onCheckChanged(AgreementWrapper agreementWrapper);
        }

        public AgreementViewHolder(View view) {
            super(view);
            this.mText = (TextView) view.findViewById(R$id.title);
            this.mCheckBox = (CheckBox) view.findViewById(R$id.checkbox);
        }

        public void bindAgreement(AgreementWrapper agreementWrapper, OnCheckChangedListener onCheckChangedListener) {
            this.mAgreement = agreementWrapper;
            this.mText.setText(parseAgreementText(agreementWrapper));
            Resources resources = this.itemView.getContext().getResources();
            if (!TextUtils.isEmpty(agreementWrapper.mLink)) {
                this.mText.setCompoundDrawablePadding(resources.getDimensionPixelSize(R$dimen.agreement_item_arrow_padding));
                this.mText.setCompoundDrawablesRelativeWithIntrinsicBounds((Drawable) null, (Drawable) null, this.itemView.getContext().getDrawable(R$drawable.miuix_appcompat_arrow_right), (Drawable) null);
            } else {
                this.mText.setCompoundDrawablesRelativeWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
            }
            if (agreementWrapper.isSelectItem) {
                this.mText.setTextAppearance(R$style.Miuix_AppCompat_TextAppearance_PreferenceList);
            } else {
                this.mText.setTextAppearance(com.miui.gallery.permission.R$style.TextAppearance_Agreement);
            }
            if (TextUtils.isEmpty(agreementWrapper.mLink)) {
                this.mText.setOnClickListener(null);
            } else {
                this.mText.setOnClickListener(this);
            }
            this.mCheckBox.setChecked(agreementWrapper.mChecked);
            this.mCheckBox.setOnClickListener(this);
            this.mCheckChangedListener = onCheckChangedListener;
        }

        public final CharSequence parseAgreementText(AgreementWrapper agreementWrapper) {
            StringBuilder sb = new StringBuilder();
            sb.append(agreementWrapper.mText);
            if (!agreementWrapper.isSelectItem) {
                sb.append(" ");
                Resources resources = this.itemView.getContext().getResources();
                if (agreementWrapper.mRequired) {
                    sb.append(resources.getString(R$string.agreement_required_tip));
                } else {
                    sb.append(resources.getString(R$string.agreement_optional_tip));
                }
            }
            return sb.toString();
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() == R$id.title) {
                AgreementsUtils.viewAgreement(view.getContext(), this.mAgreement);
            }
            if (view.getId() == R$id.checkbox) {
                this.mAgreement.mChecked = this.mCheckBox.isChecked();
                OnCheckChangedListener onCheckChangedListener = this.mCheckChangedListener;
                if (onCheckChangedListener == null) {
                    return;
                }
                onCheckChangedListener.onCheckChanged(this.mAgreement);
            }
        }

        public static View getView(ViewGroup viewGroup, int i) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
        }
    }

    /* loaded from: classes.dex */
    public static class AgreementWrapper extends Agreement {
        public final boolean isSelectItem;
        public boolean mChecked;

        public AgreementWrapper(Agreement agreement, boolean z) {
            super(agreement.mText, agreement.mLink, agreement.mRequired);
            this.isSelectItem = z;
        }
    }
}
