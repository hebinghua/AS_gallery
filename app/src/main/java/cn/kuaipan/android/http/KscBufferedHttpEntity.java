package cn.kuaipan.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/* loaded from: classes.dex */
public class KscBufferedHttpEntity extends HttpEntityWrapper {
    public final RandomInputBuffer buffer;
    public final IOException err;

    public boolean isRepeatable() {
        return true;
    }

    public KscBufferedHttpEntity(HttpEntity httpEntity, NetCacheManager netCacheManager) {
        super(httpEntity);
        RandomInputBuffer randomInputBuffer = null;
        IOException iOException = null;
        if (!httpEntity.isRepeatable() || httpEntity.getContentLength() < 0) {
            try {
                RandomInputBuffer randomInputBuffer2 = new RandomInputBuffer(httpEntity.getContent(), netCacheManager);
                e = null;
                randomInputBuffer = randomInputBuffer2;
            } catch (IOException e) {
                e = e;
            }
            this.buffer = randomInputBuffer;
            iOException = e;
        } else {
            this.buffer = null;
        }
        this.err = iOException;
    }

    public long getContentLength() {
        return ((HttpEntityWrapper) this).wrappedEntity.getContentLength();
    }

    public InputStream getContent() throws IOException {
        if (this.buffer != null) {
            return new BufferInputStream(this.buffer);
        }
        IOException iOException = this.err;
        if (iOException == null) {
            return ((HttpEntityWrapper) this).wrappedEntity.getContent();
        }
        throw iOException;
    }

    public boolean isChunked() {
        return this.buffer == null && ((HttpEntityWrapper) this).wrappedEntity.isChunked();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        if (this.buffer != null) {
            InputStream inputStream = null;
            try {
                inputStream = getContent();
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read != -1) {
                        outputStream.write(bArr, 0, read);
                    } else {
                        inputStream.close();
                        return;
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } else {
            ((HttpEntityWrapper) this).wrappedEntity.writeTo(outputStream);
        }
    }

    public boolean isStreaming() {
        return this.buffer == null && ((HttpEntityWrapper) this).wrappedEntity.isStreaming();
    }

    public void consumeContent() throws IOException {
        RandomInputBuffer randomInputBuffer = this.buffer;
        if (randomInputBuffer != null) {
            randomInputBuffer.close();
        }
        super.consumeContent();
    }
}
