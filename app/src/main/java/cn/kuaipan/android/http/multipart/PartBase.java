package cn.kuaipan.android.http.multipart;

/* loaded from: classes.dex */
public abstract class PartBase extends Part {
    public String charSet;
    public String contentType;
    public String name;
    public String transferEncoding;

    public PartBase(String str, String str2, String str3, String str4) {
        if (str == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        this.name = str;
        this.contentType = str2;
        this.charSet = str3;
        this.transferEncoding = str4;
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public String getName() {
        return this.name;
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public String getContentType() {
        return this.contentType;
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public String getCharSet() {
        return this.charSet;
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public String getTransferEncoding() {
        return this.transferEncoding;
    }
}
