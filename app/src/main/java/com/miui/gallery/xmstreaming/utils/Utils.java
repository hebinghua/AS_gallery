package com.miui.gallery.xmstreaming.utils;

import android.content.res.AssetManager;
import android.text.TextUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.StaticContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class Utils {
    private static final String ASSETS_START = "assets:/";

    public static String readFile(String str) {
        AssetManager assetManager = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.startsWith(ASSETS_START)) {
            assetManager = StaticContext.sGetAndroidContext().getAssets();
        }
        return readFile(str, assetManager);
    }

    private static String readFile(String str, AssetManager assetManager) {
        InputStream inputStream;
        InputStream inputStream2;
        InputStream open;
        InputStream inputStream3 = null;
        try {
            try {
                if (assetManager == null) {
                    open = new FileInputStream(new File(str));
                } else {
                    open = assetManager.open(str.replace(ASSETS_START, ""));
                }
            } catch (Throwable th) {
                th = th;
                inputStream3 = assetManager;
            }
        } catch (FileNotFoundException e) {
            e = e;
            inputStream2 = null;
        } catch (IOException e2) {
            e = e2;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            String str2 = new String(bArr, Keyczar.DEFAULT_ENCODING);
            BaseMiscUtil.closeSilently(open);
            return str2;
        } catch (FileNotFoundException e3) {
            inputStream2 = open;
            e = e3;
            e.printStackTrace();
            BaseMiscUtil.closeSilently(inputStream2);
            return null;
        } catch (IOException e4) {
            inputStream = open;
            e = e4;
            e.printStackTrace();
            BaseMiscUtil.closeSilently(inputStream);
            return null;
        } catch (Throwable th3) {
            inputStream3 = open;
            th = th3;
            BaseMiscUtil.closeSilently(inputStream3);
            throw th;
        }
    }
}
