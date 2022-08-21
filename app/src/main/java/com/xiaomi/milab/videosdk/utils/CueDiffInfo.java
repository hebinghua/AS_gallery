package com.xiaomi.milab.videosdk.utils;

import android.graphics.Rect;
import android.text.TextPaint;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class CueDiffInfo {
    public List<CharacterDiffResult> differentList;
    public int mTextHeight;
    public String oldText;
    public String text;
    public List<Float> oldGapList = new ArrayList();
    public List<Float> gapList = new ArrayList();

    public CueDiffInfo(String str, String str2, TextPaint textPaint) {
        this.text = str;
        this.oldText = str2;
        if (str == null || str2 == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        this.differentList = arrayList;
        arrayList.addAll(CharacterUtils.diff(str2, str));
        for (int i = 0; i < str2.length(); i++) {
            this.oldGapList.add(Float.valueOf(textPaint.measureText(String.valueOf(str2.charAt(i)))));
        }
        for (int i2 = 0; i2 < str.length(); i2++) {
            this.gapList.add(Float.valueOf(textPaint.measureText(String.valueOf(str.charAt(i2)))));
        }
        Rect rect = new Rect();
        textPaint.getTextBounds(str, 0, str.length(), rect);
        this.mTextHeight = rect.height();
    }
}
