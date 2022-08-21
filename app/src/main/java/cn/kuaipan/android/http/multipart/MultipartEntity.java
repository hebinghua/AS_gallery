package cn.kuaipan.android.http.multipart;

import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EncodingUtils;

/* loaded from: classes.dex */
public class MultipartEntity extends AbstractHttpEntity {
    public static byte[] MULTIPART_CHARS = EncodingUtils.getAsciiBytes("-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public boolean contentConsumed = false;
    public byte[] multipartBoundary;
    public HttpParams params;
    public Part[] parts;

    public boolean isStreaming() {
        return false;
    }

    public static byte[] generateMultipartBoundary() {
        Random random = new Random();
        int nextInt = random.nextInt(11) + 30;
        byte[] bArr = new byte[nextInt];
        for (int i = 0; i < nextInt; i++) {
            byte[] bArr2 = MULTIPART_CHARS;
            bArr[i] = bArr2[random.nextInt(bArr2.length)];
        }
        return bArr;
    }

    public MultipartEntity(Part[] partArr) {
        setContentType("multipart/form-data");
        if (partArr == null) {
            throw new IllegalArgumentException("parts cannot be null");
        }
        this.parts = partArr;
        this.params = null;
    }

    public byte[] getMultipartBoundary() {
        if (this.multipartBoundary == null) {
            String str = null;
            HttpParams httpParams = this.params;
            if (httpParams != null) {
                str = (String) httpParams.getParameter("http.method.multipart.boundary");
            }
            if (str != null) {
                this.multipartBoundary = EncodingUtils.getAsciiBytes(str);
            } else {
                this.multipartBoundary = generateMultipartBoundary();
            }
        }
        return this.multipartBoundary;
    }

    public void appendPart(Part[] partArr) {
        if (partArr == null || partArr.length <= 0) {
            return;
        }
        Part[] partArr2 = this.parts;
        if (partArr2 == null || partArr2.length <= 0) {
            this.parts = partArr;
            return;
        }
        Part[] partArr3 = new Part[partArr2.length + partArr.length];
        this.parts = partArr3;
        System.arraycopy(partArr2, 0, partArr3, 0, partArr2.length);
        System.arraycopy(partArr, 0, this.parts, partArr2.length, partArr.length);
    }

    public boolean isRepeatable() {
        int i = 0;
        while (true) {
            Part[] partArr = this.parts;
            if (i < partArr.length) {
                if (!partArr[i].isRepeatable()) {
                    return false;
                }
                i++;
            } else {
                return true;
            }
        }
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        Part.sendParts(outputStream, this.parts, getMultipartBoundary());
    }

    public Header getContentType() {
        StringBuffer stringBuffer = new StringBuffer("multipart/form-data");
        stringBuffer.append("; boundary=");
        stringBuffer.append(EncodingUtils.getAsciiString(getMultipartBoundary()));
        return new BasicHeader("Content-Type", stringBuffer.toString());
    }

    public long getContentLength() {
        try {
            return Part.getLengthOfParts(this.parts, getMultipartBoundary());
        } catch (Exception e) {
            Log.e("Multipart", "An exception occurred while getting the length of the parts", e);
            return 0L;
        }
    }

    public InputStream getContent() throws IOException, IllegalStateException {
        if (!isRepeatable() && this.contentConsumed) {
            throw new IllegalStateException("Content has been consumed");
        }
        this.contentConsumed = true;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Part.sendParts(byteArrayOutputStream, this.parts, this.multipartBoundary);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
