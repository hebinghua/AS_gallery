package com.miui.gallery.video;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.android.internal.SystemPropertiesCompat;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public class VideoSubtitleProvider {
    public Future mFuture;
    public Listener mListener;
    public volatile Typeface mTypeface;
    public String mVideoPath;
    public final Object mTypefaceLock = new Object();
    public ThreadPool.Job<List<SubtitleItem>> mTask = new ThreadPool.Job() { // from class: com.miui.gallery.video.VideoSubtitleProvider$$ExternalSyntheticLambda0
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public final Object mo1807run(ThreadPool.JobContext jobContext) {
            return VideoSubtitleProvider.$r8$lambda$3aLlgXFwbRn1cemMZTRrdYs8YRg(VideoSubtitleProvider.this, jobContext);
        }
    };
    public FutureListener<List<SubtitleItem>> mFutureListener = new FutureHandler<List<SubtitleItem>>() { // from class: com.miui.gallery.video.VideoSubtitleProvider.1
        {
            VideoSubtitleProvider.this = this;
        }

        @Override // com.miui.gallery.concurrent.FutureHandler
        public void onPostExecute(Future<List<SubtitleItem>> future) {
            List<SubtitleItem> list;
            if (!future.isCancelled() && (list = future.get()) != null) {
                DefaultLogger.d("VideoSubtitleProvider", "read subtitle success.");
                if (VideoSubtitleProvider.this.mListener == null) {
                    return;
                }
                VideoSubtitleProvider.this.mListener.onSubtitleParsed(VideoSubtitleProvider.this.mVideoPath, list, VideoSubtitleProvider.this.mTypeface);
            }
        }
    };
    public Context mContext = GalleryApp.sGetAndroidContext();

    /* loaded from: classes2.dex */
    public interface Listener {
        void onSubtitleParsed(String str, List<SubtitleItem> list, Typeface typeface);
    }

    public static /* synthetic */ List $r8$lambda$3aLlgXFwbRn1cemMZTRrdYs8YRg(VideoSubtitleProvider videoSubtitleProvider, ThreadPool.JobContext jobContext) {
        return videoSubtitleProvider.lambda$new$0(jobContext);
    }

    public VideoSubtitleProvider(Listener listener) {
        this.mListener = listener;
    }

    public void request(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        Future future = this.mFuture;
        if (future != null) {
            future.cancel();
        }
        this.mVideoPath = str;
        this.mFuture = ThreadManager.getMiscPool().submit(this.mTask, this.mFutureListener);
    }

    public /* synthetic */ List lambda$new$0(ThreadPool.JobContext jobContext) {
        Uri subtitleUri;
        Throwable th;
        InputStream inputStream;
        if (jobContext.isCancelled()) {
            return null;
        }
        createTextTypeface();
        Context context = this.mContext;
        String str = this.mVideoPath;
        if (context == null || TextUtils.isEmpty(str) || (subtitleUri = getSubtitleUri(context, str)) == null) {
            return null;
        }
        try {
            inputStream = context.getContentResolver().openInputStream(subtitleUri);
            try {
                try {
                    List<SubtitleItem> parseSrtInputStream = parseSrtInputStream(inputStream);
                    BaseMiscUtil.closeSilently(inputStream);
                    return parseSrtInputStream;
                } catch (Exception e) {
                    e = e;
                    DefaultLogger.w("VideoSubtitleProvider", "read subtitle error.\n", e);
                    BaseMiscUtil.closeSilently(inputStream);
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(inputStream);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            inputStream = null;
        } catch (Throwable th3) {
            th = th3;
            inputStream = null;
            BaseMiscUtil.closeSilently(inputStream);
            throw th;
        }
    }

    public void cancel() {
        Future future = this.mFuture;
        if (future != null) {
            future.cancel();
            this.mFuture = null;
        }
        this.mVideoPath = null;
    }

    public void release() {
        cancel();
        this.mContext = null;
        this.mFutureListener = null;
        this.mTypeface = null;
    }

    public static SubtitleItem findActiveSubtitle(List<SubtitleItem> list, long j) {
        if (!BaseMiscUtil.isValid(list) || j < 0) {
            return null;
        }
        int size = list.size();
        int i = 0;
        if (j < list.get(0).getStartTime()) {
            return null;
        }
        int i2 = size - 1;
        SubtitleItem subtitleItem = list.get(i2);
        if (j >= subtitleItem.getStartTime()) {
            if (j <= subtitleItem.getEndTime()) {
                return subtitleItem;
            }
            return null;
        }
        while (i2 > i + 1) {
            int i3 = (i2 + i) / 2;
            if (list.get(i3).getStartTime() > j) {
                i2 = i3;
            } else {
                i = i3;
            }
        }
        SubtitleItem subtitleItem2 = list.get(i);
        if (j <= subtitleItem2.getEndTime()) {
            return subtitleItem2;
        }
        return null;
    }

    public static Uri getSubtitleUri(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return Uri.parse("content://com.miui.video.SRT/srt").buildUpon().appendPath(str).build();
    }

    public final List<SubtitleItem> parseSrtInputStream(InputStream inputStream) throws IOException {
        String readLine;
        String readLine2;
        if (inputStream == null || inputStream.available() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Keyczar.DEFAULT_ENCODING));
        while (bufferedReader.readLine() != null && (readLine = bufferedReader.readLine()) != null) {
            SubtitleItem subtitleItem = new SubtitleItem();
            String[] split = readLine.split("-->");
            subtitleItem.setStartTime(parseMs(split[0]));
            subtitleItem.setEndTime(parseMs(split[1]));
            subtitleItem.setText(bufferedReader.readLine());
            arrayList.add(subtitleItem);
            do {
                readLine2 = bufferedReader.readLine();
                if (readLine2 != null) {
                }
            } while (!readLine2.trim().equals(""));
        }
        return arrayList;
    }

    public final void createTextTypeface() {
        synchronized (this.mTypefaceLock) {
            if (this.mTypeface == null) {
                try {
                    if (BuildUtil.isMiui10()) {
                        this.mTypeface = Typeface.createFromFile("/system/fonts/Miui-Light.ttf");
                    } else {
                        String str = SystemPropertiesCompat.get("ro.miui.ui.font.mi_font_path", "/system/fonts/MiLanProVF.ttf");
                        if (Build.VERSION.SDK_INT >= 26) {
                            this.mTypeface = new Typeface.Builder(str).setFontVariationSettings("'wght' 305").build();
                        } else {
                            this.mTypeface = Typeface.createFromFile(str);
                        }
                    }
                } catch (Exception e) {
                    DefaultLogger.d("VideoSubtitleProvider", "createTextTypeface occurs error.\n", e);
                }
            }
        }
    }

    public static long parseMs(String str) {
        return (Long.parseLong(str.split(":")[0].trim()) * 60 * 60 * 1000) + (Long.parseLong(str.split(":")[1].trim()) * 60 * 1000) + (Long.parseLong(str.split(":")[2].split(",")[0].trim()) * 1000) + Long.parseLong(str.split(":")[2].split(",")[1].trim());
    }

    /* loaded from: classes2.dex */
    public static class SubtitleItem {
        public long mEndTime;
        public long mStartTime;
        public String mText;

        public long getStartTime() {
            return this.mStartTime;
        }

        public void setStartTime(long j) {
            this.mStartTime = j;
        }

        public long getEndTime() {
            return this.mEndTime;
        }

        public void setEndTime(long j) {
            this.mEndTime = j;
        }

        public String getText() {
            return this.mText;
        }

        public void setText(String str) {
            this.mText = str;
        }
    }
}
