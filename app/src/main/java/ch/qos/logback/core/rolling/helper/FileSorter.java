package ch.qos.logback.core.rolling.helper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FileSorter {
    private final List<FilenameParser> parsers;

    public FileSorter(FilenameParser... filenameParserArr) {
        this.parsers = Arrays.asList(filenameParserArr);
    }

    public void sort(String[] strArr) {
        Arrays.sort(strArr, new Comparator<String>() { // from class: ch.qos.logback.core.rolling.helper.FileSorter.1
            @Override // java.util.Comparator
            public int compare(String str, String str2) {
                int i = 0;
                for (FilenameParser filenameParser : FileSorter.this.parsers) {
                    Comparable parseFilename = filenameParser.parseFilename(str2);
                    Comparable parseFilename2 = filenameParser.parseFilename(str);
                    if (parseFilename != null && parseFilename2 != null) {
                        i += parseFilename.compareTo(parseFilename2);
                    }
                }
                return i == 0 ? str2.compareTo(str) : i;
            }
        });
    }
}
