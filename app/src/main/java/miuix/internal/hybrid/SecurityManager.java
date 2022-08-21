package miuix.internal.hybrid;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* loaded from: classes3.dex */
public class SecurityManager {
    public static String sPublicKey;
    public Config mConfig;
    public long mExpiredTime;
    public String mSign;
    public Boolean mValidSignature;

    public SecurityManager(Config config, Context context) {
        this.mConfig = config;
        if (config != null && config.getSecurity() != null) {
            this.mExpiredTime = config.getSecurity().getTimestamp();
            this.mSign = config.getSecurity().getSignature();
        }
        if (sPublicKey == null) {
            sPublicKey = getPublicKey(context);
        }
    }

    public boolean isExpired() {
        long j = this.mExpiredTime;
        return 0 < j && j < System.currentTimeMillis();
    }

    public boolean isValidSignature() {
        if (this.mValidSignature == null) {
            try {
                this.mValidSignature = Boolean.valueOf(isValidSignature(ConfigUtils.getRawConfig(this.mConfig), this.mSign));
            } catch (Exception unused) {
                this.mValidSignature = Boolean.FALSE;
            }
        }
        return this.mValidSignature.booleanValue();
    }

    public final boolean isValidSignature(String str, String str2) throws Exception {
        return str2 != null && SignUtils.verify(str, SignUtils.getPublicKey(sPublicKey), str2);
    }

    public final String getPublicKey(Context context) {
        InputStream open;
        BufferedReader bufferedReader;
        File file = new File(getHybridBaseFolder(context), "hybrid_key.pem");
        BufferedReader bufferedReader2 = null;
        try {
            try {
                if (file.exists()) {
                    open = new FileInputStream(file);
                } else {
                    open = context.getResources().getAssets().open("keys/hybrid_key.pem");
                }
                bufferedReader = new BufferedReader(new InputStreamReader(open));
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException unused) {
        }
        try {
            String readPublicKey = readPublicKey(bufferedReader);
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return readPublicKey;
        } catch (IOException unused2) {
            throw new IllegalStateException("cannot read hybrid key.");
        } catch (Throwable th2) {
            th = th2;
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            throw th;
        }
    }

    public final String readPublicKey(BufferedReader bufferedReader) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                if (!"".equals(readLine.trim()) && !readLine.startsWith("-----")) {
                    sb.append(readLine);
                    sb.append('\r');
                }
            } else {
                return sb.substring(0, sb.length() - 1);
            }
        }
    }

    public final File getHybridBaseFolder(Context context) {
        return new File(context.getFilesDir(), "miuisdk");
    }
}
