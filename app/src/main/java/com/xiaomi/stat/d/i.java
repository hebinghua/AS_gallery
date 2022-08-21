package com.xiaomi.stat.d;

import android.text.TextUtils;
import com.xiaomi.stat.ak;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/* loaded from: classes3.dex */
public class i {
    public static final int a = 10000;
    public static final int b = 15000;
    private static final String c = "GET";
    private static final String d = "POST";
    private static final String e = "&";
    private static final String f = "=";
    private static final String g = "UTF-8";

    private i() {
    }

    public static String a(String str) throws IOException {
        return a(str, null, false);
    }

    public static String a(String str, Map<String, String> map) throws IOException {
        return a(str, map, true);
    }

    public static String a(String str, Map<String, String> map, boolean z) throws IOException {
        return a(c, str, map, z);
    }

    public static String b(String str, Map<String, String> map) throws IOException {
        return b(str, map, true);
    }

    public static String b(String str, Map<String, String> map, boolean z) throws IOException {
        return a(d, str, map, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8 */
    private static String a(String str, String str2, Map<String, String> map, boolean z) {
        String a2;
        HttpURLConnection httpURLConnection;
        OutputStream outputStream;
        InputStream inputStream;
        String str3;
        InputStream inputStream2 = null;
        if (map == null) {
            a2 = null;
        } else {
            try {
                a2 = a(map, z);
            } catch (IOException e2) {
                e = e2;
                httpURLConnection = null;
                outputStream = null;
                inputStream = outputStream;
                k.e(String.format("HttpUtil %s failed, url: %s, error: %s", str, str2, e.getMessage()));
                j.a(inputStream);
                j.a((OutputStream) outputStream);
                j.a(httpURLConnection);
                return null;
            } catch (Throwable th) {
                th = th;
                httpURLConnection = null;
                outputStream = 0;
                j.a(inputStream2);
                j.a((OutputStream) outputStream);
                j.a(httpURLConnection);
                throw th;
            }
        }
        if (!c.equals(str) || a2 == null) {
            str3 = str2;
        } else {
            str3 = str2 + "? " + a2;
        }
        httpURLConnection = (HttpURLConnection) new URL(str3).openConnection();
        try {
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(b);
            try {
                try {
                    if (c.equals(str)) {
                        httpURLConnection.setRequestMethod(c);
                    } else if (d.equals(str) && a2 != null) {
                        httpURLConnection.setRequestMethod(d);
                        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        httpURLConnection.setDoOutput(true);
                        byte[] bytes = a2.getBytes("UTF-8");
                        outputStream = httpURLConnection.getOutputStream();
                        try {
                            outputStream.write(bytes, 0, bytes.length);
                            outputStream.flush();
                            int responseCode = httpURLConnection.getResponseCode();
                            inputStream = httpURLConnection.getInputStream();
                            byte[] b2 = j.b(inputStream);
                            k.b(String.format("HttpUtil %s succeed url: %s, code: %s", str, str2, Integer.valueOf(responseCode)));
                            String str4 = new String(b2, "UTF-8");
                            j.a(inputStream);
                            j.a((OutputStream) outputStream);
                            j.a(httpURLConnection);
                            return str4;
                        } catch (IOException e3) {
                            e = e3;
                            inputStream = null;
                            k.e(String.format("HttpUtil %s failed, url: %s, error: %s", str, str2, e.getMessage()));
                            j.a(inputStream);
                            j.a((OutputStream) outputStream);
                            j.a(httpURLConnection);
                            return null;
                        } catch (Throwable th2) {
                            th = th2;
                            j.a(inputStream2);
                            j.a((OutputStream) outputStream);
                            j.a(httpURLConnection);
                            throw th;
                        }
                    }
                    byte[] b22 = j.b(inputStream);
                    k.b(String.format("HttpUtil %s succeed url: %s, code: %s", str, str2, Integer.valueOf(responseCode)));
                    String str42 = new String(b22, "UTF-8");
                    j.a(inputStream);
                    j.a((OutputStream) outputStream);
                    j.a(httpURLConnection);
                    return str42;
                } catch (IOException e4) {
                    e = e4;
                    k.e(String.format("HttpUtil %s failed, url: %s, error: %s", str, str2, e.getMessage()));
                    j.a(inputStream);
                    j.a((OutputStream) outputStream);
                    j.a(httpURLConnection);
                    return null;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream2 = inputStream;
                j.a(inputStream2);
                j.a((OutputStream) outputStream);
                j.a(httpURLConnection);
                throw th;
            }
            outputStream = null;
            int responseCode2 = httpURLConnection.getResponseCode();
            inputStream = httpURLConnection.getInputStream();
        } catch (IOException e5) {
            e = e5;
            outputStream = 0;
            inputStream = outputStream;
            k.e(String.format("HttpUtil %s failed, url: %s, error: %s", str, str2, e.getMessage()));
            j.a(inputStream);
            j.a((OutputStream) outputStream);
            j.a(httpURLConnection);
            return null;
        } catch (Throwable th4) {
            th = th4;
            outputStream = 0;
        }
    }

    private static String a(Map<String, String> map, boolean z) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                if (!TextUtils.isEmpty(entry.getKey())) {
                    if (sb.length() > 0) {
                        sb.append(e);
                    }
                    sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    sb.append(f);
                    sb.append(URLEncoder.encode(entry.getValue() == null ? "null" : entry.getValue(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException unused) {
                k.e("format params failed");
            }
        }
        if (z) {
            String a2 = a(map);
            if (sb.length() > 0) {
                sb.append(e);
            }
            sb.append(URLEncoder.encode(com.xiaomi.stat.d.f, "UTF-8"));
            sb.append(f);
            sb.append(URLEncoder.encode(a2, "UTF-8"));
        }
        return sb.toString();
    }

    public static String a(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            ArrayList<String> arrayList = new ArrayList(map.keySet());
            Collections.sort(arrayList);
            for (String str : arrayList) {
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str);
                    sb.append(map.get(str));
                }
            }
        }
        sb.append(ak.c());
        return g.c(sb.toString());
    }
}
