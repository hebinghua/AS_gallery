package com.miui.gallery.editor.photo.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import com.miui.gallery.R;
import com.miui.gallery.util.ToastUtils;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class VideoExportFragment extends DialogFragment {
    public Callbacks mCallbacks;
    public Integer mExportResult;
    public long mLastBackPressedTime;

    /* loaded from: classes2.dex */
    public interface Callbacks {
        void doCancel();

        int doExport();

        void onCancelled();

        void onExported(boolean z);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getActivity().getString(R.string.filter_sky_dynamic_saving));
        progressDialog.setProgressStyle(1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.editor.photo.app.VideoExportFragment.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4 && keyEvent.getAction() == 0) {
                    return VideoExportFragment.this.onBackPress();
                }
                return false;
            }
        });
        progressDialog.setProgress(0);
        return progressDialog;
    }

    public final boolean onBackPress() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
            this.mLastBackPressedTime = currentTimeMillis;
            if (getContext() == null) {
                return false;
            }
            ToastUtils.makeText(getActivity(), getString(R.string.filter_sky_dynamic_stop_saving), 0);
            return true;
        }
        this.mLastBackPressedTime = 0L;
        Callbacks callbacks = this.mCallbacks;
        if (callbacks == null) {
            return true;
        }
        callbacks.doCancel();
        return true;
    }

    public void setProgress(int i) {
        Dialog dialog = getDialog();
        if (dialog instanceof ProgressDialog) {
            ((ProgressDialog) dialog).setProgress(i);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        new ExportTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        Integer num = this.mExportResult;
        if (num != null) {
            publishResult(num.intValue());
        }
    }

    /* loaded from: classes2.dex */
    public static class ExportTask extends AsyncTask<Void, Void, Integer> {
        public final WeakReference<VideoExportFragment> mFragmentWeakReference;

        public ExportTask(VideoExportFragment videoExportFragment) {
            this.mFragmentWeakReference = new WeakReference<>(videoExportFragment);
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            Callbacks callbacks;
            VideoExportFragment videoExportFragment = this.mFragmentWeakReference.get();
            if (videoExportFragment != null && (callbacks = videoExportFragment.mCallbacks) != null) {
                return Integer.valueOf(callbacks.doExport());
            }
            return 2;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((ExportTask) num);
            if (num == null) {
                num = 2;
            }
            VideoExportFragment videoExportFragment = this.mFragmentWeakReference.get();
            if (videoExportFragment != null) {
                videoExportFragment.publishResult(num.intValue());
            }
        }
    }

    public final void publishResult(int i) {
        if (!isResumed()) {
            this.mExportResult = Integer.valueOf(i);
        } else if (i == 3) {
            Callbacks callbacks = this.mCallbacks;
            if (callbacks == null) {
                return;
            }
            callbacks.onCancelled();
        } else {
            Callbacks callbacks2 = this.mCallbacks;
            if (callbacks2 == null) {
                return;
            }
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            callbacks2.onExported(z);
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
