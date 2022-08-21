package com.miui.gallery.vlog.caption.ai;

import android.util.Base64;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public class VoiceApiUtils {
    public static volatile boolean sInited;

    private static native byte[] nativeGetApiKey();

    private static native byte[] nativeGetApiSecret();

    private static native byte[] nativeGetAppID();

    public static void loadLibrary() {
        if (sInited) {
            return;
        }
        String libraryPath = ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).getLibraryPath();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(libraryPath);
            String str = File.separator;
            sb.append(str);
            sb.append("libcrypto.so");
            System.load(sb.toString());
            System.load(libraryPath + str + "libssl.so");
            System.load(libraryPath + str + "libgallery_caption_jni.so");
            sInited = true;
        } catch (Throwable th) {
            DefaultLogger.e("VoiceApiUtils", "load ai caption lib failed.", th);
        }
    }

    public static String getAppID() {
        return new String(nativeGetAppID());
    }

    public static String getApiKey() {
        return new String(nativeGetApiKey());
    }

    public static String getApiSecret() {
        return new String(nativeGetApiSecret());
    }

    public static String getUrl() {
        try {
            URL url = new URL("wss://xiaomi-ist-api.xfyun.cn/v2/ist".replace("ws://", h.e).replace("wss://", h.f));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String format = simpleDateFormat.format(new Date());
            String host = url.getHost();
            StringBuilder sb = new StringBuilder("host: ");
            sb.append(host);
            sb.append("\n");
            sb.append("date: ");
            sb.append(format);
            sb.append("\n");
            sb.append("GET ");
            sb.append(url.getPath());
            sb.append(" HTTP/1.1");
            Charset forName = Charset.forName(Keyczar.DEFAULT_ENCODING);
            Mac mac = Mac.getInstance("hmacsha256");
            System.out.println(sb.toString());
            mac.init(new SecretKeySpec(getApiSecret().getBytes(forName), "hmacsha256"));
            return String.format("%s?authorization=%s&host=%s&date=%s", "wss://xiaomi-ist-api.xfyun.cn/v2/ist", URLEncoder.encode(encodeToStringBase64RFC4648(String.format("hmac username=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", getApiKey(), "hmac-sha256", "host date request-line", encodeToStringBase64RFC4648(mac.doFinal(sb.toString().getBytes(forName)))).getBytes(forName))), URLEncoder.encode(host), URLEncoder.encode(format));
        } catch (Exception e) {
            DefaultLogger.e("VoiceApiUtils", "assembleRequestUrl: " + e);
            return "";
        }
    }

    public static String encodeToStringBase64RFC4648(byte[] bArr) {
        return Base64.encodeToString(bArr, 2);
    }
}
