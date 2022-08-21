package com.miui.gallery.data;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.CityDatabaseHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import miuix.core.util.IOUtils;

/* loaded from: classes.dex */
public class CitySearcher {
    public static CitySearcher sThis;
    public CityDatabaseHelper.CityBoundRectList mCityBoundRects;
    public CityDatabaseHelper mDbHelper;
    public ConcurrentMap<String, CityDatabaseHelper.CityBoundary> mCityBoundaries = new ConcurrentHashMap();
    public PhotoGpsCache mPhotoGpsCache = new PhotoGpsCache();

    /* loaded from: classes.dex */
    public static class PhotoGpsCache extends HashMap<String, String> {
        public static String makeKey(Coordinate coordinate) {
            if (coordinate == null || !coordinate.isValid()) {
                return "";
            }
            return "" + coordinate.latitude + "," + coordinate.longitude;
        }
    }

    public CitySearcher() {
        openDB(GalleryApp.sGetAndroidContext());
    }

    public static synchronized CitySearcher getInstance() {
        CitySearcher citySearcher;
        synchronized (CitySearcher.class) {
            if (sThis == null) {
                sThis = new CitySearcher();
            }
            citySearcher = sThis;
        }
        return citySearcher;
    }

    public final synchronized void appendBoundaryList(ConcurrentMap<String, CityDatabaseHelper.CityBoundary> concurrentMap) {
        if (concurrentMap.size() >= 10) {
            this.mCityBoundaries = concurrentMap;
        } else {
            if (concurrentMap.size() + this.mCityBoundaries.size() > 10) {
                ArrayDeque arrayDeque = new ArrayDeque();
                for (String str : this.mCityBoundaries.keySet()) {
                    if (!concurrentMap.containsKey(str)) {
                        arrayDeque.add(str);
                    }
                }
                while (!arrayDeque.isEmpty() && concurrentMap.size() + arrayDeque.size() > 10) {
                    this.mCityBoundaries.remove(arrayDeque.poll());
                }
            }
            this.mCityBoundaries.putAll(concurrentMap);
        }
    }

