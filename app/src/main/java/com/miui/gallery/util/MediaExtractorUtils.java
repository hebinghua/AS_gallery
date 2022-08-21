package com.miui.gallery.util;

import android.content.Context;
import android.media.MediaExtractor;
import android.net.Uri;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class MediaExtractorUtils {

    /* loaded from: classes2.dex */
    public interface MediaExtractorInitializer<T> {
        void initialize(MediaExtractor mediaExtractor, T t) throws IOException;
    }

    public static VideoInfo file(String str) {
        return extractManager(new FilePathInitializer(), str);
    }

    public static VideoInfo uri(Context context, Uri uri, Map<String, String> map) {
        return extractManager(new UriInitializer(context, map), uri);
    }

    public static <T> VideoInfo extractManager(MediaExtractorInitializer<T> mediaExtractorInitializer, T t) {
        VideoInfo videoInfo = new VideoInfo();
        MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            try {
                mediaExtractorInitializer.initialize(mediaExtractor, t);
                videoInfo = extractLabor(mediaExtractor);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DefaultLogger.d("MediaExtractorUtils", "extract video info[%s] for %s", videoInfo, t);
            return videoInfo;
        } finally {
            mediaExtractor.release();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0042, code lost:
        r6.setFrameRate(r9.getInteger("frame-rate")).setWidth(r9.getInteger(com.nexstreaming.nexeditorsdk.nexExportFormat.TAG_FORMAT_WIDTH)).setHeight(r9.getInteger(com.nexstreaming.nexeditorsdk.nexExportFormat.TAG_FORMAT_HEIGHT));
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x005d, code lost:
        if (r9.containsKey("color-transfer") == false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x005f, code lost:
        r6.setColorTransfer(r9.getInteger("color-transfer"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006a, code lost:
        if (r9.containsKey("color-standard") == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x006c, code lost:
        r6.setColorStandard(r9.getInteger("color-standard"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0077, code lost:
        if (r9.containsKey("profile") == false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0079, code lost:
        r6.setCodecProfile(r9.getInteger("profile"));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.util.MediaExtractorUtils.VideoInfo extractLabor(android.media.MediaExtractor r12) {
        /*
            java.lang.String r0 = "profile"
            java.lang.String r1 = "color-standard"
            java.lang.String r2 = "color-transfer"
            java.lang.String r3 = "height"
            java.lang.String r4 = "width"
            java.lang.String r5 = "frame-rate"
            com.miui.gallery.util.MediaExtractorUtils$VideoInfo r6 = new com.miui.gallery.util.MediaExtractorUtils$VideoInfo
            r6.<init>()
            int r7 = r12.getTrackCount()     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            r8 = 0
        L16:
            if (r8 >= r7) goto L8a
            android.media.MediaFormat r9 = r12.getTrackFormat(r8)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            java.lang.String r10 = "mime"
            java.lang.String r10 = r9.getString(r10)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            boolean r11 = android.text.TextUtils.isEmpty(r10)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            if (r11 != 0) goto L81
            java.lang.String r11 = "video/"
            boolean r10 = r10.startsWith(r11)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            if (r10 == 0) goto L81
            boolean r10 = r9.containsKey(r5)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            if (r10 == 0) goto L81
            boolean r10 = r9.containsKey(r4)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            if (r10 == 0) goto L81
            boolean r10 = r9.containsKey(r3)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            if (r10 == 0) goto L81
            int r5 = r9.getInteger(r5)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            com.miui.gallery.util.MediaExtractorUtils$VideoInfo r5 = r6.setFrameRate(r5)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            int r4 = r9.getInteger(r4)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            com.miui.gallery.util.MediaExtractorUtils$VideoInfo r4 = r5.setWidth(r4)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            int r3 = r9.getInteger(r3)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            r4.setHeight(r3)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            boolean r3 = r9.containsKey(r2)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            if (r3 == 0) goto L66
            int r2 = r9.getInteger(r2)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            r6.setColorTransfer(r2)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
        L66:
            boolean r2 = r9.containsKey(r1)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            if (r2 == 0) goto L73
            int r1 = r9.getInteger(r1)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            r6.setColorStandard(r1)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
        L73:
            boolean r1 = r9.containsKey(r0)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            if (r1 == 0) goto L8a
            int r0 = r9.getInteger(r0)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            r6.setCodecProfile(r0)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L86
            goto L8a
        L81:
            int r8 = r8 + 1
            goto L16
        L84:
            r0 = move-exception
            goto L8e
        L86:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L84
        L8a:
            r12.release()
            return r6
        L8e:
            r12.release()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.MediaExtractorUtils.extractLabor(android.media.MediaExtractor):com.miui.gallery.util.MediaExtractorUtils$VideoInfo");
    }

    /* loaded from: classes2.dex */
    public static final class FilePathInitializer implements MediaExtractorInitializer<String> {
        public FilePathInitializer() {
        }

        @Override // com.miui.gallery.util.MediaExtractorUtils.MediaExtractorInitializer
        public void initialize(MediaExtractor mediaExtractor, String str) throws IOException {
            mediaExtractor.setDataSource(str);
        }
    }

    /* loaded from: classes2.dex */
    public static final class UriInitializer implements MediaExtractorInitializer<Uri> {
        public Context mContext;
        public Map<String, String> mHeaders;

        public UriInitializer(Context context, Map<String, String> map) {
            this.mContext = context;
            this.mHeaders = map;
        }

        @Override // com.miui.gallery.util.MediaExtractorUtils.MediaExtractorInitializer
        public void initialize(MediaExtractor mediaExtractor, Uri uri) throws IOException {
            mediaExtractor.setDataSource(this.mContext, uri, this.mHeaders);
            this.mContext = null;
            this.mHeaders = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoInfo {
        public int mCodecProfile;
        public int mColorStandard;
        public int mColorTransfer;
        public int mFrameRate;
        public int mHeight;
        public int mWidth;

        public VideoInfo setColorTransfer(int i) {
            this.mColorTransfer = i;
            return this;
        }

        public VideoInfo setColorStandard(int i) {
            this.mColorStandard = i;
            return this;
        }

        public int getCodecProfile() {
            return this.mCodecProfile;
        }

        public VideoInfo setCodecProfile(int i) {
            this.mCodecProfile = i;
            return this;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public VideoInfo setWidth(int i) {
            this.mWidth = i;
            return this;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public VideoInfo setHeight(int i) {
            this.mHeight = i;
            return this;
        }

        public int getFrameRate() {
            return this.mFrameRate;
        }

        public VideoInfo setFrameRate(int i) {
            this.mFrameRate = i;
            return this;
        }

        public String toString() {
            return String.format(Locale.US, "frameRate [%d] colorTransfer [%d] width [%d] height [%d]", Integer.valueOf(this.mFrameRate), Integer.valueOf(this.mColorTransfer), Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight));
        }
    }
}
