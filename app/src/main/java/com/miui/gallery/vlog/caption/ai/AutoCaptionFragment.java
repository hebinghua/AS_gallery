package com.miui.gallery.vlog.caption.ai;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.caption.ai.CaptionExtractor;
import com.miui.gallery.vlog.entity.Caption;
import com.miui.gallery.vlog.sdk.interfaces.IClipAudioManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class AutoCaptionFragment extends AndroidFragment implements CaptionExtractor.ExtractCallback {
    public AlertDialog mAlertDialog;
    public Callback mCallback;
    public CaptionExtractor mCaptionExtractor;
    public IClipAudioManager mClipAudioManager;
    public Disposable mDisposable;
    public boolean mHasCaption;
    public ProgressDialog mProgressDialog;
    public List<IVideoClip> mVideoClips;
    public VlogDependsModule mVlogDependsModule;
    public final String TAG = "AutoCaptionFragment";
    public boolean isAutoCaptioning = false;
    public DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.vlog.caption.ai.AutoCaptionFragment.3
        {
            AutoCaptionFragment.this = this;
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            AutoCaptionFragment.this.cancel();
        }
    };

    /* loaded from: classes2.dex */
    public interface Callback {
        void onCancel();

        void onError(String str);

        void onExtracted(List<Caption> list);
    }

    public static /* synthetic */ void $r8$lambda$XXCpjrkknmJB1n89IUGDHPHssg8(AutoCaptionFragment autoCaptionFragment, boolean z, boolean z2) {
        autoCaptionFragment.lambda$showMeteredNetworkConfirmDialog$0(z, z2);
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        String string;
        super.onCreate(bundle);
        this.mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle(R$string.vlog_caption_ai_replace_title).setMessage(R$string.vlog_caption_ai_replace_message).setNegativeButton(R$string.vlog_caption_ai_cancel, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.vlog.caption.ai.AutoCaptionFragment.2
            {
                AutoCaptionFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AutoCaptionFragment.this.cancel();
            }
        }).setPositiveButton(R$string.vlog_caption_ai_conform, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.vlog.caption.ai.AutoCaptionFragment.1
            {
                AutoCaptionFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AutoCaptionFragment.this.process();
            }
        }).create();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.mProgressDialog = progressDialog;
        progressDialog.setProgressStyle(1);
        VlogDependsModule vlogDependsModule = (VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class);
        this.mVlogDependsModule = vlogDependsModule;
        ProgressDialog progressDialog2 = this.mProgressDialog;
        if (vlogDependsModule.isAiCaptionLibraryExist()) {
            string = getResources().getString(R$string.vlog_caption_ai_generating);
        } else {
            string = getResources().getString(R$string.vlog_caption_ai_connecting);
        }
        progressDialog2.setMessage(string);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setOnCancelListener(this.mOnCancelListener);
        CaptionExtractor captionExtractor = new CaptionExtractor(this.mClipAudioManager, this.mVideoClips);
        this.mCaptionExtractor = captionExtractor;
        captionExtractor.setExtractCallback(this);
        if (!AgreementsUtils.isNetworkingAgreementAccepted()) {
            ToastUtils.makeText(getActivity(), R$string.vlog_download_failed_for_notwork);
            cancel();
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(getActivity(), R$string.vlog_caption_ai_network_unavailable);
            cancel();
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            showMeteredNetworkConfirmDialog(getActivity());
        } else {
            processOrAlert();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getActivity().getWindow().addFlags(128);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        getActivity().getWindow().clearFlags(128);
    }

    public final void showMeteredNetworkConfirmDialog(FragmentActivity fragmentActivity) {
        NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.vlog.caption.ai.AutoCaptionFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                AutoCaptionFragment.$r8$lambda$XXCpjrkknmJB1n89IUGDHPHssg8(AutoCaptionFragment.this, z, z2);
            }
        });
    }

    public /* synthetic */ void lambda$showMeteredNetworkConfirmDialog$0(boolean z, boolean z2) {
        if (z) {
            processOrAlert();
        } else {
            cancel();
        }
    }

    public final void processOrAlert() {
        if (this.mHasCaption) {
            this.mAlertDialog.show();
        } else {
            process();
        }
    }

    public final void dismissAlertDialog() {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        this.mAlertDialog.dismiss();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        dismissAlertDialog();
        CaptionExtractor captionExtractor = this.mCaptionExtractor;
        if (captionExtractor != null) {
            captionExtractor.stop();
            this.mCaptionExtractor = null;
        }
        Disposable disposable = this.mDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mDisposable.dispose();
        }
        this.mCallback = null;
    }

    @Override // com.miui.gallery.vlog.caption.ai.CaptionExtractor.ExtractCallback
    public void onConnect() {
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setMessage(getResources().getString(R$string.vlog_caption_ai_generating));
    }

    @Override // com.miui.gallery.vlog.caption.ai.CaptionExtractor.ExtractCallback
    public void onProcess(int i) {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.setProgress(i);
        }
    }

    @Override // com.miui.gallery.vlog.caption.ai.CaptionExtractor.ExtractCallback
    public void onFinish(List<Caption> list) {
        this.isAutoCaptioning = false;
        DebugLogUtils.endDebugLog("AutoCaptionFragment", "AutoCaption extract");
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onExtracted(list);
        }
        dismiss();
    }

    @Override // com.miui.gallery.vlog.caption.ai.CaptionExtractor.ExtractCallback
    public void onError(String str) {
        this.isAutoCaptioning = false;
        DebugLogUtils.endDebugLog("AutoCaptionFragment", "AutoCaption extract");
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onError(str);
        }
        dismiss();
    }

    public final void cancel() {
        if (this.isAutoCaptioning) {
            this.isAutoCaptioning = false;
            DebugLogUtils.endDebugLog("AutoCaptionFragment", "AutoCaption extract");
        }
        CaptionExtractor captionExtractor = this.mCaptionExtractor;
        if (captionExtractor != null) {
            captionExtractor.stop();
            this.mCaptionExtractor = null;
        }
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCancel();
        }
        dismissProgressDialog();
        dismiss();
    }

    public void setHasCaption(boolean z) {
        this.mHasCaption = z;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setClipAudioManager(IClipAudioManager iClipAudioManager) {
        this.mClipAudioManager = iClipAudioManager;
    }

    public void setVideoClips(List<IVideoClip> list) {
        this.mVideoClips = list;
    }

    public void show(FragmentManager fragmentManager, String str) {
        fragmentManager.beginTransaction().add(this, str).commit();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
    }

    public void dismiss() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    public final void showProgressDialog() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog == null || progressDialog.isShowing()) {
            return;
        }
        this.mProgressDialog.show();
    }

    public final void dismissProgressDialog() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mProgressDialog.dismiss();
    }

    public final void process() {
        showProgressDialog();
        this.isAutoCaptioning = true;
        DebugLogUtils.startDebugLog("AutoCaptionFragment", "AutoCaption extract");
        if (!this.mVlogDependsModule.isAiCaptionLibraryExist()) {
            this.mDisposable = Schedulers.io().createWorker().schedule(new Runnable() { // from class: com.miui.gallery.vlog.caption.ai.AutoCaptionFragment.4
                {
                    AutoCaptionFragment.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).loadAiCaptionLibrary()) {
                        AutoCaptionFragment.this.mCaptionExtractor.extract();
                        return;
                    }
                    DebugLogUtils.endDebugLog("AutoCaptionFragment", "AutoCaption extract");
                    AutoCaptionFragment.this.dismiss();
                    if (AutoCaptionFragment.this.mCallback == null) {
                        return;
                    }
                    AutoCaptionFragment.this.mCallback.onError("");
                }
            });
        } else {
            this.mCaptionExtractor.extract();
        }
    }
}
