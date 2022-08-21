package xcrash;

import java.io.File;
import java.io.FilenameFilter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class FileManager {
    public static final FileManager instance = new FileManager();
    public String placeholderPrefix = "placeholder";
    public String placeholderCleanSuffix = ".clean.xcrash";
    public String placeholderDirtySuffix = ".dirty.xcrash";
    public String logDir = null;
    public int javaLogCountMax = 0;
    public int nativeLogCountMax = 0;
    public int anrLogCountMax = 0;
    public int traceLogCountMax = 1;
    public int placeholderCountMax = 0;
    public int placeholderSizeKb = 0;
    public int delayMs = 0;
    public AtomicInteger unique = new AtomicInteger();

    public static FileManager getInstance() {
        return instance;
    }

    public void initialize(String str, int i, int i2, int i3, int i4, int i5, int i6) {
        File[] listFiles;
        this.logDir = str;
        this.javaLogCountMax = i;
        this.nativeLogCountMax = i2;
        this.anrLogCountMax = i3;
        this.placeholderCountMax = i4;
        this.placeholderSizeKb = i5;
        this.delayMs = i6;
        try {
            File file = new File(str);
            if (!file.exists() || !file.isDirectory() || (listFiles = file.listFiles()) == null) {
                return;
            }
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            int i10 = 0;
            int i11 = 0;
            int i12 = 0;
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    String name = file2.getName();
                    if (!name.startsWith("tombstone_")) {
                        if (name.startsWith(this.placeholderPrefix + "_")) {
                            if (name.endsWith(this.placeholderCleanSuffix)) {
                                i11++;
                            } else if (name.endsWith(this.placeholderDirtySuffix)) {
                                i12++;
                            }
                        }
                    } else if (name.endsWith(".java.xcrash")) {
                        i7++;
                    } else if (name.endsWith(".native.xcrash")) {
                        i8++;
                    } else if (name.endsWith(".anr.xcrash")) {
                        i9++;
                    } else if (name.endsWith(".trace.xcrash")) {
                        i10++;
                    }
                }
            }
            int i13 = this.javaLogCountMax;
            if (i7 <= i13 && i8 <= this.nativeLogCountMax && i9 <= this.anrLogCountMax && i10 <= this.traceLogCountMax && i11 == this.placeholderCountMax && i12 == 0) {
                this.delayMs = -1;
                return;
            }
            if (i7 <= i13 + 10) {
                int i14 = this.nativeLogCountMax;
                if (i8 <= i14 + 10) {
                    int i15 = this.anrLogCountMax;
                    if (i9 <= i15 + 10) {
                        int i16 = this.traceLogCountMax;
                        if (i10 <= i16 + 10) {
                            int i17 = this.placeholderCountMax;
                            if (i11 <= i17 + 10 && i12 <= 10) {
                                if (i7 <= i13 && i8 <= i14 && i9 <= i15 && i10 <= i16 && i11 <= i17 && i12 <= 0) {
                                    return;
                                }
                                this.delayMs = 0;
                                return;
                            }
                        }
                    }
                }
            }
            doMaintain();
            this.delayMs = -1;
        } catch (Exception e) {
            XCrash.getLogger().e("xcrash", "FileManager init failed", e);
        }
    }

    public void maintain() {
        int i;
        if (this.logDir == null || (i = this.delayMs) < 0) {
            return;
        }
        try {
            if (i == 0) {
                new Thread(new Runnable() { // from class: xcrash.FileManager.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FileManager.this.doMaintain();
                    }
                }, "xcrash_file_mgr").start();
            } else {
                new Timer("xcrash_file_mgr").schedule(new TimerTask() { // from class: xcrash.FileManager.2
                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        FileManager.this.doMaintain();
                    }
                }, this.delayMs);
            }
        } catch (Exception e) {
            XCrash.getLogger().e("xcrash", "FileManager maintain start failed", e);
        }
    }

    public boolean maintainAnr() {
        if (!Util.checkAndCreateDir(this.logDir)) {
            return false;
        }
        try {
            return doMaintainTombstoneType(new File(this.logDir), ".anr.xcrash", this.anrLogCountMax);
        } catch (Exception e) {
            XCrash.getLogger().e("xcrash", "FileManager maintainAnr failed", e);
            return false;
        }
    }

    public File createLogFile(String str) {
        String str2 = this.logDir;
        if (str2 != null && Util.checkAndCreateDir(str2)) {
            File file = new File(str);
            File[] listFiles = new File(this.logDir).listFiles(new FilenameFilter() { // from class: xcrash.FileManager.3
                @Override // java.io.FilenameFilter
                public boolean accept(File file2, String str3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(FileManager.this.placeholderPrefix);
                    sb.append("_");
                    return str3.startsWith(sb.toString()) && str3.endsWith(FileManager.this.placeholderCleanSuffix);
                }
            });
            if (listFiles != null) {
                for (int length = listFiles.length; length > 0; length--) {
                    File file2 = listFiles[length - 1];
                    try {
                    } catch (Exception e) {
                        XCrash.getLogger().e("xcrash", "FileManager createLogFile by renameTo failed", e);
                    }
                    if (file2.renameTo(file)) {
                        return file;
                    }
                    file2.delete();
                }
            }
            try {
                if (file.createNewFile()) {
                    return file;
                }
                XCrash.getLogger().e("xcrash", "FileManager createLogFile by createNewFile failed, file already exists");
                return null;
            } catch (Exception e2) {
                XCrash.getLogger().e("xcrash", "FileManager createLogFile by createNewFile failed", e2);
                return null;
            }
        }
        return null;
    }

    public boolean appendText(String str, String str2) {
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2 = null;
        try {
            try {
                randomAccessFile = new RandomAccessFile(str, "rws");
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            long j = 0;
            if (randomAccessFile.length() > 0) {
                MappedByteBuffer map = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, randomAccessFile.length());
                long length = randomAccessFile.length();
                while (length > 0 && map.get(((int) length) - 1) == 0) {
                    length--;
                }
                j = length;
            }
            randomAccessFile.seek(j);
            randomAccessFile.write(str2.getBytes(Keyczar.DEFAULT_ENCODING));
            try {
                randomAccessFile.close();
            } catch (Exception unused) {
            }
            return true;
        } catch (Exception e2) {
            e = e2;
            randomAccessFile2 = randomAccessFile;
            XCrash.getLogger().e("xcrash", "FileManager appendText failed", e);
            if (randomAccessFile2 != null) {
                try {
                    randomAccessFile2.close();
                } catch (Exception unused2) {
                }
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
            randomAccessFile2 = randomAccessFile;
            if (randomAccessFile2 != null) {
                try {
                    randomAccessFile2.close();
                } catch (Exception unused3) {
                }
            }
            throw th;
        }
    }

    public boolean recycleLogFile(File file) {
        if (file == null) {
            return false;
        }
        if (this.logDir == null || this.placeholderCountMax <= 0) {
            try {
                return file.delete();
            } catch (Exception unused) {
                return false;
            }
        }
        try {
            File[] listFiles = new File(this.logDir).listFiles(new FilenameFilter() { // from class: xcrash.FileManager.4
                @Override // java.io.FilenameFilter
                public boolean accept(File file2, String str) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(FileManager.this.placeholderPrefix);
                    sb.append("_");
                    return str.startsWith(sb.toString()) && str.endsWith(FileManager.this.placeholderCleanSuffix);
                }
            });
            if (listFiles != null && listFiles.length >= this.placeholderCountMax) {
                try {
                    return file.delete();
                } catch (Exception unused2) {
                    return false;
                }
            }
            File file2 = new File(String.format(Locale.US, "%s/%s_%020d%s", this.logDir, this.placeholderPrefix, Long.valueOf((new Date().getTime() * 1000) + getNextUnique()), this.placeholderDirtySuffix));
            if (!file.renameTo(file2)) {
                try {
                    return file.delete();
                } catch (Exception unused3) {
                    return false;
                }
            }
            return cleanTheDirtyFile(file2);
        } catch (Exception e) {
            XCrash.getLogger().e("xcrash", "FileManager recycleLogFile failed", e);
            try {
                return file.delete();
            } catch (Exception unused4) {
                return false;
            }
        }
    }

    public final void doMaintain() {
        if (!Util.checkAndCreateDir(this.logDir)) {
            return;
        }
        File file = new File(this.logDir);
        try {
            doMaintainTombstone(file);
        } catch (Exception e) {
            XCrash.getLogger().e("xcrash", "FileManager doMaintainTombstone failed", e);
        }
        try {
            doMaintainPlaceholder(file);
        } catch (Exception e2) {
            XCrash.getLogger().e("xcrash", "FileManager doMaintainPlaceholder failed", e2);
        }
    }

    public final void doMaintainTombstone(File file) {
        doMaintainTombstoneType(file, ".native.xcrash", this.nativeLogCountMax);
        doMaintainTombstoneType(file, ".java.xcrash", this.javaLogCountMax);
        doMaintainTombstoneType(file, ".anr.xcrash", this.anrLogCountMax);
        doMaintainTombstoneType(file, ".trace.xcrash", this.traceLogCountMax);
    }

    public final boolean doMaintainTombstoneType(File file, final String str, int i) {
        File[] listFiles = file.listFiles(new FilenameFilter() { // from class: xcrash.FileManager.5
            @Override // java.io.FilenameFilter
            public boolean accept(File file2, String str2) {
                return str2.startsWith("tombstone_") && str2.endsWith(str);
            }
        });
        boolean z = true;
        if (listFiles != null && listFiles.length > i) {
            if (i > 0) {
                Arrays.sort(listFiles, new Comparator<File>() { // from class: xcrash.FileManager.6
                    @Override // java.util.Comparator
                    public int compare(File file2, File file3) {
                        return file2.getName().compareTo(file3.getName());
                    }
                });
            }
            for (int i2 = 0; i2 < listFiles.length - i; i2++) {
                if (!recycleLogFile(listFiles[i2])) {
                    z = false;
                }
            }
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0086 A[LOOP:0: B:9:0x0020->B:28:0x0086, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x008a A[EDGE_INSN: B:50:0x008a->B:30:0x008a ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void doMaintainPlaceholder(java.io.File r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            xcrash.FileManager$7 r2 = new xcrash.FileManager$7
            r2.<init>()
            java.io.File[] r2 = r1.listFiles(r2)
            if (r2 != 0) goto L10
            return
        L10:
            xcrash.FileManager$8 r3 = new xcrash.FileManager$8
            r3.<init>()
            java.io.File[] r3 = r1.listFiles(r3)
            if (r3 != 0) goto L1c
            return
        L1c:
            int r4 = r2.length
            int r5 = r3.length
            r6 = 0
            r7 = r6
        L20:
            int r8 = r0.placeholderCountMax
            if (r4 >= r8) goto L88
            r8 = 2
            if (r5 <= 0) goto L38
            int r9 = r5 + (-1)
            r9 = r3[r9]
            boolean r9 = r0.cleanTheDirtyFile(r9)
            if (r9 == 0) goto L33
            int r4 = r4 + 1
        L33:
            int r5 = r5 + (-1)
        L35:
            r17 = r7
            goto L7e
        L38:
            java.io.File r9 = new java.io.File     // Catch: java.lang.Exception -> L35
            java.util.Locale r10 = java.util.Locale.US     // Catch: java.lang.Exception -> L35
            java.lang.String r11 = "%s/%s_%020d%s"
            r12 = 4
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch: java.lang.Exception -> L35
            java.lang.String r13 = r0.logDir     // Catch: java.lang.Exception -> L35
            r12[r6] = r13     // Catch: java.lang.Exception -> L35
            java.lang.String r13 = r0.placeholderPrefix     // Catch: java.lang.Exception -> L35
            r14 = 1
            r12[r14] = r13     // Catch: java.lang.Exception -> L35
            java.util.Date r13 = new java.util.Date     // Catch: java.lang.Exception -> L35
            r13.<init>()     // Catch: java.lang.Exception -> L35
            long r13 = r13.getTime()     // Catch: java.lang.Exception -> L35
            r15 = 1000(0x3e8, double:4.94E-321)
            long r13 = r13 * r15
            int r15 = r18.getNextUnique()     // Catch: java.lang.Exception -> L35
            r17 = r7
            long r6 = (long) r15
            long r13 = r13 + r6
            java.lang.Long r6 = java.lang.Long.valueOf(r13)     // Catch: java.lang.Exception -> L7e
            r12[r8] = r6     // Catch: java.lang.Exception -> L7e
            r6 = 3
            java.lang.String r7 = r0.placeholderDirtySuffix     // Catch: java.lang.Exception -> L7e
            r12[r6] = r7     // Catch: java.lang.Exception -> L7e
            java.lang.String r6 = java.lang.String.format(r10, r11, r12)     // Catch: java.lang.Exception -> L7e
            r9.<init>(r6)     // Catch: java.lang.Exception -> L7e
            boolean r6 = r9.createNewFile()     // Catch: java.lang.Exception -> L7e
            if (r6 == 0) goto L7e
            boolean r6 = r0.cleanTheDirtyFile(r9)     // Catch: java.lang.Exception -> L7e
            if (r6 == 0) goto L7e
            int r4 = r4 + 1
        L7e:
            int r7 = r17 + 1
            int r6 = r0.placeholderCountMax
            int r6 = r6 * r8
            if (r7 <= r6) goto L86
            goto L8a
        L86:
            r6 = 0
            goto L20
        L88:
            r17 = r7
        L8a:
            if (r7 <= 0) goto L9e
            xcrash.FileManager$9 r2 = new xcrash.FileManager$9
            r2.<init>()
            java.io.File[] r2 = r1.listFiles(r2)
            xcrash.FileManager$10 r3 = new xcrash.FileManager$10
            r3.<init>()
            java.io.File[] r3 = r1.listFiles(r3)
        L9e:
            if (r2 == 0) goto Lb4
            int r1 = r2.length
            int r4 = r0.placeholderCountMax
            if (r1 <= r4) goto Lb4
            r1 = 0
        La6:
            int r4 = r2.length
            int r5 = r0.placeholderCountMax
            int r4 = r4 - r5
            if (r1 >= r4) goto Lb4
            r4 = r2[r1]
            r4.delete()
            int r1 = r1 + 1
            goto La6
        Lb4:
            if (r3 == 0) goto Lc2
            int r1 = r3.length
            r6 = 0
        Lb8:
            if (r6 >= r1) goto Lc2
            r2 = r3[r6]
            r2.delete()
            int r6 = r6 + 1
            goto Lb8
        Lc2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: xcrash.FileManager.doMaintainPlaceholder(java.io.File):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00b0 A[Catch: Exception -> 0x00b3, TRY_ENTER, TRY_LEAVE, TryCatch #6 {Exception -> 0x00b3, blocks: (B:36:0x00b0, B:21:0x0090), top: B:57:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00b5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean cleanTheDirtyFile(java.io.File r17) {
        /*
            r16 = this;
            r1 = r16
            r0 = 1024(0x400, float:1.435E-42)
            r2 = 0
            r3 = 0
            byte[] r4 = new byte[r0]     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            java.util.Arrays.fill(r4, r2)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            int r5 = r1.placeholderSizeKb     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            long r5 = (long) r5     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            long r7 = r17.length()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            int r9 = r1.placeholderSizeKb     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            int r9 = r9 * r0
            long r9 = (long) r9     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            int r0 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            r9 = 0
            r11 = 1024(0x400, double:5.06E-321)
            if (r0 <= 0) goto L29
            long r5 = r7 / r11
            long r13 = r7 % r11
            int r0 = (r13 > r9 ? 1 : (r13 == r9 ? 0 : -1))
            if (r0 == 0) goto L29
            r13 = 1
            long r5 = r5 + r13
        L29:
            java.io.FileOutputStream r13 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            java.io.File r0 = r17.getAbsoluteFile()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            r13.<init>(r0, r2)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La0
            r0 = r2
        L33:
            long r14 = (long) r0
            int r3 = (r14 > r5 ? 1 : (r14 == r5 ? 0 : -1))
            if (r3 >= 0) goto L50
            int r0 = r0 + 1
            long r14 = (long) r0
            int r3 = (r14 > r5 ? 1 : (r14 == r5 ? 0 : -1))
            if (r3 != 0) goto L4c
            long r14 = r7 % r11
            int r3 = (r14 > r9 ? 1 : (r14 == r9 ? 0 : -1))
            if (r3 == 0) goto L4c
            long r14 = r7 % r11
            int r3 = (int) r14     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r13.write(r4, r2, r3)     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            goto L33
        L4c:
            r13.write(r4)     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            goto L33
        L50:
            r13.flush()     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            java.util.Locale r0 = java.util.Locale.US     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            java.lang.String r3 = "%s/%s_%020d%s"
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            java.lang.String r5 = r1.logDir     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r4[r2] = r5     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            java.lang.String r5 = r1.placeholderPrefix     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r6 = 1
            r4[r6] = r5     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r5 = 2
            java.util.Date r6 = new java.util.Date     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r6.<init>()     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            long r6 = r6.getTime()     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r8 = 1000(0x3e8, double:4.94E-321)
            long r6 = r6 * r8
            int r8 = r16.getNextUnique()     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            long r8 = (long) r8     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            long r6 = r6 + r8
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r4[r5] = r6     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r5 = 3
            java.lang.String r6 = r1.placeholderCleanSuffix     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r4[r5] = r6     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            java.lang.String r0 = java.lang.String.format(r0, r3, r4)     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r3.<init>(r0)     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L99
            r4 = r17
            boolean r2 = r4.renameTo(r3)     // Catch: java.lang.Exception -> L94 java.lang.Throwable -> L96
            r13.close()     // Catch: java.lang.Exception -> Lb3
            goto Lb3
        L94:
            r0 = move-exception
            goto L9c
        L96:
            r0 = move-exception
            r3 = r13
            goto Lb9
        L99:
            r0 = move-exception
            r4 = r17
        L9c:
            r3 = r13
            goto La3
        L9e:
            r0 = move-exception
            goto Lb9
        La0:
            r0 = move-exception
            r4 = r17
        La3:
            xcrash.ILogger r5 = xcrash.XCrash.getLogger()     // Catch: java.lang.Throwable -> L9e
            java.lang.String r6 = "xcrash"
            java.lang.String r7 = "FileManager cleanTheDirtyFile failed"
            r5.e(r6, r7, r0)     // Catch: java.lang.Throwable -> L9e
            if (r3 == 0) goto Lb3
            r3.close()     // Catch: java.lang.Exception -> Lb3
        Lb3:
            if (r2 != 0) goto Lb8
            r17.delete()     // Catch: java.lang.Exception -> Lb8
        Lb8:
            return r2
        Lb9:
            if (r3 == 0) goto Lbe
            r3.close()     // Catch: java.lang.Exception -> Lbe
        Lbe:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: xcrash.FileManager.cleanTheDirtyFile(java.io.File):boolean");
    }

    public final int getNextUnique() {
        int incrementAndGet = this.unique.incrementAndGet();
        if (incrementAndGet >= 999) {
            this.unique.set(0);
        }
        return incrementAndGet;
    }
}
