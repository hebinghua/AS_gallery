package com.miui.gallery.ui;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.google.common.collect.EvictingQueue;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.BackupSettingsActivity;
import com.miui.gallery.activity.BackupSettingsLoginActivity;
import com.miui.gallery.activity.FloatingWindowActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.CloudBackupStatusPreference;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.ui.SlideshowIntervalDialogFragment;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.OnRequestUpdateFinishAdapter;
import com.miui.gallery.util.RequestUpdateHelper;
import com.miui.gallery.util.SecurityShareHelper;
import com.miui.gallery.util.SplitUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SyncStatusObserverWrapper;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.GalleryLoggerFactory;
import com.miui.gallery.util.logger.TimingTracing;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Queue;
import miui.cloud.util.SyncAutoSettingUtil;
import miuix.preference.TextPreference;

/* loaded from: classes2.dex */
public class GallerySettingsFragment extends BasePreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener, CloudBackupStatusPreference.CloudButtonClickListener {
    public static SyncStatusObserverWrapper sSyncStatusObserverWrapper = new SyncStatusObserverWrapper();
    public CloudBackupStatusPreference mAutoBackupPref;
    public PreferenceCategory mBrowseCategory;
    public TextPreference mCorrectTime;
    public CheckBoxPreference mCreativityPref;
    public TextPreference mDebug;
    public TextPreference mDeleteDupMedias;
    public TextPreference mDumpDBInfo;
    public CheckBoxPreference mHeifConvertPref;
    public CheckBoxPreference mHookHeifDecoder;
    public CheckBoxPreference mImageSelectionPref;
    public long mLastRequestUpdateTime;
    public Object mObserverHandle;
    public PreferenceCategory mOtherCategory;
    public PreferenceScreen mPreferenceRoot;
    public CheckBoxPreference mPrintLog;
    public TextPreference mPrivacyEntrancePref;
    public TextPreference mPrivacyPolicyPref;
    public TextPreference mProblemFeedbackPref;
    public PreferenceCategory mRecommendCategory;
    public TextPreference mSecurityShareSettingsPref;
    public PreferenceCategory mSendCategory;
    public TextPreference mShowHiddenAlbumPref;
    public TextPreference mSlideshowIntervalPref;
    public CheckBoxPreference mSlideshowLoopPref;
    public TextPreference mStorageTest;
    public CheckBoxPreference mStoryFunctionPref;
    public TextPreference mThumbnailBuildError;
    public DrawablePreference mUpdateCheckPref;
    public OnRequestUpdateFinishListener mOnRequestUpdateFinishListener = new OnRequestUpdateFinishListener();
    public DebugRecorder mDebugRecorder = new DebugRecorder();
    public final Handler mMainHandler = new Handler();
    public SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() { // from class: com.miui.gallery.ui.GallerySettingsFragment.1
        {
            GallerySettingsFragment.this = this;
        }

        @Override // android.content.SyncStatusObserver
        public void onStatusChanged(int i) {
            FragmentActivity activity;
            if (i != 1 || (activity = GallerySettingsFragment.this.getActivity()) == null || activity.isFinishing()) {
                return;
            }
            GallerySettingsFragment.this.mMainHandler.post(new SyncStatusChangedRunnable(activity, GallerySettingsFragment.this.mAutoBackupPref));
        }
    };

    /* renamed from: $r8$lambda$T0nYgfHC1BQP-NGJ90pnJ-P_6dY */
    public static /* synthetic */ void m1454$r8$lambda$T0nYgfHC1BQPNGJ90pnJP_6dY(GallerySettingsFragment gallerySettingsFragment) {
        gallerySettingsFragment.refreshSlideshowIntervalPrefValue();
    }

    /* renamed from: $r8$lambda$w1mb7-PgquoipTfVbkhbpuZOKxQ */
    public static /* synthetic */ void m1455$r8$lambda$w1mb7PgquoipTfVbkhbpuZOKxQ(GallerySettingsFragment gallerySettingsFragment, FragmentManager fragmentManager, Fragment fragment) {
        gallerySettingsFragment.lambda$onCreate$0(fragmentManager, fragment);
    }

    /* loaded from: classes2.dex */
    public static class SyncStatusChangedRunnable implements Runnable {
        public WeakReference<Activity> mActivityRef;
        public WeakReference<CloudBackupStatusPreference> mPreferenceRef;

