package com.miui.gallery3d.exif;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes3.dex */
public class ExifReader {
    public final ExifInterface mInterface;

    public ExifReader(ExifInterface exifInterface) {
        this.mInterface = exifInterface;
    }

    public ExifData read(InputStream inputStream) throws ExifInvalidFormatException, IOException {
        ExifParser parse = ExifParser.parse(inputStream, this.mInterface);
        ExifData exifData = new ExifData(parse.getByteOrder());
        for (int next = parse.next(); next != 5; next = parse.next()) {
            if (next == 0) {
                exifData.addIfdData(new IfdData(parse.getCurrentIfd()));
            } else if (next == 1) {
                ExifTag tag = parse.getTag();
                if (!tag.hasValue()) {
                    parse.registerForTagValue(tag);
                } else {
                    exifData.getIfdData(tag.getIfd()).setTag(tag);
                }
            } else if (next == 2) {
                ExifTag tag2 = parse.getTag();
                if (tag2.getDataType() == 7) {
                    parse.readFullTagValue(tag2);
                }
                exifData.getIfdData(tag2.getIfd()).setTag(tag2);
            } else if (next == 3) {
                int compressedImageSize = parse.getCompressedImageSize();
                if (compressedImageSize > 0) {
                    byte[] bArr = new byte[compressedImageSize];
                    if (compressedImageSize == parse.read(bArr)) {
                        exifData.setCompressedThumbnail(bArr);
                    } else {
                        Log.w("ExifReader", "Failed to read the compressed thumbnail");
                    }
                } else {
                    Log.d("ExifReader", "compressedImageSize=" + compressedImageSize);
                }
            } else if (next == 4) {
                int stripSize = parse.getStripSize();
                byte[] bArr2 = new byte[stripSize];
                if (stripSize == parse.read(bArr2)) {
                    exifData.setStripBytes(parse.getStripIndex(), bArr2);
                } else {
                    Log.w("ExifReader", "Failed to read the strip bytes");
                }
            }
        }
        return exifData;
    }
}
