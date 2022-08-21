package com.miui.gallery.util;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentManager;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.ui.DownloadFragment;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BulkDownloadHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

/* loaded from: classes2.dex */
public class CheckDownloadOriginHelper {
    public long mAlbumId;
    public Context mContext;
    public FragmentManager mFragmentManger;
    public CheckDownloadOriginListener mListener;
    public long[] mMediaIds;

    /* loaded from: classes2.dex */
    public interface CheckDownloadOriginListener {
        void onCanceled();

        void onComplete();

        void onStartDownload();
    }

    public static /* synthetic */ void $r8$lambda$fIMSo9lTAKoZt3jCCCSZEr85RZI(CheckDownloadOriginHelper checkDownloadOriginHelper, List list, boolean z, boolean z2) {
        checkDownloadOriginHelper.lambda$doDownloadOrigin$0(list, z, z2);
    }

    public CheckDownloadOriginHelper(Context context, FragmentManager fragmentManager, long j, long[] jArr) {
        this.mContext = context;
        this.mFragmentManger = fragmentManager;
        if (j == 2147483645) {
            this.mAlbumId = AlbumCacheManager.getInstance().getScreenshotsAlbumId();
        } else {
            this.mAlbumId = j;
        }
        this.mMediaIds = jArr;
    }

    public void start() {
        DefaultLogger.d("CheckDownloadOriginHelper", "doCheckOrigin");
        new CheckOriginTask(this.mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void setListener(CheckDownloadOriginListener checkDownloadOriginListener) {
        this.mListener = checkDownloadOriginListener;
    }

    public final void doDownloadOrigin(final List<MediaItem> list, boolean z) {
        DefaultLogger.d("CheckDownloadOriginHelper", "doDownloadOrigin %s", Integer.valueOf(list == null ? 0 : list.size()));
        if (BaseNetworkUtils.isActiveNetworkMetered() && !z) {
            NetworkConsider.consider(this.mContext, Boolean.FALSE, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.util.CheckDownloadOriginHelper$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z2, boolean z3) {
                    CheckDownloadOriginHelper.$r8$lambda$fIMSo9lTAKoZt3jCCCSZEr85RZI(CheckDownloadOriginHelper.this, list, z2, z3);
                }
            });
            return;
        }
        ArrayList<BulkDownloadHelper.BulkDownloadItem> arrayList = new ArrayList<>();
        for (MediaItem mediaItem : list) {
            arrayList.add(new BulkDownloadHelper.BulkDownloadItem(CloudUriAdapter.getDownloadUri(mediaItem.mId), DownloadType.ORIGIN_FORCE, mediaItem.mSize));
        }
        startDownloadOrigin(arrayList);
    }

    public /* synthetic */ void lambda$doDownloadOrigin$0(List list, boolean z, boolean z2) {
        if (z) {
            doDownloadOrigin(list, true);
            return;
        }
        CheckDownloadOriginListener checkDownloadOriginListener = this.mListener;
        if (checkDownloadOriginListener == null) {
            return;
        }
        checkDownloadOriginListener.onCanceled();
    }

