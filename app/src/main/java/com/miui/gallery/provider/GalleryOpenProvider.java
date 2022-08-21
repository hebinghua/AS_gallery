package com.miui.gallery.provider;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.TextUtils;
import androidx.exifinterface.media.ExifInterface;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.assistant.utils.AnalyticUtils;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.db.sqlite3.GallerySQLiteHelper;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.scanner.utils.DefaultImageValueCalculator;
import com.miui.gallery.search.utils.QueryTagCountHelper;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.VideoAttrsReader;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class GalleryOpenProvider extends ContentProvider {
    public SupportSQLiteDatabase mCacheDB;
    public SupportSQLiteDatabase mDatabase;
    public UriMatcher mUriMatcher = new UriMatcher(-1);
    public static final String TAG = GalleryOpenProvider.class.getSimpleName();
    public static final String[] NEED_RETURN_CONTENT_URI_SYSTEM_APPS = {"com.android.bluetooth"};
    public static final String[] CLOUD_COLUMNS = {j.c, "serverType", "exifGPSLatitude", "exifGPSLatitudeRef", "exifGPSLongitude", "exifGPSLongitudeRef", "dateTaken", "exifOrientation", "localGroupId", "microthumbfile", "thumbnailFile", "localFile", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "fileName", "title", "dateModified", "mimeType", "exifImageWidth", "exifImageLength", "duration"};
    public static final String[] MEDIA_COLUMNS = {j.c, "_data", "_size", "_display_name", "title", "date_added", "date_modified", "mime_type", nexExportFormat.TAG_FORMAT_WIDTH, nexExportFormat.TAG_FORMAT_HEIGHT, "description", "picasa_id", "isprivate", "latitude", "longitude", "datetaken", "orientation", "mini_thumb_magic", "bucket_id", "bucket_display_name", "bookmark", "album", "artist", "category", "duration", "language", "resolution", "tags", "mini_thumb_data", "media_type"};
    public static final String[] PRIVACY_COLUMNS = {"latitude", "longitude"};

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        initialize();
        return true;
    }

    public final void initialize() {
        this.mUriMatcher.addURI("com.miui.gallery.open", "raw/*", 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.String[][], java.io.Serializable] */
    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        Parcelable parcelable;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -586299021:
                if (str.equals("calculate_roi")) {
                    c = 0;
                    break;
                }
                break;
            case -280967762:
                if (str.equals("batchQuery")) {
                    c = 1;
                    break;
                }
                break;
            case 1359546920:
                if (str.equals("secureCardCount")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                AnalyticUtils.CropRegionInfo imageCropRegion = AnalyticUtils.getImageCropRegion(bundle.getLong("media_id"), bundle.getInt("media_width"), bundle.getInt("media_height"), bundle.getInt("media_orientation"), AnalyticUtils.RegionType.getRegionTypeById(bundle.getInt("region_type", 0)), AnalyticUtils.DataFetcherType.DATA_FETCHER_PROVIDER);
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("result_roi", imageCropRegion);
                return bundle2;
            case 1:
                if (!TextUtils.equals(getCallingPackage(), "com.xiaomi.mirror")) {
                    return null;
                }
                Parcelable[] parcelableArray = bundle.getParcelableArray("uris");
                String[] stringArray = bundle.getStringArray("projection");
                String string = bundle.getString("selection");
                String[] stringArray2 = bundle.getStringArray("selectionArgs");
                String string2 = bundle.getString("sortOrder");
                ?? r0 = (String[][]) Array.newInstance(String.class, parcelableArray.length, stringArray.length);
                for (int i = 0; i < parcelableArray.length; i++) {
                    Cursor query = query((Uri) parcelableArray[i], stringArray, string, stringArray2, string2);
                    if (query != null) {
                        try {
                            query.moveToFirst();
                            for (int i2 = 0; i2 < stringArray.length; i2++) {
                                r0[i][i2] = query.getString(query.getColumnIndex(stringArray[i2]));
                            }
                        } catch (Throwable th) {
                            try {
                                query.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    } else if (query == null) {
                    }
                    query.close();
                }
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("result", r0);
                return bundle3;
            case 2:
                if (TextUtils.equals(getCallingPackage(), "com.miui.securitycenter") && (parcelable = bundle.getParcelable("extra_receiver")) != null && (parcelable instanceof ResultReceiver)) {
                    new QueryCardCountTask((ResultReceiver) parcelable).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                }
                return null;
            default:
                return null;
        }
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        int i;
        String str3 = TAG;
        DefaultLogger.d(str3, "query uri: %s, %s, %s", uri, Arrays.toString(strArr), str);
        synchronized (this) {
            if (this.mCacheDB == null) {
                this.mCacheDB = prepareDB();
            }
        }
        MediaInfo query = query(uri);
        if (query == null) {
            DefaultLogger.e(str3, "query failed, return a empty");
            return new MatrixCursor(strArr);
        }
        if (query.mType == 3) {
            String[] strArr3 = {"_display_name", "_size"};
            File file = new File(uri.getLastPathSegment());
            if (strArr == null) {
                strArr = strArr3;
            }
            String[] strArr4 = new String[strArr.length];
            Object[] objArr = new Object[strArr.length];
            int i2 = 0;
            for (String str4 : strArr) {
                if ("_display_name".equals(str4)) {
                    strArr4[i2] = "_display_name";
                    i = i2 + 1;
                    objArr[i2] = file.getName();
                } else if ("_size".equals(str4)) {
                    strArr4[i2] = "_size";
                    i = i2 + 1;
                    objArr[i2] = Long.valueOf(file.length());
                }
                i2 = i;
            }
            String[] copyOf = copyOf(strArr4, i2);
            Object[] copyOf2 = copyOf(objArr, i2);
            MatrixCursor matrixCursor = new MatrixCursor(copyOf, 1);
            matrixCursor.addRow(copyOf2);
            return matrixCursor;
        }
        long insert = this.mCacheDB.insert("media", 5, query.toContentValue());
        if (insert > 0) {
            DefaultLogger.d(str3, "insert cache success: %d", Long.valueOf(insert));
            return this.mCacheDB.query(SupportSQLiteQueryBuilder.builder(query.mType == 1 ? "image" : "video").columns(strArr).selection("_id=?", new String[]{Long.toString(insert)}).create());
        }
        DefaultLogger.e(str3, "insert failed, return null");
        return new MatrixCursor(strArr);
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        String str = TAG;
        DefaultLogger.d(str, "query type for %s", uri);
        MediaInfo query = query(uri);
        if (query != null) {
            return query.mMimeType;
        }
        DefaultLogger.e(str, "query type for %s failed.", uri);
        return "*/*";
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        DefaultLogger.d(TAG, "open file for: %s", uri.toString());
        File file = new File(uri.getLastPathSegment());
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + file);
        }
        return ParcelFileDescriptor.open(file, 268435456);
    }

    public final MediaInfo query(Uri uri) {
        Cursor query;
        if (this.mDatabase == null) {
            this.mDatabase = GalleryDBHelper.getInstance().getReadableDatabase();
        }
        String lastPathSegment = uri.getLastPathSegment();
        if (this.mUriMatcher.match(uri) != 1) {
            query = null;
        } else {
            DefaultLogger.d(TAG, "internal query for file: %s", lastPathSegment);
            query = this.mDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(CLOUD_COLUMNS).selection("microthumbfile=? COLLATE NOCASE OR thumbnailFile = ? COLLATE NOCASE OR localFile = ? COLLATE NOCASE", new String[]{lastPathSegment, lastPathSegment, lastPathSegment}).create());
        }
        try {
            MediaInfo mediaInfo = new MediaInfo();
            if (query != null && query.moveToNext() && mediaInfo.init(query)) {
                DefaultLogger.d(TAG, "translate cursor to MediaInfo success: %s", mediaInfo);
                query.close();
                return mediaInfo;
            } else if (mediaInfo.init(lastPathSegment)) {
                DefaultLogger.d(TAG, "translate file to MediaInfo success: %s", mediaInfo);
                return mediaInfo;
            } else {
                DefaultLogger.d(TAG, "translate into MediaInfo failed: %s", uri);
                if (query != null) {
                    query.close();
                }
                return null;
            }
        } finally {
            if (query != null) {
                query.close();
            }
        }
    }

    public static ContentValues filterPrivateColumns(ContentValues contentValues) {
        ContentValues contentValues2 = new ContentValues(contentValues);
        for (String str : PRIVACY_COLUMNS) {
            contentValues2.remove(str);
        }
        return contentValues2;
    }

    /* loaded from: classes2.dex */
    public class MediaInfo {
        public String mAlbumName;
        public long mBucketId;
        public String mBucketName;
        public Long mDateModified;
        public Long mDateTaken;
        public Long mDuration;
        public String mFileName;
        public Integer mHeight;
        public long mId;
        public Double mLatitude;
        public Double mLongitude;
        public String mMimeType;
        public Integer mOrientation;
        public String mPath;
        public String mResolution;
        public Long mSize;
        public String mTitle;
        public int mType;
        public Integer mWidth;

        public MediaInfo() {
        }

        public boolean init(String str) {
            DefaultLogger.d(GalleryOpenProvider.TAG, "query media info for file: %s", str);
            File file = new File(str);
            if (!file.exists()) {
                DefaultLogger.w(GalleryOpenProvider.TAG, "file [%s] not exists.", str);
                return false;
            }
            this.mId = Long.MAX_VALUE;
            String mimeType = BaseFileMimeUtil.getMimeType(str);
            this.mMimeType = mimeType;
            if (!BaseFileMimeUtil.isImageFromMimeType(mimeType) && !BaseFileMimeUtil.isVideoFromMimeType(this.mMimeType) && !BaseFileMimeUtil.isPdfFromMimeType(this.mMimeType)) {
                String mimeTypeByParseFile = BaseFileMimeUtil.getMimeTypeByParseFile(str);
                this.mMimeType = mimeTypeByParseFile;
                if (!BaseFileMimeUtil.isImageFromMimeType(mimeTypeByParseFile) && !BaseFileMimeUtil.isVideoFromMimeType(this.mMimeType) && !BaseFileMimeUtil.isPdfFromMimeType(this.mMimeType)) {
                    DefaultLogger.w(GalleryOpenProvider.TAG, "file [%s] [%s] is not a support mimeType.", str, this.mMimeType);
                    return false;
                }
            }
            this.mType = BaseFileMimeUtil.isImageFromMimeType(this.mMimeType) ? 1 : BaseFileMimeUtil.isVideoFromMimeType(this.mMimeType) ? 2 : 3;
            this.mBucketId = Long.MAX_VALUE;
            this.mBucketName = file.getParentFile().getName();
            this.mPath = str;
            this.mSize = Long.valueOf(file.length());
            String name = file.getName();
            this.mFileName = name;
            this.mTitle = BaseFileUtils.getFileTitle(name);
            this.mDateModified = Long.valueOf(file.lastModified());
            int i = this.mType;
            if (i == 1) {
                if (BaseFileMimeUtil.hasExif(str)) {
                    DefaultLogger.d(GalleryOpenProvider.TAG, "reading exif");
                    ExifInterface mo1692create = ExifUtil.sSupportExifCreator.mo1692create(str);
                    if (mo1692create == null) {
                        DefaultLogger.e(GalleryOpenProvider.TAG, "Failed to read exif for path %s", str);
                    } else {
                        double[] latLong = mo1692create.getLatLong();
                        if (latLong != null) {
                            this.mLatitude = Double.valueOf(latLong[0]);
                            this.mLongitude = Double.valueOf(latLong[1]);
                        }
                        this.mOrientation = Integer.valueOf(ExifUtil.getRotationDegrees(mo1692create));
                        this.mDateTaken = Long.valueOf(DefaultImageValueCalculator.getDateTaken(mo1692create, this.mDateModified.longValue()));
                        this.mWidth = Integer.valueOf(mo1692create.getAttributeInt("ImageWidth", 0));
                        this.mHeight = Integer.valueOf(mo1692create.getAttributeInt("ImageLength", 0));
                        if (this.mWidth.intValue() <= 0 || this.mHeight.intValue() <= 0) {
                            decodeBounds(str);
                        }
                    }
                } else {
                    DefaultLogger.d(GalleryOpenProvider.TAG, "no exif found");
                    decodeBounds(str);
                    this.mOrientation = 0;
                    this.mDateTaken = this.mDateModified;
                }
            } else if (i == 2) {
                DefaultLogger.d(GalleryOpenProvider.TAG, "is video, reading video info");
                this.mAlbumName = this.mBucketName;
                try {
                    VideoAttrsReader read = VideoAttrsReader.read(str);
                    String title = read.getTitle();
                    if (title != null) {
                        this.mTitle = title;
                    }
                    this.mDateTaken = Long.valueOf(read.getDateTaken());
                    this.mDuration = Long.valueOf(read.getDuration());
                    this.mWidth = Integer.valueOf(read.getVideoWidth());
                    this.mHeight = Integer.valueOf(read.getVideoHeight());
                    this.mOrientation = Integer.valueOf(ExifUtil.exifOrientationToDegrees(read.getOrientation()));
                    this.mResolution = this.mWidth + "x" + this.mHeight;
                } catch (Exception e) {
                    DefaultLogger.w(GalleryOpenProvider.TAG, e);
                }
            }
            return true;
        }

        public boolean init(Cursor cursor) {
            this.mId = cursor.getLong(0);
            int i = cursor.getInt(1);
            if (i == 1) {
                this.mType = 1;
            } else if (i != 2) {
                DefaultLogger.e(GalleryOpenProvider.TAG, "%d is not a media type", Integer.valueOf(i));
                return false;
            } else {
                this.mType = 2;
            }
            String string = cursor.getString(2);
            String string2 = cursor.getString(3);
            if (string != null && string2 != null) {
                this.mLatitude = Double.valueOf(LocationUtil.convertRationalLatLonToDouble(string, string2));
            }
            String string3 = cursor.getString(4);
            String string4 = cursor.getString(5);
            if (string3 != null && string4 != null) {
                this.mLongitude = Double.valueOf(LocationUtil.convertRationalLatLonToDouble(string3, string4));
            }
            this.mDateTaken = Numbers.getLong(cursor, 6);
            this.mBucketId = cursor.getLong(8);
            this.mFileName = cursor.getString(13);
            this.mTitle = cursor.getString(14);
            this.mDateModified = Long.valueOf(cursor.getLong(15));
            this.mMimeType = cursor.getString(16);
            Cursor query = GalleryOpenProvider.this.mDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(new String[]{"fileName"}).selection("_id=?", new String[]{Long.toString(this.mBucketId)}).create());
            try {
                if (query.moveToNext()) {
                    this.mBucketName = query.getString(0);
                }
                query.close();
                if (this.mType == 2) {
                    this.mDuration = Long.valueOf(cursor.getLong(19));
                }
                String string5 = cursor.getString(11);
                this.mPath = string5;
                if (!TextUtils.isEmpty(string5)) {
                    this.mSize = Long.valueOf(cursor.getLong(12));
                    this.mWidth = Integer.valueOf(cursor.getInt(17));
                    this.mHeight = Integer.valueOf(cursor.getInt(18));
                    if (!cursor.isNull(7)) {
                        this.mOrientation = Integer.valueOf(ExifUtil.exifOrientationToDegrees(cursor.getShort(7)));
                    }
                    if (this.mType == 2) {
                        this.mResolution = this.mWidth + "x" + this.mHeight;
                        this.mAlbumName = this.mBucketName;
                    }
                    return true;
                } else if (this.mType == 2) {
                    DefaultLogger.e(GalleryOpenProvider.TAG, "current type is video, but no file path");
                    return false;
                } else {
                    String string6 = cursor.getString(10);
                    this.mPath = string6;
                    if (TextUtils.isEmpty(string6)) {
                        String string7 = cursor.getString(9);
                        this.mPath = string7;
                        if (TextUtils.isEmpty(string7)) {
                            DefaultLogger.e(GalleryOpenProvider.TAG, "no path return");
                            return false;
                        }
                    }
                    return fillByFile(this.mPath);
                }
            } catch (Throwable th) {
                query.close();
                throw th;
            }
        }

        public boolean fillByFile(String str) {
            DefaultLogger.d(GalleryOpenProvider.TAG, "try fill with %s", str);
            this.mPath = str;
            File file = new File(str);
            if (!file.exists()) {
                DefaultLogger.e(GalleryOpenProvider.TAG, "file not exists");
                return false;
            }
            String fileName = BaseFileUtils.getFileName(this.mPath);
            this.mFileName = fileName;
            this.mTitle = BaseFileUtils.getFileTitle(fileName);
            this.mSize = Long.valueOf(file.length());
            this.mMimeType = BaseFileMimeUtil.getMimeType(this.mPath);
            this.mOrientation = Integer.valueOf(ExifUtil.getRotationDegrees(ExifUtil.sSupportExifCreator.mo1692create(str)));
            decodeBounds(str);
            return true;
        }

        public final void decodeBounds(String str) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(this.mPath, options);
            this.mWidth = Integer.valueOf(options.outWidth);
            this.mHeight = Integer.valueOf(options.outHeight);
        }

        public ContentValues toContentValue() {
            ContentValues contentValues = new ContentValues(GalleryOpenProvider.MEDIA_COLUMNS.length);
            for (int i = 0; i < GalleryOpenProvider.MEDIA_COLUMNS.length; i++) {
                String str = GalleryOpenProvider.MEDIA_COLUMNS[i];
                switch (i) {
                    case 1:
                        contentValues.put(str, this.mPath);
                        break;
                    case 2:
                        contentValues.put(str, this.mSize);
                        break;
                    case 3:
                        contentValues.put(str, this.mFileName);
                        break;
                    case 4:
                        contentValues.put(str, this.mTitle);
                        break;
                    case 5:
                        contentValues.put(str, this.mDateTaken);
                        break;
                    case 6:
                        contentValues.put(str, this.mDateModified);
                        break;
                    case 7:
                        contentValues.put(str, this.mMimeType);
                        break;
                    case 8:
                        contentValues.put(str, this.mWidth);
                        break;
                    case 9:
                        contentValues.put(str, this.mHeight);
                        break;
                    case 10:
                    case 11:
                    case 12:
                    case 17:
                    case 20:
                    case 22:
                    case 23:
                    case 25:
                    case 27:
                    case 28:
                    default:
                        contentValues.putNull(str);
                        break;
                    case 13:
                        contentValues.put(str, this.mLatitude);
                        break;
                    case 14:
                        contentValues.put(str, this.mLongitude);
                        break;
                    case 15:
                        contentValues.put(str, this.mDateTaken);
                        break;
                    case 16:
                        contentValues.put(str, this.mOrientation);
                        break;
                    case 18:
                        contentValues.put(str, Long.valueOf(this.mBucketId));
                        break;
                    case 19:
                        contentValues.put(str, this.mBucketName);
                        break;
                    case 21:
                        contentValues.put(str, this.mAlbumName);
                        break;
                    case 24:
                        contentValues.put(str, this.mDuration);
                        break;
                    case 26:
                        contentValues.put(str, this.mResolution);
                        break;
                    case 29:
                        contentValues.put(str, Integer.valueOf(this.mType));
                        break;
                }
            }
            DefaultLogger.d(GalleryOpenProvider.TAG, "trans cursor to %s", GalleryOpenProvider.filterPrivateColumns(contentValues));
            return contentValues;
        }

        public String toString() {
            return "MediaInfo{mId=" + this.mId + ", mType=" + this.mType + ", mDateTaken=" + this.mDateTaken + ", mOrientation=" + this.mOrientation + ", mBucketId=" + this.mBucketId + ", mBucketName='" + this.mBucketName + CoreConstants.SINGLE_QUOTE_CHAR + ", mPath='" + this.mPath + CoreConstants.SINGLE_QUOTE_CHAR + ", mSize=" + this.mSize + ", mFileName='" + this.mFileName + CoreConstants.SINGLE_QUOTE_CHAR + ", mTitle='" + this.mTitle + CoreConstants.SINGLE_QUOTE_CHAR + ", mDateModified=" + this.mDateModified + ", mMimeType='" + this.mMimeType + CoreConstants.SINGLE_QUOTE_CHAR + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mDuration=" + this.mDuration + ", mAlbumName='" + this.mAlbumName + CoreConstants.SINGLE_QUOTE_CHAR + ", mResolution='" + this.mResolution + CoreConstants.SINGLE_QUOTE_CHAR + '}';
        }
    }

    public static SupportSQLiteDatabase prepareDB() {
        SupportSQLiteDatabase createInMemory = GallerySQLiteHelper.createInMemory();
        createInMemory.execSQL("CREATE TABLE media (_id INTEGER PRIMARY KEY AUTOINCREMENT,_data TEXT UNIQUE COLLATE NOCASE,_size INTEGER,date_added INTEGER,date_modified INTEGER,mime_type TEXT,title TEXT,description TEXT,_display_name TEXT,picasa_id TEXT,orientation INTEGER,latitude DOUBLE,longitude DOUBLE,datetaken INTEGER,mini_thumb_magic INTEGER,bucket_id TEXT,bucket_display_name TEXT,isprivate INTEGER,duration INTEGER,bookmark INTEGER,artist TEXT,album TEXT,resolution TEXT,tags TEXT,category TEXT,language TEXT,mini_thumb_data TEXT,media_type INTEGER,width INTEGER,height INTEGER)");
        createInMemory.execSQL("CREATE VIEW image AS SELECT _id,_data,_size,_display_name,mime_type,title,date_added,date_modified,description,picasa_id,isprivate,latitude,longitude,datetaken,orientation,mini_thumb_magic,bucket_id,bucket_display_name,width,height FROM media WHERE media_type=1");
        createInMemory.execSQL("CREATE VIEW video AS SELECT _id,_data,_display_name,_size,mime_type,date_added,date_modified,title,duration,artist,album,resolution,description,isprivate,tags,category,language,mini_thumb_data,latitude,longitude,datetaken,mini_thumb_magic,bucket_id,bucket_display_name,bookmark,width,height FROM media WHERE media_type=2");
        return createInMemory;
    }

    public static Uri translateToContent(String str) {
        return Uri.parse("content://com.miui.gallery.open/raw").buildUpon().appendPath(str).build();
    }

    public static boolean needReturnContentUri() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public static boolean needReturnContentUri(Context context, String str) {
        return needReturnContentUri() || isNeedReturnContentUriApp(context, str);
    }

    public static boolean needReturnContentUri(Context context, Intent intent) {
        ComponentName component;
        if (needReturnContentUri()) {
            return true;
        }
        String str = intent.getPackage();
        if (TextUtils.isEmpty(str) && (component = intent.getComponent()) != null) {
            str = component.getPackageName();
        }
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("no packageName assigned to intent");
        }
        return isNeedReturnContentUriApp(context, str);
    }

    public static boolean isNeedReturnContentUriApp(Context context, String str) {
        if (!isSystemApp(context, str)) {
            return true;
        }
        for (String str2 : NEED_RETURN_CONTENT_URI_SYSTEM_APPS) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSystemApp(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.i(TAG, "no package name");
            return false;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 0);
            if ((applicationInfo == null || (applicationInfo.flags & 1) == 0) ? false : true) {
                DefaultLogger.d(TAG, "system app: %s", str);
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            DefaultLogger.w(TAG, e);
        }
        DefaultLogger.d(TAG, "data app: %s", str);
        return false;
    }

    public static String[] copyOf(String[] strArr, int i) {
        String[] strArr2 = new String[i];
        System.arraycopy(strArr, 0, strArr2, 0, i);
        return strArr2;
    }

    public static Object[] copyOf(Object[] objArr, int i) {
        Object[] objArr2 = new Object[i];
        System.arraycopy(objArr, 0, objArr2, 0, i);
        return objArr2;
    }

    /* loaded from: classes2.dex */
    public static class QueryCardCountTask extends AsyncTask<Void, Void, Void> {
        public ResultReceiver mResultReceiver;

        public QueryCardCountTask(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            int i;
            int i2 = -1;
            if (BaseGalleryPreferences.CTA.canConnectNetwork()) {
                i2 = QueryTagCountHelper.querySecureCardCount();
                i = 0;
            } else {
                i = -1;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("result", i2);
            this.mResultReceiver.send(i, bundle);
            return null;
        }
    }
}
