package com.miui.gallery.storage.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.constants.AndroidStorageConstants;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/* loaded from: classes2.dex */
public class Utils {
    public static final String[] MEDIA_COLLECTIONS = {Environment.DIRECTORY_DCIM, Environment.DIRECTORY_PICTURES};
    public static final LazyValue<Context, String[]> APP_SPECIFIC_EXTERNAL_DIRECTORY = new AnonymousClass1();
    public static final LazyValue<Context, String> APP_SPECIFIC_INTERNAL_DIRECTORY = new LazyValue<Context, String>() { // from class: com.miui.gallery.storage.utils.Utils.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public String mo1272onInit(Context context) {
            File parentFile;
            File cacheDir = context.getCacheDir();
            if (cacheDir == null || (parentFile = cacheDir.getParentFile()) == null) {
                return null;
            }
            return parentFile.getAbsolutePath();
        }
    };
    public static final LazyValue<Context, String[]> OTHER_APP_SPECIFIC_EXTERNAL_DIRECTORY = new AnonymousClass3();
    public static final LazyValue<Context, String> OTHER_APP_SPECIFIC_INTERNAL_DIRECTORY = new LazyValue<Context, String>() { // from class: com.miui.gallery.storage.utils.Utils.4
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public String mo1272onInit(Context context) {
            File parentFile;
            File cacheDir = context.getCacheDir();
            if (cacheDir == null || (parentFile = cacheDir.getParentFile()) == null) {
                return null;
            }
            return parentFile.getParent();
        }
    };

    /* renamed from: com.miui.gallery.storage.utils.Utils$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends LazyValue<Context, String[]> {
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public String[] mo1272onInit(Context context) {
            File parentFile;
            File[] externalCacheDirs = context.getExternalCacheDirs();
            Stream.Builder builder = Stream.builder();
            for (File file : externalCacheDirs) {
                if (file != null && (parentFile = file.getParentFile()) != null) {
                    builder.add(parentFile.getAbsolutePath());
                }
            }
            return (String[]) builder.build().filter(Utils$1$$ExternalSyntheticLambda1.INSTANCE).toArray(Utils$1$$ExternalSyntheticLambda0.INSTANCE);
        }

        public static /* synthetic */ boolean lambda$onInit$0(String str) {
            return !TextUtils.isEmpty(str);
        }

        public static /* synthetic */ String[] lambda$onInit$1(int i) {
            return new String[i];
        }
    }

    /* renamed from: com.miui.gallery.storage.utils.Utils$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends LazyValue<Context, String[]> {
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public String[] mo1272onInit(Context context) {
            File[] externalCacheDirs = context.getExternalCacheDirs();
            Stream.Builder builder = Stream.builder();
            for (File file : externalCacheDirs) {
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    builder.add(parentFile.getParent());
                }
            }
            return (String[]) builder.build().filter(Utils$3$$ExternalSyntheticLambda1.INSTANCE).toArray(Utils$3$$ExternalSyntheticLambda0.INSTANCE);
        }

        public static /* synthetic */ boolean lambda$onInit$0(String str) {
            return !TextUtils.isEmpty(str);
        }

        public static /* synthetic */ String[] lambda$onInit$1(int i) {
            return new String[i];
        }
    }

    public static boolean isUnderMediaCollection(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String str2 : MEDIA_COLLECTIONS) {
            if (BaseFileUtils.contains(str2, str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUnderStandardCollection(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String str2 : AndroidStorageConstants.STANDARD_DIRECTORIES) {
            if (BaseFileUtils.contains(str2, str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUnderAppSpecificDirectory(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String str2 : APP_SPECIFIC_EXTERNAL_DIRECTORY.get(context)) {
            if (BaseFileUtils.contains(str2, str)) {
                return true;
            }
        }
        return BaseFileUtils.contains(APP_SPECIFIC_INTERNAL_DIRECTORY.get(context), str);
    }

    public static boolean isUnderOtherAppSpecificDirectory(Context context, String str) {
        if (!TextUtils.isEmpty(str) && !isUnderAppSpecificDirectory(context, str)) {
            for (String str2 : OTHER_APP_SPECIFIC_EXTERNAL_DIRECTORY.get(context)) {
                if (BaseFileUtils.contains(str2, str)) {
                    return true;
                }
            }
            return BaseFileUtils.contains(OTHER_APP_SPECIFIC_INTERNAL_DIRECTORY.get(context), str);
        }
        return false;
    }

    public static boolean triggerMediaScan(Context context, DocumentFile documentFile) {
        if (documentFile == null) {
            DefaultLogger.w("Utils", "try to trigger scan for an null file.");
            return false;
        }
        String decode = Uri.decode(String.valueOf(documentFile.getUri()));
        int i = AnonymousClass7.$SwitchMap$com$miui$gallery$util$Scheme[Scheme.ofUri(decode).ordinal()];
        if (i == 1) {
            String crop = Scheme.FILE.crop(decode);
            if (isUnderAppSpecificDirectory(context, crop) || isUnderOtherAppSpecificDirectory(context, crop)) {
                return false;
            }
            if (documentFile.exists() && !documentFile.isFile()) {
                notifySystemScanFolder(context, crop);
                return true;
            }
            String mimeType = BaseFileMimeUtil.getMimeType(crop);
            if (!BaseFileMimeUtil.isImageFromMimeType(mimeType) && !BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
                return false;
            }
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            MediaScannerConnection.scanFile(context, new String[]{crop}, new String[]{BaseFileMimeUtil.getMimeType(crop)}, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.miui.gallery.storage.utils.Utils.5
                @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                public void onScanCompleted(String str, Uri uri) {
                    DefaultLogger.v("Utils", "onScanCompleted, path:[%s], uri:[%s].", str, uri);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await(2L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                DefaultLogger.w("Utils", e);
            }
            return true;
        } else if (i != 2 || Build.VERSION.SDK_INT < 30 || !TextUtils.equals(Uri.parse(decode).getAuthority(), "media")) {
            return false;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("is_pending", (Integer) 0);
            return 1 == context.getContentResolver().update(documentFile.getUri(), contentValues, null);
        }
    }

    /* renamed from: com.miui.gallery.storage.utils.Utils$7  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass7 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$util$Scheme;

        static {
            int[] iArr = new int[Scheme.values().length];
            $SwitchMap$com$miui$gallery$util$Scheme = iArr;
            try {
                iArr[Scheme.FILE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$Scheme[Scheme.CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static void notifySystemScanFolder(Context context, String str) {
        switch (Build.VERSION.SDK_INT) {
            case 26:
            case 27:
            case 28:
            case 29:
                notifySystemScanFolderByBroadcast(context, str);
                return;
            default:
                notifySystemScanFolderByMediaScannerConnection(context, str);
                return;
        }
    }

    public static void notifySystemScanFolderByBroadcast(Context context, String str) {
        Intent intent = new Intent("miui.intent.action.MEDIA_SCANNER_SCAN_FOLDER");
        if (Build.VERSION.SDK_INT <= 28) {
            intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
        } else {
            intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaReceiver");
        }
        intent.setData(Uri.fromFile(new File(str)));
        context.sendBroadcast(intent);
    }

    public static void notifySystemScanFolderByMediaScannerConnection(Context context, String str) {
        MediaScannerConnection.scanFile(context, new String[]{str}, new String[]{BaseFileMimeUtil.getMimeType(str)}, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.miui.gallery.storage.utils.Utils.6
            @Override // android.media.MediaScannerConnection.OnScanCompletedListener
            public void onScanCompleted(String str2, Uri uri) {
                DefaultLogger.v("Utils", "onScanCompleted, path:[%s], uri:[%s].", str2, uri);
            }
        });
    }
}
