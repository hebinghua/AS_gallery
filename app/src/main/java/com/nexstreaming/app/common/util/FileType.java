package com.nexstreaming.app.common.util;

import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX WARN: Init of enum AAC can be incorrect */
/* JADX WARN: Init of enum ACC can be incorrect */
/* JADX WARN: Init of enum AVI can be incorrect */
/* JADX WARN: Init of enum BMP can be incorrect */
/* JADX WARN: Init of enum GIF can be incorrect */
/* JADX WARN: Init of enum JPEG can be incorrect */
/* JADX WARN: Init of enum K3G can be incorrect */
/* JADX WARN: Init of enum M4A can be incorrect */
/* JADX WARN: Init of enum M4V can be incorrect */
/* JADX WARN: Init of enum MOV can be incorrect */
/* JADX WARN: Init of enum MP3 can be incorrect */
/* JADX WARN: Init of enum MP4 can be incorrect */
/* JADX WARN: Init of enum PNG can be incorrect */
/* JADX WARN: Init of enum SVG can be incorrect */
/* JADX WARN: Init of enum WBMP can be incorrect */
/* JADX WARN: Init of enum WEBP can be incorrect */
/* JADX WARN: Init of enum WMV can be incorrect */
/* loaded from: classes3.dex */
public enum FileType {
    JPEG(r7, new String[]{"jpeg", "jpg"}, new int[]{255, 216, 255}),
    PNG(r7, new String[]{"png"}, new int[]{BaiduSceneResult.JEWELRY, 80, 78, 71, 13, 10, 26, 10}),
    SVG(r7, new String[]{"svg"}, new int[]{60, 115, 118, 103}, new int[]{60, 83, 86, 71}),
    WEBP(r7, new String[]{"webp"}, new int[]{82, 73, 70, 70, -1, -1, -1, -1, 87, 69, 66, 80}),
    GIF(r7, new String[]{"gif"}, new int[]{71, 73, 70, 56, 55, 97}, new int[]{71, 73, 70, 56, 57, 97}),
    M4A(r26, new String[]{"m4a"}, new int[]{0, 0, 0, 32, 102, 116, 121, 112, 77, 52, 65, 32}, new int[]{-1, -1, -1, -1, 102, 116, 121, 112, 77, 52, 65, 32}),
    M4V(r5, new String[]{"m4v"}, new int[]{0, 0, 0, 24, 102, 116, 121, 112, 109, 112, 52, 50}, new int[]{-1, -1, -1, -1, 102, 116, 121, 112, 109, 112, 52, 50}),
    MP4(r5, new String[]{"mp4"}, new int[]{0, 0, 0, 20, 102, 116, 121, 112, 105, 115, 111, 109}, new int[]{0, 0, 0, 24, 102, 116, 121, 112, 105, 115, 111, 109}, new int[]{0, 0, 0, 24, 102, 116, 121, 112, 51, 103, 112, 53}, new int[]{0, 0, 0, 28, 102, 116, 121, 112, 77, 83, 78, 86, 1, 41, 0, 70, 77, 83, 78, 86, 109, 112, 52, 50}, new int[]{-1, -1, -1, -1, 102, 116, 121, 112, 51, 103, 112, 53}, new int[]{-1, -1, -1, -1, 102, 116, 121, 112, 77, 83, 78, 86}, new int[]{-1, -1, -1, -1, 102, 116, 121, 112, 105, 115, 111, 109}, new int[]{0, 0, 0, 24, 102, 116, 121, 112, 109, 112, 52, 49}),
    F_3GP(FileCategory.VideoOrAudio, new String[]{"3gp", "3gpp", "3g2"}, new int[]{0, 0, 0, -1, 102, 116, 121, 112, 51, 103, 112}, new int[]{0, 0, 0, -1, 102, 116, 121, 112, 51, 103, 50}),
    K3G(r5, new String[]{"k3g"}),
    ACC(r5, new String[]{"acc"}),
    AVI(r5, new String[]{"avi"}, new int[]{82, 73, 70, 70, -1, -1, -1, -1, 65, 86, 73, 32, 76, 73, 83, 84}),
    MOV(r5, new String[]{"mov"}, new int[]{0, 0, 0, 20, 102, 116, 121, 112, 113, 116, 32, 32}, new int[]{102, 116, 121, 112, 113, 116, 32, 32}, new int[]{-1, -1, -1, -1, 109, 111, 111, 118}),
    WMV(r5, new String[]{"wmv"}, new int[]{48, 38, 178, 117, BaiduSceneResult.DIGITAL_PRODUCT, 102, 207, 17, 166, 217, 0, 170, 0, 98, 206, 108}),
    MP3(r26, new String[]{"mp3"}, new int[]{73, 68, 51}, new int[]{255, 251}),
    AAC(r26, new String[]{"aac"}, new int[]{255, 241}, new int[]{255, 249}),
    BMP(r7, new String[]{"bmp"}, new int[]{66, 77}),
    TTF(FileCategory.Font, new String[]{"ttf", "otf"}),
    WBMP(r7, new String[]{"wbmp"}, false);
    
