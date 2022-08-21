package com.miui.gallery.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.R;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.imagecleanlib.ImageCleanManager;
import com.miui.imagecleanlib.ImageCleanTask;
import com.miui.imagecleanlib.ImageCleanUtils;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;

/* loaded from: classes2.dex */
public class SecurityShareHelper {
    public static Handler workHandler;
    public static Handler uiHandler = new Handler(Looper.getMainLooper());
    public static ConcurrentHashMap<String, String> sCleanedImagesMap = new ConcurrentHashMap<>();
    public static final LazyValue<Void, Boolean> IS_XMAN_AVAILABLE = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.SecurityShareHelper.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r2) {
            Intent intent = new Intent("miui.intent.action.XMAN_SHARE_MANAGER");
            intent.setPackage("com.miui.xman");
            return Boolean.valueOf(BaseMiscUtil.isIntentSupported(intent));
        }
    };
    public static final LazyValue<Void, Boolean> IS_ZMAN_SECURITY_SHARE_AVAILABLE = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.SecurityShareHelper.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r2) {
            Intent intent = new Intent("miui.intent.action.XMAN_SHARE_MANAGER");
            intent.setPackage("com.miui.zman");
            return Boolean.valueOf(BaseMiscUtil.isIntentSupported(intent));
        }
    };
    public static final LazyValue<Void, Boolean> IS_SECURITYCENTER_SECURITY_SHARE_AVAILABLE = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.SecurityShareHelper.3
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r2) {
            Intent intent = new Intent("miui.intent.action.XMAN_SHARE_MANAGER");
            intent.setPackage("com.miui.securitycenter");
            return Boolean.valueOf(BaseMiscUtil.isIntentSupported(intent));
        }
    };

    /* loaded from: classes2.dex */
    public interface OnCleanDoneListener {
        void onCleanDone(List<Uri> list);
    }

    public static boolean isZmanShareEnable(Context context) {
        return getSettingValue(context, "zman_share_enable");
    }

    public static boolean getSettingValue(Context context, String str) {
        return Settings.Secure.getInt(context.getContentResolver(), str, 0) == 1;
    }

    public static boolean isHideLocationInfoEnable(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "zman_share_hide_location", 1) == 1;
    }

    public static void setHideLocationInfoEnable(Context context, int i) {
        Settings.Secure.putInt(context.getContentResolver(), "zman_share_hide_location", i);
    }

    public static boolean isHideCameraInfoEnable(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "zman_share_hide_camera", 1) == 1;
    }

    public static void setHideCameraInfoEnable(Context context, int i) {
        Settings.Secure.putInt(context.getContentResolver(), "zman_share_hide_camera", i);
    }

    public static boolean isHideLocationInfoEnableDefault(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "zman_share_hide_location_default", -1) == 1;
    }

    public static boolean isHideCameraInfoEnableDefault(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "zman_share_hide_camera_default", -1) == 1;
    }

    public static boolean isZmanCloudDisable(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "zman_cloud_disable", 0) == 1;
    }

    public static boolean isSettingDefault(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "zman_share_hide_location_default", -1) != -1;
    }

    public static void resetHideSettings(Context context) {
        if (!isSettingDefault(context)) {
            return;
        }
        setHideLocationInfoEnable(context, isHideLocationInfoEnableDefault(context) ? 1 : 0);
        setHideCameraInfoEnable(context, isHideCameraInfoEnableDefault(context) ? 1 : 0);
    }

    public static boolean haveLocationInfo(File file) {
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            String attribute = exifInterface.getAttribute("GPSAltitudeRef");
            String attribute2 = exifInterface.getAttribute("GPSLatitude");
            String attribute3 = exifInterface.getAttribute("GPSLongitude");
            if (TextUtils.isEmpty(attribute) && TextUtils.isEmpty(attribute2)) {
                if (TextUtils.isEmpty(attribute3)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            DefaultLogger.e("zman_share", "IOException: ", e);
            return false;
        }
    }

    public static boolean haveCameraInfo(File file) {
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            String attribute = exifInterface.getAttribute("ISOSpeedRatings");
            String attribute2 = exifInterface.getAttribute("FocalLength");
            String attribute3 = exifInterface.getAttribute("Flash");
            if (TextUtils.isEmpty(attribute) && TextUtils.isEmpty(attribute2)) {
                if (TextUtils.isEmpty(attribute3)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            DefaultLogger.e("zman_share", "IOException: ", e);
            return false;
        }
    }

    public static boolean isNeedSecurityShare(Context context) {
        return BuildUtil.isMiui12() && (IS_ZMAN_SECURITY_SHARE_AVAILABLE.get(null).booleanValue() || IS_SECURITYCENTER_SECURITY_SHARE_AVAILABLE.get(null).booleanValue()) && ((isHideCameraInfoEnable(context) || isHideLocationInfoEnable(context) || isZmanShareEnable(context)) && !isZmanCloudDisable(context));
    }

    public static boolean isSupportMiui12(Context context) {
        return BuildUtil.isMiui12() && (IS_ZMAN_SECURITY_SHARE_AVAILABLE.get(null).booleanValue() || IS_SECURITYCENTER_SECURITY_SHARE_AVAILABLE.get(null).booleanValue()) && !isZmanCloudDisable(context);
    }

    public static boolean isSupportMiui11(Context context, String str) {
        return BuildUtil.isMiui11(context) && !BaseBuildUtil.isInternational() && TextUtils.equals(str, "com.tencent.mm") && IS_XMAN_AVAILABLE.get(null).booleanValue();
    }

    public static String getSharePackageName(Context context) {
        return (!IS_XMAN_AVAILABLE.get(null).booleanValue() || !BuildUtil.isMiui11(context)) ? IS_SECURITYCENTER_SECURITY_SHARE_AVAILABLE.get(null).booleanValue() ? "com.miui.securitycenter" : "com.miui.zman" : "com.miui.xman";
    }

    public static void doSecurityShare(final Activity activity, final Intent intent, final List<Uri> list, ImageCleanManager.CleanProgressListener cleanProgressListener) {
        cleanImageInfoAsync(activity, list, new OnCleanDoneListener() { // from class: com.miui.gallery.util.SecurityShareHelper.4
            @Override // com.miui.gallery.util.SecurityShareHelper.OnCleanDoneListener
            public void onCleanDone(List<Uri> list2) {
                if (!BaseMiscUtil.isValid(list2)) {
                    DefaultLogger.w("zman_share", "no items returned by security share");
                    return;
                }
                boolean z = false;
                if (list2.size() > 1) {
                    intent.putParcelableArrayListExtra("android.intent.extra.STREAM", (ArrayList) list2);
                } else {
                    intent.setAction("android.intent.action.SEND");
                    intent.putExtra("android.intent.extra.STREAM", list2.get(0));
                }
                if (intent.getComponent() != null && intent.getComponent().getPackageName() != null) {
                    for (Uri uri : list2) {
                        activity.grantUriPermission(intent.getComponent().getPackageName(), uri, 1);
                    }
                }
                DefaultLogger.d("zman_share", "securitysharehelper begin to security share for uris : %s", list2);
                activity.startActivityForResult(intent, 1);
                Activity activity2 = activity;
                String packageName = activity2.getPackageName();
                List<Uri> list3 = list;
                boolean z2 = list3 == list2;
                boolean z3 = list3 == list2;
                if (list2.size() > 1) {
                    z = true;
                }
                SecurityShareHelper.analyticsImageShare(activity2, packageName, z2, z3, z);
            }
        }, cleanProgressListener);
    }

    public static void cleanImageInfoAsync(Activity activity, final List<Uri> list, final OnCleanDoneListener onCleanDoneListener, ImageCleanManager.CleanProgressListener cleanProgressListener) {
        if (workHandler == null) {
            HandlerThread handlerThread = new HandlerThread("doSecurityShare_Thread");
            handlerThread.start();
            workHandler = new Handler(handlerThread.getLooper());
        }
        final WeakReference weakReference = new WeakReference(activity);
        final WeakReference weakReference2 = new WeakReference(cleanProgressListener);
        workHandler.post(new Runnable() { // from class: com.miui.gallery.util.SecurityShareHelper.5
            @Override // java.lang.Runnable
            public void run() {
                if (weakReference.get() == null) {
                    return;
                }
                final List<Uri> cleanImageInfoSync = SecurityShareHelper.cleanImageInfoSync((Activity) weakReference.get(), list, (ImageCleanManager.CleanProgressListener) weakReference2.get());
                SecurityShareHelper.uiHandler.post(new Runnable() { // from class: com.miui.gallery.util.SecurityShareHelper.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        OnCleanDoneListener onCleanDoneListener2 = onCleanDoneListener;
                        if (onCleanDoneListener2 != null) {
                            onCleanDoneListener2.onCleanDone(cleanImageInfoSync);
                        }
                    }
                });
            }
        });
    }

    public static void startPrivacyProtectSettingsActivity(Context context, boolean z, BaseDataItem baseDataItem) {
        if (BuildUtil.isMiui12()) {
            if (!IS_ZMAN_SECURITY_SHARE_AVAILABLE.get(null).booleanValue() && !IS_SECURITYCENTER_SECURITY_SHARE_AVAILABLE.get(null).booleanValue()) {
                return;
            }
            Intent intent = new Intent();
            intent.setAction("miui.intent.action.ZMAN_PRIVACY_PROTECT_SETTING");
            intent.setPackage(getSharePackageName(context));
            intent.putExtra("multi_image", z);
            if (baseDataItem != null) {
                String itemImagePath = getItemImagePath(baseDataItem);
                if (itemImagePath == null) {
                    DefaultLogger.e("zman_share", "startPrivacyProtectSettingsActivity imagePath null");
                    return;
                }
                File file = new File(itemImagePath);
                intent.putExtra("have_location", haveLocationInfo(file));
                intent.putExtra("have_camera", haveCameraInfo(file));
            }
            context.startActivity(intent);
        }
    }

    public static void startPrivacyProtectTipSettingsActivity(Context context) {
        if (!isSupportMiui12(context) || isSettingDefault(context)) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("miui.intent.action.ZMAN_PRIVACY_PROTECT_TIP_SETTING");
        intent.setPackage(getSharePackageName(context));
        context.startActivity(intent);
    }

    public static void startShareSettingsActivity(Context context, Intent intent) {
        if (isSupportMiui12(context)) {
            Intent intent2 = new Intent();
            intent2.setAction("miui.intent.action.ZMAN_SECURITY_SHARE_SETTING");
            intent2.setPackage(getSharePackageName(context));
            MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, intent);
            context.startActivity(intent2);
        }
    }

    public static List<Uri> cleanImageInfoSync(Activity activity, List<Uri> list, ImageCleanManager.CleanProgressListener cleanProgressListener) {
        String absolutePath;
        if (activity == null) {
            return null;
        }
        Context applicationContext = activity.getApplicationContext();
        if (!isSupportMiui12(applicationContext) || (!isHideLocationInfoEnable(applicationContext) && !isHideCameraInfoEnable(applicationContext))) {
            return list;
        }
        boolean isHideLocationInfoEnable = isHideLocationInfoEnable(applicationContext);
        boolean isHideCameraInfoEnable = isHideCameraInfoEnable(applicationContext);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("zman_share", "cleanImageInfoSync");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        StringBuilder sb = new StringBuilder();
        sb.append(applicationContext.getCacheDir());
        String str = File.separator;
        sb.append(str);
        sb.append("SecurityShare");
        String sb2 = sb.toString();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE_DIRECTORY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(sb2, permission, appendInvokerTag);
        if (documentFile != null) {
            documentFile.delete();
        }
        String str2 = applicationContext.getExternalCacheDir() + str + "SecurityShare";
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, appendInvokerTag);
        if (documentFile2 != null && !documentFile2.exists()) {
            StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag);
        } else {
            DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(str2, permission, appendInvokerTag);
            if (documentFile3 != null) {
                documentFile3.delete();
            }
            StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag);
        }
        ArrayList arrayList = new ArrayList(list.size());
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            File uriToFile = uriToFile(list.get(i), applicationContext);
            if (uriToFile != null) {
                String absolutePath2 = uriToFile.getAbsolutePath();
                String mimeType = BaseFileMimeUtil.getMimeType(absolutePath2);
                boolean haveCameraInfo = ImageCleanUtils.haveCameraInfo(uriToFile);
                boolean haveLocationInfo = ImageCleanUtils.haveLocationInfo(uriToFile);
                if (!BaseFileMimeUtil.isVideoFromMimeType(mimeType) || ((haveCameraInfo && isHideCameraInfoEnable) || (haveLocationInfo && isHideLocationInfoEnable))) {
                    if (hasValideCleanedBefore(absolutePath2)) {
                        absolutePath = sCleanedImagesMap.get(absolutePath2);
                    } else {
                        absolutePath = new File(str2, System.currentTimeMillis() + "." + getFileExtension(absolutePath2)).getAbsolutePath();
                        sCleanedImagesMap.put(absolutePath2, absolutePath);
                        arrayList2.add(new ImageCleanTask(absolutePath2, absolutePath, isHideLocationInfoEnable, isHideCameraInfoEnable));
                    }
                    arrayList.add(GalleryOpenProvider.translateToContent(absolutePath));
                } else {
                    arrayList.add(list.get(i));
                }
            } else {
                arrayList.add(list.get(i));
            }
        }
        if (arrayList2.size() == 0) {
            DefaultLogger.d("zman_share", "nothing to clean");
            return arrayList;
        }
        DefaultLogger.d("zman_share", "%d of %d files need clean", Integer.valueOf(arrayList2.size()), Integer.valueOf(list.size()));
        if (cleanProgressListener != null && arrayList2.size() > 10) {
            cleanProgressListener.onProgress(0, arrayList2.size());
        }
        ImageCleanManager.getInstance().doCleanAsyncAndWait(arrayList2, cleanProgressListener);
        return arrayList;
    }

    public static boolean hasValideCleanedBefore(String str) {
        ConcurrentHashMap<String, String> concurrentHashMap = sCleanedImagesMap;
        if (concurrentHashMap != null && concurrentHashMap.containsKey(str)) {
            String str2 = sCleanedImagesMap.get(str);
            if (new File(str2).exists() && isValideCleanedImage(BaseFileUtils.getFileNameWithoutExtension(str2))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValideCleanedImage(String str) {
        if (str.isEmpty()) {
            return false;
        }
        try {
            long longValue = Long.valueOf(str).longValue();
            return System.currentTimeMillis() - longValue < 30000 && System.currentTimeMillis() - longValue > 0;
        } catch (Exception unused) {
            DefaultLogger.e("zman_share", "filename not timestamp");
            return false;
        }
    }

    public static String getItemImagePath(BaseDataItem baseDataItem) {
        if (baseDataItem == null) {
            return null;
        }
        String originalPath = baseDataItem.getOriginalPath();
        return TextUtils.isEmpty(originalPath) ? baseDataItem.getThumnailPath() : originalPath;
    }

    public static File uriToFile(Uri uri, Context context) {
        if (uri != null && !TextUtils.isEmpty(uri.toString()) && !TextUtils.isEmpty(uri.getScheme())) {
            try {
                String scheme = uri.getScheme();
                char c = 65535;
                int hashCode = scheme.hashCode();
                int i = 0;
                if (hashCode != 3143036) {
                    if (hashCode == 951530617 && scheme.equals(MiStat.Param.CONTENT)) {
                        c = 1;
                    }
                } else if (scheme.equals(Action.FILE_ATTRIBUTE)) {
                    c = 0;
                }
                if (c == 0) {
                    String encodedPath = uri.getEncodedPath();
                    if (TextUtils.isEmpty(encodedPath)) {
                        return null;
                    }
                    String decode = Uri.decode(encodedPath);
                    Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{j.c, "_data"}, "(_data='" + decode + "')", null, null);
                    if (query == null) {
                        return null;
                    }
                    query.moveToFirst();
                    while (!query.isAfterLast()) {
                        i = query.getInt(query.getColumnIndex(j.c));
                        decode = query.getString(query.getColumnIndex("_data"));
                        query.moveToNext();
                    }
                    query.close();
                    if (i == 0 && !TextUtils.isEmpty(decode)) {
                        return new File(decode);
                    }
                    return null;
                } else if (c == 1) {
                    Cursor query2 = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                    if (query2 == null) {
                        return null;
                    }
                    String string = query2.moveToFirst() ? query2.getString(query2.getColumnIndexOrThrow("_data")) : null;
                    query2.close();
                    if (!TextUtils.isEmpty(string)) {
                        return new File(string);
                    }
                    return null;
                }
            } catch (Exception e) {
                DefaultLogger.w("zman_share", "uriToFile Exception: ", e);
            }
        }
        return null;
    }

    public static String getFileExtension(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        return (lastIndexOf == -1 || str.lastIndexOf(File.separator) >= lastIndexOf) ? "" : str.substring(lastIndexOf + 1);
    }

    public static void analyticsImageShare(Context context, String str, boolean z, boolean z2, boolean z3) {
        if (!isSupportMiui12(context)) {
            return;
        }
        Intent intent = new Intent("com.miui.zman.intent.action.SHARED");
        intent.putExtra("is_multi_image", z3);
        intent.putExtra("param_src_packagename", str);
        intent.putExtra("param_image_have_location", z);
        intent.putExtra("param_image_have_camera", z2);
        intent.setPackage("com.miui.securitycenter");
        context.sendBroadcast(intent);
    }

    /* loaded from: classes2.dex */
    public static class SecureShareProgressDialogHelper {
        public AlertDialog mSecurityShareDialog;
        public TextView mSecurityShareDialogTextView;

        public void showProgressDialog(WeakReference<Activity> weakReference, int i, int i2) {
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            Activity activity = weakReference.get();
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            if (this.mSecurityShareDialog == null) {
                this.mSecurityShareDialog = new AlertDialog.Builder(activity).create();
                View inflate = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.dialog_security_share_progress, (ViewGroup) null);
                this.mSecurityShareDialogTextView = (TextView) inflate.findViewById(R.id.message);
                this.mSecurityShareDialog.setView(inflate);
                this.mSecurityShareDialog.setCancelable(false);
            }
            if (!this.mSecurityShareDialog.isShowing()) {
                this.mSecurityShareDialog.show();
            }
            this.mSecurityShareDialogTextView.setText(String.format(activity.getString(R.string.loading_text_format), Integer.valueOf(i), Integer.valueOf(i2)));
        }

        public void dismissDialog() {
            AlertDialog alertDialog = this.mSecurityShareDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.mSecurityShareDialog = null;
                this.mSecurityShareDialogTextView = null;
            }
        }
    }
}
