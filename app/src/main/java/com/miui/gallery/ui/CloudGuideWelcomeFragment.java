package com.miui.gallery.ui;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.activity.BackupSettingsLoginActivity;
import com.miui.gallery.activity.CloudGuideAutoBackupActivity;
import com.miui.gallery.activity.FloatingWindowActivity;
import com.miui.gallery.activity.GallerySettingsActivity;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.SplitUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.Locale;
import miui.cloud.util.SyncAutoSettingUtil;
import miuix.appcompat.app.AppCompatActivity;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class CloudGuideWelcomeFragment extends BasePreferenceFragment {
    public GalleryIntent$CloudGuideSource mSource;

    public static /* synthetic */ void $r8$lambda$7bTLhXdcYPIwKVzq5QUv1RoFV5I(CloudGuideWelcomeFragment cloudGuideWelcomeFragment, View view) {
        cloudGuideWelcomeFragment.lambda$onViewCreated$0(view);
    }

    @Override // com.miui.gallery.app.fragment.MiuiPreferenceFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("cloud_guide_source")) {
            this.mSource = (GalleryIntent$CloudGuideSource) intent.getSerializableExtra("cloud_guide_source");
        } else {
            this.mSource = GalleryIntent$CloudGuideSource.TOPBAR;
        }
        if (this.mSource == GalleryIntent$CloudGuideSource.TOPBAR) {
            GalleryPreferences.CloudGuide.setCloudGuideTopbarClicked();
        }
        ((AppCompatActivity) getActivity()).getAppCompatActionBar().setTitle(R.string.cloud_guide_welcome_title);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        getPreferenceManager().setSharedPreferencesName("com.miui.gallery_preferences_new");
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView textView = (TextView) view.findViewById(R.id.welcome_text);
        TextView textView2 = (TextView) view.findViewById(R.id.introduce);
        Button button = (Button) view.findViewById(R.id.next);
        ((RecyclerView) view.findViewById(R.id.recycler_view)).setImportantForAccessibility(2);
        if (this.mSource == GalleryIntent$CloudGuideSource.AUTOBACKUP) {
            textView.setText(R.string.welcome_to_auto_backup);
            textView2.setText(R.string.auto_backup_introduce);
        } else {
            textView.setText(R.string.welcome_to_micloud);
            textView2.setText(R.string.micloud_introduce);
        }
        button.setText(R.string.next);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.CloudGuideWelcomeFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                CloudGuideWelcomeFragment.$r8$lambda$7bTLhXdcYPIwKVzq5QUv1RoFV5I(CloudGuideWelcomeFragment.this, view2);
            }
        });
    }

    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        boolean z = false;
        boolean booleanExtra = getActivity().getIntent().getBooleanExtra("use_dialog", false);
        Intent intent = new Intent(getActivity(), CloudGuideAutoBackupActivity.class);
        intent.putExtra("autobackup_album_id", getActivity().getIntent().getLongExtra("autobackup_album_id", -1L));
        intent.putExtra("cloud_guide_source", this.mSource);
        intent.putExtra("use_dialog", booleanExtra);
        if (getActivity() instanceof FloatingWindowActivity) {
            z = ((FloatingWindowActivity) getActivity()).needForceSplit();
        }
        if (z) {
            SplitUtils.addMiuiFlags(intent, 16);
        }
        startActivityForResult(intent, 32);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 32) {
            super.onActivityResult(i, i2, intent);
        } else if (i2 != -1) {
            if (i2 != 1) {
                return;
            }
            getActivity().finish();
        } else {
            Account account = AccountCache.getAccount();
            if (account != null) {
                if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
                    ContentResolver.setMasterSyncAutomatically(true);
                    DefaultLogger.d("CloudGuideWelcomeFragment", "open master sync auto");
                }
                if (!ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")) {
                    SyncUtil.setSyncAutomatically(getActivity(), true);
                    DefaultLogger.d("CloudGuideWelcomeFragment", "open gallery sync auto");
                }
            }
            GalleryIntent$CloudGuideSource galleryIntent$CloudGuideSource = this.mSource;
            if (galleryIntent$CloudGuideSource != GalleryIntent$CloudGuideSource.AUTOBACKUP && galleryIntent$CloudGuideSource != GalleryIntent$CloudGuideSource.SECRET && galleryIntent$CloudGuideSource != GalleryIntent$CloudGuideSource.SHARE_INVITATION && galleryIntent$CloudGuideSource != GalleryIntent$CloudGuideSource.URL && galleryIntent$CloudGuideSource != GalleryIntent$CloudGuideSource.TRASH_BIN && galleryIntent$CloudGuideSource != GalleryIntent$CloudGuideSource.TOPBAR) {
                Intent intent2 = new Intent(getActivity(), HomePageActivity.class);
                intent2.addFlags(67108864);
                startActivity(intent2);
            } else if (galleryIntent$CloudGuideSource == GalleryIntent$CloudGuideSource.TOPBAR) {
                if (account == null) {
                    Intent intent3 = new Intent(getActivity(), HomePageActivity.class);
                    intent3.addFlags(67108864);
                    startActivity(intent3);
                } else {
                    Intent intent4 = new Intent(getActivity(), GallerySettingsActivity.class);
                    intent4.putExtra("show_login_settings", true);
                    intent4.putExtra("use_dialog", true);
                    boolean z = false;
                    if (getActivity() instanceof FloatingWindowActivity) {
                        z = ((FloatingWindowActivity) getActivity()).needForceSplit();
                    }
                    if (z) {
                        SplitUtils.addMiuiFlags(intent4, 16);
                    }
                    startActivity(intent4);
                    startActivity(new Intent(getActivity(), BackupSettingsLoginActivity.class));
                }
            }
            getActivity().setResult(-1);
            getActivity().finish();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        statLoginState(this.mSource, SyncUtil.existXiaomiAccount(getActivity()), SyncUtil.isGalleryCloudSyncable(getActivity()));
        super.onDestroy();
    }

    public final void statLoginState(GalleryIntent$CloudGuideSource galleryIntent$CloudGuideSource, boolean z, boolean z2) {
        String format = String.format(Locale.US, "guide_login_status_%s", galleryIntent$CloudGuideSource.name());
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Event.LOGIN, String.valueOf(z));
        hashMap.put("syncable", String.valueOf(z2));
        StatHelper.recordCountEvent("cloud_guide", format, hashMap);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(getActivity(), "cloud_guide_welcome");
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(getActivity(), "cloud_guide_welcome");
    }
}
