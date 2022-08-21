package com.miui.gallery.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.android.internal.WindowCompat;
import com.github.chrisbanes.photoview.PhotoView;
import com.miui.gallery.R;
import com.miui.gallery.activity.BurstPhotoActivity;
import com.miui.gallery.adapter.BurstPhotoPageAdapter;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.card.ui.detail.DownloadDialog;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.picker.uri.DownloadConfirmDialog;
import com.miui.gallery.picker.uri.Downloader;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.BurstPhotoFragment;
import com.miui.gallery.ui.BurstPhotoPreviewFragment;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.ui.TextListDialogFragment;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SecretAlbumCryptoUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.timeburst.ComposeCallback;
import com.miui.gallery.video.timeburst.IBurstPhotoComposer;
import com.miui.gallery.video.timeburst.Resolution;
import com.miui.gallery.video.timeburst.ResolutionLevel;
import com.miui.gallery.video.timeburst.TimeBurstVideoChecker;
import com.miui.gallery.video.timeburst.Utils;
import com.xiaomi.stat.MiStat;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class BurstPhotoFragment extends AbstractViewPagerFragment {
    public ChoiceModeManager mChoiceManager;
    public int mCurPos;
    public String mFileFilter;
    public boolean mHasDataLoaded;
    public boolean mIsFirstEntered;
    public boolean mIsTimeBurst;
    public List<Integer> mSelectedItems;
    public TextView mTitle;
    public TransitionConfig mTransitionConfig;

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public String getPageName() {
        return "burst";
    }

    public int getThemeRes() {
        return 2131952029;
    }

    public static BurstPhotoFragment newInstance(Intent intent) {
        Uri uri;
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
            uri = intent.getData();
        } else {
            uri = null;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (uri != null) {
            bundle.putString("photo_uri", uri.toString());
        }
        if (intent != null && intent.getBooleanExtra("has_transition", false)) {
            bundle.putBoolean("has_transition", true);
        }
        BurstPhotoFragment burstPhotoFragment = new BurstPhotoFragment();
        burstPhotoFragment.setArguments(bundle);
        return burstPhotoFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
        this.mTransitionConfig = new TransitionConfig(bundle.getBoolean("has_transition"), bundle.getString("extra_image_url"));
        boolean z = bundle.getBoolean("is_time_burst");
        this.mIsTimeBurst = z;
        this.mFileFilter = z ? "_TIMEBURST" : "_BURST";
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            boolean z = bundle.getBoolean("has_data_loaded", false);
            this.mHasDataLoaded = z;
            if (z) {
                this.mCurPos = bundle.getInt("current_position", 0);
                if (bundle.get("selected_items") != null) {
                    this.mSelectedItems = bundle.getIntegerArrayList("selected_items");
                }
            } else {
                this.mCurPos = 0;
            }
        }
        setThemeRes(getThemeRes());
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        onContentChanged();
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public PhotoPageAdapter getPhotoAdapter() {
        ImageLoadParams imageLoadParams = getImageLoadParams();
        return new BurstPhotoPageAdapter(getActivity(), getInitCount(), imageLoadParams, getEnterViewInfo(getArguments().getInt("photo_init_position", 0), imageLoadParams != null ? imageLoadParams.getKey() : -1L), this, getPhotoPageInteractionListener());
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onViewInflated(View view) {
        configViewLayout(view);
        WindowCompat.setCutoutModeShortEdges(this.mActivity.getWindow());
        TextView textView = (TextView) view.findViewById(R.id.title);
        this.mTitle = textView;
        if (this.mIsTimeBurst) {
            textView.setText(R.string.time_burst_preview_title);
        } else {
            textView.setText(R.string.burst_preview_title);
        }
        this.mChoiceManager = this.mIsTimeBurst ? new TimeBurstChoiceModeManager() : new BurstChoiceModeManager();
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onItemChanged(int i) {
        ChoiceModeManager choiceModeManager = this.mChoiceManager;
        if (choiceModeManager != null) {
            choiceModeManager.onItemChanged(i);
        }
    }

    public final void configViewLayout(View view) {
        view.findViewById(R.id.top_part).setPadding(0, getArguments().getInt("notch_height", 0), 0, 0);
        this.mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.burst_pager_margin));
        ((BurstPhotoPageAdapter) this.mAdapter).setPagerView(this.mPager);
        ((BurstPhotoPageAdapter) this.mAdapter).setMaxPagerItemWidth(getResources().getDimensionPixelSize(R.dimen.burst_pager_item_max_width));
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onDataSetLoaded(BaseDataSet baseDataSet, boolean z) {
        super.onDataSetLoaded(baseDataSet, z);
        this.mHasDataLoaded = true;
        ChoiceModeManager choiceModeManager = this.mChoiceManager;
        if (choiceModeManager != null) {
            if (z) {
                choiceModeManager.onStart();
            }
            this.mChoiceManager.onDataSetLoaded(baseDataSet);
        }
    }

    /* loaded from: classes2.dex */
    public abstract class ChoiceModeManager implements PhotoPageAdapter.MultiChoiceModeListener {
        public PhotoPageAdapter.ChoiceMode mBurstChoiceMode;

        public abstract void attachFragment(Fragment fragment);

        public abstract void doSave();

        @Override // com.miui.gallery.adapter.PhotoPageAdapter.MultiChoiceModeListener
        public final void onAllItemsCheckedStateChanged(boolean z) {
        }

        public abstract void release();

        public ChoiceModeManager() {
            BurstPhotoFragment.this = r1;
            setUpPreviewFragment();
        }

        public BurstPhotoPreviewFragment setUpPreviewFragment() {
            BurstPhotoPreviewFragment findBurstPreviewFragment = findBurstPreviewFragment();
            findBurstPreviewFragment.setDataSet(BurstPhotoFragment.this.mAdapter.getDataSet());
            findBurstPreviewFragment.setOnItemScrolledListener(new BurstPhotoPreviewFragment.OnScrollToPositionListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager.1
                {
                    ChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.BurstPhotoPreviewFragment.OnScrollToPositionListener
                public void onScrollToPosition(int i) {
                    BurstPhotoFragment.this.mPager.setCurrentItem(i, false);
                }
            });
            findBurstPreviewFragment.setOnExitListener(new BurstPhotoPreviewFragment.OnExitListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager.2
                {
                    ChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.BurstPhotoPreviewFragment.OnExitListener
                public void onSave() {
                    ChoiceModeManager.this.doSave();
                }

                @Override // com.miui.gallery.ui.BurstPhotoPreviewFragment.OnExitListener
                public void onDiscard() {
                    ChoiceModeManager.this.discard();
                }
            });
            return findBurstPreviewFragment;
        }

        public BurstPhotoPreviewFragment findBurstPreviewFragment() {
            Fragment findFragmentByTag = BurstPhotoFragment.this.getChildFragmentManager().findFragmentByTag("BurstPhotoPreviewFragment");
            if (findFragmentByTag == null) {
                FragmentTransaction beginTransaction = BurstPhotoFragment.this.getChildFragmentManager().beginTransaction();
                BurstPhotoPreviewFragment burstPhotoPreviewFragment = new BurstPhotoPreviewFragment();
                beginTransaction.add(R.id.photo_detail_preview, burstPhotoPreviewFragment, "BurstPhotoPreviewFragment");
                beginTransaction.commitAllowingStateLoss();
                findFragmentByTag = burstPhotoPreviewFragment;
            }
            return (BurstPhotoPreviewFragment) findFragmentByTag;
        }

        public void discard() {
            setPhotoChoiceTitleVisible(false);
            exit(false, false);
        }

        public void onItemChanged(int i) {
            Fragment findFragmentByTag = BurstPhotoFragment.this.getChildFragmentManager().findFragmentByTag("BurstPhotoPreviewFragment");
            if (findFragmentByTag != null) {
                ((BurstPhotoPreviewFragment) findFragmentByTag).scrollToPosition(i);
            }
        }

        public void updateSelectMode() {
            PhotoPageAdapter.ChoiceMode choiceMode = this.mBurstChoiceMode;
            if (choiceMode == null) {
                return;
            }
            List<Integer> selectItems = choiceMode.getSelectItems();
            if (selectItems != null) {
                selectItems.size();
            }
            BurstPhotoFragment.this.mTransitionConfig.updateEditCover(selectItems);
        }

        public final void setPhotoChoiceTitleVisible(boolean z) {
            int i = z ? 0 : 4;
            if (i != BurstPhotoFragment.this.mTitle.getVisibility()) {
                BurstPhotoFragment.this.mTitle.setVisibility(i);
            }
        }

        @Override // com.miui.gallery.adapter.PhotoPageAdapter.MultiChoiceModeListener
        public void onItemCheckedStateChanged(int i, long j, boolean z) {
            updateSelectMode();
            Fragment findFragmentByTag = BurstPhotoFragment.this.getChildFragmentManager().findFragmentByTag("BurstPhotoPreviewFragment");
            if (findFragmentByTag != null) {
                ((BurstPhotoPreviewFragment) findFragmentByTag).setCheckedItem(i, z, this.mBurstChoiceMode.getSelectItems().size() > 0);
                if (BurstPhotoFragment.this.mIsFirstEntered) {
                    return;
                }
                LinearMotorHelper.performHapticFeedback(BurstPhotoFragment.this.mPagerHelper.getCurrentItem(), LinearMotorHelper.HAPTIC_MESH_NORMAL);
            }
        }

        public void onStart() {
            int intValue;
            this.mBurstChoiceMode = BurstPhotoFragment.this.mAdapter.startActionMode(this);
            BurstPhotoFragment burstPhotoFragment = BurstPhotoFragment.this;
            burstPhotoFragment.mPager.setCurrentItem(burstPhotoFragment.mCurPos);
            BurstPhotoFragment.this.mIsFirstEntered = true;
            if (BurstPhotoFragment.this.mSelectedItems != null) {
                Iterator it = BurstPhotoFragment.this.mSelectedItems.iterator();
                while (it.hasNext() && (intValue = ((Integer) it.next()).intValue()) <= BurstPhotoFragment.this.mAdapter.getCount()) {
                    this.mBurstChoiceMode.setChecked(intValue, BurstPhotoFragment.this.mAdapter.getDataItem(intValue).getKey(), true);
                }
                return;
            }
            this.mBurstChoiceMode.setChecked(0, BurstPhotoFragment.this.mAdapter.getDataItem(0).getKey(), true);
        }

        public void onDataSetLoaded(BaseDataSet baseDataSet) {
            BurstPhotoPreviewFragment burstPhotoPreviewFragment = (BurstPhotoPreviewFragment) BurstPhotoFragment.this.getChildFragmentManager().findFragmentByTag("BurstPhotoPreviewFragment");
            if (burstPhotoPreviewFragment != null) {
                burstPhotoPreviewFragment.setDataSet(baseDataSet);
            }
            updateSelectMode();
        }

        public void exit(boolean z, boolean z2) {
            BurstPhotoFragment burstPhotoFragment = BurstPhotoFragment.this;
            if (burstPhotoFragment.mActivity != null) {
                burstPhotoFragment.mTransitionConfig.exit(z, z2);
            }
        }

        public void onSaveBurstItemsCompleted(boolean z) {
            exit(true, z);
        }
    }

    /* loaded from: classes2.dex */
    public class BurstChoiceModeManager extends ChoiceModeManager {
        @Override // com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager
        public void attachFragment(Fragment fragment) {
        }

        @Override // com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager
        public void release() {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public BurstChoiceModeManager() {
            super();
            BurstPhotoFragment.this = r1;
        }

        @Override // com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager
        public void doSave() {
            List<Integer> selectItems = this.mBurstChoiceMode.getSelectItems();
            if (!BaseMiscUtil.isValid(selectItems)) {
                return;
            }
            final String string = BurstPhotoFragment.this.getString(R.string.burst_save_all);
            final String quantityString = BurstPhotoFragment.this.getResources().getQuantityString(R.plurals.burst_save_selected, selectItems.size(), Integer.valueOf(selectItems.size()));
            ArrayList arrayList = new ArrayList();
            if (BaseMiscUtil.isValid(selectItems)) {
                arrayList.add(string);
                arrayList.add(quantityString);
            }
            TextListDialogFragment newInstance = TextListDialogFragment.newInstance(arrayList, BurstPhotoFragment.this.getString(R.string.burst_save_dialog_title));
            newInstance.setOnItemSelectedListener(new TextListDialogFragment.OnItemSelectedListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.BurstChoiceModeManager.1
                {
                    BurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.TextListDialogFragment.OnItemSelectedListener
                public void onItemSelected(String str, int i) {
                    if (string.equalsIgnoreCase(str)) {
                        BurstChoiceModeManager.this.saveBurstItems(true);
                    } else if (!quantityString.equalsIgnoreCase(str)) {
                    } else {
                        BurstChoiceModeManager.this.ShowSaveSelectedDialog();
                    }
                }
            });
            newInstance.show(BurstPhotoFragment.this.getFragmentManager(), "burstSaveDialog");
        }

        public final void saveBurstItems(final boolean z) {
            ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback<Void, List<IStoragePermissionStrategy.PermissionResult>, Boolean>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.BurstChoiceModeManager.2
                {
                    BurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                public List<IStoragePermissionStrategy.PermissionResult> doPrepare(Void[] voidArr) {
                    LinkedList linkedList = new LinkedList();
                    List<Integer> selectItems = BurstChoiceModeManager.this.mBurstChoiceMode.getSelectItems();
                    for (int i = 0; i < BurstPhotoFragment.this.mAdapter.getCount(); i++) {
                        BaseDataItem dataItem = BurstPhotoFragment.this.mAdapter.getDataItem(i);
                        String str = null;
                        if (!TextUtils.isEmpty(dataItem.getOriginalPath())) {
                            str = dataItem.getOriginalPath();
                        } else if (!TextUtils.isEmpty(dataItem.getThumnailPath())) {
                            str = dataItem.getThumnailPath();
                        }
                        if (str != null && (z || selectItems.contains(Integer.valueOf(i)))) {
                            linkedList.add(StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.UPDATE));
                        }
                    }
                    return linkedList;
                }

                /* JADX WARN: Removed duplicated region for block: B:116:0x0119 A[Catch: Exception -> 0x01d7, TryCatch #0 {Exception -> 0x01d7, blocks: (B:77:0x002c, B:79:0x0046, B:81:0x005c, B:87:0x007a, B:89:0x007e, B:92:0x0089, B:94:0x0095, B:96:0x009b, B:98:0x00c0, B:118:0x01a2, B:102:0x00d2, B:104:0x00e0, B:105:0x00ec, B:107:0x00fa, B:109:0x0100, B:111:0x0106, B:114:0x0112, B:116:0x0119, B:117:0x011e, B:83:0x0065, B:85:0x006f, B:119:0x01ac, B:121:0x01b2, B:122:0x01c1, B:124:0x01c7), top: B:134:0x002c }] */
                @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public java.lang.Boolean doProcess(java.lang.Void[] r22) {
                    /*
                        Method dump skipped, instructions count: 533
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.BurstPhotoFragment.BurstChoiceModeManager.AnonymousClass2.doProcess(java.lang.Void[]):java.lang.Boolean");
                }
            }, new ProcessTask.OnPrepareCompleteListener<List<IStoragePermissionStrategy.PermissionResult>>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.BurstChoiceModeManager.3
                {
                    BurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnPrepareCompleteListener
                public boolean onPrepareComplete(List<IStoragePermissionStrategy.PermissionResult> list) {
                    boolean z2 = true;
                    for (IStoragePermissionStrategy.PermissionResult permissionResult : list) {
                        if (!permissionResult.granted) {
                            if (permissionResult.applicable) {
                                StorageSolutionProvider.get().requestPermission(BurstPhotoFragment.this.getActivity(), permissionResult.path, permissionResult.type);
                            }
                            z2 = false;
                        }
                    }
                    return z2;
                }
            });
            processTask.setCompleteListener(new ProcessTask.OnCompleteListener<Boolean>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.BurstChoiceModeManager.4
                {
                    BurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
                public void onCompleteProcess(Boolean bool) {
                    if (bool != null && bool.booleanValue()) {
                        BurstChoiceModeManager.this.onSaveBurstItemsCompleted(false);
                    }
                }
            });
            BurstPhotoFragment burstPhotoFragment = BurstPhotoFragment.this;
            processTask.showProgress(burstPhotoFragment.mActivity, burstPhotoFragment.getString(R.string.burst_save_processing));
            processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }

        public final void ShowSaveSelectedDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(BurstPhotoFragment.this.mActivity);
            int count = BurstPhotoFragment.this.mAdapter.getCount() - this.mBurstChoiceMode.getSelectItems().size();
            builder.setCancelable(true).setTitle(BurstPhotoFragment.this.getResources().getQuantityString(R.plurals.burst_save_confirm_dialog_title, count, Integer.valueOf(count))).setMessage(BurstPhotoFragment.this.getResources().getQuantityString(R.plurals.burst_save_confirm_dialog_message, count, Integer.valueOf(count))).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.BurstChoiceModeManager.5
                {
                    BurstChoiceModeManager.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    BurstChoiceModeManager.this.saveBurstItems(false);
                }
            }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create().show();
        }
    }

    /* loaded from: classes2.dex */
    public class TimeBurstChoiceModeManager extends ChoiceModeManager {
        public DialogInterface.OnClickListener mCancelDownloadListener;
        public Future<Object> mCheckDownloadFilesFuture;
        public DownloadDialog.CallBack mDownloadCallback;
        public DownloadDialog mDownloadDialog;
        public DialogInterface.OnClickListener mDownloadListener;
        public ArrayList<Downloader.DownloadTask> mDownloadPendings;
        public Downloader mDownloader;
        public String mExtention;
        public NetworkConsider.OnConfirmed mOnConfirmed;
        public int mOpration;
        public String[] mResults;
        public SaveVideoManager mSaveVideoManager;

        /* renamed from: $r8$lambda$rtZHCH2BKPWMbVL_AhqpX-Ru6Gc */
        public static /* synthetic */ void m1432$r8$lambda$rtZHCH2BKPWMbVL_AhqpXRu6Gc(TimeBurstChoiceModeManager timeBurstChoiceModeManager, boolean z, boolean z2) {
            timeBurstChoiceModeManager.lambda$showNetworkMeteredDialog$0(z, z2);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TimeBurstChoiceModeManager() {
            super();
            BurstPhotoFragment.this = r1;
            this.mDownloadPendings = new ArrayList<>();
            this.mOnConfirmed = new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.8
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public void onConfirmed(boolean z, boolean z2) {
                    if (!z) {
                        if (TimeBurstChoiceModeManager.this.mDownloader == null) {
                            return;
                        }
                        TimeBurstChoiceModeManager.this.mDownloader.cancel();
                    } else if (BaseNetworkUtils.isNetworkConnected()) {
                        TimeBurstChoiceModeManager.this.startDownload();
                    } else {
                        ToastUtils.makeText(BurstPhotoFragment.this.mActivity, (int) R.string.picker_no_network_message);
                        TimeBurstChoiceModeManager.this.showConfirmDialog(true);
                    }
                }
            };
            this.mDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.9
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BaseNetworkUtils.isNetworkConnected()) {
                        TimeBurstChoiceModeManager.this.startDownload();
                        return;
                    }
                    ToastUtils.makeText(BurstPhotoFragment.this.mActivity, (int) R.string.picker_no_network_message);
                    TimeBurstChoiceModeManager.this.showConfirmDialog(true);
                }
            };
            this.mDownloadCallback = new DownloadDialog.CallBack() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.10
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.card.ui.detail.DownloadDialog.CallBack
                public void cancel() {
                    if (TimeBurstChoiceModeManager.this.mDownloader != null) {
                        TimeBurstChoiceModeManager.this.mDownloader.cancel();
                    }
                }
            };
            this.mCancelDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.11
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (TimeBurstChoiceModeManager.this.mDownloader != null) {
                        TimeBurstChoiceModeManager.this.mDownloader.cancel();
                    }
                }
            };
        }

        @Override // com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager
        public BurstPhotoPreviewFragment setUpPreviewFragment() {
            return super.setUpPreviewFragment();
        }

        @Override // com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager, com.miui.gallery.adapter.PhotoPageAdapter.MultiChoiceModeListener
        public void onItemCheckedStateChanged(int i, long j, boolean z) {
            updateSelectMode();
            Fragment findFragmentByTag = BurstPhotoFragment.this.getChildFragmentManager().findFragmentByTag("BurstPhotoPreviewFragment");
            if (findFragmentByTag != null) {
                ((BurstPhotoPreviewFragment) findFragmentByTag).setCheckedItem(i, z, this.mBurstChoiceMode.getSelectItems().size() > 0 || isSaveVideoEnable());
                if (BurstPhotoFragment.this.mIsFirstEntered) {
                    return;
                }
                LinearMotorHelper.performHapticFeedback(BurstPhotoFragment.this.mPagerHelper.getCurrentItem(), LinearMotorHelper.HAPTIC_MESH_NORMAL);
            }
        }

        public final boolean isSaveVideoEnable() {
            return TimeBurstVideoChecker.isDeviceSupport() && BurstPhotoFragment.this.mAdapter.getCount() >= 30;
        }

        @Override // com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager
        public void doSave() {
            SamplingStatHelper.recordCountEvent("photo_extra", "time_burst_click_save");
            final List<Integer> selectItems = this.mBurstChoiceMode.getSelectItems();
            final String string = BurstPhotoFragment.this.getString(R.string.time_burst_save_video);
            final String string2 = BurstPhotoFragment.this.getString(R.string.time_burst_save_all);
            String string3 = BurstPhotoFragment.this.getString(R.string.time_burst_save_select);
            ArrayList arrayList = new ArrayList();
            if (isSaveVideoEnable()) {
                arrayList.add(string);
            }
            if (BaseMiscUtil.isValid(selectItems)) {
                arrayList.add(string2);
                arrayList.add(string3);
            }
            if (!BaseMiscUtil.isValid(arrayList)) {
                return;
            }
            TextListDialogFragment newInstance = TextListDialogFragment.newInstance(arrayList, BurstPhotoFragment.this.getString(R.string.time_burst_save_dialog_title));
            newInstance.setOnItemSelectedListener(new TextListDialogFragment.OnItemSelectedListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.1
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.TextListDialogFragment.OnItemSelectedListener
                public void onItemSelected(String str, int i) {
                    boolean z = false;
                    if (string.equalsIgnoreCase(str)) {
                        TimeBurstChoiceModeManager.this.mOpration = 0;
                    } else if (string2.equalsIgnoreCase(str)) {
                        TimeBurstChoiceModeManager.this.mOpration = 1;
                        HashMap hashMap = new HashMap();
                        hashMap.put("all", string2);
                        if (BaseMiscUtil.isValid(selectItems)) {
                            hashMap.put(MiStat.Param.COUNT, Integer.toString(selectItems.size()));
                        }
                        SamplingStatHelper.recordCountEvent("photo_extra", "time_burst_save_photo", hashMap);
                    } else {
                        TimeBurstChoiceModeManager.this.mOpration = 2;
                        TimeBurstChoiceModeManager.this.checkSaveCondition(z);
                    }
                    z = true;
                    TimeBurstChoiceModeManager.this.checkSaveCondition(z);
                }
            });
            newInstance.show(BurstPhotoFragment.this.getFragmentManager(), "timeBurstSaveDialog");
        }

        public final void checkSaveCondition(final boolean z) {
            this.mCheckDownloadFilesFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.2
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Object mo1807run(ThreadPool.JobContext jobContext) {
                    if (BurstPhotoFragment.this.mAdapter.getCount() > 0) {
                        DefaultLogger.d("BurstPhotoFragment", "start check download file.");
                        TimeBurstChoiceModeManager.this.checkDownloadPendingFiles(z);
                        return null;
                    }
                    return null;
                }
            }, new FutureHandler<Object>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.3
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<Object> future) {
                    if (future == null || future.isCancelled()) {
                        return;
                    }
                    TimeBurstChoiceModeManager timeBurstChoiceModeManager = TimeBurstChoiceModeManager.this;
                    if (BurstPhotoFragment.this.mActivity == null) {
                        return;
                    }
                    if (BaseMiscUtil.isValid(timeBurstChoiceModeManager.mDownloadPendings)) {
                        if (BaseGalleryPreferences.CTA.canConnectNetwork()) {
                            TimeBurstChoiceModeManager.this.checkNetworkAndDownload();
                            return;
                        } else {
                            AgreementsUtils.showUserAgreements(BurstPhotoFragment.this.mActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.3.1
                                {
                                    AnonymousClass3.this = this;
                                }

                                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                                public void onAgreementInvoked(boolean z2) {
                                    if (z2) {
                                        TimeBurstChoiceModeManager.this.checkNetworkAndDownload();
                                    }
                                }
                            });
                            return;
                        }
                    }
                    TimeBurstChoiceModeManager.this.onDownloadEnd();
                }
            });
        }

        public final void checkNetworkAndDownload() {
            if (!BaseNetworkUtils.isNetworkConnected()) {
                ToastUtils.makeText(BurstPhotoFragment.this.mActivity, (int) R.string.picker_no_network_message);
            } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
                showConfirmDialog(false);
            } else {
                startDownload();
            }
        }

        public void onDownloadEnd() {
            boolean z = false;
            BaseDataItem dataItem = BurstPhotoFragment.this.mAdapter.getDataItem(0);
            int i = this.mOpration;
            if (i == 0) {
                if (this.mSaveVideoManager == null) {
                    this.mSaveVideoManager = new SaveVideoManager(dataItem, this.mResults);
                }
                this.mSaveVideoManager.showSaveVideoDialog();
                return;
            }
            if (i == 1) {
                z = true;
            }
            copyBurstItem(z, dataItem.isSecret());
        }

        public void copyBurstItem(final boolean z, final boolean z2) {
            String[] strArr = this.mResults;
            if (strArr == null) {
                return;
            }
            String str = strArr[0];
            ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback<Void, List<IStoragePermissionStrategy.PermissionResult>, Boolean>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.4
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                public List<IStoragePermissionStrategy.PermissionResult> doPrepare(Void[] voidArr) {
                    LinkedList linkedList = new LinkedList();
                    List<Integer> selectItems = TimeBurstChoiceModeManager.this.mBurstChoiceMode.getSelectItems();
                    for (int i = 0; i < TimeBurstChoiceModeManager.this.mResults.length; i++) {
                        if (z || selectItems.contains(Integer.valueOf(i))) {
                            String str2 = TimeBurstChoiceModeManager.this.mResults[i];
                            if (!TextUtils.isEmpty(str2) && new File(str2).exists()) {
                                String fileName = z2 ? BurstPhotoFragment.this.mAdapter.getDataItem(i).getTitle() + "." + TimeBurstChoiceModeManager.this.getExtention() : BaseFileUtils.getFileName(str2);
                                if (!TextUtils.isEmpty(fileName)) {
                                    String replace = fileName.replace(BurstPhotoFragment.this.mFileFilter, "_");
                                    String parentFolderPath = BaseFileUtils.getParentFolderPath(str2);
                                    File file = new File(parentFolderPath, replace);
                                    if (file.exists()) {
                                        file = new File(parentFolderPath, DownloadPathHelper.addPostfixToFileName(replace, String.valueOf(System.currentTimeMillis())));
                                    }
                                    linkedList.add(StorageSolutionProvider.get().checkPermission(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT));
                                }
                            }
                        }
                    }
                    return linkedList;
                }

                @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                public Boolean doProcess(Void[] voidArr) {
                    DocumentFile documentFile;
                    boolean decrypt;
                    DocumentFile documentFile2;
                    DocumentFile documentFile3;
                    List<Integer> selectItems = TimeBurstChoiceModeManager.this.mBurstChoiceMode.getSelectItems();
                    try {
                        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("BurstPhotoFragment", "doProcess");
                        boolean z3 = false;
                        for (int i = 0; i < TimeBurstChoiceModeManager.this.mResults.length; i++) {
                            if (z || selectItems.contains(Integer.valueOf(i))) {
                                String str2 = TimeBurstChoiceModeManager.this.mResults[i];
                                if (!TextUtils.isEmpty(str2)) {
                                    File file = new File(str2);
                                    if (file.exists()) {
                                        String fileName = z2 ? BurstPhotoFragment.this.mAdapter.getDataItem(i).getTitle() + "." + TimeBurstChoiceModeManager.this.getExtention() : BaseFileUtils.getFileName(str2);
                                        if (!TextUtils.isEmpty(fileName)) {
                                            String replace = fileName.replace(BurstPhotoFragment.this.mFileFilter, "_");
                                            String parentFolderPath = BaseFileUtils.getParentFolderPath(str2);
                                            File file2 = new File(parentFolderPath, replace);
                                            if (file2.exists()) {
                                                file2 = new File(parentFolderPath, DownloadPathHelper.addPostfixToFileName(replace, String.valueOf(System.currentTimeMillis())));
                                            }
                                            if (z2) {
                                                if (BurstPhotoFragment.this.mAdapter.getDataItem(i).getSecretKey() != null) {
                                                    decrypt = SecretAlbumCryptoUtils.decrypt(TimeBurstChoiceModeManager.this.mResults[i], file2.getAbsolutePath(), false, BurstPhotoFragment.this.mAdapter.getDataItem(i).getSecretKey());
                                                } else {
                                                    decrypt = StorageSolutionProvider.get().copyFile(str2, file2.getAbsolutePath(), appendInvokerTag);
                                                }
                                                if (decrypt) {
                                                    if (file.lastModified() > 0 && (documentFile3 = StorageSolutionProvider.get().getDocumentFile(file2.getAbsolutePath(), IStoragePermissionStrategy.Permission.UPDATE, appendInvokerTag)) != null && documentFile3.exists()) {
                                                        StorageSolutionProvider.get().setLastModified(documentFile3, file.lastModified() - 1);
                                                    }
                                                    if (SaveToCloudUtil.saveToCloudDB(BurstPhotoFragment.this.mActivity, new SaveParams.Builder().setSaveFile(file2).setAlbumId(-1000L).setLocalFlag(8).setCredible(true).build()).getMediaId() <= 0 && (documentFile2 = StorageSolutionProvider.get().getDocumentFile(file2.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                                                        documentFile2.delete();
                                                    }
                                                }
                                            } else if (StorageSolutionProvider.get().copyFile(str2, file2.getAbsolutePath(), appendInvokerTag)) {
                                                if (file.lastModified() > 0 && (documentFile = StorageSolutionProvider.get().getDocumentFile(file2.getAbsolutePath(), IStoragePermissionStrategy.Permission.UPDATE, appendInvokerTag)) != null && documentFile.exists()) {
                                                    StorageSolutionProvider.get().setLastModified(documentFile, file.lastModified() - 1);
                                                }
                                                ScannerEngine.getInstance().scanFile(BurstPhotoFragment.this.mActivity, file2.getAbsolutePath(), 13);
                                            }
                                            if (!z3) {
                                                BurstPhotoFragment.this.mTransitionConfig.updateEditCover(file2.getAbsolutePath());
                                                z3 = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return Boolean.TRUE;
                    } catch (Exception unused) {
                        return Boolean.FALSE;
                    }
                }
            }, new ProcessTask.OnPrepareCompleteListener<List<IStoragePermissionStrategy.PermissionResult>>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.5
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnPrepareCompleteListener
                public boolean onPrepareComplete(List<IStoragePermissionStrategy.PermissionResult> list) {
                    boolean z3 = true;
                    for (IStoragePermissionStrategy.PermissionResult permissionResult : list) {
                        if (!permissionResult.granted) {
                            if (permissionResult.applicable) {
                                StorageSolutionProvider.get().requestPermission(BurstPhotoFragment.this.getActivity(), permissionResult.path, permissionResult.type);
                            }
                            z3 = false;
                        }
                    }
                    return z3;
                }
            });
            processTask.setCompleteListener(new ProcessTask.OnCompleteListener<Boolean>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.6
                {
                    TimeBurstChoiceModeManager.this = this;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
                public void onCompleteProcess(Boolean bool) {
                    if (bool == null) {
                        return;
                    }
                    TimeBurstChoiceModeManager.this.onSaveBurstItemsCompleted(true);
                }
            });
            BurstPhotoFragment burstPhotoFragment = BurstPhotoFragment.this;
            processTask.showProgress(burstPhotoFragment.mActivity, burstPhotoFragment.getString(R.string.burst_save_processing));
            processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }

        public final void checkDownloadPendingFiles(boolean z) {
            if (this.mDownloadPendings.size() > 0) {
                this.mDownloadPendings.clear();
            }
            this.mResults = new String[BurstPhotoFragment.this.mAdapter.getCount()];
            List<Integer> selectItems = this.mBurstChoiceMode.getSelectItems();
            if (z || BaseMiscUtil.isValid(selectItems)) {
                for (int i = 0; i < BurstPhotoFragment.this.mAdapter.getCount(); i++) {
                    BaseDataItem dataItem = BurstPhotoFragment.this.mAdapter.getDataItem(i);
                    String originalPath = dataItem.getOriginalPath();
                    if (!TextUtils.isEmpty(originalPath) && new File(originalPath).exists()) {
                        this.mResults[i] = originalPath;
                    } else if (z || selectItems.contains(Integer.valueOf(i))) {
                        this.mDownloadPendings.add(new Downloader.DownloadTask(CloudUriAdapter.getDownloadUri(dataItem.getKey()), DownloadType.ORIGIN_FORCE, (int) dataItem.getSize(), i));
                    }
                }
            }
        }

        public final String getExtention() {
            if (this.mExtention == null) {
                BurstPhotoFragment burstPhotoFragment = BurstPhotoFragment.this;
                String str = (String) SafeDBUtil.safeQuery(burstPhotoFragment.mActivity, GalleryContract.Cloud.CLOUD_URI, new String[]{"fileName"}, "_id=?", new String[]{String.valueOf(burstPhotoFragment.mAdapter.getDataItem(0).getKey())}, (String) null, new SafeDBUtil.QueryHandler<String>() { // from class: com.miui.gallery.ui.BurstPhotoFragment.TimeBurstChoiceModeManager.7
                    {
                        TimeBurstChoiceModeManager.this = this;
                    }

                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                    /* renamed from: handle */
                    public String mo1808handle(Cursor cursor) {
                        if (cursor == null || !cursor.moveToFirst()) {
                            return null;
                        }
                        return cursor.getString(0);
                    }
                });
                this.mExtention = TextUtils.isEmpty(str) ? "jpg" : BaseFileUtils.getExtension(str);
            }
            return this.mExtention;
        }

        public final void showNetworkMeteredDialog() {
            NetworkConsider.consider(BurstPhotoFragment.this.mActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.ui.BurstPhotoFragment$TimeBurstChoiceModeManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    BurstPhotoFragment.TimeBurstChoiceModeManager.m1432$r8$lambda$rtZHCH2BKPWMbVL_AhqpXRu6Gc(BurstPhotoFragment.TimeBurstChoiceModeManager.this, z, z2);
                }
            });
        }

        public /* synthetic */ void lambda$showNetworkMeteredDialog$0(boolean z, boolean z2) {
            if (z) {
                startDownload();
            } else {
                BurstPhotoFragment.this.finish();
            }
        }

        public final void showConfirmDialog(boolean z) {
            if (!z && NetworkConsider.sAgreedUsingMeteredNetwork && BaseNetworkUtils.isNetworkConnected()) {
                this.mOnConfirmed.onConfirmed(true, BaseNetworkUtils.isActiveNetworkMetered());
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("download_file_count", this.mDownloadPendings.size());
            bundle.putInt("local_file_count", this.mResults.length);
            int i = 0;
            Iterator<Downloader.DownloadTask> it = this.mDownloadPendings.iterator();
            while (it.hasNext()) {
                i += it.next().mSize;
            }
            bundle.putInt("download_file_size", i);
            bundle.putBoolean("retry_mode", z);
            DownloadConfirmDialog downloadConfirmDialog = new DownloadConfirmDialog();
            downloadConfirmDialog.setArguments(bundle);
            downloadConfirmDialog.showAllowingStateLoss(BurstPhotoFragment.this.getFragmentManager(), "confirm_dialog");
        }

        public final void startDownload() {
            Downloader downloader = this.mDownloader;
            if (downloader != null) {
                downloader.cancel();
                this.mDownloader.destroy();
            }
            Downloader downloader2 = new Downloader(this.mDownloadPendings, new DownloadProgressListener());
            this.mDownloader = downloader2;
            downloader2.start();
        }

        /* loaded from: classes2.dex */
        public class DownloadProgressListener implements Downloader.DownloadListener {
            public int total;

            public DownloadProgressListener() {
                TimeBurstChoiceModeManager.this = r1;
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onStart(List<Downloader.DownloadTask> list) {
                DefaultLogger.d("BurstPhotoFragment", "download start, %d", Integer.valueOf(list.size()));
                this.total = list.size();
                TimeBurstChoiceModeManager.this.mDownloadDialog = new DownloadDialog();
                TimeBurstChoiceModeManager.this.mDownloadDialog.show(BurstPhotoFragment.this.mActivity);
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onEnd(List<Downloader.DownloadResult> list, List<Downloader.DownloadResult> list2) {
                DefaultLogger.d("BurstPhotoFragment", "download end, success: %d, fails: %d", Integer.valueOf(list.size()), Integer.valueOf(list2.size()));
                TimeBurstChoiceModeManager.this.mDownloadDialog.dismissSafely();
                TimeBurstChoiceModeManager.this.mDownloadDialog = null;
                parseDownloadResult(list, list2);
                if (list2.isEmpty()) {
                    if (BurstPhotoFragment.this.mChoiceManager != null) {
                        TimeBurstChoiceModeManager.this.onDownloadEnd();
                    }
                } else {
                    TimeBurstChoiceModeManager.this.mDownloadPendings.clear();
                    for (Downloader.DownloadResult downloadResult : list2) {
                        downloadResult.mTask.mType = DownloadType.ORIGIN;
                        TimeBurstChoiceModeManager.this.mDownloadPendings.add(downloadResult.mTask);
                    }
                    if (!BaseNetworkUtils.isNetworkConnected()) {
                        ToastUtils.makeText(BurstPhotoFragment.this.mActivity, (int) R.string.picker_no_network_message);
                    } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
                        TimeBurstChoiceModeManager.this.showNetworkMeteredDialog();
                    } else {
                        TimeBurstChoiceModeManager.this.showConfirmDialog(true);
                    }
                }
                TimeBurstChoiceModeManager.this.mDownloader.destroy();
                TimeBurstChoiceModeManager.this.mDownloader = null;
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onCancelled(List<Downloader.DownloadResult> list, List<Downloader.DownloadResult> list2) {
                DefaultLogger.d("BurstPhotoFragment", "download cancelled, success: %d, fails: %d", Integer.valueOf(list.size()), Integer.valueOf(list2.size()));
                TimeBurstChoiceModeManager.this.mDownloadDialog.dismissSafely();
                TimeBurstChoiceModeManager.this.mDownloadDialog = null;
                parseDownloadResult(list, list2);
                TimeBurstChoiceModeManager.this.mDownloader.destroy();
                TimeBurstChoiceModeManager.this.mDownloader = null;
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onUpdate(List<Downloader.DownloadResult> list, List<Downloader.DownloadResult> list2) {
                TimeBurstChoiceModeManager.this.mDownloadDialog.updateProgress(list.size() / this.total);
            }

            public final void parseDownloadResult(List<Downloader.DownloadResult> list, List<Downloader.DownloadResult> list2) {
                for (Downloader.DownloadResult downloadResult : list) {
                    TimeBurstChoiceModeManager.this.mResults[downloadResult.mTask.mPosition] = downloadResult.mPath;
                }
            }
        }

        @Override // com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager
        public void attachFragment(Fragment fragment) {
            if (fragment instanceof DownloadConfirmDialog) {
                DownloadConfirmDialog downloadConfirmDialog = (DownloadConfirmDialog) fragment;
                downloadConfirmDialog.setPositiveListener(this.mDownloadListener);
                downloadConfirmDialog.setNegativeListener(this.mCancelDownloadListener);
            } else if (!(fragment instanceof DownloadDialog)) {
            } else {
                ((DownloadDialog) fragment).setCallBack(this.mDownloadCallback);
            }
        }

        @Override // com.miui.gallery.ui.BurstPhotoFragment.ChoiceModeManager
        public void release() {
            Future<Object> future = this.mCheckDownloadFilesFuture;
            if (future != null) {
                future.cancel();
                this.mCheckDownloadFilesFuture = null;
            }
            SaveVideoManager saveVideoManager = this.mSaveVideoManager;
            if (saveVideoManager != null) {
                saveVideoManager.onDestroy();
                this.mSaveVideoManager = null;
            }
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public View getLayout(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return LayoutInflater.from(this.mActivity).inflate(R.layout.burst_photo_page, viewGroup, false);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.ui.PhotoPagerHelper.OnImageLoadFinishListener
    public void onImageLoadFinish(String str) {
        super.onImageLoadFinish(str);
        this.mTransitionConfig.checkCoverLoad(Uri.parse(str).getPath());
    }

    public boolean onBackPressed() {
        ChoiceModeManager choiceModeManager = this.mChoiceManager;
        if (choiceModeManager != null) {
            choiceModeManager.discard();
        }
        Fragment findFragmentByTag = getChildFragmentManager().findFragmentByTag("BurstPhotoPreviewFragment");
        if (findFragmentByTag != null) {
            ((BurstPhotoPreviewFragment) findFragmentByTag).discard();
            return true;
        }
        return true;
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mTransitionConfig.release();
        ChoiceModeManager choiceModeManager = this.mChoiceManager;
        if (choiceModeManager != null) {
            choiceModeManager.release();
        }
    }

    public void onAttachDialogFragment(Fragment fragment) {
        ChoiceModeManager choiceModeManager = this.mChoiceManager;
        if (choiceModeManager != null) {
            choiceModeManager.attachFragment(fragment);
        }
    }

    /* loaded from: classes2.dex */
    public class TransitionConfig {
        public Drawable mEditCoverDrawable;
        public Drawable mEnterCoverDrawable;
        public boolean mHasTransition;
        public String mLastEditCoverPath;
        public Runnable mOnLoadTimeOut = new LoadOutTimeRunnable(this);
        public String mTransitionFilePath;

        public TransitionConfig(boolean z, String str) {
            BurstPhotoFragment.this = r3;
            this.mHasTransition = z;
            this.mTransitionFilePath = str;
            ThreadManager.getMainHandler().postDelayed(this.mOnLoadTimeOut, 1000L);
        }

        public void checkCoverLoad(String str) {
            if (str == null || this.mEnterCoverDrawable != null) {
                return;
            }
            PhotoView photoView = BurstPhotoFragment.this.mPagerHelper.getCurrentItem().getPhotoView();
            if (str.equals(this.mTransitionFilePath)) {
                if (this.mEnterCoverDrawable == null) {
                    this.mEnterCoverDrawable = BurstPhotoFragment.createDrawableInCaseOfRecycle(photoView);
                }
                ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
                startTransition();
            }
            if (!str.equals(this.mLastEditCoverPath) || this.mEditCoverDrawable != null) {
                return;
            }
            this.mEditCoverDrawable = BurstPhotoFragment.createDrawableInCaseOfRecycle(photoView);
        }

        public final void startTransition() {
            PhotoView photoView;
            PhotoPageItem currentItem = BurstPhotoFragment.this.mPagerHelper.getCurrentItem();
            if (currentItem == null || (photoView = currentItem.getPhotoView()) == null || !BurstPhotoFragment.this.isAdded()) {
                return;
            }
            ViewCompat.setTransitionName(photoView, BurstPhotoFragment.this.getString(R.string.burst_transition_image_view));
            ((BurstPhotoActivity) BurstPhotoFragment.this.getActivity()).onTransitionImageLoadFinish();
        }

        public void updateEditCover(List<Integer> list) {
            PhotoPageItem item;
            if (list == null || list.size() == 0 || (item = BurstPhotoFragment.this.mPagerHelper.getItem(list.get(0).intValue())) == null || item.getDataItem() == null || Objects.equals(item.getDataItem().getPathDisplayBetter(), this.mLastEditCoverPath)) {
                return;
            }
            this.mLastEditCoverPath = item.getDataItem().getPathDisplayBetter();
            this.mEditCoverDrawable = BurstPhotoFragment.createDrawableInCaseOfRecycle(item.getPhotoView());
        }

        public void updateEditCover(String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            this.mLastEditCoverPath = str;
        }

        public void exit(boolean z, boolean z2) {
            if (BurstPhotoFragment.this.mActivity == null) {
                return;
            }
            setActivityResult(z, (!z || !z2) ? this.mTransitionFilePath : this.mLastEditCoverPath);
            if (this.mHasTransition) {
                PhotoPageItem currentItem = BurstPhotoFragment.this.mPagerHelper.getCurrentItem();
                PhotoPageItem item = BurstPhotoFragment.this.mPagerHelper.getItem(0);
                if (item != null) {
                    ViewCompat.setTransitionName(item.getPhotoView(), null);
                }
                ViewCompat.setTransitionName(currentItem.getPhotoView(), BurstPhotoFragment.this.getString(R.string.burst_transition_image_view));
                ((BurstPhotoActivity) BurstPhotoFragment.this.mActivity).onExit(z ? this.mEditCoverDrawable : this.mEnterCoverDrawable);
                ActivityCompat.finishAfterTransition(BurstPhotoFragment.this.mActivity);
                return;
            }
            BurstPhotoFragment.this.mActivity.finish();
        }

        public final void setActivityResult(boolean z, String str) {
            Intent intent = new Intent();
            intent.putExtra("extra_photo_edit_type", "burst_photo_type");
            if (z) {
                str = renameCoverPath(str);
            }
            if (!TextUtils.isEmpty(str)) {
                intent.setData(Uri.fromFile(new File(str)));
            }
            intent.putExtra("time_burst_photo_save_result", true);
            BurstPhotoFragment.this.mActivity.setResult(-1, intent);
        }

        public final String renameCoverPath(String str) {
            return (str == null || !str.contains(BurstPhotoFragment.this.mFileFilter)) ? str : str.replace(BurstPhotoFragment.this.mFileFilter, "_");
        }

        public final void release() {
            ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
        }
    }

    public static Drawable createDrawableInCaseOfRecycle(PhotoView photoView) {
        Drawable drawable = photoView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            long currentTimeMillis = System.currentTimeMillis();
            Bitmap copyBitmap = Bitmaps.copyBitmap(((BitmapDrawable) drawable).getBitmap());
            DefaultLogger.d("BurstPhotoFragment", "copy bitmap costs: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return new BitmapDrawable(photoView.getContext().getResources(), copyBitmap);
        }
        return drawable;
    }

    /* loaded from: classes2.dex */
    public static class LoadOutTimeRunnable implements Runnable {
        public WeakReference<TransitionConfig> mRef;

        public LoadOutTimeRunnable(TransitionConfig transitionConfig) {
            this.mRef = new WeakReference<>(transitionConfig);
        }

        @Override // java.lang.Runnable
        public void run() {
            TransitionConfig transitionConfig = this.mRef.get();
            if (transitionConfig == null) {
                return;
            }
            DefaultLogger.d("BurstPhotoFragment", "load cover time out.");
            transitionConfig.startTransition();
        }
    }

    /* loaded from: classes2.dex */
    public class SaveVideoManager {
        public ComposeCallback mComposeCallback = new ComposeCallback() { // from class: com.miui.gallery.ui.BurstPhotoFragment.SaveVideoManager.3
            {
                SaveVideoManager.this = this;
            }

            @Override // com.miui.gallery.video.timeburst.ComposeCallback
            public void onError() {
                ToastUtils.makeText(BurstPhotoFragment.this.getContext(), (int) R.string.burst_save_error_msg);
                DefaultLogger.d("BurstPhotoFragment", "Burst export video error");
                if (SaveVideoManager.this.mSaveVideoDialog != null) {
                    SaveVideoManager.this.mSaveVideoDialog.dismissSafely();
                }
            }

            @Override // com.miui.gallery.video.timeburst.ComposeCallback
            public void onFinish(String str) {
                DefaultLogger.d("BurstPhotoFragment", "Burst export video finish");
                if (SaveVideoManager.this.mSaveVideoDialog != null) {
                    SaveVideoManager.this.mSaveVideoDialog.dismissSafely();
                }
                if (!TextUtils.isEmpty(str)) {
                    SaveVideoManager saveVideoManager = SaveVideoManager.this;
                    if (BurstPhotoFragment.this.mActivity == null) {
                        return;
                    }
                    saveVideoManager.afterSave(str);
                }
            }

            @Override // com.miui.gallery.video.timeburst.ComposeCallback
            public void onCancel() {
                DefaultLogger.d("BurstPhotoFragment", "Burst export video cancel");
                if (SaveVideoManager.this.mSaveVideoDialog != null) {
                    SaveVideoManager.this.mSaveVideoDialog.dismissSafely();
                }
            }

            @Override // com.miui.gallery.video.timeburst.ComposeCallback
            public void onProgress(int i) {
                if (SaveVideoManager.this.mSaveVideoDialog != null) {
                    SaveVideoManager.this.mSaveVideoDialog.setProgress(i);
                }
            }
        };
        public int mDegree;
        public int mHeight;
        public boolean mIsSecret;
        public String[] mResults;
        public DoubleCheckProgressDialogFragment mSaveVideoDialog;
        public IBurstPhotoComposer mVideoComposer;
        public int mWidth;

        public SaveVideoManager(BaseDataItem baseDataItem, String[] strArr) {
            BurstPhotoFragment.this = r1;
            this.mWidth = baseDataItem.getWidth();
            this.mHeight = baseDataItem.getHeight();
            this.mDegree = ExifUtil.exifOrientationToDegrees(baseDataItem.getOrientation());
            this.mIsSecret = baseDataItem.isSecret();
            this.mResults = strArr;
        }

        public void showSaveVideoDialog() {
            if (this.mWidth < 0 || this.mHeight < 0) {
                return;
            }
            final String string = BurstPhotoFragment.this.getString(R.string.burst_save_video_choice_4k, 4);
            final String string2 = BurstPhotoFragment.this.getString(R.string.burst_save_video_choice_1080, 1080);
            final String string3 = BurstPhotoFragment.this.getString(R.string.burst_save_video_choice_720, 720);
            ArrayList arrayList = new ArrayList();
            final Map<ResolutionLevel, Resolution> supportResolutionList = Utils.getSupportResolutionList("video/avc", this.mWidth, this.mHeight);
            if (!BaseMiscUtil.isValid(supportResolutionList)) {
                return;
            }
            if (supportResolutionList.get(ResolutionLevel.K4) != null) {
                arrayList.add(string);
            }
            if (supportResolutionList.get(ResolutionLevel.P1080) != null) {
                arrayList.add(string2);
            }
            if (supportResolutionList.get(ResolutionLevel.P720) != null) {
                arrayList.add(string3);
            }
            TextListDialogFragment newInstance = TextListDialogFragment.newInstance(arrayList, BurstPhotoFragment.this.getString(R.string.burst_save_video_title));
            newInstance.setOnItemSelectedListener(new TextListDialogFragment.OnItemSelectedListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.SaveVideoManager.1
                {
                    SaveVideoManager.this = this;
                }

                /* JADX WARN: Removed duplicated region for block: B:39:0x0040 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:40:0x0041  */
                @Override // com.miui.gallery.ui.TextListDialogFragment.OnItemSelectedListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void onItemSelected(java.lang.String r10, int r11) {
                    /*
                        Method dump skipped, instructions count: 254
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.BurstPhotoFragment.SaveVideoManager.AnonymousClass1.onItemSelected(java.lang.String, int):void");
                }
            });
            newInstance.show(BurstPhotoFragment.this.getFragmentManager(), "SELECT_RESOLUTION_DIALOG");
        }

        public final void setActivityResult(String str) {
            Intent intent = new Intent();
            intent.setData(Uri.fromFile(new File(str)));
            intent.putExtra("extra_photo_edit_type", "burst_photo_type");
            intent.putExtra("time_burst_photo_save_result", true);
            BurstPhotoFragment.this.mActivity.setResult(-1, intent);
            BurstPhotoFragment.this.finish();
        }

        public final void createSaveVideoDialog() {
            if (this.mSaveVideoDialog == null) {
                DoubleCheckProgressDialogFragment doubleCheckProgressDialogFragment = new DoubleCheckProgressDialogFragment();
                this.mSaveVideoDialog = doubleCheckProgressDialogFragment;
                doubleCheckProgressDialogFragment.setStyle(1);
                this.mSaveVideoDialog.setMessage(BurstPhotoFragment.this.getContext().getString(R.string.burst_save_video_progerss));
                this.mSaveVideoDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.BurstPhotoFragment.SaveVideoManager.2
                    {
                        SaveVideoManager.this = this;
                    }

                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        SaveVideoManager.this.mVideoComposer.cancel();
                    }
                });
            }
        }

        public final void afterSave(String str) {
            DocumentFile documentFile;
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("BurstPhotoFragment", "afterSave");
            if (this.mIsSecret) {
                if (SaveToCloudUtil.saveToCloudDB(BurstPhotoFragment.this.mActivity, new SaveParams.Builder().setSaveFile(new File(str)).setAlbumId(-1000L).setLocalFlag(8).setCredible(true).build()).getMediaId() <= 0 && (documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                    documentFile.delete();
                }
            } else {
                ScannerEngine.getInstance().scanFile(BurstPhotoFragment.this.mActivity, str, 13);
                DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                if (documentFile2 != null) {
                    StorageSolutionProvider.get().apply(documentFile2);
                }
            }
            setActivityResult(str);
        }

        public void onDestroy() {
            IBurstPhotoComposer iBurstPhotoComposer = this.mVideoComposer;
            if (iBurstPhotoComposer != null) {
                iBurstPhotoComposer.cancel();
                this.mVideoComposer.release();
                this.mVideoComposer = null;
            }
            DoubleCheckProgressDialogFragment doubleCheckProgressDialogFragment = this.mSaveVideoDialog;
            if (doubleCheckProgressDialogFragment != null) {
                doubleCheckProgressDialogFragment.dismissSafely();
                this.mSaveVideoDialog = null;
            }
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        PhotoPageAdapter.ChoiceMode choiceMode;
        super.onSaveInstanceState(bundle);
        if (this.mHasDataLoaded) {
            bundle.putInt("current_position", this.mPager.getCurrentItem());
            bundle.putBoolean("has_data_loaded", this.mHasDataLoaded);
            ChoiceModeManager choiceModeManager = this.mChoiceManager;
            if (choiceModeManager == null || (choiceMode = choiceModeManager.mBurstChoiceMode) == null) {
                return;
            }
            bundle.putIntegerArrayList("selected_items", (ArrayList) choiceMode.getSelectItems());
        }
    }
}
