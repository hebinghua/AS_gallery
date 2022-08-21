package com.miui.gallery.activity.facebaby;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import com.bumptech.glide.RequestManager;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.MiuiPreferenceFragment;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.provider.deprecated.ThumbnailInfo;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.glide.GlideRequestManagerHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Calendar;
import miuix.preference.TextPreference;

/* loaded from: classes.dex */
public abstract class BabyAlbumSettingsFragment extends MiuiPreferenceFragment {
    public String mAlbumName;
    public TextPreference mAutoupdatePre;
    public Long mBabyAlbumLocalId;
    public BabyInfo mBabyInfoFrom;
    public TextView mBabyNameTv;
    public Preference mBabyNicknamePre;
    public Preference mBabySexPre;
    public String mBirthday;
    public int mBirthdayDay;
    public int mBirthdayMonth;
    public int mBirthdayYear;
    public Preference mChooseDate;
    public Bitmap mCoverFaceBitmap;
    public String mCoverFacePath;
    public ImageView mFace;
    public Long mFaceAlbumLocalId;
    public boolean mHaveSaveBabyInfo;
    public boolean mIsOtherShareAlbum;
    public String mNickName;
    public String mPeopleId;
    public Preference mRelationPre;
    public String mRelationText;
    public ThumbnailInfo mThumbnailInfo;
    public String mSex = "male";
    public String mRelation = "father";
    public Boolean mIsAutoupdate = Boolean.TRUE;
    public Handler mHandler = new Handler();

    public static /* synthetic */ boolean $r8$lambda$hK6Jo6unZhFnvs0Qad8Nl_Y4Zzc(BabyAlbumSettingsFragment babyAlbumSettingsFragment, Preference preference) {
        return babyAlbumSettingsFragment.lambda$onViewCreated$0(preference);
    }

    public abstract int getPreferenceResourceId();

    public abstract void justSaveInfo2DbByJson();

    public abstract void saveBabyInfo();

