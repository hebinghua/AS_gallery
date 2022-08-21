package com.miui.gallery.provider;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.activity.facebaby.BabyLockWallpaperSettingActivity;
import com.miui.gallery.data.DBCloud;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBShareImage;
import com.miui.gallery.data.DecodeUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class BabyLockWallpaperProvider extends ContentProvider {
    public static final Uri BASE_URI = Uri.parse("content://com.miui.gallery.cloud.baby.wallpaper_provider");

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

    /* loaded from: classes2.dex */
    public static final class PathSegments {
        public final boolean isSharerAlbum;
        public final String itemLocalId;

        public PathSegments(boolean z, String str) {
            this.isSharerAlbum = z;
            this.itemLocalId = str;
        }

        public static PathSegments parse(List<String> list) {
            if (list == null || list.size() < 2) {
                return null;
            }
            String str = list.get(0);
            String str2 = list.get(1);
            if ((!"owner_album".equals(str) && !"sharer_album".equals(str)) || TextUtils.isEmpty(str2)) {
                return null;
            }
            return new PathSegments("sharer_album".equals(str), str2);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.isSharerAlbum ? "sharer_album" : "owner_album");
            sb.append(h.g);
            sb.append(this.itemLocalId);
            return sb.toString();
        }
    }

    /* loaded from: classes2.dex */
    public static final class CroppedPhotosPathHelper {
        public static final String CROPPED_PHOTOS_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator + ".cropped-photos";
        public static int sLastFileIndex = 1;

        public static String getCroppedPhotoPath(Uri uri, String str) {
            File[] listFiles;
            DocumentFile documentFile;
            if (uri == null || TextUtils.isEmpty(str)) {
                return null;
            }
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("BabyLock", "getCroppedPhotoPath");
            String str2 = CROPPED_PHOTOS_DIR;
            File file = new File(str2, getCroppedPhotoFileName(uri, str));
            if (file.isFile()) {
                return file.getAbsolutePath();
            }
            sLastFileIndex = 1 - sLastFileIndex;
            File file2 = new File(str2, getCroppedPhotoFileName(uri, str));
            if (!file2.isFile() && (listFiles = new File(str2).listFiles()) != null) {
                String croppedPhotosPathPrefix = getCroppedPhotosPathPrefix();
                for (File file3 : listFiles) {
                    if (file3.getName().startsWith(croppedPhotosPathPrefix) && (documentFile = StorageSolutionProvider.get().getDocumentFile(file3.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                        documentFile.delete();
                    }
                }
            }
            return file2.getAbsolutePath();
        }

        public static String getCroppedPhotoFileName(Uri uri, String str) {
            return getCroppedPhotosPathPrefix() + getCroppedPhotosPathPostfix(uri, str);
        }

        public static String getCroppedPhotosPathPrefix() {
            return sLastFileIndex + "-";
        }

        public static String getCroppedPhotosPathPostfix(Uri uri, String str) {
            return uri.hashCode() + "-" + str.hashCode();
        }
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        int appVersionCode = MiscUtil.getAppVersionCode("com.android.systemui");
        String callingPackage = getCallingPackage();
        if (Build.VERSION.SDK_INT > 29) {
            if (!"com.miui.miwallpaper".equalsIgnoreCase(callingPackage) && !"com.android.systemui".equalsIgnoreCase(callingPackage)) {
                return bundle2;
            }
        } else if (appVersionCode >= 201906250 && !"com.android.systemui".equalsIgnoreCase(callingPackage)) {
            return bundle2;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        if ("enableProvideLockWallpaper".equals(str)) {
            bundle2.putBoolean("result_boolean", provideLockWallpaperEnabled());
        } else if ("getSettingsComponent".equals(str)) {
            bundle2.putString("result_string", getSettingsComponent());
        } else if ("getNextLockWallpaperUri".equals(str)) {
            bundle2.putString("result_string", getNextLockWallpaperUri());
        } else {
            DefaultLogger.e("BabyLock", "unsupported method: " + str);
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
        return bundle2;
    }

    public final boolean provideLockWallpaperEnabled() {
        long currentTimeMillis = System.currentTimeMillis();
        boolean existBabyAlbum = BabyLockWallpaperDataManager.getInstance().existBabyAlbum();
        DefaultLogger.d("BabyLock", "provideLockWallpaperEnabled: " + existBabyAlbum + ", cost: " + (System.currentTimeMillis() - currentTimeMillis));
        return existBabyAlbum;
    }

    public final String getSettingsComponent() {
        return new ComponentName(getContext(), BabyLockWallpaperSettingActivity.class).flattenToString();
    }

    public final String getNextLockWallpaperUri() {
        return BabyLockWallpaperDataManager.getInstance().getRandomUri();
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String str) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        if (!"r".equals(str)) {
            DefaultLogger.e("BabyLock", "illegal mode: " + str);
            return null;
        } else if (uri == null || !"com.miui.gallery.cloud.baby.wallpaper_provider".equals(uri.getAuthority())) {
            DefaultLogger.e("BabyLock", "illegal uri: " + uri);
            return null;
        } else {
            final PathSegments parse = PathSegments.parse(uri.getPathSegments());
            if (parse == null) {
                DefaultLogger.e("BabyLock", "illegal uri: " + uri);
                return null;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            DBImage dBImage = (DBImage) GalleryUtils.safeQuery(parse.isSharerAlbum ? "shareImage" : "cloud", com.miui.gallery.cloud.CloudUtils.getProjectionAll(), String.format(Locale.US, "%s=?", j.c), new String[]{parse.itemLocalId}, (String) null, new GalleryUtils.QueryHandler<DBImage>() { // from class: com.miui.gallery.provider.BabyLockWallpaperProvider.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
                /* renamed from: handle */
                public DBImage mo1712handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToNext()) {
                        return null;
                    }
                    return parse.isSharerAlbum ? new DBShareImage(cursor) : new DBCloud(cursor);
                }
            });
            String tryToCrop = tryToCrop(dBImage, uri, BabyLockWallpaperDataManager.getWallpaperFilePath(dBImage));
            if (!TextUtils.isEmpty(tryToCrop)) {
                try {
                    parcelFileDescriptor = ParcelFileDescriptor.open(new File(tryToCrop), 268435456);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return parcelFileDescriptor;
        }
    }

    public final String tryToCrop(DBImage dBImage, Uri uri, String str) {
        int i;
        boolean z;
        BitmapFactory.Options options;
        RectF facePos;
        float min;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        DefaultLogger.d("BabyLock", "start tryToCrop %s", dBImage.getId());
        ExifUtil.ExifInfo parseRotationInfo = ExifUtil.parseRotationInfo(str, null);
        if (parseRotationInfo != null) {
            DefaultLogger.d("BabyLock", "rotation: %s, flip: %s", Integer.valueOf(parseRotationInfo.rotation), Boolean.valueOf(parseRotationInfo.flip));
            i = parseRotationInfo.rotation;
            z = i == 90 || i == 270;
        } else {
            i = 0;
            z = false;
        }
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options2);
        int i2 = z ? options2.outHeight : options2.outWidth;
        int i3 = z ? options2.outWidth : options2.outHeight;
        if (i2 < i3 && i == 0) {
            return str;
        }
        String croppedPhotoPath = CroppedPhotosPathHelper.getCroppedPhotoPath(uri, str);
        if (TextUtils.isEmpty(croppedPhotoPath)) {
            return null;
        }
        if (new File(croppedPhotoPath).isFile()) {
            DefaultLogger.d("BabyLock", "cache hit");
            return croppedPhotoPath;
        }
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();
        if (i2 > i3 && (facePos = BabyLockWallpaperDataManager.getFacePos(dBImage)) != null) {
            DefaultLogger.d("BabyLock", "start to crop face region");
            int i4 = (i3 * screenWidth) / screenHeight;
            float f = i2;
            float centerX = ExifUtil.adjustRectOrientation(1, 1, facePos, dBImage.getJsonExifInteger("orientation", 0).intValue(), true).centerX() * f;
            float f2 = i4 / 2;
            float f3 = 0.0f;
            if (centerX < f2) {
                min = Math.min(i4 + 0.0f, f);
            } else {
                min = Math.min(centerX + f2, f);
                f3 = Math.max(0.0f, min - i4);
            }
            try {
                Rect rect = new Rect((int) f3, 0, (int) min, i3);
                BitmapFactory.Options options3 = new BitmapFactory.Options();
                options3.inSampleSize = BitmapUtils.computeSampleSizeSmaller(rect.width(), rect.height(), screenWidth, screenHeight);
                if (parseRotationInfo != null) {
                    rect = ExifUtil.adjustRectOrientation(i2, i3, rect, parseRotationInfo.exifOrientation, false);
                }
                if (saveBitmapFile(DecodeUtils.considerOrientation(BitmapRegionDecoder.newInstance(str, true).decodeRegion(rect, options3), parseRotationInfo), croppedPhotoPath)) {
                    return croppedPhotoPath;
                }
                DefaultLogger.w("BabyLock", "failed to save cropped bitmap!");
            } catch (Throwable th) {
                DefaultLogger.e("BabyLock", "tryToCrop occur error.\n", th);
                th.printStackTrace();
            }
        }
        try {
            DefaultLogger.d("BabyLock", "start to adjust image orientation");
            options = new BitmapFactory.Options();
            options.inSampleSize = BitmapUtils.computeSampleSizeSmaller(i2, i3, screenWidth, screenHeight);
        } catch (Throwable th2) {
            DefaultLogger.e("BabyLock", "adjust image orientation occur error.\n", th2);
        }
        if (saveBitmapFile(DecodeUtils.considerOrientation(BitmapFactory.decodeFile(str, options), parseRotationInfo), croppedPhotoPath)) {
            return croppedPhotoPath;
        }
        DefaultLogger.w("BabyLock", "failed to save bitmap!");
        return str;
    }

    public static boolean saveBitmapFile(Bitmap bitmap, String str) {
        if (bitmap != null) {
            File file = new File(str);
            File saveBitmapToFile = com.miui.gallery.cloud.CloudUtils.saveBitmapToFile(bitmap, file.getParent(), file.getName());
            bitmap.recycle();
            return saveBitmapToFile != null;
        }
        return false;
    }
}
