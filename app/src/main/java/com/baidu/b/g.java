package com.baidu.b;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/* loaded from: classes.dex */
public class g {
    private Context a;
    private c b;

    public g(Context context, c cVar) {
        this.a = context;
        this.b = cVar;
    }

    private f a() {
        File file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid2");
        if (file.exists()) {
            return f.b(com.baidu.b.f.c.a(file));
        }
        return null;
    }

    private f a(Context context) {
        List<b> b = this.b.b(context);
        f fVar = null;
        if (b != null) {
            String str = "files";
            File filesDir = context.getFilesDir();
            if (!str.equals(filesDir.getName())) {
                Log.e("CuidV266Manager", "fetal error:: app files dir name is unexpectedly :: " + filesDir.getAbsolutePath());
                str = filesDir.getName();
            }
            for (b bVar : b) {
                if (!bVar.d) {
                    File file = new File(new File(bVar.a.dataDir, str), "libcuid.so");
                    if (file.exists() && (fVar = f.b(com.baidu.b.f.c.a(file))) != null) {
                        break;
                    }
                }
            }
        }
        return fVar;
    }

    private f b() {
        return f.a(c("com.baidu.deviceid"), c("bd_setting_i"));
    }

    private boolean b(String str) {
        return this.a.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }

    private String c(String str) {
        try {
            return Settings.System.getString(this.a.getContentResolver(), str);
        } catch (Exception e) {
            com.baidu.b.f.c.a(e);
            return null;
        }
    }

    private String d(String str) {
        return "0";
    }

    private f e(String str) {
        String str2;
        String[] split;
        String str3 = "";
        File file = new File(Environment.getExternalStorageDirectory(), "baidu/.cuid");
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid");
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append("\r\n");
            }
            bufferedReader.close();
            byte[] a = com.baidu.b.c.a.g.a();
            split = new String(com.baidu.b.c.a.c.a(a, a, com.baidu.b.d.a.a(sb.toString().getBytes()))).split("=");
        } catch (FileNotFoundException | IOException | Exception unused) {
        }
        if (split != null && split.length == 2) {
            str2 = split[0];
            try {
                str3 = split[1];
            } catch (FileNotFoundException | IOException | Exception unused2) {
            }
            return f.a(str3, str2);
        }
        str2 = str3;
        return f.a(str3, str2);
    }

    public f a(String str) {
        f a = a(this.a);
        if (a == null) {
            a = f.b(c("com.baidu.deviceid.v2"));
        }
        boolean b = b("android.permission.READ_EXTERNAL_STORAGE");
        if (a == null && b) {
            a = a();
        }
        if (a == null) {
            a = b();
        }
        boolean z = false;
        if (a == null && b) {
            z = true;
            a = e(d(""));
        }
        if (!z) {
            d("");
        }
        if (a != null) {
            a.c();
        }
        return a;
    }
}
