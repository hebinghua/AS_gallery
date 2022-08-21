package com.miui.gallery.signature.dialog.manage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import com.miui.gallery.R;
import com.miui.gallery.signature.SignatureConfig;
import com.miui.gallery.signature.SignatureManageActivity;
import com.miui.gallery.signature.dialog.manage.SignatureManageAdapter;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import miuix.internal.widget.DialogParentPanel;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class SignatureManagerDialog extends GalleryDialogFragment {
    public boolean mCurrentSignatureIsDelete;
    public String mCurrentSignaturePath;
    public Disposable mLoadSignaturesDisposable;
    public RecyclerView mRecyclerView;
    public SignatureManageAdapter mSignatureManageAdapter;
    public boolean finish = true;
    public SignatureManageAdapter.ClickDeleteSignatureListener mDeleteListener = new SignatureManageAdapter.ClickDeleteSignatureListener() { // from class: com.miui.gallery.signature.dialog.manage.SignatureManagerDialog$$ExternalSyntheticLambda1
        @Override // com.miui.gallery.signature.dialog.manage.SignatureManageAdapter.ClickDeleteSignatureListener
        public final void onDelete(SignatureManageModel signatureManageModel, int i) {
            SignatureManagerDialog.$r8$lambda$yt4H3RFpBAkijWKYTIKvKJFFPMg(SignatureManagerDialog.this, signatureManageModel, i);
        }
    };
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.signature.dialog.manage.SignatureManagerDialog.1
        {
            SignatureManagerDialog.this = this;
        }

        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(androidx.recyclerview.widget.RecyclerView recyclerView, View view, int i) {
            SignatureManageModel itemData;
            SignatureManageActivity currentActivity;
            if (SignatureManagerDialog.this.mSignatureManageAdapter.getItemViewType(i) == 2) {
                if (!SignatureManagerDialog.this.checkIsMax()) {
                    SignatureManageActivity currentActivity2 = SignatureManagerDialog.this.getCurrentActivity();
                    if (currentActivity2 != null) {
                        currentActivity2.prepareDataAndFinish(1, null, SignatureManagerDialog.this.mCurrentSignatureIsDelete);
                    }
                } else {
                    ToastUtils.makeText(SignatureManagerDialog.this.getContext(), SignatureManagerDialog.this.getResources().getString(R.string.signature_view_max_signature_limit_string));
                    return true;
                }
            } else if (SignatureManagerDialog.this.mSignatureManageAdapter.getItemViewType(i) == 3 && (itemData = SignatureManagerDialog.this.mSignatureManageAdapter.getItemData(i)) != null && !TextUtils.isEmpty(itemData.getPath()) && (currentActivity = SignatureManagerDialog.this.getCurrentActivity()) != null) {
                currentActivity.prepareDataAndFinish(2, itemData.getPath(), SignatureManagerDialog.this.mCurrentSignatureIsDelete);
            }
            return true;
        }
    };
    public final Comparator<File> sortDateComparator = new Comparator<File>() { // from class: com.miui.gallery.signature.dialog.manage.SignatureManagerDialog.2
        {
            SignatureManagerDialog.this = this;
        }

        @Override // java.util.Comparator
        public int compare(File file, File file2) {
            int i = ((file.lastModified() - file2.lastModified()) > 0L ? 1 : ((file.lastModified() - file2.lastModified()) == 0L ? 0 : -1));
            if (i > 0) {
                return -1;
            }
            return i == 0 ? 0 : 1;
        }
    };

    /* renamed from: $r8$lambda$GdPmm62Y4ymWnQuZ-AnlD9z9l5c */
    public static /* synthetic */ void m1395$r8$lambda$GdPmm62Y4ymWnQuZAnlD9z9l5c(SignatureManagerDialog signatureManagerDialog, ObservableEmitter observableEmitter) {
        signatureManagerDialog.lambda$loadLocalSignatures$2(observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$MbtBsyAMsku4G_dG6WhsDfW8Ias(SignatureManagerDialog signatureManagerDialog, List list) {
        signatureManagerDialog.lambda$loadLocalSignatures$3(list);
    }

    /* renamed from: $r8$lambda$P3oG6x-DikxQ2xQYb8Zmf2Ly1a8 */
    public static /* synthetic */ void m1396$r8$lambda$P3oG6xDikxQ2xQYb8Zmf2Ly1a8(SignatureManagerDialog signatureManagerDialog, AlertDialog alertDialog, DialogInterface dialogInterface) {
        signatureManagerDialog.lambda$onCreateDialog$0(alertDialog, dialogInterface);
    }

    public static /* synthetic */ void $r8$lambda$yt4H3RFpBAkijWKYTIKvKJFFPMg(SignatureManagerDialog signatureManagerDialog, SignatureManageModel signatureManageModel, int i) {
        signatureManagerDialog.lambda$new$5(signatureManageModel, i);
    }

    public static SignatureManagerDialog newInstance(String str) {
        SignatureManagerDialog signatureManagerDialog = new SignatureManagerDialog();
        Bundle bundle = new Bundle();
        bundle.putString("param_current_signature_path", str);
        signatureManagerDialog.setArguments(bundle);
        return signatureManagerDialog;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments == null || !arguments.containsKey("param_current_signature_path")) {
            return;
        }
        this.mCurrentSignaturePath = arguments.getString("param_current_signature_path", "");
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), 2131951621);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.photo_editor_signature_manage_dialog, (ViewGroup) null, false);
        configView(inflate);
        builder.setTitle(R.string.signature_manage_dialog_title_string).setView(inflate).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.signature.dialog.manage.SignatureManagerDialog$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                SignatureManagerDialog.m1396$r8$lambda$P3oG6xDikxQ2xQYb8Zmf2Ly1a8(SignatureManagerDialog.this, create, dialogInterface);
            }
        });
        return create;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(AlertDialog alertDialog, DialogInterface dialogInterface) {
        TextView textView;
        alertDialog.getButton(-2).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.signature_dialog_manage_item_text_size));
        int identifier = alertDialog.getContext().getResources().getIdentifier("alertTitle", "id", getContext().getPackageName());
        if (identifier <= 0 || (textView = (TextView) alertDialog.findViewById(identifier)) == null) {
            return;
        }
        textView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.signature_dialog_manage_item_text_size));
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            FrameLayout frameLayout = (FrameLayout) window.getDecorView().findViewById(R.id.customPanel);
            if (frameLayout != null) {
                frameLayout.setPadding(0, 0, 0, 0);
            }
            window.setFlags(1024, 1024);
        }
    }

    public final void adaptWithMultiWindow() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRecyclerView.getLayoutParams();
        if (getActivity().isInMultiWindowMode()) {
            layoutParams.height = -2;
            layoutParams.width = -1;
        } else {
            layoutParams.height = (int) getResources().getDimension(R.dimen.signature_dialog_manage_max_list_height);
        }
        this.mRecyclerView.setLayoutParams(layoutParams);
        for (ViewParent parent = this.mRecyclerView.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof DialogParentPanel) {
                parent.getParent().requestLayout();
                return;
            }
        }
    }

    public final void configView(View view) {
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.signature_recycler_view);
        SignatureManageAdapter signatureManageAdapter = new SignatureManageAdapter(getContext(), null);
        this.mSignatureManageAdapter = signatureManageAdapter;
        signatureManageAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mSignatureManageAdapter.setClickDeleteSignatureListener(this.mDeleteListener);
        this.mRecyclerView.setAdapter(this.mSignatureManageAdapter);
        this.mRecyclerView.setLayoutManager(new CustomScrollerLinearLayoutManager(getActivity()));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adaptWithMultiWindow();
        loadLocalSignatures();
    }

    public final void loadLocalSignatures() {
        this.mLoadSignaturesDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.signature.dialog.manage.SignatureManagerDialog$$ExternalSyntheticLambda2
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                SignatureManagerDialog.m1395$r8$lambda$GdPmm62Y4ymWnQuZAnlD9z9l5c(SignatureManagerDialog.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.signature.dialog.manage.SignatureManagerDialog$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                SignatureManagerDialog.$r8$lambda$MbtBsyAMsku4G_dG6WhsDfW8Ias(SignatureManagerDialog.this, (List) obj);
            }
        }, SignatureManagerDialog$$ExternalSyntheticLambda4.INSTANCE);
    }

    public /* synthetic */ void lambda$loadLocalSignatures$2(ObservableEmitter observableEmitter) throws Exception {
        File[] listFiles = SignatureConfig.getSignatureFolder(getContext()).listFiles(SignatureManagerDialog$$ExternalSyntheticLambda5.INSTANCE);
        if (listFiles != null) {
            Arrays.sort(listFiles, this.sortDateComparator);
            ArrayList arrayList = new ArrayList();
            for (File file : listFiles) {
                arrayList.add(new SignatureManageModel(file.getAbsolutePath()));
            }
            observableEmitter.onNext(arrayList);
        }
    }

    public /* synthetic */ void lambda$loadLocalSignatures$3(List list) throws Exception {
        this.mSignatureManageAdapter.setSignatureManageModels(list);
    }

    public static /* synthetic */ void lambda$loadLocalSignatures$4(Throwable th) throws Exception {
        Log.d("SignatureManagerDialog", "accept " + th.getMessage());
    }

    public /* synthetic */ void lambda$new$5(SignatureManageModel signatureManageModel, int i) {
        if (signatureManageModel == null || TextUtils.isEmpty(signatureManageModel.getPath())) {
            return;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(signatureManageModel.getPath(), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("SignatureManagerDialog", "mDeleteListener"));
        if (!(documentFile != null && documentFile.delete())) {
            return;
        }
        if (!TextUtils.isEmpty(this.mCurrentSignaturePath) && this.mCurrentSignaturePath.equals(signatureManageModel.getPath())) {
            this.mCurrentSignatureIsDelete = true;
        }
        this.mSignatureManageAdapter.getSignatureManageModels().remove(signatureManageModel);
        if (this.mSignatureManageAdapter.getSignatureManageModels().size() == 1) {
            dismissAllowingStateLoss();
            return;
        }
        this.mSignatureManageAdapter.notifyItemRemoved(i);
        if (i == this.mSignatureManageAdapter.getSignatureManageModels().size()) {
            return;
        }
        SignatureManageAdapter signatureManageAdapter = this.mSignatureManageAdapter;
        signatureManageAdapter.notifyItemRangeChanged(i, signatureManageAdapter.getSignatureManageModels().size() - i);
    }

    public final boolean checkIsMax() {
        File signatureFolder = SignatureConfig.getSignatureFolder(getContext());
        return signatureFolder.exists() && signatureFolder.list().length >= 30;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        Disposable disposable = this.mLoadSignaturesDisposable;
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                this.mLoadSignaturesDisposable.dispose();
            }
            this.mLoadSignaturesDisposable = null;
        }
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        SignatureManageActivity currentActivity = getCurrentActivity();
        if (currentActivity == null || !this.finish) {
            return;
        }
        if (this.mCurrentSignatureIsDelete) {
            currentActivity.prepareDataAndFinish(-1, null, true);
        }
        currentActivity.finish();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        SignatureManageActivity currentActivity = getCurrentActivity();
        if (currentActivity == null || !this.finish) {
            return;
        }
        currentActivity.finish();
    }

    public final SignatureManageActivity getCurrentActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof SignatureManageActivity) {
            return (SignatureManageActivity) activity;
        }
        return null;
    }
}
