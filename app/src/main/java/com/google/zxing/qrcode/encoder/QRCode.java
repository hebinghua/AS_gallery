package com.google.zxing.qrcode.encoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;

/* loaded from: classes.dex */
public final class QRCode {
    public ErrorCorrectionLevel ecLevel;
    public int maskPattern = -1;
    public ByteMatrix matrix;
    public Mode mode;
    public Version version;

    public static boolean isValidMaskPattern(int i) {
        return i >= 0 && i < 8;
    }

    public ByteMatrix getMatrix() {
        return this.matrix;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append("<<\n");
        sb.append(" mode: ");
        sb.append(this.mode);
        sb.append("\n ecLevel: ");
        sb.append(this.ecLevel);
        sb.append("\n version: ");
        sb.append(this.version);
        sb.append("\n maskPattern: ");
        sb.append(this.maskPattern);
        if (this.matrix == null) {
            sb.append("\n matrix: null\n");
        } else {
            sb.append("\n matrix:\n");
            sb.append(this.matrix);
        }
        sb.append(">>\n");
        return sb.toString();
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setECLevel(ErrorCorrectionLevel errorCorrectionLevel) {
        this.ecLevel = errorCorrectionLevel;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setMaskPattern(int i) {
        this.maskPattern = i;
    }

    public void setMatrix(ByteMatrix byteMatrix) {
        this.matrix = byteMatrix;
    }
}
