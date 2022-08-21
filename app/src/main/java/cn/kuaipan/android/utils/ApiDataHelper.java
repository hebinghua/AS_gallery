package cn.kuaipan.android.utils;

import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.http.KscHttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.json.JSONException;

/* loaded from: classes.dex */
public class ApiDataHelper {
    public static Map<String, Object> contentToMap(KscHttpResponse kscHttpResponse) throws KscException, InterruptedException {
        InputStream inputStream = null;
        try {
            try {
                try {
                    try {
                        InputStream content = kscHttpResponse.getContent();
                        if (content == null) {
                            throw new KscException(501001, kscHttpResponse.dump());
                        }
                        Map<String, Object> map = (Map) JsonUtils.parser(content);
                        if (map == null || map.isEmpty()) {
                            throw new KscException(501003, kscHttpResponse.dump());
                        }
                        try {
                            content.close();
                        } catch (Throwable unused) {
                        }
                        return map;
                    } catch (JSONException e) {
                        throw new KscException(501001, kscHttpResponse.dump(), e);
                    }
                } catch (IOException e2) {
                    throw KscException.newException(e2, kscHttpResponse.dump());
                }
            } catch (android.util.MalformedJsonException e3) {
                throw new KscException(501001, kscHttpResponse.dump(), e3);
            } catch (ClassCastException e4) {
                throw new KscException(501003, kscHttpResponse.dump(), e4);
            }
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (Throwable unused2) {
            }
            throw th;
        }
    }

    public static String asString(Map<String, Object> map, String str) {
        if (map == null) {
            throw new IllegalArgumentException("DataMap can't be null when parse.");
        }
        Object obj = map.get(str);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    public static Number asNumber(Object obj, Number number) {
        if (obj == null) {
            return number;
        }
        if (obj instanceof Number) {
            return (Number) obj;
        }
        String obj2 = obj.toString();
        try {
            return Long.valueOf(Long.parseLong(obj2));
        } catch (NumberFormatException unused) {
            return Double.valueOf(Double.parseDouble(obj2));
        }
    }
}
