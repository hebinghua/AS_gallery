package cn.kuaipan.android.kss.upload;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.exception.KscRuntimeException;
import cn.kuaipan.android.kss.IDataFactory;
import cn.kuaipan.android.kss.KssDef;
import cn.kuaipan.android.utils.OAuthTimeUtils;
import cn.kuaipan.android.utils.SQLUtility;
import cn.kuaipan.android.utils.SyncAccessor;
import com.xiaomi.stat.a.j;

/* loaded from: classes.dex */
public class UploadTaskStore implements KssDef {
    public static volatile Looper sLooper;
    public final SyncAccessor mAccessor = new SyncAccessor(getCommonLooper()) { // from class: cn.kuaipan.android.kss.upload.UploadTaskStore.1
        @Override // cn.kuaipan.android.utils.SyncAccessor
        public Object handleAccess(int i, Object... objArr) {
            if (i == 0) {
                UploadTaskStore.this.mDBHelper.update(((Number) objArr[0]).intValue(), (KssUploadInfo) objArr[1], (UploadChunkInfoPersist) objArr[2]);
            } else if (i == 1) {
                UploadTaskStore.this.mDBHelper.delete(((Number) objArr[0]).intValue());
            } else if (i == 2) {
                return UploadTaskStore.this.mDBHelper.queryPos(((Number) objArr[0]).intValue());
            } else if (i == 3) {
                int intValue = ((Number) objArr[0]).intValue();
                UploadTaskStore.this.mDBHelper.deleteBefore(OAuthTimeUtils.currentTime() - 85376000);
                try {
                    return UploadTaskStore.this.mDBHelper.queryKss(intValue, UploadTaskStore.this.mDataFactory);
                } catch (Throwable th) {
                    Log.w("UploadTaskStore", "Meet exception when parser kss from db", th);
                }
            } else {
                return super.handleAccess(i, objArr);
            }
            return null;
        }
    };
    public final DBHelper mDBHelper;
    public final IDataFactory mDataFactory;

    public UploadTaskStore(Context context, IDataFactory iDataFactory) {
        if (context == null || iDataFactory == null) {
            throw new KscRuntimeException(500002, "context and dataFactory can't be null");
        }
        this.mDBHelper = DBHelper.getInstance(context);
        this.mDataFactory = iDataFactory;
    }

    public void putUploadInfo(int i, KssUploadInfo kssUploadInfo) throws InterruptedException {
        this.mAccessor.access(0, Integer.valueOf(i), kssUploadInfo, new UploadChunkInfoPersist());
    }

    public void removeUploadInfo(int i) throws InterruptedException {
        this.mAccessor.access(1, Integer.valueOf(i));
    }

    public KssUploadInfo getUploadInfo(int i) throws InterruptedException {
        return (KssUploadInfo) this.mAccessor.access(3, Integer.valueOf(i));
    }

    public UploadChunkInfoPersist getUploadPos(int i) throws InterruptedException {
        return (UploadChunkInfoPersist) this.mAccessor.access(2, Integer.valueOf(i));
    }

    public void updateUploadInfo(int i, KssUploadInfo kssUploadInfo, UploadChunkInfoPersist uploadChunkInfoPersist) {
        this.mDBHelper.update(i, kssUploadInfo, uploadChunkInfoPersist);
    }

    /* loaded from: classes.dex */
    public static class DBHelper extends SQLiteOpenHelper {
        public static volatile DBHelper sInstance;
        public static final String WHERE_DEL = SQLUtility.getSelectionWithTemplete("%s<?", "gen_time");
        public static final String WHERE_QUERY = SQLUtility.getSelection("task_hash");
        public static final String[] QUERY_POS = {"chunk_pos", "upload_id"};
        public static final String[] QUERY_KSS = {"kss_request", "kss_file_info", "kss_upload_id", "gen_time"};

        public static DBHelper getInstance(Context context) {
            DBHelper dBHelper = sInstance;
            if (dBHelper == null) {
                synchronized (DBHelper.class) {
                    dBHelper = sInstance;
                    if (dBHelper == null) {
                        if (context == null) {
                            throw new NullPointerException("Context should not be null.");
                        }
                        dBHelper = new DBHelper(context);
                        sInstance = dBHelper;
                    }
                }
            }
            return dBHelper;
        }

        public DBHelper(Context context) {
            super(context, "ksssdk_infos.db", (SQLiteDatabase.CursorFactory) null, 4);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            SQLUtility.createTable(sQLiteDatabase, "upload_chunks", j.c + " INTEGER PRIMARY KEY AUTOINCREMENT, task_hash INTEGER NOT NULL UNIQUE, kss_request STRING NOT NULL, kss_file_info STRING NOT NULL, kss_upload_id STRING NOT NULL, chunk_pos LONG NOT NULL DEFAULT 0, upload_id STRING NOT NULL, gen_time LONG NOT NULL DEFAULT 0");
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            if (i != 4) {
                Log.w("DBHelper", "Destroying all old data.");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS upload_chunks");
                onCreate(sQLiteDatabase);
            }
        }

        public void deleteBefore(long j) {
            getWritableDatabase().delete("upload_chunks", WHERE_DEL, new String[]{String.valueOf(j)});
        }

