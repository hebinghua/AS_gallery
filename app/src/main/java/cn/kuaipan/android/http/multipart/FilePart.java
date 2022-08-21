package cn.kuaipan.android.http.multipart;

import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.EncodingUtils;

/* loaded from: classes.dex */
public class FilePart extends PartBase {
    public static final byte[] FILE_NAME_BYTES = EncodingUtils.getAsciiBytes("; filename=");
    public PartSource source;

    public FilePart(String str, PartSource partSource, String str2, String str3) {
        super(str, str2 == null ? "application/octet-stream" : str2, str3 == null ? "ISO-8859-1" : str3, "binary");
        if (partSource == null) {
            throw new IllegalArgumentException("Source may not be null");
        }
        this.source = partSource;
    }

    public FilePart(String str, File file) throws FileNotFoundException {
        this(str, new FilePartSource(file), null, null);
    }

    public FilePart(String str, String str2, byte[] bArr) {
        this(str, new ByteArrayPartSource(str2, bArr), null, null);
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public void sendDispositionHeader(OutputStream outputStream) throws IOException {
        super.sendDispositionHeader(outputStream);
        String fileName = this.source.getFileName();
        if (fileName != null) {
            outputStream.write(FILE_NAME_BYTES);
            byte[] bArr = Part.QUOTE_BYTES;
            outputStream.write(bArr);
            outputStream.write(EncodingUtils.getAsciiBytes(fileName));
            outputStream.write(bArr);
        }
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public void sendData(OutputStream outputStream) throws IOException {
        if (lengthOfData() == 0) {
            Log.d("FilePart", "No data to send.");
            return;
        }
        byte[] bArr = new byte[4096];
        InputStream createInputStream = this.source.createInputStream();
        while (true) {
            try {
                int read = createInputStream.read(bArr);
                if (read < 0) {
                    return;
                }
                outputStream.write(bArr, 0, read);
            } finally {
                createInputStream.close();
            }
        }
    }

    @Override // cn.kuaipan.android.http.multipart.Part
    public long lengthOfData() {
        return this.source.getLength();
    }
}
