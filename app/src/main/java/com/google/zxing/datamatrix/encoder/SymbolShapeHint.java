package com.google.zxing.datamatrix.encoder;

/* loaded from: classes.dex */
public enum SymbolShapeHint {
    FORCE_NONE,
    FORCE_SQUARE,
    FORCE_RECTANGLE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static SymbolShapeHint[] valuesCustom() {
        SymbolShapeHint[] valuesCustom = values();
        int length = valuesCustom.length;
        SymbolShapeHint[] symbolShapeHintArr = new SymbolShapeHint[length];
        System.arraycopy(valuesCustom, 0, symbolShapeHintArr, 0, length);
        return symbolShapeHintArr;
    }
}