    public Bundle getExtras() {
        return getActivity().getIntent().getExtras();
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(getPreferenceResourceId(), str);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mBabyNicknamePre = findPreference("baby_name");
        this.mChooseDate = findPreference("baby_birthday");
        this.mBabySexPre = findPreference("baby_sexy");
        this.mRelationPre = findPreference("owner_relation");
        this.mFace = (ImageView) getView().findViewById(R.id.go_to_setting);
        this.mBabyNameTv = (TextView) getView().findViewById(R.id.album_name);
        TextPreference textPreference = (TextPreference) findPreference("auto_update_face");
        this.mAutoupdatePre = textPreference;
        textPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment$$ExternalSyntheticLambda0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public final boolean onPreferenceClick(Preference preference) {
                return BabyAlbumSettingsFragment.$r8$lambda$hK6Jo6unZhFnvs0Qad8Nl_Y4Zzc(BabyAlbumSettingsFragment.this, preference);
            }
        });
        Bundle extras = getExtras();
        Long valueOf = Long.valueOf(extras.getLong("babyAlbumLocalId", -1L));
        this.mBabyAlbumLocalId = valueOf;
        if (valueOf.longValue() != -1) {
            BabyInfo babyInfo = (BabyInfo) extras.getParcelable("baby_info");
            this.mBabyInfoFrom = babyInfo;
            flatBabyInfo(babyInfo);
            this.mThumbnailInfo = new ThumbnailInfo(this.mBabyAlbumLocalId.longValue(), this.mIsOtherShareAlbum, extras.getString("thumbnail_info_of_baby"));
        }
        this.mCoverFacePath = extras.getString("face_path");
        this.mCoverFaceBitmap = (Bitmap) extras.getParcelable("face_bitmap");
        String string = extras.getString("album_name");
        this.mAlbumName = string;
        if (TextUtils.isEmpty(string)) {
            this.mAlbumName = getString(R.string.face_album_not_named);
        }
        this.mBabyNameTv.setText(this.mAlbumName);
        Bitmap bitmap = this.mCoverFaceBitmap;
        if (bitmap != null) {
            this.mFace.setImageBitmap(bitmap);
        }
        if (!TextUtils.isEmpty(this.mCoverFacePath)) {
            bindFaceCover();
        }
    }

    public /* synthetic */ boolean lambda$onViewCreated$0(Preference preference) {
        startActivityForResult(getIntentToAutoUploadPage(), 0);
        return false;
    }

    public final void bindFaceCover() {
        RequestManager safeGet = GlideRequestManagerHelper.safeGet(this.mFace);
        if (safeGet != null) {
            safeGet.mo985asBitmap().mo972placeholder((int) R.drawable.people_face_default).mo962load(GalleryModel.of(this.mCoverFacePath)).into(this.mFace);
        } else {
            DefaultLogger.w("BabyAlbumSettingActivityBase", "bindHeadFacePic failed, maybe caused by page destroy");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        this.mHaveSaveBabyInfo = false;
        super.onResume();
        setPreferencesValue();
    }

    public void setPreferencesValue() {
        this.mAutoupdatePre.setText(getString(this.mIsAutoupdate.booleanValue() ? R.string.auto_update_on : R.string.auto_update_off));
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        justSaveInfo2DbByJson();
    }

    public final void flatBabyInfo(BabyInfo babyInfo) {
        if (babyInfo != null) {
            this.mNickName = babyInfo.mNickName;
            String str = babyInfo.mBirthday;
            this.mBirthday = str;
            getBirthYearMonthDay(str);
            this.mSex = babyInfo.mSex;
            this.mRelation = babyInfo.mRelation;
            this.mRelationText = babyInfo.mRelationText;
            this.mIsAutoupdate = Boolean.valueOf(babyInfo.mAutoupdate);
            this.mPeopleId = babyInfo.mPeopleId;
            return;
        }
        this.mIsAutoupdate = Boolean.FALSE;
    }

    public static String combine2Birthday(int i, int i2, int i3) {
        return i + "-" + i2 + "-" + i3;
    }

    public static int[] getCurrentYearMonthDay(long j) {
        Calendar acquire = GalleryDateUtils.acquire();
        acquire.setTimeInMillis(j);
        int[] iArr = {acquire.get(1), acquire.get(2) + 1, acquire.get(5)};
        GalleryDateUtils.release(acquire);
        return iArr;
    }

    public static boolean isValidDate(int i, int i2, int i3) {
        return i > 0 && i2 > 0 && i2 <= 12 && i3 > 0 && i3 < GalleryDateUtils.daysInMonth(GalleryDateUtils.isLeapYear(i), i2);
    }

    public void getBirthYearMonthDay(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String[] split = str.split("-");
        if (split.length != 3) {
            return;
        }
        this.mBirthdayYear = Integer.parseInt(split[0]);
        this.mBirthdayMonth = Integer.parseInt(split[1]);
        this.mBirthdayDay = Integer.parseInt(split[2]);
    }

    public boolean babyInfoChanged() {
        BabyInfo babyInfo = this.mBabyInfoFrom;
        return babyInfo == null || !TextUtils.equals(this.mNickName, babyInfo.mNickName) || !TextUtils.equals(this.mBirthday, babyInfo.mBirthday) || !TextUtils.equals(this.mSex, babyInfo.mSex) || !TextUtils.equals(this.mRelation, babyInfo.mRelation) || !TextUtils.equals(this.mRelationText, babyInfo.mRelationText) || this.mIsAutoupdate.booleanValue() != babyInfo.mAutoupdate || !TextUtils.equals(this.mPeopleId, babyInfo.mPeopleId);
    }

    public BabyInfo toBabyInfo(String str, int i) {
        return new BabyInfo(this.mNickName, this.mBirthday, this.mSex, this.mRelation, this.mRelationText, str, this.mIsAutoupdate.booleanValue(), i);
    }

    public Intent getIntentToAutoUploadPage() {
        Intent intent = new Intent(getContext(), BabyAlbumAutoUpdateSettingActivity.class);
        intent.putExtra("baby-info", toBabyInfo(this.mPeopleId, 0));
        return intent;
    }
}
