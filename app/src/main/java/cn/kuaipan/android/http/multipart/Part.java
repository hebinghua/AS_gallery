package cn.kuaipan.android.http.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.util.EncodingUtils;

/* loaded from: classes.dex */
public abstract class Part {
    public static final byte[] BOUNDARY_BYTES;
    public static final byte[] CHARSET_BYTES;
    public static final byte[] CONTENT_DISPOSITION_BYTES;
    public static final byte[] CONTENT_TRANSFER_ENCODING_BYTES;
    public static final byte[] CONTENT_TYPE_BYTES;
    public static final byte[] CRLF_BYTES;
    public static final byte[] DEFAULT_BOUNDARY_BYTES;
    public static final byte[] EXTRA_BYTES;
    public static final byte[] QUOTE_BYTES;
    public byte[] boundaryBytes;

    public abstract String getCharSet();

    public abstract String getContentType();

    public abstract String getName();

    public abstract String getTransferEncoding();

    public boolean isRepeatable() {
        return true;
    }

    public abstract long lengthOfData() throws IOException;

    public abstract void sendData(OutputStream outputStream) throws IOException;

    static {
        byte[] asciiBytes = EncodingUtils.getAsciiBytes("----------------314159265358979323846");
        BOUNDARY_BYTES = asciiBytes;
        DEFAULT_BOUNDARY_BYTES = asciiBytes;
        CRLF_BYTES = EncodingUtils.getAsciiBytes("\r\n");
        QUOTE_BYTES = EncodingUtils.getAsciiBytes("\"");
        EXTRA_BYTES = EncodingUtils.getAsciiBytes("--");
        CONTENT_DISPOSITION_BYTES = EncodingUtils.getAsciiBytes("Content-Disposition: form-data; name=");
        CONTENT_TYPE_BYTES = EncodingUtils.getAsciiBytes("Content-Type: ");
        CHARSET_BYTES = EncodingUtils.getAsciiBytes("; charset=");
        CONTENT_TRANSFER_ENCODING_BYTES = EncodingUtils.getAsciiBytes("Content-Transfer-Encoding: ");
    }

    public byte[] getPartBoundary() {
        byte[] bArr = this.boundaryBytes;
        return bArr == null ? DEFAULT_BOUNDARY_BYTES : bArr;
    }

    public void setPartBoundary(byte[] bArr) {
        this.boundaryBytes = bArr;
    }

    public void sendStart(OutputStream outputStream) throws IOException {
        outputStream.write(EXTRA_BYTES);
        outputStream.write(getPartBoundary());
        outputStream.write(CRLF_BYTES);
    }

    public void sendDispositionHeader(OutputStream outputStream) throws IOException {
        outputStream.write(CONTENT_DISPOSITION_BYTES);
        byte[] bArr = QUOTE_BYTES;
        outputStream.write(bArr);
        outputStream.write(EncodingUtils.getAsciiBytes(getName()));
        outputStream.write(bArr);
    }

    public void sendContentTypeHeader(OutputStream outputStream) throws IOException {
        String contentType = getContentType();
        if (contentType != null) {
            outputStream.write(CRLF_BYTES);
            outputStream.write(CONTENT_TYPE_BYTES);
            outputStream.write(EncodingUtils.getAsciiBytes(contentType));
            String charSet = getCharSet();
            if (charSet == null) {
                return;
            }
            outputStream.write(CHARSET_BYTES);
            outputStream.write(EncodingUtils.getAsciiBytes(charSet));
        }
    }

    public void sendTransferEncodingHeader(OutputStream outputStream) throws IOException {
        String transferEncoding = getTransferEncoding();
        if (transferEncoding != null) {
            outputStream.write(CRLF_BYTES);
            outputStream.write(CONTENT_TRANSFER_ENCODING_BYTES);
            outputStream.write(EncodingUtils.getAsciiBytes(transferEncoding));
        }
    }

    public void sendEndOfHeader(OutputStream outputStream) throws IOException {
        byte[] bArr = CRLF_BYTES;
        outputStream.write(bArr);
        outputStream.write(bArr);
    }

    public void sendEnd(OutputStream outputStream) throws IOException {
        outputStream.write(CRLF_BYTES);
    }

    public void send(OutputStream outputStream) throws IOException {
        sendStart(outputStream);
        sendDispositionHeader(outputStream);
        sendContentTypeHeader(outputStream);
        sendTransferEncodingHeader(outputStream);
        sendEndOfHeader(outputStream);
        sendData(outputStream);
        sendEnd(outputStream);
    }

    public long length() throws IOException {
        if (lengthOfData() < 0) {
            return -1L;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        sendStart(byteArrayOutputStream);
        sendDispositionHeader(byteArrayOutputStream);
        sendContentTypeHeader(byteArrayOutputStream);
        sendTransferEncodingHeader(byteArrayOutputStream);
        sendEndOfHeader(byteArrayOutputStream);
        sendEnd(byteArrayOutputStream);
        return byteArrayOutputStream.size() + lengthOfData();
    }

    public String toString() {
        return getName();
    }

    public static void sendParts(OutputStream outputStream, Part[] partArr, byte[] bArr) throws IOException {
        if (partArr == null) {
            throw new IllegalArgumentException("Parts may not be null");
        }
        if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("partBoundary may not be empty");
        }
        for (int i = 0; i < partArr.length; i++) {
            partArr[i].setPartBoundary(bArr);
            partArr[i].send(outputStream);
        }
        byte[] bArr2 = EXTRA_BYTES;
        outputStream.write(bArr2);
        outputStream.write(bArr);
        outputStream.write(bArr2);
        outputStream.write(CRLF_BYTES);
    }

    public static long getLengthOfParts(Part[] partArr, byte[] bArr) throws IOException {
        if (partArr == null) {
            throw new IllegalArgumentException("Parts may not be null");
        }
        long j = 0;
        for (int i = 0; i < partArr.length; i++) {
            partArr[i].setPartBoundary(bArr);
            long length = partArr[i].length();
            if (length < 0) {
                return -1L;
            }
            j += length;
        }
        byte[] bArr2 = EXTRA_BYTES;
        return j + bArr2.length + bArr.length + bArr2.length + CRLF_BYTES.length;
    }
}
