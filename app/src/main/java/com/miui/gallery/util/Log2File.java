package com.miui.gallery.util;

import android.os.Environment;
import android.os.Process;
import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.os.Rom;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import miuix.core.util.SoftReferenceSingleton;

/* loaded from: classes2.dex */
public class Log2File {
    public boolean mCanLog;
    public BufferedWriter mOut;
    public StringBuilder mStringBuilder;
    public static final SoftReferenceSingleton<SimpleDateFormat> TIME_FORMAT = new SoftReferenceSingleton<SimpleDateFormat>() { // from class: com.miui.gallery.util.Log2File.1
        @Override // miuix.core.util.SoftReferenceSingleton
        /* renamed from: createInstance  reason: collision with other method in class */
        public SimpleDateFormat mo2622createInstance() {
            return new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        }
    };
    public static final SoftReferenceSingleton<SimpleDateFormat> DATE_FORMAT = new SoftReferenceSingleton<SimpleDateFormat>() { // from class: com.miui.gallery.util.Log2File.2
        @Override // miuix.core.util.SoftReferenceSingleton
        /* renamed from: createInstance  reason: collision with other method in class */
        public SimpleDateFormat mo2622createInstance() {
            return new SimpleDateFormat("MM-dd");
        }
    };
    public static final String LOG_FEATURE_OPEN_PATH = StorageUtils.getPathInPrimaryStorage(StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM + "/openlog");

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static Log2File instance = new Log2File();
    }

    public Log2File() {
        this.mStringBuilder = new StringBuilder();
        this.mCanLog = false;
        if (Rom.IS_ALPHA || new File(LOG_FEATURE_OPEN_PATH).exists()) {
            this.mCanLog = true;
            initialWriter();
        }
    }

    public final void initialWriter() {
        if (this.mCanLog) {
            String format = DATE_FORMAT.get().format(new Date());
            String str = Environment.DIRECTORY_DOWNLOADS;
            try {
                this.mOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(StorageUtils.getPathInPrimaryStorage(BaseFileUtils.concat(str, format + "_gallery_log")), true)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Log2File getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void flushLog(String str, String str2, Throwable th) {
        if (!this.mCanLog) {
            return;
        }
        if (this.mOut == null) {
            initialWriter();
        }
        if (this.mOut == null) {
            return;
        }
        this.mStringBuilder.append(TIME_FORMAT.get().format(new Date()));
        this.mStringBuilder.append(' ');
        this.mStringBuilder.append('/');
        this.mStringBuilder.append(str);
        this.mStringBuilder.append(CoreConstants.LEFT_PARENTHESIS_CHAR);
        this.mStringBuilder.append(Process.myPid());
        this.mStringBuilder.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
        this.mStringBuilder.append(CoreConstants.COLON_CHAR);
        this.mStringBuilder.append(str2);
        if (th != null) {
            this.mStringBuilder.append(CoreConstants.COLON_CHAR);
            this.mStringBuilder.append(Log.getStackTraceString(th));
        }
        this.mStringBuilder.append('\n');
        try {
            BufferedWriter bufferedWriter = this.mOut;
            if (bufferedWriter != null) {
                bufferedWriter.write(this.mStringBuilder.toString());
                this.mOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mStringBuilder.setLength(0);
    }

    public boolean canLog() {
        return this.mCanLog;
    }
}