    public final void startDownloadOrigin(ArrayList<BulkDownloadHelper.BulkDownloadItem> arrayList) {
        DefaultLogger.d("CheckDownloadOriginHelper", "startDownloadOrigin %s", Integer.valueOf(arrayList == null ? 0 : arrayList.size()));
        DownloadFragment.OnDownloadListener onDownloadListener = new DownloadFragment.OnDownloadListener() { // from class: com.miui.gallery.util.CheckDownloadOriginHelper.1
            {
                CheckDownloadOriginHelper.this = this;
            }

            @Override // com.miui.gallery.ui.DownloadFragment.OnDownloadListener
            public void onDownloadComplete(List<BulkDownloadHelper.BulkDownloadItem> list, List<BulkDownloadHelper.BulkDownloadItem> list2) {
                DefaultLogger.d("CheckDownloadOriginHelper", "onDownloadComplete fails: %s", Integer.valueOf(list2 == null ? 0 : list2.size()));
                if (list2 == null || list2.isEmpty()) {
                    if (CheckDownloadOriginHelper.this.mListener == null) {
                        return;
                    }
                    CheckDownloadOriginHelper.this.mListener.onComplete();
                    return;
                }
                final ArrayList arrayList2 = new ArrayList(list2);
                DialogUtil.showInfoDialog(CheckDownloadOriginHelper.this.mContext, false, CheckDownloadOriginHelper.this.mContext.getString(R.string.download_retry_message), CheckDownloadOriginHelper.this.mContext.getString(R.string.download_retry_title), (int) R.string.download_retry_text, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.util.CheckDownloadOriginHelper.1.1
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CheckDownloadOriginHelper.this.startDownloadOrigin(arrayList2);
                    }
                }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.util.CheckDownloadOriginHelper.1.2
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (CheckDownloadOriginHelper.this.mListener != null) {
                            CheckDownloadOriginHelper.this.mListener.onCanceled();
                        }
                    }
                });
            }

            @Override // com.miui.gallery.ui.DownloadFragment.OnDownloadListener
            public void onCanceled() {
                DefaultLogger.d("CheckDownloadOriginHelper", "download canceled");
                if (CheckDownloadOriginHelper.this.mListener != null) {
                    CheckDownloadOriginHelper.this.mListener.onCanceled();
                }
            }
        };
        DownloadFragment newInstance = DownloadFragment.newInstance(arrayList);
        newInstance.setOnDownloadListener(onDownloadListener);
        newInstance.showAllowingStateLoss(this.mFragmentManger, "DownloadFragment");
    }

    /* loaded from: classes2.dex */
    public class CheckOriginTask extends AsyncTask<Void, Void, List<MediaItem>> {
        public final String[] PROJECTION = {j.c, MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "localFile", "serverTag", "serverId", "serverStatus"};
        public Context mContext;

        public CheckOriginTask(Context context) {
            CheckDownloadOriginHelper.this = r7;
            this.mContext = context;
        }

        @Override // android.os.AsyncTask
        public List<MediaItem> doInBackground(Void... voidArr) {
            JSONArray jSONArray = null;
            if (!isLocalAlbum()) {
                DefaultLogger.d("CheckDownloadOriginHelper", "album is not local");
                return null;
            }
            List<MediaItem> queryMediaItemByIds = queryMediaItemByIds();
            if (!BaseMiscUtil.isValid(queryMediaItemByIds)) {
                return null;
            }
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CheckDownloadOriginHelper", "CheckOriginTask");
            ArrayList arrayList = null;
            for (MediaItem mediaItem : queryMediaItemByIds) {
                if ("recovery".equalsIgnoreCase(mediaItem.mServerStatus)) {
                    if (jSONArray == null) {
                        jSONArray = new JSONArray();
                    }
                    jSONArray.put(new TrashUtils.RequestItemInfo(mediaItem.mServerId, mediaItem.mServerTag).toJSON());
                }
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(mediaItem.mPath, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                if (documentFile == null || !documentFile.exists()) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(mediaItem);
                }
            }
            if (jSONArray != null) {
                TrashUtils.doRecoveryRequest(jSONArray, this.mContext);
            }
            return arrayList;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(List<MediaItem> list) {
            if (BaseMiscUtil.isValid(list)) {
                if (CheckDownloadOriginHelper.this.mListener != null) {
                    CheckDownloadOriginHelper.this.mListener.onStartDownload();
                }
                CheckDownloadOriginHelper.this.doDownloadOrigin(list, false);
                return;
            }
            DefaultLogger.d("CheckDownloadOriginHelper", "no item to download");
            if (CheckDownloadOriginHelper.this.mListener == null) {
                return;
            }
            CheckDownloadOriginHelper.this.mListener.onComplete();
        }

        public final boolean isLocalAlbum() {
            Integer num = (Integer) SafeDBUtil.safeQuery(this.mContext, GalleryContract.Album.URI, new String[0], "_id = ? AND attributes&1 =0 ", new String[]{String.valueOf(CheckDownloadOriginHelper.this.mAlbumId)}, (String) null, new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.util.CheckDownloadOriginHelper.CheckOriginTask.1
                {
                    CheckOriginTask.this = this;
                }

                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public Integer mo1808handle(Cursor cursor) {
                    return Integer.valueOf(cursor == null ? 0 : cursor.getCount());
                }
            });
            return num != null && num.intValue() > 0;
        }

        public final List<MediaItem> queryMediaItemByIds() {
            StringBuilder sb = new StringBuilder();
            sb.append("_id IN (");
            sb.append(TextUtils.join(",", MiscUtil.arrayToList(CheckDownloadOriginHelper.this.mMediaIds)));
            sb.append(") AND ");
            sb.append("localFlag");
            sb.append("=");
            boolean z = false;
            sb.append(0);
            String sb2 = sb.toString();
            long[] jArr = CheckDownloadOriginHelper.this.mMediaIds;
            int length = jArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (ShareMediaManager.isOtherShareMediaId(jArr[i])) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            return (List) SafeDBUtil.safeQuery(this.mContext, z ? GalleryContract.Media.URI_ALL : GalleryContract.Media.URI, this.PROJECTION, sb2, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<MediaItem>>() { // from class: com.miui.gallery.util.CheckDownloadOriginHelper.CheckOriginTask.2
                {
                    CheckOriginTask.this = this;
                }

                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public List<MediaItem> mo1808handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    ArrayList arrayList = null;
                    do {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        MediaItem mediaItem = new MediaItem();
                        mediaItem.mId = cursor.getLong(0);
                        mediaItem.mSize = cursor.getLong(1);
                        mediaItem.mPath = cursor.getString(2);
                        mediaItem.mServerTag = cursor.getLong(3);
                        mediaItem.mServerId = cursor.getString(4);
                        mediaItem.mServerStatus = cursor.getString(5);
                        arrayList.add(mediaItem);
                    } while (cursor.moveToNext());
                    return arrayList;
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public static class MediaItem {
        public long mId;
        public String mPath;
        public String mServerId;
        public String mServerStatus;
        public long mServerTag;
        public long mSize;

        public MediaItem() {
        }
    }
}
