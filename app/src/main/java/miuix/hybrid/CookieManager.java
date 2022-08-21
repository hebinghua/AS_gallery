package miuix.hybrid;

import miuix.internal.hybrid.provider.WebViewFactory;

/* loaded from: classes3.dex */
public abstract class CookieManager {
    public boolean acceptCookie() {
        return false;
    }

    public boolean allowFileSchemeCookiesImpl() {
        return false;
    }

    public String getCookie(String str) {
        return null;
    }

    public boolean hasCookies() {
        return false;
    }

    public void removeAllCookie() {
    }

    public void removeExpiredCookie() {
    }

    public void removeSessionCookie() {
    }

    public void setAcceptCookie(boolean z) {
    }

    public void setAcceptFileSchemeCookiesImpl(boolean z) {
    }

    public void setCookie(String str, String str2) {
    }

    public static CookieManager getInstance() {
        return WebViewFactory.getProvider(null).getCookieManager();
    }

    public static boolean allowFileSchemeCookies() {
        return getInstance().allowFileSchemeCookiesImpl();
    }

    public static void setAcceptFileSchemeCookies(boolean z) {
        getInstance().setAcceptFileSchemeCookiesImpl(z);
    }
}
