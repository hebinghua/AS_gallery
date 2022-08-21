package cn.kuaipan.android.http.client;

import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public interface URIRedirector {
    boolean redirect(HttpContext httpContext);
}