        public SyncStatusChangedRunnable(Activity activity, CloudBackupStatusPreference cloudBackupStatusPreference) {
            this.mActivityRef = new WeakReference<>(activity);
            this.mPreferenceRef = new WeakReference<>(cloudBackupStatusPreference);
        }

        @Override // java.lang.Runnable
        public void run() {
            Activity activity = this.mActivityRef.get();
            CloudBackupStatusPreference cloudBackupStatusPreference = this.mPreferenceRef.get();
            if (activity == null || activity.isFinishing() || cloudBackupStatusPreference == null) {
                return;
            }
            Account account = AccountCache.getAccount();
            DefaultLogger.d("GallerySettingsFragment", "masterSyncAutomatically: %s, xiaomiGlobalSyncAutomatically %s, gallerySyncAutomatically %s", Boolean.valueOf(ContentResolver.getMasterSyncAutomatically()), Boolean.valueOf(SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()), Boolean.valueOf(ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")));
            cloudBackupStatusPreference.setAutoBackupStatus(account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider"));
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        getPreferenceManager().setSharedPreferencesName("com.miui.gallery_preferences_new");
        addPreferencesFromResource(R.xml.gallery_preferences);
        this.mPreferenceRoot = (PreferenceScreen) findPreference("root");
        CloudBackupStatusPreference cloudBackupStatusPreference = (CloudBackupStatusPreference) findPreference("backup_automatically");
        this.mAutoBackupPref = cloudBackupStatusPreference;
        cloudBackupStatusPreference.setCloudClickListener(this);
        Account account = AccountCache.getAccount();
        DefaultLogger.w("GallerySettingsFragment", "masterSyncAutomatically: %s, xiaomiGlobalSyncAutomatically %s, gallerySyncAutomatically %s", Boolean.valueOf(ContentResolver.getMasterSyncAutomatically()), Boolean.valueOf(SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()), Boolean.valueOf(account == null ? false : ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")));
        boolean z = account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider");
        CloudBackupStatusPreference cloudBackupStatusPreference2 = this.mAutoBackupPref;
        if (cloudBackupStatusPreference2 != null) {
            cloudBackupStatusPreference2.setAutoBackupStatus(z);
            this.mAutoBackupPref.setUseDialog(isUseDialog());
            this.mAutoBackupPref.setForceSplit(isForceSplit());
        }
        this.mRecommendCategory = (PreferenceCategory) findPreference("recommend_category");
        this.mImageSelectionPref = (CheckBoxPreference) findPreference("image_selection_function");
        this.mStoryFunctionPref = (CheckBoxPreference) findPreference("story_function");
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("creativity_function");
        this.mCreativityPref = checkBoxPreference;
        checkBoxPreference.setOnPreferenceChangeListener(this);
        if (BaseBuildUtil.isInternational()) {
            this.mRecommendCategory.removePreference(this.mCreativityPref);
        }
        if (!MediaFeatureManager.isImageFeatureCalculationEnable()) {
            this.mRecommendCategory.removePreference(this.mImageSelectionPref);
            this.mRecommendCategory.removePreference(this.mStoryFunctionPref);
        } else {
            this.mImageSelectionPref.setOnPreferenceChangeListener(this);
            this.mStoryFunctionPref.setOnPreferenceChangeListener(this);
            if (!MediaFeatureManager.isDeviceSupportStoryFunction()) {
                this.mRecommendCategory.removePreference(this.mStoryFunctionPref);
            }
        }
        this.mBrowseCategory = (PreferenceCategory) findPreference("browse_category");
        TextPreference textPreference = (TextPreference) findPreference(GalleryPreferences.PrefKeys.HIDDEN_ALBUM_SHOW);
        this.mShowHiddenAlbumPref = textPreference;
        textPreference.setOnPreferenceClickListener(this);
        this.mShowHiddenAlbumPref.setVisible(PermissionUtils.canAccessStorage(getContext()));
        TextPreference textPreference2 = (TextPreference) findPreference(GalleryPreferences.PrefKeys.SLIDESHOW_INTERVAL);
        this.mSlideshowIntervalPref = textPreference2;
        textPreference2.setOnPreferenceClickListener(this);
        CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference(GalleryPreferences.PrefKeys.SLIDESHOW_LOOP);
        this.mSlideshowLoopPref = checkBoxPreference2;
        checkBoxPreference2.setChecked(GalleryPreferences.SlideShow.isSlideShowLoop());
        this.mSlideshowLoopPref.setOnPreferenceChangeListener(this);
        this.mOtherCategory = (PreferenceCategory) findPreference("others");
        this.mUpdateCheckPref = (DrawablePreference) findPreference("update_check");
        if (!RequestUpdateHelper.getInstance().isRequestUpdateEnable()) {
            this.mOtherCategory.removePreference(this.mUpdateCheckPref);
        } else {
            this.mUpdateCheckPref.setOnPreferenceClickListener(this);
            this.mUpdateCheckPref.setSummary(getString(R.string.update_check_title, "3.4.7.3".split("-")[0]));
            if (GalleryPreferences.RequestUpdatePref.isNeedHint() && GalleryPreferences.RequestUpdatePref.isFindNewVersion()) {
                this.mUpdateCheckPref.setTextColor(R.color.update_find_new_version_text_color);
                if (!GalleryPreferences.RequestUpdatePref.isConfirmNewVersion()) {
                    this.mUpdateCheckPref.setDrawable(R.drawable.red_dot_ic_large);
                }
                this.mUpdateCheckPref.setDrawablePadding(getResources().getDimensionPixelSize(R.dimen.update_red_dot_start_margin));
                this.mUpdateCheckPref.setText(R.string.update_new_version);
            }
        }
        TextPreference textPreference3 = (TextPreference) findPreference("problem_feedback");
        this.mProblemFeedbackPref = textPreference3;
        textPreference3.setOnPreferenceClickListener(this);
        this.mPrivacyPolicyPref = (TextPreference) findPreference("privacy_policy");
        this.mPrivacyEntrancePref = (TextPreference) findPreference("privacy_entrance");
        if (BaseBuildUtil.isInternational()) {
            this.mOtherCategory.removePreference(this.mPrivacyEntrancePref);
            this.mPrivacyPolicyPref.setOnPreferenceClickListener(this);
        } else {
            this.mPrivacyEntrancePref.setOnPreferenceClickListener(this);
            this.mOtherCategory.removePreference(this.mPrivacyPolicyPref);
        }
        this.mSendCategory = (PreferenceCategory) findPreference("send");
        this.mHeifConvertPref = (CheckBoxPreference) findPreference("heif_convert");
        this.mSecurityShareSettingsPref = (TextPreference) findPreference("security_share_settings");
        boolean z2 = SecurityShareHelper.isSupportMiui12(getContext()) && (SecurityShareHelper.IS_SECURITYCENTER_SECURITY_SHARE_AVAILABLE.get(null).booleanValue() || SecurityShareHelper.IS_ZMAN_SECURITY_SHARE_AVAILABLE.get(null).booleanValue());
        boolean isUnsupportedMediaType = true ^ IncompatibleMediaType.isUnsupportedMediaType("image/heif");
        if (isUnsupportedMediaType) {
            this.mHeifConvertPref.setChecked(GalleryPreferences.IncompatibleMedia.isIncompatibleMediaAutoConvert());
            this.mHeifConvertPref.setOnPreferenceChangeListener(this);
        } else {
            this.mSendCategory.removePreference(this.mHeifConvertPref);
            GalleryPreferences.IncompatibleMedia.setIncompatibleMediaAutoConvert(false);
        }
        if (z2) {
            this.mSecurityShareSettingsPref.setOnPreferenceClickListener(this);
        } else {
            this.mSendCategory.removePreference(this.mSecurityShareSettingsPref);
        }
        if (!z2 && !isUnsupportedMediaType) {
            this.mPreferenceRoot.removePreference(this.mSendCategory);
        }
        refreshSlideshowIntervalPrefValue();
    }

    public final void showSyncSettingDialog() {
        ConfirmDialog.showConfirmDialog(getFragmentManager(), getString(R.string.backup_sync_dialog_title), getString(R.string.backup_sync_dialog_tips), getString(R.string.cancel), getString(R.string.backup_sync_dialog_confirm), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.ui.GallerySettingsFragment.2
            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onCancel(DialogFragment dialogFragment) {
            }

            {
                GallerySettingsFragment.this = this;
            }

            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onConfirm(DialogFragment dialogFragment) {
                Intent intent = new Intent("android.settings.ACCOUNT_LIST");
                intent.setPackage("com.android.settings");
                DefaultLogger.d("GallerySettingsFragment", "go to sync settings");
                GallerySettingsFragment.this.startActivity(intent);
                TrackController.trackClick("403.22.1.1.11335", AutoTracking.getRef(), "open");
            }
        });
    }

    public final void addDebugPreferencesX() {
        addPreferencesFromResource(R.xml.gallery_debug_preferences);
        if (findPreference("debug") != null) {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("print_log");
            this.mPrintLog = checkBoxPreference;
            checkBoxPreference.setChecked(BaseGalleryPreferences.Debug.isPrintLog());
            this.mPrintLog.setOnPreferenceChangeListener(this);
            TextPreference textPreference = (TextPreference) findPreference("debug_gallery");
            this.mDebug = textPreference;
            textPreference.setOnPreferenceClickListener(this);
            TextPreference textPreference2 = (TextPreference) findPreference("correct_photo_time");
            this.mCorrectTime = textPreference2;
            textPreference2.setOnPreferenceClickListener(this);
            TextPreference textPreference3 = (TextPreference) findPreference("delete_dup_medias");
            this.mDeleteDupMedias = textPreference3;
            textPreference3.setOnPreferenceClickListener(this);
            TextPreference textPreference4 = (TextPreference) findPreference("thumbnail_build_error");
            this.mThumbnailBuildError = textPreference4;
            textPreference4.setOnPreferenceClickListener(this);
            TextPreference textPreference5 = (TextPreference) findPreference("dump_dbinfo");
            this.mDumpDBInfo = textPreference5;
            textPreference5.setOnPreferenceClickListener(this);
            CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference(BaseGalleryPreferences.PrefKeys.HOOK_HEIF_DECODER);
            this.mHookHeifDecoder = checkBoxPreference2;
            checkBoxPreference2.setPersistent(false);
            this.mHookHeifDecoder.setChecked(BaseGalleryPreferences.Debug.isHookHeifDecoder());
            this.mHookHeifDecoder.setOnPreferenceChangeListener(this);
            TextPreference textPreference6 = (TextPreference) findPreference("storage_test");
            this.mStorageTest = textPreference6;
            textPreference6.setOnPreferenceClickListener(this);
            ((Vibrator) GalleryApp.sGetAndroidContext().getSystemService("vibrator")).vibrate(100L);
        }
    }

    @Override // com.miui.gallery.app.fragment.MiuiPreferenceFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        getChildFragmentManager().addFragmentOnAttachListener(new FragmentOnAttachListener() { // from class: com.miui.gallery.ui.GallerySettingsFragment$$ExternalSyntheticLambda0
            @Override // androidx.fragment.app.FragmentOnAttachListener
            public final void onAttachFragment(FragmentManager fragmentManager, Fragment fragment) {
                GallerySettingsFragment.m1455$r8$lambda$w1mb7PgquoipTfVbkhbpuZOKxQ(GallerySettingsFragment.this, fragmentManager, fragment);
            }
        });
        super.onCreate(bundle);
    }

    public /* synthetic */ void lambda$onCreate$0(FragmentManager fragmentManager, Fragment fragment) {
        if (fragment instanceof SlideshowIntervalDialogFragment) {
            ((SlideshowIntervalDialogFragment) fragment).setCompleteListener(new SlideshowIntervalDialogFragment.CompleteListener() { // from class: com.miui.gallery.ui.GallerySettingsFragment$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.ui.SlideshowIntervalDialogFragment.CompleteListener
                public final void onComplete() {
                    GallerySettingsFragment.m1454$r8$lambda$T0nYgfHC1BQPNGJ90pnJP_6dY(GallerySettingsFragment.this);
                }
            });
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(getActivity(), "settings");
        boolean z = true;
        if (this.mObserverHandle == null) {
            sSyncStatusObserverWrapper.setSyncStatusObserverWorker(this.mSyncStatusObserver);
            this.mObserverHandle = ContentResolver.addStatusChangeListener(1, sSyncStatusObserverWrapper);
        }
        Account account = AccountCache.getAccount();
        DefaultLogger.w("GallerySettingsFragment", "masterSyncAutomatically: %s, xiaomiGlobalSyncAutomatically %s, gallerySyncAutomatically %s", Boolean.valueOf(ContentResolver.getMasterSyncAutomatically()), Boolean.valueOf(SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()), Boolean.valueOf(account == null ? false : ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")));
        if (account == null || !SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() || !ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")) {
            z = false;
        }
        CloudBackupStatusPreference cloudBackupStatusPreference = this.mAutoBackupPref;
        if (cloudBackupStatusPreference != null) {
            cloudBackupStatusPreference.setAutoBackupStatus(z);
        }
        CheckBoxPreference checkBoxPreference = this.mImageSelectionPref;
        if (checkBoxPreference != null) {
            checkBoxPreference.setChecked(GalleryPreferences.Assistant.isImageSelectionFunctionOn());
        }
        CheckBoxPreference checkBoxPreference2 = this.mStoryFunctionPref;
        if (checkBoxPreference2 != null) {
            checkBoxPreference2.setChecked(GalleryPreferences.Assistant.isStoryFunctionOn());
        }
        CheckBoxPreference checkBoxPreference3 = this.mCreativityPref;
        if (checkBoxPreference3 != null) {
            checkBoxPreference3.setChecked(GalleryPreferences.Assistant.isCreativityFunctionOn());
        }
        CheckBoxPreference checkBoxPreference4 = this.mSlideshowLoopPref;
        if (checkBoxPreference4 != null) {
            checkBoxPreference4.setChecked(GalleryPreferences.SlideShow.isSlideShowLoop());
        }
        CheckBoxPreference checkBoxPreference5 = this.mHeifConvertPref;
        if (checkBoxPreference5 != null) {
            checkBoxPreference5.setChecked(GalleryPreferences.IncompatibleMedia.isIncompatibleMediaAutoConvert());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        sSyncStatusObserverWrapper.setSyncStatusObserverWorker(null);
        Object obj = this.mObserverHandle;
        if (obj != null) {
            ContentResolver.removeStatusChangeListener(obj);
            this.mObserverHandle = null;
        }
        SamplingStatHelper.recordPageEnd(getActivity(), "settings");
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        getActivity().finish();
        return true;
    }

    public final void showSlideshowIntervalDialog() {
        new SlideshowIntervalDialogFragment().showAllowingStateLoss(getChildFragmentManager(), "SlideshowIntervalDialogFragment");
    }

    public final void refreshSlideshowIntervalPrefValue() {
        int slideShowInterval = GalleryPreferences.SlideShow.getSlideShowInterval();
        this.mSlideshowIntervalPref.setText(getResources().getQuantityString(R.plurals.slideshow_interval_value, slideShowInterval, Integer.valueOf(slideShowInterval)));
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        boolean needForceSplit = getActivity() instanceof FloatingWindowActivity ? ((FloatingWindowActivity) getActivity()).needForceSplit() : false;
        if (preference == this.mSlideshowIntervalPref) {
            TrackController.trackClick("403.22.0.1.11345", AutoTracking.getRef());
            showSlideshowIntervalDialog();
            return true;
        } else if (preference == this.mSecurityShareSettingsPref) {
            TrackController.trackClick("403.22.0.1.11347", AutoTracking.getRef());
            SecurityShareHelper.startShareSettingsActivity(getContext(), getActivity().getIntent());
            return true;
        } else if (preference == this.mProblemFeedbackPref) {
            TrackController.trackClick("403.22.0.1.11352", AutoTracking.getRef());
            IntentUtil.gotoProblemFeedback(getActivity());
            return true;
        } else if (TextUtils.equals(preference.getKey(), "debug_gallery")) {
            DebugUtil.generateDebugLog(getActivity());
            return true;
        } else if (TextUtils.equals(preference.getKey(), "correct_photo_time")) {
            DebugUtil.correctPhotoTime(getActivity());
            return true;
        } else if (TextUtils.equals(preference.getKey(), "delete_dup_medias")) {
            DebugUtil.deleteDupMedias(getActivity());
            return true;
        } else if (TextUtils.equals(preference.getKey(), "thumbnail_build_error")) {
            DebugUtil.clearThumbnailErrorLog(getActivity());
            return true;
        } else if (TextUtils.equals(preference.getKey(), "dump_dbinfo")) {
            DebugUtil.dumpDatabaseInfo();
            return true;
        } else if (preference == this.mPrivacyPolicyPref) {
            TrackController.trackClick("403.22.0.1.11351", AutoTracking.getRef());
            IntentUtil.gotoPrivacyPolicy(getActivity());
            return true;
        } else if (preference == this.mShowHiddenAlbumPref) {
            TrackController.trackClick("403.22.0.1.11344", AutoTracking.getRef());
            IntentUtil.gotoHiddenAlbumPage(getActivity(), getActivity().getIntent().getBooleanExtra("use_dialog", false), needForceSplit);
            return true;
        } else if (preference == this.mStorageTest) {
            IntentUtil.gotoStorageTestPage(getActivity());
            return true;
        } else if (preference == this.mPrivacyEntrancePref) {
            TrackController.trackClick("403.22.2.1.16229");
            IntentUtil.gotoPrivacySettingsActivity(getActivity(), getActivity().getIntent().getBooleanExtra("use_dialog", false), needForceSplit);
            return true;
        } else if (preference != this.mUpdateCheckPref || System.currentTimeMillis() - this.mLastRequestUpdateTime < 3000) {
            return true;
        } else {
            RequestUpdateHelper requestUpdateHelper = RequestUpdateHelper.getInstance();
            requestUpdateHelper.registerOnRequestUpdateFinishListener(this.mOnRequestUpdateFinishListener);
            requestUpdateHelper.requestUpdate(getActivity(), 2);
            this.mLastRequestUpdateTime = System.currentTimeMillis();
            TrackController.trackClick("403.52.0.1.16067", AutoTracking.getRef(), GalleryPreferences.RequestUpdatePref.isFindNewVersion() ? "yes" : "no");
            return true;
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        String str = "open";
        if (preference == this.mImageSelectionPref) {
            setImageSelectionFunctionState(((Boolean) obj).booleanValue());
            HashMap hashMap = new HashMap(1);
            hashMap.put("to", obj.toString());
            SamplingStatHelper.recordCountEvent("settings", "settings_change_image_selection_switch", hashMap);
            String ref = AutoTracking.getRef();
            if (!((Boolean) obj).booleanValue()) {
                str = "close";
            }
            TrackController.trackClick("403.22.0.1.11341", ref, str);
        } else if (preference == this.mStoryFunctionPref) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            setStoryFunctionState(booleanValue);
            HashMap hashMap2 = new HashMap(1);
            hashMap2.put("to", obj.toString());
            SamplingStatHelper.recordCountEvent("settings", "settings_change_story_generation_switch", hashMap2);
            String ref2 = AutoTracking.getRef();
            if (!((Boolean) obj).booleanValue()) {
                str = "close";
            }
            TrackController.trackClick("403.22.0.1.11342", ref2, str);
            updateWidgetStatus(booleanValue);
        } else if (TextUtils.equals(preference.getKey(), "print_log")) {
            Boolean bool = (Boolean) obj;
            BaseGalleryPreferences.Debug.printLog(bool.booleanValue());
            TimingTracing.setEnabled(bool.booleanValue());
            GalleryLoggerFactory.updateLogLevel();
        } else if (TextUtils.equals(preference.getKey(), "heif_convert")) {
            Boolean bool2 = (Boolean) obj;
            GalleryPreferences.IncompatibleMedia.setIncompatibleMediaAutoConvert(bool2.booleanValue());
            String ref3 = AutoTracking.getRef();
            if (!bool2.booleanValue()) {
                str = "close";
            }
            TrackController.trackClick("403.22.0.1.11348", ref3, str);
        } else if (TextUtils.equals(preference.getKey(), GalleryPreferences.PrefKeys.SLIDESHOW_LOOP)) {
            Boolean bool3 = (Boolean) obj;
            GalleryPreferences.SlideShow.setSlideShowLoop(bool3.booleanValue());
            String ref4 = AutoTracking.getRef();
            if (!bool3.booleanValue()) {
                str = "close";
            }
            TrackController.trackClick("403.22.0.1.11346", ref4, str);
        } else if (TextUtils.equals(preference.getKey(), BaseGalleryPreferences.PrefKeys.HOOK_HEIF_DECODER)) {
            BaseGalleryPreferences.Debug.hookHeifDecoder(((Boolean) obj).booleanValue());
        } else if (preference == this.mCreativityPref) {
            boolean booleanValue2 = ((Boolean) obj).booleanValue();
            GalleryPreferences.Assistant.setCreativityFunctionState(booleanValue2);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("com.miui.gallery.action.SWITCH_CREATIVITY_FUNCTION"));
            String ref5 = AutoTracking.getRef();
            if (!booleanValue2) {
                str = "close";
            }
            TrackController.trackClick("403.22.0.1.16610", ref5, str);
        }
        return true;
    }

    public final void updateWidgetStatus(boolean z) {
        Intent intent = new Intent();
        intent.putExtra("from_story_function_change", true);
        intent.putExtra("is_story_function_on", z);
        intent.setAction("miui.appwidget.action.APPWIDGET_UPDATE");
        FragmentActivity activity = getActivity();
        if (activity != null) {
            intent.setPackage(activity.getPackageName());
            activity.sendBroadcast(intent);
            DefaultLogger.d("GallerySettingsFragment", "---log---GallerySettingsFragment send ACTION_MIUI_APPWIDGET_UPDATE finish isStoryFunctionOn: %b>", Boolean.valueOf(z));
        }
    }

    public final boolean setImageSelectionFunctionState(boolean z) {
        GalleryPreferences.Assistant.setImageSelectionFunctionState(z);
        DefaultLogger.d("GallerySettingsFragment", "The image selection fuction state:" + z);
        return true;
    }

    public final boolean setStoryFunctionState(boolean z) {
        GalleryPreferences.Assistant.setStoryFunctionState(z);
        DefaultLogger.d("GallerySettingsFragment", "The story fuction state:" + z);
        if (!z) {
            PendingTaskManager.getInstance().cancelAll(2);
            PendingTaskManager.getInstance().cancelAll(10);
            return true;
        }
        Account account = AccountCache.getAccount();
        if (account == null) {
            return true;
        }
        GalleryCloudSyncTagUtils.setCardSyncTag(account, 0L);
        GalleryCloudSyncTagUtils.setCardSyncInfo(account, "");
        SyncUtil.requestSync(StaticContext.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(68L).build());
        SamplingStatHelper.recordCountEvent("assistant", "request_sync_card_function");
        return true;
    }

    public void onKeyDown(int i, KeyEvent keyEvent) {
        if (this.mDebugRecorder.onKeyDown(i)) {
            addDebugPreferencesX();
        }
    }

    @Override // com.miui.gallery.ui.CloudBackupStatusPreference.CloudButtonClickListener
    public void onBackupClick() {
        if (this.mAutoBackupPref.getAutoBackupStatus()) {
            boolean z = false;
            boolean booleanExtra = getActivity().getIntent().getBooleanExtra("use_dialog", false);
            Intent intent = new Intent(getActivity(), BackupSettingsActivity.class);
            intent.putExtra("use_dialog", booleanExtra);
            if (getActivity() instanceof FloatingWindowActivity) {
                z = ((FloatingWindowActivity) getActivity()).needForceSplit();
            }
            if (z) {
                SplitUtils.addMiuiFlags(intent, 16);
            }
            startActivity(intent);
            return;
        }
        if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
            showSyncSettingDialog();
        }
        MergeDataDialogFragment.newInstance(true).showAllowingStateLoss(getChildFragmentManager(), "MergeDataDialogFragment");
    }

    @Override // com.miui.gallery.ui.CloudBackupStatusPreference.CloudButtonClickListener
    public void onSpaceClick() {
        TrackController.trackClick("403.22.0.1.18942");
        IntentUtil.gotoMiCloudVipPage(getActivity(), getActivity().getIntent(), new Pair("source", IntentUtil.getMiCloudVipPageSource("gallery_button_setting")));
    }

    /* loaded from: classes2.dex */
    public static class DebugRecorder {
        public Boolean[] code;
        public Queue<Boolean> mQueue;

        public DebugRecorder() {
            Boolean bool = Boolean.TRUE;
            Boolean bool2 = Boolean.FALSE;
            Boolean[] boolArr = {bool, bool, bool2, bool2, bool, bool2};
            this.code = boolArr;
            this.mQueue = EvictingQueue.create(boolArr.length);
        }

        public boolean onKeyDown(int i) {
            if (i == 24) {
                this.mQueue.offer(Boolean.TRUE);
            } else if (i == 25) {
                this.mQueue.offer(Boolean.FALSE);
            }
            return check();
        }

        public final boolean check() {
            boolean z = false;
            int i = 0;
            for (Boolean bool : this.mQueue) {
                int i2 = i + 1;
                z = bool.equals(this.code[i]);
                if (!z) {
                    return false;
                }
                i = i2;
            }
            return z && i == this.code.length;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        CloudBackupStatusPreference cloudBackupStatusPreference = this.mAutoBackupPref;
        if (cloudBackupStatusPreference != null) {
            cloudBackupStatusPreference.cancel();
        }
        this.mSyncStatusObserver = null;
        this.mMainHandler.removeCallbacksAndMessages(null);
        RequestUpdateHelper.getInstance().unregisterOnRequestUpdateFinishListener(this.mOnRequestUpdateFinishListener);
        RequestUpdateHelper.getInstance().release();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 60) {
            DefaultLogger.d("GallerySettingsFragment", "go to sync settings" + i2);
            if (i2 != -1) {
                return;
            }
            startActivityForResult(new Intent(getActivity(), BackupSettingsLoginActivity.class), 61);
            return;
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.app.fragment.MiuiPreferenceFragment, miuix.preference.PreferenceFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
    }

    public final void updateConfiguration(Configuration configuration) {
        CloudBackupStatusPreference cloudBackupStatusPreference = this.mAutoBackupPref;
        if (cloudBackupStatusPreference != null) {
            cloudBackupStatusPreference.setUseDialog(isUseDialog());
            this.mAutoBackupPref.setForceSplit(isForceSplit());
            this.mAutoBackupPref.onConfigChanged(configuration, isUseDialog(), isForceSplit());
        }
    }

    public final boolean isUseDialog() {
        FragmentActivity activity = getActivity();
        if (activity == null || !(activity instanceof FloatingWindowActivity)) {
            return false;
        }
        return ((FloatingWindowActivity) activity).useDialog();
    }

    public final boolean isForceSplit() {
        FragmentActivity activity = getActivity();
        if (activity == null || !(activity instanceof FloatingWindowActivity)) {
            return false;
        }
        return ((FloatingWindowActivity) activity).needForceSplit();
    }

    /* loaded from: classes2.dex */
    public class OnRequestUpdateFinishListener extends OnRequestUpdateFinishAdapter {
        public OnRequestUpdateFinishListener() {
            GallerySettingsFragment.this = r1;
        }

        @Override // com.miui.gallery.util.RequestUpdateHelper.OnRequestUpdateFinishListener
        public void onDialogCreate(int i, int i2, UpdateDialogFragment updateDialogFragment) {
            updateDialogFragment.addOnDialogButtonClickListener(GallerySettingsFragment.this.mOnRequestUpdateFinishListener);
            if (!GallerySettingsFragment.this.isResumed() || i2 != 2) {
                return;
            }
            updateDialogFragment.show(GallerySettingsFragment.this.getChildFragmentManager(), "GallerySettingsFragment");
            GalleryPreferences.RequestUpdatePref.setIsConfirmNewVersion(true);
            TrackController.trackExpose("403.64.0.1.16064", AutoTracking.getRef());
        }

        @Override // com.miui.gallery.ui.UpdateDialogFragment.OnDialogButtonClickListener
        public void onDelayClick(boolean z, int i) {
            GalleryPreferences.RequestUpdatePref.setIsFindNewVersion(!z);
            if (z) {
                GallerySettingsFragment.this.mUpdateCheckPref.setText("");
                TrackController.trackClick("403.64.0.1.16066", AutoTracking.getRef(), "yes");
                return;
            }
            if (!GalleryPreferences.RequestUpdatePref.isNeedHint()) {
                GallerySettingsFragment.this.mUpdateCheckPref.setText("");
            } else {
                GallerySettingsFragment.this.mUpdateCheckPref.setTextColor(R.color.update_find_new_version_text_color);
                GallerySettingsFragment.this.mUpdateCheckPref.setDrawableDisplay(false);
                GallerySettingsFragment.this.mUpdateCheckPref.setText(R.string.update_new_version);
            }
            TrackController.trackClick("403.64.0.1.16066", AutoTracking.getRef(), "no");
        }

        @Override // com.miui.gallery.ui.UpdateDialogFragment.OnDialogButtonClickListener
        public void onUpdateClick(int i) {
            if (!GalleryPreferences.RequestUpdatePref.isNeedHint()) {
                GallerySettingsFragment.this.mUpdateCheckPref.setText("");
            } else {
                GallerySettingsFragment.this.mUpdateCheckPref.setDrawableDisplay(false);
            }
            TrackController.trackClick("403.64.0.1.16065");
        }

        @Override // com.miui.gallery.util.RequestUpdateHelper.OnRequestUpdateFinishListener
        public void onFailure(int i) {
            if (i == 1) {
                ToastUtils.makeText(GallerySettingsFragment.this.getActivity(), (int) R.string.update_latest_version);
            } else {
                ToastUtils.makeText(GallerySettingsFragment.this.getActivity(), (int) R.string.update_network_error);
            }
        }
    }
}
