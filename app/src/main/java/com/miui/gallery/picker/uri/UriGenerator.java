package com.miui.gallery.picker.uri;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.LongSparseArray;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.picker.uri.Downloader;
import com.miui.gallery.picker.uri.OriginUrlRequestor;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class UriGenerator {
    public static final String[] CHECKED_ITEM_PROJECTION = {j.c, "sha1", "serverType", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "exifImageLength", "exifImageWidth"};
    public WeakReference<FragmentActivity> mActivityWeakReference;
    public List<CheckableAdapter.CheckedItem> mCheckedItems;
    public ArrayList<Downloader.DownloadTask> mDownloadPendings;
    public PrepareProgressDialog mDownloadProgressDialog;
    public Downloader mDownloader;
    public Picker.ImageType mImageType;
    public ArrayList<OriginUrlRequestor.OriginUrlRequestTask> mOriginRequestPendings;
    public PrepareProgressDialog mOriginRequestProgressDialog;
    public OriginUrlRequestor mOriginUrlRequestor;
    public Uri[] mResults;
    public String[] mSha1s;
    public UriGenerateListener mUriGenerateListener;
    public AsyncTask<Void, Void, Void> mDataBaseQueryTask = new AsyncTask<Void, Void, Void>() { // from class: com.miui.gallery.picker.uri.UriGenerator.1
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            UriGenerator.this.queryCheckedItems();
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r1) {
            UriGenerator.this.getUrisFromCheckedItems();
        }
    };
    public NetworkConsider.OnConfirmed mOnConfirmed = new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.picker.uri.UriGenerator.3
        @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
        public void onConfirmed(boolean z, boolean z2) {
            if (!z) {
                if (UriGenerator.this.mDownloader == null) {
                    return;
                }
                UriGenerator.this.mDownloader.cancel();
            } else if (BaseNetworkUtils.isNetworkConnected()) {
                UriGenerator.this.startDownload();
            } else {
                ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.picker_no_network_message);
                UriGenerator.this.showDownloadConfirmDialog(true);
            }
        }
    };
    public DialogInterface.OnClickListener mDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.picker.uri.UriGenerator.4
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (BaseNetworkUtils.isNetworkConnected()) {
                UriGenerator.this.startDownload();
                return;
            }
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.picker_no_network_message);
            UriGenerator.this.showDownloadConfirmDialog(true);
        }
    };
    public DialogInterface.OnClickListener mCancelDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.picker.uri.UriGenerator.5
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (UriGenerator.this.mDownloader != null) {
                UriGenerator.this.mDownloader.cancel();
            }
        }
    };
    public DialogInterface.OnClickListener mContinueDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.picker.uri.UriGenerator.6
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            FragmentActivity fragmentActivity = (FragmentActivity) UriGenerator.this.mActivityWeakReference.get();
            if (UriGenerator.this.mDownloadProgressDialog == null || fragmentActivity == null) {
                return;
            }
            UriGenerator.this.mDownloadProgressDialog.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "prepare_progress_dialog");
        }
    };
    public DialogInterface.OnCancelListener mCancelOriginInfoListener = new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.picker.uri.UriGenerator.7
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            if (UriGenerator.this.mOriginUrlRequestor != null) {
                UriGenerator.this.mOriginUrlRequestor.cancel();
            }
        }
    };
    public DialogInterface.OnCancelListener mCancelConfirmListener = new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.picker.uri.UriGenerator.8
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            int remainSize = UriGenerator.this.mDownloader == null ? 0 : UriGenerator.this.mDownloader.getRemainSize();
            FragmentActivity fragmentActivity = (FragmentActivity) UriGenerator.this.mActivityWeakReference.get();
            if (remainSize <= 0 || fragmentActivity == null) {
                return;
            }
            DownloadCancelDialog downloadCancelDialog = new DownloadCancelDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("remaining_count", remainSize);
            downloadCancelDialog.setCancelListener(UriGenerator.this.mCancelDownloadListener);
            downloadCancelDialog.setContinueListener(UriGenerator.this.mContinueDownloadListener);
            downloadCancelDialog.setArguments(bundle);
            downloadCancelDialog.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "cancel_dialog");
        }
    };

    /* loaded from: classes2.dex */
    public interface UriGenerateListener {
        void onFail();

        void onSuccess(Uri[] uriArr, List<OriginUrlRequestor.OriginInfo> list);
    }

    public final void queryCheckedItems() {
        if (BaseMiscUtil.isValid(this.mCheckedItems)) {
            final LongSparseArray longSparseArray = new LongSparseArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.mCheckedItems.size(); i++) {
                CheckableAdapter.CheckedItem checkedItem = this.mCheckedItems.get(i);
                longSparseArray.put(checkedItem.getId(), checkedItem);
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(checkedItem.getId());
            }
            FragmentActivity fragmentActivity = this.mActivityWeakReference.get();
            if (fragmentActivity == null) {
                return;
            }
            SafeDBUtil.safeQuery(fragmentActivity, UriUtil.appendGroupBy(GalleryContract.Media.URI_PICKER, "sha1", null), CHECKED_ITEM_PROJECTION, String.format("_id IN (%s) AND (localGroupId!=-1000) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", sb.toString()), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.picker.uri.UriGenerator.2
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public Void mo1808handle(Cursor cursor) {
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            CheckableAdapter.CheckedItem checkedItem2 = (CheckableAdapter.CheckedItem) longSparseArray.get(cursor.getLong(0));
                            if (checkedItem2 != null) {
                                checkedItem2.setServerType(cursor.getInt(2));
                                checkedItem2.setSize(cursor.getLong(3));
                                checkedItem2.setWidth(cursor.getInt(5));
                                checkedItem2.setHeight(cursor.getInt(4));
                            }
                        }
                        return null;
                    }
                    return null;
                }
            });
        }
    }

    public UriGenerator(FragmentActivity fragmentActivity, List<CheckableAdapter.CheckedItem> list, Picker.ImageType imageType, UriGenerateListener uriGenerateListener) {
        this.mActivityWeakReference = new WeakReference<>(fragmentActivity);
        this.mCheckedItems = list;
        this.mImageType = imageType;
        this.mUriGenerateListener = uriGenerateListener;
    }

    public void generate() {
        Picker.ImageType imageType;
        FragmentActivity fragmentActivity = this.mActivityWeakReference.get();
        if (fragmentActivity == null || fragmentActivity.isDestroyed() || !BaseMiscUtil.isValid(this.mCheckedItems) || (imageType = this.mImageType) == null || this.mUriGenerateListener == null) {
            UriGenerateListener uriGenerateListener = this.mUriGenerateListener;
            if (uriGenerateListener == null) {
                return;
            }
            uriGenerateListener.onFail();
        } else if (imageType == Picker.ImageType.ORIGIN || imageType == Picker.ImageType.ORIGIN_OR_DOWNLOAD_INFO) {
            this.mDataBaseQueryTask.execute(new Void[0]);
        } else {
            getUrisFromCheckedItems();
        }
    }

    public final void getUrisFromCheckedItems() {
        String quantityString;
        DownloadType downloadType;
        this.mResults = new Uri[this.mCheckedItems.size()];
        this.mSha1s = new String[this.mCheckedItems.size()];
        this.mDownloadPendings = new ArrayList<>();
        this.mOriginRequestPendings = new ArrayList<>();
        for (int i = 0; i < this.mCheckedItems.size(); i++) {
            CheckableAdapter.CheckedItem checkedItem = this.mCheckedItems.get(i);
            this.mSha1s[i] = checkedItem.getSha1();
            String originFile = checkedItem.getOriginFile();
            if (!TextUtils.isEmpty(originFile)) {
                File file = new File(originFile);
                if (file.exists()) {
                    this.mResults[i] = Uri.fromFile(file);
                }
            }
            Picker.ImageType imageType = this.mImageType;
            if (imageType == Picker.ImageType.ORIGIN) {
                if (BaseNetworkUtils.isActiveNetworkMetered()) {
                    downloadType = DownloadType.ORIGIN_FORCE;
                } else {
                    downloadType = DownloadType.ORIGIN;
                }
                this.mDownloadPendings.add(new Downloader.DownloadTask(CloudUriAdapter.getDownloadUri(checkedItem.getId()), downloadType, (int) checkedItem.getSize(), i));
            } else {
                if (imageType == Picker.ImageType.ORIGIN_OR_DOWNLOAD_INFO) {
                    this.mOriginRequestPendings.add(new OriginUrlRequestor.OriginUrlRequestTask(i, CloudUriAdapter.getDownloadUri(checkedItem.getId()), checkedItem.getHeight(), checkedItem.getWidth(), checkedItem.getServerType()));
                }
                String thumbnailFile = checkedItem.getThumbnailFile();
                if (!TextUtils.isEmpty(thumbnailFile)) {
                    File file2 = new File(thumbnailFile);
                    if (file2.exists()) {
                        this.mResults[i] = Uri.fromFile(file2);
                    }
                }
                this.mDownloadPendings.add(new Downloader.DownloadTask(CloudUriAdapter.getDownloadUri(checkedItem.getId()), DownloadType.THUMBNAIL, 0, i));
            }
        }
        DefaultLogger.d("UriGenerator", "picked file: %d, pending: %d", Integer.valueOf(this.mResults.length), Integer.valueOf(this.mDownloadPendings.size()));
        FragmentActivity fragmentActivity = this.mActivityWeakReference.get();
        if (this.mDownloadPendings.isEmpty()) {
            deliverResult(null);
        } else if (!SyncUtil.existXiaomiAccount(fragmentActivity)) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), GalleryApp.sGetAndroidContext().getResources().getQuantityString(R.plurals.picker_skip_unavailable_images, this.mDownloadPendings.size(), Integer.valueOf(this.mDownloadPendings.size())));
            if (this.mResults.length == 0) {
                this.mUriGenerateListener.onFail();
            } else {
                deliverResult(null);
            }
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            showDownloadConfirmDialog(false);
        } else if (BaseNetworkUtils.isNetworkConnected()) {
            if (this.mResults.length == 0) {
                quantityString = GalleryApp.sGetAndroidContext().getResources().getQuantityString(R.plurals.picker_all_image_will_download, this.mDownloadPendings.size());
            } else {
                quantityString = GalleryApp.sGetAndroidContext().getResources().getQuantityString(R.plurals.picker_file_will_download, this.mDownloadPendings.size(), Integer.valueOf(this.mDownloadPendings.size()));
            }
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), quantityString);
            startDownload();
        } else {
            showDownloadConfirmDialog(true);
        }
    }

    public final void deliverResult(List<OriginUrlRequestor.OriginInfo> list) {
        if (this.mResults.length == 0) {
            this.mUriGenerateListener.onFail();
        } else if (this.mImageType == Picker.ImageType.ORIGIN_OR_DOWNLOAD_INFO && !BaseMiscUtil.isValid(list)) {
            OriginUrlRequestor originUrlRequestor = new OriginUrlRequestor(this.mOriginRequestPendings, new OriginUrlRequestProgressListener());
            this.mOriginUrlRequestor = originUrlRequestor;
            originUrlRequestor.start(this.mResults, this.mSha1s);
        } else {
            this.mUriGenerateListener.onSuccess(this.mResults, list);
        }
    }

    public final void showDownloadConfirmDialog(boolean z) {
        FragmentActivity fragmentActivity = this.mActivityWeakReference.get();
        if (fragmentActivity != null) {
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
            downloadConfirmDialog.setPositiveListener(this.mDownloadListener);
            downloadConfirmDialog.setNegativeListener(this.mCancelDownloadListener);
            downloadConfirmDialog.setArguments(bundle);
            downloadConfirmDialog.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "confirm_dialog");
        }
    }

    public final void startDownload() {
        Downloader downloader = this.mDownloader;
        if (downloader != null) {
            downloader.cancel();
            this.mDownloader.destroy();
        }
        Downloader downloader2 = new Downloader(this.mDownloadPendings, new Downloader.DownloadListener() { // from class: com.miui.gallery.picker.uri.UriGenerator.9
            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onStart(List<Downloader.DownloadTask> list) {
                DefaultLogger.d("UriGenerator", "download start, %d", Integer.valueOf(list.size()));
                FragmentActivity fragmentActivity = (FragmentActivity) UriGenerator.this.mActivityWeakReference.get();
                if (fragmentActivity != null) {
                    UriGenerator.this.mDownloadProgressDialog = new PrepareProgressDialog();
                    UriGenerator.this.mDownloadProgressDialog.setMax(list.size());
                    UriGenerator.this.mDownloadProgressDialog.setStage(0);
                    UriGenerator.this.mDownloadProgressDialog.setCancelListener(UriGenerator.this.mCancelConfirmListener);
                    UriGenerator.this.mDownloadProgressDialog.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "prepare_progress_dialog");
                }
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onUpdate(List<Downloader.DownloadResult> list, List<Downloader.DownloadResult> list2) {
                UriGenerator.this.mDownloadProgressDialog.updateProgress(list.size() + list2.size());
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onEnd(List<Downloader.DownloadResult> list, List<Downloader.DownloadResult> list2) {
                DefaultLogger.d("UriGenerator", "download end, success: %d, fails: %d", Integer.valueOf(list.size()), Integer.valueOf(list2.size()));
                if (UriGenerator.this.mDownloadProgressDialog != null) {
                    Dialog dialog = UriGenerator.this.mDownloadProgressDialog.getDialog();
                    if (dialog != null && dialog.isShowing()) {
                        UriGenerator.this.mDownloadProgressDialog.dismissAllowingStateLoss();
                    }
                    FragmentActivity fragmentActivity = (FragmentActivity) UriGenerator.this.mActivityWeakReference.get();
                    if (fragmentActivity != null) {
                        Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag("cancel_dialog");
                        if (findFragmentByTag instanceof DownloadCancelDialog) {
                            ((DownloadCancelDialog) findFragmentByTag).dismiss();
                        }
                    }
                    UriGenerator.this.mDownloadProgressDialog = null;
                }
                parseDownloadResult(list, list2);
                if (list2.isEmpty()) {
                    UriGenerator.this.deliverResult(null);
                } else {
                    UriGenerator.this.showDownloadConfirmDialog(true);
                }
                UriGenerator.this.mDownloader.destroy();
                UriGenerator.this.mDownloader = null;
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onCancelled(List<Downloader.DownloadResult> list, List<Downloader.DownloadResult> list2) {
                DefaultLogger.d("UriGenerator", "download cancelled, success: %d, fails: %d", Integer.valueOf(list.size()), Integer.valueOf(list2.size()));
                if (UriGenerator.this.mDownloadProgressDialog != null) {
                    Dialog dialog = UriGenerator.this.mDownloadProgressDialog.getDialog();
                    if (dialog != null && dialog.isShowing()) {
                        UriGenerator.this.mDownloadProgressDialog.dismissAllowingStateLoss();
                    }
                    UriGenerator.this.mDownloadProgressDialog = null;
                }
                parseDownloadResult(list, list2);
                UriGenerator.this.mDownloader.destroy();
                UriGenerator.this.mDownloader = null;
            }

            public final void parseDownloadResult(List<Downloader.DownloadResult> list, List<Downloader.DownloadResult> list2) {
                for (Downloader.DownloadResult downloadResult : list) {
                    if (downloadResult != null && !TextUtils.isEmpty(downloadResult.mPath)) {
                        File file = new File(downloadResult.mPath);
                        if (file.exists()) {
                            UriGenerator.this.mResults[downloadResult.mTask.mPosition] = Uri.fromFile(file);
                        }
                    }
                    list2.add(downloadResult);
                }
                UriGenerator.this.mDownloadPendings.clear();
                for (Downloader.DownloadResult downloadResult2 : list2) {
                    UriGenerator.this.mDownloadPendings.add(downloadResult2.mTask);
                }
            }
        });
        this.mDownloader = downloader2;
        downloader2.start();
    }

    /* loaded from: classes2.dex */
    public class OriginUrlRequestProgressListener implements OriginUrlRequestor.ProgressListener {
        public OriginUrlRequestProgressListener() {
        }

        @Override // com.miui.gallery.picker.uri.OriginUrlRequestor.ProgressListener
        public void onStart(int i) {
            FragmentActivity fragmentActivity = (FragmentActivity) UriGenerator.this.mActivityWeakReference.get();
            if (fragmentActivity != null) {
                UriGenerator.this.mOriginRequestProgressDialog = new PrepareProgressDialog();
                UriGenerator.this.mOriginRequestProgressDialog.setMax(i);
                UriGenerator.this.mOriginRequestProgressDialog.setStage(1);
                UriGenerator.this.mOriginRequestProgressDialog.setCancelListener(UriGenerator.this.mCancelOriginInfoListener);
                UriGenerator.this.mOriginRequestProgressDialog.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "prepare_progress_dialog");
            }
        }

        @Override // com.miui.gallery.picker.uri.OriginUrlRequestor.ProgressListener
        public void onEnd(ArrayList<OriginUrlRequestor.OriginInfo> arrayList, boolean z) {
            finish();
            if (BaseMiscUtil.isValid(arrayList) && z) {
                UriGenerator.this.deliverResult(arrayList);
                return;
            }
            DefaultLogger.e("UriGenerator", "selected images OriginInfo generate error");
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.picker_origin_request_progress_fail);
            UriGenerator.this.mUriGenerateListener.onFail();
        }

        @Override // com.miui.gallery.picker.uri.OriginUrlRequestor.ProgressListener
        public void onUpdate(int i) {
            if (UriGenerator.this.mOriginRequestProgressDialog != null) {
                UriGenerator.this.mOriginRequestProgressDialog.updateProgress(i);
            }
        }

        @Override // com.miui.gallery.picker.uri.OriginUrlRequestor.ProgressListener
        public void onCancelled() {
            finish();
        }

        public final void finish() {
            if (UriGenerator.this.mOriginRequestProgressDialog != null) {
                Dialog dialog = UriGenerator.this.mOriginRequestProgressDialog.getDialog();
                if (dialog != null && dialog.isShowing()) {
                    UriGenerator.this.mOriginRequestProgressDialog.dismissAllowingStateLoss();
                }
                UriGenerator.this.mOriginRequestProgressDialog = null;
            }
            if (UriGenerator.this.mOriginRequestPendings != null) {
                UriGenerator.this.mOriginRequestPendings.clear();
            }
            if (UriGenerator.this.mOriginUrlRequestor != null) {
                UriGenerator.this.mOriginUrlRequestor.destroy();
                UriGenerator.this.mOriginUrlRequestor = null;
            }
        }
    }
}