    private static final int CHECK_SIZE = 32;
    private static final String LOG_TAG = "FileType";
    private static final byte[] WEBP_HEADER;
    private static final Map<String, FileType> extensionMap;
    private final FileCategory category;
    private final boolean extensionOnly;
    private final String[] extensions;
    private final a imp;
    private final boolean isSupportedFormat;

    /* loaded from: classes3.dex */
    public enum FileCategory {
        Audio,
        Video,
        VideoOrAudio,
        Image,
        Font
    }

    static {
        FileCategory fileCategory = FileCategory.Image;
        FileCategory fileCategory2 = FileCategory.Audio;
        FileCategory fileCategory3 = FileCategory.Video;
        extensionMap = new HashMap();
        WEBP_HEADER = new byte[]{82, 73, 70, 70, 87, 69, 66, 80};
    }

    FileType(FileCategory fileCategory, String[] strArr, boolean z) {
        this.imp = new a() { // from class: com.nexstreaming.app.common.util.FileType.1
            @Override // com.nexstreaming.app.common.util.FileType.a
            public boolean a(byte[] bArr) {
                return false;
            }
        };
        this.category = fileCategory;
        this.extensions = strArr;
        this.extensionOnly = false;
        this.isSupportedFormat = z;
    }

    FileType(FileCategory fileCategory, String[] strArr) {
        this.imp = new a() { // from class: com.nexstreaming.app.common.util.FileType.2
            @Override // com.nexstreaming.app.common.util.FileType.a
            public boolean a(byte[] bArr) {
                return false;
            }
        };
        this.category = fileCategory;
        this.extensions = strArr;
        this.extensionOnly = true;
        this.isSupportedFormat = true;
    }

    FileType(FileCategory fileCategory, String[] strArr, final int[]... iArr) {
        this.imp = new a() { // from class: com.nexstreaming.app.common.util.FileType.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.nexstreaming.app.common.util.FileType.a
            public boolean a(byte[] bArr) {
                int i = 0;
                while (true) {
                    int[][] iArr2 = iArr;
                    if (i < iArr2.length) {
                        int[] iArr3 = iArr2[i];
                        if (bArr.length >= iArr3.length) {
                            for (int i2 = 0; i2 < iArr3.length; i2++) {
                                if (iArr3[i2] != -1 && bArr[i2] != ((byte) iArr3[i2])) {
                                    break;
                                }
                            }
                            return true;
                        }
                        i++;
                    } else {
                        return false;
                    }
                }
            }
        };
        this.category = fileCategory;
        this.extensions = strArr;
        this.extensionOnly = false;
        this.isSupportedFormat = true;
    }

    private static void a() {
        FileType[] values;
        if (!extensionMap.isEmpty()) {
            return;
        }
        for (FileType fileType : values()) {
            for (String str : fileType.extensions) {
                extensionMap.put(str, fileType);
            }
        }
    }

    public boolean isSupportedFormat() {
        return this.isSupportedFormat;
    }

    public boolean isImage() {
        return this.category == FileCategory.Image;
    }

    public boolean isVideo() {
        FileCategory fileCategory = this.category;
        return fileCategory == FileCategory.Video || fileCategory == FileCategory.VideoOrAudio;
    }

    public boolean isAudio() {
        FileCategory fileCategory = this.category;
        return fileCategory == FileCategory.Audio || fileCategory == FileCategory.VideoOrAudio;
    }

    /* loaded from: classes3.dex */
    public static abstract class a {
        public abstract boolean a(byte[] bArr);

        private a() {
        }
    }

    public FileCategory getCategory() {
        return this.category;
    }

    public static FileType fromExtension(String str) {
        int lastIndexOf;
        if (str != null && (lastIndexOf = str.lastIndexOf(46)) >= 0 && lastIndexOf >= str.lastIndexOf(47)) {
            String lowerCase = str.substring(lastIndexOf + 1).toLowerCase(Locale.US);
            Map<String, FileType> map = extensionMap;
            if (map.isEmpty()) {
                a();
            }
            return map.get(lowerCase);
        }
        return null;
    }

    public static FileType fromExtension(File file) {
        if (file == null) {
            return null;
        }
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(46);
        String lowerCase = lastIndexOf >= 0 ? name.substring(lastIndexOf + 1).toLowerCase(Locale.US) : null;
        if (lowerCase == null) {
            return null;
        }
        Map<String, FileType> map = extensionMap;
        if (map.isEmpty()) {
            a();
        }
        return map.get(lowerCase);
    }

    public static FileType fromFile(String str) {
        if (str == null) {
            return null;
        }
        return fromFile(new File(str));
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00d6 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x00d4 A[EDGE_INSN: B:77:0x00d4->B:48:0x00d4 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.nexstreaming.app.common.util.FileType fromFile(java.io.File r14) {
        /*
            Method dump skipped, instructions count: 294
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.util.FileType.fromFile(java.io.File):com.nexstreaming.app.common.util.FileType");
    }
}
