package com.miui.mediaeditor.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.miui.gallery.editor.photo.utils.MemoryFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.miui.mediaeditor.config.BaseMediaEditorConfig;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes3.dex */
public class MediaEditorApiHelper {
    public static final List<FunctionModel> FUNCTION_MODELS = new LinkedList();
    public static final Map<String, Boolean> FUNC_SUPPORTED_CACHE = new ConcurrentHashMap();
    public static String sEditExportedPath;

    static {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        StaticContext.sGetAndroidContext().registerReceiver(new PackageReceiver(), intentFilter);
    }

    public static Bundle safeCallMediaEditorProvider(String str, String str2, Bundle bundle) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            Bundle call = StaticContext.sGetAndroidContext().getContentResolver().call(BaseMediaEditorConfig.MI_MEDIA_EDITOR_API_URI, str, str2, bundle);
            DefaultLogger.d("MediaEditorApiUtils", "gallery call mediaeditor ,api consume : %d , methods is : %s , bundle is : %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), str, call == null ? "null" : call.toString());
            return call;
        } catch (Exception e) {
            e.printStackTrace();
            DefaultLogger.d("MediaEditorApiUtils", "gallery call mediaeditor error, message is :   -> %s", e.getMessage());
            return null;
        }
    }

    public static List<FunctionModel> getFunctionModels() {
        List<FunctionModel> unmodifiableList;
        List<FunctionModel> list = FUNCTION_MODELS;
        synchronized (list) {
            if (list.isEmpty()) {
                ArrayList<ParcelableFunctionModel> parcelableFunctionModelList = getParcelableFunctionModelList();
                if (BaseMiscUtil.isValid(parcelableFunctionModelList)) {
                    for (Iterator<ParcelableFunctionModel> it = parcelableFunctionModelList.iterator(); it.hasNext(); it = it) {
                        ParcelableFunctionModel next = it.next();
                        FUNCTION_MODELS.add(new FunctionModel(next.getFunctionName(), next.getFunctionIcon(), next.getFunctionDesc(), next.getFunctionLimitMaxSize(), next.getFunctionLimitMinSize(), next.getFunctionSupportMimeType(), next.getFunctionClassName1(), next.getFunctionClassName2(), next.getFunctionLoadCode(), next.getFunctionTag(), next.isDeviceSupport(), next.getExtraInfo()));
                    }
                }
            }
            unmodifiableList = Collections.unmodifiableList(FUNCTION_MODELS);
        }
        return unmodifiableList;
    }

    public static Map<String, FunctionModel> getFunctionModelMap() {
        HashMap hashMap = new HashMap();
        for (FunctionModel functionModel : getFunctionModels()) {
            hashMap.put(functionModel.getFunctionTag(), functionModel);
        }
        return hashMap;
    }

    public static int getCollageMaxImageSize() {
        Bundle safeCallMediaEditorProvider = safeCallMediaEditorProvider("method_collage_max_image_size", "", null);
        if (safeCallMediaEditorProvider == null) {
            return 6;
        }
        return safeCallMediaEditorProvider.getInt("key_common_max_size", 6);
    }

    public static ArrayList<ParcelableFunctionModel> getParcelableFunctionModelList() {
        Bundle safeCallMediaEditorProvider = safeCallMediaEditorProvider("method_request_function_list", null, null);
        if (safeCallMediaEditorProvider != null) {
            safeCallMediaEditorProvider.setClassLoader(ParcelableFunctionModel.class.getClassLoader());
            return safeCallMediaEditorProvider.getParcelableArrayList("key_function_list");
        }
        return null;
    }

    public static boolean isDeviceSupportPhotoMovie() {
        return isFunctionSupported("method_is_device_support_photo_movie");
    }

    public static boolean isPhotoMovieAvailable() {
        return isFunctionAvailable("method_is_photo_movie_available");
    }

    public static boolean isDeviceSupportVlog() {
        return isFunctionSupported("method_is_device_support_vlog");
    }

    public static boolean isVlogAvailable() {
        return isFunctionAvailable("method_is_vlog_available");
    }

    public static boolean canAccessSecretAlbum() {
        Bundle safeCallMediaEditorProvider = safeCallMediaEditorProvider("method_can_access_secret_album", null, null);
        return safeCallMediaEditorProvider != null && safeCallMediaEditorProvider.getBoolean("key_common_result");
    }

    public static boolean isDeviceSupportMagicMatting() {
        return isFunctionSupported("method_is_device_support_magic_matting");
    }

    public static boolean isMagicMattingAvailable() {
        return isFunctionAvailable("method_is_magic_matting_available");
    }

    public static boolean isDeviceSupportIDPhoto() {
        return isFunctionSupported("method_is_device_support_id_photo");
    }

    public static boolean isIDPhotoAvailable() {
        return isFunctionAvailable("method_is_id_photo_available");
    }

    public static boolean isDeviceSupportArtStill() {
        return isFunctionSupported("method_is_device_support_art_still");
    }

    public static boolean isArtStillAvailable() {
        return isFunctionAvailable("method_is_art_still_available");
    }

    public static boolean isDeviceSupportVideoPost() {
        return isFunctionSupported("method_is_device_support_video_post");
    }

    public static boolean isVideoPostAvailable() {
        return isFunctionAvailable("method_is_video_post_available");
    }

    public static boolean isVideoEditorAvailable() {
        return isFunctionAvailable("method_is_video_editor_available");
    }

    public static boolean isFunctionAvailable(String str) {
        Bundle safeCallMediaEditorProvider = safeCallMediaEditorProvider(str, null, null);
        return safeCallMediaEditorProvider != null && safeCallMediaEditorProvider.getBoolean("key_common_is_available", false);
    }

    public static boolean isFunctionSupported(String str) {
        Map<String, Boolean> map = FUNC_SUPPORTED_CACHE;
        Boolean bool = map.get(str);
        if (bool == null) {
            Bundle safeCallMediaEditorProvider = safeCallMediaEditorProvider(str, null, null);
            boolean z = false;
            if (safeCallMediaEditorProvider != null && safeCallMediaEditorProvider.getBoolean("key_common_is_support", false)) {
                z = true;
            }
            bool = Boolean.valueOf(z);
            map.put(str, bool);
        }
        return bool.booleanValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [android.os.Bundle, java.lang.String, android.util.Printer] */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    public static Bitmap getImageCache(Uri uri) {
        Bitmap bitmap;
        TimingTracing.beginTracing("MediaEditorApiUtils", "getImageCache");
        try {
            Bundle bundle = new Bundle();
            bundle.putParcelable("key_common_uri", uri);
            Bundle safeCallMediaEditorProvider = safeCallMediaEditorProvider("method_get_image_cache_bitmap", null, bundle);
            TimingTracing.addSplit("METHOD_GET_CACHE");
            if (safeCallMediaEditorProvider != null) {
                int i = safeCallMediaEditorProvider.getInt("key_get_image_cache_bitmap_length");
                ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) safeCallMediaEditorProvider.getParcelable("key_get_image_cache_bitmap_file_descriptor");
                if (i > 0 && parcelFileDescriptor != null) {
                    try {
                        try {
                            FileInputStream inputStream = MemoryFileUtils.getInputStream(parcelFileDescriptor);
                            try {
                                byte[] bArr = new byte[i];
                                inputStream.read(bArr, 0, i);
                                TimingTracing.addSplit("MemoryFileUtils.read");
                                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, i);
                                try {
                                    TimingTracing.addSplit("BitmapFactory.decodeByteArray");
                                    inputStream.close();
                                    bitmap = decodeByteArray;
                                } catch (Throwable th) {
                                    th = th;
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    }
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } catch (IOException e) {
                            e = e;
                            i = 0;
                            e.printStackTrace();
                            bitmap = i;
                            return bitmap;
                        }
                    } catch (IOException e2) {
                        e = e2;
                        e.printStackTrace();
                        bitmap = i;
                        return bitmap;
                    }
                    return bitmap;
                }
            }
            bitmap = null;
            return bitmap;
        } finally {
            safeCallMediaEditorProvider("method_release_image_cache_bitmap", null, null);
            updateJustEditExportedPath(null);
            TimingTracing.addSplit("METHOD_RELEASE_CACHE");
            TimingTracing.stopTracing(null);
        }
    }

    public static synchronized void updateJustEditExportedPath(String str) {
        synchronized (MediaEditorApiHelper.class) {
            sEditExportedPath = str;
        }
    }

    public static synchronized boolean isJustEditExportedPath(String str) {
        boolean z;
        synchronized (MediaEditorApiHelper.class) {
            if (!TextUtils.isEmpty(sEditExportedPath)) {
                if (TextUtils.equals(sEditExportedPath, str)) {
                    z = true;
                }
            }
            z = false;
        }
        return z;
    }

    /* loaded from: classes3.dex */
    public static class PackageReceiver extends BroadcastReceiver {
        public PackageReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getData() == null) {
                return;
            }
            String action = intent.getAction();
            String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
            if ((!"android.intent.action.PACKAGE_REMOVED".equals(intent.getAction()) && !"android.intent.action.PACKAGE_ADDED".equals(action)) || !"com.miui.mediaeditor".equals(schemeSpecificPart)) {
                return;
            }
            DefaultLogger.d("MediaEditorApiUtils", "action: %s, invalidate cache", action);
            synchronized (MediaEditorApiHelper.FUNCTION_MODELS) {
                MediaEditorApiHelper.FUNCTION_MODELS.clear();
            }
            MediaEditorApiHelper.FUNC_SUPPORTED_CACHE.clear();
        }
    }
}
