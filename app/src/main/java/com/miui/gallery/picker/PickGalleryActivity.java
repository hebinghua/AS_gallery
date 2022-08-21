package com.miui.gallery.picker;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageStartupHelper;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.picker.uri.DownloadCancelDialog;
import com.miui.gallery.picker.uri.DownloadConfirmDialog;
import com.miui.gallery.picker.uri.OriginUrlRequestor;
import com.miui.gallery.picker.uri.PrepareProgressDialog;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.ImageSelectionTipFragment;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.ui.StorageGuideFragment;
import com.miui.gallery.ui.album.picker.PickAlbumPageFragment;
import com.miui.gallery.ui.share.ConvertItem;
import com.miui.gallery.ui.share.DownloadItem;
import com.miui.gallery.ui.share.PrepareTask;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes2.dex */
public class PickGalleryActivity extends PickerActivity implements HomePageStartupHelper.Attacher {
    public boolean isStartedActivity;
    public int mCloseType;
    public boolean mDisablePendingTransition;
    public PrepareProgressDialog mDownloadProgressDialog;
    public HomePageStartupHelper mHomePageStartHelper;
    public String[] mIDs;
    public ArrayList<OriginUrlRequestor.OriginUrlRequestTask> mOriginRequestPendings;
    public PrepareProgressDialog mOriginRequestProgressDialog;
    public OriginUrlRequestor mOriginUrlRequestor;
    public ParseTask mParseTask;
    public Intent mPickIntent;
    public HashMap<String, ArrayList<PickerPrepareItem>> mPreparePendingsMap;
    public PrepareTask<PickerPrepareItem> mPrepareTask;
    public Uri[] mResults;
    public String[] mSha1s;
    public int mCurrentPagePosition = 0;
    public int mHasStoragePermission = -1;
    public NetworkConsider.OnConfirmed mOnConfirmed = new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.picker.PickGalleryActivity.2
        @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
        public void onConfirmed(boolean z, boolean z2) {
            if (!z) {
                PickGalleryActivity.this.cancelPreparing();
                if (PickGalleryActivity.this.getPicker() == null || PickGalleryActivity.this.getPicker().getMode() != Picker.Mode.SINGLE) {
                    return;
                }
                PickGalleryActivity.this.getPicker().clear();
            } else if (BaseNetworkUtils.isNetworkConnected()) {
                PickGalleryActivity.this.startPrepare();
            } else {
                ToastUtils.makeText(PickGalleryActivity.this, (int) R.string.picker_no_network_message);
                PickGalleryActivity.this.showConfirmDialog(true);
            }
        }
    };
    public DialogInterface.OnClickListener mDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.picker.PickGalleryActivity.3
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (BaseNetworkUtils.isNetworkConnected()) {
                PickGalleryActivity.this.startPrepare();
                return;
            }
            ToastUtils.makeText(PickGalleryActivity.this, (int) R.string.picker_no_network_message);
            PickGalleryActivity.this.showConfirmDialog(true);
        }
    };
    public DialogInterface.OnClickListener mCancelDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.picker.PickGalleryActivity.4
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            PickGalleryActivity.this.cancelPreparing();
            if (PickGalleryActivity.this.getPicker() == null || PickGalleryActivity.this.getPicker().getMode() != Picker.Mode.SINGLE) {
                return;
            }
            PickGalleryActivity.this.getPicker().clear();
        }
    };
    public DialogInterface.OnClickListener mContinueDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.picker.PickGalleryActivity.5
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (PickGalleryActivity.this.mDownloadProgressDialog != null) {
                PickGalleryActivity.this.mDownloadProgressDialog.showAllowingStateLoss(PickGalleryActivity.this.getSupportFragmentManager(), "prepare_progress_dialog");
            }
        }
    };
    public DialogInterface.OnCancelListener mCancelConfirmListener = new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.picker.PickGalleryActivity.6
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            int remainSize = PickGalleryActivity.this.mPrepareTask == null ? 0 : PickGalleryActivity.this.mPrepareTask.getRemainSize();
            if (remainSize > 0) {
                DownloadCancelDialog downloadCancelDialog = new DownloadCancelDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("remaining_count", remainSize);
                downloadCancelDialog.setArguments(bundle);
                downloadCancelDialog.showAllowingStateLoss(PickGalleryActivity.this.getSupportFragmentManager(), "cancel_dialog");
            }
        }
    };

    public final String getPageName(int i) {
        if (i == 0) {
            return "home";
        }
        if (i != 1) {
            return null;
        }
        return "album";
    }

    @Override // com.miui.gallery.picker.PickerCompatActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mPicker == null) {
            return;
        }
        restoreData();
        loadMediaCacheIfNeed();
        sendStatistics();
        if (bundle != null) {
            Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag("confirm_dialog");
            Fragment findFragmentByTag2 = getSupportFragmentManager().findFragmentByTag("prepare_progress_dialog");
            Fragment findFragmentByTag3 = getSupportFragmentManager().findFragmentByTag("cancel_dialog");
            Fragment findFragmentByTag4 = getSupportFragmentManager().findFragmentByTag("FragmentManager");
            if (findFragmentByTag != null || findFragmentByTag2 != null || findFragmentByTag3 != null || findFragmentByTag4 != null) {
                FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
                if (findFragmentByTag != null) {
                    beginTransaction.remove(findFragmentByTag);
                }
                if (findFragmentByTag2 != null) {
                    beginTransaction.remove(findFragmentByTag2);
                }
                if (findFragmentByTag3 != null) {
                    beginTransaction.remove(findFragmentByTag3);
                }
                if (findFragmentByTag3 != null) {
                    beginTransaction.remove(findFragmentByTag4);
                }
                beginTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        Intent intent = (Intent) getIntent().getParcelableExtra("pick_intent");
        this.mPickIntent = intent;
        if (!checkUriPermissionFlags(intent)) {
            finish();
            return;
        }
        this.mCloseType = getIntent().getIntExtra("pick_close_type", 1);
        this.mDisablePendingTransition = getIntent().getBooleanExtra("disable_pending_transition", false);
        ImageSelectionTipFragment.showImageSelectionTipDialogIfNecessary(this);
    }

    public final boolean checkUriPermissionFlags(Intent intent) {
        if (intent != null) {
            int flags = intent.getFlags();
            if ((flags & 1) == 0 && (flags & 2) == 0) {
                return true;
            }
            DefaultLogger.w("PickGalleryActivity", "intent has no uri permission flags!");
            return false;
        }
        return true;
    }

    public final void checkPermissionAndInitTab() {
        int i = 1 ^ (PermissionUtils.canAccessStorage(this, true) ? 1 : 0);
        DefaultLogger.i("PickGalleryActivity", "checkPermissionAndInitTab, newpermission = " + i + " currentPermission = " + this.mHasStoragePermission);
        if (this.mHasStoragePermission != i) {
            this.mHasStoragePermission = i;
            this.mActionBar.setFragmentViewPagerMode(this);
            this.mActionBar.removeAllFragmentTab();
            if (this.mHasStoragePermission == 0) {
                setupTabFragments();
                return;
            }
            ActionBar actionBar = this.mActionBar;
            actionBar.addFragmentTab("StorageGuideFragment", actionBar.newTab().setText(R.string.home_page_label), StorageGuideFragment.class, null, false);
            ActionBar actionBar2 = this.mActionBar;
            actionBar2.addFragmentTab("StorageGuideFragment", actionBar2.newTab().setText(R.string.album_page_label), StorageGuideFragment.class, null, false);
        }
    }

    public final void restoreData() {
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra("pick_sha1");
        if (BaseMiscUtil.isValid(stringArrayListExtra)) {
            Iterator<String> it = stringArrayListExtra.iterator();
            while (it.hasNext()) {
                this.mPicker.pick(it.next());
            }
        }
    }

    public final boolean isNeedLoadMediaCache() {
        Picker picker = this.mPicker;
        return (picker == null || picker.getMediaType() == null || this.mPicker.getMediaType() != Picker.MediaType.ALL) ? false : true;
    }

    public final void loadMediaCacheIfNeed() {
        if (!isNeedLoadMediaCache()) {
            return;
        }
        HomePageStartupHelper homePageStartupHelper = new HomePageStartupHelper(this);
        this.mHomePageStartHelper = homePageStartupHelper;
        homePageStartupHelper.attach(this);
        this.mHomePageStartHelper.onActivityCreate();
    }

    @Override // com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        super.onPermissionsChecked(permissionArr, iArr, zArr);
        DefaultLogger.i("PickGalleryActivity", "onPermissionsChecked:" + Arrays.toString(permissionArr));
        if (isCrossUserPick()) {
            dispatchPermissionChecked(permissionArr, iArr);
        }
    }

    public final void dispatchPermissionChecked(Permission[] permissionArr, int[] iArr) {
        PickHomePageFragment pickHomePageFragment;
        if (permissionArr == null || permissionArr.length == 0 || (pickHomePageFragment = (PickHomePageFragment) getSupportFragmentManager().findFragmentByTag("PickHomePageFragment")) == null || !contains(permissionArr, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            return;
        }
        pickHomePageFragment.onPermissionsChecked();
    }

    public final boolean contains(Permission[] permissionArr, String str) {
        for (Permission permission : permissionArr) {
            if (str.equalsIgnoreCase(permission.mName)) {
                return true;
            }
        }
        return false;
    }

    public final void sendStatistics() {
        String callingPackage = getCallingPackage();
        if (TextUtils.isEmpty(callingPackage)) {
            callingPackage = "unknown";
        }
        HashMap hashMap = new HashMap();
        hashMap.put("calling_package", callingPackage);
        SamplingStatHelper.recordCountEvent("picker", "open_picker", hashMap);
        SamplingStatHelper.recordStringPropertyEvent("best_image_count", String.valueOf(MediaFeatureCacheManager.getInstance().getBestImageCount(false)));
        HashMap hashMap2 = new HashMap();
        hashMap2.put("tip", "403.25.0.1.13685");
        hashMap2.put(nexExportFormat.TAG_FORMAT_PATH, callingPackage);
        if (!TextUtils.isEmpty(AutoTracking.getRef())) {
            hashMap2.put("ref_tip", AutoTracking.getRef());
        }
        AutoTracking.trackView(hashMap2);
    }

    @Override // com.miui.gallery.picker.PickerCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(this, getPageName(this.mCurrentPagePosition));
        PrepareTask<PickerPrepareItem> prepareTask = this.mPrepareTask;
        if (prepareTask != null) {
            prepareTask.pause();
        }
    }

    @Override // com.miui.gallery.picker.PickerCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        checkPermissionAndInitTab();
        SamplingStatHelper.recordPageStart(this, getPageName(this.mCurrentPagePosition));
        PrepareTask<PickerPrepareItem> prepareTask = this.mPrepareTask;
        if (prepareTask != null) {
            prepareTask.resume();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        cancelPreparing();
        HashMap<String, ArrayList<PickerPrepareItem>> hashMap = this.mPreparePendingsMap;
        if (hashMap != null) {
            hashMap.clear();
        }
        if (this.mResults != null) {
            this.mResults = null;
        }
        if (this.mSha1s != null) {
            this.mSha1s = null;
        }
        if (this.mIDs != null) {
            this.mIDs = null;
        }
        OriginUrlRequestor originUrlRequestor = this.mOriginUrlRequestor;
        if (originUrlRequestor != null) {
            originUrlRequestor.cancel();
        }
        ArrayList<OriginUrlRequestor.OriginUrlRequestTask> arrayList = this.mOriginRequestPendings;
        if (arrayList != null) {
            arrayList.clear();
        }
    }

    @Override // com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        Intent intent;
        super.onConfigurationChanged(configuration);
        if (!BuildUtil.isFoldingDevice() || (intent = this.mPickIntent) == null || (configuration.screenLayout & 15) != 3 || !this.isStartedActivity || !intent.getBooleanExtra("is_from_widget_click", false)) {
            return;
        }
        finish();
    }

    @Override // com.miui.gallery.picker.PickerActivity
    public void onDone(int i) {
        if (this.mParseTask == null) {
            ParseTask parseTask = new ParseTask(getPicker());
            this.mParseTask = parseTask;
            parseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return;
        }
        DefaultLogger.w("PickGalleryActivity", "parse task is running, skip");
    }

    public final void setupTabFragments() {
        ActionBar actionBar = this.mActionBar;
        actionBar.addFragmentTab("PickHomePageFragment", actionBar.newTab().setText(R.string.home_page_label), PickHomePageFragment.class, null, false);
        ActionBar actionBar2 = this.mActionBar;
        actionBar2.addFragmentTab("PickAlbumPageFragment", actionBar2.newTab().setText(R.string.album_page_label), PickAlbumPageFragment.class, null, false);
        this.mActionBar.addOnFragmentViewPagerChangeListener(new ActionBar.FragmentViewPagerChangeListener() { // from class: com.miui.gallery.picker.PickGalleryActivity.1
            @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
            public void onPageScrolled(int i, float f, boolean z, boolean z2) {
            }

            @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
            public void onPageSelected(int i) {
                DefaultLogger.d("PickGalleryActivity", "onPageSelected %d", Integer.valueOf(i));
                if (PickGalleryActivity.this.mCurrentPagePosition != i) {
                    PickGalleryActivity pickGalleryActivity = PickGalleryActivity.this;
                    SamplingStatHelper.recordPageEnd(pickGalleryActivity, pickGalleryActivity.getPageName(pickGalleryActivity.mCurrentPagePosition));
                    PickGalleryActivity pickGalleryActivity2 = PickGalleryActivity.this;
                    SamplingStatHelper.recordPageStart(pickGalleryActivity2, pickGalleryActivity2.getPageName(i));
                }
                PickGalleryActivity.this.mCurrentPagePosition = i;
                if (PickGalleryActivity.this.mCurrentPagePosition == 1) {
                    TrackController.trackExpose("403.25.0.1.11311", AutoTracking.getRef());
                }
            }
        });
    }

    @Override // com.miui.gallery.activity.HomePageStartupHelper.Attacher
    public HomePageStartupHelper getStartupHelper() {
        return this.mHomePageStartHelper;
    }

    /* loaded from: classes2.dex */
    public class ParseTask extends AsyncTask<Void, Void, Cursor> {
        public boolean isMayCalledFromInner;
        public String mPickSha1Results;

        public ParseTask(Picker picker) {
            this.mPickSha1Results = TextUtils.join(",", picker);
        }

        @Override // android.os.AsyncTask
        public Cursor doInBackground(Void... voidArr) {
            long currentTimeMillis = System.currentTimeMillis();
            Cursor query = PickGalleryActivity.this.getContentResolver().query(UriUtil.appendGroupBy(GalleryContract.Media.URI_PICKER, j.c, null), PickerActivity.PICKABLE_PROJECTION, String.format("_id IN (%s) AND (localGroupId!=-1000) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", this.mPickSha1Results), null, String.format("INSTR('%s', _id)", this.mPickSha1Results));
            if (query != null) {
                query.getCount();
            }
            DefaultLogger.d("PickGalleryActivity", "ParseTask#doInBackground costs: %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            this.isMayCalledFromInner = isMayCalledFromInner();
            return query;
        }

        public final boolean isMayCalledFromInner() {
            String callingPackage = PickGalleryActivity.this.getCallingPackage();
            if (TextUtils.isEmpty(callingPackage)) {
                if (PickGalleryActivity.this.mPickIntent == null) {
                    return false;
                }
                if (!TextUtils.isEmpty(PickGalleryActivity.this.mPickIntent.getPackage())) {
                    return TextUtils.equals(PickGalleryActivity.this.getPackageName(), PickGalleryActivity.this.mPickIntent.getPackage());
                }
                if (PickGalleryActivity.this.mPickIntent.getComponent() != null) {
                    return TextUtils.equals(PickGalleryActivity.this.getPackageName(), PickGalleryActivity.this.mPickIntent.getComponent().getPackageName());
                }
                if (PickGalleryActivity.this.mPickIntent.getData() == null) {
                    return false;
                }
                return TextUtils.equals("gallery.i.mi.com", PickGalleryActivity.this.mPickIntent.getData().getAuthority());
            }
            return TextUtils.equals(PickGalleryActivity.this.getPackageName(), callingPackage);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Cursor cursor) {
            String quantityString;
            PickGalleryActivity.this.mParseTask = null;
            try {
                if (cursor == null) {
                    throw new IllegalStateException("return a null cursor");
                }
                try {
                    DefaultLogger.d("PickGalleryActivity", "Picker capacity: %d, result %d items", Integer.valueOf(PickGalleryActivity.this.getPicker().capacity()), Integer.valueOf(cursor.getCount()));
                    if (PickGalleryActivity.this.getPicker().getResultType() == Picker.ResultType.ID) {
                        Intent resultIntent = PickGalleryActivity.this.getResultIntent();
                        ArrayList arrayList = new ArrayList();
                        int i = 0;
                        while (cursor.moveToNext()) {
                            long j = cursor.getLong(0);
                            arrayList.add(Long.valueOf(j));
                            if (MediaFeatureCacheManager.getInstance().isBestImage(j, false, false, null)) {
                                i++;
                            }
                        }
                        PickGalleryActivity.this.statBestImageCount(cursor.getCount(), i);
                        resultIntent.putExtra("pick-result-data", arrayList);
                        PickGalleryActivity.this.doCompleteOperation(resultIntent);
                    } else {
                        PickGalleryActivity.this.prepareResult(cursor, this.isMayCalledFromInner);
                        DefaultLogger.d("PickGalleryActivity", "picked file: %d, pending: %d", Integer.valueOf(PickGalleryActivity.this.mResults.length), Integer.valueOf(PickGalleryActivity.this.getAllPreparePendings().size()));
                        if (PickGalleryActivity.this.getPreparePendingToDownload().isEmpty()) {
                            PickGalleryActivity.this.parseResult();
                        } else if (!SyncUtil.existXiaomiAccount(PickGalleryActivity.this)) {
                            ToastUtils.makeText(PickGalleryActivity.this, PickGalleryActivity.this.getResources().getQuantityString(R.plurals.picker_skip_unavailable_images, PickGalleryActivity.this.getPreparePendingToDownload().size(), Integer.valueOf(PickGalleryActivity.this.getPreparePendingToDownload().size())));
                            PickGalleryActivity.this.parseResult();
                        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
                            PickGalleryActivity.this.showConfirmDialog(false);
                        } else if (BaseNetworkUtils.isNetworkConnected()) {
                            if (PickGalleryActivity.this.mResults.length != 0) {
                                quantityString = PickGalleryActivity.this.getResources().getQuantityString(R.plurals.picker_file_will_download, PickGalleryActivity.this.getPreparePendingToDownload().size(), Integer.valueOf(PickGalleryActivity.this.getPreparePendingToDownload().size()));
                            } else {
                                quantityString = PickGalleryActivity.this.getResources().getQuantityString(R.plurals.picker_all_image_will_download, PickGalleryActivity.this.getPreparePendingToDownload().size());
                            }
                            ToastUtils.makeText(PickGalleryActivity.this, quantityString);
                            PickGalleryActivity.this.startPrepare();
                        } else {
                            PickGalleryActivity.this.showConfirmDialog(true);
                        }
                    }
                } catch (Exception e) {
                    DefaultLogger.e("PickGalleryActivity", "Parse failed %s", e);
                }
            } finally {
                cursor.close();
            }
        }
    }

    public final HashMap<String, ArrayList<PickerPrepareItem>> initPreparePendingsMap() {
        HashMap<String, ArrayList<PickerPrepareItem>> hashMap = this.mPreparePendingsMap;
        if (hashMap != null) {
            return hashMap;
        }
        HashMap<String, ArrayList<PickerPrepareItem>> hashMap2 = new HashMap<>(2);
        this.mPreparePendingsMap = hashMap2;
        hashMap2.put("prepare_pending_to_download", new ArrayList<>());
        this.mPreparePendingsMap.put("prepare_pending_others", new ArrayList<>());
        return this.mPreparePendingsMap;
    }

    public final ArrayList<PickerPrepareItem> getPreparePendingToDownload() {
        initPreparePendingsMap();
        return this.mPreparePendingsMap.get("prepare_pending_to_download");
    }

    public final ArrayList<PickerPrepareItem> getPreparePendingOthers() {
        initPreparePendingsMap();
        return this.mPreparePendingsMap.get("prepare_pending_others");
    }

    public final ArrayList<PickerPrepareItem> getAllPreparePendings() {
        initPreparePendingsMap();
        ArrayList<PickerPrepareItem> arrayList = new ArrayList<>();
        arrayList.addAll(getPreparePendingToDownload());
        arrayList.addAll(getPreparePendingOthers());
        return arrayList;
    }

    public final void prepareResult(Cursor cursor, boolean z) {
        DownloadType downloadType;
        DefaultLogger.d("PickGalleryActivity", "called from inner: %s", Boolean.valueOf(z));
        int count = cursor.getCount();
        this.mResults = new Uri[count];
        this.mSha1s = new String[count];
        this.mIDs = new String[count];
        this.mPreparePendingsMap = null;
        this.mPreparePendingsMap = initPreparePendingsMap();
        this.mOriginRequestPendings = new ArrayList<>();
        int i = 0;
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            int i2 = 1;
            this.mSha1s[position] = cursor.getString(1);
            this.mIDs[position] = String.valueOf(cursor.getLong(0));
            String string = cursor.getString(4);
            long j = cursor.getLong(0);
            if (MediaFeatureCacheManager.getInstance().isBestImage(j, false, false, null)) {
                i++;
            }
            if (!TextUtils.isEmpty(string)) {
                File file = new File(string);
                if (file.exists()) {
                    this.mResults[position] = Uri.fromFile(file);
                    if (!z && IncompatibleMediaType.isAutoConvertMediaType(cursor.getString(10)) && GalleryPreferences.IncompatibleMedia.isIncompatibleMediaAutoConvert()) {
                        getPreparePendingOthers().add(new PickerPrepareItem.Builder().setSrcUri(Uri.fromFile(file)).setFileTitle(cursor.getString(9)).setSize(cursor.getInt(6)).setPosition(position).setFlags(4).build());
                    }
                }
            }
            Picker.ImageType imageType = getPicker().getImageType();
            if (imageType == Picker.ImageType.ORIGIN) {
                if (BaseNetworkUtils.isActiveNetworkMetered()) {
                    downloadType = DownloadType.ORIGIN_FORCE;
                } else {
                    downloadType = DownloadType.ORIGIN;
                }
                PickerPrepareItem.Builder position2 = new PickerPrepareItem.Builder().setSrcUri(CloudUriAdapter.getDownloadUri(j)).setDownloadType(downloadType).setSize(cursor.getInt(6)).setPosition(position);
                if (!z && IncompatibleMediaType.isAutoConvertMediaType(cursor.getString(10)) && GalleryPreferences.IncompatibleMedia.isIncompatibleMediaAutoConvert()) {
                    position2.setFileTitle(cursor.getString(9));
                    i2 = 5;
                }
                position2.setFlags(i2);
                getPreparePendingToDownload().add(position2.build());
            } else {
                if (imageType == Picker.ImageType.ORIGIN_OR_DOWNLOAD_INFO) {
                    this.mOriginRequestPendings.add(new OriginUrlRequestor.OriginUrlRequestTask(position, CloudUriAdapter.getDownloadUri(j), cursor.getInt(7), cursor.getInt(8), cursor.getInt(5)));
                }
                String string2 = cursor.getString(3);
                if (!TextUtils.isEmpty(string2)) {
                    File file2 = new File(string2);
                    if (file2.exists()) {
                        this.mResults[position] = Uri.fromFile(file2);
                    }
                }
                getPreparePendingToDownload().add(new PickerPrepareItem.Builder().setSrcUri(CloudUriAdapter.getDownloadUri(j)).setDownloadType(DownloadType.THUMBNAIL).setPosition(position).setFlags(1).build());
            }
        }
        statBestImageCount(count, i);
    }

    public final void statBestImageCount(int i, int i2) {
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        hashMap.put("best_image_count", Integer.valueOf(i2));
        SamplingStatHelper.recordCountEvent("picker", "pick_result", hashMap);
    }

    public final void parseResult() {
        Uri[] uriArr;
        ClipData clipData;
        if (this.mResults.length == 0) {
            finish();
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        int i = 0;
        while (true) {
            uriArr = this.mResults;
            if (i >= uriArr.length) {
                break;
            }
            if (uriArr[i] != null) {
                arrayList.add(uriArr[i]);
                arrayList2.add(this.mSha1s[i]);
                arrayList3.add(this.mIDs[i]);
            }
            i++;
        }
        if (uriArr.length != arrayList.size()) {
            this.mResults = (Uri[]) arrayList.toArray(new Uri[arrayList.size()]);
            this.mSha1s = (String[]) arrayList2.toArray(new String[arrayList2.size()]);
            this.mIDs = (String[]) arrayList3.toArray(new String[arrayList3.size()]);
            if (this.mResults.length == 0) {
                finish();
                return;
            }
        }
        Picker.ResultType resultType = getPicker().getResultType();
        DefaultLogger.d("PickGalleryActivity", "parse raw results[%s] for %s", this.mResults, resultType);
        if (resultType == Picker.ResultType.OPEN_URI) {
            int i2 = 0;
            while (true) {
                Uri[] uriArr2 = this.mResults;
                if (i2 >= uriArr2.length) {
                    break;
                }
                uriArr2[i2] = GalleryOpenProvider.translateToContent(uriArr2[i2].getPath());
                i2++;
            }
        }
        if (getPicker().getImageType() == Picker.ImageType.ORIGIN_OR_DOWNLOAD_INFO) {
            OriginUrlRequestor originUrlRequestor = new OriginUrlRequestor(this.mOriginRequestPendings, new OriginUrlRequestProgressListener());
            this.mOriginUrlRequestor = originUrlRequestor;
            originUrlRequestor.start(this.mResults, this.mSha1s);
            return;
        }
        Intent resultIntent = getResultIntent();
        Picker.MediaType mediaType = getPicker().getMediaType();
        if (getPicker().getMode() == Picker.Mode.MULTIPLE) {
            if (mediaType == Picker.MediaType.IMAGE) {
                clipData = new ClipData("data", new String[]{"image/*", "text/uri-list"}, new ClipData.Item(this.mResults[0]));
            } else if (mediaType == Picker.MediaType.ALL) {
                clipData = new ClipData("data", new String[]{"image/*", "video/*", "text/uri-list"}, new ClipData.Item(this.mResults[0]));
            } else {
                clipData = new ClipData("data", new String[]{"video/*", "text/uri-list"}, new ClipData.Item(this.mResults[0]));
            }
            for (int i3 = 1; i3 < this.mResults.length; i3++) {
                clipData.addItem(new ClipData.Item(this.mResults[i3]));
            }
            resultIntent.setClipData(clipData);
            resultIntent.putExtra("pick-result-data", new ArrayList(Arrays.asList(this.mResults)));
            resultIntent.putExtra("pick_sha1", new ArrayList(Arrays.asList(this.mIDs)));
        } else if (resultType == Picker.ResultType.OPEN_URI) {
            Uri uri = this.mResults[0];
            resultIntent.setDataAndType(uri, getContentResolver().getType(uri));
        } else {
            parseSingle(resultIntent, this.mResults[0].getPath());
        }
        resultIntent.setFlags(1);
        doCompleteOperation(resultIntent);
    }

    /* loaded from: classes2.dex */
    public class OriginUrlRequestProgressListener implements OriginUrlRequestor.ProgressListener {
        public OriginUrlRequestProgressListener() {
        }

        @Override // com.miui.gallery.picker.uri.OriginUrlRequestor.ProgressListener
        public void onStart(int i) {
            PickGalleryActivity.this.mOriginRequestProgressDialog = new PrepareProgressDialog();
            PickGalleryActivity.this.mOriginRequestProgressDialog.setMax(i);
            PickGalleryActivity.this.mOriginRequestProgressDialog.setStage(1);
            PickGalleryActivity.this.mOriginRequestProgressDialog.showAllowingStateLoss(PickGalleryActivity.this.getSupportFragmentManager(), "prepare_progress_dialog");
        }

        @Override // com.miui.gallery.picker.uri.OriginUrlRequestor.ProgressListener
        public void onEnd(ArrayList<OriginUrlRequestor.OriginInfo> arrayList, boolean z) {
            finish();
            if (z) {
                PickGalleryActivity.this.parseOriginUrlResult(arrayList);
            } else {
                DefaultLogger.e("PickGalleryActivity", "selected images OriginInfo generate error");
            }
        }

        @Override // com.miui.gallery.picker.uri.OriginUrlRequestor.ProgressListener
        public void onUpdate(int i) {
            if (PickGalleryActivity.this.mOriginRequestProgressDialog != null) {
                PickGalleryActivity.this.mOriginRequestProgressDialog.updateProgress(i);
            }
        }

        @Override // com.miui.gallery.picker.uri.OriginUrlRequestor.ProgressListener
        public void onCancelled() {
            finish();
        }

        public final void finish() {
            if (PickGalleryActivity.this.mOriginRequestProgressDialog != null) {
                PickGalleryActivity.this.mOriginRequestProgressDialog.dismissSafely();
                PickGalleryActivity.this.mOriginRequestProgressDialog = null;
            }
            if (PickGalleryActivity.this.mOriginRequestPendings != null) {
                PickGalleryActivity.this.mOriginRequestPendings.clear();
            }
            if (PickGalleryActivity.this.mOriginUrlRequestor != null) {
                PickGalleryActivity.this.mOriginUrlRequestor.destroy();
                PickGalleryActivity.this.mOriginUrlRequestor = null;
            }
        }
    }

    public final void parseOriginUrlResult(ArrayList<OriginUrlRequestor.OriginInfo> arrayList) {
        Intent resultIntent = getResultIntent();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(arrayList.get(i).toJson());
        }
        resultIntent.putExtra("pick-result-origin-download-info", arrayList2);
        resultIntent.putExtra("pick-result-data", new ArrayList(Arrays.asList(this.mResults)));
        resultIntent.putExtra("pick_sha1", new ArrayList(Arrays.asList(this.mIDs)));
        resultIntent.setFlags(1);
        doCompleteOperation(resultIntent);
    }

    public final Intent getResultIntent() {
        return isPickIntentMode() ? this.mPickIntent : new Intent();
    }

    public final void doCompleteOperation(Intent intent) {
        TrackController.trackClick("403.25.0.1.11310", AutoTracking.getRef());
        int i = -1;
        if (isPickIntentMode()) {
            Uri data = intent.getData();
            int i2 = this.mCloseType;
            boolean z = i2 == 3 || i2 == 4;
            if (data != null && TextUtils.equals(data.getAuthority(), "gallery.i.mi.com")) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("start_activity_for_result", z);
                bundle.putParcelable("extra_intent", intent);
                if (z) {
                    i = 42;
                }
                bundle.putInt("request_code", i);
                ActionURIHandler.handleUri(this, data, bundle);
            } else {
                this.mPickIntent.putExtra("is_start_activity_for_result", z);
                if (z) {
                    startActivityForResult(this.mPickIntent, 42);
                    this.isStartedActivity = true;
                } else {
                    startActivity(intent);
                }
                if (this.mDisablePendingTransition) {
                    overridePendingTransition(0, 0);
                }
            }
            if (this.mCloseType != 1 && !this.mPickIntent.getBooleanExtra("isLargeScreenMode", false)) {
                return;
            }
            finish();
            return;
        }
        setResult(-1, intent);
        finish();
    }

    public final boolean isPickIntentMode() {
        return this.mPickIntent != null;
    }

    @Override // com.miui.gallery.picker.PickerBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 42) {
            if (!checkUriPermissionFlags(intent)) {
                finish();
            } else if (i2 == -1) {
                setResult(-1, intent);
                if (intent != null && intent.getBooleanExtra("extra_pick_finish_remove_task", false)) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            } else if (this.mCloseType != 4) {
            } else {
                this.mPicker.clear();
            }
        }
    }

    public final void parseSingle(Intent intent, String str) {
        if (getPicker().getResultType() == Picker.ResultType.LEGACY_MEDIA) {
            Pair<Uri, String> queryMediaUri = queryMediaUri(str);
            if (queryMediaUri == null) {
                return;
            }
            intent.setDataAndType((Uri) queryMediaUri.first, (String) queryMediaUri.second);
            return;
        }
        Uri fromFile = Uri.fromFile(new File(str));
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        if ("*/*".equals(mimeType)) {
            intent.setData(fromFile);
        } else {
            intent.setDataAndType(fromFile, mimeType);
        }
    }

    public final Pair<Uri, String> queryMediaUri(String str) {
        Uri uri;
        String str2;
        Cursor query;
        Cursor cursor = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                if (getPicker().getMediaType() == Picker.MediaType.IMAGE) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    str2 = "vnd.android.cursor.dir/image";
                    query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{j.c}, "_data=?", new String[]{str}, null);
                } else {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    str2 = "vnd.android.cursor.dir/video";
                    query = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{j.c}, "_data=?", new String[]{str}, null);
                }
                cursor = query;
                if (cursor != null && cursor.moveToFirst()) {
                    uri = uri.buildUpon().appendPath(cursor.getString(0)).build();
                }
                return new Pair<>(uri, str2);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return null;
    }

    public final void showConfirmDialog(boolean z) {
        if (!z && NetworkConsider.sAgreedUsingMeteredNetwork && BaseNetworkUtils.isNetworkConnected()) {
            this.mOnConfirmed.onConfirmed(true, BaseNetworkUtils.isActiveNetworkMetered());
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("download_file_count", getPreparePendingToDownload().size());
        Uri[] uriArr = this.mResults;
        int i = 0;
        bundle.putInt("local_file_count", uriArr == null ? 0 : uriArr.length);
        Iterator<PickerPrepareItem> it = getPreparePendingToDownload().iterator();
        while (it.hasNext()) {
            i += it.next().getSize();
        }
        bundle.putInt("download_file_size", i);
        bundle.putBoolean("retry_mode", z);
        DownloadConfirmDialog downloadConfirmDialog = new DownloadConfirmDialog();
        downloadConfirmDialog.setArguments(bundle);
        downloadConfirmDialog.showAllowingStateLoss(getSupportFragmentManager(), "confirm_dialog");
    }

    public final void cancelPreparing() {
        PrepareTask<PickerPrepareItem> prepareTask = this.mPrepareTask;
        if (prepareTask != null) {
            prepareTask.release();
            this.mPrepareTask = null;
        }
    }

    public final void startPrepare() {
        cancelPreparing();
        ArrayList<PickerPrepareItem> allPreparePendings = getAllPreparePendings();
        this.mPrepareTask = new PrepareTask(this, allPreparePendings.size(), new OnPrepareListener()).invoke(allPreparePendings);
    }

    /* loaded from: classes2.dex */
    public class OnPrepareListener implements PrepareTask.OnPrepareListener<PickerPrepareItem> {
        public OnPrepareListener() {
        }

        @Override // com.miui.gallery.ui.share.PrepareTask.OnPrepareListener
        public void onStarted(ArrayList<PickerPrepareItem> arrayList) {
            DefaultLogger.d("PickGalleryActivity", "download start, %d", Integer.valueOf(arrayList.size()));
            PickGalleryActivity.this.mDownloadProgressDialog = new PrepareProgressDialog();
            PickGalleryActivity.this.mDownloadProgressDialog.setMax(arrayList.size());
            PickGalleryActivity.this.mDownloadProgressDialog.setStage(0);
            PickGalleryActivity.this.mDownloadProgressDialog.showAllowingStateLoss(PickGalleryActivity.this.getSupportFragmentManager(), "prepare_progress_dialog");
        }

        @Override // com.miui.gallery.ui.share.PrepareTask.OnPrepareListener
        public void onProgressUpdate(int i) {
            PickGalleryActivity.this.mDownloadProgressDialog.updateProgress(i);
        }

        @Override // com.miui.gallery.ui.share.PrepareTask.OnPrepareListener
        public void onCancelled(ArrayList<PickerPrepareItem> arrayList) {
            if (PickGalleryActivity.this.mDownloadProgressDialog != null) {
                PickGalleryActivity.this.mDownloadProgressDialog.dismissSafely();
                PickGalleryActivity.this.mDownloadProgressDialog = null;
            }
            parseDownloadResult(arrayList);
            DefaultLogger.d("PickGalleryActivity", "prepare cancelled, success: %d, fails: %d", Integer.valueOf(arrayList.size() - PickGalleryActivity.this.getAllPreparePendings().size()), Integer.valueOf(PickGalleryActivity.this.getAllPreparePendings().size()));
            PickGalleryActivity.this.cancelPreparing();
        }

        @Override // com.miui.gallery.ui.share.PrepareTask.OnPrepareListener
        public void onPrepared(ArrayList<PickerPrepareItem> arrayList) {
            if (PickGalleryActivity.this.mDownloadProgressDialog != null) {
                PickGalleryActivity.this.mDownloadProgressDialog.dismissSafely();
                Fragment findFragmentByTag = PickGalleryActivity.this.getSupportFragmentManager().findFragmentByTag("cancel_dialog");
                if (findFragmentByTag != null && (findFragmentByTag instanceof DownloadCancelDialog)) {
                    ((DownloadCancelDialog) findFragmentByTag).dismissSafely();
                }
                PickGalleryActivity.this.mDownloadProgressDialog = null;
            }
            parseDownloadResult(arrayList);
            DefaultLogger.d("PickGalleryActivity", "prepare end, success: %d, fails: %d", Integer.valueOf(arrayList.size() - PickGalleryActivity.this.getAllPreparePendings().size()), Integer.valueOf(PickGalleryActivity.this.getAllPreparePendings().size()));
            if (PickGalleryActivity.this.getPreparePendingToDownload().isEmpty()) {
                PickGalleryActivity.this.parseResult();
            } else {
                PickGalleryActivity.this.showConfirmDialog(true);
            }
            PickGalleryActivity.this.cancelPreparing();
        }

        public final void parseDownloadResult(List<PickerPrepareItem> list) {
            LinkedList linkedList = new LinkedList();
            if (BaseMiscUtil.isValid(list)) {
                for (PickerPrepareItem pickerPrepareItem : list) {
                    Uri preparedUri = pickerPrepareItem.getPreparedUri();
                    int position = pickerPrepareItem.getPosition();
                    if (preparedUri == null || !new File(preparedUri.getPath()).exists() || position < 0 || position >= PickGalleryActivity.this.mResults.length) {
                        linkedList.add(pickerPrepareItem);
                    } else {
                        PickGalleryActivity.this.mResults[pickerPrepareItem.getPosition()] = preparedUri;
                    }
                }
            }
            PickGalleryActivity.this.getPreparePendingToDownload().clear();
            PickGalleryActivity.this.getPreparePendingToDownload().addAll(linkedList);
        }
    }

    @Override // com.miui.gallery.picker.PickerActivity, androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DownloadConfirmDialog) {
            DownloadConfirmDialog downloadConfirmDialog = (DownloadConfirmDialog) fragment;
            downloadConfirmDialog.setPositiveListener(this.mDownloadListener);
            downloadConfirmDialog.setNegativeListener(this.mCancelDownloadListener);
        } else if (fragment instanceof PrepareProgressDialog) {
            ((PrepareProgressDialog) fragment).setCancelListener(this.mCancelConfirmListener);
        } else if (!(fragment instanceof DownloadCancelDialog)) {
        } else {
            DownloadCancelDialog downloadCancelDialog = (DownloadCancelDialog) fragment;
            downloadCancelDialog.setCancelListener(this.mCancelDownloadListener);
            downloadCancelDialog.setContinueListener(this.mContinueDownloadListener);
        }
    }

    /* loaded from: classes2.dex */
    public static class PickerPrepareItem implements DownloadItem, ConvertItem {
        public static final Parcelable.Creator<PickerPrepareItem> CREATOR = new Parcelable.Creator<PickerPrepareItem>() { // from class: com.miui.gallery.picker.PickGalleryActivity.PickerPrepareItem.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public PickerPrepareItem mo1175createFromParcel(Parcel parcel) {
                return new PickerPrepareItem(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public PickerPrepareItem[] mo1176newArray(int i) {
                return new PickerPrepareItem[i];
            }
        };
        public DownloadType mDownloadType;
        public String mFileTitle;
        public int mFlags;
        public int mPosition;
        public Uri mPreparedUri;
        public int mSize;
        public Uri mSrcUri;
        public Uri mTempUri;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public PickerPrepareItem(Builder builder) {
            this.mSrcUri = builder.mSrcUri;
            this.mFileTitle = builder.mFileTitle;
            this.mDownloadType = builder.mDownloadType;
            this.mFlags = builder.mFlags;
            this.mSize = builder.mSize;
            this.mPosition = builder.mPosition;
        }

        public PickerPrepareItem(Parcel parcel) {
            this.mSrcUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
            this.mFileTitle = parcel.readString();
            this.mDownloadType = (DownloadType) parcel.readParcelable(DownloadType.class.getClassLoader());
            this.mFlags = parcel.readInt();
            this.mSize = parcel.readInt();
            this.mPosition = parcel.readInt();
            this.mPreparedUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSrcUri, i);
            parcel.writeString(this.mFileTitle);
            parcel.writeParcelable(this.mDownloadType, i);
            parcel.writeInt(this.mFlags);
            parcel.writeInt(this.mSize);
            parcel.writeInt(this.mPosition);
            parcel.writeParcelable(this.mPreparedUri, i);
        }

        @Override // com.miui.gallery.ui.share.DownloadItem
        public DownloadType getDownloadType() {
            return this.mDownloadType;
        }

        @Override // com.miui.gallery.ui.share.PrepareItem
        public Uri getSrcUri() {
            return this.mSrcUri;
        }

        @Override // com.miui.gallery.ui.share.ConvertItem
        public String getFileTitle() {
            return this.mFileTitle;
        }

        @Override // com.miui.gallery.ui.share.PrepareItem
        public Uri getPreparedUri() {
            return this.mPreparedUri;
        }

        @Override // com.miui.gallery.ui.share.PrepareItem
        public int getFlags() {
            return this.mFlags;
        }

        @Override // com.miui.gallery.ui.share.PrepareItem
        public void onPrepared(Uri uri) {
            this.mPreparedUri = uri;
        }

        @Override // com.miui.gallery.ui.share.PrepareItem
        public Uri getPreparedUriInLastStep() {
            return this.mTempUri;
        }

        public final int getSize() {
            return this.mSize;
        }

        public final int getPosition() {
            return this.mPosition;
        }

        @Override // com.miui.gallery.ui.share.PrepareItem
        public void onStepPrepared(Uri uri, int i) {
            this.mTempUri = uri;
            this.mFlags &= ~i;
        }

        /* loaded from: classes2.dex */
        public static class Builder {
            public DownloadType mDownloadType;
            public String mFileTitle;
            public int mFlags;
            public int mPosition;
            public int mSize;
            public Uri mSrcUri;

            public Builder setSrcUri(Uri uri) {
                this.mSrcUri = uri;
                return this;
            }

            public Builder setFileTitle(String str) {
                this.mFileTitle = str;
                return this;
            }

            public Builder setDownloadType(DownloadType downloadType) {
                this.mDownloadType = downloadType;
                return this;
            }

            public Builder setFlags(int i) {
                this.mFlags = i;
                return this;
            }

            public Builder setSize(int i) {
                this.mSize = i;
                return this;
            }

            public Builder setPosition(int i) {
                this.mPosition = i;
                return this;
            }

            public PickerPrepareItem build() {
                return new PickerPrepareItem(this);
            }
        }
    }
}
