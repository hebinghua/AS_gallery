package cn.kuaipan.android.http.client;

import cn.kuaipan.android.utils.NetworkHelpers;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.apache.http.params.AbstractHttpParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public class KscHttpRoutePlanner extends DefaultHttpRoutePlanner {
    public KscHttpRoutePlanner(SchemeRegistry schemeRegistry) {
        super(schemeRegistry);
    }

    public HttpRoute determineRoute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException {
        HttpHost currentProxy = NetworkHelpers.getCurrentProxy();
        if (currentProxy != null) {
            AbstractHttpParams kscHttpParams = new KscHttpParams(httpRequest.getParams());
            ConnRouteParams.setDefaultProxy(kscHttpParams, currentProxy);
            httpRequest.setParams(kscHttpParams);
        }
        return super.determineRoute(httpHost, httpRequest, httpContext);
    }

    /* loaded from: classes.dex */
    public class KscHttpParams extends AbstractHttpParams {
        public final HttpParams mExtParams;
        public final HttpParams mOrgParams;

        public KscHttpParams(HttpParams httpParams) {
            this.mOrgParams = httpParams;
            this.mExtParams = new BasicHttpParams();
        }

        public KscHttpParams(HttpParams httpParams, HttpParams httpParams2) {
            this.mOrgParams = httpParams;
            this.mExtParams = httpParams2;
        }

        public Object getParameter(String str) {
            Object parameter = this.mExtParams.getParameter(str);
            return parameter == null ? this.mOrgParams.getParameter(str) : parameter;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public HttpParams setParameter(String str, Object obj) {
            this.mExtParams.setParameter(str, obj);
            return this;
        }

        public HttpParams copy() {
            return new KscHttpParams(this.mOrgParams, this.mExtParams.copy());
        }

        public boolean removeParameter(String str) {
            boolean removeParameter = this.mExtParams.removeParameter(str);
            if (!removeParameter) {
                try {
                    return this.mOrgParams.removeParameter(str);
                } catch (Exception unused) {
                    return removeParameter;
                }
            }
            return removeParameter;
        }
    }
}
