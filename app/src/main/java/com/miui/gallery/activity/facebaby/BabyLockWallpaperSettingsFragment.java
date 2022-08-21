package com.miui.gallery.activity.facebaby;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import androidx.preference.PreferenceGroup;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.fragment.MiuiPreferenceFragment;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.BabyLockWallpaperDataManager;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryPreferenceCategory;
import com.miui.keyguard.LockScreenHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class BabyLockWallpaperSettingsFragment extends MiuiPreferenceFragment {
    public PreferenceGroup mPreferenceGroup;
    public Map<String, Boolean> mNewStates = Maps.newHashMap();
    public Map<String, Boolean> mOldStates = Maps.newHashMap();
    public Set<String> mSelectedAlbums = Sets.newHashSet();

    public static /* synthetic */ void $r8$lambda$30G9bColHyGrP5tDiyiSehmlcqU(BabyLockWallpaperSettingsFragment babyLockWallpaperSettingsFragment, DialogInterface dialogInterface, int i) {
        babyLockWallpaperSettingsFragment.lambda$onExit$2(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$NW8gDbGfind7DOJTWAs63e6JCI0(BabyLockWallpaperSettingsFragment babyLockWallpaperSettingsFragment, View view) {
        babyLockWallpaperSettingsFragment.lambda$onViewCreated$0(view);
    }

    /* renamed from: $r8$lambda$OfyLcv-XZo_QIvEFXU19umf3OiU */
    public static /* synthetic */ void m480$r8$lambda$OfyLcvXZo_QIvEFXU19umf3OiU(BabyLockWallpaperSettingsFragment babyLockWallpaperSettingsFragment, String str, CompoundButton compoundButton, boolean z) {
        babyLockWallpaperSettingsFragment.lambda$refresh$4(str, compoundButton, z);
    }

    public static /* synthetic */ void $r8$lambda$iBpYiIR4Bg1b4GguHWXgkKgWEyM(BabyLockWallpaperSettingsFragment babyLockWallpaperSettingsFragment, DialogInterface dialogInterface, int i) {
        babyLockWallpaperSettingsFragment.lambda$onExit$3(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$mPW_qNBM7KU2OE5cdChX1l8fsx4(BabyLockWallpaperSettingsFragment babyLockWallpaperSettingsFragment, DialogInterface dialogInterface, int i) {
        babyLockWallpaperSettingsFragment.lambda$onExit$1(dialogInterface, i);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(R.xml.baby_lock_wallpaper_preferences, str);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        view.findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.facebaby.BabyLockWallpaperSettingsFragment$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                BabyLockWallpaperSettingsFragment.$r8$lambda$NW8gDbGfind7DOJTWAs63e6JCI0(BabyLockWallpaperSettingsFragment.this, view2);
            }
        });
        this.mPreferenceGroup = (GalleryPreferenceCategory) findPreference("baby_lock_wallpaper_preference");
        if (!isBabyLockWallpaperInUse()) {
            GalleryPreferences.BabyLock.cleanBabyLockWallpaperAlbumState();
        }
        if (getActivity().getIntent() != null) {
            String stringExtra = getActivity().getIntent().getStringExtra("album_checked_by_default");
            if (TextUtils.isEmpty(stringExtra)) {
                return;
            }
            boolean booleanExtra = getActivity().getIntent().getBooleanExtra("is_other_shared", false);
            long parseLong = Long.parseLong(stringExtra);
            boolean isBabyAlbumForLockWallpaper = GalleryPreferences.BabyLock.isBabyAlbumForLockWallpaper(parseLong, booleanExtra);
            DefaultLogger.i("faceface", "albumid;" + parseLong + " is share:" + booleanExtra);
            if (isBabyAlbumForLockWallpaper) {
                return;
            }
            DefaultLogger.i("faceface", "chect albumid;" + parseLong + " is share:" + booleanExtra);
            this.mNewStates.put(GalleryPreferences.BabyLock.createPathSuffix(parseLong, booleanExtra), Boolean.TRUE);
        }
    }

    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        boolean z = !this.mSelectedAlbums.isEmpty();
        saveState();
        if (z) {
            LockScreenHelper.setLockWallpaperProvider(getContext().getContentResolver(), "com.miui.gallery.cloud.baby.wallpaper_provider");
            BabyLockWallpaperDataManager.getInstance().refresh();
            TrackController.trackClick("403.42.2.1.11297", "403.42.2.1.11296");
        } else if (isBabyLockWallpaperInUse()) {
            LockScreenHelper.setLockWallpaperProvider(getContext().getContentResolver(), null);
            ToastUtils.makeText(getActivity(), (int) R.string.baby_lock_wallpaper_cancelled);
        }
        getActivity().finish();
    }

    public void onExit() {
        boolean z;
        DialogInterface.OnClickListener onClickListener;
        int i;
        int i2;
        Iterator<Map.Entry<String, Boolean>> it = this.mNewStates.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            Map.Entry<String, Boolean> next = it.next();
            if (next.getValue() != null && !next.getValue().equals(this.mOldStates.get(next.getKey()))) {
                z = true;
                break;
            }
        }
        if (!z) {
            getActivity().finish();
            return;
        }
        if (this.mSelectedAlbums.isEmpty()) {
            onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.activity.facebaby.BabyLockWallpaperSettingsFragment$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    BabyLockWallpaperSettingsFragment.$r8$lambda$mPW_qNBM7KU2OE5cdChX1l8fsx4(BabyLockWallpaperSettingsFragment.this, dialogInterface, i3);
                }
            };
            i = R.string.cancel_baby_lock_wallpaper_confirm_msg;
            i2 = 17039370;
        } else {
            onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.activity.facebaby.BabyLockWallpaperSettingsFragment$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    BabyLockWallpaperSettingsFragment.$r8$lambda$30G9bColHyGrP5tDiyiSehmlcqU(BabyLockWallpaperSettingsFragment.this, dialogInterface, i3);
                }
            };
            i = R.string.baby_lock_wallpaper_save_settings_msg;
            i2 = R.string.apply;
        }
        BabyLockWallpaperDataManager.getInstance().refresh();
        DialogUtil.showConfirmAlertWithCancel(getActivity(), onClickListener, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.activity.facebaby.BabyLockWallpaperSettingsFragment$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                BabyLockWallpaperSettingsFragment.$r8$lambda$iBpYiIR4Bg1b4GguHWXgkKgWEyM(BabyLockWallpaperSettingsFragment.this, dialogInterface, i3);
            }
        }, null, getString(i), getString(i2), 17039360);
    }

    public /* synthetic */ void lambda$onExit$1(DialogInterface dialogInterface, int i) {
        saveState();
        LockScreenHelper.setLockWallpaperProvider(getContext().getContentResolver(), null);
        ToastUtils.makeText(getActivity(), (int) R.string.baby_lock_wallpaper_cancelled);
        getActivity().finish();
    }

    public /* synthetic */ void lambda$onExit$2(DialogInterface dialogInterface, int i) {
        saveState();
        LockScreenHelper.setLockWallpaperProvider(getContext().getContentResolver(), "com.miui.gallery.cloud.baby.wallpaper_provider");
        BabyLockWallpaperDataManager.getInstance().refresh();
        getActivity().finish();
    }

    public /* synthetic */ void lambda$onExit$3(DialogInterface dialogInterface, int i) {
        getActivity().finish();
    }

    public final void saveState() {
        for (Map.Entry<String, Boolean> entry : this.mNewStates.entrySet()) {
            GalleryPreferences.BabyLock.setBabyAlbumForLockWallpaper(entry.getKey(), entry.getValue().booleanValue());
        }
    }

    public final void refresh() {
        boolean isBabyAlbumForLockWallpaper;
        ArrayList<BabyLockWallpaperDataManager.BabyAlbumInfo> allBabyAlbums = BabyLockWallpaperDataManager.getInstance().getAllBabyAlbums();
        this.mPreferenceGroup.removeAll();
        this.mSelectedAlbums.clear();
        if (allBabyAlbums != null) {
            for (BabyLockWallpaperDataManager.BabyAlbumInfo babyAlbumInfo : allBabyAlbums) {
                final String createPathSuffix = GalleryPreferences.BabyLock.createPathSuffix(babyAlbumInfo.localId, babyAlbumInfo.isOtherShared);
                if (this.mNewStates.containsKey(createPathSuffix)) {
                    isBabyAlbumForLockWallpaper = this.mNewStates.get(createPathSuffix).booleanValue();
                } else {
                    isBabyAlbumForLockWallpaper = GalleryPreferences.BabyLock.isBabyAlbumForLockWallpaper(createPathSuffix);
                    this.mOldStates.put(createPathSuffix, Boolean.valueOf(isBabyAlbumForLockWallpaper));
                }
                if (isBabyAlbumForLockWallpaper) {
                    this.mSelectedAlbums.add(createPathSuffix);
                }
                this.mPreferenceGroup.addPreference(new BabyAlbumPreference(getContext(), babyAlbumInfo, new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.activity.facebaby.BabyLockWallpaperSettingsFragment$$ExternalSyntheticLambda4
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        BabyLockWallpaperSettingsFragment.m480$r8$lambda$OfyLcvXZo_QIvEFXU19umf3OiU(BabyLockWallpaperSettingsFragment.this, createPathSuffix, compoundButton, z);
                    }
                }, isBabyAlbumForLockWallpaper));
            }
        }
    }

    public /* synthetic */ void lambda$refresh$4(String str, CompoundButton compoundButton, boolean z) {
        this.mNewStates.put(str, Boolean.valueOf(z));
        if (z) {
            this.mSelectedAlbums.add(str);
        } else {
            this.mSelectedAlbums.remove(str);
        }
    }

    public final boolean isBabyLockWallpaperInUse() {
        return "com.miui.gallery.cloud.baby.wallpaper_provider".equals(LockScreenHelper.getLockWallpaperProvider(getContext().getContentResolver()));
    }
}
