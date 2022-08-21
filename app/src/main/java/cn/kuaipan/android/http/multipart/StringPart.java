package cn.kuaipan.android.http.multipart;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.util.EncodingUtils;

/* loaded from: classes.dex */
public class StringPart extends PartBase {
    public byte[] content;
    public String value;

    public StringPart(String str, String str2, String str3) {
        super(str, "text/plain", str3 == null ? "US-ASCII" : str3, "8bit");
        if (str2 == null) {
            throw new IllegalArgumentException("Value may not be null");
        }
        if (str2.indexOf(0) != -1) {
            throw new IllegalArgumentException("NULs may not be present in string parts");
        }
        this.value = str2;
    }

    public final byte[] getContent() {
        if (this.content == null) {
            this.content = EncodingUtils.getBytes(this.value, getCharSet());
        }
        return this.content;
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public void sendData(OutputStream outputStream) throws IOException {
        outputStream.write(getContent());
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public long lengthOfData() {
        return getContent().length;
    }
}
