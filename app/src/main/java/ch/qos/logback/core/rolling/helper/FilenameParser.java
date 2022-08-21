package ch.qos.logback.core.rolling.helper;

import java.lang.Comparable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface FilenameParser<T extends Comparable<T>> {
    T parseFilename(String str);
}
