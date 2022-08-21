package com.miui.gallery.activity.facebaby;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.cloud.operation.create.CreateGroupItem;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.provider.deprecated.ThumbnailInfo;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.baby.FindFace2CreateBabyAlbum;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.DatePickerDialog;
import miuix.pickerwidget.widget.DatePicker;

/* loaded from: classes.dex */
public class OwnerBabyAlbumSettingsFragment extends BabyAlbumSettingsFragment implements Preference.OnPreferenceChangeListener {
    public boolean mBabyFaceChanged;
    public String mCoverFaceId;
    public DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment.3
        {
            OwnerBabyAlbumSettingsFragment.this = this;
        }

        @Override // miuix.appcompat.app.DatePickerDialog.OnDateSetListener
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment = OwnerBabyAlbumSettingsFragment.this;
            ownerBabyAlbumSettingsFragment.mBirthdayYear = i;
            int i4 = i2 + 1;
            ownerBabyAlbumSettingsFragment.mBirthdayMonth = i4;
            ownerBabyAlbumSettingsFragment.mBirthdayDay = i3;
            ownerBabyAlbumSettingsFragment.mBirthday = BabyAlbumSettingsFragment.combine2Birthday(i, i4, i3);
            OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment2 = OwnerBabyAlbumSettingsFragment.this;
            ownerBabyAlbumSettingsFragment2.mChooseDate.setSummary(ownerBabyAlbumSettingsFragment2.mBirthday);
        }
    };
    public boolean mNeedSaveBabyFace;

    public static /* synthetic */ void $r8$lambda$1rESzJcX0sZM7qZILiYvb104NSA(OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment, View view) {
        ownerBabyAlbumSettingsFragment.lambda$onViewCreated$1(view);
    }

    public static /* synthetic */ void $r8$lambda$e3iyKkaMNGe5UtBzPEBoKus8bPI(OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment, EditText editText, AlertDialog alertDialog, DialogInterface dialogInterface) {
        ownerBabyAlbumSettingsFragment.lambda$rename$2(editText, alertDialog, dialogInterface);
    }

    public static /* synthetic */ void $r8$lambda$efb3MzM23DQYiN8lSu9uMdX7q2Y(OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment, View view) {
        ownerBabyAlbumSettingsFragment.lambda$onViewCreated$0(view);
    }

    public static /* synthetic */ Object $r8$lambda$uon2HiuPEEXPuxYazYha2VnksWk(OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment, ThreadPool.JobContext jobContext) {
        return ownerBabyAlbumSettingsFragment.lambda$refreshBabyFace$3(jobContext);
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public int getPreferenceResourceId() {
        return R.xml.owner_baby_album_preferences;
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Bundle extras = getExtras();
        this.mFaceAlbumLocalId = Long.valueOf(extras.getLong("faceAlbumLocalId", -1L));
        if (extras.containsKey("faceAlbumServerId")) {
            this.mPeopleId = extras.getString("faceAlbumServerId");
        }
        this.mCoverFaceId = extras.getString("faceAlbumCoverFaceServerId");
        this.mBabyNicknamePre.setOnPreferenceChangeListener(this);
        this.mBabySexPre.setOnPreferenceChangeListener(this);
        this.mRelationPre.setOnPreferenceChangeListener(this);
        View findViewById = getView().findViewById(R.id.finish_fill_baby);
        if (createOrBrowse()) {
            findViewById.setVisibility(0);
            String string = extras.getString("birthday");
            this.mBirthday = string;
            getBirthYearMonthDay(string);
            String string2 = extras.getString("name");
            this.mNickName = string2;
            if (TextUtils.isEmpty(string2)) {
                this.mNickName = getString(R.string.face_album_not_named);
            }
            this.mBabyNameTv.setText(this.mNickName);
        }
        findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                OwnerBabyAlbumSettingsFragment.$r8$lambda$efb3MzM23DQYiN8lSu9uMdX7q2Y(OwnerBabyAlbumSettingsFragment.this, view2);
            }
        });
        this.mFace.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                OwnerBabyAlbumSettingsFragment.$r8$lambda$1rESzJcX0sZM7qZILiYvb104NSA(OwnerBabyAlbumSettingsFragment.this, view2);
            }
        });
        if (createOrBrowse()) {
            setCoverFaceAndBirthdayForCreateBaby();
        }
    }

    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        if (this.mNickName.equalsIgnoreCase(getString(R.string.face_album_not_named))) {
            ToastUtils.makeText(getContext(), getString(R.string.input_nickname_toast));
            return;
        }
        ThreadManager.getMiscPool().submit(new CreateBabyAlbumAndSaveBabyInfoJob(this.mNickName, toBabyInfo(this.mPeopleId, 0), new NormalPeopleFaceMediaSet(this.mFaceAlbumLocalId.longValue(), this.mPeopleId, this.mNickName), getActivity(), this.mCoverFaceId));
    }

    public /* synthetic */ void lambda$onViewCreated$1(View view) {
        IntentUtil.pickFace(getActivity(), getString(R.string.set_face_image), this.mPeopleId, String.valueOf(this.mFaceAlbumLocalId), null, 1, true);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        ((BabyAlbumSettingsFragment) this).mHandler.removeCallbacksAndMessages(null);
    }

    /* renamed from: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements ThreadPool.Job<Void> {
        /* renamed from: $r8$lambda$qF_DvSXZnt3rBPWZeN-GH6fi9jQ */
        public static /* synthetic */ void m481$r8$lambda$qF_DvSXZnt3rBPWZeNGH6fi9jQ(AnonymousClass1 anonymousClass1) {
            anonymousClass1.lambda$run$0();
        }

        public AnonymousClass1() {
            OwnerBabyAlbumSettingsFragment.this = r1;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            OwnerBabyAlbumSettingsFragment.this.loadAndSetBabyFace(jobContext);
            int[] currentYearMonthDay = BabyAlbumSettingsFragment.getCurrentYearMonthDay(FaceManager.queryTimeOfOldestImagesOfOnePerson(OwnerBabyAlbumSettingsFragment.this.mPeopleId));
            int i = currentYearMonthDay[0];
            int i2 = currentYearMonthDay[1];
            int i3 = currentYearMonthDay[2];
            OwnerBabyAlbumSettingsFragment.this.mBirthday = BabyAlbumSettingsFragment.combine2Birthday(i, i2, i3);
            OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment = OwnerBabyAlbumSettingsFragment.this;
            ownerBabyAlbumSettingsFragment.getBirthYearMonthDay(ownerBabyAlbumSettingsFragment.mBirthday);
            ((BabyAlbumSettingsFragment) OwnerBabyAlbumSettingsFragment.this).mHandler.post(new Runnable() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OwnerBabyAlbumSettingsFragment.AnonymousClass1.m481$r8$lambda$qF_DvSXZnt3rBPWZeNGH6fi9jQ(OwnerBabyAlbumSettingsFragment.AnonymousClass1.this);
                }
            });
            return null;
        }

        public /* synthetic */ void lambda$run$0() {
            OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment = OwnerBabyAlbumSettingsFragment.this;
            Preference preference = ownerBabyAlbumSettingsFragment.mChooseDate;
            if (preference != null) {
                preference.setSummary(ownerBabyAlbumSettingsFragment.mBirthday);
            }
        }
    }

    public void setCoverFaceAndBirthdayForCreateBaby() {
        ThreadManager.getMiscPool().submit(new AnonymousClass1());
    }

    public final void loadAndSetBabyFace(ThreadPool.JobContext jobContext) {
        Bitmap bitmap = this.mCoverFaceBitmap;
        if (bitmap == null) {
            final FaceRegionRectF[] faceRegionRectFArr = new FaceRegionRectF[1];
            final String queryCoverImageOfOnePerson = FaceManager.queryCoverImageOfOnePerson(this.mPeopleId, -1L, faceRegionRectFArr);
            if (queryCoverImageOfOnePerson == null) {
                return;
            }
            ((BabyAlbumSettingsFragment) this).mHandler.post(new Runnable() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment.2
                {
                    OwnerBabyAlbumSettingsFragment.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    Glide.with(OwnerBabyAlbumSettingsFragment.this.mFace).mo985asBitmap().mo962load(GalleryModel.of(queryCoverImageOfOnePerson)).mo946apply((BaseRequestOptions<?>) GlideOptions.peopleFaceOf(faceRegionRectFArr[0])).into(OwnerBabyAlbumSettingsFragment.this.mFace);
                }
            });
            return;
        }
        ImageView imageView = this.mFace;
        if (imageView == null) {
            return;
        }
        imageView.setImageBitmap(bitmap);
    }

    public final boolean createOrBrowse() {
        return this.mBabyAlbumLocalId.longValue() == -1;
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public void setPreferencesValue() {
        super.setPreferencesValue();
        this.mBabyNicknamePre.setSummary(this.mNickName);
        this.mChooseDate.setSummary(this.mBirthday);
        ((ListPreference) this.mBabySexPre).setValue(this.mSex);
        Preference preference = this.mBabySexPre;
        preference.setSummary(((ListPreference) preference).getEntry());
        ((ListPreference) this.mRelationPre).setValue(this.mRelation);
        String str = (String) ((ListPreference) this.mRelationPre).getEntry();
        this.mRelationText = str;
        this.mRelationPre.setSummary(str);
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        Preference preference2 = this.mBabySexPre;
        if (preference == preference2) {
            String str = (String) obj;
            this.mSex = str;
            ((ListPreference) preference2).setValue(str);
            Preference preference3 = this.mBabySexPre;
            preference3.setSummary(((ListPreference) preference3).getEntry());
            return true;
        }
        Preference preference4 = this.mRelationPre;
        if (preference != preference4) {
            return true;
        }
        String str2 = (String) obj;
        this.mRelation = str2;
        ((ListPreference) preference4).setValue(str2);
        String str3 = (String) ((ListPreference) this.mRelationPre).getEntry();
        this.mRelationText = str3;
        this.mRelationPre.setSummary(str3);
        return true;
    }

    @Override // miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    public boolean onPreferenceTreeClick(Preference preference) {
        int i;
        int i2;
        int i3;
        if ("baby_birthday".equals(preference.getKey())) {
            if (!BabyAlbumSettingsFragment.isValidDate(this.mBirthdayYear, this.mBirthdayMonth, this.mBirthdayDay)) {
                int[] currentYearMonthDay = BabyAlbumSettingsFragment.getCurrentYearMonthDay(System.currentTimeMillis());
                i = currentYearMonthDay[0];
                i2 = currentYearMonthDay[1];
                i3 = currentYearMonthDay[2];
            } else {
                i = this.mBirthdayYear;
                i2 = this.mBirthdayMonth;
                i3 = this.mBirthdayDay;
            }
            Context context = getContext();
            DatePickerDialog.OnDateSetListener onDateSetListener = this.mDateSetListener;
            new DatePickerDialog(context, onDateSetListener, i, i2 - 1, i3).show();
            return true;
        }
        if ("baby_name".equals(preference.getKey())) {
            rename(this.mNickName, getActivity());
        }
        return super.onPreferenceTreeClick(preference);
    }

    public void rename(String str, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater from = LayoutInflater.from(builder.getContext());
        builder.setTitle(R.string.input_baby_name);
        View inflate = from.inflate(R.layout.baby_name_input_dialog_view, (ViewGroup) null);
        final EditText editText = (EditText) inflate.findViewById(R.id.custom_dialog_content);
        editText.setText(str);
        editText.selectAll();
        builder.setView(inflate);
        builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment.4
            {
                OwnerBabyAlbumSettingsFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                String validateInputTextAndUpdateButtonState = OwnerBabyAlbumSettingsFragment.this.validateInputTextAndUpdateButtonState(editText.getText().toString(), (AlertDialog) dialogInterface);
                if (!TextUtils.isEmpty(validateInputTextAndUpdateButtonState)) {
                    ToastUtils.makeText(activity, validateInputTextAndUpdateButtonState);
                    return;
                }
                OwnerBabyAlbumSettingsFragment.this.mNickName = editText.getText().toString();
                OwnerBabyAlbumSettingsFragment ownerBabyAlbumSettingsFragment = OwnerBabyAlbumSettingsFragment.this;
                ownerBabyAlbumSettingsFragment.mBabyNicknamePre.setSummary(ownerBabyAlbumSettingsFragment.mNickName);
            }
        });
        builder.setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                OwnerBabyAlbumSettingsFragment.$r8$lambda$e3iyKkaMNGe5UtBzPEBoKus8bPI(OwnerBabyAlbumSettingsFragment.this, editText, create, dialogInterface);
            }
        });
        editText.addTextChangedListener(new TextWatcher() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment.5
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            {
                OwnerBabyAlbumSettingsFragment.this = this;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                String validateInputTextAndUpdateButtonState = OwnerBabyAlbumSettingsFragment.this.validateInputTextAndUpdateButtonState(editText.getText().toString(), create);
                if (!TextUtils.isEmpty(validateInputTextAndUpdateButtonState)) {
                    ToastUtils.makeText(activity, validateInputTextAndUpdateButtonState);
                }
            }
        });
        create.show();
        create.getWindow().setSoftInputMode(5);
    }

    public /* synthetic */ void lambda$rename$2(EditText editText, AlertDialog alertDialog, DialogInterface dialogInterface) {
        validateInputTextAndUpdateButtonState(editText.getText().toString(), alertDialog);
    }

    public final String validateInputTextAndUpdateButtonState(String str, AlertDialog alertDialog) {
        String checkFileNameValid = CreateGroupItem.checkFileNameValid(getContext(), str);
        alertDialog.getButton(-1).setEnabled(TextUtils.isEmpty(checkFileNameValid));
        return checkFileNameValid;
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public void saveBabyInfo() {
        if (!createOrBrowse()) {
            if (!babyInfoChanged() && !this.mBabyFaceChanged) {
                return;
            }
            BabyInfo babyInfo = toBabyInfo(this.mPeopleId, 1);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("baby-info", babyInfo);
            bundle.putString("name", this.mNickName);
            bundle.putLong("babyAlbumLocalId", this.mBabyAlbumLocalId.longValue());
            bundle.putString("baby-people-id", this.mPeopleId);
            bundle.putLong("faceAlbumLocalId", this.mFaceAlbumLocalId.longValue());
            ThumbnailInfo thumbnailInfo = this.mThumbnailInfo;
            if (thumbnailInfo != null) {
                bundle.putString("thumbnail_info_of_baby", thumbnailInfo.toString());
            }
            intent.putExtras(bundle);
            getActivity().setResult(12, intent);
        }
    }

    /* loaded from: classes.dex */
    public static class SaveBabyInfoJob implements ThreadPool.Job<Void> {
        public Long mAlbumLocalId;
        public BabyInfo mBabyInfo;
        public Long mFaceAlbumId;
        public String mPeopleId;
        public ThumbnailInfo mThumbnailInfo;

        public SaveBabyInfoJob(String str, BabyInfo babyInfo, Long l, Long l2, ThumbnailInfo thumbnailInfo) {
            this.mPeopleId = str;
            this.mBabyInfo = babyInfo;
            this.mFaceAlbumId = l;
            this.mAlbumLocalId = l2;
            this.mThumbnailInfo = thumbnailInfo;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            OwnerBabyAlbumSettingsFragment.saveInfo2Db(this.mBabyInfo, this.mPeopleId, this.mFaceAlbumId, String.valueOf(this.mAlbumLocalId), this.mThumbnailInfo);
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class CreateBabyAlbumAndSaveBabyInfoJob implements ThreadPool.Job<Void> {
        public WeakReference<Activity> mActivityRef;
        public BabyInfo mBabyInfo;
        public String mCoverFaceId;
        public NormalPeopleFaceMediaSet mFaceMediaSet;
        public String mNickName;

        public CreateBabyAlbumAndSaveBabyInfoJob(String str, BabyInfo babyInfo, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, Activity activity, String str2) {
            this.mNickName = str;
            this.mBabyInfo = babyInfo;
            this.mFaceMediaSet = normalPeopleFaceMediaSet;
            this.mActivityRef = new WeakReference<>(activity);
            this.mCoverFaceId = str2;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            Activity activity = this.mActivityRef.get();
            if (activity == null || activity.isDestroyed()) {
                return null;
            }
            FindFace2CreateBabyAlbum.createBabyAlbumAndSaveBabyInfo(this.mNickName, this.mBabyInfo, this.mFaceMediaSet, activity, this.mCoverFaceId);
            return null;
        }
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public void justSaveInfo2DbByJson() {
        if (this.mHaveSaveBabyInfo || createOrBrowse()) {
            return;
        }
        if (!babyInfoChanged() && !this.mNeedSaveBabyFace) {
            return;
        }
        this.mHaveSaveBabyInfo = true;
        this.mNeedSaveBabyFace = false;
        ThreadPool miscPool = ThreadManager.getMiscPool();
        String str = this.mPeopleId;
        miscPool.submit(new SaveBabyInfoJob(str, toBabyInfo(str, 1), this.mFaceAlbumLocalId, this.mBabyAlbumLocalId, this.mThumbnailInfo));
    }

    public static void saveInfo2Db(BabyInfo babyInfo, String str, Long l, String str2, ThumbnailInfo thumbnailInfo) {
        String str3 = "";
        if (thumbnailInfo == null) {
            ContentValues contentValues = new ContentValues();
            if (babyInfo != null) {
                str3 = babyInfo.toJSON();
            }
            contentValues.put("babyInfoJson", str3);
            contentValues.put("peopleId", str);
            GalleryUtils.safeUpdate(GalleryCloudUtils.ALBUM_URI, contentValues, "_id = ?", new String[]{str2});
        } else {
            String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(23);
            ContentValues contentValues2 = new ContentValues(3);
            contentValues2.put("thumbnailInfo", thumbnailInfo.toString());
            if (babyInfo != null) {
                str3 = babyInfo.toJSON();
            }
            contentValues2.put("babyInfoJson", str3);
            contentValues2.put("peopleId", str);
            if (GalleryUtils.safeExec(String.format("update %s set %s=%s, %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s=%s", "album", CallMethod.ARG_EXTRA_STRING, AlbumDataHelper.genUpdateAlbumExtraInfoSql(contentValues2, false), "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, j.c, str2))) {
                AlbumCacheManager.getInstance().update(Long.parseLong(str2), contentValues2);
            }
        }
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, (ContentObserver) null, false);
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 0) {
            if (i2 != -1 || intent == null) {
                return;
            }
            this.mIsAutoupdate = Boolean.valueOf(((BabyInfo) intent.getParcelableExtra("baby-info")).mAutoupdate);
        } else if (i != 31) {
            super.onActivityResult(i, i2, intent);
        } else if (i2 != -1 || intent == null) {
        } else {
            this.mCoverFaceId = intent.getStringExtra("picked_face_id");
            if (!createOrBrowse()) {
                if (this.mThumbnailInfo == null) {
                    this.mThumbnailInfo = new ThumbnailInfo(this.mBabyAlbumLocalId.longValue(), this.mIsOtherShareAlbum, null);
                }
                this.mThumbnailInfo.setFaceId(this.mCoverFaceId);
            }
            this.mBabyFaceChanged = true;
            this.mNeedSaveBabyFace = true;
            refreshBabyFace();
        }
    }

    public final void refreshBabyFace() {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return OwnerBabyAlbumSettingsFragment.$r8$lambda$uon2HiuPEEXPuxYazYha2VnksWk(OwnerBabyAlbumSettingsFragment.this, jobContext);
            }
        });
    }

    public /* synthetic */ Object lambda$refreshBabyFace$3(ThreadPool.JobContext jobContext) {
        loadBabyFace(jobContext);
        return null;
    }

    public final void loadBabyFace(ThreadPool.JobContext jobContext) {
        if (!TextUtils.isEmpty(this.mCoverFaceId)) {
            final FaceRegionRectF[] faceRegionRectFArr = new FaceRegionRectF[1];
            final String faceByFaceId = ThumbnailInfo.getFaceByFaceId(this.mCoverFaceId, faceRegionRectFArr);
            ((BabyAlbumSettingsFragment) this).mHandler.post(new Runnable() { // from class: com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment.6
                {
                    OwnerBabyAlbumSettingsFragment.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    Glide.with(OwnerBabyAlbumSettingsFragment.this.mFace).mo985asBitmap().mo962load(GalleryModel.of(faceByFaceId)).mo946apply((BaseRequestOptions<?>) GlideOptions.peopleFaceOf(faceRegionRectFArr[0])).into(OwnerBabyAlbumSettingsFragment.this.mFace);
                }
            });
        }
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public Intent getIntentToAutoUploadPage() {
        Intent intentToAutoUploadPage = super.getIntentToAutoUploadPage();
        intentToAutoUploadPage.putExtra("allow_to_reassociate", false);
        return intentToAutoUploadPage;
    }
}
