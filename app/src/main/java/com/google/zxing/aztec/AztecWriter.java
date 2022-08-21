package com.google.zxing.aztec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.aztec.encoder.AztecCode;
import com.google.zxing.aztec.encoder.Encoder;
import com.google.zxing.common.BitMatrix;
import java.nio.charset.Charset;
import java.util.Map;

/* loaded from: classes.dex */
public final class AztecWriter implements Writer {
    public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) {
        Number number = null;
        String str2 = map == null ? null : (String) map.get(EncodeHintType.CHARACTER_SET);
        Number number2 = map == null ? null : (Number) map.get(EncodeHintType.ERROR_CORRECTION);
        if (map != null) {
            number = (Number) map.get(EncodeHintType.AZTEC_LAYERS);
        }
        return encode(str, barcodeFormat, i, i2, str2 == null ? DEFAULT_CHARSET : Charset.forName(str2), number2 == null ? 33 : number2.intValue(), number == null ? 0 : number.intValue());
    }

    public static BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Charset charset, int i3, int i4) {
        if (barcodeFormat != BarcodeFormat.AZTEC) {
            throw new IllegalArgumentException("Can only encode AZTEC, but got " + barcodeFormat);
        }
        return renderResult(Encoder.encode(str.getBytes(charset), i3, i4), i, i2);
    }

    public static BitMatrix renderResult(AztecCode aztecCode, int i, int i2) {
        BitMatrix matrix = aztecCode.getMatrix();
        if (matrix == null) {
            throw new IllegalStateException();
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int max = Math.max(i, width);
        int max2 = Math.max(i2, height);
        int min = Math.min(max / width, max2 / height);
        int i3 = (max - (width * min)) / 2;
        int i4 = (max2 - (height * min)) / 2;
        BitMatrix bitMatrix = new BitMatrix(max, max2);
        int i5 = 0;
        while (i5 < height) {
            int i6 = 0;
            int i7 = i3;
            while (i6 < width) {
                if (matrix.get(i6, i5)) {
                    bitMatrix.setRegion(i7, i4, min, min);
                }
                i6++;
                i7 += min;
            }
            i5++;
            i4 += min;
        }
        return bitMatrix;
    }
}
