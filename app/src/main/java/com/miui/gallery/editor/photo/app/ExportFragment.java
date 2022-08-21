package com.miui.gallery.editor.photo.app;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import com.miui.gallery.R;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class ExportFragment extends DialogFragment {
    public Callbacks mCallbacks;
    public Boolean mExportResult;

    /* loaded from: classes2.dex */
    public interface Callbacks {
        boolean doExport();

        void onCancelled(boolean z);

        void onExported(boolean z);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getActivity().getString(R.string.photo_editor_saving));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        new ExportTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        Boolean bool = this.mExportResult;
        if (bool != null) {
            publishResult(bool);
        }
    }

    /* loaded from: classes2.dex */
    public static class ExportTask extends AsyncTask<Void, Void, Boolean> {
        public final WeakReference<ExportFragment> mFragmentWeakReference;

        public ExportTask(ExportFragment exportFragment) {
            this.mFragmentWeakReference = new WeakReference<>(exportFragment);
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            Callbacks callbacks;
            ExportFragment exportFragment = this.mFragmentWeakReference.get();
            if (exportFragment != null && (callbacks = exportFragment.mCallbacks) != null) {
                return Boolean.valueOf(callbacks.doExport());
            }
            return Boolean.FALSE;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            super.onPostExecute((ExportTask) bool);
            ExportFragment exportFragment = this.mFragmentWeakReference.get();
            if (exportFragment != null) {
                exportFragment.publishResult(bool);
            }
        }
    }

    public final void publishResult(Boolean bool) {
        if (!isAdded() || isRemoving()) {
            Callbacks callbacks = this.mCallbacks;
            if (callbacks == null) {
                return;
            }
            callbacks.onCancelled(bool.booleanValue());
        } else if (isResumed()) {
            Callbacks callbacks2 = this.mCallbacks;
            if (callbacks2 == null) {
                return;
            }
            callbacks2.onExported(bool.booleanValue());
        } else {
            this.mExportResult = bool;
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mCallbacks = null;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }
}