        public UploadChunkInfoPersist queryPos(int i) {
            Cursor query = getReadableDatabase().query("upload_chunks", QUERY_POS, WHERE_QUERY, new String[]{String.valueOf(i)}, null, null, null);
            try {
                UploadChunkInfoPersist uploadChunkInfoPersist = new UploadChunkInfoPersist();
                if (query != null && query.moveToFirst()) {
                    long j = query.getInt(query.getColumnIndex("chunk_pos"));
                    String string = query.getString(query.getColumnIndex("upload_id"));
                    if (!TextUtils.isEmpty(string)) {
                        uploadChunkInfoPersist.pos = j;
                        uploadChunkInfoPersist.upload_id = string;
                    }
                }
                return uploadChunkInfoPersist;
            } finally {
                if (query != null) {
                    query.close();
                }
            }
        }

        public KssUploadInfo queryKss(int i, IDataFactory iDataFactory) throws KscException {
            Cursor query = getReadableDatabase().query("upload_chunks", QUERY_KSS, WHERE_QUERY, new String[]{String.valueOf(i)}, null, null, null);
            KssUploadInfo kssUploadInfo = null;
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String string = query.getString(query.getColumnIndex("kss_request"));
                        String string2 = query.getString(query.getColumnIndex("kss_file_info"));
                        long j = query.getLong(query.getColumnIndex("gen_time"));
                        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
                            KssUploadInfo kssUploadInfo2 = new KssUploadInfo(new UploadFileInfo(string2), iDataFactory.createUploadRequestResult(string), j);
                            kssUploadInfo2.setUploadId(query.getString(query.getColumnIndex("kss_upload_id")));
                            kssUploadInfo = kssUploadInfo2;
                        }
                        return null;
                    }
                } finally {
                    query.close();
                }
            }
            if (query != null) {
            }
            return kssUploadInfo;
        }

        public void update(int i, KssUploadInfo kssUploadInfo, UploadChunkInfoPersist uploadChunkInfoPersist) {
            if (kssUploadInfo == null) {
                return;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("task_hash", Integer.valueOf(i));
            contentValues.put("kss_file_info", kssUploadInfo.getFileInfo().toString());
            contentValues.put("kss_request", kssUploadInfo.getRequestResult().toString());
            if (TextUtils.isEmpty(kssUploadInfo.getUploadId())) {
                contentValues.put("kss_upload_id", "");
            } else {
                contentValues.put("kss_upload_id", kssUploadInfo.getUploadId());
            }
            contentValues.put("chunk_pos", Long.valueOf(uploadChunkInfoPersist.pos));
            if (TextUtils.isEmpty(uploadChunkInfoPersist.upload_id)) {
                contentValues.put("upload_id", "");
            } else {
                contentValues.put("upload_id", uploadChunkInfoPersist.upload_id);
            }
            contentValues.put("gen_time", Long.valueOf(kssUploadInfo.getGenerateTime()));
            getWritableDatabase().replace("upload_chunks", null, contentValues);
        }

        public void delete(int i) {
            getWritableDatabase().delete("upload_chunks", WHERE_QUERY, new String[]{String.valueOf(i)});
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x002f, code lost:
        r0 = new android.os.HandlerThread("KssMaster - UploadRecorder", 10);
        r0.start();
        r0 = r0.getLooper();
     */
    @android.annotation.SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.os.Looper getCommonLooper() {
        /*
            android.os.Looper r0 = cn.kuaipan.android.kss.upload.UploadTaskStore.sLooper
            r1 = 0
            if (r0 != 0) goto L7
            r2 = r1
            goto Lb
        L7:
            java.lang.Thread r2 = r0.getThread()
        Lb:
            if (r2 == 0) goto L13
            boolean r2 = r2.isAlive()
            if (r2 != 0) goto L14
        L13:
            r0 = r1
        L14:
            if (r0 != 0) goto L46
            java.lang.Class<cn.kuaipan.android.kss.KssMaster> r2 = cn.kuaipan.android.kss.KssMaster.class
            monitor-enter(r2)
            android.os.Looper r0 = cn.kuaipan.android.kss.upload.UploadTaskStore.sLooper     // Catch: java.lang.Throwable -> L43
            if (r0 != 0) goto L1f
            r3 = r1
            goto L23
        L1f:
            java.lang.Thread r3 = r0.getThread()     // Catch: java.lang.Throwable -> L43
        L23:
            if (r3 == 0) goto L2d
            boolean r3 = r3.isAlive()     // Catch: java.lang.Throwable -> L43
            if (r3 != 0) goto L2c
            goto L2d
        L2c:
            r1 = r0
        L2d:
            if (r1 != 0) goto L40
            android.os.HandlerThread r0 = new android.os.HandlerThread     // Catch: java.lang.Throwable -> L43
            java.lang.String r1 = "KssMaster - UploadRecorder"
            r3 = 10
            r0.<init>(r1, r3)     // Catch: java.lang.Throwable -> L43
            r0.start()     // Catch: java.lang.Throwable -> L43
            android.os.Looper r0 = r0.getLooper()     // Catch: java.lang.Throwable -> L43
            goto L41
        L40:
            r0 = r1
        L41:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L43
            goto L46
        L43:
            r0 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L43
            throw r0
        L46:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuaipan.android.kss.upload.UploadTaskStore.getCommonLooper():android.os.Looper");
    }
}
