package miuix.internal.hybrid;

/* loaded from: classes3.dex */
public class Permission {
    public boolean applySubdomains;
    public boolean forbidden;
    public String uri;

    public String getUri() {
        return this.uri;
    }

    public void setUri(String str) {
        this.uri = str;
    }

    public boolean isApplySubdomains() {
        return this.applySubdomains;
    }

    public void setApplySubdomains(boolean z) {
        this.applySubdomains = z;
    }

    public void setForbidden(boolean z) {
        this.forbidden = z;
    }
}
