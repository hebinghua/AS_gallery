package com.miui.gallery.cloudcontrol.strategies;

import ch.qos.logback.core.util.FileSize;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.cloudcontrol.Merger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class ScannerStrategy extends BaseStrategy {
    public static final Merger<ScannerStrategy> CLOUD_FIRST_MERGER = new Merger<ScannerStrategy>() { // from class: com.miui.gallery.cloudcontrol.strategies.ScannerStrategy.1
        @Override // com.miui.gallery.cloudcontrol.Merger
        public ScannerStrategy merge(ScannerStrategy scannerStrategy, ScannerStrategy scannerStrategy2) {
            if (scannerStrategy2.mStreamFileStrategy == null && scannerStrategy.mStreamFileStrategy != null) {
                scannerStrategy2.mStreamFileStrategy = StreamFileStrategy.cloneFrom(scannerStrategy.mStreamFileStrategy);
            }
            scannerStrategy2.mFileSizeLimitStrategy = FileSizeLimitStrategy.merge(scannerStrategy.mFileSizeLimitStrategy, scannerStrategy2.mFileSizeLimitStrategy);
            if (scannerStrategy2.mSpecialTypeMediaStrategy == null && scannerStrategy.mSpecialTypeMediaStrategy != null) {
                scannerStrategy2.mSpecialTypeMediaStrategy = SpecialTypeMediaStrategy.cloneFrom(scannerStrategy.mSpecialTypeMediaStrategy);
            }
            return scannerStrategy2;
        }
    };
    @SerializedName("file_size_limit")
    private FileSizeLimitStrategy mFileSizeLimitStrategy;
    @SerializedName("special_type_media")
    private SpecialTypeMediaStrategy mSpecialTypeMediaStrategy;
    @SerializedName("stream_file")
    private StreamFileStrategy mStreamFileStrategy;

    public FileSizeLimitStrategy getFileSizeStrategy() {
        FileSizeLimitStrategy fileSizeLimitStrategy = this.mFileSizeLimitStrategy;
        return fileSizeLimitStrategy != null ? fileSizeLimitStrategy : FileSizeLimitStrategy.createDefault();
    }

    public SpecialTypeMediaStrategy getSpecialTypeMediaStrategy() {
        SpecialTypeMediaStrategy specialTypeMediaStrategy = this.mSpecialTypeMediaStrategy;
        return specialTypeMediaStrategy != null ? specialTypeMediaStrategy : SpecialTypeMediaStrategy.createDefault();
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        super.doAdditionalProcessing();
        StreamFileStrategy streamFileStrategy = this.mStreamFileStrategy;
        if (streamFileStrategy != null) {
            streamFileStrategy.doAdditionalProcessing();
        }
        FileSizeLimitStrategy fileSizeLimitStrategy = this.mFileSizeLimitStrategy;
        if (fileSizeLimitStrategy != null) {
            fileSizeLimitStrategy.doAdditionalProcessing();
        }
    }

    public static ScannerStrategy createDefault() {
        ScannerStrategy scannerStrategy = new ScannerStrategy();
        scannerStrategy.mStreamFileStrategy = StreamFileStrategy.createDefault();
        scannerStrategy.mFileSizeLimitStrategy = FileSizeLimitStrategy.createDefault();
        scannerStrategy.mSpecialTypeMediaStrategy = SpecialTypeMediaStrategy.createDefault();
        return scannerStrategy;
    }

    /* loaded from: classes.dex */
    public static class StreamFileStrategy {
        @SerializedName("delay_milliseconds")
        private long mDelayMilliseconds;
        public transient HashMap<String, StreamFolder> mFoldersMap;
        @SerializedName("folders")
        private List<StreamFolder> mStreamFolders;

        public static StreamFileStrategy createDefault() {
            StreamFileStrategy streamFileStrategy = new StreamFileStrategy();
            streamFileStrategy.mDelayMilliseconds = 2000L;
            ArrayList arrayList = new ArrayList();
            streamFileStrategy.mStreamFolders = arrayList;
            arrayList.add(new StreamFolder("bluetooth"));
            streamFileStrategy.doAdditionalProcessing();
            return streamFileStrategy;
        }

        public final void doAdditionalProcessing() {
            if (this.mStreamFolders != null) {
                HashMap<String, StreamFolder> hashMap = this.mFoldersMap;
                if (hashMap == null) {
                    this.mFoldersMap = new HashMap<>();
                } else {
                    hashMap.clear();
                }
                for (StreamFolder streamFolder : this.mStreamFolders) {
                    this.mFoldersMap.put(streamFolder.getPath(), streamFolder);
                }
            }
        }

        public static StreamFileStrategy cloneFrom(StreamFileStrategy streamFileStrategy) {
            StreamFileStrategy streamFileStrategy2 = new StreamFileStrategy();
            streamFileStrategy2.mDelayMilliseconds = streamFileStrategy.mDelayMilliseconds;
            streamFileStrategy2.mStreamFolders = Lists.newArrayList(streamFileStrategy.mStreamFolders);
            streamFileStrategy2.mFoldersMap = Maps.newHashMap(streamFileStrategy.mFoldersMap);
            return streamFileStrategy2;
        }
    }

    /* loaded from: classes.dex */
    public static class StreamFolder {
        @SerializedName(nexExportFormat.TAG_FORMAT_PATH)
        private String mPath;

        public StreamFolder(String str) {
            this.mPath = str;
        }

        public String getPath() {
            return this.mPath;
        }
    }

    /* loaded from: classes.dex */
    public static class FileSizeLimitStrategy {
        @SerializedName("image_max_size")
        private long mImageMaxSize;
        @SerializedName("video_max_size")
        private long mVideoMaxSize;

        public static FileSizeLimitStrategy createDefault() {
            FileSizeLimitStrategy fileSizeLimitStrategy = new FileSizeLimitStrategy();
            fileSizeLimitStrategy.mImageMaxSize = 100L;
            fileSizeLimitStrategy.mVideoMaxSize = 4096L;
            fileSizeLimitStrategy.doAdditionalProcessing();
            return fileSizeLimitStrategy;
        }

        public long getImageMaxSize() {
            return this.mImageMaxSize;
        }

        public long getVideoMaxSize() {
            return this.mVideoMaxSize;
        }

        public void doAdditionalProcessing() {
            this.mImageMaxSize *= FileSize.MB_COEFFICIENT;
            this.mVideoMaxSize *= FileSize.MB_COEFFICIENT;
        }

        public String toString() {
            return "FileSizeLimitStrategy{mImageMaxSize=" + this.mImageMaxSize + ", mVideoMaxSize=" + this.mVideoMaxSize + '}';
        }

        public static FileSizeLimitStrategy merge(FileSizeLimitStrategy fileSizeLimitStrategy, FileSizeLimitStrategy fileSizeLimitStrategy2) {
            if (fileSizeLimitStrategy == null) {
                return fileSizeLimitStrategy2;
            }
            if (fileSizeLimitStrategy2 == null) {
                return fileSizeLimitStrategy;
            }
            long j = fileSizeLimitStrategy.mImageMaxSize;
            if (j > fileSizeLimitStrategy2.mImageMaxSize) {
                fileSizeLimitStrategy2.mImageMaxSize = j;
            }
            long j2 = fileSizeLimitStrategy.mVideoMaxSize;
            if (j2 > fileSizeLimitStrategy2.mVideoMaxSize) {
                fileSizeLimitStrategy2.mVideoMaxSize = j2;
            }
            return fileSizeLimitStrategy2;
        }
    }

    /* loaded from: classes.dex */
    public static class SpecialTypeMediaStrategy {
        @SerializedName("hfr_120fps_lower_bound")
        private long mHfr120FpsLowerBound;
        @SerializedName("hfr_120fps_upper_bound")
        private long mHfr120FpsUpperBound;
        @SerializedName("hfr_1920fps_lower_bound")
        private long mHfr1920FpsLowerBound;
        @SerializedName("hfr_1920fps_upper_bound")
        private long mHfr1920FpsUpperBound;
        @SerializedName("hfr_240fps_lower_bound")
        private long mHfr240FpsLowerBound;
        @SerializedName("hfr_240fps_upper_bound")
        private long mHfr240FpsUpperBound;
        @SerializedName("hfr_3840fps_lower_bound")
        private long mHfr3840FpsLowerBound;
        @SerializedName("hfr_3840fps_upper_bound")
        private long mHfr3840FpsUpperBound;
        @SerializedName("hfr_480fps_lower_bound")
        private long mHfr480FpsLowerBound;
        @SerializedName("hfr_480fps_upper_bound")
        private long mHfr480FpsUpperBound;
        @SerializedName("hfr_960fps_lower_bound")
        private long mHfr960FpsLowerBound;
        @SerializedName("hfr_960fps_upper_bound")
        private long mHfr960FpsUpperBound;

        public static SpecialTypeMediaStrategy createDefault() {
            SpecialTypeMediaStrategy specialTypeMediaStrategy = new SpecialTypeMediaStrategy();
            specialTypeMediaStrategy.mHfr120FpsLowerBound = 100L;
            specialTypeMediaStrategy.mHfr120FpsUpperBound = 140L;
            specialTypeMediaStrategy.mHfr240FpsLowerBound = 160L;
            specialTypeMediaStrategy.mHfr240FpsUpperBound = 260L;
            specialTypeMediaStrategy.mHfr480FpsLowerBound = 400L;
            specialTypeMediaStrategy.mHfr480FpsUpperBound = 500L;
            specialTypeMediaStrategy.mHfr960FpsLowerBound = 900L;
            specialTypeMediaStrategy.mHfr960FpsUpperBound = 980L;
            specialTypeMediaStrategy.mHfr1920FpsLowerBound = 1800L;
            specialTypeMediaStrategy.mHfr1920FpsUpperBound = 1940L;
            specialTypeMediaStrategy.mHfr3840FpsLowerBound = 3600L;
            specialTypeMediaStrategy.mHfr3840FpsUpperBound = 4000L;
            return specialTypeMediaStrategy;
        }

        public static SpecialTypeMediaStrategy cloneFrom(SpecialTypeMediaStrategy specialTypeMediaStrategy) {
            SpecialTypeMediaStrategy specialTypeMediaStrategy2 = new SpecialTypeMediaStrategy();
            specialTypeMediaStrategy2.mHfr120FpsLowerBound = specialTypeMediaStrategy.mHfr120FpsLowerBound;
            specialTypeMediaStrategy2.mHfr120FpsUpperBound = specialTypeMediaStrategy.mHfr120FpsUpperBound;
            specialTypeMediaStrategy2.mHfr240FpsLowerBound = specialTypeMediaStrategy.mHfr240FpsLowerBound;
            specialTypeMediaStrategy2.mHfr240FpsUpperBound = specialTypeMediaStrategy.mHfr240FpsUpperBound;
            specialTypeMediaStrategy2.mHfr480FpsLowerBound = specialTypeMediaStrategy.mHfr480FpsLowerBound;
            specialTypeMediaStrategy2.mHfr480FpsUpperBound = specialTypeMediaStrategy.mHfr480FpsUpperBound;
            specialTypeMediaStrategy2.mHfr960FpsLowerBound = specialTypeMediaStrategy.mHfr960FpsLowerBound;
            specialTypeMediaStrategy2.mHfr960FpsUpperBound = specialTypeMediaStrategy.mHfr960FpsUpperBound;
            specialTypeMediaStrategy2.mHfr1920FpsLowerBound = specialTypeMediaStrategy.mHfr1920FpsLowerBound;
            specialTypeMediaStrategy2.mHfr1920FpsUpperBound = specialTypeMediaStrategy.mHfr1920FpsUpperBound;
            specialTypeMediaStrategy2.mHfr3840FpsLowerBound = specialTypeMediaStrategy.mHfr3840FpsLowerBound;
            specialTypeMediaStrategy2.mHfr3840FpsUpperBound = specialTypeMediaStrategy.mHfr3840FpsUpperBound;
            return specialTypeMediaStrategy2;
        }

        public long getHfr120FpsLowerBound() {
            return this.mHfr120FpsLowerBound;
        }

        public long getHfr120FpsUpperBound() {
            return this.mHfr120FpsUpperBound;
        }

        public long getHfr240FpsLowerBound() {
            return this.mHfr240FpsLowerBound;
        }

        public long getHfr240FpsUpperBound() {
            return this.mHfr240FpsUpperBound;
        }

        public long getHfr480FpsLowerBound() {
            return this.mHfr480FpsLowerBound;
        }

        public long getHfr480FpsUpperBound() {
            return this.mHfr480FpsUpperBound;
        }

        public long getHfr960FpsLowerBound() {
            return this.mHfr960FpsLowerBound;
        }

        public long getHfr960FpsUpperBound() {
            return this.mHfr960FpsUpperBound;
        }

        public long getHfr1920FpsLowerBound() {
            return this.mHfr1920FpsLowerBound;
        }

        public long getHfr1920FpsUpperBound() {
            return this.mHfr1920FpsUpperBound;
        }

        public long getHfr3840FpsLowerBound() {
            return this.mHfr3840FpsLowerBound;
        }

        public long getHfr3840FpsUpperBound() {
            return this.mHfr3840FpsUpperBound;
        }

        public String toString() {
            return "SpecialTypeMediaStrategy{mHfr120FpsLowerBound=" + this.mHfr120FpsLowerBound + ", mHfr120FpsUpperBound=" + this.mHfr120FpsUpperBound + ", mHfr240FpsLowerBound=" + this.mHfr240FpsLowerBound + ", mHfr240FpsUpperBound=" + this.mHfr240FpsUpperBound + ", mHfr480FpsLowerBound=" + this.mHfr480FpsLowerBound + ", mHfr480FpsUpperBound=" + this.mHfr480FpsUpperBound + ", mHfr960FpsLowerBound=" + this.mHfr960FpsLowerBound + ", mHfr960FpsUpperBound=" + this.mHfr960FpsUpperBound + ", mHfr1920FpsLowerBound=" + this.mHfr1920FpsLowerBound + ", mHfr1920FpsUpperBound=" + this.mHfr1920FpsUpperBound + ", mHfr3840FpsLowerBound=" + this.mHfr3840FpsLowerBound + ", mHfr3840FpsUpperBound=" + this.mHfr3840FpsUpperBound + '}';
        }
    }
}
