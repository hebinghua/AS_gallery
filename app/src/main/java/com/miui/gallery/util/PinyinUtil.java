package com.miui.gallery.util;

import java.util.Iterator;
import miuix.pinyin.utilities.ChinesePinyinConverter;

/* loaded from: classes2.dex */
public class PinyinUtil {
    public static String get(String str) {
        StringBuilder sb = new StringBuilder();
        Iterator<ChinesePinyinConverter.Token> it = ChinesePinyinConverter.getInstance(StaticContext.sGetAndroidContext()).get(str).iterator();
        while (it.hasNext()) {
            sb.append(it.next().target);
        }
        return sb.toString();
    }
}
