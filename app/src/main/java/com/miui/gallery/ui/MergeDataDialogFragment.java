package com.miui.gallery.ui;

import android.accounts.Account;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.BackupSettingsActivity;
import com.miui.gallery.activity.FloatingWindowActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.FileSizeFormatter;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SplitUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.xiaomi.stat.a.j;
import java.lang.ref.WeakReference;
import java.util.Locale;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class MergeDataDialogFragment extends GalleryDialogFragment {
    public static String URL_MICLOUD_PRIVACY_POLICY = "https://privacy.mi.com/xiaomicloud/%s";
    public static String URL_MICLOUD_USER_AGREEMENT = "https://i.mi.com/useragreement?lang=%s&region=%s";
    public boolean mJumpToSettings;
    public DialogInterface.OnClickListener mMergePositiveListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.MergeDataDialogFragment.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            Account account = AccountCache.getAccount();
            if (account != null) {
                GalleryPreferences.MiCloud.setPrivacyPolicyAgreed(account.name, true);
            }
            MergeDataDialogFragment.this.clearDirtyAndSetEnable();
        }
    };

    public static MergeDataDialogFragment newInstance(boolean z) {
        MergeDataDialogFragment mergeDataDialogFragment = new MergeDataDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("jump_to_settings", z);
        mergeDataDialogFragment.setArguments(bundle);
        return mergeDataDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mJumpToSettings = arguments.getBoolean("jump_to_settings");
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            ((AlertDialog) getDialog()).getMessageView().setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("jump_to_settings", this.mJumpToSettings);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        Account account = AccountCache.getAccount();
        if (account != null && GalleryPreferences.MiCloud.isPrivacyPolicyRejected(account.name)) {
            return new AlertDialog.Builder(getActivity()).setTitle(R.string.title_tip).setMessage(getString(R.string.privacy_cloud_confirm_rejected)).setPositiveButton(R.string.back, (DialogInterface.OnClickListener) null).setCancelable(true).create();
        }
        return new AlertDialog.Builder(getActivity()).setTitle(buildTitleString()).setMessage(buildMessageString()).setPositiveButton(17039370, this.mMergePositiveListener).setNegativeButton(17039360, buildNegativeListener()).setCancelable(true).create();
    }

    public final boolean needShowPrivacyDialog() {
        Account account = AccountCache.getAccount();
        return !BaseBuildUtil.isInternational() && !GalleryPreferences.MiCloud.isPrivacyPolicyAgreed(account == null ? "" : account.name);
    }

    public final CharSequence buildTitleString() {
        if (!needShowPrivacyDialog()) {
            TrackController.trackExpose("403.27.7.1.16845", AutoTracking.getRef());
            return getString(R.string.sync_backup_enable_confirm_title);
        }
        return getString(R.string.privacy_cloud_confirm);
    }

    public final CharSequence buildMessageString() {
        if (!needShowPrivacyDialog()) {
            return getString(R.string.sync_backup_enable_confirm_message);
        }
        Locale localeFromContext = FileSizeFormatter.localeFromContext(getContext());
        if (localeFromContext == null) {
            localeFromContext = Locale.US;
        }
        String locale = localeFromContext.toString();
        Locale locale2 = Locale.US;
        return Html.fromHtml(getString(R.string.privacy_cloud_confirm_detail, String.format(locale2, URL_MICLOUD_USER_AGREEMENT, localeFromContext.getLanguage(), localeFromContext.getCountry()), String.format(locale2, URL_MICLOUD_PRIVACY_POLICY, locale)));
    }

    public final DialogInterface.OnClickListener buildNegativeListener() {
        if (!needShowPrivacyDialog()) {
            return MergeDataDialogFragment$$ExternalSyntheticLambda0.INSTANCE;
        }
        return null;
    }

    public static /* synthetic */ void lambda$buildNegativeListener$0(DialogInterface dialogInterface, int i) {
        TrackController.trackClick("403.27.7.1.16826", AutoTracking.getRef());
    }

    public final void clearDirtyAndSetEnable() {
        TrackController.trackClick("403.22.1.1.11335", AutoTracking.getRef(), "open");
        if (!this.mJumpToSettings) {
            TrackController.trackClick("403.27.7.1.15963", AutoTracking.getRef());
        }
        ThreadManager.getMiscPool().submit(new CleanJob(), new CleanHandler(getActivity(), this.mJumpToSettings));
    }

    /* loaded from: classes2.dex */
    public static class CleanJob implements ThreadPool.Job<Boolean> {
        public CleanJob() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Boolean mo1807run(ThreadPool.JobContext jobContext) {
            return (Boolean) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{j.c}, String.format(Locale.US, "%s AND %s IS NOT NULL AND %s != ''", CloudUtils.itemIsNotGroup, "serverId", "serverId"), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Boolean>() { // from class: com.miui.gallery.ui.MergeDataDialogFragment.CleanJob.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public Boolean mo1808handle(Cursor cursor) {
                    if (cursor != null && cursor.getCount() > 0) {
                        return Boolean.TRUE;
                    }
                    return Boolean.FALSE;
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public static class CleanHandler extends FutureHandler<Boolean> {
        public WeakReference<Activity> mActivityRef;
        public boolean mJumpToSettings;

        public CleanHandler(Activity activity, boolean z) {
            this.mActivityRef = new WeakReference<>(activity);
            this.mJumpToSettings = z;
        }

        @Override // com.miui.gallery.concurrent.FutureHandler
        public void onPostExecute(Future<Boolean> future) {
            if (future != null && future.get().booleanValue()) {
                Preference.setSyncShouldClearDataBase(true);
            }
            Activity activity = this.mActivityRef.get();
            if (activity == null || activity.isDestroyed() || !SyncUtil.setSyncAutomatically(activity, true, 60) || !this.mJumpToSettings) {
                return;
            }
            boolean z = false;
            boolean booleanExtra = activity.getIntent().getBooleanExtra("use_dialog", false);
            Intent intent = new Intent(activity, BackupSettingsActivity.class);
            intent.putExtra("use_dialog", booleanExtra);
            if (activity instanceof FloatingWindowActivity) {
                z = ((FloatingWindowActivity) activity).needForceSplit();
            }
            if (z) {
                SplitUtils.addMiuiFlags(intent, 16);
            }
            activity.startActivity(intent);
        }
    }
}
