package com.miui.gallery.people.mark;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.people.mark.MarkPeopleDialogFragment;
import com.miui.gallery.people.mark.MarkPeopleSuggestionContract;
import com.miui.gallery.people.model.People;
import com.miui.gallery.ui.ToastDialogFragment;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MarkMyselfViewHelper implements MarkPeopleSuggestionContract.View {
    public Fragment mFragment;
    public MarkPeopleSuggestionPresenter mMarkMyselfPresenter = null;
    public MarkMyselfSuggestionModel mMarkMyselfModel = null;
    public MarkPeopleDialogFragment mMarkDialogFragment = null;
    public Bundle mMarkMyselfOptions = null;
    public DialogInterface.OnClickListener mLoadMoreClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.people.mark.MarkMyselfViewHelper$$ExternalSyntheticLambda1
        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            MarkMyselfViewHelper.$r8$lambda$A8UDA49K5Bv863b6BJZwY2pIlgw(MarkMyselfViewHelper.this, dialogInterface, i);
        }
    };
    public DialogInterface.OnCancelListener mCancelListener = new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.people.mark.MarkMyselfViewHelper$$ExternalSyntheticLambda0
        @Override // android.content.DialogInterface.OnCancelListener
        public final void onCancel(DialogInterface dialogInterface) {
            MarkMyselfViewHelper.$r8$lambda$FDJNsmyWYTHELMpMnQw9lVzg3io(MarkMyselfViewHelper.this, dialogInterface);
        }
    };
    public MarkPeopleDialogFragment.OnPeopleSelectListener mPeopleSelectListener = new MarkPeopleDialogFragment.OnPeopleSelectListener() { // from class: com.miui.gallery.people.mark.MarkMyselfViewHelper$$ExternalSyntheticLambda2
        @Override // com.miui.gallery.people.mark.MarkPeopleDialogFragment.OnPeopleSelectListener
        public final void onPeopleSelected(int i, People people) {
            MarkMyselfViewHelper.$r8$lambda$AG2NkS1jltdYxZZSMVX_5cTunyE(MarkMyselfViewHelper.this, i, people);
        }
    };

    public static /* synthetic */ void $r8$lambda$A8UDA49K5Bv863b6BJZwY2pIlgw(MarkMyselfViewHelper markMyselfViewHelper, DialogInterface dialogInterface, int i) {
        markMyselfViewHelper.lambda$new$0(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$AG2NkS1jltdYxZZSMVX_5cTunyE(MarkMyselfViewHelper markMyselfViewHelper, int i, People people) {
        markMyselfViewHelper.lambda$new$2(i, people);
    }

    public static /* synthetic */ void $r8$lambda$FDJNsmyWYTHELMpMnQw9lVzg3io(MarkMyselfViewHelper markMyselfViewHelper, DialogInterface dialogInterface) {
        markMyselfViewHelper.lambda$new$1(dialogInterface);
    }

    /* renamed from: $r8$lambda$wKec-EETcZWAqVG7i-6ODVdV1hQ */
    public static /* synthetic */ void m1167$r8$lambda$wKecEETcZWAqVG7i6ODVdV1hQ(MarkMyselfViewHelper markMyselfViewHelper, ToastDialogFragment toastDialogFragment) {
        markMyselfViewHelper.lambda$showInfoDialog$3(toastDialogFragment);
    }

    public /* synthetic */ void lambda$new$0(DialogInterface dialogInterface, int i) {
        this.mMarkMyselfPresenter.loadMore();
    }

    public /* synthetic */ void lambda$new$1(DialogInterface dialogInterface) {
        this.mMarkMyselfPresenter.ignoreSuggestion();
    }

    public /* synthetic */ void lambda$new$2(int i, People people) {
        this.mMarkMyselfPresenter.markPeople(people);
    }

    public MarkMyselfViewHelper(Fragment fragment) {
        this.mFragment = fragment;
    }

    public boolean onStart() {
        if (this.mFragment.getActivity() == null) {
            return false;
        }
        this.mMarkDialogFragment = (MarkPeopleDialogFragment) this.mFragment.getFragmentManager().findFragmentByTag("MarkMyself");
        if (this.mMarkMyselfOptions == null) {
            this.mMarkMyselfOptions = MarkPeopleSuggestionContract.buildMarkInfoForMarkMyself(this.mFragment.getActivity());
        }
        if (this.mMarkMyselfModel == null) {
            this.mMarkMyselfModel = new MarkMyselfSuggestionModel();
        }
        if (this.mMarkMyselfModel.needMark(this.mMarkMyselfOptions)) {
            MarkPeopleSuggestionPresenter markPeopleSuggestionPresenter = new MarkPeopleSuggestionPresenter(this.mFragment.getActivity(), this, this.mMarkMyselfModel, this.mMarkMyselfOptions);
            this.mMarkMyselfPresenter = markPeopleSuggestionPresenter;
            markPeopleSuggestionPresenter.start();
            return true;
        }
        if (isSuggestionDialogVisible()) {
            MarkPeopleSuggestionPresenter markPeopleSuggestionPresenter2 = new MarkPeopleSuggestionPresenter(this.mFragment.getActivity(), this, this.mMarkMyselfModel, this.mMarkMyselfOptions);
            this.mMarkMyselfPresenter = markPeopleSuggestionPresenter2;
            markPeopleSuggestionPresenter2.start();
        }
        return false;
    }

    public void onStop() {
        MarkPeopleSuggestionPresenter markPeopleSuggestionPresenter = this.mMarkMyselfPresenter;
        if (markPeopleSuggestionPresenter != null) {
            markPeopleSuggestionPresenter.stop();
        }
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.View
    public void showSuggestionDialog(String str, String str2, ArrayList<People> arrayList) {
        if (this.mFragment.getFragmentManager() == null) {
            return;
        }
        if (this.mMarkDialogFragment == null) {
            this.mMarkDialogFragment = new MarkPeopleDialogFragment();
        }
        this.mMarkDialogFragment.setTitle(str);
        this.mMarkDialogFragment.setLoadMoreButton(str2, this.mLoadMoreClickListener);
        this.mMarkDialogFragment.setCancelButton(false, this.mCancelListener);
        this.mMarkDialogFragment.setPeopleList(arrayList, this.mPeopleSelectListener);
        if (this.mMarkDialogFragment.isAdded() || this.mFragment.getFragmentManager().findFragmentByTag("MarkMyself") != null) {
            return;
        }
        this.mMarkDialogFragment.showAllowingStateLoss(this.mFragment.getFragmentManager(), "MarkMyself");
        this.mFragment.getFragmentManager().executePendingTransactions();
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.View
    public void dismissSuggestionDialog() {
        MarkPeopleDialogFragment markPeopleDialogFragment = this.mMarkDialogFragment;
        if (markPeopleDialogFragment != null) {
            markPeopleDialogFragment.dismissAllowingStateLoss();
            this.mMarkDialogFragment = null;
        }
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.View
    public boolean isSuggestionDialogVisible() {
        MarkPeopleDialogFragment markPeopleDialogFragment = this.mMarkDialogFragment;
        return markPeopleDialogFragment != null && markPeopleDialogFragment.isAdded();
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.View
    public void goToActivity(Uri uri) {
        if (this.mFragment.getActivity() != null) {
            ActionURIHandler.handleUri(this.mFragment.getActivity(), uri);
        }
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.View
    public void showMarkResult(boolean z, String str) {
        if (this.mFragment.getActivity() == null || TextUtils.isEmpty(str)) {
            return;
        }
        ToastUtils.makeText(this.mFragment.getActivity(), str);
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.View
    public void showInfoDialog(String str, String str2) {
        if (this.mFragment.getActivity() != null) {
            final ToastDialogFragment newInstance = ToastDialogFragment.newInstance(str, str2, R.string.ok);
            if (newInstance.isAdded() || this.mFragment.getFragmentManager().findFragmentByTag("SetUpGroup") != null) {
                return;
            }
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.people.mark.MarkMyselfViewHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    MarkMyselfViewHelper.m1167$r8$lambda$wKecEETcZWAqVG7i6ODVdV1hQ(MarkMyselfViewHelper.this, newInstance);
                }
            });
        }
    }

    public /* synthetic */ void lambda$showInfoDialog$3(ToastDialogFragment toastDialogFragment) {
        toastDialogFragment.showAllowingStateLoss(this.mFragment.getFragmentManager(), "SetUpGroup");
    }

    public void setLoadMoreMarkResult(boolean z) {
        this.mMarkMyselfPresenter.setLoadMoreMarkResult(z);
    }
}
