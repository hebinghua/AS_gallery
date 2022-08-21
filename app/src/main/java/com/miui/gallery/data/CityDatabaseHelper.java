package com.miui.gallery.data;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.data.CityDatabaseUtils;
import com.miui.gallery.db.sqlite3.GallerySQLiteOpenHelperFactory;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.portJava.Polygon;
import com.miui.gallery.util.portJava.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class CityDatabaseHelper implements SupportSQLiteOpenHelper {
    public static final String[] PROJECTION = {Marker.ANY_MARKER};
    public static final ArrayList<TableColumn> sCityBoundaryColumns;
    public final SupportSQLiteDatabase mDatabase = openDB();
    public SupportSQLiteOpenHelper mDelegate;

    /* loaded from: classes.dex */
    public static class CityBoundRectList extends HashMap<String, CityBoundRect> {
    }

    static {
        ArrayList<TableColumn> arrayList = new ArrayList<>();
        sCityBoundaryColumns = arrayList;
        arrayList.add(new CityBoundaryColumn("cityid", "text"));
        arrayList.add(new CityBoundaryColumn("boundary", "blob"));
        arrayList.add(new CityBoundaryColumn("rect", "blob"));
    }

    /* loaded from: classes.dex */
    public static class CityBoundaryColumn extends TableColumn {
        public CityBoundaryColumn(String str, String str2) {
            super(new TableColumn.Builder().setName(str).setType(str2));
        }
    }

    /* loaded from: classes.dex */
    public static class CityBoundary {
        public Boundary[] boundaries;
        public String cityId;

        /* loaded from: classes.dex */
        public static class Boundary implements Serializable {
            public static int FLAG_ADD = 1;
            public static int FlAG_MINUS = 2;
            private static final long serialVersionUID = 1;
            public Polygon boundary;
            public int flag;

            public String toString() {
                String str;
                StringBuilder sb = new StringBuilder();
                sb.append("Boundary flag:");
                sb.append(this.flag);
                sb.append("  boundray:");
                if (this.boundary != null) {
                    str = this.boundary.xpoints[0] + "," + this.boundary.ypoints[0];
                } else {
                    str = "null";
                }
                sb.append(str);
                return sb.toString();
            }
        }

        public boolean containsIntCoordinate(int i, int i2) {
            Boundary[] boundaryArr;
            boolean z = false;
            for (Boundary boundary : this.boundaries) {
                int i3 = boundary.flag;
                if (i3 == Boundary.FLAG_ADD) {
                    if (!z) {
                        z = boundary.boundary.contains(i, i2);
                    }
                } else if (i3 == Boundary.FlAG_MINUS && boundary.boundary.contains(i, i2)) {
                    return false;
                }
            }
            return z;
        }

        public CityBoundary(String str, Boundary[] boundaryArr) {
            this.cityId = str;
            this.boundaries = boundaryArr;
        }
    }

    /* loaded from: classes.dex */
    public static class CityBoundRect {
        public BoundRect[] boundRects;
        public String cityId;

        /* loaded from: classes.dex */
        public static class BoundRect implements Serializable {
            public static int FLAG_ADD = 1;
            private static final long serialVersionUID = 1;
            public Rectangle boundRect;
            public int flag;

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("BoundRect flag:");
                sb.append(this.flag);
                sb.append("  BoundRect:");
                Rectangle rectangle = this.boundRect;
                sb.append(rectangle != null ? rectangle.toString() : "null");
                return sb.toString();
            }
        }

        public CityBoundRect(String str, BoundRect[] boundRectArr) {
            this.cityId = str;
            this.boundRects = boundRectArr;
        }

        public boolean containsIntCoordinate(int i, int i2) {
            BoundRect[] boundRectArr = this.boundRects;
            if (boundRectArr != null) {
                for (BoundRect boundRect : boundRectArr) {
                    if (boundRect.flag == BoundRect.FLAG_ADD && boundRect.boundRect.contains(i, i2)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public CityDatabaseHelper(Context context, String str) {
        this.mDelegate = new GallerySQLiteOpenHelperFactory().create(SupportSQLiteOpenHelper.Configuration.builder(context).name(str).callback(new SQLiteOpenHelperCallback(1)).build());
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public String getDatabaseName() {
        return this.mDelegate.getDatabaseName();
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public void setWriteAheadLoggingEnabled(boolean z) {
        this.mDelegate.setWriteAheadLoggingEnabled(z);
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public SupportSQLiteDatabase getWritableDatabase() {
        return this.mDelegate.getWritableDatabase();
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public SupportSQLiteDatabase getReadableDatabase() {
        return this.mDelegate.getReadableDatabase();
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.mDelegate.close();
    }

    public boolean isDbOpened() {
        SupportSQLiteDatabase supportSQLiteDatabase = this.mDatabase;
        return supportSQLiteDatabase != null && supportSQLiteDatabase.isOpen();
    }

    public final SupportSQLiteDatabase openDB() {
        try {
            return getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CityBoundRectList loadCityBoundRects() {
        if (!isDbOpened()) {
            return null;
        }
        return (CityBoundRectList) SafeDBUtil.safeQuery(this.mDatabase, "cityBoundary", new String[]{"cityid", "rect"}, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<CityBoundRectList>() { // from class: com.miui.gallery.data.CityDatabaseHelper.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public CityBoundRectList mo1808handle(Cursor cursor) {
                if (cursor == null) {
                    DefaultLogger.e("CityDatabaseHelper", "loadCityBoundRects fails, the returned cursor is null");
                    return null;
                }
                CityBoundRectList cityBoundRectList = new CityBoundRectList();
                int columnIndex = cursor.getColumnIndex("cityid");
                int columnIndex2 = cursor.getColumnIndex("rect");
                while (cursor.moveToNext()) {
                    String string = cursor.getString(columnIndex);
                    cityBoundRectList.put(string, new CityBoundRect(string, (CityBoundRect.BoundRect[]) CityDatabaseUtils.PolygonHelper.parseFromByteArray(cursor.getBlob(columnIndex2))));
                }
                return cityBoundRectList;
            }
        });
    }

    public ConcurrentMap<String, CityBoundary> queryCityBoundary(ArrayList<String> arrayList) {
        if (!isDbOpened() || arrayList.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("cityid IN(");
        for (int i = 0; i < arrayList.size(); i++) {
            if (i > 0) {
                sb.append(CoreConstants.COMMA_CHAR);
            }
            sb.append("\"");
            sb.append(arrayList.get(i));
            sb.append("\"");
        }
        sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
        return (ConcurrentMap) SafeDBUtil.safeQuery(this.mDatabase, "cityBoundary", PROJECTION, sb.toString(), (String[]) null, (String) null, CityDatabaseHelper$$ExternalSyntheticLambda0.INSTANCE);
    }

    public static /* synthetic */ ConcurrentMap lambda$queryCityBoundary$0(Cursor cursor) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        if (cursor == null) {
            DefaultLogger.e("CityDatabaseHelper", "queryCityBoundary fails, the returned cursor is null");
            return concurrentHashMap;
        }
        while (cursor.moveToNext()) {
            String string = cursor.getString(0);
            concurrentHashMap.put(string, new CityBoundary(string, (CityBoundary.Boundary[]) CityDatabaseUtils.PolygonHelper.parseFromByteArray(cursor.getBlob(1))));
        }
        return concurrentHashMap;
    }

    /* loaded from: classes.dex */
    public static class SQLiteOpenHelperCallback extends SupportSQLiteOpenHelper.Callback {
        public SQLiteOpenHelperCallback(int i) {
            super(i);
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
            DefaultLogger.e("CityDatabaseHelper", "should not create: city.db is a readonly database");
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            DefaultLogger.e("CityDatabaseHelper", "should not upgrade: city.db is a readonly database");
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            DefaultLogger.e("CityDatabaseHelper", "should not downgrade: city.db is a readonly database");
        }
    }
}
