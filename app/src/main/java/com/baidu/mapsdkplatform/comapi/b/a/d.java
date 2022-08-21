package com.baidu.mapsdkplatform.comapi.b.a;

import com.baidu.mapsdkplatform.comapi.util.g;
import java.io.File;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class d implements Runnable {
    public final /* synthetic */ c a;

    public d(c cVar) {
        this.a = cVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        String str;
        File[] listFiles;
        String str2;
        if (g.a().b() == null) {
            return;
        }
        str = c.a;
        File file = new File(str);
        if (!file.exists() || (listFiles = file.listFiles()) == null || listFiles.length == 0) {
            return;
        }
        try {
            Arrays.sort(listFiles, new e());
        } catch (Exception unused) {
        }
        int length = listFiles.length;
        if (length > 10) {
            length = 10;
        }
        for (int i = 0; i < length; i++) {
            File file2 = listFiles[i];
            if (!file2.isDirectory() && file2.exists() && file2.isFile()) {
                String name = file2.getName();
                str2 = c.b;
                if (name.contains(str2) && (file2.getName().endsWith(".txt") || (file2.getName().endsWith(".zip") && file2.exists()))) {
                    this.a.a(file2);
                }
            }
        }
        if (listFiles.length <= 10) {
            return;
        }
        this.a.a(listFiles);
    }
}
