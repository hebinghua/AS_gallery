package com.miui.gallery.biz.story.data;

import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.picker.uri.Downloader;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DownloadStatus.kt */
/* loaded from: classes.dex */
public abstract class DownloadStatus {
    public /* synthetic */ DownloadStatus(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    /* compiled from: DownloadStatus.kt */
    /* loaded from: classes.dex */
    public static final class STARTED extends DownloadStatus {
        public final List<Downloader.DownloadTask> tasks;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof STARTED) && Intrinsics.areEqual(this.tasks, ((STARTED) obj).tasks);
        }

        public int hashCode() {
            return this.tasks.hashCode();
        }

        public String toString() {
            return "STARTED(tasks=" + this.tasks + CoreConstants.RIGHT_PARENTHESIS_CHAR;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public STARTED(List<? extends Downloader.DownloadTask> tasks) {
            super(null);
            Intrinsics.checkNotNullParameter(tasks, "tasks");
            this.tasks = tasks;
        }

        public final List<Downloader.DownloadTask> getTasks() {
            return this.tasks;
        }
    }

    public DownloadStatus() {
    }

    /* compiled from: DownloadStatus.kt */
    /* loaded from: classes.dex */
    public static final class PROGRESS extends DownloadStatus {
        public final float progress;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof PROGRESS) && Intrinsics.areEqual(Float.valueOf(this.progress), Float.valueOf(((PROGRESS) obj).progress));
        }

        public int hashCode() {
            return Float.hashCode(this.progress);
        }

        public String toString() {
            return "PROGRESS(progress=" + this.progress + CoreConstants.RIGHT_PARENTHESIS_CHAR;
        }

        public PROGRESS(float f) {
            super(null);
            this.progress = f;
        }

        public final float getProgress() {
            return this.progress;
        }
    }

    /* compiled from: DownloadStatus.kt */
    /* loaded from: classes.dex */
    public static final class CANCELLED extends DownloadStatus {
        public final List<Downloader.DownloadResult> fails;
        public final List<Downloader.DownloadResult> success;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CANCELLED)) {
                return false;
            }
            CANCELLED cancelled = (CANCELLED) obj;
            return Intrinsics.areEqual(this.success, cancelled.success) && Intrinsics.areEqual(this.fails, cancelled.fails);
        }

        public int hashCode() {
            return (this.success.hashCode() * 31) + this.fails.hashCode();
        }

        public String toString() {
            return "CANCELLED(success=" + this.success + ", fails=" + this.fails + CoreConstants.RIGHT_PARENTHESIS_CHAR;
        }

        public final List<Downloader.DownloadResult> getSuccess() {
            return this.success;
        }

        public final List<Downloader.DownloadResult> getFails() {
            return this.fails;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public CANCELLED(List<? extends Downloader.DownloadResult> success, List<? extends Downloader.DownloadResult> fails) {
            super(null);
            Intrinsics.checkNotNullParameter(success, "success");
            Intrinsics.checkNotNullParameter(fails, "fails");
            this.success = success;
            this.fails = fails;
        }
    }

    /* compiled from: DownloadStatus.kt */
    /* loaded from: classes.dex */
    public static final class COMPLETED extends DownloadStatus {
        public final List<Downloader.DownloadResult> fails;
        public final List<Downloader.DownloadResult> success;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof COMPLETED)) {
                return false;
            }
            COMPLETED completed = (COMPLETED) obj;
            return Intrinsics.areEqual(this.success, completed.success) && Intrinsics.areEqual(this.fails, completed.fails);
        }

        public int hashCode() {
            return (this.success.hashCode() * 31) + this.fails.hashCode();
        }

        public String toString() {
            return "COMPLETED(success=" + this.success + ", fails=" + this.fails + CoreConstants.RIGHT_PARENTHESIS_CHAR;
        }

        public final List<Downloader.DownloadResult> getSuccess() {
            return this.success;
        }

        public final List<Downloader.DownloadResult> getFails() {
            return this.fails;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public COMPLETED(List<? extends Downloader.DownloadResult> success, List<? extends Downloader.DownloadResult> fails) {
            super(null);
            Intrinsics.checkNotNullParameter(success, "success");
            Intrinsics.checkNotNullParameter(fails, "fails");
            this.success = success;
            this.fails = fails;
        }
    }
}
