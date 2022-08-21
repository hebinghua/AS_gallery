package cn.kuaipan.android.kss.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.InputStreamEntity;

/* loaded from: classes.dex */
public class KssInputStreamEntity extends InputStreamEntity {
    public final InputStream content;

    public KssInputStreamEntity(InputStream inputStream, long j) {
        super(inputStream, j);
        inputStream.mark((int) Math.min(2147483647L, j));
        this.content = inputStream;
    }

    public boolean isRepeatable() {
        return super.isRepeatable() || this.content.markSupported();
    }

    public InputStream getContent() throws IOException {
        this.content.reset();
        return super.getContent();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        this.content.reset();
        super.writeTo(outputStream);
    }
}
