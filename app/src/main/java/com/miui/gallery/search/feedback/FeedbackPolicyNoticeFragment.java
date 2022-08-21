package com.miui.gallery.search.feedback;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.text.UrlSpan;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class FeedbackPolicyNoticeFragment extends GalleryDialogFragment {
    public DialogInterface.OnClickListener mPositiveClickListener;

    public static /* synthetic */ void $r8$lambda$s8_seBdRL48jqZqwoAvX5STPoYI(FeedbackPolicyNoticeFragment feedbackPolicyNoticeFragment, DialogInterface dialogInterface, int i) {
        feedbackPolicyNoticeFragment.lambda$onCreateDialog$0(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$swzVa_FdLQiz8fkKXha4NUtjcZs(FeedbackPolicyNoticeFragment feedbackPolicyNoticeFragment, DialogInterface dialogInterface, int i) {
        feedbackPolicyNoticeFragment.lambda$onCreateDialog$1(dialogInterface, i);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(false);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.user_notice_title).setMessage(buildUserNotice(getActivity(), R.string.search_feedback_user_notice, R.string.search_feedback_user_policy)).setPositiveButton(R.string.user_agree, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.search.feedback.FeedbackPolicyNoticeFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                FeedbackPolicyNoticeFragment.$r8$lambda$s8_seBdRL48jqZqwoAvX5STPoYI(FeedbackPolicyNoticeFragment.this, dialogInterface, i);
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.search.feedback.FeedbackPolicyNoticeFragment$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                FeedbackPolicyNoticeFragment.$r8$lambda$swzVa_FdLQiz8fkKXha4NUtjcZs(FeedbackPolicyNoticeFragment.this, dialogInterface, i);
            }
        }).setCheckBox(true, getString(R.string.do_not_remind_me)).create();
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        GalleryPreferences.Search.setShouldShowFeedbackPolicy(!((AlertDialog) getDialog()).isChecked());
        DialogInterface.OnClickListener onClickListener = this.mPositiveClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(dialogInterface, i);
        }
    }

    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        getActivity().finish();
    }

    public void setOnPositiveButtonClickListener(DialogInterface.OnClickListener onClickListener) {
        this.mPositiveClickListener = onClickListener;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getMessageView().setMovementMethod(LinkMovementMethod.getInstance());
    }

    public final SpannableStringBuilder buildUserNotice(final FragmentActivity fragmentActivity, int i, int i2) {
        String string = fragmentActivity.getString(i2);
        String string2 = fragmentActivity.getString(i, new Object[]{string});
        UrlSpan.UrlSpanOnClickListener urlSpanOnClickListener = new UrlSpan.UrlSpanOnClickListener() { // from class: com.miui.gallery.search.feedback.FeedbackPolicyNoticeFragment.1
            {
                FeedbackPolicyNoticeFragment.this = this;
            }

            @Override // com.miui.gallery.text.UrlSpan.UrlSpanOnClickListener
            public void onClick() {
                SearchFeedbackHelper.goToFeedbackPolicyPage(fragmentActivity);
            }
        };
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string2);
        int indexOf = string2.indexOf(string);
        spannableStringBuilder.setSpan(new UrlSpan(urlSpanOnClickListener), indexOf, string.length() + indexOf, 33);
        return spannableStringBuilder;
    }
}
