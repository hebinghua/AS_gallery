package com.xiaomi.push;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import ch.qos.logback.core.util.FileSize;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes3.dex */
public class dj implements LoggerInterface {
    public static volatile dj a;

    /* renamed from: a  reason: collision with other field name */
    public Context f211a;

    /* renamed from: a  reason: collision with other field name */
    public Handler f212a;
    public String b;
    public String c = "";

    /* renamed from: a  reason: collision with other field name */
    public static final SimpleDateFormat f209a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aaa");

    /* renamed from: a  reason: collision with other field name */
    public static String f208a = "/MiPushLog";

    /* renamed from: a  reason: collision with other field name */
    public static List<Pair<String, Throwable>> f210a = Collections.synchronizedList(new ArrayList());

    public dj(Context context) {
        this.f211a = context;
        if (context.getApplicationContext() != null) {
            this.f211a = context.getApplicationContext();
        }
        this.b = this.f211a.getPackageName() + "-" + Process.myPid();
        HandlerThread handlerThread = new HandlerThread("Log2FileHandlerThread");
        handlerThread.start();
        this.f212a = new Handler(handlerThread.getLooper());
    }

    public static dj a(Context context) {
        if (a == null) {
            synchronized (dj.class) {
                if (a == null) {
                    a = new dj(context);
                }
            }
        }
        return a;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:80:0x016e -> B:109:0x0173). Please submit an issue!!! */
    /* renamed from: a  reason: collision with other method in class */
    public final void m2043a() {
        FileLock fileLock;
        RandomAccessFile randomAccessFile;
        File file;
        File externalFilesDir;
        BufferedWriter bufferedWriter = null;
        try {
            try {
                if (TextUtils.isEmpty(this.c) && (externalFilesDir = this.f211a.getExternalFilesDir(null)) != null) {
                    this.c = externalFilesDir.getAbsolutePath() + "";
                }
                file = new File(this.c + f208a);
            } catch (IOException e) {
                Log.e(this.b, "", e);
            }
        } catch (Exception e2) {
            e = e2;
            fileLock = null;
            randomAccessFile = null;
        } catch (Throwable th) {
            th = th;
            fileLock = null;
            randomAccessFile = null;
        }
        if ((!file.exists() || !file.isDirectory()) && !file.mkdirs()) {
            Log.w(this.b, "Create mipushlog directory fail.");
            return;
        }
        File file2 = new File(file, "log.lock");
        if (!file2.exists() || file2.isDirectory()) {
            file2.createNewFile();
        }
        randomAccessFile = new RandomAccessFile(file2, "rw");
        try {
            fileLock = randomAccessFile.getChannel().lock();
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(file, "log1.txt"), true)));
                    while (!f210a.isEmpty()) {
                        try {
                            Pair<String, Throwable> remove = f210a.remove(0);
                            String str = (String) remove.first;
                            if (remove.second != null) {
                                str = (str + "\n") + Log.getStackTraceString((Throwable) remove.second);
                            }
                            bufferedWriter2.write(str + "\n");
                        } catch (Exception e3) {
                            e = e3;
                            bufferedWriter = bufferedWriter2;
                            Log.e(this.b, "", e);
                            if (bufferedWriter != null) {
                                try {
                                    bufferedWriter.close();
                                } catch (IOException e4) {
                                    Log.e(this.b, "", e4);
                                }
                            }
                            if (fileLock != null && fileLock.isValid()) {
                                try {
                                    fileLock.release();
                                } catch (IOException e5) {
                                    Log.e(this.b, "", e5);
                                }
                            }
                            if (randomAccessFile == null) {
                                return;
                            }
                            randomAccessFile.close();
                        } catch (Throwable th2) {
                            th = th2;
                            bufferedWriter = bufferedWriter2;
                            if (bufferedWriter != null) {
                                try {
                                    bufferedWriter.close();
                                } catch (IOException e6) {
                                    Log.e(this.b, "", e6);
                                }
                            }
                            if (fileLock != null && fileLock.isValid()) {
                                try {
                                    fileLock.release();
                                } catch (IOException e7) {
                                    Log.e(this.b, "", e7);
                                }
                            }
                            if (randomAccessFile != null) {
                                try {
                                    randomAccessFile.close();
                                } catch (IOException e8) {
                                    Log.e(this.b, "", e8);
                                }
                            }
                            throw th;
                        }
                    }
                    bufferedWriter2.flush();
                    bufferedWriter2.close();
                    File file3 = new File(file, "log1.txt");
                    if (file3.length() >= FileSize.MB_COEFFICIENT) {
                        File file4 = new File(file, "log0.txt");
                        if (file4.exists() && file4.isFile()) {
                            file4.delete();
                        }
                        file3.renameTo(file4);
                    }
                    if (fileLock != null && fileLock.isValid()) {
                        try {
                            fileLock.release();
                        } catch (IOException e9) {
                            Log.e(this.b, "", e9);
                        }
                    }
                    randomAccessFile.close();
                } catch (Exception e10) {
                    e = e10;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Exception e11) {
            e = e11;
            fileLock = null;
        } catch (Throwable th4) {
            th = th4;
            fileLock = null;
        }
    }

    @Override // com.xiaomi.channel.commonutils.logger.LoggerInterface
    public final void log(String str) {
        log(str, null);
    }

    @Override // com.xiaomi.channel.commonutils.logger.LoggerInterface
    public final void log(String str, Throwable th) {
        this.f212a.post(new dk(this, str, th));
    }
}
