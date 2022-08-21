package ch.qos.logback.core.rolling.helper;

import android.text.TextUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
class FileFinder {
    private static final String REGEX_MARKER_END = "(?:\uffff)?";
    private static final String REGEX_MARKER_START = "(?:\ufffe)?";
    private FileProvider fileProvider;

    public FileFinder(FileProvider fileProvider) {
        this.fileProvider = fileProvider;
    }

    public List<String> findFiles(String str) {
        List<PathPart> splitPath = splitPath(str);
        return toAbsolutePaths(findFiles(splitPath.get(0).listFiles(this.fileProvider), splitPath, 1));
    }

    public List<String> findDirs(String str) {
        List<PathPart> splitPath = splitPath(str);
        ArrayList arrayList = new ArrayList();
        findDirs(splitPath.get(0).listFiles(this.fileProvider), splitPath, 1, arrayList);
        return toAbsolutePaths(arrayList);
    }

    private List<String> toAbsolutePaths(List<File> list) {
        ArrayList arrayList = new ArrayList();
        for (File file : list) {
            arrayList.add(file.getAbsolutePath());
        }
        return arrayList;
    }

    private List<File> findFiles(List<File> list, List<PathPart> list2, int i) {
        ArrayList arrayList = new ArrayList();
        PathPart pathPart = list2.get(i);
        if (i >= list2.size() - 1) {
            for (File file : list) {
                if (pathPart.matches(file)) {
                    arrayList.add(file);
                }
            }
            return arrayList;
        }
        for (File file2 : list) {
            if (this.fileProvider.isDirectory(file2) && pathPart.matches(file2)) {
                arrayList.addAll(findFiles(Arrays.asList(this.fileProvider.listFiles(file2, null)), list2, i + 1));
            }
        }
        return arrayList;
    }

    private void findDirs(List<File> list, List<PathPart> list2, int i, List<File> list3) {
        if (i >= list2.size() - 1) {
            return;
        }
        PathPart pathPart = list2.get(i);
        for (File file : list) {
            if (this.fileProvider.isDirectory(file) && pathPart.matches(file)) {
                list3.add(file);
                findDirs(Arrays.asList(this.fileProvider.listFiles(file, null)), list2, i + 1, list3);
            }
        }
    }

    public List<PathPart> splitPath(String str) {
        String[] split;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (String str2 : str.split(File.separator)) {
            boolean z = str2.contains(REGEX_MARKER_START) && str2.contains(REGEX_MARKER_END);
            String replace = str2.replace(REGEX_MARKER_START, "").replace(REGEX_MARKER_END, "");
            if (z) {
                if (!arrayList2.isEmpty()) {
                    arrayList.add(new LiteralPathPart(TextUtils.join(File.separator, arrayList2)));
                    arrayList2.clear();
                }
                arrayList.add(new RegexPathPart(replace));
            } else {
                arrayList2.add(replace);
            }
        }
        if (!arrayList2.isEmpty()) {
            arrayList.add(new LiteralPathPart(TextUtils.join(File.separator, arrayList2)));
        }
        return arrayList;
    }

    public static String regexEscapePath(String str) {
        String str2 = File.separator;
        if (str.contains(str2)) {
            String[] split = str.split(str2);
            for (int i = 0; i < split.length; i++) {
                if (split[i].length() > 0) {
                    split[i] = REGEX_MARKER_START + split[i] + REGEX_MARKER_END;
                }
            }
            return TextUtils.join(File.separator, split);
        }
        return REGEX_MARKER_START + str + REGEX_MARKER_END;
    }

    public static String unescapePath(String str) {
        return str.replace(REGEX_MARKER_START, "").replace(REGEX_MARKER_END, "");
    }
}
