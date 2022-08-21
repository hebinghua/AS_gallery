package okhttp3;

import java.io.IOException;

/* loaded from: classes3.dex */
public interface Interceptor {

    /* loaded from: classes3.dex */
    public interface Chain {
        int connectTimeoutMillis();

        Response proceed(Request request) throws IOException;

        int readTimeoutMillis();

        Request request();

        int writeTimeoutMillis();
    }

    Response intercept(Chain chain) throws IOException;
}
