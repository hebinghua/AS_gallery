package com.miui.bugreport.logprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/* loaded from: classes.dex */
public class DumpLogProvider extends ContentProvider {
    public static final File a = new File(h.g);
    public static volatile HashMap<String, File> b = new HashMap<>();
    public static volatile boolean c;

    public static File a(File file, String... strArr) {
        for (String str : strArr) {
            if (str != null) {
                file = new File(file, str);
            }
        }
        return file;
    }

    public static HashMap<String, File> a(Context context, String str) {
        XmlResourceParser loadXmlMetaData;
        HashMap<String, File> hashMap = new HashMap<>();
        try {
            loadXmlMetaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).loadXmlMetaData(context.getPackageManager(), str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loadXmlMetaData == null) {
            return hashMap;
        }
        while (true) {
            int next = loadXmlMetaData.next();
            if (next == 1) {
                break;
            } else if (next == 2) {
                String name = loadXmlMetaData.getName();
                File file = null;
                String attributeValue = loadXmlMetaData.getAttributeValue(null, "name");
                String attributeValue2 = loadXmlMetaData.getAttributeValue(null, nexExportFormat.TAG_FORMAT_PATH);
                if ("root-path".equals(name)) {
                    file = a;
                } else if ("files-path".equals(name)) {
                    file = context.getFilesDir();
                } else if ("cache-path".equals(name)) {
                    file = context.getCacheDir();
                } else if ("external-path".equals(name)) {
                    file = Environment.getExternalStorageDirectory();
                } else if ("external-files-path".equals(name)) {
                    File[] externalFilesDirs = context.getExternalFilesDirs(null);
                    if (externalFilesDirs.length > 0) {
                        file = externalFilesDirs[0];
                    }
                } else if ("external-cache-path".equals(name)) {
                    File[] externalCacheDirs = context.getExternalCacheDirs();
                    if (externalCacheDirs.length > 0) {
                        file = externalCacheDirs[0];
                    }
                } else if (Build.VERSION.SDK_INT >= 21 && "external-media-path".equals(name)) {
                    File[] externalMediaDirs = context.getExternalMediaDirs();
                    if (externalMediaDirs.length > 0) {
                        file = externalMediaDirs[0];
                    }
                }
                if (file != null) {
                    a(attributeValue, a(file, attributeValue2));
                }
            }
        }
        return hashMap;
    }

    public static void a(String str, File file) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            b.put(str, file.getCanonicalFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean a(Context context) {
        try {
            return (context.getPackageManager().getPackageInfo("com.miui.bugreport", 0).applicationInfo.flags & 1) != 0;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
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
        c = a(getContext());
        b.putAll(a(getContext(), "com.miui.bugreport.DEFAULT_LOG_DIR"));
        b.putAll(a(getContext(), "com.miui.bugreport.LOG_DIR"));
        return true;
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String str) {
        if (!c) {
            return null;
        }
        String path = Uri.parse(Uri.decode(uri.toString())).getPath();
        if (TextUtils.isEmpty(path)) {
            throw new FileNotFoundException();
        }
        try {
            File file = new File(path);
            String canonicalPath = file.getCanonicalPath();
            for (Map.Entry<String, File> entry : b.entrySet()) {
                if (canonicalPath.startsWith(entry.getValue().getPath())) {
                    return ParcelFileDescriptor.open(file, 268435456);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (!c || Build.VERSION.SDK_INT < 23) {
            return null;
        }
        LinkedList linkedList = new LinkedList();
        ArrayList<String> arrayList = new ArrayList<>();
        for (Map.Entry<String, File> entry : b.entrySet()) {
            File value = entry.getValue();
            if (value.exists()) {
                linkedList.offer(value);
                while (linkedList.size() != 0) {
                    File file = (File) linkedList.poll();
                    if (file.isFile()) {
                        arrayList.add(file.getPath());
                    } else {
                        File[] listFiles = file.listFiles();
                        if (listFiles != null) {
                            for (File file2 : listFiles) {
                                if (file2.isFile()) {
                                    arrayList.add(file2.getPath());
                                } else if (file2.isDirectory()) {
                                    linkedList.offer(file2);
                                }
                            }
                        }
                    }
                }
            }
        }
        MatrixCursor matrixCursor = new MatrixCursor(new String[0]);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("logDir", arrayList);
        matrixCursor.setExtras(bundle);
        return matrixCursor;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
