package com.google.zxing.qrcode.decoder;

/* loaded from: classes.dex */
public enum ErrorCorrectionLevel {
    L(1),
    M(0),
    Q(3),
    H(2);
    
    public static final ErrorCorrectionLevel[] FOR_BITS;
    private final int bits;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static ErrorCorrectionLevel[] valuesCustom() {
        ErrorCorrectionLevel[] valuesCustom = values();
        int length = valuesCustom.length;
        ErrorCorrectionLevel[] errorCorrectionLevelArr = new ErrorCorrectionLevel[length];
        System.arraycopy(valuesCustom, 0, errorCorrectionLevelArr, 0, length);
        return errorCorrectionLevelArr;
    }

    static {
        ErrorCorrectionLevel errorCorrectionLevel = L;
        ErrorCorrectionLevel errorCorrectionLevel2 = M;
        ErrorCorrectionLevel errorCorrectionLevel3 = Q;
        FOR_BITS = new ErrorCorrectionLevel[]{errorCorrectionLevel2, errorCorrectionLevel, H, errorCorrectionLevel3};
    }

    ErrorCorrectionLevel(int i) {
        this.bits = i;
    }

    public int getBits() {
        return this.bits;
    }
}