    public final ConcurrentMap<String, CityDatabaseHelper.CityBoundary> findBoundaries(CityDatabaseHelper.CityBoundRectList cityBoundRectList) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str : cityBoundRectList.keySet()) {
            CityDatabaseHelper.CityBoundary cityBoundary = this.mCityBoundaries.get(str);
            if (cityBoundary == null) {
                arrayList.add(str);
            } else {
                concurrentHashMap.put(str, cityBoundary);
            }
        }
        ConcurrentMap<String, CityDatabaseHelper.CityBoundary> queryCityBoundary = this.mDbHelper.queryCityBoundary(arrayList);
        if (queryCityBoundary != null && !queryCityBoundary.isEmpty()) {
            concurrentHashMap.putAll(queryCityBoundary);
        }
        return concurrentHashMap;
    }

    public final String findInBoundRects(int i, int i2, CityDatabaseHelper.CityBoundRectList cityBoundRectList) {
        ConcurrentMap<String, CityDatabaseHelper.CityBoundary> findBoundaries;
        if (cityBoundRectList == null || cityBoundRectList.isEmpty() || (findBoundaries = findBoundaries(cityBoundRectList)) == null || findBoundaries.isEmpty()) {
            return null;
        }
        appendBoundaryList(findBoundaries);
        for (CityDatabaseHelper.CityBoundary cityBoundary : findBoundaries.values()) {
            if (cityBoundary.containsIntCoordinate(i, i2)) {
                return cityBoundary.cityId;
            }
        }
        return null;
    }

    public String decode(Coordinate coordinate) {
        if (coordinate == null || !isDBOpened()) {
            return null;
        }
        try {
            String makeKey = PhotoGpsCache.makeKey(coordinate);
            String str = this.mPhotoGpsCache.get(makeKey);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
            int convertIntLat = LocationUtil.convertIntLat(coordinate.latitude);
            int convertIntLat2 = LocationUtil.convertIntLat(coordinate.longitude);
            for (CityDatabaseHelper.CityBoundary cityBoundary : this.mCityBoundaries.values()) {
                if (cityBoundary.containsIntCoordinate(convertIntLat, convertIntLat2)) {
                    return cityBoundary.cityId;
                }
            }
            CityDatabaseHelper.CityBoundRectList cityBoundRects = getCityBoundRects();
            CityDatabaseHelper.CityBoundRectList cityBoundRectList = new CityDatabaseHelper.CityBoundRectList();
            for (CityDatabaseHelper.CityBoundRect cityBoundRect : cityBoundRects.values()) {
                if (cityBoundRect.containsIntCoordinate(convertIntLat, convertIntLat2)) {
                    cityBoundRectList.put(cityBoundRect.cityId, cityBoundRect);
                }
            }
            String findInBoundRects = findInBoundRects(convertIntLat, convertIntLat2, cityBoundRectList);
            if (TextUtils.isEmpty(findInBoundRects)) {
                return null;
            }
            this.mPhotoGpsCache.put(makeKey, findInBoundRects);
            return findInBoundRects;
        } catch (Exception e) {
            DefaultLogger.w("CitySearcher", e);
            return null;
        }
    }

    public final synchronized CityDatabaseHelper.CityBoundRectList getCityBoundRects() {
        CityDatabaseHelper.CityBoundRectList cityBoundRectList = this.mCityBoundRects;
        if (cityBoundRectList != null) {
            return cityBoundRectList;
        }
        CityDatabaseHelper cityDatabaseHelper = this.mDbHelper;
        if (cityDatabaseHelper == null) {
            return null;
        }
        CityDatabaseHelper.CityBoundRectList loadCityBoundRects = cityDatabaseHelper.loadCityBoundRects();
        this.mCityBoundRects = loadCityBoundRects;
        return loadCityBoundRects;
    }

    public synchronized boolean openDB(Context context) {
        CityDatabaseHelper cityDatabaseHelper = this.mDbHelper;
        if (cityDatabaseHelper != null) {
            return cityDatabaseHelper.isDbOpened();
        }
        CityDatabaseHelper cityDatabaseHelper2 = new CityDatabaseHelper(context, new DataBaseFileLoader(context).loadFile());
        this.mDbHelper = cityDatabaseHelper2;
        return cityDatabaseHelper2.isDbOpened();
    }

    public synchronized void preLoadData() {
        if (isDBOpened()) {
            getCityBoundRects();
        }
    }

    public final synchronized boolean isDBOpened() {
        boolean z;
        CityDatabaseHelper cityDatabaseHelper = this.mDbHelper;
        if (cityDatabaseHelper != null) {
            if (cityDatabaseHelper.isDbOpened()) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    /* loaded from: classes.dex */
    public static class DataBaseFileLoader {
        public Context mContext;
        public String mDataBasePath;

        public DataBaseFileLoader(Context context) {
            this.mContext = context;
            this.mDataBasePath = context.getApplicationInfo().dataDir + "/databases";
        }

        public String loadFile() {
            if (isFileCopied() || copyFile()) {
                return this.mDataBasePath + h.g + "city.db";
            }
            return "/data/miui/gallery/city.db";
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r3v1 */
        /* JADX WARN: Type inference failed for: r3v10 */
        /* JADX WARN: Type inference failed for: r3v2 */
        /* JADX WARN: Type inference failed for: r3v3 */
        /* JADX WARN: Type inference failed for: r3v4, types: [java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r3v5, types: [java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r3v7, types: [java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r3v9, types: [java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r7v0, types: [boolean] */
        /* JADX WARN: Type inference failed for: r7v4 */
        /* JADX WARN: Type inference failed for: r7v9 */
        public final boolean copyFile() {
            OutputStream outputStream;
            FileOutputStream fileOutputStream;
            IOException e;
            FileOutputStream fileOutputStream2;
            File file = new File(this.mDataBasePath);
            ?? r3 = "city.db";
            File file2 = new File(file, (String) r3);
            File file3 = new File(file, "city.db.md5");
            try {
                ?? exists = file.exists();
                if (exists == 0) {
                    file.mkdirs();
                }
                if (file2.exists() && !file2.delete()) {
                    return false;
                }
                if (file3.exists()) {
                    if (!file3.delete()) {
                        return false;
                    }
                }
                InputStream inputStream = null;
                try {
                    try {
                        r3 = this.mContext.getAssets().open(r3);
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (IOException e2) {
                    e = e2;
                    r3 = 0;
                    fileOutputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                    outputStream = null;
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);
                    throw th;
                }
                try {
                    fileOutputStream = new FileOutputStream(file2);
                    try {
                        IOUtils.copy(r3, fileOutputStream);
                        IOUtils.closeQuietly((InputStream) r3);
                        IOUtils.closeQuietly(fileOutputStream);
                        try {
                            try {
                                r3 = this.mContext.getAssets().open("city.db.md5");
                                fileOutputStream2 = new FileOutputStream(file3);
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } catch (IOException e3) {
                            e = e3;
                        }
                        try {
                            IOUtils.copy(r3, fileOutputStream2);
                            IOUtils.closeQuietly((InputStream) r3);
                            IOUtils.closeQuietly(fileOutputStream2);
                            return true;
                        } catch (IOException e4) {
                            e = e4;
                            fileOutputStream = fileOutputStream2;
                            DefaultLogger.w("DataBaseFileLoader", e);
                            IOUtils.closeQuietly((InputStream) r3);
                            IOUtils.closeQuietly(fileOutputStream);
                            return false;
                        } catch (Throwable th4) {
                            th = th4;
                            fileOutputStream = fileOutputStream2;
                            IOUtils.closeQuietly((InputStream) r3);
                            IOUtils.closeQuietly(fileOutputStream);
                            throw th;
                        }
                    } catch (IOException e5) {
                        e = e5;
                        DefaultLogger.w("DataBaseFileLoader", e);
                        IOUtils.closeQuietly((InputStream) r3);
                        IOUtils.closeQuietly(fileOutputStream);
                        return false;
                    }
                } catch (IOException e6) {
                    e = e6;
                    fileOutputStream = null;
                    r3 = r3;
                    e = e;
                    DefaultLogger.w("DataBaseFileLoader", e);
                    IOUtils.closeQuietly((InputStream) r3);
                    IOUtils.closeQuietly(fileOutputStream);
                    return false;
                } catch (Throwable th5) {
                    th = th5;
                    exists = 0;
                    inputStream = r3;
                    outputStream = exists;
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);
                    throw th;
                }
            } catch (Exception e7) {
                DefaultLogger.w("DataBaseFileLoader", e7);
                return false;
            }
        }

        public final boolean isFileCopied() {
            InputStream inputStream;
            FileInputStream fileInputStream;
            if (!new File(this.mDataBasePath, "city.db").exists()) {
                return false;
            }
            File file = new File(this.mDataBasePath, "city.db.md5");
            if (!file.exists()) {
                return false;
            }
            InputStream inputStream2 = null;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (IOException e) {
                e = e;
                inputStream = null;
            } catch (Throwable th) {
                th = th;
                inputStream = null;
            }
            try {
                inputStream2 = this.mContext.getAssets().open("city.db.md5");
                boolean compareStream = compareStream(fileInputStream, inputStream2);
                IOUtils.closeQuietly(fileInputStream);
                IOUtils.closeQuietly(inputStream2);
                return compareStream;
            } catch (IOException e2) {
                e = e2;
                inputStream = inputStream2;
                inputStream2 = fileInputStream;
                try {
                    DefaultLogger.w("DataBaseFileLoader", e);
                    IOUtils.closeQuietly(inputStream2);
                    IOUtils.closeQuietly(inputStream);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    IOUtils.closeQuietly(inputStream2);
                    IOUtils.closeQuietly(inputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = inputStream2;
                inputStream2 = fileInputStream;
                IOUtils.closeQuietly(inputStream2);
                IOUtils.closeQuietly(inputStream);
                throw th;
            }
        }

        public final boolean compareStream(InputStream inputStream, InputStream inputStream2) throws IOException {
            int read;
            if (!(inputStream instanceof BufferedInputStream)) {
                inputStream = new BufferedInputStream(inputStream);
            }
            if (!(inputStream2 instanceof BufferedInputStream)) {
                inputStream2 = new BufferedInputStream(inputStream2);
            }
            do {
                read = inputStream.read();
                if (read == -1) {
                    return inputStream2.read() == -1;
                }
            } while (read == inputStream2.read());
            return false;
        }
    }
}
