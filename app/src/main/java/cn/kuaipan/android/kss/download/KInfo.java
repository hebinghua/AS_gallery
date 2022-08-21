package cn.kuaipan.android.kss.download;

import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;
import cn.kuaipan.android.utils.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/* loaded from: classes.dex */
public class KInfo {
    public final File mFile;
    public final Bundle mData = new Bundle();
    public final Properties mProp = new Properties();

    public KInfo(File file) {
        this.mFile = file;
    }

    public String getHash() {
        return this.mData.getString("hash");
    }

    public void setHash(String str) {
        this.mData.putString("hash", str);
    }

    public boolean loadToMap(LoadMap loadMap) {
        return loadMap.load(this.mData.getBundle("load_map"));
    }

    public void setLoadMap(LoadMap loadMap) {
        Bundle bundle = new Bundle();
        loadMap.save(bundle);
        this.mData.putBundle("load_map", bundle);
    }

    public void delete() {
        this.mFile.delete();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v9, types: [java.io.OutputStream] */
    public void save() {
        Throwable th;
        FileOutputStream fileOutputStream;
        IOException e;
        Properties properties = this.mProp;
        ?? bundleToString = bundleToString(this.mData);
        properties.put("data", bundleToString);
        try {
            try {
                fileOutputStream = new FileOutputStream(this.mFile);
                try {
                    this.mProp.store(fileOutputStream, (String) null);
                    fileOutputStream.flush();
                    bundleToString = fileOutputStream;
                } catch (IOException e2) {
                    e = e2;
                    Log.w("KInfo", "Failed save kinfo to " + this.mFile, e);
                    bundleToString = fileOutputStream;
                    bundleToString.close();
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    bundleToString.close();
                } catch (Throwable unused) {
                }
                throw th;
            }
        } catch (IOException e3) {
            fileOutputStream = null;
            e = e3;
        } catch (Throwable th3) {
            bundleToString = 0;
            th = th3;
            bundleToString.close();
            throw th;
        }
        try {
            bundleToString.close();
        } catch (Throwable unused2) {
        }
    }

    public void load() {
        FileInputStream fileInputStream;
        Throwable th;
        IOException e;
        if (!this.mFile.exists()) {
            return;
        }
        try {
            fileInputStream = new FileInputStream(this.mFile);
            try {
                try {
                    this.mProp.load(fileInputStream);
                    String property = this.mProp.getProperty("data");
                    if (!TextUtils.isEmpty(property)) {
                        Bundle stringToBundle = stringToBundle(property);
                        this.mData.clear();
                        this.mData.putAll(stringToBundle);
                    }
                } catch (IOException e2) {
                    e = e2;
                    Log.w("KInfo", "Failed load kinfo from " + this.mFile, e);
                    fileInputStream.close();
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    fileInputStream.close();
                } catch (Throwable unused) {
                }
                throw th;
            }
        } catch (IOException e3) {
            fileInputStream = null;
            e = e3;
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            fileInputStream.close();
            throw th;
        }
        try {
            fileInputStream.close();
        } catch (Throwable unused2) {
        }
    }

    public static File getInfoFile(File file) {
        String parent = file.getParent();
        String str = file.getName() + ".kinfo";
        if (!str.startsWith(".")) {
            str = "." + str;
        }
        return new File(parent, str);
    }

    public static String bundleToString(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            bundle.writeToParcel(obtain, 0);
            return Base64.encodeToString(obtain.marshall(), 0);
        } finally {
            obtain.recycle();
        }
    }

    public static Bundle stringToBundle(String str) {
        byte[] decode = Base64.decode(str, 0);
        Parcel obtain = Parcel.obtain();
        try {
            obtain.unmarshall(decode, 0, decode.length);
            obtain.setDataPosition(0);
            return (Bundle) Bundle.CREATOR.createFromParcel(obtain);
        } finally {
            obtain.recycle();
        }
    }
}
