package com.miui.mediaeditor.api;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.MemoryFile;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.model.MediaSceneTagManager;
import com.miui.gallery.editor.photo.screen.helper.ScreenEditorHelper;
import com.miui.gallery.editor.photo.utils.MemoryFileUtils;
import com.miui.gallery.glide.load.model.PreloadModel;
import com.miui.gallery.glide.util.GlideLoadingUtils;
import com.miui.gallery.model.SecretInfo;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class GalleryProviderForMediaEditor extends ContentProvider {
    public ImageCacheManager mCacheManager;

    public static /* synthetic */ void $r8$lambda$SZBLOMvfgJ7rJcZlu6Skp2VsWDk(ResultReceiver resultReceiver, long[] jArr) {
        resultReceiver.send(100, null);
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        this.mCacheManager = new ImageCacheManager();
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        long currentTimeMillis = System.currentTimeMillis();
        Bundle bundle2 = null;
        if (TextUtils.equals(getCallingPackage(), "com.miui.mediaeditor") || TextUtils.equals(getCallingPackage(), "com.miui.screenshot")) {
            char c = 65535;
            try {
                switch (str.hashCode()) {
                    case -2029176099:
                        if (str.equals("method_add_to_favorites_by_path")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1777258089:
                        if (str.equals("method_get_image_cache_bitmap")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -905785453:
                        if (str.equals("method_is_device_support_video_analytic")) {
                            c = 14;
                            break;
                        }
                        break;
                    case -800189882:
                        if (str.equals("method_release_image_cache_bitmap")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -508119874:
                        if (str.equals("method_gallery_scanner_scan_single")) {
                            c = '\f';
                            break;
                        }
                        break;
                    case -470904819:
                        if (str.equals("method_delete_cloud_by_path")) {
                            c = 7;
                            break;
                        }
                        break;
                    case -313696746:
                        if (str.equals("method_get_secret_info")) {
                            c = 5;
                            break;
                        }
                        break;
                    case -53239445:
                        if (str.equals("method_get_scene_tag_list_by_path")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -52386973:
                        if (str.equals("method_parse_flags_for_video")) {
                            c = 11;
                            break;
                        }
                        break;
                    case 234823916:
                        if (str.equals("method_delete_file_local_and_cloud")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case 247517746:
                        if (str.equals("method_get_scene_tag_list_by_path_in_bacth")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 962539330:
                        if (str.equals("method_update_local_db_not_show_in_recycle_bin")) {
                            c = '\n';
                            break;
                        }
                        break;
                    case 1575231383:
                        if (str.equals("method_delete_by_path")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 1690178826:
                        if (str.equals("method_gallery_scanner_clean_single")) {
                            c = '\r';
                            break;
                        }
                        break;
                    case 2037728232:
                        if (str.equals("method_save_to_cloud_db")) {
                            c = '\t';
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        bundle2 = addToFavoritesByPath(bundle);
                        break;
                    case 1:
                        bundle2 = getSceneTagListByPathInBacth(bundle);
                        break;
                    case 2:
                        bundle2 = getSceneTagListByPath(bundle);
                        break;
                    case 3:
                        bundle2 = getImageCacheBitmap(bundle);
                        break;
                    case 4:
                        bundle2 = releaseImageCacheBitmap();
                        break;
                    case 5:
                        bundle2 = getSecretInfo(bundle);
                        break;
                    case 6:
                        bundle2 = deleteByPath(bundle);
                        break;
                    case 7:
                        bundle2 = deleteCloudByPath(bundle);
                        break;
                    case '\b':
                        bundle2 = deleteFileLocalAndCloud(bundle);
                        break;
                    case '\t':
                        bundle2 = saveToCloudDB(bundle);
                        break;
                    case '\n':
                        bundle2 = updateLocalDBNotShowInRecycleBin(bundle);
                        break;
                    case 11:
                        bundle2 = parseFlagsForVideo(bundle);
                        break;
                    case '\f':
                        bundle2 = scanFile(bundle);
                        break;
                    case '\r':
                        bundle2 = cleanFile(bundle);
                        break;
                    case 14:
                        bundle2 = isDeviceSupportVideoAnalytic();
                        break;
                }
                DefaultLogger.d("GalleryProviderForMedia", "MediaEditorProviderForGallery call method api consume : %d , methods is %s ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), str);
                return bundle2 == null ? super.call(str, str2, bundle) : bundle2;
            } catch (Exception e) {
                DefaultLogger.e("GalleryProviderForMedia", e.getMessage());
                return super.call(str, str2, bundle);
            }
        }
        return null;
    }

    public final Bundle addToFavoritesByPath(Bundle bundle) {
        if (bundle == null || !bundle.containsKey("key_common_path")) {
            return null;
        }
        CloudUtils.addToFavoritesByPath(getContext(), bundle.getString("key_common_path"));
        return null;
    }

    public final Bundle scanFile(Bundle bundle) {
        if (bundle == null || !bundle.containsKey("key_common_path") || !bundle.containsKey("code")) {
            return null;
        }
        ScannerEngine.getInstance().scanFile(getContext(), bundle.getString("key_common_path"), bundle.getInt("code"));
        return null;
    }

    public final Bundle cleanFile(Bundle bundle) {
        if (bundle == null || !bundle.containsKey("key_common_path") || !bundle.containsKey("code")) {
            return null;
        }
        ScannerEngine.getInstance().cleanFile(getContext(), bundle.getString("key_common_path"), bundle.getInt("code"));
        return null;
    }

    public final Bundle parseFlagsForVideo(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (bundle.containsKey("key_common_path")) {
            long parseFlagsForVideo = SpecialTypeMediaUtils.parseFlagsForVideo(bundle.getString("key_common_path"));
            Bundle bundle2 = new Bundle();
            bundle2.putLong("flag", parseFlagsForVideo);
            return bundle2;
        }
        return new Bundle();
    }

    public final Bundle updateLocalDBNotShowInRecycleBin(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        ScreenEditorHelper.updateLocalDBNotShowInRecycleBin(bundle.getString("key_common_path"));
        return null;
    }

    public final Bundle saveToCloudDB(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = bundle.getString("key_common_path");
        long j = bundle.getLong("key_common_id");
        int i = bundle.getInt("local_flag");
        long mediaId = SaveToCloudUtil.saveToCloudDB(getContext(), new SaveParams.Builder().setSaveFile(new File(string)).setAlbumId(j).setLocalFlag(i).setCredible(bundle.getBoolean("credible")).build()).getMediaId();
        Bundle bundle2 = new Bundle();
        bundle2.putLong("key_common_id", mediaId);
        return bundle2;
    }

    public final Bundle deleteFileLocalAndCloud(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        int i = bundle.getInt("key_common_reason");
        int i2 = bundle.getInt("options");
        String[] stringArray = bundle.getStringArray("key_common_path");
        final ResultReceiver resultReceiver = (ResultReceiver) bundle.getParcelable("resultReceiver");
        DeletionTask deletionTask = new DeletionTask();
        DeletionTask.Param param = new DeletionTask.Param(stringArray, i2, i);
        deletionTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.mediaeditor.api.GalleryProviderForMediaEditor$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public final void onCompleteProcess(Object obj) {
                GalleryProviderForMediaEditor.$r8$lambda$SZBLOMvfgJ7rJcZlu6Skp2VsWDk(resultReceiver, (long[]) obj);
            }
        });
        deletionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
        return new Bundle();
    }

    public final Bundle deleteCloudByPath(Bundle bundle) throws StoragePermissionMissingException {
        if (bundle == null) {
            return null;
        }
        long[] deleteCloudByPath = CloudUtils.deleteCloudByPath(getContext(), bundle.getInt("key_common_reason"), bundle.getString("key_common_path"));
        Bundle bundle2 = new Bundle();
        bundle2.putLongArray("key_common_result", deleteCloudByPath);
        return bundle2;
    }

    public final Bundle deleteByPath(Bundle bundle) throws StoragePermissionMissingException {
        if (bundle == null) {
            return null;
        }
        long[] deleteByPath = CloudUtils.deleteByPath(getContext(), bundle.getInt("key_common_reason"), bundle.getStringArray("key_common_path"));
        Bundle bundle2 = new Bundle();
        bundle2.putLongArray("key_common_result", deleteByPath);
        return bundle2;
    }

    public final Bundle getSecretInfo(Bundle bundle) {
        if (bundle == null || bundle.getLong("key_common_id", -1L) == -1) {
            return null;
        }
        SecretInfo secretInfo = CloudUtils.getSecretInfo(getContext(), bundle.getLong("key_common_id", -1L), new SecretInfo());
        Bundle bundle2 = new Bundle();
        bundle2.putString("key_common_path", secretInfo.mSecretPath);
        bundle2.putByteArray("info_key", secretInfo.mSecretKey);
        return bundle2;
    }

    public final Bundle getImageCacheBitmap(Bundle bundle) {
        Uri uri = bundle != null ? (Uri) bundle.getParcelable("key_common_uri") : null;
        if (uri == null) {
            return null;
        }
        return this.mCacheManager.getCache(uri);
    }

    public final Bundle releaseImageCacheBitmap() {
        return this.mCacheManager.releaseCache();
    }

    public final Bundle getSceneTagListByPath(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        ArrayList<MediaScene> arrayList = (ArrayList) AnalyticFaceAndSceneManager.getInstance().getSceneTagListByPath(bundle.getString("key_common_path"), bundle.getBoolean("only_from_db"), bundle.getBoolean("is_quick_cal"));
        if (bundle.getInt("editor_version", 0) < 1) {
            arrayList = transferToOld(arrayList);
        }
        Bundle bundle2 = new Bundle();
        bundle2.putParcelableArrayList("key_common_result", arrayList);
        return bundle2;
    }

    public static ArrayList<MediaScene> transferToOld(ArrayList<MediaScene> arrayList) {
        if (!BaseMiscUtil.isValid(arrayList)) {
            return arrayList;
        }
        Iterator<MediaScene> it = arrayList.iterator();
        while (it.hasNext()) {
            MediaScene next = it.next();
            next.setSceneTag(MediaSceneTagManager.transferToOldTagValue(next.getSceneTag()));
        }
        return arrayList;
    }

    public final Bundle getSceneTagListByPathInBacth(Bundle bundle) {
        List<List<MediaScene>> sceneTagListByPathInBacth;
        if (bundle == null || (sceneTagListByPathInBacth = AnalyticFaceAndSceneManager.getInstance().getSceneTagListByPathInBacth(bundle.getStringArrayList("key_common_path"), bundle.getBoolean("only_from_db"), bundle.getBoolean("is_quick_cal"))) == null) {
            return null;
        }
        int i = bundle.getInt("editor_version", 0);
        int size = sceneTagListByPathInBacth.size();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("result_length", size);
        for (int i2 = 0; i2 < size; i2++) {
            List<MediaScene> list = sceneTagListByPathInBacth.get(i2);
            if (i < 1) {
                transferToOld((ArrayList) list);
            }
            bundle2.putParcelableArrayList("key_common_result" + i2, (ArrayList) list);
        }
        return bundle2;
    }

    public final Bundle isDeviceSupportVideoAnalytic() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("key_common_result", AnalyticFaceAndSceneManager.isDeviceSupportVideo());
        return bundle;
    }

    /* loaded from: classes3.dex */
    public static class ImageCacheManager {
        public MemoryFile mCache;
        public int mCallingToken;
        public final Handler mHandler;

        public ImageCacheManager() {
            this.mHandler = new Handler(ThreadManager.getWorkThreadLooper()) { // from class: com.miui.mediaeditor.api.GalleryProviderForMediaEditor.ImageCacheManager.1
                {
                    ImageCacheManager.this = this;
                }

                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    ImageCacheManager.this.handleMessage(message);
                }
            };
        }

        public final void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            DefaultLogger.e("ImageCacheManager", "the caller[pid: %d] still doesn't release the cache after %dms", Integer.valueOf(this.mCallingToken), 5000L);
            releaseCache();
        }

        public final void sendCacheTimeout() {
            cancelCacheTimeout();
            this.mHandler.sendEmptyMessageDelayed(1, 5000L);
        }

        public final void cancelCacheTimeout() {
            this.mHandler.removeMessages(1);
        }

        public synchronized Bundle getCache(Uri uri) {
            Bundle bundle;
            TimingTracing.beginTracing("ImageCacheManager", "getCache");
            releaseCache();
            TimingTracing.addSplit("releaseCache");
            bundle = new Bundle();
            Bitmap blockingLoad = GlideLoadingUtils.blockingLoad(Glide.with(StaticContext.sGetAndroidContext()), PreloadModel.of(uri.toString()));
            if (blockingLoad != null && !blockingLoad.isRecycled()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                blockingLoad.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                TimingTracing.addSplit("preview.compress");
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                MemoryFile createMemoryFile = MemoryFileUtils.createMemoryFile("preview-bitmap-name", byteArray.length);
                TimingTracing.addSplit("MemoryFileUtils.createMemoryFile");
                if (createMemoryFile != null) {
                    try {
                        createMemoryFile.writeBytes(byteArray, 0, 0, byteArray.length);
                        createMemoryFile.allowPurging(false);
                        TimingTracing.addSplit("memoryFile.writeBytes");
                        this.mCache = createMemoryFile;
                        ParcelFileDescriptor parcelFileDescriptor = MemoryFileUtils.getParcelFileDescriptor(createMemoryFile);
                        TimingTracing.addSplit("MemoryFileUtils.getParcelFileDescriptorEx");
                        bundle.putInt("key_get_image_cache_bitmap_length", createMemoryFile.length());
                        bundle.putParcelable("key_get_image_cache_bitmap_file_descriptor", parcelFileDescriptor);
                        this.mCallingToken = Binder.getCallingPid();
                        sendCacheTimeout();
                    } catch (IOException e) {
                        e.printStackTrace();
                        createMemoryFile.close();
                    }
                }
            }
            TimingTracing.stopTracing(null);
            return bundle;
        }

        public synchronized Bundle releaseCache() {
            Bundle bundle;
            cancelCacheTimeout();
            bundle = new Bundle();
            MemoryFile memoryFile = this.mCache;
            if (memoryFile != null) {
                memoryFile.close();
                this.mCache = null;
                bundle.putBoolean("key_common_result", true);
            }
            bundle.putBoolean("key_common_result", false);
            return bundle;
        }
    }
}
